package com.practice.myapplication;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "note_table")
public class NoteEntity {

    @PrimaryKey (autoGenerate = true)
    private int id;

    private String title;

    private String description;

    private String date;

    public NoteEntity(String title, String description, String date) {
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }

}
