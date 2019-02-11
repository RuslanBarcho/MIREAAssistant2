package radonsoft.mireaassistant.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import radonsoft.mireaassistant.R;
import radonsoft.mireaassistant.ui.schedule.SchedulePageFragment;

public class ScheduleViewPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<SchedulePageFragment> fragments;
    private Context context;

    public ScheduleViewPagerAdapter(FragmentManager fm, ArrayList<SchedulePageFragment> fragments, Context context){
        super(fm);
        this.context = context;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getResources().getString(R.string.monday_cut);
            case 1:
                return context.getResources().getString(R.string.tuesday_cut);
            case 2:
                return context.getResources().getString(R.string.wednesday_cut);
            case 3:
                return context.getResources().getString(R.string.thursday_cut);
            case 4:
                return context.getResources().getString(R.string.friday_cut);
            case 5:
                return context.getResources().getString(R.string.saturday_cut);
            default:
                return "";
        }
    }
}
