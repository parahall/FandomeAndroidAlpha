package com.fandome.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fandome.application.PlayerDetailsActivity;
import com.fandome.infra.Keys;
import com.fandome.infra.MemoryCache;
import com.fandome.models.GameDetailsModel;
import com.fandome.models.PlayerModel;
import com.fandome.views.TeamFormationView;
import com.fandome.views.TeamFormationView.IPlayerClickedListener;

public class TeamsFragment extends BaseFragment {

	private GameDetailsModel gameDetails;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Log.d("TeamsFragment", "TeamsFragment.onCreateView");
		TeamFormationView teamFormationView = new TeamFormationView(
				getActivity());

		teamFormationView
				.setPlayerClickedListener(new IPlayerClickedListener() {

					public void OnPlayerClicked(PlayerModel player) {
						MemoryCache.getInstance().add(player.getId(), player);
						Intent intent = new Intent(getActivity(),
								PlayerDetailsActivity.class);
						intent.putExtra(Keys.Players.PlayerId, player.getId());
						getActivity().startActivity(intent);
						getActivity()
								.overridePendingTransition(
										android.R.anim.fade_in,
										android.R.anim.fade_out);
					}
				});

		if (savedInstanceState != null) {
			String gameId = savedInstanceState.getString(Keys.Games.GameId);
			setGameDetails(MemoryCache.getInstance().getAs(gameId,
					GameDetailsModel.class));
		}

		if (getGameDetails() != null) {
			teamFormationView.setGameDetails(getGameDetails());
		}
		Log.d("TeamsFragment", "TeamsFragment.onCreateView finished");

		return teamFormationView;
	}

	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		MemoryCache.getInstance().add(gameDetails.getId(), gameDetails);
		outState.putString(Keys.Games.GameId, gameDetails.getId());
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public GameDetailsModel getGameDetails() {
		return gameDetails;
	}

	public void setGameDetails(GameDetailsModel gameDetails) {
		this.gameDetails = gameDetails;
	}

}
