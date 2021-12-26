package com.ute.hakidictionary.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ute.hakidictionary.R;
import com.ute.hakidictionary.activities.WordDetailEVActivity;
import com.ute.hakidictionary.model.WordSearch;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private Context context;
    private List<WordSearch> wordSearchList;

    public HistoryAdapter(Context context, List<WordSearch> wordSearchList) {
        this.context = context;
        this.wordSearchList = wordSearchList;
    }

    public void setData(List<WordSearch> list){
        this.wordSearchList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
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

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        private TextView txtName;
        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            layout = itemView.findViewById(R.id.layout);
        }
    }
}
