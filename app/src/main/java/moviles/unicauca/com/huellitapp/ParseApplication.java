package moviles.unicauca.com.huellitapp;

import android.app.Application;


import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;


/**
 * Created by geovanny on 29/09/15.
 */
public class ParseApplication extends Application
{
    public final String APPID="G3OV6sOxPztKy1iGrgicpJBvyMjZQGLEGY7woUD0";
    public final String CLIENTKEY="HACeJ3CIuMxlMWyjrbJ6AJKvOFNRnkaNVNqqYj8U";

    @Override
    public void onCreate()
    {
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, APPID, CLIENTKEY);
        ParseFacebookUtils.initialize(this);
        FacebookSdk.sdkInitialize(getApplicationContext());


    }
}
