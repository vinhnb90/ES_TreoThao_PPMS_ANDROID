package com.es.tungnv.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.es.tungnv.entity.TthtEntityLoaiCongTo;
import com.es.tungnv.utils.TthtCommon;
import com.es.tungnv.utils.TthtConstantVariables;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TUNGNV on 3/19/2016.
 */
public class TthtSQLiteConnection extends SQLiteOpenHelper {

    //region Khai báo biến
    private static TthtSQLiteConnection instance;
    private SQLiteDatabase database;
    //endregion

    //region Khởi tạo
    private TthtSQLiteConnection(Context context) {
        super(context, Environment.getExternalStorageDirectory() + TthtConstantVariables.PROGRAM_DB_PATH + TthtConstantVariables.DATABASE_NAME, null, TthtConstantVariables.DATABASE_VERSION);
        SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory() + TthtConstantVariables.PROGRAM_DB_PATH + TthtConstantVariables.DATABASE_NAME, null);
    }

    @Override
    public synchronized void close() {
        if (database != null) {
            database.close();
            database = null;
        }
        super.close();
    }

    public static synchronized TthtSQLiteConnection getInstance(Context context) {
        if (instance == null) {
            instance = new TthtSQLiteConnection(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TthtConstantVariables.CREATE_TABLE_DVIQLY);
        db.execSQL(TthtConstantVariables.CREATE_TABLE_TRAM);
        db.execSQL(TthtConstantVariables.CREATE_TABLE_REMEMBER);
        db.execSQL(TthtConstantVariables.CREATE_TABLE_BBAN_CONGTO);
        db.execSQL(TthtConstantVariables.CREATE_TABLE_DETAIL_CONGTO);
        db.execSQL(TthtConstantVariables.CREATE_TABLE_CONGTO_TI);
        db.execSQL(TthtConstantVariables.CREATE_TABLE_CONGTO_TU);
        db.execSQL(TthtConstantVariables.CREATE_TABLE_ANH);
        db.execSQL(TthtConstantVariables.CREATE_TABLE_LOAI_CONGTO);
        db.execSQL(TthtConstantVariables.CREATE_TABLE_BBAN_TU_TI);
        db.execSQL(TthtConstantVariables.CREATE_TABLE_CHITIET_TU_TI);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TthtConstantVariables.TABLE_NAME_DVIQLY);
        db.execSQL("DROP TABLE IF EXISTS " + TthtConstantVariables.TABLE_NAME_TRAM);
        db.execSQL("DROP TABLE IF EXISTS " + TthtConstantVariables.TABLE_NAME_REMEMBER);
        db.execSQL("DROP TABLE IF EXISTS " + TthtConstantVariables.TABLE_NAME_BBAN_CONGTO);
        db.execSQL("DROP TABLE IF EXISTS " + TthtConstantVariables.TABLE_NAME_DETAIL_CONGTO);
        db.execSQL("DROP TABLE IF EXISTS " + TthtConstantVariables.TABLE_NAME_CONGTO_TI);
        db.execSQL("DROP TABLE IF EXISTS " + TthtConstantVariables.TABLE_NAME_CONGTO_TU);
        db.execSQL("DROP TABLE IF EXISTS " + TthtConstantVariables.TABLE_NAME_ANH);
        db.execSQL("DROP TABLE IF EXISTS " + TthtConstantVariables.TABLE_NAME_LOAI_CONGTO);
        db.execSQL("DROP TABLE IF EXISTS " + TthtConstantVariables.TABLE_NAME_BBAN_TU_TI);
        db.execSQL("DROP TABLE IF EXISTS " + TthtConstantVariables.TABLE_NAME_CHITIET_TU_TI);


        onCreate(db);
    }
    //endregion

    //region Xử lý bảng đơn vị
    public long insertDataDVIQLY(String MA_DVIQLY, String TEN_DVIQLY) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MA_DVIQLY", MA_DVIQLY);
        values.put("TEN_DVIQLY", TEN_DVIQLY);
        return database.insert(TthtConstantVariables.TABLE_NAME_DVIQLY, null, values);
    }

    public long deleteAllDataDVIQLY() {
        database = this.getWritableDatabase();
        return database.delete(TthtConstantVariables.TABLE_NAME_DVIQLY, null, null);
    }

    public List<String> getDataDVIQLY() {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + TthtConstantVariables.TABLE_NAME_DVIQLY;
        Cursor c = database.rawQuery(strQuery, null);
        List<String> list_dviqly = new ArrayList<String>();
        if (c.moveToFirst()) {
            do {
                list_dviqly.add(new StringBuilder(c.getString(1)).append(" - ").append(c.getString(2)).toString());
            } while (c.moveToNext());
        }
        return list_dviqly;
    }

    public boolean checkCToByIDBBanAndMaBDong(int ID_BBAN_TRTH, String MA_BDONG) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + TthtConstantVariables.TABLE_NAME_DETAIL_CONGTO + " WHERE ID_BBAN_TRTH = " + ID_BBAN_TRTH + " AND MA_BDONG = '" + MA_BDONG + "'";

        Cursor cursor = database.rawQuery(strQuery, null);
        int totalRows = cursor.getCount();
        cursor.close();
        if (totalRows > 0) {
            return true;
        } else return false;
    }

    public boolean checkCtoDaGui(int ID_BBAN_TRTH, String MA_DVIQLY, String MA_TRAM, String MA_BDONG, String NGAY_TRTH) {
        database = this.getReadableDatabase();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("SELECT COUNT(*) FROM (SELECT * FROM ");
        strQuery.append(TthtConstantVariables.TABLE_NAME_DETAIL_CONGTO);
        strQuery.append(" D INNER JOIN (SELECT * FROM ");
        strQuery.append(TthtConstantVariables.TABLE_NAME_BBAN_CONGTO);
        strQuery.append(" WHERE MA_TRAM = '");
        strQuery.append(MA_TRAM);
        strQuery.append("') AS B WHERE D.ID_BBAN_TRTH = B.ID_BBAN_TRTH)  WHERE MA_BDONG = '");
        strQuery.append(MA_BDONG);
        strQuery.append("' AND NGAY_TRTH = '");
        strQuery.append(NGAY_TRTH);
        strQuery.append("'");
        strQuery.append(" AND TRANG_THAI_DU_LIEU = 2");
        strQuery.append(" AND MA_NVIEN = '");
        strQuery.append(TthtCommon.getMaNvien());
        strQuery.append("'");
        strQuery.append(" AND ID_BBAN_TRTH = '");
        strQuery.append(ID_BBAN_TRTH);
        strQuery.append("'");
        Cursor c = database.rawQuery(strQuery.toString(), null);
        if (c.moveToFirst()) {
            if (c.getInt(0) > 0) return true;
            else return false;
        }
        return false;
    }


    public List<String> getListIDMaDviQly() {
        List<String> rowid = new ArrayList<String>();
        database = this.getReadableDatabase();
        String strQuery = new StringBuilder("SELECT MA_DVIQLY FROM ").append(TthtConstantVariables.TABLE_NAME_DVIQLY).toString();
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
        String strQuery = "SELECT MA_DVIQLY FROM " + TthtConstantVariables.TABLE_NAME_DVIQLY;
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
        String strQuery = "SELECT MA_DVIQLY FROM " + TthtConstantVariables.TABLE_NAME_DVIQLY + " WHERE ID = " + ID_DVIQLY;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getString(0);
        }
        return "";
    }

    public String getTenDviByMa(String MA_DVIQLY) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT TEN_DVIQLY FROM " + TthtConstantVariables.TABLE_NAME_DVIQLY + " WHERE MA_DVIQLY = '" + MA_DVIQLY + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getString(0);
        }
        return "";
    }
    //endregion

    //region Xử lý bảng Trạm
    public long insertDataTRAM(String MA_TRAM, String MA_DVIQLY, String TEN_TRAM, String LOAI_TRAM, String CSUAT_TRAM, String MA_CAP_DA, String MA_CAP_DA_RA, String DINH_DANH) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MA_TRAM", MA_TRAM);
        values.put("MA_DVIQLY", MA_DVIQLY);
        values.put("TEN_TRAM", TEN_TRAM);
        values.put("LOAI_TRAM", LOAI_TRAM);
        values.put("CSUAT_TRAM", CSUAT_TRAM);
        values.put("MA_CAP_DA", MA_CAP_DA);
        values.put("MA_CAP_DA_RA", MA_CAP_DA_RA);
        values.put("DINH_DANH", DINH_DANH);
        return database.insert(TthtConstantVariables.TABLE_NAME_TRAM, null, values);
    }

    public long deleteAllDataTRAM() {
        database = this.getWritableDatabase();
        return database.delete(TthtConstantVariables.TABLE_NAME_TRAM, null, null);
    }

    public long deleteDataTRAMWithMA_DVIQLY(String MA_DVIQLY) {
        database = this.getWritableDatabase();
        return database.delete(TthtConstantVariables.TABLE_NAME_TRAM, "MA_DVIQLY=?",
                new String[]{MA_DVIQLY});
    }

    public Cursor getDataTRAM() {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + TthtConstantVariables.TABLE_NAME_TRAM;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c;
        }
        return null;
    }

    public Cursor getInfoTRAMWithMA_TRAM(String MA_TRAM) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + TthtConstantVariables.TABLE_NAME_TRAM + " WHERE MA_TRAM = '" + MA_TRAM + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c;
        }
        return null;
    }

    public long updateDataTram(String MA_TRAM, String MA_DVIQLY, String TEN_TRAM, String LOAI_TRAM, String CSUAT_TRAM, String MA_CAP_DA, String MA_CAP_DA_RA, String DINH_DANH) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MA_TRAM", MA_TRAM);
        values.put("MA_DVIQLY", MA_DVIQLY);
        values.put("TEN_TRAM", TEN_TRAM);
        values.put("LOAI_TRAM", LOAI_TRAM);
        values.put("CSUAT_TRAM", CSUAT_TRAM);
        values.put("MA_CAP_DA", MA_CAP_DA);
        values.put("MA_CAP_DA_RA", MA_CAP_DA_RA);
        values.put("DINH_DANH", DINH_DANH);
        long ins = database.update(TthtConstantVariables.TABLE_NAME_TRAM, values,
                "MA_TRAM=? AND MA_DVIQLY=?", new String[]{MA_TRAM, MA_DVIQLY});
        database.close();
        return ins;
    }

    //endregion

    //region Xử lý bảng nhớ mật khẩu
    public long insertDataRememger(String MA_DVIQLY, String USERNAME, String PASSWORD) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MA_DVIQLY", MA_DVIQLY);
        values.put("USERNAME", USERNAME);
        values.put("PASSWORD", PASSWORD);
        return database.insert(TthtConstantVariables.TABLE_NAME_REMEMBER, null, values);
    }

    public long deleteAllDataRemember() {
        database = this.getWritableDatabase();
        return database.delete(TthtConstantVariables.TABLE_NAME_REMEMBER, null, null);
    }

    public Cursor getDataRemember() {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + TthtConstantVariables.TABLE_NAME_REMEMBER;
        return database.rawQuery(strQuery, null);
    }

    public boolean checkRemember() {
        int count = 0;
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + TthtConstantVariables.TABLE_NAME_REMEMBER;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            count = c.getInt(0);
        }
        if (count > 0)
            return true;
        else
            return false;
    }

    public boolean checkInfoInputCompairWithRememberTable(String maDonViQuanLyChoose, String userInput, String passInput) {
        database = this.getReadableDatabase();
        String queryCheck = "Select * From "
                + TthtConstantVariables.TABLE_NAME_REMEMBER
                + " Where "
                + " MA_DVIQLY ='"
                + maDonViQuanLyChoose
                + "' AND USERNAME = '"
                + userInput
                + "' AND PASSWORD = '"
                + passInput
                + "'";
        Cursor c = database.rawQuery(queryCheck, null);
        int count = c.getCount();
        if (count > 0)
            return true;
        else
            return false;
    }
    //endregion

    //region Xử lý bảng biên bản
    public long insertDataBBanCto(String MA_DVIQLY, int ID_BBAN_TRTH, String MA_DDO, String SO_BBAN, String NGAY_TRTH, String MA_NVIEN,
                                  String MA_LDO, String NGAY_TAO, String NGUOI_TAO, String NGAY_SUA, String NGUOI_SUA, String MA_CNANG,
                                  String MA_YCAU_KNAI, int TRANG_THAI, String GHI_CHU, int Id_BBAN_CONGTO, String LOAI_BBAN,
                                  String TEN_KHANG, String DCHI_HDON, String DTHOAI, String MA_GCS_CTO, String MA_TRAM, String MA_HDONG,
                                  String MA_KHANG, String LY_DO_TREO_THAO) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MA_DVIQLY", MA_DVIQLY);
        values.put("ID_BBAN_TRTH", ID_BBAN_TRTH);
        values.put("MA_DDO", MA_DDO);
        values.put("SO_BBAN", SO_BBAN);
        values.put("NGAY_TRTH", NGAY_TRTH);
        values.put("MA_NVIEN", MA_NVIEN);
        values.put("MA_LDO", MA_LDO);
        values.put("NGAY_TAO", NGAY_TAO);
        values.put("NGUOI_TAO", NGUOI_TAO);
        values.put("NGAY_SUA", NGAY_SUA);
        values.put("NGUOI_SUA", NGUOI_SUA);
        values.put("MA_CNANG", MA_CNANG);
        values.put("MA_YCAU_KNAI", MA_YCAU_KNAI);
        values.put("TRANG_THAI", TRANG_THAI);
        values.put("GHI_CHU", GHI_CHU);
        values.put("ID_BBAN_CONGTO", Id_BBAN_CONGTO);
        values.put("LOAI_BBAN", LOAI_BBAN);
        values.put("TEN_KHANG", TEN_KHANG);
        values.put("DCHI_HDON", DCHI_HDON);
        values.put("DTHOAI", DTHOAI);
        values.put("MA_GCS_CTO", MA_GCS_CTO);
        values.put("MA_TRAM", MA_TRAM);
        values.put("MA_HDONG", MA_HDONG);

        values.put("MA_KHANG", MA_KHANG);
        values.put("LY_DO_TREO_THAO", LY_DO_TREO_THAO);
        long ins = database.insert(TthtConstantVariables.TABLE_NAME_BBAN_CONGTO, null, values);
        database.close();
        return ins;
    }

    public long updateGhiChuBBan(int Id_BBAN_CONGTO, String GHI_CHU) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("GHI_CHU", GHI_CHU);
        long ins = database.update(TthtConstantVariables.TABLE_NAME_BBAN_CONGTO, values,
                "Id_BBAN_CONGTO=?", new String[]{String.valueOf(Id_BBAN_CONGTO)});
        database.close();
        return ins;
    }

    public long updateTinhTrangBBan(int ID_BBAN_CONGTO, int TRANG_THAI) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TRANG_THAI", TRANG_THAI);
        long ins = database.update(TthtConstantVariables.TABLE_NAME_BBAN_CONGTO, values,
                "ID_BBAN_CONGTO=?", new String[]{String.valueOf(ID_BBAN_CONGTO)});
        database.close();
        return ins;
    }


    public int getTinhTrangBBanTThao(int ID_BBAN_TRTH) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT TRANG_THAI FROM " + TthtConstantVariables.TABLE_NAME_BBAN_CONGTO + " WHERE ID_BBAN_TRTH = " + ID_BBAN_TRTH;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;
    }

    public Cursor getAllDataBBanTThao() {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + TthtConstantVariables.TABLE_NAME_BBAN_CONGTO;
        return database.rawQuery(strQuery, null);
    }

    public Cursor getAllMaTramBBanTThao(String MA_DVIQLY, String MA_NVIEN) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT DISTINCT MA_TRAM FROM " + TthtConstantVariables.TABLE_NAME_BBAN_CONGTO + " WHERE MA_DVIQLY = '" + MA_DVIQLY + "' AND MA_NVIEN = '" + MA_NVIEN + "' GROUP BY MA_TRAM";
        Cursor c = database.rawQuery(strQuery, null);
        if (c != null && c.moveToFirst()) {
            return c;
        }
        return null;
    }

    public Cursor getAllBBanTThao(int ID_BBAN_CONGTO) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + TthtConstantVariables.TABLE_NAME_BBAN_CONGTO + " WHERE ID_BBAN_CONGTO = " + ID_BBAN_CONGTO;
        Cursor c = database.rawQuery(strQuery, null);
        if (c != null && c.moveToFirst()) {
            return c;
        }
        return null;
    }


    public int countBBanWithDateSelected(String NGAY_TRTH, String MA_NVIEN, String MA_TRAM) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT count(ID_BBAN_TRTH) FROM " + TthtConstantVariables.TABLE_NAME_BBAN_CONGTO + " where NGAY_TRTH = '" + NGAY_TRTH + "' and MA_NVIEN = '" + MA_NVIEN + "' and MA_TRAM = '" + MA_TRAM + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;

    }

    public int countBBanWithDateSelectedFULLMA_TRAM(String NGAY_TRTH, String MA_NVIEN, String MA_DVIQLY) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT count(ID_BBAN_TRTH) FROM " + TthtConstantVariables.TABLE_NAME_BBAN_CONGTO + " where NGAY_TRTH = '" + NGAY_TRTH + "' and MA_NVIEN = '" + MA_NVIEN + "' and MA_DVIQLY = '" + MA_DVIQLY + "'";
        Cursor c = database.rawQuery(strQuery, null);
        int count = 0;
        if (c.moveToFirst()) {
            count = c.getInt(0);
        }
        return count;

    }

    public Cursor getBBanByIDBBanTRTH(int ID_BBAN_TRTH) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + TthtConstantVariables.TABLE_NAME_BBAN_CONGTO + " WHERE ID_BBAN_TRTH = " + ID_BBAN_TRTH;
        Cursor c = database.rawQuery(strQuery, null);
        if (c != null && c.moveToFirst()) {
            return c;
        }
        return null;

    }


    public long deleteDataBBanByMaDviAndID(String MA_DVIQLY, int ID_BBAN_TRTH) {
        database = this.getWritableDatabase();
        return database.delete(TthtConstantVariables.TABLE_NAME_BBAN_CONGTO, "MA_DVIQLY=? and ID_BBAN_TRTH=?",
                new String[]{MA_DVIQLY, String.valueOf(ID_BBAN_TRTH)});
    }

    public long updateDataBBanByMaDviAndID(String MA_DVIQLY, int ID_BBAN_TRTH, String MA_DDO, String SO_BBAN, String NGAY_TRTH, String MA_NVIEN,
                                           String MA_LDO, String NGAY_TAO, String NGUOI_TAO, String NGAY_SUA, String NGUOI_SUA, String MA_CNANG,
                                           String MA_YCAU_KNAI, int TRANG_THAI, String GHI_CHU, int Id_BBAN_CONGTO, String LOAI_BBAN,
                                           String TEN_KHANG, String DCHI_HDON, String DTHOAI, String MA_GCS_CTO, String MA_TRAM, String MA_HDONG,
                                           String MA_KHANG, String LY_DO_TREO_THAO) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MA_DVIQLY", MA_DVIQLY);
        values.put("ID_BBAN_TRTH", ID_BBAN_TRTH);
        values.put("MA_DDO", MA_DDO);
        values.put("SO_BBAN", SO_BBAN);
        values.put("NGAY_TRTH", NGAY_TRTH);
        values.put("MA_NVIEN", MA_NVIEN);
        values.put("MA_LDO", MA_LDO);
        values.put("NGAY_TAO", NGAY_TAO);
        values.put("NGUOI_TAO", NGUOI_TAO);
        values.put("NGAY_SUA", NGAY_SUA);
        values.put("NGUOI_SUA", NGUOI_SUA);
        values.put("MA_CNANG", MA_CNANG);
        values.put("MA_YCAU_KNAI", MA_YCAU_KNAI);
        values.put("TRANG_THAI", TRANG_THAI);
        values.put("GHI_CHU", GHI_CHU);
        values.put("ID_BBAN_CONGTO", Id_BBAN_CONGTO);
        values.put("LOAI_BBAN", LOAI_BBAN);
        values.put("TEN_KHANG", TEN_KHANG);
        values.put("DCHI_HDON", DCHI_HDON);
        values.put("DTHOAI", DTHOAI);
        values.put("MA_GCS_CTO", MA_GCS_CTO);
        values.put("MA_TRAM", MA_TRAM);
        values.put("MA_HDONG", MA_HDONG);
        values.put("MA_KHANG", MA_KHANG);
        values.put("LY_DO_TREO_THAO", LY_DO_TREO_THAO);
        long ins = database.update(TthtConstantVariables.TABLE_NAME_BBAN_CONGTO, values,
                "MA_DVIQLY=? and ID_BBAN_CONGTO=? and MA_NVIEN =?", new String[]{MA_DVIQLY, String.valueOf(Id_BBAN_CONGTO), TthtCommon.getMaNvien()});
        database.close();
        return ins;
    }


    public int getCountBBanByDvi(String MA_DVIQLY) {
        database = this.getReadableDatabase();
        StringBuilder strQuery = new StringBuilder("SELECT COUNT(*) FROM ");
        strQuery.append(TthtConstantVariables.TABLE_NAME_BBAN_CONGTO);
        strQuery.append(" WHERE MA_DVIQLY = '");
        strQuery.append(MA_DVIQLY);
        strQuery.append("'");
        Cursor c = database.rawQuery(strQuery.toString(), null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;
    }

    public int deleteAllBBan() {
        SQLiteDatabase db = getReadableDatabase();
        int rowEffect = db.delete(TthtConstantVariables.TABLE_NAME_BBAN_CONGTO, null, null);
        db.close();
        return rowEffect;
    }

    //endregion

    //region Xử lý bảng chi tiết thông tin công tơ
    public long insertDataTTinCto(String MA_DVIQLY, int ID_BBAN_TRTH, String MA_CTO, String SO_CTO, int LAN, String MA_BDONG,
                                  String NGAY_BDONG, String MA_CLOAI, String LOAI_CTO, int VTRI_TREO, String MO_TA_VTRI_TREO, String MA_SOCBOOC, int SO_VIENCBOOC, int LOAI_HOM,
                                  String MA_SOCHOM, int SO_VIENCHOM, int HS_NHAN, String NGAY_TAO, String NGUOI_TAO, String NGAY_SUA,
                                  String NGUOI_SUA, String MA_CNANG, String SO_TU, String SO_TI, String SO_COT, String SO_HOM, String CHI_SO,
                                  String NGAY_KDINH, String NAM_SX, String TEM_CQUANG, String MA_CHIKDINH, String MA_TEM, int SOVIEN_CHIKDINH,
                                  String DIEN_AP, String DONG_DIEN, String HANGSO_K, String MA_NUOC, String TEN_NUOC, int ID_CHITIET_CTO,
                                  String SO_KIM_NIEM_CHI, String TTRANG_NPHONG, String TEN_LOAI_CTO, String PHUONG_THUC_DO_XA, String GHI_CHU, int TRANG_THAI_DU_LIEU, int ID_BBAN_TUTI) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MA_DVIQLY", MA_DVIQLY);
        values.put("MA_NVIEN", TthtCommon.getMaNvien());
        values.put("ID_BBAN_TRTH", ID_BBAN_TRTH);
        values.put("MA_CTO", MA_CTO);
        values.put("SO_CTO", SO_CTO);
        values.put("LAN", LAN);
        values.put("MA_BDONG", MA_BDONG);
        values.put("NGAY_BDONG", NGAY_BDONG);
        values.put("MA_CLOAI", MA_CLOAI);
        values.put("LOAI_CTO", LOAI_CTO);
        values.put("VTRI_TREO", VTRI_TREO);
        values.put("MO_TA_VTRI_TREO", MO_TA_VTRI_TREO);
        values.put("MA_SOCBOOC", MA_SOCBOOC);
        values.put("SO_VIENCBOOC", SO_VIENCBOOC);
        values.put("LOAI_HOM", LOAI_HOM);
        values.put("MA_SOCHOM", MA_SOCHOM);
        values.put("SO_VIENCHOM", SO_VIENCHOM);
        values.put("HS_NHAN", HS_NHAN);
        values.put("NGAY_TAO", NGAY_TAO);
        values.put("NGUOI_TAO", NGUOI_TAO);
        values.put("NGAY_SUA", NGAY_SUA);
        values.put("NGUOI_SUA", NGUOI_SUA);
        values.put("MA_CNANG", MA_CNANG);
        values.put("SO_TU", SO_TU);
        values.put("SO_TI", SO_TI);
        values.put("SO_COT", SO_COT);
        values.put("SO_HOM", SO_HOM);
        values.put("CHI_SO", CHI_SO);
        values.put("NGAY_KDINH", NGAY_KDINH);
        values.put("NAM_SX", NAM_SX);
        values.put("TEM_CQUANG", TEM_CQUANG);
        values.put("MA_CHIKDINH", MA_CHIKDINH);
        values.put("MA_TEM", MA_TEM);
        values.put("SOVIEN_CHIKDINH", SOVIEN_CHIKDINH);
        values.put("DIEN_AP", DIEN_AP);
        values.put("DONG_DIEN", DONG_DIEN);
        values.put("HANGSO_K", HANGSO_K);
        values.put("MA_NUOC", MA_NUOC);
        values.put("TEN_NUOC", TEN_NUOC);
        values.put("ID_CHITIET_CTO", ID_CHITIET_CTO);
        values.put("SO_KIM_NIEM_CHI", SO_KIM_NIEM_CHI);
        values.put("TTRANG_NPHONG", TTRANG_NPHONG);
        values.put("TEN_LOAI_CTO", TEN_LOAI_CTO);
        values.put("PHUONG_THUC_DO_XA", PHUONG_THUC_DO_XA);
        values.put("GHI_CHU", GHI_CHU);
        values.put("TRANG_THAI_DU_LIEU", TRANG_THAI_DU_LIEU);
        values.put("ID_BBAN_TUTI", ID_BBAN_TUTI);
        long ins = database.insert(TthtConstantVariables.TABLE_NAME_DETAIL_CONGTO, null, values);
        database.close();
        return ins;
    }

    public long updateCongTo(String MA_BDONG, String ID_BBAN_TRTH, String CHI_SO, String TEM_CQUANG, String LAN, String SO_KIM_NIEM_CHI, String MA_CHIKDINH, String MA_SOCHOM, String MA_SOCBOOC,
                             String SOVIEN_CHIKDINH, String SO_VIENCHOM, String SO_VIENCBOOC, String TTRANG_NPHONG, String LOAI_HOM,
                             String PHUONG_THUC_DO_XA, String GHI_CHU, String TRANG_THAI_DU_LIEU) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("CHI_SO", CHI_SO);
        values.put("TEM_CQUANG", TEM_CQUANG);
        values.put("LAN", LAN);
        values.put("SO_KIM_NIEM_CHI", SO_KIM_NIEM_CHI);
        values.put("MA_CHIKDINH", MA_CHIKDINH);
        values.put("MA_SOCHOM", MA_SOCHOM);
        values.put("MA_SOCBOOC", MA_SOCBOOC);
        values.put("SO_VIENCHOM", SO_VIENCHOM);
        values.put("SOVIEN_CHIKDINH", SOVIEN_CHIKDINH);
        values.put("SO_VIENCHOM", SO_VIENCHOM);
        values.put("SO_VIENCBOOC", SO_VIENCBOOC);
        values.put("TTRANG_NPHONG", TTRANG_NPHONG);
        values.put("LOAI_HOM", LOAI_HOM);
        values.put("PHUONG_THUC_DO_XA", PHUONG_THUC_DO_XA);
        values.put("GHI_CHU", GHI_CHU);
        values.put("TRANG_THAI_DU_LIEU", TRANG_THAI_DU_LIEU);

        long ins = database.update(TthtConstantVariables.TABLE_NAME_DETAIL_CONGTO, values,
                "MA_BDONG=? and ID_BBAN_TRTH=?", new String[]{MA_BDONG, String.valueOf(ID_BBAN_TRTH)});
        database.close();
        return ins;
    }

    public long updateCongToSauLap(String MA_BDONG, String ID_BBAN_TRTH, String CHI_SO_SAULAP_TUTI,
                                   String DONG_DIEN_SAULAP_TUTI, String DIEN_AP_SAULAP_TUTI,
                                   String HANGSO_K_SAULAP_TUTI, String CAP_CX_SAULAP_TUTI,
                                   String SO_TU_SAULAP_TUTI, String SO_TI_SAULAP_TUTI,
                                   String HS_NHAN_SAULAP_TUTI, String TRANG_THAI_DU_LIEU) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("HS_NHAN_SAULAP_TUTI", HS_NHAN_SAULAP_TUTI);
        values.put("DONG_DIEN_SAULAP_TUTI", DONG_DIEN_SAULAP_TUTI);
        values.put("DIEN_AP_SAULAP_TUTI", DIEN_AP_SAULAP_TUTI);

        values.put("HANGSO_K_SAULAP_TUTI", HANGSO_K_SAULAP_TUTI);
        values.put("CAP_CX_SAULAP_TUTI", CAP_CX_SAULAP_TUTI);
        values.put("SO_TU_SAULAP_TUTI", SO_TU_SAULAP_TUTI);
        values.put("SO_TI_SAULAP_TUTI", SO_TI_SAULAP_TUTI);
        values.put("CHI_SO_SAULAP_TUTI", CHI_SO_SAULAP_TUTI);
        values.put("TRANG_THAI_DU_LIEU", TRANG_THAI_DU_LIEU);

        long ins = database.update(TthtConstantVariables.TABLE_NAME_DETAIL_CONGTO, values,
                "MA_BDONG=? and ID_BBAN_TRTH=?", new String[]{MA_BDONG, String.valueOf(ID_BBAN_TRTH)});
        database.close();
        return ins;
    }


    public long updateCongToWhenSubmit(int ID_BBAN_TRTH, String MA_BDONG, String TRANG_THAI_DU_LIEU) {
        database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("TRANG_THAI_DU_LIEU", TRANG_THAI_DU_LIEU);
        long countRowUpdated = database.update(TthtConstantVariables.TABLE_NAME_DETAIL_CONGTO, values,
                "ID_BBAN_TRTH=? AND MA_BDONG=? ", new String[]{String.valueOf(ID_BBAN_TRTH), MA_BDONG});
        return countRowUpdated;
    }

    public long updateChiSoTTinCtoWhenDownload(int ID_BBAN_TRTH,
                                               String MA_DVIQLY, String SO_KIM_NIEM_CHI,
                                               String MA_SOCHOM, String MA_CHIKDINH,
                                               String MA_SOCBOOC, int HS_NHAN,
                                               int LOAI_HOM, String PHUONG_THUC_DO_XA,
                                               int TRANG_THAI_DU_LIEU, String MA_BDONG, int ID_BBAN_TUTI) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("SO_KIM_NIEM_CHI", SO_KIM_NIEM_CHI);
        values.put("MA_SOCHOM", MA_SOCHOM);
        values.put("MA_CHIKDINH", MA_CHIKDINH);
        values.put("MA_SOCBOOC", MA_SOCBOOC);
        values.put("HS_NHAN", HS_NHAN);
        values.put("LOAI_HOM", LOAI_HOM);
        values.put("PHUONG_THUC_DO_XA", PHUONG_THUC_DO_XA);
        values.put("TRANG_THAI_DU_LIEU", TRANG_THAI_DU_LIEU);
        values.put("ID_BBAN_TUTI", ID_BBAN_TUTI);

        long ins = database.update(TthtConstantVariables.TABLE_NAME_DETAIL_CONGTO, values,
                "MA_DVIQLY=? and ID_BBAN_TRTH=? and MA_NVIEN=? and MA_BDONG=?", new String[]{MA_DVIQLY, String.valueOf(ID_BBAN_TRTH), TthtCommon.getMaNvien(), MA_BDONG});
        database.close();
        return ins;
    }

    public Cursor getAllDataTTinTThao() {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + TthtConstantVariables.TABLE_NAME_DETAIL_CONGTO;
        return database.rawQuery(strQuery, null);
    }

    public Cursor getCToByMaCto(String MA_CTO) {
        SQLiteDatabase db = getReadableDatabase();
        String strQuery = "SELECT * FROM " + TthtConstantVariables.TABLE_NAME_DETAIL_CONGTO + " WHERE MA_CTO = '" + MA_CTO + "'";

        Cursor cursor = db.rawQuery(strQuery, null);
        int totalRows = cursor.getCount();

        if (cursor != null && totalRows != 0 && cursor.moveToFirst()) {
            return cursor;
        }
        return null;
    }

    public Cursor getCToByIDBBan(int ID_BBAN_TRTH) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + TthtConstantVariables.TABLE_NAME_DETAIL_CONGTO + " WHERE ID_BBAN_TRTH = " + ID_BBAN_TRTH;
        return database.rawQuery(strQuery, null);
    }

    public Cursor getCToByIDBBanTuTi(int ID_BBAN_TUTI) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + TthtConstantVariables.TABLE_NAME_DETAIL_CONGTO + " WHERE ID_BBAN_TUTI = " + ID_BBAN_TUTI;
        Cursor cursor = database.rawQuery(strQuery, null);
        if (cursor != null && cursor.moveToFirst())
            return cursor;
        return null;
    }

    public Cursor getCToByIDBBanWithMaBDong(int ID_BBAN_TRTH, String MA_BDONG) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + TthtConstantVariables.TABLE_NAME_DETAIL_CONGTO + " WHERE ID_BBAN_TRTH = " + ID_BBAN_TRTH + " AND MA_BDONG = '" + MA_BDONG + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if (c != null && c.moveToFirst()) {
            return c;
        }
        return null;
    }

    public long deleteDataCToByMaDviAndID(String MA_DVIQLY, int ID_BBAN_TRTH) {
        database = this.getWritableDatabase();
        return database.delete(TthtConstantVariables.TABLE_NAME_DETAIL_CONGTO, "MA_DVIQLY=? and ID_BBAN_TRTH=?",
                new String[]{MA_DVIQLY, String.valueOf(ID_BBAN_TRTH)});
    }

    public int deleteAllDataCTo() {
        SQLiteDatabase db = getReadableDatabase();
        int rowEffect = db.delete(TthtConstantVariables.TABLE_NAME_DETAIL_CONGTO, null, null);
        db.close();
        return rowEffect;
    }

    public int getCountCToByDvi(String MA_DVIQLY, String MA_BDONG) {
        database = this.getReadableDatabase();
        StringBuilder strQuery = new StringBuilder("SELECT COUNT(*) FROM ");
        strQuery.append(TthtConstantVariables.TABLE_NAME_DETAIL_CONGTO);
        strQuery.append(" WHERE MA_DVIQLY = '");
        strQuery.append(MA_DVIQLY);
        strQuery.append("' AND MA_BDONG = '");
        strQuery.append(MA_BDONG);
        strQuery.append("'");
        Cursor c = database.rawQuery(strQuery.toString(), null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;
    }

    public int getCountCToByDviWithMaTram(String MA_DVIQLY, String MA_BDONG, String MA_TRAM, String NGAY_TRTH) {
        database = this.getReadableDatabase();
        StringBuilder strQuery = new StringBuilder();

        strQuery.append("SELECT DISTINCT * FROM (SELECT * FROM (SELECT * FROM ");
        strQuery.append(TthtConstantVariables.TABLE_NAME_DETAIL_CONGTO);
        strQuery.append(" WHERE MA_DVIQLY = '").append(MA_DVIQLY).append("')");
        strQuery.append(" D INNER JOIN ");
        strQuery.append(TthtConstantVariables.TABLE_NAME_BBAN_CONGTO);
        strQuery.append(" B ON D.ID_BBAN_TRTH = B.ID_BBAN_TRTH) WHERE MA_BDONG = '");
        strQuery.append(MA_BDONG);
        strQuery.append("' AND NGAY_TRTH = '");
        strQuery.append(NGAY_TRTH);
        strQuery.append("'");
        strQuery.append(" AND MA_NVIEN = '");
        strQuery.append(TthtCommon.getMaNvien());
        strQuery.append("'");
        strQuery.append(" AND MA_TRAM LIKE '%");
        strQuery.append(MA_TRAM);
        strQuery.append("%'");

        Cursor c = database.rawQuery(strQuery.toString(), null);
        return c.getCount();
    }

    public int getCountCToByDviFullMaTram(String MA_DVIQLY, String MA_BDONG, String NGAY_TRTH) {
        database = this.getReadableDatabase();
        StringBuilder strQuery = new StringBuilder();

        strQuery.append("SELECT DISTINCT * FROM (SELECT * FROM (SELECT * FROM ");
        strQuery.append(TthtConstantVariables.TABLE_NAME_DETAIL_CONGTO);
        strQuery.append(" WHERE MA_DVIQLY = '").append(MA_DVIQLY).append("')");
        strQuery.append(" D INNER JOIN ");
        strQuery.append(TthtConstantVariables.TABLE_NAME_BBAN_CONGTO);
        strQuery.append(" B ON D.ID_BBAN_TRTH = B.ID_BBAN_TRTH) WHERE MA_BDONG = '");
        strQuery.append(MA_BDONG);
        strQuery.append("' AND NGAY_TRTH = '");
        strQuery.append(NGAY_TRTH);
        strQuery.append("'");
        strQuery.append(" AND MA_NVIEN = '");
        strQuery.append(TthtCommon.getMaNvien());
        strQuery.append("'");

        Cursor c = database.rawQuery(strQuery.toString(), null);
        if (c.moveToFirst()) {
            return c.getCount();
        }
        return 0;
    }

    public int getCountCToDaGhiByDviWithMaTram(String MA_DVIQLY, String MA_BDONG, String MA_NVIEN, String MA_TRAM, String NGAY_TRTH) {
        database = this.getReadableDatabase();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("SELECT DISTINCT * FROM (SELECT * FROM (SELECT * FROM ");
        strQuery.append(TthtConstantVariables.TABLE_NAME_DETAIL_CONGTO);
        strQuery.append(" WHERE MA_DVIQLY = '").append(MA_DVIQLY).append("')");
        strQuery.append(" D INNER JOIN ");
        strQuery.append(TthtConstantVariables.TABLE_NAME_BBAN_CONGTO);
        strQuery.append(" B ON D.ID_BBAN_TRTH = B.ID_BBAN_TRTH) WHERE MA_BDONG = '");
        strQuery.append(MA_BDONG);
        strQuery.append("' AND NGAY_TRTH = '");
        strQuery.append(NGAY_TRTH);
        strQuery.append("'");
        strQuery.append(" AND MA_NVIEN = '");
        strQuery.append(MA_NVIEN);
        strQuery.append("' AND TRANG_THAI_DU_LIEU = 1 AND MA_BDONG = '");
        strQuery.append(MA_BDONG).append("'");
        strQuery.append(" AND MA_TRAM LIKE '%");
        strQuery.append(MA_TRAM).append("%'");

        Cursor c = database.rawQuery(strQuery.toString(), null);
        return c.getCount();
    }


    public int getCountCToDaGhiByDviFullMaTram(String MA_DVIQLY, String MA_BDONG, int typeDataSet, String MA_NVIEN, String NGAY_TRTH) {
        database = this.getReadableDatabase();
        StringBuilder strQuery = new StringBuilder();
        if (typeDataSet == 1) {
            strQuery = new StringBuilder("SELECT COUNT(*) FROM ");
            strQuery.append(TthtConstantVariables.TABLE_NAME_DETAIL_CONGTO);
            strQuery.append(" WHERE MA_DVIQLY = '");
            strQuery.append(MA_DVIQLY);
            strQuery.append("' AND MA_BDONG = '");
            strQuery.append(MA_BDONG);
            strQuery.append("' AND CHI_SO <> ''");
        }
        if (typeDataSet == 2) {
            strQuery.append("select count(bb.ID_BBAN_TRTH) as SO_CTO_DA_GHI from( select * from bban_congto where ma_nvien = '");
            strQuery.append(MA_NVIEN);
            strQuery.append("') bb inner join ( select ID_BBAN_TRTH,MA_DVIQLY , count(ID_BBAN_TRTH) from ( select * from detail_congto where trang_thai_du_lieu = '1' ) group by ID_BBAN_TRTH, MA_DVIQLY having count(ID_BBAN_TRTH) = 2 ) temp on bb.MA_DVIQLY = temp.MA_DVIQLY and bb.ID_BBAN_TRTH = temp.ID_BBAN_TRTH");
        }
        if (typeDataSet == 3) {

            strQuery.append("SELECT DISTINCT * FROM (SELECT * FROM (SELECT * FROM ");
            strQuery.append(TthtConstantVariables.TABLE_NAME_DETAIL_CONGTO);
            strQuery.append(" WHERE MA_DVIQLY = '").append(MA_DVIQLY).append("')");
            strQuery.append(" D INNER JOIN ");
            strQuery.append(TthtConstantVariables.TABLE_NAME_BBAN_CONGTO);
            strQuery.append(" B ON D.ID_BBAN_TRTH = B.ID_BBAN_TRTH) WHERE MA_BDONG = '");
            strQuery.append(MA_BDONG);
            strQuery.append("' AND NGAY_TRTH = '");
            strQuery.append(NGAY_TRTH);
            strQuery.append("'");
            strQuery.append(" AND MA_NVIEN = '");
            strQuery.append(TthtCommon.getMaNvien());
            strQuery.append("' AND TRANG_THAI_DU_LIEU = 1 AND MA_BDONG = '");
            strQuery.append(MA_BDONG).append("'");
        }

        Cursor c = database.rawQuery(strQuery.toString(), null);
        if (c.moveToFirst()) {
            return c.getCount();
        }
        return 0;
    }


    public int getCountCToDaGuiByDviWithMaTram(String MA_DVIQLY, String MA_NVIEN, String MA_TRAM, String MA_BDONG, String NGAY_TRTH) {
        database = this.getReadableDatabase();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("SELECT DISTINCT * FROM (SELECT * FROM (SELECT * FROM ");
        strQuery.append(TthtConstantVariables.TABLE_NAME_DETAIL_CONGTO);
        strQuery.append(" WHERE MA_DVIQLY = '").append(MA_DVIQLY).append("')");
        strQuery.append(" D INNER JOIN ");
        strQuery.append(TthtConstantVariables.TABLE_NAME_BBAN_CONGTO);
        strQuery.append(" B ON D.ID_BBAN_TRTH = B.ID_BBAN_TRTH) WHERE MA_BDONG = '");
        strQuery.append(MA_BDONG);
        strQuery.append("' AND NGAY_TRTH = '");
        strQuery.append(NGAY_TRTH);
        strQuery.append("'");
        strQuery.append(" AND MA_NVIEN = '");
        strQuery.append(MA_NVIEN);
        strQuery.append("' AND TRANG_THAI_DU_LIEU = 2 AND MA_BDONG = '");
        strQuery.append(MA_BDONG).append("'");
        strQuery.append(" AND MA_TRAM = '%");
        strQuery.append(MA_TRAM).append("%'");

        Cursor c = database.rawQuery(strQuery.toString(), null);
        return c.getCount();
    }


    public int getCountCToDaGuiByDviFullMaTram(String MA_DVIQLY, String MA_NVIEN, String MA_BDONG, int typeDataSet, String NGAY_TRTH) {
        database = this.getReadableDatabase();
        StringBuilder strQuery = new StringBuilder();
        if (typeDataSet == 3) {
            strQuery.append("SELECT DISTINCT * FROM (SELECT * FROM (SELECT * FROM ");
            strQuery.append(TthtConstantVariables.TABLE_NAME_DETAIL_CONGTO);
            strQuery.append(" WHERE MA_DVIQLY = '").append(MA_DVIQLY).append("')");
            strQuery.append(" D INNER JOIN ");
            strQuery.append(TthtConstantVariables.TABLE_NAME_BBAN_CONGTO);
            strQuery.append(" B ON D.ID_BBAN_TRTH = B.ID_BBAN_TRTH) WHERE MA_BDONG = '");
            strQuery.append(MA_BDONG);
            strQuery.append("' AND NGAY_TRTH = '");
            strQuery.append(NGAY_TRTH);
            strQuery.append("'");
            strQuery.append(" AND MA_NVIEN = '");
            strQuery.append(MA_NVIEN);
            strQuery.append("' AND TRANG_THAI_DU_LIEU = 2 AND MA_BDONG = '");
            strQuery.append(MA_BDONG).append("'");
        } else {

            strQuery.append("SELECT * FROM ");
            strQuery.append(TthtConstantVariables.TABLE_NAME_BBAN_CONGTO);
            strQuery.append(" WHERE MA_DVIQLY = '");
            strQuery.append(MA_DVIQLY);
            strQuery.append("' AND TRANG_THAI = ");
            strQuery.append(String.valueOf(2));
        }
        Cursor c = database.rawQuery(strQuery.toString(), null);
        if (c.moveToFirst()) {
            return c.getCount();
        }
        return 0;
    }


    //endregion

    //region Xử lý bảng TI
    public long insertDataTI(String MA_DVIQLY, int ID_BBAN_TRTH, String MA_TI, String SO_TI, int LAN, String MA_BDONG, String NGAY_BDONG,
                             String MA_CLOAI, String TYSO_DAU, String NGAY_TAO, String NGUOI_TAO, String NGAY_SUA, String NGUOI_SUA,
                             String MA_CNANG, int ID_CONGTO_TI) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MA_DVIQLY", MA_DVIQLY);
        values.put("ID_BBAN_TRTH", ID_BBAN_TRTH);
        values.put("MA_TI", MA_TI);
        values.put("SO_TI", SO_TI);
        values.put("LAN", LAN);
        values.put("MA_BDONG", MA_BDONG);
        values.put("NGAY_BDONG", NGAY_BDONG);
        values.put("MA_CLOAI", MA_CLOAI);
        values.put("TYSO_DAU", TYSO_DAU);
        values.put("NGAY_TAO", NGAY_TAO);
        values.put("NGUOI_TAO", NGUOI_TAO);
        values.put("NGAY_SUA", NGAY_SUA);
        values.put("NGUOI_SUA", NGUOI_SUA);
        values.put("MA_CNANG", MA_CNANG);
        values.put("ID_CONGTO_TI", ID_CONGTO_TI);
        long ins = database.insert(TthtConstantVariables.TABLE_NAME_CONGTO_TI, null, values);
        database.close();
        return ins;
    }

    public Cursor getAllDataTI() {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + TthtConstantVariables.TABLE_NAME_CONGTO_TI;
        return database.rawQuery(strQuery, null);
    }

    public long deleteDataTIByMaDviAndID(String MA_DVIQLY, int ID_BBAN_TRTH) {
        database = this.getWritableDatabase();
        return database.delete(TthtConstantVariables.TABLE_NAME_CONGTO_TI, "MA_DVIQLY=? and ID_BBAN_TRTH=?",
                new String[]{MA_DVIQLY, String.valueOf(ID_BBAN_TRTH)});
    }

    public int getCountTIByDvi(String MA_DVIQLY) {
        database = this.getReadableDatabase();
        StringBuilder strQuery = new StringBuilder("SELECT COUNT(*) FROM ");
        strQuery.append(TthtConstantVariables.TABLE_NAME_CONGTO_TI);
        strQuery.append(" WHERE MA_DVIQLY = '");
        strQuery.append(MA_DVIQLY);
        strQuery.append("'");
        Cursor c = database.rawQuery(strQuery.toString(), null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;
    }
    //endregion

    //region Xử lý bảng TU
    public long insertDataTU(String MA_DVIQLY, int ID_BBAN_TRTH, String MA_TU, String SO_TU, int LAN, String MA_BDONG, String NGAY_BDONG,
                             String MA_CLOAI, String TYSO_DAU, String NGAY_TAO, String NGUOI_TAO, String NGAY_SUA, String NGUOI_SUA,
                             String MA_CNANG, int ID_CONGTO_TU) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MA_DVIQLY", MA_DVIQLY);
        values.put("ID_BBAN_TRTH", ID_BBAN_TRTH);
        values.put("MA_TU", MA_TU);
        values.put("SO_TU", SO_TU);
        values.put("LAN", LAN);
        values.put("MA_BDONG", MA_BDONG);
        values.put("NGAY_BDONG", NGAY_BDONG);
        values.put("MA_CLOAI", MA_CLOAI);
        values.put("TYSO_DAU", TYSO_DAU);
        values.put("NGAY_TAO", NGAY_TAO);
        values.put("NGUOI_TAO", NGUOI_TAO);
        values.put("NGAY_SUA", NGAY_SUA);
        values.put("NGUOI_SUA", NGUOI_SUA);
        values.put("MA_CNANG", MA_CNANG);
        values.put("ID_CONGTO_TU", ID_CONGTO_TU);
        long ins = database.insert(TthtConstantVariables.TABLE_NAME_CONGTO_TU, null, values);
        database.close();
        return ins;
    }

    public Cursor getAllDataTU() {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + TthtConstantVariables.TABLE_NAME_CONGTO_TU;
        return database.rawQuery(strQuery, null);
    }

    public long deleteDataTUByMaDviAndID(String MA_DVIQLY, int ID_BBAN_TRTH) {
        database = this.getWritableDatabase();
        return database.delete(TthtConstantVariables.TABLE_NAME_CONGTO_TU, "MA_DVIQLY=? and ID_BBAN_TRTH=?",
                new String[]{MA_DVIQLY, String.valueOf(ID_BBAN_TRTH)});
    }

    public int getCountTUByDvi(String MA_DVIQLY) {
        database = this.getReadableDatabase();
        StringBuilder strQuery = new StringBuilder("SELECT COUNT(*) FROM ");
        strQuery.append(TthtConstantVariables.TABLE_NAME_CONGTO_TU);
        strQuery.append(" WHERE MA_DVIQLY = '");
        strQuery.append(MA_DVIQLY);
        strQuery.append("'");
        Cursor c = database.rawQuery(strQuery.toString(), null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;
    }
    //endregion

    //region Xử lý bảng ảnh
    public Cursor checkAnh(int ID_CHITIET_TUTI, String MA_DVIQLY, int ID_BBAN_TUTI, int ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE TYPE_IMAGE) {
        database = this.getReadableDatabase();
        StringBuilder strQuery = new StringBuilder("SELECT * FROM ").append(TthtConstantVariables.TABLE_NAME_ANH)
                .append(" WHERE")
                .append(" ID_CHITIET_TUTI = ")
                .append(ID_CHITIET_TUTI)
                .append(" AND")
                .append(" MA_DVIQLY = ")
                .append("'").append(MA_DVIQLY).append("'")
                .append(" AND")
                .append(" ID_BBAN_TUTI = ")
                .append(ID_BBAN_TUTI)
                .append(" AND")
                .append(" ID_CHITIET_CTO = ")
                .append(ID_CHITIET_CTO)
                .append(" AND")
                .append(" TYPE = ")
                .append("'").append(TYPE_IMAGE.toString()).append("'");
        Cursor cursor = database.rawQuery(strQuery.toString(), null);
        if (cursor.moveToFirst())
            return cursor;
        else return null;
    }

    public long insertDataAnh(int ID_CHITIET_TUTI, String TEN_ANH, String MA_DVIQLY, int ID_BBAN_TUTI, int ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE TYPE_IMAGE, String CREATE_DAY) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MA_DVIQLY", MA_DVIQLY);
        values.put("TEN_ANH", TEN_ANH);
        values.put("TYPE", TYPE_IMAGE.toString());
        values.put("CREATE_DAY", CREATE_DAY);
        values.put("ID_CHITIET_CTO", ID_CHITIET_CTO);
        values.put("ID_CHITIET_TUTI", ID_CHITIET_TUTI);
        values.put("ID_BBAN_TUTI", ID_BBAN_TUTI);

        long ins = database.insert(TthtConstantVariables.TABLE_NAME_ANH, null, values);
        database.close();
        return ins;
    }

    public long updateDataAnh(int ID_CHITIET_TUTI, String TEN_ANH, String MA_DVIQLY, int ID_BBAN_TUTI, int ID_CHITIET_CTO, int TYPE, String CREATE_DAY) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ID_CHITIET_TUTI", ID_CHITIET_TUTI);
        values.put("TEN_ANH", TEN_ANH);
        values.put("MA_DVIQLY", MA_DVIQLY);
        values.put("ID_BBAN_TUTI", ID_BBAN_TUTI);
        values.put("ID_CHITIET_CTO", TYPE);
        values.put("TYPE", ID_CHITIET_CTO);
        values.put("CREATE_DAY", CREATE_DAY);
        long ins = database.update(TthtConstantVariables.TABLE_NAME_ANH, values,
                "ID_CHITIET_CTO=? AND TYPE=?", new String[]{String.valueOf(ID_CHITIET_CTO), String.valueOf(TYPE)});
        database.close();
        return ins;
    }

    public Cursor getAllDataAnh() {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + TthtConstantVariables.TABLE_NAME_ANH;
        return database.rawQuery(strQuery, null);
    }

    public Cursor getDataAnhByIDCto(int ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE TYPE) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + TthtConstantVariables.TABLE_NAME_ANH + " WHERE ID_CHITIET_CTO = " + ID_CHITIET_CTO + " AND TYPE = " + TYPE.toString();
        Cursor c = database.rawQuery(strQuery, null);
        return c;
    }

    public long deleteALLAnh() {
        database = this.getWritableDatabase();
        return database.delete(
                TthtConstantVariables.TABLE_NAME_ANH,
                null,
                null);
    }

    public long deleteAnh(int ID_CHITIET_TUTI, String MA_DVIQLY, int ID_BBAN_TUTI, int ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE TYPE_IMAGE) {
        database = this.getWritableDatabase();
        return database.delete(
                TthtConstantVariables.TABLE_NAME_ANH,
                "ID_CHITIET_TUTI=? and MA_DVIQLY=? and ID_BBAN_TUTI=? and ID_CHITIET_CTO=? and TYPE=?",
                new String[]{String.valueOf(ID_CHITIET_TUTI), MA_DVIQLY, String.valueOf(ID_BBAN_TUTI), String.valueOf(ID_CHITIET_CTO), TYPE_IMAGE.toString()});
    }
    //endregion

    //region Xử lý bảng loại Công tơ
    public Cursor getLoaiCongTo(String TEN_LOAI_CTO) {
        TthtEntityLoaiCongTo loaiCongTo = null;
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + TthtConstantVariables.TABLE_NAME_LOAI_CONGTO + " WHERE TEN_LOAI_CTO = '" + TEN_LOAI_CTO + "'";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst())
            return cursor;
        return null;
    }

    public List<TthtEntityLoaiCongTo> getAllLoaiCongTo() {
        SQLiteDatabase db = getReadableDatabase();
        List<TthtEntityLoaiCongTo> loaiCongToList = new ArrayList<TthtEntityLoaiCongTo>();
        String sql = "SELECT * FROM " + TthtConstantVariables.TABLE_NAME_LOAI_CONGTO;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                TthtEntityLoaiCongTo loaiCongTo = new TthtEntityLoaiCongTo(
                        cursor.getString(cursor.getColumnIndex("MA_CLOAI")),
                        cursor.getString(cursor.getColumnIndex("LOAI_CTO")),
                        cursor.getString(cursor.getColumnIndex("MO_TA")),
                        cursor.getString(cursor.getColumnIndex("SO_PHA")),
                        cursor.getString(cursor.getColumnIndex("SO_DAY")),
                        cursor.getInt(cursor.getColumnIndex("HS_NHAN")),
                        cursor.getString(cursor.getColumnIndex("SO_CS")),
                        cursor.getString(cursor.getColumnIndex("CAP_CXAC_P")),
                        cursor.getString(cursor.getColumnIndex("CAP_CXAC_Q")),
                        cursor.getString(cursor.getColumnIndex("DONG_DIEN")),
                        cursor.getString(cursor.getColumnIndex("DIEN_AP")),
                        cursor.getString(cursor.getColumnIndex("VH_CONG")),
                        cursor.getString(cursor.getColumnIndex("MA_NUOC")),
                        cursor.getString(cursor.getColumnIndex("MA_HANG")),
                        cursor.getString(cursor.getColumnIndex("HANGSO_K"))
                );
                loaiCongToList.add(loaiCongTo);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return loaiCongToList;
    }

    public int countLoaiCongTo() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + TthtConstantVariables.TABLE_NAME_LOAI_CONGTO;
        Cursor cursor = db.rawQuery(sql, null);
        int totalRows = cursor.getCount();
        cursor.close();
        return totalRows;
    }

    public int deleteAllLoaiCongTo() {
        SQLiteDatabase db = getReadableDatabase();
        int rowEffect = db.delete(TthtConstantVariables.TABLE_NAME_LOAI_CONGTO, null, null);
        db.close();
        return rowEffect;
    }

    private boolean checkLoaiCongToExist(String TEN_LOAI_CTO) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + TthtConstantVariables.TABLE_NAME_LOAI_CONGTO + " WHERE " + "LOAI_CTO" + " = " + TEN_LOAI_CTO;
        Cursor cursor = db.rawQuery(sql, null);
        int totalRows = cursor.getCount();
        cursor.close();
        if (totalRows > 0) {
            return true;
        } else return false;
    }

    public long insertLoaiCongTo(String MA_CLOAI, String TEN_LOAI_CTO, String MO_TA, String SO_PHA, String SO_DAY, String SO_CS, String CAP_CXAC_P,
                                 String CAP_CXAC_Q, String DONG_DIEN, String DIEN_AP, String VH_CONG, String MA_NUOC, String MA_HANG, String HANGSO_K, String PTHUC_DOXA, String TEN_NUOC) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MA_CLOAI", MA_CLOAI.trim());
        values.put("TEN_LOAI_CTO", TEN_LOAI_CTO.trim());
        values.put("MO_TA", MO_TA.trim());
        values.put("SO_PHA", SO_PHA.trim());
        values.put("SO_DAY", SO_DAY.trim());
        values.put("SO_CS", SO_CS.trim());
        values.put("CAP_CXAC_P", CAP_CXAC_P.trim());
        values.put("CAP_CXAC_Q", CAP_CXAC_Q.trim());
        values.put("DONG_DIEN", DONG_DIEN.trim());
        values.put("DIEN_AP", DIEN_AP.trim());
        values.put("VH_CONG", VH_CONG.trim());
        values.put("MA_NUOC", MA_NUOC.trim());
        values.put("MA_HANG", MA_HANG.trim());
        values.put("HANGSO_K", HANGSO_K.trim());
        values.put("PTHUC_DOXA", PTHUC_DOXA.trim());
        values.put("TEN_NUOC", TEN_NUOC.trim());

        long ins = database.insert(TthtConstantVariables.TABLE_NAME_LOAI_CONGTO, null, values);
        database.close();
        return ins;
    }
    //endregion

    //region Xử lý bảng Biên bản TU TI
    public long insertBBanTuTi(
            String MA_DVIQLY, int ID_BBAN_TUTI,
            String MA_DDO, String SO_BBAN, String NGAY_TRTH,
            String MA_NVIEN, int TRANG_THAI, String TEN_KHANG,
            String DCHI_HDON, String DTHOAI, String MA_GCS_CTO,
            String MA_TRAM, String LY_DO_TREO_THAO,
            String MA_KHANG, int ID_BBAN_WEB_TUTI, String NVIEN_KCHI
    ) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MA_DVIQLY", MA_DVIQLY);
        values.put("ID_BBAN_TUTI", ID_BBAN_TUTI);
        values.put("MA_DDO", MA_DDO);
        values.put("SO_BBAN", SO_BBAN);
        values.put("NGAY_TRTH", NGAY_TRTH);
        values.put("MA_NVIEN", MA_NVIEN);
        values.put("TRANG_THAI", TRANG_THAI);
        values.put("TEN_KHANG", TEN_KHANG);
        values.put("DCHI_HDON", DCHI_HDON);
        values.put("DTHOAI", DTHOAI);
        values.put("MA_GCS_CTO", MA_GCS_CTO);
        values.put("MA_TRAM", MA_TRAM);
        values.put("LY_DO_TREO_THAO", LY_DO_TREO_THAO);
        values.put("MA_KHANG", MA_KHANG);
        values.put("ID_BBAN_WEB_TUTI", ID_BBAN_WEB_TUTI);
        values.put("NVIEN_KCHI", NVIEN_KCHI);

        long ins = database.insert(TthtConstantVariables.TABLE_NAME_BBAN_TU_TI, null, values);
        database.close();
        return ins;
    }

    public boolean checkBBanTuTi(int ID_BBAN_TUTI) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + TthtConstantVariables.TABLE_NAME_BBAN_TU_TI + " WHERE ID_BBAN_TUTI = " + ID_BBAN_TUTI;
        Cursor cursor = database.rawQuery(strQuery, null);
        int totalRows = cursor.getCount();
        cursor.close();
        if (totalRows > 0) {
            return true;
        } else return false;
    }

    public long updateBBanTuTi(String MA_DVIQLY, int ID_BBAN_TUTI,
                               String MA_DDO, String SO_BBAN, String NGAY_TRTH,
                               String MA_NVIEN, int TRANG_THAI, String TEN_KHANG,
                               String DCHI_HDON, String DTHOAI, String MA_GCS_CTO,
                               String MA_TRAM, String LY_DO_TREO_THAO,
                               String MA_KHANG, int ID_BBAN_WEB_TUTI, String NVIEN_KCHI) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MA_DVIQLY", MA_DVIQLY);
        values.put("ID_BBAN_TUTI", ID_BBAN_TUTI);
        values.put("MA_DDO", MA_DDO);
        values.put("SO_BBAN", SO_BBAN);
        values.put("NGAY_TRTH", NGAY_TRTH);
        values.put("MA_NVIEN", MA_NVIEN);
        values.put("TRANG_THAI", TRANG_THAI);
        values.put("TEN_KHANG", TEN_KHANG);
        values.put("DCHI_HDON", DCHI_HDON);
        values.put("DTHOAI", DTHOAI);
        values.put("MA_GCS_CTO", MA_GCS_CTO);
        values.put("MA_TRAM", MA_TRAM);
        values.put("LY_DO_TREO_THAO", LY_DO_TREO_THAO);
        values.put("MA_KHANG", MA_KHANG);
        values.put("ID_BBAN_WEB_TUTI", ID_BBAN_WEB_TUTI);
        values.put("NVIEN_KCHI", NVIEN_KCHI);
        long ins = database.update(TthtConstantVariables.TABLE_NAME_BBAN_TU_TI, values,
                "ID_BBAN_TUTI=?", new String[]{String.valueOf(ID_BBAN_TUTI)});
        database.close();
        return ins;
    }

    public Cursor getBBanTuTi() {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + TthtConstantVariables.TABLE_NAME_BBAN_TU_TI;
        return database.rawQuery(strQuery, null);
    }

    public Cursor getBBanTuTiWithID_BBAN_TUTI(int ID_BBAN_TUTI) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + TthtConstantVariables.TABLE_NAME_BBAN_TU_TI + " WHERE ID_BBAN_TUTI = " + ID_BBAN_TUTI;
        return database.rawQuery(strQuery, null);
//        if (c.moveToFirst()) {
//            return c;
//        }
//        return null;
    }

    public long deleteBBanTuTiWithID_CHITIET_TUTI(int ID_BBAN_TUTI) {
        database = this.getWritableDatabase();
        return database.delete(TthtConstantVariables.TABLE_NAME_BBAN_TU_TI, "ID_BBAN_TUTI=?",
                new String[]{String.valueOf(ID_BBAN_TUTI)});
    }

    public long deleteBBanTuTiWithDviAndMaNVien(String MA_DVIQLY, String MA_NVIEN) {
        database = this.getWritableDatabase();
        return database.delete(TthtConstantVariables.TABLE_NAME_BBAN_TU_TI, "MA_DVIQLY=? and MA_NVIEN=?",
                new String[]{String.valueOf(MA_DVIQLY), String.valueOf(MA_NVIEN)});
    }
    //endregion

    //region Xử lý bảng Chi tiết TU TI
    public long insertTuTi(String MA_CLOAI, String LOAI_TU_TI, String MO_TA, int SO_PHA, String TYSO_DAU, int CAP_CXAC, int CAP_DAP, String MA_NUOC, String MA_HANG, int TRANG_THAI, Boolean IS_TU, int ID_BBAN_TUTI, int ID_CHITIET_TUTI, String SO_TU_TI, String NUOC_SX, String SO_TEM_KDINH, String NGAY_KDINH, String MA_CHI_KDINH, String MA_CHI_HOP_DDAY, int SO_VONG_THANH_CAI, String TYSO_BIEN, String MA_BDONG, String MA_DVIQLY) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MA_CLOAI", MA_CLOAI);
        values.put("LOAI_TU_TI", LOAI_TU_TI);
        values.put("MO_TA", MO_TA);
        values.put("SO_PHA", SO_PHA);
        values.put("TYSO_DAU", TYSO_DAU);
        values.put("CAP_CXAC", CAP_CXAC);
        values.put("CAP_DAP", CAP_DAP);
        values.put("MA_NUOC", MA_NUOC);
        values.put("MA_HANG", MA_HANG);
        values.put("TRANG_THAI", TRANG_THAI);
        values.put("IS_TU", IS_TU.toString());
        values.put("ID_BBAN_TUTI", ID_BBAN_TUTI);
        values.put("ID_CHITIET_TUTI", ID_CHITIET_TUTI);
        values.put("SO_TU_TI", SO_TU_TI);
        values.put("NUOC_SX", NUOC_SX);
        values.put("SO_TEM_KDINH", SO_TEM_KDINH);
        values.put("NGAY_KDINH", NGAY_KDINH);
        values.put("MA_CHI_KDINH", MA_CHI_KDINH);
        values.put("MA_CHI_HOP_DDAY", MA_CHI_HOP_DDAY);
        values.put("SO_VONG_THANH_CAI", SO_VONG_THANH_CAI);
        values.put("TYSO_BIEN", TYSO_BIEN);
        values.put("MA_BDONG", MA_BDONG);
        values.put("MA_DVIQLY", MA_DVIQLY);

        long ins = database.insert(TthtConstantVariables.TABLE_NAME_CHITIET_TU_TI, null, values);
        database.close();
        return ins;
    }

    public long updateTuTi(String MA_CLOAI, String LOAI_TU_TI, String MO_TA, int SO_PHA, String TYSO_DAU, int CAP_CXAC, int CAP_DAP, String MA_NUOC, String MA_HANG, int TRANG_THAI, boolean IS_TU, int ID_BBAN_TUTI, int ID_CHITIET_TUTI, String SO_TU_TI, String NUOC_SX, String SO_TEM_KDINH, String NGAY_KDINH, String MA_CHI_KDINH, String MA_CHI_HOP_DDAY, int SO_VONG_THANH_CAI, String TYSO_BIEN, String MA_BDONG, String MA_DVIQLY) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MA_CLOAI", MA_CLOAI);
        values.put("LOAI_TU_TI", LOAI_TU_TI);
        values.put("MO_TA", MO_TA);
        values.put("SO_PHA", SO_PHA);
        values.put("TYSO_DAU", TYSO_DAU);
        values.put("CAP_CXAC", CAP_CXAC);
        values.put("CAP_DAP", CAP_DAP);
        values.put("MA_NUOC", MA_NUOC);
        values.put("MA_HANG", MA_HANG);
        values.put("TRANG_THAI", TRANG_THAI);
        values.put("IS_TU", IS_TU);
        values.put("ID_BBAN_TUTI", ID_BBAN_TUTI);
        values.put("SO_TU_TI", SO_TU_TI);
        values.put("NUOC_SX", NUOC_SX);
        values.put("SO_TEM_KDINH", SO_TEM_KDINH);
        values.put("NGAY_KDINH", NGAY_KDINH);
        values.put("MA_CHI_KDINH", MA_CHI_KDINH);
        values.put("MA_CHI_HOP_DDAY", MA_CHI_HOP_DDAY);
        values.put("SO_VONG_THANH_CAI", SO_VONG_THANH_CAI);
        values.put("TYSO_BIEN", TYSO_BIEN);
        values.put("MA_BDONG", MA_BDONG);
        values.put("MA_DVIQLY", MA_DVIQLY);
        long ins = database.update(TthtConstantVariables.TABLE_NAME_CHITIET_TU_TI, values,
                "ID_CHITIET_TUTI=?", new String[]{String.valueOf(ID_CHITIET_TUTI)});
        database.close();
        return ins;
    }

    public Cursor getTuTi() {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + TthtConstantVariables.TABLE_NAME_CHITIET_TU_TI;
        return database.rawQuery(strQuery, null);
    }

    public Cursor getTuTiWithID_BBAN_TUTI(int ID_BBAN_TUTI) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + TthtConstantVariables.TABLE_NAME_CHITIET_TU_TI + " WHERE ID_BBAN_TUTI = " + ID_BBAN_TUTI;
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            return c;
        }
        return null;
    }

    public boolean checkTuTi(int ID_CHITIET_TUTI) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + TthtConstantVariables.TABLE_NAME_CHITIET_TU_TI + " WHERE ID_CHITIET_TUTI = " + ID_CHITIET_TUTI;
        Cursor cursor = database.rawQuery(strQuery, null);
        int totalRows = cursor.getCount();
        cursor.close();
        if (totalRows > 0) {
            return true;
        } else return false;
    }

    public long deleteTuTiWithID_CHITIET_TUTI(int ID_CHITIET_TUTI) {
        database = this.getWritableDatabase();
        return database.delete(TthtConstantVariables.TABLE_NAME_CHITIET_TU_TI, "ID_CHITIET_CTO=?",
                new String[]{String.valueOf(ID_CHITIET_TUTI)});
    }

    public long deleteChiTietTuTiWithDviAndMaNVien(String MA_DVIQLY, String MA_NVIEN) {
        database = this.getWritableDatabase();
        return database.delete(TthtConstantVariables.TABLE_NAME_CHITIET_TU_TI, "MA_DVIQLY=? and MA_NVIEN=?",
                new String[]{String.valueOf(MA_DVIQLY), String.valueOf(MA_NVIEN)});
    }

    //endregion


    public Cursor getDataKhangFullMA_TRAM(String MA_BDONG, String MA_NVIEN, String MA_DVIQLY, String NGAY_TRTH, TthtCommon.FILTER_DATA_FILL FILTER_DATA_FILL) {
        database = this.getReadableDatabase();
        StringBuilder strQuery = new StringBuilder();
        String query = "";

        if (FILTER_DATA_FILL == TthtCommon.FILTER_DATA_FILL.ALL) {
            strQuery.append("SELECT DISTINCT * FROM (SELECT * FROM (SELECT * FROM ");
            strQuery.append(TthtConstantVariables.TABLE_NAME_DETAIL_CONGTO);
            strQuery.append(" WHERE MA_DVIQLY = '").append(MA_DVIQLY).append("')");
            strQuery.append(" D INNER JOIN ");
            strQuery.append(TthtConstantVariables.TABLE_NAME_BBAN_CONGTO);
            strQuery.append(" B ON D.ID_BBAN_TRTH = B.ID_BBAN_TRTH) WHERE MA_BDONG = '");
            strQuery.append(MA_BDONG);
//            strQuery.append("' AND NGAY_TRTH = '");
//            strQuery.append(NGAY_TRTH);
            strQuery.append("'");
            strQuery.append(" AND MA_NVIEN = '");
            strQuery.append(MA_NVIEN);
            strQuery.append("'");
            query = strQuery.toString();
        }

        if (FILTER_DATA_FILL == TthtCommon.FILTER_DATA_FILL.TREO || FILTER_DATA_FILL == TthtCommon.FILTER_DATA_FILL.THAO) {
            query = "SELECT * FROM DETAIL_CONGTO " +
                    " WHERE ID_BBAN_TRTH " +
                    " IN " +
                    "(" +
                    " SELECT ID_BBAN_TRTH FROM" +
                    "(" +
                    " SELECT ID_BBAN_TRTH, NGAY_TRTH FROM BBAN_CONGTO WHERE MA_DVIQLY = '" + MA_DVIQLY + "' AND NGAY_TRTH <= '" + NGAY_TRTH + "' AND MA_NVIEN = '" + MA_NVIEN + "' AND TRANG_THAI = '1'" +
                    ")" +
                    " ORDER BY NGAY_TRTH DESC" +
                    ")" +
                    " AND MA_BDONG = '" + FILTER_DATA_FILL.toString() + "'";
//            strQuery.append("SELECT DISTINCT * FROM (SELECT * FROM (SELECT * FROM ");
//            strQuery.append(TthtConstantVariables.TABLE_NAME_DETAIL_CONGTO);
//            strQuery.append(" WHERE MA_DVIQLY = '").append(MA_DVIQLY).append("')");
//            strQuery.append(" D INNER JOIN ");
//            strQuery.append(TthtConstantVariables.TABLE_NAME_BBAN_CONGTO);
//            strQuery.append(" B ON D.ID_BBAN_TRTH = B.ID_BBAN_TRTH) WHERE MA_BDONG = '");
//            strQuery.append(MA_BDONG);
//            strQuery.append("' AND NGAY_TRTH = '");
//            strQuery.append(NGAY_TRTH);
//            strQuery.append("'");
//            strQuery.append(" AND MA_NVIEN = '");
//            strQuery.append(MA_NVIEN);
//            strQuery.append("'");
        }

        if (FILTER_DATA_FILL == TthtCommon.FILTER_DATA_FILL.PHIEU_TREO_THAO_HISTORY) {
            query = "SELECT  " +
                    "BBAN.ID_BBAN_TRTH AS ID_BBAN_TRTH, TEN_KHANG,DCHI_HDON, SO_BBAN, MA_CTO, CHI_SO, CHI_SO_SAULAP_TUTI,MA_GCS_CTO,MA_TRAM, ID_CHITIET_CTO, SO_CTO, TRANG_THAI_DU_LIEU  " +
                    "FROM (  " +
                    " SELECT ID_BBAN_TRTH, DETAIL_CONGTO.CHI_SO, DETAIL_CONGTO.CHI_SO_SAULAP_TUTI, DETAIL_CONGTO.MA_CTO, DETAIL_CONGTO.SO_CTO, DETAIL_CONGTO.ID_CHITIET_CTO, DETAIL_CONGTO.TRANG_THAI_DU_LIEU  " +
                    " FROM DETAIL_CONGTO   " +
                    " WHERE ID_BBAN_TRTH   " +
                    " IN  (  " +
                    "   SELECT ID_BBAN_TRTH " +
                    "   FROM BBAN_CONGTO  " +
                    "   WHERE  " +
                    "   MA_DVIQLY = '" +
                    MA_DVIQLY +
                    "'  " +
                    "   AND NGAY_TRTH < '" +
                    NGAY_TRTH +
                    "'  " +
                    "   AND MA_NVIEN = '" +
                    MA_NVIEN +
                    "'  " +
                    "   AND TRANG_THAI = '1' " +
                    "   ORDER BY NGAY_TRTH DESC " +
                    "   )  " +
                    " AND MA_BDONG = '" +
                    MA_BDONG +
                    "')  " +
                    "  " +
                    "DETAIL JOIN  " +
                    " (SELECT ID_BBAN_TRTH, TEN_KHANG, DCHI_HDON, SO_BBAN,MA_GCS_CTO, MA_TRAM " +
                    " FROM BBAN_CONGTO  " +
                    " WHERE MA_DVIQLY = '" +
                    MA_DVIQLY +
                    "' AND NGAY_TRTH < '" +
                    NGAY_TRTH +
                    "' AND MA_NVIEN = '" +
                    MA_NVIEN +
                    "' AND TRANG_THAI = '1') BBAN " +
                    "  " +
                    "ON DETAIL.ID_BBAN_TRTH = BBAN.ID_BBAN_TRTH";
        }

        if (FILTER_DATA_FILL == TthtCommon.FILTER_DATA_FILL.PHIEU_TREO_THAO_TODAY) {
            query =
                    "SELECT BBAN.ID_BBAN_TRTH AS ID_BBAN_TRTH, TEN_KHANG,DCHI_HDON, SO_BBAN, MA_CTO, SO_CTO, CHI_SO, CHI_SO_SAULAP_TUTI, MA_GCS_CTO, MA_TRAM, ID_CHITIET_CTO, TRANG_THAI_DU_LIEU " +
                            "FROM ( " +
                            "  SELECT  ID_BBAN_TRTH, DETAIL_CONGTO.CHI_SO, DETAIL_CONGTO.CHI_SO_SAULAP_TUTI, DETAIL_CONGTO.MA_CTO, DETAIL_CONGTO.SO_CTO, DETAIL_CONGTO.ID_CHITIET_CTO, DETAIL_CONGTO.TRANG_THAI_DU_LIEU  " +
                            "  FROM DETAIL_CONGTO   " +
                            "  WHERE ID_BBAN_TRTH   " +
                            "  IN   " +
                            "  (    " +
                            "      SELECT ID_BBAN_TRTH  " +
                            "      FROM BBAN_CONGTO  " +
                            "      WHERE MA_DVIQLY = '" +
                            MA_DVIQLY +
                            "'  " +
                            "      AND NGAY_TRTH = '" +
                            NGAY_TRTH +
                            "'  " +
                            "      AND MA_NVIEN = '" +
                            MA_NVIEN +
                            "' " +
                            "  )  " +
                            "      AND MA_BDONG = '" +
                            MA_BDONG +
                            "' )  " +
                            "    DETAIL  " +
                            "    JOIN  " +
                            "      (SELECT  " +
                            "      ID_BBAN_TRTH, TEN_KHANG, DCHI_HDON, SO_BBAN,MA_GCS_CTO, MA_TRAM  " +
                            "      FROM BBAN_CONGTO  " +
                            "      WHERE MA_DVIQLY = '" +
                            MA_DVIQLY +
                            "'  " +
                            "      AND NGAY_TRTH = '" +
                            NGAY_TRTH +
                            "'  " +
                            "      AND MA_NVIEN = '" +
                            MA_NVIEN +
                            "')  " +
                            "    BBAN  " +
                            "    ON DETAIL.ID_BBAN_TRTH = BBAN.ID_BBAN_TRTH";
        }

        Cursor c = database.rawQuery(query, null);
        if (c.moveToFirst()) {
            return c;
        }
        return null;
    }

    public Cursor getTRANG_THAI_DU_LIEU(int ID_CHITIET_CTO) {
        if (ID_CHITIET_CTO <= 0)
            return null;
        database = this.getReadableDatabase();
        Cursor c = database.rawQuery("SELECT TRANG_THAI_DU_LIEU FROM DETAIL_CONGTO WHERE ID_CHITIET_CTO = '" + ID_CHITIET_CTO + "'", null);
        if (c.moveToFirst()) {
            return c;
        }
        return null;
    }

    public Cursor getID_CHITIET_CTO(int id_bban_trth, String ma_bdong) {
        if (id_bban_trth <= 0 || ma_bdong == null || ma_bdong.isEmpty())
            return null;

        database = this.getReadableDatabase();
        Cursor c = database.rawQuery("SELECT ID_CHITIET_CTO FROM DETAIL_CONGTO WHERE ID_BBAN_TRTH = '" + id_bban_trth + "' AND MA_BDONG = '" + ma_bdong + "'", null);
        if (c.moveToFirst()) {
            return c;
        }
        return null;
    }

    public Cursor getID_BBANTUTI(int id_chitiet_cto) {
        if (id_chitiet_cto <= 0)
            return null;

        database = this.getReadableDatabase();
        Cursor c = database.rawQuery("SELECT ID_BBAN_TUTI FROM DETAIL_CONGTO WHERE ID_CHITIET_CTO = '" + id_chitiet_cto + "'", null);
        if (c.moveToFirst()) {
            return c;
        }
        return null;
    }

    public Cursor getID_TUTI(int id_bban_tuti) {
        if (id_bban_tuti <= 0)
            return null;

        database = this.getReadableDatabase();
        Cursor c = database.rawQuery("SELECT ID FROM CHITIET_TU_TI WHERE ID_BBAN_TUTI = '" + id_bban_tuti + "'", null);
        if (c.moveToFirst()) {
            return c;
        }
        return null;
    }

    public Cursor getCToByID_CHITIET_CTO(int id_chitiet_cto) {
        if (id_chitiet_cto <= 0)
            return null;

        database = this.getReadableDatabase();
        Cursor c = database.rawQuery("SELECT * FROM DETAIL_CONGTO WHERE ID_CHITIET_CTO = '" + id_chitiet_cto + "'", null);
        if (c.moveToFirst()) {
            return c;
        }
        return null;
    }

    public Cursor getDataTUTI(int id_bban_tuti) {
        if (id_bban_tuti <= 0)
            return null;

        database = this.getReadableDatabase();
        Cursor c = database.rawQuery("SELECT * FROM CHITIET_TU_TI WHERE ID_BBAN_TUTI = '" + id_bban_tuti + "'", null);
        if (c.moveToFirst()) {
            return c;
        }
        return null;
    }


//    public Cursor getDataKhangSent(String MA_CTO){
//        database = this.getReadableDatabase();
//        StringBuilder strQuery = new StringBuilder("SELECT * FROM ").append(TthtConstantVariables.TABLE_NAME_BBAN_CONGTO)
//                .append(" B INNER JOIN ").append(TthtConstantVariables.TABLE_NAME_DETAIL_CONGTO)
//                .append(" D ON B.MA_DVIQLY = D.MA_DVIQLY AND MA_CTO = '")
//                .append(MA_CTO)
//                .append("'");
//        return database.rawQuery(strQuery.toString(), null);
//    }
    //endregion

}
