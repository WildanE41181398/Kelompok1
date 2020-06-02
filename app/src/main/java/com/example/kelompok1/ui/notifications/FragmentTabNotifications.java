package com.example.kelompok1.ui.notifications;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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

public class FragmentTabNotifications extends Fragment {

    private List<ModelNotification> listNotifications = new ArrayList<>();
    private RecyclerView recyclerView;
    private String idUser;

    public static FragmentTabNotifications newInstance(){
        return new FragmentTabNotifications();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragmess_notifications, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        recyclerView = root.findViewById(R.id.rv_notif);
        idUser = "USR00001";
        getListNotifications();

        return root;
    }

    private void getListNotifications(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading ...");
        progressDialog.show();

        String URL_PROMOSI = "http://192.168.5.145/kelompok1_tif_d/OrenzLaundry/api/messages/getallnotification";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PROMOSI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equals("200")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i=0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String strId = object.getString("id_transaksi").trim();
                                    String strStatus = object.getString("status").trim();
                                    String strTanggal = object.getString("tgl_transaksi").trim();

                                    ModelNotification modelNotification = new ModelNotification();
                                    modelNotification.setId(strId);
                                    modelNotification.setStatus(strStatus);
                                    modelNotification.setTanggal(strTanggal);
                                    listNotifications.add(modelNotification);
                                }
                                setupRecyclerView(listNotifications);
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
                params.put("user", idUser);
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public void setupRecyclerView(List<ModelNotification> listNotifications){
        RecyclerViewTabNotificationAdapter myAdapter = new RecyclerViewTabNotificationAdapter(getContext(), listNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(myAdapter);
    }
}
