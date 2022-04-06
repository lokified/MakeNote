package com.loki.makenote.utils;

import androidx.cardview.widget.CardView;

import com.loki.makenote.models.Notes;

public interface NotesClickListener {

    void onClick(Notes notes);
    void onLongClick(Notes notes, CardView cardView);
}
