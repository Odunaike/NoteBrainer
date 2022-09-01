package com.practice.myapplication;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoteRepository {

    private LiveData<List<NoteEntity>> notes;
    private NoteDao noteDao;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public NoteRepository(Application application) {
        NoteDatabase noteDatabase = NoteDatabase.getInstance(application);
        noteDao = noteDatabase.noteDao();
        notes = noteDao.getAllNotes();
    }

    public void insert(NoteEntity note) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                noteDao.insert(note);
            }
        });
    }
    public void update(NoteEntity note) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                noteDao.update(note);
            }
        });
    }
    public void delete(NoteEntity note) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                noteDao.delete(note);
            }
        });
    }

    public LiveData<List<NoteEntity>> getAllNotes() {
        return notes;
    }


}
