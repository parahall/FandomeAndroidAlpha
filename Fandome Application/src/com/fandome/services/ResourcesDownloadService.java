package com.fandome.services;

import java.io.IOException;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.android.bitmapfun.util.ImagesCache;
import com.fandome.communication.ArticlesManager;
import com.fandome.communication.GameManager;
import com.fandome.communication.UsersImagesManager;
import com.fandome.infra.DatesHelper;
import com.fandome.infra.DiskCache;
import com.fandome.infra.ImagesHelper;
import com.fandome.infra.Keys;
import com.fandome.infra.ListHelper;
import com.fandome.infra.MemoryCache;
import com.fandome.infra.SerializetionManager;
import com.fandome.models.ArticleModel;
import com.fandome.models.GameModel;
import com.fandome.models.PlayerModel;
import com.parse.ParseException;

public class ResourcesDownloadService extends Service {
	public static final String OperationKeyword = "operation";

	public class Operation {
		public static final String GalleryImagesList = "GalleryImagesList";
		public static final String ArticlesList = "ArticlesList";
		public static final String LoadResources = "LoadResources";
	}

	public class CacheTimeout {
		public static final int ImagesList = 10 * 60 * 1000; // 10 mins
		public static final int ArticleList = 24 * 60 * 60 * 1000; // 24 hours
	}

	private Object syncLoadResources = new Object();
	private IBinder mBinder = new ResourcesDownloadBinder();
	private DownloadThread downloadThread;

	@Override
	public void onCreate() {
		super.onCreate();
		downloadThread = new DownloadThread();
		downloadThread.start();
	}

	@Override
	public int onStartCommand(final Intent intent, int flags, int startId) {
		new Thread(new Runnable() {

			public void run() {
				doOperation(intent);
			}
		}).start();

		return Service.START_NOT_STICKY;
	}

	public void doOperation(Intent intent) {
		log("Service starting");
		if (intent.getAction() != null) {
			log("service action: " + intent.getAction());
		}
		if (intent.getAction() != null
				&& intent.getAction().equals(Operation.GalleryImagesList)) {
			try {
				GameModel currentGame = GameManager.getInstance()
						.getCurrentGameSync();
				List<String> imagesList = loadImagesList(
						currentGame.getObjectId(), CacheTimeout.ImagesList);
				Intent result = new Intent("ResourceManagerResult");
				result.putExtra(OperationKeyword, Operation.GalleryImagesList);
				result.putExtra(Keys.Images.ImagesList,
						ListHelper.fromListToString(imagesList));
				sendBroadcast(result);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (intent.getAction() != null
				&& intent.getAction().equals(Operation.ArticlesList)) {
			try {
				loadArticlesList(CacheTimeout.ArticleList);
				Intent result = new Intent("ResourceManagerResult");
				result.putExtra(OperationKeyword, Operation.ArticlesList);
				sendBroadcast(result);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
		} else if (intent.getAction() != null
				&& intent.getAction().equals(Operation.LoadResources)) {
			synchronized (syncLoadResources) {
				try {
					GameModel currentGame = GameManager.getInstance()
							.getCurrentGameSync();
					List<String> imagesList = null;
					try {
						imagesList = loadImagesList(currentGame.getObjectId(),
								CacheTimeout.ImagesList);

					} catch (java.text.ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					try {
						List<ArticleModel> loadArticlesList = loadArticlesList(CacheTimeout.ArticleList);
						loadArticlesImages(loadArticlesList);
					} catch (StreamCorruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (java.text.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (imagesList != null)
						loadUsersImages(imagesList);
					loadTeams(currentGame);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private void loadTeams(GameModel game) {
		log("start load teams");
		List<PlayerModel> homePlayers = game.getHomeGameDetails().getLineup();
		for (PlayerModel playerModel : homePlayers) {
			String imageUrl = playerModel.getImageUrl();
			boolean isImageInCache = isImageInCache(imageUrl);
			if (!isImageInCache) {
				log("Starting download image of player: "
						+ playerModel.getFullName());
				downloadImage(imageUrl);
			}
		}

		List<PlayerModel> awayPlayers = game.getAwayGameDetails().getLineup();
		for (PlayerModel playerModel : awayPlayers) {
			String imageUrl = playerModel.getImageUrl();
			boolean isImageInCache = isImageInCache(imageUrl);
			if (!isImageInCache) {
				log("Starting download image of player: "
						+ playerModel.getFullName());
				downloadImage(imageUrl);
			}
		}
		log("end load teams");
	}

	private void loadUsersImages(List<String> imagesList) {
		log("start load user images");
		for (int i = 0; i < imagesList.size(); i++) {
			String imageUrl = imagesList.get(i);
			if (!isImageInCache(imageUrl)) {
				downloadImage(imageUrl);
			}
		}
		log("end load user images");
	}

	private void loadArticlesImages(List<ArticleModel> articles) {
		log("start load articles");
		for (ArticleModel articleModel : articles) {
			String imageUrl = articleModel.getImageUrl();
			if (imageUrl == null)
				continue;
			if (!isImageInCache(imageUrl)) {
				downloadImage(imageUrl);
			}
		}
		log("end load articles");
	}

	private BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<String>(150);

	private void downloadImage(String url) {
		try {
			int queueSize = 0;
			synchronized (blockingQueue) {
				blockingQueue.put(url);
				queueSize = blockingQueue.size();
			}

			log(String.format("add image url to queue: %s", url));
			log("queue size: " + queueSize);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class DownloadThread extends Thread {
		public void run() {
			log("DownloadThread is started");
			while (true) {
				try {
					String url = blockingQueue.take();
					int queueSize = 0;
					synchronized (blockingQueue) {
						queueSize = blockingQueue.size();
					}
					try {
						log("getting url from queue: " + url);
						log("queue size: " + queueSize);
						log("starting download url from web: " + url);
						Bitmap bitmap = ImagesHelper.downloadImageFromUrl(url);
						log("adding downloaded image to cache: " + url);
						BitmapDrawable drawable = ImagesHelper.convert(bitmap);
						if (drawable != null) {
							int imageSize = ImagesCache.getBitmapSize(drawable);
							log("image size: " + imageSize);
							ImagesCache.getInstance().addBitmapToCache(url,
									drawable, false);
							log("image added to cache seccussfully");
						}
						if (bitmap != null) {
							bitmap.recycle();
							bitmap = null;
						}
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						blockingQueue.add(url);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						blockingQueue.add(url);
					}
					catch (Exception e) {
						e.printStackTrace();
						blockingQueue.add(url);
					}

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}

	private boolean isImageInCache(String url) {
		boolean isContain = ImagesCache.getInstance()
				.getBitmapFromMemCache(url) != null;
		if (!isContain) {
			isContain = ImagesCache.getInstance().isKeyContainInDiskCache(url);
		}
		return isContain;
	}

	private Object downloadImagesListInBackgroundLock = new Object();
	private Object syncLoadImagesList = new Object();
	private static boolean downloadImagesListInProgress = false;

	@SuppressWarnings("unchecked")
	private List<String> loadImagesList(final String gameId, int timeOut)
			throws ParseException, java.text.ParseException {
		synchronized (syncLoadImagesList) {
			log("starting load images list");
			boolean isTimedOut = false;
			List<String> imagesList = null;
			imagesList = (List<String>) MemoryCache.getInstance().get(
					Keys.Images.ImagesList);
			if (imagesList != null) {
				Date memcacheTime = MemoryCache.getInstance().getTime(
						Keys.Images.ImagesList);
				isTimedOut = DatesHelper.isTimedOut(memcacheTime, timeOut);
				log(String
						.format("images list found in memory cache from date: %s isTimedOut: %s",
								DatesHelper.getDate(memcacheTime), isTimedOut));
			}

			// if images not in mem cache seek on disk cache
			if (imagesList == null) {
				log("images list not found in memcache");
				String imagesListLastTimeStr = DiskCache.getInstance().get(
						Keys.Images.ImagesListTime);
				if (imagesListLastTimeStr != null) {
					Date diskCacheTime = DatesHelper
							.getDate(imagesListLastTimeStr);
					isTimedOut = DatesHelper.isTimedOut(diskCacheTime, timeOut);
					String diskImageListStr = DiskCache.getInstance().get(
							Keys.Images.ImagesList);
					log(String
							.format("found images list in disk cache from date: %s, isTimedOut: %s",
									imagesListLastTimeStr, isTimedOut));
					if (diskImageListStr != null) {
						imagesList = ListHelper
								.fromStringToList(diskImageListStr);
						MemoryCache.getInstance().add(Keys.Images.ImagesList,
								imagesList);
						log("insert images list to memcahce");
					}
				}
			}

			if (imagesList == null) {
				log("can't find images list either memcahce or diskcache, start fetching images list from web");
				imagesList = downloadImagesListAndSaveInCache(gameId);
			} else if (isTimedOut) {
				boolean shouldDownload = false;
				synchronized (downloadImagesListInBackgroundLock) {
					if (!downloadImagesListInProgress) {
						shouldDownload = downloadImagesListInProgress = true;
					}
				}
				if (shouldDownload) {
					log("images list is timedout, starting fetch new version");
					new AsyncTask<Void, Void, Void>() {
						@Override
						protected Void doInBackground(Void... arg0) {
							try {
								downloadImagesListAndSaveInCache(gameId);
							} catch (ParseException e) {
								log("error occur while trying fetch images list in background "
										+ e.getMessage());
								// TODO Auto-generated catch block
								e.printStackTrace();
							} finally {
								downloadImagesListInProgress = false;
							}
							return null;
						}
					}.execute(null, null);
				} else {
					log("images list is timedout, but fetching process already started, no action");
				}
			}

			return imagesList;
		}
	}

	private List<String> downloadImagesListAndSaveInCache(String gameId)
			throws ParseException {
		log("start fetching images list");
		List<String> imagesList = UsersImagesManager.getInstance()
				.getImagesList(gameId);
		MemoryCache.getInstance().add(Keys.Images.ImagesList, imagesList);
		String imagesListString = ListHelper.fromListToString(imagesList);
		DiskCache.getInstance().add(Keys.Images.ImagesList, imagesListString);
		DiskCache.getInstance().add(Keys.Images.ImagesListTime,
				DatesHelper.getNowAsString());
		log("seccussfully fetch images list and add it to memcache and diskchache");
		return imagesList;
	}

	private Object syncLoadArticlesList = new Object();

	@SuppressWarnings("unchecked")
	private List<ArticleModel> loadArticlesList(int timeOut)
			throws ParseException, java.text.ParseException,
			StreamCorruptedException, IOException, ClassNotFoundException {
		synchronized (syncLoadArticlesList) {
			log("starting load articles List");
			List<ArticleModel> memcacheResult = (List<ArticleModel>) MemoryCache
					.getInstance().get(Keys.Articles.ArticleListKey, timeOut);
			if (memcacheResult != null) {
				log("articles list found in memory cache");
				return memcacheResult;
			}

			log("articles list not found in memcache, searching in disk cache");
			String articlesListLastTimeStr = DiskCache.getInstance().get(
					Keys.Articles.ArticleListTimeKey);
			if (articlesListLastTimeStr != null) {
				Date diskCacheTime = DatesHelper
						.getDate(articlesListLastTimeStr);
				boolean isTimedOut = DatesHelper.isTimedOut(diskCacheTime,
						timeOut);
				log(String
						.format("found articles list in disk cache from date: %s, isTimedOut: %s",
								articlesListLastTimeStr, isTimedOut));
				if (!isTimedOut) {
					byte[] diskArticlesListBytes = DiskCache.getInstance()
							.getBytes(Keys.Articles.ArticleListKey);
					if (diskArticlesListBytes != null) {
						List<ArticleModel> articlesList;
						articlesList = (List<ArticleModel>) SerializetionManager
								.deserialize(diskArticlesListBytes);
						MemoryCache.getInstance().add(
								Keys.Articles.ArticleListKey, articlesList);
						log("get articles list from diskcache and insert them to memcahce");
						return articlesList;
					}
				}
			} else {
				log("can't find articles list either memcahce or diskcache, start fetching articles list from web");
			}

			List<ArticleModel> articlesList = ArticlesManager.getInstance()
					.getArticles();
			log("get articles successfully");
			MemoryCache.getInstance().add(Keys.Articles.ArticleListKey,
					articlesList);
			log("articles was added to memcache");
			try {
				byte[] articlesBytes = SerializetionManager
						.seralize((Serializable) articlesList);
				DiskCache.getInstance().addBytes(Keys.Articles.ArticleListKey,
						articlesBytes);
				DiskCache.getInstance().add(Keys.Articles.ArticleListTimeKey,
						DatesHelper.getNowAsString());
				log("articles was added to disk cache");
			} catch (IOException e) {
				log("error occur while trying add articles list to disk cache");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return articlesList;
		}
	}

	private void log(String message) {
		Log.d(getClass().getSimpleName(), message);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

	public class ResourcesDownloadBinder extends Binder {
		public ResourcesDownloadService getService() {
			return ResourcesDownloadService.this;
		}
	}

}
