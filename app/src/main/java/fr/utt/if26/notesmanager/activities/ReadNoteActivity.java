package fr.utt.if26.notesmanager.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import fr.utt.if26.notesmanager.Item;
import fr.utt.if26.notesmanager.Note;
import fr.utt.if26.notesmanager.NotesPersistance;
import fr.utt.if26.notesmanager.R;

public class ReadNoteActivity extends AppCompatActivity {

    String navigationText = "";
    int noteId = -1;
    Note item;
    NotesPersistance db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_note);

        db = new NotesPersistance(ReadNoteActivity.this, null);

        TextView navigationTextView = (TextView) findViewById(R.id.navigation);

        try {
            noteId = getIntent().getIntExtra("noteId", -1);
            navigationText = getIntent().getStringExtra("navigation");
        } catch (Exception e){
            e.printStackTrace();
        }

        navigationTextView.setText(navigationText);

    }


    @Override
    protected void onResume() {
        super.onResume();

        item = (Note) db.getItem(noteId);


        TextView noteNameTextView = (TextView) findViewById(R.id.noteName);
        TextView noteContentTextView = (TextView) findViewById(R.id.noteContent);
        noteContentTextView.setMovementMethod(new ScrollingMovementMethod());

        noteNameTextView.setText(item.getName());
        noteContentTextView.setText(item.getContent());
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_read_note, menu);
        return true;
    }


    /**
     * Gère les options du menu (ajouter une note ou un dossier)
     * @param item bouton du menu cliqué
     * @return true si le bouton est implémenté, false sinon
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editNote:
                editItem();
                return true;
            case R.id.renameNote:
                renameItem();
                return true;
            case R.id.deleteNote:
                deleteItem();
                return true;
        }
        return false;
    }

    private void editItem() {
        Context context = getApplicationContext();
        Intent intent = new Intent(this, EditNoteActivity.class);
        intent.putExtra("noteId", noteId);
        intent.putExtra("noteName", item.getName());
        intent.putExtra("content", item.getContent());
        intent.putExtra("navigation", navigationText);
        startActivity(intent);
    }


    @SuppressLint("SetTextI18n")
    private void renameItem() {
        //création de la popup qui demandera à l'utilisateur le nouveau nom de l'item
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("InflateParams")
        final View view = inflater.inflate(R.layout.popup_rename_item,null);
        AlertDialog alertDialog = new AlertDialog.Builder(ReadNoteActivity.this).create();
        alertDialog.setCancelable(true);

        final EditText newName = (EditText) view.findViewById(R.id.newName);
        TextView renameItemTextView = (TextView) view.findViewById(R.id.renameItemTextView);
        renameItemTextView.setText("Rename " + item.getName() + " in :");
        alertDialog.setTitle("Rename note");

        //renommage de l'item
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newNameString = newName.getText().toString();
                db.renameItem(item.getId(), newNameString);
                onResume();
            }
        });

        //annulation
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //affichage du popup
        alertDialog.setView(view);
        alertDialog.show();
    }


    private void deleteItem(){
        //création d'une popup de confirmation
        AlertDialog alertDialog = new AlertDialog.Builder(ReadNoteActivity.this).create();
        alertDialog.setTitle("Deleting " + item.getName());
        alertDialog.setMessage("Are you sure ?");
        alertDialog.setCancelable(true);

        //suppression de l'item
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.deleteItem(item.getId(), false);
                finish();
            }
        });

        //annulation
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //affichage du popup
        alertDialog.show();
    }
}
