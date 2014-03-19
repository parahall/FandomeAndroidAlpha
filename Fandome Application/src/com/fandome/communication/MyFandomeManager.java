package com.fandome.communication;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.util.Log;
import android.widget.Toast;

import com.fandome.application.ApplicationManager;
import com.fandome.intefaces.IFinishNotify;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class MyFandomeManager {

	private List<String> articlesList = new ArrayList<String>();
	private List<String> imagesList = new ArrayList<String>();
	private final String IMAGE_RELATION = "ImagesList";
	private final String IMAGE_OBJECTTYPE = "GameImages";
	private final String ARTICLE_RELATION = "ArticlesList";
	private final String ARTICLE_OBJECTTYPE = "Article";

	private static MyFandomeManager instance = new MyFandomeManager();

	private MyFandomeManager() {
	}
	
	public void init(final IFinishNotify<Void> callback){
		UserManager.getInstance().GetCurrentUserAsync(new IFinishNotify<ParseUser>() {
			
			public void onFinished(ParseUser user) {
				if (user != null) {
						Log.d("MyFandome", "User saved! " + user.getObjectId());
						getAllArticles();
						// getAllImages();
						callback.onFinished(null);
				}
			}
			
			public void onError(Exception e) {
				callback.onError(e);
			}
		});
	}

	private void getAllImages() {
		ParseRelation relation = UserManager.getInstance().GetCurrentUser().getRelation("ImagesList");
		relation.getQuery().findInBackground(new FindCallback() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					for (ParseObject parseObject : objects) {
						imagesList.add(parseObject.getObjectId());
					}
				} else {
					Log.d("MyFandome", e.getMessage());
				}

			}
		});
	}

	private void getAllArticles() {
		ParseRelation relation = UserManager.getInstance().GetCurrentUser().getRelation(ARTICLE_RELATION);
		relation.getQuery().findInBackground(new FindCallback() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					for (ParseObject parseObject : objects) {
						articlesList.add(parseObject.getObjectId());
						for (String objId : articlesList) {
							Log.d("MyFandome", "Article stored: " + objId);
						}
					}
					getAllImages();
				} else {
					Log.d("MyFandome", e.getMessage());
				}
			}
		});
	}

	public static MyFandomeManager getInstance() {
		return instance;
	}

	public void addImageId(String id) {
		if (UserManager.getInstance().GetCurrentUser() != null && !contain(id)) {
			imagesList.add(id);
			saveRelation(IMAGE_RELATION, IMAGE_OBJECTTYPE, id);
		}
	}

	private void saveRelation(final String relation, final String objectType,
			final String id) {
		UserManager.getInstance().GetCurrentUser().getRelation(relation).add(
				ParseObject.createWithoutData(objectType, id));
		UserManager.getInstance().GetCurrentUser().saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				if (e == null) {
					Log.d("MyFandome",
							"Object of type: " + objectType + "and id: " + id
									+ " saved succesfully for user: "
									+ UserManager.getInstance().GetCurrentUser().getObjectId());
					String object;
					if(objectType == ARTICLE_OBJECTTYPE){
						object = "Article";
					} else {
						object = "Image";
					}
					Toast.makeText(ApplicationManager.getAppContext(), object + " was saved on MyFandome", Toast.LENGTH_SHORT).show();
				} else {
					Log.d("MyFandome", "Error while saving article id: " + id
							+ " for user: " + UserManager.getInstance().GetCurrentUser().getObjectId()
							+ "with reason " + e.getMessage());
				}

			}
		});
	}

	public void sendDigitalProgramRequest(){
		//SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
		UserManager.getInstance().GetCurrentUser().put("digitalProgram",new Date());
		UserManager.getInstance().GetCurrentUser().saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				if (e==null){
					//showAlertDialog();
				} else {
					Log.d("MyFandome","Error while saving digital program request with reason: "+e.getMessage());
				}
			}
		});
	}
	
	
	
	public void addArticleId(String id) {
		if (UserManager.getInstance().GetCurrentUser() != null && !contain(id)) {
			articlesList.add(id);
			saveRelation(ARTICLE_RELATION, ARTICLE_OBJECTTYPE, id);
		}
	}

	public boolean contain(String id) {
		try{
			if (articlesList.contains(id) || imagesList.contains(id)) {
				return true;
			} else {
				return false;
			}
		}
		catch(Exception e){
			return false;
		}
	}

	public void remove(final String id, final String relation) {
		articlesList.remove(id);
		imagesList.remove(id);
		if (relation == ARTICLE_RELATION) {
			UserManager.getInstance().GetCurrentUser().getRelation(ARTICLE_RELATION).remove(
					ParseObject.createWithoutData(ARTICLE_OBJECTTYPE, id));
		} else {
			UserManager.getInstance().GetCurrentUser().getRelation(IMAGE_RELATION).remove(
					ParseObject.createWithoutData(IMAGE_OBJECTTYPE, id));
		}
		UserManager.getInstance().GetCurrentUser().saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				if (e == null) {
					Log.d("MyFandome",
							"User Saved after removing relation with id: " + id);
					String object;
					if (relation == ARTICLE_RELATION) {
						object = "Article";
					} else {
						object = "Image";
					}
					Toast.makeText(ApplicationManager.getAppContext(), object + " was removed from MyFandome", Toast.LENGTH_SHORT).show();
				} else {
					Log.d("MyFandome", "Failed to remove relation with : " + id
							+ "error msg: " + e.getMessage());
				}
			}
		});
	}

	public void sendDigitalProgramFeedback(String answer) {
		UserManager.getInstance().GetCurrentUser().put("digitalProgramFeedback",answer);
		UserManager.getInstance().GetCurrentUser().saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				
			}
		});
	}

}
