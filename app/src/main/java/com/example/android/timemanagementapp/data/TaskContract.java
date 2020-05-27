package com.example.android.timemanagementapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class TaskContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.timemanagementapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_LISTS = "lists";
    public static final String PATH_TASKS = "tasks";

    // To prevent someone from accidentally instantiating the contract class.
    private TaskContract() { }

    // Table for Lists
    public static final class ListEntry implements BaseColumns {

        // content://content_authority/table_name
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_LISTS);

        public static final String TABLE_NAME = "lists";
        public static final String COLUMN_LIST_TITLE = "listTitle";
        public static final String COLUMN_LIST_CREATED_AT = "listCreatedAt";
    }

    // Table for Tasks
    public static final class TaskEntry implements BaseColumns {

        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_TASK_TITLE = "taskTitle";
        public static final String COLUMN_TASK_PRIORITY = "taskPriority";
        public static final String COLUMN_TASK_CREATED_AT = "listCreatedAt";
        public static final String COLUMN_TASK_NOTE = "listNote";
    }
}
