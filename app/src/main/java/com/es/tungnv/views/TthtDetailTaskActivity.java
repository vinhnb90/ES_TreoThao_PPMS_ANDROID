package com.es.tungnv.views;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.es.tungnv.adapters.ViewPageAdapter;
import com.es.tungnv.db.TthtSQLiteConnection;
import com.es.tungnv.entity.InfoPhieuTreoThao;
import com.es.tungnv.entity.TthtKHangEntity;
import com.es.tungnv.fragments.TthtGhiCongToFragment;
import com.es.tungnv.fragments.TthtGhiTuTiFragment;
import com.es.tungnv.utils.Common;
import com.es.tungnv.utils.TthtCommon;

import java.util.ArrayList;
import java.util.List;

public class TthtDetailTaskActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private ViewPager viewPager;
    private Fragment treoThaoCongToFrag, treoThaoTuTiFrag;
    private ViewPageAdapter viewPageAdapter;
    private String MA_BDONG;
    private int ID_BBAN_TRTH;
    private int ID_CHITIET_CTO;
    private int POSITION;

    private int ID_BBAN_TUTI;

    private boolean isDoiSoat = false;
    private List<TthtKHangEntity> lstKhangHangTreo = new ArrayList<>();
    private List<TthtKHangEntity> lstKhangHangThao = new ArrayList<>();
    private List<InfoPhieuTreoThao> lstInfoPhieuTreoThao = new ArrayList<>();
    private List<Integer> lstID_TUTI = new ArrayList<>();

    private Button btPre, btNext, btSwitchTreoThao, btGhi;
    private boolean isSwitchThao = false;
    private TthtSQLiteConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ttht_activity_detail_task);
        getBundle();
        //show popup plese waitting
        initView();
        setAction();
    }

    @Override
    public void onBackPressed() {

        //TODO response MA_BDONG to MainActivity
        String MA_BDONG = "";
        Fragment fragmentVisible = viewPageAdapter.getItem(viewPager.getCurrentItem());
        if (fragmentVisible != null && fragmentVisible instanceof TthtGhiCongToFragment) {
            MA_BDONG = ((TthtGhiCongToFragment) fragmentVisible).getMA_BDONG();
        }
        if (fragmentVisible != null && fragmentVisible instanceof TthtGhiTuTiFragment) {
            MA_BDONG = ((TthtGhiTuTiFragment) fragmentVisible).getMA_BDONG();
        }
        Intent intentCallBack = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("MA_BDONG", MA_BDONG);
        intentCallBack.putExtras(bundle);
        setResult(RESULT_OK, intentCallBack);
        super.onBackPressed();
    }

    /*@Override
    public void callBackActionRemoveTagLayout() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                viewPageAdapter = new ViewPageAdapter(getFragmentManager());
                viewPageAdapter.addFragment(treoThaoCongToFrag, "TREO THÁO CÔNG TƠ");
                viewPager.setAdapter(viewPageAdapter);
            }
        });
    }*/

    private void getBundle() {
        Bundle bundle = getIntent().getExtras();
        MA_BDONG = bundle.getString("MA_BDONG", "");
        ID_BBAN_TRTH = bundle.getInt("ID_BBAN_TRTH", 0);
        ID_CHITIET_CTO = bundle.getInt("ID_CHITIET_CTO", 0);
        POSITION = bundle.getInt("POSITION", 0);
        lstInfoPhieuTreoThao = TthtCommon.getListPhieu();

//        objectDataTreo = bundle.getParcelable("OBJECT_DATA_TREO");
//        objectDataThao = bundle.getParcelable("OBJECT_DATA_THAO");
//        lstKhangHangTreo = bundle.getParcelableArrayList("LIST_DATA_TREO");
//        lstKhangHangThao = bundle.getParcelableArrayList("LIST_DATA_THAO");
        isDoiSoat = bundle.getBoolean("IS_DOI_SOAT");
//        int sizeKhang = lstKhangHangTreo.size();
//        for (int i = 0; i < sizeKhang; i++) {
//            if (lstKhangHangTreo.get(i).getTthtBBanEntity().getID_BBAN_TRTH() == ID_BBAN_TRTH) {
//                posObject = i;
//            }
//        }
    }

    private void initView() {
        treoThaoCongToFrag = new TthtGhiCongToFragment();
        treoThaoTuTiFrag = new TthtGhiTuTiFragment();
        btPre = (Button) findViewById(R.id.ttht_activity_detail_task_truoc);
        btNext = (Button) findViewById(R.id.ttht_activity_detail_task_sau);
        btSwitchTreoThao = (Button) findViewById(R.id.ttht_activity_detail_task_btSwitchTreoThao);
        btGhi = (Button) findViewById(R.id.ttht_activity_detail_task_ghi);

        //check điểm đầu và cuối
        if (POSITION == 0) {
            btPre.setEnabled(false);
            btGhi.setEnabled(true);
            btNext.setEnabled(true);
        }

        if (POSITION == lstInfoPhieuTreoThao.size()) {
            btPre.setEnabled(true);
            btGhi.setEnabled(true);
            btNext.setEnabled(false);
        }

        mTabLayout = (TabLayout) findViewById(R.id.tabs_ttht_ac);
        viewPager = (ViewPager) findViewById(R.id.pagers_ttht_ac);
        viewPageAdapter = new ViewPageAdapter(getFragmentManager());
    }

    private void setAction() {
        //TODO send ID_BBAN_TRTH and MA_BDONG to fragment
        Bundle bundle = new Bundle();
        bundle.putInt("ID_BBAN_TRTH", ID_BBAN_TRTH);
        bundle.putInt("ID_CHITIET_CTO", ID_CHITIET_CTO);
        bundle.putString("MA_BDONG", MA_BDONG);
        bundle.putBoolean("IS_DOI_SOAT", isDoiSoat);

        treoThaoCongToFrag.setArguments(bundle);
        treoThaoTuTiFrag.setArguments(bundle);
        connection = TthtSQLiteConnection.getInstance(this);
//        objectData = (MA_BDONG.equals(TthtCommon.arrMaBDong[0])) ? objectDataTreo : objectDataThao;

        //lấy TRANG_THAI_DU_LIEU
        Cursor c1 = connection.getID_CHITIET_CTO(ID_BBAN_TRTH, MA_BDONG);
        Cursor c = connection.getTRANG_THAI_DU_LIEU(ID_CHITIET_CTO);

        int TRANG_THAI_DU_LIEU = c.getInt(c.getColumnIndex("TRANG_THAI_DU_LIEU"));
        if (TRANG_THAI_DU_LIEU == 2) {
            btGhi.setEnabled(false);
        } else {
            btGhi.setEnabled(true);
        }
        c.close();

        if (isDoiSoat) {
            btGhi.setEnabled(false);
        }

        Cursor cursorGetID_BBAN_TUTI = connection.getID_BBANTUTI(ID_CHITIET_CTO);
        if (cursorGetID_BBAN_TUTI != null) {
            cursorGetID_BBAN_TUTI.moveToFirst();
            ID_BBAN_TUTI = cursorGetID_BBAN_TUTI.getInt(cursorGetID_BBAN_TUTI.getColumnIndex("ID_BBAN_TUTI"));
        }
        cursorGetID_BBAN_TUTI.close();

        //TODO set viewpager
        viewPageAdapter.addFragment(treoThaoCongToFrag, MA_BDONG.equalsIgnoreCase(TthtCommon.arrMaBDong[0]) ? "CÔNG TƠ TREO" : "CÔNG TƠ THÁO");

        if (ID_BBAN_TUTI != 0) {
            viewPageAdapter.addFragment(treoThaoTuTiFrag, "THÁO TREO TU TI");
        }
        viewPager.setAdapter(viewPageAdapter);

        mTabLayout.setupWithViewPager(viewPager);
        mTabLayout.setSelectedTabIndicatorColor(Color.GREEN);
        mTabLayout.setSelectedTabIndicatorHeight(3);
        mTabLayout.setTabTextColors(Color.BLACK, Color.WHITE);
    }

    public void onClickSwitchTreoThao(View view) {
        //đổi treo thành tháo hoặc ngược lại
        if (MA_BDONG.equals(TthtCommon.arrMaBDong[0])) {
            MA_BDONG = TthtCommon.arrMaBDong[1];
        } else
            MA_BDONG = TthtCommon.arrMaBDong[0];

        Cursor c = connection.getID_CHITIET_CTO(ID_BBAN_TRTH, MA_BDONG);
        if (c == null)
            return;

        ID_CHITIET_CTO = c.getInt(c.getColumnIndex("ID_CHITIET_CTO"));


        //TODO send ID_BBAN_TRTH and MA_BDONG to fragment
        try {
            refreshData(MA_BDONG);

//            if (!isSwitchThao) {
//                objectData = objectDataThao;
//                refreshData(TthtCommon.arrMaBDong[1]);
//            } else {
//                objectData = objectDataTreo;
//                refreshData(TthtCommon.arrMaBDong[0]);
//            }
            isSwitchThao = !isSwitchThao;
        } catch (Exception e) {
            Common.showAlertDialogGreen(this, "Lỗi (398)", Color.RED, e.getMessage(), Color.WHITE, "OK", Color.RED);
            return;
        }
    }

    public void onClickPre(View view) {
        btNext.setEnabled(true);

        if (POSITION == 0)
            return;
        if (POSITION == 1) {
            btPre.setEnabled(false);
        }
        POSITION--;

        ID_BBAN_TRTH = lstInfoPhieuTreoThao.get(POSITION).getID_BBAN_TRTH();
        ID_CHITIET_CTO = lstInfoPhieuTreoThao.get(POSITION).getID_CHITIET_CTO();

        try {
            refreshData(MA_BDONG);
        } catch (Exception e) {
            Common.showAlertDialogGreen(this, "Lỗi (397)", Color.RED, e.getMessage(), Color.WHITE, "OK", Color.RED);
            return;
        }
    }

    public void refreshData(String MA_BDONG) throws Exception {
        try {
            Fragment fragmentCongTo = null, fragmentTuTi = null;
            fragmentCongTo = viewPageAdapter.getItem(0);
            //TODO Kiểm tra xem đã lưu các dữ liệu công tơ chưa
            if (fragmentCongTo instanceof TthtGhiCongToFragment) {
                ((TthtGhiCongToFragment) fragmentCongTo).checkSaveDataCongTo((TthtGhiCongToFragment) fragmentCongTo);
            }
            List<Fragment> fragmentList = new ArrayList<>();
            List<String> titleList = new ArrayList<>();

            fragmentList.add(fragmentCongTo);
            if (MA_BDONG.equals(TthtCommon.arrMaBDong[0])) {
                titleList.add("TREO CÔNG TƠ");
            } else {
                titleList.add("THÁO CÔNG TƠ");
            }

            //TODO set disable biên bản đã được gửi
            //lấy TRANG_THAI_DU_LIEU
            Cursor c = connection.getTRANG_THAI_DU_LIEU(ID_CHITIET_CTO);

            int TRANG_THAI_DU_LIEU = c.getInt(c.getColumnIndex("TRANG_THAI_DU_LIEU"));

            if (TRANG_THAI_DU_LIEU == 2) {
                btGhi.setEnabled(false);
            } else {
                btGhi.setEnabled(true);
            }

            ID_BBAN_TUTI = 0;
            Cursor cursorGetID_BBAN_TUTI = connection.getID_BBANTUTI(ID_CHITIET_CTO);
            if (cursorGetID_BBAN_TUTI != null && cursorGetID_BBAN_TUTI.getCount() != 0) {
                ID_BBAN_TUTI = cursorGetID_BBAN_TUTI.getInt(cursorGetID_BBAN_TUTI.getColumnIndex("ID_BBAN_TUTI"));
            }

            if (ID_BBAN_TUTI != 0) {
                Cursor cursorGetDataTUTI = connection.getID_TUTI(ID_BBAN_TUTI);
                lstID_TUTI.clear();
                if (cursorGetDataTUTI != null && cursorGetDataTUTI.getCount() != 0) {
                    do {
                        int ID = cursorGetDataTUTI.getColumnIndex("ID");
                        lstID_TUTI.add(ID);
                    } while (cursorGetDataTUTI.moveToNext());
                }

                if (viewPageAdapter.getCount() == 1) {
                    fragmentTuTi = treoThaoTuTiFrag;
                } else {
                    fragmentTuTi = viewPageAdapter.getItem(1);
                    if (fragmentTuTi instanceof TthtGhiTuTiFragment) {
                        ((TthtGhiTuTiFragment) fragmentTuTi).checkSaveDataTuTi((TthtGhiTuTiFragment) fragmentTuTi);
                    }
                }

                fragmentList.add(fragmentTuTi);
                titleList.add("TREO THÁO TU TI");
            }

            viewPager.setAdapter(null);
            viewPageAdapter = new ViewPageAdapter(getFragmentManager());
            for (int i = 0; i < fragmentList.size(); i++) {
                viewPageAdapter.addFragment(fragmentList.get(i), titleList.get(i));
            }
            viewPager.setAdapter(viewPageAdapter);
            viewPager.setCurrentItem(0);
            viewPager.invalidate();
            mTabLayout.setupWithViewPager(viewPager);

            try {
                Cursor cursor = null;
                for (Fragment fragment : fragmentList) {
                    if (fragment instanceof TthtGhiCongToFragment) {
                        cursor = connection.getID_CHITIET_CTO(ID_BBAN_TRTH, MA_BDONG);
                        if(cursor!=null)
                        {
                            cursor.moveToFirst();
                            ID_CHITIET_CTO = cursor.getInt(cursor.getColumnIndex("ID_CHITIET_CTO"));
                        }
                        cursor.close();
                        ((TthtGhiCongToFragment) fragment).refreshDataCongTo(ID_BBAN_TRTH, ID_CHITIET_CTO);
                    }

                    if (fragment instanceof TthtGhiTuTiFragment) {
                        ((TthtGhiTuTiFragment) fragment).refreshDataTuTi(ID_BBAN_TRTH, ID_CHITIET_CTO, ID_BBAN_TUTI);
                    }
                }
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }


    public void onClickGhi(View view) {
        boolean isWriteDataCongToComplete = false, isWriteDataTuTiComplete = false;
        try {
            Fragment fragmentCongTo = viewPageAdapter.getItem(0);
            if (fragmentCongTo != null && fragmentCongTo instanceof TthtGhiCongToFragment) {
                isWriteDataCongToComplete = ((TthtGhiCongToFragment) fragmentCongTo).checkWriteDataCongToComplete((TthtGhiCongToFragment) fragmentCongTo);
            }
            if (!isWriteDataCongToComplete) {
                throw new Exception("Bạn chưa nhập đủ dữ liệu Công tơ");
            }
            Fragment fragmentTuTi = null;
            if (ID_BBAN_TUTI != 0) {
                fragmentTuTi = viewPageAdapter.getItem(1);
                if (fragmentTuTi != null && fragmentTuTi instanceof TthtGhiTuTiFragment) {
                    isWriteDataTuTiComplete = ((TthtGhiTuTiFragment) fragmentTuTi).checkWriteDataTuTiComplete((TthtGhiTuTiFragment) fragmentTuTi);
                }
                if (!isWriteDataTuTiComplete) {
                    throw new Exception("Bạn chưa nhập đủ dữ liệu Tu Ti");
                }
            }
            //TODO write data to database
            if (isWriteDataCongToComplete) {
                ((TthtGhiCongToFragment) fragmentCongTo).saveDataCongTo();
                if (fragmentTuTi != null) {
                    ((TthtGhiTuTiFragment) fragmentTuTi).saveDataTuTi((TthtGhiTuTiFragment) fragmentTuTi);
                }
            }

            Toast.makeText(TthtDetailTaskActivity.this, "Ghi dữ liệu thành công", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Common.showAlertDialogGreen(TthtDetailTaskActivity.this, "Lỗi (396)", Color.RED, e.getMessage(), Color.WHITE, "OK", Color.RED);
        }
    }


    public void onClickNext(View view) {
        btPre.setEnabled(true);
        if (POSITION == lstInfoPhieuTreoThao.size() - 1)
            return;
        if (POSITION == lstInfoPhieuTreoThao.size() - 2) {
            btNext.setEnabled(false);
        }

        POSITION++;

        try {
            ID_BBAN_TRTH = lstInfoPhieuTreoThao.get(POSITION).getID_BBAN_TRTH();
            ID_CHITIET_CTO = lstInfoPhieuTreoThao.get(POSITION).getID_CHITIET_CTO();

            refreshData(MA_BDONG);
        } catch (Exception e) {
            Common.showAlertDialogGreen(this, "Lỗi (395)", Color.RED, e.getMessage(), Color.WHITE, "OK", Color.RED);
            return;
        }
    }

}
