package com.es.tungnv.views;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.es.tungnv.db.PpmsSqliteConnection;
import com.es.tungnv.entity.PpmsEntityDepartment;
import com.es.tungnv.entity.PpmsEntityNhanVien;
import com.es.tungnv.sharepref.SharePrefManager;
import com.es.tungnv.utils.Common;
import com.es.tungnv.utils.Data;
import com.es.tungnv.utils.PpmsCommon;
import com.es.tungnv.utils.Security;
import com.es.tungnv.utils.TthtCommon;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.os.Handler;

import static com.es.tungnv.utils.PpmsCommon.getJSONData;

public class PpmsLoginActivity extends AppCompatActivity implements View.OnClickListener {

    //region Khai báo biến UI
    private EditText etIP, etPass;
    private AutoCompleteTextView autoEtUser;
    private TextView tvVersion;
    private Spinner spDviQly;
    private ImageButton ibDownload;
    private Button btLogin;
    private ImageButton ibtDonVi;
    //endregion

    //region khai báo biến sử dụng

    //TODO khai báo biến đối tượng
    private PpmsSqliteConnection connection;
    public static ProgressBar sPBar;
    private static SharePrefManager sSharePrefManager;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.ppms_activity_login);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getSupportActionBar().hide();
            CheckFileConfigExist();
            initComponents();
        } catch (Exception ex) {
            Common.showAlertDialogGreen(this, "Lỗi", Color.WHITE, "Lỗi khởi tạo", Color.WHITE, "OK", Color.WHITE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //TODO reload data to show in folder
        PpmsCommon.LoadFolder(PpmsLoginActivity.this);

        //TODO if has data session then fill auto text
        List<String> userNameList = new ArrayList<>();
        Cursor cursorSession = connection.runQueryReturnCursor(connection.sQueryGetAllRowsSession);
        if (cursorSession != null) {
            do {
                String userName = cursorSession.getString(cursorSession.getColumnIndex("USER_SESSION"));
                userNameList.add(userName);
            } while (cursorSession.moveToNext());
            cursorSession.close();
            fillDataAutoText(userNameList, this, autoEtUser);
        }


        //TODO resume shareRef if has then fill data, else then set data autocomplete editText userName
        String IPShareRef = sSharePrefManager.getSharePref("sharePrefIP", MODE_PRIVATE).getString("IP", "");
        String departCodeShareRef = sSharePrefManager.getSharePref("sharePrefDepart", MODE_PRIVATE).getString("Depart", "");
        String userShareRef = sSharePrefManager.getSharePref("sharePrefUser", MODE_PRIVATE).getString("User", "");
        String passShareRef = sSharePrefManager.getSharePref("sharePrefPass", MODE_PRIVATE).getString("Pass", "");

        if (!departCodeShareRef.equals("") && !userShareRef.equals("") && !passShareRef.equals("")) {
            Cursor cursor = null;
            List<String> listDepart = new ArrayList<>();
            int position = 0;
            try {

                //TODO get data depart from sqlite and fill to spinner depart
                String queryGetAllDepart = connection.getsQueryGetAllRowsDepart;
                cursor = connection.runQueryReturnCursor(queryGetAllDepart);
                if (cursor == null) {
                    throw new SQLiteException("Không lấy được dữ liệu bảng Đơn vị!");
                }

                //TODO get list String depart
                int count = 1;
                do {
                    String departCodeSQLite = cursor.getString(cursor.getColumnIndex("CODE"));
                    String departNameSQLite = cursor.getString(cursor.getColumnIndex("NAME"));

                    //TODO mark position in spinner Depart
                    if (departCodeShareRef.equals(departCodeSQLite)) {
                        position = count;
                    }

                    //TODO add to list Depart
                    listDepart.add(departCodeSQLite + " - " + departNameSQLite);
                    count++;
                } while (cursor.moveToNext());

                cursor.close();
            } catch (SQLiteException sqliteEx) {
                new ProgressDialog(this).show(this, "Lỗi login: ", sqliteEx.getMessage(), false, true);
                return;
            }

            if (position == 0) {
                new ProgressDialog(this).show(this, "Lỗi login: ", "Không có dữ liệu đơn vị!", false, true);
                return;
            }


            etIP.setText(IPShareRef);
            //TODO set data depart on the spinner depart
            setDataDepartOnSpiner(listDepart, this, spDviQly);
            spDviQly.setSelection(position - 1);

            //TODO set user, pass
            autoEtUser.setText(userShareRef);
            etPass.setText(passShareRef);
        }
    }

    @Override
    public void onClick(View view) {
        int idView = view.getId();
        switch (idView) {
            case R.id.ppms_activity_login_ib_loaddvi:

                Data.setURL(etIP.getText().toString());

                sSharePrefManager
                        .getSharePref("sharePrefIP", MODE_PRIVATE)
                        .edit().putString("IP", etIP.getText().toString()).commit();

                //TODO call async depart
                final AsyncCallDepartment asyncCallDepartment = new AsyncCallDepartment(this, spDviQly, connection);
                asyncCallDepartment.execute();

                //TODO maximum time connect 10s
                sPBar.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (sPBar.getVisibility() == View.VISIBLE) {
                            sPBar.setVisibility(View.GONE);
                            PpmsCommon.showTitleByDialog(PpmsLoginActivity.this, "Thông báo ", "Không thể kết nối được với máy chủ !\nThời gian chờ kết nối: 10s.", true, 10000);
                            asyncCallDepartment.cancel(true);
                        }
                    }
                }, 10000);
                break;

            case R.id.ppms_activity_login_bt_login:
                //TODO check input
                Data.setURL(etIP.getText().toString());
                checkInput();
                break;
        }
    }

    private void initComponents() {

        //TODO initial UI
        etIP = (EditText) findViewById(R.id.ppms_activity_login_et_ip);
        autoEtUser = (AutoCompleteTextView) findViewById(R.id.ppms_activity_login_et_username);
        etPass = (EditText) findViewById(R.id.ppms_activity_login_auto_et_password);
        spDviQly = (Spinner) findViewById(R.id.ppms_activity_login_sp_dvi);
        ibDownload = (ImageButton) findViewById(R.id.ppms_activity_login_ib_loaddvi);
        btLogin = (Button) findViewById(R.id.ppms_activity_login_bt_login);
        ibtDonVi = (ImageButton) findViewById(R.id.ppms_activity_login_ib_loaddvi);
        tvVersion = (TextView) findViewById(R.id.ppms_activity_login_tv_version);
        sPBar = (ProgressBar) findViewById(R.id.ppms_activity_login_pbar);

        //TODO initial var
        sSharePrefManager = SharePrefManager.getInstance(this);
        sSharePrefManager.addSharePref("sharePrefIP", MODE_PRIVATE);
        sSharePrefManager.addSharePref("sharePrefDepart", MODE_PRIVATE);
        sSharePrefManager.addSharePref("sharePrefUser", MODE_PRIVATE);
        sSharePrefManager.addSharePref("sharePrefPass", MODE_PRIVATE);
        connection = PpmsSqliteConnection.getInstance(this);

        //TODO set action
        btLogin.setOnClickListener(this);
        ibDownload.setOnClickListener(this);
        try {
            tvVersion.setText(new StringBuilder("Version: ").append(BuildConfig.VERSION_NAME).append(" - Emei: ").append(Common.GetIMEI(this)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void checkInput() {
        final String user = autoEtUser.getText().toString().trim();
        final String pass = etPass.getText().toString().trim();
//        final String passDecode64 = PpmsCommon.convertToMd5(etPass.getText().toString().trim());
        if (user.equals("")) {
            Toast.makeText(this, "Bạn chưa nhập thông tin tài khoản!", Toast.LENGTH_SHORT).show();
            autoEtUser.setError("Không nhập rỗng!");
            return;
        }

        if (pass.equals("")) {
            Toast.makeText(this, "Bạn chưa nhập mật khẩu!", Toast.LENGTH_SHORT).show();
            etPass.setError("Không nhập rỗng!");
            return;
        }

        if (spDviQly.getSelectedItem() == null) {
            Toast.makeText(this, "Bạn chưa chọn đơn vị!", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] departStrings = spDviQly.getSelectedItem().toString().split(" - ");
        final String departCode = departStrings[0];
        final String IP = etIP.getText().toString();
        //TODO check connect wifi to login offline mode
        if (PpmsCommon.isConnectingWifi(this)) {
            final AsyncCallLogin asyncCallLogin = new AsyncCallLogin(this, autoEtUser, etPass, departCode, IP);

            //TODO maximum time connect 10s
            sPBar.setVisibility(View.VISIBLE);
            Handler handlerLogin = new Handler();
            handlerLogin.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (sPBar.getVisibility() == View.VISIBLE) {
                        sPBar.setVisibility(View.GONE);

                        asyncCallLogin.cancel(true);

                        //TODO check login offline mode
                        loginOffline(user, pass, departCode, false);
                    }
                }
            }, 5000);

            asyncCallLogin.execute();
            return;
        }else
        //TODO login offline mode
        loginOffline(user, pass, departCode, false);

    }

    private void loginOffline(final String user, String pass, String departCode, boolean isServerRequestOK) {
        //TODO get data session login saved
        String queryGetSession = connection.getsQueryGetRowSessionWithUsername(user);
        Cursor cursorSession = null;
        cursorSession = connection.runQueryReturnCursor(queryGetSession);

        //TODO if has login then login, else message must to connect wifi before login
        if (cursorSession != null) {
            String codeDepartSession = cursorSession.getString(cursorSession.getColumnIndex("CODE_DEPART"));
            String passSession = cursorSession.getString(cursorSession.getColumnIndex("PASS_SESSION"));

            //TODO compair data input
            //TODO encryption pass shareRef again
            pass = PpmsCommon.convertToMd5(pass);
            if (departCode.equals(codeDepartSession) && pass.equals(passSession)) {

                //TODO check where call method login show message
                if (!isServerRequestOK) {
                    AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                    builder.setTitle("Thông báo:");
                    builder.setMessage("Không thể kết nối được với máy chủ." +
                            "Thời gian chờ kết nối: 5s.\n" +
                            "Tài khoản này có thể đăng nhập offline.");

                    builder.setNegativeButton("OK", new Dialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //TODO call PpmsTaskActivity and pass value USERNAME
                            callTaskActivity(user);
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {

                    //TODO call PpmsTaskActivity and pass value USERNAME
                    callTaskActivity(user);
                }


            } else {
                sPBar.setVisibility(View.GONE);
                PpmsCommon.showTitleByDialog(PpmsLoginActivity.this, "Thông báo ", "Bạn phải kết nối wifi trước khi đăng nhập!", true, 10000);
                return;
            }
            cursorSession.close();
        } else {
            sPBar.setVisibility(View.GONE);
            PpmsCommon.showTitleByDialog(PpmsLoginActivity.this, "Thông báo ", "Bạn phải kết nối wifi trước khi đăng nhập!", true, 10000);
            return;
        }
    }

    //region region call service web api Đăng nhập
    class AsyncCallLogin extends AsyncTask<String, String, PpmsEntityNhanVien> {
        private Context context;
        private String mURL, departCode, IP;
        private String mDataJSON = "";
        private AutoCompleteTextView autoEtUser;
        private EditText etPass;
        private String user, passDecryptionMD5;

        public AsyncCallLogin(Context context, AutoCompleteTextView autoEtUser, EditText etPass, String mDepartCode, String IP) {
            if (context instanceof PpmsLoginActivity) {
                this.context = context;
                this.departCode = mDepartCode;
                this.IP = IP;
                this.autoEtUser = autoEtUser;
                this.etPass = etPass;
                user = this.autoEtUser.getText().toString().trim();
                passDecryptionMD5 = PpmsCommon.convertToMd5(this.etPass.getText().toString().trim());
            } else {
                throw new ClassCastException("Không đúng context truyền vào!");
            }

            this.mURL = Data.getUrlQueryCheckLogin(user, passDecryptionMD5, mDepartCode);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sPBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected PpmsEntityNhanVien doInBackground(String... stringParam) {

            //TODO initial param
            PpmsEntityNhanVien nhanVien = null;
            InputStreamReader reader = null;
            Gson gson = new Gson();
            Type type = new TypeToken<PpmsEntityNhanVien>() {
            }.getType();

            //TODO get data
            try {
                mDataJSON = getJSONData(mURL);
            } catch (Exception e) {

                //TODO check wifi
                if (PpmsCommon.isConnectingWifi(context)) {
                    publishProgress("Không thể kết nối được với máy chủ.");
                } else {
                    publishProgress("Cần phải kết nối mạng wifi.");
                }
                return null;
            }

            //TODO check data reveice and parse json
            if (mDataJSON.equals("")) {
                throw new JsonSyntaxException("Dữ liệu trả về là rỗng.");
            }

            //TODO check nhanVien result
            nhanVien = new Gson().fromJson(mDataJSON, type);
            if (nhanVien.getNhanVienId() == 0) {
                //TODO covert message from web api
                String messageString = mDataJSON.substring("{\"Message\":\"".length() - 1, mDataJSON.length() - 1 - "\"}".length());
                publishProgress(messageString);
                return null;
            }

            return nhanVien;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            //TODO set UI
            String message = values[0];
            sPBar.setVisibility(View.GONE);
            PpmsCommon.showTitleByDialog(context, "Lỗi đăng nhập", message, true, 10000);
            this.cancel(true);
        }

        @Override
        protected void onPostExecute(PpmsEntityNhanVien emp) {
            super.onPostExecute(emp);
            sPBar.setVisibility(View.GONE);

            if (emp.getNhanVienId() == 0) {
                PpmsCommon.showTitleByDialog(context, "Lỗi đăng nhập", "Nhân viên này không có dữ liệu trên máy chủ!", true, 10000);
                return;
            }

            //TODO add save sharePref
            String depart = departCode;

            int idEmp = emp.getNhanVienId();

            sSharePrefManager
                    .getSharePref("sharePrefIP", MODE_PRIVATE)
                    .edit().putString("IP", IP).commit();

            sSharePrefManager
                    .getSharePref("sharePrefDepart", MODE_PRIVATE)
                    .edit().putString("Depart", depart).commit();


            sSharePrefManager
                    .getSharePref("sharePrefUser", MODE_PRIVATE)
                    .edit().putString("User", autoEtUser.getText().toString().trim()).commit();

            sSharePrefManager
                    .getSharePref("sharePrefPass", MODE_PRIVATE)
                    .edit().putString("Pass", etPass.getText().toString().trim()).commit();

            //TODO save data employee
            ContentValues contentValuesEmp = new ContentValues();
            contentValuesEmp.put("MA_NVIEN", emp.getNhanVienId());
            contentValuesEmp.put("NAME_EMP", emp.getHoTen());
            contentValuesEmp.put("PHONE_EMP", emp.getSDT());
            contentValuesEmp.put("ADRESS_EMP", emp.getDiaChi());
            contentValuesEmp.put("EMAIL_EMP", emp.getEmail());
            contentValuesEmp.put("USER_EMP", emp.getTenDangNhap());
            contentValuesEmp.put("PASS_EMP", emp.getMatKhau());
            contentValuesEmp.put("GENDER_EMP", emp.getGenderId());
            contentValuesEmp.put("DEPART_EMP", emp.getPpmsEntityDepartmentId());
            contentValuesEmp.put("IS_ACTIVE_EMP", emp.isActive());

            //TODO check exist data to insert or update
            String checkExistRowEmp = connection.getsQueryGetRowEmpWithUsername(String.valueOf(emp.getTenDangNhap()));
            boolean isExistRowEmp = connection.isRowExistData(checkExistRowEmp);
            long rowsAffectEmp = 0;
            if (isExistRowEmp) {
                rowsAffectEmp = connection.updateData(contentValuesEmp, connection.TABLE_EMPLOYEE, connection.MA_NVIEN, String.valueOf(emp.getNhanVienId()));
            } else {
                rowsAffectEmp = connection.insertData(contentValuesEmp, connection.TABLE_EMPLOYEE);
            }
            if (rowsAffectEmp == 0) {
                PpmsCommon.showTitleByDialog(context, "Cảnh báo:", "Không ghi được dữ liệu thông tin nhân viên!", true, 10000);
                return;
            }


            //TODO save session login
            ContentValues contentValuesSession = new ContentValues();
            contentValuesSession.put("CODE_DEPART", departCode);
            contentValuesSession.put("USER_SESSION", user);
            contentValuesSession.put("PASS_SESSION", passDecryptionMD5);
            contentValuesSession.put("MA_NVIEN", idEmp);

            //TODO check exist data to insert or update
            String checkExistRow = connection.getsQueryGetRowSessionWithUsername(user);
            boolean isExistRow = connection.isRowExistData(checkExistRow);
            long rowsAffect = 0;
            if (isExistRow) {
                rowsAffect = connection.updateData(contentValuesSession, connection.TABLE_SESSION, connection.USER_SESION, user);
            } else {
                rowsAffect = connection.insertData(contentValuesSession, connection.TABLE_SESSION);
            }
            if (rowsAffect == 0) {
                PpmsCommon.showTitleByDialog(context, "Cảnh báo:", "Không ghi được dữ liệu thông tin phiên đăng nhập!", true, 10000);
            }

            //TODO call PpmsTaskActivity and pass value EMPLOYEE
            Intent intentCallMain = new Intent(PpmsLoginActivity.this, PpmsTaskActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("EMPLOYEE", emp);
            intentCallMain.putExtras(bundle);
            startActivity(intentCallMain);
        }
    }
    //endregion

    //region region call service web api Cập nhật đơn vị
    class AsyncCallDepartment extends AsyncTask<Void, String, List<PpmsEntityDepartment>> {
        private Context context;
        private Spinner spinDepart;
        private String mURL;
        private String mDataJSON = "";

        public AsyncCallDepartment(Context context, Spinner spinnerDepart, PpmsSqliteConnection connection) {
            this.context = context;
            this.spinDepart = spinnerDepart;
            this.mURL = Data.getUrlQueryGetDepartment();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sPBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<PpmsEntityDepartment> doInBackground(Void... voids) {

            //TODO initial param
            List<PpmsEntityDepartment> listDepart = new ArrayList<>();
            InputStreamReader reader = null;
            Gson gson = new Gson();
            Type type = new TypeToken<List<PpmsEntityDepartment>>() {
            }.getType();

            //TODO get data
            try {
                mDataJSON = getJSONData(mURL);
            } catch (Exception e) {
                publishProgress("Không kết nối được với máy chủ.");
                return null;
            }

            //TODO check data reveice and parse json
            try {
                if (mDataJSON.equals("")) {
                    throw new JsonSyntaxException("Không nhận được dữ liệu từ máy chủ.");
                }
                listDepart = new Gson().fromJson(mDataJSON, type);
            } catch (JsonSyntaxException e) {
                publishProgress(e.getMessage());
                return null;
            }
            return listDepart;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            //TODO set UI
            String message = values[0];
            sPBar.setVisibility(View.GONE);
            PpmsCommon.showTitleByDialog(context, "Lỗi đăng nhập:", message, true, 10000);
            this.cancel(true);
        }

        @Override
        protected void onPostExecute(List<PpmsEntityDepartment> listDepart) {
            super.onPostExecute(listDepart);

            //TODO check result
            sPBar.setVisibility(View.GONE);
            if (listDepart.size() == 0) {
                PpmsCommon.showTitleByDialog(context, "Lỗi đăng nhập:", "Nhân viên này không có dữ liệu trên máy chủ!", true, 10000);
                return;
            }


            //TODO get data depart
            List<String> listNameDepart = new ArrayList<>();
            for (PpmsEntityDepartment department : listDepart) {
                listNameDepart.add(department.getDepartmentCode() + " - " + department.getDepartmentName());
            }

            //TODO set data depart on the spinner depart
            setDataDepartOnSpiner(listNameDepart, context, spinDepart);

            //TODO set data depart in sqlite
            insertDataDepartToSqlite(listDepart);
        }

        private void insertDataDepartToSqlite(List<PpmsEntityDepartment> listDepart) {
            try {

                //TODO delete all row depart
                connection.deleteData(connection.TABLE_DEPART);

                //TODO insert into depart
                for (PpmsEntityDepartment department : listDepart) {
                    ContentValues contentValuesInsert = new ContentValues();
                    contentValuesInsert.put("MA_DEPART", department.getDepartmentId());
                    contentValuesInsert.put("NAME", department.getDepartmentName());
                    contentValuesInsert.put("ADDRESS", department.getAddress());
                    contentValuesInsert.put("PHONE", department.getPhoneNumber());
                    contentValuesInsert.put("EMAIL", department.getEmail());
                    contentValuesInsert.put("MA_PARENT_DEPART", department.getParentId());
                    contentValuesInsert.put("LEVEL_DEPART", department.getDepartmentLevel());
                    contentValuesInsert.put("CODE", department.getDepartmentCode());
                    contentValuesInsert.put("INDEX_DEPART", department.getDepartmentIndex());
                    contentValuesInsert.put("DATE_CREATE", department.getCreateDate());
                    contentValuesInsert.put("USER_CREATE", department.getCreateUser());
                    contentValuesInsert.put("IS_ACTIVED", department.getActive());

                    long rowsAffect = connection.insertData(contentValuesInsert, connection.TABLE_DEPART);
                    if (rowsAffect == 0) {
                        throw new SQLiteException("Không ghi được vào bảng đơn vị!");
                    }
                }
            } catch (SQLiteException sqlex) {
                PpmsCommon.showTitleByDialog(context, "Lỗi đăng nhập:", sqlex.getMessage(), true, 10000);
                return;
            }

        }

    }

    private void setDataDepartOnSpiner(List<String> listDepart, Context context, Spinner spinDepart) {
        //TODO check data input
        try {
            if (listDepart.size() == 0) {
                throw new Exception("Không có dữ liệu đơn vị!");
            }
        } catch (Exception e) {
            sPBar.setVisibility(View.GONE);
            PpmsCommon.showTitleByDialog(PpmsLoginActivity.this, "Lỗi Đăng nhập: ", e.getMessage(), true, 10000);
            return;
        }

        //TODO set data on spin
        ArrayAdapter<String> itemsAdapterDepart = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, listDepart);
        spinDepart.setAdapter(itemsAdapterDepart);
    }
    //endregion

    private void fillDataAutoText(List<String> listUser, Context context, AutoCompleteTextView autoEtUser) {
        ArrayAdapter<String> adapterUser = new ArrayAdapter<String>
                (context, android.R.layout.select_dialog_item, listUser);
        autoEtUser.setThreshold(2);
        autoEtUser.setAdapter(adapterUser);
        autoEtUser.invalidate();
    }

    //region tạo đường dẫn lưu
    private void CheckFileConfigExist() {
        File programDirectory = new File(Environment.getExternalStorageDirectory() + PpmsCommon.PROGRAM_PATH);
        if (!programDirectory.exists()) {
            programDirectory.mkdirs();
        }
        File programDbDirectory = new File(Environment.getExternalStorageDirectory() + PpmsCommon.PROGRAM_DB_PATH);
        if (!programDbDirectory.exists()) {
            programDbDirectory.mkdirs();
        }
        File programSampleDirectory = new File(Environment.getExternalStorageDirectory() + PpmsCommon.PROGRAM_DB_BACKUP_PATH);
        if (!programSampleDirectory.exists()) {
            programSampleDirectory.mkdirs();
        }
        File programPhotoDirectory = new File(Environment.getExternalStorageDirectory() + PpmsCommon.PROGRAM_PHOTOS_PATH);
        if (!programPhotoDirectory.exists()) {
            programPhotoDirectory.mkdirs();
        }
    }


    private void callTaskActivity(String user) {
        Intent intentCallMain = new Intent(PpmsLoginActivity.this, PpmsTaskActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("USER_EMP", user);
        intentCallMain.putExtras(bundle);
        startActivity(intentCallMain);
    }

    //endregion

}