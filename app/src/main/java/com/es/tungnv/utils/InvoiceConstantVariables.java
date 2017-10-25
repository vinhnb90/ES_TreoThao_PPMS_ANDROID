package com.es.tungnv.utils;

/**
 * Created by TUNGNV on 2/25/2016.
 */
public class InvoiceConstantVariables {

    public static final String PROGRAM_PATH = "/ESINVOICE/";
    public static final String PROGRAM_DB_PATH = "/ESINVOICE/DB/";
    public static final String CFG_FILENAME = "ESINVOICE.cfg";
    public static final String[] CFG_COLUMN = {"IP_SV_1", "VERSION"};

    public static final String NAMESPACE = "http://tempuri.org/";

    public static final int RESULT_LOAD_IMAGE = 1;

    public static final String[] MA_DVIQLY_YB = {"PA1001", "PA1002", "PA1003", "PA1004", "PA1005", "PA1006", "PA1007", "PA1008"};
    public static final String[] TEN_DVIQLY_YB = {"Điện lực TP Yên Bái", "Điện Lực Trấn Yên", "Điện Lực Lục Yên", "Điện lực Yên Bình", "Điện lực Nghĩa Lộ", "Điện Lực Mù Cang Chải", "Điện Lực Văn Yên", "Điện Lực Văn Chấn"};

    public static final String DATABASE_NAME = "INVOICE.s3db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME_THUNGAN = "D_THUNGAN";
    public static final String TABLE_NAME_POS_KHACHHANG = "D_POS_KHACHHANG";
    public static final String TABLE_NAME_LOG = "D_LOG";
    public static final String CREATE_TABLE_THUNGAN = "CREATE TABLE " + TABLE_NAME_THUNGAN
            + "(ID INTEGER PRIMARY KEY NOT NULL, " + "MA_DVIQLY TEXT, " + "TEN_DVIQLY TEXT, " + "DCHI_DVIQLY TEXT, "
            + "MA_STHUE TEXT, " + "SO_TKHOAN TEXT, " + "MA_TNGAN TEXT, " + "TEN_TNGAN TEXT, " + "MKHAU_TNGAN TEXT, "
            + "DTHOAI_TNGAN TEXT, "  + "MA_MAY TEXT, " + "DU_PHONG_1 TEXT, "+ "DU_PHONG_2 TEXT)";
    public static final String CREATE_TABLE_POS_KHACHHANG = "CREATE TABLE " + TABLE_NAME_POS_KHACHHANG
            + "(ID INTEGER PRIMARY KEY NOT NULL, " + "THANG_HT INTEGER NOT NULL, " + "NAM_HT INTEGER NOT NULL, " + "MA_DVIQLY TEXT  NOT NULL, "
            + "ID_HDON INTEGER NOT NULL, " + "LOAI_HDON TEXT NOT NULL, " + "LOAI_PSINH TEXT NOT NULL, " + "MA_KHANG TEXT NOT NULL, "
            + "MA_KHTT TEXT NOT NULL, " + "TEN_KHANG TEXT NOT NULL, " + "TEN_KHANG1 TEXT NOT NULL, " + "TEN_KHTT TEXT NOT NULL, "
            + "DCHI_KHANG TEXT, " + "DCHI_KHTT TEXT, " + "LOAI_KHANG INTEGER NOT NULL, " + "MANHOM_KHANG TEXT NOT NULL, "
            + "NAM_PS TEXT NOT NULL, " + "THANG_PS TEXT NOT NULL, " + "KY_PS TEXT NOT NULL, " + "TIEN_PSINH TEXT NOT NULL, "
            + "THUE_PSINH TEXT NOT NULL, " + "MA_SOGCS TEXT NOT NULL, " + "STT_TRANG INTEGER NOT NULL, " + "STT TEXT NOT NULL, "
            + "TTRANG_SSAI INTEGER NOT NULL, " + "LAN_GIAO INTEGER NOT NULL, " + "HTHUC_TTOAN TEXT NOT NULL, " + "LOAI_TBAO INTEGER NOT NULL, "
            + "NGAY_PHANH TEXT NOT NULL, " + "DTHOAI_KHANG TEXT, " + "DIEN_THOAI TEXT, " + "DTHOAI_NONG TEXT, " + "DTHOAI_TRUC TEXT, "
            + "SO_SERY TEXT NOT NULL, " + "NGAY_DKY TEXT NOT NULL, " + "NGAY_CKY TEXT NOT NULL, " + "NGAY_GIAO TEXT NOT NULL, "
            + "SO_BBGIAO INTEGER NOT NULL, " + "MA_TNGAN TEXT NOT NULL, " + "TIEN_NO TEXT NOT NULL, " + "THUE_NO TEXT NOT NULL, "
            + "NGAY_NOP TEXT NOT NULL, " + "TINH_TRANG INTEGER NOT NULL, " + "TRANG_THAI INTEGER NOT NULL, " + "SOLAN_BNHAN INTEGER NOT NULL, "
            + "SOLAN_TBAO INTEGER NOT NULL, " + "SO_CTO TEXT NOT NULL, " + "SO_HO TEXT NOT NULL, " + "DIEN_TTHU TEXT NOT NULL, "
            + "CS_DKY TEXT NOT NULL, " + "CS_CKY TEXT NOT NULL, " + "CTIET_GIA TEXT NOT NULL, " + "CTIET_DNTT TEXT NOT NULL, "
            + "CTIET_TIEN TEXT NOT NULL, " + "MA_NHANG TEXT, " + "EMAIL TEXT, " + "MA_DVIQLY_THUHO TEXT, " + "MA_TNGAN_THUHO TEXT)";
    public static final String CREATE_TABLE_LOG = "CREATE TABLE " + TABLE_NAME_LOG
            + "(ID INTEGER PRIMARY KEY NOT NULL, " + "THOI_GIAN TEXT, " + "TEN_HAM TEXT, " + "GIA_TRI TEXT, "
            + "THONG_BAO TEXT)";

}
