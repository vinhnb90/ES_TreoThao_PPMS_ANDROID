package com.es.tungnv.utils;

import com.es.tungnv.views.R;

/**
 * Created by TUNGNV on 3/18/2016.
 */
public class GcsConstantVariables {

    public static final String NAMESPACE = "http://tempuri.org/";

    public final static int MENU_AN_HIEN = 0;
    public final static int MENU_HINH_THUC_HIEN_THI = 1;

    public static final String PROGRAM_PATH = "/ESGCS/";
    public static final String PROGRAM_DATA_PATH = "/ESGCS/DB/";
    public static final String PROGRAM_BACKUP_PATH = "/ESGCS/Backup/";
    public static final String PROGRAM_TEMP_PATH = "/ESGCS/Temp/";
    public static final String PROGRAM_PHOTO_PATH = "/ESGCS/Photo/";
    public static final String CFG_FILENAME = "ESGCS.cfg";
    public static final String[] CFG_COLUMN = new String[] { "WarningEnable3", "WarningEnable",
            "WarningEnable2", "VuotMuc", "DuoiDinhMuc", "VuotMuc2",
            "DuoiDinhMuc2", "IP", "DVIQLY", "VERSION" };
    public static final String[] ARR_TINH_TRANG_SYMBOL = {"", "U", "Q", "C", "K", "V", "M", "T", "L", "X", "H", "D", "G"};
    public static final String[] ARR_TINH_TRANG = new String[] {"Trạng thái C.tơ", "U - Không dùng", "Q - Qua vòng", "C - Cháy",
            "K - Kẹt", "V - Vắng nhà", "M - Mất", "T - CS KH báo", "L - Quá số", "X - Có thay đổi", "H - Hỏng", "D - Bị cắt điện",
            "G - Bị rời vị trí"};

    public static final String[] COLARR = {"ID_ROUTE", "MA_NVGCS", "MA_KHANG", "MA_DDO",
            "MA_NVIEN", "MA_GC", "MA_QUYEN", "MA_TRAM", "BOCSO_ID",
            "LOAI_BCS", "LOAI_CS", "TEN_KHANG", "DIA_CHI", "MA_NN", "SO_HO",
            "MA_CTO", "SERY_CTO", "HSN", "CS_CU", "TTR_CU", "SL_CU",
            "SL_TTIEP", "NGAY_CU", "CS_MOI", "TTR_MOI", "SL_MOI", "CHUOI_GIA",
            "KY", "THANG", "NAM", "NGAY_MOI", "NGUOI_GCS", "SL_THAO",
            "KIMUA_CSPK", "MA_COT", "CGPVTHD", "HTHUC_TBAO_DK", "DTHOAI_SMS",
            "EMAIL", "THOI_GIAN", "X", "Y", "SO_TIEN", "HTHUC_TBAO_TH", "TENKHANG_RUTGON",
            "TTHAI_DBO", "DU_PHONG", "TEN_FILE", "GHICHU", "TT_KHAC", "ID_SQLITE",
            "SLUONG_1","SLUONG_2","SLUONG_3","SO_HOM", "CHU_KY", "HINH_ANH", "PMAX", "NGAY_PMAX" };

//    public static final String[] ARR_MENU = {"Ghi chú", "Xóa chỉ số", "Xóa chỉ số, xóa ảnh"};
    public static final String[] ARR_MENU = {"Xóa chỉ số, xóa ảnh"};
//    public static final Integer[] ARR_ICON_MENU = {R.mipmap.gcs_ic_note, R.mipmap.gcs_ic_delete, R.mipmap.gcs_ic_delete_anh};
    public static final Integer[] ARR_ICON_MENU = {R.mipmap.gcs_ic_delete, R.mipmap.gcs_ic_delete_anh};

    public static String DATABASE_NAME = "ESGCS.s3db";
    public static final int DATABASE_VERSION = 2;

    public static final String TABLE_NAME_INDEX = "gcsindex";
    public static final String CREATE_TABLE_INDEX = "CREATE TABLE "
            + TABLE_NAME_INDEX
            + "(_id integer primary key autoincrement, so text, vitri integer)";
    public static final String TABLE_NAME_ROUTE = "GCS_LO_TRINH";
    public static final String CREATE_TABLE_ROUTE = "CREATE TABLE [GCS_LO_TRINH] ("
            + "[ID_ROUTE] INTEGER  PRIMARY KEY AUTOINCREMENT NULL," + "[SERY_CTO] VARCHAR(50)  NULL,"
            + "[STT_ORG] INTEGER  NULL," + "[STT_ROUTE] INTEGER  NULL,"
            + "[LOAI_BCS] VARCHAR(50)  NULL," + "[TEN_FILE] VARCHAR(200)  NULL)";
    public static final String TABLE_NAME_SO = "GCS_SO_NVGCS";
    public static final String CREATE_TABLE_SO = "CREATE TABLE "
            + TABLE_NAME_SO + "(_id integer primary key autoincrement,"
            + "MA_DVIQLY text," + "MA_NVGCS text," + "TEN_SOGCS text,"
            + "MA_DQL text)";
    public static final String TABLE_NAME_CHISO = "GCS_CHISO_HHU";
    public static final String CREATE_TABLE_CHISO = "CREATE TABLE "
            + TABLE_NAME_CHISO + "(ID INTEGER ,"
            + "MA_NVGCS TEXT," + "MA_KHANG TEXT," + "MA_DDO TEXT,"
            + "MA_NVIEN TEXT," + "MA_GC TEXT," + "MA_QUYEN TEXT,"
            + "MA_TRAM TEXT," + "BOCSO_ID TEXT," + "LOAI_BCS TEXT,"
            + "LOAI_CS TEXT," + "TEN_KHANG TEXT," + "DIA_CHI TEXT,"
            + "MA_NN TEXT," + "SO_HO INTEGER," + "MA_CTO TEXT,"
            + "SERY_CTO TEXT," + "HSN INTEGER," + "CS_CU FLOAT,"
            + "TTR_CU TEXT," + "SL_CU FLOAT," + "SL_TTIEP INTEGER,"
            + "NGAY_CU TEXT," + "CS_MOI FLOAT," + "TTR_MOI TEXT,"
            + "SL_MOI FLOAT," + "CHUOI_GIA TEXT," + "KY INTEGER,"
            + "THANG INTEGER," + "NAM INTEGER," + "NGAY_MOI TEXT,"
            + "NGUOI_GCS TEXT," + "SL_THAO FLOAT," + "KIMUA_CSPK INTEGER,"
            + "MA_COT TEXT," + "CGPVTHD TEXT," + "HTHUC_TBAO_DK TEXT,"
            + "DTHOAI_SMS TEXT," + "EMAIL TEXT," + "THOI_GIAN TEXT,"
            + "X TEXT," + "Y TEXT," + "SO_TIEN FLOAT,"
            + "HTHUC_TBAO_TH TEXT," + "TENKHANG_RUTGON TEXT,"
            + "TTHAI_DBO INTEGER," + "DU_PHONG TEXT," + "TEN_FILE TEXT,"
            + "GHICHU TEXT,"+ "[TT_KHAC] TEXT  NULL,"+ "[ID_SQLITE] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + "[SLUONG_1] TEXT  NULL," + "[SLUONG_2] TEXT  NULL," + "[SLUONG_3] TEXT  NULL," + "[SO_HOM] TEXT  NULL,"
            + "[CHU_KY] BLOB NULL," + "[HINH_ANH] BLOB NULL," + "[PMAX] FLOAT NULL," + "[NGAY_PMAX] TEXT NULL," + "[STR_CHECK_DSOAT] TEXT NULL)";
    public static final String TABLE_NAME_CUSTOMER = "GCS_CUSTOMER";
    @SuppressWarnings("unused")
    public static final String CREATE_TABLE_CUSTOMER = "CREATE TABLE "
            + TABLE_NAME_CUSTOMER
            + "(id integer primary key autoincrement, noi_dung text)";

    public static final String TABLE_NAME_LOG_DELETE = "GCS_LOG_DELETE";
    public static final String CREATE_TABLE_LOG_DELETE = "CREATE TABLE " + TABLE_NAME_LOG_DELETE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT," + "MA_QUYEN TEXT," + "SERY_CTO TEXT," + "CS_XOA TEXT," + "NGAY_XOA TEXT," + "LY_DO TEXT)";

    public static final String TABLE_NAME_LOG_CS = "GCS_LOG";
    public static final String CREATE_TABLE_LOG_CS = "CREATE TABLE " + TABLE_NAME_LOG_CS + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "TEN_FILE TEXT," + "MA_QUYEN TEXT," + "MA_CTO TEXT," + "SERY_CTO TEXT," + "LOAI_BCS TEXT," + "CHI_SO TEXT," + "NGAY_SUA TEXT," + "LOAI INTEGER)";

}
