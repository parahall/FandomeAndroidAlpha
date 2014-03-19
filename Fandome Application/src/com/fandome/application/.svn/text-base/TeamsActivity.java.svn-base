package com.fandome.application;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import com.fandome.communication.GameManager;
import com.fandome.fragments.TeamsFragment;
import com.fandome.infra.GeneralUtility;
import com.fandome.models.GameDetailsModel;
import com.fandome.models.GameModel;

public class TeamsActivity extends MasterPageActivity{

	private ProgressDialog dialog;
	private ViewPager viewPager;
	private TeamsPagerAdapter teamsPagerAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_teams);
		Log.d("TeamsFragment", "TeamsActivity.onCreate");
		
		viewPager = (ViewPager)findViewById(R.id.pager);
		teamsPagerAdapter = new TeamsPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(teamsPagerAdapter);
		dialog = GeneralUtility.getCancelableProgressDialog("Loading", "Fetch Team Formation", this);
		GameModel game = GameManager.getInstance().getCurrentGameFromCache();
		if(game != null){
	        Log.d("TeamsFragment", "TeamsActivity.onGameArrive");
			List<GameDetailsModel> gameDetailsList = new ArrayList<GameDetailsModel>();
			gameDetailsList.add(game.getHomeGameDetails());
			gameDetailsList.add(game.getAwayGameDetails());
			teamsPagerAdapter.setGameDetails(gameDetailsList);
			teamsPagerAdapter.notifyDataSetChanged();
			dialog.dismiss();
		}else{
			Toast.makeText(this, "teams still updating, please try again in few seconds", Toast.LENGTH_SHORT).show();
			finish();
		}
	}
	
	
	
	public class TeamsPagerAdapter extends FragmentPagerAdapter{
		private List<GameDetailsModel> gameDetails;
		public TeamsPagerAdapter(FragmentManager fm) {
			super(fm);
			gameDetails = new ArrayList<GameDetailsModel>();
		}

		@Override
		public Fragment getItem(int index) {
			Log.d("TeamsFragment", "TeamsPagerAdapter.getItem");
			TeamsFragment fragment = (TeamsFragment)Fragment.instantiate(getBaseContext(), TeamsFragment.class.getName());
			fragment.setGameDetails(getGameDetails().get(index));
			return fragment;
		}

		@Override
		public int getCount() {
			return getGameDetails().size();
		}

		public List<GameDetailsModel> getGameDetails() {
			return gameDetails;
		}

		public void setGameDetails(List<GameDetailsModel> gameDetails) {
			this.gameDetails = gameDetails;
		}
		
	}

}
