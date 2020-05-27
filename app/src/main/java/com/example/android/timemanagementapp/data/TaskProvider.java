package com.example.android.timemanagementapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.android.timemanagementapp.data.TaskContract.ListEntry;


public class TaskProvider extends ContentProvider {

    private static final int LISTS = 100;
    private static final int LIST_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final String LOG_TAG = TaskProvider.class.getSimpleName();

    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(TaskContract.CONTENT_AUTHORITY, TaskContract.ListEntry.TABLE_NAME, LISTS);
        matcher.addURI(TaskContract.CONTENT_AUTHORITY, TaskContract.ListEntry.TABLE_NAME + "/#", LIST_ID);

        return matcher;
    }

    private TaskDbHelper mDbHelper;


    @Override
    public boolean onCreate() {
        mDbHelper = new TaskDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor;

        switch(sUriMatcher.match(uri)) {
            case LISTS:
                // for the LISTS code, query the lists table directly with the given
                // projection, selection, selection arguments, and sort order
                cursor = db.query(
                        ListEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case LIST_ID:
                // for the LIST_ID code, extract out the ID from the URI,
                // the selection will be "_id=?",
                // the selection argument will be a String array containing the actual ID
                long id = ContentUris.parseId(uri);
                selection = ListEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(id)};
                cursor = db.query(
                        ListEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        // TODO: implement later
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (sUriMatcher.match(uri)) {
            case LISTS:
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                long id = db.insert(
                        ListEntry.TABLE_NAME,
                        null,
                        values
                );
                if (id == -1) {
                    Log.e(LOG_TAG, "Failed to insert row for " + uri);
                    return null;
                }
                // Notify all listeners that the data has changed for the list content URI
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsDeleted;

        switch(sUriMatcher.match(uri)) {
            case LISTS:
                rowsDeleted = db.delete(ListEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case LIST_ID:
                long id = ContentUris.parseId(uri);
                selection = ListEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(id) };
                rowsDeleted = db.delete(
                        ListEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsUpdated = 0;

        switch (sUriMatcher.match(uri)) {
            case LISTS:
                rowsUpdated = db.update(
                        ListEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );
                break;

            case LIST_ID:
                long id = ContentUris.parseId(uri);
                selection = ListEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(id) };
                rowsUpdated = db.update(
                        ListEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );
                break;
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
