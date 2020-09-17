package com.example.android.timemanagementapp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.timemanagementapp.R;
import com.example.android.timemanagementapp.models.Task;
import com.example.android.timemanagementapp.persistence.TaskRepository;
import com.example.android.timemanagementapp.utils.LinedEditText;
import com.example.android.timemanagementapp.utils.Utility;

public class TaskActivity extends AppCompatActivity implements
        View.OnTouchListener,
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener,
        View.OnClickListener,
        TextWatcher {

    private static final String TAG = "NoteActivity";
    private static final int EDIT_MODE_ENABLED = 1;
    private static final int EDIT_MODE_DISABLED = 0;

    // UI components
    private LinedEditText mLinedEditText;
    private EditText mEditTitle;
    private TextView mViewTitle;
    private RelativeLayout mCheckContainer, mBackArrowContainer;
    private ImageButton mCheck, mBackArrow;


    // vars
    private boolean mIsNewTask;
    private Task mTaskInitial, mTaskFinal;
    private GestureDetector mGestureDetector;
    private int mMode;
    private TaskRepository mTaskRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        mLinedEditText = findViewById(R.id.note_text);
        mEditTitle = findViewById(R.id.note_edit_title);
        mViewTitle = findViewById(R.id.note_text_title);
        mCheck = findViewById(R.id.toolbar_check);
        mBackArrow = findViewById(R.id.toolbar_back_arrow);
        mCheckContainer = findViewById(R.id.check_container);
        mBackArrowContainer = findViewById(R.id.back_arrow_container);

        mTaskRepository = new TaskRepository(this);

        setListeners();

        if(getIncomingIntent()){
            setNewNoteProperties();
            enableEditMode();
        }
        else{
            setNoteProperties();
            disableContentInteraction();
        }
    }

    private void saveChanges(){
        if(mIsNewTask){
            saveNewNote();
        }else{
            updateNote();
        }
    }

    public void updateNote() {
        mTaskRepository.updateTask(mTaskFinal);
    }

    public void saveNewNote() {
        mTaskRepository.insertTask(mTaskFinal);
    }

    private void setListeners(){
        mGestureDetector = new GestureDetector(this, this);
        mLinedEditText.setOnTouchListener(this);
        mCheck.setOnClickListener(this);
        mViewTitle.setOnClickListener(this);
        mBackArrow.setOnClickListener(this);
        mEditTitle.addTextChangedListener(this);
    }

    private boolean getIncomingIntent(){
        if(getIntent().hasExtra("task")){
            mTaskInitial = getIntent().getParcelableExtra("task");

            mTaskFinal = new Task();
            mTaskFinal.setTitle(mTaskInitial.getTitle());
            mTaskFinal.setContent(mTaskInitial.getContent());
            mTaskFinal.setTimestamp(mTaskInitial.getTimestamp());
            mTaskFinal.setId(mTaskInitial.getId());

            mMode = EDIT_MODE_ENABLED;
            mIsNewTask = false;
            return false;
        }
        mMode = EDIT_MODE_ENABLED;
        mIsNewTask = true;
        return true;
    }

    private void disableContentInteraction(){
        mLinedEditText.setKeyListener(null);
        mLinedEditText.setFocusable(false);
        mLinedEditText.setFocusableInTouchMode(false);
        mLinedEditText.setCursorVisible(false);
        mLinedEditText.clearFocus();
    }

    private void enableContentInteraction(){
        mLinedEditText.setKeyListener(new EditText(this).getKeyListener());
        mLinedEditText.setFocusable(true);
        mLinedEditText.setFocusableInTouchMode(true);
        mLinedEditText.setCursorVisible(true);
        mLinedEditText.requestFocus();
    }

    private void enableEditMode(){
        mBackArrowContainer.setVisibility(View.GONE);
        mCheckContainer.setVisibility(View.VISIBLE);

        mViewTitle.setVisibility(View.GONE);
        mEditTitle.setVisibility(View.VISIBLE);

        mMode = EDIT_MODE_ENABLED;

        enableContentInteraction();
    }

    private void disableEditMode(){
        Log.d(TAG, "disableEditMode: called.");
        mBackArrowContainer.setVisibility(View.VISIBLE);
        mCheckContainer.setVisibility(View.GONE);

        mViewTitle.setVisibility(View.VISIBLE);
        mEditTitle.setVisibility(View.GONE);

        mMode = EDIT_MODE_DISABLED;

        disableContentInteraction();

        // Check if they typed anything into the note. Don't want to save an empty note.
        String temp = mLinedEditText.getText().toString();
        temp = temp.replace("\n", "");
        temp = temp.replace(" ", "");
        if(temp.length() > 0){
            mTaskFinal.setTitle(mEditTitle.getText().toString());
            mTaskFinal.setContent(mLinedEditText.getText().toString());
            String timestamp = Utility.getCurrentTimeStamp();
            mTaskFinal.setTimestamp(timestamp);

            Log.d(TAG, "disableEditMode: initial: " + mTaskInitial.toString());
            Log.d(TAG, "disableEditMode: final: " + mTaskFinal.toString());

            // If the note was altered, save it.
            if(!mTaskFinal.getContent().equals(mTaskInitial.getContent())
                    || !mTaskFinal.getTitle().equals(mTaskInitial.getTitle())){
                Log.d(TAG, "disableEditMode: called?");
                saveChanges();
            }
        }
    }

    private void setNewNoteProperties(){
        mViewTitle.setText("Task Title");
        mEditTitle.setText("Task Title");

        mTaskFinal = new Task();
        mTaskInitial = new Task();
        mTaskInitial.setTitle("Task Title");
    }

    private void setNoteProperties(){
        mViewTitle.setText(mTaskInitial.getTitle());
        mEditTitle.setText(mTaskInitial.getTitle());
        mLinedEditText.setText(mTaskInitial.getContent());
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return mGestureDetector.onTouchEvent(motionEvent);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        Log.d(TAG, "onDoubleTap: double tapped.");
        enableEditMode();
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.toolbar_back_arrow:{
                finish();
                break;
            }
            case R.id.toolbar_check:{
                disableEditMode();
                break;
            }
            case R.id.note_text_title:{
                enableEditMode();
                mEditTitle.requestFocus();
                mEditTitle.setSelection(mEditTitle.length());
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(mMode == EDIT_MODE_ENABLED){
            onClick(mCheck);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("mode", mMode);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMode = savedInstanceState.getInt("mode");
        if(mMode == EDIT_MODE_ENABLED){
            enableEditMode();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        mViewTitle.setText(charSequence.toString());
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}