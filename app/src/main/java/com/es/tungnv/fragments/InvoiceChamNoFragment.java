package com.es.tungnv.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;

import com.es.tungnv.adapters.InvoiceDialogPosKHAdapter;
import com.es.tungnv.adapters.InvoicePosKHAdapter;
import com.es.tungnv.db.InvoiceSQLiteConnection;
import com.es.tungnv.entity.InvoicePosKHEntity;
import com.es.tungnv.utils.Common;
import com.es.tungnv.utils.EPrinter;
import com.es.tungnv.utils.IntentIntegrator;
import com.es.tungnv.utils.IntentResult;
import com.es.tungnv.utils.InvoiceCameraPreview;
import com.es.tungnv.utils.InvoiceCommon;
import com.es.tungnv.utils.InvoiceConstantVariables;
import com.es.tungnv.utils.InvoicePrinter;
import com.es.tungnv.views.R;
import com.es.tungnv.webservice.InvoiceAsyncCallWS;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by TUNGNV on 2/24/2016.
 */
public class InvoiceChamNoFragment extends Fragment implements View.OnClickListener, Runnable{

    private View rootView;
    private EditText etSearch;
    private ImageButton ibSearch, ibScanner, ibChamNo, ibClear;
    private Spinner spLoaiHD, spSo;
    private RecyclerView rvKH;
    private TextView tvTongHD, tvTongTien;
    private FrameLayout flBarcode;

    private RecyclerView.LayoutManager layoutManager;
    private InvoicePosKHAdapter posKHAdapter;
    private List<InvoicePosKHEntity> listData;
    private List<InvoicePosKHEntity> listChamNo;
    private List<Cursor> listCursor;

    public static InvoiceSQLiteConnection connection;
    private static ProgressDialog progressDialog;
    private InvoiceAsyncCallWS asyncCallWS;

    private ArrayList<JSONObject> arrData;
    private BluetoothAdapter bluetoothAdapter;
    public static ArrayAdapter<String> btArrayAdapter;
    private String BluetoothMac = "";
    private InvoiceCommon iCommon = new InvoiceCommon();

    private Camera mCamera;
    private InvoiceCameraPreview mPreview;
    private Handler autoFocusHandler;

    ImageScanner scanner;

    private boolean barcodeScanned = false;
    private boolean previewing = true;
    public MediaPlayer mp;


    static {
        System.loadLibrary("iconv");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            if(InvoiceCommon.getVERSION().equals("YB")){
                rootView = inflater.inflate(R.layout.invoice_fragment_chamno_yb, null);
            } else {
                rootView = inflater.inflate(R.layout.invoice_fragment_chamno, null);
            }
            autoFocusHandler = new Handler();
            mCamera = getCameraInstance();
            scanner = new ImageScanner();
            scanner.setConfig(0, Config.X_DENSITY, 3);
            scanner.setConfig(0, Config.Y_DENSITY, 3);

            mPreview = new InvoiceCameraPreview(this.getActivity(), mCamera, previewCb, autoFocusCB);
            initComponent(rootView);
            flBarcode.addView(mPreview);

            connection = new InvoiceSQLiteConnection(this.getActivity());
            asyncCallWS = new InvoiceAsyncCallWS();
            setDataOnSpinner();
        } catch(Exception ex) {
            Toast.makeText(getActivity().getApplicationContext(), "Error create view: " + ex.toString(), Toast.LENGTH_LONG).show();
        }
        return rootView;
    }

    @Override
    public void onResume() {
        try {
            super.onResume();
            if (etSearch.getText().toString().trim().equals(""))
                setDataOnRecyclerView(0, "Tất cả sổ");
        } catch(Exception ex) {
            Toast.makeText(getActivity().getApplicationContext(), "Error onResume: " + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void initComponent(View rootView){
        etSearch = (EditText) rootView.findViewById(R.id.invoice_fragment_chamno_et_search);
        if(!InvoiceCommon.getVERSION().equals("YB")) {
            ibSearch = (ImageButton) rootView.findViewById(R.id.invoice_fragment_chamno_ib_search);
            ibScanner = (ImageButton) rootView.findViewById(R.id.invoice_fragment_chamno_ib_scanner);
        } else {
            flBarcode = (FrameLayout) rootView.findViewById(R.id.invoice_fragment_chamno_fl_barcode);
        }
        ibChamNo = (ImageButton) rootView.findViewById(R.id.invoice_fragment_chamno_ib_cham);
        ibClear = (ImageButton) rootView.findViewById(R.id.invoice_fragment_chamno_ib_clear);
        spLoaiHD = (Spinner) rootView.findViewById(R.id.invoice_fragment_chamno_sp_loai_hd);
        spSo = (Spinner) rootView.findViewById(R.id.invoice_fragment_chamno_sp_so);
        rvKH = (RecyclerView) rootView.findViewById(R.id.invoice_fragment_chamno_rv_kh);
        tvTongHD = (TextView) rootView.findViewById(R.id.invoice_fragment_chamno_tv_hd);
        tvTongTien = (TextView) rootView.findViewById(R.id.invoice_fragment_chamno_tv_tien);

        listData = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this.getActivity());
        rvKH.setHasFixedSize(true);
        rvKH.setLayoutManager(layoutManager);

        if(!InvoiceCommon.getVERSION().equals("YB")) {
            ibSearch.setOnClickListener(this);
            ibScanner.setOnClickListener(this);
        }
        ibChamNo.setOnClickListener(this);
        ibClear.setOnClickListener(this);

        etSearch.post(new Runnable() {
            @Override
            public void run() {
                etSearch.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        try {
                            List<InvoicePosKHEntity> data = new ArrayList<InvoicePosKHEntity>();
                            String query = Common.removeAccent(s.toString().trim().toLowerCase());
                            for (InvoicePosKHEntity entity : listData) {
                                if (Common.removeAccent(entity.getSO().toLowerCase()).contains(query)
                                        || Common.removeAccent(entity.getSTT().toLowerCase()).contains(query)
                                        || Common.removeAccent(entity.getMA().toLowerCase()).contains(query)
                                        || Common.removeAccent(entity.getTEN_KH().toLowerCase()).contains(query)
                                        || Common.removeAccent(entity.getDIA_CHI().toLowerCase()).contains(query)
                                        || Common.removeAccent(connection.getIDHdon(entity.getMA()).toLowerCase()).contains(query)) {
                                    data.add(entity);
                                }
                            }
                            posKHAdapter.updateList(data);

//                            if(s.toString().length() == 0){
//                                reloadBarcode();
//                            }
                        } catch(Exception ex) {
                            Toast.makeText(InvoiceChamNoFragment.this.getActivity(), "Lỗi tìm kiếm:\n" + ex.toString(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.invoice_fragment_chamno_ib_search:
                IntentIntegrator integrator = new IntentIntegrator(InvoiceChamNoFragment.this.getActivity());
                integrator.initiateScan(InvoiceChamNoFragment.this);
                break;
            case R.id.invoice_fragment_chamno_ib_scanner:
                showBlueToothSettingBarcode();
                break;
            case R.id.invoice_fragment_chamno_ib_cham:
                if(posKHAdapter.listChoose.size() > 0 && connection.getTrangThaiByMaKH(posKHAdapter.listChoose.get(0)) < 6) {
                    showDialogChamNo();
                }
                break;

            case R.id.invoice_fragment_chamno_ib_clear:
                etSearch.setText("");
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        try {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if (scanResult != null) {
                String barcode = scanResult.getContents();
                String type = scanResult.getFormatName();
                etSearch.setText(barcode);
            }
        } catch(Exception ex) {
            Common.showAlertDialogGreen(InvoiceChamNoFragment.this.getActivity(), "Lỗi", Color.RED, "Lỗi quét mã vạch\n" + ex.toString(), Color.WHITE, "OK", Color.WHITE);
        }
    }

    @Override
    public void onDestroy() {
        try {
            super.onDestroy();
            InvoiceChamNoFragment.this.getActivity().unregisterReceiver(ActionFoundReceiver);
        } catch(Exception ex) {
            //Common.showAlertDialogGreen(InvoiceChamNoFragment.this.getActivity(), "Lỗi", Color.RED, "onDestroy error\n" + ex.toString(), Color.WHITE, "OK", Color.WHITE);
        }
    }

    private void reloadBarcode() {
        if (barcodeScanned) {
            barcodeScanned = false;
            //etSearch.setText("");
            mCamera.setPreviewCallback(previewCb);
            mCamera.startPreview();
            previewing = true;
            mCamera.autoFocus(autoFocusCB);
        }
    }

    private void showDialogChamNo(){
        try{
            final Dialog dialog = new Dialog(this.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.invoice_dialog_cham_no);
            dialog.getWindow().setLayout(android.app.ActionBar.LayoutParams.MATCH_PARENT, android.app.ActionBar.LayoutParams.MATCH_PARENT);

            RecyclerView rvChamNo = (RecyclerView) dialog.findViewById(R.id.invoice_dialog_chamno_rv_kh);
            final Button btInBienNhan = (Button) dialog.findViewById(R.id.invoice_dialog_chamno_bt_in_biennhan);
            final Button btInThongBao = (Button) dialog.findViewById(R.id.invoice_dialog_chamno_bt_in_thongbao);
            final Button btHuyCham = (Button) dialog.findViewById(R.id.invoice_dialog_chamno_bt_huycham);
            TextView tvTongTienNo = (TextView) dialog.findViewById(R.id.invoice_dialog_cham_no_tv_tienno);

            if(InvoiceCommon.cfgInfo.getVERSION().equals("TB")){
                btInThongBao.setVisibility(View.GONE);
                btInBienNhan.setText("Chấm nợ");
            } else {
                btInThongBao.setVisibility(View.VISIBLE);
                btInBienNhan.setText("In biên nhận");
            }

            LinearLayoutManager dialoglayoutManager = new LinearLayoutManager(this.getActivity());
            rvChamNo.setHasFixedSize(true);
            rvChamNo.setLayoutManager(dialoglayoutManager);

            if(connection.getTinhTrangByMaKH(posKHAdapter.listChoose.get(0)) == 0){
                btInBienNhan.setEnabled(true);
                btInThongBao.setEnabled(true);
                btHuyCham.setEnabled(false);
            } else {
                btInBienNhan.setEnabled(false);
                btInThongBao.setEnabled(false);
                btHuyCham.setEnabled(true);
            }

            listChamNo = new ArrayList<>();
            listCursor = new ArrayList<>();
            double tong_tien_no = 0d;
            for (String ma_kh : posKHAdapter.listChoose){
                Cursor c = connection.getAllDataKHACHHANGByMaKH(ma_kh);
                if(c.moveToFirst()){
                    InvoicePosKHEntity entity = new InvoicePosKHEntity();
                    entity.setSTT(c.getString(c.getColumnIndex("STT")).equals("null")?"":c.getString(c.getColumnIndex("STT")));
                    entity.setSO(c.getString(c.getColumnIndex("MA_SOGCS")).equals("null") ? "" : c.getString(c.getColumnIndex("MA_SOGCS")));
                    entity.setTEN_KH(c.getString(c.getColumnIndex("TEN_KHANG")).equals("null") ? "" : c.getString(c.getColumnIndex("TEN_KHANG")));
                    entity.setDIEN_THOAI(c.getString(c.getColumnIndex("DTHOAI_KHANG")).equals("null") ? "" : c.getString(c.getColumnIndex("DTHOAI_KHANG")));
                    entity.setDIA_CHI(c.getString(c.getColumnIndex("DCHI_KHANG")).equals("null") ? "" : c.getString(c.getColumnIndex("DCHI_KHANG")));
                    entity.setSO_TIEN(c.getString(c.getColumnIndex("TIEN_NO")).equals("null") ? "" : c.getString(c.getColumnIndex("TIEN_NO")));
                    entity.setNGAY_NOP(c.getString(c.getColumnIndex("NGAY_NOP")).equals("null") ? "" : c.getString(c.getColumnIndex("NGAY_NOP")));
                    entity.setMA(c.getString(c.getColumnIndex("MA_KHANG")).equals("null") ? "" : c.getString(c.getColumnIndex("MA_KHANG")));
                    entity.setSO_CTO(c.getString(c.getColumnIndex("SO_CTO")).equals("null") ? "" : c.getString(c.getColumnIndex("SO_CTO")));
                    entity.setKY(c.getString(c.getColumnIndex("KY_PS")).equals("null") ? "" : c.getString(c.getColumnIndex("KY_PS")));
                    entity.setTHANG(c.getString(c.getColumnIndex("THANG_PS")).equals("null") ? "" : c.getString(c.getColumnIndex("THANG_PS")));
                    entity.setNAM(c.getString(c.getColumnIndex("NAM_PS")).equals("null") ? "" : c.getString(c.getColumnIndex("NAM_PS")));
                    listChamNo.add(entity);
                    tong_tien_no += (c.getDouble(c.getColumnIndex("TIEN_NO")) + c.getDouble(c.getColumnIndex("THUE_NO")));
                    listCursor.add(c);
                }
            }

            InvoiceDialogPosKHAdapter adapterChamNo = new InvoiceDialogPosKHAdapter(listChamNo);
            rvChamNo.setAdapter(adapterChamNo);
            tvTongTienNo.setText("" + Common.formatMoney(new BigDecimal("" + tong_tien_no).toString()));

            btInBienNhan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (InvoiceCommon.cfgInfo.getVERSION().equals("TB")) {
                        InBienNhan(btHuyCham, btInBienNhan, btInThongBao);
                    } else if (InvoiceCommon.cfgInfo.getVERSION().equals("YB")) {
                        showDialogIn(btHuyCham, btInBienNhan, btInThongBao, EPrinter.BIEN_NHAN, listCursor);
                    }
                }
            });

            btInThongBao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogIn(btHuyCham, btInBienNhan, btInThongBao, EPrinter.THONG_BAO, listCursor);
                }
            });

            btHuyCham.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v){
                    HuyCham(btHuyCham, btInBienNhan, btInThongBao);
                }
            });

            dialog.setOnCancelListener(new DialogInterface.OnCancelListener()

            {
                @Override
                public void onCancel(DialogInterface dialog) {
                    setDataOnRecyclerView(getTrangThai(spLoaiHD.getSelectedItemPosition()), spSo.getSelectedItem().toString());
                    reloadBarcode();
                }
            });

                dialog.show();
        } catch(Exception ex) {
            Toast.makeText(getActivity().getApplicationContext(), "Lỗi hiển thị hộp thoại chấm nợ:\n" + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void showDialogIn(final Button btHuyCham,final Button btInBienNhan,final Button btInThongBao, final EPrinter ePrinter, final List<Cursor> listCursor){
        try{
            final Dialog dialog = new Dialog(this.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.invoice_dialog_printer);
            dialog.getWindow().setLayout(android.app.ActionBar.LayoutParams.MATCH_PARENT, android.app.ActionBar.LayoutParams.MATCH_PARENT);

            ListView lvThietBi = (ListView) dialog.findViewById(R.id.invoice_dialog_printer_lv_thietbi);
            final Button btIn = (Button) dialog.findViewById(R.id.invoice_dialog_printer_bt_in);
            final Button btTim = (Button) dialog.findViewById(R.id.invoice_dialog_printer_bt_tim);
            final TextView tvTenThietBi = (TextView) dialog.findViewById(R.id.invoice_dialog_printer_tv_ten_thietbi);

            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            btArrayAdapter = new ArrayAdapter<String>(InvoiceChamNoFragment.this.getActivity(), R.layout.invoice_frament_chamno_listitem);//android.R.layout.simple_list_item_1
            lvThietBi.setAdapter(btArrayAdapter);

            CheckBlueToothState(tvTenThietBi, btTim);

            InvoiceChamNoFragment.this.getActivity().registerReceiver(ActionFoundReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
            if(!BluetoothMac.equals("")){
                tvTenThietBi.setText("Đã kết nối:\n" + BluetoothMac);
            }

            lvThietBi.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String addMAC = (String) parent.getItemAtPosition(position);
                    String[] addMACs = addMAC.split("\n");
                    BluetoothMac = addMACs[1];
                    tvTenThietBi.setText("Đã kết nối:\n" + BluetoothMac);
                }
            });

            btTim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btArrayAdapter.clear();
                    bluetoothAdapter.startDiscovery();
                }
            });

            btIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (InvoiceCommon.printer == null) {
                            InvoiceCommon.printer = new InvoicePrinter(BluetoothMac);
                        } else {
                            InvoiceCommon.printer.MAC = BluetoothMac;
                        }
                        String strPrinter = "";
                        if (ePrinter == EPrinter.BIEN_NHAN)
                            strPrinter = strPrinterBienNhan(listCursor);
                        else {
                            strPrinter = strPrinterThongBao(listCursor);
                        }
                        InvoiceCommon.printer.Print(strPrinter);
                        if (ePrinter == EPrinter.BIEN_NHAN)
                            InBienNhan(btHuyCham, btInBienNhan, btInThongBao);
                        dialog.dismiss();
                    } catch (Exception ex) {
                        Common.showAlertDialogGreen(InvoiceChamNoFragment.this.getActivity(), "Lỗi", Color.RED, "Lỗi in\n" + ex.toString(), Color.WHITE, "OK", Color.WHITE);
                    }
                }
            });

            dialog.show();
        } catch(Exception ex) {
            Toast.makeText(getActivity().getApplicationContext(), "Lỗi hiển thị hộp thoại chấm nợ:\n" + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private String strPrinterThongBao(List<Cursor> listCursor)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();

        String MA_TNGAN = InvoiceCommon.getUSERNAME();
        Cursor cTN = connection.getDataTHUNGANByMa(MA_TNGAN);

        String TEN_TNGAN = "";
        String DTHOAI_TNGAN = "";
        String DCHI_DVIQLY = "";
        if(cTN.moveToFirst()){
            TEN_TNGAN = cTN.getString(cTN.getColumnIndex("TEN_TNGAN"));
            DTHOAI_TNGAN = cTN.getString(cTN.getColumnIndex("DTHOAI_TNGAN"));
            DCHI_DVIQLY = cTN.getString(cTN.getColumnIndex("DCHI_DVIQLY"));
        }
        String NGAY_TBAO = sdf.format(cal.getTime());

        StringBuilder str = new StringBuilder();
        if(listCursor.size() > 0) {
            for (Cursor c : listCursor) {
                if(c.moveToFirst()) {
                    String MA_DVIQLY = connection.getMaDVIByUser(InvoiceCommon.getUSERNAME());
                    String TEN_DVIQLY = getTenDviByMaDvi(MA_DVIQLY);
                    String NGAY_DKY = c.getString(c.getColumnIndex("NGAY_DKY"));
                    String NGAY_CKY = c.getString(c.getColumnIndex("NGAY_CKY"));
                    String TEN_KHANG = c.getString(c.getColumnIndex("TEN_KHANG"));
                    String DCHI_KHANG = c.getString(c.getColumnIndex("DCHI_KHANG"));
                    String MA_KHANG = c.getString(c.getColumnIndex("MA_KHANG"));
                    String DTHOAI_KHANG = c.getString(c.getColumnIndex("DTHOAI_KHANG"));
                    String MA_LT = c.getString(c.getColumnIndex("MA_SOGCS")) + " - " + c.getString(c.getColumnIndex("STT"));
                    String SO_HO = c.getString(c.getColumnIndex("SO_HO"));
                    String SO_CTO = c.getString(c.getColumnIndex("SO_CTO"));
                    String CS_CKY = c.getString(c.getColumnIndex("CS_CKY"));
                    String CS_DKY = c.getString(c.getColumnIndex("CS_DKY"));
                    String CTIET_DNTT = c.getString(c.getColumnIndex("CTIET_DNTT"));
                    String CTIET_GIA = c.getString(c.getColumnIndex("CTIET_GIA"));
                    String CTIET_TIEN = c.getString(c.getColumnIndex("CTIET_TIEN"));
                    String DIEN_TTHU = c.getString(c.getColumnIndex("DIEN_TTHU"));
                    String TIEN_NO = c.getString(c.getColumnIndex("TIEN_NO"));
                    String THUE_NO = c.getString(c.getColumnIndex("THUE_NO"));
                    String TONG_TIEN = "" + (Double.parseDouble(TIEN_NO) + Double.parseDouble(THUE_NO));

                    String DTHOAI_GDKH = c.getString(c.getColumnIndex("DIEN_THOAI"));
                    String DTHOAI_SUACHUA = c.getString(c.getColumnIndex("DTHOAI_NONG"));

                    String LAN_IN = "" + (c.getInt(c.getColumnIndex("SOLAN_TBAO")) + 1);

                    str.append(TEN_DVIQLY + "\n\n");
                    str.append("  GIAY THONG BAO TIEN DIEN" + "\n");
                    str.append(" (Không có giá trị thanh toán)" + "\n\n");
                    str.append("(Kỳ GCS từ " + NGAY_DKY + " đến " + NGAY_CKY + ")\n");
                    str.append("Tên KH: " + TEN_KHANG + "\n");
                    str.append("Địa chỉ: " + DCHI_KHANG + "\n");
                    str.append("Mã KH: " + MA_KHANG + "\n");
                    str.append("ĐTKH: " + DTHOAI_KHANG + "\n");
                    str.append("Mã LT: " + MA_LT + "   Số hộ: " + SO_HO + "\n");
                    str.append("Số công tơ: " + SO_CTO + "\n");
                    str.append("CSCK: " + CS_CKY + "   CSDK: " + CS_DKY + "\n");
                    str.append("ĐNTT  | Đơn giá    | Thành tiền\n");
                    String[] sDNTT = CTIET_DNTT.split(";");
                    String[] sGIA = CTIET_GIA.split(";");
                    String[] sTIEN = CTIET_TIEN.split(";");
                    for (int i = 0; i < sDNTT.length; i++) {
                        str.append(sDNTT[i].toString() + "       " + sGIA[i].toString() + "         " + Common.formatMoney(new BigDecimal(sTIEN[i].toString()).toString()) + "\n");
                    }
                    str.append("------------------------------" + "\n");
                    str.append(DIEN_TTHU + " kWh            " + Common.formatMoney(new BigDecimal(TIEN_NO).toString()) + "\n");
                    str.append("Thuế GTGT             " + Common.formatMoney(new BigDecimal(THUE_NO).toString()) + "\n");
                    str.append("------------------------------" + "\n");
                    str.append("Tổng cộng            " + Common.formatMoney(new BigDecimal(TONG_TIEN).toString()) + "\n\n");
                    str.append("Kính đề nghị qúy khách hàng đến thanh toán tiền điền tại: " + DCHI_DVIQLY + "\n");
                    str.append("TNV: " + TEN_TNGAN + "\n");
                    str.append("ĐT: " + DTHOAI_TNGAN + "\n");
                    str.append("ĐT GDKH: " + DTHOAI_GDKH + "\n");
                    str.append("ĐT Sửa chữa: " + DTHOAI_SUACHUA + "\n");
                    str.append("Ngày TB: " + NGAY_TBAO + "\n");
                    str.append("  ------- In lần " + LAN_IN + " -------  " + "\n");
                    str.append("\n");
                    str.append("\n");

                    connection.updateSoLanTB(MA_KHANG, LAN_IN);
                }
            }
        }
        return str.toString();
    }

    private String getTenDviByMaDvi(String MA_DVIQLY){
        try{
            for(int i = 0; i < InvoiceConstantVariables.MA_DVIQLY_YB.length; i++){
                String ma = InvoiceConstantVariables.MA_DVIQLY_YB[i].toString().trim();
                if(MA_DVIQLY.equals(ma)){
                    return InvoiceConstantVariables.TEN_DVIQLY_YB[i].toString().trim();
                }
            }
            return "";
        } catch(Exception ex) {
            ex.toString();
            return "Lỗi";
        }
    }

    private String strPrinterBienNhan(List<Cursor> listCursor)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Calendar cal = Calendar.getInstance();

        String MA_TNGAN = InvoiceCommon.getUSERNAME();
        Cursor cTN = connection.getDataTHUNGANByMa(MA_TNGAN);

        String TEN_TNGAN = "";
        String DTHOAI_TNGAN = "";
        String DCHI_DVIQLY = "";
        if(cTN.moveToFirst()){
            TEN_TNGAN = cTN.getString(cTN.getColumnIndex("TEN_TNGAN"));
            DTHOAI_TNGAN = cTN.getString(cTN.getColumnIndex("DTHOAI_TNGAN"));
            DCHI_DVIQLY = cTN.getString(cTN.getColumnIndex("DCHI_DVIQLY"));
        }
        String NGAY_TT = sdf.format(cal.getTime());

        StringBuilder str = new StringBuilder();
        if(listCursor.size() > 0) {
            for (Cursor c : listCursor) {
                if(c.moveToFirst()) {
                    String MA_DVIQLY = connection.getMaDVIByUser(InvoiceCommon.getUSERNAME());
                    String TEN_DVIQLY = getTenDviByMaDvi(MA_DVIQLY);
                    String NGAY_DKY = c.getString(c.getColumnIndex("NGAY_DKY"));
                    String NGAY_CKY = c.getString(c.getColumnIndex("NGAY_CKY"));
                    String TEN_KHANG = c.getString(c.getColumnIndex("TEN_KHANG"));
                    String DCHI_KHANG = c.getString(c.getColumnIndex("DCHI_KHANG"));
                    String MA_KHANG = c.getString(c.getColumnIndex("MA_KHANG"));
                    String DTHOAI_KHANG = c.getString(c.getColumnIndex("DTHOAI_KHANG"));
                    String MAUSO = "";
                    String SO_SERY = c.getString(c.getColumnIndex("SO_SERY"));
                    String ID_HDON = c.getString(c.getColumnIndex("ID_HDON"));
                    String MA_LT = c.getString(c.getColumnIndex("MA_SOGCS")) + " - " + c.getString(c.getColumnIndex("STT"));
                    String SO_HO = c.getString(c.getColumnIndex("SO_HO"));
                    String SO_CTO = c.getString(c.getColumnIndex("SO_CTO"));
                    String CS_CKY = c.getString(c.getColumnIndex("CS_CKY"));
                    String CS_DKY = c.getString(c.getColumnIndex("CS_DKY"));
                    String CTIET_DNTT = c.getString(c.getColumnIndex("CTIET_DNTT"));
                    String CTIET_GIA = c.getString(c.getColumnIndex("CTIET_GIA"));
                    String CTIET_TIEN = c.getString(c.getColumnIndex("CTIET_TIEN"));
                    String DIEN_TTHU = c.getString(c.getColumnIndex("DIEN_TTHU"));
                    String TIEN_NO = c.getString(c.getColumnIndex("TIEN_NO"));
                    String THUE_NO = c.getString(c.getColumnIndex("THUE_NO"));
                    String TONG_TIEN = "" + (Double.parseDouble(TIEN_NO) + Double.parseDouble(THUE_NO));

                    String DTHOAI_GDKH = c.getString(c.getColumnIndex("DIEN_THOAI"));
                    String DTHOAI_SUACHUA = c.getString(c.getColumnIndex("DTHOAI_NONG"));

                    String LAN_IN = "" + (c.getInt(c.getColumnIndex("SOLAN_BNHAN")) + 1);

                    str.append(TEN_DVIQLY + "\n\n");
                    str.append(" BIEN NHAN THANH TOAN TIEN DIEN" + "\n\n");
                    str.append("(Kỳ GCS từ " + NGAY_DKY + " đến " + NGAY_CKY + ")\n");
                    str.append("Tên KH: " + TEN_KHANG + "\n");
                    str.append("Địa chỉ: " + DCHI_KHANG + "\n");
                    str.append("Mã KH: " + MA_KHANG + "\n");
                    str.append("ĐTKH: " + DTHOAI_KHANG + "\n");
                    str.append("Mẫu số HĐĐT: " + MAUSO + "\n");
                    str.append("Sery HĐĐT: " + SO_SERY + "\n");
                    str.append("ID hóa đơn: " + ID_HDON + "\n");
                    str.append("Mã LT: " + MA_LT + "   Số hộ: " + SO_HO + "\n");
                    str.append("Số công tơ: " + SO_CTO + "\n");
                    str.append("CSCK: " + CS_CKY + "   CSĐK: " + CS_DKY + "\n");
                    str.append("ĐNTT  | Đơn giá    | Thành tiền\n");
                    String[] sDNTT = CTIET_DNTT.split(";");
                    String[] sGIA = CTIET_GIA.split(";");
                    String[] sTIEN = CTIET_TIEN.split(";");
                    for (int i = 0; i < sDNTT.length; i++) {
                        str.append(sDNTT[i].toString() + "       " + sGIA[i].toString() + "         " + Common.formatMoney(new BigDecimal(sTIEN[i].toString()).toString()) + "\n");
                    }
                    str.append("------------------------------" + "\n");
                    str.append(DIEN_TTHU + " kWh            " + Common.formatMoney(new BigDecimal(TIEN_NO).toString()) + "\n");
                    str.append("Thuế GTGT             " + Common.formatMoney(new BigDecimal(THUE_NO).toString()) + "\n");
                    str.append("------------------------------" + "\n");
                    str.append("Tổng cộng            " + Common.formatMoney(new BigDecimal(TONG_TIEN).toString()) + "\n\n");
                    str.append("Ngày TT: " + NGAY_TT + "\n");
                    str.append("TNV: " + TEN_TNGAN + "\n");
                    str.append("ĐT: " + DTHOAI_TNGAN + "\n");
                    str.append("ĐT GDKH: " + DTHOAI_GDKH + "\n");
                    str.append("ĐT Sửa chữa: " + DTHOAI_SUACHUA + "\n");
                    str.append("Lưu ý: Biên nhận này xác thực\nkhách hàng đã thanh toán tiền\nđiện. Để nhận hóa đơn, quý\nkhách vui lòng truy cập trang\nWeb http://cskh.com.vn để lấy\nhóa đơn\n");
                    str.append("  ------- In lần " + LAN_IN + " -------  " + "\n");
                    str.append("\n");
                    str.append("\n");
                }
            }
        }
        return str.toString();
    }

    private final BroadcastReceiver ActionFoundReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                btArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                btArrayAdapter.notifyDataSetChanged();
            }
        }
    };

    private void CheckBlueToothState(TextView stateBluetooth, Button btnScanDevice) {
        if (InvoiceCommon.printer.mBTAdapter == null) {
            stateBluetooth.setText("Bluetooth không hỗ trợ");
        } else {
            if (InvoiceCommon.printer.mBTAdapter.isEnabled()) {
                if (InvoiceCommon.printer.mBTAdapter.isDiscovering()) {
                    stateBluetooth.setText("Đang tìm thiết bị...");
                    btnScanDevice.setEnabled(false);
                } else {
                    stateBluetooth.setText("Bluetooth đã bật.");
                    btnScanDevice.setEnabled(true);
                }
            } else {
                stateBluetooth.setText("Bluetooth đã tắt !");
                Intent enableBtIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, InvoicePrinter.REQUEST_ENABLE_BT);
            }
        }
    }

    private void InBienNhan(Button btHuyCham, Button btInBienNhan, Button btInThongBao){
        try {
            Date toDay = Calendar.getInstance().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            String MA_DVIQLY = connection.getMaDVIByUser(InvoiceCommon.getUSERNAME());
            String MA_TNGAN = InvoiceCommon.getUSERNAME();
            String ID_HDON = "";
            String NGAY_NOP = sdf.format(toDay);
            int SOLAN_BNHAN = 0;
            int SOLAN_TBAO = 0;
            int TINH_TRANG = 1;// đã chấm
            int TRANG_THAI = 2;// Đã chấm offline
            String TT_HDON = "";

            StringBuilder sbMaKH = new StringBuilder();
            JSONObject jsonObj = null;
            JSONArray jsonArr = new JSONArray();
            ArrayList<String> arrMaKH = new ArrayList<String>();
            int countResult = 0;
            boolean isNetworkActive = Common.isNetworkOnline(InvoiceChamNoFragment.this.getActivity());
            for (InvoicePosKHEntity entity : listChamNo) {
                String MA_KHANG = entity.getMA();
                ID_HDON = connection.getIDHdon(MA_KHANG);
                SOLAN_BNHAN = connection.getSoLanBNhan(MA_KHANG) + 1;
                SOLAN_TBAO = connection.getSoLanTBao(MA_KHANG);
                if (!connection.checkChamHTK(MA_KHANG)) {
                    if (connection.updateChamNo(entity.getMA(), NGAY_NOP, SOLAN_BNHAN, SOLAN_TBAO, TINH_TRANG, TRANG_THAI) != -1) {
                        countResult++;
                        if (isNetworkActive) {
                            jsonObj = new JSONObject();
                            jsonObj.put("ID_HDON", ID_HDON);
                            jsonObj.put("NGAY_NOP", NGAY_NOP);
                            jsonObj.put("LOAI_TBAO", "1");
                            jsonObj.put("SOLAN_BNHAN", SOLAN_BNHAN);
                            jsonObj.put("SOLAN_TBAO", SOLAN_TBAO);
                            jsonArr.put(jsonObj);
                        }
                    }
                } else {
                    sbMaKH.append(connection.getTenKHByMa(entity.getMA()) + ", ");
                }
            }

            if (countResult > 0) {
                btHuyCham.setEnabled(true);
                btInBienNhan.setEnabled(false);
                btInThongBao.setEnabled(false);
            }

            if (isNetworkActive) {
                TT_HDON = jsonArr.toString();
                String result = asyncCallWS.WS_UPDATE_CHAM_NO_CALL(MA_DVIQLY, MA_TNGAN, TT_HDON);
                if(result.contains("ERROR")){
                    Common.showAlertDialogGreen(InvoiceChamNoFragment.this.getActivity(), "Lỗi server", Color.WHITE, result, Color.WHITE, "OK", Color.WHITE);
                } else {
                    JSONArray arr = new JSONArray(result);
                    if (arr.length() > 0) {
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject jO = arr.getJSONObject(i);
                            if (jO.getString("KET_QUA").equals("6")) {
                                connection.updateTrangThaiByID(jO.getString("ID_HDON"), 6);
                                btHuyCham.setEnabled(false);
                                btInBienNhan.setEnabled(false);
                                btInThongBao.setEnabled(false);
                            } else if (jO.getString("KET_QUA").equals("7")) {
                                connection.updateTrangThaiByID(jO.getString("ID_HDON"), 7);
                                btHuyCham.setEnabled(false);
                                btInBienNhan.setEnabled(false);
                                btInThongBao.setEnabled(false);
                            } else {
                                connection.updateTrangThaiByID(jO.getString("ID_HDON"), 3);
                                btHuyCham.setEnabled(true);
                                btInBienNhan.setEnabled(false);
                                btInThongBao.setEnabled(false);
                            }
                        }
                    } else {
                        Common.showAlertDialogGreen(InvoiceChamNoFragment.this.getActivity(), "Thông báo", Color.WHITE, "Chấm nợ trên server thất bại", Color.WHITE, "OK", Color.WHITE);
                    }
                }
            }

            if (sbMaKH.length() > 0) {
                Common.showAlertDialogGreen(InvoiceChamNoFragment.this.getActivity(), "Thông báo", Color.WHITE, "Các khách hàng "
                        + sbMaKH.substring(0, sbMaKH.toString().trim().length() - 1)
                        + " đã chấm hình thức khác", Color.WHITE, "OK", Color.WHITE);
            }

            Toast.makeText(InvoiceChamNoFragment.this.getActivity(), "Chấm nợ thành công " + countResult + " hóa đơn", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Common.showAlertDialogGreen(InvoiceChamNoFragment.this.getActivity(), "Lỗi", Color.RED, "Lỗi in biên nhận:\n" + ex.toString(), Color.WHITE, "OK", Color.WHITE);
        }
    }

    private void HuyCham(Button btHuyCham, Button btInBienNhan, Button btInThongBao){
        try {
            String MA_DVIQLY = connection.getMaDVIByUser(InvoiceCommon.getUSERNAME());
            String MA_TNGAN = InvoiceCommon.getUSERNAME();
            String TT_HDON = "";
            String ID_HDON = "";
            String NGAY_NOP = "";

            JSONObject jsonObj = null;
            JSONArray jsonArr = new JSONArray();
            JSONArray jsonArr2 = new JSONArray();

            for (InvoicePosKHEntity entity : listChamNo) {
                String MA_KHANG = entity.getMA();
                ID_HDON = connection.getIDHdon(MA_KHANG);
                NGAY_NOP = connection.getNgayNopHdon(MA_KHANG);

                jsonObj = new JSONObject();
                jsonObj.put("ID_HDON", ID_HDON);
                jsonObj.put("NGAY_NOP", NGAY_NOP);
                jsonArr.put(jsonObj);
            }

            boolean isNetworkActive = Common.isNetworkOnline(InvoiceChamNoFragment.this.getActivity());
            if (isNetworkActive) {
                int countHuyCham = 0;
                if(jsonArr.length() > 0) {
                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject jO = jsonArr.getJSONObject(i);
                        int TRANG_THAI = connection.getTrangThaiByID(jO.getString("ID_HDON"));
                        if (connection.updateHuyCham(jO.getString("ID_HDON"), 1, 0, TRANG_THAI == 2 ? "" : NGAY_NOP) != -1) {
                            if (TRANG_THAI == 3)
                                jsonArr2.put(jO);
                            else
                                countHuyCham++;
                        }
                    }
                }

                if(jsonArr2.length() > 0) {
                    TT_HDON = jsonArr2.toString();
                    String result = asyncCallWS.WS_UPDATE_HUY_CHAM_CALL(MA_DVIQLY, MA_TNGAN, TT_HDON);
                    JSONArray arr = new JSONArray(result);
                    if(arr.length() > 0) {
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject jO = arr.getJSONObject(i);
                            if (jO.getString("KET_QUA").equals("null")) {
                                connection.updateNgayNop(jO.getString("ID_HDON"), "");
                                countHuyCham++;
                            } else {
                                connection.rollBackHuyCham(jO.getString("ID_HDON"), 3, 1);
                            }
                        }
                    } else {
                        Common.showAlertDialogGreen(InvoiceChamNoFragment.this.getActivity(), "Lỗi", Color.RED, "Hủy chấm trên server thất bại", Color.WHITE, "OK", Color.WHITE);
                    }
                    if(countHuyCham > 0){
                        btHuyCham.setEnabled(false);
                        btInBienNhan.setEnabled(true);
                        btInThongBao.setEnabled(true);
                        Toast.makeText(InvoiceChamNoFragment.this.getActivity(), "Hủy chấm thành công " + countHuyCham + " hóa đơn", Toast.LENGTH_LONG).show();
                    } else {
                        Common.showAlertDialogGreen(InvoiceChamNoFragment.this.getActivity(), "Lỗi", Color.RED, "Hủy chấm thất bại", Color.WHITE, "OK", Color.WHITE);
                    }
                } else {
                    if(countHuyCham > 0){
                        Common.showAlertDialogGreen(InvoiceChamNoFragment.this.getActivity(), "Thông báo", Color.WHITE, "Hủy chấm thành công: " + countHuyCham + " hóa đơn", Color.WHITE, "OK", Color.WHITE);
                    } else {
                        Common.showAlertDialogGreen(InvoiceChamNoFragment.this.getActivity(), "Lỗi", Color.RED, "Hủy chấm trên thiết bị thất bại", Color.WHITE, "OK", Color.RED);
                    }
                }
            } else {
                int countHuyCham = 0;
                for (int i = 0; i < jsonArr.length(); i++) {
                    JSONObject jO = jsonArr.getJSONObject(i);
                    int TRANG_THAI = connection.getTrangThaiByID(jO.getString("ID_HDON"));
                    if(TRANG_THAI == 2) {
                        if (connection.updateHuyCham(jO.getString("ID_HDON"), 1, 0, "") != -1) {
                            countHuyCham++;
                        }
                    }
                }
                if(countHuyCham > 0){
                    Common.showAlertDialogGreen(InvoiceChamNoFragment.this.getActivity(), "Thông báo", Color.WHITE, "Hủy chấm thành công: " + countHuyCham + " hóa đơn", Color.WHITE, "OK", Color.WHITE);
                } else {
                    Common.showAlertDialogGreen(InvoiceChamNoFragment.this.getActivity(), "Lỗi", Color.RED, "Hủy chấm trên thiết bị thất bại", Color.WHITE, "OK", Color.RED);
                }
            }
        } catch(Exception ex) {
            Common.showAlertDialogGreen(InvoiceChamNoFragment.this.getActivity(), "Lỗi", Color.RED, "Lỗi hủy chấm:\n" + ex.toString(), Color.WHITE, "OK", Color.WHITE);
        }
    }

    private void setDataOnSpinner(){
        try{
            String [] sArrLoaiHD = {"Chưa chấm", "Đã chấm offline", "Đã chấm online", "Hình thức khác"};
            ArrayAdapter<String> adapterLoaiHD = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, sArrLoaiHD);
            spLoaiHD.setAdapter(adapterLoaiHD);

            ArrayList<String> arrSo = new ArrayList<String>();
            arrSo.add("Tất cả sổ");
            Cursor c = connection.getAllDataSo();
            if(c.moveToFirst()) {
                do {
                    arrSo.add(c.getString(0));
                } while (c.moveToNext());
            }
            ArrayAdapter<String> adapterSo = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrSo);
            spSo.setAdapter(adapterSo);

            spLoaiHD.post(new Runnable() {
                @Override
                public void run() {
                    spLoaiHD.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            setDataOnRecyclerView(getTrangThai(position), spSo.getSelectedItem().toString());
                            posKHAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            });

            spSo.post(new Runnable() {
                @Override
                public void run() {
                    spSo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            setDataOnRecyclerView(getTrangThai(spLoaiHD.getSelectedItemPosition()), parent.getSelectedItem().toString());
                            posKHAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            });
        } catch(Exception ex) {
            Toast.makeText(getActivity().getApplicationContext(), "Error set data on spinner: " + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private int getTrangThai(int posSpinner){
        switch (spLoaiHD.getSelectedItemPosition()){
            case 0:
                return 0;
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 5;
        }
        return 0;
    }

    private void setDataOnRecyclerView(int TINH_TRANG, String MA_SOGCS) {
        try {
            listData = new ArrayList<>();
            Cursor c = null;
            if(MA_SOGCS.equals("Tất cả sổ")) {
                if (TINH_TRANG == 0)
                    c = connection.getAllDataKHACHHANGChuaCham(InvoiceCommon.getUSERNAME(), TINH_TRANG);//TINH_TRANG - 0: Chưa chấm, 1: Đã chấm
                else if (TINH_TRANG == 2 || TINH_TRANG == 3)
                    c = connection.getAllDataKHACHHANGByTrangThai(InvoiceCommon.getUSERNAME(), TINH_TRANG);
                else if (TINH_TRANG == 5)
                    c = connection.getAllDataKHACHHANGByTrangThai2(InvoiceCommon.getUSERNAME(), TINH_TRANG);
            } else {
                if (TINH_TRANG == 0)
                    c = connection.getAllDataKHACHHANGChuaChamAndSo(InvoiceCommon.getUSERNAME(), TINH_TRANG, MA_SOGCS);//TINH_TRANG - 0: Chưa chấm, 1: Đã chấm
                else if (TINH_TRANG == 2 || TINH_TRANG == 3)
                    c = connection.getAllDataKHACHHANGChuaChamAndSoByTrangThai(InvoiceCommon.getUSERNAME(), TINH_TRANG, MA_SOGCS);
                else if (TINH_TRANG == 5)
                    c = connection.getAllDataKHACHHANGChuaChamAndSoByTrangThai2(InvoiceCommon.getUSERNAME(), TINH_TRANG, MA_SOGCS);
            }
            if(c.moveToFirst()){
                do {
                    InvoicePosKHEntity entity = new InvoicePosKHEntity();
                    entity.setSTT(c.getString(c.getColumnIndex("STT")).equals("null")?"":c.getString(c.getColumnIndex("STT")));
                    entity.setSO(c.getString(c.getColumnIndex("MA_SOGCS")).equals("null")?"":c.getString(c.getColumnIndex("MA_SOGCS")));
                    entity.setTEN_KH(c.getString(c.getColumnIndex("TEN_KHANG")).equals("null") ? "" : c.getString(c.getColumnIndex("TEN_KHANG")));
                    entity.setDIEN_THOAI(c.getString(c.getColumnIndex("DTHOAI_KHANG")).equals("null")?"":c.getString(c.getColumnIndex("DTHOAI_KHANG")));
                    entity.setDIA_CHI(c.getString(c.getColumnIndex("DCHI_KHANG")).equals("null")?"":c.getString(c.getColumnIndex("DCHI_KHANG")));
                    entity.setSO_TIEN(c.getString(c.getColumnIndex("TIEN_NO")).equals("null")?"":c.getString(c.getColumnIndex("TIEN_NO")));
                    entity.setNGAY_NOP(c.getString(c.getColumnIndex("NGAY_NOP")).equals("null")?"":c.getString(c.getColumnIndex("NGAY_NOP")));
                    entity.setMA(c.getString(c.getColumnIndex("MA_KHANG")).equals("null")?"":c.getString(c.getColumnIndex("MA_KHANG")));
                    entity.setSO_CTO(c.getString(c.getColumnIndex("SO_CTO")).equals("null")?"":c.getString(c.getColumnIndex("SO_CTO")));
                    entity.setKY(c.getString(c.getColumnIndex("KY_PS")).equals("null")?"":c.getString(c.getColumnIndex("KY_PS")));
                    entity.setTHANG(c.getString(c.getColumnIndex("THANG_PS")).equals("null")?"":c.getString(c.getColumnIndex("THANG_PS")));
                    entity.setNAM(c.getString(c.getColumnIndex("NAM_PS")).equals("null")?"":c.getString(c.getColumnIndex("NAM_PS")));
                    listData.add(entity);
                } while (c.moveToNext());
            }
            posKHAdapter = new InvoicePosKHAdapter(listData, InvoiceChamNoFragment.this.getActivity());
            rvKH.setAdapter(posKHAdapter);
            tvTongHD.setText("Tổng hóa đơn: " + listData.size());
            if(MA_SOGCS.equals("Tất cả sổ")) {
                if (TINH_TRANG == 0)
                    tvTongTien.setText("Tổng tiền: " + Common.formatMoney(new BigDecimal((connection.sumTienNoByTinhTrang(InvoiceCommon.getUSERNAME(), TINH_TRANG) + connection.sumThueNoByTinhTrang(InvoiceCommon.getUSERNAME(), TINH_TRANG))).toString()));
                else if (TINH_TRANG == 2 || TINH_TRANG == 3) {
                    tvTongTien.setText("Tổng tiền: " + Common.formatMoney(new BigDecimal((connection.sumTienNoByTrangThai(InvoiceCommon.getUSERNAME(), TINH_TRANG) + connection.sumThueNoByTrangThai(InvoiceCommon.getUSERNAME(), TINH_TRANG))).toString()));
                } else if (TINH_TRANG == 5) {
                    String tt = Common.formatMoney(new BigDecimal((connection.sumTienNoByTrangThai2(InvoiceCommon.getUSERNAME(), TINH_TRANG) + connection.sumThueNoByTrangThai2(InvoiceCommon.getUSERNAME(), TINH_TRANG))).toString());
                    tvTongTien.setText("Tổng tiền: " + Common.formatMoney(new BigDecimal((connection.sumTienNoByTrangThai2(InvoiceCommon.getUSERNAME(), TINH_TRANG) + connection.sumThueNoByTrangThai2(InvoiceCommon.getUSERNAME(), TINH_TRANG))).toString()));
                }
            } else {
                if (TINH_TRANG == 0)
                    tvTongTien.setText("Tổng tiền: " + Common.formatMoney(new BigDecimal((connection.sumTienNoByTinhTrangAndSo(InvoiceCommon.getUSERNAME(), TINH_TRANG, MA_SOGCS) + connection.sumThueNoByTinhTrangAndSo(InvoiceCommon.getUSERNAME(), TINH_TRANG, MA_SOGCS))).toString()));
                else if (TINH_TRANG == 2 || TINH_TRANG == 3) {
                    tvTongTien.setText("Tổng tiền: " + Common.formatMoney(new BigDecimal((connection.sumTienNoByTrangThaiAndSo(InvoiceCommon.getUSERNAME(), TINH_TRANG, MA_SOGCS) + connection.sumThueNoByTrangThaiAndSo(InvoiceCommon.getUSERNAME(), TINH_TRANG, MA_SOGCS))).toString()));
                } else if (TINH_TRANG == 5) {
                    tvTongTien.setText("Tổng tiền: " + Common.formatMoney(new BigDecimal((connection.sumTienNoByTrangThaiAndSo2(InvoiceCommon.getUSERNAME(), TINH_TRANG, MA_SOGCS) + connection.sumThueNoByTrangThaiAndSo2(InvoiceCommon.getUSERNAME(), TINH_TRANG, MA_SOGCS))).toString()));
                }
            }
        } catch(Exception ex) {
            Common.showAlertDialogGreen(InvoiceChamNoFragment.this.getActivity(), "Lỗi", Color.RED, "Không hiển thị được dữ liệu\n" + ex.toString(), Color.WHITE, "OK", Color.WHITE);
        }
    }

    private void showProgressDialog(){
        progressDialog = ProgressDialog.show(InvoiceChamNoFragment.this.getActivity(), "Please Wait ...", "Đang lấy dữ liệu ...", true, false);
        Thread thread = new Thread(InvoiceChamNoFragment.this);
        thread.start();
    }

    private void getPS(String MA_DVIQLY, String MA_MAY, String MA_TNGAN){
        try {
            arrData = new ArrayList<>();
            arrData = asyncCallWS.WS_GET_PS_CALL(MA_DVIQLY, MA_MAY, MA_TNGAN);
        } catch(Exception ex) {
            Common.showAlertDialogGreen(this.getActivity(), "Lỗi", Color.RED, "Lỗi lấy dữ liệu:\n" + ex.toString(), Color.WHITE, "OK", Color.WHITE);
        }
    }

    private void setData(){
        try {
            int count = 0;
            if(arrData != null) {
                for (JSONObject object : arrData) {
                    if (connection.insertDataKHACHHANG(object.getInt("THANG_HT"), object.getInt("NAM_HT"), object.getString("MA_NVIEN"),
                            object.getInt("ID_HDON"), object.getString("LOAI_HDON"), object.getString("LOAI_PSINH"), object.getString("MA_KHANG"),
                            object.getString("MA_KHTT"), object.getString("TEN_KHANG"), object.getString("TEN_KHANG1"), object.getString("TEN_KHTT"),
                            object.getString("DCHI_KHANG"), object.getString("DCHI_KHTT"), object.getInt("LOAI_KHANG"), object.getString("MANHOM_KHANG"),
                            object.getString("NAM_PS"), object.getString("THANG_PS"), object.getString("KY_PS"), object.getString("TIEN_PSINH"),
                            object.getString("THUE_PSINH"), object.getString("MA_SOGCS"), object.getInt("STT_TRANG"), object.getString("STT"),
                            object.getInt("TTRANG_SSAI"), object.getInt("LAN_GIAO"), object.getString("HTHUC_TTOAN"), object.getInt("LOAI_TBAO"),
                            object.getString("NGAY_PHANH"), object.getString("DTHOAI_KHANG"), object.getString("DIEN_THOAI"), object.getString("DTHOAI_NONG"),
                            object.getString("DTHOAI_TRUC"), object.getString("SO_SERY"), object.getString("NGAY_DKY"), object.getString("NGAY_CKY"),
                            object.getString("NGAY_GIAO"), object.getInt("SO_BBGIAO"), object.getString("MA_TNGAN"), object.getString("TIEN_NO"),
                            object.getString("THUE_NO"), object.getString("NGAY_NOP"), object.getInt("TINH_TRANG"), object.getInt("TRANG_THAI"),
                            object.getInt("SOLAN_BNHAN"), object.getInt("SOLAN_TBAO"), object.getString("SO_CTO"), object.getInt("SO_HO"),
                            object.getString("DIEN_TTHU"), object.getString("CS_DKY"), object.getString("CS_CKY"), object.getString("CTIET_GIA"),
                            object.getString("CTIET_DNTT"), object.getString("CTIET_TIEN"), object.getString("MA_NHANG"), object.getString("EMAIL"),
                            object.getString("MA_DVIQLY_THUHO"), object.getString("MA_TNGAN_THUHO")) != -1) {
                        count++;
                    }
                }
                if(count == arrData.size()) {
                    String result = asyncCallWS.WS_UPDATE_TRANG_THAI_GETPS_CALL(connection.getMaDVIByUser(InvoiceCommon.getUSERNAME()), InvoiceCommon.getUSERNAME(), count);
                    if (result == null || result.equals("")){
                        Common.showAlertDialogGreen(this.getActivity(), "Thông báo", Color.WHITE, "Đã lấy đủ hóa đơn", Color.WHITE, "OK", Color.WHITE);
                        setDataOnRecyclerView(0, "Tất cả sổ");
                    } else {
                        connection.deleteAllDataKHACHHANGByMaTN(InvoiceCommon.getUSERNAME());
                        Common.showAlertDialogGreen(this.getActivity(), "Lỗi", Color.RED, "Lấy thiếu " + (arrData.size() - count) + " hóa đơn, vui lòng lấy lại", Color.WHITE, "OK", Color.WHITE);
                    }
                } else {
                    connection.deleteAllDataKHACHHANGByMaTN(InvoiceCommon.getUSERNAME());
                    Common.showAlertDialogGreen(this.getActivity(), "Lỗi", Color.RED, "Lấy thiếu " + (arrData.size() - count) + " hóa đơn, vui lòng lấy lại", Color.WHITE, "OK", Color.WHITE);
                }

            } else {
                Common.showAlertDialogGreen(this.getActivity(), "Lỗi", Color.RED, "Lấy dữ liệu thất bại", Color.WHITE, "OK", Color.WHITE);
            }
        } catch(Exception ex) {
            Common.showAlertDialogGreen(this.getActivity(), "Lỗi", Color.RED, "Lỗi hiển thị dữ liệu:\n" + ex.toString(), Color.WHITE, "OK", Color.WHITE);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            setData();
            progressDialog.dismiss();
        }
    };

    @Override
    public void run() {
        try {
            Thread.sleep(500);
            getPS(connection.getMaDVIByUser(InvoiceCommon.getUSERNAME()), Common.GetIMEI(InvoiceChamNoFragment.this.getActivity()), InvoiceCommon.getUSERNAME());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        handler.sendEmptyMessage(0);
    }

    private void showBlueToothSettingBarcode(){
        try{
            final BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
            if (!bluetooth.isEnabled()){
                bluetooth.enable();
            }

            final Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.bluetooth.BluetoothSettings");
            intent.setComponent(cn);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch(Exception ex) {
            ex.toString();
        }
    }

    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void onPause() {
        super.onPause();
        releaseCamera();
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e){
        }
        return c;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                mCamera.autoFocus(autoFocusCB);
        }
    };

    PreviewCallback previewCb = new PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            try{
                Camera.Parameters parameters = camera.getParameters();
                Size size = parameters.getPreviewSize();

                Image barcode = new Image(size.width, size.height, "Y800");
                barcode.setData(data);

                int result = scanner.scanImage(barcode);

                if (result != 0) {
                    // Parameters p = mCamera.getParameters();
                    // p.setFlashMode(Parameters.FLASH_MODE_OFF);
                    // mCamera.setParameters(p);

                    previewing = false;
                    mCamera.setPreviewCallback(null);
                    mCamera.stopPreview();

                    SymbolSet syms = scanner.getResults();
                    for (Symbol sym : syms) {
                        mp = MediaPlayer.create(InvoiceChamNoFragment.this.getActivity(), R.raw.beep);
                        mp.start();
//                        etSearch.setText(sym.getData());
                        barcodeScanned = true;

//                        InvoiceChamNoFragment.this.getActivity().runOnUiThread(new Runnable() {
//
//                            @Override
//                            public void run() {
//                                try {
//                                } catch (Exception ex) {
//                                    ex.toString();
//                                }
//                            }
//                        });

                        String MA_KHANG = sym.getData();
                        if(MA_KHANG != null && !MA_KHANG.equals("")) {
                            if (connection.checkMaKHang(MA_KHANG)) {
                                if (posKHAdapter.listChoose.contains(MA_KHANG)) {
                                    posKHAdapter.listChoose.remove(MA_KHANG);
                                } else {
                                    posKHAdapter.listChoose.add(MA_KHANG);
                                }

                                if (posKHAdapter.listChoose.size() > 0 && connection.getTrangThaiByMaKH(posKHAdapter.listChoose.get(0)) < 6) {
                                    showDialogChamNo();
                                }
                            } else {
                                Toast.makeText(InvoiceChamNoFragment.this.getActivity(), "Không tồn tại khách hàng", Toast.LENGTH_LONG).show();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(1500);
                                            previewing = true;
                                            mCamera.setPreviewCallback(previewCb);
                                            mCamera.startPreview();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            }
                        }
                    }
                }
            } catch(Exception ex) {
                ex.toString();
            }
        }
    };

    // Mimic continuous auto-focusing
    AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };

}
