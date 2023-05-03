package com.example.factsaboutnumbers.di.module;

import android.app.Application;
import android.content.Context;

import com.example.factsaboutnumbers.ui.main.MainActivity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module(includes = {RetrofitModule.class, RoomModule.class})
public abstract class AppModule {

    @Binds
    abstract Context provideContext(Application application);

    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity mainActivityInjector();
}
