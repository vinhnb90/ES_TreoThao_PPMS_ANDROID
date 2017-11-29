package com.es.tungnv.entity;

import java.util.LinkedHashMap;

/**
 * Created by TUNGNV on 3/31/2016.
 */
public class EsspEntityDviQly {

    public int ID;
    public String MA_DVIQLY;
    public String TEN_DVIQLY;

    public EsspEntityDviQly(){
        this.ID = -1;
        this.MA_DVIQLY = null;
        this.TEN_DVIQLY = null;
    }

    public EsspEntityDviQly(int ID, String MA_DVIQLY, String TEN_DVIQLY){
        this.ID = ID;
        this.MA_DVIQLY = MA_DVIQLY;
        this.TEN_DVIQLY = TEN_DVIQLY;
    }

    public LinkedHashMap<String, String> ToLinkedHashMap(){
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put("ID", ID+"");
        map.put("MA_NVIEN",  MA_DVIQLY);
        map.put("TEN_DVIQLY",  TEN_DVIQLY);
        return map;
    }

}
