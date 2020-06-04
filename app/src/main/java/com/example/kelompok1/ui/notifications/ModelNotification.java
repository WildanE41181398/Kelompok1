package com.example.kelompok1.ui.notifications;

public class ModelNotification {

    private String id, status, tanggal;
    private String dtl_nama, dtl_harga, dtl_berat, dtl_subtotal;

    public ModelNotification(){};

    public String getId(){ return id;}
    public void setId(String id){this.id = id;}

    public String getStatus(){ return status; }
    public void setStatus(String status){this.status=status;}

    public String getTanggal(){ return tanggal; }
    public void setTanggal(String tanggal){ this.tanggal = tanggal;}

    public String getDtl_nama(){ return dtl_nama; }
    public void setDtl_nama(String dtl_nama){ this.dtl_nama = dtl_nama; }

    public String getDtl_harga(){ return dtl_harga; }
    public void setDtl_harga(String dtl_harga){ this.dtl_harga = dtl_harga; }

    public String getDtl_berat(){ return dtl_berat; }
    public void setDtl_berat(String dtl_berat){ this.dtl_berat = dtl_berat; }

    public String getDtl_subtotal(){ return dtl_subtotal; }
    public void setDtl_subtotal(String dtl_subtotal){ this.dtl_subtotal = dtl_subtotal; }
}
