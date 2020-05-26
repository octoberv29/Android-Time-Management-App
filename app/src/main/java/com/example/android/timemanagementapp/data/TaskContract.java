package com.example.android.timemanagementapp.data;

import android.provider.BaseColumns;

public class TaskContract {

    // To prevent someone from accidentally instantiating the contract class.
    private TaskContract() { }


    // Table for Lists
    public static final class ListEntry implements BaseColumns {

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
