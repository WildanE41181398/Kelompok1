package com.example.kelompok1.ui.notifications;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DetailMessage extends AppCompatActivity {

    String id;
    TextView tv_judul, tv_body, tv_date;
    Button btn_ok, btn_hapus;
    ProgressDialog progressDialog;
    StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_message);

        id = getIntent().getStringExtra("id");

        tv_judul = findViewById(R.id.tv_judulmessage);
        tv_body = findViewById(R.id.tv_bodymessage);
        tv_date = findViewById(R.id.tv_datemessage);
        btn_ok = findViewById(R.id.btn_okmessage);
        btn_hapus = findViewById(R.id.btn_deletemessage);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tv_body.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        }

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailMessage.this);

                builder.setTitle("Perhatian");
                builder.setMessage("Apakah anda yakin ingin menghapus pesan ini?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteMessage();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog dialog = builder.create();
                // Display the alert dialog on interface
                dialog.show();
            }
        });

        getPromosibyId();
    }

    public void deleteMessage(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Memuat ...");
        progressDialog.show();

        String URL_CHECK_EMAIL = "http://192.168.5.145/kelompok1_tif_d/OrenzLaundry/api/messages/deletemessageid";
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
//                                    JSONObject object = jsonArray.getJSONObject(i);
//                                    String strSubjek = object.getString("subjek_pesan").trim();
//                                    String strBody = object.getString("isi_pesan").trim();
//
//                                    Objects.requireNonNull(getSupportActionBar()).setTitle(strSubjek);
//                                    tv_judul.setText(strSubjek);
//                                    tv_body.setText(strBody);
                                Toast.makeText(getBaseContext(), "Pesan berhasil dihapus!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getBaseContext(), BerandaOrenz.class);
                                intent.putExtra("NAVIGATION", "NOTIFIKASI");
                                startActivity(intent);
                            } else {
                                Toast.makeText(DetailMessage.this, "Tidak dapat memuat data", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(DetailMessage.this, "Error" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(DetailMessage.this, "Error Response" + error.toString(), Toast.LENGTH_LONG).show();
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

    public void getPromosibyId(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Memuat ...");
        progressDialog.show();

        String URL_CHECK_EMAIL = "http://192.168.5.145/kelompok1_tif_d/OrenzLaundry/api/messages/getmessageid";
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
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String strSubjek = object.getString("subjek_pesan").trim();
                                    String strBody = object.getString("isi_pesan").trim();

                                    Objects.requireNonNull(getSupportActionBar()).setTitle(strSubjek);
                                    tv_judul.setText(strSubjek);
                                    tv_body.setText(strBody);
                                }
                            } else {
                                Toast.makeText(DetailMessage.this, "Tidak dapat memuat data", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(DetailMessage.this, "Error" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(DetailMessage.this, "Error Response" + error.toString(), Toast.LENGTH_LONG).show();
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

}
