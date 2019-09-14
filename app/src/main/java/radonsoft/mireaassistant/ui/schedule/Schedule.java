package radonsoft.mireaassistant.ui.schedule;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import radonsoft.mireaassistant.R;
import radonsoft.mireaassistant.ui.main.MainActivity;
import radonsoft.mireaassistant.utils.CalendarUtil;
import radonsoft.mireaassistant.utils.ScheduleViewPagerAdapter;

import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

public class Schedule extends Fragment {

    int currentDay = 0;
    int weekType = 0;
    View mRootView;
    private ArrayList<SchedulePageFragment> fragments;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    private ScheduleViewPagerAdapter viewPagerAdapter;
    String[] daysArray = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота"};

    @BindView(R.id.scheduleViewPager)
    ViewPager viewPager;

    @BindView(R.id.scheduleTabLayout)
    TabLayout tabLayout;

    @BindView(R.id.dayView)
    TextView textView;

    @BindView(R.id.week_button)
    Button weekButton;

    @OnClick(R.id.week_button)
    void clickWeekButton(){
        if (weekType == 1){
            weekType = 0;
        } else {
            weekType = 1;
        }
        configureWeekButton();
        updateRecycler();
    }

    @OnLongClick(R.id.week_button)
    boolean longClickWeekButton(){
        int temp = weekType;
        weekType = CalendarUtil.getWeekType();
        if(temp != weekType){
            configureWeekButton();
        }
        updateRecycler();
        return true;
    }

    public Schedule() { }

    public interface updateSchedule {
        void updateSchedule();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        daysArray = getResources().getStringArray(R.array.schedule_days);
        if (savedInstanceState!= null){
            weekType = savedInstanceState.getInt("weekType");
            currentDay = savedInstanceState.getInt("currentDay");
        } else {
            weekType = CalendarUtil.getWeekType();
            currentDay = CalendarUtil.getToday();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_schedule, container, false);
        ButterKnife.bind(this, mRootView);
        configureWeekButton();

        fragments = new ArrayList<>();
        for (int i = 0; i<6; i++){
            Bundle bundle = new Bundle();
            bundle.putInt("day", i);
            bundle.putInt("weekType", weekType);
            SchedulePageFragment fragment = new SchedulePageFragment();
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }

        viewPagerAdapter = new ScheduleViewPagerAdapter(getChildFragmentManager(), fragments, getContext());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(6);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentDay = tab.getPosition();
                textView.setText(daysArray[tab.getPosition()]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        Objects.requireNonNull(tabLayout.getTabAt(currentDay)).select();

        //code for refresh widget
        mSwipeRefreshLayout = mRootView.findViewById(R.id.refresh);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3
        );
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            ((MainActivity) getActivity()).updateSchedule();
        });
        mSwipeRefreshLayout.setEnabled(false);

        return mRootView;
    }

    public void updateRecycler(){
        for (int i = 0; i< 6; i++){
            ((SchedulePageFragment) getChildFragmentManager().getFragments().get(i)).updateRecyclerView(weekType);
        }
    }

    private void configureWeekButton(){
        if (weekType == 1){
            weekButton.setText(getResources().getString(R.string.week_odd));
        } else {
            weekButton.setText(getResources().getString(R.string.week_even));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentDay", currentDay);
        outState.putInt("weekType", weekType);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){

        }
    }
}
