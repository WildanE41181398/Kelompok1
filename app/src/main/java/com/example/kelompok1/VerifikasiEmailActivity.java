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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kelompok1.Helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class VerifikasiEmailActivity extends AppCompatActivity {

    EditText etKodeVerif;
    Button btnKirim;
    TextView tvYourEmail, tvVerifResend;
    RequestQueue requestQueue;
    String tmpKodeVerif;
    ProgressDialog progressDialog;
    String tmpEmail, BaseUrl;
    Boolean CheckEditText;
    String IdUser = "USR000000000001";
    ProgressBar loading;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifikasi_email);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Verifikasi Email");
        etKodeVerif = findViewById(R.id.et_kodeverif);
        btnKirim = findViewById(R.id.btn_verifakun_kirim);
        tvYourEmail = findViewById(R.id.tv_verifakun_youremail);
        tvVerifResend = findViewById(R.id.tv_verifakun_resend);
        requestQueue = Volley.newRequestQueue(VerifikasiEmailActivity.this);
        progressDialog = new ProgressDialog(VerifikasiEmailActivity.this);
        loading = findViewById(R.id.loading);
        BaseUrl = SessionManager.BASE_URL;
        IdUser = getIntent().getStringExtra("id_user");

        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckEditTextIsEmpty();

                if (CheckEditText){
                    MatchingEmail();
                }else{
                    Toast.makeText(VerifikasiEmailActivity.this, "Mohon isi kode verifikasi dengan benar", Toast.LENGTH_LONG).show();
                }
            }
        });

        tvVerifResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResendEmail();
            }
        });

        GetEmail();

    }

    private void MatchingEmail(){
        loading.setVisibility(View.VISIBLE);
        btnKirim.setVisibility(View.GONE);

        String URL_MATCHING = BaseUrl + "api/verifakun/matchcode/";
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

                                    Intent intent = new Intent(VerifikasiEmailActivity.this, EmailVerifiedActivity.class);
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
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loading.setVisibility(View.GONE);
                            btnKirim.setVisibility(View.VISIBLE);
                            Toast.makeText(VerifikasiEmailActivity.this, "Error" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        btnKirim.setVisibility(View.VISIBLE);
                        Toast.makeText(VerifikasiEmailActivity.this, "Error" + error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", tmpEmail);
                params.put("token", tmpKodeVerif);
                params.put("Content-Type", "application/x-www-form-urlencoded");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void GetEmail(){
        loading.setVisibility(View.VISIBLE);
        btnKirim.setVisibility(View.GONE);

        String URL_CHECK_EMAIL = BaseUrl + "api/verifakun/checkemail/";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHECK_EMAIL,
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
                                    String email = object.getString("email").trim();

                                    tvYourEmail.setText(email);
                                    tmpEmail = email;
                                    loading.setVisibility(View.GONE);
                                    btnKirim.setVisibility(View.VISIBLE);
                                }
                            } else {
                                loading.setVisibility(View.GONE);
                                btnKirim.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loading.setVisibility(View.GONE);
                            btnKirim.setVisibility(View.VISIBLE);
                            Toast.makeText(VerifikasiEmailActivity.this, "Error" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        btnKirim.setVisibility(View.VISIBLE);
                        Toast.makeText(VerifikasiEmailActivity.this, "Error" + error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", IdUser);
                params.put("Content-Type", "application/x-www-form-urlencoded");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void  ResendEmail(){
        loading.setVisibility(View.VISIBLE);
        tvVerifResend.setVisibility(View.GONE);

        String URL_RESEND_EMAIL = BaseUrl + "api/verifakun/resendemail/";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_RESEND_EMAIL,
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
                                    String email = object.getString("email").trim();

                                    loading.setVisibility(View.GONE);
                                    tvVerifResend.setVisibility(View.VISIBLE);

                                    Toast.makeText(VerifikasiEmailActivity.this, "Kode verifikasi telah dikirimkan kepada " + email, Toast.LENGTH_LONG).show();
                                }
                            } else {
                                loading.setVisibility(View.GONE);
                                tvVerifResend.setVisibility(View.VISIBLE);
                                Toast.makeText(VerifikasiEmailActivity.this, message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loading.setVisibility(View.GONE);
                            tvVerifResend.setVisibility(View.VISIBLE);
                            Toast.makeText(VerifikasiEmailActivity.this, "Error" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        tvVerifResend.setVisibility(View.VISIBLE);
                        Toast.makeText(VerifikasiEmailActivity.this, "Error" + error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", tmpEmail);
                params.put("Content-Type", "application/x-www-form-urlencoded");

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20), //After the set time elapses the request will timeout
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
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
