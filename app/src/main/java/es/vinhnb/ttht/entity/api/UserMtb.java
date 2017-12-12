package es.vinhnb.ttht.entity.api;

import com.google.gson.annotations.SerializedName;

public class UserMtb {
    @SerializedName("MA_NHAN_VIEN")
    public String MA_NHAN_VIEN;

    @SerializedName("ERROR")
    public String ERROR;


    public UserMtb(String MA_NHAN_VIEN, String ERROR) {
        this.MA_NHAN_VIEN = MA_NHAN_VIEN;
        this.ERROR = ERROR;
    }
}