package org.techtown.finalexam;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Movie;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class Fragment2 extends Fragment {
    EditText editText;
    TextView textView;

    static RequestQueue requestQueue;

    RecyclerView recyclerView;
    LibraryAdapter adapter;

    //작은도서관 정보 확인 기능
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment2, container, false);

        editText = rootView.findViewById(R.id.editText);
        textView = rootView.findViewById(R.id.textView);

        //조회하기
        Button button = rootView.findViewById(R.id.button2A);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeRequest();
            }
        });

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        }

        recyclerView = rootView.findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new LibraryAdapter();
        recyclerView.setAdapter(adapter);

        return rootView;
    }
    //요청
    public void makeRequest() {
        String url = editText.getText().toString();
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        println("응답 -> " + response);
                        processResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        println("에러 -> " + error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                return params;
            }
        };
        request.setShouldCache(false);
        requestQueue.add(request);
        println("요청 보냄.");
    }
    //상태 확인
    public void println(String data) {
        Log.d("Fragment2", data);
    }
    //응답 과정
    public void processResponse(String response) {
        Gson gson = new Gson();
        LList lList = gson.fromJson(response, LList.class);

        println("도서관의 수 : " + lList.response.body.items.size());

        for (int i = 0; i < lList.response.body.items.size(); i++) {
            Library library = lList.response.body.items.get(i);

            adapter.addItem(library);
        }

        adapter.notifyDataSetChanged();
    }

}