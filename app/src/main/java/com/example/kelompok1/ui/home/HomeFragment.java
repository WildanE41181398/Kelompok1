package com.example.kelompok1.ui.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kelompok1.Helper.SessionManager;
import com.example.kelompok1.PaketregulerActivity;
import com.example.kelompok1.PaketsatuanActivity;
import com.example.kelompok1.R;
import com.example.kelompok1.TopUp;
import com.example.kelompok1.ui.promosi.DetailPromosi;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private SliderLayout sliderLayout, caraKerja;
    private ArrayList<ModelHome>  listHomes;
    private CardView top_up, paket_reguler, paket_express, paket_satuan;
    private TextView tv_saldo;
    private SessionManager sessionManager;
    private String IdUser, BaseUrl;
    private CarouselView carouselView;
    private int[] carakerjaImages = {R.drawable.ol_homekerja_01, R.drawable.ol_homesaldo_02, R.drawable.ol_homekerja_01, R.drawable.ol_homesaldo_02, R.drawable.ol_homekerja_01};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        sliderLayout = root.findViewById(R.id.imageSlider);
//        caraKerja = root.findViewById(R.id.caraKerja);
        top_up = root.findViewById(R.id.cv_top_up);
        paket_reguler = root.findViewById(R.id.cv_paket_reguler);
        paket_express = root.findViewById(R.id.cv_paket_express);
        paket_satuan = root.findViewById(R.id.cv_paket_satuan);
        tv_saldo = root.findViewById(R.id.tv_saldohome);
        carouselView = root.findViewById(R.id.carousel_cara_kerja);
        sessionManager = new SessionManager(getContext());

        HashMap<String, String> user = sessionManager.getUserDetail();
        BaseUrl = SessionManager.BASE_URL;

        IdUser = user.get(SessionManager.ID);

        sliderLayout.setIndicatorAnimation(SliderLayout.Animations.THIN_WORM);
        listHomes = new ArrayList<>();
        sliderLayout.setScrollTimeInSec(3);

        SliderView sliderView = new SliderView(getContext());

        carouselView.setPageCount(carakerjaImages.length);
        carouselView.setImageListener(imageListener);

        getDataUser();
        getGambarPromosi();

        top_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TopUp.class);
                startActivity(intent);
            }
        });

        paket_reguler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
 //               Toast.makeText(getContext(), "Aksi pada Paket Reguler", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), PaketregulerActivity.class);
                startActivity(intent);
            }
        });

        paket_express.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Aksi pada Paket Express", Toast.LENGTH_LONG).show();
            }
        });

        paket_satuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "Aksi pada Paket Satuan", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), PaketsatuanActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(carakerjaImages[position]);
        }
    };

    private void setupdata(List<ModelHome> list){
        for (int i=0; i<list.size(); i++){
            final ModelHome home = list.get(i);
            SliderView sliderView = new SliderView(getContext());
            sliderView.setImageUrl(BaseUrl + "assets/files/gambar_promo/" + home.getGambarpromosi());

            sliderView.setImageScaleType(ImageView.ScaleType.FIT_XY);
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                    Intent intent = new Intent(getContext(), DetailPromosi.class);
                    intent.putExtra("id", home.getId());
                    startActivity(intent);
                }
            });
            final int finalI = i;
            sliderLayout.addSliderView(sliderView);
        }
    }

    private void getDataUser(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading ...");
        progressDialog.show();

        String URL_GETUSER = BaseUrl + "api/home/getuser";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GETUSER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (status.equals("200")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String saldo = object.getString("saldo").trim();
                                    tv_saldo.setText("Saldo : Rp. " + saldo);

                                }
                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error " + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error Response " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", IdUser);
                params.put("Content-Type", "application/x-www-form-urlencoded");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    private void getGambarPromosi(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading ...");
        progressDialog.show();

        String URL_PROMOSI = BaseUrl + "api/home/getgambarpromosi";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PROMOSI,
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
                                    String strId = object.getString("id_promo").trim();
                                    String strJudul = object.getString("judul_promo").trim();
                                    String strGambar = object.getString("gambar").trim();

                                    ModelHome home = new ModelHome();
                                    home.setId(strId);
                                    home.setJudul(strJudul);
                                    home.setGambarpromosi(strGambar);
                                    listHomes.add(home);
                                    }
                                setupdata(listHomes);
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

    private void getPaket(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading ...");
        progressDialog.show();

        String URL_PROMOSI = BaseUrl + "api/home/getgambarpromosi";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PROMOSI,
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
                                    String strId = object.getString("id_promo").trim();
                                    String strJudul = object.getString("judul_promo").trim();
                                    String strGambar = object.getString("gambar").trim();

                                    ModelHome home = new ModelHome();
                                    home.setId(strId);
                                    home.setJudul(strJudul);
                                    home.setGambarpromosi(strGambar);
                                    listHomes.add(home);
                                }
                                setupdata(listHomes);
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

}
