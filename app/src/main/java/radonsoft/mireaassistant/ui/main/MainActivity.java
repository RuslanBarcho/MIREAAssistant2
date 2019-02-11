package radonsoft.mireaassistant.ui.main;


import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Objects;

import radonsoft.mireaassistant.database.AppDatabase;
import radonsoft.mireaassistant.network.forms.ScheduleForm;
import radonsoft.mireaassistant.ui.settings.GroupDialogFragment;
import radonsoft.mireaassistant.ui.schedule.Schedule;
import radonsoft.mireaassistant.ui.settings.Settings;
import radonsoft.mireaassistant.utils.Translit;
import radonsoft.mireaassistant.R;
import radonsoft.mireaassistant.utils.StyleApplicator;

public class MainActivity extends AppCompatActivity implements GroupDialogFragment.GroupDialogListener, Schedule.updateSchedule {

    SharedPreferences sharedPreferences;
    FragmentManager fragmentManager;
    BottomNavigationView navigation;
    MainViewModel viewModel;
    AppDatabase db;
    String groupName;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_schedule:
                fragmentManager.beginTransaction()
                        .show(Objects.requireNonNull(fragmentManager.findFragmentByTag("one")))
                        .hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("two")))
                        .commit();
                break;

            case R.id.navigation_settings:
                fragmentManager.beginTransaction()
                        .show(Objects.requireNonNull(fragmentManager.findFragmentByTag("two")))
                        .hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("one")))
                        .commit();
                break;
        }
        return true;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "schedule")
                .allowMainThreadQueries()
                .build();

        groupName = getGroupName();

        fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag("one") == null & fragmentManager.findFragmentByTag("two") == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.container_content, new Schedule(), "one")
                    .add(R.id.container_content, new Settings(), "two")
                    .commit();
            fragmentManager.popBackStackImmediate();
        }

        setContentView(R.layout.activity_main);
        StyleApplicator.style(this);

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (savedInstanceState == null) {
            navigation.setSelectedItemId(R.id.navigation_schedule);
        } else {
            navigation.setSelectedItemId(savedInstanceState.getInt("currentTab"));
        }

        if (savedInstanceState == null) downloadSchedule(sharedPreferences.getString("GroupName", ""));
        viewModel.data.observe(this, data -> {
            if (data != null){
                completeDownload();
                viewModel.data.postValue(null);
            }
        });
        viewModel.error.observe(this, this::errorOdd);
    }

    @Override
    public void onGroupSelected(String groupName) {
        downloadSchedule(groupName);
    }

    @Override
    public void updateSchedule() {
        downloadSchedule(groupName);
    }

    private void downloadSchedule(String groupName) {
        Translit translit = new Translit();
        String result = translit.cyr2lat(groupName);
        viewModel.getSchedule(new ScheduleForm(0, 0, result.toLowerCase()), this);
        this.groupName = groupName;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        //saving current activity instance
        super.onSaveInstanceState(outState);
        outState.putInt("currentTab", navigation.getSelectedItemId());
        outState.putString("grName", groupName);
    }

    public void completeDownload() {
        try {
            Schedule schedule = (Schedule) fragmentManager.findFragmentByTag("one");
            schedule.updateRecycler();
            saveGroupName(groupName);
            setGroupName();
            Toast.makeText(this, getResources().getText(R.string.downloaded_msg), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("ERROR", e.toString());
        }
    }

    public void errorOdd(String error) {
        if (error != null){
            groupName = getGroupName();
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            viewModel.error.postValue(null);
        }
    }

    private void saveGroupName(String name) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("GroupName", name).apply();
    }

    private void setGroupName() {
        Settings settings = (Settings) fragmentManager.findFragmentByTag("two");
        if (settings != null) {
            settings.setGroup(groupName);
        }
    }

    private String getGroupName() {
        return sharedPreferences.getString("GroupName", "null");
    }
}
