package com.example.kelompok1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kelompok1.Helper.SessionManager;
import com.example.kelompok1.adaptermodel.AdapterSpinnerTrs1;
import com.example.kelompok1.adaptermodel.DataTrs1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class TransaksiTahap2 extends AppCompatActivity {

    TextView tvDate, tvPaket, tvLokasi;
    Button btnDate, btnSubmit;
    Calendar myCalendar;
    Spinner spinnerWaktu;
    ProgressDialog progressDialog;
    AdapterSpinnerTrs1 adapter;
    String BaseUrl;
    String id_paket, id_transaksi, id_user, latIntent, langIntent, tmpAlamat, tglAntar, waktuAntar;
    List<DataTrs1> listWaktu = new ArrayList<DataTrs1>();
    SessionManager sessionManager;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_tahap2);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Transaksi");
        tvLokasi = findViewById(R.id.tv_lokasitrs2);
        tvPaket = findViewById(R.id.tv_pakettrs2);
        tvDate = findViewById(R.id.tv_datetrs2);
        btnDate = findViewById(R.id.btn_datetrs2);
        btnSubmit = findViewById(R.id.btn_trs2);
        myCalendar = Calendar.getInstance();
        spinnerWaktu = findViewById(R.id.spinnerTrs2);
        BaseUrl = SessionManager.BASE_URL;
        progressDialog = new ProgressDialog(TransaksiTahap2.this);
        id_paket = "PKT000000000001";
        
        getNamaPaket();
        
        sessionManager = new SessionManager(getBaseContext());
        HashMap<String, String> user = sessionManager.getUserDetail();
        id_user = user.get(SessionManager.ID);

        if (getIntent().getStringExtra("id_paket") != null){
            id_paket = getIntent().getStringExtra("id_paket");
        }

        if (getIntent().getStringExtra("id_transaksi") != null){
            id_transaksi = getIntent().getStringExtra("id_transaksi");
        }

        if (getIntent().getStringExtra("latIntent") != null && getIntent().getStringExtra("langIntent") != null){
            latIntent = getIntent().getStringExtra("latIntent");
            langIntent = getIntent().getStringExtra("langIntent");
            tmpAlamat = getIntent().getStringExtra("alamat");

//            Toast.makeText(TransaksiTahap1.this, "Lat : " + latIntent + "\n Alamat : " + tmpAlamat, Toast.LENGTH_LONG).show();
        }
        
        tvLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TransaksiTahap2.this, LokasiAntar.class);
                intent.putExtra("id_paket", id_paket);
                intent.putExtra("id_transaksi", id_transaksi);

                if (getIntent().getStringExtra("latIntent") != null && getIntent().getStringExtra("langIntent") != null){
                    intent.putExtra("latIntent", latIntent);
                    intent.putExtra("langIntent", langIntent);
                    intent.putExtra("alamat", tmpAlamat);
                }
                startActivity(intent);
            }
        });
        
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(TransaksiTahap2.this, new DatePickerDialog.OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, month);
                        myCalendar.set(Calendar.DAY_OF_MONTH, day);

                        String formatTanggal = "dd MMMM yyyy";
                        SimpleDateFormat dbsdf = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat sdf = new SimpleDateFormat(formatTanggal);
                        tvDate.setText(sdf.format(myCalendar.getTime()));
                        tglAntar = dbsdf.format(myCalendar.getTime());

                        if (myCalendar.getTimeInMillis() < (System.currentTimeMillis() - 86401000)){
                            Toast.makeText(TransaksiTahap2.this, "Tanggal yang kamu pilih salah!", Toast.LENGTH_LONG).show();
                            myCalendar.setTimeInMillis(System.currentTimeMillis() - 1000);
                            tvDate.setText(sdf.format(myCalendar.getTime()));
                        }
                    }
                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(tmpAlamat) && !TextUtils.isEmpty(tglAntar)) {

                    updateTransaksi();
                }else{
                    Toast.makeText(TransaksiTahap2.this, "Mohon pilih lokasi atau pilih tanggal jemput anda terlebih dahulu!", Toast.LENGTH_LONG).show();
                }
            }
        });
        
        spinnerWaktu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                waktuAntar = listWaktu.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(TransaksiTahap2.this, "Pilih salah satu waktu jemput.", Toast.LENGTH_LONG).show();
            }
        });

        adapter = new AdapterSpinnerTrs1(TransaksiTahap2.this, listWaktu);
        spinnerWaktu.setAdapter(adapter);

        callData();
    }

    private void callData() {
        listWaktu.clear();

        progressDialog.setMessage("Loading...");
        progressDialog.show();
        showDialog();

        String URL_PROMOSI = BaseUrl + "api/transaksi";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PROMOSI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String error = jsonObject.getString("error");
                            if (status.equals("200")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i=0; i < jsonArray.length(); i++){

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    DataTrs1 dataTrs1 = new DataTrs1();

                                    dataTrs1.setId(object.getString("id"));
                                    dataTrs1.setWaktu(object.getString("waktu"));

                                    listWaktu.add(dataTrs1);
                                }
                                adapter.notifyDataSetChanged();
                                hideDialog();
                            }else{
                                Toast.makeText(TransaksiTahap2.this, "Tidak dapat memuat data", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TransaksiTahap2.this, "Error" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        hideDialog();
                        Toast.makeText(TransaksiTahap2.this, "Error Response " + error.toString(), Toast.LENGTH_LONG).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(TransaksiTahap2.this);
        requestQueue.add(stringRequest);
    }

    private void getNamaPaket() {
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(true);
        progressDialog.show();

        String URL_API = BaseUrl + "api/transaksi/getnamapaketmobile";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_API,
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

                                    tvPaket.setText("PAKET " + object.getString("nama_jenis_paket").toUpperCase());
                                }
                            }else{
                                Toast.makeText(TransaksiTahap2.this, "Tidak dapat memuat data", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TransaksiTahap2.this, "Error" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(TransaksiTahap2.this, "Error Response " + error.toString(), Toast.LENGTH_LONG).show();
                    }

                })
        {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("id_paket", id_paket);
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(TransaksiTahap2.this);
        requestQueue.add(stringRequest);
    }

    private void updateTransaksi() {
        progressDialog.setMessage("Loading ...");
        progressDialog.setCancelable(true);
        progressDialog.show();

        String URL_API = BaseUrl + "api/transaksi/updateantartrsmobile";
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equals("200")) {
                                Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(TransaksiTahap2.this, BerandaOrenz.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra("NAVIGATION", "NOTIFIKASI");
                                startActivity(intent);
                            }else{
                                Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TransaksiTahap2.this, "Error" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(TransaksiTahap2.this, "Error Response " + error.toString(), Toast.LENGTH_LONG).show();
                    }

                })
        {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("id_transaksi", id_transaksi);
                params.put("id_user", id_user);
                params.put("alamat", tmpAlamat);
                params.put("lat", latIntent);
                params.put("lang", langIntent);
                params.put("tgl_antar", tglAntar);
                params.put("waktu_antar", waktuAntar);
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(TransaksiTahap2.this);
        requestQueue.add(stringRequest);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }


    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
