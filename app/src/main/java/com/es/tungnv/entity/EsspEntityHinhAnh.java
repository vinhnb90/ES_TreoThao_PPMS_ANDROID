package com.es.tungnv.entity;

/**
 * Created by TUNGNV on 4/7/2016.
 */
public class EsspEntityHinhAnh {

    int HOSO_ID;
    String TEN_ANH;
    String GHI_CHU;
    String TINH_TRANG;

    public void setHOSO_ID(int HOSO_ID) {
        this.HOSO_ID = HOSO_ID;
    }

    public int getHOSO_ID() {
        return HOSO_ID;
    }

    public String getTEN_ANH() {
        return TEN_ANH;
    }

    public void setTEN_ANH(String TEN_ANH) {
        this.TEN_ANH = TEN_ANH;
    }

    public String getGHI_CHU() {
        return GHI_CHU;
    }

    public void setGHI_CHU(String GHI_CHU) {
        this.GHI_CHU = GHI_CHU;
    }

    public String getTINH_TRANG() {
        return TINH_TRANG;
    }

    public void setTINH_TRANG(String TINH_TRANG) {
        this.TINH_TRANG = TINH_TRANG;
    }

    public EsspEntityHinhAnh(){

    }

    public EsspEntityHinhAnh(int HOSO_ID, String TEN_ANH, String GHI_CHU, String TINH_TRANG){
        this.HOSO_ID = HOSO_ID;
        this.TEN_ANH = TEN_ANH;
        this.GHI_CHU = GHI_CHU;
        this.TINH_TRANG = TINH_TRANG;
    }

}
