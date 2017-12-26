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

@Table(name = "TABLE_HISTORY_UPLOAD")
public class TABLE_HISTORY_UPLOAD {

    @EnumNameCollumn()
    public enum table {
        ID_TABLE_HISTORY_DETAIL,
        ID_TABLE_HISTORY,
        MA_NVIEN,
        ID_BBAN_TRTH,
        ID_BBAN_CONGTO,
        TYPE_RESPONSE_UPLOAD,
        MESSAGE_RESPONSE;

        public static String getName() {
            return "TABLE_HISTORY_UPLOAD";
        }
    }

    @PrimaryKey
    @AutoIncrement
    @Collumn(name = "ID_TABLE_HISTORY_DETAIL", type = TYPE.INTEGER, other = "NOT NULL")
    private int ID_TABLE_HISTORY_DETAIL;

    @Collumn(name = "ID_TABLE_HISTORY", type = TYPE.INTEGER, other = "NOT NULL")
    private int ID_TABLE_HISTORY;

    @Collumn(name = "MA_NVIEN")
    private String MA_NVIEN;

    @Collumn(name = "ID_BBAN_TRTH", type = TYPE.INTEGER)
    private int ID_BBAN_TRTH;

    @Collumn(name = "ID_BBAN_CONGTO", type = TYPE.INTEGER)
    private int ID_BBAN_CONGTO;

    @Collumn(name = "TYPE_RESPONSE_UPLOAD")
    private String TYPE_RESPONSE_UPLOAD;

    @Collumn(name = "MESSAGE_RESPONSE")
    private String MESSAGE_RESPONSE;


    public TABLE_HISTORY_UPLOAD() {
    }

    public TABLE_HISTORY_UPLOAD(
            @Params(name = "ID_TABLE_HISTORY_DETAIL") int ID_TABLE_HISTORY_DETAIL,
            @Params(name = "ID_TABLE_HISTORY") int ID_TABLE_HISTORY,
            @Params(name = "MA_NVIEN") String MA_NVIEN,
            @Params(name = "ID_BBAN_TRTH") int ID_BBAN_TRTH,
            @Params(name = "ID_BBAN_CONGTO") int ID_BBAN_CONGTO,
            @Params(name = "TYPE_RESPONSE_UPLOAD") String TYPE_RESPONSE_UPLOAD,
            @Params(name = "MESSAGE_RESPONSE") String MESSAGE_RESPONSE

    ) {
        this.ID_TABLE_HISTORY_DETAIL = ID_TABLE_HISTORY_DETAIL;
        this.ID_TABLE_HISTORY = ID_TABLE_HISTORY;
        this.MA_NVIEN = MA_NVIEN;
        this.ID_BBAN_TRTH = ID_BBAN_TRTH;
        this.ID_BBAN_CONGTO = ID_BBAN_CONGTO;
        this.TYPE_RESPONSE_UPLOAD = TYPE_RESPONSE_UPLOAD;
        this.MESSAGE_RESPONSE = MESSAGE_RESPONSE;

    }


    public int getID_TABLE_HISTORY_DETAIL() {
        return ID_TABLE_HISTORY_DETAIL;
    }

    public void setID_TABLE_HISTORY_DETAIL(int ID_TABLE_HISTORY_DETAIL) {
        this.ID_TABLE_HISTORY_DETAIL = ID_TABLE_HISTORY_DETAIL;
    }

    public int getID_TABLE_HISTORY() {
        return ID_TABLE_HISTORY;
    }

    public void setID_TABLE_HISTORY(int ID_TABLE_HISTORY) {
        this.ID_TABLE_HISTORY = ID_TABLE_HISTORY;
    }

    public String getMA_NVIEN() {
        return MA_NVIEN;
    }

    public void setMA_NVIEN(String MA_NVIEN) {
        this.MA_NVIEN = MA_NVIEN;
    }

    public int getID_BBAN_TRTH() {
        return ID_BBAN_TRTH;
    }

    public void setID_BBAN_TRTH(int ID_BBAN_TRTH) {
        this.ID_BBAN_TRTH = ID_BBAN_TRTH;
    }

    public String getTYPE_RESPONSE_UPLOAD() {
        return TYPE_RESPONSE_UPLOAD;
    }

    public void setTYPE_RESPONSE_UPLOAD(String TYPE_RESPONSE_UPLOAD) {
        this.TYPE_RESPONSE_UPLOAD = TYPE_RESPONSE_UPLOAD;
    }

    public String getMESSAGE_RESPONSE() {
        return MESSAGE_RESPONSE;
    }

    public void setMESSAGE_RESPONSE(String MESSAGE_RESPONSE) {
        this.MESSAGE_RESPONSE = MESSAGE_RESPONSE;
    }

    public int getID_BBAN_CONGTO() {
        return ID_BBAN_CONGTO;
    }

    public void setID_BBAN_CONGTO(int ID_BBAN_CONGTO) {
        this.ID_BBAN_CONGTO = ID_BBAN_CONGTO;
    }
}
