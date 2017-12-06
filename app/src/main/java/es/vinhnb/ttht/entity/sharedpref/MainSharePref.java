package es.vinhnb.ttht.entity.sharedpref;

import esolutions.com.esdatabaselib.baseSharedPref.anonation.KeyType;
import esolutions.com.esdatabaselib.baseSharedPref.anonation.SharePref;
import esolutions.com.esdatabaselib.baseSharedPref.anonation.TYPE;
import esolutions.com.esdatabaselib.baseSqlite.anonation.Params;

/**
 * Created by VinhNB on 12/6/2017.
 */

@SharePref(name = "MainSharePref")
public class MainSharePref {
    @KeyType(name = "posClicked", TYPE = TYPE.INT)
    public int posClicked;

    @KeyType(name = "sizeList", TYPE = TYPE.INT)
    public int sizeList;

    @KeyType(name = "tagMenuNaviLeft")
    public String tagMenuNaviLeft;

    public MainSharePref(@Params(name = "posClicked") int posClicked, @Params(name = "sizeList") int sizeList, @Params(name =  "tagMenuNaviLeft") String tagMenuNaviLeft) {
        this.posClicked = posClicked;
        this.sizeList = sizeList;
        this.tagMenuNaviLeft = tagMenuNaviLeft;
    }
}
