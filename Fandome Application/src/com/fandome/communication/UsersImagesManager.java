package com.fandome.communication;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.example.android.bitmapfun.util.ImagesCache;
import com.fandome.infra.DiskCache;
import com.fandome.infra.ImagesHelper;
import com.fandome.infra.Keys;
import com.fandome.infra.ListHelper;
import com.fandome.infra.MemoryCache;
import com.fandome.intefaces.IFinishNotify;
import com.fandome.models.GameModel;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class UsersImagesManager {
	
	private static UsersImagesManager instance = new UsersImagesManager();

	private UsersImagesManager() {
	}

	public static UsersImagesManager getInstance() {
		return instance;
	}
	
	public List<String> map(List<ParseObject> images) throws ParseException {
		List<String> result = new ArrayList<String>();
		if (images != null) {
			for (ParseObject image : images) {
				ParseFile imageFile = (ParseFile)image.get("image");
				String url = imageFile.getUrl();
				result.add(url);
			}
		}
		return result;
	}
	
	public List<String> getImagesList(String gameId) throws ParseException {
		ParseQuery query = getQuery();

		List<String> images = null;
		ParseObject gameObject = new ParseObject("Game");
		gameObject.setObjectId(gameId);
		query = query.whereEqualTo("gameId", gameObject);
		
		List<ParseObject> imagesResult = query.find();
		images = map(imagesResult);

		return images;
	}

	public List<String> getImagesListFromCache() {
		@SuppressWarnings("unchecked")
		List<String> memCache = (List<String>)MemoryCache.getInstance().get(Keys.Images.ImagesList);
		if(memCache != null){
			return memCache; 	
		}
		
		String diskCache = DiskCache.getInstance().get(Keys.Images.ImagesList);
		List<String> diskCacheResult = null;
		if(diskCache != null){
			diskCacheResult = ListHelper.fromStringToList(diskCache);
		}
		return diskCacheResult;
	}

	
	
	
	
	
	
	
	
	public void uploadImage(Bitmap bitmap, final String fileName, final IFinishNotify<String> callback){
    	new AsyncTask<Bitmap, Void, Result<String>>(){

			@Override
			protected Result<String> doInBackground(Bitmap... params) {
				ParseFile file = new ParseFile(fileName, ImagesHelper.ConvertImageFromBitmaptoByteArray(params[0]));
				
		    	try {
					file.save();
					ParseObject image = new ParseObject("GameImages");
			    	image.put("gameId", getCurrentGameParseObject());
			    	image.put("image", file);
			    	image.save();
			    	String imageUrl = file.getUrl();
			    	ImagesCache.getInstance().addBitmapToCache(imageUrl, ImagesHelper.convert(params[0]));
			    	return new Result<String>(imageUrl);
				} catch (ParseException e1) {
					e1.printStackTrace();
					callback.onError(e1);
				}
		    	return null;
			}

			@Override
			protected void onPostExecute(Result<String> result) {
				if(result.getError() == null)
					callback.onFinished(result.getResult());
				else
					callback.onError(result.getError());
			}
			
			
    	}.execute(bitmap);
    }
	
	private ParseObject getCurrentGameParseObject() throws ParseException{
		GameModel currentGame = GameManager.getInstance().getCurrentGameSync();
		return ParseObject.createWithoutData("Game", currentGame.getObjectId());
	}

	private ParseQuery getQuery() {
		ParseQuery query = new ParseQuery("GameImages");
		query.addAscendingOrder("createdAt");
		return query;
	}

	public interface ICallback {
		void onimageListArrived(List<String> images);
		void onimageArrived(Drawable image);

		void onError(Exception e);
	}
}
