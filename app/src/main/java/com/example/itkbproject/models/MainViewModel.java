package com.example.itkbproject.models;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.itkbproject.Entry;
import com.example.itkbproject.models.EntryRepository;

import java.util.List;


public class MainViewModel extends AndroidViewModel {

    private EntryRepository entryRepository;
    private LiveData<List<Entry>> entryLiveData;
    private static MutableLiveData<List<Entry>> searchResults;

    public MainViewModel(Application application) {
        super(application);
        entryRepository = new EntryRepository(application);
        searchResults = entryRepository.getSearchResults();
        entryLiveData = entryRepository.getEntryLiveData();
    }

    public static MutableLiveData<List<Entry>> getSearchResults(){
        return searchResults;
    }

    public void findKeyword(String keyword) {entryRepository.findKeyword(keyword); }

    public void findTitle(String title) {entryRepository.findTitle(title); }

    public void findCategory(String category){
        entryRepository.findCategory(category);
    }

    public void findSubcategory(String subcategory) {entryRepository.findSubcategory(subcategory); }

    public void findDescription(String description) {entryRepository.findDescription(description); }

    public void findDate(String dateFrom,String dateTo) {entryRepository.findDate(dateFrom,dateTo); } //TBD

    public LiveData<List<Entry>> getEntryLiveData() {
        return entryLiveData;
    }
}