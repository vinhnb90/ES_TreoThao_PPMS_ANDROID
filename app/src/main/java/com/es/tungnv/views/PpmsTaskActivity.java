package com.es.tungnv.views;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.es.tungnv.db.PpmsSqliteConnection;
import com.es.tungnv.entity.PpmsEntityNhanVien;
import com.es.tungnv.entity.PpmsEntityTask;
import com.es.tungnv.fragments.PpmsDateTimePickerFragment;
import com.es.tungnv.fragments.PpmsDoiSoatFragment;
import com.es.tungnv.fragments.PpmsReportFragment;
import com.es.tungnv.fragments.PpmsTaskFragment;
import com.es.tungnv.interfaces.IPpmsTasks;
import com.es.tungnv.interfaces.ITasksChoose;
import com.es.tungnv.utils.Common;
import com.es.tungnv.utils.Data;
import com.es.tungnv.utils.PpmsCommon;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.es.tungnv.utils.PpmsCommon.getJSONData;

public class PpmsTaskActivity extends FragmentActivity implements ITasksChoose, IPpmsTasks, ISearchVisible {
    //TODO Khai báo biến UI
    private Button btnTask, btnHistory, btnReport, btnUpload, btnCheck, btnDownload, btnChooseOrClearAll, btnSearch;
    private LinearLayout llMain, llSearch, llSearchByText, llSearchByDate;
    private EditText etSearchByText;
    private TextView tvChooseDate;
    private RadioButton rdSearchByText, rdSearchByDate;

    //TODO Khai báo biến đối tượng
    private static PpmsEntityNhanVien sEmp;
    private PpmsSqliteConnection connection;
    private PpmsTaskFragment fragTask;
    private PpmsReportFragment fragReport;
    private PpmsDoiSoatFragment fragDoiSoat;
    private static boolean isClickChooseOrClearAll;
    private static boolean isClickBtnSearch;
    private static List<PpmsEntityTask> sListTask = new ArrayList<PpmsEntityTask>();
    private static List<PpmsEntityTask> sListTaskChooseCommit = new ArrayList<PpmsEntityTask>();
    private static List<PpmsEntityTask> sListTaskPrepareCommit = new ArrayList<PpmsEntityTask>();

    //TODO Khai báo biến phục vụ upload
    private ProgressDialog progressDialog;
    int countFileSent = 0;
    private int progressBarStatus = 0;
    private int maxSize = 0;
    private float snapProgresBar = 1f;
    private Handler progressBarHandler = new Handler();

    private Handler handlerUpload = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            StringBuilder sbMsg = new StringBuilder();
            sbMsg.append("Đã gửi lên máy chủ ").append(countFileSent).append("/").append(sListTaskChooseCommit.size()).append(" biên bản\n");
            Common.showAlertDialogGreen(PpmsTaskActivity.this, "Thông báo", Color.WHITE,
                    sbMsg.toString(), Color.WHITE, "OK", Color.WHITE);
            countFileSent = 0;
            progressDialog.dismiss();
            Fragment fragTaskVisible = getSupportFragmentManager().findFragmentById(R.id.ll_ppms_task_main);
            if (fragTaskVisible != null && fragTaskVisible.isVisible() && fragTaskVisible instanceof PpmsDoiSoatFragment) {
                ((PpmsDoiSoatFragment) fragTaskVisible).setDataRecyclerViewDoiSoat();
            }
        }
    };

    //TODO Khai báo biến phục vụ download
    private long countFileDownload = 0;

    //region region override
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ppms_activity_task);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        try {
            initViews();
            initValues();
            getBundel();
            setAction();
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        PpmsCommon.LoadFolder(PpmsTaskActivity.this);

        //TODO check which fragment is visible and refresh fragment to update UI
        //TODO check task fragment
        PpmsTaskFragment fragTaskVisible = (PpmsTaskFragment) getSupportFragmentManager().findFragmentByTag("FRAG_TASK");
        if (fragTaskVisible != null && fragTaskVisible.isVisible()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.detach(this.fragTask);
            ft.attach(this.fragTask);
            ft.commit();
        }

        //TODO check report fragment
        PpmsReportFragment fragReportVisible = (PpmsReportFragment) getSupportFragmentManager().findFragmentByTag("FRAG_REPORT");
        if (fragReportVisible != null && fragReportVisible.isVisible()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.detach(this.fragReport);
            ft.attach(this.fragReport);
            ft.commit();
        }

        //TODO check doi soat fragment
        PpmsDoiSoatFragment fragDoiSoatVisible = (PpmsDoiSoatFragment) getSupportFragmentManager().findFragmentByTag("FRAG_CHECK");
        if (fragDoiSoatVisible != null && fragDoiSoatVisible.isVisible()) {
            Bundle idFrag = new Bundle();
            idFrag.putParcelable("EMPLOYEE", sEmp);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.detach(this.fragDoiSoat);
            fragDoiSoat = new PpmsDoiSoatFragment();
            fragDoiSoat.setArguments(idFrag);
            ft.attach(this.fragDoiSoat);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (llSearch.getVisibility() == View.GONE) {
            super.onBackPressed();
        }
        clickSearchMenu(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isClickChooseOrClearAll = false;
        sListTask.clear();
        sListTaskChooseCommit.clear();
        sListTaskPrepareCommit.clear();
        isClickBtnSearch = false;

    }

    @Override
    public void sendIClickChooseArray(boolean[] isClickChooseTaskArray, List<PpmsEntityTask> listTaskPrepareCommit) {
        if (isClickChooseTaskArray.length != 0) {
            sListTaskChooseCommit.clear();
            for (int i = 0; i < isClickChooseTaskArray.length; i++) {
                if (isClickChooseTaskArray[i]) {
                    sListTaskChooseCommit.add(listTaskPrepareCommit.get(i));
                }
            }
        }
    }

    @Override
    public void sendITaskArray(List<PpmsEntityTask> listTask) {
        sListTask.clear();
        if (sListTask.size() == 0) {
            sListTask.addAll(listTask);
        }
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PpmsCommon.REQUEST_CODE_DOISOAT_TO_DETAIL
                && resultCode == PpmsCommon.RESPONSE_CODE_DETAIL_TO_DOISOAT
                && data != null) {
            //TODO update List
            Bundle bundle = data.getExtras();
            int pos = bundle.getInt("POSITION", -1);
            PpmsEntityTask task = bundle.getParcelable("TASK");
            if (pos != -1 && task != null) {
//                adapterRviewDoiSoat.updateTask(pos, task);
            }
        }
    }
*/
    //endregion

    //region region method onClick on Task Activity

    public void clickTaskMenu(View view) {

        //TODO scale button
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_image));

        //TODO reset again
        isClickBtnSearch = false;

        //TODO set arguments if not aready fragment visible
        fragTask = new PpmsTaskFragment();
        Bundle idFrag = new Bundle();
        idFrag.putString("WHICH_FRAG", "TASK_FRAG");
        idFrag.putParcelable("EMPLOYEE", sEmp);
        fragTask.setArguments(idFrag);

        //TODO replace fragReport
        getSupportFragmentManager().beginTransaction().replace(R.id.ll_ppms_task_main, fragTask, "FRAG_TASK").commit();

        //TODO show visible bottom menu
        setVisibleMenuBottom(1);
    }

    public void clickHistoryMenu(View view) {

        //TODO scale button
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_image));

        //TODO reset again
        isClickBtnSearch = false;

        //TODO set arguments if not aready fragment visible
        fragTask = new PpmsTaskFragment();
        Bundle idFrag = new Bundle();
        idFrag.putString("WHICH_FRAG", "HISTORY_FRAG");
        idFrag.putParcelable("EMPLOYEE", sEmp);
        fragTask.setArguments(idFrag);
        getSupportFragmentManager().beginTransaction().replace(R.id.ll_ppms_task_main, fragTask, "FRAG_TASK").commit();

        //TODO show visible bottom menu
        setVisibleMenuBottom(2);
    }

    public void clickReportMenu(View view) {
        //TODO scale button
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_image));

        //TODO visible menu bottom
        setVisibleMenuBottom(3);

        //TODO set arguments if not aready fragment visible
        fragReport = new PpmsReportFragment();
        Bundle idFrag = new Bundle();
        idFrag.putString("WHICH_FRAG", "REPORT_FRAG");
        idFrag.putParcelable("EMPLOYEE", sEmp);
        fragReport.setArguments(idFrag);

        //TODO replace fragReport
        getSupportFragmentManager().beginTransaction().replace(R.id.ll_ppms_task_main, fragReport, "FRAG_REPORT").commit();
    }

    public void clickDownloadMenu(View view) {
        //TODO scale button
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_image));

        if (sEmp != null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Đang đồng bộ biên bản ...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.show();

            Thread thread = new Thread(new Runnable() {
                @Override
                public synchronized void run() {
                    try {
                        Thread.sleep(50);
                        new AsyncCallTask(PpmsTaskActivity.this, sEmp.getNhanVienId(), false).execute();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();

        }
    }

    public void clickDoiSoatMenu(View view) {
        //TODO scale button
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale_image);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //TODO show menu bottom visible
                setVisibleMenuBottom(4);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);

        //TODO reset again
        isClickBtnSearch = false;

        //TODO send sEmp to fragTask
        fragDoiSoat = new PpmsDoiSoatFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("EMPLOYEE", sEmp);
        fragDoiSoat.setArguments(bundle);

        //TODO replace fragReport
        getSupportFragmentManager().beginTransaction().replace(R.id.ll_ppms_task_main, fragDoiSoat, "FRAG_CHECK").commit();

    }

    public void clickUploadMenu(View view) {
        //TODO scale button
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_image));

        //TODO reset again
        isClickBtnSearch = false;

        if (!Common.isNetworkOnline(PpmsTaskActivity.this)) {
            PpmsCommon.showTitleByDialog(PpmsTaskActivity.this, "Thông báo ", "Bạn chưa có kết nối mạng!", true, 5000);
            return;
        }

        if (sListTaskChooseCommit.size() == 0) {
            PpmsCommon.showTitleByDialog(PpmsTaskActivity.this, "Thông báo ", "Bạn chưa chọn bất kì phiếu phúc tra nào!", false, 3000);
            return;
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Đang gửi biên bản ...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();

        progressBarStatus = 0;
        maxSize = sListTaskChooseCommit.size();
        snapProgresBar = (float) maxSize / 100f;

        //TODO create a thread running for task convert image to byte64 spend more time UI
        Thread thread = new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                try {
                    Thread.sleep(50);

                    for (int i = 0; i < sListTaskChooseCommit.size(); i++) {
                        PpmsEntityTask task = sListTaskChooseCommit.get(i);

                        //TODO check exist file Image
                        File imageFile = new File(task.getAnhCongTo());
                        if (imageFile.exists()) {
                            //TODO get byte Bitmap
                            Bitmap imageBitmap = BitmapFactory.decodeFile(task.getAnhCongTo());
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] imageByte = baos.toByteArray();
                            String byteBimap = Base64.encodeToString(imageByte, Base64.DEFAULT);

                            //TODO get image name
                            String imageName = imageFile.getName();

                            //TODO  set json Object
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("PhanCongId", task.getPhanCongId());
                            jsonObject.put("SaiChiSo", Boolean.parseBoolean(task.getCoSaiChiSo().trim()));
                            jsonObject.put("TinhTrangHopCongTo", task.getTinhTrangNiemPhong());
                            jsonObject.put("NgayPhucTra", task.getNgayPhucTra());
                            jsonObject.put("NhanXetDeXuat", task.getNhanXetDeXuat());
                            jsonObject.put("ChiSoPhucTra", task.getChiSoPhucTra());
                            jsonObject.put("AnhCongTo", byteBimap);
                            jsonObject.put("TenAnhCongTo", imageName);

                            //TODO upload task and get json response
                            String stringResponse = new AsyncCallUploadTask(PpmsTaskActivity.this).execute(jsonObject).get();
                            if (stringResponse != "") {
                                if (stringResponse.equals("true")) {
                                    countFileSent++;

                                    //TODO update TRANG_THAI
                                    String queryTRANG_THAI = connection.getQueryUpdateTRANG_THAIWhenUploadSuccess(task.getNhanVienId(), task.getPhanCongId());
                                    connection.runQueryReturnCursor(queryTRANG_THAI);
                                }

                                //TODO update progressBar
                                progressBarStatus = (int) (countFileSent / snapProgresBar);
                                progressBarHandler.post(new Runnable() {
                                    public void run() {
                                        progressDialog.setProgress(progressBarStatus);
                                    }
                                });
                            }
                        }


                    }
                    handlerUpload.sendEmptyMessage(0);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ;
            }
        });
        thread.start();
    }

    public void clickChooseOrClearAllMenu(View view) {
        //TODO scale button
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_image));

        //TODO reset again
        isClickBtnSearch = false;

        if (!isClickChooseOrClearAll)
            isClickChooseOrClearAll = true;
        else
            isClickChooseOrClearAll = false;

        PpmsDoiSoatFragment fragDoiSoattVisible = (PpmsDoiSoatFragment) getSupportFragmentManager().findFragmentByTag("FRAG_CHECK");
        if (fragDoiSoattVisible != null && fragDoiSoattVisible.isVisible()) {
            fragDoiSoattVisible.setVisiblechooseOrClearAll(isClickChooseOrClearAll);
        } else {
            isClickChooseOrClearAll = !isClickChooseOrClearAll;
        }

    }

    public void clickSearchMenu(@Nullable View view) {
        //TODO slide ll search from bottom to top and top to bottom
        if (!isClickBtnSearch) {
            Animation bottomUp = AnimationUtils.loadAnimation(this,
                    R.anim.bottom_up);
            llSearch.startAnimation(bottomUp);
            llSearch.setVisibility(View.VISIBLE);
        } else {
//            etSearchByText.setText("");
//            etSearchByText.requestFocus();
            Animation bottomDown = AnimationUtils.loadAnimation(this,
                    R.anim.bottom_down);
            llSearch.startAnimation(bottomDown);
            llSearch.setVisibility(View.GONE);
        }
        isClickBtnSearch = !isClickBtnSearch;
    }

    public void clickChooseDate(View view) {
        PpmsDateTimePickerFragment newFragmentStart = new PpmsDateTimePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("TEXTVIEW_DATE", R.id.ppms_activity_task_tv_choose_date);
        newFragmentStart.setArguments(bundle);
        newFragmentStart.show(getSupportFragmentManager(), "FRAG_PICK_DATE");
    }

    //endregion

    //region region method onClick on Report Fragment
    public void clickChooseMonth(View view) {
        //TODO check report fragment
        PpmsReportFragment fragmentVisible = (PpmsReportFragment) getSupportFragmentManager().findFragmentByTag("FRAG_REPORT");
        if (fragmentVisible != null && fragmentVisible.isVisible()) {
            fragmentVisible.clickViewInFragment(view);
        }

    }

    @Override
    public void setVisibleSearch() {
        if (llSearch.getVisibility() == View.VISIBLE) {
            clickSearchMenu(null);

        }
    }
    //endregion

    //region region call service download task
    class AsyncCallTask extends AsyncTask<Void, String, List<PpmsEntityTask>> {
        private Context context;
        private String mURL;
        private String mDataJSON = "";

        /**
         * Download task from server with state = true is Task is captured
         *
         * @param context
         * @param idNhanvien
         * @param stateTask
         */
        public AsyncCallTask(Context context, int idNhanvien, boolean stateTask) {
            this.context = context;
            this.mURL = Data.getUrlQueryGetTask(idNhanvien, String.valueOf(stateTask));
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialogTask.dismiss();
//            progressDialogTask.show(context, "Thông báo:", "Đang cập nhật dữ liệu biên bản.", true, true);
        }

        @Override
        protected List<PpmsEntityTask> doInBackground(Void... voids) {

            //TODO initial param
            List<PpmsEntityTask> listTask = new ArrayList<>();
            InputStreamReader reader = null;
            Gson gson = new Gson();
            Type type = new TypeToken<List<PpmsEntityTask>>() {
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

            if (mDataJSON.equals("")) {
                publishProgress("Không thể kết nối được với máy chủ.");
            }

            //TODO check data reveice and parse json
            try {
                listTask = new Gson().fromJson(mDataJSON, type);
            } catch (JsonSyntaxException e) {

                //TODO cast mDataJson
                JSONObject jsonObject = null;
                String messageReponse = "";
                try {
                    jsonObject = new JSONObject(mDataJSON);
                    messageReponse = jsonObject.getString("Message");
                } catch (JSONException e1) {
                    publishProgress("Không thể kết nối được với máy chủ.\n Thông tin lỗi: " + e1.getMessage());
                }

                publishProgress(messageReponse);
                return null;
            }
            return listTask;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            //TODO set UI
            final String message = values[0];
            progressBarHandler.post(new Runnable() {
                public void run() {
                    progressDialog.dismiss();
//                    progressDialog.setTitle("Thông báo");
//                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                    progressDialog.setCancelable(true);
//                    progressDialog.setCanceledOnTouchOutside(true);
//                    progressDialog.setProgress(100);
//                    progressDialog.setMessage(message);
                }
            });
            Common.showAlertDialogGreen(PpmsTaskActivity.this, "Thông báo", Color.WHITE,
                    "Cập nhật dữ liệu thành công!", Color.WHITE, "OK", Color.WHITE);
            this.cancel(true);
        }

        @Override
        protected void onPostExecute(List<PpmsEntityTask> listTask) {
            super.onPostExecute(listTask);
            progressDialog.dismiss();
            //TODO check result
            if (listTask.size() == 0) {
                Common.showAlertDialogGreen(PpmsTaskActivity.this, "Thông báo", Color.WHITE,
                        "Không có dữ liệu nào được cập nhật!", Color.WHITE, "OK", Color.WHITE);
                return;
            }

            //TODO set data depart in sqlite
            insertDataTaskToSqlite(listTask);

            //TODO refresh fragment to update UI
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.detach(fragTask);
            ft.attach(fragTask);
            ft.commit();
            Common.showAlertDialogGreen(PpmsTaskActivity.this, "Thông báo", Color.WHITE,
                    "Cập nhật dữ liệu thành công!", Color.WHITE, "OK", Color.WHITE);
        }

        private void insertDataTaskToSqlite(List<PpmsEntityTask> listTask) {
            try {

                //TODO show process bar
                int maxSize = listTask.size();
                float snap = (float) maxSize / 100f;
                countFileDownload = 0;
                //TODO insert into task

                for (int i = 0; i < listTask.size(); i++) {

                    //TODO check task exist
                    PpmsEntityTask task = listTask.get(i);
                    String checkExistRowTask = connection.getQueryCheckTaskExistWithMA_PHAN_CONG(String.valueOf(task.getPhanCongId()));
                    boolean isExistRowTask = connection.isRowExistData(checkExistRowTask);
                    long rowsAffectTask = 0;
                    if (isExistRowTask) {

                        //TODO update if exist
                        //TODO not update collumn  ANH_CTO, SAI_CHI_SO, NGAY_PHUC_TRA, NHAN_XET_PHUC_TRA, CHI_SO_PHUC_TRA, TINH_TRANG_NIEM_PHONG
                        ContentValues contentValuesUpdate = new ContentValues();
                        contentValuesUpdate.put("MA_PHAN_CONG", task.getPhanCongId());
                        contentValuesUpdate.put("MA_NVIEN", task.getMaDonViQLy());
                        contentValuesUpdate.put("MA_DDO", task.getMaDiemDo());
                        contentValuesUpdate.put("MA_SO_GCS", task.getMaSoGcs());
                        contentValuesUpdate.put("MA_KH", task.getMaKhachHang());
                        contentValuesUpdate.put("CHI_SO_MOI", task.getChiSoMoi());
                        contentValuesUpdate.put("CHI_SO_CU", task.getChiSoCu());
                        contentValuesUpdate.put("SAN_LUONG", task.getSanLuong());
                        contentValuesUpdate.put("SO_CTO", task.getSoCongTo());
                        contentValuesUpdate.put("MA_CTO", task.getMaCongTo());
                        contentValuesUpdate.put("NGAY_PHAN_CONG", task.getNgayPhanCong());
                        contentValuesUpdate.put("THANG_KIEM_TRA", task.getThangKiemTra());
                        contentValuesUpdate.put("BUNDLE_MA_NVIEN", task.getNhanVienId());
                        contentValuesUpdate.put("TY_LE_CHENH_LECH", task.getTyLeChenhLech());
                        contentValuesUpdate.put("SAN_LUONG_TB", task.getSanLuongTB());
                        contentValuesUpdate.put("TEN_TRAM", task.getTenTram());
                        contentValuesUpdate.put("HS_NHAN", task.getHeSoNhan());
//                        contentValuesUpdate.put("ANH_CTO", task.getSoHopCongTo());
//                        contentValuesUpdate.put("SAI_CHI_SO", task.getSoOCongTo());
                        contentValuesUpdate.put("TEN_KH", task.getTenKhachHang());
                        contentValuesUpdate.put("DIA_CHI_DUNG_DIEN", task.getDiaChiDungDien());
                        contentValuesUpdate.put("SO_O", task.getSoOCongTo());
                        contentValuesUpdate.put("SO_COT", task.getSoCotCongTo());
                        contentValuesUpdate.put("LOAI_HOP", task.getLoaiHopCongTo());
                        contentValuesUpdate.put("LOAI_CTO", task.getLoaiCongTo());
                        contentValuesUpdate.put("TINH_TRANG_CTO", task.getTinhTrangHopCongTo());
//                        contentValuesUpdate.put("NGAY_PHUC_TRA", task.getTinhTrangSuDungDien());
                        contentValuesUpdate.put("NGAY_GHI_DIEN", task.getNgayGhiDien());
                        contentValuesUpdate.put("MUC_DICH_SD_DIEN", task.getMucDichSuDungDien());
                        contentValuesUpdate.put("TINH_TRANG_SD_DIEN", task.getTinhTrangSuDungDien());
//                        contentValuesUpdate.put("NHAN_XET_PHUC_TRA", task.getTinhTrangSuDungDien());
//                        contentValuesUpdate.put("CHI_SO_PHUC_TRA", task.getTinhTrangSuDungDien());
//                        contentValuesUpdate.put("TINH_TRANG_NIEM_PHONG", task.getTinhTrangSuDungDien());

                        rowsAffectTask = connection.updateData(contentValuesUpdate, connection.TABLE_TASK, connection.MA_PHAN_CONG, String.valueOf(task.getPhanCongId()));
                    } else {

                        ContentValues contentValuesInsert = new ContentValues();
                        contentValuesInsert.put("MA_PHAN_CONG", task.getPhanCongId());
                        contentValuesInsert.put("MA_NVIEN", task.getMaDonViQLy());
                        contentValuesInsert.put("MA_DDO", task.getMaDiemDo());
                        contentValuesInsert.put("MA_SO_GCS", task.getMaSoGcs());
                        contentValuesInsert.put("MA_KH", task.getMaKhachHang());
                        contentValuesInsert.put("CHI_SO_MOI", task.getChiSoMoi());
                        contentValuesInsert.put("CHI_SO_CU", task.getChiSoCu());
                        contentValuesInsert.put("SAN_LUONG", task.getSanLuong());
                        contentValuesInsert.put("SO_CTO", task.getSoCongTo());
                        contentValuesInsert.put("MA_CTO", task.getMaCongTo());
                        contentValuesInsert.put("NGAY_PHAN_CONG", task.getNgayPhanCong());
                        contentValuesInsert.put("THANG_KIEM_TRA", task.getThangKiemTra());
                        contentValuesInsert.put("BUNDLE_MA_NVIEN", task.getNhanVienId());
                        contentValuesInsert.put("TY_LE_CHENH_LECH", task.getTyLeChenhLech());
                        contentValuesInsert.put("SAN_LUONG_TB", task.getSanLuongTB());
                        contentValuesInsert.put("TEN_TRAM", task.getTenTram());
                        contentValuesInsert.put("HS_NHAN", task.getHeSoNhan());
                        contentValuesInsert.put("ANH_CTO", task.getAnhCongTo());
                        contentValuesInsert.put("SAI_CHI_SO", task.getCoSaiChiSo());
                        contentValuesInsert.put("TEN_KH", task.getTenKhachHang());
                        contentValuesInsert.put("DIA_CHI_DUNG_DIEN", task.getDiaChiDungDien());
                        contentValuesInsert.put("SO_O", task.getSoOCongTo());
                        contentValuesInsert.put("SO_COT", task.getSoCotCongTo());
                        contentValuesInsert.put("LOAI_HOP", task.getLoaiHopCongTo());
                        contentValuesInsert.put("LOAI_CTO", task.getLoaiCongTo());
                        contentValuesInsert.put("TINH_TRANG_CTO", task.getTinhTrangHopCongTo());
                        contentValuesInsert.put("NGAY_PHUC_TRA", task.getNgayPhucTra());
                        contentValuesInsert.put("NGAY_GHI_DIEN", task.getNgayGhiDien());
                        contentValuesInsert.put("MUC_DICH_SD_DIEN", task.getMucDichSuDungDien());
                        contentValuesInsert.put("TINH_TRANG_SD_DIEN", task.getTinhTrangSuDungDien());
                        contentValuesInsert.put("NHAN_XET_PHUC_TRA", task.getNhanXetDeXuat());
                        contentValuesInsert.put("CHI_SO_PHUC_TRA", task.getChiSoPhucTra());
                        contentValuesInsert.put("TINH_TRANG_NIEM_PHONG", task.getTinhTrangNiemPhong());

                        //insert with TRANG_THAI = 0 is none write data
                        contentValuesInsert.put("TRANG_THAI", 0);
                        rowsAffectTask = connection.insertData(contentValuesInsert, connection.TABLE_TASK);
                    }

                    if (rowsAffectTask == 0) {
                        progressBarHandler.post(new Runnable() {
                            public void run() {
                                progressDialog.show(context, "Kết quả: ", "Không có biên bản phúc tra nào được cập nhật!", false, true);
                            }
                        });
                        return;
                    } else {
                        countFileDownload++;
                        progressBarStatus = (int) (countFileDownload / snap);
                        progressBarHandler.post(new Runnable() {
                            public void run() {
                                progressDialog.setProgress(progressBarStatus);
                            }
                        });
                    }
                }

                if (countFileDownload == listTask.size()) {
                    progressBarHandler.post(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
//                            progressDialog.setMessage("Cập nhật dữ liệu thành công!");
//                            progressDialog.setProgress(100);
//                            progressDialog.setCanceledOnTouchOutside(true);
//                            progressDialog.setCancelable(true);
                        }
                    });
                }

                return;
            } catch (final SQLiteException sqlex) {
                progressBarHandler.post(new Runnable() {
                    public void run() {
                        progressDialog.show(context, "Lỗi login: ", sqlex.getMessage(), false, true);
                    }
                });
                return;
            }

        }


    }

    //endregion

    //region call service upload task

    public class AsyncCallUploadTask extends AsyncTask<JSONObject, Void, String> {
        private Context context;
        private String mURL;
        private String mJsonData = "";

        public AsyncCallUploadTask(Context context) {
            this.context = context;
            this.mURL = Data.getURLCommitTask();
        }

        @Override
        protected String doInBackground(JSONObject... objects) {
            String result = "";
            mJsonData = objects[0].toString();
            try {
                //tạo connection post
                URL url = new URL(mURL);
                HttpURLConnection client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                client.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                client.setDoOutput(true);
                client.setDoInput(true);

                //Đẩy luồng byte utf-8 dữ liệu JSON lên service
                OutputStream os = client.getOutputStream();
                os.write(mJsonData.getBytes("UTF-8"));
                os.flush();
                os.close();

                // đọc object json gửi về
                InputStream in = new BufferedInputStream(client.getInputStream());
                result = IOUtils.toString(in, "UTF-8");

                //close
                in.close();
                client.disconnect();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (Exception e3) {
                e3.printStackTrace();
            }
            return result;
        }
    }

    //endregion

    //region region method common
    private void setAction() {

        //TODO set arguments if not aready fragment visible
        Bundle idFrag = new Bundle();
        idFrag.putString("WHICH_FRAG", "TASK_FRAG");
        idFrag.putParcelable("EMPLOYEE", sEmp);
        fragTask.setArguments(idFrag);

        //TODO set fragment defaul is fragTask
        getSupportFragmentManager().beginTransaction().add(R.id.ll_ppms_task_main, fragTask, "FRAG_TASK").commit();

        //TODO show visible bottom menu
        setVisibleMenuBottom(1);

        rdSearchByText.post(new Runnable() {
            @Override
            public void run() {
                rdSearchByText.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (rdSearchByText.isChecked()) {
                            llSearchByText.setVisibility(View.VISIBLE);
                            rdSearchByDate.setChecked(false);
                            llSearchByDate.setVisibility(View.GONE);
                        } else {
                            llSearchByText.setVisibility(View.GONE);
                            rdSearchByDate.setChecked(true);
                            llSearchByDate.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });

        rdSearchByDate.post(new Runnable() {
            @Override
            public void run() {
                rdSearchByDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (rdSearchByDate.isChecked()) {
                            llSearchByDate.setVisibility(View.VISIBLE);
                            rdSearchByText.setChecked(false);
                            llSearchByText.setVisibility(View.GONE);
                        } else {
                            llSearchByDate.setVisibility(View.GONE);
                            rdSearchByText.setChecked(true);
                            llSearchByText.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });

        etSearchByText.post(new Runnable() {
            @Override
            public void run() {
                etSearchByText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (sListTask.size() == 0) {
                            return;
                        }
                        try {
                            //TODO check fragment present visible, if it include recyler correct then refresh data on that fragment
                            Fragment fragTaskVisible = getSupportFragmentManager().findFragmentById(R.id.ll_ppms_task_main);
                            if (fragTaskVisible != null && fragTaskVisible.isVisible()) {

                                //TODO create a new list task by query fitle
                                List<PpmsEntityTask> data = new ArrayList<PpmsEntityTask>();
                                String textWatcher = Common.removeAccent(s.toString().trim().toLowerCase());

                                //TODO fitle data with query
                                //TODO if is PpmsTaskFragment
                                if (fragTaskVisible instanceof PpmsTaskFragment) {

                                    //TODO search by TenKhachHang or DiaChiDungDien
                                    for (PpmsEntityTask entity : sListTask) {
                                        if (Common.removeAccent(entity.getTenKhachHang().toLowerCase()).contains(textWatcher)
                                                || Common.removeAccent(entity.getDiaChiDungDien().toLowerCase()).contains(textWatcher)
                                                || Common.removeAccent(entity.getMaSoGcs().toLowerCase()).contains(textWatcher)
                                                ) {
                                            data.add(entity);
                                        }
                                    }

                                    //TODO refresh data with new data
                                    ((PpmsTaskFragment) fragTaskVisible).updateList(data);
                                }

                                //TODO if is PpmsDoiSoatFragment
                                if (fragTaskVisible instanceof PpmsDoiSoatFragment) {


                                }
                            }
                        } catch (Exception ex) {
                            Toast.makeText(PpmsTaskActivity.this, "Lỗi tìm kiếm:\n" + ex.toString(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }
        });

        tvChooseDate.post(new Runnable() {
            @Override
            public void run() {
                tvChooseDate.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (sListTask.size() == 0) {
                            return;
                        }
                        try {
                            //TODO check fragment present visible, if it include recyler correct then refresh data on that fragment
                            Fragment fragTaskVisible = getSupportFragmentManager().findFragmentById(R.id.ll_ppms_task_main);
                            if (fragTaskVisible != null && fragTaskVisible.isVisible()) {

                                //TODO create a new list task by query fitle
                                List<PpmsEntityTask> data = new ArrayList<PpmsEntityTask>();
                                String textWatcher = Common.removeAccent(s.toString().trim().toLowerCase());

                                //TODO fitle data with query
                                //TODO if is PpmsTaskFragment
                                if (fragTaskVisible instanceof PpmsTaskFragment) {

                                    //TODO search by TenKhachHang or DiaChiDungDien
                                    for (PpmsEntityTask entity : sListTask) {

                                        //TODO convert tvDate 2016-07-11T01:01:01 to 11/07/2016
                                        String NgayPhanCong = "", NgayPhucTra = "";
                                        if (!entity.getNgayPhanCong().equals(""))
                                            NgayPhanCong = PpmsCommon.convertDateTimeType(entity.getNgayPhanCong(), 1);
                                        if (!entity.getNgayPhucTra().equals(""))
                                            NgayPhucTra = PpmsCommon.convertDateTimeType(entity.getNgayPhucTra(), 1);

                                        if (Common.removeAccent(NgayPhanCong.toLowerCase()).contains(textWatcher)
                                                || Common.removeAccent(NgayPhucTra.toLowerCase()).contains(textWatcher)) {
                                            data.add(entity);
                                        }
                                    }

                                    //TODO refresh data with new data
                                    ((PpmsTaskFragment) fragTaskVisible).updateList(data);
                                }

                                //TODO if is PpmsDoiSoatFragment
                                if (fragTaskVisible instanceof PpmsDoiSoatFragment) {


                                }
                            }
                        } catch (Exception ex) {
                            Toast.makeText(PpmsTaskActivity.this, "Lỗi tìm kiếm:\n" + ex.toString(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }
        });
    }

    private void initValues() {
        connection = PpmsSqliteConnection.getInstance(this);
        fragTask = new PpmsTaskFragment();
        fragReport = new PpmsReportFragment();
        fragDoiSoat = new PpmsDoiSoatFragment();
    }

    private void initViews() {

        //TODO init view
        btnTask = (Button) findViewById(R.id.btn_ppms_task_menu);
        btnHistory = (Button) findViewById(R.id.btn_ppms_history_menu);
        btnReport = (Button) findViewById(R.id.btn_ppms_report_menu);
        btnDownload = (Button) findViewById(R.id.btn_ppms_download_menu);
        btnChooseOrClearAll = (Button) findViewById(R.id.btn_ppms_choose_or_clear_all_menu);
        btnCheck = (Button) findViewById(R.id.btn_ppms_check_menu);
        btnUpload = (Button) findViewById(R.id.btn_ppms_upload_menu);
        btnSearch = (Button) findViewById(R.id.btn_ppms_search_menu);
        llMain = (LinearLayout) findViewById(R.id.ll_ppms_task_main);
        llSearch = (LinearLayout) findViewById(R.id.ppms_activity_task_ll_search);
        etSearchByText = (EditText) findViewById(R.id.ppms_activity_task_et_search_by_text);
        tvChooseDate = (TextView) findViewById(R.id.ppms_activity_task_tv_choose_date);
        llSearchByText = (LinearLayout) findViewById(R.id.ppms_activity_task_ll_searchByText);
        llSearchByDate = (LinearLayout) findViewById(R.id.ppms_activity_task_ll_searchByDate);

        rdSearchByText = (RadioButton) findViewById(R.id.ppms_activity_task_rd_searchByText);
        rdSearchByDate = (RadioButton) findViewById(R.id.ppms_activity_task_rd_searchByDate);

        llSearchByText.setVisibility(View.GONE);
        llSearchByDate.setVisibility(View.GONE);

    }

    private void getBundel() throws Exception {
        Intent getIntent = getIntent();
        Bundle bundle = getIntent.getExtras();
        if (bundle == null) {
            throw new Exception("Không có dữ liệu nhân viên.");
        }

        if (bundle.containsKey("EMPLOYEE")) {
            sEmp = bundle.getParcelable("EMPLOYEE");
            return;
        }

        //TODO has user name to sEmp
        if (bundle.containsKey("USER_EMP")) {
            String user = bundle.getString("USER_EMP");
            Cursor cursorEmp = connection.runQueryReturnCursor(connection.getsQueryGetRowEmpWithUsername(user));
            if (cursorEmp == null) {
                throw new Exception("Không có dữ liệu nhân viên.");
            }
            int idEmp = cursorEmp.getInt(cursorEmp.getColumnIndex("BUNDLE_MA_NVIEN"));
            String nameEmp = cursorEmp.getString(cursorEmp.getColumnIndex("NAME_EMP"));
            String phoneEmp = cursorEmp.getString(cursorEmp.getColumnIndex("PHONE_EMP"));
            String adressEmp = cursorEmp.getString(cursorEmp.getColumnIndex("ADRESS_EMP"));
            String emailEmp = cursorEmp.getString(cursorEmp.getColumnIndex("EMAIL_EMP"));
            String userEmp = cursorEmp.getString(cursorEmp.getColumnIndex("USER_EMP"));
            String passEmp = cursorEmp.getString(cursorEmp.getColumnIndex("PASS_EMP"));
            String genderEmp = cursorEmp.getString(cursorEmp.getColumnIndex("GENDER_EMP"));
            int departEmp = cursorEmp.getInt(cursorEmp.getColumnIndex("DEPART_EMP"));
            String stringIsActiveEmp = cursorEmp.getString(cursorEmp.getColumnIndex("IS_ACTIVE_EMP"));
            boolean isActiveEmp = false;
            try {
                isActiveEmp = Boolean.parseBoolean(stringIsActiveEmp);
            } catch (Exception e) {
                throw new Exception("Lỗi khi parse dữ liệu cột IS_ACTIVE_EMP.");
            }
            sEmp = new PpmsEntityNhanVien(idEmp, nameEmp, phoneEmp, adressEmp, emailEmp, userEmp, passEmp, genderEmp, departEmp, isActiveEmp);
            return;
        }
    }

    /**
     * set visible element bottom menu
     *
     * @param stateVisible stateVisible = 1: case fragment task visible
     *                     stateVisible = 2: case fragment history visible
     *                     stateVisible = 3: case fragment report visible
     *                     stateVisible = 4: case fragment doiSoat visible
     */
    private void setVisibleMenuBottom(int stateVisible) {
        btnUpload.setVisibility(View.GONE);
        btnDownload.setVisibility(View.GONE);
        btnCheck.setVisibility(View.GONE);
        btnChooseOrClearAll.setVisibility(View.GONE);
        btnSearch.setVisibility(View.GONE);
        llSearch.setVisibility(View.GONE);

        //TODO set visible
        switch (stateVisible) {
            case 1:
                btnDownload.setVisibility(View.VISIBLE);
                btnCheck.setVisibility(View.VISIBLE);
                btnSearch.setVisibility(View.VISIBLE);
                break;
            case 2:
                btnSearch.setVisibility(View.VISIBLE);
                break;
            case 3:
                break;
            case 4:
                btnUpload.setVisibility(View.VISIBLE);
                btnChooseOrClearAll.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    //endregion
}