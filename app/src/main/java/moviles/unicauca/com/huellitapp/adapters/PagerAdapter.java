package moviles.unicauca.com.huellitapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import java.util.List;


import moviles.unicauca.com.huellitapp.fragments.TitleFragment;

/**
 * Created by geovanny on 18/09/15.
 */
public class PagerAdapter extends FragmentStatePagerAdapter
{
    List<TitleFragment> data;

    public PagerAdapter(FragmentManager fm, List<TitleFragment> data)
    {
        super(fm);
        this.data=data;
    }

    public void init(int position,TitleFragment fragment)
    {
        data.set(position,fragment);
    }

    @Override
    public Fragment getItem(int position)
    {
        return data.get(position);
    }

    @Override
    public int getCount()
    {
        return data.size();
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return data.get(position).getTitle();
    }
}
