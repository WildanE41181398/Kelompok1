package com.example.kelompok1.Helper;

import java.lang.ref.SoftReference;

import javax.sql.StatementEvent;

public class ModelPaketSatuan {

    private String nama_barang, harga, id;

    public ModelPaketSatuan(){

    }

    public String getId(){ return id; }

    public void setId(String id){ this.id = id; }

    public String getNama_barang(){ return nama_barang; }

    public void setNama_barang(String nama_barang){ this.nama_barang = nama_barang; }

    public String getHarga(){ return harga; }

    public  void setHarga(String harga){ this.harga = harga; }
}
