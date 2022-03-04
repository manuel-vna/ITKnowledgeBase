package com.example.itkbproject;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "itkbTable")
public class Entry {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String category;
    private String date;
    private String subcategory;
    private String description;
    private String source;

    //constructors
    @Ignore
    public Entry(int id, String title, String category, String date) {
        this(id,title,category,date,null,null,null);
    }
    @Ignore
    public Entry(int id, String title, String category, String date, String subcategory) {
        this(id,title,category,date,subcategory,null,null);
    }
    @Ignore
    public Entry(int id, String title, String category, String date, String subcategory,String description) {
        this(id,title,category,date,subcategory,description,null);
    }

    public Entry(int id, String title, String category, String date, String subcategory, String description, String source) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.date = date;
        this.subcategory = subcategory;
        this.description = description;
        this.source = source;
    }


    //getters
    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getCategory() {
        return category;
    }
    public String getSubcategory() {
        return subcategory;
    }
    public String getDescription() {
        return description;
    }
    public String getSource() {
        return source;
    }
    public String getDate() {
        return date;
    }
    //setters
    public void setId() {
        this.id = id;
    }
    public void setTitle() {
        this.title = title;
    }
    public void setCategory() {
        this.category = category;
    }
    public void setSubcategory() {
        this.subcategory = subcategory;
    }
    public void setDescription() {
        this.description = description;
    }
    public void setSource() {
        this.source = source;
    }
    public void setDate() {
        this.date = date;
    }
}
