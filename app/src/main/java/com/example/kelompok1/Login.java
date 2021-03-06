package com.example.kelompok1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.kelompok1.fcm.OrenzFirebaseMessagingService;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Login extends AppCompatActivity {

    EditText Email, Password;
    TextView LoginButton, FacebookButton, GmailButton, LupaButton, RegisterButton;
    RequestQueue requestQueue;
    String EmailHolder, PasswordHolder, BaseUrl, tokenDevice;
    ProgressDialog progressDialog;
    Boolean CheckEditText;
    SessionManager sessionManager;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Login Account");

        Email = findViewById(R.id.editText_Email);
        Password = findViewById(R.id.editText_Password);
        LoginButton = findViewById(R.id.button_login);
        FacebookButton = findViewById(R.id.tv_facebook);
        GmailButton = findViewById(R.id.tv_gmail);
        LupaButton = findViewById(R.id.tv_lupakata);
        RegisterButton = findViewById(R.id.tv_register);
        sessionManager = new SessionManager(this);
        requestQueue = Volley.newRequestQueue(Login.this);
        progressDialog = new ProgressDialog(Login.this);

        OrenzFirebaseMessagingService orenz = new OrenzFirebaseMessagingService();
        tokenDevice = orenz.onTokenSend();

        BaseUrl = SessionManager.BASE_URL;

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckEditTextIsEmptyOrNot();

                if (CheckEditText){
                    UserLogin();
                }else{
                    Toast.makeText(Login.this, "Mohon isi semua data", Toast.LENGTH_LONG).show();

                }

            }
        });

        LupaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, LupakatasandiActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }

    public void updateToken(final String id){
        progressDialog.setMessage("Mohon Tunggu memperbarui token..");
        progressDialog.show();

        String HttpUrl = BaseUrl + "api/home/updatetokendevice";
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equals("200")){
                                //Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(Login.this, "Error" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        progressDialog.dismiss();
                        Toast.makeText(Login.this, "Error Response" + volleyError.toString(), Toast.LENGTH_LONG).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("id_user", id);
                params.put("device_token", tokenDevice);
                params.put("Content-Type","application/x-www-form-urlencoded");

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void UserLogin(){

        progressDialog.setMessage("Mohon Tunggu");
        progressDialog.show();
        LoginButton.setVisibility(View.GONE);

        String HttpUrl = BaseUrl + "api/home/login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");

                            if (message.equals("success")){
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id = object.getString("id_user").trim();
                                    updateToken(id);
                                    sessionManager.createSession(id);


                                    Intent intent = new Intent(Login.this, BerandaOrenz.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    LoginButton.setVisibility(View.VISIBLE);
                                    finish();
                                }
                            } else {
                                LoginButton.setVisibility(View.VISIBLE);
                                Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                            LoginButton.setVisibility(View.VISIBLE);
                            Toast.makeText(Login.this, "Error" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        //Hiding the progress dialog after all task complete.
                        progressDialog.dismiss();

                        //Showing error message if something goes wrong.
                        Toast.makeText(Login.this, "Error Response" + volleyError.toString(), Toast.LENGTH_LONG).show();

                    }
                })
        {
                @Override
                protected Map<String, String> getParams() {

                    //Creating map string params.
                    Map<String, String> params = new HashMap<>();

                    //Adding all values to params.
                    //The first argument should be same your MySql database table columns.
                    params.put("email", EmailHolder);
                    params.put("password", PasswordHolder);
                    params.put("Content-Type","application/x-www-form-urlencoded");

                    return params;
                }

        };

        //Creating RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }

    public void CheckEditTextIsEmptyOrNot(){

        //Getting values from EditText.
        EmailHolder = Email.getText().toString().trim();
        PasswordHolder = Password.getText().toString().trim();

        //Checking wheter EditText value is empty or not.
        if (TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder))
        {

            //if any of EditText is empty then set variable value as False.
            CheckEditText = false;

        }else{
            //if any of EditText is empty then set variable value as false.
            CheckEditText = true;
        }
    }

    boolean doubleBackPressed = false;
    @Override
    public void onBackPressed(){
        if (doubleBackPressed){
            finish();
            System.exit(0);
        }else{
            final ConstraintLayout constraintLayout = findViewById(R.id.constrainLogin);
            Snackbar.make(constraintLayout, "Tekan tombol kembali sekali lagi", Snackbar.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackPressed=false;
                }
            }, 2000);
        }
    }



}


