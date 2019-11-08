package radonsoft.mireaassistant.network;

import io.reactivex.Single;
import radonsoft.mireaassistant.network.forms.ScheduleForm;
import radonsoft.mireaassistant.network.models.Schedule;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Ruslan Vinter on 14.09.2017.
 */
public interface ScheduleService {
    @POST("schedule/get")
    Single<Schedule> getScheduleName(@Body ScheduleForm scheduleForm);
}
