package com.es.tungnv.entity;

public class InvoiceConfigInfo {
    private String IP_SV_1;
    private String VERSION;

    public String getIP_SV_1() {
        return IP_SV_1;
    }

    public void setIP_SV_1(String iP_SV_1) {
        IP_SV_1 = iP_SV_1;
    }

    public String getVERSION() {
        return VERSION;
    }

    public void setVERSION(String vERSION) {
        VERSION = vERSION;
    }

    public InvoiceConfigInfo() {
        super();
        IP_SV_1 = "";
        VERSION = "";
    }

    public InvoiceConfigInfo(String ip1, String ver) {
        this.IP_SV_1 = ip1;
        this.VERSION = ver;
    }

}
