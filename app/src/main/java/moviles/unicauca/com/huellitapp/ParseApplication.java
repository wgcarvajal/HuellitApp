package moviles.unicauca.com.huellitapp;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by geovanny on 29/09/15.
 */
public class ParseApplication extends Application
{
    public final String appId="G3OV6sOxPztKy1iGrgicpJBvyMjZQGLEGY7woUD0";
    public final String clientKey="HACeJ3CIuMxlMWyjrbJ6AJKvOFNRnkaNVNqqYj8U";



    @Override
    public void onCreate()
    {
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this,appId,clientKey);
    }
}
