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
import com.ute.hakidictionary.adapter.HistoryAdapter;
import com.ute.hakidictionary.model.WordSearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HistoryVEFragment extends Fragment {
    ArrayList<WordSearch> list;
    RecyclerView rcv;
    int userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history_v_e, container, false);

        rcv = (RecyclerView) view.findViewById(R.id.rcv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcv.setLayoutManager(linearLayoutManager);

        //lấy id user
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId",0);

        getListHistoryVE();

        return view;
    }

    private void getListHistoryVE() {
        AndroidNetworking.get("http://ec2-3-144-36-186.us-east-2.compute.amazonaws.com:3030/api/historyVieEng/" + userId)
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
                                wordSearch.setId(onj.getInt("idDicVE"));
                                wordSearch.setName(onj.getString("name"));
                                list.add(wordSearch);
                            }catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                        HistoryAdapter historyAdapter = new HistoryAdapter(getActivity(),list);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        rcv.setLayoutManager(linearLayoutManager);
                        rcv.setAdapter(historyAdapter);
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
}