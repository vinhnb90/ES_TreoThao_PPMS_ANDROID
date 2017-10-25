package com.es.tungnv.entity;

import java.util.LinkedHashMap;

/**
 * Created by TUNGNV on 3/31/2016.
 */
public class EsspEntityVatTu {

    private String MA_VTU;
    private String TEN_VTU;
    private String MA_LOAI_CPHI;
    private String DON_GIA;
    private String DVI_TINH;
    private String DON_GIA_KH;
    private int LOAI;
    private int CHUNG_LOAI;
    private String DG_TRONGOI;
    private String DG_NCONG;
    private String DG_VTU;
    private String DG_MTCONG;

    public String getMA_VTU() {
        return MA_VTU;
    }

    public void setMA_VTU(String mA_VTU) {
        MA_VTU = mA_VTU;
    }

    public String getTEN_VTU() {
        return TEN_VTU;
    }

    public void setTEN_VTU(String tEN_VTU) {
        TEN_VTU = tEN_VTU;
    }

    public String getMA_LOAI_CPHI() {
        return MA_LOAI_CPHI;
    }

    public void setMA_LOAI_CPHI(String mA_LOAI_CPHI) {
        MA_LOAI_CPHI = mA_LOAI_CPHI;
    }

    public String getDON_GIA() {
        return DON_GIA;
    }

    public void setDON_GIA(String dON_GIA) {
        DON_GIA = dON_GIA;
    }

    public String getDVI_TINH() {
        return DVI_TINH;
    }

    public void setDVI_TINH(String dVI_TINH) {
        DVI_TINH = dVI_TINH;
    }

    public String getDON_GIA_KH() {
        return DON_GIA_KH;
    }

    public void setDON_GIA_KH(String dON_GIA_KH) {
        DON_GIA_KH = dON_GIA_KH;
    }

    public int getLOAI() {
        return LOAI;
    }

    public void setLOAI(int lOAI) {
        LOAI = lOAI;
    }

    public int getCHUNG_LOAI() {
        return CHUNG_LOAI;
    }

    public void setCHUNG_LOAI(int cHUNG_LOAI) {
        CHUNG_LOAI = cHUNG_LOAI;
    }

    public String getDG_TRONGOI() {
        return DG_TRONGOI;
    }

    public void setDG_TRONGOI(String DG_TRONGOI) {
        this.DG_TRONGOI = DG_TRONGOI;
    }

    public String getDG_NCONG() {
        return DG_NCONG;
    }

    public void setDG_NCONG(String DG_NCONG) {
        this.DG_NCONG = DG_NCONG;
    }

    public String getDG_VTU() {
        return DG_VTU;
    }

    public void setDG_VTU(String DG_VTU) {
        this.DG_VTU = DG_VTU;
    }

    public String getDG_MTCONG() {
        return DG_MTCONG;
    }

    public void setDG_MTCONG(String DG_MTCONG) {
        this.DG_MTCONG = DG_MTCONG;
    }

    public EsspEntityVatTu(){

    }

    public EsspEntityVatTu(String MA_VTU, String TEN_VTU, String MA_LOAI_CPHI, String DON_GIA, String DVI_TINH, String DON_GIA_KH, int LOAI, int CHUNG_LOAI){
        this.MA_VTU = MA_VTU;
        this.TEN_VTU = TEN_VTU;
        this.MA_LOAI_CPHI = MA_LOAI_CPHI;
        this.DON_GIA = DON_GIA;
        this.DVI_TINH = DVI_TINH;
        this.DON_GIA_KH = DON_GIA_KH;
        this.LOAI = LOAI;
        this.CHUNG_LOAI = CHUNG_LOAI;
    }

    public LinkedHashMap<String, String> ToLinkedHashMap(){
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put("MA_VTU", MA_VTU);
        map.put("TEN_VTU",  TEN_VTU);
        map.put("MA_LOAI_CPHI", MA_LOAI_CPHI);
        map.put("DON_GIA", DON_GIA);
        map.put("DVI_TINH", DVI_TINH);
        map.put("DON_GIA_KH", DON_GIA_KH);
        map.put("LOAI", "" + LOAI);
        map.put("CHUNG_LOAI", "" + CHUNG_LOAI);
        return map;
    }

}
