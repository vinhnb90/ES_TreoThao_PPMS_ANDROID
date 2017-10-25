package com.es.tungnv.entity;

/**
 * Created by TUNGNV on 4/4/2016.
 */
public class EsspEntityPhuongAn {

    private int ID;
    private int HOSO_ID;
    private String DODEM_CTO_A;
    private String DODEM_CTO_V;
    private String DODEM_CTO_TI;
    private int VTRI_CTO;
    private String COT_SO;
    private String VI_TRI_KHAC;
    private int HTHUC_TTOAN;
    private String DIEM_DAUDIEN;
    private int DTUONG_KHANG;
    private int TTRANG_SDUNG_DIEN;
    private int HTHUC_GCS;
    private String HTK;
    private int TINH_TRANG;

    public void setCOT_SO(String COT_SO) {
        this.COT_SO = COT_SO;
    }

    public String getCOT_SO() {
        return COT_SO;
    }

    public void setVI_TRI_KHAC(String VI_TRI_KHAC) {
        this.VI_TRI_KHAC = VI_TRI_KHAC;
    }

    public String getVI_TRI_KHAC() {
        return VI_TRI_KHAC;
    }

    public void setHTK(String HTK) {
        this.HTK = HTK;
    }

    public String getHTK() {
        return HTK;
    }

    public void setTTRANG_SDUNG_DIEN(int TTRANG_SDUNG_DIEN) {
        this.TTRANG_SDUNG_DIEN = TTRANG_SDUNG_DIEN;
    }

    public int getTTRANG_SDUNG_DIEN() {
        return TTRANG_SDUNG_DIEN;
    }

    public void setHTHUC_GCS(int HTHUC_GCS) {
        this.HTHUC_GCS = HTHUC_GCS;
    }

    public int getHTHUC_GCS() {
        return HTHUC_GCS;
    }

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public void setHOSO_ID(int HOSO_ID) {
        this.HOSO_ID = HOSO_ID;
    }

    public int getHOSO_ID() {
        return HOSO_ID;
    }

    public String getDODEM_CTO_A() {
        return DODEM_CTO_A;
    }

    public void setDODEM_CTO_A(String dODEM_CTO_A) {
        DODEM_CTO_A = dODEM_CTO_A;
    }

    public String getDODEM_CTO_V() {
        return DODEM_CTO_V;
    }

    public void setDODEM_CTO_V(String dODEM_CTO_V) {
        DODEM_CTO_V = dODEM_CTO_V;
    }

    public String getDODEM_CTO_TI() {
        return DODEM_CTO_TI;
    }

    public void setDODEM_CTO_TI(String dODEM_CTO_TI) {
        DODEM_CTO_TI = dODEM_CTO_TI;
    }

    public int getVTRI_CTO() {
        return VTRI_CTO;
    }

    public void setVTRI_CTO(int vTRI_CTO) {
        VTRI_CTO = vTRI_CTO;
    }

    public int getHTHUC_TTOAN() {
        return HTHUC_TTOAN;
    }

    public void setHTHUC_TTOAN(int hTHUC_TTOAN) {
        HTHUC_TTOAN = hTHUC_TTOAN;
    }

    public String getDIEM_DAUDIEN() {
        return DIEM_DAUDIEN;
    }

    public void setDIEM_DAUDIEN(String dIEM_DAUDIEN) {
        DIEM_DAUDIEN = dIEM_DAUDIEN;
    }

    public int getDTUONG_KHANG() {
        return DTUONG_KHANG;
    }

    public void setDTUONG_KHANG(int dTUONG_KHANG) {
        DTUONG_KHANG = dTUONG_KHANG;
    }

    public int getTINH_TRANG() {
        return TINH_TRANG;
    }

    public void setTINH_TRANG(int tINH_TRANG) {
        TINH_TRANG = tINH_TRANG;
    }

    public EsspEntityPhuongAn() {

    }

    public EsspEntityPhuongAn(int ID, int HOSO_ID,
                              String DODEM_CTO_A, String DODEM_CTO_V, String DODEM_CTO_TI,
                              int VTRI_CTO, String COT_SO, String VI_TRI_KHAC, int HTHUC_TTOAN, String DIEM_DAUDIEN,
                              int TTRANG_SDUNG_DIEN, int HTHUC_GCS, String HTK, int TINH_TRANG) {
        this.ID = ID;
        this.HOSO_ID = HOSO_ID;
        this.DODEM_CTO_A = DODEM_CTO_A;
        this.DODEM_CTO_V = DODEM_CTO_V;
        this.DODEM_CTO_TI = DODEM_CTO_TI;
        this.VTRI_CTO = VTRI_CTO;
        this.COT_SO = COT_SO;
        this.VI_TRI_KHAC = VI_TRI_KHAC;
        this.HTHUC_TTOAN = HTHUC_TTOAN;
        this.DIEM_DAUDIEN = DIEM_DAUDIEN;
        this.TTRANG_SDUNG_DIEN = TTRANG_SDUNG_DIEN;
        this.HTHUC_GCS = HTHUC_GCS;
        this.HTK = HTK;
        this.TINH_TRANG = TINH_TRANG;
    }

}
