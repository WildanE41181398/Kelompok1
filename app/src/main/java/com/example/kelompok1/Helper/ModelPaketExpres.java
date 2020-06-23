package com.example.kelompok1.Helper;

public class ModelPaketExpres {

    private String nama_isi_paket, durasi_paket, harga, id;

    public ModelPaketExpres(){

    }

    public String getId(){ return id; }

    public void setId(String id){ this.id = id; }

    public String getNama_isi_paket(){ return nama_isi_paket; }

    public void setNama_isi_paket(String nama_isi_paket){ this.nama_isi_paket = nama_isi_paket; }

    public String getHarga(){ return harga; }

    public void setHarga(String harga){ this.harga = harga; }

    public String getDurasi_paket(){ return durasi_paket; }

    public void setDurasi_paket(String durasi_paket){ this.durasi_paket = durasi_paket; }
}
