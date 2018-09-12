package radonsoft.mireaassistant.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "tableSchedule")
public class TableSchedule {

    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "weekType")
    public int weekType;
    @ColumnInfo(name = "number")
    public int number;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "type")
    public String type;
    @ColumnInfo(name = "classroom")
    public String classroom;
    @ColumnInfo(name = "teacher")
    public String teacher;

    public TableSchedule(String name, int weekType, int number,  String type, String classroom, String teacher){
        this.name = name;
        this.weekType = weekType;
        this.number = number;
        this.type = type;
        this.classroom = classroom;
        this.teacher = teacher;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWeekType() {
        return weekType;
    }

    public void setWeekType(int weekType) {
        this.weekType = weekType;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
