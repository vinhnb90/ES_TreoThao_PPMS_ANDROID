package com.esolutions.esloginlib.lib;

import java.util.List;

/**
 * Created by VinhNB on 10/26/2017.
 */

public interface LoginInteface<T> {
    void openMainView(LoginFragment.LoginData mLoginData);

    LoginFragment.ResultLogin checkServerLogin(LoginFragment.LoginData loginData) throws Exception;

    List<T> callServerDepart() throws Exception;

    void saveDBDepart(List<T> list) throws Exception;

    List<T> selectDBDepart();

    void saveDataSharePref(LoginFragment.LoginData loginData) throws Exception;

    LoginFragment.LoginData getDataLoginSharedPref() throws Exception;
}
