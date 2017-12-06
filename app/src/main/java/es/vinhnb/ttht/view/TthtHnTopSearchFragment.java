package es.vinhnb.ttht.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import static es.vinhnb.ttht.view.TthtHnBaseActivity.BUNDLE_TAG_MENU;
import static es.vinhnb.ttht.view.TthtHnBaseActivity.BUNDLE_TYPE_TOPMENU;

public class TthtHnTopSearchFragment extends TthtHnBaseFragment {

    private IOnTthtHnTopSearchFragment mListener;

    private Drawable drawable10, drawable11;

    private Unbinder unbinder;
    private IInteractionDataCommon onIDataCommon;

    private String typeSearchString;
    private String messageSearch;

    private SharePrefManager prefManager;


    //menu top
    @BindView(R.id.sp_search)
    Spinner spSearch;

    @BindView(R.id.et_search)
    EditText etSearch;

    @BindView(R.id.ibtn_clearsearch)
    ImageButton ibtnClear;

    @BindView(R.id.iv_spin_search)
    ImageButton ivSpinClick;


    TthtHnSQLDAO mSqlDAO;
    private TthtHnMainActivity.TagMenuTop tagMenuTop;
    private TthtHnMainActivity.TagMenuNaviLeft tagMenuNaviLeft;

    public TthtHnTopSearchFragment() {
        // Required empty public constructor
    }

    public static TthtHnTopSearchFragment newInstance(TthtHnMainActivity.TagMenuNaviLeft tagMenuNaviLeft, TthtHnMainActivity.TagMenuTop tagMenuTop) {
        TthtHnTopSearchFragment fragment = new TthtHnTopSearchFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_TYPE_TOPMENU, tagMenuTop);
        bundle.putSerializable(BUNDLE_TAG_MENU, tagMenuNaviLeft);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            tagMenuNaviLeft = (TthtHnMainActivity.TagMenuNaviLeft) bundle.getSerializable(BUNDLE_TAG_MENU);
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
        View viewRoot = inflater.inflate(R.layout.fragment_tththn_menu_search, container, false);
        unbinder = ButterKnife.bind(TthtHnTopSearchFragment.this, viewRoot);


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
        try {
            if (tagMenuNaviLeft == TthtHnMainActivity.TagMenuNaviLeft.CTO_TREO || tagMenuNaviLeft == TthtHnMainActivity.TagMenuNaviLeft.CTO_THAO) {
                MenuTopSearchSharePref menuTopSearchSharePref = (MenuTopSearchSharePref) prefManager.getSharePrefObject(MenuTopSearchSharePref.class);
                typeSearchString = menuTopSearchSharePref.typeSearchString;
                messageSearch = menuTopSearchSharePref.messageSearch;
            } else {
                typeSearchString = messageSearch = "";
                prefManager.writeDataSharePref(MenuTopSearchSharePref.class, new MenuTopSearchSharePref(typeSearchString, messageSearch));
            }

        } catch (Exception e) {
            e.printStackTrace();
            ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex08.getContent(), e.getMessage(), null);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        drawable10 = ContextCompat.getDrawable(context, R.drawable.xml_tththn_rectangle10);
        drawable11 = ContextCompat.getDrawable(context, R.drawable.xml_tththn_rectangle11);


        if (context instanceof IOnTthtHnTopSearchFragment) {
            mListener = (IOnTthtHnTopSearchFragment) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IOnTthtHnTopSearchFragment");
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
        fillSpinner(tagMenuNaviLeft);
    }

    private void fillSpinner(TthtHnMainActivity.TagMenuNaviLeft tagMenuNaviLeft) {
        ArrayAdapter<String> adapterSearch = null;

        switch (tagMenuNaviLeft) {
            case BBAN_CTO:
                adapterSearch = new ArrayAdapter<>(getActivity(),
                        R.layout.row_tththn_spin_white, Common.TYPE_SEARCH_BBAN.getArray());
                break;
            case TRAM:

                break;
            case CTO_TREO:
            case CTO_THAO:
                adapterSearch = new ArrayAdapter<>(getActivity(),
                        R.layout.row_tththn_spin_white, Common.TYPE_SEARCH_CTO.getArray());
                break;
            case CHUNG_LOAI:
                break;
            case CHITIET_CTO_TREO:
                break;
            case CHITIET_CTO_THAO:
                break;
            case CHITIET_BBAN_TUTI_TREO:
                break;
            case CHITIET_BBAN_TUTI_THAO:
                break;
            case EMPTY1:
                break;
            case LINE1:
                break;
            case LINE2:
                break;
            case DOWNLOAD:
                break;
            case UPLOAD:
                break;
            case HISTORY:
                break;
        }


        if (adapterSearch == null)
            return;


        adapterSearch.setDropDownViewResource(R.layout.row_tththn_spin_dropdown_select_white);
        spSearch.setAdapter(adapterSearch);
    }

    @Override
    public void setAction(Bundle savedInstanceState) throws Exception {
        //menu top
        catchClick();


        if (tagMenuNaviLeft == TthtHnMainActivity.TagMenuNaviLeft.CTO_TREO || tagMenuNaviLeft == TthtHnMainActivity.TagMenuNaviLeft.CTO_THAO) {
            //fillData save state
            MenuTopSearchSharePref topSearchSharePref = (MenuTopSearchSharePref) prefManager.getSharePrefObject(MenuTopSearchSharePref.class);
            this.messageSearch = topSearchSharePref.messageSearch;
            this.typeSearchString = topSearchSharePref.typeSearchString;

            if (!TextUtils.isEmpty(messageSearch) || !TextUtils.isEmpty(typeSearchString)) {
                int posInArray = Common.TYPE_SEARCH_CTO.getPosInArray(typeSearchString);
                spSearch.setSelection(posInArray);
                etSearch.setText(messageSearch);
            }
        }
    }

    private void showTopMenu(View view) {
        //set background menu
        setBackgroundTopMenu(view);


        //set visible
        spSearch.setVisibility(View.VISIBLE);
        etSearch.setVisibility(View.VISIBLE);
        ibtnClear.setVisibility(View.VISIBLE);


        //get info bban tu ti
        if (onIDataCommon.getID_BBAN_TUTI_CTO() == 0) {
            etSearch.setVisibility(View.GONE);
        }
    }

    private void catchClick() {
        ivSpinClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spSearch.performClick();
//                if (!TextUtils.isEmpty(typeSearchString))
//                    etSearch.setText("");
            }
        });

        spSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                if (!TextUtils.isEmpty(typeSearchString))
//                    etSearch.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        ibtnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Common.runAnimationClickView(view, R.anim.tththn_scale_view_pull, DELAY_ANIM / 2);

                    getView().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                etSearch.setText("");
                            } catch (Exception e) {
                                e.printStackTrace();
                                ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex08.getContent(), e.getMessage(), null);
                            }
                        }
                    }, DELAY_ANIM);

                } catch (Exception e) {
                    e.printStackTrace();
                    ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex08.getContent(), e.getMessage(), null);
                }
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    mListener.clickSearch(spSearch.getSelectedItem().toString(), etSearch.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex08.getContent(), e.getMessage(), null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setBackgroundTopMenu(View view) {
        spSearch.setBackground(drawable10);
        etSearch.setBackground(drawable10);
        ibtnClear.setBackground(drawable10);

        if (view.getId() == etSearch.getId()) {
            etSearch.setBackground(drawable11);
        }
        if (view.getId() == spSearch.getId()) {
            spSearch.setBackground(drawable11);
        }
        if (view.getId() == ibtnClear.getId()) {
            ibtnClear.setBackground(drawable11);
        }
    }

    public void refreshTagTopMenu(TthtHnMainActivity.TagMenuNaviLeft tagMenuNaviLeft, TthtHnMainActivity.TagMenuTop tagMenuTop) {
        this.tagMenuNaviLeft = tagMenuNaviLeft;
        this.tagMenuTop = tagMenuTop;
        fillSpinner(tagMenuNaviLeft);
    }

    public void saveInfoSearch() throws Exception {
        MenuTopSearchSharePref menuTopSearchSharePref = new MenuTopSearchSharePref(spSearch.getSelectedItem().toString(), etSearch.getText().toString());
        prefManager.writeDataSharePref(MenuTopSearchSharePref.class, menuTopSearchSharePref);
    }

    public interface IOnTthtHnTopSearchFragment {
        void clickSearch(String typeSearchString, String messageSearch);
    }
}
