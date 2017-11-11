package es.vinhnb.ttht.entity.sharedpref;

import esolutions.com.esdatabaselib.baseSharedPref.anonation.KeyType;
import esolutions.com.esdatabaselib.baseSharedPref.anonation.SharePref;
import esolutions.com.esdatabaselib.baseSharedPref.anonation.TYPE;
import esolutions.com.esdatabaselib.baseSqlite.anonation.Params;

/**
 * Created by VinhNB on 11/11/2017.
 */

@SharePref(name = "LoginSharePref")
public class LoginSharePref {
    @KeyType(name = "ip")
    public String ip;

    @KeyType(name = "posSpinDvi", TYPE = TYPE.INT)
    public int posSpinDvi;

    @KeyType(name = "user")
    public String user;

    @KeyType(name = "pass")
    public String pass;

    @KeyType(name = "isCheckSave", TYPE = TYPE.BOOLEAN)
    public boolean isCheckSave;

    public LoginSharePref(@Params(name = "ip") String ip,
                          @Params(name = "posSpinDvi") int posSpinDvi,
                          @Params(name = "user") String user,
                          @Params(name = "pass") String pass,
                          @Params(name = "isCheckSave") boolean isCheckSave) {
        this.ip = ip;
        this.posSpinDvi = posSpinDvi;
        this.user = user;
        this.pass = pass;
        this.isCheckSave = isCheckSave;
    }
}
