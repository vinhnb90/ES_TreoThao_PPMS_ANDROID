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

@Table(name = "TABLE_LYDO_TREOTHAO")
public class TABLE_LYDO_TREOTHAO {

    @EnumNameCollumn()
    public enum table {
        ID_TABLE_LYDO_TREOTHAO,
        MA_DVIQLY,
        MA_LDO,
        TEN_LDO,
        NHOM;

        public static String getName(){
            return "TABLE_LYDO_TREOTHAO";
        }
    }

    @PrimaryKey
    @AutoIncrement
    @Collumn(name = "ID_TABLE_LYDO_TREOTHAO", type = TYPE.INTEGER, other = "NOT NULL")
    private int ID_TABLE_LYDO_TREOTHAO;

    @Collumn(name = "MA_DVIQLY", type = TYPE.TEXT, other = "NOT NULL")
    private String MA_DVIQLY;

    @Collumn(name = "MA_LDO")
    private String MA_LDO;


    @Collumn(name = "TEN_LDO")
    private String TEN_LDO;


    @Collumn(name = "NHOM", type = TYPE.INTEGER, other = "NOT NULL")
    private int NHOM;


    public TABLE_LYDO_TREOTHAO() {
    }

    public TABLE_LYDO_TREOTHAO(
            @Params(name = "ID_TABLE_LYDO_TREOTHAO") int ID_TABLE_LYDO_TREOTHAO,
            @Params(name = "MA_DVIQLY") String MA_DVIQLY,
            @Params(name = "MA_LDO") String MA_LDO,
            @Params(name = "TEN_LDO") String TEN_LDO,
            @Params(name = "NHOM") int NHOM) {
        this.ID_TABLE_LYDO_TREOTHAO = ID_TABLE_LYDO_TREOTHAO;
        this.MA_DVIQLY = MA_DVIQLY;
        this.MA_LDO = MA_LDO;
        this.TEN_LDO = TEN_LDO;
        this.NHOM = NHOM;
    }

    public int getID_TABLE_LYDO_TREOTHAO() {
        return ID_TABLE_LYDO_TREOTHAO;
    }

    public void setID_TABLE_LYDO_TREOTHAO(int ID_TABLE_LYDO_TREOTHAO) {
        this.ID_TABLE_LYDO_TREOTHAO = ID_TABLE_LYDO_TREOTHAO;
    }

    public String getMA_DVIQLY() {
        return MA_DVIQLY;
    }

    public void setMA_DVIQLY(String MA_DVIQLY) {
        this.MA_DVIQLY = MA_DVIQLY;
    }

    public String getMA_LDO() {
        return MA_LDO;
    }

    public void setMA_LDO(String MA_LDO) {
        this.MA_LDO = MA_LDO;
    }

    public String getTEN_LDO() {
        return TEN_LDO;
    }

    public void setTEN_LDO(String TEN_LDO) {
        this.TEN_LDO = TEN_LDO;
    }

    public int getNHOM() {
        return NHOM;
    }

    public void setNHOM(int NHOM) {
        this.NHOM = NHOM;
    }

    @Override
    public String toString() {
        return MA_LDO + " - " + TEN_LDO;
    }
}
