package radonsoft.mireaassistant.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import radonsoft.mireaassistant.ui.schedule.SchedulePageFragment;

public class ScheduleViewPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<SchedulePageFragment> fragments;

    public ScheduleViewPagerAdapter(FragmentManager fm, ArrayList<SchedulePageFragment> fragments){
        super(fm);
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
                return "ПН";
            case 1:
                return "ВТ";
            case 2:
                return "СР";
            case 3:
                return "ЧТ";
            case 4:
                return "ПТ";
            case 5:
                return "СБ";
            default:
                return null;
        }
    }
}
