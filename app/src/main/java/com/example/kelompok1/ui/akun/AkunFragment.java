package com.example.kelompok1.ui.akun;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kelompok1.Beranda;
import com.example.kelompok1.BerandaOrenz;
import com.example.kelompok1.Helper.SessionManager;
import com.example.kelompok1.R;
import com.example.kelompok1.ResetPassword;
import com.example.kelompok1.TransaksiTahap1;
import com.example.kelompok1.VerifikasiEmailActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AkunFragment extends Fragment {

    private static final int REQUEST_PERMISSION = 1;
    private AkunViewModel akunViewModel;
    private Button btn_logout, btn_update, btn_aktivasi, btn_reset, btn_image;
    private Spinner spinnerJK;
    private TextView tv_date;
    private EditText et_name, et_email, et_nohp, et_alamat, et_username;
    private Calendar myCalendar;
    private String id_user, BaseUrl;
    private String tmpNama, tmpEmail, tmpNohp, tmpAlamat, tmpUsername, tmpJK, tmpTanggal;
    private SessionManager sessionManager;
    private Boolean issetImportantValue;
    private ProgressDialog progressDialog;

    private Bitmap bitmap;
    CircleImageView foto_profil;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){

        akunViewModel = ViewModelProviders.of(this).get(AkunViewModel.class);
        View root = inflater.inflate(R.layout.fragment_akun, container, false);
        et_name = root.findViewById(R.id.namaBio);
        et_email = root.findViewById(R.id.emailBio);
        et_nohp = root.findViewById(R.id.no_hpBio);
        et_alamat = root.findViewById(R.id.alamatBio);
        et_username = root.findViewById(R.id.usernameBio);
        spinnerJK = root.findViewById(R.id.spinnerJK);
        btn_logout = root.findViewById(R.id.btn_logout) ;
        btn_update = root.findViewById(R.id.btn_update);
        btn_aktivasi = root.findViewById(R.id.btn_aktivasi);
        btn_reset = root.findViewById(R.id.btn_reset);
        btn_image = root.findViewById(R.id.btn_foto);
        tv_date = root.findViewById(R.id.tanggal_lahirBio);
        foto_profil = root.findViewById(R.id.foto_profil);

        myCalendar = Calendar.getInstance();
        sessionManager = new SessionManager(getContext());
        progressDialog = new ProgressDialog(getContext());
        BaseUrl = SessionManager.BASE_URL;

        HashMap<String, String> user = sessionManager.getUserDetail();
        id_user = user.get(SessionManager.ID);

        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                }
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Perhatian");

                // Ask the final question
                builder.setMessage("Apakah anda yakin ingin keluar?");

                // Set the alert dialog yes button click listener
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sessionManager.logout();
                    }
                });

                // Set the alert dialog no button click listener
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when No button clicked
                    }
                });

                AlertDialog dialog = builder.create();
                // Display the alert dialog on interface
                dialog.show();

            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEmptyValue();
                if (issetImportantValue){
                    UpdateUser();
                }else {
                    Toast.makeText(getContext(), "Mohon lengkapi semua data diatas!", Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_aktivasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), VerifikasiEmailActivity.class);
                intent.putExtra("id_user", id_user);
                startActivity(intent);
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ResetPassword.class);
                intent.putExtra("IdUserTAG", id_user);
                startActivity(intent);
            }
        });

        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, month);
                        myCalendar.set(Calendar.DAY_OF_MONTH, day);

                        String formatTanggal = "dd MMMM yyyy";
                        SimpleDateFormat dbsdf = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat sdf = new SimpleDateFormat(formatTanggal);
                        tv_date.setText(sdf.format(myCalendar.getTime()));
                        tmpTanggal = dbsdf.format(myCalendar.getTime());
//                        tglJemput = dbsdf.format(myCalendar.getTime());
                    }
                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        spinnerJK.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){
                    tmpJK = "Laki laki";
                }else {
                    tmpJK = "Perempuan";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        GetUser();

        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // required permissions granted, start crop image activity
            chooseFile();
        } else {
            Toast.makeText(getContext(), "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

    private void chooseFile() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 100);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null){
            Uri filePath = data.getData();
            try{
                bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getContext()).getContentResolver(), filePath);
                foto_profil.setImageBitmap(bitmap);
            } catch (IOException e){
                e.printStackTrace();
            }

//            Toast.makeText(getContext(), getStringImage(bitmap), Toast.LENGTH_LONG).show();
            UploadFoto(id_user, getStringImage(bitmap));
        }
    }

    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageByteArray, Base64.DEFAULT);
    }

    private void UploadFoto(final String id_user, final String foto){
        progressDialog.setMessage("Uploading..");
        progressDialog.show();

        String URL_UPDATE = BaseUrl + "api/profil/uploadfoto";
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equals("200")){
                                Toast.makeText(getContext(),message, Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(getContext(), BerandaOrenz.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                intent.putExtra("NAVIGATION", "AKUN");
//                                startActivity(intent);
                            } else {
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Cek " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", id_user);
                params.put("foto", foto);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void GetUser(){
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String URL_CHECK_EMAIL = BaseUrl + "api/profil/getuser";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHECK_EMAIL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");

                            if (message.equals("success")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String nama = object.getString("nama_user").trim();
                                    String email = object.getString("email").trim();
                                    String no_hp = object.getString("no_hp").trim();
                                    String jenis_kelamin = object.getString("jenis_kelamin").trim();
                                    String tgl_lahir = object.getString("tgl_lahir").trim();
                                    String photo = object.getString("photo").trim();
                                    String alamat = object.getString("alamat").trim();
                                    String username = object.getString("username").trim();
                                    String status = object.getString("status").trim();

                                    et_name.setText(nama);
                                    et_email.setText(email);

                                    if (no_hp.equals("null")){
                                        et_nohp.setText("");
                                    }else {
                                        et_nohp.setText(no_hp);
                                    }

                                    if (alamat.equals("null")){
                                        et_alamat.setText("");
                                    }else{
                                        et_alamat.setText(alamat);
                                    }

                                    if (username.equals("null")){
                                        et_username.setText("");
                                    }else {
                                        et_username.setText(username);
                                    }

                                    if (tgl_lahir.equals("null")){
                                        tv_date.setText("");
                                    }else{
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
                                        tv_date.setText(sdf.format(myCalendar.getTime()));
                                    }

                                    if (jenis_kelamin.equals("Laki laki")){
                                        spinnerJK.setSelection(0);
                                        tmpJK = "Laki laki";
                                    }else {
                                        spinnerJK.setSelection(1);
                                        tmpJK = "Perempuan";
                                    }

                                    if (status.equals("1")){
                                        btn_aktivasi.setVisibility(View.GONE);
                                    }else{
                                        btn_aktivasi.setVisibility(View.VISIBLE);
                                    }

                                    if (photo.equals("null")){
                                        foto_profil.setImageResource(R.drawable.ic_account_circle_black_18dp);
                                    }else{
                                        Picasso.with(getContext()).load(BaseUrl + "assets/files/gambar_customer/" + photo).into(foto_profil);
                                    }
                                }
                            } else {
                                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Error" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Error Response" + error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", id_user);
                params.put("Content-Type", "application/x-www-form-urlencoded");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void UpdateUser(){
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String URL_CHECK_EMAIL = BaseUrl + "api/profil/updateprofil";
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL_CHECK_EMAIL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");

                            if (message.equals("success")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    Intent intent = new Intent(getContext(), BerandaOrenz.class);
                                    intent.putExtra("NAVIGATION", "AKUN");
                                    startActivity(intent);
                                }
                            } else {
                                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Error" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Error Response" + error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", id_user);
                params.put("nama_user", tmpNama);
                params.put("email", tmpEmail);
                params.put("alamat", tmpAlamat);
                params.put("no_hp", tmpNohp);
                params.put("jenis_kelamin", tmpJK);
                params.put("username", tmpUsername);
                params.put("tgl_lahir", tmpTanggal);
                params.put("Content-Type", "application/x-www-form-urlencoded");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void checkEmptyValue(){
        tmpNama = et_name.getText().toString().trim();
        tmpEmail = et_email.getText().toString().trim();
        tmpAlamat = et_alamat.getText().toString().trim();
        tmpNohp = et_nohp.getText().toString().trim();
        tmpUsername = et_username.getText().toString().trim();

        if (TextUtils.isEmpty(tmpNama) || TextUtils.isEmpty(tmpEmail) ||
                TextUtils.isEmpty(tmpNohp) || TextUtils.isEmpty(tmpUsername) || TextUtils.isEmpty(tmpAlamat)){
            issetImportantValue = false;
        }else {
            issetImportantValue = true;
        }
    }
}
