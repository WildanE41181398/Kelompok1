package com.example.kelompok1.ui.promosi;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kelompok1.Helper.SessionManager;
import com.example.kelompok1.R;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DetailPromosi extends AppCompatActivity {

    String id_promo, BaseUrl;
    TextView tv_desc, tv_syarat, tv_awal, tv_akhir, tv_kode;
    ImageView iv_promosi;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_promosi);
        id_promo = getIntent().getStringExtra("id");

        tv_desc = findViewById(R.id.tv_desc_body);
        tv_syarat = findViewById(R.id.tv_syarat_body);
        tv_awal = findViewById(R.id.tv_awal);
        tv_akhir = findViewById(R.id.tv_akhir);
        iv_promosi = findViewById(R.id.iv_detail_promosi);
        BaseUrl = SessionManager.BASE_URL;

        getPromosibyId();

    }

    public void getPromosibyId(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Memuat ...");
        progressDialog.show();

            String URL_CHECK_EMAIL = BaseUrl + "api/promosi/getidbyname/";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHECK_EMAIL,
                    new Response.Listener<String>() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @SuppressLint("ResourceAsColor")
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String message = jsonObject.getString("message");
                                progressDialog.dismiss();

                                if (message.equals("success")) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        String strNama = object.getString("judul_promo").trim();
                                        String strDesc = object.getString("deskripsi").trim();
                                        String strSyarat = object.getString("syarat_ketentuan").trim();
                                        String strAwal = object.getString("awal").trim();
                                        String strAkhir = object.getString("akhir").trim();
                                        String strStatus = object.getString("status").trim();
                                        String strGambar = object.getString("gambar").trim();

                                        Objects.requireNonNull(getSupportActionBar()).setTitle(strNama);
                                        tv_desc.setText(strDesc);
                                        tv_awal.setText("Awal Periode : " + strAwal.substring(0, 10));
                                        tv_akhir.setText("Akhir Periode : " + strAkhir.substring(0, 10));
                                        Picasso.with(DetailPromosi.this).load(BaseUrl + "assets/files/gambar_promo/" + strGambar).into(iv_promosi);

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                            tv_syarat.setText(Html.fromHtml(strSyarat, Html.FROM_HTML_MODE_LEGACY));
                                        } else {
                                            tv_syarat.setText(Html.fromHtml(strSyarat));
                                        }

                                    }
                                } else {
                                    Toast.makeText(DetailPromosi.this, "Tidak dapat memuat data", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                                Toast.makeText(DetailPromosi.this, "Error" + e.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(DetailPromosi.this, "Error Response" + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    })
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("id", id_promo);
                    params.put("Content-Type", "application/x-www-form-urlencoded");

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
    }
}
