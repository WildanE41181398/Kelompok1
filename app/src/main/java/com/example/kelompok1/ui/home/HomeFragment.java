package com.example.kelompok1.ui.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kelompok1.R;
import com.example.kelompok1.TopUp;
import com.example.kelompok1.ui.promosi.DetailPromosi;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private SliderLayout sliderLayout, caraKerja;
    private ArrayList<ModelHome>  listHomes;
    private CardView top_up, paket_reguler, paket_express, paket_satuan;
    private TextView tv_saldo;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;
    private String IdUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        sliderLayout = root.findViewById(R.id.imageSlider);
//        caraKerja = root.findViewById(R.id.caraKerja);
        top_up = root.findViewById(R.id.cv_top_up);
        paket_reguler = root.findViewById(R.id.cv_paket_reguler);
        paket_express = root.findViewById(R.id.cv_paket_express);
        paket_satuan = root.findViewById(R.id.cv_paket_satuan);
        tv_saldo = root.findViewById(R.id.tv_saldohome);

        IdUser = "USR00001";

        sliderLayout.setIndicatorAnimation(SliderLayout.Animations.THIN_WORM);
        listHomes = new ArrayList<>();
        sliderLayout.setScrollTimeInSec(3);

        SliderView sliderView = new SliderView(getContext());

        getDataUser();
        getGambarPromosi();

        top_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TopUp.class);
                startActivity(intent);
            }
        });

        paket_reguler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Aksi pada Paket Reguler", Toast.LENGTH_LONG).show();
            }
        });

        paket_express.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Aksi pada Paket Express", Toast.LENGTH_LONG).show();
            }
        });

        paket_satuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Aksi pada Paket Satuan", Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }



    private void setupdata(List<ModelHome> list){
        for (int i=0; i<4; i++){
            final ModelHome home = list.get(i);
            SliderView sliderView = new SliderView(getContext());
            sliderView.setImageUrl("http://192.168.5.145/kelompok1_tif_d/OrenzLaundry/assets/files/gambar_promo/" + home.getGambarpromosi());

            sliderView.setImageScaleType(ImageView.ScaleType.FIT_XY);
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                    Intent intent = new Intent(getContext(), DetailPromosi.class);
                    intent.putExtra("id", home.getId());
                    startActivity(intent);
                }
            });
            final int finalI = i;
            sliderLayout.addSliderView(sliderView);
        }
    }

    private void getDataUser(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading ...");
        progressDialog.show();

        String URL_GETUSER = "http://192.168.5.145/kelompok1_tif_d/OrenzLaundry/api/home/getuser";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GETUSER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (status.equals("200")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String saldo = object.getString("token").trim();
                                    tv_saldo.setText("Saldo : Rp. " + saldo);

                                }
                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error " + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error Response " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", IdUser);
                params.put("Content-Type", "application/x-www-form-urlencoded");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    private void getGambarPromosi(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading ...");
        progressDialog.show();

        String URL_PROMOSI = "http://192.168.5.145/kelompok1_tif_d/OrenzLaundry/api/home/getgambarpromosi";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PROMOSI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equals("200")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i=0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String strId = object.getString("id_promo").trim();
                                    String strJudul = object.getString("judul_promo").trim();
                                    String strGambar = object.getString("gambar").trim();

                                    ModelHome home = new ModelHome();
                                    home.setId(strId);
                                    home.setJudul(strJudul);
                                    home.setGambarpromosi(strGambar);
                                    listHomes.add(home);
                                    }
                                setupdata(listHomes);
                            }else{
                                Toast.makeText(getContext(), "Tidak dapat memuat data", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Error Response " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    private void getPaket(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading ...");
        progressDialog.show();

        String URL_PROMOSI = "http://192.168.5.145/kelompok1_tif_d/OrenzLaundry/api/home/getgambarpromosi";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PROMOSI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equals("200")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i=0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String strId = object.getString("id_promo").trim();
                                    String strJudul = object.getString("judul_promo").trim();
                                    String strGambar = object.getString("gambar").trim();

                                    ModelHome home = new ModelHome();
                                    home.setId(strId);
                                    home.setJudul(strJudul);
                                    home.setGambarpromosi(strGambar);
                                    listHomes.add(home);
                                }
                                setupdata(listHomes);
                            }else{
                                Toast.makeText(getContext(), "Tidak dapat memuat data", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Error Response " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

}
