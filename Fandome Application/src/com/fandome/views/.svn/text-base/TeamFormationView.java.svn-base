package com.fandome.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fandome.application.R;
import com.fandome.models.GameDetailsModel;
import com.fandome.models.PlayerModel;

public class TeamFormationView extends LinearLayout{
	private LinearLayout goalkeeperZone;
	private LinearLayout defendersZone;
	private LinearLayout defendmiddefendersZone;
	private LinearLayout middefendersZone;
	private LinearLayout attackmiddefendersZone;
	private LinearLayout strikersZone;
	private RelativeLayout soccerField;
	private boolean isHome;
	private LinearLayout[] zones;
	private ImageView playerLayout;
	private IPlayerClickedListener playerClickedListener;
	
	public TeamFormationView(Context context) {
		this(context, null);
		
	}
	
	public TeamFormationView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public TeamFormationView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		LayoutInflater li = LayoutInflater.from(context);
		li.inflate(R.layout.team_formation_layout, this);
		
		soccerField = (RelativeLayout)findViewById(R.id.soccerFieldRelavite);
    	goalkeeperZone = (LinearLayout)findViewById(R.id.goalkeeperZone);
    	defendersZone = (LinearLayout)findViewById(R.id.defendersZone);
    	defendmiddefendersZone = (LinearLayout)findViewById(R.id.defendmidefendersZone);
    	middefendersZone = (LinearLayout)findViewById(R.id.midefendersZone);
    	attackmiddefendersZone = (LinearLayout)findViewById(R.id.attackmidefendersZone);
    	strikersZone = (LinearLayout)findViewById(R.id.strikersZone);
    	
    	zones = new LinearLayout[]{defendersZone, defendmiddefendersZone, middefendersZone, attackmiddefendersZone, strikersZone};
	}
	
	private View getPlayer(final PlayerModel player){
    	PlayerView playerView = new PlayerView(getContext());
    	playerView.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				if(playerClickedListener != null)
					playerClickedListener.OnPlayerClicked(player);
			}
		});
    	playerView.setPlayer(player);
		return playerView;
    }
	public void setGameDetails(GameDetailsModel gameDetailsModel){
		clear();
		
		isHome =gameDetailsModel.getIsHome();
    	if (isHome){
    		soccerField.setBackgroundResource(R.drawable.home_field);
    	} else {
    		soccerField.setBackgroundResource(R.drawable.away_field);
    	}
		
		
		goalkeeperZone.addView(getPlayer(gameDetailsModel.getLineup().get(0)));
		int playerIndex = 1;
		int zoneIndex = 0;
		
		for (int count : gameDetailsModel.getFormation().getFormation()) {
			for (int i = 0; i < count; i++) {
				zones[zoneIndex].addView(getPlayer(gameDetailsModel.getLineup().get(playerIndex++)));
			}
			zoneIndex++;
		}
	}
	
	private void clear(){
		goalkeeperZone.removeAllViews();
		defendersZone.removeAllViews();
		defendmiddefendersZone.removeAllViews();
		middefendersZone.removeAllViews();
		attackmiddefendersZone.removeAllViews();
		strikersZone.removeAllViews();
	}
	
	public IPlayerClickedListener getPlayerClickedListener() {
		return playerClickedListener;
	}

	public void setPlayerClickedListener(IPlayerClickedListener playerClickedListener) {
		this.playerClickedListener = playerClickedListener;
	}

	public interface IPlayerClickedListener{
		void OnPlayerClicked(PlayerModel player);
	}
}
