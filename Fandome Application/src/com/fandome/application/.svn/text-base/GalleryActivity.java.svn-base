package com.fandome.application;

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.fandome.communication.UsersImagesManager;
import com.fandome.infra.CameraHelper;
import com.fandome.infra.GeneralUtility;
import com.fandome.infra.ImagesHelper;
import com.fandome.infra.Keys;
import com.fandome.intefaces.IFinishNotify;
import com.fandome.listadapters.GalleryAdapter;
import com.fandome.models.ArticleModel;
import com.fandome.receivers.ResourceDownloadReceiver;
import com.fandome.services.ResourcesDownloadService;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class GalleryActivity extends MasterPageActivity implements ResourceDownloadReceiver.Callback{
	private final int FACEBOOK_LOGIN_CODE = 1;
	private final int CAMERA_CODE = 2;
	private final int PICK_IMAGE = 3;
	private GalleryAdapter imageAdapter;
	private ProgressDialog dialog;
	private Uri takenImagePath;
	private int imageHeight;
	private int imageWidth;
	private GridView gridview;
	private boolean mIsBound = false;
	private ResourceDownloadReceiver resourceDownloadReceiver;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);
		dialog = GeneralUtility.getCancelableProgressDialog("Loading Images",
				"", this);
		resourceDownloadReceiver = new ResourceDownloadReceiver(this);
		gridview = (GridView) findViewById(R.id.gridview);
		imageHeight = (int) (GeneralUtility.getSceenSize(this).y / 2.5D);
		imageWidth = 16*imageHeight/9;
		imageAdapter = new GalleryAdapter(this, imageHeight, imageWidth);
		gridview.setAdapter(imageAdapter);

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				String imageId = (String) parent
						.getItemAtPosition(position);
				if (imageId != null ) {
					Intent intent = new Intent(GalleryActivity.this,
							SingleImageActivity.class);
					intent.putExtra(Keys.Images.SingleImage, imageId);
					startActivity(intent);
				} else {
					Toast.makeText(GalleryActivity.this,
							"please wait while image is loading",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	public void onResume(){
		super.onResume();
		registerReceiver(resourceDownloadReceiver, resourceDownloadReceiver.getIntentFilter());	
		Intent intent = new Intent(this, ResourcesDownloadService.class);
		intent.setAction(ResourcesDownloadService.Operation.GalleryImagesList);
		startService(intent);
	}
	
	public void onPause(){
		super.onPause();
		unregisterReceiver(resourceDownloadReceiver);
	}


	public void onChoosePhotoClicked(View view) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"),
				PICK_IMAGE);
	}
	

	public void onTakePhotoClicked(View v) {
		if (CameraHelper.isIntentAvailable(this)) {
			Intent takePictureIntent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			File f;
			try {
				f = CameraHelper.getNewPhotoTempFile();
				takenImagePath = Uri.fromFile(f);
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						takenImagePath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			startActivityForResult(takePictureIntent, CAMERA_CODE);
		} else {
			Toast.makeText(this, "your device hasn't camera support",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == FACEBOOK_LOGIN_CODE)
			ParseFacebookUtils.finishAuthentication(requestCode, resultCode,
					data);
		else if (requestCode == CAMERA_CODE && resultCode == Activity.RESULT_OK) {
			if (takenImagePath != null) {
				CameraHelper.addImageToGallery(this, takenImagePath);
				final Bitmap image = ImagesHelper.getBitmapFromPath(takenImagePath.getPath(), imageWidth, imageHeight);
				if (image != null) {
					UsersImagesManager.getInstance().uploadImage(image, GeneralUtility.getNewImageFileName(), new IFinishNotify<String>() {
						
						public void onFinished(String result) {
							imageAdapter.addNewImage(result);
						}
						
						public void onError(Exception e) {
							Toast.makeText(GalleryActivity.this, "can't upload your image to server", Toast.LENGTH_LONG).show();
						}
					});
					Toast.makeText(this, "The photo uploading may have delays due to low bandwidth", Toast.LENGTH_LONG).show();
				}
			}
		} else if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
			try{
				Bitmap image = ImagesHelper.getPickedImage(data, getContentResolver(), imageWidth, imageHeight);
				if (image != null) {
					UsersImagesManager.getInstance().uploadImage(image, GeneralUtility.getNewImageFileName(), new IFinishNotify<String>() {
						
						public void onFinished(String result) {
							imageAdapter.addNewImage(result);
						}
						
						public void onError(Exception e) {
							
						}
					});
					Toast.makeText(this, "The photo uploading may have delays due to low bandwidth", Toast.LENGTH_SHORT).show();
				}
			}
			catch(Exception e){
				Toast.makeText(this, "can't load this image", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void facebookConnect() {
		ParseFacebookUtils.logIn(GalleryActivity.this, FACEBOOK_LOGIN_CODE,
				new LogInCallback() {
					@Override
					public void done(ParseUser user, ParseException err) {
						if (user == null) {
							Log.d("MyApp",
									"Uh oh. The user cancelled the Facebook login.");
						} else if (user.isNew()) {
							Log.d("MyApp",
									"User signed up and logged in through Facebook!");
						} else {
							Log.d("MyApp", "User logged in through Facebook!");
						}
					}
				});
	}

	public void onImagesList(List<String> imagesList) {
		if(dialog != null && dialog.isShowing())
			dialog.dismiss();
		if(imagesList != null){
			for (String imageId : imagesList) {
				imageAdapter.addNewImage(imageId);
			}
		}
	}

	public void onArticlesList(List<ArticleModel> articles) {
		// TODO Auto-generated method stub
		
	}

}
