package com.es.tungnv.draws;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.es.tungnv.entity.EsspPoint;
import com.es.tungnv.enums.EsspShapeType;
import com.es.tungnv.enums.EsspSnapType;
import com.es.tungnv.utils.Common;
import com.es.tungnv.utils.EsspCommon;
import com.es.tungnv.utils.EsspShape;
import com.es.tungnv.utils.EsspSnapPoint;
import com.es.tungnv.views.EsspSoDoCDActivity;
import com.es.tungnv.views.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TUNGNV on 8/25/2016.
 */
public class EsspCanvasView extends SurfaceView {

    //region Khai báo biến
    public List<EsspShape> pLineEsspShapes = new ArrayList<>();
    public List<EsspSnapPoint> pSnapPoint = new ArrayList<>();
    public List<EsspPoint> pSnapPointDuong = new ArrayList<>();
    public EsspPoint esspPointDown;
    public EsspPoint esspPointMove;
    private EsspShape esspShape = new EsspShape();

    private float xStart = 0f, yStart = 0f;
    private float xEnd = 0f, yEnd = 0f;
    private float xMove = 0f, yMove = 0f;
    private float dx = 0f, dy = 0f;
    private float dxHom = 0f, dyHom = 0f;
    private float xPoleLeft = 0f, xPoleRight = 0f;
    private float yPoleMin = 0f, yPoleMax = 0f;
    private Path pWall = new Path();
    public int posText = -1;
    private ArrayList<Float> pStartSnapX = new ArrayList<>();
    private ArrayList<Float> pStartSnapY = new ArrayList<>();
    private int countIndex = 0;
    private float scaleBmp = 2.5f;

    private float minBox = 0f;
    public boolean isViewDraw = false;
    public boolean isAllowViewDraw = true;
    private Bitmap bmView = null;
    //endregion

    //region Constructor
    public EsspCanvasView(Context context) {
        super(context);
    }

    public EsspCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EsspCanvasView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    //endregion

    //region init data and draw
    @Override
    public void onDraw(Canvas canvas) {
        try {
            initShape();
            drawTop(canvas);
            EsspCommon.check_save = true;
            for (int i = 0; i < pLineEsspShapes.size(); i++) {
                if (pLineEsspShapes.get(i).getEsspShapeType() == EsspShapeType.DUONG) {
                    canvas.save();
                    canvas.translate(pLineEsspShapes.get(i).getDx(), pLineEsspShapes.get(i).getDy());
                    canvas.rotate(pLineEsspShapes.get(i).getAngle(), pLineEsspShapes.get(i).getStartPoint()[0].x,
                            pLineEsspShapes.get(i).getStartPoint()[0].y);
                    canvas.drawPath(pLineEsspShapes.get(i).getPath()[0], pLineEsspShapes.get(i).getPaint()[0]);
                    canvas.restore();
                }
            }
            for (int i = 0; i < pLineEsspShapes.size(); i++) {
                if (pLineEsspShapes.get(i).getEsspShapeType() == EsspShapeType.DUONG) {
                    canvas.save();
                    canvas.translate(pLineEsspShapes.get(i).getDx(), pLineEsspShapes.get(i).getDy());
                    canvas.rotate(pLineEsspShapes.get(i).getAngle(), pLineEsspShapes.get(i).getStartPoint()[0].x,
                            pLineEsspShapes.get(i).getStartPoint()[0].y);
                    canvas.drawPath(pLineEsspShapes.get(i).getPath()[1], pLineEsspShapes.get(i).getPaint()[1]);
                    canvas.restore();
                }
            }
            for (int i = 0; i < pLineEsspShapes.size(); i++) {
                canvas.save();
                canvas.translate(pLineEsspShapes.get(i).getDx(), pLineEsspShapes.get(i).getDy());
                canvas.rotate(pLineEsspShapes.get(i).getAngle(), pLineEsspShapes.get(i).getStartPoint()[0].x,
                        pLineEsspShapes.get(i).getStartPoint()[0].y);
                switch (pLineEsspShapes.get(i).getEsspShapeType()) {
                    case BRUSH:
                        for (int j = 0; j < pLineEsspShapes.get(i).getPath().length; j++) {
                            canvas.drawPath(pLineEsspShapes.get(i).getPath()[j], pLineEsspShapes.get(i).getPaint()[j]);
                        }
                        break;
                    case LINE:
                        for (int j = 0; j < pLineEsspShapes.get(i).getStartPoint().length; j++) {
                            canvas.drawLine(pLineEsspShapes.get(i).getStartPoint()[j].x, pLineEsspShapes.get(i).getStartPoint()[j].y,
                                    pLineEsspShapes.get(i).getEndPoint()[j].x, pLineEsspShapes.get(i).getEndPoint()[j].y,
                                    pLineEsspShapes.get(i).getPaint()[j]);
                        }
                        break;
                    case LINE_SNAP:
                        for (int j = 0; j < pLineEsspShapes.get(i).getStartPoint().length; j++) {
                            canvas.drawLine(pLineEsspShapes.get(i).getStartPoint()[j].x, pLineEsspShapes.get(i).getStartPoint()[j].y,
                                    pLineEsspShapes.get(i).getEndPoint()[j].x, pLineEsspShapes.get(i).getEndPoint()[j].y,
                                    pLineEsspShapes.get(i).getPaint()[j]);
                        }
                        break;
                    case SMOOTH_LINE:
                        for (int j = 0; j < pLineEsspShapes.get(i).getPath().length; j++) {
                            canvas.drawPath(pLineEsspShapes.get(i).getPath()[j], pLineEsspShapes.get(i).getPaint()[j]);
                        }
                        break;
                    case ARROW1:
                        for (int j = 0; j < pLineEsspShapes.get(i).getPath().length; j++) {
                            canvas.drawLine(pLineEsspShapes.get(i).getStartPoint()[j].x, pLineEsspShapes.get(i).getStartPoint()[j].y,
                                    pLineEsspShapes.get(i).getEndPoint()[j].x, pLineEsspShapes.get(i).getEndPoint()[j].y,
                                    pLineEsspShapes.get(i).getPaint()[0]);
                            canvas.drawPath(pLineEsspShapes.get(i).getPath()[j], pLineEsspShapes.get(i).getPaint()[1]);
                        }
                        break;
                    case ARROW2:
                        for (int j = 0; j < pLineEsspShapes.get(i).getPath().length; j++) {
                            canvas.drawLine(pLineEsspShapes.get(i).getStartPoint()[j].x, pLineEsspShapes.get(i).getStartPoint()[j].y,
                                    pLineEsspShapes.get(i).getEndPoint()[j].x, pLineEsspShapes.get(i).getEndPoint()[j].y,
                                    pLineEsspShapes.get(i).getPaint()[0]);
                            canvas.drawPath(pLineEsspShapes.get(i).getPath()[j], pLineEsspShapes.get(i).getPaint()[1]);
                        }
                        break;
                    case NOTE_LINE:
                        for (int j = 0; j < pLineEsspShapes.get(i).getPath().length; j++) {
                            canvas.drawPath(pLineEsspShapes.get(i).getPath()[j], pLineEsspShapes.get(i).getPaint()[j]);
                        }
                        break;
                    case ELECTRICITY_LINE:
                        for (int j = 0; j < pLineEsspShapes.get(i).getPath().length; j++) {
                            canvas.drawPath(pLineEsspShapes.get(i).getPath()[j], pLineEsspShapes.get(i).getPaint()[j]);
                        }
                        break;
                    case TEXT:
                        for (int j = 0; j < pLineEsspShapes.get(i).getStartPoint().length; j++) {
                            canvas.drawText(pLineEsspShapes.get(i).getText()[j], pLineEsspShapes.get(i).getStartPoint()[j].x,
                                pLineEsspShapes.get(i).getStartPoint()[j].y, pLineEsspShapes.get(i).getPaint()[j]);
                        }
                        break;
                    case POWER_POLES_SQUARE:
                        for (int j = 0; j < pLineEsspShapes.get(i).getPath().length; j++) {
                            canvas.drawPath(pLineEsspShapes.get(i).getPath()[j], pLineEsspShapes.get(i).getPaint()[j]);
                        }
                        canvas.drawText(pLineEsspShapes.get(i).getText()[0], pLineEsspShapes.get(i).getStartPoint()[0].x,
                                pLineEsspShapes.get(i).getStartPoint()[0].y, pLineEsspShapes.get(i).getPaint()[4]);
                        break;
                    case POWER_POLES_CIRCLE:
                        for (int j = 0; j < pLineEsspShapes.get(i).getPath().length; j++) {
                            canvas.drawPath(pLineEsspShapes.get(i).getPath()[j], pLineEsspShapes.get(i).getPaint()[j]);
                        }
                        canvas.drawText(pLineEsspShapes.get(i).getText()[0], pLineEsspShapes.get(i).getStartPoint()[0].x,
                                pLineEsspShapes.get(i).getStartPoint()[0].y, pLineEsspShapes.get(i).getPaint()[4]);
                        break;
                    case POWER_POLES_EXTRA:
                        for (int j = 0; j < pLineEsspShapes.get(i).getPath().length; j++) {
                            canvas.drawPath(pLineEsspShapes.get(i).getPath()[j], pLineEsspShapes.get(i).getPaint()[j]);
                        }
                        break;
                    case WALL:
                        for (int j = 0; j < pLineEsspShapes.get(i).getPath().length; j++) {
                            canvas.drawPath(pLineEsspShapes.get(i).getPath()[j], pLineEsspShapes.get(i).getPaint()[j]);
                        }
                        break;
                    case BOX6:
                        for (int j = 0; j < pLineEsspShapes.get(i).getPath().length; j++) {
                            canvas.drawPath(pLineEsspShapes.get(i).getPath()[j], pLineEsspShapes.get(i).getPaint()[j]);
                        }
                        break;
                    case BOX4:
                        for (int j = 0; j < pLineEsspShapes.get(i).getPath().length; j++) {
                            canvas.drawPath(pLineEsspShapes.get(i).getPath()[j], pLineEsspShapes.get(i).getPaint()[j]);
                        }
                        break;
                    case BOX2:
                        for (int j = 0; j < pLineEsspShapes.get(i).getPath().length; j++) {
                            canvas.drawPath(pLineEsspShapes.get(i).getPath()[j], pLineEsspShapes.get(i).getPaint()[j]);
                        }
                        break;
                    case BOX1:
                        for (int j = 0; j < pLineEsspShapes.get(i).getPath().length; j++) {
                            canvas.drawPath(pLineEsspShapes.get(i).getPath()[j], pLineEsspShapes.get(i).getPaint()[j]);
                        }
                        break;
                    case BOX3F:
                        for (int j = 0; j < pLineEsspShapes.get(i).getPath().length; j++) {
                            canvas.drawPath(pLineEsspShapes.get(i).getPath()[j], pLineEsspShapes.get(i).getPaint()[j]);
                        }
                        break;
                    case BOX_BEHIND:
                        for (int j = 0; j < pLineEsspShapes.get(i).getPath().length; j++) {
                            canvas.drawPath(pLineEsspShapes.get(i).getPath()[j], pLineEsspShapes.get(i).getPaint()[j]);
                        }
                        break;
                    case ARROW_NOTE:
                        for (int j = 0; j < pLineEsspShapes.get(i).getPath().length; j++) {
                            canvas.drawLine(pLineEsspShapes.get(i).getStartPoint()[j].x, pLineEsspShapes.get(i).getStartPoint()[j].y,
                                    pLineEsspShapes.get(i).getEndPoint()[j].x, pLineEsspShapes.get(i).getEndPoint()[j].y,
                                    pLineEsspShapes.get(i).getPaint()[0]);
                            canvas.drawPath(pLineEsspShapes.get(i).getPath()[j], pLineEsspShapes.get(i).getPaint()[1]);
                            canvas.drawText(pLineEsspShapes.get(i).getText()[0], pLineEsspShapes.get(i).getStartPoint()[1].x,
                                    pLineEsspShapes.get(i).getStartPoint()[1].y, pLineEsspShapes.get(i).getPaint()[2]);
                        }
                        break;
                    case RECTANGLE:
                        for (int j = 0; j < pLineEsspShapes.get(i).getPath().length; j++) {
                            canvas.drawPath(pLineEsspShapes.get(i).getPath()[j], pLineEsspShapes.get(i).getPaint()[j]);
                        }
                        break;
                    case RECTANGLE_NAME:
                        for (int j = 0; j < pLineEsspShapes.get(i).getPath().length; j++) {
                            canvas.drawPath(pLineEsspShapes.get(i).getPath()[j], pLineEsspShapes.get(i).getPaint()[j]);
                            Rect boundTenKH = new Rect();
                            pLineEsspShapes.get(i).getPaint()[1].getTextBounds(pLineEsspShapes.get(i).getText()[0], 0,
                                    pLineEsspShapes.get(i).getText()[0].length(), boundTenKH);
                            StringBuilder[] stringBuilder = Common.splitMultiText(pLineEsspShapes.get(i).getText()[0], 1.7f * getWidth() / 5, boundTenKH.width());
                            for (int k = 0; k < stringBuilder.length; k++) {
                                canvas.drawText(stringBuilder[k].toString(), pLineEsspShapes.get(i).getStartPoint()[j].x,
                                        pLineEsspShapes.get(i).getStartPoint()[j].y + k * boundTenKH.height() + 10, pLineEsspShapes.get(i).getPaint()[1]);
                            }
                        }
                        break;
                    case CIRCLE:
                        for (int j = 0; j < pLineEsspShapes.get(i).getStartPoint().length; j++) {
                            float radius = (float) Math.sqrt(Math.pow(pLineEsspShapes.get(i).getEndPoint()[j].x - pLineEsspShapes.get(i).getStartPoint()[j].x, 2)
                                    + Math.pow(pLineEsspShapes.get(i).getEndPoint()[j].y - pLineEsspShapes.get(i).getStartPoint()[j].y, 2));
                            canvas.drawCircle(pLineEsspShapes.get(i).getStartPoint()[j].x, pLineEsspShapes.get(i).getStartPoint()[j].y, radius,
                                    pLineEsspShapes.get(i).getPaint()[j]);
                        }
                        break;
                    case SUBSTATION:
                        for (int j = 0; j < pLineEsspShapes.get(i).getPath().length; j++) {
                            canvas.drawPath(pLineEsspShapes.get(i).getPath()[j], pLineEsspShapes.get(i).getPaint()[j]);
                        }
                        break;
                    case SUBSTATION_CIRCLE:
                        canvas.drawCircle(pLineEsspShapes.get(i).getStartPoint()[0].x, pLineEsspShapes.get(i).getStartPoint()[0].y,
                                getWidth()/70, pLineEsspShapes.get(i).getPaint()[0]);
                        canvas.drawPath(pLineEsspShapes.get(i).getPath()[0], pLineEsspShapes.get(i).getPaint()[1]);
                        break;
                    case HOUSE:
                        for (int j = 0; j < pLineEsspShapes.get(i).getPath().length; j++) {
                            canvas.drawPath(pLineEsspShapes.get(i).getPath()[j], pLineEsspShapes.get(i).getPaint()[j]);
                        }
                        break;
                    case TOP_HOUSE_1:
                        for (int j = 0; j < pLineEsspShapes.get(i).getPath().length; j++) {
                            canvas.drawPath(pLineEsspShapes.get(i).getPath()[j], pLineEsspShapes.get(i).getPaint()[j]);
                        }
                        break;
                    case TOP_HOUSE_2:
                        for (int j = 0; j < pLineEsspShapes.get(i).getPath().length; j++) {
                            canvas.drawPath(pLineEsspShapes.get(i).getPath()[j], pLineEsspShapes.get(i).getPaint()[j]);
                        }
                        break;
                    case POWER_POLES_CIRCLE_TOP:
                        for (int j = 0; j < pLineEsspShapes.get(i).getPath().length; j++) {
                            canvas.drawPath(pLineEsspShapes.get(i).getPath()[j], pLineEsspShapes.get(i).getPaint()[1]);
                            float radius = (float) Math.sqrt(Math.pow(pLineEsspShapes.get(i).getEndPoint()[j].x - pLineEsspShapes.get(i).getStartPoint()[j].x, 2)
                                    + Math.pow(pLineEsspShapes.get(i).getEndPoint()[j].y - pLineEsspShapes.get(i).getStartPoint()[j].y, 2));
                            canvas.drawCircle(pLineEsspShapes.get(i).getStartPoint()[j].x, pLineEsspShapes.get(i).getStartPoint()[j].y, radius,
                                    pLineEsspShapes.get(i).getPaint()[0]);
                        }
                        break;
                    case POWER_POLES_TOP:
                        canvas.drawCircle(pLineEsspShapes.get(i).getStartPoint()[0].x, pLineEsspShapes.get(i).getStartPoint()[0].y,
                                getWidth()/70, pLineEsspShapes.get(i).getPaint()[0]);
                        canvas.drawCircle(pLineEsspShapes.get(i).getStartPoint()[0].x, pLineEsspShapes.get(i).getStartPoint()[0].y,
                                getWidth()/190, pLineEsspShapes.get(i).getPaint()[1]);
                        break;
                    case POWER_POLES_EXTRA_TOP:
                        for (int j = 0; j < pLineEsspShapes.get(i).getPath().length; j++) {
                            canvas.drawPath(pLineEsspShapes.get(i).getPath()[j], pLineEsspShapes.get(i).getPaint()[j]);
                            float radius = (float) Math.sqrt(Math.pow(pLineEsspShapes.get(i).getEndPoint()[j].x - pLineEsspShapes.get(i).getStartPoint()[j].x, 2)
                                    + Math.pow(pLineEsspShapes.get(i).getEndPoint()[j].y - pLineEsspShapes.get(i).getStartPoint()[j].y, 2));
                            canvas.drawCircle(pLineEsspShapes.get(i).getStartPoint()[j].x, pLineEsspShapes.get(i).getStartPoint()[j].y, radius,
                                    pLineEsspShapes.get(i).getPaint()[j]);
                        }
                        break;
                }
                canvas.restore();
            }
            if (EsspSoDoCDActivity.isShowSnapPoint) {
                for (int i = 0; i < pSnapPoint.size(); i++) {
                    canvas.drawCircle(pSnapPoint.get(i).getPoint().x, pSnapPoint.get(i).getPoint().y, 3, getPaintRed());
                }
            }
            drawRectBound(canvas);
            viewDraw(canvas);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initShape() {
        if (esspShape != null) {
            esspShape = new EsspShape();
        }
    }

    private Paint getPaint() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(EsspCommon.color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(EsspCommon.width);
        return paint;
    }

    private Paint getPaintRed() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(EsspCommon.width);
        return paint;
    }

    public void clear() {
        try {
            pLineEsspShapes.clear();
            pSnapPoint.clear();
            pStartSnapX.clear();
            pStartSnapY.clear();
            xStart = 0f;
            yStart = 0f;
            xMove = 0f;
            yMove = 0f;
            dx = 0f;
            dy = 0f;
            dxHom = 0f;
            dyHom = 0f;
            xPoleLeft = 0f;
            xPoleRight = 0f;
            yPoleMin = 0f;
            yPoleMax = 0f;
            minBox = 0f;
            isViewDraw = false;
            isAllowViewDraw = true;
            bmView = null;
            EsspSoDoCDActivity.isDrawTop = true;
            EsspCommon.esspShapeType = EsspShapeType.BRUSH;
            EsspCommon.textSize = 18;
            EsspCommon.width = 1.5f;
            EsspCommon.isBuildPole = false;
            EsspCommon.isBuildBox = false;
            EsspCommon.text = "";
            invalidate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int getcWidth() {
        return getWidth();
    }

    public int getcHeight() {
        return getHeight();
    }
    //endregion

    //region Draw shape
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            xMove = ev.getX();
            yMove = ev.getY();
            if (xMove <= EsspSoDoCDActivity.SIZE_CROP_VIEW / 2) {
                xMove = EsspSoDoCDActivity.SIZE_CROP_VIEW / 2;
            }
            if (xMove >= getWidth() - EsspSoDoCDActivity.SIZE_CROP_VIEW / 2) {
                xMove = getWidth() - EsspSoDoCDActivity.SIZE_CROP_VIEW / 2;
            }
            if (yMove <= EsspSoDoCDActivity.SIZE_CROP_VIEW / 2) {
                yMove = EsspSoDoCDActivity.SIZE_CROP_VIEW / 2;
            }
            if (yMove >= getHeight() - EsspSoDoCDActivity.SIZE_CROP_VIEW / 2) {
                yMove = getHeight() - EsspSoDoCDActivity.SIZE_CROP_VIEW / 2;
            }
        }
        esspShape = new EsspShape();
        switch (EsspCommon.esspShapeType) {
            case MOVE:
                if (EsspSoDoCDActivity.POS_SELECTED_LAYER != -1) {
                    move(ev);
                }
                break;
            case BRUSH:
                drawBrush(ev);
                break;
            case LINE:
                drawLine(ev);
                break;
            case LINE_SNAP:
                drawLineSnap(ev);
                break;
            case SMOOTH_LINE:
                drawSmoothLine(ev);
                break;
            case ARROW1:
                drawArrow1(ev);
                break;
            case ARROW2:
                drawArrow2(ev);
                break;
            case NOTE_LINE:
                drawNoteLine(ev);
                break;
            case ELECTRICITY_LINE:
                drawElectrictyLine(ev);
                break;
            case DUONG:
                drawStreet(ev);
                break;
            case NGO:
                drawLane(ev);
                break;
            case TEXT:
                drawText(ev);
                break;
            case POWER_POLES_SQUARE:
                drawPowerPolesRectangle(ev);
                break;
            case POWER_POLES_CIRCLE:
                drawPowerPolesCircle(ev);
                break;
            case POWER_POLES_EXTRA:
                drawPowerPolesExtra(ev);
                break;
            case WALL:
                drawWall(ev);
                break;
            case BOX6:
                drawBox6(ev);
                break;
            case BOX4:
                drawBox4(ev);
                break;
            case BOX2:
                drawBox2(ev);
                break;
            case BOX1:
                drawBox1(ev);
                break;
            case BOX3F:
                drawBox3f(ev);
                break;
            case BOX_BEHIND:
                drawBoxBehind(ev);
                break;
            case ARROW_NOTE:
                drawArrowNote(ev);
                break;
            case RECTANGLE:
                drawRectangle(ev);
                break;
            case RECTANGLE_NAME:
                drawRectangleName(ev);
                break;
            case CIRCLE:
                drawCircle(ev);
                break;
            case SUBSTATION:
                drawSubstation(ev);
                break;
            case SUBSTATION_CIRCLE:
                drawSubstationCircle(ev);
                break;
            case HOUSE:
                drawHouse(ev);
                break;
            case TOP_HOUSE_1:
                drawTopHouse1(ev);
                break;
            case TOP_HOUSE_2:
                drawTopHouse2(ev);
                break;
            case POWER_POLES_CIRCLE_TOP:
                drawPolesCircleTop(ev);
                break;
            case POWER_POLES_TOP:
                drawPolesTop(ev);
                break;
            case POWER_POLES_EXTRA_TOP:
                drawPolesExtraTop(ev);
                break;
        }
        try {
            if (ev.getAction() == MotionEvent.ACTION_MOVE) {
                if (isViewDraw && isAllowViewDraw) {
                    setDrawingCacheEnabled(true);
                    buildDrawingCache();
//                    Thread tBmp = new Thread(new Runnable() {
//                        @Override
//                        public void run() {
                            bmView = Bitmap.createScaledBitmap(getDrawingCache(), (int) (getWidth() / scaleBmp), (int) (getHeight() / scaleBmp), true);
//                        }
//                    });
//                    tBmp.start();
//                    tBmp.interrupt();
                }
            }
            if (ev.getAction() == MotionEvent.ACTION_UP) {
                if (isViewDraw && isAllowViewDraw) {
                    isViewDraw = false;
                    bmView.recycle();
                    bmView = null;
                    invalidate();
                }
            }
        } catch(Exception ex) {
            Log.e("Error view draw", ex.getMessage());
        }
        return true;
    }

    private void move(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                countIndex = 0;
                pStartSnapX = new ArrayList<>();
                pStartSnapY = new ArrayList<>();
                xStart = ev.getX();
                yStart = ev.getY();
                dx = pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getDx();
                dy = pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getDy();
                if (pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getEsspShapeType() == EsspShapeType.POWER_POLES_SQUARE
                        || pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getEsspShapeType() == EsspShapeType.POWER_POLES_CIRCLE) {
                    for (int i = 0; i < pSnapPoint.size(); i++) {
                        if (pSnapPoint.get(i).getType() == EsspSnapType.POLES_CIRCLE
                                || pSnapPoint.get(i).getType() == EsspSnapType.POLES_RECTANGLE
                                || pSnapPoint.get(i).getType() == EsspSnapType.BOX) {
                            pStartSnapX.add(pSnapPoint.get(i).getPoint().x);
                            pStartSnapY.add(pSnapPoint.get(i).getPoint().y);
                        }
                    }
                    for (int i = 0; i < pLineEsspShapes.size(); i++) {
                        if (pLineEsspShapes.get(i).getEsspShapeType() == EsspShapeType.BOX1
                                || pLineEsspShapes.get(i).getEsspShapeType() == EsspShapeType.BOX2
                                || pLineEsspShapes.get(i).getEsspShapeType() == EsspShapeType.BOX4
                                || pLineEsspShapes.get(i).getEsspShapeType() == EsspShapeType.BOX6
                                || pLineEsspShapes.get(i).getEsspShapeType() == EsspShapeType.BOX_BEHIND) {
                            dxHom = pLineEsspShapes.get(i).getDx();
//                            dyHom = pLineEsspShapes.get(i).getDy();
                        }
                    }
                } else if (pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getEsspShapeType() == EsspShapeType.POWER_POLES_EXTRA) {
                    for (int i = 0; i < pSnapPoint.size(); i++) {
                        if (pSnapPoint.get(i).getType() == EsspSnapType.POLES_EXTRA
                                && pSnapPoint.get(i).getId() == pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getId()) {
                            pStartSnapX.add(pSnapPoint.get(i).getPoint().x);
                            pStartSnapY.add(pSnapPoint.get(i).getPoint().y);
                        }
                    }
                } else if (pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getEsspShapeType() == EsspShapeType.HOUSE) {
                    for (int i = 0; i < pSnapPoint.size(); i++) {
                        if (pSnapPoint.get(i).getType() == EsspSnapType.HOUSE
                                && pSnapPoint.get(i).getId() == pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getId()) {
                            pStartSnapX.add(pSnapPoint.get(i).getPoint().x);
                            pStartSnapY.add(pSnapPoint.get(i).getPoint().y);
                        }
                    }
                } else if (pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getEsspShapeType() == EsspShapeType.BOX6
                        || pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getEsspShapeType() == EsspShapeType.BOX4
                        || pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getEsspShapeType() == EsspShapeType.BOX2
                        || pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getEsspShapeType() == EsspShapeType.BOX1) {
                    for (int i = 0; i < pSnapPoint.size(); i++) {
                        if (pSnapPoint.get(i).getType() == EsspSnapType.BOX
                                && pSnapPoint.get(i).getId() == pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getId()) {
                            pStartSnapX.add(pSnapPoint.get(i).getPoint().x);
                            pStartSnapY.add(pSnapPoint.get(i).getPoint().y);
                        }
                    }
                } else if (pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getEsspShapeType() == EsspShapeType.RECTANGLE_NAME) {
                    for (int i = 0; i < pSnapPoint.size(); i++) {
                        if (pSnapPoint.get(i).getType() == EsspSnapType.RECTANGLE_NAME
                                && pSnapPoint.get(i).getId() == pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getId()) {
                            pStartSnapX.add(pSnapPoint.get(i).getPoint().x);
                            pStartSnapY.add(pSnapPoint.get(i).getPoint().y);
                        }
                    }
                }
            case MotionEvent.ACTION_MOVE:
                if (pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getEsspShapeType() == EsspShapeType.POWER_POLES_SQUARE
                        || pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getEsspShapeType() == EsspShapeType.POWER_POLES_CIRCLE) {
                    pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).setDx(dx + ev.getX() - xStart);
                    pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).setDy(dy);
                    //Di chuyển điểm snap
                    if (pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getEsspShapeType() == EsspShapeType.POWER_POLES_CIRCLE) {
                        countIndex = 0;
                        for (int i = 0; i < pSnapPoint.size(); i++) {
                            if (pSnapPoint.get(i).getType() == EsspSnapType.POLES_CIRCLE
                                    || pSnapPoint.get(i).getType() == EsspSnapType.BOX) {
                                PointF p = pSnapPoint.get(i).getPoint();
                                p.x = pStartSnapX.get(countIndex) + ev.getX() - xStart;
                                p.y = pStartSnapY.get(countIndex);

                                moveSmoothLine(p, i);

                                pSnapPoint.get(i).setPoint(p);
                                countIndex++;
                            }
                        }
                    } else if (pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getEsspShapeType() == EsspShapeType.POWER_POLES_SQUARE) {
                        countIndex = 0;
                        for (int i = 0; i < pSnapPoint.size(); i++) {
                            if (pSnapPoint.get(i).getType() == EsspSnapType.POLES_RECTANGLE
                                    || pSnapPoint.get(i).getType() == EsspSnapType.BOX) {
                                PointF p = pSnapPoint.get(i).getPoint();
                                p.x = pStartSnapX.get(countIndex) + ev.getX() - xStart;
                                p.y = pStartSnapY.get(countIndex);

                                moveSmoothLine(p, i);

                                pSnapPoint.get(i).setPoint(p);
                                countIndex++;
                            }
                        }
                    }
                    //Di chuyển hòm
                    for (int i = 0; i < pLineEsspShapes.size(); i++) {
                        if (pLineEsspShapes.get(i).getEsspShapeType() == EsspShapeType.BOX1
                                || pLineEsspShapes.get(i).getEsspShapeType() == EsspShapeType.BOX2
                                || pLineEsspShapes.get(i).getEsspShapeType() == EsspShapeType.BOX4
                                || pLineEsspShapes.get(i).getEsspShapeType() == EsspShapeType.BOX6
                                || pLineEsspShapes.get(i).getEsspShapeType() == EsspShapeType.BOX_BEHIND) {
                            pLineEsspShapes.get(i).setDx(dxHom + ev.getX() - xStart);
//                            pLineEsspShapes.get(i).setDy(dyHom);
                        }
                    }
                } else if (pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getEsspShapeType() == EsspShapeType.HOUSE
                        || pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getEsspShapeType() == EsspShapeType.POWER_POLES_EXTRA
                        || pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getEsspShapeType() == EsspShapeType.ARROW_NOTE) {
                    pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).setDx(dx + ev.getX() - xStart);
                    pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).setDy(dy);
                    //Di chuyển điểm snap
                    if (pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getEsspShapeType() == EsspShapeType.HOUSE) {
                        countIndex = 0;
                        for (int i = 0; i < pSnapPoint.size(); i++) {
                            if (pSnapPoint.get(i).getType() == EsspSnapType.HOUSE
                                    && pSnapPoint.get(i).getId() == pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getId()) {
                                PointF p = pSnapPoint.get(i).getPoint();
                                p.x = pStartSnapX.get(countIndex) + ev.getX() - xStart;
                                p.y = pStartSnapY.get(countIndex);

                                moveSmoothLine(p, i);

                                pSnapPoint.get(i).setPoint(p);
                                countIndex++;
                            }
                        }
                    } else if (pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getEsspShapeType() == EsspShapeType.POWER_POLES_EXTRA) {
                        countIndex = 0;
                        for (int i = 0; i < pSnapPoint.size(); i++) {
                            if (pSnapPoint.get(i).getType() == EsspSnapType.POLES_EXTRA
                                    && pSnapPoint.get(i).getId() == pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getId()) {
                                PointF p = pSnapPoint.get(i).getPoint();
                                p.x = pStartSnapX.get(countIndex) + ev.getX() - xStart;
                                p.y = pStartSnapY.get(countIndex);

                                moveSmoothLine(p, i);

                                pSnapPoint.get(i).setPoint(p);
                                countIndex++;
                            }
                        }
                    }
                } else if (pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getEsspShapeType() == EsspShapeType.BOX1
                        || pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getEsspShapeType() == EsspShapeType.BOX2
                        || pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getEsspShapeType() == EsspShapeType.BOX4
                        || pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getEsspShapeType() == EsspShapeType.BOX6
                        || pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getEsspShapeType() == EsspShapeType.BOX_BEHIND) {
                    if(EsspCommon.isBuildPole)
                        pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).setDx(dx);
                    else if(EsspCommon.isBuildWall)
                        pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).setDx(dx + ev.getX() - xStart);
                    pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).setDy(dy + ev.getY() - yStart);
                    if (pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getEsspShapeType() != EsspShapeType.BOX_BEHIND) {
                        countIndex = 0;
                        for (int i = 0; i < pSnapPoint.size(); i++) {
                            if (pSnapPoint.get(i).getType() == EsspSnapType.BOX
                                    && pSnapPoint.get(i).getId() == pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getId()) {
                                PointF p = pSnapPoint.get(i).getPoint();
                                if(EsspCommon.isBuildPole)
                                    p.x = pStartSnapX.get(countIndex);
                                else if(EsspCommon.isBuildWall)
                                    p.x = pStartSnapX.get(countIndex) + ev.getX() - xStart;
                                p.y = pStartSnapY.get(countIndex) + ev.getY() - yStart;
                                pSnapPoint.get(i).setPoint(p);
                                countIndex++;
                            }
                        }
                    }
                } else {
                    pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).setDx(dx + ev.getX() - xStart);
                    pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).setDy(dy + ev.getY() - yStart);
                    if (pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getEsspShapeType() == EsspShapeType.RECTANGLE_NAME) {
                        countIndex = 0;
                        for (int i = 0; i < pSnapPoint.size(); i++) {
                            if (pSnapPoint.get(i).getType() == EsspSnapType.RECTANGLE_NAME
                                    && pSnapPoint.get(i).getId() == pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getId()) {
                                PointF p = pSnapPoint.get(i).getPoint();
                                p.x = pStartSnapX.get(countIndex) + ev.getX() - xStart;
                                p.y = pStartSnapY.get(countIndex) + ev.getY() - yStart;
                                pSnapPoint.get(i).setPoint(p);
                                countIndex++;
                            }
                        }
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getEsspShapeType() == EsspShapeType.POWER_POLES_SQUARE
                        || pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getEsspShapeType() == EsspShapeType.POWER_POLES_CIRCLE) {
                    pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).setDx(dx + ev.getX() - xStart);
                    pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).setDy(dy);
                    float dx = ev.getX() - xStart;
                    xPoleLeft += dx;
                    xPoleRight += dx;
                }
                break;
        }
    }

    private void drawBrush(MotionEvent ev) {
        try {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(EsspCommon.color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(EsspCommon.width);

            PointF[] pointStarts = new PointF[]{new PointF()};
            Path[] paths = new Path[]{new Path()};

            esspShape.setEsspShapeType(EsspShapeType.BRUSH);
            esspShape.setDrawable(R.mipmap.icon_draw_pen);
            esspShape.setPaint(new Paint[]{paint});
            esspShape.setDx(0);
            esspShape.setDy(0);
            esspShape.setAngle(0);
            esspShape.setId(System.currentTimeMillis());
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isViewDraw = true;
                    pointStarts[0].x = ev.getX();
                    pointStarts[0].y = ev.getY();
                    PointF p1 = getPointSnap(pointStarts[0].x, pointStarts[0].y);
                    pointStarts[0].x = p1.x;
                    pointStarts[0].y = p1.y;
                    paths[0].moveTo((ev.getX()), (ev.getY()));
                    esspShape.setStartPoint(pointStarts);
                    esspShape.setPath(paths);
                    pLineEsspShapes.add(esspShape);
                    break;
                case MotionEvent.ACTION_MOVE:
                    float x = ev.getX();
                    float y = ev.getY();
                    PointF p2 = getPointSnap(x, y);
                    x = p2.x;
                    y = p2.y;
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    EsspSoDoCDActivity.layerAdapter.updateList(pLineEsspShapes);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawLine(MotionEvent ev) {
        try {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(EsspCommon.color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(EsspCommon.width);

            PointF[] pointStarts = new PointF[]{new PointF()};
            PointF[] pointEnds = new PointF[]{new PointF()};

            esspShape.setEsspShapeType(EsspShapeType.LINE);
            esspShape.setDrawable(R.mipmap.icon_draw_line);
            esspShape.setPaint(new Paint[]{paint});
            esspShape.setDx(0);
            esspShape.setDy(0);
            esspShape.setAngle(0);
            esspShape.setId(System.currentTimeMillis());
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isViewDraw = true;
                    pointStarts[0].x = ev.getX();
                    pointStarts[0].y = ev.getY();
                    pointEnds[0].x = ev.getX();
                    pointEnds[0].y = ev.getY();
                    PointF p1 = getPointSnap(pointStarts[0].x, pointStarts[0].y);
                    pointStarts[0].x = p1.x;
                    pointStarts[0].y = p1.y;
                    esspShape.setStartPoint(pointStarts);
                    esspShape.setEndPoint(pointEnds);
                    pLineEsspShapes.add(esspShape);
                    break;
                case MotionEvent.ACTION_MOVE:
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getEndPoint()[0].x = ev.getX();
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getEndPoint()[0].y = ev.getY();
                    PointF p2 = getPointSnap(pLineEsspShapes.get(pLineEsspShapes.size() - 1).getEndPoint()[0].x,
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getEndPoint()[0].y);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getEndPoint()[0].x = p2.x;
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getEndPoint()[0].y = p2.y;
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    EsspSoDoCDActivity.layerAdapter.updateList(pLineEsspShapes);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawLineSnap(MotionEvent ev) {
        try {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(EsspCommon.color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(EsspCommon.width);

            PointF[] pointStarts = new PointF[]{new PointF()};
            PointF[] pointEnds = new PointF[]{new PointF()};

            esspShape.setEsspShapeType(EsspShapeType.LINE);
            esspShape.setDrawable(R.mipmap.icon_draw_line);
            esspShape.setPaint(new Paint[]{paint});
            esspShape.setDx(0);
            esspShape.setDy(0);
            esspShape.setAngle(0);
            esspShape.setId(System.currentTimeMillis());
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isViewDraw = true;
                    pointStarts[0].x = ev.getX();
                    pointStarts[0].y = ev.getY();
                    pointEnds[0].x = ev.getX();
                    pointEnds[0].y = ev.getY();
                    esspShape.setStartPoint(pointStarts);
                    esspShape.setEndPoint(pointEnds);
                    pLineEsspShapes.add(esspShape);
                    break;
                case MotionEvent.ACTION_MOVE:
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getEndPoint()[0].x = ev.getX();
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getEndPoint()[0].y = ev.getY();
                    float disX = Math.abs(pLineEsspShapes.get(pLineEsspShapes.size() - 1).getEndPoint()[0].x
                            - pLineEsspShapes.get(pLineEsspShapes.size() - 1).getStartPoint()[0].x);
                    float disY = Math.abs(pLineEsspShapes.get(pLineEsspShapes.size() - 1).getEndPoint()[0].y
                            - pLineEsspShapes.get(pLineEsspShapes.size() - 1).getStartPoint()[0].y);
                    if (disX >= disY) {
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getEndPoint()[0].y =
                                pLineEsspShapes.get(pLineEsspShapes.size() - 1).getStartPoint()[0].y;
                    } else {
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getEndPoint()[0].x =
                                pLineEsspShapes.get(pLineEsspShapes.size() - 1).getStartPoint()[0].x;
                    }
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    EsspSoDoCDActivity.layerAdapter.updateList(pLineEsspShapes);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawSmoothLine(MotionEvent ev) {
        try {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(EsspCommon.color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(EsspCommon.width);

            Path[] paths = new Path[]{new Path()};

            esspShape.setEsspShapeType(EsspShapeType.SMOOTH_LINE);
            esspShape.setDrawable(R.mipmap.icon_draw_curve);
            esspShape.setPaint(new Paint[]{paint});
            esspShape.setDx(0);
            esspShape.setDy(0);
            esspShape.setAngle(0);
            esspShape.setId(System.currentTimeMillis());
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isViewDraw = true;
                    xStart = ev.getX();
                    yStart = ev.getY();
                    PointF p1 = getPointSnap(xStart, yStart);
                    xStart = p1.x;
                    yStart = p1.y;
                    esspShape.setStartPoint(new PointF[]{new PointF(xStart, yStart)});
                    esspShape.setEndPoint(new PointF[]{new PointF(xStart, yStart)});
                    esspShape.setPath(paths);
                    pLineEsspShapes.add(esspShape);
                    break;
                case MotionEvent.ACTION_MOVE:
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0] = new Path();
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xStart, yStart);
                    float x1 = (ev.getX() + xStart) / 2;
                    float y1 = Common.getMin(yStart, ev.getY()) + 80;
                    float x2 = ev.getX();
                    float y2 = ev.getY();
                    PointF p2 = getPointSnap(x2, y2);
                    x2 = p2.x;
                    y2 = p2.y;
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].quadTo(x1, y1, x2, y2);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getEndPoint()[0].x = x2;
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getEndPoint()[0].y = y2;
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    EsspSoDoCDActivity.layerAdapter.updateList(pLineEsspShapes);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawNoteLine(MotionEvent ev) {
        try {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(EsspCommon.color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(EsspCommon.width);

            PointF[] pointStarts = new PointF[]{new PointF()};
            Path[] paths = new Path[]{new Path()};

            esspShape.setEsspShapeType(EsspShapeType.NOTE_LINE);
            esspShape.setDrawable(R.mipmap.icon_draw_note_line);
            esspShape.setPaint(new Paint[]{paint});
            esspShape.setDx(0);
            esspShape.setDy(0);
            esspShape.setAngle(0);
            esspShape.setId(System.currentTimeMillis());
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isViewDraw = true;
                    pointStarts[0].x = ev.getX();
                    pointStarts[0].y = ev.getY();
                    esspShape.setStartPoint(pointStarts);
                    xStart = ev.getX();
                    yStart = ev.getY();
                    PointF p = getPointSnap(xStart, yStart);
                    xStart = p.x;
                    yStart = p.y;
                    esspShape.setPath(paths);
                    pLineEsspShapes.add(esspShape);
                    break;
                case MotionEvent.ACTION_MOVE:
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0] = new Path();
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xStart, yStart);
                    float x = xStart + (ev.getX() - xStart) / 4;
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x, ev.getY());
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(ev.getX(), ev.getY());
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    EsspSoDoCDActivity.layerAdapter.updateList(pLineEsspShapes);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawElectrictyLine(MotionEvent ev) {
        try {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(EsspCommon.color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(EsspCommon.width);

            PointF[] pointStarts = new PointF[]{new PointF()};
            Path[] paths = new Path[]{new Path()};

            esspShape.setEsspShapeType(EsspShapeType.ELECTRICITY_LINE);
            esspShape.setDrawable(R.mipmap.icon_draw_electricity_line);
            esspShape.setPaint(new Paint[]{paint});
            esspShape.setDx(0);
            esspShape.setDy(0);
            esspShape.setAngle(0);
            esspShape.setId(System.currentTimeMillis());
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isViewDraw = true;
                    pointStarts[0].x = ev.getX();
                    pointStarts[0].y = ev.getY();
                    esspShape.setStartPoint(pointStarts);
                    xStart = ev.getX();
                    yStart = ev.getY();
                    PointF p1 = getPointSnap(xStart, yStart);
                    xStart = p1.x;
                    yStart = p1.y;
                    esspShape.setPath(paths);
                    pLineEsspShapes.add(esspShape);
                    break;
                case MotionEvent.ACTION_MOVE:
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0] = new Path();
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xStart, yStart);
                    float x = xStart + (ev.getX() - xStart) / 2;
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].quadTo(xStart, ev.getY(),
                            x, ev.getY());
                    float x2 = ev.getX();
                    float y2 = ev.getY();
                    PointF p2 = getPointSnap(x2, y2);
                    x2 = p2.x;
                    y2 = p2.y;
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x2, y2);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    EsspSoDoCDActivity.layerAdapter.updateList(pLineEsspShapes);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawArrow1(MotionEvent ev) {
        try {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(EsspCommon.color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(EsspCommon.width);

            Paint paintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintFill.setStrokeCap(Paint.Cap.ROUND);
            paintFill.setColor(EsspCommon.color);
            paintFill.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setStrokeWidth(EsspCommon.width);

            PointF[] pointStarts = new PointF[]{new PointF()};
            PointF[] pointEnds = new PointF[]{new PointF()};

            esspShape.setEsspShapeType(EsspShapeType.ARROW1);
            esspShape.setDrawable(R.mipmap.icon_draw_arrow1);
            esspShape.setPaint(new Paint[]{paint, paintFill});
            esspShape.setDx(0);
            esspShape.setDy(0);
            esspShape.setAngle(0);
            esspShape.setId(System.currentTimeMillis());
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isViewDraw = true;
                    pointStarts[0].x = ev.getX();
                    pointStarts[0].y = ev.getY();
                    pointEnds[0].x = ev.getX();
                    pointEnds[0].y = ev.getY();
                    esspShape.setStartPoint(pointStarts);
                    esspShape.setEndPoint(pointEnds);
                    esspShape.setPath(new Path[]{new Path()});
                    pLineEsspShapes.add(esspShape);
                    break;
                case MotionEvent.ACTION_MOVE:
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0] = new Path();
                    float x = ev.getX();
                    float y = ev.getY();
                    float disX = x - pLineEsspShapes.get(pLineEsspShapes.size() - 1).getStartPoint()[0].x;
                    float disY = y - pLineEsspShapes.get(pLineEsspShapes.size() - 1).getStartPoint()[0].y;
                    if (Math.abs(disX) >= Math.abs(disY)) {
                        y = pLineEsspShapes.get(pLineEsspShapes.size() - 1).getStartPoint()[0].y;
                        float xArrow = 0f;
                        xArrow = disX / Math.abs(disX);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(x, y);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x - 15 * xArrow, y - 10);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x - 15 * xArrow, y + 10);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x, y);
                    } else {
                        x = pLineEsspShapes.get(pLineEsspShapes.size() - 1).getStartPoint()[0].x;
                        float yArrow = 0f;
                        yArrow = disY / Math.abs(disY);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(x, y);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x - 10, y - 15 * yArrow);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x + 10, y - 15 * yArrow);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x, y);
                    }
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getEndPoint()[0].x = x;
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getEndPoint()[0].y = y;
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    EsspSoDoCDActivity.layerAdapter.updateList(pLineEsspShapes);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawArrow2(MotionEvent ev) {
        try {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(EsspCommon.color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(EsspCommon.width);

            Paint paintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintFill.setStrokeCap(Paint.Cap.ROUND);
            paintFill.setColor(EsspCommon.color);
            paintFill.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setStrokeWidth(EsspCommon.width);

            PointF[] pointStarts = new PointF[]{new PointF()};
            PointF[] pointEnds = new PointF[]{new PointF()};

            esspShape.setEsspShapeType(EsspShapeType.ARROW2);
            esspShape.setDrawable(R.mipmap.icon_draw_arrow2);
            esspShape.setPaint(new Paint[]{paint, paintFill});
            esspShape.setDx(0);
            esspShape.setDy(0);
            esspShape.setAngle(0);
            esspShape.setId(System.currentTimeMillis());
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isViewDraw = true;
                    pointStarts[0].x = ev.getX();
                    pointStarts[0].y = ev.getY();
                    pointEnds[0].x = ev.getX();
                    pointEnds[0].y = ev.getY();
                    esspShape.setStartPoint(pointStarts);
                    esspShape.setEndPoint(pointEnds);
                    esspShape.setPath(new Path[]{new Path()});
                    pLineEsspShapes.add(esspShape);
                    break;
                case MotionEvent.ACTION_MOVE:
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0] = new Path();
                    float x = ev.getX();
                    float y = ev.getY();
                    float disX = x - pLineEsspShapes.get(pLineEsspShapes.size() - 1).getStartPoint()[0].x;
                    float disY = y - pLineEsspShapes.get(pLineEsspShapes.size() - 1).getStartPoint()[0].y;
                    float xStart = pLineEsspShapes.get(pLineEsspShapes.size() - 1).getStartPoint()[0].x;
                    float yStart = pLineEsspShapes.get(pLineEsspShapes.size() - 1).getStartPoint()[0].y;
                    if (Math.abs(disX) >= Math.abs(disY)) {
                        y = pLineEsspShapes.get(pLineEsspShapes.size() - 1).getStartPoint()[0].y;
                        float xArrow = 0f;
                        xArrow = disX / Math.abs(disX);

                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xStart, yStart);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xStart - 15 * xArrow * -1, yStart - 10);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xStart - 15 * xArrow * -1, yStart + 10);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xStart, yStart);

                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(x, y);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x - 15 * xArrow, y - 10);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x - 15 * xArrow, y + 10);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x, y);
                    } else {
                        x = pLineEsspShapes.get(pLineEsspShapes.size() - 1).getStartPoint()[0].x;
                        float yArrow = 0f;
                        yArrow = disY / Math.abs(disY);

                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xStart, yStart);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xStart - 10, yStart - 15 * yArrow * -1);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xStart + 10, yStart - 15 * yArrow * -1);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xStart, yStart);

                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(x, y);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x - 10, y - 15 * yArrow);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x + 10, y - 15 * yArrow);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x, y);
                    }
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getEndPoint()[0].x = x;
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getEndPoint()[0].y = y;
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    EsspSoDoCDActivity.layerAdapter.updateList(pLineEsspShapes);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawRectangle(MotionEvent ev) {
        try {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(EsspCommon.color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(EsspCommon.width);

            PointF[] pointStarts = new PointF[]{new PointF()};
            Path[] paths = new Path[]{new Path()};

            esspShape.setEsspShapeType(EsspShapeType.RECTANGLE);
            esspShape.setDrawable(R.mipmap.icon_draw_rectangle);
            esspShape.setPaint(new Paint[]{paint});
            esspShape.setDx(0);
            esspShape.setDy(0);
            esspShape.setAngle(0);
            esspShape.setId(System.currentTimeMillis());
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    xStart = ev.getX();
                    yStart = ev.getY();
                    pointStarts[0].x = ev.getX();
                    pointStarts[0].y = ev.getY();
                    esspShape.setStartPoint(pointStarts);
                    esspShape.setPath(paths);
                    pLineEsspShapes.add(esspShape);
                    break;
                case MotionEvent.ACTION_MOVE:
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0] = new Path();
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xStart, yStart);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(ev.getX(), yStart);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(ev.getX(), ev.getY());
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xStart, ev.getY());
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xStart, yStart);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    EsspSoDoCDActivity.layerAdapter.updateList(pLineEsspShapes);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawRectangleName(MotionEvent ev) {
        try {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(EsspCommon.color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(EsspCommon.width);

            Paint paintText = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            paintText.setStrokeCap(Paint.Cap.ROUND);
            paintText.setColor(EsspCommon.color);
            paintText.setStyle(Paint.Style.FILL);
            paintText.setStrokeWidth(EsspCommon.width);
            paintText.setTextSize(EsspCommon.textSize);

            Path[] paths = new Path[]{new Path()};

            Rect bounds = new Rect();
            paintText.getTextBounds(EsspSoDoCDActivity.TEN_KHANG, 0, EsspSoDoCDActivity.TEN_KHANG.length(), bounds);

            PointF[] pointStarts = new PointF[]{new PointF()};

            esspShape.setEsspShapeType(EsspShapeType.RECTANGLE_NAME);
            esspShape.setDrawable(R.mipmap.icon_draw_rectangle_name);
            esspShape.setPaint(new Paint[]{paint, paintText});
            esspShape.setDx(0);
            esspShape.setDy(0);
            esspShape.setAngle(0);
            esspShape.setId(System.currentTimeMillis());
            esspShape.setText(new String[]{EsspSoDoCDActivity.TEN_KHANG});
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    esspShape.setStartPoint(pointStarts);
                    esspShape.setPath(paths);
                    pLineEsspShapes.add(esspShape);
                    break;
                case MotionEvent.ACTION_MOVE:
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0] = new Path();
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(ev.getX() - getWidth() / 5, ev.getY() - 5 * getHeight() / 84);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(ev.getX() + getWidth() / 5, ev.getY() - 5 * getHeight() / 84);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(ev.getX() + getWidth() / 5, ev.getY() + 5 * getHeight() / 84);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(ev.getX() - getWidth() / 5, ev.getY() + 5 * getHeight() / 84);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(ev.getX() - getWidth() / 5, ev.getY() - 5 * getHeight() / 84);

                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getStartPoint()[0].x = ev.getX() - getWidth() / 5 + 10;
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getStartPoint()[0].y = ev.getY() - 5 * getHeight() / 84 + 10;
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    EsspSnapPoint snapPoint = new EsspSnapPoint();
                    snapPoint.setType(EsspSnapType.RECTANGLE_NAME);
                    snapPoint.setId(pLineEsspShapes.get(pLineEsspShapes.size() - 1).getId());
                    PointF point = new PointF();
                    point.x = ev.getX();
                    point.y = ev.getY() + 5 * getHeight() / 84;
                    snapPoint.setPoint(point);
                    pSnapPoint.add(snapPoint);

                    EsspSnapPoint snapPoint2 = new EsspSnapPoint();
                    snapPoint2.setType(EsspSnapType.RECTANGLE_NAME);
                    snapPoint2.setId(pLineEsspShapes.get(pLineEsspShapes.size() - 1).getId());
                    PointF point2 = new PointF();
                    point2.x = ev.getX() - getWidth() / 10;
                    point2.y = ev.getY() + 5 * getHeight() / 84;
                    snapPoint2.setPoint(point2);
                    pSnapPoint.add(snapPoint2);

                    EsspSnapPoint snapPoint3 = new EsspSnapPoint();
                    snapPoint3.setType(EsspSnapType.RECTANGLE_NAME);
                    snapPoint3.setId(pLineEsspShapes.get(pLineEsspShapes.size() - 1).getId());
                    PointF point3 = new PointF();
                    point3.x = ev.getX() + getWidth() / 10;
                    point3.y = ev.getY() + 5 * getHeight() / 84;
                    snapPoint3.setPoint(point3);
                    pSnapPoint.add(snapPoint3);

                    EsspSoDoCDActivity.layerAdapter.updateList(pLineEsspShapes);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawCircle(MotionEvent ev) {
        try {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(EsspCommon.color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(EsspCommon.width);

            PointF[] pointStarts = new PointF[]{new PointF()};
            PointF[] pointEnds = new PointF[]{new PointF()};

            esspShape.setEsspShapeType(EsspShapeType.CIRCLE);
            esspShape.setDrawable(R.mipmap.icon_draw_circle);
            esspShape.setPaint(new Paint[]{paint});
            esspShape.setDx(0);
            esspShape.setDy(0);
            esspShape.setAngle(0);
            esspShape.setId(System.currentTimeMillis());
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pointStarts[0].x = ev.getX();
                    pointStarts[0].y = ev.getY();
                    pointEnds[0].x = ev.getX();
                    pointEnds[0].y = ev.getY();
                    esspShape.setStartPoint(pointStarts);
                    esspShape.setEndPoint(pointEnds);
                    pLineEsspShapes.add(esspShape);
                    break;
                case MotionEvent.ACTION_MOVE:
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getEndPoint()[0].x = ev.getX();
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getEndPoint()[0].y = ev.getY();
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    EsspSoDoCDActivity.layerAdapter.updateList(pLineEsspShapes);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawPolesTop(MotionEvent ev) {
        try {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(EsspCommon.color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(EsspCommon.width);

            Paint paintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintFill.setStrokeCap(Paint.Cap.ROUND);
            paintFill.setColor(EsspCommon.color);
            paintFill.setStyle(Paint.Style.FILL_AND_STROKE);
            paintFill.setStrokeWidth(EsspCommon.width);

            PointF[] pointStarts = new PointF[]{new PointF()};

            esspShape.setEsspShapeType(EsspShapeType.POWER_POLES_TOP);
            esspShape.setDrawable(R.mipmap.icon_draw_cot_top);
            esspShape.setPaint(new Paint[]{paint, paintFill});
            esspShape.setDx(0);
            esspShape.setDy(0);
            esspShape.setAngle(0);
            esspShape.setId(System.currentTimeMillis());
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pointStarts[0].x = ev.getX();
                    pointStarts[0].y = ev.getY();
                    esspShape.setStartPoint(pointStarts);
                    pLineEsspShapes.add(esspShape);
                    break;
                case MotionEvent.ACTION_MOVE:
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getStartPoint()[0].x = ev.getX();
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getStartPoint()[0].y = ev.getY();
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    EsspSnapPoint snapPoint = new EsspSnapPoint();
                    snapPoint.setType(EsspSnapType.POWER_POLES_TOP);
                    snapPoint.setId(pLineEsspShapes.get(pLineEsspShapes.size() - 1).getId());
                    PointF point = new PointF();
                    point.x = ev.getX();
                    point.y = ev.getY();
                    snapPoint.setPoint(point);
                    pSnapPoint.add(snapPoint);

                    EsspSoDoCDActivity.layerAdapter.updateList(pLineEsspShapes);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawSubstation(MotionEvent ev) {
        try {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(EsspCommon.color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(EsspCommon.width);

            Paint paintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintFill.setStrokeCap(Paint.Cap.ROUND);
            paintFill.setColor(EsspCommon.color);
            paintFill.setStyle(Paint.Style.FILL_AND_STROKE);
            paintFill.setStrokeWidth(EsspCommon.width);

            PointF[] pointStarts = new PointF[]{new PointF()};
            Path[] paths = new Path[]{new Path(), new Path()};

            esspShape.setEsspShapeType(EsspShapeType.SUBSTATION);
            esspShape.setDrawable(R.mipmap.icon_draw_substation);
            esspShape.setPaint(new Paint[]{paint, paintFill});
            esspShape.setDx(0);
            esspShape.setDy(0);
            esspShape.setAngle(0);
            esspShape.setId(System.currentTimeMillis());
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isViewDraw = true;
                    pointStarts[0].x = ev.getX();
                    pointStarts[0].y = ev.getY();
                    esspShape.setStartPoint(pointStarts);
                    esspShape.setPath(paths);
                    pLineEsspShapes.add(esspShape);
                    break;
                case MotionEvent.ACTION_MOVE:
                    float x = ev.getX();
                    float y = ev.getY();
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0] = new Path();
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(x - getcWidth() / 44, y - getcHeight() / 44);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x + getcWidth() / 44, y - getcHeight() / 44);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x + getcWidth() / 44, y + getcHeight() / 44);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x - getcWidth() / 44, y + getcHeight() / 44);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x - getcWidth() / 44, y - getcHeight() / 44);

                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1] = new Path();
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(x - getcWidth() / 44, y + getcHeight() / 44);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(x, y - getcHeight() / 44);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(x + getcWidth() / 44, y + getcHeight() / 44);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(x - getcWidth() / 44, y + getcHeight() / 44);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    EsspSoDoCDActivity.layerAdapter.updateList(pLineEsspShapes);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawSubstationCircle(MotionEvent ev) {
        try {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(EsspCommon.color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(EsspCommon.width);

            Paint paintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintFill.setStrokeCap(Paint.Cap.ROUND);
            paintFill.setColor(EsspCommon.color);
            paintFill.setStyle(Paint.Style.FILL_AND_STROKE);
            paintFill.setStrokeWidth(EsspCommon.width);

            PointF[] pointStarts = new PointF[]{new PointF()};
            Path[] paths = new Path[]{new Path()};

            esspShape.setEsspShapeType(EsspShapeType.SUBSTATION_CIRCLE);
            esspShape.setDrawable(R.mipmap.icon_draw_substation_circle);
            esspShape.setPaint(new Paint[]{paint, paintFill});
            esspShape.setPath(paths);
            esspShape.setDx(0);
            esspShape.setDy(0);
            esspShape.setAngle(0);
            esspShape.setId(System.currentTimeMillis());
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pointStarts[0].x = ev.getX();
                    pointStarts[0].y = ev.getY();
                    esspShape.setStartPoint(pointStarts);
                    pLineEsspShapes.add(esspShape);
                    break;
                case MotionEvent.ACTION_MOVE:
                    float radius = getWidth()/70;
                    float ck = (float)(radius * Math.cos(Math.toRadians(30)));
                    float cd = (float)(radius * Math.sin(Math.toRadians(30)));

                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0] = new Path();
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(ev.getX(), ev.getY() - radius);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(ev.getX() - ck, ev.getY() + cd);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(ev.getX() + ck, ev.getY() + cd);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(ev.getX(), ev.getY() - radius);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getStartPoint()[0].x = ev.getX();
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getStartPoint()[0].y = ev.getY();
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    EsspSnapPoint snapPoint = new EsspSnapPoint();
                    snapPoint.setType(EsspSnapType.SUBSTATION_CIRCLE);
                    snapPoint.setId(pLineEsspShapes.get(pLineEsspShapes.size() - 1).getId());
                    PointF point = new PointF();
                    point.x = ev.getX();
                    point.y = ev.getY();
                    snapPoint.setPoint(point);
                    pSnapPoint.add(snapPoint);

                    EsspSoDoCDActivity.layerAdapter.updateList(pLineEsspShapes);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawHouse(MotionEvent ev) {
        try {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(EsspCommon.color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(EsspCommon.width);

            Paint paintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintFill.setStrokeCap(Paint.Cap.ROUND);
            paintFill.setColor(EsspCommon.color);
            paintFill.setStyle(Paint.Style.FILL_AND_STROKE);
            paintFill.setStrokeWidth(EsspCommon.width);

            PointF[] pointStarts = new PointF[]{new PointF()};
            Path[] paths = new Path[]{new Path(), new Path()};

            esspShape.setEsspShapeType(EsspShapeType.HOUSE);
            esspShape.setDrawable(R.mipmap.icon_draw_house);
            esspShape.setPaint(new Paint[]{paint, paintFill});
            esspShape.setDx(0);
            esspShape.setDy(0);
            esspShape.setAngle(0);
            esspShape.setId(System.currentTimeMillis());
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pointStarts[0].x = ev.getX();
                    pointStarts[0].y = ev.getY();
                    esspShape.setStartPoint(pointStarts);
                    esspShape.setPath(paths);
                    pLineEsspShapes.add(esspShape);
                    break;
                case MotionEvent.ACTION_MOVE:
                    float height = getHeight() - getHeight() / 3.5f;
                    float x = ev.getX();
                    float y = getHeight() / 3.5f + height / 2 + 3.5f * height / 8 - getcHeight() / 20;
                    //Vẽ nhà
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0] = new Path();
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(x - getcWidth() / 20, y - getcHeight() / 20);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x + getcWidth() / 20, y - getcHeight() / 20);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x + getcWidth() / 20, y + getcHeight() / 20);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x - getcWidth() / 20, y + getcHeight() / 20);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x - getcWidth() / 20, y - getcHeight() / 20);
                    //Vẽ cửa chính
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(x - 0.25f * getcWidth() / 20, y + getcHeight() / 20);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x - 0.25f * getcWidth() / 20, y - 0.25f * getcHeight() / 20);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x + 0.25f * getcWidth() / 20, y - 0.25f * getcHeight() / 20);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x + 0.25f * getcWidth() / 20, y + getcHeight() / 20);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x - 0.25f * getcWidth() / 20, y + getcHeight() / 20);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(x, y - 0.25f * getcHeight() / 20);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x, y + getcHeight() / 20);
                    //Vẽ cửa sổ trái
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(x - 0.75f * getcWidth() / 20 - 0.13f * getcWidth() / 20, y - 0.45f * getcHeight() / 20);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x - 0.75f * getcWidth() / 20 - 0.13f * getcWidth() / 20 + 0.5f * getcWidth() / 20, y - 0.45f * getcHeight() / 20);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x - 0.75f * getcWidth() / 20 - 0.13f * getcWidth() / 20 + 0.5f * getcWidth() / 20, y + 0.45f * getcHeight() / 20);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x - 0.75f * getcWidth() / 20 - 0.13f * getcWidth() / 20, y + 0.45f * getcHeight() / 20);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x - 0.75f * getcWidth() / 20 - 0.13f * getcWidth() / 20, y - 0.45f * getcHeight() / 20);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(x - 0.625f * getcWidth() / 20, y - 0.45f * getcHeight() / 20);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x - 0.625f * getcWidth() / 20, y + 0.45f * getcHeight() / 20);
                    //Vẽ cửa sổ phải
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(x + 0.75f * getcWidth() / 20 + 0.13f * getcWidth() / 20, y - 0.45f * getcHeight() / 20);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x + 0.75f * getcWidth() / 20 + 0.13f * getcWidth() / 20 - 0.5f * getcWidth() / 20, y - 0.45f * getcHeight() / 20);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x + 0.75f * getcWidth() / 20 + 0.13f * getcWidth() / 20 - 0.5f * getcWidth() / 20, y + 0.45f * getcHeight() / 20);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x + 0.75f * getcWidth() / 20 + 0.13f * getcWidth() / 20, y + 0.45f * getcHeight() / 20);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x + 0.75f * getcWidth() / 20 + 0.13f * getcWidth() / 20, y - 0.45f * getcHeight() / 20);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(x + 0.625f * getcWidth() / 20, y - 0.45f * getcHeight() / 20);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x + 0.625f * getcWidth() / 20, y + 0.45f * getcHeight() / 20);
                    // Vẽ mái
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1] = new Path();
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(x - getcWidth() / 20 - 0.5f * (getcWidth() / 20), y - getcHeight() / 20);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(x - getcWidth() / 20 + 0.25f * (getcWidth() / 20), y - 2 * getcHeight() / 20);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(x + getcWidth() / 20 - 0.25f * (getcWidth() / 20), y - 2 * getcHeight() / 20);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(x + getcWidth() / 20 + 0.5f * (getcWidth() / 20), y - getcHeight() / 20);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(x - getcWidth() / 20 - 0.5f * (getcWidth() / 20), y - getcHeight() / 20);
                    // Vẽ điểm đấu dây
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(x - getcWidth() / 20 + 5, y - getcHeight() / 20 + 5);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(x - getcWidth() / 20 + 10, y - getcHeight() / 20 + 5);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(x - getcWidth() / 20 + 10, y - getcHeight() / 20 + 10);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(x - getcWidth() / 20 + 5, y - getcHeight() / 20 + 10);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(x - getcWidth() / 20 + 5, y - getcHeight() / 20 + 5);

                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    EsspSnapPoint snapPoint = new EsspSnapPoint();
                    snapPoint.setType(EsspSnapType.HOUSE);
                    snapPoint.setId(pLineEsspShapes.get(pLineEsspShapes.size() - 1).getId());
                    PointF point = new PointF();
                    point.x = ev.getX() - getcWidth() / 20 + 7.5f;
                    point.y = getHeight() / 3.5f + (getHeight() - getHeight() / 3.5f) / 2 + 3.5f * (getHeight() - getHeight() / 3.5f) / 8 - getcHeight() / 20 - getcHeight() / 20 + 7.5f;
                    snapPoint.setPoint(point);
                    pSnapPoint.add(snapPoint);

                    EsspSoDoCDActivity.layerAdapter.updateList(pLineEsspShapes);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawTopHouse1(MotionEvent ev) {
        try {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(EsspCommon.color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(EsspCommon.width);

            PointF[] pointStarts = new PointF[]{new PointF()};
            Path[] paths = new Path[]{new Path()};

            esspShape.setEsspShapeType(EsspShapeType.TOP_HOUSE_1);
            esspShape.setDrawable(R.mipmap.icon_draw_top_house);
            esspShape.setPaint(new Paint[]{paint});
            esspShape.setDx(0);
            esspShape.setDy(0);
            esspShape.setAngle(0);
            esspShape.setId(System.currentTimeMillis());
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isViewDraw = true;
                    pointStarts[0].x = ev.getX();
                    pointStarts[0].y = ev.getY();
                    esspShape.setStartPoint(pointStarts);
                    esspShape.setPath(paths);
                    pLineEsspShapes.add(esspShape);
                    break;
                case MotionEvent.ACTION_MOVE:
                    float x = ev.getX();
                    float y = ev.getY();
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0] = new Path();
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(x - getcWidth() / 44, y - getcHeight() / 44);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x + getcWidth() / 44, y - getcHeight() / 44);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x + getcWidth() / 44, y + getcHeight() / 44);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x - getcWidth() / 44, y + getcHeight() / 44);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x - getcWidth() / 44, y - getcHeight() / 44);

                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(x - getcWidth() / 44, y - getcHeight() / 44);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x - getcWidth() / 44 + (getcWidth() / 22) / 3, y);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x - getcWidth() / 44, y + getcHeight() / 44);

                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(x + getcWidth() / 44, y - getcHeight() / 44);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x + getcWidth() / 44 - (getcWidth() / 22) / 3, y);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x + getcWidth() / 44, y + getcHeight() / 44);

                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(x - getcWidth() / 44 + (getcWidth() / 22) / 3, y);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x + getcWidth() / 44 - (getcWidth() / 22) / 3, y);

                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    EsspSnapPoint snapPoint = new EsspSnapPoint();
                    snapPoint.setType(EsspSnapType.TOP_HOUSE_1);
                    snapPoint.setId(pLineEsspShapes.get(pLineEsspShapes.size() - 1).getId());
                    PointF point = new PointF();
                    point.x = ev.getX();
                    point.y = ev.getY();
                    snapPoint.setPoint(point);
                    pSnapPoint.add(snapPoint);

                    EsspSoDoCDActivity.layerAdapter.updateList(pLineEsspShapes);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawTopHouse2(MotionEvent ev) {
        try {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(EsspCommon.color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(EsspCommon.width);

            Paint paintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintFill.setStrokeCap(Paint.Cap.ROUND);
            paintFill.setColor(EsspCommon.color);
            paintFill.setStyle(Paint.Style.FILL_AND_STROKE);
            paintFill.setStrokeWidth(EsspCommon.width);

            PointF[] pointStarts = new PointF[]{new PointF()};
            Path[] paths = new Path[]{new Path(), new Path()};

            esspShape.setEsspShapeType(EsspShapeType.TOP_HOUSE_2);
            esspShape.setDrawable(R.mipmap.icon_draw_house);
            esspShape.setPaint(new Paint[]{paint, paintFill});
            esspShape.setDx(0);
            esspShape.setDy(0);
            esspShape.setAngle(0);
            esspShape.setId(System.currentTimeMillis());
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pointStarts[0].x = ev.getX();
                    pointStarts[0].y = ev.getY();
                    esspShape.setStartPoint(pointStarts);
                    esspShape.setPath(paths);
                    pLineEsspShapes.add(esspShape);
                    break;
                case MotionEvent.ACTION_MOVE:
                    float x = ev.getX();
                    float y = ev.getY();//getHeight() / 3.5f + height / 2 + 3.5f * height / 8 - getcHeight() / 20;
                    //Vẽ nhà
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0] = new Path();
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(x - getcWidth() / 40, y - getcHeight() / 40);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x + getcWidth() / 40, y - getcHeight() / 40);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x + getcWidth() / 40, y + getcHeight() / 40);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x - getcWidth() / 40, y + getcHeight() / 40);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x - getcWidth() / 40, y - getcHeight() / 40);
                    //Vẽ cửa chính
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(x - 0.25f * getcWidth() / 40, y + getcHeight() / 40);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x - 0.25f * getcWidth() / 40, y - 0.25f * getcHeight() / 40);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x + 0.25f * getcWidth() / 40, y - 0.25f * getcHeight() / 40);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x + 0.25f * getcWidth() / 40, y + getcHeight() / 40);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x - 0.25f * getcWidth() / 40, y + getcHeight() / 40);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(x, y - 0.25f * getcHeight() / 40);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x, y + getcHeight() / 40);
                    //Vẽ cửa sổ trái
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(x - 0.75f * getcWidth() / 40 - 0.13f * getcWidth() / 40, y - 0.45f * getcHeight() / 40);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x - 0.75f * getcWidth() / 40 - 0.13f * getcWidth() / 40 + 0.5f * getcWidth() / 40, y - 0.45f * getcHeight() / 40);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x - 0.75f * getcWidth() / 40 - 0.13f * getcWidth() / 40 + 0.5f * getcWidth() / 40, y + 0.45f * getcHeight() / 40);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x - 0.75f * getcWidth() / 40 - 0.13f * getcWidth() / 40, y + 0.45f * getcHeight() / 40);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x - 0.75f * getcWidth() / 40 - 0.13f * getcWidth() / 40, y - 0.45f * getcHeight() / 40);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(x - 0.625f * getcWidth() / 40, y - 0.45f * getcHeight() / 40);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x - 0.625f * getcWidth() / 40, y + 0.45f * getcHeight() / 40);
                    //Vẽ cửa sổ phải
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(x + 0.75f * getcWidth() / 40 + 0.13f * getcWidth() / 40, y - 0.45f * getcHeight() / 40);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x + 0.75f * getcWidth() / 40 + 0.13f * getcWidth() / 40 - 0.5f * getcWidth() / 40, y - 0.45f * getcHeight() / 40);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x + 0.75f * getcWidth() / 40 + 0.13f * getcWidth() / 40 - 0.5f * getcWidth() / 40, y + 0.45f * getcHeight() / 40);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x + 0.75f * getcWidth() / 40 + 0.13f * getcWidth() / 40, y + 0.45f * getcHeight() / 40);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x + 0.75f * getcWidth() / 40 + 0.13f * getcWidth() / 40, y - 0.45f * getcHeight() / 40);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(x + 0.625f * getcWidth() / 40, y - 0.45f * getcHeight() / 40);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x + 0.625f * getcWidth() / 40, y + 0.45f * getcHeight() / 40);
                    // Vẽ mái
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1] = new Path();
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(x - getcWidth() / 40 - 0.5f * (getcWidth() / 40), y - getcHeight() / 40);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(x - getcWidth() / 40 + 0.25f * (getcWidth() / 40), y - 2 * getcHeight() / 40);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(x + getcWidth() / 40 - 0.25f * (getcWidth() / 40), y - 2 * getcHeight() / 40);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(x + getcWidth() / 40 + 0.5f * (getcWidth() / 40), y - getcHeight() / 40);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(x - getcWidth() / 40 - 0.5f * (getcWidth() / 40), y - getcHeight() / 40);
                    // Vẽ điểm đấu dây
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(x - getcWidth() / 40 + 5, y - getcHeight() / 40 + 5);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(x - getcWidth() / 40 + 10, y - getcHeight() / 40 + 5);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(x - getcWidth() / 40 + 10, y - getcHeight() / 40 + 10);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(x - getcWidth() / 40 + 5, y - getcHeight() / 40 + 10);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(x - getcWidth() / 40 + 5, y - getcHeight() / 40 + 5);

                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    EsspSnapPoint snapPoint = new EsspSnapPoint();
                    snapPoint.setType(EsspSnapType.TOP_HOUSE_2);
                    snapPoint.setId(pLineEsspShapes.get(pLineEsspShapes.size() - 1).getId());
                    PointF point = new PointF();
                    point.x = ev.getX();
                    point.y = ev.getY();
                    snapPoint.setPoint(point);
                    pSnapPoint.add(snapPoint);

                    EsspSoDoCDActivity.layerAdapter.updateList(pLineEsspShapes);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawPolesExtraTop(MotionEvent ev) {
        try {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(EsspCommon.color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(EsspCommon.width);

            Path[] paths = new Path[]{new Path()};
            PointF[] pointStarts = new PointF[]{new PointF()};
            PointF[] pointEnds = new PointF[]{new PointF()};

            esspShape.setEsspShapeType(EsspShapeType.POWER_POLES_EXTRA_TOP);
            esspShape.setDrawable(R.mipmap.icon_draw_poles_extra_top);
            esspShape.setPaint(new Paint[]{paint});
            esspShape.setDx(0);
            esspShape.setDy(0);
            esspShape.setAngle(0);
            esspShape.setId(System.currentTimeMillis());
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isViewDraw = true;
                    pointStarts[0].x = ev.getX();
                    pointStarts[0].y = ev.getY();
                    pointEnds[0].x = ev.getX() + getcHeight() / 210;
                    pointEnds[0].y = ev.getY();
                    esspShape.setStartPoint(pointStarts);
                    esspShape.setEndPoint(pointEnds);
                    esspShape.setPath(paths);
                    pLineEsspShapes.add(esspShape);
                    break;
                case MotionEvent.ACTION_MOVE:
                    float x = ev.getX();
                    float y = ev.getY();
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0] = new Path();
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(x - getcHeight() / 90, y - getcHeight() / 90);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x + getcHeight() / 90, y - getcHeight() / 90);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x + getcHeight() / 90, y + getcHeight() / 90);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x - getcHeight() / 90, y + getcHeight() / 90);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x - getcHeight() / 90, y - getcHeight() / 90);

                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getStartPoint()[0].x = ev.getX();
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getStartPoint()[0].y = ev.getY();
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getEndPoint()[0].x = ev.getX() + getcHeight() / 210;
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getEndPoint()[0].y = ev.getY();

                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    EsspSoDoCDActivity.layerAdapter.updateList(pLineEsspShapes);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawPolesCircleTop(MotionEvent ev) {
        try {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(EsspCommon.color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(EsspCommon.width);

            Paint paintfill = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintfill.setStrokeCap(Paint.Cap.ROUND);
            paintfill.setColor(EsspCommon.color);
            paintfill.setStyle(Paint.Style.FILL_AND_STROKE);
            paintfill.setStrokeWidth(EsspCommon.width);

            Path[] paths = new Path[]{new Path()};
            PointF[] pointStarts = new PointF[]{new PointF()};
            PointF[] pointEnds = new PointF[]{new PointF()};

            esspShape.setEsspShapeType(EsspShapeType.POWER_POLES_CIRCLE_TOP);
            esspShape.setDrawable(R.mipmap.icon_draw_poles_circle_top);
            esspShape.setPaint(new Paint[]{paint, paintfill});
            esspShape.setDx(0);
            esspShape.setDy(0);
            esspShape.setAngle(0);
            esspShape.setId(System.currentTimeMillis());
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isViewDraw = true;
                    pointStarts[0].x = ev.getX();
                    pointStarts[0].y = ev.getY();
                    pointEnds[0].x = ev.getX() + getcHeight() / 160;
                    pointEnds[0].y = ev.getY();
                    esspShape.setStartPoint(pointStarts);
                    esspShape.setEndPoint(pointEnds);
                    esspShape.setPath(paths);
                    pLineEsspShapes.add(esspShape);
                    break;
                case MotionEvent.ACTION_MOVE:
                    float x = ev.getX();
                    float y = ev.getY();
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getStartPoint()[0].x = ev.getX();
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getStartPoint()[0].y = ev.getY();
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getEndPoint()[0].x = ev.getX() + getcHeight() / 80;
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getEndPoint()[0].y = ev.getY();

                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0] = new Path();
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(x - getcHeight() / 100, y + 0.5f * getcHeight() / 80);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x + getcHeight() / 100, y + 0.5f * getcHeight() / 80);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x + getcHeight() / 100, y + 1.2f * getcHeight() / 80);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x - getcHeight() / 100, y + 1.2f * getcHeight() / 80);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x - getcHeight() / 100, y + 0.5f * getcHeight() / 80);

                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    EsspSoDoCDActivity.layerAdapter.updateList(pLineEsspShapes);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawText(MotionEvent ev) {
        try {
            Paint paintText = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            paintText.setStrokeCap(Paint.Cap.ROUND);
            paintText.setColor(EsspCommon.color);
            paintText.setStyle(Paint.Style.FILL);
            paintText.setStrokeWidth(EsspCommon.width);
            paintText.setTextSize(EsspCommon.textSize);

            PointF[] pointStarts = new PointF[]{new PointF()};
            PointF[] pointEnds = new PointF[]{new PointF()};

            esspShape.setEsspShapeType(EsspShapeType.TEXT);
            esspShape.setDrawable(R.mipmap.icon_draw_text);
            esspShape.setPaint(new Paint[]{paintText});
            esspShape.setDx(0);
            esspShape.setDy(0);
            esspShape.setAngle(0);
            esspShape.setId(System.currentTimeMillis());
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isViewDraw = true;
                    pointStarts[0].x = ev.getX();
                    pointStarts[0].y = ev.getY();
                    esspShape.setStartPoint(pointStarts);
                    esspShape.setEndPoint(pointEnds);
                    esspShape.setText(new String[]{EsspCommon.text});
                    pLineEsspShapes.add(esspShape);
                    break;
                case MotionEvent.ACTION_MOVE:
                    PointF pStart = changePointTextOnStreet(ev.getX(), ev.getY(), MotionEvent.ACTION_DOWN);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getStartPoint()[0].x = pStart.x;
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getStartPoint()[0].y = pStart.y;

                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    if(posText == -1) {
                        posText = pLineEsspShapes.size() - 1;
                    }
                    EsspSoDoCDActivity.layerAdapter.updateList(pLineEsspShapes);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawPowerPolesCircle(MotionEvent ev) {
        try {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(EsspCommon.color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(EsspCommon.width);

            Paint paintdash = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintdash.setStrokeCap(Paint.Cap.ROUND);
            paintdash.setColor(EsspCommon.color);
            paintdash.setStyle(Paint.Style.STROKE);
            paintdash.setStrokeWidth(EsspCommon.width);
            paintdash.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));

            Paint paintfill = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintfill.setStrokeCap(Paint.Cap.ROUND);
            paintfill.setColor(EsspCommon.color);
            paintfill.setStyle(Paint.Style.FILL_AND_STROKE);
            paintfill.setStrokeWidth(EsspCommon.width);

            Paint paintfillwhite = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintfillwhite.setStrokeCap(Paint.Cap.ROUND);
            paintfillwhite.setColor(Color.WHITE);
            paintfillwhite.setStyle(Paint.Style.FILL_AND_STROKE);
            paintfillwhite.setStrokeWidth(EsspCommon.width);

            Paint paintText = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            paintText.setStrokeCap(Paint.Cap.ROUND);
            paintText.setColor(EsspCommon.color);
            paintText.setStyle(Paint.Style.FILL);
            paintText.setStrokeWidth(EsspCommon.width);
            paintText.setTextSize(EsspCommon.textSize - 6);

            Rect bounds = new Rect();
            paintText.getTextBounds("HPD", 0, "HPD".length(), bounds);

            PointF[] pointStarts = new PointF[]{new PointF()};
            Path[] paths = new Path[]{new Path(), new Path(), new Path(), new Path()};

            if (!EsspCommon.isBuildPole && !EsspCommon.isBuildWall) {
                esspShape.setEsspShapeType(EsspShapeType.POWER_POLES_CIRCLE);
                esspShape.setDrawable(R.mipmap.icon_draw_power_roles);
                esspShape.setPaint(new Paint[]{paint, paintdash, paintfill, paintfillwhite, paintText});
                esspShape.setDx(0);
                esspShape.setDy(0);
                esspShape.setAngle(0);
                esspShape.setId(System.currentTimeMillis());
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        xStart = ev.getX();
                        yStart = ev.getY();
                        pointStarts[0].x = ev.getX();
                        pointStarts[0].y = ev.getY();
                        esspShape.setStartPoint(pointStarts);
                        esspShape.setText(new String[]{"HPD"});

                        esspShape.setPath(paths);
                        pLineEsspShapes.add(esspShape);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float height = getHeight() - getHeight() / 3.5f;
                        float xHCD = 0f;
                        float yHCD = getHeight() / 3.5f + height / 2;
                        float xHCN = 0f;
                        float yHCN = getHeight() / 3.5f + height / 2;
                        if (ev.getX() <= xStart) {
                            xHCD = ev.getX();
                            xHCN = xStart + Math.abs(xStart - ev.getX());
                        } else {
                            xHCD = xStart - Math.abs(xStart - ev.getX());
                            xHCN = ev.getX();
                        }
                        //Vẽ cột mặt chiếu đứng
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0] = new Path();
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCD - getHeight() / 80, yHCD - 3.5f * height / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD + getHeight() / 80, yHCD - 3.5f * height / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD + getHeight() / 80 + 4, yHCD + 3.5f * height / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD - getHeight() / 80 - 4, yHCD + 3.5f * height / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD - getHeight() / 80, yHCD - 3.5f * height / 8);
                        //Vẽ đất
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCD - getHeight() / 15, yHCD + 3.5f * height / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD + getHeight() / 15, yHCD + 3.5f * height / 8);
                        for (float i = xHCD - getHeight() / 15; i <= xHCD + getHeight() / 15; i += 11) {
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(i, yHCD + 3.5f * height / 8);
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(i - 5, yHCD + 3.5f * height / 8 + 15);
                        }
                        //Vẽ đường dây
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCD - getHeight() / 10, yHCD - 3.5f * height / 8 + getHeight() / 90);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD + getHeight() / 10, yHCD - 3.5f * height / 8 + getHeight() / 90);
                        //Vẽ đường sọc giữa
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1] = new Path();
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xHCD, yHCD - 3.5f * height / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xHCD, yHCD + 3.5f * height / 8);
                        //Vẽ điểm đấu dây
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[2] = new Path();
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[2].moveTo(xHCD - getHeight() / 140, yHCD - 3.5f * height / 8 + getHeight() / 90);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[2].lineTo(xHCD + getHeight() / 140, yHCD - 3.5f * height / 8 + getHeight() / 90);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[2].lineTo(xHCD + getHeight() / 140, yHCD - 3.5f * height / 8 + getHeight() / 45);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[2].lineTo(xHCD - getHeight() / 140, yHCD - 3.5f * height / 8 + getHeight() / 45);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[2].lineTo(xHCD - getHeight() / 140, yHCD - 3.5f * height / 8 + getHeight() / 90);
                        //Vẽ hộp phân dây
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[3] = new Path();
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[3].moveTo(xHCD - getHeight() / 80 - getHeight() / 100 + EsspCommon.width, yHCD - 3.5f * height / 8 + getHeight() / 30 + EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[3].lineTo(xHCD + getHeight() / 80 + getHeight() / 100 - EsspCommon.width, yHCD - 3.5f * height / 8 + getHeight() / 30 + EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[3].lineTo(xHCD + getHeight() / 80 + getHeight() / 100 - EsspCommon.width, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 25 - EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[3].lineTo(xHCD - getHeight() / 80 - getHeight() / 100 + EsspCommon.width, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 25 - EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[3].lineTo(xHCD - getHeight() / 80 - getHeight() / 100 + EsspCommon.width, yHCD - 3.5f * height / 8 + getHeight() / 30 + EsspCommon.width);

                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCD - getHeight() / 80 - getHeight() / 100, yHCD - 3.5f * height / 8 + getHeight() / 30);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD + getHeight() / 80 + getHeight() / 100, yHCD - 3.5f * height / 8 + getHeight() / 30);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD + getHeight() / 80 + getHeight() / 100, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 25);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD - getHeight() / 80 - getHeight() / 100, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 25);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD - getHeight() / 80 - getHeight() / 100, yHCD - 3.5f * height / 8 + getHeight() / 30);
                        //Vẽ text
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getStartPoint()[0].x = xHCD - bounds.width() / 2;
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getStartPoint()[0].y = yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 45;
                        //Vẽ đường dây
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[3].moveTo(xHCD - getHeight() / 80 - getHeight() / 100 + 2 * EsspCommon.width, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20 + EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[3].lineTo(xHCD + getHeight() / 80 + getHeight() / 100 - 2 * EsspCommon.width, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20 + EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[3].lineTo(xHCD + getHeight() / 80 + getHeight() / 100 - 2 * EsspCommon.width, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 16 - EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[3].lineTo(xHCD - getHeight() / 80 - getHeight() / 100 + 2 * EsspCommon.width, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 16 - EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[3].lineTo(xHCD - getHeight() / 80 - getHeight() / 100 + 2 * EsspCommon.width, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20 + EsspCommon.width);

                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCD - getHeight() / 80 - getHeight() / 120, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD + getHeight() / 80 + getHeight() / 120, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD + getHeight() / 80 + getHeight() / 120, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 16);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD - getHeight() / 80 - getHeight() / 120, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 16);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD - getHeight() / 80 - getHeight() / 120, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20);

                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCD - getHeight() / 80 - getHeight() / 120, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].quadTo(xHCD - getHeight() / 80 - getHeight() / 120, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 15,
                                xHCD - getHeight() / 10, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 15);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCD - getHeight() / 80 - getHeight() / 120, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 16);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].quadTo(xHCD - getHeight() / 80 - getHeight() / 120, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 13,
                                xHCD - getHeight() / 12, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 11);

                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCD + getHeight() / 80 + getHeight() / 120, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].quadTo(xHCD + getHeight() / 80 + getHeight() / 120, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 15,
                                xHCD + getHeight() / 10, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 15);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCD + getHeight() / 80 + getHeight() / 120, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 16);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].quadTo(xHCD + getHeight() / 80 + getHeight() / 120, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 13,
                                xHCD + getHeight() / 12, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 11);

                        //Vẽ cột mặt chiếu ngang
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCN - getHeight() / 80, yHCN - 3.5f * height / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCN + getHeight() / 80, yHCN - 3.5f * height / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCN + getHeight() / 80 + 4, yHCN + 3.5f * height / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCN - getHeight() / 80 - 4, yHCN + 3.5f * height / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCN - getHeight() / 80, yHCN - 3.5f * height / 8);
                        //Vẽ đất
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCN - getHeight() / 15, yHCN + 3.5f * height / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCN + getHeight() / 15, yHCN + 3.5f * height / 8);
                        for (float i = xHCN - getHeight() / 15; i <= xHCN + getHeight() / 15; i += 11) {
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(i, yHCN + 3.5f * height / 8);
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(i - 5, yHCN + 3.5f * height / 8 + 15);
                        }
                        //Vẽ đường sọc giữa
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xHCN, yHCN - 3.5f * height / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xHCN, yHCN + 3.5f * height / 8);
                        //Vẽ điểm đấu dây
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[2].moveTo(xHCN - getHeight() / 140, yHCN - 3.5f * height / 8 + getHeight() / 90);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[2].lineTo(xHCN + getHeight() / 140, yHCN - 3.5f * height / 8 + getHeight() / 90);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[2].lineTo(xHCN + getHeight() / 140, yHCN - 3.5f * height / 8 + getHeight() / 45);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[2].lineTo(xHCN - getHeight() / 140, yHCN - 3.5f * height / 8 + getHeight() / 45);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[2].lineTo(xHCN - getHeight() / 140, yHCN - 3.5f * height / 8 + getHeight() / 90);
                        //Vẽ hộp phân dây
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCN - getHeight() / 40, yHCN - 3.5f * height / 8 + getHeight() / 30);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCN - getHeight() / 80, yHCN - 3.5f * height / 8 + getHeight() / 30);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCN - getHeight() / 80, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 25);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCN - getHeight() / 40, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 25);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCN - getHeight() / 40, yHCN - 3.5f * height / 8 + getHeight() / 30);
                        //Vẽ đường dây
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[3].moveTo(xHCN - getHeight() / 80 - getHeight() / 100 + 2 * EsspCommon.width, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20 + EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[3].lineTo(xHCN + getHeight() / 80 + getHeight() / 100 - 2 * EsspCommon.width, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20 + EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[3].lineTo(xHCN + getHeight() / 80 + getHeight() / 100 - 2 * EsspCommon.width, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 16 - EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[3].lineTo(xHCN - getHeight() / 80 - getHeight() / 100 + 2 * EsspCommon.width, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 16 - EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[3].lineTo(xHCN - getHeight() / 80 - getHeight() / 100 + 2 * EsspCommon.width, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20 + EsspCommon.width);

                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCN - getHeight() / 80 - getHeight() / 120, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCN + getHeight() / 80 + getHeight() / 120, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCN + getHeight() / 80 + getHeight() / 120, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 16);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCN - getHeight() / 80 - getHeight() / 120, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 16);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCN - getHeight() / 80 - getHeight() / 120, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20);

                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCN - getHeight() / 80 - getHeight() / 120, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].quadTo(xHCN - getHeight() / 80 - getHeight() / 120, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 15,
                                xHCN - getHeight() / 10, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 15);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCN - getHeight() / 80 - getHeight() / 120, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 16);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].quadTo(xHCN - getHeight() / 80 - getHeight() / 120, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 13,
                                xHCN - getHeight() / 12, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 11);

                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCN + getHeight() / 80 + getHeight() / 120, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].quadTo(xHCN + getHeight() / 80 + getHeight() / 120, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 15,
                                xHCN + getHeight() / 10, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 15);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCN + getHeight() / 80 + getHeight() / 120, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 16);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].quadTo(xHCN + getHeight() / 80 + getHeight() / 120, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 13,
                                xHCN + getHeight() / 12, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 11);
                        invalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                        EsspCommon.isBuildPole = true;
                        if (ev.getX() <= xStart) {
                            xPoleLeft = ev.getX();
                            xPoleRight = xStart + Math.abs(xStart - ev.getX());
                        } else {
                            xPoleLeft = xStart - Math.abs(xStart - ev.getX());
                            xPoleRight = ev.getX();
                        }
                        yPoleMax = getHeight() / 3.5f + (getHeight() - getHeight() / 3.5f) / 2 + 3.5f * (getHeight() - getHeight() / 3.5f) / 8 - getHeight() / 30;
                        yPoleMin = getHeight() / 3.5f + (getHeight() - getHeight() / 3.5f) / 2 - 3.5f * (getHeight() - getHeight() / 3.5f) / 8 + getHeight() / 30 + getHeight() / 16 + getHeight() / 30;

                        EsspSnapPoint snapPoint = new EsspSnapPoint();
                        snapPoint.setType(EsspSnapType.POLES_CIRCLE);
                        snapPoint.setId(pLineEsspShapes.get(pLineEsspShapes.size() - 1).getId());
                        PointF point = new PointF();
                        point.x = xStart + Math.abs(xStart - ev.getX()) + getHeight() / 80 + getHeight() / 120;
                        point.y = getHeight() / 3.5f + (getHeight() - getHeight() / 3.5f) / 2 - 3.5f * (getHeight() - getHeight() / 3.5f) / 8 + getHeight() / 30 + getHeight() / 16;
                        snapPoint.setPoint(point);
                        pSnapPoint.add(snapPoint);

                        EsspSoDoCDActivity.layerAdapter.updateList(pLineEsspShapes);
                        break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawPowerPolesRectangle(MotionEvent ev) {
        try {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(EsspCommon.color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(EsspCommon.width);

            Paint paintdash = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintdash.setStrokeCap(Paint.Cap.ROUND);
            paintdash.setColor(EsspCommon.color);
            paintdash.setStyle(Paint.Style.STROKE);
            paintdash.setStrokeWidth(EsspCommon.width);
            paintdash.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));

            Paint paintfill = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintfill.setStrokeCap(Paint.Cap.ROUND);
            paintfill.setColor(EsspCommon.color);
            paintfill.setStyle(Paint.Style.FILL_AND_STROKE);
            paintfill.setStrokeWidth(EsspCommon.width);

            Paint paintfillwhite = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintfillwhite.setStrokeCap(Paint.Cap.ROUND);
            paintfillwhite.setColor(Color.WHITE);
            paintfillwhite.setStyle(Paint.Style.FILL_AND_STROKE);
            paintfillwhite.setStrokeWidth(EsspCommon.width);

            Paint paintText = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            paintText.setStrokeCap(Paint.Cap.ROUND);
            paintText.setColor(EsspCommon.color);
            paintText.setStyle(Paint.Style.FILL);
            paintText.setStrokeWidth(EsspCommon.width);
            paintText.setTextSize(EsspCommon.textSize - 6);

            Rect bounds = new Rect();
            paintText.getTextBounds("HPD", 0, "HPD".length(), bounds);

            PointF[] pointStarts = new PointF[]{new PointF()};
            Path[] paths = new Path[]{new Path(), new Path(), new Path(), new Path()};

            if (!EsspCommon.isBuildPole && !EsspCommon.isBuildWall) {
                esspShape.setEsspShapeType(EsspShapeType.POWER_POLES_SQUARE);
                esspShape.setDrawable(R.mipmap.icon_draw_poles_rectanle);
                esspShape.setPaint(new Paint[]{paint, paintdash, paintfill, paintfillwhite, paintText});
                esspShape.setDx(0);
                esspShape.setDy(0);
                esspShape.setAngle(0);
                esspShape.setId(System.currentTimeMillis());
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        xStart = ev.getX();
                        yStart = ev.getY();
                        pointStarts[0].x = ev.getX();
                        pointStarts[0].y = ev.getY();
                        esspShape.setStartPoint(pointStarts);
                        esspShape.setText(new String[]{"HPD"});

                        esspShape.setPath(paths);
                        pLineEsspShapes.add(esspShape);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float height = getHeight() - getHeight() / 3.5f;
                        float xHCD = 0f;
                        float yHCD = getHeight() / 3.5f + height / 2;
                        float xHCN = 0f;
                        float yHCN = getHeight() / 3.5f + height / 2;
                        if (ev.getX() <= xStart) {
                            xHCD = ev.getX();
                            xHCN = xStart + Math.abs(xStart - ev.getX());
                        } else {
                            xHCD = xStart - Math.abs(xStart - ev.getX());
                            xHCN = ev.getX();
                        }
                        //Vẽ cột mặt chiếu đứng
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0] = new Path();
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCD - getHeight() / 80, yHCD - 3.5f * height / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD + getHeight() / 80, yHCD - 3.5f * height / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD + getHeight() / 80 + 4, yHCD + 3.5f * height / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD - getHeight() / 80 - 4, yHCD + 3.5f * height / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD - getHeight() / 80, yHCD - 3.5f * height / 8);
                        //Vẽ đất
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCD - getHeight() / 15, yHCD + 3.5f * height / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD + getHeight() / 15, yHCD + 3.5f * height / 8);
                        for (float i = xHCD - getHeight() / 15; i <= xHCD + getHeight() / 15; i += 11) {
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(i, yHCD + 3.5f * height / 8);
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(i - 5, yHCD + 3.5f * height / 8 + 15);
                        }
                        //Vẽ đường dây HCD
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCD - getHeight() / 10, yHCD - 3.5f * height / 8 + getHeight() / 90);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD + getHeight() / 10, yHCD - 3.5f * height / 8 + getHeight() / 90);
                        //Vẽ ô
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCD - getHeight() / 110, yHCD - 3 * getHeight() / 160 - 4 * getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD + getHeight() / 110, yHCD - 3 * getHeight() / 160 - 4 * getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD + getHeight() / 110, yHCD - 3 * getHeight() / 160 - 3 * getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD - getHeight() / 110, yHCD - 3 * getHeight() / 160 - 3 * getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD - getHeight() / 110, yHCD - 3 * getHeight() / 160 - 4 * getHeight() / 20);

                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCD - getHeight() / 110, yHCD - 2 * getHeight() / 160 - 3 * getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD + getHeight() / 110, yHCD - 2 * getHeight() / 160 - 3 * getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD + getHeight() / 110, yHCD - 2 * getHeight() / 160 - 2 * getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD - getHeight() / 110, yHCD - 2 * getHeight() / 160 - 2 * getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD - getHeight() / 110, yHCD - 2 * getHeight() / 160 - 3 * getHeight() / 20);

                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCD - getHeight() / 110, yHCD - getHeight() / 160 - 2 * getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD + getHeight() / 110, yHCD - getHeight() / 160 - 2 * getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD + getHeight() / 110, yHCD - getHeight() / 160 - getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD - getHeight() / 110, yHCD - getHeight() / 160 - getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD - getHeight() / 110, yHCD - getHeight() / 160 - 2 * getHeight() / 20);

                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCD - getHeight() / 110, yHCD - getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD + getHeight() / 110, yHCD - getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD + getHeight() / 110, yHCD);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD - getHeight() / 110, yHCD);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD - getHeight() / 110, yHCD - getHeight() / 20);

                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCD - getHeight() / 110, yHCD + getHeight() / 160);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD + getHeight() / 110, yHCD + getHeight() / 160);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD + getHeight() / 110, yHCD + getHeight() / 160 + getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD - getHeight() / 110, yHCD + getHeight() / 160 + getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD - getHeight() / 110, yHCD + getHeight() / 160);

                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCD - getHeight() / 110, yHCD + 2 * getHeight() / 160 + getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD + getHeight() / 110, yHCD + 2 * getHeight() / 160 + getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD + getHeight() / 110, yHCD + 2 * getHeight() / 160 + 2 * getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD - getHeight() / 110, yHCD + 2 * getHeight() / 160 + 2 * getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD - getHeight() / 110, yHCD + 2 * getHeight() / 160 + getHeight() / 20);

                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCD - getHeight() / 110, yHCD + 3 * getHeight() / 160 + 2 * getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD + getHeight() / 110, yHCD + 3 * getHeight() / 160 + 2 * getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD + getHeight() / 110, yHCD + 3 * getHeight() / 160 + 3 * getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD - getHeight() / 110, yHCD + 3 * getHeight() / 160 + 3 * getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD - getHeight() / 110, yHCD + 3 * getHeight() / 160 + 2 * getHeight() / 20);

                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCD - getHeight() / 110, yHCD + 4 * getHeight() / 160 + 3 * getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD + getHeight() / 110, yHCD + 4 * getHeight() / 160 + 3 * getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD + getHeight() / 110, yHCD + 4 * getHeight() / 160 + 4 * getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD - getHeight() / 110, yHCD + 4 * getHeight() / 160 + 4 * getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD - getHeight() / 110, yHCD + 4 * getHeight() / 160 + 3 * getHeight() / 20);
                        //Vẽ điểm đấu dây
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[2] = new Path();
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[2].moveTo(xHCD - getHeight() / 140, yHCD - 3.5f * height / 8 + getHeight() / 90);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[2].lineTo(xHCD + getHeight() / 140, yHCD - 3.5f * height / 8 + getHeight() / 90);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[2].lineTo(xHCD + getHeight() / 140, yHCD - 3.5f * height / 8 + getHeight() / 45);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[2].lineTo(xHCD - getHeight() / 140, yHCD - 3.5f * height / 8 + getHeight() / 45);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[2].lineTo(xHCD - getHeight() / 140, yHCD - 3.5f * height / 8 + getHeight() / 90);
                        //Vẽ hộp phân dây
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[3] = new Path();
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[3].moveTo(xHCD - getHeight() / 80 - getHeight() / 100 + EsspCommon.width, yHCD - 3.5f * height / 8 + getHeight() / 30 + EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[3].lineTo(xHCD + getHeight() / 80 + getHeight() / 100 - EsspCommon.width, yHCD - 3.5f * height / 8 + getHeight() / 30 + EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[3].lineTo(xHCD + getHeight() / 80 + getHeight() / 100 - EsspCommon.width, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 25 - EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[3].lineTo(xHCD - getHeight() / 80 - getHeight() / 100 + EsspCommon.width, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 25 - EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[3].lineTo(xHCD - getHeight() / 80 - getHeight() / 100 + EsspCommon.width, yHCD - 3.5f * height / 8 + getHeight() / 30 + EsspCommon.width);

                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCD - getHeight() / 80 - getHeight() / 100, yHCD - 3.5f * height / 8 + getHeight() / 30);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD + getHeight() / 80 + getHeight() / 100, yHCD - 3.5f * height / 8 + getHeight() / 30);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD + getHeight() / 80 + getHeight() / 100, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 25);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD - getHeight() / 80 - getHeight() / 100, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 25);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD - getHeight() / 80 - getHeight() / 100, yHCD - 3.5f * height / 8 + getHeight() / 30);
                        //Vẽ text
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getStartPoint()[0].x = xHCD - bounds.width() / 2;
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getStartPoint()[0].y = yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 45;
                        //Vẽ đường dây
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[3].moveTo(xHCD - getHeight() / 80 - getHeight() / 100 + 2 * EsspCommon.width, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20 + EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[3].lineTo(xHCD + getHeight() / 80 + getHeight() / 100 - 2 * EsspCommon.width, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20 + EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[3].lineTo(xHCD + getHeight() / 80 + getHeight() / 100 - 2 * EsspCommon.width, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 16 - EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[3].lineTo(xHCD - getHeight() / 80 - getHeight() / 100 + 2 * EsspCommon.width, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 16 - EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[3].lineTo(xHCD - getHeight() / 80 - getHeight() / 100 + 2 * EsspCommon.width, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20 + EsspCommon.width);

                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCD - getHeight() / 80 - getHeight() / 120, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD + getHeight() / 80 + getHeight() / 120, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD + getHeight() / 80 + getHeight() / 120, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 16);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD - getHeight() / 80 - getHeight() / 120, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 16);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD - getHeight() / 80 - getHeight() / 120, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20);

                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCD - getHeight() / 80 - getHeight() / 120, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].quadTo(xHCD - getHeight() / 80 - getHeight() / 120, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 15,
                                xHCD - getHeight() / 10, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 15);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCD - getHeight() / 80 - getHeight() / 120, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 16);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].quadTo(xHCD - getHeight() / 80 - getHeight() / 120, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 13,
                                xHCD - getHeight() / 12, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 11);

                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCD + getHeight() / 80 + getHeight() / 120, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].quadTo(xHCD + getHeight() / 80 + getHeight() / 120, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 15,
                                xHCD + getHeight() / 10, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 15);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCD + getHeight() / 80 + getHeight() / 120, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 16);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].quadTo(xHCD + getHeight() / 80 + getHeight() / 120, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 13,
                                xHCD + getHeight() / 12, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 11);

                        //Vẽ cột mặt chiếu ngang
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCN - getHeight() / 80, yHCN - 3.5f * height / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCN + getHeight() / 80, yHCN - 3.5f * height / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCN + getHeight() / 80 + 4, yHCN + 3.5f * height / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCN - getHeight() / 80 - 4, yHCN + 3.5f * height / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCN - getHeight() / 80, yHCN - 3.5f * height / 8);
                        //Vẽ đất
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCN - getHeight() / 15, yHCN + 3.5f * height / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCN + getHeight() / 15, yHCN + 3.5f * height / 8);
                        for (float i = xHCN - getHeight() / 15; i <= xHCN + getHeight() / 15; i += 11) {
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(i, yHCN + 3.5f * height / 8);
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(i - 5, yHCN + 3.5f * height / 8 + 15);
                        }
                        //Vẽ điểm đấu dây
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[2].moveTo(xHCN - getHeight() / 140, yHCN - 3.5f * height / 8 + getHeight() / 90);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[2].lineTo(xHCN + getHeight() / 140, yHCN - 3.5f * height / 8 + getHeight() / 90);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[2].lineTo(xHCN + getHeight() / 140, yHCN - 3.5f * height / 8 + getHeight() / 45);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[2].lineTo(xHCN - getHeight() / 140, yHCN - 3.5f * height / 8 + getHeight() / 45);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[2].lineTo(xHCN - getHeight() / 140, yHCN - 3.5f * height / 8 + getHeight() / 90);
                        //Vẽ hộp phân dây
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCN - getHeight() / 40, yHCN - 3.5f * height / 8 + getHeight() / 30);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCN - getHeight() / 80, yHCN - 3.5f * height / 8 + getHeight() / 30);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCN - getHeight() / 80, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 25);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCN - getHeight() / 40, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 25);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCN - getHeight() / 40, yHCN - 3.5f * height / 8 + getHeight() / 30);
                        //Vẽ đường dây
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[3].moveTo(xHCN - getHeight() / 80 - getHeight() / 100 + 2 * EsspCommon.width, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20 + EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[3].lineTo(xHCN + getHeight() / 80 + getHeight() / 100 - 2 * EsspCommon.width, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20 + EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[3].lineTo(xHCN + getHeight() / 80 + getHeight() / 100 - 2 * EsspCommon.width, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 16 - EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[3].lineTo(xHCN - getHeight() / 80 - getHeight() / 100 + 2 * EsspCommon.width, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 16 - EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[3].lineTo(xHCN - getHeight() / 80 - getHeight() / 100 + 2 * EsspCommon.width, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20 + EsspCommon.width);

                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCN - getHeight() / 80 - getHeight() / 120, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCN + getHeight() / 80 + getHeight() / 120, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCN + getHeight() / 80 + getHeight() / 120, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 16);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCN - getHeight() / 80 - getHeight() / 120, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 16);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCN - getHeight() / 80 - getHeight() / 120, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20);

                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCN - getHeight() / 80 - getHeight() / 120, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].quadTo(xHCN - getHeight() / 80 - getHeight() / 120, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 15,
                                xHCN - getHeight() / 10, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 15);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCN - getHeight() / 80 - getHeight() / 120, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 16);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].quadTo(xHCN - getHeight() / 80 - getHeight() / 120, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 13,
                                xHCN - getHeight() / 12, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 11);

                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCN + getHeight() / 80 + getHeight() / 120, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].quadTo(xHCN + getHeight() / 80 + getHeight() / 120, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 15,
                                xHCN + getHeight() / 10, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 15);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCN + getHeight() / 80 + getHeight() / 120, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 16);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].quadTo(xHCN + getHeight() / 80 + getHeight() / 120, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 13,
                                xHCN + getHeight() / 12, yHCN - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 11);
                        invalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                        EsspCommon.isBuildPole = true;
                        if (ev.getX() <= xStart) {
                            xPoleLeft = ev.getX();
                            xPoleRight = xStart + Math.abs(xStart - ev.getX());
                        } else {
                            xPoleLeft = xStart - Math.abs(xStart - ev.getX());
                            xPoleRight = ev.getX();
                        }
                        yPoleMax = getHeight() / 3.5f + (getHeight() - getHeight() / 3.5f) / 2 + 3.5f * (getHeight() - getHeight() / 3.5f) / 8 - getHeight() / 30;
                        yPoleMin = getHeight() / 3.5f + (getHeight() - getHeight() / 3.5f) / 2 - 3.5f * (getHeight() - getHeight() / 3.5f) / 8 + getHeight() / 30 + getHeight() / 16 + getHeight() / 30;

                        EsspSnapPoint snapPoint = new EsspSnapPoint();
                        snapPoint.setType(EsspSnapType.POLES_RECTANGLE);
                        snapPoint.setId(pLineEsspShapes.get(pLineEsspShapes.size() - 1).getId());
                        PointF point = new PointF();
                        point.x = xStart + Math.abs(xStart - ev.getX()) + getHeight() / 80 + getHeight() / 120;
                        point.y = getHeight() / 3.5f + (getHeight() - getHeight() / 3.5f) / 2 - 3.5f * (getHeight() - getHeight() / 3.5f) / 8 + getHeight() / 30 + getHeight() / 16;
                        snapPoint.setPoint(point);
                        pSnapPoint.add(snapPoint);

                        EsspSoDoCDActivity.layerAdapter.updateList(pLineEsspShapes);
                        break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawPowerPolesExtra(MotionEvent ev) {
        try {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(EsspCommon.color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(EsspCommon.width);

            Paint paintdash = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintdash.setStrokeCap(Paint.Cap.ROUND);
            paintdash.setColor(EsspCommon.color);
            paintdash.setStyle(Paint.Style.STROKE);
            paintdash.setStrokeWidth(EsspCommon.width);
            paintdash.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));

            Paint paintfill = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintfill.setStrokeCap(Paint.Cap.ROUND);
            paintfill.setColor(EsspCommon.color);
            paintfill.setStyle(Paint.Style.FILL_AND_STROKE);
            paintfill.setStrokeWidth(EsspCommon.width);

            Paint paintfillwhite = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintfillwhite.setStrokeCap(Paint.Cap.ROUND);
            paintfillwhite.setColor(Color.WHITE);
            paintfillwhite.setStyle(Paint.Style.FILL_AND_STROKE);
            paintfillwhite.setStrokeWidth(EsspCommon.width);

            PointF[] pointStarts = new PointF[]{new PointF()};
            Path[] paths = new Path[]{new Path(), new Path(), new Path(), new Path()};

            esspShape.setEsspShapeType(EsspShapeType.POWER_POLES_EXTRA);
            esspShape.setDrawable(R.mipmap.icon_draw_poles_extra);
            esspShape.setPaint(new Paint[]{paint, paintdash, paintfill, paintfillwhite});
            esspShape.setDx(0);
            esspShape.setDy(0);
            esspShape.setAngle(0);
            esspShape.setId(System.currentTimeMillis());
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    xStart = ev.getX();
                    yStart = ev.getY();
                    pointStarts[0].x = ev.getX();
                    pointStarts[0].y = ev.getY();
                    esspShape.setStartPoint(pointStarts);
                    esspShape.setPath(paths);
                    pLineEsspShapes.add(esspShape);
                    break;
                case MotionEvent.ACTION_MOVE:
                    float height = getHeight() - getHeight() / 3.5f;
                    float xHCD = ev.getX();
                    float yHCD = getHeight() / 3.5f + height / 2;
                    //Vẽ cột mặt chiếu đứng
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0] = new Path();
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCD - getHeight() / 80, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD + getHeight() / 80, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD + getHeight() / 80 + 4, yHCD + 3.5f * height / 8);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD - getHeight() / 80 - 4, yHCD + 3.5f * height / 8);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD - getHeight() / 80, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20);
                    //Vẽ đất
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCD - getHeight() / 15, yHCD + 3.5f * height / 8);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD + getHeight() / 15, yHCD + 3.5f * height / 8);
                    for (float i = xHCD - getHeight() / 15; i <= xHCD + getHeight() / 15; i += 11) {
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(i, yHCD + 3.5f * height / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(i - 5, yHCD + 3.5f * height / 8 + 15);
                    }
                    //Vẽ đường sọc giữa
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1] = new Path();
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xHCD, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xHCD, yHCD + 3.5f * height / 8);
                    //Vẽ điểm đấu dây
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[2] = new Path();
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[2].moveTo(xHCD - getHeight() / 140, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20 + getHeight() / 90);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[2].lineTo(xHCD + getHeight() / 140, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20 + getHeight() / 90);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[2].lineTo(xHCD + getHeight() / 140, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20 + getHeight() / 45);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[2].lineTo(xHCD - getHeight() / 140, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20 + getHeight() / 45);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[2].lineTo(xHCD - getHeight() / 140, yHCD - 3.5f * height / 8 + getHeight() / 30 + getHeight() / 20 + getHeight() / 90);
                    //Vẽ đường dây
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xHCD - getHeight() / 80 - getHeight() / 120, yHCD - 3.5f * height / 8 + 2 * getHeight() / 30 + 2 * getHeight() / 20);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD + getHeight() / 80 + getHeight() / 120, yHCD - 3.5f * height / 8 + 2 * getHeight() / 30 + 2 * getHeight() / 20);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD + getHeight() / 80 + getHeight() / 120, yHCD - 3.5f * height / 8 + 2 * getHeight() / 30 + getHeight() / 20 + getHeight() / 16);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD - getHeight() / 80 - getHeight() / 120, yHCD - 3.5f * height / 8 + 2 * getHeight() / 30 + getHeight() / 20 + getHeight() / 16);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD - getHeight() / 80 - getHeight() / 120, yHCD - 3.5f * height / 8 + 2 * getHeight() / 30 + 2 * getHeight() / 20);
                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xHCD + getHeight() / 80 + getHeight() / 120, yHCD - 3.5f * height / 8 + 2 * getHeight() / 30 + getHeight() / 20 + getHeight() / 16);

                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    EsspSnapPoint snapPoint = new EsspSnapPoint();
                    snapPoint.setType(EsspSnapType.POLES_EXTRA);
                    snapPoint.setId(pLineEsspShapes.get(pLineEsspShapes.size() - 1).getId());
                    PointF point = new PointF();
                    point.x = ev.getX() - getHeight() / 80 - getHeight() / 120;
                    point.y = getHeight() / 3.5f + (getHeight() - getHeight() / 3.5f) / 2 - 3.5f * (getHeight() - getHeight() / 3.5f) / 8 + 2 * getHeight() / 30 + 2 * getHeight() / 20;
                    snapPoint.setPoint(point);
                    pSnapPoint.add(snapPoint);

                    EsspSnapPoint snapPoint2 = new EsspSnapPoint();
                    snapPoint2.setType(EsspSnapType.POLES_EXTRA);
                    snapPoint2.setId(pLineEsspShapes.get(pLineEsspShapes.size() - 1).getId());
                    PointF point2 = new PointF();
                    point2.x = ev.getX() + getHeight() / 80 + getHeight() / 120;
                    point2.y = getHeight() / 3.5f + (getHeight() - getHeight() / 3.5f) / 2 - 3.5f * (getHeight() - getHeight() / 3.5f) / 8 + 2 * getHeight() / 30 + getHeight() / 20 + getHeight() / 16;
                    snapPoint2.setPoint(point2);
                    pSnapPoint.add(snapPoint2);

                    EsspSoDoCDActivity.layerAdapter.updateList(pLineEsspShapes);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawWall(MotionEvent ev) {
        try {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(EsspCommon.color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(EsspCommon.width);

            Paint paintGray = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintGray.setStrokeCap(Paint.Cap.ROUND);
            paintGray.setColor(Color.LTGRAY);
            paintGray.setStyle(Paint.Style.STROKE);
            paintGray.setStrokeWidth(EsspCommon.width);

            PointF[] pointStarts = new PointF[]{new PointF()};
            Path[] paths = new Path[]{new Path(), new Path()};

            if (!EsspCommon.isBuildWall && !EsspCommon.isBuildPole) {
                esspShape.setEsspShapeType(EsspShapeType.WALL);
                esspShape.setDrawable(R.mipmap.icon_draw_wall);
                esspShape.setPaint(new Paint[]{paint, paintGray});
                esspShape.setDx(0);
                esspShape.setDy(0);
                esspShape.setAngle(0);
                esspShape.setId(System.currentTimeMillis());
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        xStart = ev.getX();
                        yStart = ev.getY();
                        pointStarts[0].x = ev.getX();
                        pointStarts[0].y = ev.getY();
                        esspShape.setStartPoint(pointStarts);
                        esspShape.setPath(paths);
                        pLineEsspShapes.add(esspShape);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0] = new Path();
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xStart, yStart);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(ev.getX(), yStart);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(ev.getX(), ev.getY());
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xStart, ev.getY());
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xStart, yStart);

                        float wWall = Math.abs(ev.getX() - xStart);
                        float hWall = Math.abs(ev.getY() - yStart);

                        int wDirection = (int) ((ev.getX() - xStart) / wWall);
                        int hDirection = (int) ((ev.getY() - yStart) / hWall);

                        float wBrick = getWidth() / 22;
                        float hBrick = getHeight() / 22;

                        int numRow = (int) (hWall / hBrick);
                        int numColumn = (int) (wWall / (wBrick));

                        if (numRow > 0) {
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1] = new Path();
                            for (int i = 1; i <= numRow + 1; i++) {
                                if (i <= numRow) {
                                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xStart, yStart + hDirection * (hBrick * i));
                                    pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(ev.getX(), yStart + hDirection * (hBrick * i));
                                    if (numColumn > 0) {
                                        for (int j = 1; j <= numColumn; j++) {
                                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xStart - wDirection * (i % 2 == 0 ? 0 : (wBrick / 2)) + wDirection * (wBrick * j), yStart + hDirection * (hBrick * (i - 1)));
                                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xStart - wDirection * (i % 2 == 0 ? 0 : (wBrick / 2)) + wDirection * (wBrick * j), yStart + hDirection * (hBrick * i));
                                        }
                                    }
                                } else {
                                    if (numColumn > 0) {
                                        for (int j = 1; j <= numColumn; j++) {
                                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xStart - wDirection * (i % 2 == 0 ? 0 : (wBrick / 2)) + wDirection * (wBrick * j), yStart + hDirection * (hBrick * (i - 1)));
                                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xStart - wDirection * (i % 2 == 0 ? 0 : (wBrick / 2)) + wDirection * (wBrick * j), yStart + hDirection * hWall);
                                        }
                                    }
                                }
                            }
                        }

                        invalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                        EsspCommon.isBuildWall = true;
                        pWall = pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0];
                        EsspSoDoCDActivity.layerAdapter.updateList(pLineEsspShapes);
                        break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawBox6(MotionEvent ev) {
        try {
            Paint paintfillwhite = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintfillwhite.setStrokeCap(Paint.Cap.ROUND);
            paintfillwhite.setColor(Color.WHITE);
            paintfillwhite.setStyle(Paint.Style.FILL_AND_STROKE);
            paintfillwhite.setStrokeWidth(EsspCommon.width);

            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(EsspCommon.color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(EsspCommon.width);

            PointF[] pointStarts = new PointF[]{new PointF()};
            Path[] paths = new Path[]{new Path(), new Path()};

            if (EsspCommon.isBuildPole || EsspCommon.isBuildWall) {
                esspShape.setEsspShapeType(EsspShapeType.BOX6);
                esspShape.setDrawable(R.mipmap.icon_draw_box_6);
                esspShape.setPaint(new Paint[]{paintfillwhite, paint});
                esspShape.setDx(0);
                esspShape.setDy(0);
                esspShape.setAngle(0);
                esspShape.setId(System.currentTimeMillis());
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(EsspCommon.isBuildPole)
                            xStart = (xPoleLeft + xPoleRight) / 2;
                        else
                            xStart = ev.getX();
                        yStart = ev.getY();
                        pointStarts[0].x = ev.getX();
                        pointStarts[0].y = ev.getY();
                        esspShape.setStartPoint(pointStarts);
                        esspShape.setPath(paths);
                        pLineEsspShapes.add(esspShape);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float xTop = ev.getX();
                        float xLeft = 0f;
                        float y = ev.getY();
                        float direction = (xStart - ev.getX()) / Math.abs(ev.getX() - xStart);
                        float wHom = getHeight() / 20;
                        float hHom = getHeight() / 14;
                        float hMatHom = 5 * getHeight() / 84;
                        float dinhCot = getHeight() / 3.5f + (getHeight() - getHeight() / 3.5f) / 2 - 3.5f * (getHeight() - getHeight() / 3.5f) / 8;
                        if (EsspCommon.isBuildPole) {
                            if (ev.getX() <= xStart) {
                                xTop = xPoleLeft;
                                xLeft = xPoleRight;
                            } else {
                                xTop = xPoleRight;
                                xLeft = xPoleLeft;
                            }
                            if (y < yPoleMin) {
                                y = yPoleMin;
                            }
                            if (y > yPoleMax) {
                                y = yPoleMax;
                            }
                        } else if (EsspCommon.isBuildWall) {
                            if(pWall != null) {
                                RectF rWall = new RectF();
                                pWall.computeBounds(rWall, true);
                                if(xTop < rWall.left + wHom){
                                    xTop = rWall.left + wHom;
                                }
                                if(xTop > rWall.right - wHom){
                                    xTop = rWall.right - wHom;
                                }
                                if(y < rWall.top + hHom) {
                                    y = rWall.top + hHom;
                                }
                                if(y > rWall.bottom - hHom) {
                                    y = rWall.bottom - hHom;
                                }
                            }
                        }
                        //Vẽ hòm mặt trước
                        //Nền hòm
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0] = new Path();
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xTop - getHeight() / 40 + EsspCommon.width, y - getHeight() / 28 + EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xTop + getHeight() / 40 - EsspCommon.width, y - getHeight() / 28 + EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xTop + getHeight() / 40 - EsspCommon.width, y + getHeight() / 28 - EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xTop - getHeight() / 40 + EsspCommon.width, y + getHeight() / 28 - EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xTop - getHeight() / 40 + EsspCommon.width, y - getHeight() / 28 + EsspCommon.width);
                        //Khung hòm
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1] = new Path();
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xTop - getHeight() / 40, y - getHeight() / 28);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop + getHeight() / 40, y - getHeight() / 28);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop + getHeight() / 40, y + getHeight() / 28);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40, y + getHeight() / 28);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40, y - getHeight() / 28);
                        //Đường ngang trên
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xTop - getHeight() / 40, y + getHeight() / 50);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop + getHeight() / 40, y + getHeight() / 50);
                        //Đường ngang dưới
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xTop - getHeight() / 40, y + getHeight() / 52);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop + getHeight() / 40, y + getHeight() / 52);
                        //Vẽ ô 1
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xTop - getHeight() / 40 + wHom / 10, y - getHeight() / 28 + hMatHom / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 3 * wHom / 10, y - getHeight() / 28 + hMatHom / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 3 * wHom / 10, y - getHeight() / 28 + 3 * hMatHom / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + wHom / 10, y - getHeight() / 28 + 3 * hMatHom / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + wHom / 10, y - getHeight() / 28 + hMatHom / 8);
                        //Vẽ ô 2
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xTop - getHeight() / 40 + 4 * wHom / 10, y - getHeight() / 28 + hMatHom / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 6 * wHom / 10, y - getHeight() / 28 + hMatHom / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 6 * wHom / 10, y - getHeight() / 28 + 3 * hMatHom / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 4 * wHom / 10, y - getHeight() / 28 + 3 * hMatHom / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 4 * wHom / 10, y - getHeight() / 28 + hMatHom / 8);
                        //Vẽ ô 3
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xTop - getHeight() / 40 + 7 * wHom / 10, y - getHeight() / 28 + hMatHom / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 9 * wHom / 10, y - getHeight() / 28 + hMatHom / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 9 * wHom / 10, y - getHeight() / 28 + 3 * hMatHom / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 7 * wHom / 10, y - getHeight() / 28 + 3 * hMatHom / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 7 * wHom / 10, y - getHeight() / 28 + hMatHom / 8);
                        //Vẽ ô 4
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xTop - getHeight() / 40 + wHom / 10, y - getHeight() / 28 + 4 * hMatHom / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 3 * wHom / 10, y - getHeight() / 28 + 4 * hMatHom / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 3 * wHom / 10, y - getHeight() / 28 + 6 * hMatHom / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + wHom / 10, y - getHeight() / 28 + 6 * hMatHom / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + wHom / 10, y - getHeight() / 28 + 4 * hMatHom / 8);
                        //Vẽ ô 5
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xTop - getHeight() / 40 + 4 * wHom / 10, y - getHeight() / 28 + 4 * hMatHom / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 6 * wHom / 10, y - getHeight() / 28 + 4 * hMatHom / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 6 * wHom / 10, y - getHeight() / 28 + 6 * hMatHom / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 4 * wHom / 10, y - getHeight() / 28 + 6 * hMatHom / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 4 * wHom / 10, y - getHeight() / 28 + 4 * hMatHom / 8);
                        //Vẽ ô 6
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xTop - getHeight() / 40 + 7 * wHom / 10, y - getHeight() / 28 + 4 * hMatHom / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 9 * wHom / 10, y - getHeight() / 28 + 4 * hMatHom / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 9 * wHom / 10, y - getHeight() / 28 + 6 * hMatHom / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 7 * wHom / 10, y - getHeight() / 28 + 6 * hMatHom / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 7 * wHom / 10, y - getHeight() / 28 + 4 * hMatHom / 8);
                        //Vẽ hòm mặt ngang
                        if(EsspCommon.isBuildPole) {
                            float wPartCot = getHeight() / 80;
                            float hCot = 3.5f * (getHeight() - getHeight() / 3.5f) / 4;
                            float kcHomTrenVsDinhCot = y - getHeight() / 28 - dinhCot;
                            float kcHomDuoiVsDinhCot = y - getHeight() / 28 - dinhCot + hHom;
                            float doNghiengTren = kcHomTrenVsDinhCot * 4 / hCot;
                            float doNghiengDuoi = kcHomDuoiVsDinhCot * 4 / hCot;
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xLeft - direction * (wPartCot + 5 * wPartCot / 3), y - getHeight() / 28);
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xLeft - direction * (wPartCot + doNghiengTren), y - getHeight() / 28);
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xLeft - direction * (wPartCot + doNghiengDuoi), y + getHeight() / 28);
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xLeft - direction * (wPartCot + wPartCot), y + getHeight() / 28);
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xLeft - direction * (wPartCot + wPartCot + 2), y + getHeight() / 28 - hHom / 4);
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xLeft - direction * (wPartCot + 5 * wPartCot / 3 - 2), y + getHeight() / 28 - hHom / 4 - 2);
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xLeft - direction * (wPartCot + 5 * wPartCot / 3), y - getHeight() / 28);
                        }
                        invalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                        float x_Top = ev.getX();
                        float yH = ev.getY();
                        float w_Hom = getHeight() / 20;
                        float h_MatHom = 5 * getHeight() / 84;

                        if (EsspCommon.isBuildPole) {
                            if (ev.getX() <= xStart) {
                                x_Top = xPoleLeft;
                            } else {
                                x_Top = xPoleRight;
                            }
                            if (yH < yPoleMin) {
                                yH = yPoleMin;
                            }
                            if (yH > yPoleMax) {
                                yH = yPoleMax;
                            }
                        } else if (EsspCommon.isBuildWall) {
                            if(pWall != null) {
                                RectF rWall = new RectF();
                                pWall.computeBounds(rWall, true);
                                if(x_Top < rWall.left + w_Hom){
                                    x_Top = rWall.left + w_Hom;
                                }
                                if(x_Top > rWall.right - w_Hom){
                                    x_Top = rWall.right - w_Hom;
                                }
                                if(yH < rWall.top + h_MatHom) {
                                    yH = rWall.top + h_MatHom;
                                }
                                if(yH > rWall.bottom - h_MatHom) {
                                    yH = rWall.bottom - h_MatHom;
                                }
                            }
                        }

                        EsspSnapPoint snapPoint = new EsspSnapPoint();
                        snapPoint.setType(EsspSnapType.BOX);
                        snapPoint.setId(pLineEsspShapes.get(pLineEsspShapes.size() - 1).getId());
                        PointF point = new PointF();
                        point.x = x_Top - getHeight() / 40 + 2 * w_Hom / 10;
                        point.y = yH - getHeight() / 28 + 2 * h_MatHom / 8;
                        snapPoint.setPoint(point);
                        pSnapPoint.add(snapPoint);

                        EsspSnapPoint snapPoint2 = new EsspSnapPoint();
                        snapPoint2.setType(EsspSnapType.BOX);
                        snapPoint2.setId(pLineEsspShapes.get(pLineEsspShapes.size() - 1).getId());
                        PointF point2 = new PointF();
                        point2.x = x_Top - getHeight() / 40 + 5 * w_Hom / 10;
                        point2.y = yH - getHeight() / 28 + 2 * h_MatHom / 8;
                        snapPoint2.setPoint(point2);
                        pSnapPoint.add(snapPoint2);

                        EsspSnapPoint snapPoint3 = new EsspSnapPoint();
                        snapPoint3.setType(EsspSnapType.BOX);
                        snapPoint3.setId(pLineEsspShapes.get(pLineEsspShapes.size() - 1).getId());
                        PointF point3 = new PointF();
                        point3.x = x_Top - getHeight() / 40 + 8 * w_Hom / 10;
                        point3.y = yH - getHeight() / 28 + 2 * h_MatHom / 8;
                        snapPoint3.setPoint(point3);
                        pSnapPoint.add(snapPoint3);

                        EsspSnapPoint snapPoint4 = new EsspSnapPoint();
                        snapPoint4.setType(EsspSnapType.BOX);
                        snapPoint4.setId(pLineEsspShapes.get(pLineEsspShapes.size() - 1).getId());
                        PointF point4 = new PointF();
                        point4.x = x_Top - getHeight() / 40 + 2 * w_Hom / 10;
                        point4.y = yH - getHeight() / 28 + 5 * h_MatHom / 8;
                        snapPoint4.setPoint(point4);
                        pSnapPoint.add(snapPoint4);

                        EsspSnapPoint snapPoint5 = new EsspSnapPoint();
                        snapPoint5.setType(EsspSnapType.BOX);
                        snapPoint5.setId(pLineEsspShapes.get(pLineEsspShapes.size() - 1).getId());
                        PointF point5 = new PointF();
                        point5.x = x_Top - getHeight() / 40 + 5 * w_Hom / 10;
                        point5.y = yH - getHeight() / 28 + 5 * h_MatHom / 8;
                        snapPoint5.setPoint(point5);
                        pSnapPoint.add(snapPoint5);

                        EsspSnapPoint snapPoint6 = new EsspSnapPoint();
                        snapPoint6.setType(EsspSnapType.BOX);
                        snapPoint6.setId(pLineEsspShapes.get(pLineEsspShapes.size() - 1).getId());
                        PointF point6 = new PointF();
                        point6.x = x_Top - getHeight() / 40 + 8 * w_Hom / 10;
                        point6.y = yH - getHeight() / 28 + 5 * h_MatHom / 8;
                        snapPoint6.setPoint(point6);
                        pSnapPoint.add(snapPoint6);

                        if (minBox <= yH + getHeight() / 28) {
                            minBox = yH + getHeight() / 28;
                        }

                        EsspCommon.isBuildBox = true;

                        EsspSoDoCDActivity.layerAdapter.updateList(pLineEsspShapes);
                        break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawBox4(MotionEvent ev) {
        try {
            Paint paintfillwhite = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintfillwhite.setStrokeCap(Paint.Cap.ROUND);
            paintfillwhite.setColor(Color.WHITE);
            paintfillwhite.setStyle(Paint.Style.FILL_AND_STROKE);
            paintfillwhite.setStrokeWidth(EsspCommon.width);

            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(EsspCommon.color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(EsspCommon.width);

            PointF[] pointStarts = new PointF[]{new PointF()};
            Path[] paths = new Path[]{new Path(), new Path()};

            if (EsspCommon.isBuildPole || EsspCommon.isBuildWall) {
                esspShape.setEsspShapeType(EsspShapeType.BOX4);
                esspShape.setDrawable(R.mipmap.icon_draw_box_4);
                esspShape.setPaint(new Paint[]{paintfillwhite, paint});
                esspShape.setDx(0);
                esspShape.setDy(0);
                esspShape.setAngle(0);
                esspShape.setId(System.currentTimeMillis());
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(EsspCommon.isBuildPole)
                            xStart = (xPoleLeft + xPoleRight) / 2;
                        else
                            xStart = ev.getX();
                        yStart = ev.getY();
                        pointStarts[0].x = ev.getX();
                        pointStarts[0].y = ev.getY();
                        esspShape.setStartPoint(pointStarts);
                        esspShape.setPath(paths);
                        pLineEsspShapes.add(esspShape);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float xTop = ev.getX();
                        float xLeft = 0f;
                        float y = ev.getY();
                        float direction = (xStart - ev.getX()) / Math.abs(ev.getX() - xStart);
                        float wHom = getHeight() / 20;
                        float hHom = getHeight() / 14;
                        float hMatHom = 5 * getHeight() / 84;
                        float dinhCot = getHeight() / 3.5f + (getHeight() - getHeight() / 3.5f) / 2 - 3.5f * (getHeight() - getHeight() / 3.5f) / 8;
                        if (EsspCommon.isBuildPole) {
                            if (ev.getX() <= xStart) {
                                xTop = xPoleLeft;
                                xLeft = xPoleRight;
                            } else {
                                xTop = xPoleRight;
                                xLeft = xPoleLeft;
                            }
                            if (y < yPoleMin) {
                                y = yPoleMin;
                            }
                            if (y > yPoleMax) {
                                y = yPoleMax;
                            }
                        } else if (EsspCommon.isBuildWall) {
                            if(pWall != null) {
                                RectF rWall = new RectF();
                                pWall.computeBounds(rWall, true);
                                if(xTop < rWall.left + wHom){
                                    xTop = rWall.left + wHom;
                                }
                                if(xTop > rWall.right - wHom){
                                    xTop = rWall.right - wHom;
                                }
                                if(y < rWall.top + hHom) {
                                    y = rWall.top + hHom;
                                }
                                if(y > rWall.bottom - hHom) {
                                    y = rWall.bottom - hHom;
                                }
                            }
                        }
                        //Vẽ hòm mặt trước
                        //Nền hòm
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0] = new Path();
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xTop - getHeight() / 40 + EsspCommon.width, y - getHeight() / 28 + EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xTop + getHeight() / 40 - EsspCommon.width, y - getHeight() / 28 + EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xTop + getHeight() / 40 - EsspCommon.width, y + getHeight() / 28 - EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xTop - getHeight() / 40 + EsspCommon.width, y + getHeight() / 28 - EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xTop - getHeight() / 40 + EsspCommon.width, y - getHeight() / 28 + EsspCommon.width);
                        //Khung hòm
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1] = new Path();
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xTop - getHeight() / 40, y - getHeight() / 28);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop + getHeight() / 40, y - getHeight() / 28);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop + getHeight() / 40, y + getHeight() / 28);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40, y + getHeight() / 28);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40, y - getHeight() / 28);
                        //Đường ngang trên
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xTop - getHeight() / 40, y + getHeight() / 40);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop + getHeight() / 40, y + getHeight() / 40);
                        //Đường ngang dưới
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xTop - getHeight() / 40, y + getHeight() / 42);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop + getHeight() / 40, y + getHeight() / 42);
                        //Vẽ ô 1
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xTop - getHeight() / 40 + wHom / 7, y - getHeight() / 28 + hMatHom / 7);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 3 * wHom / 7, y - getHeight() / 28 + hMatHom / 7);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 3 * wHom / 7, y - getHeight() / 28 + 3 * hMatHom / 7);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + wHom / 7, y - getHeight() / 28 + 3 * hMatHom / 7);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + wHom / 7, y - getHeight() / 28 + hMatHom / 7);
                        //Vẽ ô 2
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xTop - getHeight() / 40 + 4 * wHom / 7, y - getHeight() / 28 + hMatHom / 7);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 6 * wHom / 7, y - getHeight() / 28 + hMatHom / 7);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 6 * wHom / 7, y - getHeight() / 28 + 3 * hMatHom / 7);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 4 * wHom / 7, y - getHeight() / 28 + 3 * hMatHom / 7);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 4 * wHom / 7, y - getHeight() / 28 + hMatHom / 7);
                        //Vẽ ô 3
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xTop - getHeight() / 40 + wHom / 7, y - getHeight() / 28 + 4 * hMatHom / 7);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 3 * wHom / 7, y - getHeight() / 28 + 4 * hMatHom / 7);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 3 * wHom / 7, y - getHeight() / 28 + 6 * hMatHom / 7);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + wHom / 7, y - getHeight() / 28 + 6 * hMatHom / 7);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + wHom / 7, y - getHeight() / 28 + 4 * hMatHom / 7);
                        //Vẽ ô 4
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xTop - getHeight() / 40 + 4 * wHom / 7, y - getHeight() / 28 + 4 * hMatHom / 7);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 6 * wHom / 7, y - getHeight() / 28 + 4 * hMatHom / 7);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 6 * wHom / 7, y - getHeight() / 28 + 6 * hMatHom / 7);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 4 * wHom / 7, y - getHeight() / 28 + 6 * hMatHom / 7);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 4 * wHom / 7, y - getHeight() / 28 + 4 * hMatHom / 7);
                        //Vẽ hòm mặt ngang
                        if(EsspCommon.isBuildPole) {
                            float wPartCot = getHeight() / 80;
                            float hCot = 3.5f * (getHeight() - getHeight() / 3.5f) / 4;
                            float kcHomTrenVsDinhCot = y - getHeight() / 28 - dinhCot;
                            float kcHomDuoiVsDinhCot = y - getHeight() / 28 - dinhCot + hHom;
                            float doNghiengTren = kcHomTrenVsDinhCot * 4 / hCot;
                            float doNghiengDuoi = kcHomDuoiVsDinhCot * 4 / hCot;
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xLeft - direction * (wPartCot + 5 * wPartCot / 3), y - getHeight() / 28);
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xLeft - direction * (wPartCot + doNghiengTren), y - getHeight() / 28);
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xLeft - direction * (wPartCot + doNghiengDuoi), y + getHeight() / 28);
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xLeft - direction * (wPartCot + wPartCot), y + getHeight() / 28);
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xLeft - direction * (wPartCot + wPartCot + 2), y + getHeight() / 28 - hHom / 4);
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xLeft - direction * (wPartCot + 5 * wPartCot / 3 - 2), y + getHeight() / 28 - hHom / 4 - 2);
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xLeft - direction * (wPartCot + 5 * wPartCot / 3), y - getHeight() / 28);
                        }
                        invalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                        float x_Top = ev.getX();
                        float yH = ev.getY();
                        float w_Hom = getHeight() / 20;
                        float h_MatHom = 5 * getHeight() / 84;

                        if (EsspCommon.isBuildPole) {
                            if (ev.getX() <= xStart) {
                                x_Top = xPoleLeft;
                            } else {
                                x_Top = xPoleRight;
                            }
                            if (yH < yPoleMin) {
                                yH = yPoleMin;
                            }
                            if (yH > yPoleMax) {
                                yH = yPoleMax;
                            }
                        } else if (EsspCommon.isBuildWall) {
                            if(pWall != null) {
                                RectF rWall = new RectF();
                                pWall.computeBounds(rWall, true);
                                if(x_Top < rWall.left + w_Hom){
                                    x_Top = rWall.left + w_Hom;
                                }
                                if(x_Top > rWall.right - w_Hom){
                                    x_Top = rWall.right - w_Hom;
                                }
                                if(yH < rWall.top + h_MatHom) {
                                    yH = rWall.top + h_MatHom;
                                }
                                if(yH > rWall.bottom - h_MatHom) {
                                    yH = rWall.bottom - h_MatHom;
                                }
                            }
                        }

                        EsspSnapPoint snapPoint = new EsspSnapPoint();
                        snapPoint.setType(EsspSnapType.BOX);
                        snapPoint.setId(pLineEsspShapes.get(pLineEsspShapes.size() - 1).getId());
                        PointF point = new PointF();
                        point.x = x_Top - getHeight() / 40 + 2 * w_Hom / 7;
                        point.y = yH - getHeight() / 28 + 2 * h_MatHom / 7;
                        snapPoint.setPoint(point);
                        pSnapPoint.add(snapPoint);

                        EsspSnapPoint snapPoint2 = new EsspSnapPoint();
                        snapPoint2.setType(EsspSnapType.BOX);
                        snapPoint2.setId(pLineEsspShapes.get(pLineEsspShapes.size() - 1).getId());
                        PointF point2 = new PointF();
                        point2.x = x_Top - getHeight() / 40 + 5 * w_Hom / 7;
                        point2.y = yH - getHeight() / 28 + 2 * h_MatHom / 7;
                        snapPoint2.setPoint(point2);
                        pSnapPoint.add(snapPoint2);

                        EsspSnapPoint snapPoint3 = new EsspSnapPoint();
                        snapPoint3.setType(EsspSnapType.BOX);
                        snapPoint3.setId(pLineEsspShapes.get(pLineEsspShapes.size() - 1).getId());
                        PointF point3 = new PointF();
                        point3.x = x_Top - getHeight() / 40 + 2 * w_Hom / 7;
                        point3.y = yH - getHeight() / 28 + 5 * h_MatHom / 7;
                        snapPoint3.setPoint(point3);
                        pSnapPoint.add(snapPoint3);

                        EsspSnapPoint snapPoint4 = new EsspSnapPoint();
                        snapPoint4.setType(EsspSnapType.BOX);
                        snapPoint4.setId(pLineEsspShapes.get(pLineEsspShapes.size() - 1).getId());
                        PointF point4 = new PointF();
                        point4.x = x_Top - getHeight() / 40 + 5 * w_Hom / 7;
                        point4.y = yH - getHeight() / 28 + 5 * h_MatHom / 7;
                        snapPoint4.setPoint(point4);
                        pSnapPoint.add(snapPoint4);

                        if (minBox <= yH + getHeight() / 28) {
                            minBox = yH + getHeight() / 28;
                        }
                        EsspCommon.isBuildBox = true;

                        EsspSoDoCDActivity.layerAdapter.updateList(pLineEsspShapes);
                        break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawBox2(MotionEvent ev) {
        try {
            Paint paintfillwhite = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintfillwhite.setStrokeCap(Paint.Cap.ROUND);
            paintfillwhite.setColor(Color.WHITE);
            paintfillwhite.setStyle(Paint.Style.FILL_AND_STROKE);
            paintfillwhite.setStrokeWidth(EsspCommon.width);

            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(EsspCommon.color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(EsspCommon.width);

            PointF[] pointStarts = new PointF[]{new PointF()};
            Path[] paths = new Path[]{new Path(), new Path()};

            if (EsspCommon.isBuildPole || EsspCommon.isBuildWall) {
                esspShape.setEsspShapeType(EsspShapeType.BOX2);
                esspShape.setDrawable(R.mipmap.icon_draw_box_2);
                esspShape.setPaint(new Paint[]{paintfillwhite, paint});
                esspShape.setDx(0);
                esspShape.setDy(0);
                esspShape.setAngle(0);
                esspShape.setId(System.currentTimeMillis());
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(EsspCommon.isBuildPole)
                            xStart = (xPoleLeft + xPoleRight) / 2;
                        else
                            xStart = ev.getX();
                        yStart = ev.getY();
                        pointStarts[0].x = ev.getX();
                        pointStarts[0].y = ev.getY();
                        esspShape.setStartPoint(pointStarts);
                        esspShape.setPath(paths);
                        pLineEsspShapes.add(esspShape);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float xTop = ev.getX();
                        float xLeft = 0f;
                        float y = ev.getY();
                        float direction = (xStart - ev.getX()) / Math.abs(ev.getX() - xStart);
                        float wHom = getHeight() / 20;
                        float hHom = getHeight() / 14;
                        float hMatHom = 5 * getHeight() / 84;
                        float dinhCot = getHeight() / 3.5f + (getHeight() - getHeight() / 3.5f) / 2 - 3.5f * (getHeight() - getHeight() / 3.5f) / 8;
                        if (EsspCommon.isBuildPole) {
                            if (ev.getX() <= xStart) {
                                xTop = xPoleLeft;
                                xLeft = xPoleRight;
                            } else {
                                xTop = xPoleRight;
                                xLeft = xPoleLeft;
                            }
                            if (y < yPoleMin) {
                                y = yPoleMin;
                            }
                            if (y > yPoleMax) {
                                y = yPoleMax;
                            }
                        } else if (EsspCommon.isBuildWall) {
                            if(pWall != null) {
                                RectF rWall = new RectF();
                                pWall.computeBounds(rWall, true);
                                if(xTop < rWall.left + wHom){
                                    xTop = rWall.left + wHom;
                                }
                                if(xTop > rWall.right - wHom){
                                    xTop = rWall.right - wHom;
                                }
                                if(y < rWall.top + hHom) {
                                    y = rWall.top + hHom;
                                }
                                if(y > rWall.bottom - hHom) {
                                    y = rWall.bottom - hHom;
                                }
                            }
                        }
                        //Vẽ hòm mặt trước
                        //Nền hòm
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0] = new Path();
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xTop - getHeight() / 40 + EsspCommon.width, y - getHeight() / 28 + EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xTop + getHeight() / 40 - EsspCommon.width, y - getHeight() / 28 + EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xTop + getHeight() / 40 - EsspCommon.width, y + getHeight() / 28 - EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xTop - getHeight() / 40 + EsspCommon.width, y + getHeight() / 28 - EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xTop - getHeight() / 40 + EsspCommon.width, y - getHeight() / 28 + EsspCommon.width);
                        //Khung hòm
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1] = new Path();
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xTop - getHeight() / 40, y - getHeight() / 28);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop + getHeight() / 40, y - getHeight() / 28);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop + getHeight() / 40, y + getHeight() / 28);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40, y + getHeight() / 28);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40, y - getHeight() / 28);
                        //Đường ngang trên
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xTop - getHeight() / 40, y + getHeight() / 40);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop + getHeight() / 40, y + getHeight() / 40);
                        //Đường ngang dưới
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xTop - getHeight() / 40, y + getHeight() / 42);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop + getHeight() / 40, y + getHeight() / 42);
                        //Vẽ ô 1
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xTop - getHeight() / 40 + wHom / 7, y - getHeight() / 28 + 2 * hMatHom / 6);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 3 * wHom / 7, y - getHeight() / 28 + 2 * hMatHom / 6);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 3 * wHom / 7, y - getHeight() / 28 + 4 * hMatHom / 6);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + wHom / 7, y - getHeight() / 28 + 4 * hMatHom / 6);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + wHom / 7, y - getHeight() / 28 + 2 * hMatHom / 6);
                        //Vẽ ô 2
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xTop - getHeight() / 40 + 4 * wHom / 7, y - getHeight() / 28 + 2 * hMatHom / 6);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 6 * wHom / 7, y - getHeight() / 28 + 2 * hMatHom / 6);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 6 * wHom / 7, y - getHeight() / 28 + 4 * hMatHom / 6);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 4 * wHom / 7, y - getHeight() / 28 + 4 * hMatHom / 6);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 4 * wHom / 7, y - getHeight() / 28 + 2 * hMatHom / 6);
                        //Vẽ hòm mặt ngang
                        if(EsspCommon.isBuildPole) {
                            float wPartCot = getHeight() / 80;
                            float hCot = 3.5f * (getHeight() - getHeight() / 3.5f) / 4;
                            float kcHomTrenVsDinhCot = y - getHeight() / 28 - dinhCot;
                            float kcHomDuoiVsDinhCot = y - getHeight() / 28 - dinhCot + hHom;
                            float doNghiengTren = kcHomTrenVsDinhCot * 4 / hCot;
                            float doNghiengDuoi = kcHomDuoiVsDinhCot * 4 / hCot;
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xLeft - direction * (wPartCot + 5 * wPartCot / 3), y - getHeight() / 28);
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xLeft - direction * (wPartCot + doNghiengTren), y - getHeight() / 28);
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xLeft - direction * (wPartCot + doNghiengDuoi), y + getHeight() / 28);
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xLeft - direction * (wPartCot + wPartCot), y + getHeight() / 28);
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xLeft - direction * (wPartCot + wPartCot + 2), y + getHeight() / 28 - hHom / 4);
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xLeft - direction * (wPartCot + 5 * wPartCot / 3 - 2), y + getHeight() / 28 - hHom / 4 - 2);
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xLeft - direction * (wPartCot + 5 * wPartCot / 3), y - getHeight() / 28);
                        }
                        invalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                        float x_Top = ev.getX();
                        float yH = ev.getY();
                        float w_Hom = getHeight() / 20;
                        float h_MatHom = 5 * getHeight() / 84;

                        if (EsspCommon.isBuildPole) {
                            if (ev.getX() <= xStart) {
                                x_Top = xPoleLeft;
                            } else {
                                x_Top = xPoleRight;
                            }
                            if (yH < yPoleMin) {
                                yH = yPoleMin;
                            }
                            if (yH > yPoleMax) {
                                yH = yPoleMax;
                            }
                        } else if (EsspCommon.isBuildWall) {
                            if(pWall != null) {
                                RectF rWall = new RectF();
                                pWall.computeBounds(rWall, true);
                                if(x_Top < rWall.left + w_Hom){
                                    x_Top = rWall.left + w_Hom;
                                }
                                if(x_Top > rWall.right - w_Hom){
                                    x_Top = rWall.right - w_Hom;
                                }
                                if(yH < rWall.top + h_MatHom) {
                                    yH = rWall.top + h_MatHom;
                                }
                                if(yH > rWall.bottom - h_MatHom) {
                                    yH = rWall.bottom - h_MatHom;
                                }
                            }
                        }

                        EsspSnapPoint snapPoint = new EsspSnapPoint();
                        snapPoint.setType(EsspSnapType.BOX);
                        snapPoint.setId(pLineEsspShapes.get(pLineEsspShapes.size() - 1).getId());
                        PointF point = new PointF();
                        point.x = x_Top - getHeight() / 40 + 2 * w_Hom / 7;
                        point.y = yH - getHeight() / 28 + h_MatHom / 2;
                        snapPoint.setPoint(point);
                        pSnapPoint.add(snapPoint);

                        EsspSnapPoint snapPoint2 = new EsspSnapPoint();
                        snapPoint2.setType(EsspSnapType.BOX);
                        snapPoint2.setId(pLineEsspShapes.get(pLineEsspShapes.size() - 1).getId());
                        PointF point2 = new PointF();
                        point2.x = x_Top - getHeight() / 40 + 5 * w_Hom / 7;
                        point2.y = yH - getHeight() / 28 + h_MatHom / 2;
                        snapPoint2.setPoint(point2);
                        pSnapPoint.add(snapPoint2);

                        if (minBox <= yH + getHeight() / 28) {
                            minBox = yH + getHeight() / 28;
                        }
                        EsspCommon.isBuildBox = true;

                        EsspSoDoCDActivity.layerAdapter.updateList(pLineEsspShapes);
                        break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawBox1(MotionEvent ev) {
        try {
            Paint paintfillwhite = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintfillwhite.setStrokeCap(Paint.Cap.ROUND);
            paintfillwhite.setColor(Color.WHITE);
            paintfillwhite.setStyle(Paint.Style.FILL_AND_STROKE);
            paintfillwhite.setStrokeWidth(EsspCommon.width);

            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(EsspCommon.color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(EsspCommon.width);

            PointF[] pointStarts = new PointF[]{new PointF()};
            Path[] paths = new Path[]{new Path(), new Path()};

            if (EsspCommon.isBuildPole || EsspCommon.isBuildWall) {
                esspShape.setEsspShapeType(EsspShapeType.BOX1);
                esspShape.setDrawable(R.mipmap.icon_draw_box_1);
                esspShape.setPaint(new Paint[]{paintfillwhite, paint});
                esspShape.setDx(0);
                esspShape.setDy(0);
                esspShape.setAngle(0);
                esspShape.setId(System.currentTimeMillis());
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(EsspCommon.isBuildPole)
                            xStart = (xPoleLeft + xPoleRight) / 2;
                        else
                            xStart = ev.getX();
                        yStart = ev.getY();
                        pointStarts[0].x = ev.getX();
                        pointStarts[0].y = ev.getY();
                        esspShape.setStartPoint(pointStarts);
                        esspShape.setPath(paths);
                        pLineEsspShapes.add(esspShape);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float xTop = ev.getX();
                        float xLeft = 0f;
                        float y = ev.getY();
                        float direction = (xStart - ev.getX()) / Math.abs(ev.getX() - xStart);
                        float wHom = getHeight() / 20;
                        float hHom = getHeight() / 14;
                        float hMatHom = 5 * getHeight() / 84;
                        float dinhCot = getHeight() / 3.5f + (getHeight() - getHeight() / 3.5f) / 2 - 3.5f * (getHeight() - getHeight() / 3.5f) / 8;
                        if (EsspCommon.isBuildPole) {
                            if (ev.getX() <= xStart) {
                                xTop = xPoleLeft;
                                xLeft = xPoleRight;
                            } else {
                                xTop = xPoleRight;
                                xLeft = xPoleLeft;
                            }
                            if (y < yPoleMin) {
                                y = yPoleMin;
                            }
                            if (y > yPoleMax) {
                                y = yPoleMax;
                            }
                        } else if (EsspCommon.isBuildWall) {
                            if(pWall != null) {
                                RectF rWall = new RectF();
                                pWall.computeBounds(rWall, true);
                                if(xTop < rWall.left + wHom){
                                    xTop = rWall.left + wHom;
                                }
                                if(xTop > rWall.right - wHom){
                                    xTop = rWall.right - wHom;
                                }
                                if(y < rWall.top + hHom) {
                                    y = rWall.top + hHom;
                                }
                                if(y > rWall.bottom - hHom) {
                                    y = rWall.bottom - hHom;
                                }
                            }
                        }
                        //Vẽ hòm mặt trước
                        //Nền hòm
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0] = new Path();
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xTop - getHeight() / 40 + EsspCommon.width, y - getHeight() / 28 + EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xTop + getHeight() / 40 - EsspCommon.width, y - getHeight() / 28 + EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xTop + getHeight() / 40 - EsspCommon.width, y + getHeight() / 28 - EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xTop - getHeight() / 40 + EsspCommon.width, y + getHeight() / 28 - EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xTop - getHeight() / 40 + EsspCommon.width, y - getHeight() / 28 + EsspCommon.width);
                        //Khung hòm
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1] = new Path();
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xTop - getHeight() / 40, y - getHeight() / 28);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop + getHeight() / 40, y - getHeight() / 28);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop + getHeight() / 40, y + getHeight() / 28);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40, y + getHeight() / 28);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40, y - getHeight() / 28);
                        //Đường ngang trên
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xTop - getHeight() / 40, y + getHeight() / 40);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop + getHeight() / 40, y + getHeight() / 40);
                        //Đường ngang dưới
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xTop - getHeight() / 40, y + getHeight() / 42);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop + getHeight() / 40, y + getHeight() / 42);
                        //Vẽ ô 1
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xTop - getHeight() / 40 + wHom / 4, y - getHeight() / 28 + 2 * hMatHom / 6);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 3 * wHom / 4, y - getHeight() / 28 + 2 * hMatHom / 6);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 3 * wHom / 4, y - getHeight() / 28 + 4 * hMatHom / 6);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + wHom / 4, y - getHeight() / 28 + 4 * hMatHom / 6);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + wHom / 4, y - getHeight() / 28 + 2 * hMatHom / 6);
                        //Vẽ hòm mặt ngang
                        if(EsspCommon.isBuildPole) {
                            float wPartCot = getHeight() / 80;
                            float hCot = 3.5f * (getHeight() - getHeight() / 3.5f) / 4;
                            float kcHomTrenVsDinhCot = y - getHeight() / 28 - dinhCot;
                            float kcHomDuoiVsDinhCot = y - getHeight() / 28 - dinhCot + hHom;
                            float doNghiengTren = kcHomTrenVsDinhCot * 4 / hCot;
                            float doNghiengDuoi = kcHomDuoiVsDinhCot * 4 / hCot;
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xLeft - direction * (wPartCot + 5 * wPartCot / 3), y - getHeight() / 28);
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xLeft - direction * (wPartCot + doNghiengTren), y - getHeight() / 28);
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xLeft - direction * (wPartCot + doNghiengDuoi), y + getHeight() / 28);
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xLeft - direction * (wPartCot + wPartCot), y + getHeight() / 28);
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xLeft - direction * (wPartCot + wPartCot + 2), y + getHeight() / 28 - hHom / 4);
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xLeft - direction * (wPartCot + 5 * wPartCot / 3 - 2), y + getHeight() / 28 - hHom / 4 - 2);
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xLeft - direction * (wPartCot + 5 * wPartCot / 3), y - getHeight() / 28);
                        }
                        invalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                        float x_Top = ev.getX();
                        float yH = ev.getY();
                        float w_Hom = getHeight() / 20;
                        float h_MatHom = 5 * getHeight() / 84;

                        if (EsspCommon.isBuildPole) {
                            if (ev.getX() <= xStart) {
                                x_Top = xPoleLeft;
                            } else {
                                x_Top = xPoleRight;
                            }
                            if (yH < yPoleMin) {
                                yH = yPoleMin;
                            }
                            if (yH > yPoleMax) {
                                yH = yPoleMax;
                            }
                        } else if (EsspCommon.isBuildWall) {
                            if(pWall != null) {
                                RectF rWall = new RectF();
                                pWall.computeBounds(rWall, true);
                                if(x_Top < rWall.left + w_Hom){
                                    x_Top = rWall.left + w_Hom;
                                }
                                if(x_Top > rWall.right - w_Hom){
                                    x_Top = rWall.right - w_Hom;
                                }
                                if(yH < rWall.top + h_MatHom) {
                                    yH = rWall.top + h_MatHom;
                                }
                                if(yH > rWall.bottom - h_MatHom) {
                                    yH = rWall.bottom - h_MatHom;
                                }
                            }
                        }

                        EsspSnapPoint snapPoint = new EsspSnapPoint();
                        snapPoint.setType(EsspSnapType.BOX);
                        snapPoint.setId(pLineEsspShapes.get(pLineEsspShapes.size() - 1).getId());
                        PointF point = new PointF();
                        point.x = x_Top - getHeight() / 40 + w_Hom / 2;
                        point.y = yH - getHeight() / 28 + h_MatHom / 2;
                        snapPoint.setPoint(point);
                        pSnapPoint.add(snapPoint);

                        if (minBox <= yH + getHeight() / 28) {
                            minBox = yH + getHeight() / 28;
                        }
                        EsspCommon.isBuildBox = true;

                        EsspSoDoCDActivity.layerAdapter.updateList(pLineEsspShapes);
                        break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawBox3f(MotionEvent ev) {
        try {
            Paint paintfillwhite = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintfillwhite.setStrokeCap(Paint.Cap.ROUND);
            paintfillwhite.setColor(Color.WHITE);
            paintfillwhite.setStyle(Paint.Style.FILL_AND_STROKE);
            paintfillwhite.setStrokeWidth(EsspCommon.width);

            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(EsspCommon.color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(EsspCommon.width);

            PointF[] pointStarts = new PointF[]{new PointF()};
            Path[] paths = new Path[]{new Path(), new Path()};

            if (EsspCommon.isBuildPole || EsspCommon.isBuildWall) {
                esspShape.setEsspShapeType(EsspShapeType.BOX3F);
                esspShape.setDrawable(R.mipmap.icon_draw_box_1);
                esspShape.setPaint(new Paint[]{paintfillwhite, paint});
                esspShape.setDx(0);
                esspShape.setDy(0);
                esspShape.setAngle(0);
                esspShape.setId(System.currentTimeMillis());
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(EsspCommon.isBuildPole)
                            xStart = (xPoleLeft + xPoleRight) / 2;
                        else
                            xStart = ev.getX();
                        yStart = ev.getY();
                        pointStarts[0].x = ev.getX();
                        pointStarts[0].y = ev.getY();
                        esspShape.setStartPoint(pointStarts);
                        esspShape.setPath(paths);
                        pLineEsspShapes.add(esspShape);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float xTop = ev.getX();
                        float xLeft = 0f;
                        float y = ev.getY();
                        float direction = (xStart - ev.getX()) / Math.abs(ev.getX() - xStart);
                        float wHom = getHeight() / 20;
                        float hHom = getHeight() / 14;
                        float hMatHom = 5 * getHeight() / 84;
                        float dinhCot = getHeight() / 3.5f + (getHeight() - getHeight() / 3.5f) / 2 - 3.5f * (getHeight() - getHeight() / 3.5f) / 8;
                        if (EsspCommon.isBuildPole) {
                            if (ev.getX() <= xStart) {
                                xTop = xPoleLeft;
                                xLeft = xPoleRight;
                            } else {
                                xTop = xPoleRight;
                                xLeft = xPoleLeft;
                            }
                            if (y < yPoleMin) {
                                y = yPoleMin;
                            }
                            if (y > yPoleMax) {
                                y = yPoleMax;
                            }
                        } else if (EsspCommon.isBuildWall) {
                            if(pWall != null) {
                                RectF rWall = new RectF();
                                pWall.computeBounds(rWall, true);
                                if(xTop < rWall.left + wHom){
                                    xTop = rWall.left + wHom;
                                }
                                if(xTop > rWall.right - wHom){
                                    xTop = rWall.right - wHom;
                                }
                                if(y < rWall.top + hHom) {
                                    y = rWall.top + hHom;
                                }
                                if(y > rWall.bottom - hHom) {
                                    y = rWall.bottom - hHom;
                                }
                            }
                        }
                        //Vẽ hòm mặt trước
                        //Nền hòm
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0] = new Path();
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xTop - getHeight() / 40 + EsspCommon.width, y - getHeight() / 28 + EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xTop + getHeight() / 40 - EsspCommon.width, y - getHeight() / 28 + EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xTop + getHeight() / 40 - EsspCommon.width, y + getHeight() / 28 - EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xTop - getHeight() / 40 + EsspCommon.width, y + getHeight() / 28 - EsspCommon.width);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xTop - getHeight() / 40 + EsspCommon.width, y - getHeight() / 28 + EsspCommon.width);
                        //Khung hòm
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1] = new Path();
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xTop - getHeight() / 40, y - getHeight() / 28);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop + getHeight() / 40, y - getHeight() / 28);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop + getHeight() / 40, y + getHeight() / 28);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40, y + getHeight() / 28);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40, y - getHeight() / 28);
                        //Khung trong
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xTop - getHeight() / 40 + wHom/10, y - getHeight() / 28 + wHom/10);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop + getHeight() / 40 - wHom/10, y - getHeight() / 28 + wHom/10);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop + getHeight() / 40 - wHom/10, y + getHeight() / 28 - 2 * wHom/10);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop + getHeight() / 40 - wHom/10 - (wHom - wHom/5) / 7, y + getHeight() / 28 - 2 * wHom/10);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop + getHeight() / 40 - wHom/10 - (wHom - wHom/5) / 7, y + getHeight() / 28 - wHom/10);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop + getHeight() / 40 - wHom/10 - 2 * (wHom - wHom/5) / 7, y + getHeight() / 28 - wHom/10);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop + getHeight() / 40 - wHom/10 - 2 * (wHom - wHom/5) / 7, y + getHeight() / 28 - 2 * wHom/10);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + wHom/10 + 2 * (wHom - wHom/5) / 7, y + getHeight() / 28 - 2 * wHom/10);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + wHom/10 + 2 * (wHom - wHom/5) / 7, y + getHeight() / 28 - wHom/10);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + wHom/10 + (wHom - wHom/5) / 7, y + getHeight() / 28 - wHom/10);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + wHom/10 + (wHom - wHom/5) / 7, y + getHeight() / 28 - 2 * wHom/10);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + wHom/10, y + getHeight() / 28 - 2 * wHom/10);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + wHom/10, y - getHeight() / 28 + wHom/10);
                        //Đường ngang trên
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xTop - getHeight() / 40 + wHom/10, y + getHeight() / 80);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop + getHeight() / 40 - wHom/10, y + getHeight() / 80);
                        //Đường ngang dưới
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xTop - getHeight() / 40 + wHom/10, y + getHeight() / 84);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop + getHeight() / 40 - wHom/10, y + getHeight() / 84);
                        //Vẽ ô 1
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xTop - getHeight() / 40 + wHom / 4, y - getHeight() / 28 + 2 * hMatHom / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 3 * wHom / 4, y - getHeight() / 28 + 2 * hMatHom / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + 3 * wHom / 4, y - getHeight() / 28 + 4 * hMatHom / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + wHom / 4, y - getHeight() / 28 + 4 * hMatHom / 8);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xTop - getHeight() / 40 + wHom / 4, y - getHeight() / 28 + 2 * hMatHom / 8);
                        //Vẽ hòm mặt ngang
                        if(EsspCommon.isBuildPole) {
                            float wPartCot = getHeight() / 80;
                            float hCot = 3.5f * (getHeight() - getHeight() / 3.5f) / 4;
                            float kcHomTrenVsDinhCot = y - getHeight() / 28 - dinhCot;
                            float kcHomDuoiVsDinhCot = y - getHeight() / 28 - dinhCot + hHom;
                            float doNghiengTren = kcHomTrenVsDinhCot * 4 / hCot;
                            float doNghiengDuoi = kcHomDuoiVsDinhCot * 4 / hCot;
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(xLeft - direction * (wPartCot + 5 * wPartCot / 3), y - getHeight() / 28);
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xLeft - direction * (wPartCot + doNghiengTren), y - getHeight() / 28);
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xLeft - direction * (wPartCot + doNghiengDuoi), y + getHeight() / 28);
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xLeft - direction * (wPartCot + wPartCot), y + getHeight() / 28);
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xLeft - direction * (wPartCot + wPartCot + 2), y + getHeight() / 28 - hHom / 4);
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xLeft - direction * (wPartCot + 5 * wPartCot / 3 - 2), y + getHeight() / 28 - hHom / 4 - 2);
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(xLeft - direction * (wPartCot + 5 * wPartCot / 3), y - getHeight() / 28);
                        }
                        invalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                        float x_Top = ev.getX();
                        float w_Hom = getHeight() / 20;
                        float h_MatHom = 5 * getHeight() / 84;
                        float yH = ev.getY();

                        if (EsspCommon.isBuildPole) {
                            if (ev.getX() <= xStart) {
                                x_Top = xPoleLeft;
                            } else {
                                x_Top = xPoleRight;
                            }
                            if (yH < yPoleMin) {
                                yH = yPoleMin;
                            }
                            if (yH > yPoleMax) {
                                yH = yPoleMax;
                            }
                        } else if (EsspCommon.isBuildWall) {
                            if(pWall != null) {
                                RectF rWall = new RectF();
                                pWall.computeBounds(rWall, true);
                                if(x_Top < rWall.left + w_Hom){
                                    x_Top = rWall.left + w_Hom;
                                }
                                if(x_Top > rWall.right - w_Hom){
                                    x_Top = rWall.right - w_Hom;
                                }
                                if(yH < rWall.top + h_MatHom) {
                                    yH = rWall.top + h_MatHom;
                                }
                                if(yH > rWall.bottom - h_MatHom) {
                                    yH = rWall.bottom - h_MatHom;
                                }
                            }
                        }

                        EsspSnapPoint snapPoint = new EsspSnapPoint();
                        snapPoint.setType(EsspSnapType.BOX);
                        snapPoint.setId(pLineEsspShapes.get(pLineEsspShapes.size() - 1).getId());
                        PointF point = new PointF();
                        point.x = x_Top - getHeight() / 40 + w_Hom / 2;
                        point.y = yH - getHeight() / 28 + 3 * h_MatHom / 8;
                        snapPoint.setPoint(point);
                        pSnapPoint.add(snapPoint);

                        if (minBox <= yH + getHeight() / 28) {
                            minBox = yH + getHeight() / 28;
                        }
                        EsspCommon.isBuildBox = true;

                        EsspSoDoCDActivity.layerAdapter.updateList(pLineEsspShapes);
                        break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawBoxBehind(MotionEvent ev) {
        try {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(EsspCommon.color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(EsspCommon.width);

            PointF[] pointStarts = new PointF[]{new PointF()};
            Path[] paths = new Path[]{new Path()};

            if (EsspCommon.isBuildPole) {
                esspShape.setEsspShapeType(EsspShapeType.BOX4);
                esspShape.setDrawable(R.mipmap.icon_draw_box_behind);
                esspShape.setPaint(new Paint[]{paint});
                esspShape.setDx(0);
                esspShape.setDy(0);
                esspShape.setAngle(0);
                esspShape.setId(System.currentTimeMillis());
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        xStart = (xPoleLeft + xPoleRight) / 2;
                        yStart = ev.getY();
                        pointStarts[0].x = ev.getX();
                        pointStarts[0].y = ev.getY();
                        esspShape.setStartPoint(pointStarts);
                        esspShape.setPath(paths);
                        pLineEsspShapes.add(esspShape);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float xTop = 0f;
                        float xLeft = 0f;
                        float direction = (ev.getX() - xStart) / Math.abs(ev.getX() - xStart);
                        if (ev.getX() <= xStart) {
                            xTop = xPoleLeft;
                            xLeft = xPoleRight;
                        } else {
                            xTop = xPoleRight;
                            xLeft = xPoleLeft;
                        }
                        float y = ev.getY();
                        if (y < yPoleMin) {
                            y = yPoleMin;
                        }
                        if (y > yPoleMax) {
                            y = yPoleMax;
                        }
                        float wHom = getHeight() / 20;
                        float hHom = getHeight() / 14;
                        float hMatHom = 5 * getHeight() / 84;
                        float dinhCot = getHeight() / 3.5f + (getHeight() - getHeight() / 3.5f) / 2 - 3.5f * (getHeight() - getHeight() / 3.5f) / 8;
                        float wPartCot = getHeight() / 80;
                        float hCot = 3.5f * (getHeight() - getHeight() / 3.5f) / 4;
                        float kcHomTrenVsDinhCot = y - getHeight() / 28 - dinhCot;
                        float kcHomDuoiVsDinhCot = y - getHeight() / 28 - dinhCot + hHom;
                        float doNghiengTren = kcHomTrenVsDinhCot * 4 / hCot;
                        float doNghiengDuoi = kcHomDuoiVsDinhCot * 4 / hCot;
                        //Vẽ hòm mặt trước
                        //Khung hòm
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0] = new Path();
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xTop - getHeight() / 80 - doNghiengTren, y - getHeight() / 28);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xTop - getHeight() / 40, y - getHeight() / 28);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xTop - getHeight() / 40, y + getHeight() / 28);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xTop - getHeight() / 80 - doNghiengDuoi, y + getHeight() / 28);

                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xTop + getHeight() / 80 + doNghiengTren, y - getHeight() / 28);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xTop + getHeight() / 40, y - getHeight() / 28);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xTop + getHeight() / 40, y + getHeight() / 28);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xTop + getHeight() / 80 + doNghiengDuoi, y + getHeight() / 28);

                        //Vẽ hòm mặt ngang
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xLeft - direction * (wPartCot + 5 * wPartCot / 3), y - getHeight() / 28);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xLeft - direction * (wPartCot + doNghiengTren), y - getHeight() / 28);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xLeft - direction * (wPartCot + doNghiengDuoi), y + getHeight() / 28);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xLeft - direction * (wPartCot + wPartCot), y + getHeight() / 28);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xLeft - direction * (wPartCot + wPartCot + 2), y + getHeight() / 28 - hHom / 4);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xLeft - direction * (wPartCot + 5 * wPartCot / 3 - 2), y + getHeight() / 28 - hHom / 4 - 2);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xLeft - direction * (wPartCot + 5 * wPartCot / 3), y - getHeight() / 28);
                        invalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                        float yH = ev.getY();
                        if (yH < yPoleMin) {
                            yH = yPoleMin;
                        }
                        if (yH > yPoleMax) {
                            yH = yPoleMax;
                        }
                        if (minBox <= yH + getHeight() / 28) {
                            minBox = yH + getHeight() / 28;
                        }
                        EsspCommon.isBuildBox = true;

                        EsspSoDoCDActivity.layerAdapter.updateList(pLineEsspShapes);
                        break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawArrowNote(MotionEvent ev) {
        try {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(EsspCommon.color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(EsspCommon.width);

            Paint paintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintFill.setStrokeCap(Paint.Cap.ROUND);
            paintFill.setColor(EsspCommon.color);
            paintFill.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setStrokeWidth(EsspCommon.width);

            Paint paintText = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            paintText.setStrokeCap(Paint.Cap.ROUND);
            paintText.setColor(EsspCommon.color);
            paintText.setStyle(Paint.Style.FILL);
            paintText.setStrokeWidth(EsspCommon.width);
            paintText.setTextSize(EsspCommon.textSize);

            Rect bounds = new Rect();
            paintText.getTextBounds(EsspCommon.text, 0, EsspCommon.text.length(), bounds);

            if (EsspCommon.isBuildBox) {
                PointF[] pointStarts = new PointF[]{new PointF(), new PointF()};
                PointF[] pointEnds = new PointF[]{new PointF()};

                esspShape.setEsspShapeType(EsspShapeType.ARROW_NOTE);
                esspShape.setDrawable(R.mipmap.icon_draw_arrow_note);
                esspShape.setPaint(new Paint[]{paint, paintFill, paintText});
                esspShape.setDx(0);
                esspShape.setDy(0);
                esspShape.setAngle(0);
                esspShape.setId(System.currentTimeMillis());

                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        pointStarts[0].x = ev.getX();
                        pointStarts[0].y = minBox;
                        pointStarts[1].x = ev.getX() - bounds.width() - 5;
                        pointStarts[1].y = (getHeight() / 3.5f + (getHeight() - getHeight() / 3.5f) / 2 + 3.5f * (getHeight() - getHeight() / 3.5f) / 8 + minBox) / 2 - bounds.height() / 2;
                        pointEnds[0].x = ev.getX();
                        pointEnds[0].y = getHeight() / 3.5f + (getHeight() - getHeight() / 3.5f) / 2 + 3.5f * (getHeight() - getHeight() / 3.5f) / 8;
                        esspShape.setText(new String[]{EsspCommon.text});
                        esspShape.setStartPoint(pointStarts);
                        esspShape.setEndPoint(pointEnds);
                        esspShape.setPath(new Path[]{new Path()});
                        pLineEsspShapes.add(esspShape);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0] = new Path();
                        float x = ev.getX();
                        float y = pLineEsspShapes.get(pLineEsspShapes.size() - 1).getEndPoint()[0].y;
                        float xStart = ev.getX();
                        float yStart = pLineEsspShapes.get(pLineEsspShapes.size() - 1).getStartPoint()[0].y;

                        float disY = y - pLineEsspShapes.get(pLineEsspShapes.size() - 1).getStartPoint()[0].y;
                        x = pLineEsspShapes.get(pLineEsspShapes.size() - 1).getStartPoint()[0].x;
                        float yArrow = disY / Math.abs(disY);

                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(xStart, yStart);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xStart - 10, yStart - 15 * yArrow * -1);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xStart + 10, yStart - 15 * yArrow * -1);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(xStart, yStart);

                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(x, y);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x - 10, y - 15 * yArrow);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x + 10, y - 15 * yArrow);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x, y);

                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getEndPoint()[0].x = x;
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getStartPoint()[0].x = xStart;

                        if (xPoleLeft >= x) {
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getStartPoint()[1].x = ev.getX() - bounds.width() - 5;
                        } else {
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).getStartPoint()[1].x = ev.getX() + 5;
                        }
                        invalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                        EsspSoDoCDActivity.layerAdapter.updateList(pLineEsspShapes);
                        break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawStreet(MotionEvent ev) {
        try {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(EsspCommon.color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(EsspCommon.width);

            Paint paintWhite = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintWhite.setStrokeCap(Paint.Cap.ROUND);
            paintWhite.setColor(Color.WHITE);
            paintWhite.setStyle(Paint.Style.FILL);
            paintWhite.setStrokeWidth(EsspCommon.width);

            Path[] paths = new Path[]{new Path(), new Path()};

            esspShape.setEsspShapeType(EsspShapeType.DUONG);
            esspShape.setDrawable(R.mipmap.icon_draw_duong);
            esspShape.setPaint(new Paint[]{paint, paintWhite});
            esspShape.setDx(0);
            esspShape.setDy(0);
            esspShape.setAngle(0);
            esspShape.setId(System.currentTimeMillis());
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isViewDraw = true;
                    xStart = ev.getX();
                    yStart = ev.getY();

                    PointF pStart = changePointStreet(xStart, yStart, MotionEvent.ACTION_DOWN);
                    xStart = pStart.x;
                    yStart = pStart.y;

                    esspShape.setStartPoint(new PointF[]{new PointF(xStart, yStart)});
                    esspShape.setPath(paths);
                    if(posText == -1) {
                        pLineEsspShapes.add(esspShape);
                    } else {
                        pLineEsspShapes.add(posText, esspShape);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(posText == -1) {
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0] = new Path();
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1] = new Path();
                    } else {
                        pLineEsspShapes.get(posText).getPath()[0] = new Path();
                        pLineEsspShapes.get(posText).getPath()[1] = new Path();
                    }

                    xEnd = ev.getX();
                    yEnd = ev.getY();

                    PointF pEnd = changePointStreet(xEnd, yEnd, MotionEvent.ACTION_MOVE);
                    xEnd = pEnd.x;
                    yEnd = pEnd.y;

                    float x1 = 0f;
                    float x2 = 0f;
                    float x3 = 0f;
                    float x4 = 0f;
                    float y1 = 0f;
                    float y2 = 0f;
                    float y3 = 0f;
                    float y4 = 0f;
                    float x1F = 0f;
                    float x2F = 0f;
                    float x3F = 0f;
                    float x4F = 0f;
                    float y1F = 0f;
                    float y2F = 0f;
                    float y3F = 0f;
                    float y4F = 0f;
                    float wTouch = xEnd - xStart;
                    float hTouch = yEnd - yStart;

                    float d1 = Math.abs(xEnd - xStart);
                    float d2 = Math.abs(yEnd - yStart);
                    double conor = Math.toDegrees(Math.atan(d1 / d2));
                    float hStreet = 0f;

                    if (Math.abs(wTouch) >= Math.abs(hTouch)) {
                        hStreet = (float) ((EsspSoDoCDActivity.wStreet / 2) / Math.sin(Math.toRadians(conor)));
                        x1 = xStart;
                        x2 = xEnd;
                        x3 = xStart;
                        x4 = xEnd;

                        y1 = yStart - hStreet;
                        y3 = yStart + hStreet;
                        y2 = yEnd - hStreet;
                        y4 = yEnd + hStreet;

                        x1F = xStart;
                        x2F = xEnd;
                        x3F = xStart;
                        x4F = xEnd;

                        y1F = yStart - hStreet + EsspCommon.width;
                        y3F = yStart + hStreet - EsspCommon.width;
                        y2F = yEnd - hStreet + EsspCommon.width;
                        y4F = yEnd + hStreet - EsspCommon.width;


                    } else {
                        hStreet = (float) ((EsspSoDoCDActivity.wStreet / 2) / Math.cos(Math.toRadians(conor)));
                        y1 = yStart;
                        y2 = yEnd;
                        y3 = yStart;
                        y4 = yEnd;

                        x1 = xStart - hStreet;
                        x3 = xStart + hStreet;
                        x2 = xEnd - hStreet;
                        x4 = xEnd + hStreet;

                        y1F = yStart;
                        y2F = yEnd;
                        y3F = yStart;
                        y4F = yEnd;

                        x1F = xStart - hStreet + EsspCommon.width;
                        x3F = xStart + hStreet - EsspCommon.width;
                        x2F = xEnd - hStreet + EsspCommon.width;
                        x4F = xEnd + hStreet - EsspCommon.width;
                    }

                    if (esspPointDown != null) {
                        PointF p1 = new PointF(x1, y1);
                        PointF p2 = new PointF(x2, y2);
                        PointF ps1 = esspPointDown.getP1();
                        PointF ps2 = esspPointDown.getP2();
                        PointF pDown1 = getPointIntersect(p1, p2, ps1, ps2);
                        PointF p3 = new PointF(x3, y3);
                        PointF p4 = new PointF(x4, y4);
                        PointF pDown2 = getPointIntersect(p3, p4, ps1, ps2);
                        x1 = pDown1.x;
                        y1 = pDown1.y;
                        x3 = pDown2.x;
                        y3 = pDown2.y;

                        PointF p1F = new PointF(x1F, y1F);
                        PointF p2F = new PointF(x2F, y2F);
                        PointF ps1F = esspPointDown.getP1();
                        PointF ps2F = esspPointDown.getP2();
                        PointF pDown1F = getPointIntersect(p1F, p2F, ps1F, ps2F);
                        PointF p3F = new PointF(x3F, y3F);
                        PointF p4F = new PointF(x4F, y4F);
                        PointF pDown2F = getPointIntersect(p3F, p4F, ps1F, ps2F);
                        x1F = pDown1F.x;
                        y1F = pDown1F.y;
                        x3F = pDown2F.x;
                        y3F = pDown2F.y;
                    }

                    if (esspPointMove != null) {
                        PointF p1 = new PointF(x1, y1);
                        PointF p2 = new PointF(x2, y2);
                        PointF ps1 = esspPointMove.getP1();
                        PointF ps2 = esspPointMove.getP2();
                        PointF pDown1 = getPointIntersect(p1, p2, ps1, ps2);
                        PointF p3 = new PointF(x3, y3);
                        PointF p4 = new PointF(x4, y4);
                        PointF pDown2 = getPointIntersect(p3, p4, ps1, ps2);
                        x2 = pDown1.x;
                        y2 = pDown1.y;
                        x4 = pDown2.x;
                        y4 = pDown2.y;

                        PointF p1F = new PointF(x1F, y1F);
                        PointF p2F = new PointF(x2F, y2F);
                        PointF ps1F = esspPointMove.getP1();
                        PointF ps2F = esspPointMove.getP2();
                        PointF pDown1F = getPointIntersect(p1F, p2F, ps1F, ps2F);
                        PointF p3F = new PointF(x3F, y3F);
                        PointF p4F = new PointF(x4F, y4F);
                        PointF pDown2F = getPointIntersect(p3F, p4F, ps1F, ps2F);
                        x2F = pDown1F.x;
                        y2F = pDown1F.y;
                        x4F = pDown2F.x;
                        y4F = pDown2F.y;
                    }

                    if(posText == -1) {
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(x1, y1);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x2, y2);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(x3, y3);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x4, y4);

                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(x1F, y1F);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(x2F, y2F);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(x4F, y4F);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(x3F, y3F);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(x1F, y1F);
                    } else {
                        pLineEsspShapes.get(posText).getPath()[0].moveTo(x1, y1);
                        pLineEsspShapes.get(posText).getPath()[0].lineTo(x2, y2);
                        pLineEsspShapes.get(posText).getPath()[0].moveTo(x3, y3);
                        pLineEsspShapes.get(posText).getPath()[0].lineTo(x4, y4);

                        pLineEsspShapes.get(posText).getPath()[1].moveTo(x1F, y1F);
                        pLineEsspShapes.get(posText).getPath()[1].lineTo(x2F, y2F);
                        pLineEsspShapes.get(posText).getPath()[1].lineTo(x4F, y4F);
                        pLineEsspShapes.get(posText).getPath()[1].lineTo(x3F, y3F);
                        pLineEsspShapes.get(posText).getPath()[1].lineTo(x1F, y1F);
                    }
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    EsspPoint point = new EsspPoint();
                    point.setP1(new PointF(xStart, yStart));
                    point.setP2(new PointF(xEnd, yEnd));
                    pSnapPointDuong.add(point);
                    EsspSoDoCDActivity.layerAdapter.updateList(pLineEsspShapes);
                    esspPointDown = null;
                    esspPointMove = null;
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawLane(MotionEvent ev) {
        try {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(EsspCommon.color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(EsspCommon.width);

            Paint paintWhite = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintWhite.setStrokeCap(Paint.Cap.ROUND);
            paintWhite.setColor(Color.WHITE);
            paintWhite.setStyle(Paint.Style.FILL);
            paintWhite.setStrokeWidth(EsspCommon.width);

            Path[] paths = new Path[]{new Path(), new Path()};

            esspShape.setEsspShapeType(EsspShapeType.DUONG);
            esspShape.setDrawable(R.mipmap.icon_draw_ngo);
            esspShape.setPaint(new Paint[]{paint, paintWhite});
            esspShape.setDx(0);
            esspShape.setDy(0);
            esspShape.setAngle(0);
            esspShape.setId(System.currentTimeMillis());
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isViewDraw = true;
                    xStart = ev.getX();
                    yStart = ev.getY();

                    PointF pStart = changePointStreet(xStart, yStart, MotionEvent.ACTION_DOWN);
                    xStart = pStart.x;
                    yStart = pStart.y;

                    esspShape.setStartPoint(new PointF[]{new PointF(xStart, yStart)});
                    esspShape.setPath(paths);
                    if(posText == -1) {
                        pLineEsspShapes.add(esspShape);
                    } else {
                        pLineEsspShapes.add(posText, esspShape);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(posText == -1) {
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0] = new Path();
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1] = new Path();
                    } else {
                        pLineEsspShapes.get(posText).getPath()[0] = new Path();
                        pLineEsspShapes.get(posText).getPath()[1] = new Path();
                    }

                    xEnd = ev.getX();
                    yEnd = ev.getY();

                    PointF pEnd = changePointStreet(xEnd, yEnd, MotionEvent.ACTION_MOVE);
                    xEnd = pEnd.x;
                    yEnd = pEnd.y;

                    float x1 = 0f;
                    float x2 = 0f;
                    float x3 = 0f;
                    float x4 = 0f;
                    float y1 = 0f;
                    float y2 = 0f;
                    float y3 = 0f;
                    float y4 = 0f;
                    float x1F = 0f;
                    float x2F = 0f;
                    float x3F = 0f;
                    float x4F = 0f;
                    float y1F = 0f;
                    float y2F = 0f;
                    float y3F = 0f;
                    float y4F = 0f;
                    float wTouch = xEnd - xStart;
                    float hTouch = yEnd - yStart;

                    float d1 = Math.abs(xEnd - xStart);
                    float d2 = Math.abs(yEnd - yStart);
                    double conor = Math.toDegrees(Math.atan(d1 / d2));
                    float hStreet = 0f;

                    if (Math.abs(wTouch) >= Math.abs(hTouch)) {
                        hStreet = (float) ((EsspSoDoCDActivity.wLane / 2) / Math.sin(Math.toRadians(conor)));
                        x1 = xStart;
                        x2 = xEnd;
                        x3 = xStart;
                        x4 = xEnd;

                        y1 = yStart - hStreet;
                        y3 = yStart + hStreet;
                        y2 = yEnd - hStreet;
                        y4 = yEnd + hStreet;

                        x1F = xStart;
                        x2F = xEnd;
                        x3F = xStart;
                        x4F = xEnd;

                        y1F = yStart - hStreet + EsspCommon.width;
                        y3F = yStart + hStreet - EsspCommon.width;
                        y2F = yEnd - hStreet + EsspCommon.width;
                        y4F = yEnd + hStreet - EsspCommon.width;


                    } else {
                        hStreet = (float) ((EsspSoDoCDActivity.wLane / 2) / Math.cos(Math.toRadians(conor)));
                        y1 = yStart;
                        y2 = yEnd;
                        y3 = yStart;
                        y4 = yEnd;

                        x1 = xStart - hStreet;
                        x3 = xStart + hStreet;
                        x2 = xEnd - hStreet;
                        x4 = xEnd + hStreet;

                        y1F = yStart;
                        y2F = yEnd;
                        y3F = yStart;
                        y4F = yEnd;

                        x1F = xStart - hStreet + EsspCommon.width;
                        x3F = xStart + hStreet - EsspCommon.width;
                        x2F = xEnd - hStreet + EsspCommon.width;
                        x4F = xEnd + hStreet - EsspCommon.width;
                    }

                    if (esspPointDown != null) {
                        PointF p1 = new PointF(x1, y1);
                        PointF p2 = new PointF(x2, y2);
                        PointF ps1 = esspPointDown.getP1();
                        PointF ps2 = esspPointDown.getP2();
                        PointF pDown1 = getPointIntersect(p1, p2, ps1, ps2);
                        PointF p3 = new PointF(x3, y3);
                        PointF p4 = new PointF(x4, y4);
                        PointF pDown2 = getPointIntersect(p3, p4, ps1, ps2);
                        x1 = pDown1.x;
                        y1 = pDown1.y;
                        x3 = pDown2.x;
                        y3 = pDown2.y;

                        PointF p1F = new PointF(x1F, y1F);
                        PointF p2F = new PointF(x2F, y2F);
                        PointF ps1F = esspPointDown.getP1();
                        PointF ps2F = esspPointDown.getP2();
                        PointF pDown1F = getPointIntersect(p1F, p2F, ps1F, ps2F);
                        PointF p3F = new PointF(x3F, y3F);
                        PointF p4F = new PointF(x4F, y4F);
                        PointF pDown2F = getPointIntersect(p3F, p4F, ps1F, ps2F);
                        x1F = pDown1F.x;
                        y1F = pDown1F.y;
                        x3F = pDown2F.x;
                        y3F = pDown2F.y;
                    }

                    if (esspPointMove != null) {
                        PointF p1 = new PointF(x1, y1);
                        PointF p2 = new PointF(x2, y2);
                        PointF ps1 = esspPointMove.getP1();
                        PointF ps2 = esspPointMove.getP2();
                        PointF pDown1 = getPointIntersect(p1, p2, ps1, ps2);
                        PointF p3 = new PointF(x3, y3);
                        PointF p4 = new PointF(x4, y4);
                        PointF pDown2 = getPointIntersect(p3, p4, ps1, ps2);
                        x2 = pDown1.x;
                        y2 = pDown1.y;
                        x4 = pDown2.x;
                        y4 = pDown2.y;

                        PointF p1F = new PointF(x1F, y1F);
                        PointF p2F = new PointF(x2F, y2F);
                        PointF ps1F = esspPointMove.getP1();
                        PointF ps2F = esspPointMove.getP2();
                        PointF pDown1F = getPointIntersect(p1F, p2F, ps1F, ps2F);
                        PointF p3F = new PointF(x3F, y3F);
                        PointF p4F = new PointF(x4F, y4F);
                        PointF pDown2F = getPointIntersect(p3F, p4F, ps1F, ps2F);
                        x2F = pDown1F.x;
                        y2F = pDown1F.y;
                        x4F = pDown2F.x;
                        y4F = pDown2F.y;
                    }

                    if(posText == -1) {
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(x1, y1);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x2, y2);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].moveTo(x3, y3);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[0].lineTo(x4, y4);

                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].moveTo(x1F, y1F);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(x2F, y2F);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(x4F, y4F);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(x3F, y3F);
                        pLineEsspShapes.get(pLineEsspShapes.size() - 1).getPath()[1].lineTo(x1F, y1F);
                    } else {
                        pLineEsspShapes.get(posText).getPath()[0].moveTo(x1, y1);
                        pLineEsspShapes.get(posText).getPath()[0].lineTo(x2, y2);
                        pLineEsspShapes.get(posText).getPath()[0].moveTo(x3, y3);
                        pLineEsspShapes.get(posText).getPath()[0].lineTo(x4, y4);

                        pLineEsspShapes.get(posText).getPath()[1].moveTo(x1F, y1F);
                        pLineEsspShapes.get(posText).getPath()[1].lineTo(x2F, y2F);
                        pLineEsspShapes.get(posText).getPath()[1].lineTo(x4F, y4F);
                        pLineEsspShapes.get(posText).getPath()[1].lineTo(x3F, y3F);
                        pLineEsspShapes.get(posText).getPath()[1].lineTo(x1F, y1F);
                    }
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    EsspPoint point = new EsspPoint();
                    point.setP1(new PointF(xStart, yStart));
                    point.setP2(new PointF(xEnd, yEnd));
                    pSnapPointDuong.add(point);
                    EsspSoDoCDActivity.layerAdapter.updateList(pLineEsspShapes);
                    esspPointDown = null;
                    esspPointMove = null;
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //endregion

    //region Function get point snap
    private PointF getPointSnap(float x, float y) {
        PointF p = new PointF();
        double disMin = 1000d;
        PointF minPoint = new PointF();
        for (int i = 0; i < pSnapPoint.size(); i++) {
            EsspSnapPoint snapPoint = pSnapPoint.get(i);
            double dis = Math.sqrt(Math.pow(x - snapPoint.getPoint().x, 2) + Math.pow(y - snapPoint.getPoint().y, 2));
            if (disMin >= dis) {
                disMin = dis;
                minPoint = snapPoint.getPoint();
            }
        }
        double dis = Math.sqrt(Math.pow(x - minPoint.x, 2) + Math.pow(y - minPoint.y, 2));
        if (dis <= getHeight() / 12) {
            p = minPoint;
        } else {
            p = new PointF(x, y);
        }
        return p;
    }

    private PointF changePointStreet(float x, float y, int action) {
        PointF pStreet = new PointF(x, y);
        try {
            float disMin = 1000f;
            if (pSnapPointDuong != null) {
                for (int i = 0; i < pSnapPointDuong.size(); i++) {
                    PointF pTouch = new PointF(x, y);
                    PointF pOnStreet = getPointOnStreet(pSnapPointDuong.get(i).getP1(), pSnapPointDuong.get(i).getP2(), pTouch);
                    float dis = calDis(pTouch, pOnStreet);
                    if (disMin >= dis) {
                        disMin = dis;
                        if (disMin <= EsspSoDoCDActivity.wStreet * 2f) {
                            pStreet = pOnStreet;
                            if (action == MotionEvent.ACTION_DOWN) {
                                esspPointDown = pSnapPointDuong.get(i);
                            } else {
                                esspPointMove = pSnapPointDuong.get(i);
                            }
                        } else {
                            esspPointDown = null;
                            esspPointMove = null;
                        }
                    }
                }
            }
            return pStreet;
        } catch (Exception ex) {
            ex.printStackTrace();
            return pStreet;
        }
    }

    private PointF changePointTextOnStreet(float x, float y, int action) {
        PointF pStreet = new PointF(x, y);
        try {
            float disMin = 1000f;
            if (pSnapPointDuong != null) {
                for (int i = 0; i < pSnapPointDuong.size(); i++) {
                    PointF pTouch = new PointF(x, y);
                    PointF pOnStreet = getPointOnStreet(pSnapPointDuong.get(i).getP1(), pSnapPointDuong.get(i).getP2(), pTouch);
                    float dis = calDis(pTouch, pOnStreet);
                    if (disMin >= dis) {
                        disMin = dis;
                        if (disMin <= EsspSoDoCDActivity.wStreet * 2f) {
                            pStreet = pOnStreet;
                            pStreet.y += EsspSoDoCDActivity.wStreet/4;
                            if (action == MotionEvent.ACTION_DOWN) {
                                esspPointDown = pSnapPointDuong.get(i);
                            } else {
                                esspPointMove = pSnapPointDuong.get(i);
                            }
                            float dau = 1f;
                            float angle = 0f;
                            float a = Math.abs(pSnapPointDuong.get(i).getP1().x - pSnapPointDuong.get(i).getP2().x);
                            float b = Math.abs(pSnapPointDuong.get(i).getP1().y - pSnapPointDuong.get(i).getP2().y);
                            float tana = b/a;
//                            angle = (float)(tana * (180/Math.PI));
                            angle = (float)(Math.atan(tana)/(Math.PI/180));
                            if((pSnapPointDuong.get(i).getP1().x <= pSnapPointDuong.get(i).getP2().x
                                    && pSnapPointDuong.get(i).getP1().y <= pSnapPointDuong.get(i).getP2().y)
                                    || (pSnapPointDuong.get(i).getP1().x >= pSnapPointDuong.get(i).getP2().x
                                    && pSnapPointDuong.get(i).getP1().y >= pSnapPointDuong.get(i).getP2().y)){
                                dau = 1f;
                            } else {
                                dau = -1f;
                            }
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).setAngle(dau * angle);
                        } else {
                            esspPointDown = null;
                            esspPointMove = null;
                            pLineEsspShapes.get(pLineEsspShapes.size() - 1).setAngle(0);
                        }
                    }
                }
            }
            return pStreet;
        } catch (Exception ex) {
            return pStreet;
        }
    }

    private float calDis(PointF pTouch, PointF pointOnStreet) {
        return (float) Math.sqrt(Math.pow(pTouch.x - pointOnStreet.x, 2) + Math.pow(pTouch.y - pointOnStreet.y, 2));
    }

    private PointF getPointOnStreet(PointF p1, PointF p2, PointF pTouch) {
        try {
            float Ux = p2.x - p1.x;
            float Uy = p2.y - p1.y;
            float a1 = Uy;
            float b1 = -Ux;
            float c1 = Ux * p1.y - Uy * p1.x;
            float a2 = Ux;
            float b2 = Uy;
            float c2 = -Ux * pTouch.x - Uy * pTouch.y;
            float y = (a2 * c1 - a1 * c2) / (a1 * b2 - a2 * b1);
            float x = (-b1 * y - c1) / a1;
            return new PointF(x, y);
        } catch (Exception ex) {
            return pTouch;
        }
    }

    private PointF getPointIntersect(PointF p1, PointF p2, PointF ps1, PointF ps2) {
        try {
            float Upx = p2.x - p1.x;
            float Upy = p2.y - p1.y;
            float Upsx = ps2.x - ps1.x;
            float Upsy = ps2.y - ps1.y;
            float a1 = Upy;
            float b1 = -Upx;
            float c1 = Upx * p1.y - Upy * p1.x;
            float a2 = Upsy;
            float b2 = -Upsx;
            float c2 = Upsx * ps1.y - Upsy * ps1.x;
            float y = (a2 * c1 - a1 * c2) / (a1 * b2 - a2 * b1);
            float x = (-b1 * y - c1) / a1;
            return new PointF(x, y);
        } catch (Exception ex) {
            return p1;
        }
    }
    //endregion

    //region init shape
    public void drawTop(Canvas canvas) {
        try {
            if (EsspSoDoCDActivity.isDrawTop) {
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setStrokeCap(Paint.Cap.ROUND);
                paint.setColor(EsspCommon.color);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(EsspCommon.width);

                Paint paintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
                paintFill.setStrokeCap(Paint.Cap.ROUND);
                paintFill.setColor(EsspCommon.color);
                paintFill.setStyle(Paint.Style.FILL);
                paintFill.setStrokeWidth(EsspCommon.width);

                Paint paintText = new TextPaint(Paint.ANTI_ALIAS_FLAG);
                paintText.setStrokeCap(Paint.Cap.ROUND);
                paintText.setColor(EsspCommon.color);
                paintText.setStyle(Paint.Style.FILL);
                paintText.setStrokeWidth(EsspCommon.width);
                paintText.setTextSize(EsspCommon.textSize);

                canvas.drawLine(0, getcHeight() / 3.5f, getcWidth(), getcHeight() / 3.5f, getPaint());
                //Khung tên
                Path path = new Path();
                path.moveTo(getWidth() / 5, (getHeight() / 3.5f) / 12);
                path.lineTo(3 * getWidth() / 5, (getHeight() / 3.5f) / 12);
                path.lineTo(3 * getWidth() / 5, (getHeight() / 3.5f) / 2);
                path.lineTo(getWidth() / 5, (getHeight() / 3.5f) / 2);
                path.lineTo(getWidth() / 5, (getHeight() / 3.5f) / 12);
                //Đường dây vào nhà
                path.moveTo(getWidth() / 10, 7.2f * (getHeight() / 3.5f) / 12);
                path.lineTo(6.5f * getWidth() / 10, 7.2f * (getHeight() / 3.5f) / 12);
                path.moveTo(2 * getWidth() / 5, (getHeight() / 3.5f) / 2);
                path.quadTo(2 * getWidth() / 5, 7 * (getHeight() / 3.5f) / 12, 2.2f * getWidth() / 5, 7 * (getHeight() / 3.5f) / 12);
                path.lineTo(6.5f * getWidth() / 10, 7 * (getHeight() / 3.5f) / 12);
                //Cột treo công tơ
                float radius = (7.2f * (getHeight() / 3.5f) / 12 - (getHeight() / 3.5f) / 2) / 2;
                canvas.drawCircle(6.5f * getWidth() / 10 + radius, 7.1f * (getHeight() / 3.5f) / 12, radius, paint);

                Path pathFill = new Path();
                pathFill.moveTo(6.5f * getWidth() / 10 + radius / 5, 7.4f * (getHeight() / 3.5f) / 12);
                pathFill.lineTo(6.5f * getWidth() / 10 + 9 * radius / 5, 7.4f * (getHeight() / 3.5f) / 12);
                pathFill.lineTo(6.5f * getWidth() / 10 + 9 * radius / 5, 7.8f * (getHeight() / 3.5f) / 12);
                pathFill.lineTo(6.5f * getWidth() / 10 + radius / 5, 7.8f * (getHeight() / 3.5f) / 12);
                pathFill.lineTo(6.5f * getWidth() / 10 + radius / 5, 7.4f * (getHeight() / 3.5f) / 12);
                //Ghi chú vị trí treo công tơ
                path.moveTo(6.5f * getWidth() / 10 + 9 * radius / 5, 7.4f * (getHeight() / 3.5f) / 12);
                path.lineTo(3.5f * getWidth() / 5, 8 * (getHeight() / 3.5f) / 10);
                path.lineTo(4.5f * getWidth() / 5, 8 * (getHeight() / 3.5f) / 10);
                //Cột 1
                path.moveTo(2.3f * getWidth() / 5, 7.1f * (getHeight() / 3.5f) / 12 - radius);
                path.lineTo(2.3f * getWidth() / 5 + 2 * radius, 7.1f * (getHeight() / 3.5f) / 12 - radius);
                path.lineTo(2.3f * getWidth() / 5 + 2 * radius, 7.1f * (getHeight() / 3.5f) / 12 + radius);
                path.lineTo(2.3f * getWidth() / 5, 7.1f * (getHeight() / 3.5f) / 12 + radius);
                path.lineTo(2.3f * getWidth() / 5, 7.1f * (getHeight() / 3.5f) / 12 - radius);
                canvas.drawCircle(2.3f * getWidth() / 5 + radius, 7.1f * (getHeight() / 3.5f) / 12, radius / 2, paint);
                //Cột 2
                float xCot2 = (6.5f * getWidth() / 10 + 2.3f * getWidth() / 5 + 2 * radius) / 2;

                path.moveTo(xCot2, 7.1f * (getHeight() / 3.5f) / 12 - radius);
                path.lineTo(xCot2 + 2 * radius, 7.1f * (getHeight() / 3.5f) / 12 - radius);
                path.lineTo(xCot2 + 2 * radius, 7.1f * (getHeight() / 3.5f) / 12 + radius);
                path.lineTo(xCot2, 7.1f * (getHeight() / 3.5f) / 12 + radius);
                path.lineTo(xCot2, 7.1f * (getHeight() / 3.5f) / 12 - radius);
                canvas.drawCircle(xCot2 + radius, 7.1f * (getHeight() / 3.5f) / 12, radius / 2, paint);
                //Ghi chú cáp sau công tơ
                path.moveTo(xCot2 + 2 * radius, 7.0f * (getHeight() / 3.5f) / 12);
                path.lineTo(3 * getWidth() / 5 + 2 * radius, 5 * (getHeight() / 3.5f) / 12);
                path.lineTo(4 * getWidth() / 5, 5 * (getHeight() / 3.5f) / 12);
                //Đường dây ra trạm
                path.moveTo(6.5f * getWidth() / 10 + 2 * radius, 7.1f * (getHeight() / 3.5f) / 12);
                path.lineTo(3.5f * getWidth() / 5, 7.1f * (getHeight() / 3.5f) / 12);
                //Trạm
                path.moveTo(3.5f * getWidth() / 5, 7.1f * (getHeight() / 3.5f) / 12 - 2 * radius);
                path.lineTo(3.5f * getWidth() / 5 + 6 * radius, 7.1f * (getHeight() / 3.5f) / 12 - 2 * radius);
                path.lineTo(3.5f * getWidth() / 5 + 6 * radius, 7.1f * (getHeight() / 3.5f) / 12 + 2 * radius);
                path.lineTo(3.5f * getWidth() / 5, 7.1f * (getHeight() / 3.5f) / 12 + 2 * radius);
                path.lineTo(3.5f * getWidth() / 5, 7.1f * (getHeight() / 3.5f) / 12 - 2 * radius);

                pathFill.moveTo(3.5f * getWidth() / 5, 7.1f * (getHeight() / 3.5f) / 12 + 2 * radius);
                pathFill.lineTo(3.5f * getWidth() / 5 + 3 * radius, 7.1f * (getHeight() / 3.5f) / 12 - 2 * radius);
                pathFill.lineTo(3.5f * getWidth() / 5 + 6 * radius, 7.1f * (getHeight() / 3.5f) / 12 + 2 * radius);
                //Điền chữ lên bản vẽ
                if (!EsspSoDoCDActivity.SO_COT.isEmpty()) {
                    Rect boundSoCot = new Rect();
                    paintText.getTextBounds(EsspSoDoCDActivity.SO_COT, 0, EsspSoDoCDActivity.SO_COT.length(), boundSoCot);
                    canvas.drawText(EsspSoDoCDActivity.SO_COT, xCot2 + 2 * radius - boundSoCot.width() + 20, 7.2f * (getHeight() / 3.5f) / 10, paintText);
                }
                if (!EsspSoDoCDActivity.TEN_KHANG.isEmpty()) {
                    Rect boundTenKH = new Rect();
                    paintText.getTextBounds(EsspSoDoCDActivity.TEN_KHANG, 0, EsspSoDoCDActivity.TEN_KHANG.length(), boundTenKH);
                    StringBuilder[] stringBuilder = Common.splitMultiText(EsspSoDoCDActivity.TEN_KHANG, 1.7f * getWidth() / 5, boundTenKH.width());
                    for (int i = 0; i < stringBuilder.length; i++) {
                        canvas.drawText(stringBuilder[i].toString(), getWidth() / 5 + 10,
                                (getHeight() / 3.5f) / 12 + (i + 1) * boundTenKH.height() + 10, paintText);
                    }
                }
                if (!EsspSoDoCDActivity.DIA_CHI.isEmpty()) {
                    Rect boundDiaChi = new Rect();
                    paintText.getTextBounds(EsspSoDoCDActivity.DIA_CHI, 0, EsspSoDoCDActivity.DIA_CHI.length(), boundDiaChi);
                    StringBuilder[] stringBuilder = Common.splitMultiText(EsspSoDoCDActivity.DIA_CHI, 2f * getWidth() / 5, boundDiaChi.width());
                    for (int i = 0; i < stringBuilder.length; i++) {
                        canvas.drawText(stringBuilder[i].toString(), getWidth() / 5, 8.8f * (getHeight() / 3.5f) / 10 + i * boundDiaChi.height(), paintText);
                    }
                }
                if (!EsspSoDoCDActivity.TEN_TRAM.isEmpty()) {
                    Rect boundTram = new Rect();
                    paintText.getTextBounds(EsspSoDoCDActivity.TEN_TRAM, 0, EsspSoDoCDActivity.TEN_TRAM.length(), boundTram);
                    StringBuilder[] stringBuilder = Common.splitMultiText(EsspSoDoCDActivity.TEN_TRAM, getWidth() / 5, boundTram.width());
                    for (int i = 0; i < stringBuilder.length; i++) {
                        canvas.drawText(stringBuilder[i].toString(), 3.5f * getWidth() / 5 + 6 * radius + 20,
                                7.1f * (getHeight() / 3.5f) / 12 - 2 * radius + (i + 1) * boundTram.height(), paintText);
                    }
                }

                canvas.drawText("Vị trí treo công tơ", 3.5f * getWidth() / 5, 8.8f * (getHeight() / 3.5f) / 10, paintText);

                canvas.drawPath(path, paint);
                canvas.drawPath(pathFill, paintFill);
//                initShape();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void drawRectBound(Canvas canvas) {
        if (EsspSoDoCDActivity.POS_SELECTED_LAYER != -1) {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(EsspCommon.width);
            paint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));

            Paint paintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintFill.setStrokeCap(Paint.Cap.ROUND);
            paintFill.setColor(Color.RED);
            paintFill.setStyle(Paint.Style.FILL);
            paintFill.setStrokeWidth(EsspCommon.width);

            Path path = new Path();
            Path path0 = new Path();
            RectF rectBounds = new RectF();
            RectF rectBounds0 = new RectF();
            PointF startPoint = new PointF();
            PointF endPoint = new PointF();
            float radius = 0f;
            canvas.save();
            canvas.translate(pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getDx(), pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getDy());
            switch (pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getEsspShapeType()) {
                case BRUSH:
                    path = pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getPath()[0];
                    path.computeBounds(rectBounds, true);
                    canvas.drawRect(rectBounds, paint);
                    canvas.drawRect(rectBounds.left - getHeight() / 160, rectBounds.top - getHeight() / 160, rectBounds.left + getHeight() / 160, rectBounds.top + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.left - getHeight() / 160, rectBounds.bottom - getHeight() / 160, rectBounds.left + getHeight() / 160, rectBounds.bottom + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.right - getHeight() / 160, rectBounds.top - getHeight() / 160, rectBounds.right + getHeight() / 160, rectBounds.top + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.right - getHeight() / 160, rectBounds.bottom - getHeight() / 160, rectBounds.right + getHeight() / 160, rectBounds.bottom + getHeight() / 160, paintFill);
                    break;
                case LINE:
                    startPoint = pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getStartPoint()[0];
                    endPoint = pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getEndPoint()[0];
                    canvas.drawRect(startPoint.x, startPoint.y, endPoint.x, endPoint.y, paint);
                    canvas.drawRect(startPoint.x - getHeight() / 160, startPoint.y - getHeight() / 160, startPoint.x + getHeight() / 160, startPoint.y + getHeight() / 160, paintFill);
                    canvas.drawRect(startPoint.x - getHeight() / 160, endPoint.y - getHeight() / 160, startPoint.x + getHeight() / 160, endPoint.y + getHeight() / 160, paintFill);
                    canvas.drawRect(endPoint.x - getHeight() / 160, startPoint.y - getHeight() / 160, endPoint.x + getHeight() / 160, startPoint.y + getHeight() / 160, paintFill);
                    canvas.drawRect(endPoint.x - getHeight() / 160, endPoint.y - getHeight() / 160, endPoint.x + getHeight() / 160, endPoint.y + getHeight() / 160, paintFill);
                    break;
                case SMOOTH_LINE:
                    path = pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getPath()[0];
                    rectBounds = new RectF();
                    path.computeBounds(rectBounds, true);
                    canvas.drawRect(rectBounds, paint);
                    canvas.drawRect(rectBounds.left - getHeight() / 160, rectBounds.top - getHeight() / 160, rectBounds.left + getHeight() / 160, rectBounds.top + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.left - getHeight() / 160, rectBounds.bottom - getHeight() / 160, rectBounds.left + getHeight() / 160, rectBounds.bottom + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.right - getHeight() / 160, rectBounds.top - getHeight() / 160, rectBounds.right + getHeight() / 160, rectBounds.top + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.right - getHeight() / 160, rectBounds.bottom - getHeight() / 160, rectBounds.right + getHeight() / 160, rectBounds.bottom + getHeight() / 160, paintFill);
                    break;
                case ARROW1:
                    startPoint = pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getStartPoint()[0];
                    endPoint = pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getEndPoint()[0];
                    float xArrow1Start = startPoint.x;
                    float yArrow1Start = startPoint.y;
                    float xArrow1End = endPoint.x;
                    float yArrow1End = endPoint.y;
                    if (xArrow1Start == xArrow1End) {
                        xArrow1Start -= 10;
                        xArrow1End += 10;
                    } else {
                        yArrow1Start -= 10;
                        yArrow1End += 10;
                    }
                    canvas.drawRect(xArrow1Start, yArrow1Start, xArrow1End, yArrow1End, paint);
                    canvas.drawRect(xArrow1Start - getHeight() / 160, yArrow1Start - getHeight() / 160, xArrow1Start + getHeight() / 160, yArrow1Start + getHeight() / 160, paintFill);
                    canvas.drawRect(xArrow1Start - getHeight() / 160, yArrow1End - getHeight() / 160, xArrow1Start + getHeight() / 160, yArrow1End + getHeight() / 160, paintFill);
                    canvas.drawRect(xArrow1End - getHeight() / 160, yArrow1Start - getHeight() / 160, xArrow1End + getHeight() / 160, yArrow1Start + getHeight() / 160, paintFill);
                    canvas.drawRect(xArrow1End - getHeight() / 160, yArrow1End - getHeight() / 160, xArrow1End + getHeight() / 160, yArrow1End + getHeight() / 160, paintFill);
                    break;
                case ARROW2:
                    startPoint = pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getStartPoint()[0];
                    endPoint = pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getEndPoint()[0];
                    float xArrow2Start = startPoint.x;
                    float yArrow2Start = startPoint.y;
                    float xArrow2End = endPoint.x;
                    float yArrow2End = endPoint.y;
                    if (xArrow2Start == xArrow2End) {
                        xArrow2Start -= 10;
                        xArrow2End += 10;
                    } else {
                        yArrow2Start -= 10;
                        yArrow2End += 10;
                    }
                    canvas.drawRect(xArrow2Start, yArrow2Start, xArrow2End, yArrow2End, paint);
                    canvas.drawRect(xArrow2Start - getHeight() / 160, yArrow2Start - getHeight() / 160, xArrow2Start + getHeight() / 160, yArrow2Start + getHeight() / 160, paintFill);
                    canvas.drawRect(xArrow2Start - getHeight() / 160, yArrow2End - getHeight() / 160, xArrow2Start + getHeight() / 160, yArrow2End + getHeight() / 160, paintFill);
                    canvas.drawRect(xArrow2End - getHeight() / 160, yArrow2Start - getHeight() / 160, xArrow2End + getHeight() / 160, yArrow2Start + getHeight() / 160, paintFill);
                    canvas.drawRect(xArrow2End - getHeight() / 160, yArrow2End - getHeight() / 160, xArrow2End + getHeight() / 160, yArrow2End + getHeight() / 160, paintFill);
                    break;
                case NOTE_LINE:
                    path = pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getPath()[0];
                    path.computeBounds(rectBounds, true);
                    canvas.drawRect(rectBounds, paint);
                    canvas.drawRect(rectBounds.left - getHeight() / 160, rectBounds.top - getHeight() / 160, rectBounds.left + getHeight() / 160, rectBounds.top + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.left - getHeight() / 160, rectBounds.bottom - getHeight() / 160, rectBounds.left + getHeight() / 160, rectBounds.bottom + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.right - getHeight() / 160, rectBounds.top - getHeight() / 160, rectBounds.right + getHeight() / 160, rectBounds.top + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.right - getHeight() / 160, rectBounds.bottom - getHeight() / 160, rectBounds.right + getHeight() / 160, rectBounds.bottom + getHeight() / 160, paintFill);
                    break;
                case ELECTRICITY_LINE:
                    path = pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getPath()[0];
                    path.computeBounds(rectBounds, true);
                    canvas.drawRect(rectBounds, paint);
                    canvas.drawRect(rectBounds.left - getHeight() / 160, rectBounds.top - getHeight() / 160, rectBounds.left + getHeight() / 160, rectBounds.top + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.left - getHeight() / 160, rectBounds.bottom - getHeight() / 160, rectBounds.left + getHeight() / 160, rectBounds.bottom + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.right - getHeight() / 160, rectBounds.top - getHeight() / 160, rectBounds.right + getHeight() / 160, rectBounds.top + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.right - getHeight() / 160, rectBounds.bottom - getHeight() / 160, rectBounds.right + getHeight() / 160, rectBounds.bottom + getHeight() / 160, paintFill);
                    break;
                case TEXT:
                    Paint pText = pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getPaint()[0];
                    String text = pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getText()[0];
                    PointF pointText = pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getStartPoint()[0];
                    Rect rectTextBound = new Rect();
                    pText.getTextBounds(text, 0, text.length(), rectTextBound);
                    float left = rectTextBound.left + pointText.x;
                    float right = rectTextBound.right + pointText.x;
                    float top = rectTextBound.top + pointText.y;
                    float bottom = rectTextBound.bottom + pointText.y;
                    canvas.drawRect(left, top, right, bottom, paint);
                    canvas.drawRect(left - getHeight() / 160, top - getHeight() / 160, left + getHeight() / 160, top + getHeight() / 160, paintFill);
                    canvas.drawRect(left - getHeight() / 160, bottom - getHeight() / 160, left + getHeight() / 160, bottom + getHeight() / 160, paintFill);
                    canvas.drawRect(right - getHeight() / 160, top - getHeight() / 160, right + getHeight() / 160, top + getHeight() / 160, paintFill);
                    canvas.drawRect(right - getHeight() / 160, bottom - getHeight() / 160, right + getHeight() / 160, bottom + getHeight() / 160, paintFill);
                    break;
                case POWER_POLES_SQUARE:
                    path = pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getPath()[0];
                    path.computeBounds(rectBounds, true);
                    canvas.drawRect(rectBounds, paint);
                    canvas.drawRect(rectBounds.left - getHeight() / 160, rectBounds.top - getHeight() / 160, rectBounds.left + getHeight() / 160, rectBounds.top + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.left - getHeight() / 160, rectBounds.bottom - getHeight() / 160, rectBounds.left + getHeight() / 160, rectBounds.bottom + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.right - getHeight() / 160, rectBounds.top - getHeight() / 160, rectBounds.right + getHeight() / 160, rectBounds.top + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.right - getHeight() / 160, rectBounds.bottom - getHeight() / 160, rectBounds.right + getHeight() / 160, rectBounds.bottom + getHeight() / 160, paintFill);
                    break;
                case POWER_POLES_CIRCLE:
                    path = pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getPath()[0];
                    path.computeBounds(rectBounds, true);
                    canvas.drawRect(rectBounds, paint);
                    canvas.drawRect(rectBounds.left - getHeight() / 160, rectBounds.top - getHeight() / 160, rectBounds.left + getHeight() / 160, rectBounds.top + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.left - getHeight() / 160, rectBounds.bottom - getHeight() / 160, rectBounds.left + getHeight() / 160, rectBounds.bottom + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.right - getHeight() / 160, rectBounds.top - getHeight() / 160, rectBounds.right + getHeight() / 160, rectBounds.top + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.right - getHeight() / 160, rectBounds.bottom - getHeight() / 160, rectBounds.right + getHeight() / 160, rectBounds.bottom + getHeight() / 160, paintFill);
                    break;
                case POWER_POLES_EXTRA:
                    path = pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getPath()[0];
                    path.computeBounds(rectBounds, true);
                    canvas.drawRect(rectBounds, paint);
                    canvas.drawRect(rectBounds.left - getHeight() / 160, rectBounds.top - getHeight() / 160, rectBounds.left + getHeight() / 160, rectBounds.top + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.left - getHeight() / 160, rectBounds.bottom - getHeight() / 160, rectBounds.left + getHeight() / 160, rectBounds.bottom + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.right - getHeight() / 160, rectBounds.top - getHeight() / 160, rectBounds.right + getHeight() / 160, rectBounds.top + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.right - getHeight() / 160, rectBounds.bottom - getHeight() / 160, rectBounds.right + getHeight() / 160, rectBounds.bottom + getHeight() / 160, paintFill);
                    break;
                case BOX6:
                    path = pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getPath()[1];
                    path.computeBounds(rectBounds, true);
                    canvas.drawRect(rectBounds, paint);
                    canvas.drawRect(rectBounds.left - getHeight() / 160, rectBounds.top - getHeight() / 160, rectBounds.left + getHeight() / 160, rectBounds.top + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.left - getHeight() / 160, rectBounds.bottom - getHeight() / 160, rectBounds.left + getHeight() / 160, rectBounds.bottom + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.right - getHeight() / 160, rectBounds.top - getHeight() / 160, rectBounds.right + getHeight() / 160, rectBounds.top + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.right - getHeight() / 160, rectBounds.bottom - getHeight() / 160, rectBounds.right + getHeight() / 160, rectBounds.bottom + getHeight() / 160, paintFill);
                    break;
                case BOX4:
                    path = pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getPath()[1];
                    path.computeBounds(rectBounds, true);
                    canvas.drawRect(rectBounds, paint);
                    canvas.drawRect(rectBounds.left - getHeight() / 160, rectBounds.top - getHeight() / 160, rectBounds.left + getHeight() / 160, rectBounds.top + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.left - getHeight() / 160, rectBounds.bottom - getHeight() / 160, rectBounds.left + getHeight() / 160, rectBounds.bottom + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.right - getHeight() / 160, rectBounds.top - getHeight() / 160, rectBounds.right + getHeight() / 160, rectBounds.top + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.right - getHeight() / 160, rectBounds.bottom - getHeight() / 160, rectBounds.right + getHeight() / 160, rectBounds.bottom + getHeight() / 160, paintFill);
                    break;
                case BOX2:
                    path = pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getPath()[1];
                    path.computeBounds(rectBounds, true);
                    canvas.drawRect(rectBounds, paint);
                    canvas.drawRect(rectBounds.left - getHeight() / 160, rectBounds.top - getHeight() / 160, rectBounds.left + getHeight() / 160, rectBounds.top + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.left - getHeight() / 160, rectBounds.bottom - getHeight() / 160, rectBounds.left + getHeight() / 160, rectBounds.bottom + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.right - getHeight() / 160, rectBounds.top - getHeight() / 160, rectBounds.right + getHeight() / 160, rectBounds.top + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.right - getHeight() / 160, rectBounds.bottom - getHeight() / 160, rectBounds.right + getHeight() / 160, rectBounds.bottom + getHeight() / 160, paintFill);
                    break;
                case BOX1:
                    path = pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getPath()[1];
                    path.computeBounds(rectBounds, true);
                    canvas.drawRect(rectBounds, paint);
                    canvas.drawRect(rectBounds.left - getHeight() / 160, rectBounds.top - getHeight() / 160, rectBounds.left + getHeight() / 160, rectBounds.top + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.left - getHeight() / 160, rectBounds.bottom - getHeight() / 160, rectBounds.left + getHeight() / 160, rectBounds.bottom + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.right - getHeight() / 160, rectBounds.top - getHeight() / 160, rectBounds.right + getHeight() / 160, rectBounds.top + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.right - getHeight() / 160, rectBounds.bottom - getHeight() / 160, rectBounds.right + getHeight() / 160, rectBounds.bottom + getHeight() / 160, paintFill);
                    break;
                case BOX_BEHIND:
                    path = pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getPath()[0];
                    path.computeBounds(rectBounds, true);
                    canvas.drawRect(rectBounds, paint);
                    canvas.drawRect(rectBounds.left - getHeight() / 160, rectBounds.top - getHeight() / 160, rectBounds.left + getHeight() / 160, rectBounds.top + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.left - getHeight() / 160, rectBounds.bottom - getHeight() / 160, rectBounds.left + getHeight() / 160, rectBounds.bottom + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.right - getHeight() / 160, rectBounds.top - getHeight() / 160, rectBounds.right + getHeight() / 160, rectBounds.top + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.right - getHeight() / 160, rectBounds.bottom - getHeight() / 160, rectBounds.right + getHeight() / 160, rectBounds.bottom + getHeight() / 160, paintFill);
                    break;
                case ARROW_NOTE:
                    path = pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getPath()[0];
                    path.computeBounds(rectBounds, true);
                    canvas.drawRect(rectBounds, paint);
                    canvas.drawRect(rectBounds.left - getHeight() / 160, rectBounds.top - getHeight() / 160, rectBounds.left + getHeight() / 160, rectBounds.top + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.left - getHeight() / 160, rectBounds.bottom - getHeight() / 160, rectBounds.left + getHeight() / 160, rectBounds.bottom + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.right - getHeight() / 160, rectBounds.top - getHeight() / 160, rectBounds.right + getHeight() / 160, rectBounds.top + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.right - getHeight() / 160, rectBounds.bottom - getHeight() / 160, rectBounds.right + getHeight() / 160, rectBounds.bottom + getHeight() / 160, paintFill);
                    break;
                case RECTANGLE:
                    path = pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getPath()[0];
                    path.computeBounds(rectBounds, true);
                    canvas.drawRect(rectBounds, paint);
                    canvas.drawRect(rectBounds.left - getHeight() / 160, rectBounds.top - getHeight() / 160, rectBounds.left + getHeight() / 160, rectBounds.top + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.left - getHeight() / 160, rectBounds.bottom - getHeight() / 160, rectBounds.left + getHeight() / 160, rectBounds.bottom + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.right - getHeight() / 160, rectBounds.top - getHeight() / 160, rectBounds.right + getHeight() / 160, rectBounds.top + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.right - getHeight() / 160, rectBounds.bottom - getHeight() / 160, rectBounds.right + getHeight() / 160, rectBounds.bottom + getHeight() / 160, paintFill);
                    break;
                case RECTANGLE_NAME:
                    path = pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getPath()[0];
                    path.computeBounds(rectBounds, true);
                    canvas.drawRect(rectBounds, paint);
                    canvas.drawRect(rectBounds.left - getHeight() / 160, rectBounds.top - getHeight() / 160, rectBounds.left + getHeight() / 160, rectBounds.top + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.left - getHeight() / 160, rectBounds.bottom - getHeight() / 160, rectBounds.left + getHeight() / 160, rectBounds.bottom + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.right - getHeight() / 160, rectBounds.top - getHeight() / 160, rectBounds.right + getHeight() / 160, rectBounds.top + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.right - getHeight() / 160, rectBounds.bottom - getHeight() / 160, rectBounds.right + getHeight() / 160, rectBounds.bottom + getHeight() / 160, paintFill);
                    break;
                case CIRCLE:
                    startPoint = pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getStartPoint()[0];
                    endPoint = pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getEndPoint()[0];
                    radius = (float) (Math.sqrt(Math.pow(endPoint.x - startPoint.x, 2) + Math.pow(endPoint.y - startPoint.y, 2)));
                    canvas.drawRect(startPoint.x - radius, startPoint.y - radius, startPoint.x + radius, startPoint.y + radius, paint);
                    canvas.drawRect(startPoint.x - radius - getHeight() / 160, startPoint.y - radius - getHeight() / 160, startPoint.x - radius + getHeight() / 160, startPoint.y - radius + getHeight() / 160, paintFill);
                    canvas.drawRect(startPoint.x - radius - getHeight() / 160, startPoint.y + radius - getHeight() / 160, startPoint.x - radius + getHeight() / 160, startPoint.y + radius + getHeight() / 160, paintFill);
                    canvas.drawRect(startPoint.x + radius - getHeight() / 160, startPoint.y - radius - getHeight() / 160, startPoint.x + radius + getHeight() / 160, startPoint.y - radius + getHeight() / 160, paintFill);
                    canvas.drawRect(startPoint.x + radius - getHeight() / 160, startPoint.y + radius - getHeight() / 160, startPoint.x + radius + getHeight() / 160, startPoint.y + radius + getHeight() / 160, paintFill);
                    break;
                case SUBSTATION:
                    path = pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getPath()[0];
                    path.computeBounds(rectBounds, true);
                    canvas.drawRect(rectBounds, paint);
                    canvas.drawRect(rectBounds.left - getHeight() / 160, rectBounds.top - getHeight() / 160, rectBounds.left + getHeight() / 160, rectBounds.top + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.left - getHeight() / 160, rectBounds.bottom - getHeight() / 160, rectBounds.left + getHeight() / 160, rectBounds.bottom + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.right - getHeight() / 160, rectBounds.top - getHeight() / 160, rectBounds.right + getHeight() / 160, rectBounds.top + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.right - getHeight() / 160, rectBounds.bottom - getHeight() / 160, rectBounds.right + getHeight() / 160, rectBounds.bottom + getHeight() / 160, paintFill);
                    break;
                case HOUSE:
                    path = pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getPath()[1];
                    path0 = pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getPath()[0];
                    path.computeBounds(rectBounds, true);
                    path0.computeBounds(rectBounds0, true);
                    canvas.drawRect(rectBounds.left, rectBounds.top, rectBounds.right, rectBounds.bottom + rectBounds0.height(), paint);
                    canvas.drawRect(rectBounds.left - getHeight() / 160, rectBounds.top - getHeight() / 160, rectBounds.left + getHeight() / 160, rectBounds.top + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.left - getHeight() / 160, rectBounds.bottom + rectBounds0.height() - getHeight() / 160, rectBounds.left + getHeight() / 160, rectBounds.bottom + rectBounds0.height() + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.right - getHeight() / 160, rectBounds.top - getHeight() / 160, rectBounds.right + getHeight() / 160, rectBounds.top + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.right - getHeight() / 160, rectBounds.bottom + rectBounds0.height() - getHeight() / 160, rectBounds.right + getHeight() / 160, rectBounds.bottom + rectBounds0.height() + getHeight() / 160, paintFill);
                    break;
                case POWER_POLES_CIRCLE_TOP:
                    startPoint = pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getStartPoint()[0];
                    endPoint = pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getEndPoint()[0];
                    radius = (float) (Math.sqrt(Math.pow(endPoint.x - startPoint.x, 2) + Math.pow(endPoint.y - startPoint.y, 2)));
                    canvas.drawRect(startPoint.x - radius, startPoint.y - radius, startPoint.x + radius, startPoint.y + radius, paint);
                    canvas.drawRect(startPoint.x - radius - getHeight() / 160, startPoint.y - radius - getHeight() / 160, startPoint.x - radius + getHeight() / 160, startPoint.y - radius + getHeight() / 160, paintFill);
                    canvas.drawRect(startPoint.x - radius - getHeight() / 160, startPoint.y + radius - getHeight() / 160, startPoint.x - radius + getHeight() / 160, startPoint.y + radius + getHeight() / 160, paintFill);
                    canvas.drawRect(startPoint.x + radius - getHeight() / 160, startPoint.y - radius - getHeight() / 160, startPoint.x + radius + getHeight() / 160, startPoint.y - radius + getHeight() / 160, paintFill);
                    canvas.drawRect(startPoint.x + radius - getHeight() / 160, startPoint.y + radius - getHeight() / 160, startPoint.x + radius + getHeight() / 160, startPoint.y + radius + getHeight() / 160, paintFill);
                    break;
                case POWER_POLES_EXTRA_TOP:
                    path = pLineEsspShapes.get(EsspSoDoCDActivity.POS_SELECTED_LAYER).getPath()[0];
                    path.computeBounds(rectBounds, true);
                    canvas.drawRect(rectBounds, paint);
                    canvas.drawRect(rectBounds.left - getHeight() / 160, rectBounds.top - getHeight() / 160, rectBounds.left + getHeight() / 160, rectBounds.top + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.left - getHeight() / 160, rectBounds.bottom - getHeight() / 160, rectBounds.left + getHeight() / 160, rectBounds.bottom + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.right - getHeight() / 160, rectBounds.top - getHeight() / 160, rectBounds.right + getHeight() / 160, rectBounds.top + getHeight() / 160, paintFill);
                    canvas.drawRect(rectBounds.right - getHeight() / 160, rectBounds.bottom - getHeight() / 160, rectBounds.right + getHeight() / 160, rectBounds.bottom + getHeight() / 160, paintFill);
                    break;
            }
            canvas.restore();
        }
    }

    public void viewDraw(Canvas canvas) {
        try {
            if (isViewDraw && isAllowViewDraw) {
                canvas.save();
                canvas.scale(2, 2);
                Paint paintRed = new Paint(Paint.ANTI_ALIAS_FLAG);
                paintRed.setStrokeCap(Paint.Cap.ROUND);
                paintRed.setColor(Color.RED);
                paintRed.setStyle(Paint.Style.STROKE);
                paintRed.setStrokeWidth(EsspCommon.width);

                Rect rSrc = new Rect((int) (xMove / scaleBmp - EsspSoDoCDActivity.SIZE_CROP_VIEW / 4), (int) (yMove / scaleBmp - EsspSoDoCDActivity.SIZE_CROP_VIEW / 4),
                        (int) (xMove / scaleBmp + EsspSoDoCDActivity.SIZE_CROP_VIEW / 4), (int) (yMove / scaleBmp + EsspSoDoCDActivity.SIZE_CROP_VIEW / 4));
                int left = 5;
                int top = 5;
                int right = (int) (EsspSoDoCDActivity.SIZE_CROP_VIEW) + 5;
                int bottom = (int) (EsspSoDoCDActivity.SIZE_CROP_VIEW) + 5;
                if (xMove >= 2 * EsspSoDoCDActivity.SIZE_CROP_VIEW + 20 || yMove >= 2 * EsspSoDoCDActivity.SIZE_CROP_VIEW + 20) {
                    left = 5;
                    right = (int) (EsspSoDoCDActivity.SIZE_CROP_VIEW) + 5;
                } else {
                    left = (int) (getWidth() - 10 - 2 * EsspSoDoCDActivity.SIZE_CROP_VIEW) / 2;
                    right = (getWidth() - 10) / 2;
                }
                Rect rDst = new Rect(left, top, right, bottom);
                canvas.drawBitmap(bmView, rSrc, rDst, paintRed);
                canvas.restore();
                canvas.drawRect(2 * left, 2 * top, 2 * right, 2 * bottom, paintRed);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean checkSave() {
        if (pLineEsspShapes.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    private void moveSmoothLine(PointF p, int i) {
        for (int j = 0; j < pLineEsspShapes.size(); j++) {
            if (pLineEsspShapes.get(j).getEsspShapeType() == EsspShapeType.SMOOTH_LINE) {
                if ((int) pLineEsspShapes.get(j).getStartPoint()[0].x == (int) pSnapPoint.get(i).getPoint().x
                        && (int) pLineEsspShapes.get(j).getStartPoint()[0].y == (int) pSnapPoint.get(i).getPoint().y) {
                    pLineEsspShapes.get(j).setStartPoint(new PointF[]{p});
                    pLineEsspShapes.get(j).getPath()[0] = new Path();
                    pLineEsspShapes.get(j).getPath()[0].moveTo(pLineEsspShapes.get(j).getStartPoint()[0].x,
                            pLineEsspShapes.get(j).getStartPoint()[0].y);
                    float x1 = (pLineEsspShapes.get(j).getEndPoint()[0].x
                            + pLineEsspShapes.get(j).getStartPoint()[0].x) / 2;
                    float y1 = Common.getMin(pLineEsspShapes.get(j).getStartPoint()[0].y,
                            pLineEsspShapes.get(j).getEndPoint()[0].y) + 80;
                    pLineEsspShapes.get(j).getPath()[0].quadTo(
                            x1, y1, pLineEsspShapes.get(j).getEndPoint()[0].x,
                            pLineEsspShapes.get(j).getEndPoint()[0].y);
                }
                if ((int) pLineEsspShapes.get(j).getEndPoint()[0].x == (int) pSnapPoint.get(i).getPoint().x
                        && (int) pLineEsspShapes.get(j).getEndPoint()[0].y == (int) pSnapPoint.get(i).getPoint().y) {
                    pLineEsspShapes.get(j).setEndPoint(new PointF[]{p});
                    pLineEsspShapes.get(j).getPath()[0] = new Path();
                    pLineEsspShapes.get(j).getPath()[0].moveTo(pLineEsspShapes.get(j).getEndPoint()[0].x,
                            pLineEsspShapes.get(j).getEndPoint()[0].y);
                    float x1 = (pLineEsspShapes.get(j).getStartPoint()[0].x
                            + pLineEsspShapes.get(j).getEndPoint()[0].x) / 2;
                    float y1 = Common.getMin(pLineEsspShapes.get(j).getEndPoint()[0].y,
                            pLineEsspShapes.get(j).getStartPoint()[0].y) + 80;
                    pLineEsspShapes.get(j).getPath()[0].quadTo(
                            x1, y1, pLineEsspShapes.get(j).getStartPoint()[0].x,
                            pLineEsspShapes.get(j).getStartPoint()[0].y);
                }
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

    }
    //endregion

}
