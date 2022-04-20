package com.loki.makenote.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.loki.makenote.R;
import com.loki.makenote.models.Notes;
import com.loki.makenote.utils.NotesClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    private Context mContext;
    private List<Notes> mNotes;
    private NotesClickListener mListener;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    public NotesAdapter(Context mContext, List<Notes> mNotes, NotesClickListener mListener) {
        this.mContext = mContext;
        this.mNotes = mNotes;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesViewHolder(LayoutInflater.from(mContext).inflate(R.layout.notes_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.bindNotes(mNotes.get(position));
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public void filterList(List<Notes> filteredNotes) {
        mNotes = filteredNotes;
        notifyDataSetChanged();
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder {

        CardView mCardNotes;
        TextView mTxtTitle, mTxtNotes, mtxtDate, mNoteTitle, mNoteBody, mCancelBtn, mEditBtn;
        ImageView mIvPin;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);

            mCardNotes = itemView.findViewById(R.id.notes_container);
            mTxtTitle = itemView.findViewById(R.id.txt_title);
            mIvPin = itemView.findViewById(R.id.iv_pin);
            mTxtNotes = itemView.findViewById(R.id.txt_notes);
            mtxtDate = itemView.findViewById(R.id.txt_date);

        }

        public void bindNotes(Notes notes){
            mTxtTitle.setText(notes.getTitle());
            mTxtTitle.setSelected(true);

            mTxtNotes.setText(notes.getNotes());

            mtxtDate.setText(notes.getDate());
            mtxtDate.setSelected(true);

            //show/hide pin
            if (notes.isPinned()) {
                mIvPin.setImageResource(R.drawable.ic_baseline_push_pin_24);
            }
            else {
                mIvPin.setImageResource(0);
            }

            //notes card on clicked
            mCardNotes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createPopUpDetail(notes);
                }
            });

            //notes card on long click
            mCardNotes.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    mListener.onLongClick(mNotes.get(getAdapterPosition()), mCardNotes);
                    return true;
                }
            });

        }


        //create a popup

        private void createPopUpDetail(Notes notes) {

            dialogBuilder = new AlertDialog.Builder(mContext);
            View popUp = LayoutInflater.from(mContext).inflate(R.layout.popup_detail, null);

            mNoteTitle = (TextView) popUp.findViewById(R.id.note_header);
            mNoteBody = (TextView) popUp.findViewById(R.id.notes_body);

            mCancelBtn = (TextView) popUp.findViewById(R.id.exit_btn);
            mEditBtn = (TextView) popUp.findViewById(R.id.edit_btn);

            mNoteTitle.setText(notes.getTitle());
            mNoteBody.setText(notes.getNotes());

            dialogBuilder.setView(popUp);
            dialog = dialogBuilder.create();
            dialog.show();

            mCancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });


            mEditBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onClick(notes);
                    dialog.dismiss();
                }
            });

        }
    }
}
