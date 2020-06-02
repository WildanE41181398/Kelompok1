package com.example.kelompok1.ui.promosi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kelompok1.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewPromosiAdapter extends RecyclerView.Adapter<RecyclerViewPromosiAdapter.MyViewHolder> {

    private Context mContext;
    private List<ModelPromosi> mData;

    public RecyclerViewPromosiAdapter(Context mContext, List<ModelPromosi> mData){
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public RecyclerViewPromosiAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.row_promosi, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {
        myViewHolder.akhir_priode.setText("Hingga " + mData.get(position).getAkhir_priode().substring(0, 10));

//        Picasso.get().load("http://192.168.5.145/kelompok1_tif_d/OrenzLaundry/assets/files/gambar_promo/" + mData.get(position).getGambar()).into(myViewHolder.gambar);
        Picasso.with(mContext).load("http://192.168.5.145/kelompok1_tif_d/OrenzLaundry/assets/files/gambar_promo/" + mData.get(position).getGambar()).into(myViewHolder.gambar);

        myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailPromosi.class);
                intent.putExtra("id", mData.get(position).getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView akhir_priode;
        ImageView gambar;
        LinearLayout linearLayout;

        MyViewHolder(View itemview) {
            super(itemview);

            akhir_priode = itemview.findViewById(R.id.tanggal_promosi);
            gambar = itemview.findViewById(R.id.iv_promosi);
            linearLayout = itemview.findViewById(R.id.linear_promosi);
        }
    }

}
