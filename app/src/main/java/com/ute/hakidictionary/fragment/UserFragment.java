package com.ute.hakidictionary.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.squareup.picasso.Picasso;
import com.ute.hakidictionary.R;
import com.ute.hakidictionary.activities.LoginActivity;
import com.ute.hakidictionary.activities.SplashScreenActivity;
import com.ute.hakidictionary.adapter.NotificationAdapter;
import com.ute.hakidictionary.adapter.YourWordAdapter;
import com.ute.hakidictionary.model.CountWord;
import com.ute.hakidictionary.model.Notification;
import com.ute.hakidictionary.model.WordSearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

public class UserFragment extends Fragment {
    TextView txtCountYourWord, txtHistory;
    ArrayList<WordSearch> list;
    Button btnLogout;
    int userId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        txtCountYourWord = view.findViewById(R.id.count_your_word);
        txtHistory = view.findViewById(R.id.count_history);
        btnLogout = view.findViewById(R.id.btn_logout);

        //lấy id user
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId",0);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SplashScreenActivity.class);
                startActivity(intent);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("userId");
                editor.commit();
            }
        });

        getCountYourWord();
        getCountHistory();
        return view;
    }

    private void getCountYourWord() {
        AndroidNetworking.get("http://ec2-3-144-36-186.us-east-2.compute.amazonaws.com:3030/api/statistic/countYourWord/" + userId)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        {
                            try {
                                txtCountYourWord.setText("" + response.getInt("sum"));
                            }catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
    private void getCountHistory() {
        AndroidNetworking.get("http://ec2-3-144-36-186.us-east-2.compute.amazonaws.com:3030/api/statistic/countHistoryWord/" + userId)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        {
                            try {
                                txtHistory.setText("" + response.getInt("sum"));
                            }catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
}