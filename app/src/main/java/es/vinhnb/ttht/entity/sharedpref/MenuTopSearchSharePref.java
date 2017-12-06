package es.vinhnb.ttht.entity.sharedpref;

import esolutions.com.esdatabaselib.baseSharedPref.anonation.KeyType;
import esolutions.com.esdatabaselib.baseSharedPref.anonation.SharePref;
import esolutions.com.esdatabaselib.baseSharedPref.anonation.TYPE;
import esolutions.com.esdatabaselib.baseSqlite.anonation.Params;

/**
 * Created by VinhNB on 11/11/2017.
 */

@SharePref(name = "MenuTopSearchSharePref")
public class MenuTopSearchSharePref {
    @KeyType(name = "typeSearchString")
    public String typeSearchString;

    @KeyType(name = "messageSearch")
    public String messageSearch;



    public MenuTopSearchSharePref(@Params(name = "typeSearchString") String typeSearchString,
                                  @Params(name = "messageSearch") String messageSearch
                      ) {
        this.typeSearchString = typeSearchString;
        this.messageSearch = messageSearch;
    }
}
