package radonsoft.mireaassistant.network.forms;

/**
 * Created by Ruslan on 16.09.17.
 */
public class ScheduleForm {
    public int term;
    public int institute;
    public String group;

    public ScheduleForm(int term, int institute, String group) {
        this.term = term;
        this.institute = institute;
        this.group = group;
    }
}
