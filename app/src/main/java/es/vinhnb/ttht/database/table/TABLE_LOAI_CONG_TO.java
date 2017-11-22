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

@Table(name = "TABLE_LOAI_CONG_TO")
public class TABLE_LOAI_CONG_TO {
    @EnumNameCollumn()
    public enum declared {
        ID_TABLE_LOAI_CONG_TO,
        MA_CLOAI,
        TEN_LOAI_CTO,
        MO_TA,
        SO_PHA,
        SO_DAY,
        HS_NHAN,
        SO_CS,
        CAP_CXAC_P,
        CAP_CXAC_Q,
        DONG_DIEN,
        DIEN_AP,
        VH_CONG,
        MA_NUOC,
        MA_HANG,
        HANGSO_K,
        PTHUC_DOXA,
        TEN_NUOC;
    }

    @PrimaryKey
    @AutoIncrement
    @Collumn(name = "ID_TABLE_LOAI_CONG_TO", type = TYPE.INTEGER, other = "NOT NULL")
    private int ID_TABLE_LOAI_CONG_TO;

    @Collumn(name = "MA_CLOAI", type = TYPE.TEXT, other = "NOT NULL")
    private String MA_CLOAI;

    @Collumn(name = "TEN_LOAI_CTO", type = TYPE.TEXT, other = "NOT NULL")
    private String TEN_LOAI_CTO;

    @Collumn(name = "MO_TA", type = TYPE.TEXT)
    private String MO_TA;

    @Collumn(name = "SO_PHA", type = TYPE.TEXT)
    private String SO_PHA;

    @Collumn(name = "SO_DAY", type = TYPE.TEXT)
    private String SO_DAY;

    @Collumn(name = "HS_NHAN", type = TYPE.INTEGER)
    private int HS_NHAN;

    @Collumn(name = "SO_CS", type = TYPE.TEXT)
    private String SO_CS;

    @Collumn(name = "CAP_CXAC_P", type = TYPE.TEXT)
    private String CAP_CXAC_P;

    @Collumn(name = "CAP_CXAC_Q", type = TYPE.TEXT)
    private String CAP_CXAC_Q;

    @Collumn(name = "DONG_DIEN", type = TYPE.TEXT)
    private String DONG_DIEN;

    @Collumn(name = "DIEN_AP", type = TYPE.TEXT)
    private String DIEN_AP;

    @Collumn(name = "VH_CONG", type = TYPE.TEXT)
    private String VH_CONG;

    @Collumn(name = "MA_NUOC", type = TYPE.TEXT)
    private String MA_NUOC;

    @Collumn(name = "MA_HANG", type = TYPE.TEXT)
    private String MA_HANG;

    @Collumn(name = "HANGSO_K", type = TYPE.TEXT)
    private String HANGSO_K;

    @Collumn(name = "PTHUC_DOXA", type = TYPE.TEXT)
    private String PTHUC_DOXA;

    @Collumn(name = "TEN_NUOC", type = TYPE.TEXT)
    private String TEN_NUOC;

    public TABLE_LOAI_CONG_TO() {
    }

    public TABLE_LOAI_CONG_TO(@Params(name = "ID_TABLE_LOAI_CONG_TO") int ID_TABLE_LOAI_CONG_TO,
                              @Params(name = "MA_CLOAI") String MA_CLOAI,
                              @Params(name = "TEN_LOAI_CTO") String TEN_LOAI_CTO,
                              @Params(name = "MO_TA") String MO_TA,
                              @Params(name = "SO_PHA") String SO_PHA,
                              @Params(name = "SO_DAY") String SO_DAY,
                              @Params(name = "HS_NHAN") int HS_NHAN,
                              @Params(name = "SO_CS") String SO_CS,
                              @Params(name = "CAP_CXAC_P") String CAP_CXAC_P,
                              @Params(name = "CAP_CXAC_Q") String CAP_CXAC_Q,
                              @Params(name = "DONG_DIEN") String DONG_DIEN,
                              @Params(name = "DIEN_AP") String DIEN_AP,
                              @Params(name = "VH_CONG") String VH_CONG,
                              @Params(name = "MA_NUOC") String MA_NUOC,
                              @Params(name = "MA_HANG") String MA_HANG,
                              @Params(name = "HANGSO_K") String HANGSO_K,
                              @Params(name = "PTHUC_DOXA") String PTHUC_DOXA,
                              @Params(name = "TEN_NUOC") String TEN_NUOC) {
        this.ID_TABLE_LOAI_CONG_TO = ID_TABLE_LOAI_CONG_TO;
        this.MA_CLOAI = MA_CLOAI;
        this.TEN_LOAI_CTO = TEN_LOAI_CTO;
        this.MO_TA = MO_TA;
        this.SO_PHA = SO_PHA;
        this.SO_DAY = SO_DAY;
        this.HS_NHAN = HS_NHAN;
        this.SO_CS = SO_CS;
        this.CAP_CXAC_P = CAP_CXAC_P;
        this.CAP_CXAC_Q = CAP_CXAC_Q;
        this.DONG_DIEN = DONG_DIEN;
        this.DIEN_AP = DIEN_AP;
        this.VH_CONG = VH_CONG;
        this.MA_NUOC = MA_NUOC;
        this.MA_HANG = MA_HANG;
        this.HANGSO_K = HANGSO_K;
        this.PTHUC_DOXA = PTHUC_DOXA;
        this.TEN_NUOC = TEN_NUOC;
    }

    public int getID_TABLE_LOAI_CONG_TO() {
        return ID_TABLE_LOAI_CONG_TO;
    }

    public void setID_TABLE_LOAI_CONG_TO(int ID_TABLE_LOAI_CONG_TO) {
        this.ID_TABLE_LOAI_CONG_TO = ID_TABLE_LOAI_CONG_TO;
    }

    public String getMA_CLOAI() {
        return MA_CLOAI;
    }

    public void setMA_CLOAI(String MA_CLOAI) {
        this.MA_CLOAI = MA_CLOAI;
    }

    public String getTEN_LOAI_CTO() {
        return TEN_LOAI_CTO;
    }

    public void setTEN_LOAI_CTO(String TEN_LOAI_CTO) {
        this.TEN_LOAI_CTO = TEN_LOAI_CTO;
    }

    public String getMO_TA() {
        return MO_TA;
    }

    public void setMO_TA(String MO_TA) {
        this.MO_TA = MO_TA;
    }

    public String getSO_PHA() {
        return SO_PHA;
    }

    public void setSO_PHA(String SO_PHA) {
        this.SO_PHA = SO_PHA;
    }

    public String getSO_DAY() {
        return SO_DAY;
    }

    public void setSO_DAY(String SO_DAY) {
        this.SO_DAY = SO_DAY;
    }

    public int getHS_NHAN() {
        return HS_NHAN;
    }

    public void setHS_NHAN(int HS_NHAN) {
        this.HS_NHAN = HS_NHAN;
    }

    public String getSO_CS() {
        return SO_CS;
    }

    public void setSO_CS(String SO_CS) {
        this.SO_CS = SO_CS;
    }

    public String getCAP_CXAC_P() {
        return CAP_CXAC_P;
    }

    public void setCAP_CXAC_P(String CAP_CXAC_P) {
        this.CAP_CXAC_P = CAP_CXAC_P;
    }

    public String getCAP_CXAC_Q() {
        return CAP_CXAC_Q;
    }

    public void setCAP_CXAC_Q(String CAP_CXAC_Q) {
        this.CAP_CXAC_Q = CAP_CXAC_Q;
    }

    public String getDONG_DIEN() {
        return DONG_DIEN;
    }

    public void setDONG_DIEN(String DONG_DIEN) {
        this.DONG_DIEN = DONG_DIEN;
    }

    public String getDIEN_AP() {
        return DIEN_AP;
    }

    public void setDIEN_AP(String DIEN_AP) {
        this.DIEN_AP = DIEN_AP;
    }

    public String getVH_CONG() {
        return VH_CONG;
    }

    public void setVH_CONG(String VH_CONG) {
        this.VH_CONG = VH_CONG;
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

    public String getHANGSO_K() {
        return HANGSO_K;
    }

    public void setHANGSO_K(String HANGSO_K) {
        this.HANGSO_K = HANGSO_K;
    }

    public String getPTHUC_DOXA() {
        return PTHUC_DOXA;
    }

    public void setPTHUC_DOXA(String PTHUC_DOXA) {
        this.PTHUC_DOXA = PTHUC_DOXA;
    }

    public String getTEN_NUOC() {
        return TEN_NUOC;
    }

    public void setTEN_NUOC(String TEN_NUOC) {
        this.TEN_NUOC = TEN_NUOC;
    }
}
