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

@Table(name = "TABLE_HISTORY")
public class TABLE_HISTORY {
    @EnumNameCollumn()
    public enum table {
        ID_TABLE_HISTORY,

        //chung
        MA_DVIQLY,
        MA_NVIEN,

        //server
        DATE_CALL_API,
        TYPE_CALL_API, // kiểu download hay upload
        TYPE_RESULT, // thành công hay thất bại
        MESSAGE_RESULT, //nội dung thông báo

        //thông tin
        SO_BBAN,
        SO_CTO_THAO,
        SO_CTO_TREO,
        SO_BBAN_TUTI,
        SO_TU,
        SO_TI,
        SO_TRAM,
        SO_CHUNGLOAI;


        public static String getName() {
            return "TABLE_HISTORY";
        }
    }

    @PrimaryKey
    @AutoIncrement
    @Collumn(name = "ID_TABLE_HISTORY", type = TYPE.INTEGER, other = "NOT NULL")
    private int ID_TABLE_HISTORY;

    @Collumn(name = "MA_DVIQLY", type = TYPE.TEXT, other = "NOT NULL")
    private String MA_DVIQLY;

    @Collumn(name = "MA_NVIEN", type = TYPE.TEXT)
    private String MA_NVIEN;

    @Collumn(name = "DATE_CALL_API", type = TYPE.TEXT)
    private String DATE_CALL_API;

    @Collumn(name = "TYPE_CALL_API", type = TYPE.TEXT)
    private String TYPE_CALL_API;

    @Collumn(name = "TYPE_RESULT", type = TYPE.TEXT)
    private String TYPE_RESULT;

    @Collumn(name = "MESSAGE_RESULT", type = TYPE.TEXT)
    private String MESSAGE_RESULT;

    @Collumn(name = "SO_BBAN_API", type = TYPE.TEXT)
    private int SO_BBAN_API;

    @Collumn(name = "SO_CTO_THAO_API", type = TYPE.TEXT)
    private int SO_CTO_THAO_API;

    @Collumn(name = "SO_CTO_TREO_API", type = TYPE.TEXT)
    private int SO_CTO_TREO_API;

    @Collumn(name = "SO_BBAN_TUTI_API", type = TYPE.TEXT)
    private int SO_BBAN_TUTI_API;

    @Collumn(name = "SO_TU_API", type = TYPE.TEXT)
    private int SO_TU_API;

    @Collumn(name = "SO_TI_API", type = TYPE.TEXT)
    private int SO_TI_API;

    @Collumn(name = "SO_TRAM_API", type = TYPE.TEXT)
    private int SO_TRAM_API;

    @Collumn(name = "SO_CHUNGLOAI_API", type = TYPE.INTEGER)
    private int SO_CHUNGLOAI_API;


    public TABLE_HISTORY() {
    }

    public TABLE_HISTORY(@Params(name = "ID_TABLE_HISTORY") int ID_TABLE_HISTORY,
                         @Params(name = "MA_DVIQLY") String MA_DVIQLY,
                         @Params(name = "MA_NVIEN") String MA_NVIEN,

                         @Params(name = "DATE_CALL_API") String DATE_CALL_API,
                         @Params(name = "TYPE_CALL_API") String TYPE_CALL_API,
                         @Params(name = "TYPE_RESULT") String TYPE_RESULT,
                         @Params(name = "MESSAGE_RESULT") String MESSAGE_RESULT,

                         @Params(name = "SO_BBAN_API") int SO_BBAN_API,
                         @Params(name = "SO_CTO_THAO_API") int SO_CTO_THAO_API,
                         @Params(name = "SO_CTO_TREO_API") int SO_CTO_TREO_API,
                         @Params(name = "SO_BBAN_TUTI_API") int SO_BBAN_TUTI_API,
                         @Params(name = "SO_TU_API") int SO_TU_API,
                         @Params(name = "SO_TI_API") int SO_TI_API,
                         @Params(name = "SO_TRAM_API") int SO_TRAM_API,
                         @Params(name = "SO_CHUNGLOAI_API") int SO_CHUNGLOAI_API
    ) {
        this.ID_TABLE_HISTORY = ID_TABLE_HISTORY;
        this.MA_DVIQLY = MA_DVIQLY;
        this.MA_NVIEN = MA_NVIEN;

        this.DATE_CALL_API = DATE_CALL_API;
        this.TYPE_CALL_API = TYPE_CALL_API;
        this.TYPE_RESULT = TYPE_RESULT;
        this.MESSAGE_RESULT = MESSAGE_RESULT;


        this.SO_BBAN_API = SO_BBAN_API;
        this.SO_CTO_THAO_API = SO_CTO_THAO_API;
        this.SO_CTO_TREO_API = SO_CTO_TREO_API;
        this.SO_BBAN_TUTI_API = SO_BBAN_TUTI_API;
        this.SO_TU_API = SO_TU_API;
        this.SO_TI_API = SO_TI_API;
        this.SO_TRAM_API = SO_TRAM_API;
        this.SO_CHUNGLOAI_API = SO_CHUNGLOAI_API;
    }

    public int getID_TABLE_HISTORY() {
        return ID_TABLE_HISTORY;
    }

    public void setID_TABLE_HISTORY(int ID_TABLE_HISTORY) {
        this.ID_TABLE_HISTORY = ID_TABLE_HISTORY;
    }

    public String getMA_DVIQLY() {
        return MA_DVIQLY;
    }

    public void setMA_DVIQLY(String MA_DVIQLY) {
        this.MA_DVIQLY = MA_DVIQLY;
    }

    public String getMA_NVIEN() {
        return MA_NVIEN;
    }

    public void setMA_NVIEN(String MA_NVIEN) {
        this.MA_NVIEN = MA_NVIEN;
    }

    public String getDATE_CALL_API() {
        return DATE_CALL_API;
    }

    public void setDATE_CALL_API(String DATE_CALL_API) {
        this.DATE_CALL_API = DATE_CALL_API;
    }

    public String getTYPE_CALL_API() {
        return TYPE_CALL_API;
    }

    public void setTYPE_CALL_API(String TYPE_CALL_API) {
        this.TYPE_CALL_API = TYPE_CALL_API;
    }

    public String getTYPE_RESULT() {
        return TYPE_RESULT;
    }

    public void setTYPE_RESULT(String TYPE_RESULT) {
        this.TYPE_RESULT = TYPE_RESULT;
    }

    public String getMESSAGE_RESULT() {
        return MESSAGE_RESULT;
    }

    public void setMESSAGE_RESULT(String MESSAGE_RESULT) {
        this.MESSAGE_RESULT = MESSAGE_RESULT;
    }

    public int getSO_BBAN_API() {
        return SO_BBAN_API;
    }

    public void setSO_BBAN_API(int SO_BBAN_API) {
        this.SO_BBAN_API = SO_BBAN_API;
    }

    public int getSO_CTO_THAO_API() {
        return SO_CTO_THAO_API;
    }

    public void setSO_CTO_THAO_API(int SO_CTO_THAO_API) {
        this.SO_CTO_THAO_API = SO_CTO_THAO_API;
    }

    public int getSO_CTO_TREO_API() {
        return SO_CTO_TREO_API;
    }

    public void setSO_CTO_TREO_API(int SO_CTO_TREO_API) {
        this.SO_CTO_TREO_API = SO_CTO_TREO_API;
    }

    public int getSO_BBAN_TUTI_API() {
        return SO_BBAN_TUTI_API;
    }

    public void setSO_BBAN_TUTI_API(int SO_BBAN_TUTI_API) {
        this.SO_BBAN_TUTI_API = SO_BBAN_TUTI_API;
    }

    public int getSO_TU_API() {
        return SO_TU_API;
    }

    public void setSO_TU_API(int SO_TU_API) {
        this.SO_TU_API = SO_TU_API;
    }

    public int getSO_TI_API() {
        return SO_TI_API;
    }

    public void setSO_TI_API(int SO_TI_API) {
        this.SO_TI_API = SO_TI_API;
    }

    public int getSO_TRAM_API() {
        return SO_TRAM_API;
    }

    public void setSO_TRAM_API(int SO_TRAM_API) {
        this.SO_TRAM_API = SO_TRAM_API;
    }

    public int getSO_CHUNGLOAI_API() {
        return SO_CHUNGLOAI_API;
    }

    public void setSO_CHUNGLOAI_API(int SO_CHUNGLOAI_API) {
        this.SO_CHUNGLOAI_API = SO_CHUNGLOAI_API;
    }
}
