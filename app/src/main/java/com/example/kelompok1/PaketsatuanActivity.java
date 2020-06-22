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
import com.example.kelompok1.Helper.ModelPaketSatuan;
import com.example.kelompok1.Helper.RvPaketSatuanAdapter;
import com.example.kelompok1.Helper.SessionManager;
import com.example.kelompok1.ui.promosi.ModelPromosi;
import com.example.kelompok1.ui.promosi.RecyclerViewPromosiAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaketsatuanActivity extends AppCompatActivity {

    private ModelPaketSatuan modelPaketSatuan;
    private String BaseUrl;
    private List<ModelPaketSatuan> listPaketSatuan = new ArrayList<>();
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paketsatuan);

        recyclerView = findViewById(R.id.rv_paketsatuan);
        BaseUrl = SessionManager.BASE_URL;
        getListPaketSatuan();
    }

    private void getListPaketSatuan() {
        final ProgressDialog progressDialog = new ProgressDialog(PaketsatuanActivity.this);
        progressDialog.setMessage("Loading ...");
        progressDialog.show();

        String URL_API = BaseUrl + "api/PaketSatuan";
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
                                for (int i=0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String strId = object.getString("id_paket").trim();
                                    String strharga = object.getString("harga").trim();
                                    String strnama_barang = object.getString("nama_barang").trim();

                                    ModelPaketSatuan modelPaketSatuan = new ModelPaketSatuan();
                                    modelPaketSatuan.setId(strId);
                                    modelPaketSatuan.setHarga(strharga);
                                    modelPaketSatuan.setNama_barang(strnama_barang);

                                    listPaketSatuan.add(modelPaketSatuan);

                                }
                                setupRecyclerView(listPaketSatuan);
                            }else{
                                Toast.makeText(PaketsatuanActivity.this, message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(PaketsatuanActivity.this, "Error" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(PaketsatuanActivity.this, "Error Response " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(PaketsatuanActivity.this);
        requestQueue.add(stringRequest);
    }

    public void setupRecyclerView(List<ModelPaketSatuan> listPaketSatuan){
        RvPaketSatuanAdapter myAdapter = new RvPaketSatuanAdapter(PaketsatuanActivity.this, listPaketSatuan);
        recyclerView.setLayoutManager(new LinearLayoutManager(PaketsatuanActivity.this));
        recyclerView.setAdapter(myAdapter);
    }
}
