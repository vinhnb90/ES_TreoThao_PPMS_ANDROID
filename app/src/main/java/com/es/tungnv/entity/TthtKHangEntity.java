package com.es.tungnv.entity;

import java.util.ArrayList;

/**
 * Created by TUNGNV on 8/24/2016.
 */
public class TthtKHangEntity {

    private int stt;
    private TthtBBanEntity tthtBBanEntity;
    private TthtCtoEntity tthtCtoEntity;
    private TthtBBanTuTiEntity tthtBBanTuTiEntity;
    private ArrayList<TthtTuTiEntity> tthtTuTiEntity = new ArrayList<>();


    public TthtKHangEntity() {
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public TthtBBanEntity getTthtBBanEntity() {
        return tthtBBanEntity;
    }

    public void setTthtBBanEntity(TthtBBanEntity tthtBBanEntity) {
        this.tthtBBanEntity = tthtBBanEntity;
    }

    public TthtCtoEntity getTthtCtoEntity() {
        return tthtCtoEntity;
    }

    public void setTthtCtoEntity(TthtCtoEntity tthtCtoEntity) {
        this.tthtCtoEntity = tthtCtoEntity;
    }

    public TthtBBanTuTiEntity getTthtBBanTuTiEntity() {
        return tthtBBanTuTiEntity;
    }

    public void setTthtBBanTuTiEntity(TthtBBanTuTiEntity tthtBBanTuTiEntity) {
        this.tthtBBanTuTiEntity = tthtBBanTuTiEntity;
    }

    public ArrayList<TthtTuTiEntity> getTthtTuTiEntity() {
        return tthtTuTiEntity;
    }

    public void setTthtTuTiEntity(ArrayList<TthtTuTiEntity> tthtTuTiEntity) {
        this.tthtTuTiEntity = tthtTuTiEntity;
    }
}
