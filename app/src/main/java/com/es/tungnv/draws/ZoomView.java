package com.es.tungnv.draws;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceView;
import android.view.View;

import com.es.tungnv.utils.EsspCommon;
import com.es.tungnv.views.R;

/**
 * Created by TUNGNV on 8/25/2016.
 */
public class ZoomView extends SurfaceView {

    private static final int INVALID_POINTER_ID = -1;
    public Bitmap mMyChracter;
    protected float mPosX;
    protected float mPosY;

    protected float mLastTouchX;
    protected float mLastTouchY;
    private int mActivePointerId = INVALID_POINTER_ID;

    protected float mDisX;
    protected float mDisY;
    protected float mStartX;
    protected float mStartY;

    View currentView;

    private ScaleGestureDetector mScaleDetector;
    protected float mScaleFactor = 1.f;

    protected float focusX;
    protected float focusY;

    private float lastFocusX = -1;
    private float lastFocusY = -1;

    static final int IMG_WIDTH = 640;
    static final int IMG_HEIGHT = 480;

    static final int IMAGE_X_POS = 560;
    static final int IMAGE_Y_POS = 20;

    Matrix matrix;
    float sy;
    float sx;
    public static Context context;

//    protected boolean isPanAndZoom = false;

    public ZoomView(Context context) {
        this(context, null, 0);
        mMyChracter = BitmapFactory.decodeResource(context.getResources(), R.mipmap.gcs_ic_book);

        setWillNotDraw(false);
        matrix = new Matrix();
        final int width = mMyChracter.getWidth();
        final int height = mMyChracter.getHeight();
        sx = 550 / (float) width;
        sy = 700 / (float) height;
        matrix.setScale(sx, sy);
        matrix.postTranslate(sx + IMAGE_X_POS, sy + IMAGE_Y_POS);
        mMyChracter = Bitmap.createScaledBitmap(mMyChracter, height, width, true);
        currentView = this;
        this.context = context;
    }

    public ZoomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(EsspCommon.isPanAndZoom) {
            mScaleDetector.onTouchEvent(ev);

            final int action = ev.getAction();
            switch (action & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN: {

                    mStartX = ev.getX();
                    mStartY = ev.getY();

                    final float x = ev.getX() / mScaleFactor;
                    final float y = ev.getY() / mScaleFactor;
                    mLastTouchX = x;
                    mLastTouchY = y;
                    mActivePointerId = ev.getPointerId(0);

                    break;
                }

                case MotionEvent.ACTION_MOVE: {
                    final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                    final float x = ev.getX(pointerIndex) / mScaleFactor;
                    final float y = ev.getY(pointerIndex) / mScaleFactor;

                    // Only move if the ScaleGestureDetector isn't processing a gesture.
                    if (!mScaleDetector.isInProgress()) {

                        final float dx = x - mLastTouchX;
                        final float dy = y - mLastTouchY;
                        mPosX += dx;
                        mPosY += dy;

                        invalidate();
                    }

                    mLastTouchX = x;
                    mLastTouchY = y;

                    break;
                }

                case MotionEvent.ACTION_UP: {
                    mActivePointerId = INVALID_POINTER_ID;
                    mDisX += ev.getX() - mStartX;
                    mDisY += ev.getY() - mStartY;
                    break;
                }

                case MotionEvent.ACTION_CANCEL: {
                    mActivePointerId = INVALID_POINTER_ID;
                    break;
                }

                case MotionEvent.ACTION_POINTER_UP: {

                    final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                    final int pointerId = ev.getPointerId(pointerIndex);
                    if (pointerId == mActivePointerId) {
                        // This was our active pointer going up. Choose a new
                        // active pointer and adjust accordingly.
                        final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                        mLastTouchX = ev.getX(newPointerIndex) / mScaleFactor;
                        mLastTouchY = ev.getY(newPointerIndex) / mScaleFactor;
                        mActivePointerId = ev.getPointerId(newPointerIndex);
                    }
                    break;
                }
            }
        }
        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setBackgroundColor(Color.WHITE);
        canvas.save();
        canvas.scale(mScaleFactor, mScaleFactor, focusX, focusY);
        canvas.translate(mPosX, mPosY);
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);
//        canvas.drawBitmap(mMyChracter, matrix, null);
        canvas.restore();
    }

    private class ScaleListener extends
            ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {

            // float x = detector.getFocusX();
            // float y = detector.getFocusY();

            lastFocusX = -1;
            lastFocusY = -1;

            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            focusX = detector.getFocusX();
            focusY = detector.getFocusY();

            if (lastFocusX == -1)
                lastFocusX = focusX;
            if (lastFocusY == -1)
                lastFocusY = focusY;

            mPosX += (focusX - lastFocusX);
            mPosY += (focusY - lastFocusY);
            mScaleFactor = Math.max(0.5f, Math.min(mScaleFactor, 2.0f));

            lastFocusX = focusX;
            lastFocusY = focusY;

            invalidate();
            return true;
        }

    }

}
