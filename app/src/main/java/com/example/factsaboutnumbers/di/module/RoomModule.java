package com.example.factsaboutnumbers.di.module;

import android.content.Context;

import com.example.factsaboutnumbers.data.room.NumbersDao;
import com.example.factsaboutnumbers.di.util.AppScope;
import com.example.factsaboutnumbers.data.room.NumbersRoomDatabase;

import dagger.Module;
import dagger.Provides;

@Module
public class RoomModule {

    @AppScope
    @Provides
    static NumbersRoomDatabase provideNumbersRoomDatabase(Context context) {
        return NumbersRoomDatabase.getDatabase(context);
    }

    @AppScope
    @Provides
    static NumbersDao provideNumbersDao(NumbersRoomDatabase numbersRoomDatabase) {
        return numbersRoomDatabase.numbersDao();
    }
}
