package com.example.kelompok1.ui.notifications;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddMessages extends AppCompatActivity {

    EditText subjek, body;
    Button btn_kirim;
    String tmpSubjek, tmpBody, IdUser, BaseUrl;
    Boolean CheckET;
    SessionManager sessionManager;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_messages);

        subjek = findViewById(R.id.et_subjek);
        body = findViewById(R.id.et_body);
        btn_kirim = findViewById(R.id.btn_kirim_message);
        sessionManager = new SessionManager(this);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Tambahkan Pesan/Masukan");
        BaseUrl = SessionManager.BASE_URL;

        HashMap<String, String> user = sessionManager.getUserDetail();

        IdUser = user.get(SessionManager.ID);

        btn_kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddMessages.this);

                builder.setTitle("Perhatian");
                builder.setMessage("Apakah anda yakin ingin mengirimkan pesan/masukan diatas?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CheckIsEmpty();
                        if (CheckET){
                            KirimPesan();
                        }else{
                            Toast.makeText(getBaseContext(), "Mohon Isi semua field yang tersedia", Toast.LENGTH_LONG).show();
                        }
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


    }

    private void KirimPesan(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String URL_API = BaseUrl + "api/messages/sendmessage";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");

                            if (message.equalsIgnoreCase("success")) {
                                Toast.makeText(getBaseContext(), "Pesan berhasil dikirim!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getBaseContext(), BerandaOrenz.class);
                                intent.putExtra("NAVIGATION", "NOTIFIKASI");
                                startActivity(intent);
                            } else {
                                Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getBaseContext(), "Error Response" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getBaseContext(), "Error Response" + error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", IdUser);
                params.put("subjek", tmpSubjek);
                params.put("body", tmpBody);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void CheckIsEmpty(){

        tmpSubjek = subjek.getText().toString().trim();
        tmpBody = body.getText().toString().trim();

        if (TextUtils.isEmpty(tmpBody) || TextUtils.isEmpty(tmpSubjek)){
            CheckET = false;
        }else{
            CheckET = true;
        }

    }
}
