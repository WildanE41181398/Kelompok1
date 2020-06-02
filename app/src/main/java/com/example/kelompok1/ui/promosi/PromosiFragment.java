package com.example.kelompok1.ui.promosi;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kelompok1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PromosiFragment extends Fragment {

    private PromosiViewModel promosiViewModel;
    private String coba = "Mencoba text View";
    private List<ModelPromosi> listPromosi = new ArrayList<>();
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){

        promosiViewModel = ViewModelProviders.of(this).get(PromosiViewModel.class);
        View root = inflater.inflate(R.layout.fragment_promosi, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        recyclerView = root.findViewById(R.id.rv_promosi);
        getListPromosi();

        return root;
    }

    private void getListPromosi(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading ...");
        progressDialog.show();

        String URL_PROMOSI = "http://192.168.5.145/kelompok1_tif_d/OrenzLaundry/api/promosi";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PROMOSI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String error = jsonObject.getString("error");
                            if (status.equals("200")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i=0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String strId = object.getString("id_promo").trim();
                                    String strAkhir = object.getString("akhir").trim();
                                    String strGambar = object.getString("gambar").trim();

                                    ModelPromosi modelPromosi = new ModelPromosi();
                                    modelPromosi.setId(strId);
                                    modelPromosi.setAkhir_priode(strAkhir);
                                    modelPromosi.setGambar(strGambar);
                                    listPromosi.add(modelPromosi);

                                }
                            setupRecyclerView(listPromosi);
                            }else{
                                Toast.makeText(getContext(), "Tidak dapat memuat data", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Error Response " + error.toString(), Toast.LENGTH_LONG).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public void setupRecyclerView(List<ModelPromosi> listPromosi){
        RecyclerViewPromosiAdapter myAdapter = new RecyclerViewPromosiAdapter(getContext(), listPromosi);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(myAdapter);
    }
}
