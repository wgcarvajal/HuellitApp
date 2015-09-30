package moviles.unicauca.com.huellitapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import moviles.unicauca.com.huellitapp.adapters.PagerAdapter;
import moviles.unicauca.com.huellitapp.fragments.MascotaFragment;
import moviles.unicauca.com.huellitapp.fragments.TitleFragment;


public class MainActivity extends AppCompatActivity implements DrawerLayout.DrawerListener, NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    private NavigationView nav;

    private ActionBarDrawerToggle toggle;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private ViewPager pager;
    private List<TitleFragment> data;

    private PagerAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        pager= (ViewPager)findViewById(R.id.pager);


        data= new ArrayList<>();


        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("tipomascota");
        query.addAscendingOrder("tiponombre");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {


                if (e == null) {
                    for (ParseObject tipomascota : parseObjects)
                    {
                        MascotaFragment mascotaFragment= new MascotaFragment();
                        mascotaFragment.init(tipomascota.getString("tiponombre"));
                        data.add(mascotaFragment);

                    }

                    adapter=new PagerAdapter(getSupportFragmentManager(),data);
                    pager.setAdapter(adapter);

                }
            }
        });

        /*ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();*/

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
