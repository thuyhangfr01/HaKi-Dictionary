package com.ute.hakidictionary.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ute.hakidictionary.R;
import com.ute.hakidictionary.activities.WordDetailEVActivity;
import com.ute.hakidictionary.model.WordSearch;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class WordEVAdapter extends RecyclerView.Adapter<WordEVAdapter.WordViewHolder> {
    private List<WordSearch> mlistWord;
    private Context context;
    String createdTime;
    int userId;
    public WordEVAdapter(Context context, List<WordSearch> mlistWord) {
        this.mlistWord = mlistWord;
        this.context = context;
    }

    public void setData(List<WordSearch> list){
        this.mlistWord = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_av,parent,false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordEVAdapter.WordViewHolder holder, int position) {
        WordSearch wordSearch = mlistWord.get(position);
        if (wordSearch == null){
            return;
        }
        holder.txtName.setText(wordSearch.getName());

        holder.layoutSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
                createdTime = df.format(Calendar.getInstance().getTime());

                //lấy id user
                SharedPreferences sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
                userId = sharedPreferences.getInt("userId",0);

                Intent intent = new Intent(context, WordDetailEVActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("wordName", wordSearch.getName());
                intent.putExtra("wordId", wordSearch.getId());
                context.startActivity(intent);
                addListHistory(userId, wordSearch.getId(), createdTime);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mlistWord != null){
            return mlistWord.size();
        }
        return 0;
    }

    public class WordViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        RelativeLayout layoutSearch;
        public WordViewHolder(View view) {
            super(view);
            txtName = view.findViewById(R.id.text);
            layoutSearch = view.findViewById(R.id.layoutSearch);
        }
    }

    public void addListHistory(int idUser, int idDicEV, String createdTime){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("idUser", idUser);
            jsonObject.put("idDicEV", idDicEV);
            jsonObject.put("dateSearch", createdTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post("http://ec2-3-144-36-186.us-east-2.compute.amazonaws.com:3030/api/historyEngVie/addHistoryEngVie")
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
