package com.fandome.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bitmapfun.util.ImageFetcher;
import com.fandome.application.R;
import com.fandome.models.PlayerModel;

public class PlayerDetailsView extends LinearLayout {
	private TextView name;
	private KeyValueLineView position;
	private KeyValueLineView age;
	private KeyValueLineView games;
	private KeyValueLineView goals;
	private KeyValueLineView assists;
	private KeyValueLineView redCards;
	private ImageViewWithProcessIndicator backgroundImage;
	private int height = 500;
	private int width = 500;
	private ImageFetcher imageFetcher;

	public PlayerDetailsView(Context context) {
		this(context, null);

	}

	public PlayerDetailsView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PlayerDetailsView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		LayoutInflater li = LayoutInflater.from(context);
		li.inflate(R.layout.player_details_layout, this);

		name = (TextView) findViewById(R.id.playerName);
		position = (KeyValueLineView) findViewById(R.id.position);
		age = (KeyValueLineView) findViewById(R.id.age);
		games = (KeyValueLineView) findViewById(R.id.games);
		goals = (KeyValueLineView) findViewById(R.id.goals);
		assists = (KeyValueLineView) findViewById(R.id.assists);
		redCards = (KeyValueLineView) findViewById(R.id.redCards);
		backgroundImage = (ImageViewWithProcessIndicator)findViewById(R.id.backgroudImage);
		if(!isInEditMode()){
			imageFetcher = new ImageFetcher(context, width, height);
		}
		
	}
	
	public void setPlayer(PlayerModel player) {
		bindPlayer(player);
	}
	

	private void bindPlayer(PlayerModel player) {
		name.setText(player.getFullName());
		position.setKeyValue("Position", player.getPosition());
		age.setKeyValue("Birthdate", player.getBirthdate() + "");
		games.setKeyValue("Games", player.getGames() + "");
		goals.setKeyValue("Goals", player.getGoals() + "");
		assists.setKeyValue("Assists", player.getAssists() + "");
		redCards.setKeyValue("Red Cards", player.getRedCards() + "");
		
		String url = player.getImageUrl();
		imageFetcher.loadImage(url, backgroundImage);
		
	}
}
