package com.example.itkbproject.models;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.itkbproject.Entry;

import java.util.List;

@Dao
public interface EntryDao {
    @Query("Select * from itkbTable")
    LiveData<List<Entry>> getAllEntries();
    @Query("Select * from itkbTable")
    Cursor getAllEntriesasCursor();
    @Query("Select * FROM itkbTable WHERE (title LIKE '%'||:keyword||'%' OR description LIKE '%'||:keyword||'%') ")
    List<Entry> findKeyword(String keyword);
    @Query("Select * FROM itkbTable WHERE (title LIKE '%'||:title||'%')")
    List<Entry> findTitle(String title);
    @Query("Select * FROM itkbTable WHERE category= :category")
    List<Entry> findCategory(String category);
    @Query("Select * FROM itkbTable WHERE subcategory= :subcategory")
    List<Entry> findSubcategory(String subcategory);
    @Query("Select * FROM itkbTable WHERE (description LIKE '%'||:description||'%')")
    List<Entry> findDescription(String description);
    @Query("Select * FROM itkbTable WHERE (date BETWEEN :dateFrom and :dateTo)") //TBD
    List<Entry> findDate(String dateFrom,String dateTo); //TBD


    @Insert
    void insertEntry(Entry entry);
    @Update
    void updateEntry(Entry entry);
    @Delete
    void deleteEntry(Entry entry);
}