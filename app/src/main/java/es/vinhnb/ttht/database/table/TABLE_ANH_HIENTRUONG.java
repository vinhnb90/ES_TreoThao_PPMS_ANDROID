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
public class TABLE_ANH_HIENTRUONG {

    @EnumNameCollumn()
    public enum declared {
        ID_TABLE_ANH_HIENTRUONG,
        ID_CHITIET_TUTI,
        TEN_ANH,
        MA_DVIQLY,
        ID_BBAN_TUTI,
        ID_CHITIET_CTO,
        TYPE,
        CREATE_DAY;

        public static String getName(){
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
    private int TEN_ANH;

    @Collumn(name = "MA_DVIQLY", type = TEXT)
    private String MA_DVIQLY;

    @Collumn(name = "ID_BBAN_TUTI", type = INTEGER)
    private int ID_BBAN_TUTI;

    @Collumn(name = "ID_CHITIET_CTO", type = INTEGER)
    private int ID_CHITIET_CTO;

    @Collumn(name = "TYPE", type = INTEGER)
    private int TYPE;

    @Collumn(name = "CREATE_DAY", type = TEXT)
    private String CREATE_DAY;

    public TABLE_ANH_HIENTRUONG() {
    }

    public TABLE_ANH_HIENTRUONG(@Params(name = "ID_TABLE_ANH_HIENTRUONG") int ID_TABLE_ANH_HIENTRUONG,
                                @Params(name = "ID_CHITIET_TUTI") int ID_CHITIET_TUTI,
                                @Params(name = "TEN_ANH") int TEN_ANH,
                                @Params(name = "MA_DVIQLY") String MA_DVIQLY,
                                @Params(name = "ID_BBAN_TUTI") int ID_BBAN_TUTI,
                                @Params(name = "ID_CHITIET_CTO") int ID_CHITIET_CTO,
                                @Params(name = "TYPE") int TYPE,
                                @Params(name = "CREATE_DAY") String CREATE_DAY) {
        this.ID_TABLE_ANH_HIENTRUONG = ID_TABLE_ANH_HIENTRUONG;
        this.ID_CHITIET_TUTI = ID_CHITIET_TUTI;
        this.TEN_ANH = TEN_ANH;
        this.MA_DVIQLY = MA_DVIQLY;
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

    public int getTEN_ANH() {
        return TEN_ANH;
    }

    public String getMA_DVIQLY() {
        return MA_DVIQLY;
    }

    public int getID_BBAN_TUTI() {
        return ID_BBAN_TUTI;
    }

    public int getID_CHITIET_CTO() {
        return ID_CHITIET_CTO;
    }

    public int getTYPE() {
        return TYPE;
    }

    public String getCREATE_DAY() {
        return CREATE_DAY;
    }
}
