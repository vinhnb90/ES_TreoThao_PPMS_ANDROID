package com.es.tungnv.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by VinhNB_Local on 12/8/2016.
 */

public class TthtBBanTuTiEntity implements Parcelable {
    private String MA_DVIQLY;
    private String MA_DDO;
    private int ID_BBAN_TUTI;
    private String SO_BBAN;
    private String NGAY_TRTH;
    private String MA_NVIEN;
    private int TRANG_THAI;
    private String TEN_KHANG;
    private String DCHI_HDON;
    private String DTHOAI;
    private String MA_GCS_CTO;
    private String MA_TRAM;
    private String LY_DO_TREO_THAO;
    private String MA_KHANG;
    private int ID_BBAN_WEB_TUTI;
    private String NVIEN_KCHI;

    public TthtBBanTuTiEntity() {
    }

    protected TthtBBanTuTiEntity(Parcel in) {
        MA_DVIQLY = in.readString();
        MA_DDO = in.readString();
        ID_BBAN_TUTI = in.readInt();
        SO_BBAN = in.readString();
        NGAY_TRTH = in.readString();
        MA_NVIEN = in.readString();
        TRANG_THAI = in.readInt();
        TEN_KHANG = in.readString();
        DCHI_HDON = in.readString();
        DTHOAI = in.readString();
        MA_GCS_CTO = in.readString();
        MA_TRAM = in.readString();
        LY_DO_TREO_THAO = in.readString();
        MA_KHANG = in.readString();
        ID_BBAN_WEB_TUTI = in.readInt();
        NVIEN_KCHI = in.readString();
    }

    public static final Creator<TthtBBanTuTiEntity> CREATOR = new Creator<TthtBBanTuTiEntity>() {
        @Override
        public TthtBBanTuTiEntity createFromParcel(Parcel in) {
            return new TthtBBanTuTiEntity(in);
        }

        @Override
        public TthtBBanTuTiEntity[] newArray(int size) {
            return new TthtBBanTuTiEntity[size];
        }
    };

    public String getMA_DVIQLY() {
        return MA_DVIQLY;
    }

    public void setMA_DVIQLY(String MA_DVIQLY) {
        this.MA_DVIQLY = MA_DVIQLY;
    }

    public int getID_BBAN_TUTI() {
        return ID_BBAN_TUTI;
    }

    public void setID_BBAN_TUTI(int ID_BBAN_TUTI) {
        this.ID_BBAN_TUTI = ID_BBAN_TUTI;
    }

    public String getMA_DDO() {
        return MA_DDO;
    }

    public void setMA_DDO(String MA_DDO) {
        this.MA_DDO = MA_DDO;
    }

    public String getSO_BBAN() {
        return SO_BBAN;
    }

    public void setSO_BBAN(String SO_BBAN) {
        this.SO_BBAN = SO_BBAN;
    }

    public String getNGAY_TRTH() {
        return NGAY_TRTH;
    }

    public void setNGAY_TRTH(String NGAY_TRTH) {
        this.NGAY_TRTH = NGAY_TRTH;
    }

    public String getMA_NVIEN() {
        return MA_NVIEN;
    }

    public void setMA_NVIEN(String MA_NVIEN) {
        this.MA_NVIEN = MA_NVIEN;
    }

    public int getTRANG_THAI() {
        return TRANG_THAI;
    }

    public void setTRANG_THAI(int TRANG_THAI) {
        this.TRANG_THAI = TRANG_THAI;
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

    public String getDTHOAI() {
        return DTHOAI;
    }

    public void setDTHOAI(String DTHOAI) {
        this.DTHOAI = DTHOAI;
    }

    public String getMA_GCS_CTO() {
        return MA_GCS_CTO;
    }

    public void setMA_GCS_CTO(String MA_GCS_CTO) {
        this.MA_GCS_CTO = MA_GCS_CTO;
    }

    public String getMA_TRAM() {
        return MA_TRAM;
    }

    public void setMA_TRAM(String MA_TRAM) {
        this.MA_TRAM = MA_TRAM;
    }

    public String getLY_DO_TREO_THAO() {
        return LY_DO_TREO_THAO;
    }

    public void setLY_DO_TREO_THAO(String LY_DO_TREO_THAO) {
        this.LY_DO_TREO_THAO = LY_DO_TREO_THAO;
    }

    public String getMA_KHANG() {
        return MA_KHANG;
    }

    public void setMA_KHANG(String MA_KHANG) {
        this.MA_KHANG = MA_KHANG;
    }

    public int getID_BBAN_WEB_TUTI() {
        return ID_BBAN_WEB_TUTI;
    }

    public void setID_BBAN_WEB_TUTI(int ID_BBAN_WEB_TUTI) {
        this.ID_BBAN_WEB_TUTI = ID_BBAN_WEB_TUTI;
    }

    public String getNVIEN_KCHI() {
        return NVIEN_KCHI;
    }

    public void setNVIEN_KCHI(String NVIEN_KCHI) {
        this.NVIEN_KCHI = NVIEN_KCHI;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(MA_DVIQLY);
        parcel.writeString(MA_DDO);
        parcel.writeInt(ID_BBAN_TUTI);
        parcel.writeString(SO_BBAN);
        parcel.writeString(NGAY_TRTH);
        parcel.writeString(MA_NVIEN);
        parcel.writeInt(TRANG_THAI);
        parcel.writeString(TEN_KHANG);
        parcel.writeString(DCHI_HDON);
        parcel.writeString(DTHOAI);
        parcel.writeString(MA_GCS_CTO);
        parcel.writeString(MA_TRAM);
        parcel.writeString(LY_DO_TREO_THAO);
        parcel.writeString(MA_KHANG);
        parcel.writeInt(ID_BBAN_WEB_TUTI);
        parcel.writeString(NVIEN_KCHI);
    }
}
