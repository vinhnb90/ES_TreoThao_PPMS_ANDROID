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

@Table(name = "TABLE_CHITIET_TUTI")
public class TABLE_CHITIET_TUTI {
    @EnumNameCollumn()
    public enum table {
        ID_TABLE_CHITIET_TUTI,
        MA_CLOAI,
        LOAI_TU_TI,
        MO_TA,
        SO_PHA,
        TYSO_DAU,
        CAP_CXAC,
        CAP_DAP,
        MA_NUOC,
        MA_HANG,
        TRANG_THAI,
        IS_TU,
        ID_BBAN_TUTI,
        ID_CHITIET_TUTI,
        SO_TU_TI,
        NUOC_SX,
        SO_TEM_KDINH,
        NGAY_KDINH,
        MA_CHI_KDINH,

        MA_CHI_HOP_DDAY,
        SO_VONG_THANH_CAI,
        TYSO_BIEN,
        MA_BDONG,
        MA_DVIQLY,

        TRANG_THAI_DU_LIEU;

        public static String getName()
        {
            return "TABLE_CHITIET_TUTI";
        }
    }



    @PrimaryKey
    @AutoIncrement
    @Collumn(name = "ID_TABLE_CHITIET_TUTI", type = TYPE.INTEGER, other = "NOT NULL")
    private int ID_TABLE_CHITIET_TUTI;

    @Collumn(name = "MA_CLOAI", type = TYPE.TEXT)
    private String MA_CLOAI;

    @Collumn(name = "LOAI_TU_TI", type = TYPE.TEXT)
    private String LOAI_TU_TI;

    @Collumn(name = "MO_TA", type = TYPE.TEXT)
    private String MO_TA;

    @Collumn(name = "SO_PHA", type = TYPE.INTEGER)
    private int SO_PHA;

    @Collumn(name = "TYSO_DAU", type = TYPE.TEXT)
    private String TYSO_DAU;

    @Collumn(name = "CAP_CXAC", type = TYPE.INTEGER)
    private int CAP_CXAC;

    @Collumn(name = "CAP_DAP", type = TYPE.INTEGER)
    private int CAP_DAP;

    @Collumn(name = "MA_NUOC", type = TYPE.TEXT)
    private String MA_NUOC;

    @Collumn(name = "MA_HANG", type = TYPE.TEXT)
    private String MA_HANG;

    @Collumn(name = "TRANG_THAI", type = TYPE.INTEGER)
    private int TRANG_THAI;

    @Collumn(name = "IS_TU", type = TYPE.TEXT)
    private String IS_TU;

    @Collumn(name = "ID_BBAN_TUTI", type = TYPE.INTEGER)
    private int ID_BBAN_TUTI;

    @Collumn(name = "ID_CHITIET_TUTI", type = TYPE.INTEGER)
    private int ID_CHITIET_TUTI;

    @Collumn(name = "SO_TU_TI", type = TYPE.TEXT)
    private String SO_TU_TI;

    @Collumn(name = "NUOC_SX", type = TYPE.TEXT)
    private String NUOC_SX;

    @Collumn(name = "SO_TEM_KDINH", type = TYPE.TEXT)
    private String SO_TEM_KDINH;

    @Collumn(name = "NGAY_KDINH", type = TYPE.TEXT)
    private String NGAY_KDINH;

    @Collumn(name = "MA_CHI_KDINH", type = TYPE.TEXT)
    private String MA_CHI_KDINH;

    @Collumn(name = "MA_CHI_HOP_DDAY", type = TYPE.TEXT)
    private String MA_CHI_HOP_DDAY;

    @Collumn(name = "SO_VONG_THANH_CAI", type = TYPE.INTEGER)
    private int SO_VONG_THANH_CAI;

    @Collumn(name = "TYSO_BIEN", type = TYPE.TEXT)
    private String TYSO_BIEN;

    @Collumn(name = "MA_BDONG", type = TYPE.TEXT)
    private String MA_BDONG;

    @Collumn(name = "MA_DVIQLY", type = TYPE.TEXT)
    private String MA_DVIQLY;

    @Collumn(name = "TRANG_THAI_DU_LIEU", type = TYPE.TEXT)
    private String TRANG_THAI_DU_LIEU;

    public TABLE_CHITIET_TUTI() {
    }

    public TABLE_CHITIET_TUTI(@Params(name = "ID_TABLE_CHITIET_TUTI") int ID_TABLE_CHITIET_TUTI,
                              @Params(name = "MA_CLOAI") String MA_CLOAI,
                              @Params(name = "LOAI_TU_TI") String LOAI_TU_TI,
                              @Params(name = "MO_TA") String MO_TA,
                              @Params(name = "SO_PHA") int SO_PHA,
                              @Params(name = "TYSO_DAU") String TYSO_DAU,
                              @Params(name = "CAP_CXAC") int CAP_CXAC,
                              @Params(name = "CAP_DAP") int CAP_DAP,
                              @Params(name = "MA_NUOC") String MA_NUOC,
                              @Params(name = "MA_HANG") String MA_HANG,
                              @Params(name = "TRANG_THAI") int TRANG_THAI,
                              @Params(name = "IS_TU") String IS_TU,
                              @Params(name = "ID_BBAN_TUTI") int ID_BBAN_TUTI,
                              @Params(name = "ID_CHITIET_TUTI") int ID_CHITIET_TUTI,
                              @Params(name = "SO_TU_TI") String SO_TU_TI,
                              @Params(name = "NUOC_SX") String NUOC_SX,
                              @Params(name = "SO_TEM_KDINH") String SO_TEM_KDINH,
                              @Params(name = "NGAY_KDINH") String NGAY_KDINH,
                              @Params(name = "MA_CHI_KDINH") String MA_CHI_KDINH,
                              @Params(name = "MA_CHI_HOP_DDAY") String MA_CHI_HOP_DDAY,
                              @Params(name = "SO_VONG_THANH_CAI") int SO_VONG_THANH_CAI,
                              @Params(name = "TYSO_BIEN") String TYSO_BIEN,
                              @Params(name = "MA_BDONG") String MA_BDONG,
                              @Params(name = "MA_DVIQLY") String MA_DVIQLY,
                              @Params(name = "TRANG_THAI_DU_LIEU") String TRANG_THAI_DU_LIEU
                              ) {
        this.ID_TABLE_CHITIET_TUTI = ID_TABLE_CHITIET_TUTI;
        this.MA_CLOAI = MA_CLOAI;
        this.LOAI_TU_TI = LOAI_TU_TI;
        this.MO_TA = MO_TA;
        this.SO_PHA = SO_PHA;
        this.TYSO_DAU = TYSO_DAU;
        this.CAP_CXAC = CAP_CXAC;
        this.CAP_DAP = CAP_DAP;
        this.MA_NUOC = MA_NUOC;
        this.MA_HANG = MA_HANG;
        this.TRANG_THAI = TRANG_THAI;
        this.IS_TU = IS_TU;
        this.ID_BBAN_TUTI = ID_BBAN_TUTI;
        this.ID_CHITIET_TUTI = ID_CHITIET_TUTI;
        this.SO_TU_TI = SO_TU_TI;
        this.NUOC_SX = NUOC_SX;
        this.SO_TEM_KDINH = SO_TEM_KDINH;
        this.NGAY_KDINH = NGAY_KDINH;
        this.MA_CHI_KDINH = MA_CHI_KDINH;
        this.MA_CHI_HOP_DDAY = MA_CHI_HOP_DDAY;
        this.SO_VONG_THANH_CAI = SO_VONG_THANH_CAI;
        this.TYSO_BIEN = TYSO_BIEN;
        this.MA_BDONG = MA_BDONG;
        this.MA_DVIQLY = MA_DVIQLY;
        this.TRANG_THAI_DU_LIEU = TRANG_THAI_DU_LIEU;
    }

    public int getID_TABLE_CHITIET_TUTI() {
        return ID_TABLE_CHITIET_TUTI;
    }

    public void setID_TABLE_CHITIET_TUTI(int ID_TABLE_CHITIET_TUTI) {
        this.ID_TABLE_CHITIET_TUTI = ID_TABLE_CHITIET_TUTI;
    }

    public String getMA_CLOAI() {
        return MA_CLOAI;
    }

    public void setMA_CLOAI(String MA_CLOAI) {
        this.MA_CLOAI = MA_CLOAI;
    }

    public String getLOAI_TU_TI() {
        return LOAI_TU_TI;
    }

    public void setLOAI_TU_TI(String LOAI_TU_TI) {
        this.LOAI_TU_TI = LOAI_TU_TI;
    }

    public String getMO_TA() {
        return MO_TA;
    }

    public void setMO_TA(String MO_TA) {
        this.MO_TA = MO_TA;
    }

    public int getSO_PHA() {
        return SO_PHA;
    }

    public void setSO_PHA(int SO_PHA) {
        this.SO_PHA = SO_PHA;
    }

    public String getTYSO_DAU() {
        return TYSO_DAU;
    }

    public void setTYSO_DAU(String TYSO_DAU) {
        this.TYSO_DAU = TYSO_DAU;
    }

    public int getCAP_CXAC() {
        return CAP_CXAC;
    }

    public void setCAP_CXAC(int CAP_CXAC) {
        this.CAP_CXAC = CAP_CXAC;
    }

    public int getCAP_DAP() {
        return CAP_DAP;
    }

    public void setCAP_DAP(int CAP_DAP) {
        this.CAP_DAP = CAP_DAP;
    }

    public String getMA_NUOC() {
        return MA_NUOC;
    }

    public void setMA_NUOC(String MA_NUOC) {
        this.MA_NUOC = MA_NUOC;
    }

    public String getMA_HANG() {
        return MA_HANG;
    }

    public void setMA_HANG(String MA_HANG) {
        this.MA_HANG = MA_HANG;
    }

    public int getTRANG_THAI() {
        return TRANG_THAI;
    }

    public void setTRANG_THAI(int TRANG_THAI) {
        this.TRANG_THAI = TRANG_THAI;
    }

    public String getIS_TU() {
        return IS_TU;
    }

    public void setIS_TU(String IS_TU) {
        this.IS_TU = IS_TU;
    }

    public int getID_BBAN_TUTI() {
        return ID_BBAN_TUTI;
    }

    public void setID_BBAN_TUTI(int ID_BBAN_TUTI) {
        this.ID_BBAN_TUTI = ID_BBAN_TUTI;
    }

    public int getID_CHITIET_TUTI() {
        return ID_CHITIET_TUTI;
    }

    public void setID_CHITIET_TUTI(int ID_CHITIET_TUTI) {
        this.ID_CHITIET_TUTI = ID_CHITIET_TUTI;
    }

    public String getSO_TU_TI() {
        return SO_TU_TI;
    }

    public void setSO_TU_TI(String SO_TU_TI) {
        this.SO_TU_TI = SO_TU_TI;
    }

    public String getNUOC_SX() {
        return NUOC_SX;
    }

    public void setNUOC_SX(String NUOC_SX) {
        this.NUOC_SX = NUOC_SX;
    }

    public String getSO_TEM_KDINH() {
        return SO_TEM_KDINH;
    }

    public void setSO_TEM_KDINH(String SO_TEM_KDINH) {
        this.SO_TEM_KDINH = SO_TEM_KDINH;
    }

    public String getNGAY_KDINH() {
        return NGAY_KDINH;
    }

    public void setNGAY_KDINH(String NGAY_KDINH) {
        this.NGAY_KDINH = NGAY_KDINH;
    }

    public String getMA_CHI_KDINH() {
        return MA_CHI_KDINH;
    }

    public void setMA_CHI_KDINH(String MA_CHI_KDINH) {
        this.MA_CHI_KDINH = MA_CHI_KDINH;
    }

    public String getMA_CHI_HOP_DDAY() {
        return MA_CHI_HOP_DDAY;
    }

    public void setMA_CHI_HOP_DDAY(String MA_CHI_HOP_DDAY) {
        this.MA_CHI_HOP_DDAY = MA_CHI_HOP_DDAY;
    }

    public int getSO_VONG_THANH_CAI() {
        return SO_VONG_THANH_CAI;
    }

    public void setSO_VONG_THANH_CAI(int SO_VONG_THANH_CAI) {
        this.SO_VONG_THANH_CAI = SO_VONG_THANH_CAI;
    }

    public String getTYSO_BIEN() {
        return TYSO_BIEN;
    }

    public void setTYSO_BIEN(String TYSO_BIEN) {
        this.TYSO_BIEN = TYSO_BIEN;
    }

    public String getMA_BDONG() {
        return MA_BDONG;
    }

    public void setMA_BDONG(String MA_BDONG) {
        this.MA_BDONG = MA_BDONG;
    }

    public String getMA_DVIQLY() {
        return MA_DVIQLY;
    }

    public void setMA_DVIQLY(String MA_DVIQLY) {
        this.MA_DVIQLY = MA_DVIQLY;
    }

    public String getTRANG_THAI_DU_LIEU() {
        return TRANG_THAI_DU_LIEU;
    }

    public void setTRANG_THAI_DU_LIEU(String TRANG_THAI_DU_LIEU) {
        this.TRANG_THAI_DU_LIEU = TRANG_THAI_DU_LIEU;
    }
}
