package com.es.tungnv.draws;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.widget.Toast;

import com.es.tungnv.utils.Common;
import com.es.tungnv.utils.EsspCommon;
import com.es.tungnv.views.EsspViewPTNActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by TUNGNV on 9/13/2016.
 */
public class EsspPTNView extends SurfaceView {

    private float lineNumber = 1.2f;

    public EsspPTNView(Context context) {
        super(context);
    }

    public EsspPTNView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EsspPTNView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawHeader(canvas);
        drawTieuNgu(canvas);
        drawTieuDe(canvas);
        drawPhanI(canvas);
        drawPhanIMuc1(canvas);
    }

    private Paint getPaint() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1.0f);
        return paint;
    }

    private TextPaint getPaintText() {
        TextPaint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(50);
        return mTextPaint;
    }

    private Paint getPaintTextNormal() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "times.ttf");
        Paint paintText = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paintText.setStrokeCap(Paint.Cap.ROUND);
        paintText.setColor(Color.BLACK);
        paintText.setStyle(Paint.Style.FILL);
        paintText.setTextSize(EsspCommon.textSizeView);
        paintText.setTypeface(tf);
        return paintText;
    }

    private Paint getPaintTextBold() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "timesbd.ttf");
        Paint paintText = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paintText.setStrokeCap(Paint.Cap.ROUND);
        paintText.setColor(Color.BLACK);
        paintText.setStyle(Paint.Style.FILL);
        paintText.setTextSize(EsspCommon.textSizeView);
        paintText.setTypeface(tf);
        return paintText;
    }

    private void drawHeader(Canvas canvas) {
        String MA_YCAU_KNAI = EsspViewPTNActivity.entityHoSo.getCMIS_MA_YCAU_KNAI();
        String BM = "BM/HD-B09-01-01";

        Rect rectBM = new Rect();
        getPaintTextBold().getTextBounds(BM, 0, BM.length(), rectBM);

        canvas.drawText(MA_YCAU_KNAI, EsspViewPTNActivity.leftMargin + 10, EsspViewPTNActivity.topMargin, getPaintTextNormal());
        canvas.drawText(BM, EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.widthPage - rectBM.width(),
                EsspViewPTNActivity.topMargin, getPaintTextBold());
    }

    private void drawTieuNgu(Canvas canvas) {
        String DON_VI_1 = "TỔNG CÔNG TY";
        String DON_VI_2 = "ĐIỆN LỰC TP HÀ NỘI";
        String DON_VI_3 = EsspViewPTNActivity.TEN_DVIQLY;
        String QUOC_HIEU_1 = "CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM";
        String QUOC_HIEU_2 = "Độc lập - Tự do - Hạnh phúc";

        lineNumber = 2;//line 2
        canvas.drawText(EsspCommon.centerText(DON_VI_1, EsspViewPTNActivity.widthPage / 2, getPaintTextNormal()),
                EsspViewPTNActivity.leftMargin, EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextNormal());
        canvas.drawText(EsspCommon.centerText(QUOC_HIEU_1, EsspViewPTNActivity.widthPage / 2, getPaintTextBold()),
                EsspViewPTNActivity.widthPage / 2 + EsspViewPTNActivity.leftMargin,
                EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextBold());
        lineNumber = 3;//line 3
        canvas.drawText(EsspCommon.centerText(DON_VI_2, EsspViewPTNActivity.widthPage / 2, getPaintTextNormal()),
                EsspViewPTNActivity.leftMargin, EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextNormal());
        canvas.drawText(EsspCommon.centerText(QUOC_HIEU_2, EsspViewPTNActivity.widthPage / 2, getPaintTextBold()),
                EsspViewPTNActivity.widthPage / 2 + EsspViewPTNActivity.leftMargin,
                EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextBold());
        canvas.drawLine(EsspViewPTNActivity.widthPage / 2 + EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.widthPage / 4 - EsspViewPTNActivity.widthPage / 10,
                EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView + 8,
                EsspViewPTNActivity.widthPage / 2 + EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.widthPage / 4 + EsspViewPTNActivity.widthPage / 10,
                EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView + 8, getPaint());
        lineNumber = 4;//line 4
        canvas.drawText(EsspCommon.centerText(DON_VI_3, EsspViewPTNActivity.widthPage / 2, getPaintTextBold()),
                EsspViewPTNActivity.leftMargin, EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextBold());
        canvas.drawLine(EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.widthPage / 4 - EsspViewPTNActivity.widthPage / 10,
                EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView + 8,
                EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.widthPage / 4 + EsspViewPTNActivity.widthPage / 10,
                EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView + 8, getPaint());
    }

    private void drawTieuDe(Canvas canvas) {
        String TIEU_DE = "PHIẾU TIẾP NHẬN THÔNG TIN ĐỀ NGHỊ MUA ĐIỆN";
        lineNumber = 6.5f;//line 5
        canvas.drawText(EsspCommon.centerText(TIEU_DE, EsspViewPTNActivity.widthPage, getPaintTextBold()),
                EsspViewPTNActivity.leftMargin, EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView,
                getPaintTextBold());
    }

    private void drawPhanI(Canvas canvas) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdfRoot = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

            String PHAN_I = "Phần I: Phần khách hàng ghi";
            String T_TEN_KH = "Tên khách hàng/người đại diện: ";
            String T_CMT = "Số chứng minh nhân dân/hộ chiếu: ";
            String T_NGAY_CAP = "Ngày cấp: ";
            String T_NOI_CAP = "Nơi cấp: ";
            String T_DIACHI_GD = "Địa chỉ giao dịch: ";
            String T_DIACHI_SD = "Địa chỉ nơi sử dụng điện: ";
            String T_SDT_LH = "Số điện thoại liên hệ: ";
            String T_SDT_NT = "Số điện thoại thường dùng sử dụng cho dịch vụ nhắn tin của EVN HANOI: ";
            String T_EMAIL = "Địa chỉ Email nhận các dịch vụ thông báo của EVN HANOI: ";

            String TEN_KH = EsspViewPTNActivity.entityHoSo.getTEN_KHANG().isEmpty() ? "..............................." : EsspViewPTNActivity.entityHoSo.getTEN_KHANG();
            String CMT = EsspViewPTNActivity.entityHoSo.getSO_CMT().isEmpty() ? "....................." : EsspViewPTNActivity.entityHoSo.getSO_CMT();
            String NGAY_CAP = EsspViewPTNActivity.entityHoSo.getNGAY_CAP().isEmpty() ? "...................." : sdf.format(sdfRoot.parse(EsspViewPTNActivity.entityHoSo.getNGAY_CAP()));
            String NOI_CAP = EsspViewPTNActivity.entityHoSo.getNOI_CAP().isEmpty() ? "..................." : EsspViewPTNActivity.entityHoSo.getNOI_CAP();
            String DIA_CHI_GD = (EsspViewPTNActivity.entityHoSo.getSO_NHA() + EsspViewPTNActivity.entityHoSo.getDUONG_PHO()).isEmpty()
                    ? "..................." : EsspViewPTNActivity.entityHoSo.getSO_NHA() + EsspViewPTNActivity.entityHoSo.getDUONG_PHO();
            String DIA_CHI_SD = (EsspViewPTNActivity.entityHoSo.getSO_NHA_DDO() + EsspViewPTNActivity.entityHoSo.getDUONG_PHO_DDO()).isEmpty()
                    ? "..................." : EsspViewPTNActivity.entityHoSo.getSO_NHA_DDO() + EsspViewPTNActivity.entityHoSo.getDUONG_PHO_DDO();
            String SDT_LH = EsspViewPTNActivity.entityHoSo.getDTHOAI_DD().isEmpty() ? "..............................." : EsspViewPTNActivity.entityHoSo.getDTHOAI_DD();
            String SDT_NT = EsspViewPTNActivity.entityHoSo.getDTHOAI_DD().isEmpty() ? "..............................." : EsspViewPTNActivity.entityHoSo.getDTHOAI_DD();
            String EMAIL = EsspViewPTNActivity.entityHoSo.getEMAIL().isEmpty() ? "..............................." : EsspViewPTNActivity.entityHoSo.getEMAIL();

            Rect rectTTenKH = new Rect();
            getPaintTextNormal().getTextBounds(T_TEN_KH, 0, T_TEN_KH.length(), rectTTenKH);

            Rect rectTCMT = new Rect();
            getPaintTextNormal().getTextBounds(T_CMT, 0, T_CMT.length(), rectTCMT);
            Rect rectCMT = new Rect();
            getPaintTextNormal().getTextBounds(T_CMT + CMT, 0, (T_CMT + CMT).length(), rectCMT);

            Rect rectTNgayCap = new Rect();
            getPaintTextNormal().getTextBounds(T_CMT + CMT + T_NGAY_CAP, 0, (T_CMT + CMT + T_NGAY_CAP).length(), rectTNgayCap);
            Rect rectNgayCap = new Rect();
            getPaintTextNormal().getTextBounds(T_CMT + CMT + T_NGAY_CAP + NGAY_CAP, 0, (T_CMT + CMT + T_NGAY_CAP + NGAY_CAP).length(), rectNgayCap);

            Rect rectTNoiCap = new Rect();
            getPaintTextNormal().getTextBounds(T_CMT + CMT + T_NGAY_CAP + NGAY_CAP + T_NOI_CAP, 0, (T_CMT + CMT + T_NGAY_CAP + NGAY_CAP + T_NOI_CAP).length(), rectTNoiCap);

            Rect rectTDiaChiGD = new Rect();
            getPaintTextNormal().getTextBounds(T_DIACHI_GD, 0, T_DIACHI_GD.length(), rectTDiaChiGD);
            Rect rectDiaChiGD = new Rect();
            getPaintTextNormal().getTextBounds(DIA_CHI_GD, 0, DIA_CHI_GD.length(), rectDiaChiGD);

            Rect rectTDiaChiSD = new Rect();
            getPaintTextNormal().getTextBounds(T_DIACHI_SD, 0, T_DIACHI_SD.length(), rectTDiaChiSD);
            Rect rectDiaChiSD = new Rect();
            getPaintTextNormal().getTextBounds(DIA_CHI_SD, 0, DIA_CHI_SD.length(), rectDiaChiSD);

            Rect rectTSDTLH = new Rect();
            getPaintTextNormal().getTextBounds(T_SDT_LH, 0, T_SDT_LH.length(), rectTSDTLH);

            Rect rectTSDTNT = new Rect();
            getPaintTextNormal().getTextBounds(T_SDT_NT, 0, T_SDT_NT.length(), rectTSDTNT);

            Rect rectTEmail = new Rect();
            getPaintTextNormal().getTextBounds(T_EMAIL, 0, T_EMAIL.length(), rectTEmail);

            lineNumber = 8;//line 8
            canvas.drawText(PHAN_I, EsspViewPTNActivity.leftMargin,
                    EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextBold());
            lineNumber++;//line 9
            canvas.drawText(T_TEN_KH, EsspViewPTNActivity.leftMargin,
                    EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextNormal());
            canvas.drawText(TEN_KH, EsspViewPTNActivity.leftMargin + rectTTenKH.width() + 5,
                    EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextBold());
            lineNumber++;//line 10
            canvas.drawText(T_CMT,
                    EsspViewPTNActivity.leftMargin,
                    EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextNormal());
            canvas.drawText(CMT,
                    EsspViewPTNActivity.leftMargin + rectTCMT.width() + 5,
                    EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextBold());
            canvas.drawText(T_NGAY_CAP,
                    EsspViewPTNActivity.leftMargin + rectCMT.width() + 10,
                    EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextNormal());
            canvas.drawText(NGAY_CAP,
                    EsspViewPTNActivity.leftMargin + rectTNgayCap.width() + 15,
                    EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextBold());
            canvas.drawText(T_NOI_CAP,
                    EsspViewPTNActivity.leftMargin + rectNgayCap.width() + 20,
                    EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextNormal());
            canvas.drawText(NOI_CAP,
                    EsspViewPTNActivity.leftMargin + rectTNoiCap.width() + 25,
                    EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextBold());
            lineNumber++;//line 11
            canvas.drawText(T_DIACHI_GD,
                    EsspViewPTNActivity.leftMargin,
                    EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextNormal());
            StringBuilder [] sbDC_GDs = Common.splitMultiTextTimes(DIA_CHI_GD, EsspViewPTNActivity.widthPage
                    - rectTDiaChiGD.width() - 20, rectDiaChiGD.width(), getPaintTextBold());
            for (int i = 0; i < sbDC_GDs.length; i++) {
                if(i > 0)
                    lineNumber ++;
                canvas.drawText(sbDC_GDs[i].toString(),
                        EsspViewPTNActivity.leftMargin + rectTDiaChiGD.width() + 5,
                        EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextBold());
            }
            lineNumber++;//line 12
            canvas.drawText(T_DIACHI_SD,
                    EsspViewPTNActivity.leftMargin,
                    EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextNormal());
            StringBuilder [] sbDC_SDs = Common.splitMultiTextTimes(DIA_CHI_SD, EsspViewPTNActivity.widthPage
                    - rectTDiaChiSD.width() - 20, rectDiaChiSD.width(), getPaintTextBold());
            for (int i = 0; i < sbDC_SDs.length; i++) {
                if(i > 0)
                    lineNumber ++;
                canvas.drawText(sbDC_SDs[i].toString(),
                        EsspViewPTNActivity.leftMargin + rectTDiaChiSD.width() + 5,
                        EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextBold());
            }
            lineNumber++;//line 13
            canvas.drawText(T_SDT_LH,
                    EsspViewPTNActivity.leftMargin,
                    EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextNormal());
            canvas.drawText(SDT_LH, EsspViewPTNActivity.leftMargin + rectTSDTLH.width() + 5,
                    EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextBold());
            lineNumber++;//line 14
            canvas.drawText(T_SDT_NT,
                    EsspViewPTNActivity.leftMargin,
                    EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextNormal());
            canvas.drawText(SDT_NT, EsspViewPTNActivity.leftMargin + rectTSDTNT.width() + 5,
                    EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextBold());
            lineNumber++;//line 15
            canvas.drawText(T_EMAIL,
                    EsspViewPTNActivity.leftMargin,
                    EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextNormal());
            canvas.drawText(EMAIL, EsspViewPTNActivity.leftMargin + rectTEmail.width() + 5,
                    EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextBold());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawPhanIMuc1(Canvas canvas) {
        try {
            String MUC_1 = "1.   Công suất sử dụng điện theo từng mục đích:";
            String T_TONG_CS = "      Tổng công suất lắp đặt:";
            String T_CAP_DA = "Cấp điện áp sử dụng:";
            String T_CS_MAX = "      Công suất sử dụng cao nhất:";
            String T_CS_TB = "Công suất sử dụng trung bình:";
            String T_HESO_DT = "      Hệ số đồng thời:";
            String T_KW = "kW;";
            String T_V = "V";
            String T_TITLE_MUCDICH = "Mục đích dùng điện (SX, KDDV, văn phòng, SH...)";
            String T_TITLE_TENTB = "Tên thiết bị";
            String T_TITLE_DIENAP = "Điện áp sử dụng (V)";
            String T_TITLE_CONGSUAT = "Công suất (kW)";
            String T_TITLE_SOLUONG = "Số lượng";
            String T_TITLE_TONG = "Tổng (kW)";

            Rect rectKW = new Rect();
            getPaintTextNormal().getTextBounds(T_KW, 0, T_KW.length(), rectKW);

            Rect rectV = new Rect();
            getPaintTextNormal().getTextBounds(T_V, 0, T_V.length(), rectV);

            Rect rectTCS = new Rect();
            getPaintTextNormal().getTextBounds(String.valueOf(EsspViewPTNActivity.TONG_CS), 0,
                    String.valueOf(EsspViewPTNActivity.TONG_CS).length(), rectTCS);

            Rect rectCAPDA = new Rect();
            getPaintTextNormal().getTextBounds(String.valueOf(EsspViewPTNActivity.CAP_DA), 0,
                    String.valueOf(EsspViewPTNActivity.CAP_DA).length(), rectCAPDA);

            Rect rectCSMAX = new Rect();
            getPaintTextNormal().getTextBounds(String.valueOf(EsspViewPTNActivity.CS_MAX), 0,
                    String.valueOf(EsspViewPTNActivity.CS_MAX).length(), rectCSMAX);

            Rect rectCSTB = new Rect();
            getPaintTextNormal().getTextBounds(String.valueOf(EsspViewPTNActivity.CS_TB), 0,
                    String.valueOf(EsspViewPTNActivity.CS_TB).length(), rectCSTB);

            Rect rectTHESODT = new Rect();
            getPaintTextNormal().getTextBounds(T_HESO_DT, 0, T_HESO_DT.length(), rectTHESODT);

            Rect rectTMUCDICH = new Rect();
            getPaintTextNormal().getTextBounds(T_TITLE_MUCDICH, 0, T_TITLE_MUCDICH.length(), rectTMUCDICH);
            Rect rectTTENTB = new Rect();
            getPaintTextNormal().getTextBounds(T_TITLE_TENTB, 0, T_TITLE_TENTB.length(), rectTTENTB);
            Rect rectTDIENAP = new Rect();
            getPaintTextNormal().getTextBounds(T_TITLE_DIENAP, 0, T_TITLE_DIENAP.length(), rectTDIENAP);
            Rect rectTCONGSUAT = new Rect();
            getPaintTextNormal().getTextBounds(T_TITLE_CONGSUAT, 0, T_TITLE_CONGSUAT.length(), rectTCONGSUAT);
            Rect rectTSOLUONG = new Rect();
            getPaintTextNormal().getTextBounds(T_TITLE_SOLUONG, 0, T_TITLE_SOLUONG.length(), rectTSOLUONG);
            Rect rectTTONG = new Rect();
            getPaintTextNormal().getTextBounds(T_TITLE_TONG, 0, T_TITLE_TONG.length(), rectTTONG);

            lineNumber++;//line 16
            canvas.drawText(MUC_1,
                    EsspViewPTNActivity.leftMargin,
                    EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextNormal());
            lineNumber++;//line 17
            canvas.drawText(T_TONG_CS,
                    EsspViewPTNActivity.leftMargin,
                    EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextNormal());
            canvas.drawText(T_KW,
                    EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.widthPage/2 - rectKW.width() - 20,
                    EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextNormal());
            canvas.drawText(String.valueOf(EsspViewPTNActivity.TONG_CS),
                    EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.widthPage/2 - rectKW.width() - rectTCS.width() - 30,
                    EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextBold());

            canvas.drawText(T_CAP_DA,
                    EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.widthPage/2,
                    EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextNormal());
            canvas.drawText(T_V,
                    EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.widthPage - rectKW.width(),
                    EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextNormal());
            canvas.drawText(String.valueOf((int)EsspViewPTNActivity.CAP_DA),
                    EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.widthPage - rectKW.width() - rectCAPDA.width() - 10,
                    EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextBold());
            lineNumber++;//line 18
            canvas.drawText(T_CS_MAX,
                    EsspViewPTNActivity.leftMargin,
                    EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextNormal());
            canvas.drawText(T_KW,
                    EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.widthPage/2 - rectKW.width() - 20,
                    EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextNormal());
            canvas.drawText(String.valueOf(EsspViewPTNActivity.CS_MAX),
                    EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.widthPage/2 - rectKW.width() - rectCSMAX.width() - 30,
                    EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextBold());

            canvas.drawText(T_CS_TB,
                    EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.widthPage/2,
                    EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextNormal());
            canvas.drawText("kW",
                    EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.widthPage - rectKW.width(),
                    EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextNormal());
            canvas.drawText(String.valueOf(EsspViewPTNActivity.CS_TB),
                    EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.widthPage - rectKW.width() - rectCSTB.width() - 10,
                    EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextBold());
            lineNumber++;//line 19
            canvas.drawText(T_HESO_DT,
                    EsspViewPTNActivity.leftMargin,
                    EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextNormal());
            canvas.drawText(String.valueOf(EsspViewPTNActivity.HESO_DT),
                    EsspViewPTNActivity.leftMargin + 4*rectTHESODT.width()/3 + 10,
                    EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextBold());
            lineNumber+=0.3f;
            canvas.drawLine(EsspViewPTNActivity.leftMargin, EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView + 5,
                    EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.widthPage,
                    EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView + 5, getPaint());
            EsspViewPTNActivity.topTable1 = EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView + 5;
            lineNumber+=0.2f;
            lineNumber++;//line 20
            float lineNumber2 = lineNumber;
            StringBuilder [] sbMUCDICHs = Common.splitMultiTextTimes(T_TITLE_MUCDICH, EsspViewPTNActivity.wCol1Table1 - 45,
                    rectTMUCDICH.width(), getPaintTextBold());
            if(EsspViewPTNActivity.numberRow <= sbMUCDICHs.length)
                EsspViewPTNActivity.numberRow = sbMUCDICHs.length;
            for (int i = 0; i < sbMUCDICHs.length; i++) {
                if(sbMUCDICHs[i].toString().trim().isEmpty()) {
                    EsspViewPTNActivity.numberRow--;
                }
            }
            for (int i = 0; i < sbMUCDICHs.length; i++) {
                if(i > 0)
                    lineNumber ++;
                canvas.drawText(EsspCommon.centerText(sbMUCDICHs[i].toString(), EsspViewPTNActivity.wCol1Table1, getPaintTextBold()),
                        EsspViewPTNActivity.leftMargin + 5,
                        EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextBold());
            }
            lineNumber = lineNumber2;
            StringBuilder [] sbTENTBs = Common.splitMultiTextTimes(T_TITLE_TENTB, EsspViewPTNActivity.wCol2Table1 - 45,
                    rectTTENTB.width(), getPaintTextBold());
            if(EsspViewPTNActivity.numberRow <= sbTENTBs.length)
                EsspViewPTNActivity.numberRow = sbTENTBs.length;
            for (int i = 0; i < sbTENTBs.length; i++) {
                if(sbTENTBs[i].toString().trim().isEmpty()) {
                    EsspViewPTNActivity.numberRow--;
                }
            }
            for (int i = 0; i < sbTENTBs.length; i++) {
                if(i > 0)
                    lineNumber ++;
                canvas.drawText(EsspCommon.centerText(sbTENTBs[i].toString(), EsspViewPTNActivity.wCol2Table1, getPaintTextBold()),
                        EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.wCol1Table1 + 5,
                        EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextBold());
            }
            lineNumber = lineNumber2;
            StringBuilder [] sbDIENAPs = Common.splitMultiTextTimes(T_TITLE_DIENAP, EsspViewPTNActivity.wCol3Table1 - 45,
                    rectTDIENAP.width(), getPaintTextBold());
            if(EsspViewPTNActivity.numberRow <= sbDIENAPs.length)
                EsspViewPTNActivity.numberRow = sbDIENAPs.length;
            for (int i = 0; i < sbDIENAPs.length; i++) {
                if(sbDIENAPs[i].toString().trim().isEmpty()) {
                    EsspViewPTNActivity.numberRow--;
                }
            }
            for (int i = 0; i < sbDIENAPs.length; i++) {
                if(i > 0)
                    lineNumber ++;
                canvas.drawText(EsspCommon.centerText(sbDIENAPs[i].toString(), EsspViewPTNActivity.wCol3Table1, getPaintTextBold()),
                        EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.wCol1Table1 + EsspViewPTNActivity.wCol2Table1 + 5,
                        EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextBold());
            }
            lineNumber = lineNumber2;
            StringBuilder [] sbCONGSUATs = Common.splitMultiTextTimes(T_TITLE_CONGSUAT, EsspViewPTNActivity.wCol4Table1 - 45,
                    rectTCONGSUAT.width(), getPaintTextBold());
            if(EsspViewPTNActivity.numberRow <= sbCONGSUATs.length)
                EsspViewPTNActivity.numberRow = sbCONGSUATs.length;
            for (int i = 0; i < sbCONGSUATs.length; i++) {
                if(sbCONGSUATs[i].toString().trim().isEmpty()) {
                    EsspViewPTNActivity.numberRow--;
                }
            }
            for (int i = 0; i < sbCONGSUATs.length; i++) {
                if(i > 0)
                    lineNumber ++;
                canvas.drawText(EsspCommon.centerText(sbCONGSUATs[i].toString(), EsspViewPTNActivity.wCol4Table1, getPaintTextBold()),
                        EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.wCol1Table1 + EsspViewPTNActivity.wCol2Table1
                                + EsspViewPTNActivity.wCol3Table1 + 5,
                        EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextBold());
            }
            lineNumber = lineNumber2;
            StringBuilder [] sbSOLUONGs = Common.splitMultiTextTimes(T_TITLE_SOLUONG, EsspViewPTNActivity.wCol5Table1 - 45,
                    rectTSOLUONG.width(), getPaintTextBold());
            if(EsspViewPTNActivity.numberRow <= sbSOLUONGs.length)
                EsspViewPTNActivity.numberRow = sbSOLUONGs.length;
            for (int i = 0; i < sbSOLUONGs.length; i++) {
                if(sbSOLUONGs[i].toString().trim().isEmpty()) {
                    EsspViewPTNActivity.numberRow--;
                }
            }
            for (int i = 0; i < sbSOLUONGs.length; i++) {
                if(i > 0)
                    lineNumber ++;
                canvas.drawText(EsspCommon.centerText(sbSOLUONGs[i].toString(), EsspViewPTNActivity.wCol5Table1, getPaintTextBold()),
                        EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.wCol1Table1 + EsspViewPTNActivity.wCol2Table1
                                + EsspViewPTNActivity.wCol3Table1+ EsspViewPTNActivity.wCol4Table1 + 5,
                        EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextBold());
            }
            lineNumber = lineNumber2;
            StringBuilder [] sbTONGs = Common.splitMultiTextTimes(T_TITLE_TONG, EsspViewPTNActivity.wCol6Table1 - 45,
                    rectTTONG.width(), getPaintTextBold());
            if(EsspViewPTNActivity.numberRow <= sbTONGs.length)
                EsspViewPTNActivity.numberRow = sbTONGs.length;
            for (int i = 0; i < sbTONGs.length; i++) {
                if(sbTONGs[i].toString().trim().isEmpty()) {
                    EsspViewPTNActivity.numberRow--;
                }
            }
            for (int i = 0; i < sbTONGs.length; i++) {
                if(i > 0)
                    lineNumber ++;
                canvas.drawText(EsspCommon.centerText(sbTONGs[i].toString(), EsspViewPTNActivity.wCol6Table1, getPaintTextBold()),
                        EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.wCol1Table1 + EsspViewPTNActivity.wCol2Table1
                                + EsspViewPTNActivity.wCol3Table1 + EsspViewPTNActivity.wCol4Table1 + EsspViewPTNActivity.wCol5Table1 + 5,
                        EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextBold());
            }
            lineNumber = lineNumber2;
            lineNumber += EsspViewPTNActivity.numberRow;
            lineNumber += 0.3f;
            canvas.drawLine(EsspViewPTNActivity.leftMargin, EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView + 5,
                    EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.widthPage,
                    EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView + 5, getPaint());
            lineNumber2 = lineNumber;
            double sum = 0d;
            if(EsspViewPTNActivity.cTB.moveToFirst()) {
                do {
                    lineNumber++;
                    lineNumber+=0.2;
                    canvas.drawText(EsspViewPTNActivity.cTB.getString(EsspViewPTNActivity.cTB.getColumnIndex("MUC_DICH")),
                            EsspViewPTNActivity.leftMargin + 5,
                            EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextNormal());
                    canvas.drawText(EsspViewPTNActivity.cTB.getString(EsspViewPTNActivity.cTB.getColumnIndex("LOAI_TBI")),
                            EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.wCol1Table1 + 5,
                            EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextNormal());
                    Rect rText = new Rect();
                    getPaintTextNormal().getTextBounds(EsspViewPTNActivity.cTB.getString(EsspViewPTNActivity.cTB.getColumnIndex("DIENAP_SUDUNG")),
                            0, EsspViewPTNActivity.cTB.getString(EsspViewPTNActivity.cTB.getColumnIndex("DIENAP_SUDUNG")).length(), rText);
                    canvas.drawText(EsspViewPTNActivity.cTB.getString(EsspViewPTNActivity.cTB.getColumnIndex("DIENAP_SUDUNG")),
                            EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.wCol1Table1 + EsspViewPTNActivity.wCol2Table1
                                    + EsspViewPTNActivity.wCol3Table1 - rText.width() - 5,
                            EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextNormal());
                    getPaintTextNormal().getTextBounds(EsspViewPTNActivity.cTB.getString(EsspViewPTNActivity.cTB.getColumnIndex("CONG_SUAT")),
                            0, EsspViewPTNActivity.cTB.getString(EsspViewPTNActivity.cTB.getColumnIndex("CONG_SUAT")).length(), rText);
                    canvas.drawText(EsspViewPTNActivity.cTB.getString(EsspViewPTNActivity.cTB.getColumnIndex("CONG_SUAT")),
                            EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.wCol1Table1 + EsspViewPTNActivity.wCol2Table1
                                    + EsspViewPTNActivity.wCol3Table1 + EsspViewPTNActivity.wCol4Table1 - rText.width() - 5,
                            EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextNormal());
                    getPaintTextNormal().getTextBounds(EsspViewPTNActivity.cTB.getString(EsspViewPTNActivity.cTB.getColumnIndex("SO_LUONG")),
                            0, EsspViewPTNActivity.cTB.getString(EsspViewPTNActivity.cTB.getColumnIndex("SO_LUONG")).length(), rText);
                    canvas.drawText(EsspViewPTNActivity.cTB.getString(EsspViewPTNActivity.cTB.getColumnIndex("SO_LUONG")),
                            EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.wCol1Table1 + EsspViewPTNActivity.wCol2Table1
                                    + EsspViewPTNActivity.wCol3Table1 + EsspViewPTNActivity.wCol4Table1
                                    + EsspViewPTNActivity.wCol5Table1 - rText.width() - 5,
                            EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextNormal());
                    double dTONG = Common.roundTwoDecimals((double)(EsspViewPTNActivity.cTB.getFloat(EsspViewPTNActivity.cTB.getColumnIndex("CONG_SUAT"))
                            * EsspViewPTNActivity.cTB.getFloat(EsspViewPTNActivity.cTB.getColumnIndex("SO_LUONG"))));
                    sum += dTONG;
                    String TONG = String.valueOf(dTONG);
                    getPaintTextNormal().getTextBounds(TONG, 0, TONG.length(), rText);
                    canvas.drawText(TONG, EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.wCol1Table1 + EsspViewPTNActivity.wCol2Table1
                                    + EsspViewPTNActivity.wCol3Table1 + EsspViewPTNActivity.wCol4Table1
                                    + EsspViewPTNActivity.wCol5Table1 + EsspViewPTNActivity.wCol6Table1 - rText.width() - 5,
                            EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextNormal());
                    lineNumber+=0.2;
                    canvas.drawLine(EsspViewPTNActivity.leftMargin, EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView + 5,
                            EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.widthPage,
                            EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView + 5, getPaint());
                } while (EsspViewPTNActivity.cTB.moveToNext());
                EsspViewPTNActivity.bottom1Table1 = EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView + 5;
                lineNumber++;//line 19
                lineNumber+=0.2;
                String sSum = String.valueOf(sum);
                Rect rSum = new Rect();
                getPaintTextNormal().getTextBounds(sSum, 0, sSum.length(), rSum);
                canvas.drawText("Tổng cộng", EsspViewPTNActivity.leftMargin + 5,
                        EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextBold());
                canvas.drawText(sSum, EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.widthPage - rSum.width() - 5,
                        EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView, getPaintTextNormal());
//                lineNumber+=0.2;
                canvas.drawLine(EsspViewPTNActivity.leftMargin, EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView + 5,
                        EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.widthPage,
                        EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView + 5, getPaint());
                EsspViewPTNActivity.bottom2Table1 = EsspViewPTNActivity.topMargin + lineNumber * EsspCommon.textSizeView + 5;
                canvas.drawLine(EsspViewPTNActivity.leftMargin, EsspViewPTNActivity.topTable1,
                        EsspViewPTNActivity.leftMargin, EsspViewPTNActivity.bottom2Table1, getPaint());
                canvas.drawLine(EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.wCol1Table1, EsspViewPTNActivity.topTable1,
                        EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.wCol1Table1, EsspViewPTNActivity.bottom1Table1, getPaint());
                canvas.drawLine(EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.wCol1Table1 + EsspViewPTNActivity.wCol2Table1,
                        EsspViewPTNActivity.topTable1, EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.wCol1Table1
                                + EsspViewPTNActivity.wCol2Table1, EsspViewPTNActivity.bottom1Table1, getPaint());
                canvas.drawLine(EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.wCol1Table1 + EsspViewPTNActivity.wCol2Table1
                                + EsspViewPTNActivity.wCol3Table1, EsspViewPTNActivity.topTable1, EsspViewPTNActivity.leftMargin
                                + EsspViewPTNActivity.wCol1Table1 + EsspViewPTNActivity.wCol2Table1 + EsspViewPTNActivity.wCol3Table1,
                        EsspViewPTNActivity.bottom1Table1, getPaint());
                canvas.drawLine(EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.wCol1Table1 + EsspViewPTNActivity.wCol2Table1
                                + EsspViewPTNActivity.wCol3Table1 + EsspViewPTNActivity.wCol4Table1, EsspViewPTNActivity.topTable1,
                        EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.wCol1Table1 + EsspViewPTNActivity.wCol2Table1 +
                                EsspViewPTNActivity.wCol3Table1 + EsspViewPTNActivity.wCol4Table1, EsspViewPTNActivity.bottom1Table1, getPaint());
                canvas.drawLine(EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.wCol1Table1 + EsspViewPTNActivity.wCol2Table1
                                + EsspViewPTNActivity.wCol3Table1 + EsspViewPTNActivity.wCol4Table1 + EsspViewPTNActivity.wCol5Table1,
                        EsspViewPTNActivity.topTable1, EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.wCol1Table1
                                + EsspViewPTNActivity.wCol2Table1 + EsspViewPTNActivity.wCol3Table1 + EsspViewPTNActivity.wCol4Table1
                                + EsspViewPTNActivity.wCol5Table1, EsspViewPTNActivity.bottom2Table1, getPaint());
                canvas.drawLine(EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.widthPage, EsspViewPTNActivity.topTable1,
                        EsspViewPTNActivity.leftMargin + EsspViewPTNActivity.widthPage, EsspViewPTNActivity.bottom2Table1, getPaint());
            }
        } catch (Exception ex) {
            Toast.makeText(EsspPTNView.this.getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
