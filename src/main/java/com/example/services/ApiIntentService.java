package com.example.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.example.dagger.Injector;
import com.example.providers.ExampleContentProvider;
import com.example.tables.TaskTable;
import com.example.tasks.Task;

public class ApiIntentService extends IntentService implements Injector {
    private static final String SERVICE_NAME = "com.example.services.ApiService";

    public static void startService(final Context context, final Task task) {
        final Intent intent = new Intent(context, ApiIntentService.class);
        intent.putExtra(Extras.TASK, task);

        context.startService(intent);
    }

    public ApiIntentService() {
        super(SERVICE_NAME);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        final Task<Object> task = intent.getParcelableExtra(Extras.TASK);
        task.setContext(this);
        if (!task.getSyncStrategy().checkSyncNeeded()) {
            return;
        }
        try {
            final Object response = task.executeNetworking();
            task.processResponse(response);
        } catch (final Exception exc) {
            task.notifyFailure();
            return;
        }
        updateTaskTable(task);
        task.notifySuccess();
    }

    private void updateTaskTable(final Task task) {
        getContentResolver().insert(ExampleContentProvider.DatabaseUris.TASK_TABLE_URI, TaskTable.toContentValues(task));
    }

    @Override
    public void inject(final Object object) {
        ((Injector) getApplication()).inject(object);
    }

    public interface Extras {
        String TASK = "com.example.service.ApiService.TASK";
    }
}
