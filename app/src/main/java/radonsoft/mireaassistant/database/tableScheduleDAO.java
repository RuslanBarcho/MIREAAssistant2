package radonsoft.mireaassistant.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface tableScheduleDAO {

    @Query("SELECT * FROM tableSchedule")
    List<TableSchedule> getAllSchedule();

    @Query("SELECT * FROM tableSchedule WHERE number IN (:number, :number +1, :number +2, :number +3, :number +4, :number +5) AND weekType = :weekType")
    List<TableSchedule> getListByWeektypeAndNumber(int weekType, int number);

    @Query("SELECT * FROM tableSchedule WHERE weekType = :weekType")
    List<TableSchedule> getListByWeektype(int weekType);

    @Query("SELECT * FROM tableSchedule WHERE id = :id")
    List<TableSchedule> getListById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(TableSchedule... tableSchedules);

    @Delete
    void delete(TableSchedule tableSchedule);

    @Update
    void update(TableSchedule tableSchedule);

    @Query("DELETE FROM tableSchedule")
    public void nukeTable();
}
