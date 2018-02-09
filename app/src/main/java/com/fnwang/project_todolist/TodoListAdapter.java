package com.fnwang.project_todolist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fnwang.project_todolist.models.TodoItem;

import java.util.List;

/**
 * Created by ou on 18/1/28.
 */

public class TodoListAdapter extends BaseAdapter{
    //private Context context;
    private MainActivity activity;
    private List<TodoItem> data;

    public TodoListAdapter(MainActivity activity, List<TodoItem> data){
        //this.context = context;
        this.activity = activity;
        this.data = data;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertview, ViewGroup parent) {
        //lazy load
        // this is wrong!
        // View view = LayoutInflater.from(context).inflate(R.layout.list_item, null);
        ViewHolder vh;
        if(convertview == null){
            //convertview = LayoutInflater.from(context).inflate(R.layout.list_item, null);
            convertview = activity.getLayoutInflater().inflate(R.layout.list_item, parent, false);

            vh = new ViewHolder();
            vh.todoText = (TextView)convertview.findViewById(R.id.list_item_text);
            vh.doneCheckbox = (CheckBox) convertview.findViewById(R.id.list_item_check);
            //cache the view holder
            convertview.setTag(vh);
        }
        else{
            vh = (ViewHolder)convertview.getTag();
        }

        final TodoItem todo = (TodoItem) getItem(position);
        if(todo == null) {
            Log.d("error","null "+position);
        }
        vh.todoText.setText(todo.text);
        vh.doneCheckbox.setChecked(todo.done);
        setTextViewStrikeThrough(vh.todoText, todo.done);

        vh.doneCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                activity.updateTodo(position,b);
            }
        });
        convertview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, TodoEditActivity.class);
                intent.putExtra(TodoEditActivity.KEY_TODO, todo);
                activity.startActivityForResult(intent, MainActivity.REQ_CODE_TODO_EDIT);
            }
        });



        //TodoItem todoItem = data.get(position);
        //this is time consuming -> how to speed up
        //((TextView)convertview.findViewById(R.id.list_item_text)).setText(todoItem.text);
        //cache -> using a viewHolder

        return convertview;
    }
    private static class ViewHolder{
        TextView todoText;
        CheckBox doneCheckbox;
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
    //second second,这是在没有extend BaseAdapter之前的代码
//    public View getView(int position){
//        View view = LayoutInflater.from(context).inflate(R.layout.list_item, null);
//        TodoItem todoItem = data.get(position);
//        ((TextView)view.findViewById(R.id.list_item_text)).setText(todoItem.text);
//        return view;
//    }
    //first edition 这一段是原来在mainActivity里写的代码，可以看到原来的getListItemView和这里adapter的getView一样
//    private void setupUI(List<TodoItem> data) {
//        //ListView listview = (ListView) findViewById(R.id.main_list_view);
//        LinearLayout linearlayoutcontainer = (LinearLayout)findViewById(R.id.crappy_list);
//        linearlayoutcontainer.removeAllViews();
//        for(TodoItem todo : data){
//            View view = getListItemView(todo);
//            linearlayoutcontainer.addView(view);
//        }
//    }
//
//    private View getListItemView(TodoItem todo) {
//        View view = getLayoutInflater().inflate(R.layout.list_item, null);
//        ((TextView) view.findViewById(R.id.list_item_text)).setText(todo.text);
//        return view;
//    }
}
