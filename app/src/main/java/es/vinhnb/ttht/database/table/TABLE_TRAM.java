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

@Table(name = "TABLE_TRAM")
public class TABLE_TRAM {
    @EnumNameCollumn()
    public enum table {
        ID_TABLE_TRAM,
        MA_TRAM,
        MA_DVIQLY,
        TEN_TRAM,
        LOAI_TRAM,
        CSUAT_TRAM,
        MA_CAP_DA,
        MA_CAP_DA_RA,
        DINH_DANH;

        public static String getName(){
            return "TABLE_TRAM";
        }
    }


    @PrimaryKey
    @AutoIncrement
    @Collumn(name = "ID_TABLE_TRAM", type = TYPE.INTEGER, other = "NOT NULL")
    private int ID_TABLE_TRAM;

    @Collumn(name = "MA_TRAM", type = TYPE.TEXT)
    private String MA_TRAM;

    @Collumn(name = "MA_DVIQLY", type = TYPE.TEXT)
    private String MA_DVIQLY;

    @Collumn(name = "TEN_TRAM", type = TYPE.TEXT)
    private String TEN_TRAM;

    @Collumn(name = "LOAI_TRAM", type = TYPE.TEXT)
    private String LOAI_TRAM;

    @Collumn(name = "CSUAT_TRAM", type = TYPE.INTEGER)
    private int CSUAT_TRAM;

    @Collumn(name = "MA_CAP_DA", type = TYPE.TEXT)
    private String MA_CAP_DA;

    @Collumn(name = "MA_CAP_DA_RA", type = TYPE.TEXT)
    private String MA_CAP_DA_RA;

    @Collumn(name = "DINH_DANH", type = TYPE.TEXT)
    private String DINH_DANH;

    public TABLE_TRAM() {
    }

    public TABLE_TRAM(@Params(name = "ID_TABLE_TRAM") int ID_TABLE_TRAM,
                      @Params(name = "MA_TRAM") String MA_TRAM,
                      @Params(name = "MA_DVIQLY") String MA_DVIQLY,
                      @Params(name = "TEN_TRAM") String TEN_TRAM,
                      @Params(name = "LOAI_TRAM") String LOAI_TRAM,
                      @Params(name = "CSUAT_TRAM") int CSUAT_TRAM,
                      @Params(name = "MA_CAP_DA") String MA_CAP_DA,
                      @Params(name = "MA_CAP_DA_RA") String MA_CAP_DA_RA,
                      @Params(name = "DINH_DANH") String DINH_DANH) {
        this.ID_TABLE_TRAM = ID_TABLE_TRAM;
        this.MA_TRAM = MA_TRAM;
        this.MA_DVIQLY = MA_DVIQLY;
        this.TEN_TRAM = TEN_TRAM;
        this.LOAI_TRAM = LOAI_TRAM;
        this.CSUAT_TRAM = CSUAT_TRAM;
        this.MA_CAP_DA = MA_CAP_DA;
        this.MA_CAP_DA_RA = MA_CAP_DA_RA;
        this.DINH_DANH = DINH_DANH;
    }

    public int getID_TABLE_TRAM() {
        return ID_TABLE_TRAM;
    }

    public void setID_TABLE_TRAM(int ID_TABLE_TRAM) {
        this.ID_TABLE_TRAM = ID_TABLE_TRAM;
    }

    public String getMA_TRAM() {
        return MA_TRAM;
    }

    public void setMA_TRAM(String MA_TRAM) {
        this.MA_TRAM = MA_TRAM;
    }

    public String getMA_DVIQLY() {
        return MA_DVIQLY;
    }

    public void setMA_DVIQLY(String MA_DVIQLY) {
        this.MA_DVIQLY = MA_DVIQLY;
    }

    public String getTEN_TRAM() {
        return TEN_TRAM;
    }

    public void setTEN_TRAM(String TEN_TRAM) {
        this.TEN_TRAM = TEN_TRAM;
    }

    public String getLOAI_TRAM() {
        return LOAI_TRAM;
    }

    public void setLOAI_TRAM(String LOAI_TRAM) {
        this.LOAI_TRAM = LOAI_TRAM;
    }

    public int getCSUAT_TRAM() {
        return CSUAT_TRAM;
    }

    public void setCSUAT_TRAM(int CSUAT_TRAM) {
        this.CSUAT_TRAM = CSUAT_TRAM;
    }

    public String getMA_CAP_DA() {
        return MA_CAP_DA;
    }

    public void setMA_CAP_DA(String MA_CAP_DA) {
        this.MA_CAP_DA = MA_CAP_DA;
    }

    public String getMA_CAP_DA_RA() {
        return MA_CAP_DA_RA;
    }

    public void setMA_CAP_DA_RA(String MA_CAP_DA_RA) {
        this.MA_CAP_DA_RA = MA_CAP_DA_RA;
    }

    public String getDINH_DANH() {
        return DINH_DANH;
    }

    public void setDINH_DANH(String DINH_DANH) {
        this.DINH_DANH = DINH_DANH;
    }
}
