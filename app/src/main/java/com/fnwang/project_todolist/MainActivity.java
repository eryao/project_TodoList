package com.fnwang.project_todolist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fnwang.project_todolist.models.TodoItem;
import com.fnwang.project_todolist.utils.DateUtils;
import com.fnwang.project_todolist.utils.ModelUtils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int REQ_CODE_TODO_EDIT = 100;

    private static final String TODOLIST = "todo_list";

    private  TodoListAdapter adapter;
    private  List<TodoItem> todoList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //ModelUtils.clear(this, TODOLIST);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.main_list_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "Add new TodoItem", Toast.LENGTH_SHORT).show();
                //add an intent
                Intent intent = new Intent(MainActivity.this, TodoEditActivity.class);
                startActivityForResult(intent, REQ_CODE_TODO_EDIT);
            }
        });
        //setupUI(fakeData());
        //loadData();
        todoList = ModelUtils.read(this,TODOLIST,new TypeToken<List<TodoItem>>(){});
        if(todoList == null){
            todoList = new ArrayList<>();
        }

        adapter = new TodoListAdapter(this, todoList);
        ((ListView)findViewById(R.id.main_list_view)).setAdapter(adapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_CODE_TODO_EDIT && resultCode == Activity.RESULT_OK){
            //delete
            String todoId = data.getStringExtra(TodoEditActivity.KEY_TODO_ID);
           if(todoId != null){
               //delete todoItem
               for(int i = 0; i < todoList.size(); ++i){
                   TodoItem item = todoList.get(i);
                   if(TextUtils.equals(item.id, todoId)){
                       todoList.remove(i);
                       break;
                   }
               }
               adapter.notifyDataSetChanged();
               ModelUtils.save(this, TODOLIST, todoList);
           }
           else {
               TodoItem todo = data.getParcelableExtra(TodoEditActivity.KEY_TODO);
               //update todoItem
                boolean found = false;
                for(int i = 0; i < todoList.size(); i++){
                    TodoItem item = todoList.get(i);
                    if(TextUtils.equals(item.id, todo.id)){
                        found = true;
                        todoList.set(i,todo);
                        break;
                    }
                }
                if(!found) {
                    todoList.add(todo);
                }

                adapter.notifyDataSetChanged();
                ModelUtils.save(this, TODOLIST, todoList);
           }

        }
    }
    public void updateTodo(int index, boolean done) {
        todoList.get(index).done = done;

        adapter.notifyDataSetChanged();
        ModelUtils.save(this, TODOLIST, todoList);
    }
//    public void updateTodo(int index, boolean done) {
//        todoList.get(index).done = done;
//
//        adapter.notifyDataSetChanged();
//        ModelUtils.save(this, TODOLIST, todoList);
//    }



    private List<TodoItem> fakeData() {
        List<TodoItem> mainList = new ArrayList<>();
        for(int i = 0; i < 1000; i++){
            TodoItem todo = new TodoItem("todo "+(i+1), DateUtils.stringToDate("2018 1 28 5:26"));
            mainList.add(todo);
        }
        return mainList;
    }
//third edition
//    private void setupUI(@NonNull List<TodoItem> data) {
//        ListView listview = (ListView) findViewById(R.id.main_list_view);
//        listview.setAdapter(new TodoListAdapter(this,data));
        //second edition
//        LinearLayout linearlayoutcontainer = (LinearLayout)findViewById(R.id.crappy_list);
//        linearlayoutcontainer.removeAllViews();
//        TodoListAdapter adapter = new TodoListAdapter(this,data);
//        for(int i = 0; i < data.size(); i++){
//            View view = adapter.getView(i);
//            linearlayoutcontainer.addView(view);
//        }
       //first edition
//        for(TodoItem todo : data){
//            View view = getListItemView(todo);
//            linearlayoutcontainer.addView(view);
//        }
//    }

//    private View getListItemView(TodoItem todo) {
//        View view = getLayoutInflater().inflate(R.layout.list_item,null);
//        ((TextView)view.findViewById(R.id.list_item_text)).setText(todo.text);
//        return view;
//    }
}
