package com.es.tungnv.entity;

/**
 * Created by VinhNB on 10/26/2016.
 */

public class TthtDoiSoatEntity {
    private int stt;
    private int ID_BBAN_TRTH;
    private int ID_BBAN_CONGTO;
    private String MA_GCS_CTO;
    private String TEN_KHANG;
    private String DCHI_HDON;
    private int ID_CHITIET_CTO_TREO;
    private int ID_CHITIET_CTO_THAO;

    private String MA_CTO_TREO;
    private String MA_CTO_THAO;
    private String SO_CTO_TREO;
    private String SO_CTO_THAO;
    private String CHI_SO_TREO;
    private String CHI_SO_THAO;
    private String ANH_PATH_TREO;
    private String ANH_PATH_THAO;


    public TthtDoiSoatEntity() {
    }

    public TthtDoiSoatEntity(TthtDoiSoatEntity tthtDoiSoatEntity) {
        this.stt = stt;
        this.ID_BBAN_TRTH = tthtDoiSoatEntity.ID_BBAN_TRTH;
        this.MA_GCS_CTO = tthtDoiSoatEntity.MA_GCS_CTO;
        this.TEN_KHANG = tthtDoiSoatEntity.TEN_KHANG;
        this.DCHI_HDON = tthtDoiSoatEntity.DCHI_HDON;
        this.ID_CHITIET_CTO_TREO = tthtDoiSoatEntity.ID_CHITIET_CTO_TREO;
        this.ID_CHITIET_CTO_THAO = tthtDoiSoatEntity.ID_CHITIET_CTO_THAO;
        this.MA_CTO_TREO = tthtDoiSoatEntity.MA_CTO_TREO;
        this.MA_CTO_THAO = tthtDoiSoatEntity.MA_CTO_THAO;
        this.SO_CTO_TREO = tthtDoiSoatEntity.SO_CTO_TREO;
        this.SO_CTO_THAO = tthtDoiSoatEntity.SO_CTO_THAO;
        this.CHI_SO_TREO = tthtDoiSoatEntity.CHI_SO_TREO;
        this.CHI_SO_THAO = tthtDoiSoatEntity.CHI_SO_THAO;
        this.ANH_PATH_TREO = tthtDoiSoatEntity.ANH_PATH_TREO;
        this.ANH_PATH_THAO = tthtDoiSoatEntity.ANH_PATH_THAO;
        this.ID_BBAN_CONGTO = tthtDoiSoatEntity.ID_BBAN_CONGTO;

    }

    public TthtDoiSoatEntity(int stt, int ID_BBAN_TRTH, int ID_BBAN_CONGTO, String MA_GCS_CTO, String TEN_KHANG, String DCHI_HDON, int ID_CHITIET_CTO_TREO, int ID_CHITIET_CTO_THAO, String MA_CTO_TREO, String MA_CTO_THAO, String SO_CTO_TREO, String SO_CTO_THAO, String CHI_SO_TREO, String CHI_SO_THAO, String ANH_PATH_TREO, String ANH_PATH_THAO) {
        this.stt = stt;
        this.ID_BBAN_TRTH = ID_BBAN_TRTH;
        this.ID_BBAN_CONGTO = ID_BBAN_CONGTO;
        this.MA_GCS_CTO = MA_GCS_CTO;
        this.TEN_KHANG = TEN_KHANG;
        this.DCHI_HDON = DCHI_HDON;
        this.ID_CHITIET_CTO_TREO = ID_CHITIET_CTO_TREO;
        this.ID_CHITIET_CTO_THAO = ID_CHITIET_CTO_THAO;
        this.MA_CTO_TREO = MA_CTO_TREO;
        this.MA_CTO_THAO = MA_CTO_THAO;
        this.SO_CTO_TREO = SO_CTO_TREO;
        this.SO_CTO_THAO = SO_CTO_THAO;
        this.CHI_SO_TREO = CHI_SO_TREO;
        this.CHI_SO_THAO = CHI_SO_THAO;
        this.ANH_PATH_TREO = ANH_PATH_TREO;
        this.ANH_PATH_THAO = ANH_PATH_THAO;
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public int getID_BBAN_TRTH() {
        return ID_BBAN_TRTH;
    }

    public void setID_BBAN_TRTH(int ID_BBAN_TRTH) {
        this.ID_BBAN_TRTH = ID_BBAN_TRTH;
    }

    public String getMA_GCS_CTO() {
        return MA_GCS_CTO;
    }

    public void setMA_GCS_CTO(String MA_GCS_CTO) {
        this.MA_GCS_CTO = MA_GCS_CTO;
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

    public int getID_CHITIET_CTO_TREO() {
        return ID_CHITIET_CTO_TREO;
    }

    public void setID_CHITIET_CTO_TREO(int ID_CHITIET_CTO_TREO) {
        this.ID_CHITIET_CTO_TREO = ID_CHITIET_CTO_TREO;
    }

    public int getID_CHITIET_CTO_THAO() {
        return ID_CHITIET_CTO_THAO;
    }

    public void setID_CHITIET_CTO_THAO(int ID_CHITIET_CTO_THAO) {
        this.ID_CHITIET_CTO_THAO = ID_CHITIET_CTO_THAO;
    }

    public String getMA_CTO_TREO() {
        return MA_CTO_TREO;
    }

    public void setMA_CTO_TREO(String MA_CTO_TREO) {
        this.MA_CTO_TREO = MA_CTO_TREO;
    }

    public String getMA_CTO_THAO() {
        return MA_CTO_THAO;
    }

    public void setMA_CTO_THAO(String MA_CTO_THAO) {
        this.MA_CTO_THAO = MA_CTO_THAO;
    }

    public String getSO_CTO_TREO() {
        return SO_CTO_TREO;
    }

    public void setSO_CTO_TREO(String SO_CTO_TREO) {
        this.SO_CTO_TREO = SO_CTO_TREO;
    }

    public String getSO_CTO_THAO() {
        return SO_CTO_THAO;
    }

    public void setSO_CTO_THAO(String SO_CTO_THAO) {
        this.SO_CTO_THAO = SO_CTO_THAO;
    }

    public String getCHI_SO_TREO() {
        return CHI_SO_TREO;
    }

    public void setCHI_SO_TREO(String CHI_SO_TREO) {
        this.CHI_SO_TREO = CHI_SO_TREO;
    }

    public String getCHI_SO_THAO() {
        return CHI_SO_THAO;
    }

    public void setCHI_SO_THAO(String CHI_SO_THAO) {
        this.CHI_SO_THAO = CHI_SO_THAO;
    }

    public String getANH_PATH_TREO() {
        return ANH_PATH_TREO;
    }

    public void setANH_PATH_TREO(String ANH_PATH_TREO) {
        this.ANH_PATH_TREO = ANH_PATH_TREO;
    }

    public String getANH_PATH_THAO() {
        return ANH_PATH_THAO;
    }

    public void setANH_PATH_THAO(String ANH_PATH_THAO) {
        this.ANH_PATH_THAO = ANH_PATH_THAO;
    }

    public int getID_BBAN_CONGTO() {
        return ID_BBAN_CONGTO;
    }

    public void setID_BBAN_CONGTO(int ID_BBAN_CONGTO) {
        this.ID_BBAN_CONGTO = ID_BBAN_CONGTO;
    }
}
