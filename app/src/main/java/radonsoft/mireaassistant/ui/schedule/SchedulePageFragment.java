package radonsoft.mireaassistant.ui.schedule;


import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import radonsoft.mireaassistant.database.AppDatabase;
import radonsoft.mireaassistant.utils.RecyclerViewAdapter;
import radonsoft.mireaassistant.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SchedulePageFragment extends Fragment {

    @BindView(R.id.scheduleRecycler)
    RecyclerView recyclerView;

    RecyclerViewAdapter adapter;
    int day;
    int weekType = 0;
    View mRootView;
    AppDatabase db;

    public SchedulePageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        day = getArguments().getInt("day", 0);
        if (savedInstanceState != null) {
            weekType = savedInstanceState.getInt("weekType", 0);
        } else {
            weekType = getArguments().getInt("weekType", 0);
        }

        db = Room.databaseBuilder(getActivity().getApplicationContext(), AppDatabase.class, "schedule")
                .allowMainThreadQueries()
                .build();

        mRootView = inflater.inflate(R.layout.fragment_schedule_page, container, false);
        ButterKnife.bind(this, mRootView);
        adapter = new RecyclerViewAdapter(db.tableScheduleDAO().getListByWeektypeAndNumber(weekType, day * 6));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return mRootView;
    }

    public void updateRecyclerView(int weekType){
        this.weekType = weekType;
        if (db != null){
            adapter.updateData(db.tableScheduleDAO().getListByWeektypeAndNumber(weekType, day * 6));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("weekType", weekType);
        db.close();
    }

}
