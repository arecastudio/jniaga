package io.github.arecastudio.jniaga.model;

/**
 * Created by android on 12/9/17.
 */

public class DataIklan {
    private int IdIklan,IdKategri;
    private String Judul,Isi,IdUser,NamaGambar,NamaKategori;
    private double harga;

    public DataIklan() {
    }

    public int getIdIklan() {
        return IdIklan;
    }

    public void setIdIklan(int idIklan) {
        IdIklan = idIklan;
    }

    public int getIdKategri() {
        return IdKategri;
    }

    public void setIdKategri(int idKategri) {
        IdKategri = idKategri;
    }

    public String getNamaKategori() {
        return NamaKategori;
    }

    public void setNamaKategori(String namaKategori) {
        NamaKategori = namaKategori;
    }

    public String getJudul() {
        return Judul;
    }

    public void setJudul(String judul) {
        Judul = judul;
    }

    public String getIsi() {
        return Isi;
    }

    public void setIsi(String isi) {
        Isi = isi;
    }

    public String getIdUser() {
        return IdUser;
    }

    public void setIdUser(String idUser) {
        IdUser = idUser;
    }

    public String getNamaGambar() {
        return NamaGambar;
    }

    public void setNamaGambar(String namaGambar) {
        NamaGambar = namaGambar;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }
}
