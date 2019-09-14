package radonsoft.mireaassistant.utils;

import android.appwidget.AppWidgetManager;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.support.v4.util.Pair;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import radonsoft.mireaassistant.R;
import radonsoft.mireaassistant.database.AppDatabase;
import radonsoft.mireaassistant.database.TableSchedule;

public class ScheduleWidgetDataProvider extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ScheduleWidgetItemFactory(getApplicationContext(), intent);
    }

    class ScheduleWidgetItemFactory implements RemoteViewsFactory {

        private Context mContext;
        List<TableSchedule> resource = new ArrayList<>();
        List<Pair<TableSchedule, Integer>> pairs = new ArrayList<>();
        private int appWidgetId;
        AppDatabase db;

        ScheduleWidgetItemFactory(Context context, Intent i) {
            mContext = context;
            appWidgetId = i.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
            db = Room.databaseBuilder(mContext, AppDatabase.class, "schedule")
                    .allowMainThreadQueries()
                    .build();
            updateDataSet();
        }

        @Override
        public void onDataSetChanged() {
            updateDataSet();
        }

        @Override
        public void onDestroy() {
            db.close();
        }

        @Override
        public int getCount() {
            return pairs.size();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews view = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
            String nameToSet;
            if (!pairs.get(i).first.getType().equals("")){
                nameToSet = pairs.get(i).first.getName() + ", " + pairs.get(i).first.getType();
            } else {
                nameToSet = pairs.get(i).first.getName();
            }
            view.setTextViewText(R.id.item_widget_name, nameToSet);
            view.setTextViewText(R.id.item_widget_professor, pairs.get(i).first.getTeacher());
            view.setTextViewText(R.id.item_widget_class, pairs.get(i).first.getClassroom());
            Pair<String, String> time = LessonTimeGenerator.getTiming(pairs.get(i).second);
            view.setTextViewText(R.id.item_widget_begin_time, time.first);
            view.setTextViewText(R.id.item_widget_end_time, time.second);
            return view;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        private void updateDataSet(){
            pairs.clear();
            resource = db.tableScheduleDAO().getListByWeektypeAndNumber(CalendarUtil.getWeekType(), CalendarUtil.getToday() * 6);
            for (int j = 0; j< resource.size(); j++){
                if (!resource.get(j).name.equals("―")) pairs.add(new Pair<>(resource.get(j), j));
            }
        }

    }
}
