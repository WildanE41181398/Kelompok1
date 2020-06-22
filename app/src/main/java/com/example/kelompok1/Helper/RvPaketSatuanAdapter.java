package com.example.kelompok1.Helper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kelompok1.R;
import com.example.kelompok1.TransaksiTahap1;

import java.util.List;

public class RvPaketSatuanAdapter extends RecyclerView.Adapter<RvPaketSatuanAdapter.MyViewHolder> {

    private Context mContext;
    private List<ModelPaketSatuan> mData;

    public RvPaketSatuanAdapter(Context mContext, List<ModelPaketSatuan> mData){
        this.mContext = mContext;
        this.mData = mData;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.row_paket_satuan, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {
        myViewHolder.nama_barang.setText(mData.get(position).getNama_barang());
        myViewHolder.harga.setText("Rp. " + mData.get(position).getHarga());
        if (position % 2 == 0){
            myViewHolder.cardView.setCardBackgroundColor(Color.parseColor("#80c000"));
        }else {
            myViewHolder.cardView.setCardBackgroundColor(Color.parseColor("#FF9900"));
        }
        myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TransaksiTahap1.class);
                intent.putExtra("id_paket", mData.get(position).getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nama_barang, harga;
        LinearLayout linearLayout;
        CardView cardView;

        MyViewHolder(View itemview) {
            super(itemview);

            nama_barang = itemview.findViewById(R.id.tv_namabarang);
            harga = itemview.findViewById(R.id.tv_hargapaketsatuan);
            linearLayout = itemview.findViewById(R.id.linierpaketsatuan);
            cardView = itemview.findViewById(R.id.cv_paketsatuan);
        }


    }
}
