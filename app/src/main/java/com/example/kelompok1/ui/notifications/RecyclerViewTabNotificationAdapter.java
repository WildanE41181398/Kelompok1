package com.example.kelompok1.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kelompok1.R;

import java.util.List;

public class RecyclerViewTabNotificationAdapter
        extends RecyclerView.Adapter<RecyclerViewTabNotificationAdapter.MyViewHolder> {

    private Context mContext;
    private List<ModelNotification> mNotifications;

    public RecyclerViewTabNotificationAdapter(Context context, List<ModelNotification> mNotifications){
        this.mContext = context;
        this.mNotifications = mNotifications;
    }

    @NonNull
    @Override
    public RecyclerViewTabNotificationAdapter.MyViewHolder
    onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.row_notification, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.judul.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        }

        holder.date.setText(mNotifications.get(position).getTanggal().substring(0, 10));
        String judulnotif = "1";
        switch (mNotifications.get(position).getStatus()){
            case "0":
                judulnotif = "Pesanan anda berhasil dibuat! Mohon tunggu untuk konfirmasi penjemputan";
                break;
            case "1":
                judulnotif = "Kami akan menjemput cucian ke rumah anda!";
                break;
        }
        holder.judul.setText(judulnotif);
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailMessage.class);
                intent.putExtra("id", mNotifications.get(position).getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNotifications.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView judul, date;
        CardView cv;


        MyViewHolder(View itemView){
            super(itemView);

            judul = itemView.findViewById(R.id.tv_judulnotification);
            date = itemView.findViewById(R.id.tv_datenotification);
            cv = itemView.findViewById(R.id.cv_row_notification);
        }

    }
}
