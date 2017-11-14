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

@Table(name = "TABLE_BBAN_CTO")
public class TABLE_BBAN_CTO {
    @PrimaryKey
    @AutoIncrement
    @Collumn(name = "ID_TABLE_BBAN_CTO", type = TYPE.INTEGER, other = "NOT NULL")
    private int ID_TABLE_BBAN_CTO;

    @Collumn(name = "MA_DVIQLY", type = TYPE.TEXT, other = "NOT NULL")
    private String MA_DVIQLY;

    @Collumn(name = "ID_BBAN_TRTH", type = TYPE.INTEGER, other = "NOT NULL")
    private int ID_BBAN_TRTH;

    @Collumn(name = "MA_DDO", type = TYPE.TEXT)
    private String MA_DDO;

    @Collumn(name = "SO_BBAN", type = TYPE.TEXT)
    private String SO_BBAN;

    @Collumn(name = "NGAY_TRTH", type = TYPE.TEXT)
    private String NGAY_TRTH;

    @Collumn(name = "MA_NVIEN", type = TYPE.TEXT)
    private String MA_NVIEN;

    @Collumn(name = "MA_LDO", type = TYPE.TEXT)
    private String MA_LDO;

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

    @Collumn(name = "MA_YCAU_KNAI", type = TYPE.TEXT)
    private String MA_YCAU_KNAI;

    @Collumn(name = "TRANG_THAI", type = TYPE.INTEGER)
    private int TRANG_THAI;

    @Collumn(name = "GHI_CHU", type = TYPE.TEXT)
    private String GHI_CHU;

    @Collumn(name = "ID_BBAN_CONGTO", type = TYPE.INTEGER)
    private int ID_BBAN_CONGTO;

    @Collumn(name = "LOAI_BBAN", type = TYPE.TEXT)
    private String LOAI_BBAN;

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

    @Collumn(name = "MA_HDONG", type = TYPE.TEXT)
    private String MA_HDONG;

    @Collumn(name = "MA_KHANG", type = TYPE.TEXT)
    private String MA_KHANG;

    @Collumn(name = "LY_DO_TREO_THAO", type = TYPE.TEXT)
    private String LY_DO_TREO_THAO;


    public TABLE_BBAN_CTO() {
    }

    public TABLE_BBAN_CTO(@Params(name = "ID_TABLE_BBAN_CTO") int ID_TABLE_BBAN_CTO,
                          @Params(name = "MA_DVIQLY") String MA_DVIQLY,
                          @Params(name = "ID_BBAN_TRTH") int ID_BBAN_TRTH,
                          @Params(name = "MA_DDO") String MA_DDO,
                          @Params(name = "SO_BBAN") String SO_BBAN,
                          @Params(name = "NGAY_TRTH") String NGAY_TRTH,
                          @Params(name = "MA_NVIEN") String MA_NVIEN,
                          @Params(name = "MA_LDO") String MA_LDO,
                          @Params(name = "NGAY_TAO") String NGAY_TAO,
                          @Params(name = "NGUOI_TAO") String NGUOI_TAO,
                          @Params(name = "NGAY_SUA") String NGAY_SUA,
                          @Params(name = "NGUOI_SUA") String NGUOI_SUA,
                          @Params(name = "MA_CNANG") String MA_CNANG,
                          @Params(name = "MA_YCAU_KNAI") String MA_YCAU_KNAI,
                          @Params(name = "TRANG_THAI") int TRANG_THAI,
                          @Params(name = "GHI_CHU") String GHI_CHU,
                          @Params(name = "ID_BBAN_CONGTO") int ID_BBAN_CONGTO,
                          @Params(name = "LOAI_BBAN") String LOAI_BBAN,
                          @Params(name = "TEN_KHANG") String TEN_KHANG,
                          @Params(name = "DCHI_HDON") String DCHI_HDON,
                          @Params(name = "DTHOAI") String DTHOAI,
                          @Params(name = "MA_GCS_CTO") String MA_GCS_CTO,
                          @Params(name = "MA_TRAM") String MA_TRAM,
                          @Params(name = "MA_HDONG") String MA_HDONG,
                          @Params(name = "MA_KHANG") String MA_KHANG,
                          @Params(name = "LY_DO_TREO_THAO") String LY_DO_TREO_THAO) {
        this.ID_TABLE_BBAN_CTO = ID_TABLE_BBAN_CTO;
        this.MA_DVIQLY = MA_DVIQLY;
        this.ID_BBAN_TRTH = ID_BBAN_TRTH;
        this.MA_DDO = MA_DDO;
        this.SO_BBAN = SO_BBAN;
        this.NGAY_TRTH = NGAY_TRTH;
        this.MA_NVIEN = MA_NVIEN;
        this.MA_LDO = MA_LDO;
        this.NGAY_TAO = NGAY_TAO;
        this.NGUOI_TAO = NGUOI_TAO;
        this.NGAY_SUA = NGAY_SUA;
        this.NGUOI_SUA = NGUOI_SUA;
        this.MA_CNANG = MA_CNANG;
        this.MA_YCAU_KNAI = MA_YCAU_KNAI;
        this.TRANG_THAI = TRANG_THAI;
        this.GHI_CHU = GHI_CHU;
        this.ID_BBAN_CONGTO = ID_BBAN_CONGTO;
        this.LOAI_BBAN = LOAI_BBAN;
        this.TEN_KHANG = TEN_KHANG;
        this.DCHI_HDON = DCHI_HDON;
        this.DTHOAI = DTHOAI;
        this.MA_GCS_CTO = MA_GCS_CTO;
        this.MA_TRAM = MA_TRAM;
        this.MA_HDONG = MA_HDONG;
        this.MA_KHANG = MA_KHANG;
        this.LY_DO_TREO_THAO = LY_DO_TREO_THAO;
    }


    public int getID_TABLE_BBAN_CTO() {
        return ID_TABLE_BBAN_CTO;
    }

    public void setID_TABLE_BBAN_CTO(int ID_TABLE_BBAN_CTO) {
        this.ID_TABLE_BBAN_CTO = ID_TABLE_BBAN_CTO;
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

    public String getMA_LDO() {
        return MA_LDO;
    }

    public void setMA_LDO(String MA_LDO) {
        this.MA_LDO = MA_LDO;
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

    public String getMA_YCAU_KNAI() {
        return MA_YCAU_KNAI;
    }

    public void setMA_YCAU_KNAI(String MA_YCAU_KNAI) {
        this.MA_YCAU_KNAI = MA_YCAU_KNAI;
    }

    public int getTRANG_THAI() {
        return TRANG_THAI;
    }

    public void setTRANG_THAI(int TRANG_THAI) {
        this.TRANG_THAI = TRANG_THAI;
    }

    public String getGHI_CHU() {
        return GHI_CHU;
    }

    public void setGHI_CHU(String GHI_CHU) {
        this.GHI_CHU = GHI_CHU;
    }

    public int getID_BBAN_CONGTO() {
        return ID_BBAN_CONGTO;
    }

    public void setID_BBAN_CONGTO(int ID_BBAN_CONGTO) {
        this.ID_BBAN_CONGTO = ID_BBAN_CONGTO;
    }

    public String getLOAI_BBAN() {
        return LOAI_BBAN;
    }

    public void setLOAI_BBAN(String LOAI_BBAN) {
        this.LOAI_BBAN = LOAI_BBAN;
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

    public String getMA_HDONG() {
        return MA_HDONG;
    }

    public void setMA_HDONG(String MA_HDONG) {
        this.MA_HDONG = MA_HDONG;
    }

    public String getMA_KHANG() {
        return MA_KHANG;
    }

    public void setMA_KHANG(String MA_KHANG) {
        this.MA_KHANG = MA_KHANG;
    }

    public String getLY_DO_TREO_THAO() {
        return LY_DO_TREO_THAO;
    }

    public void setLY_DO_TREO_THAO(String LY_DO_TREO_THAO) {
        this.LY_DO_TREO_THAO = LY_DO_TREO_THAO;
    }
}
