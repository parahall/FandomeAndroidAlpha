package com.fandome.infra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fandome.application.ApplicationManager;

public class DiskCache {
	private static DiskCache instance = new DiskCache();
	private DictionaryOpenHelper dictionaryOpenHelper;

	private DiskCache() {
		dictionaryOpenHelper = new DictionaryOpenHelper(ApplicationManager.getAppContext());
	}

	public static DiskCache getInstance() {
		return instance;
	}
	
	public void addBytes(String key, byte[] value){
		dictionaryOpenHelper.addByteArray(key, value);
	}
	
	public void add(String key, String value){
		dictionaryOpenHelper.addString(key, value);
	}
	
	public String get(String key){
		return dictionaryOpenHelper.getString(key);
	}
	
	public byte[] getBytes(String key){
		return dictionaryOpenHelper.getBytes(key);
	}
	
	
	public class DictionaryOpenHelper extends SQLiteOpenHelper {

		private static final String DATABASE_NAME = "fandomesqllite";
	    private static final int DATABASE_VERSION = 1;
	    private static final String DICTIONARY_TABLE_NAME = "dictionary";
	    private static final String KEY_WORD = "key";
	    private static final String KEY_DEFINITION = "value";
	    private static final String DICTIONARY_TABLE_CREATE =
	                "CREATE TABLE " + DICTIONARY_TABLE_NAME + " (" +
	                KEY_WORD + " TEXT, " +
	                KEY_DEFINITION + " TEXT);";

	    DictionaryOpenHelper(Context context) {
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	    }

	    @Override
	    public void onCreate(SQLiteDatabase db) {
	        db.execSQL(DICTIONARY_TABLE_CREATE);
	    }

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
		}
		
		public void add(String key, byte[] value) {
		    SQLiteDatabase db = this.getWritableDatabase();
		 
		    ContentValues values = new ContentValues();
		    values.put(KEY_WORD, key); 
		    values.put(KEY_DEFINITION, value); 
		 
		    db.insert(DICTIONARY_TABLE_NAME, null, values);
		    db.close(); 
		}
		
		public void addString(String key, String value){
			SQLiteDatabase db = this.getWritableDatabase();
			 
		    ContentValues values = new ContentValues();
		    values.put(KEY_WORD, key); 
		    values.put(KEY_DEFINITION, value); 
		 
		    db.insert(DICTIONARY_TABLE_NAME, null, values);
		    db.close(); 
		}
		
		public void addByteArray(String key, byte[] value){
			SQLiteDatabase db = this.getWritableDatabase();
			 
		    ContentValues values = new ContentValues();
		    values.put(KEY_WORD, key); 
		    values.put(KEY_DEFINITION, value); 
		 
		    db.insert(DICTIONARY_TABLE_NAME, null, values);
		    db.close(); 
		}
		
		public String getString(String key) {
			try{
		    SQLiteDatabase db = this.getReadableDatabase();
		 
		    Cursor cursor = db.query(DICTIONARY_TABLE_NAME, new String[] { KEY_DEFINITION},
		    		KEY_WORD + "=?",
		            new String[] { key }, null, null, null, null);
		    if (cursor == null || cursor.getCount() == 0)
		    	return null;
	        
		    cursor.moveToFirst();
		    return cursor.getString(0);
			}
			catch(Exception e){
				return null;
			}
		}
		
		public byte[] getBytes(String key) {
		    SQLiteDatabase db = this.getReadableDatabase();
		 
		    Cursor cursor = db.query(DICTIONARY_TABLE_NAME, new String[] { KEY_DEFINITION},
		    		KEY_WORD + "=?",
		            new String[] { key }, null, null, null, null);
		    if (cursor == null || cursor.getCount() == 0)
		    	return null;
	        
		    cursor.moveToFirst();
		    return cursor.getBlob(0);
		}
	}
	
	
}
