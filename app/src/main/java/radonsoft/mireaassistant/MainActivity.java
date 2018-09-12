package radonsoft.mireaassistant;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import radonsoft.mireaassistant.Database.AppDatabase;
import radonsoft.mireaassistant.Fragments.GroupDialogFragment;
import radonsoft.mireaassistant.Fragments.Schedule;
import radonsoft.mireaassistant.Fragments.Settings;
import radonsoft.mireaassistant.Helpers.Translit;
import radonsoft.mireaassistant.Network.APIWrapper;

public class MainActivity extends AppCompatActivity implements GroupDialogFragment.GroupDialogListener, APIWrapper.ScheduleListener {

    SharedPreferences sharedPreferences;
    FragmentManager fragmentManager;
    LinearLayout linearLayout;
    BottomNavigationView navigation;
    AppDatabase db;
    String groupName;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
        linearLayout = findViewById(R.id.container_content);
        switch (item.getItemId()) {
            case R.id.navigation_schedule:
                if(fragmentManager.findFragmentByTag("one") != null) {
                    //if the fragment exists, show it.
                    fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("one")).commit();
                }
                if(fragmentManager.findFragmentByTag("two") != null){
                    //if the other fragment is visible, hide it.
                    fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("two")).commit();
                }
                break;
                case R.id.navigation_settings:
                    if(fragmentManager.findFragmentByTag("two") != null) {
                        //if the fragment exists, show it.
                        fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("two")).commit();
                    }
                    if(fragmentManager.findFragmentByTag("one") != null){
                        //if the other fragment is visible, hide it.
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("one")).commit();
                    }
                    break;
        }
        return true;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "schedule")
                .allowMainThreadQueries()
                .build();

        //check if db is empty and run login activity if needed
        if (db.tableScheduleDAO().getAllSchedule().isEmpty()){
            changeActivity();
        } else {
            groupName = getGroupName();
        }

        linearLayout = findViewById(R.id.container_content);
        fragmentManager = getSupportFragmentManager();
        if(fragmentManager.findFragmentByTag("one") == null) {
            fragmentManager.beginTransaction().add(R.id.container_content, new Schedule(), "one").commit();
        }
        if(fragmentManager.findFragmentByTag("two") == null) {
            fragmentManager.beginTransaction().add(R.id.container_content, new Settings(), "two").commit();
        }

        setContentView(R.layout.activity_main);
        navigation = findViewById(R.id.navigation);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View view = getWindow().getDecorView();
            view.setSystemUiVisibility(view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1){
                view.setSystemUiVisibility(view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                this.getWindow().setNavigationBarColor(getColor(R.color.colorPrimary));
            }
        }

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null){
            navigation.setSelectedItemId(R.id.navigation_schedule);
        } else {
            navigation.setSelectedItemId(savedInstanceState.getInt("currentTab"));
        }
    }

    private void changeActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            Schedule schedule = (Schedule) fragmentManager.findFragmentByTag("one");
            schedule.updateRecycler(db);
            groupName = data.getStringExtra("Group");
            saveGroupName(groupName);
            setGroupName();
        }
    }

    @Override
    public void onGroupSelected(String groupName) {
        Settings settings = (Settings) fragmentManager.findFragmentByTag("two");
        settings.testInterface(groupName);
        Translit translit = new Translit();
        String result = translit.cyr2lat(groupName);

        APIWrapper apiWrapper = new APIWrapper(this, getApplicationContext());

        apiWrapper.getScheduleOdd(result.toLowerCase(), 0);
        apiWrapper.getScheduleEven(result.toLowerCase(), 0);

        this.groupName = groupName;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        //saving current fragment instance
        super.onSaveInstanceState(outState);
        outState.putInt("currentTab", navigation.getSelectedItemId());
        outState.putString("grName", groupName);
    }

    @Override
    public void completeDownload() {
        Schedule schedule = (Schedule) fragmentManager.findFragmentByTag("one");
        schedule.updateRecycler(db);
        setGroupName();
        saveGroupName(groupName);
    }

    @Override
    public void errorOdd(String e) {
        groupName = getGroupName();
    }

    private void saveGroupName(String name){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("GroupName", name).apply();
    }

    private void setGroupName(){
        Settings settings = (Settings) fragmentManager.findFragmentByTag("two");
        if (settings != null){
            settings.setGroup(groupName);
        }
    }

    private String getGroupName(){
        return sharedPreferences.getString("GroupName", "null");
    }
}
