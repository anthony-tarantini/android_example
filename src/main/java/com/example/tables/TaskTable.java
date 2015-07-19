package com.example.tables;

import android.content.ContentValues;

import com.example.providers.DatabaseTable;
import com.example.tasks.Task;

import org.joda.time.LocalDateTime;

import java.util.Map;

public class TaskTable extends DatabaseTable {
    public static final String TABLE_NAME = "task";

    public interface Columns extends DatabaseTable.Columns{
        String IDENTIFIER = "identifier";
        String LAST_SYNC_TIME = "last_sync_time";
    }

    @Override
    protected Map<String, String> getColumnTypes() {
        Map<String, String> columnTypes = super.getColumnTypes();
        columnTypes.put(Columns.IDENTIFIER, "TEXT");
        columnTypes.put(Columns.LAST_SYNC_TIME, "TEXT");
        return columnTypes;
    }

    public static ContentValues toContentValues(final Task task){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Columns.IDENTIFIER, task.getIdentifier());
        contentValues.put(Columns.LAST_SYNC_TIME, LocalDateTime.now().toString());
        return contentValues;
    }

    @Override
    public String getName() {
        return TABLE_NAME;
    }

    @Override
    protected String getConstraint() {
        return "UNIQUE (" + Columns.IDENTIFIER + ") ON CONFLICT REPLACE";
    }
}
