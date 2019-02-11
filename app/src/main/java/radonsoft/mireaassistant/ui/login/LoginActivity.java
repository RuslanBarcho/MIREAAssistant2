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
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import radonsoft.mireaassistant.utils.Translit;
import radonsoft.mireaassistant.network.forms.ScheduleForm;
import radonsoft.mireaassistant.R;
import radonsoft.mireaassistant.ui.main.MainActivity;
import radonsoft.mireaassistant.ui.main.MainViewModel;
import radonsoft.mireaassistant.utils.StyleApplicator;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.group_input2)
    EditText groupEditText;

    @BindView(R.id.button_login)
    Button login;

    @OnClick(R.id.button_login)
    void login(){
        if (groupEditText.getText().toString().length() >= 10){
            Translit translit = new Translit();
            String result = translit.cyr2lat(groupEditText.getText().toString());
            viewModel.getSchedule(new ScheduleForm(0, 0, result.toLowerCase()), this);
            login.setText(getResources().getString(R.string.button_loading));
            groupEditText.setEnabled(false);
            login.setEnabled(false);
        }
    }

    MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StyleApplicator.style(this);
        ButterKnife.bind(this);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        groupEditText.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(20)});

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

        viewModel.data.observe(this, data -> {
            if (data != null){
                if (data){
                    changeToMain();
                    viewModel.data.postValue(null);
                }
            }
        });

        viewModel.error.observe(this, error -> {
            if (error != null){
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                groupEditText.setEnabled(true);
                login.setEnabled(true);
                login.setText(getString(R.string.button_next));
                viewModel.error.postValue(null);
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
