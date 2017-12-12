package es.vinhnb.ttht.entity.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by VinhNB on 11/16/2017.
 */

public class D_LY_DO_MODEL implements Parcelable{
    @SerializedName("MA_DVIQLY")
    public String MA_DVIQLY;

    @SerializedName("MA_LDO")
    public String MA_LDO;

    @SerializedName("TEN_LDO")
    public String TEN_LDO;

    @SerializedName("NHOM")
    public int NHOM;

    public D_LY_DO_MODEL() {
    }

    protected D_LY_DO_MODEL(Parcel in) {
        MA_DVIQLY = in.readString();
        MA_LDO = in.readString();
        TEN_LDO = in.readString();
        NHOM = in.readInt();
    }

    public static final Creator<D_LY_DO_MODEL> CREATOR = new Creator<D_LY_DO_MODEL>() {
        @Override
        public D_LY_DO_MODEL createFromParcel(Parcel in) {
            return new D_LY_DO_MODEL(in);
        }

        @Override
        public D_LY_DO_MODEL[] newArray(int size) {
            return new D_LY_DO_MODEL[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(MA_DVIQLY);
        parcel.writeString(MA_LDO);
        parcel.writeString(TEN_LDO);
        parcel.writeInt(NHOM);
    }
}
