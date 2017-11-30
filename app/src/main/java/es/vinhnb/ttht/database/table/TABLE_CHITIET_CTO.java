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

@Table(name = "TABLE_CHITIET_CTO")
public class TABLE_CHITIET_CTO implements Cloneable{
    @EnumNameCollumn()
    public enum table {
        ID_TABLE_CHITIET_CTO,
        MA_DVIQLY,
        MA_NVIEN,
        ID_BBAN_TRTH,
        MA_CTO,
        SO_CTO,
        LAN,
        MA_BDONG,
        NGAY_BDONG,
        MA_CLOAI,
        LOAI_CTO,
        VTRI_TREO,
        MO_TA_VTRI_TREO,
        MA_SOCBOOC,
        SO_VIENCBOOC,
        LOAI_HOM,
        MA_SOCHOM,
        SO_VIENCHOM,


        HS_NHAN,
        NGAY_TAO,
        NGUOI_TAO,
        NGAY_SUA,
        NGUOI_SUA,
        MA_CNANG,
        SO_TU,
        SO_TI,
        SO_COT,
        SO_HOM,
        CHI_SO,
        NGAY_KDINH,
        NAM_SX,
        TEM_CQUANG,
        MA_CHIKDINH,
        MA_TEM,
        SOVIEN_CHIKDINH,
        DIEN_AP,

        DONG_DIEN,
        HANGSO_K,
        MA_NUOC,
        TEN_NUOC,
        ID_CHITIET_CTO,
        SO_KIM_NIEM_CHI,
        TTRANG_NPHONG,
        TEN_LOAI_CTO,
        PHUONG_THUC_DO_XA,
        GHI_CHU,
        ID_BBAN_TUTI,
        HS_NHAN_SAULAP_TUTI,
        SO_TU_SAULAP_TUTI,
        SO_TI_SAULAP_TUTI,
        CHI_SO_SAULAP_TUTI,
        DIEN_AP_SAULAP_TUTI,
        DONG_DIEN_SAULAP_TUTI,
        HANGSO_K_SAULAP_TUTI,

        CAP_CX_SAULAP_TUTI,
        TRANG_THAI_DU_LIEU;

        public static String getName() {
            return "TABLE_CHITIET_CTO";
        }
    }


    @PrimaryKey
    @AutoIncrement
    @Collumn(name = "ID_TABLE_CHITIET_CTO", type = TYPE.INTEGER, other = "NOT NULL")
    private int ID_TABLE_CHITIET_CTO;

    @Collumn(name = "MA_DVIQLY", type = TYPE.TEXT, other = "NOT NULL")
    private String MA_DVIQLY;

    @Collumn(name = "MA_NVIEN", type = TYPE.TEXT, other = "NOT NULL")
    private String MA_NVIEN;

    @Collumn(name = "ID_BBAN_TRTH", type = TYPE.INTEGER, other = "NOT NULL")
    private int ID_BBAN_TRTH;

    @Collumn(name = "MA_CTO", type = TYPE.TEXT)
    private String MA_CTO;

    @Collumn(name = "SO_CTO", type = TYPE.TEXT)
    private String SO_CTO;

    @Collumn(name = "LAN", type = TYPE.INTEGER)
    private int LAN;

    @Collumn(name = "MA_BDONG", type = TYPE.TEXT)
    private String MA_BDONG;

    @Collumn(name = "NGAY_BDONG", type = TYPE.TEXT)
    private String NGAY_BDONG;

    @Collumn(name = "MA_CLOAI", type = TYPE.TEXT)
    private String MA_CLOAI;

    @Collumn(name = "LOAI_CTO", type = TYPE.TEXT)
    private String LOAI_CTO;

    @Collumn(name = "VTRI_TREO", type = TYPE.INTEGER)
    private int VTRI_TREO;

    @Collumn(name = "MO_TA_VTRI_TREO", type = TYPE.TEXT)
    private String MO_TA_VTRI_TREO;

    @Collumn(name = "MA_SOCBOOC", type = TYPE.TEXT)
    private String MA_SOCBOOC;

    @Collumn(name = "SO_VIENCBOOC", type = TYPE.INTEGER)
    private int SO_VIENCBOOC;

    @Collumn(name = "LOAI_HOM", type = TYPE.INTEGER)
    private int LOAI_HOM;

    @Collumn(name = "MA_SOCHOM", type = TYPE.TEXT)
    private String MA_SOCHOM;

    @Collumn(name = "SO_VIENCHOM", type = TYPE.INTEGER)
    private int SO_VIENCHOM;

    @Collumn(name = "HS_NHAN", type = TYPE.INTEGER)
    private int HS_NHAN;

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

    @Collumn(name = "SO_TU", type = TYPE.TEXT)
    private String SO_TU;

    @Collumn(name = "SO_TI", type = TYPE.TEXT)
    private String SO_TI;

    @Collumn(name = "SO_COT", type = TYPE.TEXT)
    private String SO_COT;

    @Collumn(name = "SO_HOM", type = TYPE.TEXT)
    private String SO_HOM;

    @Collumn(name = "CHI_SO", type = TYPE.TEXT)
    private String CHI_SO;

    @Collumn(name = "NGAY_KDINH", type = TYPE.TEXT)
    private String NGAY_KDINH;

    @Collumn(name = "NAM_SX", type = TYPE.TEXT)
    private String NAM_SX;

    @Collumn(name = "TEM_CQUANG", type = TYPE.TEXT)
    private String TEM_CQUANG;

    @Collumn(name = "MA_CHIKDINH", type = TYPE.TEXT)
    private String MA_CHIKDINH;

    @Collumn(name = "MA_TEM", type = TYPE.TEXT)
    private String MA_TEM;

    @Collumn(name = "SOVIEN_CHIKDINH", type = TYPE.INTEGER)
    private int SOVIEN_CHIKDINH;

    @Collumn(name = "DIEN_AP", type = TYPE.TEXT)
    private String DIEN_AP;

    @Collumn(name = "DONG_DIEN", type = TYPE.TEXT)
    private String DONG_DIEN;

    @Collumn(name = "HANGSO_K", type = TYPE.TEXT)
    private String HANGSO_K;

    @Collumn(name = "MA_NUOC", type = TYPE.TEXT)
    private String MA_NUOC;

    @Collumn(name = "TEN_NUOC", type = TYPE.TEXT)
    private String TEN_NUOC;

    @Collumn(name = "ID_CHITIET_CTO", type = TYPE.INTEGER)
    private int ID_CHITIET_CTO;

    @Collumn(name = "SO_KIM_NIEM_CHI", type = TYPE.TEXT)
    private String SO_KIM_NIEM_CHI;

    @Collumn(name = "TTRANG_NPHONG", type = TYPE.TEXT)
    private String TTRANG_NPHONG;

    @Collumn(name = "TEN_LOAI_CTO", type = TYPE.TEXT)
    private String TEN_LOAI_CTO;

    @Collumn(name = "PHUONG_THUC_DO_XA", type = TYPE.TEXT)
    private String PHUONG_THUC_DO_XA;

    @Collumn(name = "GHI_CHU", type = TYPE.TEXT)
    private String GHI_CHU;

    @Collumn(name = "ID_BBAN_TUTI", type = TYPE.INTEGER)
    private int ID_BBAN_TUTI;

    @Collumn(name = "HS_NHAN_SAULAP_TUTI", type = TYPE.INTEGER)
    private int HS_NHAN_SAULAP_TUTI;

    @Collumn(name = "SO_TU_SAULAP_TUTI", type = TYPE.TEXT)
    private String SO_TU_SAULAP_TUTI;

    @Collumn(name = "SO_TI_SAULAP_TUTI", type = TYPE.TEXT)
    private String SO_TI_SAULAP_TUTI;

    @Collumn(name = "CHI_SO_SAULAP_TUTI", type = TYPE.TEXT)
    private String CHI_SO_SAULAP_TUTI;

    @Collumn(name = "DIEN_AP_SAULAP_TUTI", type = TYPE.TEXT)
    private String DIEN_AP_SAULAP_TUTI;

    @Collumn(name = "DONG_DIEN_SAULAP_TUTI", type = TYPE.TEXT)
    private String DONG_DIEN_SAULAP_TUTI;

    @Collumn(name = "HANGSO_K_SAULAP_TUTI", type = TYPE.TEXT)
    private String HANGSO_K_SAULAP_TUTI;

    @Collumn(name = "CAP_CX_SAULAP_TUTI", type = TYPE.INTEGER)
    private int CAP_CX_SAULAP_TUTI;

    @Collumn(name = "TRANG_THAI_DU_LIEU", type = TYPE.TEXT)
    private String TRANG_THAI_DU_LIEU;

    public TABLE_CHITIET_CTO() {
    }

    public TABLE_CHITIET_CTO(
            @Params(name = "ID_TABLE_CHITIET_CTO") int ID_TABLE_CHITIET_CTO,
            @Params(name = "MA_DVIQLY") String MA_DVIQLY,
            @Params(name = "MA_NVIEN") String MA_NVIEN,
            @Params(name = "ID_BBAN_TRTH") int ID_BBAN_TRTH,
            @Params(name = "MA_CTO") String MA_CTO,
            @Params(name = "SO_CTO") String SO_CTO,
            @Params(name = "LAN") int LAN,
            @Params(name = "MA_BDONG") String MA_BDONG,
            @Params(name = "NGAY_BDONG") String NGAY_BDONG,
            @Params(name = "MA_CLOAI") String MA_CLOAI,
            @Params(name = "LOAI_CTO") String LOAI_CTO,
            @Params(name = "VTRI_TREO") int VTRI_TREO,
            @Params(name = "MO_TA_VTRI_TREO") String MO_TA_VTRI_TREO,
            @Params(name = "MA_SOCBOOC") String MA_SOCBOOC,
            @Params(name = "SO_VIENCBOOC") int SO_VIENCBOOC,
            @Params(name = "LOAI_HOM") int LOAI_HOM,
            @Params(name = "MA_SOCHOM") String MA_SOCHOM,
            @Params(name = "SO_VIENCHOM") int SO_VIENCHOM,
            @Params(name = "HS_NHAN") int HS_NHAN,
            @Params(name = "NGAY_TAO") String NGAY_TAO,
            @Params(name = "NGUOI_TAO") String NGUOI_TAO,
            @Params(name = "NGAY_SUA") String NGAY_SUA,
            @Params(name = "NGUOI_SUA") String NGUOI_SUA,
            @Params(name = "MA_CNANG") String MA_CNANG,
            @Params(name = "SO_TU") String SO_TU,
            @Params(name = "SO_TI") String SO_TI,
            @Params(name = "SO_COT") String SO_COT,
            @Params(name = "SO_HOM") String SO_HOM,
            @Params(name = "CHI_SO") String CHI_SO,
            @Params(name = "NGAY_KDINH") String NGAY_KDINH,
            @Params(name = "NAM_SX") String NAM_SX,
            @Params(name = "TEM_CQUANG") String TEM_CQUANG,
            @Params(name = "MA_CHIKDINH") String MA_CHIKDINH,
            @Params(name = "MA_TEM") String MA_TEM,
            @Params(name = "SOVIEN_CHIKDINH") int SOVIEN_CHIKDINH,
            @Params(name = "DIEN_AP") String DIEN_AP,
            @Params(name = "DONG_DIEN") String DONG_DIEN,
            @Params(name = "HANGSO_K") String HANGSO_K,
            @Params(name = "MA_NUOC") String MA_NUOC,
            @Params(name = "TEN_NUOC") String TEN_NUOC,
            @Params(name = "ID_CHITIET_CTO") int ID_CHITIET_CTO,
            @Params(name = "SO_KIM_NIEM_CHI") String SO_KIM_NIEM_CHI,
            @Params(name = "TTRANG_NPHONG") String TTRANG_NPHONG,
            @Params(name = "TEN_LOAI_CTO") String TEN_LOAI_CTO,
            @Params(name = "PHUONG_THUC_DO_XA") String PHUONG_THUC_DO_XA,
            @Params(name = "GHI_CHU") String GHI_CHU,
            @Params(name = "ID_BBAN_TUTI") int ID_BBAN_TUTI,
            @Params(name = "HS_NHAN_SAULAP_TUTI") int HS_NHAN_SAULAP_TUTI,
            @Params(name = "SO_TU_SAULAP_TUTI") String SO_TU_SAULAP_TUTI,
            @Params(name = "SO_TI_SAULAP_TUTI") String SO_TI_SAULAP_TUTI,
            @Params(name = "CHI_SO_SAULAP_TUTI") String CHI_SO_SAULAP_TUTI,
            @Params(name = "DIEN_AP_SAULAP_TUTI") String DIEN_AP_SAULAP_TUTI,
            @Params(name = "DONG_DIEN_SAULAP_TUTI") String DONG_DIEN_SAULAP_TUTI,
            @Params(name = "HANGSO_K_SAULAP_TUTI") String HANGSO_K_SAULAP_TUTI,
            @Params(name = "CAP_CX_SAULAP_TUTI") int CAP_CX_SAULAP_TUTI,
            @Params(name = "TRANG_THAI_DU_LIEU") String TRANG_THAI_DU_LIEU) {
        this.ID_TABLE_CHITIET_CTO = ID_TABLE_CHITIET_CTO;
        this.MA_DVIQLY = MA_DVIQLY;
        this.MA_NVIEN = MA_NVIEN;
        this.ID_BBAN_TRTH = ID_BBAN_TRTH;
        this.MA_CTO = MA_CTO;
        this.SO_CTO = SO_CTO;
        this.LAN = LAN;
        this.MA_BDONG = MA_BDONG;
        this.NGAY_BDONG = NGAY_BDONG;
        this.MA_CLOAI = MA_CLOAI;
        this.LOAI_CTO = LOAI_CTO;
        this.VTRI_TREO = VTRI_TREO;
        this.MO_TA_VTRI_TREO = MO_TA_VTRI_TREO;
        this.MA_SOCBOOC = MA_SOCBOOC;
        this.SO_VIENCBOOC = SO_VIENCBOOC;
        this.LOAI_HOM = LOAI_HOM;
        this.MA_SOCHOM = MA_SOCHOM;
        this.SO_VIENCHOM = SO_VIENCHOM;
        this.HS_NHAN = HS_NHAN;
        this.NGAY_TAO = NGAY_TAO;
        this.NGUOI_TAO = NGUOI_TAO;
        this.NGAY_SUA = NGAY_SUA;
        this.NGUOI_SUA = NGUOI_SUA;
        this.MA_CNANG = MA_CNANG;
        this.SO_TU = SO_TU;
        this.SO_TI = SO_TI;
        this.SO_COT = SO_COT;
        this.SO_HOM = SO_HOM;
        this.CHI_SO = CHI_SO;
        this.NGAY_KDINH = NGAY_KDINH;
        this.NAM_SX = NAM_SX;
        this.TEM_CQUANG = TEM_CQUANG;
        this.MA_CHIKDINH = MA_CHIKDINH;
        this.MA_TEM = MA_TEM;
        this.SOVIEN_CHIKDINH = SOVIEN_CHIKDINH;
        this.DIEN_AP = DIEN_AP;
        this.DONG_DIEN = DONG_DIEN;
        this.HANGSO_K = HANGSO_K;
        this.MA_NUOC = MA_NUOC;
        this.TEN_NUOC = TEN_NUOC;
        this.ID_CHITIET_CTO = ID_CHITIET_CTO;
        this.SO_KIM_NIEM_CHI = SO_KIM_NIEM_CHI;
        this.TTRANG_NPHONG = TTRANG_NPHONG;
        this.TEN_LOAI_CTO = TEN_LOAI_CTO;
        this.PHUONG_THUC_DO_XA = PHUONG_THUC_DO_XA;
        this.GHI_CHU = GHI_CHU;
        this.ID_BBAN_TUTI = ID_BBAN_TUTI;
        this.HS_NHAN_SAULAP_TUTI = HS_NHAN_SAULAP_TUTI;
        this.SO_TU_SAULAP_TUTI = SO_TU_SAULAP_TUTI;
        this.SO_TI_SAULAP_TUTI = SO_TI_SAULAP_TUTI;
        this.CHI_SO_SAULAP_TUTI = CHI_SO_SAULAP_TUTI;
        this.DIEN_AP_SAULAP_TUTI = DIEN_AP_SAULAP_TUTI;
        this.DONG_DIEN_SAULAP_TUTI = DONG_DIEN_SAULAP_TUTI;
        this.HANGSO_K_SAULAP_TUTI = HANGSO_K_SAULAP_TUTI;
        this.CAP_CX_SAULAP_TUTI = CAP_CX_SAULAP_TUTI;
        this.TRANG_THAI_DU_LIEU = TRANG_THAI_DU_LIEU;
    }

    public int getID_TABLE_CHITIET_CTO() {
        return ID_TABLE_CHITIET_CTO;
    }

    public void setID_TABLE_CHITIET_CTO(int ID_TABLE_CHITIET_CTO) {
        this.ID_TABLE_CHITIET_CTO = ID_TABLE_CHITIET_CTO;
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

    public int getID_BBAN_TRTH() {
        return ID_BBAN_TRTH;
    }

    public void setID_BBAN_TRTH(int ID_BBAN_TRTH) {
        this.ID_BBAN_TRTH = ID_BBAN_TRTH;
    }

    public String getMA_CTO() {
        return MA_CTO;
    }

    public void setMA_CTO(String MA_CTO) {
        this.MA_CTO = MA_CTO;
    }

    public String getSO_CTO() {
        return SO_CTO;
    }

    public void setSO_CTO(String SO_CTO) {
        this.SO_CTO = SO_CTO;
    }

    public int getLAN() {
        return LAN;
    }

    public void setLAN(int LAN) {
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

    public String getLOAI_CTO() {
        return LOAI_CTO;
    }

    public void setLOAI_CTO(String LOAI_CTO) {
        this.LOAI_CTO = LOAI_CTO;
    }

    public int getVTRI_TREO() {
        return VTRI_TREO;
    }

    public void setVTRI_TREO(int VTRI_TREO) {
        this.VTRI_TREO = VTRI_TREO;
    }

    public String getMO_TA_VTRI_TREO() {
        return MO_TA_VTRI_TREO;
    }

    public void setMO_TA_VTRI_TREO(String MO_TA_VTRI_TREO) {
        this.MO_TA_VTRI_TREO = MO_TA_VTRI_TREO;
    }

    public String getMA_SOCBOOC() {
        return MA_SOCBOOC;
    }

    public void setMA_SOCBOOC(String MA_SOCBOOC) {
        this.MA_SOCBOOC = MA_SOCBOOC;
    }

    public int getSO_VIENCBOOC() {
        return SO_VIENCBOOC;
    }

    public void setSO_VIENCBOOC(int SO_VIENCBOOC) {
        this.SO_VIENCBOOC = SO_VIENCBOOC;
    }

    public int getLOAI_HOM() {
        return LOAI_HOM;
    }

    public void setLOAI_HOM(int LOAI_HOM) {
        this.LOAI_HOM = LOAI_HOM;
    }

    public String getMA_SOCHOM() {
        return MA_SOCHOM;
    }

    public void setMA_SOCHOM(String MA_SOCHOM) {
        this.MA_SOCHOM = MA_SOCHOM;
    }

    public int getSO_VIENCHOM() {
        return SO_VIENCHOM;
    }

    public void setSO_VIENCHOM(int SO_VIENCHOM) {
        this.SO_VIENCHOM = SO_VIENCHOM;
    }

    public int getHS_NHAN() {
        return HS_NHAN;
    }

    public void setHS_NHAN(int HS_NHAN) {
        this.HS_NHAN = HS_NHAN;
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

    public String getSO_TU() {
        return SO_TU;
    }

    public void setSO_TU(String SO_TU) {
        this.SO_TU = SO_TU;
    }

    public String getSO_TI() {
        return SO_TI;
    }

    public void setSO_TI(String SO_TI) {
        this.SO_TI = SO_TI;
    }

    public String getSO_COT() {
        return SO_COT;
    }

    public void setSO_COT(String SO_COT) {
        this.SO_COT = SO_COT;
    }

    public String getSO_HOM() {
        return SO_HOM;
    }

    public void setSO_HOM(String SO_HOM) {
        this.SO_HOM = SO_HOM;
    }

    public String getCHI_SO() {
        return CHI_SO;
    }

    public void setCHI_SO(String CHI_SO) {
        this.CHI_SO = CHI_SO;
    }

    public String getNGAY_KDINH() {
        return NGAY_KDINH;
    }

    public void setNGAY_KDINH(String NGAY_KDINH) {
        this.NGAY_KDINH = NGAY_KDINH;
    }

    public String getNAM_SX() {
        return NAM_SX;
    }

    public void setNAM_SX(String NAM_SX) {
        this.NAM_SX = NAM_SX;
    }

    public String getTEM_CQUANG() {
        return TEM_CQUANG;
    }

    public void setTEM_CQUANG(String TEM_CQUANG) {
        this.TEM_CQUANG = TEM_CQUANG;
    }

    public String getMA_CHIKDINH() {
        return MA_CHIKDINH;
    }

    public void setMA_CHIKDINH(String MA_CHIKDINH) {
        this.MA_CHIKDINH = MA_CHIKDINH;
    }

    public String getMA_TEM() {
        return MA_TEM;
    }

    public void setMA_TEM(String MA_TEM) {
        this.MA_TEM = MA_TEM;
    }

    public int getSOVIEN_CHIKDINH() {
        return SOVIEN_CHIKDINH;
    }

    public void setSOVIEN_CHIKDINH(int SOVIEN_CHIKDINH) {
        this.SOVIEN_CHIKDINH = SOVIEN_CHIKDINH;
    }

    public String getDIEN_AP() {
        return DIEN_AP;
    }

    public void setDIEN_AP(String DIEN_AP) {
        this.DIEN_AP = DIEN_AP;
    }

    public String getDONG_DIEN() {
        return DONG_DIEN;
    }

    public void setDONG_DIEN(String DONG_DIEN) {
        this.DONG_DIEN = DONG_DIEN;
    }

    public String getHANGSO_K() {
        return HANGSO_K;
    }

    public void setHANGSO_K(String HANGSO_K) {
        this.HANGSO_K = HANGSO_K;
    }

    public String getMA_NUOC() {
        return MA_NUOC;
    }

    public void setMA_NUOC(String MA_NUOC) {
        this.MA_NUOC = MA_NUOC;
    }

    public String getTEN_NUOC() {
        return TEN_NUOC;
    }

    public void setTEN_NUOC(String TEN_NUOC) {
        this.TEN_NUOC = TEN_NUOC;
    }

    public int getID_CHITIET_CTO() {
        return ID_CHITIET_CTO;
    }

    public void setID_CHITIET_CTO(int ID_CHITIET_CTO) {
        this.ID_CHITIET_CTO = ID_CHITIET_CTO;
    }

    public String getSO_KIM_NIEM_CHI() {
        return SO_KIM_NIEM_CHI;
    }

    public void setSO_KIM_NIEM_CHI(String SO_KIM_NIEM_CHI) {
        this.SO_KIM_NIEM_CHI = SO_KIM_NIEM_CHI;
    }

    public String getTTRANG_NPHONG() {
        return TTRANG_NPHONG;
    }

    public void setTTRANG_NPHONG(String TTRANG_NPHONG) {
        this.TTRANG_NPHONG = TTRANG_NPHONG;
    }

    public String getTEN_LOAI_CTO() {
        return TEN_LOAI_CTO;
    }

    public void setTEN_LOAI_CTO(String TEN_LOAI_CTO) {
        this.TEN_LOAI_CTO = TEN_LOAI_CTO;
    }

    public String getPHUONG_THUC_DO_XA() {
        return PHUONG_THUC_DO_XA;
    }

    public void setPHUONG_THUC_DO_XA(String PHUONG_THUC_DO_XA) {
        this.PHUONG_THUC_DO_XA = PHUONG_THUC_DO_XA;
    }

    public String getGHI_CHU() {
        return GHI_CHU;
    }

    public void setGHI_CHU(String GHI_CHU) {
        this.GHI_CHU = GHI_CHU;
    }

    public int getID_BBAN_TUTI() {
        return ID_BBAN_TUTI;
    }

    public void setID_BBAN_TUTI(int ID_BBAN_TUTI) {
        this.ID_BBAN_TUTI = ID_BBAN_TUTI;
    }

    public int getHS_NHAN_SAULAP_TUTI() {
        return HS_NHAN_SAULAP_TUTI;
    }

    public void setHS_NHAN_SAULAP_TUTI(int HS_NHAN_SAULAP_TUTI) {
        this.HS_NHAN_SAULAP_TUTI = HS_NHAN_SAULAP_TUTI;
    }

    public String getSO_TU_SAULAP_TUTI() {
        return SO_TU_SAULAP_TUTI;
    }

    public void setSO_TU_SAULAP_TUTI(String SO_TU_SAULAP_TUTI) {
        this.SO_TU_SAULAP_TUTI = SO_TU_SAULAP_TUTI;
    }

    public String getSO_TI_SAULAP_TUTI() {
        return SO_TI_SAULAP_TUTI;
    }

    public void setSO_TI_SAULAP_TUTI(String SO_TI_SAULAP_TUTI) {
        this.SO_TI_SAULAP_TUTI = SO_TI_SAULAP_TUTI;
    }

    public String getCHI_SO_SAULAP_TUTI() {
        return CHI_SO_SAULAP_TUTI;
    }

    public void setCHI_SO_SAULAP_TUTI(String CHI_SO_SAULAP_TUTI) {
        this.CHI_SO_SAULAP_TUTI = CHI_SO_SAULAP_TUTI;
    }

    public String getDIEN_AP_SAULAP_TUTI() {
        return DIEN_AP_SAULAP_TUTI;
    }

    public void setDIEN_AP_SAULAP_TUTI(String DIEN_AP_SAULAP_TUTI) {
        this.DIEN_AP_SAULAP_TUTI = DIEN_AP_SAULAP_TUTI;
    }

    public String getDONG_DIEN_SAULAP_TUTI() {
        return DONG_DIEN_SAULAP_TUTI;
    }

    public void setDONG_DIEN_SAULAP_TUTI(String DONG_DIEN_SAULAP_TUTI) {
        this.DONG_DIEN_SAULAP_TUTI = DONG_DIEN_SAULAP_TUTI;
    }

    public String getHANGSO_K_SAULAP_TUTI() {
        return HANGSO_K_SAULAP_TUTI;
    }

    public void setHANGSO_K_SAULAP_TUTI(String HANGSO_K_SAULAP_TUTI) {
        this.HANGSO_K_SAULAP_TUTI = HANGSO_K_SAULAP_TUTI;
    }

    public int getCAP_CX_SAULAP_TUTI() {
        return CAP_CX_SAULAP_TUTI;
    }

    public void setCAP_CX_SAULAP_TUTI(int CAP_CX_SAULAP_TUTI) {
        this.CAP_CX_SAULAP_TUTI = CAP_CX_SAULAP_TUTI;
    }

    public String getTRANG_THAI_DU_LIEU() {
        return TRANG_THAI_DU_LIEU;
    }

    public void setTRANG_THAI_DU_LIEU(String TRANG_THAI_DU_LIEU) {
        this.TRANG_THAI_DU_LIEU = TRANG_THAI_DU_LIEU;
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
