package radonsoft.mireaassistant.ui.main;


import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import radonsoft.mireaassistant.R;
import radonsoft.mireaassistant.ui.login.LoginActivity;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if (preferences.getString("GroupName", "null").equals("null")) {
            Intent i = new Intent(this, LoginActivity.class);
            this.startActivity(i);
            this.finish();
        } else {
            Intent i = new Intent(this, MainActivity.class);
            this.startActivity(i);
            this.finish();
        }

        setContentView(R.layout.activity_launch);
    }
}
