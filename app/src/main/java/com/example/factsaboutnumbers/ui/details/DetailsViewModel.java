package com.example.factsaboutnumbers.ui.details;

import android.database.sqlite.SQLiteConstraintException;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.factsaboutnumbers.repository.NumbersRepository;
import com.example.factsaboutnumbers.model.Number;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class DetailsViewModel extends ViewModel {

    private NumbersRepository numbersRepository;

    private MutableLiveData<Number> requestedNumberLiveData = new MutableLiveData<>();

    private LiveData<Number> selectedNumberLiveData;

    private MutableLiveData<Boolean> loading = new MutableLiveData<>();

    private MutableLiveData<Boolean> numbersLoadError = new MutableLiveData<>();

    public LiveData<Number> getRequestedNumberLiveData() {
        return requestedNumberLiveData;
    }

    public LiveData<Number> getSelectedNumberLiveData() {
        return selectedNumberLiveData;
    }

    LiveData<Boolean> getLoading() {
        return loading;
    }

    LiveData<Boolean> getError() {
        return numbersLoadError;
    }


    @Inject
    DetailsViewModel(NumbersRepository numbersRepository) {
        this.numbersRepository = numbersRepository;
        selectedNumberLiveData = numbersRepository.getSelectedNumber();
    }

    public void requestNewFact(int intNumberRequested) {
        numbersLoadError.setValue(false);
        loading.setValue(true);
        numbersRepository.requestNewFact(intNumberRequested)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<String>() {
                    @Override
                    public void onSuccess(String fact) {
                        loading.setValue(false);
                        Number number = new Number(intNumberRequested, fact);
                        requestedNumberLiveData.setValue(number);
                        numbersRepository.saveNumber(number);
                    }

                    @Override
                    public void onError(Throwable e) {
                        loading.setValue(false);
                        numbersLoadError.setValue(true);
                    }
                });
    }

    public void getSelectedFromList(Number number) {
        numbersLoadError.setValue(false);
        numbersRepository.getSelectedFromList(number);
    }
}
