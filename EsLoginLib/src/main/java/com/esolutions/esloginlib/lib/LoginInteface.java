package com.esolutions.esloginlib.lib;

import java.util.List;

/**
 * Created by VinhNB on 10/26/2017.
 */

public interface LoginInteface<T> {
    void openMainView();

    List<T> callServerDepart();

    boolean checkServerLogin(LoginFragment.LoginSharePrefData loginSharePrefData);

    void saveDBDepart(List<T> list) throws Exception;

    List<T> selectDBDepart();

    void saveDataSharePref(LoginFragment.LoginSharePrefData loginSharePrefData) throws Exception;

    LoginFragment.LoginSharePrefData getDataLoginSharedPref() throws Exception;
}
