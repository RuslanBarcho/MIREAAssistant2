package radonsoft.mireaassistant.ui.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import radonsoft.mireaassistant.R;
import radonsoft.mireaassistant.utils.CalendarUtil;
import radonsoft.mireaassistant.utils.ScheduleWidgetDataProvider;

/**
 * Implementation of App Widget functionality.
 */
public class ScheduleWidget extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String[] days = context.getResources().getStringArray(R.array.schedule_days);
        super.onReceive(context, intent);

        if (intent.getAction().equals("android.appwidget.action.APPWIDGET_UPDATE")){
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.schedule_widget);
            Intent serviceIntent = new Intent(context, ScheduleWidgetDataProvider.class);
            views.setTextViewText(R.id.widgetDayTextView, days[CalendarUtil.getToday()]);
            views.setRemoteAdapter(R.id.scheduleListView, serviceIntent);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        String[] days = context.getResources().getStringArray(R.array.schedule_days);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.schedule_widget);
        Intent serviceIntent = new Intent(context, ScheduleWidgetDataProvider.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        views.setTextViewText(R.id.widgetDayTextView, days[CalendarUtil.getToday()]);
        views.setRemoteAdapter(R.id.scheduleListView, serviceIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.scheduleListView);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            //Toast.makeText(context, "Widget UPDATED! ", Toast.LENGTH_LONG).show();
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {

    }

}

