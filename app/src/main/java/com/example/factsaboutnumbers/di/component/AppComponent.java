package com.example.factsaboutnumbers.di.component;

import android.app.Application;

import com.example.factsaboutnumbers.base.BaseApplication;
import com.example.factsaboutnumbers.di.module.AppModule;
import com.example.factsaboutnumbers.di.module.ViewModelModule;
import com.example.factsaboutnumbers.di.util.AppScope;
import com.example.factsaboutnumbers.di.module.MainActivityModule;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@AppScope
@Component(modules = {AppModule.class, ViewModelModule.class
        , MainActivityModule.class, AndroidSupportInjectionModule.class})
public interface AppComponent extends AndroidInjector<BaseApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }
}