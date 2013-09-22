package com.complexityclass.imageprocessing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.complexityclass.imageprocessing.camera.CameraOptions;
import com.complexityclass.imageprocessing.camera.CameraPreview;

import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class MainActivity extends Activity {

	public static final String TAG = "MainActivity";
	private Camera mCamera;
	private CameraPreview mPreview;

	private PictureCallback mPicture = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {

			File pictureFile = CameraOptions
					.getOutputMediaFile(CameraOptions.MEDIA_TYPE_IMAGE);
			if (pictureFile == null) {
				Log.d(TAG,
						"Error creating media file, check storage permissions");
				return;
			}

			try {

				FileOutputStream fos = new FileOutputStream(pictureFile);
				System.out.println(fos.toString());
				fos.write(data);
				fos.close();

			} catch (FileNotFoundException e) {
				Log.d(TAG, "File not found : " + e.getMessage());
			} catch (IOException e) {
				Log.d(TAG, "Error accessing file : " + e.getMessage());
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mCamera = CameraOptions.getCameraInstance();
		mPreview = new CameraPreview(this, mCamera);
		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
		preview.addView(mPreview);

		Button captureButton = (Button) findViewById(R.id.button1);
		captureButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mCamera.takePicture(null, null, mPicture);
			}
		});

	}
	
	@Override
	protected void onPause(){
		super.onPause();
		releaseCamera();		
	}
	
	private void releaseCamera(){
		if(mCamera != null){
			mCamera.release();
			mCamera = null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
