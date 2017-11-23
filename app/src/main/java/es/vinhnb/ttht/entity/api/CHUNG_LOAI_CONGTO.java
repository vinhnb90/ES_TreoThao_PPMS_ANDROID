package es.vinhnb.ttht.entity.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by VinhNB on 11/23/2017.
 */

public class CHUNG_LOAI_CONGTO implements Parcelable{
    @SerializedName("MA_CLOAI")
    public String MA_CLOAI ;

    @SerializedName("TEN_LOAI_CTO")
    public String TEN_LOAI_CTO ;

    @SerializedName("MO_TA")
    public String MO_TA ;

    @SerializedName("SO_PHA")
    public String SO_PHA ;

    @SerializedName("SO_DAY")
    public String SO_DAY ;

    @SerializedName("HS_NHAN")
    public int HS_NHAN ;

    @SerializedName("SO_CS")
    public String SO_CS ;

    @SerializedName("CAP_CXAC_P")
    public String CAP_CXAC_P ;

    @SerializedName("CAP_CXAC_Q")
    public String CAP_CXAC_Q ;

    @SerializedName("DONG_DIEN")
    public String DONG_DIEN ;

    @SerializedName("DIEN_AP")
    public String DIEN_AP ;

    @SerializedName("VH_CONG")
    public String VH_CONG ;

    @SerializedName("MA_NUOC")
    public String MA_NUOC ;

    @SerializedName("MA_HANG")
    public String MA_HANG ;

    @SerializedName("HANGSO_K")
    public String HANGSO_K ;

    @SerializedName("PTHUC_DOXA")
    public String PTHUC_DOXA ;

    @SerializedName("TEN_NUOC")
    public String TEN_NUOC ;

    public CHUNG_LOAI_CONGTO(String MA_CLOAI, String TEN_LOAI_CTO, String MO_TA, String SO_PHA, String SO_DAY, int HS_NHAN, String SO_CS, String CAP_CXAC_P, String CAP_CXAC_Q, String DONG_DIEN, String DIEN_AP, String VH_CONG, String MA_NUOC, String MA_HANG, String HANGSO_K, String PTHUC_DOXA, String TEN_NUOC) {
        this.MA_CLOAI = MA_CLOAI;
        this.TEN_LOAI_CTO = TEN_LOAI_CTO;
        this.MO_TA = MO_TA;
        this.SO_PHA = SO_PHA;
        this.SO_DAY = SO_DAY;
        this.HS_NHAN = HS_NHAN;
        this.SO_CS = SO_CS;
        this.CAP_CXAC_P = CAP_CXAC_P;
        this.CAP_CXAC_Q = CAP_CXAC_Q;
        this.DONG_DIEN = DONG_DIEN;
        this.DIEN_AP = DIEN_AP;
        this.VH_CONG = VH_CONG;
        this.MA_NUOC = MA_NUOC;
        this.MA_HANG = MA_HANG;
        this.HANGSO_K = HANGSO_K;
        this.PTHUC_DOXA = PTHUC_DOXA;
        this.TEN_NUOC = TEN_NUOC;
    }

    protected CHUNG_LOAI_CONGTO(Parcel in) {
        MA_CLOAI = in.readString();
        TEN_LOAI_CTO = in.readString();
        MO_TA = in.readString();
        SO_PHA = in.readString();
        SO_DAY = in.readString();
        HS_NHAN = in.readInt();
        SO_CS = in.readString();
        CAP_CXAC_P = in.readString();
        CAP_CXAC_Q = in.readString();
        DONG_DIEN = in.readString();
        DIEN_AP = in.readString();
        VH_CONG = in.readString();
        MA_NUOC = in.readString();
        MA_HANG = in.readString();
        HANGSO_K = in.readString();
        PTHUC_DOXA = in.readString();
        TEN_NUOC = in.readString();
    }

    public static final Creator<CHUNG_LOAI_CONGTO> CREATOR = new Creator<CHUNG_LOAI_CONGTO>() {
        @Override
        public CHUNG_LOAI_CONGTO createFromParcel(Parcel in) {
            return new CHUNG_LOAI_CONGTO(in);
        }

        @Override
        public CHUNG_LOAI_CONGTO[] newArray(int size) {
            return new CHUNG_LOAI_CONGTO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(MA_CLOAI);
        parcel.writeString(TEN_LOAI_CTO);
        parcel.writeString(MO_TA);
        parcel.writeString(SO_PHA);
        parcel.writeString(SO_DAY);
        parcel.writeInt(HS_NHAN);
        parcel.writeString(SO_CS);
        parcel.writeString(CAP_CXAC_P);
        parcel.writeString(CAP_CXAC_Q);
        parcel.writeString(DONG_DIEN);
        parcel.writeString(DIEN_AP);
        parcel.writeString(VH_CONG);
        parcel.writeString(MA_NUOC);
        parcel.writeString(MA_HANG);
        parcel.writeString(HANGSO_K);
        parcel.writeString(PTHUC_DOXA);
        parcel.writeString(TEN_NUOC);
    }
}
