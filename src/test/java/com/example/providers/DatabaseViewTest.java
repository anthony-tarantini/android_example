package com.example.providers;

import android.database.sqlite.SQLiteDatabase;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DatabaseViewTest {

    @Test
    public void onCreate() {
        final SQLiteDatabase mockDatabase = mock(SQLiteDatabase.class);

        final DatabaseView databaseView = new DatabaseView() {
            @Override
            protected String getSelectString() {
                return "SELECT * FROM test_table WHERE test_column = 125";
            }

            @Override
            public String getName() {
                return "TEST_VIEW";
            }
        };

        databaseView.onCreate(mockDatabase);

        verify(mockDatabase).execSQL("CREATE VIEW IF NOT EXISTS TEST_VIEW AS SELECT * FROM ( SELECT * FROM test_table WHERE test_column = 125 );");
    }
}