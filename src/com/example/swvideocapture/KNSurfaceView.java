package com.example.swvideocapture;

import java.io.*;
import java.nio.*;
import java.util.*;

import android.content.*;
import android.graphics.*;
import android.hardware.*;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.hardware.Camera;
import android.os.*;
import android.util.*;
import android.view.*;

public class KNSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
	
	public KNGLRenderThread mCaptureThread;
	private SurfaceHolder mHolder;
	private Context mContext;
	private Camera mCamera;
	
	private int mWidth;
	private int mHeight;
		
	public KNSurfaceView(Context context, AttributeSet attr) {
		super(context, attr);
		init(context);
	}

	public KNSurfaceView(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context) {
		this.mContext = context;
		this.mHolder = getHolder();
		this.mHolder.addCallback(this);
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
		
		mWidth = width;
		mHeight = height;
		
		try {
			mCamera.setPreviewDisplay(mHolder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mCamera.setDisplayOrientation(90);
		mCamera.startPreview();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d("KEN", "surfaceCreated");
		
		mCamera = openFrontCamera();
		
		mCamera.setPreviewCallback(new PreviewCallback() {
			
			@Override
			public void onPreviewFrame(byte[] data, Camera camera) {
				
				Camera.Parameters param = camera.getParameters();
				
				int w = param.getPictureSize().width;
				int h = param.getPictureSize().height;
				
				Message msg = new Message();
				msg.obj = data;
				msg.arg1 = w;
				msg.arg2 = h;
				mCaptureThread.mBackHandler.sendMessage(msg);
			}
		});
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d("KEN", "surfaceDestroyed");
		mCamera.stopPreview();
	}

	
	
	private Camera openFrontCamera() {

		Camera camera = null;
		int cameraCount = 0;
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		cameraCount = Camera.getNumberOfCameras();

		for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
			Camera.getCameraInfo(camIdx, cameraInfo);
			if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
				try {
					camera = Camera.open(camIdx);
					Log.d("KEN", "Cameara Open");
				} catch (RuntimeException e) {
					Log.d("KEN", e.getLocalizedMessage());
				}
			}
		}


		Camera.Parameters param = camera.getParameters();
		List<Size> resolution = param.getSupportedPictureSizes();

		for (Size s : resolution) {
			Log.d("KEN", "SIZE : " + s.width + " " + s.height);
		}

		List<int[]> fps = param.getSupportedPreviewFpsRange();
		int[] fpsRange = fps.get(0);
		int minFps = fpsRange[0]; 
		int maxFps = fpsRange[1];
		Log.d("KEN", "MIN FPS : " + maxFps + ", MAX FPS : " + maxFps);
		param.setPreviewFpsRange(30000, 30000);
		camera.setParameters(param);
		 
		return camera;
	}	
}
