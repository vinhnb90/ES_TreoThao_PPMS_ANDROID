package es.vinhnb.ttht.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.es.tungnv.views.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.vinhnb.ttht.common.Common;
import es.vinhnb.ttht.database.dao.TthtHnSQLDAO;
import es.vinhnb.ttht.entity.sharedpref.MenuTopSearchSharePref;
import esolutions.com.esdatabaselib.baseSharedPref.SharePrefManager;
import esolutions.com.esdatabaselib.baseSqlite.SqlHelper;

import static es.vinhnb.ttht.common.Common.DELAY_ANIM;
import static es.vinhnb.ttht.common.Common.MA_BDONG.B;
import static es.vinhnb.ttht.view.TthtHnBaseActivity.BUNDLE_TYPE_TOPMENU;

public class TthtHnTopUploadFragment extends TthtHnBaseFragment {

    private IOnTthtHnTopUploadFragment mListener;

    private Unbinder unbinder;
    private IInteractionDataCommon onIDataCommon;

    private String typeSearchString;
    private String messageSearch;

    private SharePrefManager prefManager;

    //menu top
    @BindView(R.id.upload_btn_clear_upload)
    Button btnClearUpload;

    //menu top
    @BindView(R.id.upload_sp_search)
    Spinner spSearch;

    @BindView(R.id.upload_et_search)
    EditText etSearch;

    @BindView(R.id.upload_ibtn_clearsearch)
    ImageButton ibtnClear;

    @BindView(R.id.upload_iv_spin_search)
    ImageButton ivSpinClick;


    TthtHnSQLDAO mSqlDAO;
    private TthtHnMainActivity.TagMenuTop tagMenuTop;
    private boolean isSelectSpin;

    public TthtHnTopUploadFragment() {
        // Required empty public constructor
    }

    public static TthtHnTopUploadFragment newInstance(TthtHnMainActivity.TagMenuTop tagMenuTop) {
        TthtHnTopUploadFragment fragment = new TthtHnTopUploadFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_TYPE_TOPMENU, tagMenuTop);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            tagMenuTop = (TthtHnMainActivity.TagMenuTop) bundle.getSerializable(BUNDLE_TYPE_TOPMENU);
        }


        if (getContext() instanceof IInteractionDataCommon)
            this.onIDataCommon = (IInteractionDataCommon) getContext();
        else
            throw new ClassCastException("context must be implemnet IInteractionDataCommon!");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_tththn_top_upload, container, false);
        unbinder = ButterKnife.bind(TthtHnTopUploadFragment.this, viewRoot);

        try {
            initDataAndView(viewRoot);
            setAction(savedInstanceState);
        } catch (Exception e) {
            e.printStackTrace();
            ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex03.getContent(), e.getMessage(), null);
        }


        return viewRoot;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof IOnTthtHnTopUploadFragment) {
            mListener = (IOnTthtHnTopUploadFragment) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IOnTthtHnTopUploadFragment");
        }


        try {
            mSqlDAO = new TthtHnSQLDAO(SqlHelper.getIntance().openDB(), context);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //region TthtHnBaseFragment
    @Override
    public void initDataAndView(View viewRoot) throws Exception {
        //get share pref
        prefManager = SharePrefManager.getInstance();

        //show spinner
        fillSpinner();
    }


    private void fillSpinner() {
        ArrayAdapter<String> adapterSearch = new ArrayAdapter<String>(getActivity(),
                R.layout.row_tththn_spin_white, Common.TYPE_SEARCH_UPLOAD.getArrayShow());

        if (adapterSearch == null)
            return;


        adapterSearch.setDropDownViewResource(R.layout.row_tththn_spin_dropdown_select_white);
        spSearch.setAdapter(adapterSearch);
    }

    @Override
    public void setAction(Bundle savedInstanceState) throws Exception {
        //menu top
        catchClick();
    }

    private void showTopMenu(View view) {
        //set background menu

        //set visible
        spSearch.setVisibility(View.VISIBLE);
        etSearch.setVisibility(View.VISIBLE);
        ibtnClear.setVisibility(View.VISIBLE);


        //get info bban tu ti
        if (onIDataCommon.getMA_BDONG() == B) {
            if (onIDataCommon.getID_BBAN_TUTI_CTO_TREO() == 0) {
                etSearch.setVisibility(View.GONE);
            }
        } else {
            if (onIDataCommon.getID_BBAN_TUTI_CTO_THAO() == 0) {
                etSearch.setVisibility(View.GONE);
            }
        }
    }

    private void catchClick() {

        ivSpinClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spSearch.performClick();
            }
        });

        ibtnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getView().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        etSearch.setText("");
                    }
                }, DELAY_ANIM);
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!isSelectSpin)
                    mListener.clickSearchUploadTop(spSearch.getSelectedItem().toString(), etSearch.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnClearUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.clickClearUpload();
            }
        });
    }


    public interface IOnTthtHnTopUploadFragment {
        void clickSearchUploadTop(String typeSearchString, String messageSearch);

        void clickClearUpload();
    }
}
