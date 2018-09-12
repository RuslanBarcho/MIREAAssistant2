package radonsoft.mireaassistant.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.security.Key;

import radonsoft.mireaassistant.R;

public class GroupDialogFragment extends DialogFragment implements Button.OnClickListener {

    public Dialog dialog;
    InputMethodManager imm;
    Button downloadButton;
    EditText editText;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_download_dialog){
            hideKeyboard();
            this.dismiss();
            ((GroupDialogListener) getActivity()).onGroupSelected(editText.getText().toString());
        }
    }

    public interface GroupDialogListener {
        void onGroupSelected(String groupName);
    }

    public GroupDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_group_dialog, container, false);
        //configure editText and show keyboard
        editText = view.findViewById(R.id.group_input);
        editText.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(20)});
        editText.requestFocus();
        imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                editText.setSelection(editText.getText().length());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count == 4 & before == 3){
                    editText.setText(editText.getText() + "-");
                } else if (count == 4 & before == 5){
                    editText.setText(editText.getText().subSequence(0, count - 1));
                } else if (count == 7 & before == 6){
                    editText.setText(editText.getText() + "-");
                } else if (count == 7 & before == 8) {
                    editText.setText(editText.getText().subSequence(0, count - 1));
                }
            }
        });
        downloadButton = view.findViewById(R.id.button_download_dialog);
        downloadButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState){
        super.onViewCreated(v, savedInstanceState);
        //imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        dialog = super.onCreateDialog(savedInstanceState);
        dialog.setContentView(R.layout.fragment_group_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                hideKeyboard();
                return false;
            }
            return false;
        });
        return dialog;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        Log.i("Dialog", "Cancelled");
        hideKeyboard();
        super.onCancel(dialog);
    }

    private void hideKeyboard(){
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}
