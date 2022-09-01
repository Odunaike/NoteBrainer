package com.practice.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    NoteViewModel viewModel;
    FloatingActionButton fab;

    ActivityResultLauncher<Intent> activityLauncherForAddNote;
    ActivityResultLauncher<Intent> activityLauncherForNoteActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);

        registerAddNoteLauncher();
        registerNoteActivityLauncher();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyRecyclerAdapter adapter = new MyRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        viewModel.getAllNotes().observe(this, new Observer<List<NoteEntity>>() {
            @Override
            public void onChanged(List<NoteEntity> noteEntities) {
                adapter.setNotes(noteEntities);
            }
        });



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddNoteActivity.class);
                activityLauncherForAddNote.launch(i);
            }
        });

        //I want to delete note by swiping
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                int swipedPositionn = viewHolder.getAdapterPosition();
                NoteEntity swipedNote = adapter.getNote(viewHolder.getAdapterPosition());

                viewModel.delete(swipedNote);
               // adapter.notifyItemRemoved(swipedPositionn);

                Snackbar.make(getCurrentFocus(), "Deleting...", Snackbar.LENGTH_SHORT)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                             //   swipedNote.setId(swipedPositionn);
                                viewModel.insert(swipedNote);
                              //  adapter.notifyItemInserted(swipedPositionn);
                            }
                        }).setActionTextColor(Color.BLACK).show();
            }
        }).attachToRecyclerView(recyclerView);


        adapter.mySetOnItemClickListener(new MyRecyclerAdapter.MyOnItemClickListener() {
            @Override
            public void myOnItemClick(NoteEntity note) {
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                intent.putExtra("notetitle", note.getTitle());
                intent.putExtra("notedescription", note.getDescription());
                intent.putExtra("noteid", note.getId());

                activityLauncherForNoteActivity.launch(intent);
            }
        });

        //Now lets implement my TimeDialogListener and get the time and title values. I created a method to start the notification service with those values
        adapter.onNotificationSet(new MyRecyclerAdapter.TimeDialogListener() {
            @Override
            public void onTimeSelected(int selectedHour, int selectedMinute, String title) {
                startNotificationService(selectedHour,selectedMinute, title);
            }
        });


    }



    //lets register the activiity resultlauncher  for AddNoteActivity
    private void registerAddNoteLauncher() {
        activityLauncherForAddNote = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultCode = result.getResultCode();
                        Intent data = result.getData();

                        if(resultCode == RESULT_OK && data != null){
                            String userTitle = data.getStringExtra("usertitle");
                            String userBody = data.getStringExtra("userbody");
                            NoteEntity note = new NoteEntity(userTitle, userBody);
                            viewModel.insert(note);
                        }
                    }
                }
        );
    }

    //lets register NoteActivity.java ,where we can update note
    private void registerNoteActivityLauncher() {
        activityLauncherForNoteActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultCode = result.getResultCode();
                        Intent i = result.getData();
                        if (resultCode == RESULT_OK && i != null) {
                            String newTitle = i.getStringExtra("title");
                            String newBody = i.getStringExtra("body");
                            int noteID = i.getIntExtra("id", -1);

                            NoteEntity updatedNote = new NoteEntity(newTitle, newBody);
                            updatedNote.setId(noteID);
                            viewModel.update(updatedNote);
                        }
                    }
                });
    }

    public void startNotificationService(int hour, int minute, String title){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        Intent i = new Intent(this, NotificationReceiver.class);
        i.putExtra("title", title);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 100,
                i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }



}