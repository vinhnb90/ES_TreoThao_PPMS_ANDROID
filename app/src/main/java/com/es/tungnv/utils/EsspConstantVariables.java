package com.es.tungnv.utils;

import com.es.tungnv.enums.EsspShapeType;
import com.es.tungnv.views.R;

/**
 * Created by TUNGNV on 3/18/2016.
 */
public class EsspConstantVariables {

    public static final String PROGRAM_PATH = "/ESSPV2/";
    public static final String PROGRAM_DATA_PATH = "/ESSPV2/DATA/";
    public static final String PROGRAM_SAMPLE_PATH = "/ESSPV2/SAMPLE/";
    public static final String PROGRAM_PHOTO_PATH = "/ESSPV2/PHOTOS/";
    public static final String PROGRAM_PHOTO_ANH_PATH = "/ESSPV2/PHOTOS/ANH/";
    public static final String PROGRAM_PHOTO_BV_PATH = "/ESSPV2/PHOTOS/BANVE/";
    public static final String CFG_FILENAME = "ESSP.cfg";
    public static final String[] CFG_COLUMN = {"IP_SV_1", "VERSION"};

    public final static int MENU_CHI_TIET = 0;
    public final static int MENU_KHAO_SAT = 1;
    public final static int MENU_TRA_HOSO = 2;
    public final static int MENU_PHUONG_AN = 3;
    public final static int MENU_DU_TOAN = 4;
    public final static int MENU_DU_TOAN_MOI = 5;
    public final static int MENU_HINH_ANH = 6;
    public final static int MENU_BAN_VE = 7;
    public final static int MENU_KHOANG_CACH = 8;
    public final static int MENU_NHAT_KY = 9;
    public final static int MENU_THIET_BI = 10;
    public final static int MENU_PHU_TAI = 11;
    public final static int MENU_IN = 12;

    public final static String SUGGEST_TYPE_TBI = "TBI";
    public final static String SUGGEST_TYPE_MUC_DICH = "MUC_DICH_SD_DIEN";
    public final static String SUGGEST_TYPE_DIEN_AP = "DIEN_AP";

    public final static Integer [] ARR_BRUSH_ICONS = {R.mipmap.icon_draw_pen, R.mipmap.icon_draw_line, R.mipmap.icon_draw_line, R.mipmap.icon_draw_curve,
            R.mipmap.icon_draw_arrow1, R.mipmap.icon_draw_arrow2, R.mipmap.icon_draw_note_line, R.mipmap.icon_draw_electricity_line,
            R.mipmap.icon_draw_duong, R.mipmap.icon_draw_ngo};
    public final static String [] ARR_BRUSH_LABELS = {"Bút vẽ", "Đường thẳng", "Đường ngang dọc", "Dây cáp", "Mũi tên 1 chiều", "Mũi tên 2 chiều",
            "Đường ghi chú", "Đường dây", "Đường phố", "Ngõ"};
    public final static EsspShapeType[] ARR_BRUSH_TYPES = {EsspShapeType.BRUSH, EsspShapeType.LINE, EsspShapeType.LINE_SNAP,
            EsspShapeType.SMOOTH_LINE, EsspShapeType.ARROW1, EsspShapeType.ARROW2, EsspShapeType.NOTE_LINE, EsspShapeType.ELECTRICITY_LINE,
            EsspShapeType.DUONG, EsspShapeType.NGO};

    public final static Integer [] ARR_POLE_ICONS = {R.mipmap.icon_draw_power_roles, R.mipmap.icon_draw_poles_rectanle,
            R.mipmap.icon_draw_poles_extra, R.mipmap.icon_draw_wall};
    public final static String [] ARR_POLE_LABELS = {"Cột tròn", "Cột vuông", "Cột đỡ dây", "Tường"};
    public final static EsspShapeType [] ARR_POLE_TYPES = {EsspShapeType.POWER_POLES_CIRCLE, EsspShapeType.POWER_POLES_SQUARE,
            EsspShapeType.POWER_POLES_EXTRA, EsspShapeType.WALL};

    public final static Integer [] ARR_SHAPE_ICONS = {R.mipmap.icon_draw_rectangle, R.mipmap.icon_draw_rectangle_name, R.mipmap.icon_draw_circle};
    public final static String [] ARR_SHAPE_LABELS = {"Hình chữ nhật", "Khung tên khách hàng", "Hình tròn"};
    public final static EsspShapeType [] ARR_SHAPE_TYPES = {EsspShapeType.RECTANGLE, EsspShapeType.RECTANGLE_NAME, EsspShapeType.CIRCLE};

    public final static Integer [] ARR_METER_BOX_ICONS = {R.mipmap.icon_draw_substation, R.mipmap.icon_draw_substation_circle,
            R.mipmap.icon_draw_house, R.mipmap.icon_draw_house, R.mipmap.icon_draw_top_house,
            R.mipmap.icon_draw_poles_circle_top, R.mipmap.icon_draw_cot_top, R.mipmap.icon_draw_poles_extra_top,
            R.mipmap.icon_draw_box_6, R.mipmap.icon_draw_box_4, R.mipmap.icon_draw_box_2, R.mipmap.icon_draw_box_1,
            R.mipmap.icon_draw_box_1, R.mipmap.icon_draw_box_behind, R.mipmap.icon_draw_arrow_note};
    public final static String [] ARR_METER_BOX_LABELS = {"Trạm biến áp", "Trạm biến áp", "Nhà lớn", "Nhà nhỏ", "Nhà mặt bằng", "Cột tròn", "Cột tròn", "Cột đỡ dây",
            "Hòm 6", "Hòm 4", "Hòm 2", "Hòm 1", "Hòm 3 pha", "Mặt sau hòm", "Độ cao hòm"};
    public final static EsspShapeType [] ARR_METER_BOX_TYPES = {EsspShapeType.SUBSTATION, EsspShapeType.SUBSTATION_CIRCLE, EsspShapeType.HOUSE, EsspShapeType.TOP_HOUSE_2,
            EsspShapeType.TOP_HOUSE_1, EsspShapeType.POWER_POLES_CIRCLE_TOP, EsspShapeType.POWER_POLES_TOP, EsspShapeType.POWER_POLES_EXTRA_TOP,
            EsspShapeType.BOX6, EsspShapeType.BOX4, EsspShapeType.BOX2, EsspShapeType.BOX1, EsspShapeType.BOX3F,
            EsspShapeType.BOX_BEHIND, EsspShapeType.ARROW_NOTE};

    public final static Integer [] ARR_TEXT_ICONS = {R.mipmap.icon_draw_text, R.mipmap.icon_draw_text};
    public final static String [] ARR_TEXT_LABELS = {"Thêm chữ", "Sửa chữ"};
    public final static EsspShapeType [] ARR_TEXT_TYPES = {EsspShapeType.TEXT, EsspShapeType.EDIT_TEXT};

    public final static Integer [] ARR_EDIT_ICONS = {R.mipmap.icon_draw_save, R.mipmap.icon_draw_clear};
    public final static String [] ARR_EDIT_LABELS = {"Lưu", "Trang mới"};
    public final static EsspShapeType [] ARR_EDIT_TYPES = {EsspShapeType.SAVE, EsspShapeType.CLEAR};

    public final static String [] EMAIL_SUPPORT = {"tungnv36.esolutions@gmail.com", "khuyentth.esolutions@gmail.com",
            "khuyendt.esolutions@gmail.com", "thaottt.esolutions@gmail.com", "maint.esolutions@gmail.com", "nhungntb.esolutions@gmail.com"};
//    public final static String [] EMAIL_SUPPORT = {"ngannt2.esolutions@gmail.com"};

    public final static String[] ARR_TEN_BAN_VE = {"Treo cột hòm 1", "Treo cột hòm 1 có Ti", "Treo cột hòm 2 có sẵn",
            "Treo cột hòm 2 lắp mới", "Treo cột hòm 4 có sẵn", "Treo cột hòm 4 lắp mới", "Treo cột hòm 6 có sẵn",
            "Treo cột hòm 6 lắp mới", "Treo tường 1 pha", "Treo tường 3 pha", "Hạ ngầm", "Hạ ngầm treo tường"};
    public final static Integer[] ARR_BAN_VE = {R.drawable.bv_treocot_hom1, R.drawable.bv_treocot_hom1_ti,
            R.drawable.bv_treocot_hom2_cosan, R.drawable.bv_treocot_hom2_lapmoi, R.drawable.bv_treocot_hom4_cosan,
            R.drawable.bv_treocot_hom4_lapmoi, R.drawable.bv_treocot_hom6_cosan, R.drawable.bv_treocot_hom6_lapmoi,
            R.drawable.bv_treotuong1pha, R.drawable.bv_treotuong3pha, R.drawable.bv_hangam, R.drawable.bv_hangam_treotuong};
    public final static String[] ARR_MA_LOAI_YC = { "CDIEN" };
    public final static String[] ARR_MA_LOAI_HD = { "Sinh hoạt", "Ngoài mục đích sinh hoạt" };
    public final static String[] ARR_LOAI_KH = { "Cá nhân", "Cơ quan tổ chức" };
    public final static String[] ARR_LOAI_DD = {
            "C.tơ cơ HC hoặc công tơ điện tử 1 giá(KT)",
            "Công tơ cơ/điện tử 1 giá HC + VC", "Công tơ điện tử bản 3 giá",
            "Công tơ điện tử bản 2 giá", "Công tơ điện tử 3 giá bản 1 giá",
            "Công tơ điện tử 3 giá, có giá KT" };
    public final static String[] ARR_LOAI_DD_CPC = {"Công tơ cơ 1P", "Công tơ điện tử 1P",
            "Công tơ cơ 3P", "Công tơ điện tử 3P 1 giá", "Công tơ điện tử 3P 3 giá"};
    public final static String[] ARR_MA_CAP_DA = { "Dưới 380V", "Từ 380V đến dưới 6kV",
            "Từ 6kV đến dưới 20kV", "Từ 20kV đến dưới 22kV",
            "Từ 22kV đến dưới 35kV", "35kV", "Trên 35kV đến dưới 110kV",
            "Trên 110kV" };
    public final static String[] ARR_KY_MUA_CSPK = { "CÓ", "KHÔNG" };
    public final static String[] ARR_SO_PHA = { "1", "3" };
    public final static String[] ARR_HINH_THUC = { "Trọn gói", "Tự túc cáp sau", "Tự túc vật tư", "Tự túc hoàn toàn" };
    public final static String[] ARR_LOAI_CAP = { "Đường dây trên không", "Cáp ngầm" };
    public final static String[] ARR_NKY_KSAT = { "--- Chọn ---", "Nhà mới xây", "Tách hộ", "Chung cư", "Công ty", "Công trình công cộng" };

    public static final String NAMESPACE = "http://tempuri.org/";

    public static final String DATABASE_NAME = "ESSP.s3db";
    public static final int DATABASE_VERSION = 1;

    public static final int DT_TRONGOI = 0;
    public static final int DT_TUTUC_CAP = 1;
    public static final int DT_TUTUC_VTU = 2;
    public static final int DT_TUTUC_HTOAN = 3;

    public static final String TABLE_NAME_HOSO = "D_HOSO";
    public static final String CREATE_TABLE_HOSO = "CREATE TABLE " + TABLE_NAME_HOSO
            + "(HOSO_ID INTEGER, " + "MA_DVIQLY TEXT, " + "TEN_KHANG TEXT, " + "SO_NHA TEXT, " +
            "DUONG_PHO TEXT, " + "SO_CMT TEXT, " + "NGAY_CAP TEXT, " + "NOI_CAP TEXT, " + "DTHOAI_DD TEXT, " +
            "DTHOAI_CD TEXT, " + "FAX TEXT," + "EMAIL TEXT, " + "WEBSITE TEXT, " + "SO_NHA_DDO TEXT, " +
            "DUONG_PHO_DDO TEXT, " + "TKHOAN_NGANHANG TEXT, " + "MA_NHANG TEXT, " + "MA_SOTHUE TEXT, " + "CHUC_VU TEXT," +
            "NGUOI_DAI_DIEN TEXT, " + "Gender_NGUOI_DAI_DIEN INTEGER, " + "TEN_NGUOIYCAU TEXT, " + "GenderId TEXT, " +
            "MA_TRACUU TEXT, " + "CMIS_MA_YCAU_KNAI TEXT, " + "CMIS_MA_LOAI_YCAU TEXT, " +
            "TINH_TRANG TEXT, " + "MA_LOAIHD TEXT, " + "NGUON_TIEPNHAN TEXT, " + "NGUOI_TIEPNHAN TEXT, " +
            "NGAY_TIEPNHAN TEXT, " + "NGUOI_SUA TEXT, " +
            "NGAY_SUA TEXT, " + "NGAY_TAO TEXT, " + "NGAY_HENKHAOSAT TEXT, " + "NGUOI_PHANCONG TEXT, " +
            "LOAI_DDO TEXT, " + "MA_TRAM TEXT, " + "KIMUA_CSPK TEXT, " +
            "SO_PHA TEXT, "+ "MA_DVIDCHINHC2 TEXT, " + "MA_DVIDCHINHC3 TEXT, " + "USERNAME TEXT, " + "X TEXT, " +
            "Y TEXT, " + "MA_QUYEN TEXT, " + "HS_DONG_THOI TEXT, " +
            "isTachVatTu BOOLEAN, " + "LOAIHINH_THANHTOAN INTEGER, " + "isKhaiGia BOOLEAN, " + "LOAI_CAP INTEGER, " +
            "NKY_KSAT INTEGER)";

    public static final String TABLE_NAME_TRAM = "D_TRAM";
    public static final String CREATE_TABLE_TRAM = "CREATE TABLE " + TABLE_NAME_TRAM + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "MA_NVIEN TEXT, " + "MA_TRAM TEXT, " + "TEN_TRAM TEXT, " + "LOAI_TRAM TEXT, " + "CSUAT_TRAM TEXT, " + "MA_CAPDA TEXT, " +
            "MA_CAPDA_RA TEXT, " + "D_TRAM_ID INTEGER, " + "DINH_DANH TEXT)";

    public static final String TABLE_NAME_NGAN_HANG = "D_NGAN_HANG";
    public static final String CREATE_TABLE_NGAN_HANG = "CREATE TABLE " + TABLE_NAME_NGAN_HANG + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "MA_NHANG TEXT, " + "TEN_NHANG TEXT, " + "DIA_CHI TEXT, " + "DIEN_THOAI TEXT, " + "SO_TKHOAN TEXT, " + "MA_NHANG_CHA TEXT)";

    public static final String TABLE_NAME_VTU = "D_VTU";
    public static final String CREATE_TABLE_VTU = "CREATE TABLE " + TABLE_NAME_VTU + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "MA_NVIEN TEXT, " + "MA_VTU TEXT, " + "TEN_VTU TEXT, " + "MA_LOAI_CPHI TEXT, " + "DON_GIA TEXT, " + "DVI_TINH TEXT, " +
            "DON_GIA_KH TEXT, " + "LOAI INTTEGER, " + "CHUNG_LOAI INTEGER, " + "DG_TRONGOI TEXT, " + "DG_NCONG TEXT, " +
            "DG_VTU TEXT, " + "DG_MTCONG TEXT)";//0 - VT khác, 1 - Dây cáp, 2 - Công tơ, 3 - Hòm

    public static final String TABLE_NAME_DTOAN = "KS_DTOAN";
    public static final String CREATE_TABLE_DTOAN = "CREATE TABLE " + TABLE_NAME_DTOAN + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "MA_NVIEN TEXT, " + "HOSO_ID INTEGER, " + "MA_VTU TEXT, " + "MA_LOAI_CPHI TEXT, " + "SO_LUONG TEXT, " +
            "DON_GIA TEXT, " + "SO_HUU TEXT, " + "STT TEXT, " + "THANH_TIEN TEXT, " + "TT_DON_GIA TEXT, " + "TT_TU_TUC INTEGER, " +
            "TT_THU_HOI INTEGER, " + "TINH_TRANG TEXT," + "HSDC_K1NC TEXT, " + "HSDC_K2NC TEXT, " + "HSDC_MTC TEXT)";

    public static final String TABLE_NAME_PHANCONG = "KS_PHANCONG";
    public static final String CREATE_TABLE_PHANCONG = "CREATE TABLE " + TABLE_NAME_PHANCONG +
            "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " + "HOSO_ID INTEGER, " + "USER TEXT)";

    public static final String TABLE_NAME_ANH = "KS_ANH";
    public static final String CREATE_TABLE_ANH = "CREATE TABLE " + TABLE_NAME_ANH + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "HOSO_ID INTEGER, " + "TEN_ANH TEXT, " + "GHI_CHU TEXT, " + "TINH_TRANG TEXT)";

    public static final String TABLE_NAME_DVIQLY = "D_DVIQLY";
    public static final String CREATE_TABLE_DVIQLY = "CREATE TABLE " + TABLE_NAME_DVIQLY + "(ID INTEGER PRIMARY KEY NOT NULL, " +
            "MA_DVIQLY TEXT, " + "TEN_DVIQLY TEXT)";

    public static final String TABLE_NAME_REMEMBER = "D_REMEMBER";
    public static final String CREATE_TABLE_REMEMBER = "CREATE TABLE " + TABLE_NAME_REMEMBER + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "MA_DVIQLY TEXT, " + "USERNAME TEXT, " + "PASSWORD TEXT)";

    public static final String TABLE_NAME_BAN_VE = "KS_BANVE";
    public static final String CREATE_TABLE_BANVE = "CREATE TABLE " + TABLE_NAME_BAN_VE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "HOSO_ID INTEGER, " + "TEN_ANH TEXT, " + "MO_TA TEXT, " + "TINH_TRANG TEXT)";

    public static final String TABLE_NAME_TNGAI = "D_TNGAI";
    public static final String CREATE_TABLE_TNGAI = "CREATE TABLE " + TABLE_NAME_TNGAI + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "MA_DVIQLY TEXT, " + "MA_TNGAI TEXT, " + "TEN_TNGAI TEXT, " + "TCHAT_KPHUC TEXT, " + "NGUYEN_NHAN TEXT, " + "MA_CVIEC TEXT, " +
            "NGAY_HLUC TEXT)";

    public static final String TABLE_NAME_KQ_KSAT = "KS_KQ_KSAT";
    public static final String CREATE_TABLE_KQ_KSAT = "CREATE TABLE " + TABLE_NAME_KQ_KSAT + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "KQ_KHSAT_ID INTEGER, " + "HOSO_ID INTEGER, " + "MA_TNGAI TEXT, " + "GHI_CHU TEXT)";

    public static final String TABLE_NAME_NVIEN = "D_NVIEN";
    public static final String CREATE_TABLE_NVIEN = "CREATE TABLE " + TABLE_NAME_NVIEN + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "BUNDLE_MA_NVIEN TEXT, " + "USERNAME TEXT)";

    public static final String TABLE_NAME_PA = "KS_PHUONG_AN";
    public static final String CREATE_TABLE_PA = "CREATE TABLE " + TABLE_NAME_PA + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "HOSO_ID INTEGER, " + "DODEM_CTO_A TEXT, " + "DODEM_CTO_V TEXT, " + "DODEM_CTO_TI TEXT, " +
            "VTRI_CTO INTEGER, " + "COT_SO TEXT, " + "VI_TRI_KHAC TEXT, " + "HTHUC_TTOAN INTEGER, " + "DIEM_DAUDIEN TEXT, " + "DTUONG_KHANG INTEGER, " +
            "TTRANG_SDUNG_DIEN INTEGER, " + "HTHUC_GCS INTEGER, " + "HTK TEXT, " + "TINH_TRANG INTEGER)";//1 - Tiền mặt, 2 - Chuyển khoản

    public static final String TABLE_NAME_NK = "KS_NHAT_KY";
    public static final String CREATE_TABLE_NK = "CREATE TABLE " + TABLE_NAME_NK + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "HOSO_ID INTEGER, " + "TINH_TRANG_SD_DIEN TEXT, " + "MUC_DICH_SD_DIEN TEXT, " + "TINH_TRANG INTEGER)";//1 - Tiền mặt, 2 - Chuyển khoản

    public static final String TABLE_NAME_TBI = "KS_THIET_BI";
    public static final String CREATE_TABLE_TBI = "CREATE TABLE " + TABLE_NAME_TBI + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "HOSO_ID INTEGER, " + "MUC_DICH TEXT, " + "LOAI_TBI TEXT, " + "CONG_SUAT TEXT, " +
            "SO_LUONG INTEGER, " + "DIENAP_SUDUNG TEXT, " + "TINH_TRANG INTEGER)";

    public static final String TABLE_NAME_CSUAT = "KS_CONG_SUAT";
//    public static final String CREATE_TABLE_CSUAT = "CREATE TABLE " + TABLE_NAME_CSUAT + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
//            "HOSO_ID INTEGER, " + "CS1 TEXT, " + "CS2 TEXT, " + "CS3 TEXT, " + "CS4 TEXT, " + "CS5 TEXT, " +
//            "CS6 TEXT, " + "CS7 TEXT, " + "CS8 TEXT, " + "CS9 TEXT, " + "CS10 TEXT, " + "CS11 TEXT, " + "CS12 TEXT, " + "TINH_TRANG INTEGER)";
    public static final String CREATE_TABLE_CSUAT = "CREATE TABLE " + TABLE_NAME_CSUAT + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "HOSO_ID INTEGER, " + "KHOANG_TGIAN TEXT, " + "CONG_SUAT TEXT, " + "TINH_TRANG INTEGER)";

    public static final String TABLE_NAME_DMUC = "KS_DINH_MUC";
    public static final String CREATE_TABLE_DMUC = "CREATE TABLE " + TABLE_NAME_DMUC + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "HOSO_ID INTEGER, " + "TEN_CHU_HO TEXT, " + "DCHI_CHU_HO TEXT, " + "MA_KHANG TEXT, " +
            "SOHO_HTAI INTEGER, " + "SOHO_BSUA INTEGER, " + "THANG_TACH_HO TEXT, " + "NAM_TACH_HO TEXT, " + "TINH_TRANG INTEGER)";

    public static final String TABLE_NAME_TLY_HDONG = "KS_TLY_HDONG";
    public static final String CREATE_TABLE_TLY_HDONG = "CREATE TABLE " + TABLE_NAME_TLY_HDONG + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "MA_YCAU_KNAI TEXT, " + "D_HOSO_ID INTEGER, " + "LYDO_TLY TEXT, " + "MA_KHANG TEXT, " + "CS_TLY TEXT, " + "TINH_TRANG INTEGER)";

    public static final String TABLE_NAME_SO_GCS = "D_SO_GCS";
    public static final String CREATE_TABLE_SO_GCS = "CREATE TABLE " + TABLE_NAME_SO_GCS + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "MA_DVIQLY TEXT, " + "TEN_SO TEXT, " + "MA_SO TEXT)";

    public static final String TABLE_NAME_TGIAN_DBO = "D_TGIAN_DBO";
    public static final String CREATE_TABLE_TGIAN_DBO = "CREATE TABLE " + TABLE_NAME_TGIAN_DBO + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "MA_DVIQLY TEXT, " + "USERNAME TEXT, " + "DTUONG_DBO TEXT, " + "TGIAN_DBO TEXT)";

    public static final String TABLE_NAME_DTOAN_VTU_NGOAIHT = "KS_DTOAN_VTU_NGOAIHT";
    public static final String CREATE_TABLE_DTOAN_VTU_NGOAIHT = "CREATE TABLE " + TABLE_NAME_DTOAN_VTU_NGOAIHT + "(VTKHTT_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "HOSO_ID INTEGER, " + "TENVTU_MTB TEXT, " + "SO_LUONG TEXT, " + "DVI_TINH TEXT, " + "DON_GIA_KH TEXT, " + "GHI_CHU TEXT, " +
            "TINH_TRANG INTEGER, " + "CHUNG_LOAI INTEGER" + "DG_TRONGOI TEXT, " + "DG_NCONG TEXT, " + "DG_VTU TEXT, " + "DG_MTCONG TEXT," +
            "HSDC_K1NC TEXT, " + "HSDC_K2NC TEXT, " + "HSDC_MTC TEXT)";

    public static final String TABLE_NAME_DANH_MUC_SUGGEST = "D_LOG_TBI";
    public static final String CREATE_TABLE_DANH_MUC_SUGGEST = "CREATE TABLE " + TABLE_NAME_DANH_MUC_SUGGEST + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "LOG_TYPE TEXT," + "LOG_CONTENT TEXT)";

    public static final String TABLE_NAME_HOSO_NTHU = "D_HOSO_NTHU";
    public static final String CREATE_TABLE_HOSO_NTHU = "CREATE TABLE " + TABLE_NAME_HOSO_NTHU + " (HoSoId INTEGER, " +
            "isNHIEUBIEUGIA INTEGER, " + "KIMNIEMCHISO TEXT, " + "MA_HDONG TEXT, " + "MA_KHANG TEXT, " + "TEN_KHANG TEXT, " + "DIA_CHI TEXT, " +
            "SO_CMT TEXT, " + "DIEN_THOAI TEXT, " + "MA_GCS TEXT, " +
            "LYDO_TREOTHAO TEXT, " + "LOAI_CTO TEXT, " + "MA_CTO TEXT, " + "MA_NUOC TEXT, " + "TEN_NUOC TEXT, " + "NAM_SX INTEGER, " + "DONG_DIEN TEXT, " +
            "DIEN_AP TEXT, " + "HANGSO_K TEXT, " + "CCX TEXT, " + "TEM_CQUANG TEXT, " + "SOLAN_CBAO INTEGER, " + "HS_NHAN TEXT, " +
            "SO_CTO TEXT, " + "LAPQUA_TI TEXT, " + "LAPQUA_TU TEXT, " +
            "HS_NHAN_LUOI TEXT, " + "P_MAX TEXT, " + "TD_P_MAX TEXT, " + "H1_P_MAX TEXT, " + "TD_H1_P_MAX TEXT, " + "TTRANG_CHI_NIEMPHONG TEXT, " +
            "MASO_CHI_KDINH TEXT, " + "SOVIEN_CHIKDINH TEXT, " + "MASO_CHICONGQUANG TEXT, " + "SOVIEN_CHICONGQUANG TEXT, " +
            "MASO_CHIHOM TEXT, " + "SOVIEN_CHIHOM TEXT, " + "MASO_TEMKDINH TEXT, " + "NGAY_LAP TEXT, " + "NGUOI_LAP TEXT, " +
            "MA_NVIEN TEXT, " + "CMIS_MA_YCAU_KNAI TEXT, " + "NGAY_KDINH_FORMAT TEXT, " + "TEN_NHAN_VIEN TEXT, " + "SO_BIEN_BAN TEXT, " +
            "CHI_SO_TREO TEXT, " + "USER_NAME TEXT, " + "TINH_TRANG INTEGER)";

    public static final String TABLE_NAME_DUTOAN_NTHU = "D_DUTOAN_NTHU";
    public static final String CREATE_TABLE_DUTOAN_NTHU = "CREATE TABLE " + TABLE_NAME_DUTOAN_NTHU + " (DUTOAN_ID INTEGER, " +
            "HOSO_ID INTEGER, " + "MA_DVIQLY TEXT, " + "MA_VTU TEXT, " + "SO_LUONG FLOAT, " + "SO_HUU INTEGER, " + "STT INTEGER, " +
            "GHI_CHU TEXT, " + "TT_TUTUC INTEGER, " + "TT_THUHOI INTEGER, " + "THUC_TE FLOAT, " + "CHENH_LECH FLOAT, " +
            "TEN_VTU TEXT, " + "DON_VI_TINH TEXT, " + "KHTT TEXT)";
}
