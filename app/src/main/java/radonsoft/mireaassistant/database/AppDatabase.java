package radonsoft.mireaassistant.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {TableSchedule.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract tableScheduleDAO tableScheduleDAO();
}
