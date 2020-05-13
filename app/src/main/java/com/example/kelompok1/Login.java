package com.example.kelompok1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    EditText Email, Password;
    Button LoginButton;
    RequestQueue requestQueue;
    String EmailHolder, PasswordHolder;
    ProgressDialog progressDialog;
    String HttpUrl = "http://192.168.1.64/login/user_login.php";
    Boolean CheckEditText;
    String TempServerResponseMatchedValue = "Data Sesuai";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Email = (EditText) findViewById(R.id.editText_Email);
        Password = (EditText) findViewById(R.id.editText_Password);
        LoginButton = (Button) findViewById(R.id.button_login);
        requestQueue = Volley.newRequestQueue(Login.this);
        progressDialog = new ProgressDialog(Login.this);
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

    }

    public void UserLogin(){

        progressDialog.setMessage("Mohon Tunggu");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        //Hidding the progress dialog after all task complete.
                        progressDialog.dismiss();

                        //Maching server response message to our text.
                        if (ServerResponse.equalsIgnoreCase("Data Sesuai")) {

                            //If response matched then show the toast.
                            Toast.makeText(Login.this, "Login Berhasil", Toast.LENGTH_LONG).show();

                            //Finish the current login activity.
                            finish();

                            //Opening beranda using intent.
                            Intent intent = new Intent(Login.this, Beranda.class);

                            //Sending user email to another activity using intent.
                            intent.putExtra("UserEmailTAG", EmailHolder);

                            startActivity(intent);
                        } else {
                            //showing echo response message coming from server.
                            Toast.makeText(Login.this, ServerResponse, Toast.LENGTH_LONG).show();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        //Hiding the progress dialog after all task complete.
                        progressDialog.dismiss();

                        //Showing error message if something goes wrong.
                        Toast.makeText(Login.this, volleyError.toString(), Toast.LENGTH_LONG).show();

                    }
                }){
                @Override
                protected Map<String, String> getParams() {

                    //Creating map string params.
                    Map<String, String> params = new HashMap<>();

                    //Adding all values to params.
                    //The first argument should be same your MySql database table columns.
                    params.put("email", EmailHolder);
                    params.put("password", PasswordHolder);

                    return params;
                }

        };

        //Creating RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);

        //Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }

    public void CheckEditTextIsEmptyOrNot(){

        //Getting values from EditText.
        EmailHolder = Email.getText().toString().trim();
        PasswordHolder = Password.getText().toString().trim();

        //Checking wheter EditText value is empty or not.
        if (TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder)){

            //if any of EditText is empty then set variable value as False.
            CheckEditText = false;

        }else{
            //if any of EditText is empty then set variable value as false.
            CheckEditText = true;
        }
    }





}


