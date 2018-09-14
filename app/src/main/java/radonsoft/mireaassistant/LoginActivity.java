package radonsoft.mireaassistant;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import radonsoft.mireaassistant.Helpers.Translit;
import radonsoft.mireaassistant.Network.APIWrapper;

public class LoginActivity extends AppCompatActivity implements APIWrapper.ScheduleListener {

    EditText groupEditText;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        groupEditText = findViewById(R.id.group_input2);
        groupEditText.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(20)});
        login = findViewById(R.id.button_login);
        APIWrapper apiWrapper = new APIWrapper(this, getApplicationContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View view = getWindow().getDecorView();
            view.setSystemUiVisibility(view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1){
                view.setSystemUiVisibility(view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                this.getWindow().setNavigationBarColor(getColor(R.color.colorPrimary));
            }
        } else {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlue));
        }

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
             apiWrapper.getScheduleOdd(result.toLowerCase(), 0);
             apiWrapper.getScheduleEven(result.toLowerCase(), 0);
             login.setText(getResources().getString(R.string.button_loading));
         }
        });
    }

    @Override
    public void completeDownload() {
        changeToMain();
    }

    @Override
    public void errorOdd(String e) {
        Toast.makeText(this, getResources().getText(R.string.error_msg), Toast.LENGTH_SHORT).show();
        login.setText(getResources().getString(R.string.button_next));
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void changeToMain() {
        Intent intent = new Intent();
        intent.putExtra("Group", groupEditText.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}
