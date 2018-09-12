package radonsoft.mireaassistant.Network.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("schedule")
    @Expose
    private Schedule_ schedule;

    public Schedule_ getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule_ schedule) {
        this.schedule = schedule;
    }

}
