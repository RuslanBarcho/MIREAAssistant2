package radonsoft.mireaassistant.ui.main;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.persistence.room.Room;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import radonsoft.mireaassistant.database.AppDatabase;
import radonsoft.mireaassistant.database.TableSchedule;
import radonsoft.mireaassistant.network.forms.ScheduleForm;
import radonsoft.mireaassistant.network.models.Even;
import radonsoft.mireaassistant.network.models.Odd;
import radonsoft.mireaassistant.network.models.Response;
import radonsoft.mireaassistant.network.models.Schedule;
import radonsoft.mireaassistant.network.models.Schedule_;
import radonsoft.mireaassistant.network.NetworkSingleton;
import radonsoft.mireaassistant.network.ScheduleService;
import radonsoft.mireaassistant.R;
import retrofit2.HttpException;

public class MainViewModel extends ViewModel {

    public MutableLiveData<Boolean> data = new MutableLiveData<>();
    public MutableLiveData<String> error = new MutableLiveData<>();

    private ArrayList<TableSchedule> oddList = new ArrayList<>();
    private ArrayList<TableSchedule> evenList = new ArrayList<>();

    @SuppressLint("CheckResult")
    public void getSchedule(ScheduleForm form, Context context){
        oddList.clear();
        evenList.clear();
        NetworkSingleton.getRetrofit().create(ScheduleService.class)
                .getScheduleName(form)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(Schedule::getResponse)
                .map(Response::getSchedule)
                .map(Schedule_::getDays)
                .flatMapObservable(Observable::fromIterable)
                .flatMap(Observable::fromIterable)
                .doOnComplete(() -> {
                    addToDatabase(1, oddList, context);
                    addToDatabase(0, evenList, context);
                    data.postValue(true);
                })
                .subscribe(day -> {
                    Odd odd = day.getOdd();
                    Even even = day.getEven();
                    TableSchedule tableScheduleOdd = new TableSchedule(checkNull(odd.getName()).toString(), 1, evenList.size(),
                            getType(checkNull(odd.getType()).toString()), checkNull(odd.getRoom()).toString(), checkNull(odd.getTeacher()).toString());
                    oddList.add(tableScheduleOdd);
                    TableSchedule tableScheduleEven = new TableSchedule(checkNull(even.getName()).toString(), 0, evenList.size(),
                            getType(checkNull(even.getType()).toString()), checkNull(even.getRoom()).toString(), checkNull(even.getTeacher()).toString());
                    evenList.add(tableScheduleEven);
                }, e-> {
                    if (e instanceof HttpException){
                        error.postValue(context.getResources().getText(R.string.error_msg) + " код: " + ((HttpException) e).code());
                    } else {
                        error.postValue(String.valueOf(context.getResources().getText(R.string.error_msg)));
                    }
                });
    }

    private void addToDatabase(int weekType, ArrayList<TableSchedule> toAdd, Context context){
        AppDatabase db;
        db = Room.databaseBuilder(context, AppDatabase.class, "schedule")
                .allowMainThreadQueries()
                .build();
        List<TableSchedule> list = db.tableScheduleDAO().getListByWeektype(weekType);
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

    private String getType(String type){
        switch (type){
            case "1.0": return "Пр";
            case "0.0": return "Лк";
            case "2.0": return "Лаб";
        }
        return "";
    }

    private Object checkNull(Object toCheck){
        if (toCheck != null){
            return toCheck;
        } else {
            return "―";
        }
    }

}
