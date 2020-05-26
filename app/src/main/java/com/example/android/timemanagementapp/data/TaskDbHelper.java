package com.example.android.timemanagementapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import com.example.android.timemanagementapp.data.TaskContract.ListEntry;

public class TaskDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_LIST = "CREATE TABLE " + ListEntry.TABLE_NAME + " (" +
            ListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            ListEntry.COLUMN_LIST_TITLE + " TEXT NOT NULL, " +
            ListEntry.COLUMN_LIST_CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP); ";

    private static final String DROP_TABLE_LIST = "DROP TABLE IF EXISTS " + ListEntry.TABLE_NAME;

    public TaskDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_LIST);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop table and create new
        db.execSQL(DROP_TABLE_LIST);
        onCreate(db);
    }
}
