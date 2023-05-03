package com.example.factsaboutnumbers.ui.list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.factsaboutnumbers.repository.NumbersRepository;
import com.example.factsaboutnumbers.model.Number;

import java.util.List;

import javax.inject.Inject;

public class ListViewModel extends ViewModel {

    NumbersRepository numbersRepository;

    private LiveData<List<Number>> numbersLiveData;

    private MutableLiveData<LiveData<List<Number>>> updateListLiveData = new MutableLiveData<>();

    public LiveData<LiveData<List<Number>>> getUpdateListLiveData() {
        return updateListLiveData;
    }

    private boolean sortByRequestHistory = true;

    public LiveData<List<Number>> getNumbersLiveData() {
        return numbersLiveData;
    }

    @Inject
    ListViewModel(NumbersRepository numbersRepository) {
        this.numbersRepository = numbersRepository;
        initNumbersLiveData();
    }

    private void initNumbersLiveData() {
        if (sortByRequestHistory) {
            numbersLiveData = numbersRepository.getNumbersSortedByRequestHistory();
        } else {
            numbersLiveData = numbersRepository.getNumbersSortedAscending();
        }
        updateListLiveData.setValue(numbersLiveData);
    }

    public void toggleSortOrder() {
        sortByRequestHistory = !sortByRequestHistory;
        initNumbersLiveData();
    }
}
