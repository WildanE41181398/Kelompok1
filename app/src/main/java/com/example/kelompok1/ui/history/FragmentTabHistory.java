package com.example.kelompok1.ui.history;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.example.kelompok1.Helper.SessionManager;
import com.example.kelompok1.R;
import com.example.kelompok1.ui.notifications.ModelNotification;
import com.example.kelompok1.ui.notifications.RecyclerViewTabNotificationAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentTabHistory extends Fragment {

    private List<ModelHistory> listHistory = new ArrayList<>();
    private RecyclerView recyclerView;
    private String IdUser, BaseUrl;
    private SessionManager sessionManager;
    private TextView toasthst;

    public static FragmentTabHistory newInstance() { return new FragmentTabHistory(); }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fraghis_history, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        BaseUrl = SessionManager.BASE_URL;
        recyclerView = root.findViewById(R.id.rv_history);
        sessionManager = new SessionManager(getContext());
        toasthst = root.findViewById(R.id.toast_hst);

        HashMap<String, String> user = sessionManager.getUserDetail();

        IdUser = user.get(SessionManager.ID);
        getListHistory();

        return root;
    }

    private void getListHistory(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading ...");
        progressDialog.show();

        String URL_PROMOSI = BaseUrl + "api/history/getallhistory";
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
                                    String strNama = object.getString("nama_paket").trim();
                                    String strTotal = object.getString("total_harga").trim();
                                    String strTanggal = object.getString("tgl_transaksi").trim();

                                    ModelHistory modelHistory = new ModelHistory();
                                    modelHistory.setId(strId);
                                    modelHistory.setNama(strNama);
                                    modelHistory.setTanggal(strTanggal);
                                    modelHistory.setTotal(strTotal);
                                    listHistory.add(modelHistory);
                                }
                                setupRecyclerView(listHistory);
                            }else{
                                //Toast.makeText(getContext(), "Tidak dapat memuat data", Toast.LENGTH_LONG).show();
                                toasthst.setVisibility(View.VISIBLE);

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

    public void setupRecyclerView(List<ModelHistory> listHistory){
        RecyclerViewTabHistoryAdapter myAdapter = new RecyclerViewTabHistoryAdapter(getContext(), listHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(myAdapter);
    }
}
