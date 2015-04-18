package com.example.providers;

import android.database.sqlite.SQLiteDatabase;

import org.junit.Test;

import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DatabaseTableTest {

    @Test
    public void onCreate() {
        final SQLiteDatabase mockDatabase = mock(SQLiteDatabase.class);

        final DatabaseTable databaseTable = new DatabaseTable() {
            @Override
            public String getName() {
                return "TEST_TABLE";
            }

            @Override
            protected Map<String, String> getColumnTypes() {
                final Map<String, String> columnTypes = super.getColumnTypes();
                columnTypes.put("test_column", "test_type");
                return columnTypes;
            }

            @Override
            protected String getConstraint() {
                return "UNIQUE (test_column) ON CONFLICT REPLACE";
            }
        };

        databaseTable.onCreate(mockDatabase);

        verify(mockDatabase).execSQL("CREATE TABLE IF NOT EXISTS TEST_TABLE (_id INTEGER PRIMARY KEY AUTOINCREMENT, test_column test_type, UNIQUE (test_column) ON CONFLICT REPLACE);");
    }
}