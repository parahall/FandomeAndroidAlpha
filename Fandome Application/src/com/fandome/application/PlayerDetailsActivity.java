package com.fandome.application;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.fandome.infra.Keys;
import com.fandome.infra.MemoryCache;
import com.fandome.models.PlayerModel;
import com.fandome.views.PlayerDetailsView;

public class PlayerDetailsActivity extends SherlockFragmentActivity{

	private PlayerDetailsView playerDetailsView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player_details);
		
		String playerId = getIntent().getExtras().getString(Keys.Players.PlayerId);
		playerDetailsView = (PlayerDetailsView)findViewById(R.id.playerDetailsView);
		PlayerModel player = MemoryCache.getInstance().getAs(playerId, PlayerModel.class);
		
		playerDetailsView.setPlayer(player);
	}
	
	public void finish(){
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		super.finish();
	}
	


}
