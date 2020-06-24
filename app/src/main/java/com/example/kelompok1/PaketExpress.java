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
import com.example.kelompok1.Helper.ModelPaketExpres;
import com.example.kelompok1.Helper.ModelPaketSatuan;
import com.example.kelompok1.Helper.RvPaketExpresAdapter;
import com.example.kelompok1.Helper.RvPaketSatuanAdapter;
import com.example.kelompok1.Helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaketExpress extends AppCompatActivity {

    private ModelPaketExpres modelPaketExpres;
    private String BaseUrl;
    private List<ModelPaketExpres> ListPaketExpres = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paket_express);

        recyclerView = findViewById(R.id.rv_paketexpres);
        BaseUrl = SessionManager.BASE_URL;
        getListPaketExpres();

    }

    private void getListPaketExpres() {
            final ProgressDialog progressDialog = new ProgressDialog(PaketExpress.this);
            progressDialog.setMessage("Loading ...");
            progressDialog.show();

            String URL_API = BaseUrl + "api/PaketExpres";
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
                                        String strnama_isi_paket = object.getString("nama_isi_paket").trim();
                                        String strdurasi_paket = object.getString("durasi_paket").trim();
                                        String strharga = object.getString("harga").trim();

                                        ModelPaketExpres modelPaketExpres = new ModelPaketExpres();
                                        modelPaketExpres.setId(strId);
                                        modelPaketExpres.setNama_isi_paket(strnama_isi_paket);
                                        modelPaketExpres.setDurasi_paket(strdurasi_paket);
                                        modelPaketExpres.setHarga(strharga);

                                        ListPaketExpres.add(modelPaketExpres);

                                    }
                                    setupRecyclerView(ListPaketExpres);
                                }else{
                                    Toast.makeText(PaketExpress.this, message, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(PaketExpress.this, "Error" + e.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(PaketExpress.this, "Error Response " + error.toString(), Toast.LENGTH_LONG).show();
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

            RequestQueue requestQueue = Volley.newRequestQueue(PaketExpress.this);
            requestQueue.add(stringRequest);
        }

        public void setupRecyclerView(List<ModelPaketExpres> ListPaketExpres){
            RvPaketExpresAdapter myAdapter = new RvPaketExpresAdapter(PaketExpress.this, ListPaketExpres);
            recyclerView.setLayoutManager(new LinearLayoutManager(PaketExpress.this));
            recyclerView.setAdapter(myAdapter);
        }
    }