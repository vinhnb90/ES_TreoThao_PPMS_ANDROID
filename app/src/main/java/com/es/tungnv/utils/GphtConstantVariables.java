package com.es.tungnv.utils;

/**
 * Created by TUNGNV on 6/29/2016.
 */
public class GphtConstantVariables {

    public static final int MAX_LOGIN = 20;

    public static final String DATABASE_NAME = "GPHT.s3db";
    public static final int DATABASE_VERSION = 1;

    public static final int MENU_SYN_DMUC = 0;
    public static final int MENU_SYN_QUYEN = 1;

    public static final String PROGRAM_PATH = "/GPHT/";
    public static final String CFG_FILENAME = "GPHT.cfg";
    public static final String[] CFG_COLUMN = {"IP_SV_1", "VERSION"};

    public static final String[] TINH_THANH = {"Hà Nội", "Hải Phòng", "TP HCM", "Đà Nẵng", "Cần Thơ", "An Giang", "Bà Rịa - Vũng Tàu",
            "Bắc Giang", "Bắc Kạn", "Bạc Liêu", "Bắc Ninh", "Bến Tre", "Bình Định", "Bình Dương", "Bình Phước", "Bình Thuận", "Cà Mau",
            "Cao Bằng", "Đắk Lắk", "Đắk Nông", "Điện Biên", "Đồng Nai", "Đồng Tháp", "Gia Lai", "Hà Giang", "Hà Nam", "Hà Tĩnh",
            "Hải Dương", "Hậu Giang", "Hòa Bình", "Hưng Yên", "Khánh Hòa", "Kiên Giang", "Kon Tum", "Lai Châu", "Lâm Đồng", "Lạng Sơn",
            "Lào Cai", "Long An", "Nam Định", "Nghệ An", "Ninh Bình", "Ninh Thuận", "Phú Thọ", "Phú Yên", "Quảng Bình", "Quảng Nam",
            "Quảng Ngãi", "Quảng Ninh", "Quảng Trị", "Sóc Trăng", "Sơn La", "Tây Ninh", "Thái Bình", "Thái Nguyên", "Thanh Hóa",
            "Thừa Thiên Huế", "Tiền Giang", "Trà Vinh", "Tuyên Quang", "Vĩnh Long", "Vĩnh Phúc", "Yên Bái"};

    public static final String[] TINH_THANH_ACRONYM = {"H_NOI", "H_PHONG", "HCM", "D_NANG", "C_THO", "A_GIANG", "V_TAU",
            "B_GIANG", "B_KAN", "B_LIEU", "B_NINH", "B_TRE", "B_DINH", "B_DUONG", "B_PHUOC", "B_THUAN", "C_MAU",
            "C_BANG", "D_LAK", "D_NONG", "D_BIEN", "D_NOI", "D_THAP", "G_LAI", "H_GIANG", "H_NAM", "H_TINH",
            "H_DUONG", "H_GIANG", "H_BINH", "H_YEN", "K_HOA", "K_GIANG", "K_TUM", "L_CHAU", "L_DONG", "L_SON",
            "L_CAI", "L_AN", "N_DINH", "N_AN", "N_BINH", "N_THUAN", "P_THO", "P_YEN", "Q_BINH", "Q_NAM",
            "Q_NGAI", "Q_NINH", "Q_TRI", "S_TRANG", "S_LA", "T_NINH", "T_BINH", "T_NGUYEN", "T_HOA",
            "TT_HUE", "T_GIANG", "T_VINH", "T_QUANG", "V_LONG", "V_PHUC", "Y_BAI"};

    public static final String TABLE_NAME_NGUOIDUNG = "DM_NguoiDung";
    public static final String CREATE_TABLE_NGUOIDUNG = "CREATE TABLE " + TABLE_NAME_NGUOIDUNG
            + "(NV_ID INTEGER," + "Ten_TCap TEXT," + "Mat_Khau TEXT," + "Ten_DD TEXT,"
            + "Dien_Thoai TEXT," + "Email TEXT," + "STT INTEGER," + "ID_DonVi INTEGER," + "DuongDan_URL_Default TEXT," + "Admin BOOLEAN)";

    public static final String TABLE_NAME_DONVI = "DM_DonVi";
    public static final String CREATE_TABLE_DONVI = "CREATE TABLE " + TABLE_NAME_DONVI
            + "(ID_DonVi INTEGER," + "Ten_DonVi TEXT," + "Ma_DonVi TEXT)";

    public static final String TABLE_NAME_QUYEN = "Quyen_NguoiDung_HeThong";
    public static final String CREATE_TABLE_QUYEN = "CREATE TABLE " + TABLE_NAME_QUYEN
            + "(ID INTEGER," + "NV_ID INTEGER," + "ID_HeThong INTEGER," + "ID_User INTEGER)";

    public static final String TABLE_NAME_DM_HETHONG = "DM_HeThong";
    public static final String CREATE_TABLE_DM_HETHONG = "CREATE TABLE " + TABLE_NAME_DM_HETHONG
            + "(ID_HeThong INTEGER," + "Ten_HeThong TEXT," + "Address TEXT," + "ID_DonVi INTEGER," + "Key_HThong TEXT)";

    public static final String TABLE_NAME_DM_USER = "DM_User";
    public static final String CREATE_TABLE_DM_USER = "CREATE TABLE " + TABLE_NAME_DM_USER
            + "(ID_User INTEGER," + "ID_HeThong INTEGER," + "GhiChu TEXT," + "Ten_DangNhap TEXT," + "Mat_Khau TEXT)";

    public static final String TABLE_NAME_LOG_DONGBO = "tbl_Log";
    public static final String CREATE_TABLE_LOG_DONGBO = "CREATE TABLE " + TABLE_NAME_LOG_DONGBO
            + "(Id_Log INTEGER," + "ID_Bang INTEGER," + "ThoiGian TEXT," + "Noidung TEXT)";

    public static final String TABLE_NAME_LOG_QUYEN_DONGBO = "tbl_Log_quyen";
    public static final String CREATE_TABLE_LOG_QUYEN_DONGBO = "CREATE TABLE " + TABLE_NAME_LOG_QUYEN_DONGBO
            + "(Id_Log INTEGER," + "ThoiGian TEXT)";

    public static final String TABLE_NAME_LOG_DANG_NHAP = "tbl_Log_DN";
    public static final String CREATE_TABLE_LOG_DANG_NHAP = "CREATE TABLE " + TABLE_NAME_LOG_DANG_NHAP
            + "(ID INTEGER PRIMARY KEY AUTOINCREMENT," + "NV_ID INTEGER," + "TGian_Tcap TEXT)";

}
