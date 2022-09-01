package com.practice.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class AddNoteActivity extends AppCompatActivity {

    EditText noteTitle, noteBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        noteTitle = findViewById(R.id.editTextReadNoteTitle);
        noteBody = findViewById(R.id.editTextReadNoteBody);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_note_menu, menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                saveNote();
        }
        return  true;
    }

    public void saveNote() {
        String userTitle = noteTitle.getText().toString();
        String userBody = noteBody.getText().toString();
        Intent i = new Intent();
        i.putExtra("usertitle", userTitle);
        i.putExtra("userbody", userBody);
        setResult(RESULT_OK, i);
        finish();
    }

}