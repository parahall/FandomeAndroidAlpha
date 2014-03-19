package com.fandome.application;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import com.fandome.infra.Keys;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

@ReportsCrashes(formKey = "dEZUYkVseDIwMHdyMThRSnNpXzMxSWc6MQ") 
public class ApplicationManager extends Application {
	private static Context context;
	public static Activity currentActivity;
	
	private String YOUR_APPLICATION_ID = "C9G1achra9XUFYQXRskvDaB3amqbRDrMCdZ4NmM6";
	private String YOUR_CLIENT_KEY = "lMqlbNLBMkVVrMFTGzRh3lJO33MJfTDrOJYUihTg";
	private String YOUR_FACEBOOK_APP_ID = "147805498719553";

    public void onCreate(){
        super.onCreate();
        ApplicationManager.context = getApplicationContext();
        
      	//Add your initialization code here
      	Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);
      	ParseFacebookUtils.initialize(YOUR_FACEBOOK_APP_ID);


 		ParseUser.enableAutomaticUser();
 		ParseACL defaultACL = new ParseACL();
 	    
 		// If you would like all objects to be private by default, remove this line.
 		defaultACL.setPublicReadAccess(true);
 		
 		ParseACL.setDefaultACL(defaultACL, true);
 		
 		ACRA.init(this);
 		
 		Intent applicationLoaded = new Intent(Keys.Actions.ApplicationLoaded);
 		sendBroadcast(applicationLoaded);
 		
    }

    public static Context getAppContext() {
        return ApplicationManager.context;
    }
    
    public static Resources getResourceManager(){
    	return getAppContext().getResources();
    }
}
