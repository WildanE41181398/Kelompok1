package com.example.kelompok1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kelompok1.Helper.ModelPaketReguler;
import com.example.kelompok1.Helper.ModelPaketSatuan;
import com.example.kelompok1.Helper.RvPaketRegulerAdapter;
import com.example.kelompok1.Helper.RvPaketSatuanAdapter;
import com.example.kelompok1.Helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaketregulerActivity extends AppCompatActivity {

    private ModelPaketReguler modelPaketReguler;
    private String BaseUrl;
    private List<ModelPaketReguler> listPaketReguler = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paketreguler);

        recyclerView = findViewById(R.id.rv_paketreguler);
        BaseUrl = SessionManager.BASE_URL;
        getListPaketSatuan();
    }

    private void getListPaketSatuan() {
        final ProgressDialog progressDialog = new ProgressDialog(PaketregulerActivity.this);
        progressDialog.setMessage("Loading ...");
        progressDialog.show();

        String URL_API = BaseUrl + "api/PaketReguler";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equals("200")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String strId = object.getString("id_paket").trim();
                                    String strharga = object.getString("harga").trim();
                                    String strnama_isi_paket = object.getString("nama_isi_paket").trim();

                                    ModelPaketReguler modelPaketReguler = new ModelPaketReguler();
                                    modelPaketReguler.setId(strId);
                                    modelPaketReguler.setHarga(strharga);
                                    modelPaketReguler.setNama_isi_paket(strnama_isi_paket);

                                    listPaketReguler.add(modelPaketReguler);

                                }
                                setupRecyclerView(listPaketReguler);
                            } else {
                                Toast.makeText(PaketregulerActivity.this, message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(PaketregulerActivity.this, "Error" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(PaketregulerActivity.this, "Error Response " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(PaketregulerActivity.this);
        requestQueue.add(stringRequest);


    }

    public void setupRecyclerView(List<ModelPaketReguler> listPaketReguler) {
        RvPaketRegulerAdapter myAdapter = new RvPaketRegulerAdapter(PaketregulerActivity.this, listPaketReguler);
        recyclerView.setLayoutManager(new LinearLayoutManager(PaketregulerActivity.this));
        recyclerView.setAdapter(myAdapter);
    }
}