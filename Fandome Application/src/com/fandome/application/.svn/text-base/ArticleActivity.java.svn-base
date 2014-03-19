package com.fandome.application;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.fandome.communication.FacebookManager;
import com.fandome.fragments.ArticleFragment;
import com.fandome.infra.Keys;
import com.fandome.infra.MemoryCache;
import com.fandome.intefaces.IFinishNotify;
import com.fandome.models.ArticleModel;
import com.parse.ParseFacebookUtils;

public class ArticleActivity extends MasterPageActivity {

	private ViewPager viewPager;
	private ArticlesPagerAdapter articlesPagerAdapter;

	private CheckBox myFandomeBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article);

		viewPager = (ViewPager) findViewById(R.id.pager);
		articlesPagerAdapter = new ArticlesPagerAdapter(
				getSupportFragmentManager());
		viewPager.setAdapter(articlesPagerAdapter);
		myFandomeBtn = (CheckBox) findViewById(R.id.favorite);
		myFandomeBtn.setChecked(getCurrentFragment().getMyFandomeState());
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			public void onPageSelected(int arg0) {
				myFandomeBtn.setChecked(getCurrentFragment()
						.getMyFandomeState());

			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		myFandomeBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				getCurrentFragment().onMyFandomeBtnClicked(isChecked);
			}
		});

		int articleIndex;
		if (savedInstanceState != null) {
			articleIndex = savedInstanceState
					.getInt(Keys.Articles.ArticleClicked);
		} else {
			articleIndex = getIntent().getExtras().getInt(
					Keys.Articles.ArticleClicked);
		}

		viewPager.setCurrentItem(articleIndex);
	}

	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(Keys.Articles.ArticleClicked,
				viewPager.getCurrentItem());
	}

	public class ArticlesPagerAdapter extends FragmentPagerAdapter {
		private List<ArticleModel> articles;

		@SuppressWarnings("unchecked")
		public ArticlesPagerAdapter(FragmentManager fm) {
			super(fm);
			articles = (List<ArticleModel>)MemoryCache.getInstance().get(Keys.Articles.ArticleListKey);
		}

		@Override
		public Fragment getItem(int index) {
			ArticleFragment fragment = (ArticleFragment) Fragment.instantiate(
					getBaseContext(), ArticleFragment.class.getName());
			fragment.setArticle(articles.get(index));
			return fragment;
		}

		@Override
		public int getCount() {
			return articles.size();
		}

	}

	@Override
	public View getTopRightView() {
		CheckBox myFandomeBtn = (CheckBox) LayoutInflater.from(this).inflate(
				R.layout.favorite_layout, null);
		return myFandomeBtn;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_article, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		getCurrentFragment().onOptionsItemSelected(this, item);
		return true;
	}

	private ArticleFragment getCurrentFragment() {
		int currentItem = viewPager.getCurrentItem();
		return (ArticleFragment) articlesPagerAdapter.getItem(currentItem);
	}

	public void onShareClicked(View v) {
		FacebookManager.getInstance().postArticle(this,
				getCurrentFragment().getArticle(), new IFinishNotify<String>() {

					public void onFinished(String result) {

					}

					public void onError(Exception e) {

					}
				});
	}

}
