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
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    String tmpEmail;
    Boolean CheckEditText;
    ProgressBar loading;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupakatasandi);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Lupa Kata Sandi");
        EditTextEmail = findViewById(R.id.et_lupakatasandi_search);
        btnKirim = findViewById(R.id.btn_lupapassword_kirim);
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
                    Toast.makeText(LupakatasandiActivity.this, "Mohon isi Email atau Username untuk mencari akun anda", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void MatchingEmail(){
        loading.setVisibility(View.VISIBLE);
        btnKirim.setVisibility(View.GONE);

        String URL_MATCHING = "http://192.168.5.145/kelompok1_tif_d/OrenzLaundry/api/send_gmail/checkuser";
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
                                    String email = object.getString("email").trim();

                                    Intent intent = new Intent(LupakatasandiActivity.this, ForgotpasswordActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.putExtra("EmailUserTAG", email);
                                    startActivity(intent);
                                    loading.setVisibility(View.GONE);
                                    btnKirim.setVisibility(View.VISIBLE);
                                }
                            } else {
                                loading.setVisibility(View.GONE);
                                btnKirim.setVisibility(View.VISIBLE);
                                Toast.makeText(LupakatasandiActivity.this, message, Toast.LENGTH_LONG).show();
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
                params.put("search", tmpEmail);
                params.put("Content-Type", "application/x-www-form-urlencoded");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void CheckEditTextIsEmpty(){

        tmpEmail = EditTextEmail.getText().toString().trim();

        if (TextUtils.isEmpty(tmpEmail)){
            CheckEditText = false;
        }else{
            CheckEditText = true;
        }
    }
}
