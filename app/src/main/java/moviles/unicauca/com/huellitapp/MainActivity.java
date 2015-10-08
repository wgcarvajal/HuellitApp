package moviles.unicauca.com.huellitapp;

import android.app.SearchManager;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SearchEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.List;

import moviles.unicauca.com.huellitapp.adapters.PagerAdapter;
import moviles.unicauca.com.huellitapp.fragments.MascotaFragment;
import moviles.unicauca.com.huellitapp.fragments.TitleFragment;
import moviles.unicauca.com.huellitapp.modelo.Mascota;
import moviles.unicauca.com.huellitapp.modelo.TipoMascota;


public class MainActivity extends AppCompatActivity implements DrawerLayout.DrawerListener, NavigationView.OnNavigationItemSelectedListener,MascotaFragment.OnItemSelected
{
    public static String POSITION="pos";

    private DrawerLayout drawer;
    private NavigationView nav;
    private ActionBarDrawerToggle toggle;
    private ViewPager pager;
    private List<TitleFragment> data;
    private PagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int pos;

        if(savedInstanceState!=null)
        {
            pos=savedInstanceState.getInt(POSITION);
        }
        else
        {
            pos=0;
        }

        data= new ArrayList<>();
        pager= (ViewPager)findViewById(R.id.pager);
        adapter = new PagerAdapter(getSupportFragmentManager(), data);
        pager.setAdapter(adapter);

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(TipoMascota.TABLA);
        query.addAscendingOrder(TipoMascota.TIPONOMBRE);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {

                if (e == null) {

                    for (ParseObject tipomascota : parseObjects) {

                        String idioma = getResources().getString(R.string.idiomaactual);
                        MascotaFragment mascotaFragment = new MascotaFragment();
                        if (idioma.equals("ingles")) {
                            mascotaFragment.init(tipomascota.getString(TipoMascota.TIPONOMBRE), tipomascota.getString(TipoMascota.TIPONOMBREINGLES));
                        } else {
                            mascotaFragment.init(tipomascota.getString(TipoMascota.TIPONOMBRE), tipomascota.getString(TipoMascota.TIPONOMBRE));
                        }
                        data.add(mascotaFragment);
                        adapter.notifyDataSetChanged();

                    }
                    pager.setCurrentItem(pos);
                }
            }
        });
        drawer=(DrawerLayout)findViewById(R.id.drawer);

        drawer.setDrawerListener(this);
        nav= (NavigationView)findViewById(R.id.nav);
        nav.setNavigationItemSelectedListener(this);
        toggle= new ActionBarDrawerToggle(this,drawer,R.string.open_nav,R.string.close_nav);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(toggle.onOptionsItemSelected(item))
        {
            return true;
        }
        else
        {
            if (item.getItemId() == R.id.action_search)
            {
                return onSearchRequested();
            }
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
        toggle.onDrawerSlide(drawerView, slideOffset);
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
        Intent intent;
        switch (menuItem.getItemId())
        {
            case R.id.nav_profile:
                intent = new Intent(this,PerfilActivity.class);
                startActivity(intent);
            break;
            case R.id.nav_sessionout:

                ParseUser.getCurrentUser().logOut();
                intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                finish();
            break;
            case R.id.nav_mypublications:
                intent = new Intent(this,MypublicationsActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        drawer.closeDrawers();
        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(POSITION,pager.getCurrentItem());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }


    private void handleIntent(Intent intent)
    {
        if (Intent.ACTION_SEARCH.equals(intent.getAction()))
        {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doSearch(query);
        }
    }

    private void doSearch(String queryStr)
    {
        ((MascotaFragment)adapter.getItem(pager.getCurrentItem())).buscar(queryStr);

    }


    @Override
    public void onItemSelected(Mascota mascota)
    {
        Intent intent = new Intent(this,PetActivity.class);
        intent.putExtra(PetActivity.IDMASCOTA,mascota.getId());
        intent.putExtra(PetActivity.NOMBREMASCOTA,mascota.getNombre());
        intent.putExtra(PetActivity.EDADMASCOTA,""+mascota.getEdad());
        intent.putExtra(PetActivity.TIPO,mascota.getTipo());
        intent.putExtra(PetActivity.DESCRIPCIONMASCOTA,mascota.getDescripcion());
        startActivity(intent);
        Log.i("id mascota seleccion:",""+mascota.getId());
    }
}
