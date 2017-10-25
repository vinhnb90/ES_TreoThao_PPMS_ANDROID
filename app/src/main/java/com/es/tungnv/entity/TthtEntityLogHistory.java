package com.es.tungnv.entity;

/**
 * Created by VinhNB on 10/14/2016.
 */
public class TthtEntityLogHistory {
    private String ID_NHANVIEN;
    private String ACTION;

    public TthtEntityLogHistory(TthtEntityLogHistory tthtEntityLogHistory) {
        this.ID_NHANVIEN = tthtEntityLogHistory.ID_NHANVIEN;
        this.ACTION = tthtEntityLogHistory.ACTION;
    }

    public TthtEntityLogHistory(String ID_NHANVIEN, String ACTION) {
        this.ID_NHANVIEN = ID_NHANVIEN;
        this.ACTION = ACTION;
    }

    public String getID_NHANVIEN() {
        return ID_NHANVIEN;
    }

    public void setID_NHANVIEN(String ID_NHANVIEN) {
        this.ID_NHANVIEN = ID_NHANVIEN;
    }

    public String getACTION() {
        return ACTION;
    }

    public void setACTION(String ACTION) {
        this.ACTION = ACTION;
    }
}
