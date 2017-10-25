package com.es.tungnv.entity;

import java.io.Serializable;

/**
 * Created by VinhNB on 4/27/2017.
 */

public class InfoPhieuTreoThao implements Serializable{
    private int ID_BBAN_TRTH;
    private String TEN_KHANG;
    private String DCHI_HDON;
    private String SO_BBAN;
    private String CHI_SO;
    private String CHI_SO_SAULAP_TUTI;
    private String MA_CTO;
    private String SO_CTO;
    private String MA_GCS_CTO;
    private String MA_TRAM;
    private int ID_CHITIET_CTO;
    private int TRANG_THAI_DU_LIEU;

    public InfoPhieuTreoThao() {
    }

    public String getSO_CTO() {
        return SO_CTO;
    }

    public void setSO_CTO(String SO_CTO) {
        this.SO_CTO = SO_CTO;
    }

    public int getID_CHITIET_CTO() {
        return ID_CHITIET_CTO;
    }

    public void setID_CHITIET_CTO(int ID_CHITIET_CTO) {
        this.ID_CHITIET_CTO = ID_CHITIET_CTO;
    }

    public int getTRANG_THAI_DU_LIEU() {
        return TRANG_THAI_DU_LIEU;
    }

    public void setTRANG_THAI_DU_LIEU(int TRANG_THAI_DU_LIEU) {
        this.TRANG_THAI_DU_LIEU = TRANG_THAI_DU_LIEU;
    }

    public String getMA_TRAM() {
        return MA_TRAM;
    }

    public void setMA_TRAM(String MA_TRAM) {
        this.MA_TRAM = MA_TRAM;
    }

    public String getMA_GCS_CTO() {
        return MA_GCS_CTO;
    }

    public void setMA_GCS_CTO(String MA_GCS_CTO) {
        this.MA_GCS_CTO = MA_GCS_CTO;
    }

    public int getID_BBAN_TRTH() {
        return ID_BBAN_TRTH;
    }

    public void setID_BBAN_TRTH(int ID_BBAN_TRTH) {
        this.ID_BBAN_TRTH = ID_BBAN_TRTH;
    }

    public String getTEN_KHANG() {
        return TEN_KHANG;
    }

    public void setTEN_KHANG(String TEN_KHANG) {
        this.TEN_KHANG = TEN_KHANG;
    }

    public String getDCHI_HDON() {
        return DCHI_HDON;
    }

    public void setDCHI_HDON(String DCHI_HDON) {
        this.DCHI_HDON = DCHI_HDON;
    }

    public String getSO_BBAN() {
        return SO_BBAN;
    }

    public void setSO_BBAN(String SO_BBAN) {
        this.SO_BBAN = SO_BBAN;
    }

    public String getCHI_SO() {
        return CHI_SO;
    }

    public void setCHI_SO(String CHI_SO) {
        this.CHI_SO = CHI_SO;
    }

    public String getCHI_SO_SAULAP_TUTI() {
        return CHI_SO_SAULAP_TUTI;
    }

    public void setCHI_SO_SAULAP_TUTI(String CHI_SO_SAULAP_TUTI) {
        this.CHI_SO_SAULAP_TUTI = CHI_SO_SAULAP_TUTI;
    }

    public String getMA_CTO() {
        return MA_CTO;
    }

    public void setMA_CTO(String MA_CTO) {
        this.MA_CTO = MA_CTO;
    }

}
