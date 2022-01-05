package org.techtown.finalexam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class Fragment1 extends Fragment {
    MemoDB helper;
    SQLiteDatabase db;
    MyAdapter adapter;
    Cursor cursor;
    ListView list;

    //메모장 기능
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment1, container, false);
        helper=new MemoDB(getActivity());
        db=helper.getReadableDatabase();
        cursor=db.rawQuery("select * from memo order by title desc",null);//글 표시
        list=rootView.findViewById(R.id.listView);//글 목록
        adapter=new MyAdapter(getActivity(),cursor);
        list.setAdapter(adapter);
        Button btnwrite=rootView.findViewById(R.id.button1A);
        btnwrite.setOnClickListener(new View.OnClickListener() {
            //삽입 버튼
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),InsertActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }
    //메모장 글 수정 및 삭제 기능
    class MyAdapter extends CursorAdapter{
        public MyAdapter(Context context, Cursor c) {
            super(context, c);
        }
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return getLayoutInflater().inflate(R.layout.item,parent,false);
        }
        @Override
        public void bindView(View view, Context context, final Cursor cursor) {
            TextView txttitle=view.findViewById(R.id.txttitle);
            txttitle.setText(cursor.getString(1));
            TextView txtcontent=view.findViewById(R.id.txtcontent);
            txtcontent.setText(cursor.getString(2));
            Button btndel=view.findViewById(R.id.buttonD);
            final int _id=cursor.getInt(0);
            //글 삭제
            btndel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder box=new AlertDialog.Builder(getActivity());
                    box.setMessage("해당 글을 삭제하시겠습니까?");
                    box.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String sql="delete from memo where _id="+_id;
                            db.execSQL(sql);
                            onResume();
                        }
                    });
                    box.setNegativeButton("닫기",null);
                    box.show();
                }
            });
            //글 수정
            Button btnupdate=view.findViewById(R.id.buttonU);
            btnupdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(),UpdateActivity.class);
                    intent.putExtra("_id", _id);
                    startActivity(intent);
                }
            });
        }
    }
    //메모장 업데이트 기능
    @Override
    public void onResume() {
        cursor=db.rawQuery("select * from memo order by title desc",null);
        adapter.changeCursor(cursor);
        super.onResume();
    }
}