package com.ute.hakidictionary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ute.hakidictionary.R;
import com.ute.hakidictionary.model.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private Context context;
    private List<Notification> list;

    public NotificationAdapter(Context context, ArrayList<Notification> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification ano = list.get(position);

        holder.txtTitle.setText(ano.getTitle());
        Picasso.get().load(ano.getImageUrl()).into(holder.imgNoti);
        holder.txtDes.setText(String.valueOf(ano.getContent()));
        holder.txtTime.setText(String.valueOf(ano.getCreatedTime()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgNoti;
        private TextView txtTitle;
        private TextView txtDes;
        private TextView txtTime;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            imgNoti = (ImageView) itemView.findViewById(R.id.image);
            txtTitle = (TextView) itemView.findViewById(R.id.title);
            txtDes = (TextView) itemView.findViewById(R.id.description);
            txtTime = (TextView) itemView.findViewById(R.id.dateTime);
        }
    }

}
