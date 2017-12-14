package es.vinhnb.ttht.entity.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by VinhNB on 12/14/2017.
 */

public class ResponseGetSuccessPostRequest implements Parcelable{
    @SerializedName("maDonVi")
    public String maDonVi ;

    @SerializedName("maNhanVien")
    public String maNhanVien ;

    @SerializedName("result")
    public String result ;

    public ResponseGetSuccessPostRequest(String maDonVi, String maNhanVien, String result) {
        this.maDonVi = maDonVi;
        this.maNhanVien = maNhanVien;
        this.result = result;
    }

    protected ResponseGetSuccessPostRequest(Parcel in) {
        maDonVi = in.readString();
        maNhanVien = in.readString();
        result = in.readString();
    }

    public static final Creator<ResponseGetSuccessPostRequest> CREATOR = new Creator<ResponseGetSuccessPostRequest>() {
        @Override
        public ResponseGetSuccessPostRequest createFromParcel(Parcel in) {
            return new ResponseGetSuccessPostRequest(in);
        }

        @Override
        public ResponseGetSuccessPostRequest[] newArray(int size) {
            return new ResponseGetSuccessPostRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(maDonVi);
        parcel.writeString(maNhanVien);
        parcel.writeString(result);
    }
}
