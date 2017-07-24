package cf.connota.sunrinton.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import cf.connota.sunrinton.Fragment.ExtraInfoFragment;
import cf.connota.sunrinton.Fragment.FriendsFragment;

/**
 * Created by Conota on 2017-07-25.
 */

public class PagerAdapter  extends FragmentStatePagerAdapter {
    private int _numOfTabs;

    public PagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this._numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                FriendsFragment tab1 = new FriendsFragment();
                return tab1;
            case 1:
                ExtraInfoFragment tab2 = new ExtraInfoFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return _numOfTabs;
    }
}