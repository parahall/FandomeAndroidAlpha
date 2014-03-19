package com.fandome.infra;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.fandome.application.ApplicationManager;
import com.fandome.application.R;

public class CameraHelper {
	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";
	private static final String CAMERA_DIR = "/dcim/";
	
	public static boolean isIntentAvailable(Context context) {
	    final PackageManager packageManager = context.getPackageManager();
	    final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    List<ResolveInfo> list =
	            packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
	    return list.size() > 0;
	}
	
	public static Drawable handleCameraPhoto(Intent intent, String key) {
	    Drawable result = null;
	    if(intent != null){
			Bundle extras = intent.getExtras();
		    if(extras != null){
			    Bitmap imageBitmap = (Bitmap) extras.get(key);
			    if(imageBitmap != null)
			    	result = ImagesHelper.convert(imageBitmap);
		    }
	    }
	    return result;
	}
	
	private static String getAlbumName() {
		return ApplicationManager.getResourceManager().getString(R.string.album_name).replace(' ', '_');
	}

	

	private static File getAlbumStorageDir() {
		return new File (
				Environment.getExternalStorageDirectory()
				+ CAMERA_DIR
				+ getAlbumName()
				);
	}
	
	
	private static File getAlbumDir() {
		File storageDir = null;

		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			
			storageDir = getAlbumStorageDir();

			if (storageDir != null) {
				if (! storageDir.mkdirs()) {
					if (! storageDir.exists()){
						return null;
					}
				}
			}
			
		} else {
			Log.v(ApplicationManager.getResourceManager().getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
		}
		
		return storageDir;
	}

	public static File getNewPhotoTempFile() throws IOException {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
		File albumF = getAlbumDir();
		File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
		return imageF;
	}
	
	public static void addImageToGallery(Activity activity, Uri filePath) {
	    Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
	    mediaScanIntent.setData(filePath);
	    activity.sendBroadcast(mediaScanIntent);
	}
	
	public static void addImageToGallery(Activity activity, String filePath) {
		File f = new File(filePath);
	    Uri contentUri = Uri.fromFile(f);
	    addImageToGallery(activity, contentUri);
	}

}
