package es.vinhnb.ttht.database.table;


import esolutions.com.esdatabaselib.baseSqlite.anonation.AutoIncrement;
import esolutions.com.esdatabaselib.baseSqlite.anonation.Collumn;
import esolutions.com.esdatabaselib.baseSqlite.anonation.EnumNameCollumn;
import esolutions.com.esdatabaselib.baseSqlite.anonation.Params;
import esolutions.com.esdatabaselib.baseSqlite.anonation.PrimaryKey;
import esolutions.com.esdatabaselib.baseSqlite.anonation.TYPE;
import esolutions.com.esdatabaselib.baseSqlite.anonation.Table;

/**
 * Created by VinhNB on 10/10/2017.
 */

@Table(name = "TABLE_DVIQLY")
public class TABLE_DVIQLY {

    @EnumNameCollumn()
    public enum table {
        ID_TABLE_DVIQLY,
        MA_DVIQLY,
        TEN_DVIQLY;

        public static String getName(){
            return "TABLE_DVIQLY";
        }
    }

    @PrimaryKey
    @AutoIncrement
    @Collumn(name = "ID_TABLE_DVIQLY", type = TYPE.INTEGER, other = "NOT NULL")
    private int ID_TABLE_DVIQLY;

    @Collumn(name = "MA_DVIQLY", type = TYPE.TEXT, other = "NOT NULL")
    private String MA_DVIQLY;

    @Collumn(name = "TEN_DVIQLY")
    private String TEN_DVIQLY;

    public TABLE_DVIQLY() {
    }

    public TABLE_DVIQLY(@Params(name = "ID_TABLE_DVIQLY") int ID_TABLE_DVIQLY, @Params(name = "MA_DVIQLY") String MA_DVIQLY, @Params(name = "TEN_DVIQLY") String TEN_DVIQLY) {
        this.ID_TABLE_DVIQLY = ID_TABLE_DVIQLY;
        this.MA_DVIQLY = MA_DVIQLY;
        this.TEN_DVIQLY = TEN_DVIQLY;
    }

    public int getID_TABLE_DVIQLY() {
        return ID_TABLE_DVIQLY;
    }

    public void setID_TABLE_DVIQLY(int ID_TABLE_DVIQLY) {
        this.ID_TABLE_DVIQLY = ID_TABLE_DVIQLY;
    }

    public String getMA_DVIQLY() {
        return MA_DVIQLY;
    }

    public void setMA_DVIQLY(String MA_DVIQLY) {
        this.MA_DVIQLY = MA_DVIQLY;
    }

    public String getTEN_DVIQLY() {
        return TEN_DVIQLY;
    }

    public void setTEN_DVIQLY(String TEN_DVIQLY) {
        this.TEN_DVIQLY = TEN_DVIQLY;
    }

    @Override
    public String toString() {
        return MA_DVIQLY + " - " + TEN_DVIQLY;
    }
}
