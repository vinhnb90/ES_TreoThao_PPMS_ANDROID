package com.es.tungnv.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.es.tungnv.utils.InvoiceConstantVariables;

/**
 * Created by TUNGNV on 2/25/2016.
 */
public class InvoiceSQLiteConnection extends SQLiteOpenHelper{

    private SQLiteDatabase database;

    public InvoiceSQLiteConnection(Context context) {
        super(context, Environment.getExternalStorageDirectory() + InvoiceConstantVariables.PROGRAM_DB_PATH + InvoiceConstantVariables.DATABASE_NAME, null, InvoiceConstantVariables.DATABASE_VERSION);
        SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory() + InvoiceConstantVariables.PROGRAM_DB_PATH + InvoiceConstantVariables.DATABASE_NAME, null);
        this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(InvoiceConstantVariables.CREATE_TABLE_THUNGAN);
        db.execSQL(InvoiceConstantVariables.CREATE_TABLE_POS_KHACHHANG);
        db.execSQL(InvoiceConstantVariables.CREATE_TABLE_LOG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + InvoiceConstantVariables.TABLE_NAME_THUNGAN);
        db.execSQL("DROP TABLE IF EXISTS " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG);
        db.execSQL("DROP TABLE IF EXISTS " + InvoiceConstantVariables.TABLE_NAME_LOG);
        onCreate(db);
    }

    //--------------------------- XỬ LÝ BẢNG D_THUNGAN ------------------------------
    public long insertDataTHUNGAN(String MA_DVIQLY,String TEN_DVIQLY, String DCHI_DVIQLY,
                                     String MA_STHUE,String SO_TKHOAN, String MA_TNGAN,
                                     String TEN_TNGAN,String MKHAU_TNGAN, String DTHOAI_TNGAN,
                                     String MA_MAY,String DU_PHONG_1, String DU_PHONG_2){
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MA_DVIQLY", MA_DVIQLY);
        values.put("TEN_DVIQLY", TEN_DVIQLY);
        values.put("DCHI_DVIQLY", DCHI_DVIQLY);
        values.put("MA_STHUE", MA_STHUE);
        values.put("SO_TKHOAN", SO_TKHOAN);
        values.put("MA_TNGAN", MA_TNGAN);
        values.put("TEN_TNGAN", TEN_TNGAN);
        values.put("MKHAU_TNGAN", MKHAU_TNGAN);
        values.put("DTHOAI_TNGAN", DTHOAI_TNGAN);
        values.put("MA_MAY", MA_MAY);
        values.put("DU_PHONG_1", DU_PHONG_1);
        values.put("DU_PHONG_2", DU_PHONG_2);
        long ins = database.insert(InvoiceConstantVariables.TABLE_NAME_THUNGAN, null, values);
        database.close();
        return ins;
    }

    public long updateDataTHUNGAN(int ID, String MA_DVIQLY,String TEN_DVIQLY, String DCHI_DVIQLY,
                                 String MA_STHUE,String SO_TKHOAN, String MA_TNGAN,
                                 String TEN_TNGAN,String MKHAU_TNGAN, String DTHOAI_TNGAN,
                                 String MA_MAY,String DU_PHONG_1, String DU_PHONG_2){
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MA_DVIQLY", MA_DVIQLY);
        values.put("TEN_DVIQLY", TEN_DVIQLY);
        values.put("DCHI_DVIQLY", DCHI_DVIQLY);
        values.put("MA_STHUE", MA_STHUE);
        values.put("SO_TKHOAN", SO_TKHOAN);
        values.put("MA_TNGAN", MA_TNGAN);
        values.put("TEN_TNGAN", TEN_TNGAN);
        values.put("MKHAU_TNGAN", MKHAU_TNGAN);
        values.put("DTHOAI_TNGAN", DTHOAI_TNGAN);
        values.put("MA_MAY", MA_MAY);
        values.put("DU_PHONG_1", DU_PHONG_1);
        values.put("DU_PHONG_2", DU_PHONG_2);
        long ins = database.update(InvoiceConstantVariables.TABLE_NAME_THUNGAN, values, "ID=?", new String[]{"" + ID});
        database.close();
        return ins;
    }

    public long updateMkTHUNGAN(String MA_TNGAN, String MKHAU_TNGAN){
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MKHAU_TNGAN", MKHAU_TNGAN);
        long ins = database.update(InvoiceConstantVariables.TABLE_NAME_THUNGAN, values, "MA_TNGAN=?", new String[]{"" + MA_TNGAN});
        database.close();
        return ins;
    }

    public long deleteAllDataTHUNGAN() {
        database = this.getWritableDatabase();
        long ins = database.delete(InvoiceConstantVariables.TABLE_NAME_THUNGAN, null, null);
        database.close();
        return ins;
    }

    public long deleteDataTHUNGANByID(int ID) {
        database = this.getWritableDatabase();
        long ins = database.delete(InvoiceConstantVariables.TABLE_NAME_THUNGAN, "ID=?", new String[]{"" + ID});
        database.close();
        return ins;
    }

    public Cursor getAllDataTHUNGAN(){
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + InvoiceConstantVariables.TABLE_NAME_THUNGAN;
        return database.rawQuery(strQuery, null);
    }

    public Cursor getDataTHUNGANByMa(String MA_TNGAN){
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + InvoiceConstantVariables.TABLE_NAME_THUNGAN + " WHERE MA_TNGAN = '" + MA_TNGAN + "'";
        return database.rawQuery(strQuery, null);
    }

    public String getTenKHByMaTN(String MA_TNGAN){//true: chấm HKT
        database = this.getReadableDatabase();
        String strQuery = "SELECT TEN_TNGAN FROM " + InvoiceConstantVariables.TABLE_NAME_THUNGAN + " WHERE MA_TNGAN = '" + MA_TNGAN + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getString(0);
        }
        return "";
    }

    public String getMkTHUNGAN(String MA_TNGAN){
        database = this.getReadableDatabase();
        String strQuery = "SELECT MKHAU_TNGAN FROM " + InvoiceConstantVariables.TABLE_NAME_THUNGAN + " WHERE MA_TNGAN = '" + MA_TNGAN + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()) {
            return c.getString(0);
        }
        return "";
    }

    public int checkLogin(String user, String pass){
        database = this.getReadableDatabase();
        int check = 0;
        String strQuery = "SELECT COUNT(*) FROM " + InvoiceConstantVariables.TABLE_NAME_THUNGAN + " WHERE MA_TNGAN = '" + user + "' AND MKHAU_TNGAN = '" + pass + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()) {
            check = c.getInt(0);
        } else {
            check = 0;
        }
        return check;
    }

    public String getMaDVIByUser(String user){
        database = this.getReadableDatabase();
        int check = 0;
        String strQuery = "SELECT MA_DVIQLY FROM " + InvoiceConstantVariables.TABLE_NAME_THUNGAN + " WHERE MA_TNGAN = '" + user + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getString(0);
        }
        return "";
    }

    //--------------------------- XỬ LÝ BẢNG D_PHATSINH -----------------------------
    public long insertDataKHACHHANG(int THANG_HT, int NAM_HT, String MA_DVIQLY,
                                  int ID_HDON, String LOAI_HDON, String LOAI_PSINH,
                                  String MA_KHANG, String MA_KHTT,
                                  String TEN_KHANG, String TEN_KHANG1, String TEN_KHTT,
                                   String DCHI_KHANG, String DCHI_KHTT, int LOAI_KHANG,
                                   String MANHOM_KHANG, String NAM_PS, String THANG_PS,
                                   String KY_PS, String TIEN_PSINH, String THUE_PSINH,
                                   String MA_SOGCS, int STT_TRANG, String STT,
                                   int TTRANG_SSAI, int LAN_GIAO, String HTHUC_TTOAN,
                                   int LOAI_TBAO, String NGAY_PHANH, String DTHOAI_KHANG,
                                   String DIEN_THOAI, String DTHOAI_NONG, String DTHOAI_TRUC,
                                   String SO_SERY, String NGAY_DKY, String NGAY_CKY,
                                   String NGAY_GIAO, int SO_BBGIAO, String MA_TNGAN,
                                   String TIEN_NO, String THUE_NO, String NGAY_NOP,
                                   int TINH_TRANG, int TRANG_THAI, int SOLAN_BNHAN,
                                   int SOLAN_TBAO, String SO_CTO, float SO_HO,
                                   String DIEN_TTHU, String CS_DKY, String CS_CKY,
                                   String CTIET_GIA, String CTIET_DNTT, String CTIET_TIEN,
                                   String MA_NHANG, String EMAIL, String MA_DVIQLY_THUHO, String MA_TNGAN_THUHO){
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("THANG_HT", THANG_HT);
        values.put("NAM_HT", NAM_HT);
        values.put("MA_DVIQLY", MA_DVIQLY);
        values.put("ID_HDON", ID_HDON);
        values.put("LOAI_HDON", LOAI_HDON);
        values.put("LOAI_PSINH", LOAI_PSINH);
        values.put("MA_KHANG", MA_KHANG);
        values.put("MA_KHTT", MA_KHTT);
        values.put("TEN_KHANG", TEN_KHANG);
        values.put("TEN_KHANG1", TEN_KHANG1);
        values.put("TEN_KHTT", TEN_KHTT);
        values.put("DCHI_KHANG", DCHI_KHANG);
        values.put("DCHI_KHTT", DCHI_KHTT);
        values.put("LOAI_KHANG", LOAI_KHANG);
        values.put("MANHOM_KHANG", MANHOM_KHANG);
        values.put("NAM_PS", NAM_PS);
        values.put("THANG_PS", THANG_PS);
        values.put("KY_PS", KY_PS);
        values.put("TIEN_PSINH", TIEN_PSINH);
        values.put("THUE_PSINH", THUE_PSINH);
        values.put("MA_SOGCS", MA_SOGCS);
        values.put("STT_TRANG", STT_TRANG);
        values.put("STT", STT);
        values.put("TTRANG_SSAI", TTRANG_SSAI);
        values.put("LAN_GIAO", LAN_GIAO);
        values.put("HTHUC_TTOAN", HTHUC_TTOAN);
        values.put("LOAI_TBAO", LOAI_TBAO);
        values.put("NGAY_PHANH", NGAY_PHANH);
        values.put("DTHOAI_KHANG", DTHOAI_KHANG);
        values.put("DIEN_THOAI", DIEN_THOAI);
        values.put("DTHOAI_NONG", DTHOAI_NONG);
        values.put("DTHOAI_TRUC", DTHOAI_TRUC);
        values.put("SO_SERY", SO_SERY);
        values.put("NGAY_DKY", NGAY_DKY);
        values.put("NGAY_CKY", NGAY_CKY);
        values.put("NGAY_GIAO", NGAY_GIAO);
        values.put("SO_BBGIAO", SO_BBGIAO);
        values.put("MA_TNGAN", MA_TNGAN);
        values.put("TIEN_NO", TIEN_NO);
        values.put("THUE_NO", THUE_NO);
        values.put("NGAY_NOP", NGAY_NOP);
        values.put("TINH_TRANG", TINH_TRANG);
        values.put("TRANG_THAI", TRANG_THAI);
        values.put("SOLAN_BNHAN", SOLAN_BNHAN);
        values.put("SOLAN_TBAO", SOLAN_TBAO);
        values.put("SO_CTO", SO_CTO);
        values.put("SO_HO", SO_HO);
        values.put("DIEN_TTHU", DIEN_TTHU);
        values.put("CS_DKY", CS_DKY);
        values.put("CS_CKY", CS_CKY);
        values.put("CTIET_GIA", CTIET_GIA);
        values.put("CTIET_DNTT", CTIET_DNTT);
        values.put("CTIET_TIEN", CTIET_TIEN);
        values.put("MA_NHANG", MA_NHANG);
        values.put("EMAIL", EMAIL);
        values.put("MA_DVIQLY_THUHO", MA_DVIQLY_THUHO);
        values.put("MA_TNGAN_THUHO", MA_TNGAN_THUHO);
        long ins = database.insert(InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG, null, values);
        database.close();
        return ins;
    }

    public long updateChamNo(String MA_KHANG, String NGAY_NOP, int SOLAN_BNHAN, int SOLAN_TBAO, int TINH_TRANG, int TRANG_THAI) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NGAY_NOP", NGAY_NOP);
        values.put("SOLAN_BNHAN", SOLAN_BNHAN);
        values.put("SOLAN_TBAO", SOLAN_TBAO);
        values.put("TINH_TRANG", TINH_TRANG);
        values.put("TRANG_THAI", TRANG_THAI);
        long ins = database.update(InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG, values, "MA_KHANG=?", new String[]{MA_KHANG});
        database.close();
        return ins;
    }

    public long updateHuyCham(String MA_KHANG, String NGAY_NOP, int TINH_TRANG, int TRANG_THAI) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NGAY_NOP", NGAY_NOP);
        values.put("TINH_TRANG", TINH_TRANG);
        values.put("TRANG_THAI", TRANG_THAI);
        long ins = database.update(InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG, values, "MA_KHANG=?", new String[]{MA_KHANG});
        database.close();
        return ins;
    }

    public long updateTrangThai(String MA_KHANG, int TRANG_THAI) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TRANG_THAI", TRANG_THAI);
        long ins = database.update(InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG, values, "MA_KHANG=?", new String[]{MA_KHANG});
        database.close();
        return ins;
    }

    public long updateHuyCham(String ID_HDON, int TRANG_THAI, int TINH_TRANG, String NGAY_NOP) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TRANG_THAI", TRANG_THAI);
        values.put("TINH_TRANG", TINH_TRANG);
        values.put("NGAY_NOP", NGAY_NOP);
        long ins = database.update(InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG, values, "ID_HDON=?", new String[]{ID_HDON});
        database.close();
        return ins;
    }

    public long updateSoLanTB(String MA_KHANG, String SOLAN_TBAO) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("SOLAN_TBAO", SOLAN_TBAO);
        long ins = database.update(InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG, values, "MA_KHANG=?", new String[]{MA_KHANG});
        database.close();
        return ins;
    }

    public long rollBackHuyCham(String ID_HDON, int TRANG_THAI, int TINH_TRANG) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TRANG_THAI", TRANG_THAI);
        values.put("TINH_TRANG", TINH_TRANG);
        long ins = database.update(InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG, values, "ID_HDON=?", new String[]{ID_HDON});
        database.close();
        return ins;
    }

    public long updateNgayNop(String ID_HDON, String NGAY_NOP) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NGAY_NOP", NGAY_NOP);
        long ins = database.update(InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG, values, "ID_HDON=?", new String[]{ID_HDON});
        database.close();
        return ins;
    }

    public long updateTrangThaiByID(String ID_HDON, int TRANG_THAI) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TRANG_THAI", TRANG_THAI);
        long ins = database.update(InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG, values, "ID_HDON=?", new String[]{ID_HDON});
        database.close();
        return ins;
    }

    public int getSoLanTBao(String MA_KHANG){
        database = this.getReadableDatabase();
        String strQuery = "SELECT SOLAN_TBAO FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " WHERE MA_KHANG = '" + MA_KHANG + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getInt(0);
        }
        return 0;
    }

    public int getSoLanBNhan(String MA_KHANG){
        database = this.getReadableDatabase();
        String strQuery = "SELECT SOLAN_BNHAN FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " WHERE MA_KHANG = '" + MA_KHANG + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getInt(0);
        }
        return 0;
    }

    public String getIDHdon(String MA_KHANG){
        database = this.getReadableDatabase();
        String strQuery = "SELECT ID_HDON FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " WHERE MA_KHANG = '" + MA_KHANG + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getString(0);
        }
        return "";
    }

    public String getNgayNopHdon(String MA_KHANG){
        database = this.getReadableDatabase();
        String strQuery = "SELECT NGAY_NOP FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " WHERE MA_KHANG = '" + MA_KHANG + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getString(0);
        }
        return "";
    }

    public boolean checkMaKHang(String MA_KHANG){
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " WHERE MA_KHANG = '" + MA_KHANG + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getInt(0) > 0;//true: tồn tại
        }
        return false;
    }

    public long updateDataKHACHHANG(int ID, int THANG_HT, int NAM_HT, String MA_DVIQLY,
                                   int ID_HDON, String LOAI_HDON, String LOAI_PSINH,
                                   String MA_KHANG, String MA_KHTT,
                                   String TEN_KHANG, String TEN_KHANG1, String TEN_KHTT,
                                   String DCHI_KHANG, String DCHI_KHTT, int LOAI_KHANG,
                                   String MANHOM_KHANG, String NAM_PS, String THANG_PS,
                                   String KY_PS, String TIEN_PSINH, String THUE_PSINH,
                                   String MA_SOGCS, int STT_TRANG, String STT,
                                   int TTRANG_SSAI, int LAN_GIAO, String HTHUC_TTOAN,
                                   int LOAI_TBAO, String NGAY_PHANH, String DTHOAI_KHANG,
                                   String DIEN_THOAI, String DTHOAI_NONG, String DTHOAI_TRUC,
                                   String SO_SERY, String NGAY_DKY, String NGAY_CKY,
                                   String NGAY_GIAO, int SO_BBGIAO, String MA_TNGAN,
                                   String TIEN_NO, String THUE_NO, String NGAY_NOP,
                                   int TINH_TRANG, int TRANG_THAI, int SOLAN_BNHAN,
                                   int SOLAN_TBAO, String SO_CTO, float SO_HO,
                                   String DIEN_TTHU, String CS_DKY, String CS_CKY,
                                   String CTIET_GIA, String CTIET_DNTT, String CTIET_TIEN,
                                   String MA_NHANG, String EMAIL, String MA_DVIQLY_THUHO, String MA_TNGAN_THUHO){
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("THANG_HT", THANG_HT);
        values.put("NAM_HT", NAM_HT);
        values.put("MA_DVIQLY", MA_DVIQLY);
        values.put("ID_HDON", ID_HDON);
        values.put("LOAI_HDON", LOAI_HDON);
        values.put("LOAI_PSINH", LOAI_PSINH);
        values.put("MA_KHANG", MA_KHANG);
        values.put("MA_KHTT", MA_KHTT);
        values.put("TEN_KHANG", TEN_KHANG);
        values.put("TEN_KHANG1", TEN_KHANG1);
        values.put("TEN_KHTT", TEN_KHTT);
        values.put("DCHI_KHANG", DCHI_KHANG);
        values.put("DCHI_KHTT", DCHI_KHTT);
        values.put("LOAI_KHANG", LOAI_KHANG);
        values.put("MANHOM_KHANG", MANHOM_KHANG);
        values.put("NAM_PS", NAM_PS);
        values.put("THANG_PS", THANG_PS);
        values.put("KY_PS", KY_PS);
        values.put("TIEN_PSINH", TIEN_PSINH);
        values.put("THUE_PSINH", THUE_PSINH);
        values.put("MA_SOGCS", MA_SOGCS);
        values.put("STT_TRANG", STT_TRANG);
        values.put("STT", STT);
        values.put("TTRANG_SSAI", TTRANG_SSAI);
        values.put("LAN_GIAO", LAN_GIAO);
        values.put("HTHUC_TTOAN", HTHUC_TTOAN);
        values.put("LOAI_TBAO", LOAI_TBAO);
        values.put("NGAY_PHANH", NGAY_PHANH);
        values.put("DTHOAI_KHANG", DTHOAI_KHANG);
        values.put("DIEN_THOAI", DIEN_THOAI);
        values.put("DTHOAI_NONG", DTHOAI_NONG);
        values.put("DTHOAI_TRUC", DTHOAI_TRUC);
        values.put("SO_SERY", SO_SERY);
        values.put("NGAY_DKY", NGAY_DKY);
        values.put("NGAY_CKY", NGAY_CKY);
        values.put("NGAY_GIAO", NGAY_GIAO);
        values.put("SO_BBGIAO", SO_BBGIAO);
        values.put("MA_TNGAN", MA_TNGAN);
        values.put("TIEN_NO", TIEN_NO);
        values.put("THUE_NO", THUE_NO);
        values.put("NGAY_NOP", NGAY_NOP);
        values.put("TINH_TRANG", TINH_TRANG);
        values.put("TRANG_THAI", TRANG_THAI);
        values.put("SOLAN_BNHAN", SOLAN_BNHAN);
        values.put("SOLAN_TBAO", SOLAN_TBAO);
        values.put("SO_CTO", SO_CTO);
        values.put("SO_HO", SO_HO);
        values.put("DIEN_TTHU", DIEN_TTHU);
        values.put("CS_DKY", CS_DKY);
        values.put("CS_CKY", CS_CKY);
        values.put("CTIET_GIA", CTIET_GIA);
        values.put("CTIET_DNTT", CTIET_DNTT);
        values.put("CTIET_TIEN", CTIET_TIEN);
        values.put("MA_NHANG", MA_NHANG);
        values.put("EMAIL", EMAIL);
        values.put("MA_DVIQLY_THUHO", MA_DVIQLY_THUHO);
        values.put("MA_TNGAN_THUHO", MA_TNGAN_THUHO);
        long ins = database.update(InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG, values, "ID=?", new String[]{"" + ID});
        database.close();
        return ins;
    }

    public long deleteAllDataKHACHHANG() {
        database = this.getWritableDatabase();
        return database.delete(InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG, null, null);
    }

    public long deleteAllDataKHACHHANGByMaTN(String MA_TNGAN) {
        database = this.getWritableDatabase();
        return database.delete(InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG, "MA_TNGAN=?", new String[]{MA_TNGAN});
    }

    public long deleteDataKHACHHANGByID(int ID) {
        database = this.getWritableDatabase();
        long ins = database.delete(InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG, "ID=?", new String[]{"" + ID});
        database.close();
        return ins;
    }

    public long deleteDataKHACHHANGBySoBB(String SO_BBGIAO) {
        database = this.getWritableDatabase();
        long ins = database.delete(InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG, "SO_BBGIAO=?", new String[]{SO_BBGIAO});
        database.close();
        return ins;
    }

    public Cursor getAllDataKHACHHANG(){
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG;
        return database.rawQuery(strQuery, null);
    }

    public Cursor getAllSoGCS(){
        database = this.getReadableDatabase();
        String strQuery = "SELECT DISTINCT(MA_SOGCS) FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG;
        return database.rawQuery(strQuery, null);
    }

    public Cursor getAllDataKHACHHANGChuaCham(String MA_TNGAN, int TINH_TRANG){
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " where MA_TNGAN = '" + MA_TNGAN + "' AND TINH_TRANG = " + TINH_TRANG;
        return database.rawQuery(strQuery, null);
    }

    public Cursor getAllDataKHACHHANGByTrangThai(String MA_TNGAN, int TRANG_THAI){
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " where MA_TNGAN = '" + MA_TNGAN + "' AND TRANG_THAI = " + TRANG_THAI;
        return database.rawQuery(strQuery, null);
    }

    public Cursor getAllDataKHACHHANGByTrangThai2(String MA_TNGAN, int TRANG_THAI){
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " where MA_TNGAN = '" + MA_TNGAN + "' AND TRANG_THAI > " + TRANG_THAI;
        return database.rawQuery(strQuery, null);
    }

    public Cursor getAllDataKHACHHANGChamOff(String MA_TNGAN, int TRANG_THAI){
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " where MA_TNGAN = '" + MA_TNGAN + "' AND TRANG_THAI = " + TRANG_THAI;
        return database.rawQuery(strQuery, null);
    }

    public Cursor getAllDataKHACHHANGByMaKH(String MA_KHANG){
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " where MA_KHANG = '" + MA_KHANG + "'";
        return database.rawQuery(strQuery, null);
    }

    public Cursor getAllDataKHACHHANGChuaChamAndSo(String MA_TNGAN, int TINH_TRANG, String MA_SOGCS){
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " where MA_TNGAN = '" + MA_TNGAN + "' AND TINH_TRANG = " + TINH_TRANG + " AND MA_SOGCS = '" + MA_SOGCS + "'";
        return database.rawQuery(strQuery, null);
    }

    public Cursor getAllDataKHACHHANGChuaChamAndSoByTrangThai(String MA_TNGAN, int TRANG_THAI, String MA_SOGCS){
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " where MA_TNGAN = '" + MA_TNGAN + "' AND TRANG_THAI = " + TRANG_THAI + " AND MA_SOGCS = '" + MA_SOGCS + "'";
        return database.rawQuery(strQuery, null);
    }

    public Cursor getAllDataKHACHHANGChuaChamAndSoByTrangThai2(String MA_TNGAN, int TRANG_THAI, String MA_SOGCS){
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " where MA_TNGAN = '" + MA_TNGAN + "' AND TRANG_THAI > " + TRANG_THAI + " AND MA_SOGCS = '" + MA_SOGCS + "'";
        return database.rawQuery(strQuery, null);
    }

    public Cursor getAllDataKHACHHANGDaChamBySo(String MA_SOGCS){
        database = this.getReadableDatabase();
        String strQuery = "SELECT STT, TEN_KHANG, TIEN_NO FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " where MA_SOGCS = '" + MA_SOGCS + "' AND TINH_TRANG = 1";
        return database.rawQuery(strQuery, null);
    }

    public Cursor getAllDataSo(){
        database = this.getReadableDatabase();
        String strQuery = "SELECT DISTINCT(MA_SOGCS) FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG;
        return database.rawQuery(strQuery, null);
    }

    public Cursor getDataSoBBGiao(){
        database = this.getReadableDatabase();
        String strQuery = "SELECT DISTINCT(SO_BBGIAO) FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG;
        return database.rawQuery(strQuery, null);
    }

    public boolean checkChamHTK(String MA_KHANG){//true: chấm HKT
        database = this.getReadableDatabase();
        String strQuery = "SELECT TRANG_THAI FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " WHERE MA_KHANG = '" + MA_KHANG + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            if(c.getInt(0) > 5){
                return true;
            }
        }
        return false;
    }

    public String getTenKHByMa(String MA_KHANG){//true: chấm HKT
        database = this.getReadableDatabase();
        String strQuery = "SELECT TEN_KHANG FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " WHERE MA_KHANG = '" + MA_KHANG + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getString(0);
        }
        return "";
    }

    public int getTinhTrangByMaKH(String MA_KHANG) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT TINH_TRANG FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " where MA_KHANG = '" + MA_KHANG + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getInt(0);
        }
        return 0;
    }

    public int getTrangThaiByMaKH(String MA_KHANG) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT TRANG_THAI FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " where MA_KHANG = '" + MA_KHANG + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getInt(0);
        }
        return 0;
    }

    public int getTrangThaiByID(String ID_HDON) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT TRANG_THAI FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " where ID_HDON = '" + ID_HDON + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getInt(0);
        }
        return 0;
    }

    public int countPS(String MA_TNGAN) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " where MA_TNGAN = '" + MA_TNGAN + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getInt(0);
        }
        return 0;
    }

    public double sumTienNo(String MA_TNGAN) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT SUM(TIEN_NO) FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " where MA_TNGAN = '" + MA_TNGAN + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getDouble(0);
        }
        return 0;
    }

    public double sumThueNo(String MA_TNGAN) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT SUM(THUE_NO) FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " where MA_TNGAN = '" + MA_TNGAN + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getDouble(0);
        }
        return 0;
    }

    public int countPSBySo(String MA_SOGCS) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " where MA_SOGCS = '" + MA_SOGCS + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getInt(0);
        }
        return 0;
    }

    public double sumTienNoBySo(String MA_SOGCS) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT SUM(TIEN_NO) FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " where MA_SOGCS = '" + MA_SOGCS + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getDouble(0);
        }
        return 0;
    }

    public double sumThueNoBySo(String MA_SOGCS) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT SUM(THUE_NO) FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " where MA_SOGCS = '" + MA_SOGCS + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getDouble(0);
        }
        return 0;
    }

    public double sumTienNoByTinhTrang(String MA_TNGAN, int TINH_TRANG) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT SUM(TIEN_NO) FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG
                + " WHERE MA_TNGAN = '" + MA_TNGAN + "' AND TINH_TRANG = " + TINH_TRANG;
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getDouble(0);
        }
        return 0;
    }

    public double sumThueNoByTinhTrang(String MA_TNGAN, int TINH_TRANG) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT SUM(THUE_NO) FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG
                + " WHERE MA_TNGAN = '" + MA_TNGAN + "' AND TINH_TRANG = " + TINH_TRANG;
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getDouble(0);
        }
        return 0;
    }

    public double sumTienNoByTrangThai(String MA_TNGAN, int TRANG_THAI) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT SUM(TIEN_NO) FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG
                + " WHERE MA_TNGAN = '" + MA_TNGAN + "' AND TRANG_THAI = " + TRANG_THAI;
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getDouble(0);
        }
        return 0;
    }

    public double sumThueNoByTrangThai(String MA_TNGAN, int TRANG_THAI) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT SUM(THUE_NO) FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG
                + " WHERE MA_TNGAN = '" + MA_TNGAN + "' AND TRANG_THAI = " + TRANG_THAI;
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getDouble(0);
        }
        return 0;
    }

    public double sumTienNoByTrangThai2(String MA_TNGAN, int TRANG_THAI) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT SUM(TIEN_NO) FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG
                + " WHERE MA_TNGAN = '" + MA_TNGAN + "' AND TRANG_THAI > " + TRANG_THAI;
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getDouble(0);
        }
        return 0;
    }

    public double sumThueNoByTrangThai2(String MA_TNGAN, int TRANG_THAI) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT SUM(THUE_NO) FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG
                + " WHERE MA_TNGAN = '" + MA_TNGAN + "' AND TRANG_THAI > " + TRANG_THAI;
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getDouble(0);
        }
        return 0;
    }

    public double sumTienNoByTinhTrangAndSo(String MA_TNGAN, int TRANG_THAI, String MA_SOGCS) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT SUM(TIEN_NO) FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG
                + " WHERE MA_TNGAN = '" + MA_TNGAN + "' AND TRANG_THAI = " + TRANG_THAI + " AND MA_SOGCS = '" + MA_SOGCS + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getDouble(0);
        }
        return 0;
    }

    public double sumThueNoByTinhTrangAndSo(String MA_TNGAN, int TRANG_THAI, String MA_SOGCS) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT SUM(THUE_NO) FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG
                + " WHERE MA_TNGAN = '" + MA_TNGAN + "' AND TRANG_THAI = " + TRANG_THAI + " AND MA_SOGCS = '" + MA_SOGCS + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getDouble(0);
        }
        return 0;
    }

    public double sumTienNoByTrangThaiAndSo2(String MA_TNGAN, int TRANG_THAI, String MA_SOGCS) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT SUM(TIEN_NO) FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG
                + " WHERE MA_TNGAN = '" + MA_TNGAN + "' AND TRANG_THAI > " + TRANG_THAI + " AND MA_SOGCS = '" + MA_SOGCS + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getDouble(0);
        }
        return 0;
    }

    public double sumThueNoByTrangThaiAndSo2(String MA_TNGAN, int TRANG_THAI, String MA_SOGCS) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT SUM(THUE_NO) FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG
                + " WHERE MA_TNGAN = '" + MA_TNGAN + "' AND TRANG_THAI > " + TRANG_THAI + " AND MA_SOGCS = '" + MA_SOGCS + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getDouble(0);
        }
        return 0;
    }

    public double sumTienNoByTrangThaiAndSo(String MA_TNGAN, int TRANG_THAI, String MA_SOGCS) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT SUM(TIEN_NO) FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG
                + " WHERE MA_TNGAN = '" + MA_TNGAN + "' AND TRANG_THAI = " + TRANG_THAI + " AND MA_SOGCS = '" + MA_SOGCS + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getDouble(0);
        }
        return 0;
    }

    public double sumThueNoByTrangThaiAndSo(String MA_TNGAN, int TRANG_THAI, String MA_SOGCS) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT SUM(THUE_NO) FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG
                + " WHERE MA_TNGAN = '" + MA_TNGAN + "' AND TRANG_THAI = " + TRANG_THAI + " AND MA_SOGCS = '" + MA_SOGCS + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getDouble(0);
        }
        return 0;
    }

    public int countHDThu(String MA_TNGAN) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " where MA_TNGAN = '" + MA_TNGAN + "' AND TINH_TRANG = 1";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getInt(0);
        }
        return 0;
    }

    public int countHDTon(String MA_TNGAN) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " where MA_TNGAN = '" + MA_TNGAN + "' AND TINH_TRANG = 0";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getInt(0);
        }
        return 0;
    }

    public double sumTienHDThu(String MA_TNGAN) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT SUM(TIEN_NO) FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " where MA_TNGAN = '" + MA_TNGAN + "' AND TINH_TRANG = 1";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getDouble(0);
        }
        return 0;
    }

    public double sumThueHDThu(String MA_TNGAN) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT SUM(THUE_NO) FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " where MA_TNGAN = '" + MA_TNGAN + "' AND TINH_TRANG = 1";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getDouble(0);
        }
        return 0;
    }

    public double sumTienHDTon(String MA_TNGAN) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT SUM(TIEN_NO) FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " where MA_TNGAN = '" + MA_TNGAN + "' AND TINH_TRANG = 0";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getDouble(0);
        }
        return 0;
    }

    public double sumThueHDTon(String MA_TNGAN) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT SUM(THUE_NO) FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " where MA_TNGAN = '" + MA_TNGAN + "' AND TINH_TRANG = 0";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getDouble(0);
        }
        return 0;
    }

    public double sumTienOff() {
        database = this.getReadableDatabase();
        String strQuery = "SELECT SUM(TIEN_NO) FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " where TRANG_THAI = 2";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getDouble(0);
        }
        return 0;
    }

    public double sumThueOff() {
        database = this.getReadableDatabase();
        String strQuery = "SELECT SUM(THUE_NO) FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " where TRANG_THAI = 2";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getDouble(0);
        }
        return 0;
    }

    public double sumTienOn() {
        database = this.getReadableDatabase();
        String strQuery = "SELECT SUM(TIEN_NO) FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " where TRANG_THAI = 3";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getDouble(0);
        }
        return 0;
    }

    public double sumThueOn() {
        database = this.getReadableDatabase();
        String strQuery = "SELECT SUM(THUE_NO) FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " where TRANG_THAI = 3";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getDouble(0);
        }
        return 0;
    }

    public double sumTienHTK() {
        database = this.getReadableDatabase();
        String strQuery = "SELECT SUM(TIEN_NO) FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " where TRANG_THAI > 5";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getDouble(0);
        }
        return 0;
    }

    public double sumThueHTK() {
        database = this.getReadableDatabase();
        String strQuery = "SELECT SUM(THUE_NO) FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " where TRANG_THAI > 5";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getDouble(0);
        }
        return 0;
    }

    public int countHDChuaDongBo(String MA_TNGAN) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " where MA_TNGAN = '" + MA_TNGAN + "' AND TRANG_THAI = 2";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getInt(0);
        }
        return 0;
    }

    public int countHDOnLine(String MA_TNGAN) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " where MA_TNGAN = '" + MA_TNGAN + "' AND TRANG_THAI = 3";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getInt(0);
        }
        return 0;
    }

    public int countHDThuHTK(String MA_TNGAN) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + InvoiceConstantVariables.TABLE_NAME_POS_KHACHHANG + " where MA_TNGAN = '" + MA_TNGAN + "' AND (TRANG_THAI = 6 OR TRANG_THAI = 7)";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getInt(0);
        }
        return 0;
    }

    //--------------------------- XỬ LÝ BẢNG D_LOG ----------------------------------
    public long insertDataLOG(String THOI_GIAN, String TEN_HAM,
                                  String GIA_TRI,String THONG_BAO){
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("THOI_GIAN", THOI_GIAN);
        values.put("TEN_HAM", TEN_HAM);
        values.put("GIA_TRI", GIA_TRI);
        values.put("THONG_BAO", THONG_BAO);
        long ins = database.insert(InvoiceConstantVariables.TABLE_NAME_LOG, null, values);
        database.close();
        return ins;
    }

    public long updateDataLOG(int ID, String THOI_GIAN, String TEN_HAM,
                              String GIA_TRI,String THONG_BAO){
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("THOI_GIAN", THOI_GIAN);
        values.put("TEN_HAM", TEN_HAM);
        values.put("GIA_TRI", GIA_TRI);
        values.put("THONG_BAO", THONG_BAO);
        long ins = database.update(InvoiceConstantVariables.TABLE_NAME_LOG, values, "ID=?", new String[]{"" + ID});
        database.close();
        return ins;
    }

    public long deleteAllDataLOG() {
        database = this.getWritableDatabase();
        long ins = database.delete(InvoiceConstantVariables.TABLE_NAME_LOG, null, null);
        database.close();
        return ins;
    }

    public long deleteDataLOGByID(int ID) {
        database = this.getWritableDatabase();
        long ins = database.delete(InvoiceConstantVariables.TABLE_NAME_LOG, "ID=?", new String[]{"" + ID});
        database.close();
        return ins;
    }

    public Cursor getAllDataLOG(){
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + InvoiceConstantVariables.TABLE_NAME_LOG;
        return database.rawQuery(strQuery, null);
    }
}
