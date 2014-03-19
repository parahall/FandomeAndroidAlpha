package com.fandome.communication;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.List;

import com.fandome.infra.DiskCache;
import com.fandome.infra.Keys;
import com.fandome.infra.MemoryCache;
import com.fandome.infra.ParseCache;
import com.fandome.infra.SerializetionManager;
import com.fandome.infra.Stopwatch;
import com.fandome.models.GameDetailsModel;
import com.fandome.models.GameModel;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class GameManager {
	private static GameManager instance = new GameManager();
	private Object lock = new Object();
	private GameManager() {

	}

	public static GameManager getInstance() {
		return instance;
	}

	public GameModel map(ParseObject game) throws ParseException {
		GameModel result = new GameModel();
		result.setObjectId(game.getObjectId());
		result.setHome(TeamsManager.getInstance().map(
				game.getParseObject("home")));
		result.setAway(TeamsManager.getInstance().map(
				game.getParseObject("away")));
		result.setGameDate(game.getDate("gameDate"));
		GameDetailsModel map = GameDetailsManager.getInstance().map(
				game.getParseObject("homeGameDetails"), true);
		result.setHomeGameDetails(map);
		result.setAwayGameDetails(GameDetailsManager.getInstance().map(
				game.getParseObject("awayGameDetails")));
		return result;
	}

	public GameModel getCurrentGameSync() throws ParseException {
		synchronized (lock) {
			GameModel currentGameFromCache = getCurrentGameFromCache();
			if (currentGameFromCache != null)
				return currentGameFromCache;
	
			Stopwatch sw = Stopwatch.startNew();
	
			ParseQuery query = getQuery();
			ParseCache.setCachePolicy(query);
	
			GameModel game = null;
			ParseObject gameResult = query.getFirst();
			sw.stopAndRestart("get first game ");
			game = map(gameResult);
			sw.stopAndRestart("map game ");
			MemoryCache.getInstance().add(Keys.Games.CurrentGame, game);
			sw.stop("add to cahce");
			try {
				byte[] bytes = SerializetionManager.seralize(game);
				DiskCache.getInstance().addBytes(Keys.Games.CurrentGame, bytes);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			return game;
		}
	}

	public GameModel getCurrentGameFromCache() {

		GameModel memcache = MemoryCache.getInstance().getAs(
				Keys.Games.CurrentGame, GameModel.class);
		if (memcache != null)
			return memcache;

		byte[] diskCacheBytes = DiskCache.getInstance().getBytes(
				Keys.Games.CurrentGame);
		if (diskCacheBytes != null) {
			try {
				GameModel diskCache = (GameModel) SerializetionManager
						.deserialize(diskCacheBytes);
				MemoryCache.getInstance().add(Keys.Games.CurrentGame, diskCache);
				return diskCache;
			} catch (StreamCorruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	private ParseQuery getQuery() {
		ParseQuery query = new ParseQuery("Game");
		query.include("home");
		query.include("away");
		query.include("homeGameDetails");
		query.include("awayGameDetails");
		query.whereEqualTo("isEnable", true);
		ParseCache.setCachePolicy(query);
		return query;
	}

	public interface ICallback {
		void ongamesArrived(List<GameModel> games);

		void ongameArrived(GameModel game);

		void onError(Exception e);
	}

}
