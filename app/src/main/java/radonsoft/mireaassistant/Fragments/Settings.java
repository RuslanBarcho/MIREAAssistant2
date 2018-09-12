package radonsoft.mireaassistant.Fragments;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import radonsoft.mireaassistant.Database.AppDatabase;
import radonsoft.mireaassistant.Database.TableSchedule;
import radonsoft.mireaassistant.R;

import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class Settings extends Fragment {

    View mRootView;
    Button test_button;
    Button test_button_2;
    Button test_button_3;

    public Settings() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mRootView = inflater.inflate(R.layout.fragment_settings, container, false);

        test_button = mRootView.findViewById(R.id.test_button);
        test_button_3 = mRootView.findViewById(R.id.test_button4);
        test_button_2 = mRootView.findViewById(R.id.test_button3);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        test_button.setText(preferences.getString("GroupName", "null"));
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View f, Bundle savedInstanceState) {
        super.onViewCreated(f, savedInstanceState);
        test_button.setOnClickListener(v -> {
            GroupDialogFragment dialogFragment = new GroupDialogFragment();
            dialogFragment.show(getFragmentManager(), "dialog");
        });

        test_button_2.setOnClickListener(v -> {
            AppDatabase db;
            db = Room.databaseBuilder(getContext(), AppDatabase.class, "schedule")
                    .allowMainThreadQueries()
                    .build();
            db.tableScheduleDAO().nukeTable();
        });

        test_button_3.setOnClickListener(v -> {
            AppDatabase db;
            db = Room.databaseBuilder(getContext(), AppDatabase.class, "schedule")
                    .allowMainThreadQueries()
                    .build();
            List<TableSchedule> table = db.tableScheduleDAO().getAllSchedule();

            for(TableSchedule obj: table){
                Log.i("TEST", obj.name + String.valueOf(obj.number));
            }
            Toast.makeText(getContext(), "ELEMENTS IN DB: " + String.valueOf(table.size()), Toast.LENGTH_SHORT).show();
        });
    }

    public void setGroup(String group){
        test_button.setText(group);
    }

    public void testInterface(String name){

    }
}
