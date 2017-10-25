package com.es.tungnv.utils;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

/**
 * Created by TUNGNV on 3/11/2016.
 */
public class InvoicePrinterHoaDon {

    int SOKYTU = 32;
    Context ct;
    InvoiceCommon comm;

    public InvoicePrinterHoaDon() {
        comm = new InvoiceCommon();
    }

    public void PrintMsg(Context ct, Cursor c) {
        this.ct = ct;
        ArrayList<String> lstStr = CreateStringHoaDon(c);
        Print(lstStr);
    }

    public ArrayList<String> CreateStringHoaDon(Cursor c) {
        if(c.moveToFirst()){
            do {

            } while (c.moveToNext());
        }
        return null;
    }

    public void Print(ArrayList<String> lstTextInput) {
        ArrayList<String> listText = new ArrayList<String>();
        for (String text : lstTextInput) {
            ArrayList<String> listTemp = TachChuList(text, SOKYTU);
            for (String listItem : listTemp) {
                listText.add(listItem);
            }
        }

        String strPrint = "";
        for (String listTextItem : listText) {
            if (listTextItem != null && !listTextItem.trim().isEmpty())
                strPrint += String.format("%-" + SOKYTU + "s", listTextItem);

        }

        InvoiceCommon.printer.Print(strPrint);
    }

    public ArrayList<String> TachChuList(String strBangChu, int intSoKiTu) {
        ArrayList<String> strBangChuLine = new ArrayList<String>();
        int Int_Cot = 0;
        int Int_Dong = 1;
        String strBangchu_1 = strBangChu;
        int k = 0;
        int i = 0;
        int j = 0;
        // int_dong la số dòng của chuỗi tiền
        // Int_cot là số cột hoá đơn của phần in chữ (toi da bao nhieu ki tu)
        Int_Cot = intSoKiTu;

        j = 1;
        if (strBangChu.length() < Int_Cot) {
            strBangChuLine.add(strBangchu_1);
            return strBangChuLine;

        }

        while (k <= strBangChu.length()) {

            for (i = Int_Cot - 1; i >= 1; i += -1) {
                if (strBangchu_1.substring(i, i + 1).equals(" ")) {
                    break; // TODO: might not be correct. Was : Exit For
                }
            }

            strBangChuLine.add(strBangchu_1.substring(0, i + 1));
            strBangchu_1 = strBangchu_1.substring(i + 1);
            j += 1;
            k += i + 1;
            if (strBangchu_1.length() < Int_Cot) {
                strBangChuLine.add(strBangchu_1);
                break; // TODO: might not be correct. Was : Exit Do
            }

        }

        return strBangChuLine;
    }

}
