package es.vinhnb.ttht.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by VinhNB on 11/22/2017.
 */

public abstract class TthtHnBaseFragment extends Fragment {
    abstract void initDataAndView(View viewRoot) throws Exception;

    abstract void setAction(Bundle savedInstanceState) throws Exception;
}
