package com.example.kelompok1.ui.notifications;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.example.kelompok1.Helper.SessionManager;
import com.example.kelompok1.R;
import com.example.kelompok1.ui.promosi.ModelPromosi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentTabMessages extends Fragment {

    private List<ModelMessages> listMessages = new ArrayList<>();
    private RecyclerView recyclerView;
    private FloatingActionButton fa_add;
    private String IdUser;
    private SessionManager sessionManager;

    public static FragmentTabMessages newInstance(){
        return new FragmentTabMessages();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragmess_messages, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        fa_add = root.findViewById(R.id.floatingActionButton);
        recyclerView = root.findViewById(R.id.rv_messages);

        sessionManager = new SessionManager(getContext());

        HashMap<String, String> user = sessionManager.getUserDetail();

        IdUser = user.get(SessionManager.ID);
        getListMessages();

        fa_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddMessages.class);
                startActivity(intent);
            }
        });

        return root;
    }

    private void getListMessages(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading ...");
        progressDialog.show();

        String URL_PROMOSI = "http://192.168.5.145/kelompok1_tif_d/OrenzLaundry/api/messages/getallmessages";
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
                                    String strId = object.getString("id_pesan").trim();
                                    String strBody = object.getString("isi_pesan").trim();
                                    String strSubjek = object.getString("subjek_pesan").trim();

                                    ModelMessages modelMessages = new ModelMessages();
                                    modelMessages.setId(strId);
                                    modelMessages.setBody(strBody);
                                    modelMessages.setSubjek(strSubjek);
                                    listMessages.add(modelMessages);
                                }
                                setupRecyclerView(listMessages);
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
                params.put("user", IdUser);
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public void setupRecyclerView(List<ModelMessages> listMessages){
        RecyclerViewTabMessagesAdapter myAdapter = new RecyclerViewTabMessagesAdapter(getContext(), listMessages);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(myAdapter);
    }
}
