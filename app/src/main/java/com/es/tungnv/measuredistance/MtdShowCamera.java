package com.es.tungnv.measuredistance;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Camera;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

@SuppressLint("ViewConstructor")
public class MtdShowCamera extends SurfaceView implements SurfaceHolder.Callback {

	private SurfaceHolder holdMe;
	private Camera theCamera;

	public MtdShowCamera(Context context, Camera camera) {
		super(context);
		theCamera = camera;
		holdMe = getHolder();
		holdMe.addCallback(this);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int arg1, int arg2, int arg3) {
		theCamera.startPreview();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			theCamera.setPreviewDisplay(holder);
			theCamera.startPreview();
		} catch (IOException e) {
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		theCamera.stopPreview(); 
		theCamera.release();
	}

	@SuppressLint("NewApi")
	@Override
	 public boolean onTouchEvent(MotionEvent event) {
	  
	  if(event.getAction() == MotionEvent.ACTION_DOWN){
		  float x = event.getX();
	      float y = event.getY();
	      
//	      Rect touchRect = new Rect(
//	  	        (int)(x - 50), 
//	  	        (int)(y - 50), 
//	  	        (int)(x + 50), 
//	  	        (int)(y + 50));
//	      
//	      ((MainActivity)getContext()).touchFocus(touchRect);
	  }
	  
	  
	  return true;
	 }
}