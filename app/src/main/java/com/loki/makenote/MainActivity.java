package com.loki.makenote;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loki.makenote.adapter.NotesAdapter;
import com.loki.makenote.database.RoomDB;
import com.loki.makenote.models.Notes;
import com.loki.makenote.utils.NotesClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private NotesAdapter mNotesAdapter;
    private List<Notes> notes = new ArrayList<>();
    RoomDB database;

    FloatingActionButton mBtnAdd;
    RecyclerView mRecyclerNotes;
    SearchView mSearchNote;

    Notes selectedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnAdd = findViewById(R.id.fad_add);
        mRecyclerNotes = findViewById(R.id.recycler_home);
        mSearchNote = findViewById(R.id.search_view);



        //get all notes from database
        database = RoomDB.getInstance(this);
        notes = database.mainDao().getAll();

        updateRecycler(notes);

        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, AddNotesActivity.class);
                startActivityForResult(intent, 101);
            }
        });

        mSearchNote.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filter(s);
                return true;
            }
        });
    }

    //filter searched note
    private void filter(String s) {
        List<Notes> filteredNotes = new ArrayList<>();

        for (Notes singleNote : notes) {

            if (singleNote.getTitle().toLowerCase().contains(s.toLowerCase())
            || singleNote.getNotes().toLowerCase().contains(s.toLowerCase())) {
                filteredNotes.add(singleNote);
            }
        }

        mNotesAdapter.filterList(filteredNotes);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //adds notes to database and displays
        if (requestCode == 101) {

            if (resultCode == Activity.RESULT_OK) {
                Notes newNotes = (Notes) data.getSerializableExtra("note");
                database.mainDao().insert(newNotes);
                notes.clear();
                notes.addAll(database.mainDao().getAll());
                mNotesAdapter.notifyDataSetChanged();
            }
        }
        else if (requestCode == 102) {

            if (resultCode == Activity.RESULT_OK) {
                Notes newNotes = (Notes) data.getSerializableExtra("note");
                database.mainDao().update(newNotes.getId(), newNotes.getTitle(), newNotes.getNotes());
                notes.clear();
                notes.addAll(database.mainDao().getAll());
                mNotesAdapter.notifyDataSetChanged();
            }
        }
    }

    public void updateRecycler(List<Notes> notes) {
        mRecyclerNotes.setHasFixedSize(true);
        mRecyclerNotes.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        mNotesAdapter = new NotesAdapter(this, notes, notesClickListener);
        mRecyclerNotes.setAdapter(mNotesAdapter);
    }

    private final NotesClickListener notesClickListener = new NotesClickListener() {
        @Override
        public void onClick(Notes myNotes) {

            Intent intent = new Intent(MainActivity.this, AddNotesActivity.class);
            intent.putExtra("old_notes", myNotes);
            startActivityForResult(intent, 102);
        }

        @Override
        public void onLongClick(Notes notes, CardView cardView) {
            selectedNote = new Notes();
            selectedNote = notes;
            showPopUp(cardView);
        }
    };

    private void showPopUp(CardView cardView) {

        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.pin:
                if (selectedNote.isPinned()){
                    database.mainDao().pin(selectedNote.getId(), false);
                    Toast.makeText(this, "unpinned", Toast.LENGTH_SHORT).show();
                }
                else {
                    database.mainDao().pin(selectedNote.getId(), true);
                    Toast.makeText(this, "pinned", Toast.LENGTH_SHORT).show();
                }
                notes.clear();
                notes.addAll(database.mainDao().getAll());
                mNotesAdapter.notifyDataSetChanged();
                return true;

            case R.id.delete:
                confirmDelete();
                return true;

            default:
                return false;
        }

    }


    public void confirmDelete() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Delete").setMessage("Are you sure you want to delete this?")
                .setCancelable(true)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        database.mainDao().delete(selectedNote);
                        notes.remove(selectedNote);
                        mNotesAdapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        }).show();
    }
}