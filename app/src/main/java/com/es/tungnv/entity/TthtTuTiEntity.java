package com.es.tungnv.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by VinhNB_Local on 12/8/2016.
 */

public class TthtTuTiEntity implements Parcelable {
    private int ID;
    private String MA_CLOAI;
    private String LOAI_TU_TI;
    private String MO_TA;
    private int SO_PHA;
    private String TYSO_DAU;
    private int CAP_CXAC;
    private int CAP_DAP;
    private String MA_NUOC;
    private String MA_HANG;
    private int TRANG_THAI;
    private String IS_TU;
    private int ID_BBAN_TUTI;
    private int ID_CHITIET_TUTI;
    private String SO_TU_TI;
    private String NUOC_SX;
    private String SO_TEM_KDINH;
    private String NGAY_KDINH;
    private String MA_CHI_KDINH;
    private String MA_CHI_HOP_DDAY;

    private int SO_VONG_THANH_CAI;
    private String TYSO_BIEN;
    private String MA_BDONG;
    private String MA_DVIQLY;

    private String TEN_ANH_TU_TI;
    private String TEN_ANH_MACH_NHI_THU;
    private String TEN_ANH_MACH_CONG_TO;

    public TthtTuTiEntity() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getMA_CLOAI() {
        return MA_CLOAI;
    }

    public void setMA_CLOAI(String MA_CLOAI) {
        this.MA_CLOAI = MA_CLOAI;
    }

    public String getLOAI_TU_TI() {
        return LOAI_TU_TI;
    }

    public void setLOAI_TU_TI(String LOAI_TU_TI) {
        this.LOAI_TU_TI = LOAI_TU_TI;
    }

    public String getMO_TA() {
        return MO_TA;
    }

    public void setMO_TA(String MO_TA) {
        this.MO_TA = MO_TA;
    }

    public int getSO_PHA() {
        return SO_PHA;
    }

    public void setSO_PHA(int SO_PHA) {
        this.SO_PHA = SO_PHA;
    }

    public String getTYSO_DAU() {
        return TYSO_DAU;
    }

    public void setTYSO_DAU(String TYSO_DAU) {
        this.TYSO_DAU = TYSO_DAU;
    }

    public int getCAP_CXAC() {
        return CAP_CXAC;
    }

    public void setCAP_CXAC(int CAP_CXAC) {
        this.CAP_CXAC = CAP_CXAC;
    }

    public int getCAP_DAP() {
        return CAP_DAP;
    }

    public void setCAP_DAP(int CAP_DAP) {
        this.CAP_DAP = CAP_DAP;
    }

    public String getMA_NUOC() {
        return MA_NUOC;
    }

    public void setMA_NUOC(String MA_NUOC) {
        this.MA_NUOC = MA_NUOC;
    }

    public String getMA_HANG() {
        return MA_HANG;
    }

    public void setMA_HANG(String MA_HANG) {
        this.MA_HANG = MA_HANG;
    }

    public int getTRANG_THAI() {
        return TRANG_THAI;
    }

    public void setTRANG_THAI(int TRANG_THAI) {
        this.TRANG_THAI = TRANG_THAI;
    }

    public String getIS_TU() {
        return IS_TU;
    }

    public void setIS_TU(String IS_TU) {
        this.IS_TU = IS_TU;
    }

    public int getID_BBAN_TUTI() {
        return ID_BBAN_TUTI;
    }

    public void setID_BBAN_TUTI(int ID_BBAN_TUTI) {
        this.ID_BBAN_TUTI = ID_BBAN_TUTI;
    }

    public int getID_CHITIET_TUTI() {
        return ID_CHITIET_TUTI;
    }

    public void setID_CHITIET_TUTI(int ID_CHITIET_TUTI) {
        this.ID_CHITIET_TUTI = ID_CHITIET_TUTI;
    }

    public String getSO_TU_TI() {
        return SO_TU_TI;
    }

    public void setSO_TU_TI(String SO_TU_TI) {
        this.SO_TU_TI = SO_TU_TI;
    }

    public String getNUOC_SX() {
        return NUOC_SX;
    }

    public void setNUOC_SX(String NUOC_SX) {
        this.NUOC_SX = NUOC_SX;
    }

    public String getSO_TEM_KDINH() {
        return SO_TEM_KDINH;
    }

    public void setSO_TEM_KDINH(String SO_TEM_KDINH) {
        this.SO_TEM_KDINH = SO_TEM_KDINH;
    }

    public String getNGAY_KDINH() {
        return NGAY_KDINH;
    }

    public void setNGAY_KDINH(String NGAY_KDINH) {
        this.NGAY_KDINH = NGAY_KDINH;
    }

    public String getMA_CHI_KDINH() {
        return MA_CHI_KDINH;
    }

    public void setMA_CHI_KDINH(String MA_CHI_KDINH) {
        this.MA_CHI_KDINH = MA_CHI_KDINH;
    }

    public String getMA_CHI_HOP_DDAY() {
        return MA_CHI_HOP_DDAY;
    }

    public void setMA_CHI_HOP_DDAY(String MA_CHI_HOP_DDAY) {
        this.MA_CHI_HOP_DDAY = MA_CHI_HOP_DDAY;
    }

    public int getSO_VONG_THANH_CAI() {
        return SO_VONG_THANH_CAI;
    }

    public void setSO_VONG_THANH_CAI(int SO_VONG_THANH_CAI) {
        this.SO_VONG_THANH_CAI = SO_VONG_THANH_CAI;
    }

    public String getTYSO_BIEN() {
        return TYSO_BIEN;
    }

    public void setTYSO_BIEN(String TYSO_BIEN) {
        this.TYSO_BIEN = TYSO_BIEN;
    }

    public String getMA_BDONG() {
        return MA_BDONG;
    }

    public void setMA_BDONG(String MA_BDONG) {
        this.MA_BDONG = MA_BDONG;
    }

    public String getMA_DVIQLY() {
        return MA_DVIQLY;
    }

    public void setMA_DVIQLY(String MA_DVIQLY) {
        this.MA_DVIQLY = MA_DVIQLY;
    }

    public String getTEN_ANH_TU_TI() {
        return TEN_ANH_TU_TI;
    }

    public void setTEN_ANH_TU_TI(String TEN_ANH_TU_TI) {
        this.TEN_ANH_TU_TI = TEN_ANH_TU_TI;
    }

    public String getTEN_ANH_MACH_NHI_THU() {
        return TEN_ANH_MACH_NHI_THU;
    }

    public void setTEN_ANH_MACH_NHI_THU(String TEN_ANH_MACH_NHI_THU) {
        this.TEN_ANH_MACH_NHI_THU = TEN_ANH_MACH_NHI_THU;
    }

    public String getTEN_ANH_MACH_CONG_TO() {
        return TEN_ANH_MACH_CONG_TO;
    }

    public void setTEN_ANH_MACH_CONG_TO(String TEN_ANH_MACH_CONG_TO) {
        this.TEN_ANH_MACH_CONG_TO = TEN_ANH_MACH_CONG_TO;
    }

    public static Creator<TthtTuTiEntity> getCREATOR() {
        return CREATOR;
    }

    protected TthtTuTiEntity(Parcel in) {
        ID = in.readInt();
        MA_CLOAI = in.readString();
        LOAI_TU_TI = in.readString();
        MO_TA = in.readString();
        SO_PHA = in.readInt();
        TYSO_DAU = in.readString();
        CAP_CXAC = in.readInt();
        CAP_DAP = in.readInt();
        MA_NUOC = in.readString();
        MA_HANG = in.readString();
        TRANG_THAI = in.readInt();
        IS_TU = in.readString();
        ID_BBAN_TUTI = in.readInt();
        ID_CHITIET_TUTI = in.readInt();
        SO_TU_TI = in.readString();
        NUOC_SX = in.readString();
        SO_TEM_KDINH = in.readString();
        NGAY_KDINH = in.readString();
        MA_CHI_KDINH = in.readString();
        MA_CHI_HOP_DDAY = in.readString();
        SO_VONG_THANH_CAI = in.readInt();
        TYSO_BIEN = in.readString();
        MA_BDONG = in.readString();
        MA_DVIQLY = in.readString();
        TEN_ANH_TU_TI = in.readString();
        TEN_ANH_MACH_NHI_THU = in.readString();
        TEN_ANH_MACH_CONG_TO = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(MA_CLOAI);
        dest.writeString(LOAI_TU_TI);
        dest.writeString(MO_TA);
        dest.writeInt(SO_PHA);
        dest.writeString(TYSO_DAU);
        dest.writeInt(CAP_CXAC);
        dest.writeInt(CAP_DAP);
        dest.writeString(MA_NUOC);
        dest.writeString(MA_HANG);
        dest.writeInt(TRANG_THAI);
        dest.writeString(IS_TU);
        dest.writeInt(ID_BBAN_TUTI);
        dest.writeInt(ID_CHITIET_TUTI);
        dest.writeString(SO_TU_TI);
        dest.writeString(NUOC_SX);
        dest.writeString(SO_TEM_KDINH);
        dest.writeString(NGAY_KDINH);
        dest.writeString(MA_CHI_KDINH);
        dest.writeString(MA_CHI_HOP_DDAY);
        dest.writeInt(SO_VONG_THANH_CAI);
        dest.writeString(TYSO_BIEN);
        dest.writeString(MA_BDONG);
        dest.writeString(MA_DVIQLY);
        dest.writeString(TEN_ANH_TU_TI);
        dest.writeString(TEN_ANH_MACH_NHI_THU);
        dest.writeString(TEN_ANH_MACH_CONG_TO);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TthtTuTiEntity> CREATOR = new Creator<TthtTuTiEntity>() {
        @Override
        public TthtTuTiEntity createFromParcel(Parcel in) {
            return new TthtTuTiEntity(in);
        }

        @Override
        public TthtTuTiEntity[] newArray(int size) {
            return new TthtTuTiEntity[size];
        }
    };
}
