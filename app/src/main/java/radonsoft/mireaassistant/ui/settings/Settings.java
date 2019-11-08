package radonsoft.mireaassistant.ui.settings;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import radonsoft.mireaassistant.database.AppDatabase;
import radonsoft.mireaassistant.database.TableSchedule;
import radonsoft.mireaassistant.R;
import radonsoft.mireaassistant.ui.main.MainActivity;

import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Settings extends Fragment {

    View mRootView;

    @BindView(R.id.group_button)
    Button groupButton;

    @BindView(R.id.test_button3)
    Button test_button_2;

    @BindView(R.id.test_button4)
    Button test_button_3;

    @BindView(R.id.settings_about_message)
    TextView aboutView;

    @OnClick(R.id.button_feedback)
    void doFeedback(){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/r_vinter")));
    }

    @OnClick(R.id.button_refresh)
    void refresh(){
        ((MainActivity) getActivity()).updateSchedule();
    }

    @OnClick(R.id.group_button)
    void showDialog(){
        GroupDialogFragment dialogFragment = new GroupDialogFragment();
        dialogFragment.show(getFragmentManager(), "dialog");
    }

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
        ButterKnife.bind(this, mRootView);

        try {
            PackageInfo pInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
            aboutView.setText(getString(R.string.about_title, pInfo.versionName));
        } catch (PackageManager.NameNotFoundException e) {
            aboutView.setText(getString(R.string.about_title,""));
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        groupButton.setText(preferences.getString("GroupName", "null"));
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View f, Bundle savedInstanceState) {
        super.onViewCreated(f, savedInstanceState);

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
        groupButton.setText(group);
    }
}
