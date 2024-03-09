package com.example.firebase.note.firebase.presentation.widget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.firebase.R;
import com.example.firebase.note.firebase.domain.entities.NoteEntity;
import com.example.firebase.note.firebase.presentation.listener.NoteClickListener;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class NoteHolder extends RecyclerView.Adapter<NoteHolder.NoteViewHolder> {
    private final List<NoteEntity> tempNoteList;
    private final NoteClickListener listener;
    public NoteHolder(List<NoteEntity> tempNoteList, NoteClickListener listener) {
        this.tempNoteList = tempNoteList;
        this.listener = listener;
    }

    @Override
    public int getItemCount() { return tempNoteList.size(); }

    @NonNull
    @Override
    public NoteHolder.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.firebase_temp, parent, false);
        return new NoteHolder.NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder.NoteViewHolder holder, int position) {
        NoteEntity _note = tempNoteList.get(position);
        holder.bindNotes(_note);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        private final ImageView _mediaImageView;
        private final TextView _holderTitleTextView;
        private final TextView _holderNoteTextView;
        private final TextView _holderDateTextView;
        private final MaterialButton _holderEditButton;
        private final MaterialButton _holderDeleteButton;
        private NoteViewHolder(@NonNull View view) {
            super(view);
            _mediaImageView = view.findViewById(R.id.firebase_Temp_Image_View);
            _holderTitleTextView = view.findViewById(R.id.firebase_Temp_Title_Text_View);
            _holderNoteTextView = view.findViewById(R.id.firebase_Temp_Note_TextView);
            _holderDateTextView = view.findViewById(R.id.firebase_Temp_Date_TextView);
            _holderEditButton = view.findViewById(R.id.firebase_Temp_Edit_Button);
            _holderDeleteButton = view.findViewById(R.id.firebase_Temp_Delete_Button);
        }

        private void bindNotes(@NonNull NoteEntity note) {
            _holderTitleTextView.setText(note.getTitle());
            _holderNoteTextView.setText(note.getNote());
            _holderDateTextView.setText(note.getDate());
            Glide.with(itemView.getContext()).load(note.getImageUrl()).into(_mediaImageView);
            _holderEditButton.setOnClickListener(v -> listener.onNoteUpdate((ViewGroup) v.getParent(), getAdapterPosition(), note));
            _holderDeleteButton.setOnClickListener(v -> listener.onNoteDelete(note));
        }
    }
}