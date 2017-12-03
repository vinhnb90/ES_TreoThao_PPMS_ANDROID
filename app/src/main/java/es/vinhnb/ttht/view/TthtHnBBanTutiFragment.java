package es.vinhnb.ttht.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.es.tungnv.views.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.vinhnb.ttht.common.Common;

public class TthtHnBBanTutiFragment extends TthtHnBaseFragment {

    private IOnTthtHnBBanTutiFragment mListener;
    private Unbinder unbinder;

    public TthtHnBBanTutiFragment() {
        // Required empty public constructor
    }

    public static TthtHnBBanTutiFragment newInstance() {
        TthtHnBBanTutiFragment fragment = new TthtHnBBanTutiFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_ttht_hn_bban_tuti, container, false);
        unbinder = ButterKnife.bind(TthtHnBBanTutiFragment.this, viewRoot);


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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IOnTthtHnBBanTutiFragment) {
            mListener = (IOnTthtHnBBanTutiFragment) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IOnTthtHnBBanTutiFragment");
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

    }

    @Override
    public void setAction(Bundle savedInstanceState) throws Exception {

    }
    //endregion
    public interface IOnTthtHnBBanTutiFragment {
    }
}
