package com.es.tungnv.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.es.tungnv.entity.GphtEntityLogQuyen;
import com.es.tungnv.utils.GphtConstantVariables;

import java.util.ArrayList;
import java.util.List;

import static com.es.tungnv.utils.GphtConstantVariables.TABLE_NAME_DM_USER;
import static com.es.tungnv.utils.GphtConstantVariables.TABLE_NAME_NGUOIDUNG;

/**
 * Created by TUNGNV on 7/19/2016.
 */
public class GphtSqliteConnection extends SQLiteOpenHelper {

    private static GphtSqliteConnection instance;

    SQLiteDatabase database;

    private GphtSqliteConnection(Context context) {
        super(context, GphtConstantVariables.DATABASE_NAME, null, GphtConstantVariables.DATABASE_VERSION);
    }

    public synchronized static GphtSqliteConnection getInstance(Context context) {
        if(instance == null) {
            instance = new GphtSqliteConnection(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(GphtConstantVariables.CREATE_TABLE_DONVI);
        db.execSQL(GphtConstantVariables.CREATE_TABLE_DM_HETHONG);
        db.execSQL(GphtConstantVariables.CREATE_TABLE_DM_USER);
        db.execSQL(GphtConstantVariables.CREATE_TABLE_NGUOIDUNG);
        db.execSQL(GphtConstantVariables.CREATE_TABLE_QUYEN);
        db.execSQL(GphtConstantVariables.CREATE_TABLE_LOG_DONGBO);
        db.execSQL(GphtConstantVariables.CREATE_TABLE_LOG_QUYEN_DONGBO);
        db.execSQL(GphtConstantVariables.CREATE_TABLE_LOG_DANG_NHAP);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + GphtConstantVariables.TABLE_NAME_DONVI);
        db.execSQL("DROP TABLE IF EXISTS " + GphtConstantVariables.TABLE_NAME_DM_HETHONG);
        db.execSQL("DROP TABLE IF EXISTS " + GphtConstantVariables.TABLE_NAME_DM_USER);
        db.execSQL("DROP TABLE IF EXISTS " + GphtConstantVariables.TABLE_NAME_NGUOIDUNG);
        db.execSQL("DROP TABLE IF EXISTS " + GphtConstantVariables.TABLE_NAME_QUYEN);
        db.execSQL("DROP TABLE IF EXISTS " + GphtConstantVariables.TABLE_NAME_LOG_DONGBO);
        db.execSQL("DROP TABLE IF EXISTS " + GphtConstantVariables.TABLE_NAME_LOG_QUYEN_DONGBO);
        db.execSQL("DROP TABLE IF EXISTS " + GphtConstantVariables.TABLE_NAME_LOG_DANG_NHAP);
        onCreate(db);
    }

    public void deleteAll(){
        database = this.getWritableDatabase();
        database.delete(GphtConstantVariables.TABLE_NAME_DONVI, null, null);
        database.delete(GphtConstantVariables.TABLE_NAME_DM_HETHONG, null, null);
        database.delete(GphtConstantVariables.TABLE_NAME_DM_USER, null, null);
        database.delete(GphtConstantVariables.TABLE_NAME_NGUOIDUNG, null, null);
        database.delete(GphtConstantVariables.TABLE_NAME_QUYEN, null, null);
        database.delete(GphtConstantVariables.TABLE_NAME_LOG_DONGBO, null, null);
        database.delete(GphtConstantVariables.TABLE_NAME_LOG_QUYEN_DONGBO, null, null);
        database.delete(GphtConstantVariables.TABLE_NAME_LOG_DANG_NHAP, null, null);
    }

    // ------------Xử lý bảng đơn vị--------------
    public long insertDataDVIQLY(int ID_DonVi, String Ten_DonVi, String Ma_DonVi){
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ID_DonVi", ID_DonVi);
        values.put("Ten_DonVi", Ten_DonVi);
        values.put("Ma_DonVi", Ma_DonVi);
        return database.insert(GphtConstantVariables.TABLE_NAME_DONVI, null, values);
    }

    public long deleteAllDataDVIQLY() {
        database = this.getWritableDatabase();
        return database.delete(GphtConstantVariables.TABLE_NAME_DONVI, null, null);
    }

    public Cursor getAllDataDVIQLY() {
        database = this.getReadableDatabase();
        StringBuilder strQuery = new StringBuilder("SELECT * FROM ").append(GphtConstantVariables.TABLE_NAME_DONVI);
        return database.rawQuery(strQuery.toString(), null);
    }

    public List<String> getDataDVIQLY() {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + GphtConstantVariables.TABLE_NAME_DONVI;
        Cursor c = database.rawQuery(strQuery, null);
        List<String> list_dviqly = new ArrayList<String>();
        if(c.moveToFirst()){
            do{
                list_dviqly.add(new StringBuilder(c.getString(c.getColumnIndex("Ma_DonVi"))).append(" - ")
                        .append(c.getString(c.getColumnIndex("Ten_DonVi"))).toString());
            } while(c.moveToNext());
        }
        return list_dviqly;
    }

    public int getPosSelectDVIQLY(int ID_DonVi) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT ID_DonVi FROM " + GphtConstantVariables.TABLE_NAME_DONVI;
        Cursor c = database.rawQuery(strQuery, null);
        int pos = 0;
        int i = 0;
        if(c.moveToFirst()){
            do{
                if(c.getInt(c.getColumnIndex("ID_DonVi")) == ID_DonVi) {
                    pos = i;
                }
                i++;
            } while(c.moveToNext());
        }
        return pos;
    }

    public String getMaSelectDVIQLY(int ID_DonVi) {
        String MA_DVIQLY = "";
        database = this.getReadableDatabase();
        String strQuery = "SELECT Ma_DonVi FROM " + GphtConstantVariables.TABLE_NAME_DONVI + " WHERE ID_DonVi = " + ID_DonVi;
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()) {
            MA_DVIQLY = c.getString(0);
        }
        return MA_DVIQLY;
    }

    public int getIDSelectDVIQLY(String Ma_DonVi) {
        int ID_DVIQLY = 0;
        database = this.getReadableDatabase();
        String strQuery = "SELECT ID_DonVi FROM " + GphtConstantVariables.TABLE_NAME_DONVI + " WHERE Ma_DonVi = '" + Ma_DonVi + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()) {
            ID_DVIQLY = c.getInt(0);
        }
        return ID_DVIQLY;
    }

    public String getDVIQLYByID(int ID_DonVi) {
        String DVIQLY = "";
        database = this.getReadableDatabase();
        String strQuery = "SELECT Ma_DonVi, Ten_DonVi FROM " + GphtConstantVariables.TABLE_NAME_DONVI + " WHERE ID_DonVi = " + ID_DonVi;
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()) {
            DVIQLY = c.getString(0) + " - " + c.getString(1);
        }
        return DVIQLY;
    }

    public List<Integer> getListIDMaDviQly() {
        List<Integer> rowid = new ArrayList<>();
        database = this.getReadableDatabase();
        String strQuery = new StringBuilder("SELECT ID_DonVi FROM ").append(GphtConstantVariables.TABLE_NAME_DONVI).toString();
        Cursor c = database.rawQuery(strQuery, null);
        if (c.moveToFirst()) {
            do {
                rowid.add(c.getInt(0));
            } while (c.moveToNext());
        }
        return rowid;
    }

    // ------------Xử lý bảng danh mục hệ thống--------------
    public long insertDataDMHeThong(int ID_HeThong, String Ten_HeThong, String Address, int ID_DonVi, String Key_HThong){
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ID_HeThong", ID_HeThong);
        values.put("Ten_HeThong", Ten_HeThong);
        values.put("Address", Address);
        values.put("ID_DonVi", ID_DonVi);
        values.put("Key_HThong", Key_HThong);
        return database.insert(GphtConstantVariables.TABLE_NAME_DM_HETHONG, null, values);
    }

    public long deleteAllDataDMHeThong() {
        database = this.getWritableDatabase();
        return database.delete(GphtConstantVariables.TABLE_NAME_DM_HETHONG, null, null);
    }

    public Cursor getAllDataDMHeThong() {
        database = this.getReadableDatabase();
        StringBuilder strQuery = new StringBuilder("SELECT * FROM ").append(GphtConstantVariables.TABLE_NAME_DM_HETHONG);
        return database.rawQuery(strQuery.toString(), null);
    }

    // ------------Xử lý bảng danh mục user cha--------------
    public long insertDataUserCha(int NV_ID, String Ten_TCap, String Mat_Khau, String Ten_DD, String Dien_Thoai, String Email,
                                  int STT, int ID_DonVi, String DuongDan_URL_Default, boolean Admin){
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NV_ID", NV_ID);
        values.put("Ten_TCap", Ten_TCap);
        values.put("Mat_Khau", Mat_Khau);
        values.put("Ten_DD", Ten_DD);
        values.put("Dien_Thoai", Dien_Thoai);
        values.put("Email", Email);
        values.put("STT", STT);
        values.put("ID_DonVi", ID_DonVi);
        values.put("DuongDan_URL_Default", DuongDan_URL_Default);
        values.put("Admin", Admin);
        return database.insert(TABLE_NAME_NGUOIDUNG, null, values);
    }

    public long deleteAllDataUserCha() {
        database = this.getWritableDatabase();
        return database.delete(TABLE_NAME_NGUOIDUNG, null, null);
    }

    public Cursor getAllDataUserCha() {
        database = this.getReadableDatabase();
        StringBuilder strQuery = new StringBuilder("SELECT * FROM ").append(TABLE_NAME_NGUOIDUNG);
        return database.rawQuery(strQuery.toString(), null);
    }

    public Cursor getLogin(String Ten_TCap, String Mat_Khau, int ID_DonVi) {
        database = this.getReadableDatabase();
        StringBuilder strQuery = new StringBuilder("SELECT * FROM ").append(TABLE_NAME_NGUOIDUNG)
                .append(" WHERE Ten_TCap = '").append(Ten_TCap).append("' AND Mat_Khau = '").append(Mat_Khau)
                .append("' AND ID_DonVi = ").append(ID_DonVi);
        return database.rawQuery(strQuery.toString(), null);
    }

    // ------------Xử lý bảng danh mục user con--------------
    public long insertDataUserCon(int ID_User, int ID_HeThong, String GhiChu, String Ten_DangNhap, String Mat_Khau){
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ID_User", ID_User);
        values.put("ID_HeThong", ID_HeThong);
        values.put("GhiChu", GhiChu);
        values.put("Ten_DangNhap", Ten_DangNhap);
        values.put("Mat_Khau", Mat_Khau);
        return database.insert(TABLE_NAME_DM_USER, null, values);
    }

    public long deleteAllDataUserCon() {
        database = this.getWritableDatabase();
        return database.delete(TABLE_NAME_DM_USER, null, null);
    }

    public Cursor getAllDataUserCon() {
        database = this.getReadableDatabase();
        StringBuilder strQuery = new StringBuilder("SELECT * FROM ").append(TABLE_NAME_DM_USER);
        return database.rawQuery(strQuery.toString(), null);
    }

    public int getIDUserCon(String Ten_DangNhap, String Mat_Khau) {
        database = this.getReadableDatabase();
        StringBuilder strQuery = new StringBuilder("SELECT ID_User FROM ").append(TABLE_NAME_NGUOIDUNG)
                .append(" WHERE Ten_TCap = '").append(Ten_DangNhap).append("' AND Mat_Khau = '").append(Mat_Khau).append("'");
        Cursor c = database.rawQuery(strQuery.toString(), null);
        if(c.moveToNext()){
            return c.getInt(0);
        }
        return 0;
    }

    public Cursor getUser(int ID_User, int ID_HeThong) {
        database = this.getReadableDatabase();
        StringBuilder strQuery = new StringBuilder("SELECT * FROM ").append(TABLE_NAME_DM_USER)
                .append(" WHERE ID_User = ").append(ID_User).append(" AND ID_HeThong = ").append(ID_HeThong);
        return database.rawQuery(strQuery.toString(), null);
    }
    // ------------Xử lý bảng quyền--------------
    public long insertDataQuyen(int ID, int NV_ID, int ID_HeThong, int ID_User){
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ID", ID_User);
        values.put("NV_ID", NV_ID);
        values.put("ID_HeThong", ID_HeThong);
        values.put("ID_User", ID_User);
        return database.insert(GphtConstantVariables.TABLE_NAME_QUYEN, null, values);
    }

    public long deleteAllQuyen() {
        database = this.getWritableDatabase();
        return database.delete(GphtConstantVariables.TABLE_NAME_QUYEN, null, null);
    }

    public Cursor getAllDataQuyen() {
        database = this.getReadableDatabase();
        StringBuilder strQuery = new StringBuilder("SELECT * FROM ").append(GphtConstantVariables.TABLE_NAME_QUYEN);
        return database.rawQuery(strQuery.toString(), null);
    }

    // ------------Xử lý bảng log quyền--------------
    public long insertDataLogQuyen(int Id_Log, String ThoiGian){
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Id_Log", Id_Log);
        values.put("ThoiGian", ThoiGian);
        return database.insert(GphtConstantVariables.TABLE_NAME_LOG_QUYEN_DONGBO, null, values);
    }

    public long deleteAllLogQuyen() {
        database = this.getWritableDatabase();
        return database.delete(GphtConstantVariables.TABLE_NAME_LOG_QUYEN_DONGBO, null, null);
    }

    public Cursor getAllDataLogQuyen() {
        database = this.getReadableDatabase();
        StringBuilder strQuery = new StringBuilder("SELECT * FROM ").append(GphtConstantVariables.TABLE_NAME_LOG_QUYEN_DONGBO);
        return database.rawQuery(strQuery.toString(), null);
    }

    public GphtEntityLogQuyen getDataLogQuyen() {
        database = this.getReadableDatabase();
        StringBuilder strQuery = new StringBuilder("SELECT * FROM ").append(GphtConstantVariables.TABLE_NAME_LOG_QUYEN_DONGBO);
        Cursor c = database.rawQuery(strQuery.toString(), null);
        GphtEntityLogQuyen entity = new GphtEntityLogQuyen();
        if (c.moveToFirst()) {
            do {
                entity.setID(c.getInt(c.getColumnIndex("Id_Log")));
                entity.setTHOI_GIAN(c.getString(c.getColumnIndex("ThoiGian")));
            } while (c.moveToNext());
        }
        return entity;
    }

    // ------------Xử lý bảng log login--------------
    public long insertDataLogLogin(int NV_ID, String TGian_Tcap){
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NV_ID", NV_ID);
        values.put("TGian_Tcap", TGian_Tcap);
        return database.insert(GphtConstantVariables.TABLE_NAME_LOG_DANG_NHAP, null, values);
    }

    public long deleteAllLogLogin() {
        database = this.getWritableDatabase();
        return database.delete(GphtConstantVariables.TABLE_NAME_LOG_DANG_NHAP, null, null);
    }

    public Cursor getAllDataLogLogin() {
        database = this.getReadableDatabase();
        StringBuilder strQuery = new StringBuilder("SELECT * FROM ").append(GphtConstantVariables.TABLE_NAME_LOG_DANG_NHAP);
        return database.rawQuery(strQuery.toString(), null);
    }

    public Cursor getMaxDataLogLogin() {
        database = this.getReadableDatabase();
        StringBuilder strQuery = new StringBuilder("SELECT * FROM ").append(GphtConstantVariables.TABLE_NAME_LOG_DANG_NHAP)
                .append(" L, ").append(GphtConstantVariables.TABLE_NAME_NGUOIDUNG).append(" D")
                .append(" WHERE L.NV_ID = D.NV_ID AND L.ID = (SELECT MAX(ID) FROM ").append(GphtConstantVariables.TABLE_NAME_LOG_DANG_NHAP).append(")");
        return database.rawQuery(strQuery.toString(), null);
    }

    public Cursor getDataHeThong(int NV_ID) {
        database = this.getReadableDatabase();
        StringBuilder strQuery = new StringBuilder("SELECT * FROM ").append(GphtConstantVariables.TABLE_NAME_DM_HETHONG)
                .append(" L, ").append(GphtConstantVariables.TABLE_NAME_QUYEN).append(" D")
                .append(" WHERE L.ID_HeThong = D.ID_HeThong AND D.NV_ID = ").append(NV_ID);
        return database.rawQuery(strQuery.toString(), null);
    }

    public Cursor getDataHeThong(int NV_ID, String Key_HThong) {
        database = this.getReadableDatabase();
        StringBuilder strQuery = new StringBuilder("SELECT * FROM ").append(GphtConstantVariables.TABLE_NAME_DM_HETHONG)
                .append(" L, ").append(GphtConstantVariables.TABLE_NAME_QUYEN).append(" D")
                .append(" WHERE L.ID_HeThong = D.ID_HeThong AND D.NV_ID = ").append(NV_ID)
                .append(" AND Key_HThong = '").append(Key_HThong).append("'");
        return database.rawQuery(strQuery.toString(), null);
    }

}
