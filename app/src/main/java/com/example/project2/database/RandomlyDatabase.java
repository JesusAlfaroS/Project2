package com.example.project2.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.project2.MainActivity;
import com.example.project2.database.entities.Randomly;
import com.example.project2.database.entities.User;
import com.example.project2.database.typeConverters.LocalDateTypeConverter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@TypeConverters(LocalDateTypeConverter.class)
@Database(
        entities = { Randomly.class, User.class },
        version = 3,                 // <-- bumped so the seed runs
        exportSchema = false
)
public abstract class RandomlyDatabase extends RoomDatabase {

    // Literal names used in SQL strings elsewhere
    public static final String USER_TABLE = "usertable";
    public static final String RANDOMLY_TABLE = "gymLogTable";

    // Keep existing filename (typo preserved to avoid changing on-disk DB name)
    private static final String DATABASE_NAME = "GymLogDatabse";

    private static volatile RandomlyDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // Package-private (default) is fine for same-package access
    static RandomlyDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RandomlyDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    RandomlyDatabase.class,
                                    DATABASE_NAME
                            )
                            .fallbackToDestructiveMigration()
                            .addCallback(ADD_DEFAULT_VALUES) // seed users on first create
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // Seeds the two predefined users when the DB file is first created
    private static final RoomDatabase.Callback ADD_DEFAULT_VALUES = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.i(MainActivity.TAG, "DATABASE CREATED!");
            databaseWriteExecutor.execute(() -> {
                UserDAO dao = INSTANCE.userDAO();

                // Clean slate (first creation anyway)
                dao.deleteAll();

                // admin2 / admin2  (admin)
                User admin2 = new User("admin2", "admin2");
                admin2.setAdmin(true);
                dao.insert(admin2);

                // testuser1 / testuser1  (non-admin)
                User testUser1 = new User("testuser1", "testuser1");
                testUser1.setAdmin(false);
                dao.insert(testUser1);
            });
        }
    };

    // DAOs
    public abstract RandomlyDAO randomlyDAO();
    public abstract UserDAO userDAO();
}
