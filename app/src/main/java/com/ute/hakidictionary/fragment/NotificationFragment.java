package com.ute.hakidictionary.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.ute.hakidictionary.R;
import com.ute.hakidictionary.adapter.NotificationAdapter;
import com.ute.hakidictionary.model.Notification;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

public class NotificationFragment extends Fragment {
    private ListView lvContact;
    ArrayList<Notification> list;
    private int cusId;
    NotificationAdapter adapter;
    RecyclerView rcvNoti;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        rcvNoti = view.findViewById(R.id.rcv_noti);
        getNotiList();

        return view;
    }
    private void getNotiList() {
        AndroidNetworking.get("http://ec2-3-144-36-186.us-east-2.compute.amazonaws.com:3030/api/announcement")
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONArray response) {
                        list = new ArrayList<>();
                        for(int i=0;i< response.length();i++)
                        {
                            try {
                                Notification notification = new Notification();
                                JSONObject onj = response.getJSONObject(i);
                                notification.setId(onj.getInt("id"));
                                notification.setTitle(onj.getString("title"));
                                notification.setImageUrl(onj.getString("imageUrl"));
                                notification.setContent(onj.getString("content"));
                                Instant fm = Instant.parse(onj.getString("createdTime"));
                                Date d = Date.from(fm);
                                SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy");
                                String e = simpleDate.format(d);
                                notification.setCreatedTime(e);
                                list.add(notification);
                            }catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                        NotificationAdapter customAdaper = new NotificationAdapter(getActivity(),list);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        rcvNoti.setLayoutManager(linearLayoutManager);
                        rcvNoti.setAdapter(customAdaper);
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

}