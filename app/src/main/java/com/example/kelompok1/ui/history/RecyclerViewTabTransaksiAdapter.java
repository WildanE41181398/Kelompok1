package com.example.kelompok1.ui.history;

import android.annotation.SuppressLint;
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
import com.example.kelompok1.ui.notifications.DetailNotification;

import java.util.List;

public class RecyclerViewTabTransaksiAdapter
        extends RecyclerView.Adapter<RecyclerViewTabTransaksiAdapter.MyViewHolder> {

    private Context mContext;
    private List<ModelTransaksi> mTransaksi;

    public RecyclerViewTabTransaksiAdapter(Context context, List<ModelTransaksi> mTransaksi) {
        this.mContext = context;
        this.mTransaksi = mTransaksi;
    }


    @NonNull
    @Override
    public RecyclerViewTabTransaksiAdapter.MyViewHolder
    onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.row_transaksi, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewTabTransaksiAdapter.MyViewHolder holder, final int position) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.judul.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);

        }

        holder.date.setText(mTransaksi.get(position).getTanggal().substring(0, 10));
        holder.judul.setText(mTransaksi.get(position).getNama());
        holder.harga.setText("Rp. " + mTransaksi.get(position).getTotal());
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailNotification.class);
                intent.putExtra("id", mTransaksi.get(position).getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTransaksi.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView judul, date, harga;
        CardView cv;


        MyViewHolder(View itemView) {
            super(itemView);

            judul = itemView.findViewById(R.id.tv_judultransaksi);
            harga = itemView.findViewById(R.id.tv_hargatransaksi);
            date = itemView.findViewById(R.id.tv_datetransaksi);
            cv = itemView.findViewById(R.id.cv_row_transaksi);
        }

    }
}




