package es.vinhnb.ttht.entity.sqlite;


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
@Deprecated
@Table(name = "TABLE_CONGTO_TI")
public class TABLE_CONGTO_TI {
    @EnumNameCollumn()
    public enum declared {
        ID_BBAN_TRTH, ID_CONGTO_TI, ID_TABLE_CONGTO_TI, LAN, MA_BDONG, MA_CLOAI, MA_CNANG, MA_DVIQLY, MA_TI, NGAY_BDONG, NGAY_SUA, NGAY_TAO, NGUOI_SUA, NGUOI_TAO, SO_TI, TYSO_DAU;
    }

    @PrimaryKey
    @AutoIncrement
    @Collumn(name = "ID_TABLE_CONGTO_TI", type = TYPE.INTEGER, other = "NOT NULL")
    private int ID_TABLE_CONGTO_TI;

    @Collumn(name = "MA_DVIQLY", type = TYPE.TEXT, other = "NOT NULL")
    private String MA_DVIQLY;

    @Collumn(name = "ID_BBAN_TRTH", type = TYPE.INTEGER, other = "NOT NULL")
    private int ID_BBAN_TRTH;

    @Collumn(name = "MA_TI", type = TYPE.TEXT)
    private String MA_TI;

    @Collumn(name = "SO_TI", type = TYPE.TEXT)
    private String SO_TI;

    @Collumn(name = "LAN", type = TYPE.TEXT)
    private String LAN;

    @Collumn(name = "MA_BDONG", type = TYPE.TEXT)
    private String MA_BDONG;

    @Collumn(name = "NGAY_BDONG", type = TYPE.TEXT)
    private String NGAY_BDONG;

    @Collumn(name = "MA_CLOAI", type = TYPE.TEXT)
    private String MA_CLOAI;

    @Collumn(name = "TYSO_DAU", type = TYPE.TEXT)
    private String TYSO_DAU;

    @Collumn(name = "NGAY_TAO", type = TYPE.TEXT)
    private String NGAY_TAO;

    @Collumn(name = "NGUOI_TAO", type = TYPE.TEXT)
    private String NGUOI_TAO;

    @Collumn(name = "NGAY_SUA", type = TYPE.TEXT)
    private String NGAY_SUA;

    @Collumn(name = "NGUOI_SUA", type = TYPE.TEXT)
    private String NGUOI_SUA;

    @Collumn(name = "MA_CNANG", type = TYPE.TEXT)
    private String MA_CNANG;

    @Collumn(name = "ID_CONGTO_TI", type = TYPE.INTEGER)
    private int ID_CONGTO_TI;

    public TABLE_CONGTO_TI() {
    }

    public TABLE_CONGTO_TI(@Params(name = "ID_TABLE_CONGTO_TI") int ID_TABLE_CONGTO_TI,
                           @Params(name = "MA_DVIQLY") String MA_DVIQLY,
                           @Params(name = "ID_BBAN_TRTH") int ID_BBAN_TRTH,
                           @Params(name = "MA_TI") String MA_TI,
                           @Params(name = "SO_TI") String SO_TI,
                           @Params(name = "LAN") String LAN,
                           @Params(name = "MA_BDONG") String MA_BDONG,
                           @Params(name = "NGAY_BDONG") String NGAY_BDONG,
                           @Params(name = "MA_CLOAI") String MA_CLOAI,
                           @Params(name = "TYSO_DAU") String TYSO_DAU,
                           @Params(name = "NGAY_TAO") String NGAY_TAO,
                           @Params(name = "NGUOI_TAO") String NGUOI_TAO,
                           @Params(name = "NGAY_SUA") String NGAY_SUA,
                           @Params(name = "NGUOI_SUA") String NGUOI_SUA,
                           @Params(name = "MA_CNANG") String MA_CNANG,
                           @Params(name = "ID_CONGTO_TI") int ID_CONGTO_TI) {
        this.ID_TABLE_CONGTO_TI = ID_TABLE_CONGTO_TI;
        this.MA_DVIQLY = MA_DVIQLY;
        this.ID_BBAN_TRTH = ID_BBAN_TRTH;
        this.MA_TI = MA_TI;
        this.SO_TI = SO_TI;
        this.LAN = LAN;
        this.MA_BDONG = MA_BDONG;
        this.NGAY_BDONG = NGAY_BDONG;
        this.MA_CLOAI = MA_CLOAI;
        this.TYSO_DAU = TYSO_DAU;
        this.NGAY_TAO = NGAY_TAO;
        this.NGUOI_TAO = NGUOI_TAO;
        this.NGAY_SUA = NGAY_SUA;
        this.NGUOI_SUA = NGUOI_SUA;
        this.MA_CNANG = MA_CNANG;
        this.ID_CONGTO_TI = ID_CONGTO_TI;
    }

    public int getID_TABLE_CONGTO_TI() {
        return ID_TABLE_CONGTO_TI;
    }

    public void setID_TABLE_CONGTO_TI(int ID_TABLE_CONGTO_TI) {
        this.ID_TABLE_CONGTO_TI = ID_TABLE_CONGTO_TI;
    }

    public String getMA_DVIQLY() {
        return MA_DVIQLY;
    }

    public void setMA_DVIQLY(String MA_DVIQLY) {
        this.MA_DVIQLY = MA_DVIQLY;
    }

    public int getID_BBAN_TRTH() {
        return ID_BBAN_TRTH;
    }

    public void setID_BBAN_TRTH(int ID_BBAN_TRTH) {
        this.ID_BBAN_TRTH = ID_BBAN_TRTH;
    }

    public String getMA_TI() {
        return MA_TI;
    }

    public void setMA_TI(String MA_TI) {
        this.MA_TI = MA_TI;
    }

    public String getSO_TI() {
        return SO_TI;
    }

    public void setSO_TI(String SO_TI) {
        this.SO_TI = SO_TI;
    }

    public String getLAN() {
        return LAN;
    }

    public void setLAN(String LAN) {
        this.LAN = LAN;
    }

    public String getMA_BDONG() {
        return MA_BDONG;
    }

    public void setMA_BDONG(String MA_BDONG) {
        this.MA_BDONG = MA_BDONG;
    }

    public String getNGAY_BDONG() {
        return NGAY_BDONG;
    }

    public void setNGAY_BDONG(String NGAY_BDONG) {
        this.NGAY_BDONG = NGAY_BDONG;
    }

    public String getMA_CLOAI() {
        return MA_CLOAI;
    }

    public void setMA_CLOAI(String MA_CLOAI) {
        this.MA_CLOAI = MA_CLOAI;
    }

    public String getTYSO_DAU() {
        return TYSO_DAU;
    }

    public void setTYSO_DAU(String TYSO_DAU) {
        this.TYSO_DAU = TYSO_DAU;
    }

    public String getNGAY_TAO() {
        return NGAY_TAO;
    }

    public void setNGAY_TAO(String NGAY_TAO) {
        this.NGAY_TAO = NGAY_TAO;
    }

    public String getNGUOI_TAO() {
        return NGUOI_TAO;
    }

    public void setNGUOI_TAO(String NGUOI_TAO) {
        this.NGUOI_TAO = NGUOI_TAO;
    }

    public String getNGAY_SUA() {
        return NGAY_SUA;
    }

    public void setNGAY_SUA(String NGAY_SUA) {
        this.NGAY_SUA = NGAY_SUA;
    }

    public String getNGUOI_SUA() {
        return NGUOI_SUA;
    }

    public void setNGUOI_SUA(String NGUOI_SUA) {
        this.NGUOI_SUA = NGUOI_SUA;
    }

    public String getMA_CNANG() {
        return MA_CNANG;
    }

    public void setMA_CNANG(String MA_CNANG) {
        this.MA_CNANG = MA_CNANG;
    }

    public int getID_CONGTO_TI() {
        return ID_CONGTO_TI;
    }

    public void setID_CONGTO_TI(int ID_CONGTO_TI) {
        this.ID_CONGTO_TI = ID_CONGTO_TI;
    }
}
