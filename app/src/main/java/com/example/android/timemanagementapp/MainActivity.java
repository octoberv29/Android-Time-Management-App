package com.example.android.timemanagementapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.timemanagementapp.data.TaskDbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.android.timemanagementapp.data.TaskContract.ListEntry;

import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LIST_LOADER = 1;
    private FloatingActionButton mFab;
    private RecyclerView mRecyclerView;
    private ListAdapter mAdapter;

    private TaskDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbHelper = new TaskDbHelper(this);

        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewList();
//                mAdapter.swapCursor(getAllLists());
            }
        });

        setupRecyclerView();
        getSupportLoaderManager().initLoader(LIST_LOADER, null, this);
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        // setup the Adapter
//        Cursor cursor = getAllLists();
        mAdapter = new ListAdapter(this, null);
        mRecyclerView.setAdapter(mAdapter);

        // add left and right swipes for delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                //do nothing, we only care about swiping
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                long id = (long) viewHolder.itemView.getTag();
                deleteList(id);
//                mAdapter.swapCursor(getAllLists());
            }
        }).attachToRecyclerView(mRecyclerView);

        // add divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    // query db
    private Cursor getAllLists() {

        String[] projection = {
                ListEntry._ID,
                ListEntry.COLUMN_LIST_TITLE
        };

        Cursor cursor = getContentResolver().query(
                ListEntry.CONTENT_URI,
                projection,
                null,
                null,
                null
        );
        return cursor;
    }

    // Add a random item to the db
    private void addNewList() {
        ContentValues cv = new ContentValues();
        cv.put(ListEntry.COLUMN_LIST_TITLE, String.valueOf(new Random().nextInt()));

        Uri newUri = getContentResolver().insert(
                ListEntry.CONTENT_URI,
                cv
        );

    }

    // Delete an existing list from the db
    private void deleteList(long id) {
        Uri uri = ContentUris.withAppendedId(ListEntry.CONTENT_URI, id);
        String selection = ListEntry._ID + "=" + id;

        int rowsDeleted = getContentResolver().delete(
                uri,
                selection,
                null
        );
        if (rowsDeleted == 0) {
            Toast.makeText(this, "Deletion failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Deletion was successful", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main ,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_refresh:
                Toast.makeText(this, "refreshed", Toast.LENGTH_SHORT).show();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                ListEntry._ID,
                ListEntry.COLUMN_LIST_TITLE
        };

        return new CursorLoader(
                this,
                ListEntry.CONTENT_URI,
                projection,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
