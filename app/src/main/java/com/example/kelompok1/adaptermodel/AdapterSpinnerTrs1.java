package com.example.kelompok1.adaptermodel;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kelompok1.R;

import java.util.List;

public class AdapterSpinnerTrs1 extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    List<DataTrs1> item;

    public AdapterSpinnerTrs1(Activity activity, List<DataTrs1> item){
        this.activity = activity;
        this.item = item;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int i) {
        return item.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (inflater == null){
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (view == null){
            view = inflater.inflate(R.layout.row_spinner_trs1, null);
        }

        TextView tv_waktu = view.findViewById(R.id.waktutrs1);

        DataTrs1 dataTrs1;
        dataTrs1 = item.get(position);

        tv_waktu.setText(dataTrs1.getWaktu());

        return view;
    }
}
