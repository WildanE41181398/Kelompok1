package com.example.kelompok1.ui.notifications;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kelompok1.BerandaOrenz;
import com.example.kelompok1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DetailNotification extends AppCompatActivity {

    private List<ModelNotification> listNotifications = new ArrayList<>();
    TextView id_trs, tgl_trs, tgl_jemput, tgl_antar, total_bayar, catatan, judul;
    RecyclerView recyclerView;
    Button btn_ok, btn_hapus;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_notification);

        id = getIntent().getStringExtra("id");

        id_trs = findViewById(R.id.tv_right_id);
        tgl_antar = findViewById(R.id.tv_right_tglantar);
        tgl_jemput = findViewById(R.id.tv_right_tgljemput);
        tgl_trs = findViewById(R.id.tv_datenotification);
        total_bayar = findViewById(R.id.tv_right_total);
        catatan = findViewById(R.id.tv_right_catatan);
        judul = findViewById(R.id.tv_judulnotification);

        recyclerView = findViewById(R.id.rv_detailvalue);

        btn_hapus = findViewById(R.id.btn_deletenotification);
        btn_ok = findViewById(R.id.btn_oknotification);

        getNotificationId();

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteNotification();
            }
        });
    }

    public void deleteNotification(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Memuat ...");
        progressDialog.show();

        String URL_CHECK_EMAIL = "http://192.168.5.145/kelompok1_tif_d/OrenzLaundry/api/messages/deletenotificationid";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHECK_EMAIL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            progressDialog.dismiss();

                            if (message.equals("success")) {
                                Toast.makeText(getBaseContext(), "Notifikasi berhasil dihapus!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getBaseContext(), BerandaOrenz.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(DetailNotification.this, "Tidak dapat memuat data", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(DetailNotification.this, "Error" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(DetailNotification.this, "Error Response" + error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("Content-Type", "application/x-www-form-urlencoded");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getNotificationId(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Memuat ...");
        progressDialog.show();

        String URL_CHECK_EMAIL = "http://192.168.5.145/kelompok1_tif_d/OrenzLaundry/api/messages/getnotificationid";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHECK_EMAIL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            progressDialog.dismiss();

                            if (message.equals("success")) {
                                JSONArray jsonArrayTrs = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArrayTrs.length(); i++) {
                                    JSONObject object = jsonArrayTrs.getJSONObject(i);
                                    String strIdTrs = object.getString("id_transaksi").trim();
                                    String strTglTrs = object.getString("tgl_transaksi").trim();
                                    String strTglAntar = object.getString("tgl_antar").trim();
                                    String strTglJemput = object.getString("tgl_jemput").trim();
                                    String strStatus = object.getString("status").trim();
                                    String strCatatan = object.getString("catatan").trim();
                                    String strTotal = object.getString("total_harga").trim();

                                    Objects.requireNonNull(getSupportActionBar()).setTitle("Detail Transaksi");
                                    id_trs.setText(": " + strIdTrs);
                                    tgl_trs.setText(strTglTrs.substring(0, 10));
                                    tgl_antar.setText(": " + strTglAntar.substring(0, 10));
                                    tgl_jemput.setText(": " + strTglJemput.substring(0, 10));
                                    total_bayar.setText(": " + strTotal);
                                    if (strCatatan.isEmpty()){
                                        catatan.setText(": -");
                                    }else{
                                        catatan.setText(": " + strCatatan);
                                    }

                                    String strJudul = "";
                                    switch (strStatus){
                                        case "0":
                                            strJudul = "Pesanan anda berhasil dibuat! Mohon tunggu untuk konfirmasi penjemputan";
                                            break;
                                        case "1":
                                            strJudul = "Kami akan menjemput cucian ke rumah anda!";
                                            break;
                                    }

                                    judul.setText(strJudul);

                                }

                                JSONArray jsonArrayDtl = jsonObject.getJSONArray("detail");
                                for (int j = 0; j < jsonArrayDtl.length(); j++){
                                    JSONObject objectjson = jsonArrayDtl.getJSONObject(j);
                                    String strNama = objectjson.getString("nama_paket").trim();
                                    String strBerat = objectjson.getString("berat").trim();
                                    String strSubTotal = objectjson.getString("sub_total").trim();

                                    ModelNotification modelNotification = new ModelNotification();
                                    modelNotification.setDtl_nama(strNama);
                                    modelNotification.setDtl_berat(strBerat);
                                    modelNotification.setDtl_subtotal(strSubTotal);
                                    listNotifications.add(modelNotification);
                                }

                                setupRecyclerView(listNotifications);

                            } else {
                                Toast.makeText(DetailNotification.this, "Tidak dapat memuat data", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(DetailNotification.this, "Error" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(DetailNotification.this, "Error Response" + error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("Content-Type", "application/x-www-form-urlencoded");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void setupRecyclerView(List<ModelNotification> listNotifications){
        RecyclerViewDetailNotificationAdapter myAdapter = new RecyclerViewDetailNotificationAdapter(DetailNotification.this, listNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(DetailNotification.this));
        recyclerView.setAdapter(myAdapter);
    }
}
