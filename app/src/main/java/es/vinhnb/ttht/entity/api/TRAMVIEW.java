package es.vinhnb.ttht.entity.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by VinhNB on 11/23/2017.
 */

public class TRAMVIEW implements Parcelable{
    @SerializedName("MA_TRAM")
    public String MA_TRAM ;

    @SerializedName("MA_DVIQLY")
    public String MA_DVIQLY ;

    @SerializedName("TEN_TRAM")
    public String TEN_TRAM ;

    @SerializedName("LOAI_TRAM")
    public String LOAI_TRAM ;

    @SerializedName("CSUAT_TRAM")
    public int CSUAT_TRAM ;

    @SerializedName("MA_CAP_DA")
    public String MA_CAP_DA ;

    @SerializedName("MA_CAP_DA_RA")
    public String MA_CAP_DA_RA ;

    @SerializedName("DINH_DANH")
    public String DINH_DANH ;

    public TRAMVIEW(String MA_TRAM, String MA_DVIQLY, String TEN_TRAM, String LOAI_TRAM, int CSUAT_TRAM, String MA_CAP_DA, String MA_CAP_DA_RA, String DINH_DANH) {
        this.MA_TRAM = MA_TRAM;
        this.MA_DVIQLY = MA_DVIQLY;
        this.TEN_TRAM = TEN_TRAM;
        this.LOAI_TRAM = LOAI_TRAM;
        this.CSUAT_TRAM = CSUAT_TRAM;
        this.MA_CAP_DA = MA_CAP_DA;
        this.MA_CAP_DA_RA = MA_CAP_DA_RA;
        this.DINH_DANH = DINH_DANH;
    }

    protected TRAMVIEW(Parcel in) {
        MA_TRAM = in.readString();
        MA_DVIQLY = in.readString();
        TEN_TRAM = in.readString();
        LOAI_TRAM = in.readString();
        CSUAT_TRAM = in.readInt();
        MA_CAP_DA = in.readString();
        MA_CAP_DA_RA = in.readString();
        DINH_DANH = in.readString();
    }

    public static final Creator<TRAMVIEW> CREATOR = new Creator<TRAMVIEW>() {
        @Override
        public TRAMVIEW createFromParcel(Parcel in) {
            return new TRAMVIEW(in);
        }

        @Override
        public TRAMVIEW[] newArray(int size) {
            return new TRAMVIEW[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(MA_TRAM);
        parcel.writeString(MA_DVIQLY);
        parcel.writeString(TEN_TRAM);
        parcel.writeString(LOAI_TRAM);
        parcel.writeInt(CSUAT_TRAM);
        parcel.writeString(MA_CAP_DA);
        parcel.writeString(MA_CAP_DA_RA);
        parcel.writeString(DINH_DANH);
    }
}
