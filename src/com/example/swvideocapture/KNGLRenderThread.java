package com.example.swvideocapture;

import android.os.*;
import android.util.*;

public class KNGLRenderThread extends Thread{	
	
	private GL2JNIView mGLView;
	public Handler mBackHandler;
	

	public KNGLRenderThread(GL2JNIView glview) {
		this.mGLView = glview;
	}
	
	public void run() {
		
		boolean isMainThread = Looper.getMainLooper() == Looper.myLooper();
		
		Log.d("KEN", "KNCaptureThread Run, isMain" + isMainThread);
		
		Looper.prepare();
		
		mBackHandler = new Handler() {
			public void handleMessage(Message msg) {
				
				byte[] nv21 = (byte[])msg.obj;
				int w = msg.arg1;
				int h = msg.arg2;
				mGLView.setNV21Data(nv21, w, h);
			}
		};
		
		Looper.loop();
	}
}
