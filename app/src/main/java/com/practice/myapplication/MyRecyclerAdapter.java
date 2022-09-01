package com.practice.myapplication;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<NoteEntity> notes = new ArrayList<>();

    private MyOnItemClickListener listener;
    private TimeDialogListener timeListener;


    public MyRecyclerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        NoteEntity currentNote = notes.get(position);

        holder.title.setText(currentNote.getTitle());
        holder.description.setText(currentNote.getDescription());

        if (position % 5 == 0)
            holder.itemBackground.setCardBackgroundColor(ContextCompat.getColor(context,R.color.item1));
        else if (position % 5 == 1)
            holder.itemBackground.setCardBackgroundColor(ContextCompat.getColor(context,R.color.item2));
        else if (position % 5 == 2)
            holder.itemBackground.setCardBackgroundColor(ContextCompat.getColor(context,R.color.item3));
        else if (position % 5 == 3)
            holder.itemBackground.setCardBackgroundColor(ContextCompat.getColor(context,R.color.item4));
        else if (position % 5 == 4)
            holder.itemBackground.setCardBackgroundColor(ContextCompat.getColor(context,R.color.item5));


        /*I used a listener to the get the time and title of the item the notification was set for.
        So, I will get the values from the mainactivivytand start the service for notification receiver
         */
        String holderTitle = holder.title.getText().toString();
        holder.notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timeListener.onTimeSelected(selectedHour, selectedMinute, holderTitle);
                    }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(context, AlertDialog.THEME_HOLO_DARK, onTimeSetListener, 0,
                        0, true);
                timePickerDialog.setTitle("Set Notification");
                timePickerDialog.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setNotes(List<NoteEntity> notes) {

        this.notes = notes;
        notifyDataSetChanged();

    }

    public NoteEntity getNote(int position){
        return  notes.get(position);
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        private CardView itemBackground;
        private TextView title, description, date;
        ImageView notify;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemBackground = itemView.findViewById(R.id.itemBackground);
            title = itemView.findViewById(R.id.textViewTitle);
            description = itemView.findViewById(R.id.textViewDescription);
            date = itemView.findViewById(R.id.textViewDate);
            notify = itemView.findViewById(R.id.imageViewNotify);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position  = getAdapterPosition();
                    NoteEntity note = notes.get(position);
                    if(listener != null && position != RecyclerView.NO_POSITION )
                        listener.myOnItemClick(note);
                }
            });

        }
    }


    interface MyOnItemClickListener {
        void myOnItemClick(NoteEntity note);
    }

    public void mySetOnItemClickListener(MyOnItemClickListener listener){
        this.listener = listener;
    }


    interface TimeDialogListener {
        void onTimeSelected(int selectedHour, int selectedMinute, String title);
    }

    void onNotificationSet(TimeDialogListener timeListener) {
        this.timeListener = timeListener;
    }


}


