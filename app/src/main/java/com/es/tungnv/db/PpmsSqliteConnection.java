
package com.es.tungnv.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.es.tungnv.utils.PpmsCommon;
import com.es.tungnv.utils.TthtConstantVariables;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by VinhNB on 8/9/2016. Class is type Singleton
 */
public class PpmsSqliteConnection extends SQLiteOpenHelper {
    private static PpmsSqliteConnection sInstance;
    private SQLiteDatabase database;

    private static final int DATABASE_VERSION = 1;


    private PpmsSqliteConnection(final Context context) {
        super(context, Environment.getExternalStorageDirectory() + PpmsCommon.PROGRAM_DB_PATH + PpmsCommon.DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory() + PpmsCommon.PROGRAM_DB_PATH + PpmsCommon.DATABASE_NAME, null);
    }


    @Override
    public synchronized void close() {
        if (database != null) {
            database.close();
            database = null;
        }
        super.close();
    }

    public static synchronized PpmsSqliteConnection getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PpmsSqliteConnection(context.getApplicationContext());
            Log.d(TAG, "getInstance: ");
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Execute script.
        sqLiteDatabase.execSQL(QUERY_CREATE_TASK);
        sqLiteDatabase.execSQL(QUERY_CREATE_DEPART);
        sqLiteDatabase.execSQL(QUERY_CREATE_SESSION);
        sqLiteDatabase.execSQL(QUERY_CREATE_EMPLOYEE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop table
        sqLiteDatabase.execSQL(sQueryDropTableTask);
        sqLiteDatabase.execSQL(sQueryDropTableDepart);
        sqLiteDatabase.execSQL(sQueryDropTableSession);
        sqLiteDatabase.execSQL(sQueryDropTableEmp);
        // Recreate
        onCreate(sqLiteDatabase);
    }

    //region query create table task
    public static final String TABLE_TASK = "TASK";

    public static final String MA_PHAN_CONG = "MA_PHAN_CONG";

    private static final String QUERY_CREATE_TASK = "CREATE TABLE "
            + TABLE_TASK
            + "("
            + "ID_TASK INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + "MA_PHAN_CONG INTEGER NOT NULL,"
            + "MA_NVIEN TEXT, "
            + "MA_DDO TEXT, "
            + "MA_SO_GCS TEXT, "
            + "MA_KH TEXT, "
            + "CHI_SO_MOI INTEGER, "
            + "CHI_SO_CU INTEGER, "
            + "SAN_LUONG INTEGER, "
            + "SO_CTO TEXT, "
            + "MA_CTO TEXT, "
            + "NGAY_PHAN_CONG TEXT, "
            + "THANG_KIEM_TRA TEXT, "
            + "BUNDLE_MA_NVIEN INTEGER NOT NULL, "
            + "TY_LE_CHENH_LECH FLOAT, "
            + "SAN_LUONG_TB FLOAT, "
            + "TEN_TRAM TEXT, "
            + "HS_NHAN INTEGER, "
            + "ANH_CTO TEXT,"
            + "SAI_CHI_SO TEXT, "
            + "TEN_KH TEXT, "
            + "DIA_CHI_DUNG_DIEN TEXT, "
            + "SO_HOP INTEGER, "
            + "SO_O INTEGER, "
            + "SO_COT INTEGER, "
            + "LOAI_HOP TEXT, "
            + "LOAI_CTO TEXT, "
            + "TINH_TRANG_CTO TEXT, "
            + "NGAY_PHUC_TRA DATE, "
            + "NGAY_GHI_DIEN DATE, "
            + "MUC_DICH_SD_DIEN TEXT, "
            + "TINH_TRANG_SD_DIEN TEXT, "
            + "NHAN_XET_PHUC_TRA TEXT, "
            + "CHI_SO_PHUC_TRA INT, "
            + "TINH_TRANG_NIEM_PHONG TEXT,"
            + "TRANG_THAI INTEGER"
            + ")";

    //TODO drop table
    private static String sQueryDropTableTask = "DROP TABLE IF EXISTS " + TABLE_TASK;

    //TODO get all task by nhanVien
    public static String getQueryGetTask(String MA_NVIEN) {
        return "SELECT * FROM " + TABLE_TASK + " WHERE BUNDLE_MA_NVIEN = '" + MA_NVIEN + "'";
    }

    //TODO get task with MA_PHAN_CONG
    public static String getsQueryGetRowsTaskWith(int MA_NVIEN, int MA_PHAN_CONG) {
        return "SELECT * FROM " + TABLE_TASK + " WHERE BUNDLE_MA_NVIEN =" + MA_NVIEN + " AND MA_PHAN_CONG = " + MA_PHAN_CONG;
    }

    //TODO get count task with TRANG_THAI != 2 and NGAY_PHAN_CONG
    public static String getsQueryGetCountTaskNotCommitWithDate(int MA_NVIEN, String NGAY_PHAN_CONG) {
        return "SELECT ID_TASK, COUNT(ID_TASK) AS COUNT FROM " + TABLE_TASK + " WHERE BUNDLE_MA_NVIEN = " + MA_NVIEN + " AND NGAY_PHAN_CONG = '" + NGAY_PHAN_CONG + "' AND TRANG_THAI <> 2";
    }

    //TODO get count total task with TRANG_THAI = ? and NGAY_PHAN_CONG
    public static String getsQueryGetCountTaskWithDateAndTRANG_THAI(int MA_NVIEN, String NGAY_PHAN_CONG, int TRANG_THAI) {
        return "SELECT ID_TASK, COUNT(ID_TASK) AS COUNT FROM " + TABLE_TASK + " WHERE BUNDLE_MA_NVIEN = " + MA_NVIEN + " AND NGAY_PHAN_CONG = '" + NGAY_PHAN_CONG + "' AND TRANG_THAI =" + TRANG_THAI;
    }

    //TODO get count task with TRANG_THAI != 2 and NGAY_PHAN_CONG
    public static String getsQueryGetCountTaskCommitWithDate(int MA_NVIEN, String NGAY_PHAN_CONG) {
        return "SELECT ID_TASK, COUNT(ID_TASK) AS COUNT FROM " + TABLE_TASK + " WHERE BUNDLE_MA_NVIEN = " + MA_NVIEN + " AND NGAY_PHAN_CONG = '" + NGAY_PHAN_CONG + "' AND TRANG_THAI = 2";
    }

    //TODO get count task with NGAY_PHAN_CONG
    public static String getsQueryGetCountTaskWithDate(int MA_NVIEN, String dateOfColumn, String column) {
        return "SELECT COUNT(ID_TASK) AS COUNT FROM " + TABLE_TASK + " WHERE BUNDLE_MA_NVIEN = " + MA_NVIEN + " AND " + column + " LIKE '" + dateOfColumn + "%'";
    }

    /**
     * //TODO get all TASK with BUNDLE_MA_NVIEN and TRANG_THAI
     *
     * @param MA_NVIEN
     * @param TRANG_THAI =0 is not write data, = 1 is wrote data, = 2 is uploaded
     * @return
     */
    public static String getsQueryGetAllRowsTaskWith(int MA_NVIEN, int TRANG_THAI) {
        return "SELECT * FROM " + TABLE_TASK + " WHERE BUNDLE_MA_NVIEN = " + MA_NVIEN + " AND TRANG_THAI = " + TRANG_THAI;
    }

    //TODO get TRANG_THAI with BUNDLE_MA_NVIEN and MA_PHANCONG
    public static String getsQueryGetTRANG_THAIWith(int MA_NVIEN, int MA_PHAN_CONG) {
        return "SELECT * FROM " + TABLE_TASK + " WHERE BUNDLE_MA_NVIEN = " + MA_NVIEN + " AND MA_PHAN_CONG = " + MA_PHAN_CONG;
    }

    //TODO get TASK with BUNDLE_MA_NVIEN and TRANG_THAI
    public static String getsQueryGetAllRowsTaskDoiSoatWithTRANG_THAI(int MA_NVIEN, int TRANG_THAI) {
        return "SELECT * FROM " + TABLE_TASK + " WHERE BUNDLE_MA_NVIEN = " + MA_NVIEN + " AND TRANG_THAI == " + TRANG_THAI;
    }

    //TODO get TASK with BUNDLE_MA_NVIEN and TRANG_THAI != 2
    public static String getsQueryGetAllRowsTaskNotCommit(int MA_NVIEN) {
        return "SELECT * FROM " + TABLE_TASK + " WHERE BUNDLE_MA_NVIEN = " + MA_NVIEN + " AND TRANG_THAI <> 2";
    }


    //TODO delete all TASK
    public static String sQueryDeleteAllTask = "DELETE FROM " + TABLE_TASK;

    //TODO delete TASK with MA_PHAN_CONG
    public String getQueryDeleteTaskWithMA_PHAN_CONG(String MA_PHAN_CONG) {
        return "DELETE FROM " + TABLE_TASK + " WHERE MA_PHAN_CONG = " + MA_PHAN_CONG;
    }

    //TODO count Task with MA_SO_GCS
    public String getQueryCountTaskWithMA_SO_GCS(String MA_SO_GCS) {
        return "SELECT COUNT(MA_PHAN_CONG) FROM " + TABLE_TASK + " WHERE MA_SO_GCS = " + MA_SO_GCS;
    }

    //TODO check Task Exists with MA_PHAN_CONG
    public String getQueryCheckTaskExistWithMA_PHAN_CONG(String MA_PHAN_CONG) {
        return "SELECT * FROM " + TABLE_TASK + " WHERE MA_PHAN_CONG = " + MA_PHAN_CONG;
    }

    //TODO update TRANG_THAI when upload successfully
    public String getQueryUpdateTRANG_THAIWhenUploadSuccess(int MA_NVIEN, int MA_PHAN_CONG) {
        return "UPDATE " + TABLE_TASK + " SET TRANG_THAI = 2 WHERE BUNDLE_MA_NVIEN = " + MA_NVIEN + " AND MA_PHAN_CONG = " + MA_PHAN_CONG;
    }

    //TODO insert task
    public String getsQueryInsertDataToTask(
            int MA_PHAN_CONG,
            String MA_DVIQLY,
            String MA_DDO,
            String MA_SO_GCS,
            String MA_KH,
            int CHI_SO_MOI,
            int CHI_SO_CU,
            int SAN_LUONG,
            String SO_CTO,
            String MA_CTO,
            String NGAY_PHAN_CONG,
            String THANG_KIEM_TRA,
            int MA_NVIEN,
            float TY_LE_CHENH_LECH,
            float SAN_LUONG_TB,
            String TEN_TRAM,
            int HS_NHAN,
            String ANH_CTO,
            String SAI_CHI_SO,
            String TEN_KH,
            String DIA_CHI_DUNG_DIEN,
            int SO_HOP,
            int SO_O,
            int SO_COT,
            String LOAI_HOP,
            String LOAI_CTO,
            String TINH_TRANG_CTO,
            String NGAY_PHUC_TRA,
            String NGAY_GHI_DIEN,
            String MUC_DICH_SD_DIEN,
            String TINH_TRANG_SD_DIEN,
            String NHAN_XET_PHUC_TRA,
            int CHI_SO_PHUC_TRA,
            String TINH_TRANG_NIEM_PHONG
    ) {
        return "INSERT INTO " + TABLE_TASK
                + "("
                + " MA_PHAN_CONG,"
                + " MA_DVIQLY,"
                + " MA_DDO,"
                + " MA_SO_GCS,"
                + " MA_KH,"
                + " CHI_SO_MOI,"
                + " CHI_SO_CU,"
                + " SAN_LUONG,"
                + " SO_CTO,"
                + " MA_CTO,"
                + " NGAY_PHAN_CONG,"
                + " THANG_KIEM_TRA,"
                + " BUNDLE_MA_NVIEN,"
                + " TY_LE_CHENH_LECH,"
                + " SAN_LUONG_TB,"
                + " TEN_TRAM,"
                + " HS_NHAN,"
                + " ANH_CTO,"
                + " SAI_CHI_SO,"
                + " TEN_KH,"
                + " DIA_CHI_DUNG_DIEN,"
                + " SO_HOP,"
                + " SO_O,"
                + " SO_COT,"
                + " LOAI_HOP,"
                + " LOAI_CTO,"
                + " TINH_TRANG_CTO,"
                + " NGAY_PHUC_TRA,"
                + " NGAY_GHI_DIEN,"
                + " MUC_DICH_SD_DIEN,"
                + " TINH_TRANG_SD_DIEN,"
                + " NHAN_XET_PHUC_TRA,"
                + " CHI_SO_PHUC_TRA,"
                + " TINH_TRANG_NIEM_PHONG"
                + ")"
                + " VALUES"
                + " ("
                + "'" + MA_PHAN_CONG + "',"
                + "'" + MA_DVIQLY + "',"
                + "'" + MA_DDO + "',"
                + "'" + MA_SO_GCS + "',"
                + "'" + MA_KH + "',"
                + "'" + CHI_SO_MOI + "',"
                + "'" + CHI_SO_CU + "',"
                + "'" + SAN_LUONG + "',"
                + "'" + SO_CTO + "',"
                + "'" + MA_CTO + "',"
                + "'" + NGAY_PHAN_CONG + "',"
                + "'" + THANG_KIEM_TRA + "',"
                + "'" + MA_NVIEN + "',"
                + "'" + TY_LE_CHENH_LECH + "',"
                + "'" + SAN_LUONG_TB + "',"
                + "'" + TEN_TRAM + "',"
                + "'" + HS_NHAN + "',"
                + "'" + ANH_CTO + "',"
                + "'" + SAI_CHI_SO + "',"
                + "'" + TEN_KH + "',"
                + "'" + DIA_CHI_DUNG_DIEN + "',"
                + "'" + SO_HOP + "',"
                + "'" + SO_O + "',"
                + "'" + SO_COT + "',"
                + "'" + LOAI_HOP + "',"
                + "'" + LOAI_CTO + "',"
                + "'" + TINH_TRANG_CTO + "',"
                + "'" + NGAY_PHUC_TRA + "',"
                + "'" + NGAY_GHI_DIEN + "',"
                + "'" + MUC_DICH_SD_DIEN + "',"
                + "'" + TINH_TRANG_SD_DIEN + "',"
                + "'" + NHAN_XET_PHUC_TRA + "',"
                + "'" + CHI_SO_PHUC_TRA + "',"
                + "'" + TINH_TRANG_NIEM_PHONG + "',"
                + ")";
    }

    //TODO update data task with image (client local update data)
    //TODO update data task none image (server update data)
    public String getsQueryUpdateTaskWithClientOrServer(
            boolean isClient,
            int MA_PHAN_CONG,
            String MA_DVIQLY,
            String MA_DDO,
            String MA_SO_GCS,
            String MA_KH,
            int CHI_SO_MOI,
            int CHI_SO_CU,
            int SAN_LUONG,
            String SO_CTO,
            String MA_CTO,
            String NGAY_PHAN_CONG,
            String THANG_KIEM_TRA,
            int MA_NVIEN,
            float TY_LE_CHENH_LECH,
            float SAN_LUONG_TB,
            String TEN_TRAM,
            int HS_NHAN,
            String ANH_CTO,
            String SAI_CHI_SO,
            String TEN_KH,
            String DIA_CHI_DUNG_DIEN,
            int SO_HOP,
            int SO_O,
            int SO_COT,
            String LOAI_HOP,
            String LOAI_CTO,
            String TINH_TRANG_CTO,
            String NGAY_PHUC_TRA,
            String NGAY_GHI_DIEN,
            String MUC_DICH_SD_DIEN,
            String TINH_TRANG_SD_DIEN,
            String NHAN_XET_PHUC_TRA,
            int CHI_SO_PHUC_TRA,
            String TINH_TRANG_NIEM_PHONG
    ) {
        StringBuilder query = new StringBuilder();
        query.append("UPDATE INTO " + TABLE_TASK + " SET ");
        query.append(" MA_PHAN_CONG = '" + MA_PHAN_CONG + "',");
        query.append(" MA_NVIEN = '" + MA_DVIQLY + "',");
        query.append(" MA_DDO = '" + MA_DDO + "',");
        query.append(" MA_SO_GCS = '" + MA_SO_GCS + "',");
        query.append(" MA_KH = '" + MA_KH + "',");
        query.append(" CHI_SO_MOI = '" + CHI_SO_MOI + "',");
        query.append(" CHI_SO_CU = '" + CHI_SO_CU + "',");
        query.append(" SAN_LUONG = '" + SAN_LUONG + "',");
        query.append(" SO_CTO = '" + SO_CTO + "',");
        query.append(" MA_CTO = '" + MA_CTO + "',");
        query.append(" NGAY_PHAN_CONG = '" + NGAY_PHAN_CONG + "',");
        query.append(" THANG_KIEM_TRA = '" + THANG_KIEM_TRA + "',");
        query.append(" BUNDLE_MA_NVIEN = '" + MA_NVIEN + "',");
        if (isClient) {
            query.append(" TY_LE_CHENH_LECH = '" + TY_LE_CHENH_LECH + "',");
        }
        query.append(" SAN_LUONG_TB = '" + SAN_LUONG_TB + "',");
        query.append(" TEN_TRAM = '" + TEN_TRAM + "',");
        query.append(" HS_NHAN = '" + HS_NHAN + "',");
        if (isClient) {
            query.append(" ANH_CTO = '" + ANH_CTO + "',");
            query.append(" SAI_CHI_SO = '" + SAI_CHI_SO + "',");
        }
        query.append(" TEN_KH = '" + TEN_KH + "',");
        query.append(" DIA_CHI_DUNG_DIEN = '" + DIA_CHI_DUNG_DIEN + "',");
        query.append(" SO_HOP = '" + SO_HOP + "',");
        query.append(" SO_O = '" + SO_O + "',");
        query.append(" SO_COT = '" + SO_COT + "',");
        query.append(" LOAI_HOP = '" + LOAI_HOP + "',");
        query.append(" LOAI_CTO = '" + LOAI_CTO + "',");
        query.append(" TINH_TRANG_CTO = '" + TINH_TRANG_CTO + "',");
        if (isClient) {
            query.append(" NGAY_PHUC_TRA = '" + NGAY_PHUC_TRA + "',");
        }
        query.append(" NGAY_GHI_DIEN = '" + NGAY_GHI_DIEN + "',");
        query.append(" MUC_DICH_SD_DIEN = '" + MUC_DICH_SD_DIEN + "',");
        query.append(" TINH_TRANG_SD_DIEN = '" + TINH_TRANG_SD_DIEN + "',");
        if (isClient) {
            query.append(" NHAN_XET_PHUC_TRA = '" + NHAN_XET_PHUC_TRA + "',");
            query.append(" CHI_SO_PHUC_TRA = '" + CHI_SO_PHUC_TRA + "',");
            query.append(" TINH_TRANG_NIEM_PHONG = '" + TINH_TRANG_NIEM_PHONG + "',");
        }
        query.append(" WHERE MA_PHAN_CONG = '" + MA_PHAN_CONG + "'");

        return query.toString();
    }

    //endregion

    //region query create table department

    public static final String TABLE_DEPART = "DEPART";

    public static final String MA_DEPART = "MA_DEPART";

    private static final String QUERY_CREATE_DEPART = "CREATE TABLE "
            + TABLE_DEPART
            + "("
            + "ID_DEPART INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + "MA_DEPART INTEGER NOT NULL,"
            + "NAME TEXT, "
            + "ADDRESS TEXT, "
            + "PHONE TEXT, "
            + "EMAIL TEXT, "
            + "MA_PARENT_DEPART INTEGER, "
            + "LEVEL_DEPART INTEGER, "
            + "CODE TEXT, "
            + "INDEX_DEPART INTEGER, "
            + "DATE_CREATE TEXT, "
            + "USER_CREATE INTEGER, "
            + "IS_ACTIVED TEXT"
            + ")";

    //TODO drop table depart
    private static String sQueryDropTableDepart = "DROP TABLE IF EXISTS " + TABLE_DEPART;

    //TODO get all rows depart table
    public static String getsQueryGetAllRowsDepart = "SELECT * FROM " + TABLE_DEPART;

    //TODO delete all row depart table
    public static String getsQueryDeleteAllDepart = "DELETE FROM " + TABLE_DEPART;

    //TODO insert
    public String getsQueryInsertDataToDepart(
            int MA_DEPART,
            String NAME,
            String ADDRESS,
            String PHONE,
            String EMAIL,
            int MA_PARENT_DEPART,
            int LEVEL_DEPART,
            String CODE,
            int INDEX_DEPART,
            String DATE_CREATE,
            int USER_CREATE,
            boolean IS_ACTIVED
    ) {
        return "INSERT INTO " + TABLE_DEPART
                + "("
                + "MA_DEPART, "
                + "NAME, "
                + "ADDRESS, "
                + "PHONE, "
                + "EMAIL, "
                + "MA_PARENT_DEPART, "
                + "LEVEL_DEPART, "
                + "CODE, "
                + "INDEX_DEPART, "
                + "DATE_CREATE, "
                + "USER_CREATE, "
                + "IS_ACTIVED"
                + ")"
                + " VALUES"
                + " ("
                + "'" + MA_DEPART + "',"
                + "'" + NAME + "',"
                + "'" + ADDRESS + "',"
                + "'" + PHONE + "',"
                + "'" + EMAIL + "',"
                + "'" + MA_PARENT_DEPART + "',"
                + "'" + LEVEL_DEPART + "',"
                + "'" + CODE + "',"
                + "'" + INDEX_DEPART + "',"
                + "'" + DATE_CREATE + "',"
                + "'" + USER_CREATE + "',"
                + "'" + IS_ACTIVED + "'"
                + ")";
    }


    //endregion

    //region query create table session login

    public static final String TABLE_SESSION = "SESSION";

    public static final String USER_SESION = "USER_SESSION";

    private static final String QUERY_CREATE_SESSION = "CREATE TABLE "
            + TABLE_SESSION
            + "("
            + "ID_SESSION INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + "CODE_DEPART INTEGER NOT NULL,"
            + "USER_SESSION TEXT, "
            + "PASS_SESSION TEXT, "
            + "BUNDLE_MA_NVIEN TEXT"
            + ")";

    //TODO drop table session
    private static String sQueryDropTableSession = "DROP TABLE IF EXISTS " + TABLE_SESSION;

    //TODO select all row session
    public static String sQueryGetAllRowsSession = "SELECT * FROM " + TABLE_SESSION;

    //TODO select row session with username condifition
    public String getsQueryGetRowSessionWithUsername(String username) {
        return "SELECT * FROM " + TABLE_SESSION + " WHERE USER_SESSION = '" + username + "'";
    }


    //endregion

    //region query create table employee

    public static final String TABLE_EMPLOYEE = "EMPLOYEE";

    public static final String MA_NVIEN = "BUNDLE_MA_NVIEN";

    private static final String QUERY_CREATE_EMPLOYEE = "CREATE TABLE "
            + TABLE_EMPLOYEE
            + "("
            + "ID_EMP INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + "BUNDLE_MA_NVIEN TEXT NOT NULL,"
            + "NAME_EMP TEXT,"
            + "PHONE_EMP TEXT, "
            + "ADRESS_EMP TEXT, "
            + "EMAIL_EMP TEXT,"
            + "USER_EMP TEXT,"
            + "PASS_EMP TEXT,"
            + "GENDER_EMP TEXT,"
            + "DEPART_EMP INTEGER,"
            + "IS_ACTIVE_EMP TEXT"
            + ")";

    //TODO drop table session
    private static String sQueryDropTableEmp = "DROP TABLE IF EXISTS " + TABLE_EMPLOYEE;

    //TODO select all row session
    public static String sQueryGetAllRowsEmp = "SELECT * FROM " + TABLE_EMPLOYEE;

    //TODO select row session with username condifition
    public String getsQueryGetRowEmpWithUsername(String USER_EMP) {
        return "SELECT * FROM " + TABLE_EMPLOYEE + " WHERE USER_EMP = '" + USER_EMP + "'";
    }
    //endregion


    //TODO access data return a cursor
    public Cursor runQueryReturnCursor(String queryString) {
        database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(queryString, null);
        if (cursor != null && cursor.moveToFirst()) {
            return cursor;
        }
        return null;
    }

    //TODO delete table
    public long deleteData(String tableDelete) {
        database = this.getWritableDatabase();
        return database.delete(tableDelete, null, null);
    }

    //TODO insert data
    public long insertData(ContentValues contentValues, String tableInsert) {
        database = this.getWritableDatabase();
        if (contentValues == null) {
            return -1;
        }
        long ins = database.insert(tableInsert, null, contentValues);
        database.close();
        return ins;
    }

    //TODO update data
    public long updateData(ContentValues contentValues, String tableUpdate, String idCollumn, String valueColumn) {
        database = this.getWritableDatabase();
        if (contentValues == null) {
            return -1;
        }
        long update = database.update(tableUpdate, contentValues, idCollumn + "=?", new String[]{valueColumn});
        database.close();
        return update;
    }

    //TODO check exists row
    public boolean isRowExistData(String queryCheckExist) {
        database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(queryCheckExist, null);
        int totalRows = cursor.getCount();
        cursor.close();
        if (totalRows > 0) {
            return true;
        } else return false;
    }

}