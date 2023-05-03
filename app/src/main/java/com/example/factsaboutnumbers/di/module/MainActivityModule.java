package com.example.factsaboutnumbers.di.module;

import com.example.factsaboutnumbers.ui.details.DetailsFragment;
import com.example.factsaboutnumbers.ui.list.ListFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainActivityModule {

    @ContributesAndroidInjector
    abstract ListFragment listFragmentInjector();

    @ContributesAndroidInjector
    abstract DetailsFragment detailsFragmentInjector();
}
