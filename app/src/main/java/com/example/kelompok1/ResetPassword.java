package com.example.kelompok1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


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

public class ResetPassword extends AppCompatActivity {

    private TextView tv_strength;
    private EditText et_pass1, et_pass2;
    private Button btnReset;
    String tmpPass1, tmpPass2, id_user, password, BaseUrl;
    Boolean CheckEditText;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Reset Password");
        id_user = getIntent().getStringExtra("IdUserTAG");

        et_pass1 = findViewById(R.id.et_resetpassword_1);
        et_pass2 = findViewById(R.id.et_resetpassword_2);
        btnReset = findViewById(R.id.btn_resetpassword);
        BaseUrl = SessionManager.BASE_URL;

        CekPasswordSaatIni();

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ResetPassword.this);

                // Set a title for alert dialog
                builder.setTitle("Perhatian");

                // Ask the final question
                builder.setMessage("Apakah anda yakin dengan password ini?");

                // Set the alert dialog yes button click listener
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when user clicked the Yes button
                        CheckEditTextIsEmpty();
                        if (CheckEditText){
                            if (tmpPass1.equals(tmpPass2)){
                                if (tmpPass1.length() > 8){
                                    if (!tmpPass1.equals(password)){
                                        ResetPassword();
                                    }else{
                                        Toast.makeText(ResetPassword.this, "Password tidak boleh sama dengan password sebelumnya", Toast.LENGTH_LONG).show();
                                    }
                                }else{
                                    Toast.makeText(ResetPassword.this, "Password harus lebih dari 8 digit", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(ResetPassword.this, "Password yang anda masukkan tidak cocok.", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(ResetPassword.this, "Mohon isi semua kolom Password", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                // Set the alert dialog no button click listener
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when No button clicked
//                        Toast.makeText(getApplicationContext(),"No Button Clicked",Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                // Display the alert dialog on interface
                dialog.show();

            }
        });
    }

    private void  ResetPassword(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating...");
        progressDialog.show();

        String URL_UPDATE_STATUS = BaseUrl + "api/send_gmail/resetpassword";
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL_UPDATE_STATUS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");

                            if (message.equals("success")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    password = object.getString("password").trim();
                                }
                            } else {
                                Toast.makeText(ResetPassword.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ResetPassword.this, "Error" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(ResetPassword.this, "Error" + error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", id_user);
                params.put("password", tmpPass1);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void  CekPasswordSaatIni(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mengirim ulang ...");
        progressDialog.show();

        String URL_RESEND_EMAIL = BaseUrl + "api/send_gmail/getuserbyid";
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
                                    password = object.getString("password").trim();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ResetPassword.this, "Error" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(ResetPassword.this, "Error" + error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", id_user);
                params.put("Content-Type", "application/x-www-form-urlencoded");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void CheckEditTextIsEmpty(){

        tmpPass1 = et_pass1.getText().toString().trim();
        tmpPass2 = et_pass2.getText().toString().trim();

        if (TextUtils.isEmpty(tmpPass1) || TextUtils.isEmpty(tmpPass2)){
            CheckEditText = false;
        }else{
            CheckEditText = true;
        }
    }

}
