package com.example.factsaboutnumbers.di.module;

import com.example.factsaboutnumbers.data.rest.NumbersApiService;
import com.example.factsaboutnumbers.di.util.AppScope;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@Module
public class RetrofitModule {

    private static final String BASE_URL = "http://numbersapi.com/";

    @AppScope
    @Provides
    static Retrofit provideRetrofit() {
        return new Retrofit.Builder().baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }

    @AppScope
    @Provides
    static NumbersApiService provideRetrofitService(Retrofit retrofit) {
        return retrofit.create(NumbersApiService.class);
    }
}
