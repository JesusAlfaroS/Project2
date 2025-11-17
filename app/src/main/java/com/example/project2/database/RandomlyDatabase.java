package com.example.project2.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.project2.database.entities.Randomly;
import com.example.project2.MainActivity;
import com.example.project2.database.entities.User;
import com.example.project2.database.typeConverters.LocalDateTypeConverter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@TypeConverters(LocalDateTypeConverter.class)
@Database(entities = {Randomly.class, User.class},version = 1, exportSchema = false)
public abstract class RandomlyDatabase extends RoomDatabase {

    public static final String USER_TABLE = "usertable";
    private static final String DATABASE_NAME = "GymLogDatabse";
    public static final String randomlyTable = "gymLogTable";

    private static volatile  RandomlyDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static RandomlyDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized(RandomlyDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    RandomlyDatabase.class,
                                    DATABASE_NAME
                            )
                            .fallbackToDestructiveMigration()
                            .addCallback(addDefaultValues)
                            .build();

                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback addDefaultValues = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db){
            super.onCreate(db);
            Log.i(MainActivity.TAG,"DATABASE CREATED!");
            databaseWriteExecutor.execute(() -> {
                UserDAO dao = INSTANCE.userDAO();
                dao.deleteAll();
                User admin = new User("Admin1","admin1");
                admin.setAdmin((true));
                dao.insert(admin);
                User testUser1 = new User("testuser1","testuser1");
                dao.insert(testUser1);
            });

        }
    };

    public abstract RandomlyDAO randomlyDAO();

    public abstract UserDAO userDAO();

}
