package com.example.kelompok1.ui.notifications;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.example.kelompok1.Helper.SessionManager;
import com.example.kelompok1.R;
import com.example.kelompok1.TransaksiTahap2;

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
    Button btn_ok, btn_hapus, btn_konfirmasi, btn_antar, btn_batal;
    String id, id_paket, BaseUrl, statusTrs;

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
        BaseUrl = SessionManager.BASE_URL;

        recyclerView = findViewById(R.id.rv_detailvalue);

        btn_hapus = findViewById(R.id.btn_deletenotification);
        btn_ok = findViewById(R.id.btn_oknotification);
        btn_konfirmasi = findViewById(R.id.btn_konfirmasi);
        btn_antar = findViewById(R.id.btn_antar);
        btn_batal = findViewById(R.id.btn_batalnotification);

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
                deleteNotification(BaseUrl + "api/messages/deletenotificationid");
            }
        });

        btn_konfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTrsProsesPesanan();
            }
        });

        btn_antar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailNotification.this, TransaksiTahap2.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("id_transaksi", id);
                intent.putExtra("id_paket", id_paket);
                startActivity(intent);
            }
        });

        btn_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailNotification.this);
                builder.setTitle("Perhatian");
                builder.setMessage("Apakah anda yakin ingin membatalkan pesanan?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteNotification(BaseUrl + "api/transaksi/batalpesanan");
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    public void updateTrsProsesPesanan(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mengkonfirmasi ...");
        progressDialog.show();

        String URL_API = BaseUrl + "api/messages/updatekonfirmasiproses";
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equals("200")){
                                Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
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

    public void deleteNotification(String URL_API_PARAM){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Memuat ...");
        progressDialog.show();

        String URL_CHECK_EMAIL = URL_API_PARAM;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHECK_EMAIL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            progressDialog.dismiss();

                            if (status.equals("200")) {
                                Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getBaseContext(), BerandaOrenz.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra("NAVIGATION", "NOTIFIKASI");
                                startActivity(intent);
                            } else {
                                Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
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

        String URL_CHECK_EMAIL = BaseUrl + "api/messages/getnotificationid";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHECK_EMAIL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            int tglantarcheck = 0;
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
                                    statusTrs = strStatus;

                                    tglantarcheck = strTglAntar.length();
                                    Objects.requireNonNull(getSupportActionBar()).setTitle("Detail Transaksi");
                                    id_trs.setText(": " + strIdTrs);
                                    tgl_trs.setText(strTglTrs.substring(0, 10));
                                    if (strTglAntar.length() > 10) {
                                        tgl_antar.setText(": " + strTglAntar.substring(0, 10));
                                    }else{
                                        tgl_antar.setText(": - " );
                                    }

                                    if (strTglJemput.length() > 10){
                                        tgl_jemput.setText(": " + strTglJemput.substring(0, 10));
                                    }else{
                                        tgl_antar.setText(": - " );
                                    }

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
                                        case "2":
                                            strJudul = "Berikut detail pesanan/cucian yang telah kami timbang dan sortir. \n Mohon konfirmasi untuk melanjutkan proses pesanan. dengan melakukan konfirmasi, berarti anda setuju dengan tagihan harga yang tertera dibawah ini.";
                                            break;
                                        case "3":
                                            strJudul = "Pesanan sedang diproses! Mohon tunggu update terbaru ketika cucian telah siap diantarkan.";
                                            break;
                                        case "4":
                                            strJudul = "Pesanan/cuician anda siap diantarkan, mohon atur lokasi antar untuk melanjutkan. \n Mohon siapkan biaya tagihan juga ya, kami akan melakukan pembayaran sembari mengantar cucian.";
                                            break;
                                        case "5":
                                            strJudul = "Pesanan/cucian selesai diantarkan, sampai jumpa lagi.";
                                            break;
                                        case "6":
                                            strJudul = "Sayangnya pesanan dibatalkan. mohon beri kami kritik dan saran pada menu 'Pesan'";
                                            break;
                                    }

                                    judul.setText(strJudul);

                                }

                                JSONArray jsonArrayDtl = jsonObject.getJSONArray("detail");
                                for (int j = 0; j < jsonArrayDtl.length(); j++){
                                    JSONObject objectjson = jsonArrayDtl.getJSONObject(j);
                                    id_paket = objectjson.getString("id_paket").trim();
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
                                if(statusTrs!=null){
                                    switch (statusTrs){
                                        case "2":
                                            btn_konfirmasi.setVisibility(View.VISIBLE);
                                            btn_batal.setVisibility(View.VISIBLE);
                                            btn_antar.setVisibility(View.GONE);
                                            btn_hapus.setVisibility(View.GONE);
                                            break;
                                        case "4":
                                            btn_konfirmasi.setVisibility(View.GONE);
                                            btn_batal.setVisibility(View.GONE);
                                            btn_hapus.setVisibility(View.VISIBLE);
                                            btn_antar.setVisibility(View.VISIBLE);

                                            if (tglantarcheck > 10){
                                                btn_konfirmasi.setVisibility(View.GONE);
                                                btn_antar.setVisibility(View.GONE);
                                            }
                                            break;
                                    }
                                }

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
