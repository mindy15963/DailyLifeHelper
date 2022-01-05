package org.techtown.finalexam;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MemoDB extends SQLiteOpenHelper {
    //메모장 DB
    public MemoDB(@Nullable Context context) {
        super(context, "memo.db", null, 1);
    }
    @Override
    //글 작성
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table memo(_id integer primary key autoincrement,title text,content text)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}