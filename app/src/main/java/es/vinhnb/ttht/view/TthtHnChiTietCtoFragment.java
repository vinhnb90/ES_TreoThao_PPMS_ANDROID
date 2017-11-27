package es.vinhnb.ttht.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.es.tungnv.views.R;
import com.esolutions.esloginlib.lib.LoginFragment;

import es.vinhnb.ttht.common.Common;


public class TthtHnChiTietCtoFragment extends TthtHnBaseFragment {
    private OnFragmentInteractionListener mListener;
    private TthtHnMainActivity.TagMenu tagMenu;
    private LoginFragment.LoginData mLoginData;
    private String mMaNVien;

    public TthtHnChiTietCtoFragment() {
        // Required empty public constructor
    }


    public static TthtHnChiTietCtoFragment newInstance(LoginFragment.LoginData param1, String param2, TthtHnMainActivity.TagMenu tagNew) {
        TthtHnChiTietCtoFragment fragment = new TthtHnChiTietCtoFragment();
        Bundle args = new Bundle();
        args.putParcelable(TthtHnLoginActivity.BUNDLE_LOGIN, param1);
        args.putString(TthtHnLoginActivity.MA_NVIEN, param2);
        args.putSerializable(TthtHnLoginActivity.TAG_MENU, tagNew);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //getBundle
            mLoginData = (LoginFragment.LoginData) getArguments().getParcelable(TthtHnLoginActivity.BUNDLE_LOGIN);
            mMaNVien = getArguments().getString(TthtHnLoginActivity.MA_NVIEN);
            tagMenu = (TthtHnMainActivity.TagMenu) getArguments().getSerializable(TthtHnLoginActivity.TAG_MENU);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_ttht_chitiet_treo, container, false);
        try {
            initDataAndView(viewRoot);
            setAction(savedInstanceState);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return viewRoot;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListenerTthtHnMainFragment");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public TthtHnChiTietCtoFragment switchMA_BDONG( TthtHnMainActivity.TagMenu tagNew) {
        this.tagMenu = tagNew;
        return this;
    }


    //region TthtHnBaseFragment
    @Override
    void initDataAndView(View viewRoot) throws Exception {

    }

    @Override
    void setAction(Bundle savedInstanceState) throws Exception {

    }
    //endregion

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
