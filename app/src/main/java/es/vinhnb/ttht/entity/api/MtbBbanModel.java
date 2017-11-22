package es.vinhnb.ttht.entity.api;
    

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
    

/**
 * Created by VinhNB on 11/22/2017.
 */

public class MtbBbanModel implements Parcelable{
    @SerializedName("MA_DVIQLY")
    public String MA_DVIQLY;
    
    
    @SerializedName("ID_BBAN_TRTH")
    public int ID_BBAN_TRTH;
    
    @SerializedName("MA_DDO")
    public String MA_DDO;
    
    @SerializedName("SO_BBAN")
    public String SO_BBAN;
    
    @SerializedName("NGAY_TRTH")
    public String NGAY_TRTH;
    
    @SerializedName("MA_NVIEN")
    public String MA_NVIEN;
    
    @SerializedName("TEN_NVIEN_TREO_THAO")
    public String TEN_NVIEN_TREO_THAO;
    
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
    
    @SerializedName("MA_YCAU_KNAI")
    public String MA_YCAU_KNAI;
    
    @SerializedName("TRANG_THAI")
    public int TRANG_THAI;
    
    @SerializedName("GHI_CHU")
    public String GHI_CHU;
    
    @SerializedName("LOAI_BBAN")
    public String LOAI_BBAN;
    
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
    
    @SerializedName("MA_HDONG")
    public String MA_HDONG;
    
    @SerializedName("ID_BBAN_CONGTO")
    public int ID_BBAN_CONGTO;
    
    @SerializedName("LY_DO_TREO_THAO")
    public String LY_DO_TREO_THAO;
    
    @SerializedName("MA_LDO")
    public String MA_LDO;
    
    @SerializedName("MA_KHANG")
    public String MA_KHANG;

    public MtbBbanModel(String MA_DVIQLY, int ID_BBAN_TRTH, String MA_DDO, String SO_BBAN, String NGAY_TRTH, String MA_NVIEN, String TEN_NVIEN_TREO_THAO, String NGAY_TAO, String NGUOI_TAO, String NGAY_SUA, String NGUOI_SUA, String MA_CNANG, String MA_YCAU_KNAI, int TRANG_THAI, String GHI_CHU, String LOAI_BBAN, String TEN_KHANG, String DCHI_HDON, String DTHOAI, String MA_GCS_CTO, String MA_TRAM, String MA_HDONG, int ID_BBAN_CONGTO, String LY_DO_TREO_THAO, String MA_LDO, String MA_KHANG) {
        this.MA_DVIQLY = MA_DVIQLY;
        this.ID_BBAN_TRTH = ID_BBAN_TRTH;
        this.MA_DDO = MA_DDO;
        this.SO_BBAN = SO_BBAN;
        this.NGAY_TRTH = NGAY_TRTH;
        this.MA_NVIEN = MA_NVIEN;
        this.TEN_NVIEN_TREO_THAO = TEN_NVIEN_TREO_THAO;
        this.NGAY_TAO = NGAY_TAO;
        this.NGUOI_TAO = NGUOI_TAO;
        this.NGAY_SUA = NGAY_SUA;
        this.NGUOI_SUA = NGUOI_SUA;
        this.MA_CNANG = MA_CNANG;
        this.MA_YCAU_KNAI = MA_YCAU_KNAI;
        this.TRANG_THAI = TRANG_THAI;
        this.GHI_CHU = GHI_CHU;
        this.LOAI_BBAN = LOAI_BBAN;
        this.TEN_KHANG = TEN_KHANG;
        this.DCHI_HDON = DCHI_HDON;
        this.DTHOAI = DTHOAI;
        this.MA_GCS_CTO = MA_GCS_CTO;
        this.MA_TRAM = MA_TRAM;
        this.MA_HDONG = MA_HDONG;
        this.ID_BBAN_CONGTO = ID_BBAN_CONGTO;
        this.LY_DO_TREO_THAO = LY_DO_TREO_THAO;
        this.MA_LDO = MA_LDO;
        this.MA_KHANG = MA_KHANG;
    }

    protected MtbBbanModel(Parcel in) {
        MA_DVIQLY = in.readString();
        ID_BBAN_TRTH = in.readInt();
        MA_DDO = in.readString();
        SO_BBAN = in.readString();
        NGAY_TRTH = in.readString();
        MA_NVIEN = in.readString();
        TEN_NVIEN_TREO_THAO = in.readString();
        NGAY_TAO = in.readString();
        NGUOI_TAO = in.readString();
        NGAY_SUA = in.readString();
        NGUOI_SUA = in.readString();
        MA_CNANG = in.readString();
        MA_YCAU_KNAI = in.readString();
        TRANG_THAI = in.readInt();
        GHI_CHU = in.readString();
        LOAI_BBAN = in.readString();
        TEN_KHANG = in.readString();
        DCHI_HDON = in.readString();
        DTHOAI = in.readString();
        MA_GCS_CTO = in.readString();
        MA_TRAM = in.readString();
        MA_HDONG = in.readString();
        ID_BBAN_CONGTO = in.readInt();
        LY_DO_TREO_THAO = in.readString();
        MA_LDO = in.readString();
        MA_KHANG = in.readString();
    }

    public static final Creator<MtbBbanModel> CREATOR = new Creator<MtbBbanModel>() {
        @Override
        public MtbBbanModel createFromParcel(Parcel in) {
            return new MtbBbanModel(in);
        }

        @Override
        public MtbBbanModel[] newArray(int size) {
            return new MtbBbanModel[size];
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
        parcel.writeString(MA_DDO);
        parcel.writeString(SO_BBAN);
        parcel.writeString(NGAY_TRTH);
        parcel.writeString(MA_NVIEN);
        parcel.writeString(TEN_NVIEN_TREO_THAO);
        parcel.writeString(NGAY_TAO);
        parcel.writeString(NGUOI_TAO);
        parcel.writeString(NGAY_SUA);
        parcel.writeString(NGUOI_SUA);
        parcel.writeString(MA_CNANG);
        parcel.writeString(MA_YCAU_KNAI);
        parcel.writeInt(TRANG_THAI);
        parcel.writeString(GHI_CHU);
        parcel.writeString(LOAI_BBAN);
        parcel.writeString(TEN_KHANG);
        parcel.writeString(DCHI_HDON);
        parcel.writeString(DTHOAI);
        parcel.writeString(MA_GCS_CTO);
        parcel.writeString(MA_TRAM);
        parcel.writeString(MA_HDONG);
        parcel.writeInt(ID_BBAN_CONGTO);
        parcel.writeString(LY_DO_TREO_THAO);
        parcel.writeString(MA_LDO);
        parcel.writeString(MA_KHANG);
    }
}
