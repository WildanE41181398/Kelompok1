package com.example.kelompok1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LokasiAntar extends FragmentActivity implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Marker mCurrLocationMarker;
    private Location mLastLocation;
    private String id_paket, id_transaksi, latIntent, langIntent, tmpAlamat;
    private String latMap, langMap;
    private Button simpanLokasi;
    private EditText etAlamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lokasi_antar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        simpanLokasi = findViewById(R.id.btn_simpanlokasi);
        etAlamat = findViewById(R.id.et_alamat_antar);
        simpanLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tmpAlamat = etAlamat.getText().toString().trim();
                if (!TextUtils.isEmpty(tmpAlamat)){
                    Intent intent = new Intent(LokasiAntar.this, TransaksiTahap2.class);
                    intent.putExtra("id_paket", id_paket);
                    intent.putExtra("id_transaksi", id_transaksi);
                    intent.putExtra("alamat", tmpAlamat);
                    if (getIntent().getStringExtra("latIntent") != null && getIntent().getStringExtra("langIntent") != null){
                        intent.putExtra("latIntent", latIntent);
                        intent.putExtra("langIntent", langIntent);
                    }else{
                        intent.putExtra("latIntent", latMap);
                        intent.putExtra("langIntent", langMap);
                    }
                    startActivity(intent);
                }else{
                    Toast.makeText(LokasiAntar.this, "Mohon isi alamat lengkap anda terlebih dahulu!", Toast.LENGTH_LONG).show();
                }
            }
        });

        id_paket = getIntent().getStringExtra("id_paket");
        id_transaksi = getIntent().getStringExtra("id_transaksi");
        if (getIntent().getStringExtra("latIntent") != null && getIntent().getStringExtra("langIntent") != null){
            latIntent = getIntent().getStringExtra("latIntent");
            langIntent = getIntent().getStringExtra("langIntent");
            tmpAlamat = getIntent().getStringExtra("alamat");
            etAlamat.setText(tmpAlamat);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        checkLocationPermission();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(3000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        LatLng latLng;

        if (getIntent().getStringExtra("latIntent") != null && getIntent().getStringExtra("langIntent") != null){
            latIntent = getIntent().getStringExtra("latIntent");
            langIntent = getIntent().getStringExtra("langIntent");
            latLng = new LatLng(Double.parseDouble(latIntent) , Double.parseDouble(langIntent));
            MarkerOptions marker = new MarkerOptions().position( new LatLng(Double.parseDouble(latIntent) , Double.parseDouble(langIntent))).title("Lokasi Saya");
            mCurrLocationMarker = mMap.addMarker(marker);
        }else{
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
        }

        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latLng.latitude, latLng.longitude)).zoom(16).build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(final LatLng point) {
                //Hapus Marker lama jika sebelumnya terdapat marker
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                Geocoder geocoder;
                List<Address> addresses = null;
                geocoder = new Geocoder(LokasiAntar.this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String address = addresses.get(0).getAddressLine(0);

                MarkerOptions marker = new MarkerOptions().position( new LatLng(point.latitude, point.longitude)).title("Lokasi Jemput").snippet(address);
                mCurrLocationMarker = mMap.addMarker(marker);
                mCurrLocationMarker.showInfoWindow();

                Toast.makeText(LokasiAntar.this, "Address : " + address, Toast.LENGTH_LONG).show();

                latMap = String.valueOf(point.latitude);
                langMap = String.valueOf(point.longitude);

                // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                String city = addresses.get(0).getLocality();
//                String state = addresses.get(0).getAdminArea();
//                String country = addresses.get(0).getCountryName();
//                String postalCode = addresses.get(0).getPostalCode();
//                String knownName = addresses.get(0).getFeatureName();


            }
        });

        //menghentikan pembaruan lokasi
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Izin diberikan.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Izin ditolak.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}
