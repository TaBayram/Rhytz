package tusba.rhytz.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import tusba.rhytz.HomeActivity;
import tusba.rhytz.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tabtitleHome,R.string.tabtitleSongs,R.string.tabtitlePlaylists,R.string.tabtitleGenres,R.string.tabtitleArtists,R.string.tabtitleAlbums};
    private final Context mContext;
    public ArrayList<Fragment> fragments = new ArrayList<>();

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        if(position==0) fragment = HomeFragment.newInstance((position));
        else if(position == 3) fragment = GenresFragment.newInstance(((HomeActivity)mContext).music);
        else fragment = SongsFragment.newInstance(((HomeActivity)mContext).music,position);
        fragments.add(fragment);
        return fragment;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return TAB_TITLES.length;
    }


}