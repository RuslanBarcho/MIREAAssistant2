package radonsoft.mireaassistant.Network;

import io.reactivex.Single;
import radonsoft.mireaassistant.Network.Forms.ScheduleForm;
import radonsoft.mireaassistant.Network.Models.Schedule;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Ruska on 14.09.2017.
 */
public interface ScheduleService {
    @POST("schedule/get")
    Single<Schedule> getScheduleName(@Body ScheduleForm scheduleForm);
}
