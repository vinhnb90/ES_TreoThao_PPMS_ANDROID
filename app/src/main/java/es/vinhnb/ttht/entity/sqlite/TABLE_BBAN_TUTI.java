package es.vinhnb.ttht.entity.sqlite;


import esolutions.com.esdatabaselib.baseSqlite.anonation.AutoIncrement;
import esolutions.com.esdatabaselib.baseSqlite.anonation.Collumn;
import esolutions.com.esdatabaselib.baseSqlite.anonation.Params;
import esolutions.com.esdatabaselib.baseSqlite.anonation.PrimaryKey;
import esolutions.com.esdatabaselib.baseSqlite.anonation.TYPE;
import esolutions.com.esdatabaselib.baseSqlite.anonation.Table;

/**
 * Created by VinhNB on 10/10/2017.
 */

@Table(name = "TABLE_BBAN_TUTI")
public class TABLE_BBAN_TUTI {
    @PrimaryKey
    @AutoIncrement
    @Collumn(name = "ID_TABLE_BBAN_TUTI", type = TYPE.INTEGER, other = "NOT NULL")
    private int ID_TABLE_BBAN_TUTI;

    @Collumn(name = "MA_DVIQLY", type = TYPE.TEXT)
    private String MA_DVIQLY;

    @Collumn(name = "ID_BBAN_TUTI",  type = TYPE.INTEGER)
    private int ID_BBAN_TUTI;

    @Collumn(name = "MA_DDO", type = TYPE.TEXT)
    private String MA_DDO;

    @Collumn(name = "SO_BBAN", type = TYPE.TEXT)
    private String SO_BBAN;

    @Collumn(name = "NGAY_TRTH", type = TYPE.TEXT)
    private String NGAY_TRTH;

    @Collumn(name = "MA_NVIEN", type = TYPE.TEXT)
    private String MA_NVIEN;

    @Collumn(name = "TRANG_THAI", type = TYPE.INTEGER)
    private int TRANG_THAI;

    @Collumn(name = "TEN_KHANG", type = TYPE.TEXT)
    private String TEN_KHANG;

    @Collumn(name = "DCHI_HDON", type = TYPE.TEXT)
    private String DCHI_HDON;

    @Collumn(name = "DTHOAI", type = TYPE.TEXT)
    private String DTHOAI;

    @Collumn(name = "MA_GCS_CTO", type = TYPE.TEXT)
    private String MA_GCS_CTO;

    @Collumn(name = "MA_TRAM", type = TYPE.TEXT)
    private String MA_TRAM;

    @Collumn(name = "LY_DO_TREO_THAO", type = TYPE.TEXT)
    private String LY_DO_TREO_THAO;

    @Collumn(name = "MA_KHANG", type = TYPE.TEXT)
    private String MA_KHANG;

    @Collumn(name = "ID_BBAN_WEB_TUTI", type = TYPE.INTEGER)
    private int ID_BBAN_WEB_TUTI;

    @Collumn(name = "NVIEN_KCHI", type = TYPE.TEXT)
    private String NVIEN_KCHI;

    public TABLE_BBAN_TUTI() {
    }


    public TABLE_BBAN_TUTI(@Params(name = "ID_TABLE_BBAN_TUTI") int ID_TABLE_BBAN_TUTI,
                              @Params(name = "MA_DVIQLY") String MA_DVIQLY,
                              @Params(name = "ID_BBAN_TUTI") int ID_BBAN_TUTI,
                              @Params(name = "MA_DDO") String MA_DDO,
                              @Params(name = "SO_BBAN") String SO_BBAN,
                              @Params(name = "NGAY_TRTH") String NGAY_TRTH,
                              @Params(name = "MA_NVIEN") String MA_NVIEN,
                              @Params(name = "TRANG_THAI") int TRANG_THAI,
                              @Params(name = "TEN_KHANG") String TEN_KHANG,
                              @Params(name = "DCHI_HDON") String DCHI_HDON,
                              @Params(name = "DTHOAI") String DTHOAI,
                              @Params(name = "MA_GCS_CTO") String MA_GCS_CTO,
                              @Params(name = "MA_TRAM") String MA_TRAM,
                              @Params(name = "LY_DO_TREO_THAO") String LY_DO_TREO_THAO,
                              @Params(name = "MA_KHANG") String MA_KHANG,
                              @Params(name = "ID_BBAN_WEB_TUTI") int ID_BBAN_WEB_TUTI,
                              @Params(name = "NVIEN_KCHI") String NVIEN_KCHI) {
        this.ID_TABLE_BBAN_TUTI = ID_TABLE_BBAN_TUTI;
        this.MA_DVIQLY = MA_DVIQLY;
        this.ID_BBAN_TUTI = ID_BBAN_TUTI;
        this.MA_DDO = MA_DDO;
        this.SO_BBAN = SO_BBAN;
        this.NGAY_TRTH = NGAY_TRTH;
        this.MA_NVIEN = MA_NVIEN;
        this.TRANG_THAI = TRANG_THAI;
        this.TEN_KHANG = TEN_KHANG;
        this.DCHI_HDON = DCHI_HDON;
        this.DTHOAI = DTHOAI;
        this.MA_GCS_CTO = MA_GCS_CTO;
        this.MA_TRAM = MA_TRAM;
        this.LY_DO_TREO_THAO = LY_DO_TREO_THAO;
        this.MA_KHANG = MA_KHANG;
        this.ID_BBAN_WEB_TUTI = ID_BBAN_WEB_TUTI;
        this.NVIEN_KCHI = NVIEN_KCHI;
    }


    public int getID_TABLE_BBAN_TUTI() {
        return ID_TABLE_BBAN_TUTI;
    }

    public void setID_TABLE_BBAN_TUTI(int ID_TABLE_BBAN_TUTI) {
        this.ID_TABLE_BBAN_TUTI = ID_TABLE_BBAN_TUTI;
    }

    public String getMA_DVIQLY() {
        return MA_DVIQLY;
    }

    public void setMA_DVIQLY(String MA_DVIQLY) {
        this.MA_DVIQLY = MA_DVIQLY;
    }

    public int getID_BBAN_TUTI() {
        return ID_BBAN_TUTI;
    }

    public void setID_BBAN_TUTI(int ID_BBAN_TUTI) {
        this.ID_BBAN_TUTI = ID_BBAN_TUTI;
    }

    public String getMA_DDO() {
        return MA_DDO;
    }

    public void setMA_DDO(String MA_DDO) {
        this.MA_DDO = MA_DDO;
    }

    public String getSO_BBAN() {
        return SO_BBAN;
    }

    public void setSO_BBAN(String SO_BBAN) {
        this.SO_BBAN = SO_BBAN;
    }

    public String getNGAY_TRTH() {
        return NGAY_TRTH;
    }

    public void setNGAY_TRTH(String NGAY_TRTH) {
        this.NGAY_TRTH = NGAY_TRTH;
    }

    public String getMA_NVIEN() {
        return MA_NVIEN;
    }

    public void setMA_NVIEN(String MA_NVIEN) {
        this.MA_NVIEN = MA_NVIEN;
    }

    public int getTRANG_THAI() {
        return TRANG_THAI;
    }

    public void setTRANG_THAI(int TRANG_THAI) {
        this.TRANG_THAI = TRANG_THAI;
    }

    public String getTEN_KHANG() {
        return TEN_KHANG;
    }

    public void setTEN_KHANG(String TEN_KHANG) {
        this.TEN_KHANG = TEN_KHANG;
    }

    public String getDCHI_HDON() {
        return DCHI_HDON;
    }

    public void setDCHI_HDON(String DCHI_HDON) {
        this.DCHI_HDON = DCHI_HDON;
    }

    public String getDTHOAI() {
        return DTHOAI;
    }

    public void setDTHOAI(String DTHOAI) {
        this.DTHOAI = DTHOAI;
    }

    public String getMA_GCS_CTO() {
        return MA_GCS_CTO;
    }

    public void setMA_GCS_CTO(String MA_GCS_CTO) {
        this.MA_GCS_CTO = MA_GCS_CTO;
    }

    public String getMA_TRAM() {
        return MA_TRAM;
    }

    public void setMA_TRAM(String MA_TRAM) {
        this.MA_TRAM = MA_TRAM;
    }

    public String getLY_DO_TREO_THAO() {
        return LY_DO_TREO_THAO;
    }

    public void setLY_DO_TREO_THAO(String LY_DO_TREO_THAO) {
        this.LY_DO_TREO_THAO = LY_DO_TREO_THAO;
    }

    public String getMA_KHANG() {
        return MA_KHANG;
    }

    public void setMA_KHANG(String MA_KHANG) {
        this.MA_KHANG = MA_KHANG;
    }

    public int getID_BBAN_WEB_TUTI() {
        return ID_BBAN_WEB_TUTI;
    }

    public void setID_BBAN_WEB_TUTI(int ID_BBAN_WEB_TUTI) {
        this.ID_BBAN_WEB_TUTI = ID_BBAN_WEB_TUTI;
    }

    public String getNVIEN_KCHI() {
        return NVIEN_KCHI;
    }

    public void setNVIEN_KCHI(String NVIEN_KCHI) {
        this.NVIEN_KCHI = NVIEN_KCHI;
    }
}
