package es.vinhnb.ttht.entity.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by VinhNB on 11/22/2017.
 */

public class UpdateStatus implements Parcelable{
    @SerializedName("RESULT")
    public String RESULT;

    @SerializedName("ERROR")
    public String ERROR;

    public UpdateStatus(String RESULT, String ERROR) {
        this.RESULT = RESULT;
        this.ERROR = ERROR;
    }

    protected UpdateStatus(Parcel in) {
        RESULT = in.readString();
        ERROR = in.readString();
    }

    public static final Creator<UpdateStatus> CREATOR = new Creator<UpdateStatus>() {
        @Override
        public UpdateStatus createFromParcel(Parcel in) {
            return new UpdateStatus(in);
        }

        @Override
        public UpdateStatus[] newArray(int size) {
            return new UpdateStatus[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(RESULT);
        parcel.writeString(ERROR);
    }
}
