package com.example.kelompok1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.kelompok1.fcm.OrenzFirebaseMessagingService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {

    TextView tvLogin;
    Button btnRegister;
    EditText etNama, etEmail, etPass, etRepass;
    String tmpNama, tmpEmail, tmpPass, tmpRepass, tokenDevice, BaseUrl;
    Boolean CheckisEmpty;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Register Account");

        tvLogin = findViewById(R.id.textLogin);
        etPass = findViewById(R.id.et_passwordregis);
        etRepass = findViewById(R.id.et_repasswordregis);
        etNama = findViewById(R.id.et_namaregis);
        etEmail = findViewById(R.id.et_emailregis);
        btnRegister = findViewById(R.id.btnRegister);

        BaseUrl = SessionManager.BASE_URL;
        requestQueue = Volley.newRequestQueue(this);
        progressDialog = new ProgressDialog(this);

        OrenzFirebaseMessagingService orenz = new OrenzFirebaseMessagingService();
        tokenDevice = orenz.onTokenSend();
        Log.d("TOKEN", "TOKEEEENNNNN = " + tokenDevice);

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckEditTextIsEmptyOrNot();

                if (CheckisEmpty){
                    if (tmpPass.equals(tmpRepass)){
                        if (tmpPass.length() > 8){
                            RegisterAPI();
                        }else{
                            Toast.makeText(RegisterActivity.this, "Password harus lebih dari 8 digit", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(RegisterActivity.this, "Password yang anda masukkan tidak cocok.", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(RegisterActivity.this, "Mohon isi semua kolom Password", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void RegisterAPI() {
        progressDialog.setMessage("Mohon Tunggu");
        progressDialog.show();
        btnRegister.setVisibility(View.GONE);

        String HttpUrl = BaseUrl + "api/home/register";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            String status = jsonObject.getString("status");

                            if (status.equals("200")){
                                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, Login.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                btnRegister.setVisibility(View.VISIBLE);
                                finish();
                            } else {
                                btnRegister.setVisibility(View.VISIBLE);
                                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                            btnRegister.setVisibility(View.VISIBLE);
                            Toast.makeText(RegisterActivity.this, "Error" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Error Response" + volleyError.toString(), Toast.LENGTH_LONG).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {

                //Creating map string params.
                Map<String, String> params = new HashMap<>();

                //Adding all values to params.
                //The first argument should be same your MySql database table columns.
                params.put("nama", tmpNama);
                params.put("email", tmpEmail);
                params.put("password", tmpPass);
                params.put("token_device", tokenDevice);
                params.put("Content-Type","application/x-www-form-urlencoded");

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

    public void CheckEditTextIsEmptyOrNot(){

        //Getting values from EditText.
        tmpNama = etNama.getText().toString().trim();
        tmpEmail = etEmail.getText().toString().trim();
        tmpPass = etPass.getText().toString().trim();
        tmpRepass = etRepass.getText().toString().trim();

        CheckisEmpty = !TextUtils.isEmpty(tmpNama) && !TextUtils.isEmpty(tmpEmail) && !TextUtils.isEmpty(tmpPass) && !TextUtils.isEmpty(tmpRepass);
    }
}
