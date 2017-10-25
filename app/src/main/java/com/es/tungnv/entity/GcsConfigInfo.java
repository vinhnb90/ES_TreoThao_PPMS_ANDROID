package com.es.tungnv.entity;

public class GcsConfigInfo {
    private boolean WarningEnable;
    private boolean WarningEnable2;
    private boolean WarningEnable3;
    private int VuotDinhMuc;
    private int VuotDinhMuc2;
    private int DuoiDinhMuc;
    private int DuoiDinhMuc2;
    private String IP_SERVICE;
    private String DVIQLY;
    private String VERSION;

    public GcsConfigInfo() {
        super();
        WarningEnable = false;
        WarningEnable2 = false;
        WarningEnable3 = false;
        VuotDinhMuc = 200;
        VuotDinhMuc2 = 0;
        DuoiDinhMuc = 100;
        DuoiDinhMuc2 = 0;
        IP_SERVICE = "";
        DVIQLY = "";
        VERSION = "HN";
    }

    public GcsConfigInfo(boolean warningEnable3, boolean warningEnable, boolean warningEnable2, int vuotDinhMuc,
                         int duoiDinhMuc, int vuotDinhMuc2, int duoiDinhMuc2, String IP_SERVICE,String DVIQLY, String VERSION) {
        super();
        this.WarningEnable = warningEnable;
        this.WarningEnable2 = warningEnable2;
        this.WarningEnable3 = warningEnable3;

        this.VuotDinhMuc = vuotDinhMuc;
        this.VuotDinhMuc2 = vuotDinhMuc2;
        this.DuoiDinhMuc = duoiDinhMuc;
        this.DuoiDinhMuc2 = duoiDinhMuc2;
        this.IP_SERVICE = IP_SERVICE;
        this.DVIQLY = DVIQLY;
        this.VERSION = VERSION;
    }

    public void setVERSION(String VERSION) {
        this.VERSION = VERSION;
    }

    public String getVERSION() {
        return VERSION;
    }

    public String getDVIQLY() {
        return DVIQLY;
    }

    public void setDVIQLY(String dVIQLY) {
        DVIQLY = dVIQLY;
    }

    public boolean isWarningEnable3() {
        return WarningEnable3;
    }

    public void setWarningEnable3(boolean warningEnable3) {
        WarningEnable3 = warningEnable3;
    }

    public boolean isWarningEnable() {
        return WarningEnable;
    }

    public void setWarningEnable(boolean warningEnable) {
        WarningEnable = warningEnable;
    }

    public int getVuotDinhMuc() {
        return VuotDinhMuc;
    }

    public void setVuotDinhMuc(int vuotDinhMuc) {
        VuotDinhMuc = vuotDinhMuc;
    }

    public int getDuoiDinhMuc() {
        return DuoiDinhMuc;
    }

    public void setDuoiDinhMuc(int duoiDinhMuc) {
        DuoiDinhMuc = duoiDinhMuc;
    }

    public String getIP_SERVICE() {
        return IP_SERVICE;
    }

    public void setIP_SERVICE(String iP_SERVICE) {
        IP_SERVICE = iP_SERVICE;
    }

    public boolean isWarningEnable2() {
        return WarningEnable2;
    }

    public void setWarningEnable2(boolean warningEnable2) {
        WarningEnable2 = warningEnable2;
    }

    public int getVuotDinhMuc2() {
        return VuotDinhMuc2;
    }

    public void setVuotDinhMuc2(int vuotDinhMuc2) {
        VuotDinhMuc2 = vuotDinhMuc2;
    }

    public int getDuoiDinhMuc2() {
        return DuoiDinhMuc2;
    }

    public void setDuoiDinhMuc2(int duoiDinhMuc2) {
        DuoiDinhMuc2 = duoiDinhMuc2;
    }

}
