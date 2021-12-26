package com.ute.hakidictionary.activities;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.squareup.picasso.Picasso;
import com.ute.hakidictionary.R;
import com.ute.hakidictionary.model.WordSearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class WordDetailEVActivity extends AppCompatActivity {
    WebView webView;
    ImageView image, btnReturn, btnSpeech, btnLove;
    TextView txtName;
    int wordId;
    String wordName;
    TextToSpeech textToSpeech;
    boolean isPlay = false;
    int userId, idDicEV;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark

        //Anh xa
        webView = findViewById(R.id.webview_content);
        image = findViewById(R.id.image);
        txtName = findViewById(R.id.txt_name);
        btnReturn = findViewById(R.id.ic_return);
        btnSpeech = findViewById(R.id.btn_speech);
        btnLove = findViewById(R.id.btn_love);

       btnReturn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(WordDetailEVActivity.this, MainActivity.class);
               startActivity(intent);
               overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
           }
       });

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS){
                    //Select language
                    int lang = textToSpeech.setLanguage(Locale.US);
                }
            }
        });

       btnSpeech.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                String s = txtName.getText().toString();

                //Text convert to speech
               int speech = textToSpeech.speak(s, TextToSpeech.QUEUE_FLUSH, null);
           }
       });

       btnLove.setImageResource(R.drawable.ic_loving);
       btnLove.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (isPlay){
                   btnLove.setImageResource(R.drawable.ic_loving);
               }
               else{
                   btnLove.setImageResource(R.drawable.ic_loved);
                   Toast.makeText(getApplicationContext(), "Đã thêm vào Từ của bạn", Toast.LENGTH_SHORT).show();
                   addData();
               }
               isPlay = !isPlay;
           }
       });

        Intent intent = this.getIntent();
        wordId = intent.getIntExtra("wordId", 2);
        wordName = intent.getStringExtra("wordName");

        //lấy id user
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId",0);

        loadData();
    }

    public void loadData(){
        AndroidNetworking.get("http://ec2-3-144-36-186.us-east-2.compute.amazonaws.com:3030/api/dictEngVie/id/{id}")
                .addPathParameter("id", String.valueOf(wordId))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            WordSearch wordSearch = new WordSearch();
                            JSONObject obj = response.getJSONObject(0);
                            wordSearch.setId(obj.getInt("id"));
                            wordSearch.setName(obj.getString("name"));
                            wordSearch.setContent(obj.getString("content"));
                            wordSearch.setImageUrl(obj.getString("image"));

                            txtName.setText(wordSearch.getName());
                            if (!wordSearch.getImageUrl().equals("")){
                                Picasso.get().load(wordSearch.getImageUrl()).into(image);
                            }

                            String content =
                                    "<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />"+
                                            "<style>" +
                                            "    body{" +
                                            "        font-size: 18px;" +
                                            "    }" +
                                            "    .type{" +
                                            "        color: rgb(13, 13, 168);" +
                                            "        font-style:italic;" +
                                            "    }" +
                                            "    .title{" +
                                            "        color:#c90043;" +
                                            "        font-weight: bold;" +
                                            "    }" +
                                            "</style>" + wordSearch.getContent();
                            WebSettings settings = webView.getSettings();
                            settings.setDefaultTextEncodingName("utf-8");
                            webView.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.v("Error", anError.toString());
                    }
                });
    }

    public void addData(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("idUser", userId);
            jsonObject.put("idDicEV", wordId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post("http://ec2-3-144-36-186.us-east-2.compute.amazonaws.com:3030/api/yourWordEngVie/addYourWordEngVie")
                .addJSONObjectBody(jsonObject) // posting json
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
}