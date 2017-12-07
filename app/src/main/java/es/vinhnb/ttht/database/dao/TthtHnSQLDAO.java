package es.vinhnb.ttht.database.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import es.vinhnb.ttht.adapter.BBanAdapter;
import es.vinhnb.ttht.adapter.ChiTietCtoAdapter.DataChiTietCtoAdapter;
import es.vinhnb.ttht.adapter.ChungLoaiAdapter;
import es.vinhnb.ttht.adapter.HistoryAdapter;
import es.vinhnb.ttht.adapter.TramAdapter;
import es.vinhnb.ttht.common.Common;
import es.vinhnb.ttht.database.table.TABLE_ANH_HIENTRUONG;
import es.vinhnb.ttht.database.table.TABLE_BBAN_CTO;
import es.vinhnb.ttht.database.table.TABLE_BBAN_TUTI;
import es.vinhnb.ttht.database.table.TABLE_CHITIET_CTO;
import es.vinhnb.ttht.database.table.TABLE_CHITIET_TUTI;
import es.vinhnb.ttht.database.table.TABLE_HISTORY;
import es.vinhnb.ttht.database.table.TABLE_LOAI_CONG_TO;
import es.vinhnb.ttht.database.table.TABLE_TRAM;
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
                ", " +
                TABLE_BBAN_CTO.table.NGAY_TRTH.name() +
                ", " +
                TABLE_BBAN_CTO.table.SO_BBAN.name() +
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
                ", " +
                TABLE_CHITIET_CTO.table.ID_BBAN_TUTI.name() +
                ", " +
                TABLE_CHITIET_CTO.table.TRANG_THAI_DU_LIEU.name() +
                " FROM " +
                TABLE_CHITIET_CTO.table.getName() +
                " WHERE " +
                TABLE_CHITIET_CTO.table.MA_BDONG.name() +
                " = " +
                "?" +
                " )AS CONGTO ON " +
                "BBAN.ID_BBAN_TRTH_BB = CONGTO.ID_BBAN_TRTH_CTO";

        Cursor cursor = super.mDatabase.rawQuery(query, agrs);

        return super.selectCustomLazy(cursor, new ItemFactory(DataChiTietCtoAdapter.class) {
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
                dataChiTietCtoAdapter.setIdbbantuti(cursor.getInt(cursor.getColumnIndex(TABLE_CHITIET_CTO.table.ID_BBAN_TUTI.name())));
                dataChiTietCtoAdapter.setIdbbantrth(cursor.getInt(cursor.getColumnIndex("ID_BBAN_TRTH_BB")));
                dataChiTietCtoAdapter.setTRANG_THAI_DULIEU(cursor.getString(cursor.getColumnIndex(TABLE_CHITIET_CTO.table.TRANG_THAI_DU_LIEU.name())));
                dataChiTietCtoAdapter.setNgaytrth(cursor.getString(cursor.getColumnIndex(TABLE_BBAN_CTO.table.NGAY_TRTH.name())));
                dataChiTietCtoAdapter.setSobban(cursor.getString(cursor.getColumnIndex(TABLE_BBAN_CTO.table.SO_BBAN.name())));

                return dataChiTietCtoAdapter;
            }
        });
    }

    public List<TramAdapter.DataTramAdapter> getTramAdapter(String[] agrs) {
        String query = "SELECT " +
                TABLE_TRAM.table.TEN_TRAM.name() +
                ", " +
                TABLE_TRAM.table.DINH_DANH.name() +
                ", " +
                TABLE_TRAM.table.MA_DVIQLY.name() +
                ", " +
                TABLE_TRAM.table.MA_TRAM.name() +
                ", " +
                TABLE_TRAM.table.CSUAT_TRAM.name() +
                " FROM " +
                TABLE_TRAM.table.getName() +
                "";

        Cursor cursor = super.mDatabase.rawQuery(query, agrs);

        return super.selectCustomLazy(cursor, new ItemFactory(TramAdapter.DataTramAdapter.class) {
            @Override
            protected TramAdapter.DataTramAdapter create(Cursor cursor, int index) {
                TramAdapter.DataTramAdapter dataTramAdapter = new TramAdapter.DataTramAdapter();
                cursor.moveToPosition(index);

                dataTramAdapter.setTenTram(cursor.getString(cursor.getColumnIndex(TABLE_TRAM.table.TEN_TRAM.name())));
                dataTramAdapter.setCongsuat(cursor.getString(cursor.getColumnIndex(TABLE_TRAM.table.CSUAT_TRAM.name())));
                dataTramAdapter.setDinhDanh(cursor.getString(cursor.getColumnIndex(TABLE_TRAM.table.DINH_DANH.name())));
                dataTramAdapter.setMaDviQly(cursor.getString(cursor.getColumnIndex(TABLE_TRAM.table.MA_DVIQLY.name())));
                dataTramAdapter.setMaTram(cursor.getString(cursor.getColumnIndex(TABLE_TRAM.table.MA_TRAM.name())));

                return dataTramAdapter;
            }
        });
    }

    public List<ChungLoaiAdapter.DataChungLoaiAdapter> getCloaiAdapter(String[] agrs) {
        String query = "SELECT " +
                TABLE_LOAI_CONG_TO.table.MO_TA.name() +
                " ," +
                TABLE_LOAI_CONG_TO.table.TEN_LOAI_CTO.name() +
                ", " +
                TABLE_LOAI_CONG_TO.table.MA_HANG.name() +
                ", " +
                TABLE_LOAI_CONG_TO.table.TEN_NUOC.name() +
                ", " +
                TABLE_LOAI_CONG_TO.table.DIEN_AP.name() +
                ", " +
                TABLE_LOAI_CONG_TO.table.VH_CONG.name() +
                ", " +
                TABLE_LOAI_CONG_TO.table.PTHUC_DOXA.name() +
                " FROM " +
                TABLE_LOAI_CONG_TO.table.getName() +
                "";

        Cursor cursor = super.mDatabase.rawQuery(query, agrs);

        return super.selectCustomLazy(cursor, new ItemFactory(ChungLoaiAdapter.DataChungLoaiAdapter.class) {
            @Override
            protected ChungLoaiAdapter.DataChungLoaiAdapter create(Cursor cursor, int index) {
                ChungLoaiAdapter.DataChungLoaiAdapter dataCloaiAdapter = new ChungLoaiAdapter.DataChungLoaiAdapter();
                cursor.moveToPosition(index);

                dataCloaiAdapter.setDienAp(cursor.getString(cursor.getColumnIndex(TABLE_LOAI_CONG_TO.table.DIEN_AP.name())));
                dataCloaiAdapter.setMaHang(cursor.getString(cursor.getColumnIndex(TABLE_LOAI_CONG_TO.table.MA_HANG.name())));
                dataCloaiAdapter.setMota(cursor.getString(cursor.getColumnIndex(TABLE_LOAI_CONG_TO.table.MO_TA.name())));
                dataCloaiAdapter.setPhuongthucdoxa(cursor.getString(cursor.getColumnIndex(TABLE_LOAI_CONG_TO.table.PTHUC_DOXA.name())));
                dataCloaiAdapter.setTenLoaiCto(cursor.getString(cursor.getColumnIndex(TABLE_LOAI_CONG_TO.table.TEN_LOAI_CTO.name())));
                dataCloaiAdapter.setTenNuoc(cursor.getString(cursor.getColumnIndex(TABLE_LOAI_CONG_TO.table.TEN_NUOC.name())));
                dataCloaiAdapter.setVhCong(cursor.getString(cursor.getColumnIndex(TABLE_LOAI_CONG_TO.table.VH_CONG.name())));

                return dataCloaiAdapter;
            }
        });
    }

    public List<BBanAdapter.DataBBanAdapter> getBBanAdapter(String[] agrs) {
        String query = "SELECT " +
                TABLE_BBAN_CTO.table.DCHI_HDON.name() +
                ", " +
                TABLE_BBAN_CTO.table.LY_DO_TREO_THAO.name() +
                ", " +
                TABLE_BBAN_CTO.table.MA_KHANG.name() +
                ", " +
                TABLE_BBAN_CTO.table.MA_HDONG.name() +
                ", " +
                TABLE_BBAN_CTO.table.MA_GCS_CTO.name() +
                ", " +
                TABLE_BBAN_CTO.table.TEN_KHANG.name() +
                ", " +
                TABLE_BBAN_CTO.table.MA_TRAM.name() +
                ", " +
                TABLE_BBAN_CTO.table.NGAY_TRTH.name() +
                ", " +
                TABLE_BBAN_CTO.table.SO_BBAN.name() +
                " FROM " +
                TABLE_BBAN_CTO.table.getName() +
                "";

        Cursor cursor = super.mDatabase.rawQuery(query, agrs);

        return super.selectCustomLazy(cursor, new ItemFactory(BBanAdapter.DataBBanAdapter.class) {
            @Override
            protected BBanAdapter.DataBBanAdapter create(Cursor cursor, int index) {
                BBanAdapter.DataBBanAdapter dataBBanAdapter = new BBanAdapter.DataBBanAdapter();
                cursor.moveToPosition(index);

                dataBBanAdapter.setDiachiKH(cursor.getString(cursor.getColumnIndex(TABLE_BBAN_CTO.table.DCHI_HDON.name())));
                dataBBanAdapter.setLydo(cursor.getString(cursor.getColumnIndex(TABLE_BBAN_CTO.table.LY_DO_TREO_THAO.name())));
                dataBBanAdapter.setMaGCS(cursor.getString(cursor.getColumnIndex(TABLE_BBAN_CTO.table.MA_GCS_CTO.name())));
                dataBBanAdapter.setMaHopDong(cursor.getString(cursor.getColumnIndex(TABLE_BBAN_CTO.table.MA_HDONG.name())));
                dataBBanAdapter.setTenKH(cursor.getString(cursor.getColumnIndex(TABLE_BBAN_CTO.table.TEN_KHANG.name())));
                dataBBanAdapter.setMaTramcapdien(cursor.getString(cursor.getColumnIndex(TABLE_BBAN_CTO.table.MA_TRAM.name())));
                dataBBanAdapter.setMaKH(cursor.getString(cursor.getColumnIndex(TABLE_BBAN_CTO.table.MA_KHANG.name())));
                dataBBanAdapter.setNgayTrth(cursor.getString(cursor.getColumnIndex(TABLE_BBAN_CTO.table.NGAY_TRTH.name())));
                dataBBanAdapter.setSobban(cursor.getString(cursor.getColumnIndex(TABLE_BBAN_CTO.table.SO_BBAN.name())));


                return dataBBanAdapter;
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
        return super.selectCustomLazy(c, new ItemFactory(String.class) {
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
                TABLE_CHITIET_CTO.table.MA_BDONG.name() +
                " = ? ";

        Cursor c = super.mDatabase.rawQuery(query, valueCheck);
        return super.selectCustomLazy(c, new ItemFactory(String.class) {
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
        return super.selectCustomLazy(c, new ItemFactory(String.class) {
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
        return super.selectCustomLazy(c, new ItemFactory(String.class) {
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


        return super.selectCustomLazy(c, new ItemFactory<HistoryAdapter.DataHistoryAdapter>(HistoryAdapter.DataHistoryAdapter.class) {
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
                tableHistory.typeResult = cursor.getString(cursor.getColumnIndex(TABLE_HISTORY.table.TYPE_RESULT.name()));

                return tableHistory;
            }
        });
    }


    public List<TABLE_CHITIET_CTO> getChiTietCongto(String[] agrs) {
        String query = "SELECT  * " +
                " FROM " +
                TABLE_CHITIET_CTO.table.getName() +
                " WHERE " +
                TABLE_CHITIET_CTO.table.ID_BBAN_TRTH.name() +
                " = ?" +
                " AND " +
                TABLE_CHITIET_CTO.table.MA_BDONG.name() +
                " = ?" +
                " AND " +
                TABLE_CHITIET_CTO.table.MA_NVIEN.name() +
                " = ?" +
                "";

        Cursor cursor = super.mDatabase.rawQuery(query, agrs);

        return super.selectAllLazy(TABLE_CHITIET_CTO.class, cursor);
    }

    public List<TABLE_BBAN_TUTI> getBBanTuti(int ID_BBAN_TUTI, String MA_NVIEN) {
        String[] agrs = new  String[]{String.valueOf(ID_BBAN_TUTI), MA_NVIEN};
        String query = "SELECT  * " +
                " FROM " +
                TABLE_BBAN_TUTI.table.getName() +
                " WHERE " +
                TABLE_BBAN_TUTI.table.ID_BBAN_TUTI.name() +
                " = ?" +
                " AND " +
                TABLE_BBAN_TUTI.table.MA_NVIEN.name() +
                " = ?" +
                "";

        Cursor cursor = super.mDatabase.rawQuery(query, agrs);

        return super.selectAllLazy(TABLE_BBAN_TUTI.class, cursor);
    }


    public List<TABLE_CHITIET_TUTI> getChitietTuTi(int ID_BBAN_TUTI, String MA_NVIEN) {
        String[] agrs = new  String[]{String.valueOf(ID_BBAN_TUTI), MA_NVIEN};
        String query = "SELECT  * " +
                " FROM " +
                TABLE_CHITIET_TUTI.table.getName() +
                " WHERE " +
                TABLE_CHITIET_TUTI.table.ID_BBAN_TUTI.name() +
                " = ?" +
                " AND " +
                TABLE_BBAN_TUTI.table.MA_NVIEN.name() +
                " = ?" +
                "";

        Cursor cursor = super.mDatabase.rawQuery(query, agrs);

        return super.selectAllLazy(TABLE_CHITIET_TUTI.class, cursor);
    }


    public List<TABLE_BBAN_CTO> getBBan(String[] agrs) {
        String query = "SELECT  * " +
                " FROM " +
                TABLE_BBAN_CTO.table.getName() +
                " WHERE " +
                TABLE_BBAN_CTO.table.ID_BBAN_TRTH.name() +
                " = ?" +
                " AND " +
                TABLE_BBAN_CTO.table.MA_NVIEN.name() +
                " = ?" +
                "";

        Cursor cursor = super.mDatabase.rawQuery(query, agrs);
        Log.d(TAG, "getBBan: " + cursor.getCount());

        return super.selectAllLazy(TABLE_BBAN_CTO.class, cursor);
    }

    public List<TABLE_LOAI_CONG_TO> getLoaiCongto(String[] argsCloai) {
        String query = "SELECT  * " +
                " FROM " +
                TABLE_LOAI_CONG_TO.table.getName() +
                " WHERE " +
                TABLE_LOAI_CONG_TO.table.MA_CLOAI.name() +
                " = ?" +
                "";

        Cursor cursor = super.mDatabase.rawQuery(query, argsCloai);

        return super.selectAllLazy(TABLE_LOAI_CONG_TO.class, cursor);
    }

    public List<TABLE_TRAM> getTRAM(String[] argsTram) {
        String query = "SELECT  * " +
                " FROM " +
                TABLE_TRAM.table.getName() +
                " WHERE " +
                TABLE_TRAM.table.MA_TRAM +
                " = ?" +
                "";

        Cursor cursor = super.mDatabase.rawQuery(query, argsTram);

        return super.selectAllLazy(TABLE_TRAM.class, cursor);
    }


    public List<TABLE_ANH_HIENTRUONG> getAnhHienTruong(String[] argsAnhHienTruong, Common.TYPE_IMAGE typeImage) {
        StringBuilder query = new StringBuilder("SELECT  * " +
                " FROM " +
                TABLE_ANH_HIENTRUONG.table.getName() +
                " WHERE " +
                TABLE_ANH_HIENTRUONG.table.MA_NVIEN.name() +
                " = ? " +
                "AND " +
                TABLE_ANH_HIENTRUONG.table.TYPE.name() +
                " = " +
                "'" +
                typeImage.code
                +
                "'"
        );


        switch (typeImage) {
            case IMAGE_CONG_TO:
            case IMAGE_CONG_TO_NIEM_PHONG:
                query.append(" AND " +
                        TABLE_ANH_HIENTRUONG.table.ID_CHITIET_CTO.name() +
                        " = ?");
                break;


            case IMAGE_TU:
            case IMAGE_TI:
            case IMAGE_NIEM_PHONG_TI:
            case IMAGE_NIEM_PHONG_TU:
            case IMAGE_MACH_NHI_THU_TI:
            case IMAGE_MACH_NHI_THU_TU:
                query.append(" AND " +
                        TABLE_ANH_HIENTRUONG.table.ID_BBAN_TUTI.name() +
                        " = ?" +
                        " AND " +
                        TABLE_ANH_HIENTRUONG.table.ID_CHITIET_TUTI.name() +
                        " = ?");
                break;

        }

        Cursor cursor = super.mDatabase.rawQuery(query.toString(), argsAnhHienTruong);

        return super.selectAllLazy(TABLE_ANH_HIENTRUONG.class, cursor);
    }
}
