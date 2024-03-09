package com.example.firebase.note.sqlite.presentation.widget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.firebase.R;
import com.example.firebase.note.sqlite.domain.entities.NoteEntity;
import com.example.firebase.note.sqlite.presentation.listener.NoteClickListener;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class NoteHolder extends RecyclerView.Adapter<NoteHolder.NoteViewHolder> {
    private final List<NoteEntity> tempNoteList;
    private final NoteClickListener listener;

    public NoteHolder(List<NoteEntity> tempNoteList, NoteClickListener listener) {
        this.tempNoteList = tempNoteList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteHolder.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sqlite_temp, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder.NoteViewHolder holder, int position) {
        NoteEntity _note = tempNoteList.get(position);
        holder.bindNote(_note);
    }

    @Override
    public int getItemCount() {
        return tempNoteList.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        private final TextView _holderTitleTextView;
        private final TextView _holderNoteTextView;
        private final TextView _holderDateTextView;
        private final MaterialButton _holderEditButton;
        private final MaterialButton _holderDeleteButton;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            _holderTitleTextView = itemView.findViewById(R.id.sqlite_Temp_Title_TextView);
            _holderNoteTextView = itemView.findViewById(R.id.sqlite_Temp_Note_TextView);
            _holderDateTextView = itemView.findViewById(R.id.sqlite_Temp_Date_TextView);
            _holderEditButton = itemView.findViewById(R.id.sqlite_Temp_Edit_Button);
            _holderDeleteButton = itemView.findViewById(R.id.sqlite_Temp_Delete_Button);
        }

        public void bindNote(@NonNull NoteEntity note) {
            _holderTitleTextView.setText(note.getTitle());
            _holderNoteTextView.setText(note.getNote());
            _holderDateTextView.setText(note.getDate());
            _holderEditButton.setOnClickListener(v -> listener.onNoteUpdate((ViewGroup) v.getParent(), note));
            _holderDeleteButton.setOnClickListener(v -> listener.onNoteDelete(note));
        }
    }
}