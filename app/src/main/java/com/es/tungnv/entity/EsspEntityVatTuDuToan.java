package com.es.tungnv.entity;

import java.util.LinkedHashMap;

/**
 * Created by TUNGNV on 3/31/2016.
 */
public class EsspEntityVatTuDuToan {

    private int ID;
    private String MA_VTU;
    private String TEN_VTU;
    private String MA_LOAI_CPHI;
    private String DON_GIA;
    private String DVI_TINH;
    private String DON_GIA_KH;
    private String TT_DON_GIA;
    private int LOAI;
    private int CHUNG_LOAI;
    private float SO_LUONG;
    private int SO_HUU;
    private int TT_TU_TUC;
    private int TT_THU_HOI;
    private String THANH_TIEN;
    private float HSDC_K1NC;
    private float HSDC_K2NC;
    private float HSDC_MTC;

    public float getHSDC_K1NC() {
        return HSDC_K1NC;
    }

    public void setHSDC_K1NC(float HSDC_K1NC) {
        this.HSDC_K1NC = HSDC_K1NC;
    }

    public float getHSDC_K2NC() {
        return HSDC_K2NC;
    }

    public void setHSDC_K2NC(float HSDC_K2NC) {
        this.HSDC_K2NC = HSDC_K2NC;
    }

    public float getHSDC_MTC() {
        return HSDC_MTC;
    }

    public void setHSDC_MTC(float HSDC_MTC) {
        this.HSDC_MTC = HSDC_MTC;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public void setTT_TU_TUC(int TT_TU_TUC) {
        this.TT_TU_TUC = TT_TU_TUC;
    }

    public int getTT_TU_TUC() {
        return TT_TU_TUC;
    }

    public void setTT_THU_HOI(int TT_THU_HOI) {
        this.TT_THU_HOI = TT_THU_HOI;
    }

    public int getTT_THU_HOI() {
        return TT_THU_HOI;
    }

    public void setSO_HUU(int SO_HUU) {
        this.SO_HUU = SO_HUU;
    }

    public float getSO_LUONG() {
        return SO_LUONG;
    }

    public void setSO_LUONG(float SO_LUONG) {
        this.SO_LUONG = SO_LUONG;
    }

    public int getSO_HUU() {
        return SO_HUU;
    }

    public void setTHANH_TIEN(String THANH_TIEN) {
        this.THANH_TIEN = THANH_TIEN;
    }

    public String getTHANH_TIEN() {
        return THANH_TIEN;
    }

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

    public String getTT_DON_GIA() {
        return TT_DON_GIA;
    }
    public void setTT_DON_GIA(String tT_DON_GIA) {
        TT_DON_GIA = tT_DON_GIA;
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
    public EsspEntityVatTuDuToan(){

    }

    public EsspEntityVatTuDuToan(String MA_VTU, String TEN_VTU, String MA_LOAI_CPHI, String DON_GIA, String DVI_TINH, String DON_GIA_KH,
                                 String TT_DON_GIA, int LOAI, int CHUNG_LOAI, float SO_LUONG, int SO_HUU, String THANH_TIEN){
        this.MA_VTU = MA_VTU;
        this.TEN_VTU = TEN_VTU;
        this.MA_LOAI_CPHI = MA_LOAI_CPHI;
        this.DON_GIA = DON_GIA;
        this.DVI_TINH = DVI_TINH;
        this.DON_GIA_KH = DON_GIA_KH;
        this.TT_DON_GIA = TT_DON_GIA;
        this.LOAI = LOAI;
        this.CHUNG_LOAI = CHUNG_LOAI;
        this.SO_LUONG = SO_LUONG;
        this.SO_HUU = SO_HUU;
        this.THANH_TIEN = THANH_TIEN;
    }

    public LinkedHashMap<String, String> ToLinkedHashMap(){
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put("MA_VTU", MA_VTU);
        map.put("TEN_VTU",  TEN_VTU);
        map.put("MA_LOAI_CPHI", MA_LOAI_CPHI);
        map.put("DON_GIA", DON_GIA);
        map.put("DVI_TINH", DVI_TINH);
        map.put("DON_GIA_KH", DON_GIA_KH);
        map.put("TT_DON_GIA", TT_DON_GIA);
        map.put("LOAI", "" + LOAI);
        map.put("CHUNG_LOAI", "" + CHUNG_LOAI);
        return map;
    }

}
