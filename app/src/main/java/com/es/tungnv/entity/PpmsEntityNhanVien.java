
package com.es.tungnv.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by VinhNB on 8/5/2016.
 */
public class PpmsEntityNhanVien implements Parcelable {
    @SerializedName("NhanVienId")
    private int NhanVienId;
    @SerializedName("HoTen")
    private String HoTen;
    private String SDT;
    private String DiaChi;
    private String Email;
    private String TenDangNhap;
    private String MatKhau;
    private String GenderId;
    private int PpmsEntityDepartmentId;
    private boolean IsActive;

    public PpmsEntityNhanVien() {

    }

    public PpmsEntityNhanVien(PpmsEntityNhanVien emp) {
        this.NhanVienId = emp.getNhanVienId();
        this.HoTen = emp.getHoTen();
        this.SDT = emp.getSDT();
        this.DiaChi = emp.getDiaChi();
        this.Email = emp.getEmail();
        this.TenDangNhap = emp.getTenDangNhap();
        this.MatKhau = emp.getMatKhau();
        this.GenderId = emp.getGenderId();
        this.PpmsEntityDepartmentId = emp.getPpmsEntityDepartmentId();
        this.IsActive = emp.isActive();
    }

    public PpmsEntityNhanVien(int nhanVienId, String hoTen, String SDT, String diaChi, String email, String tenDangNhap,
                              String matKhau, String genderId, int departID, boolean isActive
    ) {
        this.NhanVienId = nhanVienId;
        HoTen = hoTen;
        this.SDT = SDT;
        DiaChi = diaChi;
        Email = email;
        TenDangNhap = tenDangNhap;
        MatKhau = matKhau;
        GenderId = genderId;
        this.PpmsEntityDepartmentId = departID;
        IsActive = isActive;
    }

    protected PpmsEntityNhanVien(Parcel in) {
        NhanVienId = in.readInt();
        HoTen = in.readString();
        SDT = in.readString();
        DiaChi = in.readString();
        Email = in.readString();
        TenDangNhap = in.readString();
        MatKhau = in.readString();
        GenderId = in.readString();
        PpmsEntityDepartmentId = in.readInt();
        IsActive = in.readByte() != 0;
    }

    public static final Creator<PpmsEntityNhanVien> CREATOR = new Creator<PpmsEntityNhanVien>() {
        @Override
        public PpmsEntityNhanVien createFromParcel(Parcel in) {
            return new PpmsEntityNhanVien(in);
        }

        @Override
        public PpmsEntityNhanVien[] newArray(int size) {
            return new PpmsEntityNhanVien[size];
        }
    };

    public void setNhanVienId(int nhanVienId) {
        NhanVienId = nhanVienId;
    }

    public int getNhanVienId() {
        return NhanVienId;
    }

    public String getHoTen() {
        return HoTen;
    }

    public void setHoTen(String hoTen) {
        HoTen = hoTen;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String diaChi) {
        DiaChi = diaChi;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getTenDangNhap() {
        return TenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        TenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return MatKhau;
    }

    public void setMatKhau(String matKhau) {
        MatKhau = matKhau;
    }

    public String getGenderId() {
        return GenderId;
    }

    public void setGenderId(String genderId) {
        GenderId = genderId;
    }

    public int getPpmsEntityDepartmentId() {
        return PpmsEntityDepartmentId;
    }

    public void setPpmsEntityDepartmentId(int PpmsEntityDepartmentId) {
        PpmsEntityDepartmentId = PpmsEntityDepartmentId;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(NhanVienId);
        parcel.writeString(HoTen);
        parcel.writeString(SDT);
        parcel.writeString(DiaChi);
        parcel.writeString(Email);
        parcel.writeString(TenDangNhap);
        parcel.writeString(MatKhau);
        parcel.writeString(GenderId);
        parcel.writeInt(PpmsEntityDepartmentId);
        parcel.writeByte((byte) (IsActive ? 1 : 0));
    }
}
