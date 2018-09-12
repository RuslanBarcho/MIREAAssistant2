package radonsoft.mireaassistant.Network;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import radonsoft.mireaassistant.Database.AppDatabase;
import radonsoft.mireaassistant.Database.TableSchedule;
import radonsoft.mireaassistant.LoginActivity;
import radonsoft.mireaassistant.MainActivity;
import radonsoft.mireaassistant.Network.Forms.ScheduleForm;
import radonsoft.mireaassistant.Network.Models.Even;
import radonsoft.mireaassistant.Network.Models.Odd;
import radonsoft.mireaassistant.Network.Models.Response;
import radonsoft.mireaassistant.Network.Models.Schedule;
import radonsoft.mireaassistant.Network.Models.Schedule_;

public class APIWrapper {

    private Context context;
    private MainActivity mainActivity;
    private LoginActivity loginActivity;
    private ArrayList<TableSchedule> oddList = new ArrayList<>();
    private ArrayList<TableSchedule> evenList = new ArrayList<>();

    public APIWrapper(MainActivity activity, Context context){
        //this.settings = context;
        this.context = context;
        this.mainActivity = activity;
    }

    public APIWrapper(LoginActivity activity, Context context){
        //this.settings = context;
        this.context = context;
        this.loginActivity = activity;
    }

    public interface ScheduleListener {
        void completeDownload();
        void errorOdd(String e);
    }

    @SuppressLint("CheckResult")
    public void getScheduleOdd(String groupName, int institute) {
        oddList.clear();
        NetworkSingleton.getRetrofit().create(ScheduleService.class)
                .getScheduleName(new ScheduleForm(0, institute, groupName))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(Schedule::getResponse)
                .map(Response::getSchedule)
                .map(Schedule_::getDays)
                .flatMapObservable(Observable::fromIterable)
                .flatMap(Observable::fromIterable)
                .map(day -> {
                    Odd odd = day.getOdd();
                    if (odd.getName() != null) {
                        return odd;
                    } else {
                        char dash = '―';
                        return new Odd(dash, dash, dash, dash);
                    }
                })
                .doOnComplete(() -> {
                    addToDatabase(1, oddList);
                    Log.d("Schedule", "DONE ODD");
                })
                .subscribe((Odd odd) -> {
                    String name = odd.getName().toString();
                    String type = checkNull(odd.getType()).toString();
                    String room = checkNull(odd.getRoom()).toString();
                    String teacher = checkNull(odd.getTeacher()).toString();
                    TableSchedule tableSchedule = new TableSchedule(name, 1, oddList.size(), type, room, teacher);
                    oddList.add(tableSchedule);
                    }, e -> {
                    Log.e("Schedule", e.toString(), e);});
    }

    @SuppressLint("CheckResult")
    public void getScheduleEven(String groupName, int institute) {
        evenList.clear();
        NetworkSingleton.getRetrofit().create(ScheduleService.class)
                .getScheduleName(new ScheduleForm(0, institute, groupName))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(Schedule::getResponse)
                .map(Response::getSchedule)
                .map(Schedule_::getDays)
                .flatMapObservable(Observable::fromIterable)
                .flatMap(Observable::fromIterable)
                .map(day -> {
                    Even even = day.getEven();
                    if (even.getName() != null) {
                        return even;
                    } else {
                        char dash = '―';
                        return new Even(dash, dash, dash, dash);
                    }
                })
                .doOnComplete(() -> {
                    addToDatabase(0, evenList);
                    if (loginActivity != null){
                        (loginActivity).completeDownload();
                    } else {
                        (mainActivity).completeDownload();
                    }
                    Log.d("Schedule", "DONE EVEN");
                })
                .subscribe((Even even) -> {
                    String name = even.getName().toString();
                    String type = checkNull(even.getType()).toString();
                    String room = checkNull(even.getRoom()).toString();
                    String teacher = checkNull(even.getTeacher()).toString();
                    TableSchedule tableSchedule = new TableSchedule(name, 0, evenList.size(), type, room, teacher);
                    evenList.add(tableSchedule);
                    }, e -> {
                    Log.e("Schedule", e.toString(), e);});
    }

    private void addToDatabase(int weektype, ArrayList<TableSchedule> toAdd){

        AppDatabase db;
        db = Room.databaseBuilder(context, AppDatabase.class, "schedule")
                .allowMainThreadQueries()
                .build();

        List<TableSchedule> list = db.tableScheduleDAO().getListByWeektype(weektype);

        if (list.size() == 0){
            for (TableSchedule scheduleObj:toAdd) {
                db.tableScheduleDAO().insertAll(scheduleObj);
            }
        } else {
            for (int i = 0; i<toAdd.size(); i++){
                TableSchedule scheduleObj = toAdd.get(i);
                scheduleObj.setId(list.get(i).getId());
                db.tableScheduleDAO().update(scheduleObj);
            }
        }
        db.close();
    }

    private Object checkNull(Object toCheck){
     if (toCheck != null){
         return toCheck;
     } else {
         return "―";
     }
    }
}
