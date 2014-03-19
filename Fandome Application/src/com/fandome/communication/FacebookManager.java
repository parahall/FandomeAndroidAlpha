package com.fandome.communication;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.fandome.application.ApplicationManager;
import com.fandome.facebook.ArticleFacebookConvertor;
import com.fandome.facebook.BundleFacebookCOnvertor;
import com.fandome.facebook.FeedConvertor;
import com.fandome.facebook.IFacebookObjectConvertor;
import com.fandome.intefaces.IFinishNotify;
import com.fandome.models.ArticleModel;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.SaveCallback;

public class FacebookManager {
	private static final String POST_SUCCESS_MESSAGE = "Story was posted";
	private static final String CANCEL_MESSAGE = "Publish cancelled";
	private static final String ERROR_MESSAGE = "Error posting story";
	private final int FACEBOOK_LOGIN_CODE = 1;
	public List<IFacebookObjectConvertor> converotrs = new ArrayList<IFacebookObjectConvertor>();

	private static FacebookManager instance = new FacebookManager();

	private FacebookManager() {
		converotrs.add(new BundleFacebookCOnvertor());
		converotrs.add(new FeedConvertor());
		converotrs.add(new ArticleFacebookConvertor());
	}

	public static FacebookManager getInstance() {
		return instance;
	}

	public void facebookConnect(Activity activity) {
		if (!ParseFacebookUtils.isLinked(UserManager.getInstance()
				.GetCurrentUser())) {
			ParseFacebookUtils.link(UserManager.getInstance().GetCurrentUser(),
					activity, new SaveCallback() {
						@Override
						public void done(ParseException ex) {
							if (ex != null) {
								Log.d("Facebook", ex.getMessage());
							}
							if (ParseFacebookUtils.isLinked(UserManager
									.getInstance().GetCurrentUser())) {
								Log.d("Facebook",
										"Woohoo, user logged in with Facebook!");
							}
						}
					});
		}
		// Old connect
//		ParseFacebookUtils.logIn(
//				activity,
//				FACEBOOK_LOGIN_CODE, new LogInCallback() {
//					@Override
//					public void done(ParseUser user, ParseException err) {
//						if (user == null) {
//							Log.d("Facebook",
//									"Uh oh. The user cancelled the Facebook login.");
//						} else if (user.isNew()) {
//							Log.d("Facebook",
//									"User signed up and logged in through Facebook!");
//						} else {
//							Log.d("Facebook",
//									"User logged in through Facebook!");
//						}
//					}
//				});
	}

	public void shareImage(String link, IFinishNotify<String> callback) {

	}

	public void postArticle(Activity activity, ArticleModel article,
			IFinishNotify<String> callback) {
		post(activity, article, callback);
	}

	public void post(Activity activity, Object object,
			IFinishNotify<String> callback) {
		for (IFacebookObjectConvertor convertor : converotrs) {
			if (convertor.canHandle(object)) {
				postWithDialog(activity, convertor.convert(object), callback);
				return;
			}
		}
		callback.onError(new RuntimeException(
				"can't find any FacebookObjectConvertor can handle "
						+ object.getClass().getSimpleName()));
	}

	private void postWithDialog(Activity activity, Bundle postParams,
			final IFinishNotify<String> callback) {
		try {
			WebDialog feedDialog = new WebDialog.FeedDialogBuilder(activity,
					Session.getActiveSession(), postParams)
					.setOnCompleteListener(FacebookCallback(callback)).build();
			feedDialog.show();
		} catch (Exception e) {
			if (e != null)
				System.out.println(e.getMessage());
		}
	}

	private OnCompleteListener FacebookCallback(
			final IFinishNotify<String> callback) {
		return new OnCompleteListener() {

			public void onComplete(Bundle values, FacebookException error) {

				if (error == null) {
					// When the story is posted, echo the success
					// and the post Id.
					final String postId = values.getString("post_id");
					if (postId != null) {
						Toast.makeText(ApplicationManager.getAppContext(),
								POST_SUCCESS_MESSAGE, Toast.LENGTH_SHORT)
								.show();
						if (callback != null) {
							callback.onFinished(postId);
						}
					} else {
						// User clicked the Cancel button
						Toast.makeText(ApplicationManager.getAppContext(),
								CANCEL_MESSAGE, Toast.LENGTH_SHORT).show();
						if (callback != null) {
							callback.onError(null);
						}
					}
				} else if (error instanceof FacebookOperationCanceledException) {
					// User clicked the "x" button
					Toast.makeText(ApplicationManager.getAppContext(),
							CANCEL_MESSAGE, Toast.LENGTH_SHORT).show();
					if (callback != null) {
						callback.onError(error);
					}
				} else {
					// Generic, ex: network error
					Toast.makeText(ApplicationManager.getAppContext(),
							ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
					if (callback != null) {
						callback.onError(error);
					}
				}
			}
		};
	}

	private void postWithoutDialog(Bundle postParams) {
		try {

			Request.Callback callback = new Request.Callback() {
				public void onCompleted(Response response) {
					JSONObject graphResponse = response.getGraphObject()
							.getInnerJSONObject();
					String postId = null;
					try {
						postId = graphResponse.getString("id");
					} catch (JSONException e) {
						Log.i("Facebook", "JSON error " + e.getMessage());
					}
					FacebookRequestError error = response.getError();
					if (error != null) {
						Toast.makeText(ApplicationManager.getAppContext(),
								error.getErrorMessage(), Toast.LENGTH_SHORT)
								.show();
					} else {
						Toast.makeText(
								ApplicationManager.getAppContext()
										.getApplicationContext(), postId,
								Toast.LENGTH_LONG).show();
					}
				}
			};

			Request request = new Request(ParseFacebookUtils.getSession(),
					"me/feed", postParams, HttpMethod.POST, callback);

			RequestAsyncTask task = new RequestAsyncTask(request);
			task.execute();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}

}
