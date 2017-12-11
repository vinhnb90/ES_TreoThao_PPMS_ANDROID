package es.vinhnb.ttht.entity.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by VinhNB on 11/22/2017.
 */

public class MTBModelNew {
    public MTBModelNew() {
    }

    //CHUNG
    //TODO bien ban
    @SerializedName("ID_BBAN_CONGTO")
    public int ID_BBAN_CONGTO;
    @SerializedName("MA_DVIQLY")
    public String MA_DVIQLY;
    @SerializedName("SO_BBAN")
    public String SO_BBAN;
    @SerializedName("TRANG_THAI")
    public int TRANG_THAI;


    //TODO cong to treo
    @SerializedName("LAN_CTO_TREO")
    public int LAN_CTO_TREO;
    @SerializedName("VTRI_TREO_THAO_CTO_TREO")
    public int VTRI_TREO_THAO_CTO_TREO;
    @SerializedName("SOVIEN_CBOOC_CTO_TREO")
    public int SOVIEN_CBOOC_CTO_TREO;
    @SerializedName("LOAI_HOM_CTO_TREO")
    public int LOAI_HOM_CTO_TREO;
    @SerializedName("SOVIEN_CHOM_CTO_TREO")
    public int SOVIEN_CHOM_CTO_TREO;
    @SerializedName("HS_NHAN_CTO_TREO")
    public int HS_NHAN_CTO_TREO;
    @SerializedName("SO_TU_CTO_TREO")
    public String SO_TU_CTO_TREO;
    @SerializedName("SO_TI_CTO_TREO")
    public String SO_TI_CTO_TREO;


    @SerializedName("CHI_SO_CTO_TREO")
    public String CHI_SO_CTO_TREO;
    @SerializedName("CCX_CTO_TREO")
    public int CCX_CTO_TREO;
    @SerializedName("TEM_CQUANG_CTO_TREO")
    public String TEM_CQUANG_CTO_TREO;
    @SerializedName("SOVIEN_CHIKDINH_CTO_TREO")
    public int SOVIEN_CHIKDINH_CTO_TREO;
    @SerializedName("DIEN_AP_CTO_TREO")
    public String DIEN_AP_CTO_TREO;
    @SerializedName("DONG_DIEN_CTO_TREO")
    public String DONG_DIEN_CTO_TREO;
    @SerializedName("HANGSO_K_CTO_TREO")
    public String HANGSO_K_CTO_TREO;
    @SerializedName("SO_KIM_NIEM_CHI_CTO_TREO")
    public String SO_KIM_NIEM_CHI_CTO_TREO;
    @SerializedName("TTRANG_NPHONG_CTO_TREO")
    public String TTRANG_NPHONG_CTO_TREO;
    @SerializedName("TEN_LOAI_CTO_CTO_TREO")
    public String TEN_LOAI_CTO_CTO_TREO;
    @SerializedName("PHUONG_THUC_DO_XA_CTO_TREO")
    public String PHUONG_THUC_DO_XA_CTO_TREO;
    @SerializedName("GHI_CHU_CTO_TREO")
    public String GHI_CHU_CTO_TREO;

    //TODO cong to thao
    @SerializedName("LAN_CTO_THAO")
    public int LAN_CTO_THAO;
    @SerializedName("VTRI_TREO_THAO_CTO_THAO")
    public int VTRI_TREO_THAO_CTO_THAO;
    @SerializedName("SOVIEN_CBOOC_CTO_THAO")
    public int SOVIEN_CBOOC_CTO_THAO;
    @SerializedName("LOAI_HOM_CTO_THAO")
    public int LOAI_HOM_CTO_THAO;
    @SerializedName("SOVIEN_CHOM_CTO_THAO")
    public int SOVIEN_CHOM_CTO_THAO;
    @SerializedName("HS_NHAN_CTO_THAO")
    public int HS_NHAN_CTO_THAO;
    @SerializedName("SO_TU_CTO_THAO")
    public String SO_TU_CTO_THAO;
    @SerializedName("SO_TI_CTO_THAO")
    public String SO_TI_CTO_THAO;
    @SerializedName("CHI_SO_CTO_THAO")
    public String CHI_SO_CTO_THAO;
    @SerializedName("CCX_CTO_THAO")
    public int CCX_CTO_THAO;
    @SerializedName("TEM_CQUANG_CTO_THAO")
    public String TEM_CQUANG_CTO_THAO;
    @SerializedName("SOVIEN_CHIKDINH_CTO_THAO")
    public int SOVIEN_CHIKDINH_CTO_THAO;
    @SerializedName("DIEN_AP_CTO_THAO")
    public String DIEN_AP_CTO_THAO;
    @SerializedName("DONG_DIEN_CTO_THAO")
    public String DONG_DIEN_CTO_THAO;


    @SerializedName("HANGSO_K_CTO_THAO")
    public String HANGSO_K_CTO_THAO;
    @SerializedName("SO_KIM_NIEM_CHI_CTO_THAO")
    public String SO_KIM_NIEM_CHI_CTO_THAO;
    @SerializedName("TTRANG_NPHONG_CTO_THAO")
    public String TTRANG_NPHONG_CTO_THAO;
    @SerializedName("TEN_LOAI_CTO_CTO_THAO")
    public String TEN_LOAI_CTO_CTO_THAO;
    @SerializedName("PHUONG_THUC_DO_XA_CTO_THAO")
    public String PHUONG_THUC_DO_XA_CTO_THAO;
    @SerializedName("GHI_CHU_CTO_THAO")
    public String GHI_CHU_CTO_THAO;


    //TODO update bang DU_LIEU_HIEN_TRUONG
    ////TODO anh chu ki KH
    // @SerializedName("TEN_ANH_CHU_KI")
    //public String TEN_ANH_CHU_KI;
    // @SerializedName("ANH_CHU_KI")
    //public String ANH_CHU_KI;

    //TODO anh Cong To Treo
    @SerializedName("ID_BBAN_TUTI_CTO_TREO")
    public int ID_BBAN_TUTI_CTO_TREO;
    @SerializedName("ID_CHITIET_TUTI_TU_CTO_TREO")
    public int ID_CHITIET_TUTI_TU_CTO_TREO;

    //anh cong to
    @SerializedName("ID_CHITIET_TUTI_TI_CTO_TREO")
    public int ID_CHITIET_TUTI_TI_CTO_TREO;

    @SerializedName("ID_CHITIET_CTO_TREO")
    public int ID_CHITIET_CTO_TREO;

    //anh cong to
    @SerializedName("TEN_ANH_CTO_TREO")
    public String TEN_ANH_CTO_TREO;
    @SerializedName("TEN_ANH_NIEMPHONG_CTO_TREO")
    public String TEN_ANH_NIEMPHONG_CTO_TREO;
    @SerializedName("ANH_CTO_TREO")

    public String ANH_CTO_TREO;
    @SerializedName("ANH_NIEMPHONG_CTO_TREO")
    public String ANH_NIEMPHONG_CTO_TREO;
    @SerializedName("CREATE_DAY_ANH_CTO_TREO")
    public String CREATE_DAY_ANH_CTO_TREO;
    @SerializedName("CREATE_DAY_ANH_NIEMPHONG_CTO_TREO")
    public String CREATE_DAY_ANH_NIEMPHONG_CTO_TREO;


    //anh TU
    @SerializedName("TEN_ANH_TU_CTO_TREO")
    public String TEN_ANH_TU_CTO_TREO;
    @SerializedName("ANH_TU_CTO_TREO")
    public String ANH_TU_CTO_TREO;
    @SerializedName("CREATE_DAY_ANH_TU_CTO_TREO")
    public String CREATE_DAY_ANH_TU_CTO_TREO;

    //anh TU mach nhi thu
    @SerializedName("TEN_ANH_TU_ANH_MACH_NHI_THU_CTO_TREO")
    public String TEN_ANH_TU_ANH_MACH_NHI_THU_CTO_TREO;
    @SerializedName("ANH_TU_ANH_MACH_NHI_THU_CTO_TREO")
    public String ANH_TU_ANH_MACH_NHI_THU_CTO_TREO;
    @SerializedName("CREATE_DAY_ANH_MACH_NHI_THU_TU_CTO_TREO")
    public String CREATE_DAY_ANH_MACH_NHI_THU_TU_CTO_TREO;

    //anh TU mach niem phong
    @SerializedName("TEN_ANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO")
    public String TEN_ANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO;
    @SerializedName("ANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO")
    public String ANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO;
    @SerializedName("CREATE_DAY_ANH_MACH_NIEM_PHONG_TU_CTO_TREO")
    public String CREATE_DAY_ANH_MACH_NIEM_PHONG_TU_CTO_TREO;


    //anh TI
    @SerializedName("TEN_ANH_TI_CTO_TREO")
    public String TEN_ANH_TI_CTO_TREO;
    @SerializedName("ANH_TI_CTO_TREO")
    public String ANH_TI_CTO_TREO;
    @SerializedName("CREATE_DAY_ANH_TI_CTO_TREO")
    public String CREATE_DAY_ANH_TI_CTO_TREO;

    //anh TI mach nhi thu
    @SerializedName("TEN_ANH_TI_ANH_MACH_NHI_THU_CTO_TREO")
    public String TEN_ANH_TI_ANH_MACH_NHI_THU_CTO_TREO;
    @SerializedName("ANH_TI_ANH_MACH_NHI_THU_CTO_TREO")
    public String ANH_TI_ANH_MACH_NHI_THU_CTO_TREO;
    @SerializedName("CREATE_DAY_ANH_MACH_NHI_THU_TI_CTO_TREO")
    public String CREATE_DAY_ANH_MACH_NHI_THU_TI_CTO_TREO;

    //anh TI mach niem phong
    @SerializedName("TEN_ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO")
    public String TEN_ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO;
    @SerializedName("ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO")
    public String ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO;
    @SerializedName("CREATE_DAY_ANH_MACH_NIEM_PHONG_TI_CTO_TREO")
    public String CREATE_DAY_ANH_MACH_NIEM_PHONG_TI_CTO_TREO;


    //TODO anh Cong To Thao
    @SerializedName("ID_CHITIET_CTO_THAO")
    public int ID_CHITIET_CTO_THAO;

    //anh cong to
    @SerializedName("TEN_ANH_CTO_THAO")
    public String TEN_ANH_CTO_THAO;
    @SerializedName("TEN_ANH_NIEMPHONG_CTO_THAO")
    public String TEN_ANH_NIEMPHONG_CTO_THAO;
    @SerializedName("ANH_CTO_THAO")
    public String ANH_CTO_THAO;
    @SerializedName("ANH_NIEMPHONG_CTO_THAO")
    public String ANH_NIEMPHONG_CTO_THAO;
    @SerializedName("CREATE_DAY_ANH_CTO_THAO")
    public String CREATE_DAY_ANH_CTO_THAO;
    @SerializedName("CREATE_DAY_ANH_NIEMPHONG_CTO_THAO")
    public String CREATE_DAY_ANH_NIEMPHONG_CTO_THAO;


    public int getID_BBAN_CONGTO() {
        return ID_BBAN_CONGTO;
    }

    public void setID_BBAN_CONGTO(int ID_BBAN_CONGTO) {
        this.ID_BBAN_CONGTO = ID_BBAN_CONGTO;
    }

    public String getMA_DVIQLY() {
        return MA_DVIQLY;
    }

    public void setMA_DVIQLY(String MA_DVIQLY) {
        this.MA_DVIQLY = MA_DVIQLY;
    }

    public String getSO_BBAN() {
        return SO_BBAN;
    }

    public void setSO_BBAN(String SO_BBAN) {
        this.SO_BBAN = SO_BBAN;
    }

    public int getTRANG_THAI() {
        return TRANG_THAI;
    }

    public void setTRANG_THAI(int TRANG_THAI) {
        this.TRANG_THAI = TRANG_THAI;
    }

    public int getLAN_CTO_TREO() {
        return LAN_CTO_TREO;
    }

    public void setLAN_CTO_TREO(int LAN_CTO_TREO) {
        this.LAN_CTO_TREO = LAN_CTO_TREO;
    }

    public int getVTRI_TREO_THAO_CTO_TREO() {
        return VTRI_TREO_THAO_CTO_TREO;
    }

    public void setVTRI_TREO_THAO_CTO_TREO(int VTRI_TREO_THAO_CTO_TREO) {
        this.VTRI_TREO_THAO_CTO_TREO = VTRI_TREO_THAO_CTO_TREO;
    }

    public int getSOVIEN_CBOOC_CTO_TREO() {
        return SOVIEN_CBOOC_CTO_TREO;
    }

    public void setSOVIEN_CBOOC_CTO_TREO(int SOVIEN_CBOOC_CTO_TREO) {
        this.SOVIEN_CBOOC_CTO_TREO = SOVIEN_CBOOC_CTO_TREO;
    }

    public int getLOAI_HOM_CTO_TREO() {
        return LOAI_HOM_CTO_TREO;
    }

    public void setLOAI_HOM_CTO_TREO(int LOAI_HOM_CTO_TREO) {
        this.LOAI_HOM_CTO_TREO = LOAI_HOM_CTO_TREO;
    }

    public int getSOVIEN_CHOM_CTO_TREO() {
        return SOVIEN_CHOM_CTO_TREO;
    }

    public void setSOVIEN_CHOM_CTO_TREO(int SOVIEN_CHOM_CTO_TREO) {
        this.SOVIEN_CHOM_CTO_TREO = SOVIEN_CHOM_CTO_TREO;
    }

    public int getHS_NHAN_CTO_TREO() {
        return HS_NHAN_CTO_TREO;
    }

    public void setHS_NHAN_CTO_TREO(int HS_NHAN_CTO_TREO) {
        this.HS_NHAN_CTO_TREO = HS_NHAN_CTO_TREO;
    }

    public String getSO_TU_CTO_TREO() {
        return SO_TU_CTO_TREO;
    }

    public void setSO_TU_CTO_TREO(String SO_TU_CTO_TREO) {
        this.SO_TU_CTO_TREO = SO_TU_CTO_TREO;
    }

    public String getSO_TI_CTO_TREO() {
        return SO_TI_CTO_TREO;
    }

    public void setSO_TI_CTO_TREO(String SO_TI_CTO_TREO) {
        this.SO_TI_CTO_TREO = SO_TI_CTO_TREO;
    }

    public String getCHI_SO_CTO_TREO() {
        return CHI_SO_CTO_TREO;
    }

    public void setCHI_SO_CTO_TREO(String CHI_SO_CTO_TREO) {
        this.CHI_SO_CTO_TREO = CHI_SO_CTO_TREO;
    }

    public int getCCX_CTO_TREO() {
        return CCX_CTO_TREO;
    }

    public void setCCX_CTO_TREO(int CCX_CTO_TREO) {
        this.CCX_CTO_TREO = CCX_CTO_TREO;
    }

    public String getTEM_CQUANG_CTO_TREO() {
        return TEM_CQUANG_CTO_TREO;
    }

    public void setTEM_CQUANG_CTO_TREO(String TEM_CQUANG_CTO_TREO) {
        this.TEM_CQUANG_CTO_TREO = TEM_CQUANG_CTO_TREO;
    }

    public int getSOVIEN_CHIKDINH_CTO_TREO() {
        return SOVIEN_CHIKDINH_CTO_TREO;
    }

    public void setSOVIEN_CHIKDINH_CTO_TREO(int SOVIEN_CHIKDINH_CTO_TREO) {
        this.SOVIEN_CHIKDINH_CTO_TREO = SOVIEN_CHIKDINH_CTO_TREO;
    }

    public String getDIEN_AP_CTO_TREO() {
        return DIEN_AP_CTO_TREO;
    }

    public void setDIEN_AP_CTO_TREO(String DIEN_AP_CTO_TREO) {
        this.DIEN_AP_CTO_TREO = DIEN_AP_CTO_TREO;
    }

    public String getDONG_DIEN_CTO_TREO() {
        return DONG_DIEN_CTO_TREO;
    }

    public void setDONG_DIEN_CTO_TREO(String DONG_DIEN_CTO_TREO) {
        this.DONG_DIEN_CTO_TREO = DONG_DIEN_CTO_TREO;
    }

    public String getHANGSO_K_CTO_TREO() {
        return HANGSO_K_CTO_TREO;
    }

    public void setHANGSO_K_CTO_TREO(String HANGSO_K_CTO_TREO) {
        this.HANGSO_K_CTO_TREO = HANGSO_K_CTO_TREO;
    }

    public String getSO_KIM_NIEM_CHI_CTO_TREO() {
        return SO_KIM_NIEM_CHI_CTO_TREO;
    }

    public void setSO_KIM_NIEM_CHI_CTO_TREO(String SO_KIM_NIEM_CHI_CTO_TREO) {
        this.SO_KIM_NIEM_CHI_CTO_TREO = SO_KIM_NIEM_CHI_CTO_TREO;
    }

    public String getTTRANG_NPHONG_CTO_TREO() {
        return TTRANG_NPHONG_CTO_TREO;
    }

    public void setTTRANG_NPHONG_CTO_TREO(String TTRANG_NPHONG_CTO_TREO) {
        this.TTRANG_NPHONG_CTO_TREO = TTRANG_NPHONG_CTO_TREO;
    }

    public String getTEN_LOAI_CTO_CTO_TREO() {
        return TEN_LOAI_CTO_CTO_TREO;
    }

    public void setTEN_LOAI_CTO_CTO_TREO(String TEN_LOAI_CTO_CTO_TREO) {
        this.TEN_LOAI_CTO_CTO_TREO = TEN_LOAI_CTO_CTO_TREO;
    }

    public String getPHUONG_THUC_DO_XA_CTO_TREO() {
        return PHUONG_THUC_DO_XA_CTO_TREO;
    }

    public void setPHUONG_THUC_DO_XA_CTO_TREO(String PHUONG_THUC_DO_XA_CTO_TREO) {
        this.PHUONG_THUC_DO_XA_CTO_TREO = PHUONG_THUC_DO_XA_CTO_TREO;
    }

    public String getGHI_CHU_CTO_TREO() {
        return GHI_CHU_CTO_TREO;
    }

    public void setGHI_CHU_CTO_TREO(String GHI_CHU_CTO_TREO) {
        this.GHI_CHU_CTO_TREO = GHI_CHU_CTO_TREO;
    }

    public int getLAN_CTO_THAO() {
        return LAN_CTO_THAO;
    }

    public void setLAN_CTO_THAO(int LAN_CTO_THAO) {
        this.LAN_CTO_THAO = LAN_CTO_THAO;
    }

    public int getVTRI_TREO_THAO_CTO_THAO() {
        return VTRI_TREO_THAO_CTO_THAO;
    }

    public void setVTRI_TREO_THAO_CTO_THAO(int VTRI_TREO_THAO_CTO_THAO) {
        this.VTRI_TREO_THAO_CTO_THAO = VTRI_TREO_THAO_CTO_THAO;
    }

    public int getSOVIEN_CBOOC_CTO_THAO() {
        return SOVIEN_CBOOC_CTO_THAO;
    }

    public void setSOVIEN_CBOOC_CTO_THAO(int SOVIEN_CBOOC_CTO_THAO) {
        this.SOVIEN_CBOOC_CTO_THAO = SOVIEN_CBOOC_CTO_THAO;
    }

    public int getLOAI_HOM_CTO_THAO() {
        return LOAI_HOM_CTO_THAO;
    }

    public void setLOAI_HOM_CTO_THAO(int LOAI_HOM_CTO_THAO) {
        this.LOAI_HOM_CTO_THAO = LOAI_HOM_CTO_THAO;
    }

    public int getSOVIEN_CHOM_CTO_THAO() {
        return SOVIEN_CHOM_CTO_THAO;
    }

    public void setSOVIEN_CHOM_CTO_THAO(int SOVIEN_CHOM_CTO_THAO) {
        this.SOVIEN_CHOM_CTO_THAO = SOVIEN_CHOM_CTO_THAO;
    }

    public int getHS_NHAN_CTO_THAO() {
        return HS_NHAN_CTO_THAO;
    }

    public void setHS_NHAN_CTO_THAO(int HS_NHAN_CTO_THAO) {
        this.HS_NHAN_CTO_THAO = HS_NHAN_CTO_THAO;
    }

    public String getSO_TU_CTO_THAO() {
        return SO_TU_CTO_THAO;
    }

    public void setSO_TU_CTO_THAO(String SO_TU_CTO_THAO) {
        this.SO_TU_CTO_THAO = SO_TU_CTO_THAO;
    }

    public String getSO_TI_CTO_THAO() {
        return SO_TI_CTO_THAO;
    }

    public void setSO_TI_CTO_THAO(String SO_TI_CTO_THAO) {
        this.SO_TI_CTO_THAO = SO_TI_CTO_THAO;
    }

    public String getCHI_SO_CTO_THAO() {
        return CHI_SO_CTO_THAO;
    }

    public void setCHI_SO_CTO_THAO(String CHI_SO_CTO_THAO) {
        this.CHI_SO_CTO_THAO = CHI_SO_CTO_THAO;
    }

    public int getCCX_CTO_THAO() {
        return CCX_CTO_THAO;
    }

    public void setCCX_CTO_THAO(int CCX_CTO_THAO) {
        this.CCX_CTO_THAO = CCX_CTO_THAO;
    }

    public String getTEM_CQUANG_CTO_THAO() {
        return TEM_CQUANG_CTO_THAO;
    }

    public void setTEM_CQUANG_CTO_THAO(String TEM_CQUANG_CTO_THAO) {
        this.TEM_CQUANG_CTO_THAO = TEM_CQUANG_CTO_THAO;
    }

    public int getSOVIEN_CHIKDINH_CTO_THAO() {
        return SOVIEN_CHIKDINH_CTO_THAO;
    }

    public void setSOVIEN_CHIKDINH_CTO_THAO(int SOVIEN_CHIKDINH_CTO_THAO) {
        this.SOVIEN_CHIKDINH_CTO_THAO = SOVIEN_CHIKDINH_CTO_THAO;
    }

    public String getDIEN_AP_CTO_THAO() {
        return DIEN_AP_CTO_THAO;
    }

    public void setDIEN_AP_CTO_THAO(String DIEN_AP_CTO_THAO) {
        this.DIEN_AP_CTO_THAO = DIEN_AP_CTO_THAO;
    }

    public String getDONG_DIEN_CTO_THAO() {
        return DONG_DIEN_CTO_THAO;
    }

    public void setDONG_DIEN_CTO_THAO(String DONG_DIEN_CTO_THAO) {
        this.DONG_DIEN_CTO_THAO = DONG_DIEN_CTO_THAO;
    }

    public String getHANGSO_K_CTO_THAO() {
        return HANGSO_K_CTO_THAO;
    }

    public void setHANGSO_K_CTO_THAO(String HANGSO_K_CTO_THAO) {
        this.HANGSO_K_CTO_THAO = HANGSO_K_CTO_THAO;
    }

    public String getSO_KIM_NIEM_CHI_CTO_THAO() {
        return SO_KIM_NIEM_CHI_CTO_THAO;
    }

    public void setSO_KIM_NIEM_CHI_CTO_THAO(String SO_KIM_NIEM_CHI_CTO_THAO) {
        this.SO_KIM_NIEM_CHI_CTO_THAO = SO_KIM_NIEM_CHI_CTO_THAO;
    }

    public String getTTRANG_NPHONG_CTO_THAO() {
        return TTRANG_NPHONG_CTO_THAO;
    }

    public void setTTRANG_NPHONG_CTO_THAO(String TTRANG_NPHONG_CTO_THAO) {
        this.TTRANG_NPHONG_CTO_THAO = TTRANG_NPHONG_CTO_THAO;
    }

    public String getTEN_LOAI_CTO_CTO_THAO() {
        return TEN_LOAI_CTO_CTO_THAO;
    }

    public void setTEN_LOAI_CTO_CTO_THAO(String TEN_LOAI_CTO_CTO_THAO) {
        this.TEN_LOAI_CTO_CTO_THAO = TEN_LOAI_CTO_CTO_THAO;
    }

    public String getPHUONG_THUC_DO_XA_CTO_THAO() {
        return PHUONG_THUC_DO_XA_CTO_THAO;
    }

    public void setPHUONG_THUC_DO_XA_CTO_THAO(String PHUONG_THUC_DO_XA_CTO_THAO) {
        this.PHUONG_THUC_DO_XA_CTO_THAO = PHUONG_THUC_DO_XA_CTO_THAO;
    }

    public String getGHI_CHU_CTO_THAO() {
        return GHI_CHU_CTO_THAO;
    }

    public void setGHI_CHU_CTO_THAO(String GHI_CHU_CTO_THAO) {
        this.GHI_CHU_CTO_THAO = GHI_CHU_CTO_THAO;
    }

    public int getID_BBAN_TUTI_CTO_TREO() {
        return ID_BBAN_TUTI_CTO_TREO;
    }

    public void setID_BBAN_TUTI_CTO_TREO(int ID_BBAN_TUTI_CTO_TREO) {
        this.ID_BBAN_TUTI_CTO_TREO = ID_BBAN_TUTI_CTO_TREO;
    }

    public int getID_CHITIET_TUTI_TU_CTO_TREO() {
        return ID_CHITIET_TUTI_TU_CTO_TREO;
    }

    public void setID_CHITIET_TUTI_TU_CTO_TREO(int ID_CHITIET_TUTI_TU_CTO_TREO) {
        this.ID_CHITIET_TUTI_TU_CTO_TREO = ID_CHITIET_TUTI_TU_CTO_TREO;
    }

    public int getID_CHITIET_TUTI_TI_CTO_TREO() {
        return ID_CHITIET_TUTI_TI_CTO_TREO;
    }

    public void setID_CHITIET_TUTI_TI_CTO_TREO(int ID_CHITIET_TUTI_TI_CTO_TREO) {
        this.ID_CHITIET_TUTI_TI_CTO_TREO = ID_CHITIET_TUTI_TI_CTO_TREO;
    }

    public int getID_CHITIET_CTO_TREO() {
        return ID_CHITIET_CTO_TREO;
    }

    public void setID_CHITIET_CTO_TREO(int ID_CHITIET_CTO_TREO) {
        this.ID_CHITIET_CTO_TREO = ID_CHITIET_CTO_TREO;
    }

    public String getTEN_ANH_CTO_TREO() {
        return TEN_ANH_CTO_TREO;
    }

    public void setTEN_ANH_CTO_TREO(String TEN_ANH_CTO_TREO) {
        this.TEN_ANH_CTO_TREO = TEN_ANH_CTO_TREO;
    }

    public String getTEN_ANH_NIEMPHONG_CTO_TREO() {
        return TEN_ANH_NIEMPHONG_CTO_TREO;
    }

    public void setTEN_ANH_NIEMPHONG_CTO_TREO(String TEN_ANH_NIEMPHONG_CTO_TREO) {
        this.TEN_ANH_NIEMPHONG_CTO_TREO = TEN_ANH_NIEMPHONG_CTO_TREO;
    }

    public String getANH_CTO_TREO() {
        return ANH_CTO_TREO;
    }

    public void setANH_CTO_TREO(String ANH_CTO_TREO) {
        this.ANH_CTO_TREO = ANH_CTO_TREO;
    }

    public String getANH_NIEMPHONG_CTO_TREO() {
        return ANH_NIEMPHONG_CTO_TREO;
    }

    public void setANH_NIEMPHONG_CTO_TREO(String ANH_NIEMPHONG_CTO_TREO) {
        this.ANH_NIEMPHONG_CTO_TREO = ANH_NIEMPHONG_CTO_TREO;
    }

    public String getCREATE_DAY_ANH_CTO_TREO() {
        return CREATE_DAY_ANH_CTO_TREO;
    }

    public void setCREATE_DAY_ANH_CTO_TREO(String CREATE_DAY_ANH_CTO_TREO) {
        this.CREATE_DAY_ANH_CTO_TREO = CREATE_DAY_ANH_CTO_TREO;
    }

    public String getCREATE_DAY_ANH_NIEMPHONG_CTO_TREO() {
        return CREATE_DAY_ANH_NIEMPHONG_CTO_TREO;
    }

    public void setCREATE_DAY_ANH_NIEMPHONG_CTO_TREO(String CREATE_DAY_ANH_NIEMPHONG_CTO_TREO) {
        this.CREATE_DAY_ANH_NIEMPHONG_CTO_TREO = CREATE_DAY_ANH_NIEMPHONG_CTO_TREO;
    }

    public String getTEN_ANH_TU_CTO_TREO() {
        return TEN_ANH_TU_CTO_TREO;
    }

    public void setTEN_ANH_TU_CTO_TREO(String TEN_ANH_TU_CTO_TREO) {
        this.TEN_ANH_TU_CTO_TREO = TEN_ANH_TU_CTO_TREO;
    }

    public String getANH_TU_CTO_TREO() {
        return ANH_TU_CTO_TREO;
    }

    public void setANH_TU_CTO_TREO(String ANH_TU_CTO_TREO) {
        this.ANH_TU_CTO_TREO = ANH_TU_CTO_TREO;
    }

    public String getCREATE_DAY_ANH_TU_CTO_TREO() {
        return CREATE_DAY_ANH_TU_CTO_TREO;
    }

    public void setCREATE_DAY_ANH_TU_CTO_TREO(String CREATE_DAY_ANH_TU_CTO_TREO) {
        this.CREATE_DAY_ANH_TU_CTO_TREO = CREATE_DAY_ANH_TU_CTO_TREO;
    }

    public String getTEN_ANH_TU_ANH_MACH_NHI_THU_CTO_TREO() {
        return TEN_ANH_TU_ANH_MACH_NHI_THU_CTO_TREO;
    }

    public void setTEN_ANH_TU_ANH_MACH_NHI_THU_CTO_TREO(String TEN_ANH_TU_ANH_MACH_NHI_THU_CTO_TREO) {
        this.TEN_ANH_TU_ANH_MACH_NHI_THU_CTO_TREO = TEN_ANH_TU_ANH_MACH_NHI_THU_CTO_TREO;
    }

    public String getANH_TU_ANH_MACH_NHI_THU_CTO_TREO() {
        return ANH_TU_ANH_MACH_NHI_THU_CTO_TREO;
    }

    public void setANH_TU_ANH_MACH_NHI_THU_CTO_TREO(String ANH_TU_ANH_MACH_NHI_THU_CTO_TREO) {
        this.ANH_TU_ANH_MACH_NHI_THU_CTO_TREO = ANH_TU_ANH_MACH_NHI_THU_CTO_TREO;
    }

    public String getCREATE_DAY_ANH_MACH_NHI_THU_TU_CTO_TREO() {
        return CREATE_DAY_ANH_MACH_NHI_THU_TU_CTO_TREO;
    }

    public void setCREATE_DAY_ANH_MACH_NHI_THU_TU_CTO_TREO(String CREATE_DAY_ANH_MACH_NHI_THU_TU_CTO_TREO) {
        this.CREATE_DAY_ANH_MACH_NHI_THU_TU_CTO_TREO = CREATE_DAY_ANH_MACH_NHI_THU_TU_CTO_TREO;
    }

    public String getTEN_ANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO() {
        return TEN_ANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO;
    }

    public void setTEN_ANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO(String TEN_ANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO) {
        this.TEN_ANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO = TEN_ANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO;
    }

    public String getANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO() {
        return ANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO;
    }

    public void setANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO(String ANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO) {
        this.ANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO = ANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO;
    }

    public String getCREATE_DAY_ANH_MACH_NIEM_PHONG_TU_CTO_TREO() {
        return CREATE_DAY_ANH_MACH_NIEM_PHONG_TU_CTO_TREO;
    }

    public void setCREATE_DAY_ANH_MACH_NIEM_PHONG_TU_CTO_TREO(String CREATE_DAY_ANH_MACH_NIEM_PHONG_TU_CTO_TREO) {
        this.CREATE_DAY_ANH_MACH_NIEM_PHONG_TU_CTO_TREO = CREATE_DAY_ANH_MACH_NIEM_PHONG_TU_CTO_TREO;
    }

    public String getTEN_ANH_TI_CTO_TREO() {
        return TEN_ANH_TI_CTO_TREO;
    }

    public void setTEN_ANH_TI_CTO_TREO(String TEN_ANH_TI_CTO_TREO) {
        this.TEN_ANH_TI_CTO_TREO = TEN_ANH_TI_CTO_TREO;
    }

    public String getANH_TI_CTO_TREO() {
        return ANH_TI_CTO_TREO;
    }

    public void setANH_TI_CTO_TREO(String ANH_TI_CTO_TREO) {
        this.ANH_TI_CTO_TREO = ANH_TI_CTO_TREO;
    }

    public String getCREATE_DAY_ANH_TI_CTO_TREO() {
        return CREATE_DAY_ANH_TI_CTO_TREO;
    }

    public void setCREATE_DAY_ANH_TI_CTO_TREO(String CREATE_DAY_ANH_TI_CTO_TREO) {
        this.CREATE_DAY_ANH_TI_CTO_TREO = CREATE_DAY_ANH_TI_CTO_TREO;
    }

    public String getTEN_ANH_TI_ANH_MACH_NHI_THU_CTO_TREO() {
        return TEN_ANH_TI_ANH_MACH_NHI_THU_CTO_TREO;
    }

    public void setTEN_ANH_TI_ANH_MACH_NHI_THU_CTO_TREO(String TEN_ANH_TI_ANH_MACH_NHI_THU_CTO_TREO) {
        this.TEN_ANH_TI_ANH_MACH_NHI_THU_CTO_TREO = TEN_ANH_TI_ANH_MACH_NHI_THU_CTO_TREO;
    }

    public String getANH_TI_ANH_MACH_NHI_THU_CTO_TREO() {
        return ANH_TI_ANH_MACH_NHI_THU_CTO_TREO;
    }

    public void setANH_TI_ANH_MACH_NHI_THU_CTO_TREO(String ANH_TI_ANH_MACH_NHI_THU_CTO_TREO) {
        this.ANH_TI_ANH_MACH_NHI_THU_CTO_TREO = ANH_TI_ANH_MACH_NHI_THU_CTO_TREO;
    }

    public String getCREATE_DAY_ANH_MACH_NHI_THU_TI_CTO_TREO() {
        return CREATE_DAY_ANH_MACH_NHI_THU_TI_CTO_TREO;
    }

    public void setCREATE_DAY_ANH_MACH_NHI_THU_TI_CTO_TREO(String CREATE_DAY_ANH_MACH_NHI_THU_TI_CTO_TREO) {
        this.CREATE_DAY_ANH_MACH_NHI_THU_TI_CTO_TREO = CREATE_DAY_ANH_MACH_NHI_THU_TI_CTO_TREO;
    }

    public String getTEN_ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO() {
        return TEN_ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO;
    }

    public void setTEN_ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO(String TEN_ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO) {
        this.TEN_ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO = TEN_ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO;
    }

    public String getANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO() {
        return ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO;
    }

    public void setANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO(String ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO) {
        this.ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO = ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO;
    }

    public String getCREATE_DAY_ANH_MACH_NIEM_PHONG_TI_CTO_TREO() {
        return CREATE_DAY_ANH_MACH_NIEM_PHONG_TI_CTO_TREO;
    }

    public void setCREATE_DAY_ANH_MACH_NIEM_PHONG_TI_CTO_TREO(String CREATE_DAY_ANH_MACH_NIEM_PHONG_TI_CTO_TREO) {
        this.CREATE_DAY_ANH_MACH_NIEM_PHONG_TI_CTO_TREO = CREATE_DAY_ANH_MACH_NIEM_PHONG_TI_CTO_TREO;
    }


    public int getID_CHITIET_CTO_THAO() {
        return ID_CHITIET_CTO_THAO;
    }

    public void setID_CHITIET_CTO_THAO(int ID_CHITIET_CTO_THAO) {
        this.ID_CHITIET_CTO_THAO = ID_CHITIET_CTO_THAO;
    }

    public String getTEN_ANH_CTO_THAO() {
        return TEN_ANH_CTO_THAO;
    }

    public void setTEN_ANH_CTO_THAO(String TEN_ANH_CTO_THAO) {
        this.TEN_ANH_CTO_THAO = TEN_ANH_CTO_THAO;
    }

    public String getTEN_ANH_NIEMPHONG_CTO_THAO() {
        return TEN_ANH_NIEMPHONG_CTO_THAO;
    }

    public void setTEN_ANH_NIEMPHONG_CTO_THAO(String TEN_ANH_NIEMPHONG_CTO_THAO) {
        this.TEN_ANH_NIEMPHONG_CTO_THAO = TEN_ANH_NIEMPHONG_CTO_THAO;
    }

    public String getANH_CTO_THAO() {
        return ANH_CTO_THAO;
    }

    public void setANH_CTO_THAO(String ANH_CTO_THAO) {
        this.ANH_CTO_THAO = ANH_CTO_THAO;
    }

    public String getANH_NIEMPHONG_CTO_THAO() {
        return ANH_NIEMPHONG_CTO_THAO;
    }

    public void setANH_NIEMPHONG_CTO_THAO(String ANH_NIEMPHONG_CTO_THAO) {
        this.ANH_NIEMPHONG_CTO_THAO = ANH_NIEMPHONG_CTO_THAO;
    }

    public String getCREATE_DAY_ANH_CTO_THAO() {
        return CREATE_DAY_ANH_CTO_THAO;
    }

    public void setCREATE_DAY_ANH_CTO_THAO(String CREATE_DAY_ANH_CTO_THAO) {
        this.CREATE_DAY_ANH_CTO_THAO = CREATE_DAY_ANH_CTO_THAO;
    }

    public String getCREATE_DAY_ANH_NIEMPHONG_CTO_THAO() {
        return CREATE_DAY_ANH_NIEMPHONG_CTO_THAO;
    }

    public void setCREATE_DAY_ANH_NIEMPHONG_CTO_THAO(String CREATE_DAY_ANH_NIEMPHONG_CTO_THAO) {
        this.CREATE_DAY_ANH_NIEMPHONG_CTO_THAO = CREATE_DAY_ANH_NIEMPHONG_CTO_THAO;
    }
}
