package com.ute.hakidictionary.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.ute.hakidictionary.R;
import com.ute.hakidictionary.activities.HistoryActivity;
import com.ute.hakidictionary.activities.SearchByVoiceActivity;
import com.ute.hakidictionary.activities.SearchVEActivity;
import com.ute.hakidictionary.activities.YourWordActivity;
import com.ute.hakidictionary.adapter.WordEVAdapter;
import com.ute.hakidictionary.model.WordSearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    SearchView txtSearch;
    List<WordSearch> wordSearchList;
    RecyclerView rcvWord;
    LinearLayout layoutChucNang, layoutHocCungHK;
    WordEVAdapter wordEVAdapter;
    ImageView btnDicVE, btnYourWord, btnHistory, btnVoice;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rcvWord = (RecyclerView) view.findViewById(R.id.rcv_word);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvWord.setLayoutManager(linearLayoutManager);

        btnDicVE = (ImageView) view.findViewById(R.id.btn_dicVE);
        btnYourWord = (ImageView) view.findViewById(R.id.btn_yourWord);
        btnHistory = (ImageView) view.findViewById(R.id.btn_history);
        btnVoice = (ImageView) view.findViewById(R.id.btn_voice);
        layoutChucNang = (LinearLayout) view.findViewById(R.id.chucnang);
        layoutHocCungHK = (LinearLayout) view.findViewById(R.id.hoccungHaKi);
        layoutChucNang.setVisibility(View.VISIBLE);
        layoutHocCungHK.setVisibility(View.VISIBLE);
        rcvWord.setVisibility(View.GONE);

        txtSearch = (SearchView) view.findViewById(R.id.txt_search);
        txtSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                wordEVAdapter = new WordEVAdapter(getContext(),getListWord(query.trim()));
                if(!query.equals("")){
                    rcvWord.setVisibility(View.VISIBLE);
                    layoutChucNang.setVisibility(View.GONE);
                    layoutHocCungHK.setVisibility(View.GONE);
                    final Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rcvWord.setAdapter(wordEVAdapter);
                        }
                    }, 2000);
                }
                else{
                    rcvWord.setVisibility(View.GONE);
                    layoutChucNang.setVisibility(View.VISIBLE);
                    layoutHocCungHK.setVisibility(View.VISIBLE);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                wordEVAdapter = new WordEVAdapter(getContext(),getListWord(newText.trim()));
                if(!newText.equals("")){
                    rcvWord.setVisibility(View.VISIBLE);
                    layoutChucNang.setVisibility(View.GONE);
                    layoutHocCungHK.setVisibility(View.GONE);

                    final Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rcvWord.setAdapter(wordEVAdapter);
                        }
                    }, 2000);
                }
                else{
                    rcvWord.setVisibility(View.GONE);
                    layoutChucNang.setVisibility(View.VISIBLE);
                    layoutHocCungHK.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        //intent
        btnDicVE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchVEActivity.class);
                startActivity(intent);
            }
        });
        btnYourWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), YourWordActivity.class);
                startActivity(intent);
            }
        });
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), HistoryActivity.class);
                startActivity(intent);
            }
        });
        btnVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchByVoiceActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private List<WordSearch> getListWord(String name){
        List<WordSearch> list = new ArrayList<>();
        AndroidNetworking.get("http://ec2-3-144-36-186.us-east-2.compute.amazonaws.com:3030/api/dictEngVie/" + name)
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