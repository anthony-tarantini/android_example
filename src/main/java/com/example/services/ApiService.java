package com.example.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.example.tasks.Task;

public class ApiService extends IntentService {
    private static final String SERVICE_NAME = "com.example.services.ApiService";

    public static void startService(final Context context, final Task task) {
        final Intent intent = new Intent(context, ApiService.class);
        intent.putExtra(Extras.TASK, task);

        context.startService(intent);
    }

    public ApiService() {
        super(SERVICE_NAME);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        final Task<Object> task = intent.getParcelableExtra(Extras.TASK);
        task.setContext(this);
        try {
            final Object response = task.executeNetworking();
            task.processResponse(response);
        } catch (final Exception exc) {
            task.notifyFailure();

        }
        task.notifySuccess();
    }

    public interface Extras {
        String TASK = "com.example.service.ApiService.TASK";
    }
}
