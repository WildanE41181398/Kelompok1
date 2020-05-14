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

public class LupakatasandiActivity extends AppCompatActivity {

    EditText EditTextEmail;
    Button btnKirim;
    TextView TextView2, TextView3;
    RequestQueue requestQueue;
    String tmpKodeVerif;
    ProgressDialog progressDialog;
    String tmpEmail;
    Boolean CheckEditText;
    String IdUser = "USR00001";
    ProgressBar loading;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupakatasandi);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Lupa Kata Sandi");
        EditTextEmail = findViewById(R.id.EditTextEmail);
        btnKirim = findViewById(R.id.btn_lupapassword_kirim);
        TextView2 = findViewById(R.id.TextView2);
        TextView3 = findViewById(R.id.TextView3);
        requestQueue = Volley.newRequestQueue(LupakatasandiActivity.this);
        progressDialog = new ProgressDialog(LupakatasandiActivity.this);
        loading = findViewById(R.id.loading);

        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckEditTextIsEmpty();

                if (CheckEditText){
                    MatchingEmail();
                }else{
                    Toast.makeText(LupakatasandiActivity.this, "Mohon isi Password anda dengan benar", Toast.LENGTH_LONG).show();
                }
            }
        });

        TextView3.setOnClickListener(new View.OnClickListener() {
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

        String URL_MATCHING = "http://192.168.1.19/kelompok1_tif_d/OrenzLaundry/api/sendemail/matchcode/";
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

                                    Intent intent = new Intent(LupakatasandiActivity.this, ForgotpasswordActivity.class);
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
                            Toast.makeText(LupakatasandiActivity.this, "Error" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        btnKirim.setVisibility(View.VISIBLE);
                        Toast.makeText(LupakatasandiActivity.this, "Error" + error.toString(), Toast.LENGTH_LONG).show();
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

        String URL_CHECK_EMAIL = "http://192.168.1.19/kelompok1_tif_d/OrenzLaundry/api/sendemail/checkemail/";
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

                                    TextView2.setText(email);
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
                            Toast.makeText(LupakatasandiActivity.this, "Error" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        btnKirim.setVisibility(View.VISIBLE);
                        Toast.makeText(LupakatasandiActivity.this, "Error" + error.toString(), Toast.LENGTH_LONG).show();
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
        TextView3.setVisibility(View.GONE);

        String URL_RESEND_EMAIL = "http://192.168.1.19/kelompok1_tif_d/OrenzLaundry/api/sendemail/resendemail/";
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
                                    TextView3.setVisibility(View.VISIBLE);

                                    Toast.makeText(LupakatasandiActivity.this, "Kata Sandi telah dikirimkan kepada " + email, Toast.LENGTH_LONG).show();
                                }
                            } else {
                                loading.setVisibility(View.GONE);
                                TextView3.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loading.setVisibility(View.GONE);
                            TextView3.setVisibility(View.VISIBLE);
                            Toast.makeText(LupakatasandiActivity.this, "Error" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        TextView3.setVisibility(View.VISIBLE);
                        Toast.makeText(LupakatasandiActivity.this, "Error" + error.toString(), Toast.LENGTH_LONG).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void CheckEditTextIsEmpty(){

        tmpKodeVerif = EditTextEmail.getText().toString().trim();

        if (TextUtils.isEmpty(tmpKodeVerif)){
            CheckEditText = false;
        }else{
            CheckEditText = true;
        }
    }
}
