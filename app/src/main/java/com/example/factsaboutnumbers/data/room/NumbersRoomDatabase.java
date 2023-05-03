package com.example.factsaboutnumbers.data.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.factsaboutnumbers.model.Number;

@Database(entities = {Number.class}, version = 1, exportSchema = false)
public abstract class NumbersRoomDatabase extends RoomDatabase {

    public abstract NumbersDao numbersDao();

    private static NumbersRoomDatabase INSTANCE;

    public static NumbersRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (NumbersRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    NumbersRoomDatabase.class, "numbers_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
