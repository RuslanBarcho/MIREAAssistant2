package radonsoft.mireaassistant.utils;

import android.app.Activity;
import android.os.Build;
import android.view.View;

import radonsoft.mireaassistant.R;

public class StyleApplicator {
    public static void style(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View view = activity.getWindow().getDecorView();
            view.setSystemUiVisibility(view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
                view.setSystemUiVisibility(view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                activity.getWindow().setNavigationBarColor(activity.getColor(R.color.colorPrimary));
            }
        } else {
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.colorBlue));
        }
    }
}
