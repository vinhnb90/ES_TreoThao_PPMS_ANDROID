
package com.es.tungnv.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Created by VinhNB on 8/9/2016.
 */
public class PpmsEntityTask implements Parcelable {
    private String CoSaiChiSo;
    private int PhanCongId;
    private int ChiSoMoi;
    private int ChiSoCu;
    private int SanLuong;
    private int NhanVienId;
    private int HeSoNhan;
    private int SoHopCongTo;
    private int SoOCongTo;
    private int SoCotCongTo;
    private int ChiSoPhucTra;

    private float SanLuongTB;
    private float TyLeChenhLech;

    private String MaDonViQLy;
    private String MaDiemDo;
    private String MaSoGcs;
    private String MaKhachHang;
    private String SoCongTo;
    private String MaCongTo;
    private String NgayPhanCong;
    private String ThangKiemTra;
    private String TenTram;
    private String AnhCongTo;
    private String TenKhachHang;
    private String DiaChiDungDien;
    private String LoaiHopCongTo;
    private String LoaiCongTo;
    private String TinhTrangHopCongTo;
    private String NgayPhucTra;
    private String NgayGhiDien;
    private String MucDichSuDungDien;
    private String TinhTrangSuDungDien;
    private String NhanXetDeXuat;

    private String TinhTrangNiemPhong;

    public PpmsEntityTask() {
    }

    protected PpmsEntityTask(Parcel in) {
        CoSaiChiSo = in.readString();
        PhanCongId = in.readInt();
        ChiSoMoi = in.readInt();
        ChiSoCu = in.readInt();
        SanLuong = in.readInt();
        NhanVienId = in.readInt();
        HeSoNhan = in.readInt();
        SoHopCongTo = in.readInt();
        SoOCongTo = in.readInt();
        SoCotCongTo = in.readInt();
        ChiSoPhucTra = in.readInt();
        SanLuongTB = in.readFloat();
        TyLeChenhLech = in.readFloat();
        MaDonViQLy = in.readString();
        MaDiemDo = in.readString();
        MaSoGcs = in.readString();
        MaKhachHang = in.readString();
        SoCongTo = in.readString();
        MaCongTo = in.readString();
        NgayPhanCong = in.readString();
        ThangKiemTra = in.readString();
        TenTram = in.readString();
        AnhCongTo = in.readString();
        TenKhachHang = in.readString();
        DiaChiDungDien = in.readString();
        LoaiHopCongTo = in.readString();
        LoaiCongTo = in.readString();
        TinhTrangHopCongTo = in.readString();
        NgayPhucTra = in.readString();
        NgayGhiDien = in.readString();
        MucDichSuDungDien = in.readString();
        TinhTrangSuDungDien = in.readString();
        NhanXetDeXuat = in.readString();
        TinhTrangNiemPhong = in.readString();
    }

    public static final Creator<PpmsEntityTask> CREATOR = new Creator<PpmsEntityTask>() {
        @Override
        public PpmsEntityTask createFromParcel(Parcel in) {
            return new PpmsEntityTask(in);
        }

        @Override
        public PpmsEntityTask[] newArray(int size) {
            return new PpmsEntityTask[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(CoSaiChiSo);
        dest.writeInt(PhanCongId);
        dest.writeInt(ChiSoMoi);
        dest.writeInt(ChiSoCu);
        dest.writeInt(SanLuong);
        dest.writeInt(NhanVienId);
        dest.writeInt(HeSoNhan);
        dest.writeInt(SoHopCongTo);
        dest.writeInt(SoOCongTo);
        dest.writeInt(SoCotCongTo);
        dest.writeInt(ChiSoPhucTra);
        dest.writeFloat(SanLuongTB);
        dest.writeFloat(TyLeChenhLech);
        dest.writeString(MaDonViQLy);
        dest.writeString(MaDiemDo);
        dest.writeString(MaSoGcs);
        dest.writeString(MaKhachHang);
        dest.writeString(SoCongTo);
        dest.writeString(MaCongTo);
        dest.writeString(NgayPhanCong);
        dest.writeString(ThangKiemTra);
        dest.writeString(TenTram);
        dest.writeString(AnhCongTo);
        dest.writeString(TenKhachHang);
        dest.writeString(DiaChiDungDien);
        dest.writeString(LoaiHopCongTo);
        dest.writeString(LoaiCongTo);
        dest.writeString(TinhTrangHopCongTo);
        dest.writeString(NgayPhucTra);
        dest.writeString(NgayGhiDien);
        dest.writeString(MucDichSuDungDien);
        dest.writeString(TinhTrangSuDungDien);
        dest.writeString(NhanXetDeXuat);
        dest.writeString(TinhTrangNiemPhong);
    }


    public PpmsEntityTask(PpmsEntityTask task) {
        CoSaiChiSo = task.getCoSaiChiSo();
        if (task.getPhanCongId() != 0) {
            PhanCongId = task.getPhanCongId();
        }
        ChiSoMoi = task.getChiSoMoi();
        ChiSoCu = task.getChiSoCu();
        SanLuong = task.getSanLuong();
        if (task.getNhanVienId() != 0) {
            NhanVienId = task.getNhanVienId();
        }
        SanLuongTB = task.getSanLuongTB();
        HeSoNhan = task.getHeSoNhan();
        SoHopCongTo = task.getSoHopCongTo();
        SoOCongTo = task.getSoOCongTo();
        SoCotCongTo = task.getSoCotCongTo();
        TyLeChenhLech = task.getTyLeChenhLech();
        MaDonViQLy = task.getMaDonViQLy();
        MaDiemDo = task.getMaDiemDo();
        MaSoGcs = task.getMaSoGcs();
        MaKhachHang = task.getMaKhachHang();
        SoCongTo = task.getSoCongTo();
        MaCongTo = task.getMaCongTo();
        NgayPhanCong = task.getNgayPhanCong();
        ThangKiemTra = task.getThangKiemTra();
        TenTram = task.getTenTram();
        AnhCongTo = task.getAnhCongTo();
        TenKhachHang = task.getTenKhachHang();
        DiaChiDungDien = task.getDiaChiDungDien();
        LoaiHopCongTo = task.getLoaiHopCongTo();
        LoaiCongTo = task.getLoaiCongTo();
        TinhTrangHopCongTo = task.getTinhTrangHopCongTo();
        NgayPhucTra = task.getNgayPhucTra();
        NgayGhiDien = task.getNgayGhiDien();
        MucDichSuDungDien = task.getMucDichSuDungDien();
        TinhTrangSuDungDien = task.getTinhTrangSuDungDien();
        NhanXetDeXuat = task.getNhanXetDeXuat();
        ChiSoPhucTra = task.getChiSoPhucTra();
        TinhTrangNiemPhong = task.getTinhTrangNiemPhong();
    }

    public PpmsEntityTask(int phanCongId, String maSoGcs, String maKhachHang, int chiSoMoi, int chiSoCu,
                          int sanLuong, String soCongTo, String ngayPhanCong, int nhanVienId, String anhCongTo) {
        if (phanCongId != 0) {
            PhanCongId = phanCongId;
        }
        MaSoGcs = maSoGcs;
        MaKhachHang = maKhachHang;
        ChiSoMoi = chiSoMoi;
        ChiSoCu = chiSoCu;
        SanLuong = sanLuong;
        SoCongTo = soCongTo;
        NgayPhanCong = ngayPhanCong;
        if (nhanVienId != 0) {
            NhanVienId = nhanVienId;
        }
        AnhCongTo = anhCongTo;
    }

    public PpmsEntityTask(
            int phanCongId,
            String maDonViQLy,
            String maDiemDo,
            String maSoGcs,
            String maKhachHang,
            int chiSoMoi,
            int chiSoCu,
            int sanLuong,

            String soCongTo,
            String maCongTo,
            String ngayPhanCong,
            String thangKiemTra,
            int nhanVienId,
            float tyLeChenhLech,
            float sanLuongTB,
            String tenTram,
            int heSoNhan,

            String anhCongTo,
            String coSaiChiSo,
            String tenKhachHang,
            String diaChiDungDien,
            int soHopCongTo,
            int soOCongTo,
            int soCotCongTo,

            String loaiHopCongTo,
            String loaiCongTo,
            String tinhTrangHopCongTo,
            String ngayPhucTra,
            String ngayGhiDien,

            String mucDichSuDungDien,
            String tinhTrangSuDungDien,
            String nhanXetDeXuat,
            int chiSoPhucTra,
            String tinhTrangNiemPhong) {
        CoSaiChiSo = coSaiChiSo;
        if (phanCongId != 0) {
            PhanCongId = phanCongId;
        }
        ChiSoMoi = chiSoMoi;
        ChiSoCu = chiSoCu;
        SanLuong = sanLuong;

        if (nhanVienId != 0) {
            NhanVienId = nhanVienId;
        }

        SanLuongTB = sanLuongTB;
        HeSoNhan = heSoNhan;
        SoHopCongTo = soHopCongTo;
        SoOCongTo = soOCongTo;
        SoCotCongTo = soCotCongTo;
        TyLeChenhLech = tyLeChenhLech;
        MaDonViQLy = maDonViQLy;
        MaDiemDo = maDiemDo;
        MaSoGcs = maSoGcs;
        MaKhachHang = maKhachHang;
        SoCongTo = soCongTo;
        MaCongTo = maCongTo;
        NgayPhanCong = ngayPhanCong;
        ThangKiemTra = thangKiemTra;
        TenTram = tenTram;
        AnhCongTo = anhCongTo;
        TenKhachHang = tenKhachHang;
        DiaChiDungDien = diaChiDungDien;
        LoaiHopCongTo = loaiHopCongTo;
        LoaiCongTo = loaiCongTo;
        TinhTrangHopCongTo = tinhTrangHopCongTo;
        NgayPhucTra = ngayPhucTra;
        NgayGhiDien = ngayGhiDien;
        MucDichSuDungDien = mucDichSuDungDien;
        TinhTrangSuDungDien = tinhTrangSuDungDien;
        NhanXetDeXuat = nhanXetDeXuat;
        ChiSoPhucTra = chiSoPhucTra;
        TinhTrangNiemPhong = tinhTrangNiemPhong;
    }

    public void setPhanCongId(int phanCongId) {
        PhanCongId = phanCongId;
    }

    public int getPhanCongId() {
        return PhanCongId;
    }

    public String getCoSaiChiSo() {
        return CoSaiChiSo;
    }

    public void setCoSaiChiSo(String coSaiChiSo) {
        CoSaiChiSo = coSaiChiSo;
    }

    public int getChiSoMoi() {
        return ChiSoMoi;
    }

    public void setChiSoMoi(int chiSoMoi) {
        ChiSoMoi = chiSoMoi;
    }

    public int getChiSoCu() {
        return ChiSoCu;
    }

    public void setChiSoCu(int chiSoCu) {
        ChiSoCu = chiSoCu;
    }

    public int getSanLuong() {
        return SanLuong;
    }

    public void setSanLuong(int sanLuong) {
        SanLuong = sanLuong;
    }

    public int getNhanVienId() {
        return NhanVienId;
    }

    public void setNhanVienId(int nhanVienId) {
        NhanVienId = nhanVienId;
    }

    public float getSanLuongTB() {
        return SanLuongTB;
    }

    public void setSanLuongTB(float sanLuongTB) {
        SanLuongTB = sanLuongTB;
    }

    public int getHeSoNhan() {
        return HeSoNhan;
    }

    public void setHeSoNhan(int heSoNhan) {
        HeSoNhan = heSoNhan;
    }

    public int getSoHopCongTo() {
        return SoHopCongTo;
    }

    public void setSoHopCongTo(int soHopCongTo) {
        SoHopCongTo = soHopCongTo;
    }

    public int getSoOCongTo() {
        return SoOCongTo;
    }

    public void setSoOCongTo(int soOCongTo) {
        SoOCongTo = soOCongTo;
    }

    public int getSoCotCongTo() {
        return SoCotCongTo;
    }

    public void setSoCotCongTo(int soCotCongTo) {
        SoCotCongTo = soCotCongTo;
    }

    public float getTyLeChenhLech() {
        return TyLeChenhLech;
    }

    public void setTyLeChenhLech(float tyLeChenhLech) {
        TyLeChenhLech = tyLeChenhLech;
    }

    public String getMaDonViQLy() {
        if (TextUtils.isEmpty(MaDonViQLy)) return "";
        return MaDonViQLy;
    }

    public void setMaDonViQLy(String maDonViQLy) {
        MaDonViQLy = maDonViQLy;
    }

    public String getMaDiemDo() {
        if (TextUtils.isEmpty(MaDiemDo)) return "";
        return MaDiemDo;
    }

    public void setMaDiemDo(String maDiemDo) {
        MaDiemDo = maDiemDo;
    }

    public String getMaSoGcs() {
        if (TextUtils.isEmpty(MaSoGcs)) return "";
        return MaSoGcs;
    }

    public void setMaSoGcs(String maSoGcs) {
        MaSoGcs = maSoGcs;
    }

    public String getMaKhachHang() {
        if (TextUtils.isEmpty(MaKhachHang)) return "";
        return MaKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        MaKhachHang = maKhachHang;
    }

    public String getSoCongTo() {
        if (TextUtils.isEmpty(SoCongTo)) return "";
        return SoCongTo;
    }

    public void setSoCongTo(String soCongTo) {
        SoCongTo = soCongTo;
    }

    public String getMaCongTo() {
        if (TextUtils.isEmpty(MaCongTo)) return "";
        return MaCongTo;
    }

    public void setMaCongTo(String maCongTo) {
        MaCongTo = maCongTo;
    }

    public String getNgayPhanCong() {
        if (TextUtils.isEmpty(NgayPhanCong)) return "";
        return NgayPhanCong;
    }

    public void setNgayPhanCong(String ngayPhanCong) {
        NgayPhanCong = ngayPhanCong;
    }

    public String getThangKiemTra() {
        if (TextUtils.isEmpty(ThangKiemTra)) return "";
        return ThangKiemTra;
    }

    public void setThangKiemTra(String thangKiemTra) {
        ThangKiemTra = thangKiemTra;
    }

    public String getTenTram() {
        if (TextUtils.isEmpty(TenTram)) return "";
//        if (TenTramTextUtils.isEmpty(null)) return "";
        return TenTram;
    }

    public void setTenTram(String tenTram) {
        TenTram = tenTram;
    }

    public String getAnhCongTo() {
        if (TextUtils.isEmpty(AnhCongTo)) return "";
        return AnhCongTo;
    }

    public void setAnhCongTo(String anhCongTo) {
        AnhCongTo = anhCongTo;
    }

    public String getTenKhachHang() {
        if (TextUtils.isEmpty(TenKhachHang)) return "";
        return TenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        TenKhachHang = tenKhachHang;
    }

    public String getDiaChiDungDien() {
        if (TextUtils.isEmpty(DiaChiDungDien)) return "";
        return DiaChiDungDien;
    }

    public void setDiaChiDungDien(String diaChiDungDien) {
        DiaChiDungDien = diaChiDungDien;
    }

    public String getLoaiHopCongTo() {
        if (TextUtils.isEmpty(LoaiHopCongTo)) return "";
        return LoaiHopCongTo;
    }

    public void setLoaiHopCongTo(String loaiHopCongTo) {
        LoaiHopCongTo = loaiHopCongTo;
    }

    public String getLoaiCongTo() {
        if (TextUtils.isEmpty(LoaiCongTo)) return "";
        return LoaiCongTo;
    }

    public void setLoaiCongTo(String loaiCongTo) {
        LoaiCongTo = loaiCongTo;
    }

    public String getTinhTrangHopCongTo() {
        if (TextUtils.isEmpty(TinhTrangHopCongTo)) return "";
        return TinhTrangHopCongTo;
    }

    public void setTinhTrangHopCongTo(String tinhTrangHopCongTo) {
        TinhTrangHopCongTo = tinhTrangHopCongTo;
    }

    public String getNgayPhucTra() {
        if (TextUtils.isEmpty(NgayPhucTra)) return "";
        return NgayPhucTra;
    }

    public void setNgayPhucTra(String ngayPhucTra) {
        NgayPhucTra = ngayPhucTra;
    }

    public String getNgayGhiDien() {
        if (TextUtils.isEmpty(NgayGhiDien)) return "";
        return NgayGhiDien;
    }

    public void setNgayGhiDien(String ngayGhiDien) {
        NgayGhiDien = ngayGhiDien;
    }

    public String getMucDichSuDungDien() {
        if (TextUtils.isEmpty(MucDichSuDungDien)) return "";
        return MucDichSuDungDien;
    }

    public void setMucDichSuDungDien(String mucDichSuDungDien) {
        MucDichSuDungDien = mucDichSuDungDien;
    }

    public String getTinhTrangSuDungDien() {
        if (TextUtils.isEmpty(TinhTrangSuDungDien)) return "";
        return TinhTrangSuDungDien;
    }

    public void setTinhTrangSuDungDien(String tinhTrangSuDungDien) {
        TinhTrangSuDungDien = tinhTrangSuDungDien;
    }

    public String getNhanXetDeXuat() {
        if (TextUtils.isEmpty(NhanXetDeXuat)) return "";
        return NhanXetDeXuat;
    }

    public void setNhanXetDeXuat(String nhanXetDeXuat) {
        NhanXetDeXuat = nhanXetDeXuat;
    }

    public int getChiSoPhucTra() {
        return ChiSoPhucTra;
    }

    public void setChiSoPhucTra(int chiSoPhucTra) {
        ChiSoPhucTra = chiSoPhucTra;
    }

    public String getTinhTrangNiemPhong() {
        if (TextUtils.isEmpty(TinhTrangNiemPhong)) return "";
        return TinhTrangNiemPhong;
    }

    public void setTinhTrangNiemPhong(String tinhTrangNiemPhong) {
        TinhTrangNiemPhong = tinhTrangNiemPhong;
    }
}
