package com.ute.hakidictionary.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.ute.hakidictionary.R;
import com.ute.hakidictionary.adapter.WordEVAdapter;
import com.ute.hakidictionary.model.WordSearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class SearchByVoiceActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    EditText edtSearch;
    ImageView btnVoice, btnReturn;
    WordSearch wordSearch;
    RecyclerView rcvWord;
    WordEVAdapter wordEVAdapter;
    GifImageView gifImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_voice);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark

        gifImageView = findViewById(R.id.gif);
        btnReturn = findViewById(R.id.ic_return);
        edtSearch = findViewById(R.id.edt_search);
        btnVoice = findViewById(R.id.btn_voice);
        rcvWord = findViewById(R.id.rcv_word);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rcvWord.setLayoutManager(linearLayoutManager);

        btnVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                wordEVAdapter = new WordEVAdapter(getApplicationContext(),getListWord(s.toString()));
                if(!s.toString().equals("")){
                    rcvWord.setVisibility(View.VISIBLE);
                    gifImageView.setVisibility(View.GONE);
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
                    gifImageView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void speak(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Nói từ bạn muốn tra!");

        //start intent
        try{
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        }catch (Exception e){
            Toast.makeText(SearchByVoiceActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_SPEECH_INPUT:{
                if (resultCode == RESULT_OK && data != null){
                    //get text array from voice intent
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //set to text
                    String suggestWord = result.get(0);
                    edtSearch.setText(suggestWord);
                }
                break;
            }
        }
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