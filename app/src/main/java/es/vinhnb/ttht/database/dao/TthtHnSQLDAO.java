package es.vinhnb.ttht.database.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import es.vinhnb.ttht.adapter.ChiTietCtoAdapter.DataChiTietCtoAdapter;
import es.vinhnb.ttht.database.table.TABLE_BBAN_CTO;
import es.vinhnb.ttht.database.table.TABLE_CHITIET_CTO;
import esolutions.com.esdatabaselib.baseSqlite.ItemFactory;
import esolutions.com.esdatabaselib.baseSqlite.SqlDAO;

/**
 * Created by VinhNB on 11/22/2017.
 */

public class TthtHnSQLDAO extends SqlDAO {
    public TthtHnSQLDAO(SQLiteDatabase database, Context context) {
        super(database, context);
    }

    public List<DataChiTietCtoAdapter> getTreoDataChiTietCtoAdapter(String[] agrs) {
        String query = "SELECT * FROM A JOION B";
        Cursor c = super.mDatabase.rawQuery(query, agrs);
        return super.selectAllLazy(c, new ItemFactory(DataChiTietCtoAdapter.class) {
            @Override
            protected DataChiTietCtoAdapter create(Cursor cursor, int index) {
                DataChiTietCtoAdapter dataChiTietCtoAdapter = new DataChiTietCtoAdapter();
                cursor.moveToPosition(index);

                dataChiTietCtoAdapter.setMaCto(cursor.getString(cursor.getColumnIndex(TABLE_CHITIET_CTO.declared.MA_CTO.name())));
                dataChiTietCtoAdapter.setSoCto(cursor.getString(cursor.getColumnIndex(TABLE_CHITIET_CTO.declared.SO_CTO.name())));
                dataChiTietCtoAdapter.setMaTram(cursor.getString(cursor.getColumnIndex(TABLE_BBAN_CTO.declared.MA_TRAM.name())));
                dataChiTietCtoAdapter.setMaGCS(cursor.getString(cursor.getColumnIndex(TABLE_BBAN_CTO.declared.MA_GCS_CTO.name())));
                dataChiTietCtoAdapter.setTenKH(cursor.getString(cursor.getColumnIndex(TABLE_BBAN_CTO.declared.TEN_KHANG.name())));
                dataChiTietCtoAdapter.setDiachiKH(cursor.getString(cursor.getColumnIndex(TABLE_BBAN_CTO.declared.DCHI_HDON.name())));
                dataChiTietCtoAdapter.setChiso(cursor.getString(cursor.getColumnIndex(TABLE_CHITIET_CTO.declared.CHI_SO.name())));

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

                dataChiTietCtoAdapter.setMaCto(cursor.getString(cursor.getColumnIndex(TABLE_CHITIET_CTO.declared.MA_CTO.name())));
                dataChiTietCtoAdapter.setSoCto(cursor.getString(cursor.getColumnIndex(TABLE_CHITIET_CTO.declared.SO_CTO.name())));
                dataChiTietCtoAdapter.setMaTram(cursor.getString(cursor.getColumnIndex(TABLE_BBAN_CTO.declared.MA_TRAM.name())));
                dataChiTietCtoAdapter.setMaGCS(cursor.getString(cursor.getColumnIndex(TABLE_BBAN_CTO.declared.MA_GCS_CTO.name())));
                dataChiTietCtoAdapter.setTenKH(cursor.getString(cursor.getColumnIndex(TABLE_BBAN_CTO.declared.TEN_KHANG.name())));
                dataChiTietCtoAdapter.setDiachiKH(cursor.getString(cursor.getColumnIndex(TABLE_BBAN_CTO.declared.DCHI_HDON.name())));
                dataChiTietCtoAdapter.setChiso(cursor.getString(cursor.getColumnIndex(TABLE_CHITIET_CTO.declared.CHI_SO.name())));

                return dataChiTietCtoAdapter;
            }
        });
    }

}
