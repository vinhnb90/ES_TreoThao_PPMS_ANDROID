package com.es.tungnv.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.es.tungnv.entity.EsspEntityHoSo;
import com.es.tungnv.entity.EsspEntityHoSoThiCong;
import com.es.tungnv.entity.EsspEntityPhuongAn;
import com.es.tungnv.utils.EsspConstantVariables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.es.tungnv.utils.EsspCommon.TINH_TRANG;

/**
 * Created by TUNGNV on 3/30/2016.
 */
public class EsspSqliteConnection extends SQLiteOpenHelper {

    //region Khởi tạo
    private static EsspSqliteConnection instance;
    private SQLiteDatabase database;

    private EsspSqliteConnection(Context context) {
        super(context, Environment.getExternalStorageDirectory() + EsspConstantVariables.PROGRAM_DATA_PATH + EsspConstantVariables.DATABASE_NAME, null, EsspConstantVariables.DATABASE_VERSION);
        SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory() + EsspConstantVariables.PROGRAM_DATA_PATH + EsspConstantVariables.DATABASE_NAME, null);
//        database = getWritableDatabase();
    }

    @Override
    public synchronized void close() {
        if (database != null) {
            database.close();
            database = null;
        }
        super.close();
    }

    public static synchronized EsspSqliteConnection getInstance(Context context) {
        if (instance == null) {
            instance = new EsspSqliteConnection(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(EsspConstantVariables.CREATE_TABLE_HOSO);
        db.execSQL(EsspConstantVariables.CREATE_TABLE_TRAM);
        db.execSQL(EsspConstantVariables.CREATE_TABLE_VTU);
        db.execSQL(EsspConstantVariables.CREATE_TABLE_DTOAN);
        db.execSQL(EsspConstantVariables.CREATE_TABLE_PHANCONG);
        db.execSQL(EsspConstantVariables.CREATE_TABLE_ANH);
        db.execSQL(EsspConstantVariables.CREATE_TABLE_DVIQLY);
        db.execSQL(EsspConstantVariables.CREATE_TABLE_REMEMBER);
        db.execSQL(EsspConstantVariables.CREATE_TABLE_BANVE);
        db.execSQL(EsspConstantVariables.CREATE_TABLE_TNGAI);
        db.execSQL(EsspConstantVariables.CREATE_TABLE_KQ_KSAT);
        db.execSQL(EsspConstantVariables.CREATE_TABLE_NVIEN);
        db.execSQL(EsspConstantVariables.CREATE_TABLE_PA);
        db.execSQL(EsspConstantVariables.CREATE_TABLE_NK);
        db.execSQL(EsspConstantVariables.CREATE_TABLE_TBI);
        db.execSQL(EsspConstantVariables.CREATE_TABLE_CSUAT);
        db.execSQL(EsspConstantVariables.CREATE_TABLE_DMUC);
        db.execSQL(EsspConstantVariables.CREATE_TABLE_TLY_HDONG);
        db.execSQL(EsspConstantVariables.CREATE_TABLE_SO_GCS);
        db.execSQL(EsspConstantVariables.CREATE_TABLE_TGIAN_DBO);
        db.execSQL(EsspConstantVariables.CREATE_TABLE_NGAN_HANG);
        db.execSQL(EsspConstantVariables.CREATE_TABLE_DTOAN_VTU_NGOAIHT);
        db.execSQL(EsspConstantVariables.CREATE_TABLE_DANH_MUC_SUGGEST);
        db.execSQL(EsspConstantVariables.CREATE_TABLE_HOSO_NTHU);
        db.execSQL(EsspConstantVariables.CREATE_TABLE_DUTOAN_NTHU);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EsspConstantVariables.TABLE_NAME_HOSO);
        db.execSQL("DROP TABLE IF EXISTS " + EsspConstantVariables.TABLE_NAME_TRAM);
        db.execSQL("DROP TABLE IF EXISTS " + EsspConstantVariables.TABLE_NAME_VTU);
        db.execSQL("DROP TABLE IF EXISTS " + EsspConstantVariables.TABLE_NAME_DTOAN);
        db.execSQL("DROP TABLE IF EXISTS " + EsspConstantVariables.TABLE_NAME_PHANCONG);
        db.execSQL("DROP TABLE IF EXISTS " + EsspConstantVariables.TABLE_NAME_ANH);
        db.execSQL("DROP TABLE IF EXISTS " + EsspConstantVariables.TABLE_NAME_DVIQLY);
        db.execSQL("DROP TABLE IF EXISTS " + EsspConstantVariables.TABLE_NAME_REMEMBER);
        db.execSQL("DROP TABLE IF EXISTS " + EsspConstantVariables.TABLE_NAME_BAN_VE);
        db.execSQL("DROP TABLE IF EXISTS " + EsspConstantVariables.TABLE_NAME_TNGAI);
        db.execSQL("DROP TABLE IF EXISTS " + EsspConstantVariables.TABLE_NAME_KQ_KSAT);
        db.execSQL("DROP TABLE IF EXISTS " + EsspConstantVariables.TABLE_NAME_NVIEN);
        db.execSQL("DROP TABLE IF EXISTS " + EsspConstantVariables.TABLE_NAME_PA);
        db.execSQL("DROP TABLE IF EXISTS " + EsspConstantVariables.TABLE_NAME_NK);
        db.execSQL("DROP TABLE IF EXISTS " + EsspConstantVariables.TABLE_NAME_TBI);
        db.execSQL("DROP TABLE IF EXISTS " + EsspConstantVariables.TABLE_NAME_CSUAT);
        db.execSQL("DROP TABLE IF EXISTS " + EsspConstantVariables.TABLE_NAME_DMUC);
        db.execSQL("DROP TABLE IF EXISTS " + EsspConstantVariables.TABLE_NAME_TLY_HDONG);
        db.execSQL("DROP TABLE IF EXISTS " + EsspConstantVariables.TABLE_NAME_SO_GCS);
        db.execSQL("DROP TABLE IF EXISTS " + EsspConstantVariables.TABLE_NAME_TGIAN_DBO);
        db.execSQL("DROP TABLE IF EXISTS " + EsspConstantVariables.TABLE_NAME_NGAN_HANG);
        db.execSQL("DROP TABLE IF EXISTS " + EsspConstantVariables.TABLE_NAME_DTOAN_VTU_NGOAIHT);
        db.execSQL("DROP TABLE IF EXISTS " + EsspConstantVariables.TABLE_NAME_DANH_MUC_SUGGEST);
        db.execSQL("DROP TABLE IF EXISTS " + EsspConstantVariables.TABLE_NAME_HOSO_NTHU);
        db.execSQL("DROP TABLE IF EXISTS " + EsspConstantVariables.TABLE_NAME_DUTOAN_NTHU);
        onCreate(db);
    }

    public void updateDB() {
        database = this.getWritableDatabase();
        Cursor dbCursorVTU = database.query(EsspConstantVariables.TABLE_NAME_VTU, null, null, null, null, null, null);
        String[] columnNamesVTU = dbCursorVTU.getColumnNames();
        List<String> stringListVTU = new ArrayList<String>(Arrays.asList(columnNamesVTU));
        if (!stringListVTU.contains("DG_TRONGOI")) {
            database.execSQL("ALTER TABLE " + EsspConstantVariables.TABLE_NAME_VTU + " ADD COLUMN DG_TRONGOI TEXT");
        }
        if (!stringListVTU.contains("DG_NCONG")) {
            database.execSQL("ALTER TABLE " + EsspConstantVariables.TABLE_NAME_VTU + " ADD COLUMN DG_NCONG TEXT");
        }
        if (!stringListVTU.contains("DG_VTU")) {
            database.execSQL("ALTER TABLE " + EsspConstantVariables.TABLE_NAME_VTU + " ADD COLUMN DG_VTU TEXT");
        }
        if (!stringListVTU.contains("DG_MTCONG")) {
            database.execSQL("ALTER TABLE " + EsspConstantVariables.TABLE_NAME_VTU + " ADD COLUMN DG_MTCONG TEXT");
        }

        Cursor dbCursorHS = database.query(EsspConstantVariables.TABLE_NAME_HOSO, null, null, null, null, null, null);
        String[] columnNamesHS = dbCursorHS.getColumnNames();
        List<String> stringListHS = new ArrayList<String>(Arrays.asList(columnNamesHS));
        if (!stringListHS.contains("MA_QUYEN")) {
            database.execSQL("ALTER TABLE " + EsspConstantVariables.TABLE_NAME_HOSO + " ADD COLUMN MA_QUYEN TEXT");
        }
        if (!stringListHS.contains("HS_DONG_THOI")) {
            database.execSQL("ALTER TABLE " + EsspConstantVariables.TABLE_NAME_HOSO + " ADD COLUMN HS_DONG_THOI TEXT");
        }
        if (!stringListHS.contains("isTachVatTu")) {
            database.execSQL("ALTER TABLE " + EsspConstantVariables.TABLE_NAME_HOSO + " ADD COLUMN isTachVatTu BOOLEAN");
            updateKS(false);
        }
        if (!stringListHS.contains("LOAIHINH_THANHTOAN")) {
            database.execSQL("ALTER TABLE " + EsspConstantVariables.TABLE_NAME_HOSO + " ADD COLUMN LOAIHINH_THANHTOAN INTEGER");
            updateKS(0);
        }
        if (!stringListHS.contains("isKhaiGia")) {
            database.execSQL("ALTER TABLE " + EsspConstantVariables.TABLE_NAME_HOSO + " ADD COLUMN isKhaiGia BOOLEAN");
            updateKG(false);
        }
        if (!stringListHS.contains("LOAI_CAP")) {
            database.execSQL("ALTER TABLE " + EsspConstantVariables.TABLE_NAME_HOSO + " ADD COLUMN LOAI_CAP BOOLEAN");
            updateLoaiCap(0);
        }
        if (!stringListHS.contains("NKY_KSAT")) {
            database.execSQL("ALTER TABLE " + EsspConstantVariables.TABLE_NAME_HOSO + " ADD COLUMN NKY_KSAT INTEGER");
//            updateNKy(0);
        }

        Cursor dbCursorVTNHT = database.query(EsspConstantVariables.TABLE_NAME_DTOAN_VTU_NGOAIHT, null, null, null, null, null, null);
        String[] columnNamesVTNHT = dbCursorVTNHT.getColumnNames();
        List<String> stringListVTNHT = new ArrayList<String>(Arrays.asList(columnNamesVTNHT));
        if (!stringListVTNHT.contains("CHUNG_LOAI")) {
            database.execSQL("ALTER TABLE " + EsspConstantVariables.TABLE_NAME_DTOAN_VTU_NGOAIHT + " ADD COLUMN CHUNG_LOAI INTEGER");
            updateChungLoai(1);
        }
        if (!stringListVTNHT.contains("DG_TRONGOI")) {
            database.execSQL("ALTER TABLE " + EsspConstantVariables.TABLE_NAME_DTOAN_VTU_NGOAIHT + " ADD COLUMN DG_TRONGOI TEXT");
            updateDGTronGoi(0);
        }
        if (!stringListVTNHT.contains("DG_NCONG")) {
            database.execSQL("ALTER TABLE " + EsspConstantVariables.TABLE_NAME_DTOAN_VTU_NGOAIHT + " ADD COLUMN DG_NCONG TEXT");
            updateDGNCong(0);
        }
        if (!stringListVTNHT.contains("DG_VTU")) {
            database.execSQL("ALTER TABLE " + EsspConstantVariables.TABLE_NAME_DTOAN_VTU_NGOAIHT + " ADD COLUMN DG_VTU TEXT");
            updateDGVatTu(0);
        }
        if (!stringListVTNHT.contains("DG_MTCONG")) {
            database.execSQL("ALTER TABLE " + EsspConstantVariables.TABLE_NAME_DTOAN_VTU_NGOAIHT + " ADD COLUMN DG_MTCONG TEXT");
            updateDGMTC(0);
        }
        if (!stringListVTNHT.contains("HSDC_K1NC")) {
            database.execSQL("ALTER TABLE " + EsspConstantVariables.TABLE_NAME_DTOAN_VTU_NGOAIHT + " ADD COLUMN HSDC_K1NC TEXT");
            updateHSDC1NCNHT(1);
        }
        if (!stringListVTNHT.contains("HSDC_K2NC")) {
            database.execSQL("ALTER TABLE " + EsspConstantVariables.TABLE_NAME_DTOAN_VTU_NGOAIHT + " ADD COLUMN HSDC_K2NC TEXT");
            updateHSDC2NCNHT(1);
        }
        if (!stringListVTNHT.contains("HSDC_MTC")) {
            database.execSQL("ALTER TABLE " + EsspConstantVariables.TABLE_NAME_DTOAN_VTU_NGOAIHT + " ADD COLUMN HSDC_MTC TEXT");
            updateHSDCMTCNHT(1);
        }

        Cursor dbCursorDT = database.query(EsspConstantVariables.TABLE_NAME_DTOAN, null, null, null, null, null, null);
        String[] columnNamesDT = dbCursorDT.getColumnNames();
        List<String> stringListDT = new ArrayList<String>(Arrays.asList(columnNamesDT));
        if (!stringListDT.contains("HSDC_K1NC")) {
            database.execSQL("ALTER TABLE " + EsspConstantVariables.TABLE_NAME_DTOAN + " ADD COLUMN HSDC_K1NC TEXT");
            updateHSDC1NC(1);
        }
        if (!stringListDT.contains("HSDC_K2NC")) {
            database.execSQL("ALTER TABLE " + EsspConstantVariables.TABLE_NAME_DTOAN + " ADD COLUMN HSDC_K2NC TEXT");
            updateHSDC2NC(1);
        }
        if (!stringListDT.contains("HSDC_MTC")) {
            database.execSQL("ALTER TABLE " + EsspConstantVariables.TABLE_NAME_DTOAN + " ADD COLUMN HSDC_MTC TEXT");
            updateHSDCMTC(1);
        }

        Cursor dbCursorKQKS = database.query(EsspConstantVariables.TABLE_NAME_KQ_KSAT, null, null, null, null, null, null);
        String[] columnNamesKQKS = dbCursorKQKS.getColumnNames();
        List<String> stringListKQKS = new ArrayList<String>(Arrays.asList(columnNamesKQKS));
        if (!stringListKQKS.contains("GHI_CHU")) {
            database.execSQL("ALTER TABLE " + EsspConstantVariables.TABLE_NAME_KQ_KSAT + " ADD COLUMN GHI_CHU TEXT");
        }

        database.close();
    }
    //endregion

    //region Xử lý bảng đơn vị quản lý
    public long insertDataDVIQLY(String MA_DVIQLY, String TEN_DVIQLY) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
//        values.put("ID", Integer.parseInt(ID));
        values.put("MA_DVIQLY", MA_DVIQLY);
        values.put("TEN_DVIQLY", TEN_DVIQLY);
        return database.insert(EsspConstantVariables.TABLE_NAME_DVIQLY, null, values);
    }

    public long deleteAllDataDVIQLY() {
        database = this.getWritableDatabase();
        return database.delete(EsspConstantVariables.TABLE_NAME_DVIQLY, null, null);
    }

    public List<String> getDataDVIQLY() {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + EsspConstantVariables.TABLE_NAME_DVIQLY;
        Cursor c = database.rawQuery(strQuery, null);
        List<String> list_dviqly = new ArrayList<String>();
        if (c.moveToFirst()) {
            do {
                list_dviqly.add(new StringBuilder(c.getString(1)).append(" - ").append(c.getString(2)).toString());
            } while (c.moveToNext());
        }
        return list_dviqly;
    }

    public List<String> getListIDMaDviQly() {
        List<String> rowid = new ArrayList<String>();
        database = this.getReadableDatabase();
        String strQuery = new StringBuilder("SELECT MA_DVIQLY FROM ").append(EsspConstantVariables.TABLE_NAME_DVIQLY).toString();
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            do {
                rowid.add(c.getString(0));
            } while (c.moveToNext());
        }
        return rowid;
    }

    public int getPosDviQly(String MA_DVIQLY) {
        int pos = 0;
        database = this.getReadableDatabase();
        String strQuery = "SELECT MA_DVIQLY FROM " + EsspConstantVariables.TABLE_NAME_DVIQLY;
        Cursor c = database.rawQuery(strQuery, null);
        int i = 0;
        if (c.moveToFirst()) {
            do {
                if (c.getString(0).equals(MA_DVIQLY)) {
                    pos = i;
                }
                i++;
            } while (c.moveToNext());
        }
        return pos;
    }

    public String getMaDviByID(int ID_DVIQLY) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT MA_DVIQLY FROM " + EsspConstantVariables.TABLE_NAME_DVIQLY + " WHERE ID = " + ID_DVIQLY;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getString(0);
        }
        return "";
    }

    public String getTenDviByMa(String MA_DVIQLY) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT TEN_DVIQLY FROM " + EsspConstantVariables.TABLE_NAME_DVIQLY + " WHERE MA_DVIQLY = '" + MA_DVIQLY + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getString(0);
        }
        return "";
    }
    //endregion

    //region Xử lý bảng nhớ mật khẩu
    public long insertDataRememger(String MA_DVIQLY, String USERNAME, String PASSWORD) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MA_DVIQLY", MA_DVIQLY);
        values.put("USERNAME", USERNAME);
        values.put("PASSWORD", PASSWORD);
        return database.insert(EsspConstantVariables.TABLE_NAME_REMEMBER, null, values);
    }

    public long deleteAllDataRemember() {
        database = this.getWritableDatabase();
        return database.delete(EsspConstantVariables.TABLE_NAME_REMEMBER, null, null);
    }

    public Cursor getDataRemember() {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + EsspConstantVariables.TABLE_NAME_REMEMBER;
        return database.rawQuery(strQuery, null);
    }

    public boolean checkRemember() {
        int count = 0;
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + EsspConstantVariables.TABLE_NAME_REMEMBER;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            count = c.getInt(0);
        }
        if (count > 0)
            return true;
        else
            return false;
    }
    //endregion

    //region Xử lý bảng thời gian đồng bộ
    public long insertDataTgianDbo(String MA_DVIQLY, String USERNAME, String DTUONG_DBO, String TGIAN_DBO) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MA_DVIQLY", MA_DVIQLY);
        values.put("USERNAME", USERNAME);
        values.put("DTUONG_DBO", DTUONG_DBO);
        values.put("TGIAN_DBO", TGIAN_DBO);
        return database.insert(EsspConstantVariables.TABLE_NAME_TGIAN_DBO, null, values);
    }

    public long updateDataTgianDbo(String USERNAME, String DTUONG_DBO, String TGIAN_DBO) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TGIAN_DBO", TGIAN_DBO);
        return database.update(EsspConstantVariables.TABLE_NAME_TGIAN_DBO, values, "USERNAME=? and DTUONG_DBO=?", new String[]{USERNAME, DTUONG_DBO});
    }

    public boolean isTGianDboExist(String USERNAME, String DTUONG_DBO) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + EsspConstantVariables.TABLE_NAME_TGIAN_DBO + " WHERE USERNAME = '" + USERNAME + "' AND DTUONG_DBO = '" + DTUONG_DBO + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            if (c.getInt(0) > 0) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public String getTGianDBo(String MA_DVIQLY, String USERNAME, String DTUONG_DBO) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT TGIAN_DBO FROM " + EsspConstantVariables.TABLE_NAME_TGIAN_DBO + " WHERE MA_DVIQLY = '" + MA_DVIQLY + "' AND USERNAME = '" + USERNAME + "' AND DTUONG_DBO = '" + DTUONG_DBO + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getString(0);
        }
        return "";
    }
    //endregion

    //region Xử lý bảng vật tư
    public long insertDataVT(String MA_VTU, String MA_DVIQLY, String TEN_VTU,
                             String MA_LOAI_CPHI, String DON_GIA, String DVI_TINH, String DON_GIA_KH, String LOAI, String CHUNG_LOAI,
                             String DG_TRONGOI, String DG_NCONG, String DG_VTU, String DG_MTCONG) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MA_DVIQLY", MA_DVIQLY);
        values.put("MA_VTU", MA_VTU);
        values.put("TEN_VTU", TEN_VTU);
        values.put("MA_LOAI_CPHI", MA_LOAI_CPHI);
        values.put("DON_GIA", DON_GIA);
        values.put("DVI_TINH", DVI_TINH);
        values.put("DON_GIA_KH", DON_GIA_KH);
        values.put("LOAI", LOAI);
        values.put("CHUNG_LOAI", CHUNG_LOAI);
        values.put("DG_TRONGOI", DG_TRONGOI);
        values.put("DG_NCONG", DG_NCONG);
        values.put("DG_VTU", DG_VTU);
        values.put("DG_MTCONG", DG_MTCONG);
        return database.insert(EsspConstantVariables.TABLE_NAME_VTU, null, values);
    }

    public long updateDataVT(String MA_VTU, String TEN_VTU, String MA_LOAI_CPHI, String DON_GIA, String DVI_TINH, String DON_GIA_KH,
                             String LOAI, String CHUNG_LOAI, String DG_TRONGOI, String DG_NCONG, String DG_VTU, String DG_MTCONG) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TEN_VTU", TEN_VTU);
        values.put("MA_LOAI_CPHI", MA_LOAI_CPHI);
        values.put("DON_GIA", DON_GIA);
        values.put("DVI_TINH", DVI_TINH);
        values.put("DON_GIA_KH", DON_GIA_KH);
        values.put("LOAI", LOAI);
        values.put("CHUNG_LOAI", CHUNG_LOAI);
        values.put("DG_TRONGOI", DG_TRONGOI);
        values.put("DG_NCONG", DG_NCONG);
        values.put("DG_VTU", DG_VTU);
        values.put("DG_MTCONG", DG_MTCONG);
        return database.update(EsspConstantVariables.TABLE_NAME_VTU, values, "MA_VTU=?", new String[]{"" + MA_VTU});
    }

    public Cursor getVatTu() {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + EsspConstantVariables.TABLE_NAME_VTU;
        return database.rawQuery(strQuery, null);
    }

    public Cursor getVatTu(String MA_VTU) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + EsspConstantVariables.TABLE_NAME_VTU + " WHERE MA_VTU = '" + MA_VTU + "'";
        return database.rawQuery(strQuery, null);
    }

    public Cursor getDonGiaVatDu(String MA_VTU) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT DON_GIA, DON_GIA_KH, DVI_TINH FROM " + EsspConstantVariables.TABLE_NAME_VTU + " WHERE MA_VTU = '" + MA_VTU + "'";
        return database.rawQuery(strQuery, null);
    }

    public Cursor getDonGiaVatDuMoi(String MA_VTU) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT DG_TRONGOI, DG_NCONG, DG_VTU, DG_MTCONG, CHUNG_LOAI FROM " + EsspConstantVariables.TABLE_NAME_VTU + " WHERE MA_VTU = '" + MA_VTU + "'";
        return database.rawQuery(strQuery, null);
    }

    public long deleteAllDataVT() {
        database = this.getWritableDatabase();
        return database.delete(EsspConstantVariables.TABLE_NAME_VTU, null, null);
    }

    public boolean checkVT(String MA_VTU) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + EsspConstantVariables.TABLE_NAME_VTU + " WHERE MA_VTU = '" + MA_VTU + "'";
        Cursor c = database.rawQuery(strQuery, null);
        int count = 0;
        if (c.moveToFirst()) {
            count = Integer.parseInt(c.getString(0));
        }
        if (count > 0) {
            return false;
        } else {
            return true;
        }
    }

    public int countVT(String MA_DVIQLY) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + EsspConstantVariables.TABLE_NAME_VTU + " WHERE MA_DVIQLY = '" + MA_DVIQLY + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return Integer.parseInt(c.getString(0));
        }
        return 0;
    }

    public double getDGTronGoi(String MA_VTU) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT DG_TRONGOI FROM " + EsspConstantVariables.TABLE_NAME_VTU + " WHERE MA_VTU = '" + MA_VTU + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getDouble(0);
        }
        return 0d;
    }

    //TODO VinhNB thêm
    public double getDON_GIA_KH(String MA_VTU) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT DON_GIA_KH FROM " + EsspConstantVariables.TABLE_NAME_VTU + " WHERE MA_VTU = '" + MA_VTU + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getDouble(0);
        }
        return 0d;
    }
    //TODO VinhNB thêm
    public double getDG_MTCONG(String MA_VTU) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT DG_MTCONG FROM " + EsspConstantVariables.TABLE_NAME_VTU + " WHERE MA_VTU = '" + MA_VTU + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getDouble(0);
        }
        return 0d;
    }

    public double getDGNCong(String MA_VTU) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT DG_NCONG FROM " + EsspConstantVariables.TABLE_NAME_VTU + " WHERE MA_VTU = '" + MA_VTU + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getDouble(0);
        }
        return 0d;
    }

    public double getDGVTu(String MA_VTU) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT DG_VTU FROM " + EsspConstantVariables.TABLE_NAME_VTU + " WHERE MA_VTU = '" + MA_VTU + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getDouble(0);
        }
        return 0d;
    }

    public Cursor getDG(String MA_VTU) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + EsspConstantVariables.TABLE_NAME_VTU + " WHERE MA_VTU = '" + MA_VTU + "'";
        return database.rawQuery(strQuery, null);
    }

    public int getChungLoai(String MA_VTU) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT CHUNG_LOAI FROM " + EsspConstantVariables.TABLE_NAME_VTU + " WHERE MA_VTU = '" + MA_VTU + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;
    }

    public String getTenVTu(String MA_VTU) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT TEN_VTU FROM " + EsspConstantVariables.TABLE_NAME_VTU + " WHERE MA_VTU = '" + MA_VTU + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getString(0);
        }
        return "";
    }

    public Cursor getAllDataVTu(String MA_DVIQLY) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + EsspConstantVariables.TABLE_NAME_VTU + " WHERE MA_DVIQLY = '" + MA_DVIQLY + "'";
        return database.rawQuery(strQuery, null);
    }
    //endregion

    //region Xử lý bảng trở ngại
    public long insertDataTNgai(String MA_DVIQLY, String MA_TNGAI, String TEN_TNGAI, String TCHAT_KPHUC, String NGUYEN_NHAN,
                                String MA_CVIEC, String NGAY_HLUC) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MA_DVIQLY", MA_DVIQLY);
        values.put("MA_TNGAI", MA_TNGAI);
        values.put("TEN_TNGAI", TEN_TNGAI);
        values.put("TCHAT_KPHUC", TCHAT_KPHUC);
        values.put("NGUYEN_NHAN", NGUYEN_NHAN);
        values.put("MA_CVIEC", MA_CVIEC);
        values.put("NGAY_HLUC", NGAY_HLUC);
        return database.insert(EsspConstantVariables.TABLE_NAME_TNGAI, null, values);
    }

    public int countTroNgai(String MA_DVIQLY) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + EsspConstantVariables.TABLE_NAME_TNGAI + " WHERE MA_DVIQLY = '" + MA_DVIQLY + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return Integer.parseInt(c.getString(0));
        }
        return 0;
    }

    public boolean checkTroNgai(String MA_TNGAI) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + EsspConstantVariables.TABLE_NAME_TNGAI + " WHERE MA_TNGAI = '" + MA_TNGAI + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return (Integer.parseInt(c.getString(0)) > 0);
        }
        return false;
    }

    public long deleteAllDataTN() {
        database = this.getWritableDatabase();
        return database.delete(EsspConstantVariables.TABLE_NAME_TNGAI, null, null);
    }

    public List<String> getDataTNgai() {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + EsspConstantVariables.TABLE_NAME_TNGAI;
        Cursor c = database.rawQuery(strQuery, null);
        List<String> list = new ArrayList<String>();
        list.add("0 - Không có trở ngại");
        if (c.moveToFirst()) {
            do {
                list.add(c.getString(c.getColumnIndex("MA_TNGAI")) + " - " + c.getString(c.getColumnIndex("TEN_TNGAI")));
            } while (c.moveToNext());
        }
        return list;
    }
    //endregion

    //region Xử lý bảng trạm
    public long insertDataTram(String MA_DVIQLY, String MA_TRAM,
                               String TEN_TRAM, String LOAI_TRAM, String CSUAT_TRAM,
                               String MA_CAPDA, String MA_CAPDA_RA, String DINH_DANH) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MA_DVIQLY", MA_DVIQLY);
        values.put("MA_TRAM", MA_TRAM);
        values.put("TEN_TRAM", TEN_TRAM);
        values.put("LOAI_TRAM", LOAI_TRAM);
        values.put("CSUAT_TRAM", CSUAT_TRAM);
        values.put("MA_CAPDA", MA_CAPDA);
        values.put("MA_CAPDA_RA", MA_CAPDA_RA);
        values.put("DINH_DANH", DINH_DANH);
        return database.insert(EsspConstantVariables.TABLE_NAME_TRAM, null, values);
    }

    public int checkTramExist(String MA_TRAM) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + EsspConstantVariables.TABLE_NAME_TRAM + " WHERE MA_TRAM = '" + MA_TRAM + "'";
        Cursor c = database.rawQuery(strQuery, null);
        int count = 0;
        if (c != null) {
            if (c.moveToFirst()) {
                count = Integer.parseInt(c.getString(0));
            }
            c.close();
        }
        return count;
    }

    public int countTram(String MA_DVIQLY) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + EsspConstantVariables.TABLE_NAME_TRAM + " WHERE MA_DVIQLY = '" + MA_DVIQLY + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return Integer.parseInt(c.getString(0));
        }
        return 0;
    }

    public Cursor getAllDataTram(String MA_DVIQLY) {
        database = this.getReadableDatabase();
        String strQuery = "";
        strQuery = "SELECT * FROM " + EsspConstantVariables.TABLE_NAME_TRAM + " WHERE MA_DVIQLY = '" + MA_DVIQLY + "'";
        return database.rawQuery(strQuery, null);
    }

    public String getTenTram(String MA_TRAM) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT TEN_TRAM FROM " + EsspConstantVariables.TABLE_NAME_TRAM + " WHERE MA_TRAM = '" + MA_TRAM + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getString(0);
        }
        return "";
    }

    public boolean checkMaTram(String MA_TRAM) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + EsspConstantVariables.TABLE_NAME_TRAM + " WHERE MA_TRAM = '" + MA_TRAM + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0) > 0;
        }
        return false;
    }
    //endregion

    //region Xử lý bảng ngân hàng
    public long insertDataNganHang(String MA_NHANG, String TEN_NHANG,
                                   String DIA_CHI, String DIEN_THOAI, String SO_TKHOAN,
                                   String MA_NHANG_CHA) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MA_NHANG", MA_NHANG);
        values.put("TEN_NHANG", TEN_NHANG);
        values.put("DIA_CHI", DIA_CHI);
        values.put("DIEN_THOAI", DIEN_THOAI);
        values.put("SO_TKHOAN", SO_TKHOAN);
        values.put("MA_NHANG_CHA", MA_NHANG_CHA);
        return database.insert(EsspConstantVariables.TABLE_NAME_NGAN_HANG, null, values);
    }

    public int countNganHang() {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + EsspConstantVariables.TABLE_NAME_NGAN_HANG;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;
    }

    public boolean checkNganHang(String MA_NHANG) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + EsspConstantVariables.TABLE_NAME_NGAN_HANG + " WHERE MA_NHANG = '" + MA_NHANG + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return (c.getInt(0) > 0);
        }
        return false;
    }

    public long deleteAllDataNH() {
        database = this.getWritableDatabase();
        return database.delete(EsspConstantVariables.TABLE_NAME_NGAN_HANG, null, null);
    }

    public Cursor getAllDataNganHang() {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + EsspConstantVariables.TABLE_NAME_NGAN_HANG;
        return database.rawQuery(strQuery, null);
    }

    public String getTenNHByMaNH(String MA_NHANG) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT TEN_NHANG FROM " + EsspConstantVariables.TABLE_NAME_NGAN_HANG + " WHERE MA_NHANG = '" + MA_NHANG + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getString(0);
        }
        return "";
    }

    public boolean checkMaNganHang(String MA_NHANG) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + EsspConstantVariables.TABLE_NAME_NGAN_HANG + " WHERE MA_NHANG = '" + MA_NHANG + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0) > 0;
        }
        return false;
    }
    //endregion

    //region Xử lý bảng sổ
    public long insertDataSo(String MA_DVIQLY, String TEN_SO, String MA_SO) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MA_DVIQLY", MA_DVIQLY);
        values.put("TEN_SO", TEN_SO);
        values.put("MA_SO", MA_SO);
        return database.insert(EsspConstantVariables.TABLE_NAME_SO_GCS, null, values);
    }

    public boolean checkSO(String MA_SO) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + EsspConstantVariables.TABLE_NAME_SO_GCS + " WHERE MA_SO = '" + MA_SO + "'";
        Cursor c = database.rawQuery(strQuery, null);
        int count = 0;
        if (c.moveToFirst()) {
            count = Integer.parseInt(c.getString(0));
        }
        if (c != null)
            c.close();
        if (count > 0) {
            return false;
        } else {
            return true;
        }
    }

    public long deleteAllDataSO() {
        database = this.getWritableDatabase();
        return database.delete(EsspConstantVariables.TABLE_NAME_SO_GCS, null, null);
    }

    public int countSoGCS(String MA_DVIQLY) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + EsspConstantVariables.TABLE_NAME_SO_GCS + " WHERE MA_DVIQLY = '" + MA_DVIQLY + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return Integer.parseInt(c.getString(0));
        }
        return 0;
    }

    public Cursor getAllDataQuyen(String MA_DVIQLY) {
        database = this.getReadableDatabase();
        String strQuery = "";
        strQuery = "SELECT * FROM " + EsspConstantVariables.TABLE_NAME_SO_GCS + " WHERE MA_DVIQLY = '" + MA_DVIQLY + "'";
        return database.rawQuery(strQuery, null);
    }

    public String getTenQuyen(String MA_SO) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT TEN_SO FROM " + EsspConstantVariables.TABLE_NAME_SO_GCS + " WHERE MA_SO = '" + MA_SO + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getString(0);
        }
        return "";
    }

    //endregion

    //region Xử lý bảng hồ sơ
    public long insertDataKS(int HOSO_ID, String MA_DVIQLY, String TEN_KHANG,
                             String SO_NHA, String DUONG_PHO, String SO_CMT,
                             String NGAY_CAP, String NOI_CAP, String DTHOAI_DD,
                             String DTHOAI_CD, String FAX, String EMAIL,
                             String WEBSITE, String SO_NHA_DDO, String DUONG_PHO_DDO,
                             String TKHOAN_NGANHANG, String MA_NHANG, String MA_SOTHUE,
                             String CHUC_VU, String NGUOI_DAI_DIEN, String Gender_NGUOI_DAI_DIEN, String TEN_NGUOIYCAU, String GenderId,
                             String MA_TRACUU, String CMIS_MA_YCAU_KNAI, String CMIS_MA_LOAI_YCAU, String TINH_TRANG,
                             String MA_LOAIHD, String NGUON_TIEPNHAN, String NGUOI_TIEPNHAN, String NGAY_TIEPNHAN,
                             String NGUOI_SUA, String NGAY_SUA, String NGAY_TAO, String NGAY_HENKHAOSAT, String NGUOI_PHANCONG,
                             String LOAI_DDO, String MA_TRAM, String KIMUA_CSPK, String SO_PHA, String MA_DVIDCHINHC2, String MA_DVIDCHINHC3,
                             String USERNAME, String MA_QUYEN, String HS_DONG_THOI, boolean isTachVatTu,
                             int LOAIHINH_THANHTOAN, boolean isKhaiGia, int LOAI_CAP) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("HOSO_ID", HOSO_ID);
        values.put("MA_DVIQLY", MA_DVIQLY);
        values.put("TEN_KHANG", TEN_KHANG);
        values.put("SO_NHA", SO_NHA);
        values.put("DUONG_PHO", DUONG_PHO);
        values.put("SO_CMT", SO_CMT);
        values.put("NGAY_CAP", NGAY_CAP);
        values.put("NOI_CAP", NOI_CAP);
        values.put("DTHOAI_DD", DTHOAI_DD);
        values.put("DTHOAI_CD", DTHOAI_CD);
        values.put("FAX", FAX);
        values.put("EMAIL", EMAIL);
        values.put("WEBSITE", WEBSITE);
        values.put("SO_NHA_DDO", SO_NHA_DDO);
        values.put("DUONG_PHO_DDO", DUONG_PHO_DDO);
        values.put("TKHOAN_NGANHANG", TKHOAN_NGANHANG);
        values.put("MA_NHANG", MA_NHANG);
        values.put("MA_SOTHUE", MA_SOTHUE);
        values.put("CHUC_VU", CHUC_VU);
        values.put("NGUOI_DAI_DIEN", NGUOI_DAI_DIEN);
        values.put("Gender_NGUOI_DAI_DIEN", Gender_NGUOI_DAI_DIEN);
        values.put("TEN_NGUOIYCAU", TEN_NGUOIYCAU);
        values.put("GenderId", GenderId);
        values.put("MA_TRACUU", MA_TRACUU);
        values.put("CMIS_MA_YCAU_KNAI", CMIS_MA_YCAU_KNAI);
        values.put("CMIS_MA_LOAI_YCAU", CMIS_MA_LOAI_YCAU);
        values.put("TINH_TRANG", TINH_TRANG);
        values.put("MA_LOAIHD", MA_LOAIHD);
        values.put("NGUON_TIEPNHAN", NGUON_TIEPNHAN);
        values.put("NGUOI_TIEPNHAN", NGUOI_TIEPNHAN);
        values.put("NGAY_TIEPNHAN", NGAY_TIEPNHAN);
        values.put("NGUOI_SUA", NGUOI_SUA);
        values.put("NGAY_SUA", NGAY_SUA);
        values.put("NGAY_TAO", NGAY_TAO);
        values.put("NGAY_HENKHAOSAT", NGAY_HENKHAOSAT);
        values.put("NGUOI_PHANCONG", NGUOI_PHANCONG);
        values.put("LOAI_DDO", LOAI_DDO);
        values.put("MA_TRAM", MA_TRAM);
        values.put("KIMUA_CSPK", KIMUA_CSPK);
        values.put("SO_PHA", SO_PHA);
        values.put("MA_DVIDCHINHC2", MA_DVIDCHINHC2);
        values.put("MA_DVIDCHINHC3", MA_DVIDCHINHC3);
        values.put("USERNAME", USERNAME);
        values.put("X", "");
        values.put("Y", "");
        values.put("MA_QUYEN", MA_QUYEN);
        values.put("HS_DONG_THOI", HS_DONG_THOI);
        values.put("isTachVatTu", isTachVatTu);
        values.put("LOAIHINH_THANHTOAN", LOAIHINH_THANHTOAN);
        values.put("isKhaiGia", isKhaiGia);
        values.put("LOAI_CAP", LOAI_CAP);
        return database.insert(EsspConstantVariables.TABLE_NAME_HOSO, null, values);
    }

    public long updateKS(int HOSO_ID, String CMIS_MA_LOAI_YCAU, int MA_LOAIHD,
                         int LOAI_DDO, String MA_TRAM, String TAI_KHOAN, String MA_NHANG,
                         int KIMUA_CSPK, int SO_PHA, String NGUOI_SUA, String NGAY_SUA,
                         String CHUC_VU, String NGUOI_DAI_DIEN, String X, String Y,
                         String MA_QUYEN, String HS_DONG_THOI, int LOAIHINH_THANHTOAN, int LOAI_CAP, int NKY_KSAT) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("CMIS_MA_LOAI_YCAU", CMIS_MA_LOAI_YCAU);
        values.put("MA_LOAIHD", MA_LOAIHD);
        values.put("LOAI_DDO", LOAI_DDO);
        values.put("MA_TRAM", MA_TRAM);
        values.put("TKHOAN_NGANHANG", TAI_KHOAN);
        values.put("MA_NHANG", MA_NHANG);
        values.put("KIMUA_CSPK", KIMUA_CSPK);
        values.put("SO_PHA", SO_PHA);
        values.put("NGUOI_SUA", NGUOI_SUA);
        values.put("NGAY_SUA", NGAY_SUA);
        values.put("CHUC_VU", CHUC_VU);
        values.put("NGUOI_DAI_DIEN", NGUOI_DAI_DIEN);
        values.put("X", X);
        values.put("Y", Y);
        values.put("MA_QUYEN", MA_QUYEN);
        values.put("HS_DONG_THOI", HS_DONG_THOI);
        values.put("LOAIHINH_THANHTOAN", LOAIHINH_THANHTOAN);
        values.put("LOAI_CAP", LOAI_CAP);
        values.put("NKY_KSAT", NKY_KSAT);
        return database.update(EsspConstantVariables.TABLE_NAME_HOSO, values, "HOSO_ID=?", new String[]{"" + HOSO_ID});
    }

    public long updateKS(boolean isTachVatTu) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("isTachVatTu", isTachVatTu);
        return database.update(EsspConstantVariables.TABLE_NAME_HOSO, values, null, null);
    }

    public long updateKG(boolean isKhaiGia) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("isKhaiGia", isKhaiGia);
        return database.update(EsspConstantVariables.TABLE_NAME_HOSO, values, null, null);
    }

    public long updateLoaiCap(int LOAI_CAP) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("LOAI_CAP", LOAI_CAP);
        return database.update(EsspConstantVariables.TABLE_NAME_HOSO, values, null, null);
    }

    public long updateNKy(int NKY_KSAT) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NKY_KSAT", NKY_KSAT);
        return database.update(EsspConstantVariables.TABLE_NAME_HOSO, values, null, null);
    }

    public long updateKS(int LOAIHINH_THANHTOAN) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("LOAIHINH_THANHTOAN", LOAIHINH_THANHTOAN);
        return database.update(EsspConstantVariables.TABLE_NAME_HOSO, values, null, null);
    }

    public long updateTinhTrang(int HOSO_ID, int TINH_TRANG) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TINH_TRANG", TINH_TRANG);
        return database.update(EsspConstantVariables.TABLE_NAME_HOSO, values, "HOSO_ID=?", new String[]{"" + HOSO_ID});
    }

    public long updateKhaiGia(int HOSO_ID, int isKhaiGia) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("isKhaiGia", isKhaiGia==0?false:true);
        return database.update(EsspConstantVariables.TABLE_NAME_HOSO, values, "HOSO_ID=?", new String[]{"" + HOSO_ID});
    }

    public Cursor getTenAndDchi(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT TEN_KHANG, SO_NHA_DDO, DUONG_PHO_DDO, MA_TRAM FROM " +
                EsspConstantVariables.TABLE_NAME_HOSO + " WHERE HOSO_ID = " + HOSO_ID;
        return database.rawQuery(strQuery, null);
    }

    public String getTenKH(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT TEN_KHANG FROM " + EsspConstantVariables.TABLE_NAME_HOSO + " WHERE HOSO_ID = " + HOSO_ID;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getString(0);
        }
        return "";
    }

    public String getDiaChi(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT SO_NHA, DUONG_PHO FROM " + EsspConstantVariables.TABLE_NAME_HOSO + " WHERE HOSO_ID = " + HOSO_ID;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getString(0) + " " + c.getString(1);
        }
        return "";
    }

    public String getMaTram(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT MA_TRAM FROM " + EsspConstantVariables.TABLE_NAME_HOSO + " WHERE HOSO_ID = " + HOSO_ID;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getString(0);
        }
        return "";
    }

    public int getKhaiGia(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT isKhaiGia FROM " + EsspConstantVariables.TABLE_NAME_HOSO + " WHERE HOSO_ID = " + HOSO_ID;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;
    }

    public int getLoaiCap(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT LOAI_CAP FROM " + EsspConstantVariables.TABLE_NAME_HOSO + " WHERE HOSO_ID = " + HOSO_ID;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;
    }

    public long deleteDataKS(int HOSO_ID) {
        database = this.getWritableDatabase();
        return database.delete(EsspConstantVariables.TABLE_NAME_HOSO, "HOSO_ID=?", new String[]{"" + HOSO_ID});
    }

    public boolean checkMaHS(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + EsspConstantVariables.TABLE_NAME_HOSO + " WHERE HOSO_ID = " + HOSO_ID;
        Cursor c = database.rawQuery(strQuery, null);
        int sl = 0;
        if (c.moveToFirst()) {
            sl = c.getInt(0);
        }
        if (sl == 0) {
            return true;
        } else {
            return false;
        }
    }

    public Cursor getDataHoSoByUser(String USERNAME) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + EsspConstantVariables.TABLE_NAME_HOSO + " where USERNAME = '" + USERNAME + "'";
        return database.rawQuery(strQuery, null);
    }

    public EsspEntityHoSo getDataHoSoById(int HOSO_ID, String USERNAME) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + EsspConstantVariables.TABLE_NAME_HOSO + " where HOSO_ID = " + HOSO_ID + " AND USERNAME = '" + USERNAME + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if (c != null) {
            c.moveToFirst();
        }
        EsspEntityHoSo entity = new EsspEntityHoSo();

        entity.setMA_DVIQLY(c.getString(c.getColumnIndex("MA_DVIQLY")) == null ? "" : c.getString(c.getColumnIndex("MA_DVIQLY")));
        entity.setTEN_KHANG(c.getString(c.getColumnIndex("TEN_KHANG")) == null ? "" : c.getString(c.getColumnIndex("TEN_KHANG")));
        entity.setSO_NHA(c.getString(c.getColumnIndex("SO_NHA")) == null ? "" : c.getString(c.getColumnIndex("SO_NHA")));
        entity.setDUONG_PHO(c.getString(c.getColumnIndex("DUONG_PHO")) == null ? "" : c.getString(c.getColumnIndex("DUONG_PHO")));
        entity.setSO_CMT(c.getString(c.getColumnIndex("SO_CMT")) == null ? "" : c.getString(c.getColumnIndex("SO_CMT")));
        entity.setNGAY_CAP(c.getString(c.getColumnIndex("NGAY_CAP")) == null ? "" : c.getString(c.getColumnIndex("NGAY_CAP")));
        entity.setNOI_CAP(c.getString(c.getColumnIndex("NOI_CAP")) == null ? "" : c.getString(c.getColumnIndex("NOI_CAP")));
        entity.setDTHOAI_DD(c.getString(c.getColumnIndex("DTHOAI_DD")) == null ? "" : c.getString(c.getColumnIndex("DTHOAI_DD")));
        entity.setDTHOAI_CD(c.getString(c.getColumnIndex("DTHOAI_CD")) == null ? "" : c.getString(c.getColumnIndex("DTHOAI_CD")));
        entity.setFAX(c.getString(c.getColumnIndex("FAX")) == null ? "" : c.getString(c.getColumnIndex("FAX")));
        entity.setEMAIL(c.getString(c.getColumnIndex("EMAIL")) == null ? "" : c.getString(c.getColumnIndex("EMAIL")));
        entity.setWEBSITE(c.getString(c.getColumnIndex("WEBSITE")) == null ? "" : c.getString(c.getColumnIndex("WEBSITE")));
        entity.setSO_NHA_DDO(c.getString(c.getColumnIndex("SO_NHA_DDO")) == null ? "" : c.getString(c.getColumnIndex("SO_NHA_DDO")));
        entity.setDUONG_PHO_DDO(c.getString(c.getColumnIndex("DUONG_PHO_DDO")) == null ? "" : c.getString(c.getColumnIndex("DUONG_PHO_DDO")));
        entity.setTKHOAN_NGANHANG(c.getString(c.getColumnIndex("TKHOAN_NGANHANG")) == null ? "" : c.getString(c.getColumnIndex("TKHOAN_NGANHANG")));
        entity.setMA_NGANHANG(c.getString(c.getColumnIndex("MA_NHANG")) == null ? "" : c.getString(c.getColumnIndex("MA_NHANG")));
        entity.setMA_SOTHUE(c.getString(c.getColumnIndex("MA_SOTHUE")) == null ? "" : c.getString(c.getColumnIndex("MA_SOTHUE")));
        entity.setCHUC_VU(c.getString(c.getColumnIndex("CHUC_VU")) == null ? "" : c.getString(c.getColumnIndex("CHUC_VU")));
        entity.setNGUOI_DAI_DIEN(c.getString(c.getColumnIndex("NGUOI_DAI_DIEN")) == null ? "" : c.getString(c.getColumnIndex("NGUOI_DAI_DIEN")));
        entity.setGender_NGUOI_DAI_DIEN(c.getString(c.getColumnIndex("Gender_NGUOI_DAI_DIEN")) == null ? 0 : c.getInt(c.getColumnIndex("Gender_NGUOI_DAI_DIEN")));
        entity.setTEN_NGUOIYCAU(c.getString(c.getColumnIndex("TEN_NGUOIYCAU")) == null ? "" : c.getString(c.getColumnIndex("TEN_NGUOIYCAU")));
        entity.setGenderId(c.getString(c.getColumnIndex("GenderId")) == null ? 0 : c.getInt(c.getColumnIndex("GenderId")));
        entity.setMA_TRACUU(c.getString(c.getColumnIndex("MA_TRACUU")) == null ? "" : c.getString(c.getColumnIndex("MA_TRACUU")));
        entity.setCMIS_MA_YCAU_KNAI(c.getString(c.getColumnIndex("CMIS_MA_YCAU_KNAI")) == null ? "" : c.getString(c.getColumnIndex("CMIS_MA_YCAU_KNAI")));
        entity.setCMIS_MA_LOAI_YCAU(c.getString(c.getColumnIndex("CMIS_MA_LOAI_YCAU")) == null ? "" : c.getString(c.getColumnIndex("CMIS_MA_LOAI_YCAU")));
        entity.setTINH_TRANG(c.getString(c.getColumnIndex("TINH_TRANG")) == null ? 4 : c.getInt(c.getColumnIndex("TINH_TRANG")));
        entity.setMA_LOAIHD(c.getString(c.getColumnIndex("MA_LOAIHD")) == null ? 1 : c.getInt(c.getColumnIndex("MA_LOAIHD")));
        entity.setNGUON_TIEPNHAN(c.getString(c.getColumnIndex("NGUON_TIEPNHAN")) == null ? 0 : c.getInt(c.getColumnIndex("NGUON_TIEPNHAN")));
        entity.setNGUOI_TIEPNHAN(c.getString(c.getColumnIndex("NGUOI_TIEPNHAN")) == null ? "" : c.getString(c.getColumnIndex("NGUOI_TIEPNHAN")));
        entity.setNGAY_TIEPNHAN(c.getString(c.getColumnIndex("NGAY_TIEPNHAN")) == null ? "" : c.getString(c.getColumnIndex("NGAY_TIEPNHAN")));
        entity.setNGUOI_SUA(c.getString(c.getColumnIndex("NGUOI_SUA")) == null ? "" : c.getString(c.getColumnIndex("NGUOI_SUA")));
        entity.setNGAY_SUA(c.getString(c.getColumnIndex("NGAY_SUA")) == null ? "" : c.getString(c.getColumnIndex("NGAY_SUA")));
        entity.setNGAY_TAO(c.getString(c.getColumnIndex("NGAY_TAO")) == null ? "" : c.getString(c.getColumnIndex("NGAY_TAO")));
        entity.setNGAY_HENKHAOSAT(c.getString(c.getColumnIndex("NGAY_HENKHAOSAT")) == null ? "" : c.getString(c.getColumnIndex("NGAY_HENKHAOSAT")));
        entity.setNGUOI_PHANCONG(c.getString(c.getColumnIndex("NGUOI_PHANCONG")) == null ? "" : c.getString(c.getColumnIndex("NGUOI_PHANCONG")));
        entity.setLOAI_DDO(c.getString(c.getColumnIndex("LOAI_DDO")) == null ? 1 : c.getInt(c.getColumnIndex("LOAI_DDO")));
        entity.setMA_TRAM(c.getString(c.getColumnIndex("MA_TRAM")) == null ? "" : c.getString(c.getColumnIndex("MA_TRAM")));
        entity.setKIMUA_CSPK(c.getString(c.getColumnIndex("KIMUA_CSPK")) == null ? 0 : c.getInt(c.getColumnIndex("KIMUA_CSPK")));
        entity.setSO_PHA(c.getString(c.getColumnIndex("SO_PHA")) == null ? 1 : c.getInt(c.getColumnIndex("SO_PHA")));
        entity.setMA_DVIDCHINHC2(c.getString(c.getColumnIndex("MA_DVIDCHINHC2")) == null ? "" : c.getString(c.getColumnIndex("MA_DVIDCHINHC2")));
        entity.setMA_DVIDCHINHC3(c.getString(c.getColumnIndex("MA_DVIDCHINHC3")) == null ? "" : c.getString(c.getColumnIndex("MA_DVIDCHINHC3")));
        entity.setUSERNAME(c.getString(c.getColumnIndex("USERNAME")) == null ? "" : c.getString(c.getColumnIndex("USERNAME")));
        entity.setMA_QUYEN(c.getString(c.getColumnIndex("MA_QUYEN")) == null ? "" : c.getString(c.getColumnIndex("MA_QUYEN")));
        entity.setHS_DONG_THOI(c.getString(c.getColumnIndex("HS_DONG_THOI")) == null ? "" : c.getString(c.getColumnIndex("HS_DONG_THOI")));
        entity.setLOAIHINH_THANHTOAN(c.getString(c.getColumnIndex("LOAIHINH_THANHTOAN")) == null ? 0 : c.getInt(c.getColumnIndex("LOAIHINH_THANHTOAN")));
        entity.setTachVatTu(c.getString(c.getColumnIndex("isTachVatTu")) == null ? true : Boolean.parseBoolean(c.getString(c.getColumnIndex("isTachVatTu"))));
        entity.setLOAI_CAP(c.getString(c.getColumnIndex("LOAI_CAP")) == null ? 0 : c.getInt(c.getColumnIndex("LOAI_CAP")));
        entity.setNKY_KSAT(c.getString(c.getColumnIndex("NKY_KSAT")) == null ? -1 : c.getInt(c.getColumnIndex("NKY_KSAT")));
        try {
            entity.setHOSO_ID(Integer.parseInt(c.getString(c.getColumnIndex("HOSO_ID"))));
        } catch (Exception ex) {
        }
        return entity;
    }

    public long deleteHoSo(String HOSO_ID) {
        database = this.getWritableDatabase();
        return database.delete(EsspConstantVariables.TABLE_NAME_HOSO, "HOSO_ID=?", new String[]{HOSO_ID});
    }

    public int getTinhTrangID(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT TINH_TRANG FROM " + EsspConstantVariables.TABLE_NAME_HOSO + " WHERE HOSO_ID = " + HOSO_ID;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 5;
    }

    public int getLoaiHinhTToan(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT LOAIHINH_THANHTOAN FROM " + EsspConstantVariables.TABLE_NAME_HOSO + " WHERE HOSO_ID = " + HOSO_ID;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;
    }

    public int getLoaiHDID(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT MA_LOAIHD FROM " + EsspConstantVariables.TABLE_NAME_HOSO + " WHERE HOSO_ID = " + HOSO_ID;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;
    }

    public int countHSByHoSoID2(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + EsspConstantVariables.TABLE_NAME_HOSO
                + " WHERE HOSO_ID = " + HOSO_ID + " AND TINH_TRANG = " + TINH_TRANG;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;
    }
    //endregion

    //region Xử lý bảng kết quả khảo sát
    public long insertDataKQ_KSAT(int HOSO_ID, String MA_TNGAI, int KQ_KHSAT_ID) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("HOSO_ID", HOSO_ID);
        values.put("MA_TNGAI", MA_TNGAI);
        values.put("KQ_KHSAT_ID", KQ_KHSAT_ID);
        return database.insert(EsspConstantVariables.TABLE_NAME_KQ_KSAT, null, values);
    }

    public long updateKQ_KS(int HOSO_ID, String MA_TNGAI) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MA_TNGAI", MA_TNGAI);
        return database.update(EsspConstantVariables.TABLE_NAME_KQ_KSAT, values, "HOSO_ID=?", new String[]{"" + HOSO_ID});
    }

    public long updateKQ_KS(int HOSO_ID, String MA_TNGAI, String GHI_CHU) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MA_TNGAI", MA_TNGAI);
        values.put("GHI_CHU", GHI_CHU);
        return database.update(EsspConstantVariables.TABLE_NAME_KQ_KSAT, values, "HOSO_ID=?", new String[]{"" + HOSO_ID});
    }

    public Cursor getKQKS(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + EsspConstantVariables.TABLE_NAME_KQ_KSAT + " WHERE HOSO_ID = " + HOSO_ID;
        return database.rawQuery(strQuery, null);
    }

    public int getMaTNGAI(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT MA_TNGAI FROM " + EsspConstantVariables.TABLE_NAME_KQ_KSAT + " WHERE HOSO_ID = " + HOSO_ID;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;
    }

    public long deleteKQKsat(String HOSO_ID) {
        database = this.getWritableDatabase();
        return database.delete(EsspConstantVariables.TABLE_NAME_KQ_KSAT, "HOSO_ID=?", new String[]{HOSO_ID});
    }
    //endregion

    //region Xử lý bảng phân công
    public long insertDataPC(int HOSO_ID, String USER) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("HOSO_ID", HOSO_ID);
        values.put("USER", USER);
        return database.insert(EsspConstantVariables.TABLE_NAME_PHANCONG, null, values);
    }

    public long deletePhanCong(String HOSO_ID) {
        database = this.getWritableDatabase();
        return database.delete(EsspConstantVariables.TABLE_NAME_PHANCONG, "HOSO_ID=?", new String[]{HOSO_ID});
    }
    //endregion

    //region Xử lý bảng dự toán
    public Cursor getDuToanByIdHS(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + EsspConstantVariables.TABLE_NAME_DTOAN + " WHERE HOSO_ID = " + HOSO_ID;
        return database.rawQuery(strQuery, null);
    }

    public Cursor getVTDuToanByIdHS(int HOSO_ID, int CHUNG_LOAI, int SO_HUU) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT TEN_VTU FROM " + EsspConstantVariables.TABLE_NAME_DTOAN + " DT, " + EsspConstantVariables.TABLE_NAME_VTU
                + " VT WHERE DT.MA_VTU = VT.MA_VTU AND CHUNG_LOAI = 1 AND SO_HUU = " + SO_HUU + " AND HOSO_ID = " + HOSO_ID;
        return database.rawQuery(strQuery, null);
    }

    public Cursor getAllDataDTByMaHSvsSH(int HOSO_ID, String SO_HUU) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT dt.ID, vt.MA_VTU, SO_LUONG, TEN_VTU, dt.MA_LOAI_CPHI, dt.DON_GIA, " +
                "DVI_TINH, SO_HUU, STT, THANH_TIEN, TT_DON_GIA, DON_GIA_KH, LOAI, CHUNG_LOAI, TT_TU_TUC, TT_THU_HOI, " +
                "dt.HSDC_K1NC, dt.HSDC_K2NC, dt.HSDC_MTC FROM " + EsspConstantVariables.TABLE_NAME_DTOAN + " dt, " +
                EsspConstantVariables.TABLE_NAME_VTU + " vt " + "WHERE dt.MA_VTU = vt.MA_VTU AND dt.HOSO_ID = '" + HOSO_ID + "' AND SO_HUU = '" +
                SO_HUU + "'";
        return database.rawQuery(strQuery, null);
    }

    public Cursor getDayCap(int HOSO_ID, String SO_HUU) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT dt.ID, vt.MA_VTU, SO_LUONG, TEN_VTU, dt.MA_LOAI_CPHI, dt.DON_GIA, " +
                "DVI_TINH, SO_HUU, STT, THANH_TIEN, TT_DON_GIA, DON_GIA_KH, LOAI, CHUNG_LOAI, TT_TU_TUC, TT_THU_HOI FROM " + EsspConstantVariables.TABLE_NAME_DTOAN + " dt, " +
                EsspConstantVariables.TABLE_NAME_VTU + " vt " + "WHERE dt.MA_VTU = vt.MA_VTU AND dt.HOSO_ID = " + HOSO_ID + " AND SO_HUU = '" +
                SO_HUU + "' AND CHUNG_LOAI = 1";
        return database.rawQuery(strQuery, null);
    }

    public Cursor getAllDataDTByMaHSvsSH(int HOSO_ID, String SO_HUU, String MA_VTU) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT dt.ID, vt.MA_VTU, SO_LUONG, TEN_VTU, dt.MA_LOAI_CPHI, dt.DON_GIA, " +
                "DVI_TINH, SO_HUU, STT, THANH_TIEN, TT_DON_GIA, DON_GIA_KH, LOAI, CHUNG_LOAI FROM " + EsspConstantVariables.TABLE_NAME_DTOAN + " dt, " +
                EsspConstantVariables.TABLE_NAME_VTU + " vt " + "WHERE dt.MA_VTU = vt.MA_VTU AND dt.HOSO_ID = " + HOSO_ID + " AND SO_HUU = '" +
                SO_HUU + "' AND vt.MA_VTU = '" + MA_VTU + "'";
        return database.rawQuery(strQuery, null);
    }

    public Cursor getAllDataDTByMaHSvsSH2(int HOSO_ID, String SO_HUU, String MA_VTU) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT dt.ID, vt.MA_VTU, SO_LUONG, TEN_VTU, dt.MA_LOAI_CPHI, dt.DON_GIA, " +
                "vt.DVI_TINH, SO_HUU, STT, THANH_TIEN, TT_DON_GIA, DON_GIA_KH, LOAI, CHUNG_LOAI, dt.HSDC_K1NC, dt.HSDC_K2NC, " +
                "dt.HSDC_MTC, vt.DG_TRONGOI, vt.DG_NCONG, vt.DG_VTU, vt.DG_MTCONG FROM " + EsspConstantVariables.TABLE_NAME_DTOAN + " dt, " +
                EsspConstantVariables.TABLE_NAME_VTU + " vt " + "WHERE dt.MA_VTU = vt.MA_VTU AND dt.HOSO_ID = " + HOSO_ID + " AND SO_HUU = '" +
                SO_HUU + "' AND vt.MA_VTU = '" + MA_VTU + "'";
        return database.rawQuery(strQuery, null);
    }

    public String getSoLuong(int HOSO_ID, String SO_HUU, String MA_VTU) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT SO_LUONG FROM " + EsspConstantVariables.TABLE_NAME_DTOAN + " WHERE HOSO_ID = " + HOSO_ID + " AND SO_HUU = '" +
                SO_HUU + "' AND MA_VTU = '" + MA_VTU + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getString(0);
        }
        return "1";
    }

    public String getSoLuong(int ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT SO_LUONG FROM " + EsspConstantVariables.TABLE_NAME_DTOAN + " WHERE ID = " + ID;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getString(0);
        }
        return "1";
    }

    public String getDonGia(int HOSO_ID, String SO_HUU, String MA_VTU) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT DON_GIA FROM " + EsspConstantVariables.TABLE_NAME_DTOAN + " WHERE HOSO_ID = " + HOSO_ID + " AND SO_HUU = '" +
                SO_HUU + "' AND MA_VTU = '" + MA_VTU + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getString(0);
        }
        return "0";
    }

    public double sumThanhTien(int HOSO_ID, String SO_HUU) {
        database = this.getReadableDatabase();
        //TODO VinhNB (Revert code) lý do sửa: Cột THANH_TIEN chưa được update giá trị default = 0, có bao gồm đã cộng thuế VAT 10%
        String strQuery = "SELECT SUM(THANH_TIEN) FROM " + EsspConstantVariables.TABLE_NAME_DTOAN + " WHERE HOSO_ID = " + HOSO_ID + " AND SO_HUU = '" +
                SO_HUU + "'";
//        String strQuery = "SELECT (SUM(dt.SO_LUONG*(vt.DG_VTU + vt.DG_NCONG + vt.DG_MTCONG))*1.1) FROM KS_DTOAN dt, D_VTU vt WHERE dt.HOSO_ID = " +HOSO_ID+" and dt.SO_HUU = '"+SO_HUU+"'and dt.MA_VTU = vt.MA_VTU";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getDouble(0);
        }
        return 0d;
    }

    public double sumVatLieu(int LOAIHINH_TTOAN, int HOSO_ID, String SO_HUU) {
        database = this.getReadableDatabase();
        String strQuery = "";
        if(LOAIHINH_TTOAN == 0) {
            //TODO VinhNB sua
            /*strQuery = "SELECT SUM(vt.DG_VTU*dt.HSDC_K1NC*dt.HSDC_K2NC*SO_LUONG) FROM " + EsspConstantVariables.TABLE_NAME_DTOAN + " dt," +
                    EsspConstantVariables.TABLE_NAME_VTU + " vt" + " WHERE HOSO_ID = " + HOSO_ID + " AND SO_HUU = '" +
                    SO_HUU + "' AND dt.MA_VTU = vt.MA_VTU";*/
            strQuery = "SELECT SUM(vt.DG_VTU*SO_LUONG) FROM " + EsspConstantVariables.TABLE_NAME_DTOAN + " dt," +
                    EsspConstantVariables.TABLE_NAME_VTU + " vt" + " WHERE HOSO_ID = " + HOSO_ID + " AND SO_HUU = '" +
                    SO_HUU + "' AND dt.MA_VTU = vt.MA_VTU";
        } else if(LOAIHINH_TTOAN == 1) {
            //TODO VinhNB sua
           /* strQuery = "SELECT SUM(vt.DG_VTU*dt.HSDC_K1NC*dt.HSDC_K2NC*SO_LUONG) FROM " + EsspConstantVariables.TABLE_NAME_DTOAN + " dt," +
                    EsspConstantVariables.TABLE_NAME_VTU + " vt" + " WHERE HOSO_ID = " + HOSO_ID + " AND SO_HUU = '" +
                    SO_HUU + "' AND dt.MA_VTU = vt.MA_VTU AND CHUNG_LOAI <> 1";*/
            strQuery = "SELECT SUM(vt.DG_VTU*SO_LUONG) FROM " + EsspConstantVariables.TABLE_NAME_DTOAN + " dt," +
                    EsspConstantVariables.TABLE_NAME_VTU + " vt" + " WHERE HOSO_ID = " + HOSO_ID + " AND SO_HUU = '" +
                    SO_HUU + "' AND dt.MA_VTU = vt.MA_VTU AND CHUNG_LOAI <> 1";
        } else {
            //TODO VinhNB sua
//            strQuery = "SELECT SUM(vt.DG_VTU*dt.HSDC_K1NC*dt.HSDC_K2NC*SO_LUONG) FROM " + EsspConstantVariables.TABLE_NAME_DTOAN + " dt," +
//                    EsspConstantVariables.TABLE_NAME_VTU + " vt" + " WHERE HOSO_ID = " + HOSO_ID + " AND SO_HUU = '" +
//                    SO_HUU + "' AND dt.MA_VTU = vt.MA_VTU AND CHUNG_LOAI = 5";
            strQuery = "SELECT SUM(vt.DG_VTU*SO_LUONG) FROM " + EsspConstantVariables.TABLE_NAME_DTOAN + " dt," +
                    EsspConstantVariables.TABLE_NAME_VTU + " vt" + " WHERE HOSO_ID = " + HOSO_ID + " AND SO_HUU = '" +
                    SO_HUU + "' AND dt.MA_VTU = vt.MA_VTU AND CHUNG_LOAI = 5";
        }
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getDouble(0);
        }
        return 0d;
    }

    public double sumNhanCong(int LOAIHINH_TTOAN, int HOSO_ID, String SO_HUU) {
        database = this.getReadableDatabase();
        String strQuery = "";
        if(LOAIHINH_TTOAN < 3) {
            strQuery = "SELECT SUM(vt.DG_NCONG*dt.HSDC_K1NC*dt.HSDC_K2NC*SO_LUONG) FROM " + EsspConstantVariables.TABLE_NAME_DTOAN + " dt," +
                    EsspConstantVariables.TABLE_NAME_VTU + " vt" + " WHERE HOSO_ID = " + HOSO_ID + " AND SO_HUU = '" +
                    SO_HUU + "' AND dt.MA_VTU = vt.MA_VTU";
        } else {
            strQuery = "SELECT SUM(vt.DG_NCONG*dt.HSDC_K1NC*dt.HSDC_K2NC*SO_LUONG) FROM " + EsspConstantVariables.TABLE_NAME_DTOAN + " dt," +
                    EsspConstantVariables.TABLE_NAME_VTU + " vt" + " WHERE HOSO_ID = " + HOSO_ID + " AND SO_HUU = '" +
                    SO_HUU + "' AND dt.MA_VTU = vt.MA_VTU AND CHUNG_LOAI = 5";
        }
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getDouble(0);
        }
        return 0d;
    }

    public double sumMTC(int LOAIHINH_TTOAN, int HOSO_ID, String SO_HUU) {
        database = this.getReadableDatabase();
        String strQuery = "";
        if(LOAIHINH_TTOAN < 3) {
            strQuery = "SELECT SUM(vt.DG_MTCONG*dt.HSDC_MTC*SO_LUONG) FROM " + EsspConstantVariables.TABLE_NAME_DTOAN + " dt," +
                    EsspConstantVariables.TABLE_NAME_VTU + " vt" + " WHERE HOSO_ID = " + HOSO_ID + " AND SO_HUU = '" +
                    SO_HUU + "' AND dt.MA_VTU = vt.MA_VTU";
        } else {
            strQuery = "SELECT SUM(vt.DG_MTCONG*dt.HSDC_MTC*SO_LUONG) FROM " + EsspConstantVariables.TABLE_NAME_DTOAN + " dt," +
                    EsspConstantVariables.TABLE_NAME_VTU + " vt" + " WHERE HOSO_ID = " + HOSO_ID + " AND SO_HUU = '" +
                    SO_HUU + "' AND dt.MA_VTU = vt.MA_VTU AND CHUNG_LOAI = 5";
        }
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getDouble(0);
        }
        return 0d;
    }

    public String getDonGiaKH(int HOSO_ID, String SO_HUU, String MA_VTU) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT DON_GIA_KH FROM " + EsspConstantVariables.TABLE_NAME_DTOAN + " WHERE HOSO_ID = " + HOSO_ID + " AND SO_HUU = '" +
                SO_HUU + "' AND MA_VTU = '" + MA_VTU + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getString(0);
        }
        return "0";
    }

    public String getThanhTien(int HOSO_ID, String SO_HUU, String MA_VTU) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT THANH_TIEN FROM " + EsspConstantVariables.TABLE_NAME_DTOAN + " WHERE HOSO_ID = " + HOSO_ID + " AND SO_HUU = '" +
                SO_HUU + "' AND MA_VTU = '" + MA_VTU + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getString(0);
        }
        return "0";
    }

    public long updateSoluong(int HOSO_ID, String SO_HUU, String MA_VTU, String SO_LUONG, String THANH_TIEN) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("SO_LUONG", SO_LUONG);
        values.put("THANH_TIEN", THANH_TIEN);
        return database.update(EsspConstantVariables.TABLE_NAME_DTOAN, values, "HOSO_ID=? and SO_HUU=? and MA_VTU=?", new String[]{"" + HOSO_ID, SO_HUU, MA_VTU});
    }

    public long updateSoluong(int ID, String SO_LUONG, String THANH_TIEN) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("SO_LUONG", SO_LUONG);
        values.put("THANH_TIEN", THANH_TIEN);
        return database.update(EsspConstantVariables.TABLE_NAME_DTOAN, values, "ID=?", new String[]{"" + ID});
    }

    public long updateSL(String MA_VTU, String SO_LUONG, String THANH_TIEN) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("SO_LUONG", SO_LUONG);
        values.put("THANH_TIEN", THANH_TIEN);
        return database.update(EsspConstantVariables.TABLE_NAME_DTOAN, values, "MA_VTU=?", new String[]{MA_VTU});
    }

    public long updateDonGia(int HOSO_ID, String SO_HUU, String MA_VTU, String DON_GIA, String THANH_TIEN, int TT_TU_TUC) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("DON_GIA", DON_GIA);
        values.put("THANH_TIEN", THANH_TIEN);
        values.put("TT_TU_TUC", TT_TU_TUC);
        return database.update(EsspConstantVariables.TABLE_NAME_DTOAN, values, "HOSO_ID=? and SO_HUU=? and MA_VTU=?", new String[]{"" + HOSO_ID, SO_HUU, MA_VTU});
    }

    public long updateDonGiaThanhTien(int HOSO_ID, String SO_HUU, String MA_VTU, String DON_GIA, String THANH_TIEN) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
            values.put("DON_GIA", DON_GIA);
        values.put("THANH_TIEN", THANH_TIEN);
        return database.update(EsspConstantVariables.TABLE_NAME_DTOAN, values, "HOSO_ID=? and SO_HUU=? and MA_VTU=?",
                new String[]{String.valueOf(HOSO_ID), SO_HUU, MA_VTU});
    }

    public long updateThuHoi(int HOSO_ID, String SO_HUU, String MA_VTU, int TT_THU_HOI) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TT_THU_HOI", TT_THU_HOI);
        return database.update(EsspConstantVariables.TABLE_NAME_DTOAN, values, "HOSO_ID=? and SO_HUU=? and MA_VTU=?", new String[]{"" + HOSO_ID, SO_HUU, MA_VTU});
    }

    public long updateTinhTrangDT(int HOSO_ID, int TINH_TRANG) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TINH_TRANG", TINH_TRANG);
        return database.update(EsspConstantVariables.TABLE_NAME_DTOAN, values, "HOSO_ID=?", new String[]{"" + HOSO_ID});
    }

    public int getTTDT(int HOSO_ID) {
        int tt = 0;
        database = this.getReadableDatabase();
        String strQuery = "SELECT TINH_TRANG FROM " + EsspConstantVariables.TABLE_NAME_DTOAN + " WHERE HOSO_ID = " + HOSO_ID;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            tt = Integer.parseInt(c.getString(0));
        }
        return tt;
    }

    public long insertDataDuToan(int HOSO_ID, String MA_DVIQLY,
                                 String MA_VTU, String MA_LOAI_CPHI, String SO_LUONG,
                                 String DON_GIA, String SO_HUU, String STT, String THANH_TIEN,
                                 String TINH_TRANG, String TT_DON_GIA, int TT_TU_TUC, int TT_THU_HOI) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("HOSO_ID", HOSO_ID);
        values.put("MA_DVIQLY", MA_DVIQLY);
        values.put("MA_VTU", MA_VTU);
        values.put("MA_LOAI_CPHI", MA_LOAI_CPHI);
        values.put("SO_LUONG", SO_LUONG);
        values.put("DON_GIA", DON_GIA);
        values.put("SO_HUU", SO_HUU);
        values.put("STT", STT);
        values.put("THANH_TIEN", THANH_TIEN);
        values.put("TINH_TRANG", TINH_TRANG);
        values.put("TT_DON_GIA", TT_DON_GIA);
        values.put("TT_TU_TUC", TT_TU_TUC);
        values.put("TT_THU_HOI", TT_THU_HOI);
        return database.insert(EsspConstantVariables.TABLE_NAME_DTOAN, null, values);
    }

    public long updateDataDuToan(int HOSO_ID, String MA_DVIQLY,
                                 String MA_VTU, String MA_LOAI_CPHI, String SO_LUONG,
                                 String DON_GIA, String SO_HUU, String STT, String THANH_TIEN,
                                 String TINH_TRANG, String TT_DON_GIA, int TT_TU_TUC, int TT_THU_HOI,
                                 String HSDC_K1NC, String HSDC_K2NC, String HSDC_MTC) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MA_DVIQLY", MA_DVIQLY);
        values.put("MA_LOAI_CPHI", MA_LOAI_CPHI);
        values.put("SO_LUONG", SO_LUONG);
        values.put("DON_GIA", DON_GIA);
        values.put("STT", STT);
        values.put("THANH_TIEN", THANH_TIEN);
        values.put("TINH_TRANG", TINH_TRANG);
        values.put("TT_DON_GIA", TT_DON_GIA);
        values.put("TT_TU_TUC", TT_TU_TUC);
        values.put("TT_THU_HOI", TT_THU_HOI);
        values.put("HSDC_K1NC", HSDC_K1NC);
        values.put("HSDC_K2NC", HSDC_K2NC);
        values.put("HSDC_MTC", HSDC_MTC);
        return database.update(EsspConstantVariables.TABLE_NAME_DTOAN, values, "HOSO_ID=? and SO_HUU=? and MA_VTU=?", new String[]{"" + HOSO_ID, SO_HUU, MA_VTU});
    }

    public long insertDataDuToan(int HOSO_ID, String MA_DVIQLY,
                                 String MA_VTU, String MA_LOAI_CPHI, String SO_LUONG,
                                 String DON_GIA, String SO_HUU, String STT, String THANH_TIEN,
                                 String TINH_TRANG, String TT_DON_GIA, int TT_TU_TUC, int TT_THU_HOI,
                                 String HSDC_K1NC, String HSDC_K2NC, String HSDC_MTC) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("HOSO_ID", HOSO_ID);
        values.put("MA_DVIQLY", MA_DVIQLY);
        values.put("MA_VTU", MA_VTU);
        values.put("MA_LOAI_CPHI", MA_LOAI_CPHI);
        values.put("SO_LUONG", SO_LUONG);
        values.put("DON_GIA", DON_GIA);
        values.put("SO_HUU", SO_HUU);
        values.put("STT", STT);
        values.put("THANH_TIEN", THANH_TIEN);
        values.put("TINH_TRANG", TINH_TRANG);
        values.put("TT_DON_GIA", TT_DON_GIA);
        values.put("TT_TU_TUC", TT_TU_TUC);
        values.put("TT_THU_HOI", TT_THU_HOI);
        values.put("HSDC_K1NC", HSDC_K1NC);
        values.put("HSDC_K2NC", HSDC_K2NC);
        values.put("HSDC_MTC", HSDC_MTC);
        return database.insert(EsspConstantVariables.TABLE_NAME_DTOAN, null, values);
    }

    public boolean checkDuToanExist(int HOSO_ID, String MA_VTU, String SO_HUU) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + EsspConstantVariables.TABLE_NAME_DTOAN + " WHERE HOSO_ID = " + HOSO_ID + " AND MA_VTU = '" + MA_VTU + "' AND SO_HUU = '" + SO_HUU + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            if (c.getInt(0) > 0)
                return true;
        }
        return false;
    }

    public int getTinhTrangDTByHoSoID(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT DISTINCT(TINH_TRANG) FROM " + EsspConstantVariables.TABLE_NAME_DTOAN + " WHERE HOSO_ID = " + HOSO_ID;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;
    }

    public int countTinhTrangDTByHoSoID(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + EsspConstantVariables.TABLE_NAME_DTOAN + " WHERE HOSO_ID = " + HOSO_ID + " AND TINH_TRANG = 0";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 1;
    }

    public int countDTByHoSoID(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + EsspConstantVariables.TABLE_NAME_DTOAN + " WHERE HOSO_ID = " + HOSO_ID;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;
    }

    public int countDTByHoSoID2(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + EsspConstantVariables.TABLE_NAME_DTOAN + " WHERE HOSO_ID = " + HOSO_ID + " AND TINH_TRANG = 0";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;
    }

    // return true: đã tồn tại, false: Chưa tồn tại
    public boolean checkDuToanExist(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + EsspConstantVariables.TABLE_NAME_DTOAN + " WHERE HOSO_ID = " + HOSO_ID;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            if (c.getInt(0) > 0)
                return true;
        }
        return false;
    }

    public long deleteDuToan(int HOSO_ID, String MA_VTU, String SO_HUU) {
        database = this.getWritableDatabase();
        return database.delete(EsspConstantVariables.TABLE_NAME_DTOAN, "HOSO_ID=? and MA_VTU=? and SO_HUU=?",
                new String[]{"" + HOSO_ID, MA_VTU, SO_HUU});
    }

    public long deleteDuToan(String HOSO_ID) {
        database = this.getWritableDatabase();
        return database.delete(EsspConstantVariables.TABLE_NAME_DTOAN, "HOSO_ID=?", new String[]{HOSO_ID});
    }

    public long updateHSDC1NC(int HSDC_K1NC) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("HSDC_K1NC", HSDC_K1NC);
        return database.update(EsspConstantVariables.TABLE_NAME_DTOAN, values, null, null);
    }

    public long updateHSDC2NC(int HSDC_K2NC) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("HSDC_K2NC", HSDC_K2NC);
        return database.update(EsspConstantVariables.TABLE_NAME_DTOAN, values, null, null);
    }

    public long updateHSDCMTC(int HSDC_MTC) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("HSDC_MTC", HSDC_MTC);
        return database.update(EsspConstantVariables.TABLE_NAME_DTOAN, values, null, null);
    }

    //endregion

    //region Xử lý bảng vật tư ngoài ht
    public long insertDataVTNgoaiHT(int HOSO_ID, String TENVTU_MTB, String SO_LUONG, String DVI_TINH, String DON_GIA_KH,
                                    String GHI_CHU, int TINH_TRANG, int CHUNG_LOAI) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("HOSO_ID", HOSO_ID);
        values.put("TENVTU_MTB", TENVTU_MTB);
        values.put("SO_LUONG", SO_LUONG);
        values.put("DVI_TINH", DVI_TINH);
        values.put("DON_GIA_KH", DON_GIA_KH);
        values.put("GHI_CHU", GHI_CHU);
        values.put("TINH_TRANG", TINH_TRANG);
        values.put("CHUNG_LOAI", CHUNG_LOAI);
        return database.insert(EsspConstantVariables.TABLE_NAME_DTOAN_VTU_NGOAIHT, null, values);
    }

    public long insertDataVTNgoaiHT(int HOSO_ID, String TENVTU_MTB, String SO_LUONG, String DVI_TINH, String DON_GIA_KH,
                                    String GHI_CHU, int TINH_TRANG, int CHUNG_LOAI, String DG_TRONGOI, String DG_NCONG,
                                    String DG_VTU, String DG_MTCONG,String HSDC_K1NC, String HSDC_K2NC, String HSDC_MTC) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("HOSO_ID", HOSO_ID);
        values.put("TENVTU_MTB", TENVTU_MTB);
        values.put("SO_LUONG", SO_LUONG);
        values.put("DVI_TINH", DVI_TINH);
        values.put("DON_GIA_KH", DON_GIA_KH);
        values.put("GHI_CHU", GHI_CHU);
        values.put("TINH_TRANG", TINH_TRANG);
        values.put("CHUNG_LOAI", CHUNG_LOAI);
        values.put("DG_TRONGOI", DG_TRONGOI);
        values.put("DG_NCONG", DG_NCONG);
        values.put("DG_VTU", DG_VTU);
        values.put("DG_MTCONG", DG_MTCONG);
        values.put("HSDC_K1NC", HSDC_K1NC);
        values.put("HSDC_K2NC", HSDC_K2NC);
        values.put("HSDC_MTC", HSDC_MTC);
        return database.insert(EsspConstantVariables.TABLE_NAME_DTOAN_VTU_NGOAIHT, null, values);
    }

    public long updateDataVTNgoaiHT(int VTKHTT_ID, String TENVTU_MTB, String SO_LUONG, String DVI_TINH, String DON_GIA_KH,
                                    String GHI_CHU, int TINH_TRANG, int CHUNG_LOAI, String DG_TRONGOI, String DG_NCONG,
                                    String DG_VTU, String DG_MTCONG,String HSDC_K1NC, String HSDC_K2NC, String HSDC_MTC) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TENVTU_MTB", TENVTU_MTB);
        values.put("SO_LUONG", SO_LUONG);
        values.put("DVI_TINH", DVI_TINH);
        values.put("DON_GIA_KH", DON_GIA_KH);
        values.put("GHI_CHU", GHI_CHU);
        values.put("TINH_TRANG", TINH_TRANG);
        values.put("CHUNG_LOAI", CHUNG_LOAI);
        values.put("DG_TRONGOI", DG_TRONGOI);
        values.put("DG_NCONG", DG_NCONG);
        values.put("DG_VTU", DG_VTU);
        values.put("DG_MTCONG", DG_MTCONG);
        values.put("HSDC_K1NC", HSDC_K1NC);
        values.put("HSDC_K2NC", HSDC_K2NC);
        values.put("HSDC_MTC", HSDC_MTC);
        return database.update(EsspConstantVariables.TABLE_NAME_DTOAN_VTU_NGOAIHT, values, "VTKHTT_ID=?", new String[]{String.valueOf(VTKHTT_ID)});
    }

    public int countTinhTrangDTNHTByHoSoID(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + EsspConstantVariables.TABLE_NAME_DTOAN_VTU_NGOAIHT + " WHERE HOSO_ID = " + HOSO_ID + " AND TINH_TRANG = 0";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 1;
    }

    public Cursor getDuToanNHTByIdHS(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + EsspConstantVariables.TABLE_NAME_DTOAN_VTU_NGOAIHT + " WHERE HOSO_ID = " + HOSO_ID;
        return database.rawQuery(strQuery, null);
    }

    public Cursor getDuToanNHTById(int VTKHTT_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + EsspConstantVariables.TABLE_NAME_DTOAN_VTU_NGOAIHT + " WHERE VTKHTT_ID = " + VTKHTT_ID;
        return database.rawQuery(strQuery, null);
    }

    public int getIDVTNHT() {
        database = this.getReadableDatabase();
        String strQuery = "SELECT MAX(VTKHTT_ID) FROM " + EsspConstantVariables.TABLE_NAME_DTOAN_VTU_NGOAIHT;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;
    }

    public String getSoLuongNHT(int VTKHTT_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT SO_LUONG FROM " + EsspConstantVariables.TABLE_NAME_DTOAN_VTU_NGOAIHT + " WHERE VTKHTT_ID = " + VTKHTT_ID;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getString(0);
        }
        return "0";
    }

    public long updateTinhTrangDTNHT(int VTKHTT_ID, int TINH_TRANG) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TINH_TRANG", TINH_TRANG);
        return database.update(EsspConstantVariables.TABLE_NAME_DTOAN_VTU_NGOAIHT, values, "VTKHTT_ID=?", new String[]{"" + VTKHTT_ID});
    }

    public long updateSoLuongDTNHT(int VTKHTT_ID, float SO_LUONG) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("SO_LUONG", SO_LUONG);
        return database.update(EsspConstantVariables.TABLE_NAME_DTOAN_VTU_NGOAIHT, values, "VTKHTT_ID=?", new String[]{"" + VTKHTT_ID});
    }

    public long deleteDuToanNht(int VTKHTT_ID) {
        database = this.getWritableDatabase();
        return database.delete(EsspConstantVariables.TABLE_NAME_DTOAN_VTU_NGOAIHT, "VTKHTT_ID=?",
                new String[]{"" + VTKHTT_ID});
    }

    public Cursor getDuToanNhtByIdHS(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + EsspConstantVariables.TABLE_NAME_DTOAN_VTU_NGOAIHT + " WHERE HOSO_ID = " + HOSO_ID;
        return database.rawQuery(strQuery, null);
    }

    public double sumThanhTienNHT(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT SUM(SO_LUONG * DON_GIA_KH) FROM " + EsspConstantVariables.TABLE_NAME_DTOAN_VTU_NGOAIHT + " WHERE HOSO_ID = " + HOSO_ID;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getDouble(0);
        }
        return 0d;
    }

    public double sumVatTu(int HOSO_ID) {
        database = this.getReadableDatabase();
        //TODO VinhNB sửa
//        String strQuery = "SELECT SUM(SO_LUONG * DG_VTU * HSDC_K1NC * HSDC_K2NC) FROM " + EsspConstantVariables.TABLE_NAME_DTOAN_VTU_NGOAIHT + " WHERE HOSO_ID = " + HOSO_ID;
        String strQuery = "SELECT SUM(SO_LUONG * DG_VTU) FROM " + EsspConstantVariables.TABLE_NAME_DTOAN_VTU_NGOAIHT + " WHERE HOSO_ID = " + HOSO_ID;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getDouble(0);
        }
        return 0d;
    }

    public double sumNhanCong(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT SUM(SO_LUONG * DG_NCONG * HSDC_K1NC * HSDC_K2NC) FROM " + EsspConstantVariables.TABLE_NAME_DTOAN_VTU_NGOAIHT + " WHERE HOSO_ID = " + HOSO_ID;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getDouble(0);
        }
        return 0d;
    }

    public double sumMTC(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT SUM(SO_LUONG * DG_MTCONG * HSDC_MTC) FROM " + EsspConstantVariables.TABLE_NAME_DTOAN_VTU_NGOAIHT + " WHERE HOSO_ID = " + HOSO_ID;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getDouble(0);
        }
        return 0d;
    }

    public int countDTNHTByHoSoID(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + EsspConstantVariables.TABLE_NAME_DTOAN_VTU_NGOAIHT + " WHERE HOSO_ID = " + HOSO_ID;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;
    }

    public int countDTNHTByHoSoID2(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + EsspConstantVariables.TABLE_NAME_DTOAN_VTU_NGOAIHT + " WHERE HOSO_ID = " + HOSO_ID + " AND TINH_TRANG = 0";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;
    }

    public long updateChungLoai(int CHUNG_LOAI) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("CHUNG_LOAI", CHUNG_LOAI);
        return database.update(EsspConstantVariables.TABLE_NAME_DTOAN_VTU_NGOAIHT, values, null, null);
    }

    public long updateDGTronGoi(double DG_TRONGOI) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("DG_TRONGOI", DG_TRONGOI);
        return database.update(EsspConstantVariables.TABLE_NAME_DTOAN_VTU_NGOAIHT, values, null, null);
    }

    public long updateDGVatTu(double DG_VTU) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("DG_VTU", DG_VTU);
        return database.update(EsspConstantVariables.TABLE_NAME_DTOAN_VTU_NGOAIHT, values, null, null);
    }

    public long updateDGNCong(double DG_NCONG) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("DG_NCONG", DG_NCONG);
        return database.update(EsspConstantVariables.TABLE_NAME_DTOAN_VTU_NGOAIHT, values, null, null);
    }

    public long updateDGMTC(double DG_MTCONG) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("DG_MTCONG", DG_MTCONG);
        return database.update(EsspConstantVariables.TABLE_NAME_DTOAN_VTU_NGOAIHT, values, null, null);
    }

    public long updateHSDC1NCNHT(int HSDC_K1NC) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("HSDC_K1NC", HSDC_K1NC);
        return database.update(EsspConstantVariables.TABLE_NAME_DTOAN_VTU_NGOAIHT, values, null, null);
    }

    public long updateHSDC2NCNHT(int HSDC_K2NC) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("HSDC_K2NC", HSDC_K2NC);
        return database.update(EsspConstantVariables.TABLE_NAME_DTOAN_VTU_NGOAIHT, values, null, null);
    }

    public long updateHSDCMTCNHT(int HSDC_MTC) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("HSDC_MTC", HSDC_MTC);
        return database.update(EsspConstantVariables.TABLE_NAME_DTOAN_VTU_NGOAIHT, values, null, null);
    }
    //endregion

    //region Xử lý bảng bản vẽ
    public long insertDataBanVe(int HOSO_ID, String TEN_ANH, String MO_TA, String TINH_TRANG) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("HOSO_ID", HOSO_ID);
        values.put("TEN_ANH", TEN_ANH);
        values.put("MO_TA", MO_TA);
        values.put("TINH_TRANG", TINH_TRANG);
        return database.insert(EsspConstantVariables.TABLE_NAME_BAN_VE, null, values);
    }

    public int getTTBV(int HOSO_ID) {
        int tt = 0;
        database = this.getReadableDatabase();
        String strQuery = "SELECT TINH_TRANG FROM " + EsspConstantVariables.TABLE_NAME_BAN_VE + " WHERE HOSO_ID = " + HOSO_ID;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            tt = Integer.parseInt(c.getString(0));
        }
        return tt;
    }

    public long deleteBanVe(String HOSO_ID) {
        database = this.getWritableDatabase();
        return database.delete(EsspConstantVariables.TABLE_NAME_BAN_VE, "HOSO_ID=?", new String[]{HOSO_ID});
    }

    public Cursor getBanVe(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + EsspConstantVariables.TABLE_NAME_BAN_VE + " WHERE HOSO_ID = " + HOSO_ID;
        return database.rawQuery(strQuery, null);
    }

    public int getTinhTrangBVByHoSoID(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT TINH_TRANG FROM " + EsspConstantVariables.TABLE_NAME_BAN_VE + " WHERE HOSO_ID = " + HOSO_ID;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;
    }

    public long updateTinhTrangBV(int HOSO_ID, int TINH_TRANG) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TINH_TRANG", TINH_TRANG);
        return database.update(EsspConstantVariables.TABLE_NAME_BAN_VE, values, "HOSO_ID=?", new String[]{"" + HOSO_ID});
    }

    public int countBanVeByHoSoID(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + EsspConstantVariables.TABLE_NAME_BAN_VE + " WHERE HOSO_ID = " + HOSO_ID;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;
    }

    public int countBanVeByHoSoID2(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + EsspConstantVariables.TABLE_NAME_BAN_VE + " WHERE HOSO_ID = " + HOSO_ID + " AND TINH_TRANG = 0";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;
    }
    //endregion

    //region Xử lý bảng phương án
    public int getTTPA(int HOSO_ID) {
        int tt = 0;
        database = this.getReadableDatabase();
        String strQuery = "SELECT TINH_TRANG FROM " + EsspConstantVariables.TABLE_NAME_PA + " WHERE HOSO_ID = " + HOSO_ID;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            tt = Integer.parseInt(c.getString(0));
        }
        return tt;
    }

    public boolean checkPAexist(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "";
        strQuery = "SELECT COUNT(*) FROM " + EsspConstantVariables.TABLE_NAME_PA + " WHERE HOSO_ID = " + HOSO_ID;
        Cursor c = database.rawQuery(strQuery, null);
        int sl = 0;
        if (c.moveToFirst()) {
            sl = c.getInt(0);
        }
        if (sl == 0) {
            return true;// hồ sơ chưa tồn tại
        } else {
            return false;// hồ sơ đã tồn tại
        }
    }

    public EsspEntityPhuongAn getDataPAByMaHS(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + EsspConstantVariables.TABLE_NAME_PA + " where HOSO_ID = " + HOSO_ID;
        Cursor c = database.rawQuery(strQuery, null);
        if (c != null) {
            c.moveToFirst();
        }
        EsspEntityPhuongAn entity = new EsspEntityPhuongAn();
        entity.setID(c.getInt(c.getColumnIndex("ID")));
        entity.setHOSO_ID(c.getInt(c.getColumnIndex("HOSO_ID")));
        entity.setDODEM_CTO_A(c.getString(c.getColumnIndex("DODEM_CTO_A")));
        entity.setDODEM_CTO_V(c.getString(c.getColumnIndex("DODEM_CTO_V")));
        entity.setDODEM_CTO_TI(c.getString(c.getColumnIndex("DODEM_CTO_TI")));
        entity.setVTRI_CTO(c.getInt(c.getColumnIndex("VTRI_CTO")));
        entity.setCOT_SO(c.getString(c.getColumnIndex("COT_SO")));
        entity.setVI_TRI_KHAC(c.getString(c.getColumnIndex("VI_TRI_KHAC")));
        entity.setHTHUC_TTOAN(c.getInt(c.getColumnIndex("HTHUC_TTOAN")));
        entity.setDIEM_DAUDIEN(c.getString(c.getColumnIndex("DIEM_DAUDIEN")));
        entity.setDTUONG_KHANG(c.getInt(c.getColumnIndex("DTUONG_KHANG")));
        entity.setTTRANG_SDUNG_DIEN(c.getInt(c.getColumnIndex("TTRANG_SDUNG_DIEN")));
        entity.setHTHUC_GCS(c.getInt(c.getColumnIndex("HTHUC_GCS")));
        entity.setHTK(c.getString(c.getColumnIndex("HTK")));
        entity.setTINH_TRANG(c.getInt(c.getColumnIndex("TINH_TRANG")));
        return entity;
    }

    public long insertDataPHUONGAN(int HOSO_ID, String DODEM_CTO_A, String DODEM_CTO_V,
                                   String DODEM_CTO_TI, int VTRI_CTO, String COT_SO, String VI_TRI_KHAC, int HTHUC_TTOAN,
                                   String DIEM_DAUDIEN, int TTRANG_SDUNG_DIEN, int HTHUC_GCS, String HTK, int TINH_TRANG) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("HOSO_ID", HOSO_ID);
        values.put("DODEM_CTO_A", DODEM_CTO_A);
        values.put("DODEM_CTO_V", DODEM_CTO_V);
        values.put("DODEM_CTO_TI", DODEM_CTO_TI);
        values.put("VTRI_CTO", VTRI_CTO);
        values.put("COT_SO", COT_SO);
        values.put("VI_TRI_KHAC", VI_TRI_KHAC);
        values.put("HTHUC_TTOAN", HTHUC_TTOAN);
        values.put("DIEM_DAUDIEN", DIEM_DAUDIEN);
        values.put("TTRANG_SDUNG_DIEN", TTRANG_SDUNG_DIEN);
        values.put("HTHUC_GCS", HTHUC_GCS);
        values.put("HTK", HTK);
        values.put("TINH_TRANG", TINH_TRANG);
        return database.insert(EsspConstantVariables.TABLE_NAME_PA, null, values);
    }

    public long updateDataPHUONGAN(int ID, int HOSO_ID, String DODEM_CTO_A, String DODEM_CTO_V,
                                   String DODEM_CTO_TI, int VTRI_CTO, String COT_SO, String VI_TRI_KHAC, int HTHUC_TTOAN,
                                   String DIEM_DAUDIEN, int TTRANG_SDUNG_DIEN, int HTHUC_GCS, String HTK) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("HOSO_ID", HOSO_ID);
        values.put("DODEM_CTO_A", DODEM_CTO_A);
        values.put("DODEM_CTO_V", DODEM_CTO_V);
        values.put("DODEM_CTO_TI", DODEM_CTO_TI);
        values.put("VTRI_CTO", VTRI_CTO);
        values.put("COT_SO", COT_SO);
        values.put("VI_TRI_KHAC", VI_TRI_KHAC);
        values.put("HTHUC_TTOAN", HTHUC_TTOAN);
        values.put("DIEM_DAUDIEN", DIEM_DAUDIEN);
        values.put("TTRANG_SDUNG_DIEN", TTRANG_SDUNG_DIEN);
        values.put("HTHUC_GCS", HTHUC_GCS);
        values.put("HTK", HTK);
        return database.update(EsspConstantVariables.TABLE_NAME_PA, values, "ID=?", new String[]{"" + ID});
    }

    public long deletePhuongAn(String HOSO_ID) {
        database = this.getWritableDatabase();
        return database.delete(EsspConstantVariables.TABLE_NAME_PA, "HOSO_ID=?", new String[]{HOSO_ID});
    }

    public Cursor getPhuongAnhByHoSoID(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + EsspConstantVariables.TABLE_NAME_PA + " WHERE HOSO_ID = " + HOSO_ID;
        return database.rawQuery(strQuery, null);
    }

    public int getTinhTrangPAByHoSoID(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT TINH_TRANG FROM " + EsspConstantVariables.TABLE_NAME_PA + " WHERE HOSO_ID = " + HOSO_ID;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;
    }

    public String getSoCotPAByHoSoID(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COT_SO FROM " + EsspConstantVariables.TABLE_NAME_PA + " WHERE HOSO_ID = " + HOSO_ID;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getString(0);
        }
        return "";
    }

    public long updateTinhTrangPHUONGAN(int HOSO_ID, int TINH_TRANG) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("HOSO_ID", HOSO_ID);
        values.put("TINH_TRANG", TINH_TRANG);
        return database.update(EsspConstantVariables.TABLE_NAME_PA, values, "HOSO_ID=?", new String[]{"" + HOSO_ID});
    }

    public int countPhuongAnByHoSoID(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + EsspConstantVariables.TABLE_NAME_PA + " WHERE HOSO_ID = " + HOSO_ID;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;
    }

    public int countPhuongAnByHoSoID2(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + EsspConstantVariables.TABLE_NAME_PA + " WHERE HOSO_ID = " + HOSO_ID + " AND TINH_TRANG = 0";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;
    }
    //endregion

    //region Xử lý bảng hình ảnh
    public long insertDataHinhAnh(int HOSO_ID, String TEN_ANH, String TINH_TRANG) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("HOSO_ID", HOSO_ID);
        values.put("TEN_ANH", TEN_ANH);
        values.put("TINH_TRANG", TINH_TRANG);
        return database.insert(EsspConstantVariables.TABLE_NAME_ANH, null, values);
    }

    public long updateDataHinhAnh(String TEN_ANH, String GHI_CHU) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("GHI_CHU", GHI_CHU);
        return database.update(EsspConstantVariables.TABLE_NAME_ANH, values, "TEN_ANH=?", new String[]{TEN_ANH});
    }

    public Cursor getHinhAnhByMaKH(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + EsspConstantVariables.TABLE_NAME_ANH + " WHERE HOSO_ID = " + HOSO_ID;
        return database.rawQuery(strQuery, null);
    }

    public String getGhiChuByTenAnh(String TEN_ANH) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT GHI_CHU FROM " + EsspConstantVariables.TABLE_NAME_ANH + " WHERE TEN_ANH = '" + TEN_ANH + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getString(0);
        }
        return "";
    }

    public long deleteHinhAnh(String TEN_ANH) {
        database = this.getWritableDatabase();
        return database.delete(EsspConstantVariables.TABLE_NAME_ANH, "TEN_ANH=?", new String[]{TEN_ANH});
    }

    public long deleteAnh(String HOSO_ID) {
        database = this.getWritableDatabase();
        return database.delete(EsspConstantVariables.TABLE_NAME_ANH, "HOSO_ID=?", new String[]{HOSO_ID});
    }

    public Cursor getAllDataHinhAnhByMaHS(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + EsspConstantVariables.TABLE_NAME_ANH + " WHERE HOSO_ID = " + HOSO_ID;
        return database.rawQuery(strQuery, null);
    }

    public long deleteAllDataHinhAnh(int HOSO_ID) {
        database = this.getWritableDatabase();
        return database.delete(EsspConstantVariables.TABLE_NAME_ANH, "HOSO_ID=?", new String[]{"" + HOSO_ID});
    }

    public int getTinhTrangHAByHoSoID(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT DISTINCT(TINH_TRANG) FROM " + EsspConstantVariables.TABLE_NAME_ANH + " WHERE HOSO_ID = " + HOSO_ID;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;
    }

    public int countTinhTrangHAByHoSoID(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + EsspConstantVariables.TABLE_NAME_ANH + " WHERE HOSO_ID = " + HOSO_ID + " AND TINH_TRANG = 0";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 1;
    }

    public int countHAByHoSoID(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + EsspConstantVariables.TABLE_NAME_ANH + " WHERE HOSO_ID = " + HOSO_ID;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;
    }

    public int countHAByHoSoID2(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + EsspConstantVariables.TABLE_NAME_ANH + " WHERE HOSO_ID = " + HOSO_ID + " AND TINH_TRANG = 0";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;
    }

    public long updateTinhTrangHA(int HOSO_ID, int TINH_TRANG) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TINH_TRANG", TINH_TRANG);
        return database.update(EsspConstantVariables.TABLE_NAME_ANH, values, "HOSO_ID=?", new String[]{"" + HOSO_ID});
    }
    //endregion

    //region Xử lý bảng nhật ký
    public long insertDataNhatKy(int HOSO_ID, String TINH_TRANG_SD_DIEN, String MUC_DICH_SD_DIEN, int TINH_TRANG) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("HOSO_ID", HOSO_ID);
        values.put("TINH_TRANG_SD_DIEN", TINH_TRANG_SD_DIEN);
        values.put("MUC_DICH_SD_DIEN", MUC_DICH_SD_DIEN);
        values.put("TINH_TRANG", TINH_TRANG);
        return database.insert(EsspConstantVariables.TABLE_NAME_NK, null, values);
    }

    public long updateDataNhatKy(int HOSO_ID, String TINH_TRANG_SD_DIEN, String MUC_DICH_SD_DIEN) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TINH_TRANG_SD_DIEN", TINH_TRANG_SD_DIEN);
        values.put("MUC_DICH_SD_DIEN", MUC_DICH_SD_DIEN);
        return database.update(EsspConstantVariables.TABLE_NAME_NK, values, "HOSO_ID=?", new String[]{"" + HOSO_ID});
    }

    public Cursor getAllDataNhatKyByMaHS(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + EsspConstantVariables.TABLE_NAME_NK + " WHERE HOSO_ID = " + HOSO_ID;
        return database.rawQuery(strQuery, null);
    }

    public boolean checkNhatKyExist(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + EsspConstantVariables.TABLE_NAME_NK + " WHERE HOSO_ID = " + HOSO_ID;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            if (c.getInt(0) > 0) {
                return true;
            }
        }
        return false;
    }

    public long deleteAllDataNhatKy(int HOSO_ID) {
        database = this.getWritableDatabase();
        return database.delete(EsspConstantVariables.TABLE_NAME_NK, "HOSO_ID=?", new String[]{"" + HOSO_ID});
    }
    //endregion

    //region Xử lý bảng thiết bị
    public long insertDataThietBi(int HOSO_ID, String MUC_DICH, String LOAI_TBI,
                                  String CONG_SUAT, int SO_LUONG, String DIENAP_SUDUNG, int TINH_TRANG) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("HOSO_ID", HOSO_ID);
        values.put("MUC_DICH", MUC_DICH);
        values.put("LOAI_TBI", LOAI_TBI);
        values.put("CONG_SUAT", CONG_SUAT);
        values.put("SO_LUONG", SO_LUONG);
        values.put("TINH_TRANG", TINH_TRANG);
        values.put("DIENAP_SUDUNG", DIENAP_SUDUNG);
        return database.insert(EsspConstantVariables.TABLE_NAME_TBI, null, values);
    }

    public long updateDataThietBi(int ID, int HOSO_ID, String MUC_DICH, String LOAI_TBI,
                                  String CONG_SUAT, int SO_LUONG, String DIENAP_SUDUNG) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("HOSO_ID", HOSO_ID);
        values.put("MUC_DICH", MUC_DICH);
        values.put("LOAI_TBI", LOAI_TBI);
        values.put("CONG_SUAT", CONG_SUAT);
        values.put("SO_LUONG", SO_LUONG);
        values.put("DIENAP_SUDUNG", DIENAP_SUDUNG);
        return database.update(EsspConstantVariables.TABLE_NAME_TBI, values, "ID=?", new String[]{"" + ID});
    }

    public Cursor getAllDataThietBiByMaHS(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + EsspConstantVariables.TABLE_NAME_TBI + " WHERE HOSO_ID = " + HOSO_ID;
        return database.rawQuery(strQuery, null);
    }

    public int getMaxID() {
        database = this.getReadableDatabase();
        String strQuery = "SELECT MAX(ID) FROM " + EsspConstantVariables.TABLE_NAME_TBI;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;
    }

    public long deleteDataTBIByID(int ID) {
        database = this.getWritableDatabase();
        return database.delete(EsspConstantVariables.TABLE_NAME_TBI, "ID=?", new String[]{"" + ID});
    }

    public long deleteThietBi(String HOSO_ID) {
        database = this.getWritableDatabase();
        return database.delete(EsspConstantVariables.TABLE_NAME_TBI, "HOSO_ID=?", new String[]{HOSO_ID});
    }

    public int getTinhTrangTBByHoSoID(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT TINH_TRANG FROM " + EsspConstantVariables.TABLE_NAME_TBI + " WHERE HOSO_ID = " + HOSO_ID;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;
    }

    public int countTinhTrangTBByHoSoID(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + EsspConstantVariables.TABLE_NAME_TBI + " WHERE HOSO_ID = " + HOSO_ID + " AND TINH_TRANG = 0";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 1;
    }

    public int countTBByHoSoID(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + EsspConstantVariables.TABLE_NAME_TBI + " WHERE HOSO_ID = " + HOSO_ID;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;
    }

    public int countTBByHoSoID2(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + EsspConstantVariables.TABLE_NAME_TBI + " WHERE HOSO_ID = " + HOSO_ID + " AND TINH_TRANG = 0";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;
    }

    public float sumCSTBByHoSoID(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT SUM(CONG_SUAT) FROM " + EsspConstantVariables.TABLE_NAME_TBI + " WHERE HOSO_ID = " + HOSO_ID;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getFloat(0);
        }
        return 0f;
    }

    public long updateTinhTrangTB(int HOSO_ID, int TINH_TRANG) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TINH_TRANG", TINH_TRANG);
        return database.update(EsspConstantVariables.TABLE_NAME_TBI, values, "HOSO_ID=?", new String[]{"" + HOSO_ID});
    }

    // ------------------Xử lý bảng công suất -------------------

//    public long insertDataCSUAT(int HOSO_ID, String CS1, String CS2, String CS3, String CS4,
//                                String CS5, String CS6, String CS7, String CS8, String CS9,
//                                String CS10, String CS11, String CS12, int TINH_TRANG) {
//        database = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("HOSO_ID", HOSO_ID);
//        values.put("CS1", CS1);
//        values.put("CS2", CS2);
//        values.put("CS3", CS3);
//        values.put("CS4", CS4);
//        values.put("CS5", CS5);
//        values.put("CS6", CS6);
//        values.put("CS7", CS7);
//        values.put("CS8", CS8);
//        values.put("CS9", CS9);
//        values.put("CS10", CS10);
//        values.put("CS11", CS11);
//        values.put("CS12", CS12);
//        values.put("TINH_TRANG", TINH_TRANG);
//        return database.insert(EsspConstantVariables.TABLE_NAME_CSUAT, null, values);
//    }
//
//    public long updateDataCSUAT(int HOSO_ID, String CS1, String CS2, String CS3, String CS4,
//                                String CS5, String CS6, String CS7, String CS8, String CS9,
//                                String CS10, String CS11, String CS12, int TINH_TRANG){
//        database = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("HOSO_ID", HOSO_ID);
//        values.put("CS1", CS1);
//        values.put("CS2", CS2);
//        values.put("CS3", CS3);
//        values.put("CS4", CS4);
//        values.put("CS5", CS5);
//        values.put("CS6", CS6);
//        values.put("CS7", CS7);
//        values.put("CS8", CS8);
//        values.put("CS9", CS9);
//        values.put("CS10", CS10);
//        values.put("CS11", CS11);
//        values.put("CS12", CS12);
//        values.put("TINH_TRANG", TINH_TRANG);
//        return database.update(EsspConstantVariables.TABLE_NAME_CSUAT, values, "HOSO_ID=?", new String[]{"" + HOSO_ID});
//    }
//
//    public long deleteCongSuat(String HOSO_ID) {
//        database = this.getWritableDatabase();
//        return database.delete(EsspConstantVariables.TABLE_NAME_CSUAT, "HOSO_ID=?", new String[]{HOSO_ID});
//    }
//
//    public Cursor getCSUATByID(int HOSO_ID){
//        database = this.getReadableDatabase();
//        String strQuery = "SELECT * FROM " + EsspConstantVariables.TABLE_NAME_CSUAT + " WHERE HOSO_ID = " + HOSO_ID;
//        Cursor c = database.rawQuery(strQuery, null);
//        return c;
//    }
//
//    public long updateTinhTrangCS(int HOSO_ID, int TINH_TRANG){
//        database = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("TINH_TRANG", TINH_TRANG);
//        return database.update(EsspConstantVariables.TABLE_NAME_CSUAT, values, "HOSO_ID=?", new String[]{"" + HOSO_ID});
//    }
//
//    public int getTinhTrangCSByHoSoID(int HOSO_ID){
//        database = this.getReadableDatabase();
//        String strQuery = "SELECT TINH_TRANG FROM " + EsspConstantVariables.TABLE_NAME_CSUAT + " WHERE HOSO_ID = " + HOSO_ID;
//        Cursor c = database.rawQuery(strQuery, null);
//        if(c.moveToFirst()){
//            return c.getInt(0);
//        }
//        return 0;
//    }

    public long insertDataCSUAT(int HOSO_ID, String KHOANG_TGIAN, String CONG_SUAT, int TINH_TRANG) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("HOSO_ID", HOSO_ID);
        values.put("KHOANG_TGIAN", KHOANG_TGIAN);
        values.put("CONG_SUAT", CONG_SUAT);
        values.put("TINH_TRANG", TINH_TRANG);
        return database.insert(EsspConstantVariables.TABLE_NAME_CSUAT, null, values);
    }

    public long updateDataCSUAT(int ID, String KHOANG_TGIAN, String CONG_SUAT, int TINH_TRANG) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ID", ID);
        values.put("KHOANG_TGIAN", KHOANG_TGIAN);
        values.put("CONG_SUAT", CONG_SUAT);
        values.put("TINH_TRANG", TINH_TRANG);
        return database.update(EsspConstantVariables.TABLE_NAME_CSUAT, values, "ID=?", new String[]{"" + ID});
    }

    public long deleteCongSuat(String ID) {
        database = this.getWritableDatabase();
        return database.delete(EsspConstantVariables.TABLE_NAME_CSUAT, "ID=?", new String[]{ID});
    }

    public Cursor getCSUATByID(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + EsspConstantVariables.TABLE_NAME_CSUAT + " WHERE HOSO_ID = " + HOSO_ID;
        Cursor c = database.rawQuery(strQuery, null);
        return c;
    }

    public long updateTinhTrangCS(int HOSO_ID, int TINH_TRANG) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TINH_TRANG", TINH_TRANG);
        return database.update(EsspConstantVariables.TABLE_NAME_CSUAT, values, "HOSO_ID=?", new String[]{"" + HOSO_ID});
    }

    public int getTinhTrangCSByHoSoID(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT TINH_TRANG FROM " + EsspConstantVariables.TABLE_NAME_CSUAT + " WHERE HOSO_ID = " + HOSO_ID;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;
    }

    public String getIDCSuat() {
        database = this.getReadableDatabase();
        String strQuery = "SELECT MAX(ID) FROM " + EsspConstantVariables.TABLE_NAME_CSUAT;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getString(0);
        }
        return "0";
    }

    public int countCSByHoSoID2(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + EsspConstantVariables.TABLE_NAME_CSUAT + " WHERE HOSO_ID = " + HOSO_ID + " AND TINH_TRANG = 0";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;
    }
    //endregion

    //region Xử lý bảng log thiết bị
    public long insertDataLogSuggest(String LOG_TYPE, String LOG_CONTENT) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("LOG_TYPE", LOG_TYPE);
        values.put("LOG_CONTENT", LOG_CONTENT);
        return database.insert(EsspConstantVariables.TABLE_NAME_DANH_MUC_SUGGEST, null, values);
    }

    public Cursor getDataLogSuggest(String LOG_TYPE) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + EsspConstantVariables.TABLE_NAME_DANH_MUC_SUGGEST + " WHERE LOG_TYPE = '" + LOG_TYPE + "'";
        return database.rawQuery(strQuery, null);
    }

    public boolean checkExistLogSuggest(String LOG_TYPE, String LOG_CONTENT) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + EsspConstantVariables.TABLE_NAME_DANH_MUC_SUGGEST
                + " WHERE LOG_CONTENT = '" + LOG_CONTENT + "' and LOG_TYPE = '" + LOG_TYPE + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0) > 0;
        }
        return false;
    }
    //endregion

    //region Xử lý bảng hồ sơ nghiệm thu
    public long insertDataHosoNThu(int HoSoId, int isNHIEUBIEUGIA, String KIMNIEMCHISO, String MA_HDONG, String MA_KHANG, String TEN_KHANG,
                                   String DIA_CHI, String SO_CMT, String DIEN_THOAI, String MA_GCS, String LYDO_TREOTHAO, String LOAI_CTO, String MA_CTO, String MA_NUOC, String TEN_NUOC,
                                   int NAM_SX, String DONG_DIEN, String DIEN_AP, String HANGSO_K, String CCX, String TEM_CQUANG,
                                   int SOLAN_CBAO, String HS_NHAN, String SO_CTO,
                                   String LAPQUA_TI, String LAPQUA_TU, String HS_NHAN_LUOI, String P_MAX, String TD_P_MAX, String H1_P_MAX,
                                   String TD_H1_P_MAX, String TTRANG_CHI_NIEMPHONG, String MASO_CHI_KDINH, String SOVIEN_CHIKDINH,
                                   String MASO_CHICONGQUANG, String SOVIEN_CHICONGQUANG, String MASO_CHIHOM, String SOVIEN_CHIHOM,
                                   String MASO_TEMKDINH, String NGAY_LAP, String NGUOI_LAP, String MA_DVIQLY, String CMIS_MA_YCAU_KNAI,
                                   String NGAY_KDINH_FORMAT, String TEN_NHAN_VIEN, String SO_BIEN_BAN, String USER_NAME, String CHI_SO_TREO, int TINH_TRANG) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("HoSoId", HoSoId);
        values.put("isNHIEUBIEUGIA", isNHIEUBIEUGIA);
        values.put("KIMNIEMCHISO", KIMNIEMCHISO);
        values.put("MA_HDONG", MA_HDONG);
        values.put("MA_KHANG", MA_KHANG);
        values.put("TEN_KHANG", TEN_KHANG);
        values.put("DIA_CHI", DIA_CHI);
        values.put("SO_CMT", SO_CMT);
        values.put("DIEN_THOAI", DIEN_THOAI);
        values.put("MA_GCS", MA_GCS);
        values.put("LYDO_TREOTHAO", LYDO_TREOTHAO);
        values.put("LOAI_CTO", LOAI_CTO);
        values.put("MA_CTO", MA_CTO);
        values.put("MA_NUOC", MA_NUOC);
        values.put("TEN_NUOC", TEN_NUOC);
        values.put("NAM_SX", NAM_SX);
        values.put("DONG_DIEN", DONG_DIEN);
        values.put("DIEN_AP", DIEN_AP);
        values.put("HANGSO_K", HANGSO_K);
        values.put("CCX", CCX);
        values.put("TEM_CQUANG", TEM_CQUANG);
        values.put("SOLAN_CBAO", SOLAN_CBAO);
        values.put("HS_NHAN", HS_NHAN);
        values.put("SO_CTO", SO_CTO);
        values.put("LAPQUA_TI", LAPQUA_TI);
        values.put("LAPQUA_TU", LAPQUA_TU);
        values.put("HS_NHAN_LUOI", HS_NHAN_LUOI);
        values.put("P_MAX", P_MAX);
        values.put("TD_P_MAX", TD_P_MAX);
        values.put("H1_P_MAX", H1_P_MAX);
        values.put("TD_H1_P_MAX", TD_H1_P_MAX);
        values.put("TTRANG_CHI_NIEMPHONG", TTRANG_CHI_NIEMPHONG);
        values.put("MASO_CHI_KDINH", MASO_CHI_KDINH);
        values.put("SOVIEN_CHIKDINH", SOVIEN_CHIKDINH);
        values.put("MASO_CHICONGQUANG", MASO_CHICONGQUANG);
        values.put("SOVIEN_CHICONGQUANG", SOVIEN_CHICONGQUANG);
        values.put("MASO_CHIHOM", MASO_CHIHOM);
        values.put("SOVIEN_CHIHOM", SOVIEN_CHIHOM);
        values.put("MASO_TEMKDINH", MASO_TEMKDINH);
        values.put("NGAY_LAP", NGAY_LAP);
        values.put("NGUOI_LAP", NGUOI_LAP);
        values.put("MA_DVIQLY", MA_DVIQLY);
        values.put("CMIS_MA_YCAU_KNAI", CMIS_MA_YCAU_KNAI);
        values.put("NGAY_KDINH_FORMAT", NGAY_KDINH_FORMAT);
        values.put("TEN_NHAN_VIEN", TEN_NHAN_VIEN);
        values.put("SO_BIEN_BAN", SO_BIEN_BAN);
        values.put("USER_NAME", USER_NAME);
        values.put("CHI_SO_TREO", CHI_SO_TREO);
        values.put("TINH_TRANG", TINH_TRANG);
        return database.insert(EsspConstantVariables.TABLE_NAME_HOSO_NTHU, null, values);
    }

    public Cursor getDataHosoNThu(String MA_DVIQLY, String USER_NAME) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + EsspConstantVariables.TABLE_NAME_HOSO_NTHU + " WHERE MA_DVIQLY = '" + MA_DVIQLY +
                "' AND USER_NAME = '" + USER_NAME + "'";
        return database.rawQuery(strQuery, null);
    }

    public EsspEntityHoSoThiCong getHoSoNThuByID(int HoSoId) {
        EsspEntityHoSoThiCong entity = new EsspEntityHoSoThiCong();
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + EsspConstantVariables.TABLE_NAME_HOSO_NTHU + " WHERE HoSoId = " + HoSoId;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            entity.setHoSoId(c.getInt(c.getColumnIndex("HoSoId")));
            entity.setIsNHIEUBIEUGIA(c.getInt(c.getColumnIndex("isNHIEUBIEUGIA")));
            entity.setKIMNIEMCHISO(c.getString(c.getColumnIndex("KIMNIEMCHISO")) == null ? "" : c.getString(c.getColumnIndex("KIMNIEMCHISO")));
            entity.setMA_HDONG(c.getString(c.getColumnIndex("MA_HDONG")) == null ? "" : c.getString(c.getColumnIndex("MA_HDONG")));
            entity.setMA_KHANG(c.getString(c.getColumnIndex("MA_KHANG")) == null ? "" : c.getString(c.getColumnIndex("MA_KHANG")));
            entity.setTEN_KHANG(c.getString(c.getColumnIndex("TEN_KHANG")) == null ? "" : c.getString(c.getColumnIndex("TEN_KHANG")));
            entity.setDIA_CHI(c.getString(c.getColumnIndex("DIA_CHI")) == null ? "" : c.getString(c.getColumnIndex("DIA_CHI")));
            entity.setSO_CMT(c.getString(c.getColumnIndex("SO_CMT")) == null ? "" : c.getString(c.getColumnIndex("SO_CMT")));
            entity.setDIEN_THOAI(c.getString(c.getColumnIndex("DIEN_THOAI")) == null ? "" : c.getString(c.getColumnIndex("DIEN_THOAI")));
            entity.setMA_GCS(c.getString(c.getColumnIndex("MA_GCS")) == null ? "" : c.getString(c.getColumnIndex("MA_GCS")));
            entity.setLYDO_TREOTHAO(c.getString(c.getColumnIndex("LYDO_TREOTHAO")) == null ? "" : c.getString(c.getColumnIndex("LYDO_TREOTHAO")));
            entity.setLOAI_CTO(c.getString(c.getColumnIndex("LOAI_CTO")) == null ? "" : c.getString(c.getColumnIndex("LOAI_CTO")));
            entity.setMA_CTO(c.getString(c.getColumnIndex("MA_CTO")) == null ? "" : c.getString(c.getColumnIndex("MA_CTO")));
            entity.setMA_NUOC(c.getString(c.getColumnIndex("MA_NUOC")) == null ? "" : c.getString(c.getColumnIndex("MA_NUOC")));
            entity.setTEN_NUOC(c.getString(c.getColumnIndex("TEN_NUOC")) == null ? "" : c.getString(c.getColumnIndex("TEN_NUOC")));
            entity.setNAM_SX(c.getString(c.getColumnIndex("NAM_SX")) == null ? 0 : c.getInt(c.getColumnIndex("NAM_SX")));
            entity.setDONG_DIEN(c.getString(c.getColumnIndex("DONG_DIEN")) == null ? "" : c.getString(c.getColumnIndex("DONG_DIEN")));
            entity.setDIEN_AP(c.getString(c.getColumnIndex("DIEN_AP")) == null ? "" : c.getString(c.getColumnIndex("DIEN_AP")));
            entity.setHANGSO_K(c.getString(c.getColumnIndex("HANGSO_K")) == null ? "" : c.getString(c.getColumnIndex("HANGSO_K")));
            entity.setCCX(c.getString(c.getColumnIndex("CCX")) == null ? "" : c.getString(c.getColumnIndex("CCX")));
            entity.setTEM_CQUANG(c.getString(c.getColumnIndex("TEM_CQUANG")) == null ? "" : c.getString(c.getColumnIndex("TEM_CQUANG")));
            entity.setSOLAN_CBAO(c.getString(c.getColumnIndex("SOLAN_CBAO")) == null ? 1 : c.getInt(c.getColumnIndex("SOLAN_CBAO")));
            entity.setHS_NHAN(c.getString(c.getColumnIndex("HS_NHAN")) == null ? "" : c.getString(c.getColumnIndex("HS_NHAN")));
            entity.setSO_CTO(c.getString(c.getColumnIndex("SO_CTO")) == null ? "" : c.getString(c.getColumnIndex("SO_CTO")));
            entity.setLAPQUA_TI(c.getString(c.getColumnIndex("LAPQUA_TI")) == null ? "" : c.getString(c.getColumnIndex("LAPQUA_TI")));
            entity.setLAPQUA_TU(c.getString(c.getColumnIndex("LAPQUA_TU")) == null ? "" : c.getString(c.getColumnIndex("LAPQUA_TU")));
            entity.setHS_NHAN_LUOI(c.getString(c.getColumnIndex("HS_NHAN_LUOI")) == null ? "" : c.getString(c.getColumnIndex("HS_NHAN_LUOI")));
            entity.setP_MAX(c.getString(c.getColumnIndex("P_MAX")) == null ? "" : c.getString(c.getColumnIndex("P_MAX")));
            entity.setTD_P_MAX(c.getString(c.getColumnIndex("TD_P_MAX")) == null ? "" : c.getString(c.getColumnIndex("TD_P_MAX")));
            entity.setH1_P_MAX(c.getString(c.getColumnIndex("H1_P_MAX")) == null ? "" : c.getString(c.getColumnIndex("H1_P_MAX")));
            entity.setTD_H1_P_MAX(c.getString(c.getColumnIndex("TD_H1_P_MAX")) == null ? "" : c.getString(c.getColumnIndex("TD_H1_P_MAX")));
            entity.setTTRANG_CHI_NIEMPHONG(c.getString(c.getColumnIndex("TTRANG_CHI_NIEMPHONG")) == null ? "" : c.getString(c.getColumnIndex("TTRANG_CHI_NIEMPHONG")));
            entity.setMASO_CHI_KDINH(c.getString(c.getColumnIndex("MASO_CHI_KDINH")) == null ? "" : c.getString(c.getColumnIndex("MASO_CHI_KDINH")));
            entity.setSOVIEN_CHIKDINH(c.getString(c.getColumnIndex("SOVIEN_CHIKDINH")) == null ? "" : c.getString(c.getColumnIndex("SOVIEN_CHIKDINH")));
            entity.setMASO_CHICONGQUANG(c.getString(c.getColumnIndex("MASO_CHICONGQUANG")) == null ? "" : c.getString(c.getColumnIndex("MASO_CHICONGQUANG")));
            entity.setSOVIEN_CHICONGQUANG(c.getString(c.getColumnIndex("SOVIEN_CHICONGQUANG")) == null ? "" : c.getString(c.getColumnIndex("SOVIEN_CHICONGQUANG")));
            entity.setMASO_CHIHOM(c.getString(c.getColumnIndex("MASO_CHIHOM")) == null ? "" : c.getString(c.getColumnIndex("MASO_CHIHOM")));
            entity.setSOVIEN_CHIHOM(c.getString(c.getColumnIndex("SOVIEN_CHIHOM")) == null ? "" : c.getString(c.getColumnIndex("SOVIEN_CHIHOM")));
            entity.setMASO_TEMKDINH(c.getString(c.getColumnIndex("MASO_TEMKDINH")) == null ? "" : c.getString(c.getColumnIndex("MASO_TEMKDINH")));
            entity.setNGAY_LAP(c.getString(c.getColumnIndex("NGAY_LAP")) == null ? "" : c.getString(c.getColumnIndex("NGAY_LAP")));
            entity.setNGUOI_LAP(c.getString(c.getColumnIndex("NGUOI_LAP")) == null ? "" : c.getString(c.getColumnIndex("NGUOI_LAP")));
            entity.setMA_DVIQLY(c.getString(c.getColumnIndex("MA_DVIQLY")) == null ? "" : c.getString(c.getColumnIndex("MA_DVIQLY")));
            entity.setCMIS_MA_YCAU_KNAI(c.getString(c.getColumnIndex("CMIS_MA_YCAU_KNAI")) == null ? "" : c.getString(c.getColumnIndex("CMIS_MA_YCAU_KNAI")));
            entity.setNGAY_KDINH_FORMAT(c.getString(c.getColumnIndex("NGAY_KDINH_FORMAT")) == null ? "" : c.getString(c.getColumnIndex("NGAY_KDINH_FORMAT")));
            entity.setTEN_NHAN_VIEN(c.getString(c.getColumnIndex("TEN_NHAN_VIEN")) == null ? "" : c.getString(c.getColumnIndex("TEN_NHAN_VIEN")));
            entity.setSO_BIEN_BAN(c.getString(c.getColumnIndex("SO_BIEN_BAN")) == null ? "" : c.getString(c.getColumnIndex("SO_BIEN_BAN")));
            entity.setUSER_NAME(c.getString(c.getColumnIndex("USER_NAME")) == null ? "" : c.getString(c.getColumnIndex("USER_NAME")));
            entity.setCHI_SO_TREO(c.getString(c.getColumnIndex("CHI_SO_TREO")) == null ? "" : c.getString(c.getColumnIndex("CHI_SO_TREO")));
            entity.setTINH_TRANG(c.getInt(c.getColumnIndex("TINH_TRANG")));
        }
        return entity;
    }

    public long updateDataNghiemThu(int HoSoId, String KIMNIEMCHISO, String CHI_SO_TREO) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("KIMNIEMCHISO", KIMNIEMCHISO);
        values.put("CHI_SO_TREO", CHI_SO_TREO);
        return database.update(EsspConstantVariables.TABLE_NAME_HOSO_NTHU, values, "HoSoId=?", new String[]{"" + HoSoId});
    }

    public long updateTinhTrangHSThiCong(int HoSoId, int TINH_TRANG) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TINH_TRANG", TINH_TRANG);
        return database.update(EsspConstantVariables.TABLE_NAME_HOSO_NTHU, values, "HoSoId=?", new String[]{"" + HoSoId});
    }

    public long deleteHoSoThiCong(String HoSoId) {
        database = this.getWritableDatabase();
        return database.delete(EsspConstantVariables.TABLE_NAME_HOSO_NTHU, "HoSoId=?", new String[]{HoSoId});
    }
    //endregion

    //region Xử lý bảng log thiết bị
    public long insertDataDutoanNThu(int DUTOAN_ID, int HOSO_ID, String MA_DVIQLY, String MA_VTU, float SO_LUONG,
                                     int SO_HUU, int STT, String GHI_CHU, int TT_TUTUC, int TT_THUHOI, float THUC_TE,
                                     float CHENH_LECH, String TEN_VTU, String DON_VI_TINH, String KHTT) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("DUTOAN_ID", DUTOAN_ID);
        values.put("HOSO_ID", HOSO_ID);
        values.put("MA_DVIQLY", MA_DVIQLY);
        values.put("MA_VTU", MA_VTU);
        values.put("SO_LUONG", SO_LUONG);
        values.put("SO_HUU", SO_HUU);
        values.put("STT", STT);
        values.put("GHI_CHU", GHI_CHU);
        values.put("TT_TUTUC", TT_TUTUC);
        values.put("TT_THUHOI", TT_THUHOI);
        values.put("THUC_TE", THUC_TE);
        values.put("CHENH_LECH", CHENH_LECH);
        values.put("TEN_VTU", TEN_VTU);
        values.put("DON_VI_TINH", DON_VI_TINH);
        values.put("KHTT", KHTT);
        return database.insert(EsspConstantVariables.TABLE_NAME_DUTOAN_NTHU, null, values);
    }

    public Cursor getDataDutoanNThuByID(int HOSO_ID) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + EsspConstantVariables.TABLE_NAME_DUTOAN_NTHU + " WHERE HOSO_ID = " + HOSO_ID;
        return database.rawQuery(strQuery, null);
    }

    public long updateDataDuToanTTe(int DUTOAN_ID, String THUC_TE) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("THUC_TE", THUC_TE);
        return database.update(EsspConstantVariables.TABLE_NAME_DUTOAN_NTHU, values, "DUTOAN_ID=?", new String[]{"" + DUTOAN_ID});
    }

    public long deleteDuToanThiCong(String HOSO_ID) {
        database = this.getWritableDatabase();
        return database.delete(EsspConstantVariables.TABLE_NAME_DUTOAN_NTHU, "HOSO_ID=?", new String[]{HOSO_ID});
    }


    //endregion

}
