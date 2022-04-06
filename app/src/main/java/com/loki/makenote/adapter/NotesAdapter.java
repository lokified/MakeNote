package com.loki.makenote.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        TextView mTxtTitle, mTxtNotes, mtxtDate;
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

            //change card background
            int colorCode = getRandomColor();
            mCardNotes.setCardBackgroundColor(itemView.getResources().getColor(colorCode, null));

            //notes card on clicked
            mCardNotes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mListener.onClick(notes);
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
    }

    private int getRandomColor() {
        List<Integer> colorCode = new ArrayList<>();

        colorCode.add(R.color.color1);
        colorCode.add(R.color.color2);
        colorCode.add(R.color.color3);
        colorCode.add(R.color.color4);
        colorCode.add(R.color.color5);
        colorCode.add(R.color.color11);
        colorCode.add(R.color.color5);
        colorCode.add(R.color.color7);
        colorCode.add(R.color.color8);
        colorCode.add(R.color.color9);
        colorCode.add(R.color.color10);

        Random random = new Random();
        int randomColor = random.nextInt(colorCode.size());

        return colorCode.get(randomColor);
    }
}
