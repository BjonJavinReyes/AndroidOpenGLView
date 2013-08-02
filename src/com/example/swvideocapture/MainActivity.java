package com.example.swvideocapture;

import android.app.*;
import android.content.res.*;
import android.os.*;
import android.view.*;
import android.widget.*;
 
public class MainActivity extends Activity {

	private KNSurfaceView mSurfaceView;
	private LinearLayout mGlviewArea;
	private GL2JNIView mGlView;
	private KNGLRenderThread mCaptureThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mGlviewArea = (LinearLayout)findViewById(R.id.glview);
		mGlView = new GL2JNIView(getApplicationContext());
		mGlviewArea.addView(mGlView);
		
		mCaptureThread = new KNGLRenderThread(mGlView);
		mCaptureThread.setDaemon(true);
		mCaptureThread.start();
		
		mSurfaceView= (KNSurfaceView)findViewById(R.id.surface);
		mSurfaceView.mCaptureThread = mCaptureThread;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
}


