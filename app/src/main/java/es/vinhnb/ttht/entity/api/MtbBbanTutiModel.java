package es.vinhnb.ttht.entity.api;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by VinhNB on 11/22/2017.
 */

public class MtbBbanTutiModel implements Parcelable{
    @SerializedName("MA_DVIQLY")
    public String MA_DVIQLY;

    @SerializedName("ID_BBAN_TUTI")
    public int ID_BBAN_TUTI;

    @SerializedName("MA_DDO")
    public String MA_DDO;

    @SerializedName("SO_BBAN")
    public String SO_BBAN;

    @SerializedName("NGAY_TRTH")
    @Nullable
    public String NGAY_TRTH;

    @SerializedName("BUNDLE_MA_NVIEN")
    public String MA_NVIEN;

    @SerializedName("TRANG_THAI")
    @Nullable
    public int TRANG_THAI;

    @SerializedName("TEN_KHANG")
    public String TEN_KHANG;

    @SerializedName("DCHI_HDON")
    public String DCHI_HDON;

    @SerializedName("DTHOAI")
    public String DTHOAI;

    @SerializedName("MA_GCS_CTO")
    public String MA_GCS_CTO;

    @SerializedName("MA_TRAM")
    public String MA_TRAM;

    @SerializedName("LY_DO_TREO_THAO")
    public String LY_DO_TREO_THAO;

    @SerializedName("MA_KHANG")
    public String MA_KHANG;

    @SerializedName("ID_BBAN_WEB_TUTI")
    public int ID_BBAN_WEB_TUTI;

    @SerializedName("NVIEN_KCHI")
    public String NVIEN_KCHI;


    public MtbBbanTutiModel(String MA_DVIQLY, int ID_BBAN_TUTI, String MA_DDO, String SO_BBAN, String NGAY_TRTH, String MA_NVIEN, int TRANG_THAI, String TEN_KHANG, String DCHI_HDON, String DTHOAI, String MA_GCS_CTO, String MA_TRAM, String LY_DO_TREO_THAO, String MA_KHANG, int ID_BBAN_WEB_TUTI, String NVIEN_KCHI) {
        this.MA_DVIQLY = MA_DVIQLY;
        this.ID_BBAN_TUTI = ID_BBAN_TUTI;
        this.MA_DDO = MA_DDO;
        this.SO_BBAN = SO_BBAN;
        this.NGAY_TRTH = NGAY_TRTH;
        this.MA_NVIEN = MA_NVIEN;
        this.TRANG_THAI = TRANG_THAI;
        this.TEN_KHANG = TEN_KHANG;
        this.DCHI_HDON = DCHI_HDON;
        this.DTHOAI = DTHOAI;
        this.MA_GCS_CTO = MA_GCS_CTO;
        this.MA_TRAM = MA_TRAM;
        this.LY_DO_TREO_THAO = LY_DO_TREO_THAO;
        this.MA_KHANG = MA_KHANG;
        this.ID_BBAN_WEB_TUTI = ID_BBAN_WEB_TUTI;
        this.NVIEN_KCHI = NVIEN_KCHI;
    }

    protected MtbBbanTutiModel(Parcel in) {
        MA_DVIQLY = in.readString();
        ID_BBAN_TUTI = in.readInt();
        MA_DDO = in.readString();
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

    public static final Creator<MtbBbanTutiModel> CREATOR = new Creator<MtbBbanTutiModel>() {
        @Override
        public MtbBbanTutiModel createFromParcel(Parcel in) {
            return new MtbBbanTutiModel(in);
        }

        @Override
        public MtbBbanTutiModel[] newArray(int size) {
            return new MtbBbanTutiModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(MA_DVIQLY);
        parcel.writeInt(ID_BBAN_TUTI);
        parcel.writeString(MA_DDO);
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
