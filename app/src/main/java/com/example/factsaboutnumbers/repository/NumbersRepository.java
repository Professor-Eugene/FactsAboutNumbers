package com.example.factsaboutnumbers.repository;

import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.factsaboutnumbers.L;
import com.example.factsaboutnumbers.data.rest.NumbersApiService;
import com.example.factsaboutnumbers.data.room.NumbersDao;
import com.example.factsaboutnumbers.di.util.AppScope;
import com.example.factsaboutnumbers.model.Number;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

@AppScope
public class NumbersRepository {

    private NumbersApiService numbersApiService;

    private NumbersDao numbersDao;

    private LiveData<List<Number>> numbersSortedByRequestHistory;

    private LiveData<List<Number>> numbersSortedAscending;


    private MutableLiveData<Number> selectedNumber = new MutableLiveData<>();

    public LiveData<Number> getSelectedNumber() {
        return selectedNumber;
    }


    @Inject
    NumbersRepository(NumbersApiService numbersApiService, NumbersDao numbersDao) {
        this.numbersApiService = numbersApiService;
        this.numbersDao = numbersDao;
        numbersSortedByRequestHistory = numbersDao.getNumbersSortedByRequestHistory();
        numbersSortedAscending = numbersDao.getNumbersSortedAscending();
    }

    public LiveData<List<Number>> getNumbersSortedByRequestHistory() {
        return numbersSortedByRequestHistory;
    }

    public LiveData<List<Number>> getNumbersSortedAscending() {
        return numbersSortedAscending;
    }

    public Single<String> requestNewFact(int intNumber) {
        return numbersApiService.requestNewFact(intNumber);
    }

    public void getSelectedFromList(Number number) {

        numbersDao.getSelectedFromList(number.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Number>() {
                    @Override
                    public void onSuccess(Number number) {
                        selectedNumber.setValue(number);
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.og("Error " + e);
                    }
                });
    }

    public void saveNumber(Number number) {

        Observable.fromCallable(() -> numbersDao.insert(number))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(Long index) {
                        if (index != -1) {
                            L.og("Fact saved");
                        } else {
                            L.og("Fact is already present");                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }
}
