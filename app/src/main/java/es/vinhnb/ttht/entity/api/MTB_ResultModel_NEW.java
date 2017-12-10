package es.vinhnb.ttht.entity.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by VinhNB on 11/22/2017.
 */

public class MTB_ResultModel_NEW implements Parcelable {
    @SerializedName("ID_BBAN_TRTH")
    public String ID_BBAN_TRTH;
    @SerializedName("TRANG_THAI")
    public String TRANG_THAI;
    @SerializedName("ERROR")
    public String ERROR;

    public MTB_ResultModel_NEW(String ID_BBAN_TRTH, String TRANG_THAI, String ERROR) {
        this.ID_BBAN_TRTH = ID_BBAN_TRTH;
        this.TRANG_THAI = TRANG_THAI;
        this.ERROR = ERROR;
    }

    protected MTB_ResultModel_NEW(Parcel in) {
        ID_BBAN_TRTH = in.readString();
        TRANG_THAI = in.readString();
        ERROR = in.readString();
    }

    public static final Creator<MTB_ResultModel_NEW> CREATOR = new Creator<MTB_ResultModel_NEW>() {
        @Override
        public MTB_ResultModel_NEW createFromParcel(Parcel in) {
            return new MTB_ResultModel_NEW(in);
        }

        @Override
        public MTB_ResultModel_NEW[] newArray(int size) {
            return new MTB_ResultModel_NEW[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ID_BBAN_TRTH);
        parcel.writeString(TRANG_THAI);
        parcel.writeString(ERROR);
    }
}
