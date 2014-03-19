package com.fandome.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import com.example.android.bitmapfun.util.ImageFetcher;
import com.fandome.communication.FacebookManager;
import com.fandome.communication.MyFandomeManager;
import com.fandome.facebook.FeedModel;
import com.fandome.infra.Keys;
import com.fandome.infra.MemoryCache;
import com.fandome.intefaces.IFinishNotify;
import com.fandome.views.ImageViewWithProcessIndicator;
import com.parse.ParseFacebookUtils;

public class SingleImageActivity extends MasterPageActivity {

	private ImageViewWithProcessIndicator imageView;
	private String imageId;
	private CheckBox myFandomeBtn;
	private ImageFetcher imageFetcher;
	
	private final String IMAGE_RELATION = "ImagesList";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_image);
		imageFetcher = new ImageFetcher(this, getScreenWidth(), getScreenHeight());
		imageView = (ImageViewWithProcessIndicator) findViewById(R.id.image);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			imageId = getIntent().getExtras()
					.getString(Keys.Images.SingleImage);
			imageFetcher.loadImage(imageId, imageView);
		}

		myFandomeBtn = (CheckBox) findViewById(R.id.favorite);
		if (MyFandomeManager.getInstance().contain(imageId)) {
			myFandomeBtn.setChecked(true);
		} else {
			myFandomeBtn.setChecked(false);
		}

		myFandomeBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					MyFandomeManager.getInstance().addImageId(imageId);
				} else {
					MyFandomeManager.getInstance().remove(imageId,
							IMAGE_RELATION);
				}
			}
		});
	}

	public void onShareClicked(View view) {
		FacebookManager.getInstance().facebookConnect(SingleImageActivity.this);
		//String imageUrl = IdToUrlDictionary.getInstance().getUrl(imageId);

		if (imageId == null || imageId == "") {
			Toast.makeText(this, "can't share this image", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		FeedModel feed = new FeedModel();
		feed.setName("Maccabi Tel Aviv vs Hapoel Tel Aviv");
		feed.setCaption("YEAH!!!");
		feed.setLink("fando.me");
		feed.setDescription("I just make a great photo from Derby game!");
		feed.setImage(imageId);
		FacebookManager.getInstance().post(this, feed,
				new IFinishNotify<String>() {

					public void onFinished(String result) {

					}

					public void onError(Exception e) {

					}
				});
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	  
	}
	
	
	public void finish() {
		super.finish();
		MemoryCache.getInstance().remove(imageId);
	}

	public View getTopRightView() {
		CheckBox myFandomeBtn = (CheckBox) LayoutInflater.from(this).inflate(
				R.layout.favorite_layout, null);
		return myFandomeBtn;
	}
}
