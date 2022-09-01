package com.practice.myapplication;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "note_table")
public class NoteEntity {

    @PrimaryKey (autoGenerate = true)
    private int id;

    private String title;

    private String description;

    public NoteEntity(String title, String description) {
        this.title = title;
        this.description = description;
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

    public void setId(int id) {
        this.id = id;
    }

}
