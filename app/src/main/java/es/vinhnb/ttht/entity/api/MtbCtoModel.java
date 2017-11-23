package es.vinhnb.ttht.entity.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by VinhNB on 11/22/2017.
 */

public class MtbCtoModel implements Parcelable{
    @SerializedName("MA_DVIQLY")
    public String MA_DVIQLY;

    @SerializedName("ID_BBAN_TRTH")
    public int ID_BBAN_TRTH;

    @SerializedName("MA_CTO")
    public String MA_CTO;

    @SerializedName("SO_CTO")
    public String SO_CTO;

    @SerializedName("LAN")
    public int LAN;

    @SerializedName("MA_BDONG")
    public String MA_BDONG;

    @SerializedName("NGAY_BDONG")
    public String NGAY_BDONG;

    @SerializedName("MA_CLOAI")
    public String MA_CLOAI;

    @SerializedName("LOAI_CTO")
    public String LOAI_CTO;

    @SerializedName("VTRI_TREO")
    public int VTRI_TREO;

    @SerializedName("MO_TA_VTRI_TREO")
    public String MO_TA_VTRI_TREO;

    @SerializedName("MA_SOCBOOC")

    public String MA_SOCBOOC;

    @SerializedName("SO_VIENCBOOC")
    public int SO_VIENCBOOC;

    @SerializedName("MA_SOCHOM")
    public String MA_SOCHOM;

    @SerializedName("SO_VIENCHOM")
    public int SO_VIENCHOM;

    @SerializedName("HS_NHAN")
    public int HS_NHAN;

    @SerializedName("NGAY_TAO")
    public String NGAY_TAO;

    @SerializedName("NGUOI_TAO")
    public String NGUOI_TAO;

    @SerializedName("NGAY_SUA")
    public String NGAY_SUA;

    @SerializedName("NGUOI_SUA")
    public String NGUOI_SUA;

    @SerializedName("MA_CNANG")
    public String MA_CNANG;

    @SerializedName("SO_TU")
    public String SO_TU;

    @SerializedName("SO_TI")
    public String SO_TI;

    @SerializedName("SO_COT")
    public String SO_COT;

    @SerializedName("SO_HOM")
    public String SO_HOM;

    @SerializedName("CHI_SO")
    public String CHI_SO;

    @SerializedName("NGAY_KDINH")
    public String NGAY_KDINH;

    @SerializedName("NAM_SX")
    public String NAM_SX;

    @SerializedName("TEM_CQUANG")
    public String TEM_CQUANG;

    @SerializedName("MA_CHIKDINH")
    public String MA_CHIKDINH;

    @SerializedName("MA_TEM")
    public String MA_TEM;

    @SerializedName("SOVIEN_CHIKDINH")
    public int SOVIEN_CHIKDINH;

    @SerializedName("DIEN_AP")
    public String DIEN_AP;

    @SerializedName("DONG_DIEN")
    public String DONG_DIEN;

    @SerializedName("HANGSO_K")
    public String HANGSO_K;

    @SerializedName("MA_NUOC")
    public String MA_NUOC;

    @SerializedName("TEN_NUOC")
    public String TEN_NUOC;

    @SerializedName("SOVIEN_CHIHOP")
    public int SOVIEN_CHIHOP;

    @SerializedName("SO_KIM_NIEM_CHI")
    public String SO_KIM_NIEM_CHI;

    @SerializedName("MA_CHIHOP")
    public String MA_CHIHOP;

    @SerializedName("TTRANG_NPHONG")
    public String TTRANG_NPHONG;

    @SerializedName("TEN_LOAI_CTO")
    public String TEN_LOAI_CTO;

    @SerializedName("PHUONG_THUC_DO_XA")
    public String PHUONG_THUC_DO_XA;

    @SerializedName("ID_CHITIET_CTO")
    public int ID_CHITIET_CTO;

    @SerializedName("ID_BBAN_TUTI")
    public int ID_BBAN_TUTI;

    @SerializedName("LOAI_HOM")
    public int LOAI_HOM ;

    public MtbCtoModel( String SO_COT, String SO_HOM, String CHI_SO, String NGAY_KDINH, String NAM_SX, String TEM_CQUANG, String MA_CHIKDINH, String MA_TEM, int SOVIEN_CHIKDINH, String DIEN_AP, String DONG_DIEN, String HANGSO_K, String MA_NUOC, String TEN_NUOC, int SOVIEN_CHIHOP, String SO_KIM_NIEM_CHI, String MA_CHIHOP, String TTRANG_NPHONG, String TEN_LOAI_CTO, String PHUONG_THUC_DO_XA, int ID_CHITIET_CTO, int ID_BBAN_TUTI, int LOAI_HOM) {
        this.MA_DVIQLY = MA_DVIQLY;
        this.ID_BBAN_TRTH = ID_BBAN_TRTH;
        this.MA_CTO = MA_CTO;
        this.SO_CTO = SO_CTO;
        this.LAN = LAN;
        this.MA_BDONG = MA_BDONG;
        this.NGAY_BDONG = NGAY_BDONG;
        this.MA_CLOAI = MA_CLOAI;
        this.LOAI_CTO = LOAI_CTO;
        this.VTRI_TREO = VTRI_TREO;
        this.MO_TA_VTRI_TREO = MO_TA_VTRI_TREO;
        this.MA_SOCBOOC = MA_SOCBOOC;
        this.SO_VIENCBOOC = SO_VIENCBOOC;
        this.MA_SOCHOM = MA_SOCHOM;
        this.SO_VIENCHOM = SO_VIENCHOM;
        this.HS_NHAN = HS_NHAN;
        this.NGAY_TAO = NGAY_TAO;
        this.NGUOI_TAO = NGUOI_TAO;
        this.NGAY_SUA = NGAY_SUA;
        this.NGUOI_SUA = NGUOI_SUA;
        this.MA_CNANG = MA_CNANG;
        this.SO_TU = SO_TU;
        this.SO_TI = SO_TI;
        this.SO_COT = SO_COT;
        this.SO_HOM = SO_HOM;
        this.CHI_SO = CHI_SO;
        this.NGAY_KDINH = NGAY_KDINH;
        this.NAM_SX = NAM_SX;
        this.TEM_CQUANG = TEM_CQUANG;
        this.MA_CHIKDINH = MA_CHIKDINH;
        this.MA_TEM = MA_TEM;
        this.SOVIEN_CHIKDINH = SOVIEN_CHIKDINH;
        this.DIEN_AP = DIEN_AP;
        this.DONG_DIEN = DONG_DIEN;
        this.HANGSO_K = HANGSO_K;
        this.MA_NUOC = MA_NUOC;
        this.TEN_NUOC = TEN_NUOC;
        this.SOVIEN_CHIHOP = SOVIEN_CHIHOP;
        this.SO_KIM_NIEM_CHI = SO_KIM_NIEM_CHI;
        this.MA_CHIHOP = MA_CHIHOP;
        this.TTRANG_NPHONG = TTRANG_NPHONG;
        this.TEN_LOAI_CTO = TEN_LOAI_CTO;
        this.PHUONG_THUC_DO_XA = PHUONG_THUC_DO_XA;
        this.ID_CHITIET_CTO = ID_CHITIET_CTO;
        this.ID_BBAN_TUTI = ID_BBAN_TUTI;
        this.LOAI_HOM = LOAI_HOM;
    }

    protected MtbCtoModel(Parcel in) {
        MA_DVIQLY = in.readString();
        ID_BBAN_TRTH = in.readInt();
        MA_CTO = in.readString();
        SO_CTO = in.readString();
        LAN = in.readInt();
        MA_BDONG = in.readString();
        NGAY_BDONG = in.readString();
        MA_CLOAI = in.readString();
        LOAI_CTO = in.readString();
        VTRI_TREO = in.readInt();
        MO_TA_VTRI_TREO = in.readString();
        MA_SOCBOOC = in.readString();
        SO_VIENCBOOC = in.readInt();
        MA_SOCHOM = in.readString();
        SO_VIENCHOM = in.readInt();
        HS_NHAN = in.readInt();
        NGAY_TAO = in.readString();
        NGUOI_TAO = in.readString();
        NGAY_SUA = in.readString();
        NGUOI_SUA = in.readString();
        MA_CNANG = in.readString();
        SO_TU = in.readString();
        SO_TI = in.readString();
        SO_COT = in.readString();
        SO_HOM = in.readString();
        CHI_SO = in.readString();
        NGAY_KDINH = in.readString();
        NAM_SX = in.readString();
        TEM_CQUANG = in.readString();
        MA_CHIKDINH = in.readString();
        MA_TEM = in.readString();
        SOVIEN_CHIKDINH = in.readInt();
        DIEN_AP = in.readString();
        DONG_DIEN = in.readString();
        HANGSO_K = in.readString();
        MA_NUOC = in.readString();
        TEN_NUOC = in.readString();
        SOVIEN_CHIHOP = in.readInt();
        SO_KIM_NIEM_CHI = in.readString();
        MA_CHIHOP = in.readString();
        TTRANG_NPHONG = in.readString();
        TEN_LOAI_CTO = in.readString();
        PHUONG_THUC_DO_XA = in.readString();
        ID_CHITIET_CTO = in.readInt();
        ID_BBAN_TUTI = in.readInt();
        LOAI_HOM = in.readInt();
    }

    public static final Creator<MtbCtoModel> CREATOR = new Creator<MtbCtoModel>() {
        @Override
        public MtbCtoModel createFromParcel(Parcel in) {
            return new MtbCtoModel(in);
        }

        @Override
        public MtbCtoModel[] newArray(int size) {
            return new MtbCtoModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(MA_DVIQLY);
        parcel.writeInt(ID_BBAN_TRTH);
        parcel.writeString(MA_CTO);
        parcel.writeString(SO_CTO);
        parcel.writeInt(LAN);
        parcel.writeString(MA_BDONG);
        parcel.writeString(NGAY_BDONG);
        parcel.writeString(MA_CLOAI);
        parcel.writeString(LOAI_CTO);
        parcel.writeInt(VTRI_TREO);
        parcel.writeString(MO_TA_VTRI_TREO);
        parcel.writeString(MA_SOCBOOC);
        parcel.writeInt(SO_VIENCBOOC);
        parcel.writeString(MA_SOCHOM);
        parcel.writeInt(SO_VIENCHOM);
        parcel.writeInt(HS_NHAN);
        parcel.writeString(NGAY_TAO);
        parcel.writeString(NGUOI_TAO);
        parcel.writeString(NGAY_SUA);
        parcel.writeString(NGUOI_SUA);
        parcel.writeString(MA_CNANG);
        parcel.writeString(SO_TU);
        parcel.writeString(SO_TI);
        parcel.writeString(SO_COT);
        parcel.writeString(SO_HOM);
        parcel.writeString(CHI_SO);
        parcel.writeString(NGAY_KDINH);
        parcel.writeString(NAM_SX);
        parcel.writeString(TEM_CQUANG);
        parcel.writeString(MA_CHIKDINH);
        parcel.writeString(MA_TEM);
        parcel.writeInt(SOVIEN_CHIKDINH);
        parcel.writeString(DIEN_AP);
        parcel.writeString(DONG_DIEN);
        parcel.writeString(HANGSO_K);
        parcel.writeString(MA_NUOC);
        parcel.writeString(TEN_NUOC);
        parcel.writeInt(SOVIEN_CHIHOP);
        parcel.writeString(SO_KIM_NIEM_CHI);
        parcel.writeString(MA_CHIHOP);
        parcel.writeString(TTRANG_NPHONG);
        parcel.writeString(TEN_LOAI_CTO);
        parcel.writeString(PHUONG_THUC_DO_XA);
        parcel.writeInt(ID_CHITIET_CTO);
        parcel.writeInt(ID_BBAN_TUTI);
        parcel.writeInt(LOAI_HOM);
    }
}
