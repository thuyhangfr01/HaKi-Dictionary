package com.ute.hakidictionary.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import java.util.List;

public class YourWordAdapter extends RecyclerView.Adapter<YourWordAdapter.YourWordViewHolder> {
    private Context context;
    private List<WordSearch> wordSearchList;

    public YourWordAdapter(Context context, List<WordSearch> wordSearchList) {
        this.context = context;
        this.wordSearchList = wordSearchList;
    }

    public void setData(List<WordSearch> list){
        this.wordSearchList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public YourWordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_your_word, parent, false);
        return new YourWordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YourWordViewHolder holder, int position) {
        WordSearch wordSearch = wordSearchList.get(position);
        if (wordSearch == null)
            return;

        holder.txtName.setText(wordSearch.getName());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WordDetailEVActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("wordName", wordSearch.getName());
                intent.putExtra("wordId", wordSearch.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return wordSearchList != null ? wordSearchList.size() : 0;
    }

    public class YourWordViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        private TextView txtName;
        public YourWordViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            layout = itemView.findViewById(R.id.layout);
        }
    }

    public void removeItemEV(int idUser, int idDicEV){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("idUser", idUser);
            jsonObject.put("idDicEV", idDicEV);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post("http://ec2-3-144-36-186.us-east-2.compute.amazonaws.com:3030/api/yourWordEngVie/deleteYourWordEngVie")
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
