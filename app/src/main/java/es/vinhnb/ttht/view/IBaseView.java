package es.vinhnb.ttht.view;

import android.os.Bundle;
import android.view.View;

public interface IBaseView{
     void initDataAndView(View rootView) throws Exception;

     void setAction(Bundle savedInstanceState) throws Exception;
}
