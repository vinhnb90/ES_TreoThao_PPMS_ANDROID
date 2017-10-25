package com.es.tungnv.fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.es.tungnv.db.InvoiceSQLiteConnection;
import com.es.tungnv.printer.FontDefine;
import com.es.tungnv.printer.P25ConnectionException;
import com.es.tungnv.printer.P25Connector;
import com.es.tungnv.printer.PocketPos;
import com.es.tungnv.printer.Printer;
import com.es.tungnv.utils.Common;
import com.es.tungnv.utils.EPrinter;
import com.es.tungnv.utils.InvoiceCommon;
import com.es.tungnv.utils.InvoicePrinter;
import com.es.tungnv.views.R;
import com.es.tungnv.webservice.InvoiceAsyncCallWS;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by TUNGNV on 2/24/2016.
 */
public class InvoiceTongHopFragment extends Fragment implements View.OnClickListener, Runnable{

    private View rootView;
    private TextView tvThuNgan, tvSoHDGiao, tvTienHDGiao, tvSoHDThuTon, tvSoTienThuTon, tvSoHDChuaDongBo, tvSoHDThuHTK, tvSoTienChamOffline,
    tvSoHDChamOnline, tvSoTienChamOnline, tvSoTienThuHTK;
    private Spinner spIn, spSo;
    private ImageButton ibIn;
    private Button btLayDL3G, btLayDLXml, btXuatDLXml, btDongBo, btQuyetToan, btXoaDL;

    private InvoiceAsyncCallWS asyncCallWS;
    private InvoiceSQLiteConnection connection;
    private static ProgressDialog progressDialog;
    private static ProgressDialog progressDialogSent;

    private ArrayList<JSONObject> arrData;
    private ListView lvThietBi;
    private TextView tvTenThietBi;
    private int countDBo = 0;

    private BluetoothAdapter mBluetoothAdapter;
    public static ArrayAdapter<String> btArrayAdapter;
    private String BluetoothMac = "";
    private ProgressDialog mProgressDlg;
    private ProgressDialog mConnectingDlg;

    private P25Connector mConnector;
    private ArrayList<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();
    private BluetoothDevice bConntected;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            rootView = inflater.inflate(R.layout.invoice_fragment_tonghop, null);
            connection = new InvoiceSQLiteConnection(this.getActivity());
            asyncCallWS = new InvoiceAsyncCallWS();
            initComponent(rootView);
            setDataOnSpinner();
            initData();
        } catch(Exception ex) {
            Toast.makeText(getActivity().getApplicationContext(), "Error create view: " + ex.toString(), Toast.LENGTH_LONG).show();
        }
        return rootView;
    }

    private void initComponent(View rootView){
        tvThuNgan = (TextView) rootView.findViewById(R.id.invoice_fragment_tonghop_tv_thungan);
        tvSoHDGiao = (TextView) rootView.findViewById(R.id.invoice_fragment_tonghop_tv_so_hd_giao);
        tvTienHDGiao = (TextView) rootView.findViewById(R.id.invoice_fragment_tonghop_tv_so_tien_giao);
        tvSoHDThuTon = (TextView) rootView.findViewById(R.id.invoice_fragment_tonghop_tv_so_hd_thu_ton);
        tvSoTienThuTon = (TextView) rootView.findViewById(R.id.invoice_fragment_tonghop_tv_so_tien_thu_ton);
        tvSoHDChuaDongBo = (TextView) rootView.findViewById(R.id.invoice_fragment_tonghop_tv_so_hd_chưa_dong_bo);
        tvSoTienChamOffline = (TextView) rootView.findViewById(R.id.invoice_fragment_tonghop_tv_so_tien_offline);
        tvSoHDChamOnline = (TextView) rootView.findViewById(R.id.invoice_fragment_tonghop_tv_so_hd_cham_online);
        tvSoTienChamOnline = (TextView) rootView.findViewById(R.id.invoice_fragment_tonghop_tv_so_tien_cham_online);
        tvSoHDThuHTK = (TextView) rootView.findViewById(R.id.invoice_fragment_tonghop_tv_so_hd_thu_htk);
        tvSoTienThuHTK = (TextView) rootView.findViewById(R.id.invoice_fragment_tonghop_tv_so_tien_thu_htk);
        spIn = (Spinner) rootView.findViewById(R.id.invoice_fragment_tonghop_sp_in);
        spSo = (Spinner) rootView.findViewById(R.id.invoice_fragment_tonghop_sp_so);
        ibIn = (ImageButton) rootView.findViewById(R.id.invoice_fragment_tonghop_ib_in);
        btLayDL3G = (Button) rootView.findViewById(R.id.invoice_fragment_tonghop_bt_lay_dl_3g);
        btLayDLXml = (Button) rootView.findViewById(R.id.invoice_fragment_tonghop_bt_lay_dl_xml);
        btXuatDLXml = (Button) rootView.findViewById(R.id.invoice_fragment_tonghop_bt_xuat_dl_xml);
        btDongBo = (Button) rootView.findViewById(R.id.invoice_fragment_tonghop_bt_dong_bo);
        btQuyetToan = (Button) rootView.findViewById(R.id.invoice_fragment_tonghop_bt_quyet_toan);
        btXoaDL = (Button) rootView.findViewById(R.id.invoice_fragment_tonghop_bt_xoa_dl);

        ibIn.setOnClickListener(this);
        btLayDL3G.setOnClickListener(this);
        btLayDLXml.setOnClickListener(this);
        btXuatDLXml.setOnClickListener(this);
        btDongBo.setOnClickListener(this);
        btQuyetToan.setOnClickListener(this);
        btXoaDL.setOnClickListener(this);
    }

    private void setDataOnSpinner(){
        try{
            String [] sArrKieuIn = {"In tổng hợp", "In theo sổ", "In danh sách khách hàng"};
            ArrayAdapter<String> adapterKieuIn = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, sArrKieuIn);
            spIn.setAdapter(adapterKieuIn);

            ArrayList<String> arrSo = new ArrayList<>();
            arrSo.add("Tất cả");
            Cursor c = connection.getAllSoGCS();
            if(c.moveToFirst()){
                do {
                    arrSo.add(c.getString(0));
                } while (c.moveToNext());
            }
            ArrayAdapter<String> adapterSo = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrSo);
            spSo.setAdapter(adapterSo);

            spIn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 2) {
                        spSo.setVisibility(View.VISIBLE);
                    } else {
                        spSo.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch(Exception ex) {
            Toast.makeText(getActivity().getApplicationContext(), "Error set data on spinner: " + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void initData() {
        try {
            tvThuNgan.setText(InvoiceCommon.getUSERNAME() + " - " + connection.getTenKHByMaTN(InvoiceCommon.getUSERNAME()));
            tvSoHDGiao.setText("" + connection.countPS(InvoiceCommon.getUSERNAME()));
            tvTienHDGiao.setText(Common.formatMoney(new BigDecimal(connection.sumTienNo(InvoiceCommon.getUSERNAME()) + connection.sumThueNo(InvoiceCommon.getUSERNAME())).toString()));
            tvSoHDThuTon.setText(connection.countHDThu(InvoiceCommon.getUSERNAME()) + "/" + connection.countHDTon(InvoiceCommon.getUSERNAME()));
            tvSoTienThuTon.setText(Common.formatMoney(new BigDecimal(connection.sumTienHDThu(InvoiceCommon.getUSERNAME()) + connection.sumThueHDThu(InvoiceCommon.getUSERNAME())).toString()) + "/" + Common.formatMoney(new BigDecimal(connection.sumTienHDTon(InvoiceCommon.getUSERNAME()) + connection.sumThueHDTon(InvoiceCommon.getUSERNAME())).toString()));
            tvSoHDChuaDongBo.setText("" + connection.countHDChuaDongBo(InvoiceCommon.getUSERNAME()));
            tvSoTienChamOffline.setText(Common.formatMoney(new BigDecimal(connection.sumTienOff() + connection.sumThueOff()).toString()));
            tvSoHDChamOnline.setText("" + connection.countHDOnLine(InvoiceCommon.getUSERNAME()));
            tvSoTienChamOnline.setText(Common.formatMoney(new BigDecimal(connection.sumTienOn() + connection.sumThueOn()).toString()));
            tvSoHDThuHTK.setText("" + connection.countHDThuHTK(InvoiceCommon.getUSERNAME()));
            tvSoTienThuHTK.setText(Common.formatMoney(new BigDecimal(connection.sumTienHTK() + connection.sumThueHTK()).toString()));
        } catch(Exception ex) {
            Toast.makeText(getActivity().getApplicationContext(), "Lỗi khởi tạo dữ liệu: " + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.invoice_fragment_tonghop_ib_in:
                switch (spIn.getSelectedItemPosition()){
                    case 0://In tổng hợp
                        showDialogIn2(0, spSo.getSelectedItem().toString());
                        break;
                    case 1://In theo sổ
                        showDialogIn2(1, spSo.getSelectedItem().toString());
                        break;
                    case 2://In danh sách KH
                        showDialogIn2(2, spSo.getSelectedItem().toString());
                        break;
                }
                break;
            case R.id.invoice_fragment_tonghop_bt_lay_dl_3g:
                showProgressDialog();
                break;
            case R.id.invoice_fragment_tonghop_bt_lay_dl_xml:

                break;
            case R.id.invoice_fragment_tonghop_bt_xuat_dl_xml:

                break;
            case R.id.invoice_fragment_tonghop_bt_dong_bo:
                if (Common.isNetworkOnline(InvoiceTongHopFragment.this.getActivity())){
                    launchBarDialog();
                } else {
                    Common.showAlertDialogGreen(InvoiceTongHopFragment.this.getActivity(), "Thông báo", Color.WHITE, "Bạn phải kết nối mạng", Color.WHITE, "OK", Color.WHITE);
                }
                break;
            case R.id.invoice_fragment_tonghop_bt_quyet_toan:
                if (Common.isNetworkOnline(InvoiceTongHopFragment.this.getActivity())){
                    callQuyetToan();
                } else {
                    Common.showAlertDialogGreen(InvoiceTongHopFragment.this.getActivity(), "Thông báo", Color.WHITE, "Bạn phải kết nối mạng", Color.WHITE, "OK", Color.WHITE);
                }
                break;
            case R.id.invoice_fragment_tonghop_bt_xoa_dl:
                if(connection.deleteAllDataKHACHHANG() != -1){
                    Toast.makeText(getActivity().getApplicationContext(), "Đã xóa toàn bộ dữ liệu", Toast.LENGTH_LONG).show();
                } else {
                    Common.showAlertDialogGreen(InvoiceTongHopFragment.this.getActivity(), "Lỗi", Color.WHITE, "Không xóa được dữ liệu", Color.WHITE, "OK", Color.WHITE);
                }
                break;
        }
    }

    private void showDialogIn(final int kieuIn, final String SO){
        try{
            final Dialog dialog = new Dialog(this.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.invoice_dialog_printer);
            dialog.getWindow().setLayout(android.app.ActionBar.LayoutParams.MATCH_PARENT, android.app.ActionBar.LayoutParams.MATCH_PARENT);

            lvThietBi = (ListView) dialog.findViewById(R.id.invoice_dialog_printer_lv_thietbi);
            final Button btIn = (Button) dialog.findViewById(R.id.invoice_dialog_printer_bt_in);
            final Button btTim = (Button) dialog.findViewById(R.id.invoice_dialog_printer_bt_tim);
            tvTenThietBi = (TextView) dialog.findViewById(R.id.invoice_dialog_printer_tv_ten_thietbi);

            mDeviceList.clear();
            mBluetoothAdapter	= BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter == null) {
                Toast.makeText(getActivity().getApplicationContext(), "Thiết bị không hỗ trợ Bluetooth", Toast.LENGTH_LONG).show();
            } else {
                if (!mBluetoothAdapter.isEnabled()) {
                    tvTenThietBi.setText("Bluetooth đang tắt");
                    CheckBlueToothState(tvTenThietBi, btTim);
                } else {
                    tvTenThietBi.setText("Bluetooth đã bật");

                    Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

                    if (pairedDevices != null) {
                        mDeviceList.addAll(pairedDevices);

                        updateDeviceList(lvThietBi);
                    }
                }

                mProgressDlg = new ProgressDialog(this.getActivity());

                mProgressDlg.setMessage("Đang tìm thiết bị...");
                mProgressDlg.setCancelable(false);
                mProgressDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mBluetoothAdapter.cancelDiscovery();
                    }
                });

                mConnectingDlg 	= new ProgressDialog(this.getActivity());

                mConnectingDlg.setMessage("Đang kết nối...");
                mConnectingDlg.setCancelable(false);

                mConnector = new P25Connector(new P25Connector.P25ConnectionListener() {

                    @Override
                    public void onStartConnecting() {
                        mConnectingDlg.show();
                    }

                    @Override
                    public void onConnectionSuccess() {
                        mConnectingDlg.dismiss();
                        Toast.makeText(getActivity().getApplicationContext(), "Đã kết nối", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onConnectionFailed(String error) {
                        mConnectingDlg.dismiss();
                    }

                    @Override
                    public void onConnectionCancelled() {
                        mConnectingDlg.dismiss();
                    }

                    @Override
                    public void onDisconnected() {
                        Toast.makeText(getActivity().getApplicationContext(), "Đã mất kết nối", Toast.LENGTH_LONG).show();
                    }
                });
            }

            IntentFilter filter = new IntentFilter();

            filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            filter.addAction(BluetoothDevice.ACTION_FOUND);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);

            this.getActivity().registerReceiver(mReceiver, filter);

            lvThietBi.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    String addMAC = (String) parent.getItemAtPosition(position);
//                    String[] addMACs = addMAC.split("\n");
//                    BluetoothMac = addMACs[1];
//                    tvTenThietBi.setText("Đã kết nối:\n" + BluetoothMac);
                    bConntected = mDeviceList.get(position);
                    connect(bConntected);
                }
            });

            btTim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDeviceList.clear();
                    mBluetoothAdapter.startDiscovery();

                    Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

                    if (pairedDevices != null) {
                        mDeviceList.addAll(pairedDevices);
                        updateDeviceList(lvThietBi);
                    }
                }
            });

            btIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String strPrinter = "";
                    if (kieuIn == 0) {//In tổng hợp
                        strPrinter = strPrinterTongHop();
                    } else if (kieuIn == 1){//In theo sổ
                        strPrinter = strPrinterTheoSo();
                    } else {//In danh sách KH
                        strPrinter = strPrinterDanhsachKH(SO);
                    }
                    Bitmap bmp = toGrayscale(BitmapFactory.decodeResource(getResources(), R.drawable.name));

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 80, stream);
                    byte[] byteArray = stream.toByteArray();

//                    printImage(byteArray);
                    printText(strPrinter);

//                    try {
//                        if (InvoiceCommon.printer == null) {
//                            InvoiceCommon.printer = new InvoicePrinter(BluetoothMac);
//                        } else {
//                            InvoiceCommon.printer.MAC = BluetoothMac;
//                        }
//                        String strPrinter = "";
//                        if (kieuIn == 0) {//In tổng hợp
//                            strPrinter = strPrinterTongHop();
//                        } else if (kieuIn == 1){//In theo sổ
//                            strPrinter = strPrinterTheoSo();
//                        } else {//In danh sách KH
//                            strPrinter = strPrinterDanhsachKH(SO);
//                        }
////                        int nRet = printer.Connect(BluetoothMac);
////                        if (nRet == 0) {
////                            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.logoyb);
////                            byte[] bImage = Common.encodeTobase64Byte(bm);
////                            String imagePath = Environment.getExternalStorageDirectory() + InvoiceConstantVariables.PROGRAM_PATH + "name.bmp";
//////                            printer.PrintText("jksdhjkfhjksdfhsdjkh", printer.ALIGNMENT_LEFT, printer.FT_BOLD, printer.TS_0WIDTH | printer.TS_0HEIGHT);
////                            printer.PrintImage(imagePath, printer.ALIGNMENT_LEFT, printer.FT_BOLD, printer.TS_0WIDTH | printer.TS_0HEIGHT);
////                        }
//
//                        printImage();
//
////                        InvoiceCommon.printer.Print(bImage);
//                        //InvoiceCommon.printer.Print(strPrinter);
//                    } catch (Exception ex) {
//                        Common.showAlertDialogGreen(InvoiceTongHopFragment.this.getActivity(), "Lỗi", Color.RED, "Lỗi in\n" + ex.toString(), Color.WHITE, "OK", Color.WHITE);
//                    }
                }
            });

            dialog.show();
        } catch(Exception ex) {
            Toast.makeText(getActivity().getApplicationContext(), "Lỗi hiển thị hộp thoại chấm nợ:\n" + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void showDialogIn2(final int kieuIn, final String SO){
        try{
            final Dialog dialog = new Dialog(this.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.invoice_dialog_printer);
            dialog.getWindow().setLayout(android.app.ActionBar.LayoutParams.MATCH_PARENT, android.app.ActionBar.LayoutParams.MATCH_PARENT);

            ListView lvThietBi = (ListView) dialog.findViewById(R.id.invoice_dialog_printer_lv_thietbi);
            final Button btIn = (Button) dialog.findViewById(R.id.invoice_dialog_printer_bt_in);
            final Button btTim = (Button) dialog.findViewById(R.id.invoice_dialog_printer_bt_tim);
            final TextView tvTenThietBi = (TextView) dialog.findViewById(R.id.invoice_dialog_printer_tv_ten_thietbi);

            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            btArrayAdapter = new ArrayAdapter<String>(InvoiceTongHopFragment.this.getActivity(), R.layout.invoice_frament_chamno_listitem);//android.R.layout.simple_list_item_1
            lvThietBi.setAdapter(btArrayAdapter);

            CheckBlueToothState(tvTenThietBi, btTim);

            InvoiceTongHopFragment.this.getActivity().registerReceiver(ActionFoundReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
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
                    mBluetoothAdapter.startDiscovery();
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
                        if (kieuIn == 0) {//In tổng hợp
                            strPrinter = strPrinterTongHop();
                        } else if (kieuIn == 1){//In theo sổ
                            strPrinter = strPrinterTheoSo();
                        } else {//In danh sách KH
                            strPrinter = strPrinterDanhsachKH(SO);
                        }
                        InvoiceCommon.printer.Print(strPrinter);
                        dialog.dismiss();
                    } catch (Exception ex) {
                        Common.showAlertDialogGreen(InvoiceTongHopFragment.this.getActivity(), "Lỗi", Color.RED, "Lỗi in\n" + ex.toString(), Color.WHITE, "OK", Color.WHITE);
                    }
                }
            });

            dialog.show();
        } catch(Exception ex) {
            Toast.makeText(getActivity().getApplicationContext(), "Lỗi hiển thị hộp thoại chấm nợ:\n" + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void printText(String text) {
//        byte[] line 	= Printer.printfont(text + "\n\n", FontDefine.FONT_32PX, FontDefine.Align_LEFT, (byte) 0x1A,
//                PocketPos.LANGUAGE_ENGLISH);
        byte[] line = text.getBytes();
//        byte[] senddata = PocketPos.FramePack(PocketPos.FRAME_TOF_PRINT, line, 0, line.length);

        sendData(line);
    }

    private void updateDeviceList(ListView lvThietBi) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), R.layout.invoice_frament_chamno_listitem, getArray(mDeviceList));

        lvThietBi.setAdapter(adapter);
    }

    private String[] getArray(ArrayList<BluetoothDevice> data) {
        String[] list = new String[0];

        if (data == null) return list;

        int size	= data.size();
        list		= new String[size];

        for (int i = 0; i < size; i++) {
            list[i] = data.get(i).getName();
        }

        return list;
    }

    private void connect(BluetoothDevice d) {
        if (mDeviceList == null || mDeviceList.size() == 0) {
            return;
        }

        BluetoothDevice device = d;

        if (device.getBondState() == BluetoothDevice.BOND_NONE) {
            try {
                createBond(device);
            } catch (Exception e) {
                return;
            }
        }

        try {
            if (!mConnector.isConnected()) {
                mConnector.connect(device);
            } else {
                mConnector.disconnect();
            }
        } catch (P25ConnectionException e) {
            e.printStackTrace();
        }
    }

    private void createBond(BluetoothDevice device) throws Exception {

        try {
            Class<?> cl 	= Class.forName("android.bluetooth.BluetoothDevice");
            Class<?>[] par 	= {};

            Method method 	= cl.getMethod("createBond", par);

            method.invoke(device);

        } catch (Exception e) {
            e.printStackTrace();

            throw e;
        }
    }

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
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, InvoicePrinter.REQUEST_ENABLE_BT);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        try {
            if(requestCode == InvoicePrinter.REQUEST_ENABLE_BT && resultCode != 0){
                tvTenThietBi.setText("Bluetooth đã bật.");
            }
        } catch(Exception ex) {
            Common.showAlertDialogGreen(InvoiceTongHopFragment.this.getActivity(), "Lỗi", Color.RED, "Lỗi kích hoạt bluetooth\n" + ex.toString(), Color.WHITE, "OK", Color.WHITE);
        }
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

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                final int state 	= intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                if (state == BluetoothAdapter.STATE_ON) {
                    Toast.makeText(getActivity().getApplicationContext(), "Đã bật bluetooth", Toast.LENGTH_LONG).show();
                } else if (state == BluetoothAdapter.STATE_OFF) {
                    Toast.makeText(getActivity().getApplicationContext(), "Đã tắt bluetooth", Toast.LENGTH_LONG).show();
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                mDeviceList = new ArrayList<BluetoothDevice>();

                mProgressDlg.show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                mProgressDlg.dismiss();

                updateDeviceList(lvThietBi);
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                mDeviceList.add(device);

                Toast.makeText(getActivity().getApplicationContext(), "Đã tìm thấy thiết bị: " + device.getName(), Toast.LENGTH_LONG).show();
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);

                if (state == BluetoothDevice.BOND_BONDED) {
                    Toast.makeText(getActivity().getApplicationContext(), "Đã ghép nối", Toast.LENGTH_LONG).show();
                    connect(bConntected);
                }
            }
        }
    };

    private String strPrinterTongHop()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        String NGAY_IN = sdf.format(cal.getTime());
        String MA_TNGAN = InvoiceCommon.getUSERNAME();
        String TEN_TNGAN =  connection.getTenKHByMaTN(InvoiceCommon.getUSERNAME());
        String SO_HDON_GIAO = "" + connection.countPS(InvoiceCommon.getUSERNAME());
        String SO_TIEN_GIAO = Common.formatMoney(new BigDecimal(connection.sumTienNo(InvoiceCommon.getUSERNAME()) + connection.sumThueNo(InvoiceCommon.getUSERNAME())).toString());
        String SO_HDON_THU_TON = connection.countHDThu(InvoiceCommon.getUSERNAME()) + "/" + connection.countHDTon(InvoiceCommon.getUSERNAME());
        String SO_TIEN_THU_TON = Common.formatMoney(new BigDecimal(connection.sumTienHDThu(InvoiceCommon.getUSERNAME()) + connection.sumThueHDThu(InvoiceCommon.getUSERNAME())).toString()) + "/" + Common.formatMoney(new BigDecimal(connection.sumTienHDTon(InvoiceCommon.getUSERNAME()) + connection.sumThueHDTon(InvoiceCommon.getUSERNAME())).toString());
        String SO_HDON_CHUA_DBO = "" + connection.countHDChuaDongBo(InvoiceCommon.getUSERNAME());
        String SO_HDON_THU_HTK = "" + connection.countHDThuHTK(InvoiceCommon.getUSERNAME());

        String SO_TIEN_CHAM_OFF = Common.formatMoney(new BigDecimal(connection.sumTienOff() + connection.sumThueOff()).toString());
        String SO_HD_CHAM_OFF = "" + connection.countHDOnLine(InvoiceCommon.getUSERNAME());
        String SO_TIEN_CHAM_ON = Common.formatMoney(new BigDecimal(connection.sumTienOn() + connection.sumThueOn()).toString());
        String SO_TIEN_THU_HTK = Common.formatMoney(new BigDecimal(connection.sumTienHTK() + connection.sumThueHTK()).toString());

        StringBuilder str = new StringBuilder();
        str.append("Ngày in: " + NGAY_IN + " - " + TEN_TNGAN + "\n");
        str.append("Thu ngân: " + MA_TNGAN + "\n");
        str.append("Số HĐ giao: " + SO_HDON_GIAO + "\n");
        str.append("Tổng tiền giao: " + SO_TIEN_GIAO + "\n");
        str.append("Số HĐ thu/tồn: " + SO_HDON_THU_TON + "\n");
        str.append("Số tiền thu/tồn: \n" + SO_TIEN_THU_TON + "\n");
        str.append("Số HĐ chấm offline: " + SO_HDON_CHUA_DBO + "\n");
        str.append("Số tiền chấm offline: " + SO_TIEN_CHAM_OFF + "\n");
        str.append("Số HĐ chấm online: " + SO_HD_CHAM_OFF + "\n");
        str.append("Số tiền chấm online: " + SO_TIEN_CHAM_ON + "\n");
        str.append("Số HĐ thu HTK: " + SO_HDON_THU_HTK + "\n");
        str.append("Số tiền thu HTK: " + SO_TIEN_THU_HTK + "\n\n");
        return str.toString();
    }

    private String strPrinterTheoSo()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        String NGAY_IN = sdf.format(cal.getTime());
        String MA_TNGAN = InvoiceCommon.getUSERNAME();
        String TEN_TNGAN = connection.getTenKHByMaTN(InvoiceCommon.getUSERNAME());
        String SO_HDON_GIAO = "" + connection.countPS(InvoiceCommon.getUSERNAME());
        String SO_TIEN_GIAO = Common.formatMoney(new BigDecimal(connection.sumTienNo(InvoiceCommon.getUSERNAME()) + connection.sumThueNo(InvoiceCommon.getUSERNAME())).toString());
        String SO_HDON_THU_TON = connection.countHDThu(InvoiceCommon.getUSERNAME()) + "/" + connection.countHDTon(InvoiceCommon.getUSERNAME());
        String SO_TIEN_THU_TON = Common.formatMoney(new BigDecimal(connection.sumTienHDThu(InvoiceCommon.getUSERNAME()) + connection.sumThueHDThu(InvoiceCommon.getUSERNAME())).toString()) + "/" + Common.formatMoney(new BigDecimal(connection.sumTienHDTon(InvoiceCommon.getUSERNAME()) + connection.sumThueHDTon(InvoiceCommon.getUSERNAME())).toString());
        String SO_HDON_CHUA_DBO = "" + connection.countHDChuaDongBo(InvoiceCommon.getUSERNAME());
        String SO_HDON_THU_HTK = "" + connection.countHDThuHTK(InvoiceCommon.getUSERNAME());

        String SO_TIEN_CHAM_OFF = Common.formatMoney(new BigDecimal(connection.sumTienOff() + connection.sumThueOff()).toString());
        String SO_HD_CHAM_OFF = "" + connection.countHDOnLine(InvoiceCommon.getUSERNAME());
        String SO_TIEN_CHAM_ON = Common.formatMoney(new BigDecimal(connection.sumTienOn() + connection.sumThueOn()).toString());
        String SO_TIEN_THU_HTK = Common.formatMoney(new BigDecimal(connection.sumTienHTK() + connection.sumThueHTK()).toString());

        StringBuilder str = new StringBuilder();
        str.append("Ngày in: " + NGAY_IN + "\n");
        str.append("Thu ngân: " + MA_TNGAN + " - " + connection.getTenKHByMaTN(InvoiceCommon.getUSERNAME()) + "\n");
        str.append("Số HĐ giao: " + SO_HDON_GIAO + "\n");
        str.append("Tổng tiền giao: " + SO_TIEN_GIAO + "\n");
        str.append("Số HĐ thu/tồn: " + SO_HDON_THU_TON + "\n");
        str.append("Số tiền thu/tồn: \n" + SO_TIEN_THU_TON + "\n");
        str.append("Số HĐ chấm offline: " + SO_HDON_CHUA_DBO + "\n");
        str.append("Số tiền chấm offline: " + SO_TIEN_CHAM_OFF + "\n");
        str.append("Số HĐ chấm online: " + SO_HD_CHAM_OFF + "\n");
        str.append("Số tiền chấm online: " + SO_TIEN_CHAM_ON + "\n");
        str.append("Số HĐ thu HTK: " + SO_HDON_THU_HTK + "\n");
        str.append("Số tiền thu HTK: " + SO_TIEN_THU_HTK + "\n\n");

        Cursor c = connection.getAllSoGCS();
        if(c.moveToFirst()){
            do {
                String SO_GCS = c.getString(0);
                str.append("   Sổ GCS: " + SO_GCS + "\n");
                str.append("Sổ HĐ thu: " + connection.countPSBySo(SO_GCS) + "\n");
                str.append("Sổ tiền thu: " + Common.formatMoney(new BigDecimal(connection.sumTienNoBySo(SO_GCS)).toString()) + "\n");
                str.append("\n\n");
            } while(c.moveToNext());
        }

        return str.toString();
    }

    private String strPrinterDanhsachKH(String SO)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        String NGAY_IN = sdf.format(cal.getTime());

        StringBuilder str = new StringBuilder();

        int soHD = 0;
        double soTien = 0d;
        if(SO.equals("Tất cả")) {
            Cursor c = connection.getAllSoGCS();
            if (c.moveToFirst()) {
                do {
                    String SO_GCS = c.getString(0);
                    int tongHD = connection.countPSBySo(SO_GCS);
                    double tongTien = connection.sumTienNoBySo(SO_GCS) + connection.sumThueNoBySo(SO_GCS);
                    str.append("  Sổ GCS: " + SO_GCS + "\n");
                    str.append("  Tổng HĐ: " + tongHD + "\n");
                    str.append("  Tổng tiền: " + Common.formatMoney(new BigDecimal(tongTien).toString()) + "\n");
                    str.append("\n");
                    soHD += tongHD;
                    soTien += tongTien;

                    Cursor c2 = connection.getAllDataKHACHHANGDaChamBySo(SO_GCS);
                    if(c2.moveToFirst()){
                        do {
                            str.append(c2.getString(0) + "-" + c2.getString(1) + ": " + c2.getString(2) + "\n");
                        } while (c2.moveToNext());
                        str.append("------------------------------" + "\n");
                    }
                } while (c.moveToNext());
                str.append("Tổng số HĐ thu: " + soHD + "\n");
                str.append("Tổng tiền thu: " +  Common.formatMoney(new BigDecimal(soTien).toString()) + "\n");
                str.append("------------------------------" + "\n\n");
            }
        } else {
            int tongHD = connection.countPSBySo(SO);
            double tongTien = connection.sumTienNoBySo(SO) + connection.sumThueNoBySo(SO);
            str.append("  Sổ GCS: " + SO + "\n");
            str.append("  Tổng HĐ: " + tongHD + "\n");
            str.append("  Tổng tiền: " + Common.formatMoney(new BigDecimal(tongTien).toString()) + "\n");
            str.append("\n");
            soHD += tongHD;
            soTien += tongTien;

            Cursor c2 = connection.getAllDataKHACHHANGDaChamBySo(SO);
            if(c2.moveToFirst()){
                do {
                    str.append(c2.getString(0) + "-" + c2.getString(1) + ": " + c2.getString(2) + "\n");
                } while (c2.moveToNext());
                str.append("------------------------------" + "\n");
            }
            str.append("Tổng số HĐ thu: " + soHD + "\n");
            str.append("Tổng tiền thu: " +  Common.formatMoney(new BigDecimal(soTien).toString()) + "\n\n");
            str.append("------------------------------" + "\n\n");
        }

        return str.toString();
    }

    private void callQuyetToan() {
        try{
            StringBuilder sbError = new StringBuilder();
            String TT_BBGIAO = "";
            JSONObject jsonObj = null;
            JSONArray jsonArr = new JSONArray();

            int countQuyetToan = 0;
            Cursor c = connection.getDataSoBBGiao();
            if(c.moveToFirst()) {
                do {
                    jsonObj = new JSONObject();
                    jsonObj.put("SO_BBGIAO", c.getString(0));
                    jsonArr.put(jsonObj);
                } while (c.moveToNext());
                if(jsonArr.length() > 0) {
                    TT_BBGIAO = jsonArr.toString();
                    String result = asyncCallWS.WS_UPDATE_QUYET_TOAN_CALL(connection.getMaDVIByUser(InvoiceCommon.getUSERNAME()), InvoiceCommon.getUSERNAME(), TT_BBGIAO);
                    JSONArray arr = new JSONArray(result);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject jO = arr.getJSONObject(i);
                        if (connection.deleteDataKHACHHANGBySoBB(jO.getString("SO_BBGIAO")) != -1) {
                            countQuyetToan++;
                        } else {
                            sbError.append(jO.getString("SO_BBGIAO") + "\n");
                        }
                    }
                    if(sbError.length() > 0){
                        Common.showAlertDialogGreen(InvoiceTongHopFragment.this.getActivity(), "Lỗi", Color.RED, "Lỗi: " + sbError.toString(), Color.WHITE, "OK", Color.RED);
                    } else {
                        if (countQuyetToan > 0)
                            Toast.makeText(getActivity().getApplicationContext(), "Quyết toán thành công " + countQuyetToan + " biên bản", Toast.LENGTH_LONG).show();
                        else
                            Common.showAlertDialogGreen(InvoiceTongHopFragment.this.getActivity(), "Thông báo", Color.WHITE, "Không có biên bản nào được quyết toán", Color.WHITE, "OK", Color.WHITE);
                    }
                } else {
                    Common.showAlertDialogGreen(InvoiceTongHopFragment.this.getActivity(), "Thông báo", Color.RED, "Quyết toán không thành công", Color.WHITE, "OK", Color.RED);
                }
            }
        } catch(Exception ex) {
            Common.showAlertDialogGreen(InvoiceTongHopFragment.this.getActivity(), "Lỗi", Color.RED, "Lỗi quyết toán:\n" + ex.toString(), Color.WHITE, "OK", Color.RED);
        }
    }

    private void dongBo(){
        try {
            countDBo = 0;
            boolean isNetworkActive = Common.isNetworkOnline(InvoiceTongHopFragment.this.getActivity());
            if(isNetworkActive) {
                Date toDay = Calendar.getInstance().getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                String MA_DVIQLY = connection.getMaDVIByUser(InvoiceCommon.getUSERNAME());
                String MA_TNGAN = InvoiceCommon.getUSERNAME();
                String ID_HDON = "";
                String NGAY_NOP = sdf.format(toDay);
                int SOLAN_BNHAN = 0;
                int SOLAN_TBAO = 0;
                String TT_HDON = "";

                JSONObject jsonObj = null;
                JSONArray jsonArr = new JSONArray();
                ArrayList<String> arrMaKH = new ArrayList<String>();
                Cursor c = connection.getAllDataKHACHHANGChamOff(InvoiceCommon.getUSERNAME(), 2);
                if (c.moveToFirst()) {
                    do {
                        try {
                            String MA_KHANG = c.getString(c.getColumnIndex("MA_KHANG"));
                            ID_HDON = c.getString(c.getColumnIndex("ID_HDON"));
                            SOLAN_BNHAN = c.getInt(c.getColumnIndex("SOLAN_BNHAN"));
                            SOLAN_TBAO = c.getInt(c.getColumnIndex("SOLAN_TBAO"));

                            jsonObj = new JSONObject();
                            jsonObj.put("ID_HDON", ID_HDON);
                            jsonObj.put("NGAY_NOP", NGAY_NOP);
                            jsonObj.put("LOAI_TBAO", "1");
                            jsonObj.put("SOLAN_BNHAN", SOLAN_BNHAN);
                            jsonObj.put("SOLAN_TBAO", SOLAN_TBAO);
                            jsonArr.put(jsonObj);
                        } catch (Exception ex) {
                            ex.toString();
                        }
                    } while ((c.moveToNext()));

                    TT_HDON = jsonArr.toString();
                    String result = asyncCallWS.WS_UPDATE_CHAM_NO_CALL(MA_DVIQLY, MA_TNGAN, TT_HDON);
                    JSONArray arr = new JSONArray(result);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject jO = arr.getJSONObject(i);
                        if (jO.getString("KET_QUA").equals("3")) {
                            connection.updateTrangThaiByID(jO.getString("ID_HDON"), 3);
                        } else if (jO.getString("KET_QUA").equals("6")) {
                            connection.updateTrangThaiByID(jO.getString("ID_HDON"), 6);
                        } else if (jO.getString("KET_QUA").equals("7")) {
                            connection.updateTrangThaiByID(jO.getString("ID_HDON"), 7);
                        } else {
                            connection.updateTrangThaiByID(jO.getString("ID_HDON"), 3);
                        }
                    }
                    countDBo = jsonArr.length();
                }
            } else {
                Common.showAlertDialogGreen(InvoiceTongHopFragment.this.getActivity(), "Thông báo", Color.WHITE, "Bạn phải kết nối mạng", Color.WHITE, "OK", Color.WHITE);
            }
        } catch (Exception ex) {
            ex.toString();
        }
    }

    public void launchBarDialog() {
        progressDialogSent = ProgressDialog.show(InvoiceTongHopFragment.this.getActivity(), "Please Wait ...", "Đang gửi dữ liệu ...", true, false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    dongBo();
                    handlerSent.sendEmptyMessage(0);
//                    InvoiceTongHopFragment.this.getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(InvoiceTongHopFragment.this.getActivity(), "Đồng bộ thành công!", Toast.LENGTH_LONG).show();
//                            tvSoHDChuaDongBo.setText("" + connection.countHDChuaDongBo(InvoiceCommon.getUSERNAME()));
//                            progressDialogSent.dismiss();
//                        }
//                    });
                } catch (Exception e) {
                    e.toString();
                }
            }
        }).start();
    }

    private void showProgressDialog(){
        progressDialog = ProgressDialog.show(InvoiceTongHopFragment.this.getActivity(), "Please Wait ...", "Đang lấy dữ liệu ...", true, false);
        Thread thread = new Thread(InvoiceTongHopFragment.this);
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
                    if (connection.insertDataKHACHHANG(object.getInt("THANG_HT"), object.getInt("NAM_HT"), object.getString("MA_DVIQLY"),
                            object.getInt("ID_HDON"), object.getString("LOAI_HDON"), object.getString("LOAI_PSINH"), object.getString("MA_KHANG"),
                            object.getString("MA_KHTT"), object.getString("TEN_KHANG"), object.getString("TEN_KHANG1"), object.getString("TEN_KHTT"),
                            object.getString("DCHI_KHANG"), object.getString("DCHI_KHTT"), object.getInt("LOAI_KHANG"), object.getString("MANHOM_KHANG"),
                            object.getString("NAM_PS"), object.getString("THANG_PS"), object.getString("KY_PS"), object.getString("TIEN_PSINH"),
                            object.getString("THUE_PSINH"), object.getString("MA_SOGCS"), object.getInt("STT_TRANG"), object.getString("STT"),
                            object.getInt("TTRANG_SSAI"), object.getInt("LAN_GIAO"), object.getString("HTHUC_TTOAN"), object.getInt("LOAI_TBAO"),
                            object.getString("NGAY_PHANH"), object.getString("DTHOAI_KHANG"), object.getString("DIEN_THOAI"), object.getString("DTHOAI_NONG"),
                            object.getString("DTHOAI_TRUC"), object.getString("SO_SERY"), object.getString("NGAY_DKY"), object.getString("NGAY_CKY"),
                            object.getString("NGAY_GIAO"), object.getInt("SO_BBGIAO"), object.getString("MA_TNGAN"), object.getString("TIEN_NO"),
                            object.getString("THUE_NO"), object.getString("NGAY_NOP"), object.getInt("TINH_TRANG"), 1,
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
                        initData();
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

    private void printImage(byte[] bytes) {
        try {
            byte[] formats  = { (byte)0x1B, (byte)0x58, (byte)0x31, (byte)0x24, (byte)0x2D };

            //byte[] formats	= {(byte) 0x1B, (byte) 0x58, (byte) 0x34, (byte) 0x35, (byte) 0x118	};
            byte[] bytes2	= new byte[formats.length + bytes.length];

            System.arraycopy(formats, 0, bytes2, 0, formats.length);
            System.arraycopy(bytes, 0, bytes2, formats.length, bytes.length);

            sendData(bytes2);

            byte[] newline  = Printer.printfont("\n\n",FontDefine.FONT_32PX,FontDefine.Align_LEFT,(byte)0x1A,PocketPos.LANGUAGE_ENGLISH);

            sendData(newline);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap toGrayscale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    public byte[] getByteArray(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();
    }

    public static Bitmap createBlackAndWhite(Bitmap src) {
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;

        // scan through all pixels
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                int gray = (int) (0.2989 * R + 0.5870 * G + 0.1140 * B);

                // use 128 as threshold, above -> white, below -> black
                if (gray > 128)
                    gray = 255;
                else
                    gray = 0;
                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, gray, gray, gray));
            }
        }
        return bmOut;
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b & 0xff));
        return sb.toString();
    }

    private void sendData(byte[] bytes) {
        try {
            mConnector.sendData(bytes);
        } catch (P25ConnectionException e) {
            e.printStackTrace();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            setData();
            progressDialog.dismiss();
        }
    };

    private Handler handlerSent = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(countDBo > 0)
                Toast.makeText(InvoiceTongHopFragment.this.getActivity(), "Đồng bộ thành công!", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(InvoiceTongHopFragment.this.getActivity(), "Đồng bộ thất bại!", Toast.LENGTH_LONG).show();
            tvSoHDChuaDongBo.setText("" + connection.countHDChuaDongBo(InvoiceCommon.getUSERNAME()));
            progressDialogSent.dismiss();
        }
    };

    @Override
    public void run() {
        try {
            Thread.sleep(500);
            getPS(connection.getMaDVIByUser(InvoiceCommon.getUSERNAME()), Common.GetIMEI(InvoiceTongHopFragment.this.getActivity()), InvoiceCommon.getUSERNAME());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        handler.sendEmptyMessage(0);
    }
}
