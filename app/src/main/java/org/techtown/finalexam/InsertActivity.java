package org.techtown.finalexam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class InsertActivity extends AppCompatActivity {
    MemoDB helper;
    SQLiteDatabase db;
    //메모장 글 삽입하기
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        helper=new MemoDB(this);
        db=helper.getWritableDatabase();
        //글 저장
        Button btnsave=findViewById(R.id.buttonIS);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edittitle=findViewById(R.id.edittitle);
                String strtitle=edittitle.getText().toString();
                EditText editcontent=findViewById(R.id.editcontent);
                String strcontent=editcontent.getText().toString();
                String sql="insert into memo(title,content) values(";
                sql += "'" + strtitle + "',";
                sql += "'" + strcontent + "')";
                db.execSQL(sql);
                Toast.makeText(InsertActivity.this, "저장 완료", Toast.LENGTH_SHORT).show();
                finish();
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