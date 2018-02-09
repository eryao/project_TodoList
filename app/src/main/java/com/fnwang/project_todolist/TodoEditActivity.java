package com.fnwang.project_todolist;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.fnwang.project_todolist.models.TodoItem;
import com.fnwang.project_todolist.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;

public class TodoEditActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener{
    public static final String KEY_TODO = "todo";
    public static final String KEY_TODO_ID = "todo_id";

    private EditText editText;
    private TextView dateTv;
    private TextView timeTv;
    private CheckBox complete_btn;

    private TodoItem todo;
    private Date remindDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        todo = getIntent().getParcelableExtra(KEY_TODO);
        remindDate = todo == null ? null : todo.remindDate;
        //setContentView(R.layout.activity_todo_edit);
        setupUI();
    }

    private void setupUI() {
        setContentView(R.layout.activity_todo_edit);
        setupActionbar();
        editText = (EditText)findViewById(R.id.edit_todo_detail);
        dateTv = (TextView)findViewById(R.id.edit_todo_date);
        timeTv = (TextView)findViewById(R.id.edit_todo_time);
        complete_btn = (CheckBox)findViewById(R.id.edit_todo_complete_checkBox);

        if(todo != null){
            editText.setText(todo.text);
            setTextViewStrikeThrough(editText,todo.done);
            complete_btn.setChecked(todo.done);

            findViewById(R.id.edit_todo_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delete();
                }
            });
        }
        else{
            findViewById(R.id.edit_todo_delete).setVisibility(View.GONE);
        }
        if(remindDate != null){
            dateTv.setText(DateUtils.dateToStringDate(remindDate));
            timeTv.setText(DateUtils.dateToStringTime(remindDate));
        }else{
            dateTv.setText(R.string.set_date);
            timeTv.setText(R.string.set_time);
        }
        setupDatePicker();
        setupCheckbox();
        setupSaveButton();
    }

    private void delete() {
        Intent result = new Intent();
        result.putExtra(KEY_TODO,todo.id);
        setResult(Activity.RESULT_OK, result);
        finish();
    }

    private void setupDatePicker() {
        dateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                if(remindDate != null) {
                    c.setTime(remindDate);
                }
                Dialog dialog = new DatePickerDialog(TodoEditActivity.this,TodoEditActivity.this,
                        c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        timeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                if(remindDate != null){
                    c.setTime(remindDate);
                }
                Dialog dialog = new TimePickerDialog(TodoEditActivity.this, TodoEditActivity.this,
                        c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),true);
                dialog.show();
            }
        });
    }
    private void setupCheckbox(){
        complete_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setTextViewStrikeThrough(editText,b);
                editText.setTextColor( b ? Color.GRAY : Color.GREEN);
            }
        });
        View completeWrapper = findViewById(R.id.edit_todo_complete_wrapper);
        completeWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                complete_btn.setChecked(!complete_btn.isChecked());
            }
        });

    }
    private void setupSaveButton(){
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.edit_todo_complete_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (todo == null) {
                    todo = new TodoItem(editText.getText().toString(), remindDate);
                } else {
                    todo.text = editText.getText().toString();
                    todo.remindDate = remindDate;
                }
                todo.done = complete_btn.isChecked();
            }
        });
        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_TODO, todo);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    public void setTextViewStrikeThrough(@NonNull TextView tv, boolean strikeThrough) {
        if (strikeThrough) {
            // strike through effect on the text
            tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            // no strike through effect
            tv.setPaintFlags(tv.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }
    private void setupActionbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        setTitle(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar c = Calendar.getInstance();
        if(remindDate != null) {
            c.setTime(remindDate);
        }
        c.set(i,i1,i2);
        remindDate = c.getTime();
        dateTv.setText(DateUtils.dateToStringDate(remindDate));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        Calendar c = Calendar.getInstance();
        if(remindDate != null){
            c.setTime(remindDate);
        }
        c.set(i,i1);
        remindDate = c.getTime();
        timeTv.setText(DateUtils.dateToStringTime(remindDate));

    }

}
