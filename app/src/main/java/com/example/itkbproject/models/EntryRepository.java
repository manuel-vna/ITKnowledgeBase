package com.example.itkbproject.models;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.itkbproject.Entry;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class EntryRepository {

    private EntryDao entryDao;
    private LiveData<List<Entry>> entryLiveData;

    private final MutableLiveData<List<Entry>> searchResults =
            new MutableLiveData<>();
    private List<Entry> results;



    public EntryRepository(Application application) {
        EntryDatabase db = EntryDatabase.getInstance(application);
        entryDao = db.entryDao();
        entryLiveData = entryDao.getAllEntries();
    }


    public LiveData<List<Entry>> getEntryLiveData() {
        return entryLiveData;
    }

    public void findKeyword(String keyword){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            results = entryDao.findKeyword(keyword);
            handler.sendEmptyMessage(0);

        });
        executor.shutdown();
    }

    public void findTitle(String title){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            results = entryDao.findTitle(title);
            handler.sendEmptyMessage(0);

        });
        executor.shutdown();
    }

    public void findCategory(String category){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            results = entryDao.findCategory(category);
            handler.sendEmptyMessage(0);

        });
        executor.shutdown();
    }

    public void findSubcategory(String subcategory){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            results = entryDao.findSubcategory(subcategory);
            handler.sendEmptyMessage(0);

        });
        executor.shutdown();
    }

    public void findDescription(String description){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            results = entryDao.findDescription(description);
            handler.sendEmptyMessage(0);

        });
        executor.shutdown();
    }

    public void findDate(String dateFrom,String dateTo){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            results = entryDao.findDate(dateFrom,dateTo); //TBD
            handler.sendEmptyMessage(0);

        });
        executor.shutdown();
    }

    Handler handler = new Handler(Looper.getMainLooper()){
        @Override public void handleMessage(Message msg){
            searchResults.setValue(results);
        }
    };

    public MutableLiveData<List<Entry>> getSearchResults() {
        return searchResults;
    }

}
