package radonsoft.mireaassistant.Network.Models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Schedule_ {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("term")
    @Expose
    private Integer term;
    @SerializedName("institute")
    @Expose
    private Integer institute;
    @SerializedName("group")
    @Expose
    private String group;
    @SerializedName("secret")
    @Expose
    private String secret;
    @SerializedName("last_update")
    @Expose
    private Integer lastUpdate;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("days")
    @Expose
    private List<List<Day>> days = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public Integer getInstitute() {
        return institute;
    }

    public void setInstitute(Integer institute) {
        this.institute = institute;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Integer getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Integer lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public List<List<Day>> getDays() {
        return days;
    }

    public void setDays(List<List<Day>> days) {
        this.days = days;
    }

}

