package com.fandome.infra;

import java.io.File;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Point;
import android.os.Build;
import android.os.Environment;
import android.view.Display;
import android.view.WindowManager;

import com.fandome.application.R;

public class GeneralUtility {
	public static final int IO_BUFFER_SIZE = 8 * 1024;
	
	public static ProgressDialog getCancelableProgressDialog(String title, String message, final Activity activity){
		return ProgressDialog.show(activity, title, message, false, true, new OnCancelListener() {
			
			public void onCancel(DialogInterface dialog) {
				activity.finish();
			}
		});
	}
	
	public static Dialog GetConnectionErrorDialog(final Activity activity){
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(R.string.connection_error)
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   activity.finish();
                   }
               });
        return builder.create();
	}
	
	public static String getNewImageFileName(){
		return UUID.randomUUID().toString()+".jpg";
	}
	
	public static Point getSceenSize(Context ctx){
		WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		return size;
	}
	
	public static boolean isExternalStorageRemovable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    public static File getExternalCacheDir(Context context) {
        if (hasExternalCacheDir()) {
            return context.getExternalCacheDir();
        }

        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    public static boolean hasExternalCacheDir() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }
}
