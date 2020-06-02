package com.example.kelompok1.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kelompok1.R;

import java.util.List;

public class RecyclerViewTabMessagesAdapter
        extends RecyclerView.Adapter<RecyclerViewTabMessagesAdapter.MyViewHolder> {

    private Context mContext;
    private List<ModelMessages> mMessages;

    public RecyclerViewTabMessagesAdapter(Context mContext, List<ModelMessages> mMessages){
        this.mContext = mContext;
        this.mMessages = mMessages;
    }

    @NonNull
    @Override
    public RecyclerViewTabMessagesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.row_messages, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.body.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        }
        holder.judul.setText(mMessages.get(position).getSubjek());
        holder.body.setText(mMessages.get(position).getBody().substring(0, 100) + "...");

        holder.cv_row_messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailMessage.class);
                intent.putExtra("id", mMessages.get(position).getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView judul, body, date;
        CardView cv_row_messages;


        MyViewHolder(View itemView){
            super(itemView);

            judul = itemView.findViewById(R.id.tv_judulmessages);
            body = itemView.findViewById(R.id.tv_bodymessages);
            date = itemView.findViewById(R.id.tv_datemessages);
            cv_row_messages = itemView.findViewById(R.id.cv_row_messages);
        }

    }

}
