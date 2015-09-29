package moviles.unicauca.com.huellitapp;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArraySet;
import android.util.Log;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class RootActivity extends AppCompatActivity
{


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences= getSharedPreferences(LoginActivity.PREFERENCE,MODE_PRIVATE);
        boolean loging= preferences.getBoolean(LoginActivity.KEY_LOGIN, false);

        Intent intent= null;

        if(loging)
        {

            intent=new Intent(this,MainActivity.class);
        }
        else
        {
            intent=new Intent(this,LoginActivity.class);
        }

        startActivity(intent);

    }


}
