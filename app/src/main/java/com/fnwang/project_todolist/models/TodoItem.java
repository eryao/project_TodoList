package com.fnwang.project_todolist.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by ou on 18/1/28.
 */

public class TodoItem implements Parcelable{
    public String id;
    public String text;
    public boolean done;
    public Date remindDate;

    public TodoItem(String text, Date date){
        //this.id = UUID.randomUUID().toString();
        this.text = text;
        this.done = false;
        this.remindDate = date;
    }

    protected TodoItem(Parcel in) {
        id = in.readString();
        text = in.readString();
        done = in.readByte()!= 0;
        long date = in.readLong();
        remindDate = date == 0 ? null : new Date(date);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(text);
        dest.writeByte((byte) (done ? 1 : 0));
        dest.writeLong(remindDate == null ? 0 : remindDate.getTime());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TodoItem> CREATOR = new Creator<TodoItem>() {
        @Override
        public TodoItem createFromParcel(Parcel in) {
            return new TodoItem(in);
        }

        @Override
        public TodoItem[] newArray(int size) {
            return new TodoItem[size];
        }
    };
}
