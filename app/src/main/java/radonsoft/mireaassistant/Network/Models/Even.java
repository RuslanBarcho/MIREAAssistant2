package radonsoft.mireaassistant.Network.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Even {

    @SerializedName("name")
    @Expose
    private Object name;
    @SerializedName("type")
    @Expose
    private Object type;
    @SerializedName("teacher")
    @Expose
    private Object teacher;
    @SerializedName("room")
    @Expose
    private Object room;

    public Even(Object name, Object type, Object teacher, Object room) {
        this.name = name;
        this.type = type;
        this.teacher = teacher;
        this.room = room;
    }

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public Object getTeacher() {
        return teacher;
    }

    public void setTeacher(Object teacher) {
        this.teacher = teacher;
    }

    public Object getRoom() {
        return room;
    }

    public void setRoom(Object room) {
        this.room = room;
    }

}

