package com.example.kelompok1.ui.history;

public class ModelHistory {
    private String id, nama, total, tanggal;

    public ModelHistory(){};

    public String getId(){ return id;}
    public void setId(String id){this.id = id;}

    public String getTanggal(){ return tanggal; }
    public void setTanggal(String tanggal){ this.tanggal = tanggal;}

    public String getNama(){ return nama;}
    public void setNama(String nama){this.nama = nama;}

    public String getTotal(){ return total;}
    public void setTotal(String total){this.total = total;}
}