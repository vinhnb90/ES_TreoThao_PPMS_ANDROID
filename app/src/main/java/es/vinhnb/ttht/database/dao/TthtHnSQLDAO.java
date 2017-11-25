package es.vinhnb.ttht.database.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import es.vinhnb.ttht.adapter.ChiTietCtoAdapter.DataChiTietCtoAdapter;
import es.vinhnb.ttht.adapter.HistoryAdapter;
import es.vinhnb.ttht.database.table.TABLE_BBAN_CTO;
import es.vinhnb.ttht.database.table.TABLE_BBAN_TUTI;
import es.vinhnb.ttht.database.table.TABLE_CHITIET_CTO;
import es.vinhnb.ttht.database.table.TABLE_CHITIET_TUTI;
import es.vinhnb.ttht.database.table.TABLE_HISTORY;
import esolutions.com.esdatabaselib.baseSqlite.ItemFactory;
import esolutions.com.esdatabaselib.baseSqlite.SqlDAO;
import esolutions.com.esdatabaselib.baseSqlite.SqlHelper;

import static android.content.ContentValues.TAG;

/**
 * Created by VinhNB on 11/22/2017.
 */

public class TthtHnSQLDAO extends SqlDAO {
    public TthtHnSQLDAO(SQLiteDatabase database, Context context) {
        super(database, context);
    }

    public List<DataChiTietCtoAdapter> getTreoDataChiTietCtoAdapter(String[] agrs) {
        String query = "SELECT * FROM (SELECT " +
                TABLE_BBAN_CTO.table.ID_BBAN_TRTH.name() +
                " AS ID_BBAN_TRTH_BB, " +
                TABLE_BBAN_CTO.table.TEN_KHANG.name() +
                ", " +
                TABLE_BBAN_CTO.table.DCHI_HDON.name() +
                ", " +
                TABLE_BBAN_CTO.table.MA_GCS_CTO.name() +
                ", " +
                TABLE_BBAN_CTO.table.MA_TRAM.name() +
                " FROM " +
                TABLE_BBAN_CTO.table.getName() +
                ") AS BBAN JOIN " +
                "(SELECT " +
                TABLE_CHITIET_CTO.table.ID_BBAN_TRTH.name() +
                " AS ID_BBAN_TRTH_CTO, " +
                TABLE_CHITIET_CTO.table.MA_CTO.name() +
                ", " +
                TABLE_CHITIET_CTO.table.SO_CTO.name() +
                ", " +
                TABLE_CHITIET_CTO.table.CHI_SO.name() +
                " FROM " +
                TABLE_CHITIET_CTO.table.getName() +
                " WHERE " +
                TABLE_CHITIET_CTO.table.MA_BDONG.name() +
                " = " +
                "?" +
                " )AS CONGTO ON " +
                "BBAN.ID_BBAN_TRTH_BB = CONGTO.ID_BBAN_TRTH_CTO";

        Cursor cursor = super.mDatabase.rawQuery(query, agrs);

        return super.selectAllLazy(cursor, new ItemFactory(DataChiTietCtoAdapter.class) {
            @Override
            protected DataChiTietCtoAdapter create(Cursor cursor, int index) {
                DataChiTietCtoAdapter dataChiTietCtoAdapter = new DataChiTietCtoAdapter();
                cursor.moveToPosition(index);

                dataChiTietCtoAdapter.setMaCto(cursor.getString(cursor.getColumnIndex(TABLE_CHITIET_CTO.table.MA_CTO.name())));
                dataChiTietCtoAdapter.setSoCto(cursor.getString(cursor.getColumnIndex(TABLE_CHITIET_CTO.table.SO_CTO.name())));
                dataChiTietCtoAdapter.setMaTram(cursor.getString(cursor.getColumnIndex(TABLE_BBAN_CTO.table.MA_TRAM.name())));
                dataChiTietCtoAdapter.setMaGCS(cursor.getString(cursor.getColumnIndex(TABLE_BBAN_CTO.table.MA_GCS_CTO.name())));
                dataChiTietCtoAdapter.setTenKH(cursor.getString(cursor.getColumnIndex(TABLE_BBAN_CTO.table.TEN_KHANG.name())));
                dataChiTietCtoAdapter.setDiachiKH(cursor.getString(cursor.getColumnIndex(TABLE_BBAN_CTO.table.DCHI_HDON.name())));
                dataChiTietCtoAdapter.setChiso(cursor.getString(cursor.getColumnIndex(TABLE_CHITIET_CTO.table.CHI_SO.name())));

                return dataChiTietCtoAdapter;
            }
        });
    }

    public List<DataChiTietCtoAdapter> getThaoDataChiTietCtoAdapter(String[] agrs) {
        String query = "SELECT * FROM THAO JOION B";
        Cursor c = super.mDatabase.rawQuery(query, agrs);
        return super.selectAllLazy(c, new ItemFactory(DataChiTietCtoAdapter.class) {
            @Override
            protected DataChiTietCtoAdapter create(Cursor cursor, int index) {
                DataChiTietCtoAdapter dataChiTietCtoAdapter = new DataChiTietCtoAdapter();
                cursor.moveToPosition(index);

                dataChiTietCtoAdapter.setMaCto(cursor.getString(cursor.getColumnIndex(TABLE_CHITIET_CTO.table.MA_CTO.name())));
                dataChiTietCtoAdapter.setSoCto(cursor.getString(cursor.getColumnIndex(TABLE_CHITIET_CTO.table.SO_CTO.name())));
                dataChiTietCtoAdapter.setMaTram(cursor.getString(cursor.getColumnIndex(TABLE_BBAN_CTO.table.MA_TRAM.name())));
                dataChiTietCtoAdapter.setMaGCS(cursor.getString(cursor.getColumnIndex(TABLE_BBAN_CTO.table.MA_GCS_CTO.name())));
                dataChiTietCtoAdapter.setTenKH(cursor.getString(cursor.getColumnIndex(TABLE_BBAN_CTO.table.TEN_KHANG.name())));
                dataChiTietCtoAdapter.setDiachiKH(cursor.getString(cursor.getColumnIndex(TABLE_BBAN_CTO.table.DCHI_HDON.name())));
                dataChiTietCtoAdapter.setChiso(cursor.getString(cursor.getColumnIndex(TABLE_CHITIET_CTO.table.CHI_SO.name())));

                return dataChiTietCtoAdapter;
            }
        });
    }

    public List<String> getTRANG_THAI_DU_LIEUofTABLE_BBAN_CTO(String[] valueCheck) {
        String query = "SELECT " +
                TABLE_BBAN_CTO.table.TRANG_THAI_DU_LIEU.name() +
                " FROM " +
                TABLE_BBAN_CTO.table.getName() +
                " WHERE " +
                TABLE_BBAN_CTO.table.ID_BBAN_TRTH.name() +
                " = ?";

        Cursor c = super.mDatabase.rawQuery(query, valueCheck);
        return super.selectAllLazy(c, new ItemFactory(String.class) {
            @Override
            protected String create(Cursor cursor, int index) {
                cursor.moveToPosition(index);
                return cursor.getString(cursor.getColumnIndex(TABLE_BBAN_CTO.table.TRANG_THAI_DU_LIEU.name()));
            }
        });
    }

    public List<String> getTRANG_THAI_DU_LIEUofTABLE_CHITIET_CTO(String[] valueCheck) {
        String query = "SELECT " +
                TABLE_CHITIET_CTO.table.TRANG_THAI_DU_LIEU.name() +
                " FROM " +
                TABLE_CHITIET_CTO.table.getName() +
                " WHERE " +
                TABLE_CHITIET_CTO.table.MA_CTO.name() +
                " = ? " +
                " AND " +
                TABLE_CHITIET_CTO.table.ID_BBAN_TRTH.name() +
                " = ? " +
                " AND " +
                TABLE_CHITIET_CTO.table.MA_BDONG +
                " = ? ";

        Cursor c = super.mDatabase.rawQuery(query, valueCheck);
        return super.selectAllLazy(c, new ItemFactory(String.class) {
            @Override
            protected String create(Cursor cursor, int index) {
                cursor.moveToPosition(index);
                return cursor.getString(cursor.getColumnIndex(TABLE_CHITIET_CTO.table.MA_CTO.name()));
            }
        });
    }

    public List<String> getTRANG_THAI_DU_LIEUofTABLE_BBAN_TUTI(String[] valueCheck) {
        String query = "SELECT " +
                TABLE_BBAN_TUTI.table.TRANG_THAI_DU_LIEU.name() +
                " FROM " +
                TABLE_BBAN_TUTI.table.getName() +
                " WHERE " +
                TABLE_BBAN_TUTI.table.ID_BBAN_TUTI.name() +
                " = ?";

        Cursor c = super.mDatabase.rawQuery(query, valueCheck);
        return super.selectAllLazy(c, new ItemFactory(String.class) {
            @Override
            protected String create(Cursor cursor, int index) {
                cursor.moveToPosition(index);
                return cursor.getString(cursor.getColumnIndex(TABLE_BBAN_TUTI.table.TRANG_THAI_DU_LIEU.name()));
            }
        });
    }

    public List<String> getTRANG_THAI_DU_LIEUofTABLE_CHITIET_TUTI(String[] valueCheck) {
        String query = "SELECT " +
                TABLE_CHITIET_TUTI.table.TRANG_THAI_DU_LIEU.name() +
                " FROM " +
                TABLE_CHITIET_TUTI.table.getName() +
                " WHERE " +
                TABLE_CHITIET_TUTI.table.ID_CHITIET_TUTI.name() +
                " = ?";


        Cursor c = super.mDatabase.rawQuery(query, valueCheck);
        return super.selectAllLazy(c, new ItemFactory(String.class) {
            @Override
            protected String create(Cursor cursor, int index) {
                cursor.moveToPosition(index);
                return cursor.getString(cursor.getColumnIndex(TABLE_CHITIET_TUTI.table.TRANG_THAI_DU_LIEU.name()));
            }
        });
    }


    public List<HistoryAdapter.DataHistoryAdapter> getTABLE_HISTORYinNDay(int nDay) {
        String query = "SELECT * FROM " +
                TABLE_HISTORY.table.getName() +
                " WHERE " +
                TABLE_HISTORY.table.DATE_CALL_API.name() +
                " > (SELECT DATETIME('now', '-" +
                nDay +
                " day')) ORDER BY " +
                TABLE_HISTORY.table.ID_TABLE_HISTORY.name() +
                " DESC";


        Cursor c = super.mDatabase.rawQuery(query, null);


        return super.selectAllLazy(c, new ItemFactory<HistoryAdapter.DataHistoryAdapter>(HistoryAdapter.DataHistoryAdapter.class) {
            @Override
            protected HistoryAdapter.DataHistoryAdapter create(Cursor cursor, int index) {
                cursor.moveToPosition(index);

                HistoryAdapter.DataHistoryAdapter tableHistory = new HistoryAdapter.DataHistoryAdapter();
                tableHistory.date = cursor.getString(cursor.getColumnIndex(TABLE_HISTORY.table.DATE_CALL_API.name()));
                tableHistory.notify = cursor.getString(cursor.getColumnIndex(TABLE_HISTORY.table.TYPE_RESULT.name()));
                tableHistory.message = cursor.getString(cursor.getColumnIndex(TABLE_HISTORY.table.MESSAGE_RESULT.name()));
                tableHistory.soBB = cursor.getInt(cursor.getColumnIndex(TABLE_HISTORY.table.SO_BBAN_API.name()));
                tableHistory.soThao = cursor.getInt(cursor.getColumnIndex(TABLE_HISTORY.table.SO_CTO_THAO_API.name()));
                tableHistory.soTreo = cursor.getInt(cursor.getColumnIndex(TABLE_HISTORY.table.SO_CTO_TREO_API.name()));
                tableHistory.soBBTuTi = cursor.getInt(cursor.getColumnIndex(TABLE_HISTORY.table.SO_BBAN_TUTI_API.name()));
                tableHistory.soTu = cursor.getInt(cursor.getColumnIndex(TABLE_HISTORY.table.SO_TU_API.name()));
                tableHistory.soTi = cursor.getInt(cursor.getColumnIndex(TABLE_HISTORY.table.SO_TI_API.name()));
                tableHistory.soTram = cursor.getInt(cursor.getColumnIndex(TABLE_HISTORY.table.SO_TRAM_API.name()));
                tableHistory.soChungLoai = cursor.getInt(cursor.getColumnIndex(TABLE_HISTORY.table.SO_CHUNGLOAI_API.name()));

                return tableHistory;
            }
        });
    }
}
