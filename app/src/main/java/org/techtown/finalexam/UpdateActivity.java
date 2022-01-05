package org.techtown.finalexam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UpdateActivity extends AppCompatActivity {
    int _id;
    MemoDB helper;
    SQLiteDatabase db;
    //메모장 글 수정하기
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Intent intent=getIntent();
        _id=intent.getIntExtra("_id",0);
        helper=new MemoDB(this);
        db=helper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from memo where _id="+_id, null);
        if(cursor.moveToNext()){
            TextView edittitle=findViewById(R.id.edittitle);
            edittitle.setText(cursor.getString(1));
            TextView editcontent=findViewById(R.id.editcontent);
            editcontent.setText(cursor.getString(2));
        }
        //글 저장
        Button btnsave=findViewById(R.id.buttonUU);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder box=new AlertDialog.Builder(UpdateActivity.this);
                box.setMessage("수정하시겠습니까?");
                box.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText edittitle=findViewById(R.id.edittitle);
                        String strtitle=edittitle.getText().toString();
                        EditText editcontent=findViewById(R.id.editcontent);
                        String strcontent=editcontent.getText().toString();
                        String sql="update memo set title='" + strtitle + "'," + "content='" + strcontent + "'";
                        sql += " where _id=" +_id;
                        db.execSQL(sql);
                        finish();
                    }
                });
                box.setNegativeButton("취소",null);
                box.show();
            }
        });
    }
    //돌아가기
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}