package com.example.kelompok1.ui.notifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kelompok1.R;

import java.util.List;

public class RecyclerViewDetailNotificationAdapter
        extends RecyclerView.Adapter<RecyclerViewDetailNotificationAdapter.MyViewHolder>{

    private Context mContext;
    private List<ModelNotification> mNotifications;

    public RecyclerViewDetailNotificationAdapter(Context mContext, List<ModelNotification> mNotifications){
        this.mContext = mContext;
        this.mNotifications = mNotifications;
    }

    @NonNull
    @Override
    public RecyclerViewDetailNotificationAdapter.MyViewHolder
    onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.row_detail_notification, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.no.setText(position+1 + ".");
        holder.nama.setText(mNotifications.get(position).getDtl_nama());
        holder.berat.setText(mNotifications.get(position).getDtl_berat());
        holder.subtotal.setText("Rp. " +mNotifications.get(position).getDtl_subtotal());
    }

    @Override
    public int getItemCount() {
        return mNotifications.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView no, nama, berat, subtotal;

        MyViewHolder(View itemView) {
            super(itemView);

            no = itemView.findViewById(R.id.tv_body_1);
            nama = itemView.findViewById(R.id.tv_body_2);
            berat = itemView.findViewById(R.id.tv_body_3);
            subtotal = itemView.findViewById(R.id.tv_body_5);
        }
    }
}
