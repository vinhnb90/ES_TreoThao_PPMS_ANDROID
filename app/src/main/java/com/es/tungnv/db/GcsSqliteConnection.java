package com.es.tungnv.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.es.tungnv.utils.GcsCommon;
import com.es.tungnv.utils.GcsConstantVariables;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by TUNGNV on 6/7/2016.
 */
public class GcsSqliteConnection extends SQLiteOpenHelper{

    private SQLiteDatabase database;
    private String db = null;
    
    public GcsSqliteConnection(Context context) {
        super(context, Environment.getExternalStorageDirectory() + GcsConstantVariables.PROGRAM_DATA_PATH + GcsConstantVariables.DATABASE_NAME, null, GcsConstantVariables.DATABASE_VERSION);
        SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory() + GcsConstantVariables.PROGRAM_DATA_PATH + GcsConstantVariables.DATABASE_NAME, null);
    }

    public GcsSqliteConnection(Context context, String db_name, String db_folder) {
        super(context, db_folder + "/" + db_name, null, GcsConstantVariables.DATABASE_VERSION);
        GcsConstantVariables.DATABASE_NAME = db_name;
        db = db_folder;
        DbConnect(context, db + "/" + GcsConstantVariables.DATABASE_NAME);
    }

    public void GetWritableDatabase(){
        database = this.getWritableDatabase();
    }

    public void BeginTransaction(){
        database.beginTransaction();
    }

    public void SetTransactionSuccessful(){
        database.setTransactionSuccessful();
    }

    public void EndTransaction(){
        database.endTransaction();
    }

    public long delete_array_so(String[] arr_ten_so ) {
        int result = 0;
        String list_so = "";
        for(String item : arr_ten_so){
            list_so += ",'"+item+"'";
        }
        list_so = list_so.trim().substring(1);
        String where = null;
        String[] whereArgs = null;//{list_so}

        database = this.getWritableDatabase();
        //
        where = "TEN_FILE IN ("+list_so+")";
        result = database.delete(GcsConstantVariables.TABLE_NAME_CHISO, where, whereArgs);

        where = "TEN_SOGCS IN ("+list_so+")";
        result = database.delete(GcsConstantVariables.TABLE_NAME_SO, where, whereArgs);

        where = "so IN ("+list_so+")";
        result = database.delete(GcsConstantVariables.TABLE_NAME_INDEX, where, whereArgs);

        return result;
    }

    public ArrayList<LinkedHashMap<String, String>> ConvertDataFromSqliteToArrayList(String strQuery){
        database = this.getReadableDatabase();
        Cursor c = database.rawQuery(strQuery, null);
        ArrayList<LinkedHashMap<String, String>> ListSqliteData = new ArrayList<LinkedHashMap<String, String>>();
        if (c.moveToFirst()) {
            do {
                LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                for (int j = 1; j < GcsConstantVariables.COLARR.length; j++) {
                    map.put(GcsConstantVariables.COLARR[j], c.getString(j));
                }
                String CHECK_DSOAT = c.getString(c.getColumnIndex("STR_CHECK_DSOAT"));
                if(CHECK_DSOAT == null || CHECK_DSOAT.equals("")){
                    CHECK_DSOAT = "CHUA_DOI_SOAT";
                }
                map.put("STR_CHECK_DSOAT", CHECK_DSOAT);
                ListSqliteData.add(map);
            } while (c.moveToNext());
        }
        return ListSqliteData;
    }

    public String insert_arraylist_to_db(String[] arr_ten_so, ArrayList<LinkedHashMap<String, String>> list, String MA_DQL ) {
        try{
            @SuppressWarnings("unused")
            long result = -1;
            if(database == null || !database.isOpen()){
                return "Chưa kết nối tới cơ sở dữ liệu.";
            }

            if(list.size() == 0){
                return "HAVE_NO_DATA";
            }

            ContentValues values = new ContentValues();

            // chèn từng công tơ của sổ vào bảng GCS_CHISO_HHU
            database = this.getWritableDatabase();
            for(LinkedHashMap<String, String> list_item : list){
                values = new ContentValues();
                for(int i = 1; i < GcsConstantVariables.COLARR.length; i++){
                    String colname = GcsConstantVariables.COLARR[i];
//				for(String colname : Common.colArr2){
                    //bỏ qua cột tự tăng ID_SQLITE
                    if(colname.contains("ID_SQLITE")){
                        continue;
                    }
                    values.put(colname, list_item.get(colname));
                }
                String CHECK_DSOAT = list_item.get("STR_CHECK_DSOAT");
                if(CHECK_DSOAT == null || CHECK_DSOAT.equals("")){
                    CHECK_DSOAT = "CHUA_DOI_SOAT";
                }
                values.put("STR_CHECK_DSOAT", CHECK_DSOAT);
                result = database.insert(GcsConstantVariables.TABLE_NAME_CHISO, null, values);
            }

            for(String TEN_FILE : arr_ten_so){
                // chèn vào bảng GCS_SO_NVGCS
                values = new ContentValues();
                values.put("MA_DVIQLY", list.get(0).get("MA_DVIQLY"));
                values.put("MA_NVGCS", list.get(0).get("MA_NVGCS"));
                values.put("TEN_SOGCS", TEN_FILE);
                values.put("MA_DQL", MA_DQL);
                result= database.insert(GcsConstantVariables.TABLE_NAME_SO, null, values);

                // chèn vào bảng gcsindex
                values = new ContentValues();
                values.put("so", TEN_FILE);
                values.put("vitri", 0);
                result=  database.insert(GcsConstantVariables.TABLE_NAME_INDEX, null, values);
            }

            return null;
        }
        catch(Exception e){
            return e.getMessage();
        }
    }

    private void DbConnect(Context context, String db_fullname){
        try{
            // Kiểm tra file có bị lỗi ko
            SQLLOpenHelper dbHelper = new SQLLOpenHelper(context, db_fullname, null, 1, new DbErrorHandler());
            database = dbHelper.getWritableDatabase();
            database.close();

            // Mở db
            SQLiteDatabase.openDatabase(db_fullname, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);

        }catch(Exception ex){
            boolean backup_result = GcsCommon.CreateBackup("MALFORM");
            if(backup_result){
                SQLiteDatabase.openOrCreateDatabase(db_fullname, null);
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(GcsConstantVariables.CREATE_TABLE_INDEX);
        db.execSQL(GcsConstantVariables.CREATE_TABLE_ROUTE);
        db.execSQL(GcsConstantVariables.CREATE_TABLE_SO);
        db.execSQL(GcsConstantVariables.CREATE_TABLE_CHISO);
        db.execSQL(GcsConstantVariables.CREATE_TABLE_CUSTOMER);
        db.execSQL(GcsConstantVariables.CREATE_TABLE_LOG_DELETE);
        db.execSQL(GcsConstantVariables.CREATE_TABLE_LOG_CS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + GcsConstantVariables.TABLE_NAME_INDEX);
        db.execSQL("DROP TABLE IF EXISTS " + GcsConstantVariables.TABLE_NAME_ROUTE);
        db.execSQL("DROP TABLE IF EXISTS " + GcsConstantVariables.TABLE_NAME_SO);
        db.execSQL("DROP TABLE IF EXISTS " + GcsConstantVariables.TABLE_NAME_CHISO);
        db.execSQL("DROP TABLE IF EXISTS " + GcsConstantVariables.TABLE_NAME_CUSTOMER);
        db.execSQL("DROP TABLE IF EXISTS " + GcsConstantVariables.TABLE_NAME_LOG_DELETE);
        db.execSQL("DROP TABLE IF EXISTS " + GcsConstantVariables.TABLE_NAME_LOG_CS);
    }

    // Xử lý bảng chỉ số
    public Cursor getMaQuyen(){
        database = this.getReadableDatabase();
        String strQuery = "SELECT DISTINCT(MA_QUYEN) FROM " + GcsConstantVariables.TABLE_NAME_CHISO;
        return database.rawQuery(strQuery, null);
    }

    public int getDaGhi(String MA_QUYEN){
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + GcsConstantVariables.TABLE_NAME_CHISO + " WHERE MA_QUYEN = '" + MA_QUYEN + "' AND CS_MOI <> 0 OR TTR_MOI <> NULL";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getInt(0);
        }
        return 0;
    }

    public Cursor getCSoAndTTR(String MA_QUYEN){
        database = this.getReadableDatabase();
        String strQuery = "SELECT CS_MOI, TTR_MOI FROM " + GcsConstantVariables.TABLE_NAME_CHISO + " WHERE MA_QUYEN = '" + MA_QUYEN + "'";
        return database.rawQuery(strQuery, null);
    }

    public Cursor getCSoAndTTRByMaTram(String MA_TRAM){
        database = this.getReadableDatabase();
        String strQuery = "SELECT CS_MOI, TTR_MOI FROM " + GcsConstantVariables.TABLE_NAME_CHISO + " WHERE MA_TRAM = '" + MA_TRAM + "'";
        return database.rawQuery(strQuery, null);
    }

    public int getSoLuongBanGhi(String MA_QUYEN){
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + GcsConstantVariables.TABLE_NAME_CHISO + " WHERE MA_QUYEN = '" + MA_QUYEN + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getInt(0);
        }
        return 0;
    }

    public int getSoLuongBanGhiByMaTram(String MA_TRAM){
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + GcsConstantVariables.TABLE_NAME_CHISO + " WHERE MA_TRAM = '" + MA_TRAM + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getInt(0);
        }
        return 0;
    }

    public int countBanGhi(String MA_CTO){
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + GcsConstantVariables.TABLE_NAME_CHISO + " WHERE MA_CTO = '" + MA_CTO + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getInt(0);
        }
        return 1;
    }

    public int countBanGhi(){
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + GcsConstantVariables.TABLE_NAME_CHISO;
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getInt(0);
        }
        return 1;
    }

    public Cursor getDataByMaQuyen(String MA_QUYEN){
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + GcsConstantVariables.TABLE_NAME_CHISO + " WHERE MA_QUYEN = '" + MA_QUYEN + "'";
        return database.rawQuery(strQuery, null);
    }

    public Cursor getDataTram(){
        database = this.getReadableDatabase();
        String strQuery = "SELECT DISTINCT(MA_TRAM) FROM " + GcsConstantVariables.TABLE_NAME_CHISO;
        return database.rawQuery(strQuery, null);
    }

    public Cursor getTenFile(){
        database = this.getReadableDatabase();
        String strQuery = "SELECT DISTINCT(TEN_FILE) FROM " + GcsConstantVariables.TABLE_NAME_CHISO;
        return database.rawQuery(strQuery, null);
    }

    public Float sumDTTByTram(String MA_TRAM){
        database = this.getReadableDatabase();
        String strQuery = "SELECT SUM(SL_MOI + SL_THAO) FROM " + GcsConstantVariables.TABLE_NAME_CHISO + " WHERE MA_TRAM = '" + MA_TRAM + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getFloat(0);
        }
        return 0F;
    }

    public Cursor getAllDataGCSKhongDat(String fileName) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + GcsConstantVariables.TABLE_NAME_CHISO
                + " WHERE STR_CHECK_DSOAT <> 'CHECK' AND STR_CHECK_DSOAT <> 'CTO_DTU' AND MA_QUYEN = '"
                + fileName + "'";
        return database.rawQuery(strQuery, null);
    }

    public Cursor getDataByMaQuyen(String MA_QUYEN, String MA_CTO, String LOAI_BCS){
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + GcsConstantVariables.TABLE_NAME_CHISO + " WHERE MA_QUYEN = '"
                + MA_QUYEN + "' AND MA_CTO = '" + MA_CTO + "' AND LOAI_BCS = '" + LOAI_BCS + "'";
        return database.rawQuery(strQuery, null);
    }

    public String getTenFileByMaQuyen(String MA_QUYEN) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT DISTINCT TEN_FILE FROM " + GcsConstantVariables.TABLE_NAME_CHISO + " WHERE MA_QUYEN = '" + MA_QUYEN + "'";
        Cursor c =  database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getString(0);
        }
        return "";
    }

    public Double sumSLHC(String MA_QUYEN) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT SUM(SL_MOI) FROM " + GcsConstantVariables.TABLE_NAME_CHISO +
                " WHERE MA_QUYEN = '" + MA_QUYEN + "' AND LOAI_BCS <> 'SG' AND LOAI_BCS <> 'VC'";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getDouble(0);
        }
        return 0d;
    }

    public Double sumSLVC(String MA_QUYEN) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT SUM(SL_MOI) FROM " + GcsConstantVariables.TABLE_NAME_CHISO +
                " WHERE MA_QUYEN = '" + MA_QUYEN + "' AND LOAI_BCS = 'VC'";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getDouble(0);
        }
        return 0d;
    }

    public int sumTTBT(String MA_QUYEN) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT COUNT(*) FROM " + GcsConstantVariables.TABLE_NAME_CHISO +
                " WHERE MA_QUYEN = '" + MA_QUYEN + "' AND TTR_MOI <> NULL";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getInt(0);
        }
        return 0;
    }

    public long updateChiSo(String ID_SQLITE, float CS_MOI, String TTR_MOI, float SL_MOI,
                               String THOI_GIAN, String X, String Y, String TTHAI_DBO, String TT_KHAC, String PMAX,
                               String NGAY_PMAX, String STR_CHECK_DSOAT) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("CS_MOI", CS_MOI);
        values.put("TTR_MOI", TTR_MOI);
        values.put("SL_MOI", SL_MOI);
        values.put("THOI_GIAN", THOI_GIAN);
        values.put("X", X);
        values.put("Y", Y);
        values.put("TTHAI_DBO", TTHAI_DBO);
        values.put("TT_KHAC", TT_KHAC);
        values.put("PMAX", PMAX);
        values.put("NGAY_PMAX", NGAY_PMAX);
        values.put("STR_CHECK_DSOAT", STR_CHECK_DSOAT);
        return database.update(GcsConstantVariables.TABLE_NAME_CHISO, values, "ID_SQLITE=?", new String[] { ID_SQLITE });
    }

    public long updateChiSo(String ID_SQLITE, float CS_MOI, String TTR_MOI, float SL_MOI,
                            String THOI_GIAN, String X, String Y, String PMAX, String NGAY_PMAX) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("CS_MOI", CS_MOI);
        values.put("TTR_MOI", TTR_MOI);
        values.put("SL_MOI", SL_MOI);
        values.put("THOI_GIAN", THOI_GIAN);
        values.put("X", X);
        values.put("Y", Y);
        values.put("PMAX", PMAX);
        values.put("NGAY_PMAX", NGAY_PMAX);
        return database.update(GcsConstantVariables.TABLE_NAME_CHISO, values, "ID_SQLITE=?", new String[] { ID_SQLITE });
    }

    public long updatePmax(String ID_SQLITE, String PMAX, String NGAY_PMAX) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("PMAX", PMAX);
        values.put("NGAY_PMAX", NGAY_PMAX);
        return database.update(GcsConstantVariables.TABLE_NAME_CHISO, values, "ID_SQLITE=?", new String[] { ID_SQLITE });
    }

    public long updateGhiChu(String ID_SQLITE, String GHI_CHU) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("GHICHU", GHI_CHU);
        return database.update(GcsConstantVariables.TABLE_NAME_CHISO, values, "ID_SQLITE=?", new String[] { ID_SQLITE });
    }

    public long updateDSOAT(String SERY_CTO, String STR_CHECK_DSOAT) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("STR_CHECK_DSOAT", STR_CHECK_DSOAT);
        return database.update(GcsConstantVariables.TABLE_NAME_CHISO, values, "SERY_CTO=?", new String[] { SERY_CTO });
    }

    public String getDSoat(String ID_SQLITE) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT STR_CHECK_DSOAT FROM " + GcsConstantVariables.TABLE_NAME_CHISO + " WHERE ID_SQLITE = '" + ID_SQLITE + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getString(0);
        }
        return "";
    }

    public String getGhiChu(String ID_SQLITE) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT GHICHU FROM " + GcsConstantVariables.TABLE_NAME_CHISO + " WHERE ID_SQLITE = '" + ID_SQLITE + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getString(0);
        }
        return "";
    }

    public Cursor getPmax(String ID_SQLITE) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT PMAX, NGAY_PMAX FROM " + GcsConstantVariables.TABLE_NAME_CHISO + " WHERE ID_SQLITE = '" + ID_SQLITE + "'";
        return database.rawQuery(strQuery, null);
    }

    public Cursor getDataSameSoCto(String SERY_CTO){
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + GcsConstantVariables.TABLE_NAME_CHISO + " WHERE SERY_CTO = '" + SERY_CTO + "' AND LOAI_BCS <> 'VC'";
        return database.rawQuery(strQuery, null);
    }

    public float getSlmoiById(String ID_SQLITE){
        database = this.getReadableDatabase();
        String strQuery = "SELECT SL_MOI FROM " + GcsConstantVariables.TABLE_NAME_CHISO + " WHERE ID_SQLITE = '" + ID_SQLITE + "'";
        Cursor c = database.rawQuery(strQuery, null);
        float sl_moi = 0f;
        if(c.moveToFirst()){
            sl_moi = Float.parseFloat(c.getString(0));
        }
        return sl_moi;
    }

    public long deleteSo(String TEN_FILE) {
        database = this.getWritableDatabase();
        return database.delete(GcsConstantVariables.TABLE_NAME_CHISO, "TEN_FILE=?", new String[]{TEN_FILE});
    }

    // Xử lý bảng index
    public long insertDataIndex(String so, int index) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("so", so);
        values.put("vitri", index);
        return database.insert(GcsConstantVariables.TABLE_NAME_INDEX, null, values);
    }

    public long updateDataIndex(String so, int index) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("vitri", index);
        return database.update(GcsConstantVariables.TABLE_NAME_INDEX, values, "so=?",
                new String[]{so.trim()});
    }

    public long clearDataIndex(String so) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("vitri", 0);
        return database.update(GcsConstantVariables.TABLE_NAME_INDEX, values, "so=?", new String[] {so});
    }

    public long deleteDataIndex(String so) {
        database = this.getWritableDatabase();
        return database.delete(GcsConstantVariables.TABLE_NAME_INDEX, "so=?", new String[]{so});
    }

    public int getIndex(String so) {
        database = this.getReadableDatabase();
        String strQuery = "SELECT vitri FROM " + GcsConstantVariables.TABLE_NAME_INDEX + " WHERE so = '" + so + "'";
        Cursor c = database.rawQuery(strQuery, null);
        if(c.moveToFirst()){
            return c.getInt(0);
        }
        return 0;
    }

    // Xử lý bảng sổ
    public Cursor getAllDataSo() {
        database = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + GcsConstantVariables.TABLE_NAME_SO;
        return database.rawQuery(strQuery, null);
    }

    public List<String> GetAllSo(){
        List<String> list = new ArrayList<String>();
        try{
            Cursor c = getAllDataSo();
            if(c != null ){
                if (c.moveToFirst()) {
                    do {
                        String fileName = c.getString(3);
                        list.add(fileName);
                    } while (c.moveToNext());
                }
            }
        }catch(Exception e){
            list = new ArrayList<String>();
        }
        return list;
    }

    public class SQLLOpenHelper extends SQLiteOpenHelper {

        public SQLLOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
            super(context, name, factory, version, errorHandler);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub

        }
    }

    public class DbErrorHandler implements DatabaseErrorHandler {
        @Override
        public void onCorruption(SQLiteDatabase dbObj) {
            // TODO Auto-generated method stub
        }
    }

}
