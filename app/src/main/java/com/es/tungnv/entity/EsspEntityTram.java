package com.es.tungnv.entity;

import java.util.LinkedHashMap;

/**
 * Created by TUNGNV on 3/31/2016.
 */
public class EsspEntityTram {

    private int D_TRAM_ID;
    private String MA_DVIQLY;
    private String MA_TRAM;
    private String TEN_TRAM;
    private String LOAI_TRAM;
    private String CSUAT_TRAM;
    private String MA_CAPDA;
    private String MA_CAPDA_RA;
    private String DINH_DANH;

    public int getD_TRAM_ID() {
        return D_TRAM_ID;
    }

    public void setD_TRAM_ID(int d_TRAM_ID) {
        D_TRAM_ID = d_TRAM_ID;
    }

    public String getMA_DVIQLY() {
        return MA_DVIQLY;
    }

    public void setMA_DVIQLY(String mA_DVIQLY) {
        MA_DVIQLY = mA_DVIQLY;
    }

    public String getMA_TRAM() {
        return MA_TRAM;
    }

    public void setMA_TRAM(String mA_TRAM) {
        MA_TRAM = mA_TRAM;
    }

    public String getTEN_TRAM() {
        return TEN_TRAM;
    }

    public void setTEN_TRAM(String tEN_TRAM) {
        TEN_TRAM = tEN_TRAM;
    }

    public String getLOAI_TRAM() {
        return LOAI_TRAM;
    }

    public void setLOAI_TRAM(String lOAI_TRAM) {
        LOAI_TRAM = lOAI_TRAM;
    }

    public String getCSUAT_TRAM() {
        return CSUAT_TRAM;
    }

    public void setCSUAT_TRAM(String cSUAT_TRAM) {
        CSUAT_TRAM = cSUAT_TRAM;
    }

    public String getMA_CAPDA() {
        return MA_CAPDA;
    }

    public void setMA_CAPDA(String mA_CAPDA) {
        MA_CAPDA = mA_CAPDA;
    }

    public String getMA_CAPDA_RA() {
        return MA_CAPDA_RA;
    }

    public void setMA_CAPDA_RA(String mA_CAPDA_RA) {
        MA_CAPDA_RA = mA_CAPDA_RA;
    }

    public String getDINH_DANH() {
        return DINH_DANH;
    }

    public void setDINH_DANH(String dINH_DANH) {
        DINH_DANH = dINH_DANH;
    }

    public EsspEntityTram() {

    }

    public EsspEntityTram(String MA_DVIQLY, String MA_TRAM, String TEN_TRAM,
                          String LOAI_TRAM, String CSUAT_TRAM, String MA_CAPDA,
                          String MA_CAPDA_RA, int D_TRAM_ID, String DINH_DANH) {
        this.MA_DVIQLY = MA_DVIQLY;
        this.MA_TRAM = MA_TRAM;
        this.TEN_TRAM = TEN_TRAM;
        this.LOAI_TRAM = LOAI_TRAM;
        this.CSUAT_TRAM = CSUAT_TRAM;
        this.MA_CAPDA = MA_CAPDA;
        this.MA_CAPDA_RA = MA_CAPDA_RA;
        this.D_TRAM_ID = D_TRAM_ID;
        this.DINH_DANH = DINH_DANH;
    }

    public LinkedHashMap<String, String> ToLinkedHashMap(){
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put("MA_DVIQLY", MA_DVIQLY);
        map.put("MA_TRAM",  MA_TRAM);
        map.put("TEN_TRAM", TEN_TRAM);
        map.put("LOAI_TRAM", LOAI_TRAM);
        map.put("CSUAT_TRAM", CSUAT_TRAM);
        map.put("MA_CAPDA", MA_CAPDA);
        map.put("MA_CAPDA_RA", MA_CAPDA_RA);
        map.put("D_TRAM_ID", "" + D_TRAM_ID);
        map.put("DINH_DANH", DINH_DANH);
        return map;
    }

}
