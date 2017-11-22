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

@Table(name = "TABLE_SESSION")
public class TABLE_SESSION {
    @EnumNameCollumn()
    public enum declared {
        ID_TABLE_SESSION,
        MA_DVIQLY,
        USERNAME,
        PASSWORD,
        DATE_LOGIN;
    }


    @PrimaryKey
    @AutoIncrement
    @Collumn(name = "ID_TABLE_SESSION", type = TYPE.INTEGER, other = "NOT NULL")
    public int ID_TABLE_SESSION;

    @Collumn(name = "MA_DVIQLY", type = TYPE.TEXT, other = "NOT NULL")
    public String MA_DVIQLY;

    @Collumn(name = "USERNAME", type = TYPE.TEXT, other = "NOT NULL")
    public String USERNAME;

    @Collumn(name = "PASSWORD", type = TYPE.TEXT, other = "NOT NULL")
    public String PASSWORD;

    @Collumn(name = "DATE_LOGIN", type = TYPE.TEXT, other = "NOT NULL")
    public String DATE_LOGIN;

    public TABLE_SESSION() {
    }

    public TABLE_SESSION(@Params(name = "ID_TABLE_SESSION") int ID_TABLE_SESSION,
                         @Params(name = "MA_DVIQLY") String MA_DVIQLY,
                         @Params(name = "USERNAME") String USERNAME,
                         @Params(name = "PASSWORD") String PASSWORD,
                         @Params(name = "DATE_LOGIN") String DATE_LOGIN) {
        this.ID_TABLE_SESSION = ID_TABLE_SESSION;
        this.MA_DVIQLY = MA_DVIQLY;
        this.USERNAME = USERNAME;
        this.PASSWORD = PASSWORD;
        this.DATE_LOGIN = DATE_LOGIN;
    }

    public int getID_TABLE_SESSION() {
        return ID_TABLE_SESSION;
    }

    public void setID_TABLE_SESSION(int ID_TABLE_SESSION) {
        this.ID_TABLE_SESSION = ID_TABLE_SESSION;
    }

    public String getMA_DVIQLY() {
        return MA_DVIQLY;
    }

    public void setMA_DVIQLY(String MA_DVIQLY) {
        this.MA_DVIQLY = MA_DVIQLY;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public String getDATE_LOGIN() {
        return DATE_LOGIN;
    }

    public void setDATE_LOGIN(String DATE_LOGIN) {
        this.DATE_LOGIN = DATE_LOGIN;
    }
}
