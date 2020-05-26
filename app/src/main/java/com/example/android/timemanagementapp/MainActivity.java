package com.example.android.timemanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.android.timemanagementapp.data.TaskDbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.android.timemanagementapp.data.TaskContract.ListEntry;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

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
                mAdapter.swapCursor(getAllLists());
            }
        });

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        // setup the Adapter
        Cursor cursor = getAllLists();
        mAdapter = new ListAdapter(this, cursor);
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

                mAdapter.swapCursor(getAllLists());
            }
        }).attachToRecyclerView(mRecyclerView);

        // add divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    private Cursor getAllLists() {
        // query db
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {ListEntry._ID, ListEntry.COLUMN_LIST_TITLE};
        Cursor cursor = db.query(
                ListEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        return cursor;
    }

    private long addNewList() {
        // Add a random item to the db
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Random random = new Random();

        ContentValues cv = new ContentValues();
        cv.put(ListEntry.COLUMN_LIST_TITLE, String.valueOf(random.nextInt()));

        return db.insert(ListEntry.TABLE_NAME, null, cv);
    }

    private int deleteList(long id) {
        // Delete an existing list from the db
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        return db.delete(
                ListEntry.TABLE_NAME,
                ListEntry._ID + "=" + id,
                null
        );
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
}
