package com.practice.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.List;

public class NoteActivity extends AppCompatActivity {

    EditText title, body;
    Intent intent;
    int noteID;
    String noteTitle, noteBody;
    RecyclerView recyclerView ;

    NoteViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        title = findViewById(R.id.editTextReadNoteTitle);
        body = findViewById(R.id.editTextReadNoteBody);

        //let memake it in reader mode by not enabling the user to edit now
        enable(false);

        getData();



    }

    public void getData() {
        intent = getIntent();
        noteTitle = intent.getStringExtra("notetitle");
        noteBody = intent.getStringExtra("notedescription");
        noteID = intent.getIntExtra("noteid", -1);

        title.setText(noteTitle);
        body.setText(noteBody);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.edit_menu:
                enable(true);
                break;
            case R.id.edit_save_menu:
                updateNote();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void enable(boolean val) {
        title.setEnabled(val);
        body.setEnabled(val);
    }

    public void updateNote() {
        String newTitle = title.getText().toString();
        String newBody = body.getText().toString();

        Intent i = new Intent();
        i.putExtra("title",newTitle);
        i.putExtra("body", newBody);
        if (noteID != -1) {
            i.putExtra("id", noteID);
            setResult(RESULT_OK, i);
            finish();
        }
    }

}