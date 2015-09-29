package moviles.unicauca.com.huellitapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends AppCompatActivity implements DrawerLayout.DrawerListener, NavigationView.OnNavigationItemSelectedListener
{

    private DrawerLayout drawer;
    private NavigationView nav;

    private ActionBarDrawerToggle toggle;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences= getSharedPreferences(LoginActivity.PREFERENCE,MODE_PRIVATE);
        editor=preferences.edit();

        drawer=(DrawerLayout)findViewById(R.id.drawer);
        drawer.setDrawerListener(this);
        nav= (NavigationView)findViewById(R.id.nav);

        nav.setNavigationItemSelectedListener(this);

        toggle= new ActionBarDrawerToggle(this,drawer,R.string.open_nav,R.string.close_nav);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(toggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }


    @Override
    public void onDrawerSlide(View drawerView, float slideOffset)
    {
        toggle.onDrawerSlide(drawerView,slideOffset);
    }

    @Override
    public void onDrawerOpened(View drawerView)
    {
        toggle.onDrawerOpened(drawerView);
    }

    @Override
    public void onDrawerClosed(View drawerView)
    {
        toggle.onDrawerClosed(drawerView);

    }

    @Override
    public void onDrawerStateChanged(int newState)
    {
        toggle.onDrawerStateChanged(newState);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId())
        {
            case R.id.nav_sesionout:

                editor.putBoolean(LoginActivity.KEY_LOGIN, false);
                editor.commit();
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                finish();
            break;


        }
        drawer.closeDrawers();
        return false;
    }
}
