package com.example.kelompok1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ForgotSendCode extends AppCompatActivity {

    Button btnKirim;
    EditText etKodeVerif;
    TextView tvYourEmail, tvVerifResend;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    ProgressBar loading;
    Boolean CheckEditText;
    String email, tmpKodeVerif;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_send_code);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Lupa Kata Sandi");
        etKodeVerif = findViewById(R.id.et_kodeverif);
        btnKirim = findViewById(R.id.btn_verifakun_kirim);
        tvYourEmail = findViewById(R.id.tv_verifakun_youremail);
        tvVerifResend = findViewById(R.id.tv_verifakun_resend);
        loading = findViewById(R.id.loading);
        requestQueue = Volley.newRequestQueue(ForgotSendCode.this);
        progressDialog = new ProgressDialog(ForgotSendCode.this);

        email = getIntent().getStringExtra("EmailUserTAG");
        tvYourEmail.setText(email);

        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckEditTextIsEmpty();

                if (CheckEditText){
                    MatchingEmail();
                }else{
                    Toast.makeText(ForgotSendCode.this, "Mohon isi kode verifikasi dengan benar", Toast.LENGTH_LONG).show();
                }
            }
        });

        tvVerifResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResendEmail();
            }
        });
    }

    private void MatchingEmail(){
        loading.setVisibility(View.VISIBLE);
        btnKirim.setVisibility(View.GONE);

        String URL_MATCHING = "http://192.168.5.145/kelompok1_tif_d/OrenzLaundry/api/send_gmail/checktoken";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_MATCHING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");

                            if (message.equals("success")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id_user = object.getString("id_user").trim();

                                    Intent intent = new Intent(ForgotSendCode.this, ResetPassword.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.putExtra("IdUserTAG", id_user);
                                    startActivity(intent);
                                    loading.setVisibility(View.GONE);
                                    btnKirim.setVisibility(View.VISIBLE);
                                }
                            } else {
                                loading.setVisibility(View.GONE);
                                btnKirim.setVisibility(View.VISIBLE);
                                Toast.makeText(ForgotSendCode.this, message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loading.setVisibility(View.GONE);
                            btnKirim.setVisibility(View.VISIBLE);
                            Toast.makeText(ForgotSendCode.this, "Error" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        btnKirim.setVisibility(View.VISIBLE);
                        Toast.makeText(ForgotSendCode.this, "Error" + error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("token", tmpKodeVerif);
                params.put("Content-Type", "application/x-www-form-urlencoded");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void  ResendEmail(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mengirim ulang ...");
        progressDialog.show();

        String URL_RESEND_EMAIL = "http://192.168.5.145/kelompok1_tif_d/OrenzLaundry/api/verifakun/resendemail/";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_RESEND_EMAIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");

                            if (message.equals("success")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String email = object.getString("email").trim();

                                    Toast.makeText(ForgotSendCode.this, "Kode verifikasi telah dikirimkan kepada " + email, Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ForgotSendCode.this, "Error" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(ForgotSendCode.this, "Error" + error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("Content-Type", "application/x-www-form-urlencoded");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void CheckEditTextIsEmpty(){

        tmpKodeVerif = etKodeVerif.getText().toString().trim();

        if (TextUtils.isEmpty(tmpKodeVerif)){
            CheckEditText = false;
        }else{
            CheckEditText = true;
        }
    }
}
