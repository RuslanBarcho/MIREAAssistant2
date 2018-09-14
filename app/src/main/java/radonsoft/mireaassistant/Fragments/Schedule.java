package radonsoft.mireaassistant.Fragments;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;
import radonsoft.mireaassistant.Database.AppDatabase;
import radonsoft.mireaassistant.Helpers.RecyclerViewAdapter;
import radonsoft.mireaassistant.R;

import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Schedule extends Fragment {

    int currentDay = 0;
    int weekType = 0;
    View mRootView;
    RadioRealButtonGroup days;
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    TextView textView;
    Button weekButton;
    AppDatabase db;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    String[] daysArray = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота"};

    public Schedule() { }

    public interface updateSchedule {
        void updateSchedule();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = Room.databaseBuilder(getContext(), AppDatabase.class, "schedule")
                .allowMainThreadQueries()
                .build();
        daysArray = getResources().getStringArray(R.array.schedule_days);
        if (savedInstanceState!= null){
            weekType = savedInstanceState.getInt("weekType");
            currentDay = savedInstanceState.getInt("currentDay");
        } else {
            getWeekNumber();
            currentDay = getToday();
        }
        configureAdapter();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_schedule, container, false);

        // Configure view elements
        days = mRootView.findViewById(R.id.days);
        weekButton = mRootView.findViewById(R.id.week_button);
        textView = mRootView.findViewById(R.id.dayView);
        days.setPosition(currentDay);
        configureWeekButton();

        //setting up recyclerView with database
        recyclerView = mRootView.findViewById(R.id.recycler);
        if (adapter != null){
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
            textView.setText(daysArray[currentDay]);
        }

        days.setOnPositionChangedListener((button, position, t) -> {
            updateRecycler(position, db);
        });

        weekButton.setOnClickListener(v -> {
            if (weekType == 1){
                weekType = 0;
            } else {
                weekType = 1;
            }
            configureWeekButton();
            updateRecycler(currentDay, db);
        });
        mSwipeRefreshLayout = mRootView.findViewById(R.id.refresh);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3
        );
        //code for refresh widget
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            ((updateSchedule) getActivity()).updateSchedule();
        });

        return mRootView;
    }

    public void updateRecycler(int position, AppDatabase db){
        currentDay = position;
        adapter.updateData(db.tableScheduleDAO().getListByWeektypeAndNumber(weekType, currentDay * 6));
        adapter.notifyDataSetChanged();
        textView.setText(daysArray[position]);
    }

    public void updateRecycler(AppDatabase db){
        adapter.updateData(db.tableScheduleDAO().getListByWeektypeAndNumber(weekType, currentDay * 6));
        adapter.notifyDataSetChanged();
    }

    private void getWeekNumber() {
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(Calendar.DATE, 0);
        int weekNumber = gc.get(Calendar.WEEK_OF_YEAR) - 1;
        Calendar calendar = Calendar.getInstance();
        int today = (calendar.get(Calendar.DAY_OF_WEEK));
        if (today == 1){
            weekNumber ++;
        }
        if (weekNumber % 2 == 0){
            weekType = 0;
        } else {
            weekType = 1;
        }
    }

    public int getToday(){
        Calendar calendar = Calendar.getInstance();
        int today = (calendar.get(Calendar.DAY_OF_WEEK)) - 2;
        if (today == -1){
            today = 0;
        }
        return today;
    }

    private void configureAdapter(){
        adapter = new RecyclerViewAdapter(db.tableScheduleDAO().getListByWeektypeAndNumber(weekType, currentDay * 6));
    }

    private void configureWeekButton(){
        if (weekType == 1){
            weekButton.setText("Нечетная");
        } else {
            weekButton.setText("Четная");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        //saving current fragment instance
        super.onSaveInstanceState(outState);
        outState.putInt("currentDay", currentDay);
        outState.putInt("weekType", weekType);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            updateRecycler(currentDay, db);
        }
    }
}
