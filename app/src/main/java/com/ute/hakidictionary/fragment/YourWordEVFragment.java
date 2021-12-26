package com.ute.hakidictionary.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.ute.hakidictionary.R;
import com.ute.hakidictionary.adapter.YourWordAdapter;
import com.ute.hakidictionary.model.WordSearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class YourWordEVFragment extends Fragment{
    ArrayList<WordSearch> list;
    RecyclerView rcv;
    YourWordAdapter adapter;
    int userId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_your_word_e_v, container, false);

        rcv = (RecyclerView) view.findViewById(R.id.rcv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcv.setLayoutManager(linearLayoutManager);

        //lấy id user
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId",0);

        getListYourWordEV();

        return view;
    }

    private void getListYourWordEV() {
        AndroidNetworking.get("http://ec2-3-144-36-186.us-east-2.compute.amazonaws.com:3030/api/yourWordEngVie/" + userId)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONArray response) {
                        list = new ArrayList<>();
                        for(int i=0;i< response.length();i++)
                        {
                            try {
                                WordSearch wordSearch = new WordSearch();
                                JSONObject onj = response.getJSONObject(i);
                                wordSearch.setId(onj.getInt("idDicEV"));
                                wordSearch.setName(onj.getString("name"));
                                list.add(wordSearch);
                            }catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                        YourWordAdapter wordAdapter = new YourWordAdapter(getActivity(),list);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        rcv.setLayoutManager(linearLayoutManager);
                        rcv.setAdapter(wordAdapter);
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

}