package es.vinhnb.ttht.view;

import com.esolutions.esloginlib.lib.LoginFragment;

import es.vinhnb.ttht.common.Common;

public interface IInteractionDataCommon {
    int getID_BBAN_TRTH();

    int getID_BBAN_TUTI_CTO();

    Common.MA_BDONG getMA_BDONG();

    LoginFragment.LoginData getLoginData();

    String getMaNVien();


    void setID_BBAN_TRTH(int ID_BBAN_TRTH);

    void setID_BBAN_TUTI_CTO(int ID_BBAN_TUTI_CTO);

    void setMA_BDONG(Common.MA_BDONG MA_BDONG);

    void setLoginData(LoginFragment.LoginData mLoginData);

    void setMaNVien(String mMaNVien);



    void setVisiblePbarLoad(boolean isShow);

    void setMenuNaviAndTitle(TthtHnMainActivity.TagMenuNaviLeft tagMenuNaviLeft);

    void setNextCto(int posOld);

    void setPreCto(int posOld);
}
