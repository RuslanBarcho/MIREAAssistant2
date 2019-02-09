package radonsoft.mireaassistant.ui.login;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import radonsoft.mireaassistant.utils.Translit;
import radonsoft.mireaassistant.network.forms.ScheduleForm;
import radonsoft.mireaassistant.R;
import radonsoft.mireaassistant.ui.main.MainActivity;
import radonsoft.mireaassistant.ui.main.MainViewModel;
import radonsoft.mireaassistant.utils.StyleApplicator;

public class LoginActivity extends AppCompatActivity {

    EditText groupEditText;
    Button login;
    MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        groupEditText = findViewById(R.id.group_input2);
        groupEditText.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(20)});
        login = findViewById(R.id.button_login);

        StyleApplicator.style(this);

        groupEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count == 4 & before == 3){
                    groupEditText.append("-");
                } else if (count == 4 & before == 5){
                    int length = groupEditText.getText().length();
                    if (length > 0) groupEditText.getText().delete(length - 1, length);
                } else if (count == 7 & before == 6){
                    groupEditText.append("-");
                } else if (count == 7 & before == 8) {
                    int length = groupEditText.getText().length();
                    if (length > 0) groupEditText.getText().delete(length - 1, length);
                }
            }
        });

        login.setOnClickListener(v -> {
         if (groupEditText.getText().toString().length() == 10){
             Translit translit = new Translit();
             String result = translit.cyr2lat(groupEditText.getText().toString());
             viewModel.getSchedule(new ScheduleForm(0, 0, result.toLowerCase()), this);
             login.setText(getResources().getString(R.string.button_loading));
             groupEditText.setEnabled(false);
         }
        });

        viewModel.data.observe(this, data -> {
            if (data != null){
                if (data){
                    changeToMain();
                    viewModel.data.postValue(null);
                }
            }
        });
    }

    public void changeToMain() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        preferences.edit().putString("GroupName", groupEditText.getText().toString()).apply();
        Intent i = new Intent(this, MainActivity.class);
        this.startActivity(i);
        this.finish();
    }
}
