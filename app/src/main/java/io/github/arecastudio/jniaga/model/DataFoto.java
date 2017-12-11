package io.github.arecastudio.jniaga.model;

import java.io.FileInputStream;

/**
 * Created by android on 12/10/17.
 */

public class DataFoto {
    private String nama,uuidNama;
    private int ukuran;
    private FileInputStream fis;

    public DataFoto(){}

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getUuidNama() {
        return uuidNama;
    }

    public void setUuidNama(String uuidNama) {
        this.uuidNama = uuidNama;
    }

    public int getUkuran() {
        return ukuran;
    }

    public void setUkuran(int ukuran) {
        this.ukuran = ukuran;
    }

    public FileInputStream getFis() {
        return fis;
    }

    public void setFis(FileInputStream fis) {
        this.fis = fis;
    }
}
