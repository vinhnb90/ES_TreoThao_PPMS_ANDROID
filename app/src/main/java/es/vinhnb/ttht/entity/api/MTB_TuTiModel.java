package es.vinhnb.ttht.entity.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by VinhNB on 11/23/2017.
 */

public class MTB_TuTiModel implements Parcelable{
    @SerializedName("MA_DVIQLY")
    public String MA_DVIQLY ;

    @SerializedName("MA_CLOAI")
    public String MA_CLOAI ;

    @SerializedName("LOAI_TU_TI")
    public String LOAI_TU_TI ;

    @SerializedName("MO_TA")
    public String MO_TA ;

    @SerializedName("SO_PHA")
    public int SO_PHA ;

    @SerializedName("TYSO_DAU")
    public String TYSO_DAU ;

    @SerializedName("CAP_CXAC")
    public int CAP_CXAC ;

    @SerializedName("CAP_DAP")
    public int CAP_DAP ;

    @SerializedName("MA_NUOC")
    public String MA_NUOC ;

    @SerializedName("MA_HANG")
    public String MA_HANG ;

    @SerializedName("TRANG_THAI")
    public int TRANG_THAI ;

    @SerializedName("IS_TU")
    public boolean IS_TU ;

    @SerializedName("ID_BBAN_TUTI")
    public int ID_BBAN_TUTI ;

    @SerializedName("ID_CHITIET_TUTI")
    public int ID_CHITIET_TUTI ;

    @SerializedName("SO_TU_TI")
    public String SO_TU_TI ;

    @SerializedName("NUOC_SX")
    public String NUOC_SX ;

    @SerializedName("SO_TEM_KDINH")
    public String SO_TEM_KDINH ;

    @SerializedName("NGAY_KDINH")
    public String NGAY_KDINH ;

    @SerializedName("MA_CHI_KDINH")
    public String MA_CHI_KDINH ;

    @SerializedName("MA_CHI_HOP_DDAY")
    public String MA_CHI_HOP_DDAY ;

    @SerializedName("SO_VONG_THANH_CAI")
    public int SO_VONG_THANH_CAI ;

    @SerializedName("TYSO_BIEN")
    public String TYSO_BIEN ;

    @SerializedName("MA_BDONG")
    public String MA_BDONG ;


    public MTB_TuTiModel(String MA_DVIQLY, String MA_CLOAI, String LOAI_TU_TI, String MO_TA, int SO_PHA, String TYSO_DAU, int CAP_CXAC, int CAP_DAP, String MA_NUOC, String MA_HANG, int TRANG_THAI, boolean IS_TU, int ID_BBAN_TUTI, int ID_CHITIET_TUTI, String SO_TU_TI, String NUOC_SX, String SO_TEM_KDINH, String NGAY_KDINH, String MA_CHI_KDINH, String MA_CHI_HOP_DDAY, int SO_VONG_THANH_CAI, String TYSO_BIEN, String MA_BDONG) {
        this.MA_DVIQLY = MA_DVIQLY;
        this.MA_CLOAI = MA_CLOAI;
        this.LOAI_TU_TI = LOAI_TU_TI;
        this.MO_TA = MO_TA;
        this.SO_PHA = SO_PHA;
        this.TYSO_DAU = TYSO_DAU;
        this.CAP_CXAC = CAP_CXAC;
        this.CAP_DAP = CAP_DAP;
        this.MA_NUOC = MA_NUOC;
        this.MA_HANG = MA_HANG;
        this.TRANG_THAI = TRANG_THAI;
        this.IS_TU = IS_TU;
        this.ID_BBAN_TUTI = ID_BBAN_TUTI;
        this.ID_CHITIET_TUTI = ID_CHITIET_TUTI;
        this.SO_TU_TI = SO_TU_TI;
        this.NUOC_SX = NUOC_SX;
        this.SO_TEM_KDINH = SO_TEM_KDINH;
        this.NGAY_KDINH = NGAY_KDINH;
        this.MA_CHI_KDINH = MA_CHI_KDINH;
        this.MA_CHI_HOP_DDAY = MA_CHI_HOP_DDAY;
        this.SO_VONG_THANH_CAI = SO_VONG_THANH_CAI;
        this.TYSO_BIEN = TYSO_BIEN;
        this.MA_BDONG = MA_BDONG;
    }

    protected MTB_TuTiModel(Parcel in) {
        MA_DVIQLY = in.readString();
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
        IS_TU = in.readByte() != 0;
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
    }

    public static final Creator<MTB_TuTiModel> CREATOR = new Creator<MTB_TuTiModel>() {
        @Override
        public MTB_TuTiModel createFromParcel(Parcel in) {
            return new MTB_TuTiModel(in);
        }

        @Override
        public MTB_TuTiModel[] newArray(int size) {
            return new MTB_TuTiModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(MA_DVIQLY);
        parcel.writeString(MA_CLOAI);
        parcel.writeString(LOAI_TU_TI);
        parcel.writeString(MO_TA);
        parcel.writeInt(SO_PHA);
        parcel.writeString(TYSO_DAU);
        parcel.writeInt(CAP_CXAC);
        parcel.writeInt(CAP_DAP);
        parcel.writeString(MA_NUOC);
        parcel.writeString(MA_HANG);
        parcel.writeInt(TRANG_THAI);
        parcel.writeByte((byte) (IS_TU ? 1 : 0));
        parcel.writeInt(ID_BBAN_TUTI);
        parcel.writeInt(ID_CHITIET_TUTI);
        parcel.writeString(SO_TU_TI);
        parcel.writeString(NUOC_SX);
        parcel.writeString(SO_TEM_KDINH);
        parcel.writeString(NGAY_KDINH);
        parcel.writeString(MA_CHI_KDINH);
        parcel.writeString(MA_CHI_HOP_DDAY);
        parcel.writeInt(SO_VONG_THANH_CAI);
        parcel.writeString(TYSO_BIEN);
        parcel.writeString(MA_BDONG);
    }
}
