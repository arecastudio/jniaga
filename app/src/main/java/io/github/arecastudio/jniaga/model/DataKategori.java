package io.github.arecastudio.jniaga.model;

import java.util.ArrayList;

/**
 * Created by android on 12/2/17.
 */

public class DataKategori extends ArrayList {
    private int id,icon;
    private String nama;

    public DataKategori() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
