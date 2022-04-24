package com.loki.makenote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.loki.makenote.models.Notes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNotesActivity extends AppCompatActivity {

    EditText mEditTitle, mEditNotes;
    ImageView mSave;
    Notes mNotes;

    Toolbar toolbar;

    boolean isOldNote = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        mEditTitle = findViewById(R.id.edit_title);
        mEditNotes = findViewById(R.id.edit_notes);
        mSave = findViewById(R.id.Iv_save);
        toolbar = findViewById(R.id.toolBar_notes);

        setSupportActionBar(toolbar);
        toolbar.setTitle("Add note");
        updateNotes();

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mEditTitle.getText().toString();
                String description = mEditNotes.getText().toString();

                if (isFormValid()) {

                    SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm a");
                    Date date = new Date();

                    if (!isOldNote) {
                        mNotes = new Notes();
                    }

                    mNotes.setTitle(title);
                    mNotes.setNotes(description);
                    mNotes.setDate(format.format(date));

                    Intent intent = new Intent();
                    intent.putExtra("note",mNotes);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });

    }

    private boolean isFormValid() {
        String description = mEditNotes.getText().toString();

        if (description.isEmpty()) {
            mEditNotes.setError("Please enter notes");
            return false;
        }

        return true;
    }

    //updates note
    public void updateNotes() {
        mNotes = new Notes();

        try {
            mNotes = (Notes) getIntent().getSerializableExtra("old_notes");
            mEditTitle.setText(mNotes.getTitle());
            mEditNotes.setText(mNotes.getNotes());
            isOldNote = true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}