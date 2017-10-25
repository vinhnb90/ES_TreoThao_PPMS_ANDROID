
package com.es.tungnv.utils;

/**
 * Created by VinhNB on 8/5/2016.
 */
public class Data {
    // Tag any context for logger
    public static final String TAG = "LOGGER";

    public static String sPath = "";
    // name file share preferene
    public static final String NAME_SHARE_REF_LOGIN = "esolutions.com.vn.ppms.activities.LoginActivity";

    private static String URL;

    private static String URL_CHECK_LOGIN = "/api/nhanvienphuctra/GetNhanVienPhucTra?";

    private static String URL_UPDATE_INFO = "/api/nhanvienphuctra/SetThongTinNhanVien";

    private static String URL_UPDATE_PASS = "/api/nhanvienphuctra/SetPassNhanVien";

    private static String URL_GET_TASK = "/api/phancongphuctra/GetPhanCongChuaPhucTra?";

    private static String URL_GET_HISORY_TASK = "/api/phancongphuctra/GetLichSuPhanCongPhucTra?";

    private static String URL_COMMIT_TASK = "/api/phancongphuctra/SetPhanCongPhucTra";

    private static String URL_GET_DEPARTMENT = "/api/nhanvienphuctra/GetDonViPhucTra";

    public static void setURL(String URL) {
        Data.URL = "http://"+ URL;
    }

    public static String getURLCommitTask(){
        return URL + URL_COMMIT_TASK;
    }
    public static String getUrlQueryGetDepartment() {
        return URL + URL_GET_DEPARTMENT;
    }

    // url query check login server
    public static String getUrlQueryCheckLogin(String sUsername, String sPassword, String sDepartCode) {
        return URL + URL_CHECK_LOGIN
                + "user="
                + sUsername
                + "&pass="
                + sPassword
                + "&departCode="
                + sDepartCode;
    }

    //url lấy về các phân công chưa phúc tra
    public static String getUrlQueryGetTask(int idNhanVien, String stateTask) {
        return URL + URL_GET_TASK
                + "idNhanVien="
                + idNhanVien
                + "&trangThaiPhucTra="
                + stateTask;
    }

    //url lấy về các phân công chưa phúc tra
    public static String getUrlQueryGetHistoryTask(String idNhanVien, String ngayBatDau, String ngayKetThuc) {
        return URL + URL_GET_HISORY_TASK
                + "idNhanVien="
                + idNhanVien
                + "&ngayBatDau="
                + ngayBatDau
                + "&ngayKetThuc="
                + ngayKetThuc;
    }
}
