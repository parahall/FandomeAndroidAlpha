package com.fandome.infra;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.example.android.bitmapfun.util.ImagesCache;
import com.example.android.bitmapfun.util.RecyclingBitmapDrawable;
import com.example.android.bitmapfun.util.Utils;
import com.fandome.application.ApplicationManager;
import com.fandome.application.BuildConfig;

public class ImagesHelper {
	private static final int MAX_SAMPLE_WITH = 500;
	private static final int MAX_SAMPLE_HEIGHT = 500;

	public static Bitmap downloadImageFromUrl(String url) throws MalformedURLException, IOException{
        InputStream in = new java.net.URL(url).openStream();
        
     // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 100, 100);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        // If we're running on Honeycomb or newer, try to use inBitmap
        if (Utils.hasHoneycomb()) {
            addInBitmapOptions(options, ImagesCache.getInstance());
        }

        return BitmapFactory.decodeStream(in, null, options);
	}
	
	public static BitmapDrawable convert(Bitmap image){
		if(image == null)
			return null;
		return new RecyclingBitmapDrawable(ApplicationManager.getResourceManager(), image);
	}
	
	
	public static byte[] ConvertImageFromBitmaptoByteArray(Bitmap bitmap){
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
		return stream.toByteArray();
	}
	
	public static Bitmap getBitmapFromPath(String path, int with, int height){
		ExifInterface exif;
		int orientationTag = 0; 
		try {
			exif = new ExifInterface(path);
			orientationTag = Integer.parseInt(exif.getAttribute(ExifInterface.TAG_ORIENTATION));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	    // Get the dimensions of the bitmap
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    bmOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(path, bmOptions);
	    int photoW = bmOptions.outWidth;
	    int photoH = bmOptions.outHeight;
	  
	    // Determine how much to scale down the image
	    int scaleFactor = Math.min(photoW/with, photoH/height);
	  
	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = scaleFactor;
	    bmOptions.inPurgeable = true;
	  
	    
	    Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
	    
	    return fixOrientation(bitmap, orientationTag);
	}
	
	private static Bitmap fixOrientation(Bitmap bitmap, int orientationTag){
		if(orientationTag == 6){
			bitmap=rotate(bitmap, 90);
		}else if(orientationTag == 8){
			bitmap=rotate(bitmap, 270);
		}else if(orientationTag == 3){
			bitmap=rotate(bitmap, 180);
		}
		
		return bitmap;
	}
	
	private static Bitmap rotate(Bitmap source, int degree){
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(source , 0, 0, source.getWidth(), source.getHeight(), matrix, true);
	}
	
	public static Bitmap getPickedImage(Intent data, ContentResolver contentResolver, int width, int height) throws Exception{
		try{
			if (data != null && data.getData() != null) {
				Uri _uri = data.getData();
	
				if (_uri != null) {
					// User had pick an image.
					Cursor cursor = 
							contentResolver.query(_uri,
									new String[] { android.provider.MediaStore.Images.ImageColumns.DATA },
									null, null, null);
					cursor.moveToFirst();
	
					// Link to the image
					final String imageFilePath = cursor.getString(0);
					cursor.close();
					return getBitmapFromPath(imageFilePath, width, height);
				}
			}
		}
		catch(RuntimeException e){
			throw new Exception("can't load image");
		}
		throw new Exception("can't load image");
	}
	
	/**
     * Decode and sample down a bitmap from resources to the requested width and height.
     *
     * @param res The resources object containing the image data
     * @param resId The resource id of the image data
     * @param reqWidth The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @param cache The ImageCache used to find candidate bitmaps for use with inBitmap
     * @return A bitmap sampled down from the original with the same aspect ratio and dimensions
     *         that are equal to or greater than the requested width and height
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
            int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // If we're running on Honeycomb or newer, try to use inBitmap
        if (Utils.hasHoneycomb()) {
            addInBitmapOptions(options, ImagesCache.getInstance());
        }

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * Decode and sample down a bitmap from a file to the requested width and height.
     *
     * @param filename The full path of the file to decode
     * @param reqWidth The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @param cache The ImageCache used to find candidate bitmaps for use with inBitmap
     * @return A bitmap sampled down from the original with the same aspect ratio and dimensions
     *         that are equal to or greater than the requested width and height
     */
    public static Bitmap decodeSampledBitmapFromFile(String filename,
            int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filename, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // If we're running on Honeycomb or newer, try to use inBitmap
        if (Utils.hasHoneycomb()) {
            addInBitmapOptions(options, ImagesCache.getInstance());
        }

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filename, options);
    }

    /**
     * Decode and sample down a bitmap from a file input stream to the requested width and height.
     *
     * @param fileDescriptor The file descriptor to read from
     * @param reqWidth The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @param cache The ImageCache used to find candidate bitmaps for use with inBitmap
     * @return A bitmap sampled down from the original with the same aspect ratio and dimensions
     *         that are equal to or greater than the requested width and height
     */
    public static Bitmap decodeSampledBitmapFromDescriptor(
            FileDescriptor fileDescriptor, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        // If we're running on Honeycomb or newer, try to use inBitmap
        if (Utils.hasHoneycomb()) {
            addInBitmapOptions(options, ImagesCache.getInstance());
        }

        return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static void addInBitmapOptions(BitmapFactory.Options options, ImagesCache cache) {
        // inBitmap only works with mutable bitmaps so force the decoder to
        // return mutable bitmaps.
        options.inMutable = true;

        if (cache != null) {
            // Try and find a bitmap to use for inBitmap
            Bitmap inBitmap = cache.getBitmapFromReusableSet(options);

            if (inBitmap != null) {
                if (BuildConfig.DEBUG) {
                    Log.d("ImagesHelper", "Found bitmap to use for inBitmap");
                }
                options.inBitmap = inBitmap;
            }
        }
    }

    /**
     * Calculate an inSampleSize for use in a {@link BitmapFactory.Options} object when decoding
     * bitmaps using the decode* methods from {@link BitmapFactory}. This implementation calculates
     * the closest inSampleSize that will result in the final decoded bitmap having a width and
     * height equal to or larger than the requested width and height. This implementation does not
     * ensure a power of 2 is returned for inSampleSize which can be faster when decoding but
     * results in a larger bitmap which isn't as useful for caching purposes.
     *
     * @param options An options object with out* params already populated (run through a decode*
     *            method with inJustDecodeBounds==true
     * @param reqWidth The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return The value to be used for inSampleSize
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
            int reqWidth, int reqHeight) {
    	reqWidth = Math.min(reqWidth, MAX_SAMPLE_WITH);
    	reqHeight = Math.min(reqHeight, MAX_SAMPLE_HEIGHT);
    	
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }
	
}
