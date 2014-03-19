package com.fandome.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fandome.application.R;
import com.fandome.models.PlayerModel;

public class PlayerView extends LinearLayout {

	private PlayerModel player;
	private TextView name;
	private ImageView image;

	public PlayerView(Context context) {
		this(context, null);

	}

	public PlayerView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PlayerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		LayoutInflater li = LayoutInflater.from(context);
		li.inflate(R.layout.player_layout, this);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(10, 0, 10, 0);

		name = (TextView) findViewById(R.id.playerName);
		image = (ImageView) findViewById(R.id.playerImage);
		
	}

	public PlayerModel getPlayer() {
		return player;
	}

	public void setPlayer(PlayerModel player) {
		this.player = player;
		bindModel(player);
	}

	private void bindModel(PlayerModel player) {
		name.setText(player.getFullName());
		if (player.IsHome()) {
			image.setImageResource(R.drawable.player_home);
		} else {
			image.setImageResource(R.drawable.player_away);
		}
//		if (player.getImage() != null)
//			image.setImageDrawable(player.getImage());
	}

}
