package com.ute.hakidictionary.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.ute.hakidictionary.R;
import com.ute.hakidictionary.adapter.WordEVAdapter;
import com.ute.hakidictionary.adapter.WordVEAdapter;
import com.ute.hakidictionary.model.WordSearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchVEActivity extends AppCompatActivity {
    SearchView txtSearch;
    List<WordSearch> wordSearchList;
    RecyclerView rcvWord;
    WordVEAdapter wordVEAdapter;
    ImageView btn_return;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_veactivity);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark

        txtSearch = findViewById(R.id.txt_search);
        rcvWord = findViewById(R.id.rcv_word);
        btn_return = findViewById(R.id.ic_return);

        rcvWord = findViewById(R.id.rcv_word);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rcvWord.setLayoutManager(linearLayoutManager);

        txtSearch = findViewById(R.id.txt_search);
        txtSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                wordVEAdapter = new WordVEAdapter(getApplicationContext(),getListWord(query.trim()));
                if(!query.equals("")){
                    final Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rcvWord.setAdapter(wordVEAdapter);
                        }
                    }, 2000);
                }
                else{
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                wordVEAdapter = new WordVEAdapter(getApplicationContext(),getListWord(newText.trim()));
                if(!newText.equals("")){
                    final Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rcvWord.setAdapter(wordVEAdapter);
                        }
                    }, 2000);
                }
                else{
                }
                return false;
            }
        });

        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });
    }
    private List<WordSearch> getListWord(String name){
        List<WordSearch> list = new ArrayList<>();
        AndroidNetworking.get("http://ec2-3-144-36-186.us-east-2.compute.amazonaws.com:3030/api/dictVieEng/" + name)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++){
                            try {
                                WordSearch wordSearch = new WordSearch();
                                JSONObject obj =  response.getJSONObject(i);
                                wordSearch.setId(obj.getInt("id"));
                                wordSearch.setName(obj.getString("name"));
                                list.add(wordSearch);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
        return list;
    }
}