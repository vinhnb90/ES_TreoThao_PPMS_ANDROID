package es.vinhnb.ttht.database.table;

import esolutions.com.esdatabaselib.baseSqlite.anonation.AutoIncrement;
import esolutions.com.esdatabaselib.baseSqlite.anonation.Collumn;
import esolutions.com.esdatabaselib.baseSqlite.anonation.EnumNameCollumn;
import esolutions.com.esdatabaselib.baseSqlite.anonation.Params;
import esolutions.com.esdatabaselib.baseSqlite.anonation.PrimaryKey;
import esolutions.com.esdatabaselib.baseSqlite.anonation.Table;

import static esolutions.com.esdatabaselib.baseSqlite.anonation.TYPE.INTEGER;
import static esolutions.com.esdatabaselib.baseSqlite.anonation.TYPE.TEXT;

/**
 * Created by VinhNB on 10/10/2017.
 */

@Table(name = "TABLE_ANH_HIENTRUONG")
public class TABLE_ANH_HIENTRUONG implements Cloneable{

    @EnumNameCollumn()
    public enum table {
        ID_TABLE_ANH_HIENTRUONG,
        TEN_ANH,
        MA_NVIEN,
        ID_BBAN_TUTI,
        ID_CHITIET_TUTI,
        ID_CHITIET_CTO,
        TYPE,
        CREATE_DAY;

        public static String getName() {
            return "TABLE_ANH_HIENTRUONG";
        }
    }



    @PrimaryKey
    @AutoIncrement
    @Collumn(name = "ID_TABLE_ANH_HIENTRUONG", type = INTEGER, other = "NOT NULL")
    private int ID_TABLE_ANH_HIENTRUONG;

    @Collumn(name = "ID_CHITIET_TUTI", type = INTEGER)
    private int ID_CHITIET_TUTI;

    @Collumn(name = "TEN_ANH", type = TEXT)
    private String TEN_ANH;

    @Collumn(name = "MA_NVIEN", type = TEXT)
    private String MA_NVIEN;

    @Collumn(name = "ID_BBAN_TUTI", type = INTEGER)
    private int ID_BBAN_TUTI;

    @Collumn(name = "ID_CHITIET_CTO", type = INTEGER)
    private int ID_CHITIET_CTO;

    @Collumn(name = "TYPE", type = TEXT)
    private String TYPE;

    @Collumn(name = "CREATE_DAY", type = TEXT)
    private String CREATE_DAY;

    public TABLE_ANH_HIENTRUONG() {
    }

    public TABLE_ANH_HIENTRUONG(@Params(name = "ID_TABLE_ANH_HIENTRUONG") int ID_TABLE_ANH_HIENTRUONG,
                                @Params(name = "ID_CHITIET_TUTI") int ID_CHITIET_TUTI,
                                @Params(name = "TEN_ANH") String TEN_ANH,
                                @Params(name = "MA_NVIEN") String MA_NVIEN,
                                @Params(name = "ID_BBAN_TUTI") int ID_BBAN_TUTI,
                                @Params(name = "ID_CHITIET_CTO") int ID_CHITIET_CTO,
                                @Params(name = "TYPE") String TYPE,
                                @Params(name = "CREATE_DAY") String CREATE_DAY) {
        this.ID_TABLE_ANH_HIENTRUONG = ID_TABLE_ANH_HIENTRUONG;
        this.ID_CHITIET_TUTI = ID_CHITIET_TUTI;
        this.TEN_ANH = TEN_ANH;
        this.MA_NVIEN = MA_NVIEN;
        this.ID_BBAN_TUTI = ID_BBAN_TUTI;
        this.ID_CHITIET_CTO = ID_CHITIET_CTO;
        this.TYPE = TYPE;
        this.CREATE_DAY = CREATE_DAY;
    }


    public int getID_TABLE_ANH_HIENTRUONG() {
        return ID_TABLE_ANH_HIENTRUONG;
    }

    public int getID_CHITIET_TUTI() {
        return ID_CHITIET_TUTI;
    }

    public String getTEN_ANH() {
        return TEN_ANH;
    }

    public String getMA_NVIEN() {
        return MA_NVIEN;
    }

    public int getID_BBAN_TUTI() {
        return ID_BBAN_TUTI;
    }

    public int getID_CHITIET_CTO() {
        return ID_CHITIET_CTO;
    }

    public String getTYPE() {
        return TYPE;
    }

    public String getCREATE_DAY() {
        return CREATE_DAY;
    }


    public void setID_TABLE_ANH_HIENTRUONG(int ID_TABLE_ANH_HIENTRUONG) {
        this.ID_TABLE_ANH_HIENTRUONG = ID_TABLE_ANH_HIENTRUONG;
    }

    public void setID_CHITIET_TUTI(int ID_CHITIET_TUTI) {
        this.ID_CHITIET_TUTI = ID_CHITIET_TUTI;
    }

    public void setTEN_ANH(String TEN_ANH) {
        this.TEN_ANH = TEN_ANH;
    }

    public void setMA_NVIEN(String MA_NVIEN) {
        this.MA_NVIEN = MA_NVIEN;
    }

    public void setID_BBAN_TUTI(int ID_BBAN_TUTI) {
        this.ID_BBAN_TUTI = ID_BBAN_TUTI;
    }

    public void setID_CHITIET_CTO(int ID_CHITIET_CTO) {
        this.ID_CHITIET_CTO = ID_CHITIET_CTO;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public void setCREATE_DAY(String CREATE_DAY) {
        this.CREATE_DAY = CREATE_DAY;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
