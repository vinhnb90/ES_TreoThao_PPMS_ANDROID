package com.es.tungnv.utils;

import android.graphics.PointF;

import com.es.tungnv.enums.EsspSnapType;

/**
 * Created by TUNGNV on 9/5/2016.
 */
public class EsspSnapPoint {

    private EsspSnapType type;
    private PointF point;
    private long Id;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public EsspSnapType getType() {
        return type;
    }

    public void setType(EsspSnapType type) {
        this.type = type;
    }

    public PointF getPoint() {
        return point;
    }

    public void setPoint(PointF point) {
        this.point = point;
    }

}
