package com.example.kelompok1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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

public class EmailVerifiedActivity extends AppCompatActivity {

    Button btnHome;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    String id_user, BaseUrl;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verified);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Verifikasi Email");
        btnHome = findViewById(R.id.btnEmailVerified_home);
        requestQueue = Volley.newRequestQueue(EmailVerifiedActivity.this);
        progressDialog = new ProgressDialog(EmailVerifiedActivity.this);
        BaseUrl = SessionManager.BASE_URL;

         id_user = getIntent().getStringExtra("IdUserTAG");

         UpdateStatus();

         btnHome.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

             }
         });
    }

    private void  UpdateStatus(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating...");
        progressDialog.show();

        String URL_UPDATE_STATUS = BaseUrl + "api/verifakun/updatestatus/";
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL_UPDATE_STATUS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String error = jsonObject.getString("error");
                            String message = jsonObject.getString("message");

                            if (status.equals("200") && error.equals("false")) {
                                Toast.makeText(EmailVerifiedActivity.this, message, Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(EmailVerifiedActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EmailVerifiedActivity.this, "Error" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(EmailVerifiedActivity.this, "Error" + error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", id_user);
                params.put("status", "1");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
