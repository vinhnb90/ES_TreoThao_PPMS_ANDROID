package com.es.tungnv.utils;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import com.es.tungnv.enums.EsspShapeType;


/**
 * Created by TUNGNV on 8/28/2016.
 */
public class EsspShape {

    private Paint[] paint;
    private EsspShapeType esspShapeType;
    private Path [] path;
    private PointF [] startPoint;
    private PointF [] endPoint;
    private String [] text;
    private int drawable;
    private float dx;
    private float dy;
    private float angle;
    private long Id;

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public float getDx() {
        return dx;
    }

    public void setDx(float dx) {
        this.dx = dx;
    }

    public float getDy() {
        return dy;
    }

    public void setDy(float dy) {
        this.dy = dy;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public Paint[] getPaint() {
        return paint;
    }

    public void setPaint(Paint[] paint) {
        this.paint = paint;
    }

    public EsspShapeType getEsspShapeType() {
        return esspShapeType;
    }

    public void setEsspShapeType(EsspShapeType esspShapeType) {
        this.esspShapeType = esspShapeType;
    }

    public Path[] getPath() {
        return path;
    }

    public void setPath(Path[] path) {
        this.path = path;
    }

    public PointF[] getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(PointF[] startPoint) {
        this.startPoint = startPoint;
    }

    public PointF[] getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(PointF[] endPoint) {
        this.endPoint = endPoint;
    }

    public String[] getText() {
        return text;
    }

    public void setText(String[] text) {
        this.text = text;
    }
}
