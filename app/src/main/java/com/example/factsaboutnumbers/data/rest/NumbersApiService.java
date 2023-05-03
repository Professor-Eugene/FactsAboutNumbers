package com.example.factsaboutnumbers.data.rest;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NumbersApiService {

    @GET("{number}")
    Single<String> requestNewFact(@Path("number") int number);
}
