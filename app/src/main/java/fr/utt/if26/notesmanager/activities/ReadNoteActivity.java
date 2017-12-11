package fr.utt.if26.notesmanager.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import fr.utt.if26.notesmanager.Item;
import fr.utt.if26.notesmanager.Note;
import fr.utt.if26.notesmanager.NotesPersistance;
import fr.utt.if26.notesmanager.R;

public class ReadNoteActivity extends AppCompatActivity {

    String navigationText = "";
    Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_note);

        TextView navigationTextView = (TextView) findViewById(R.id.navigation);
        TextView noteNameTextView = (TextView) findViewById(R.id.noteName);
        TextView noteContentTextView = (TextView) findViewById(R.id.noteContent);
        noteContentTextView.setMovementMethod(new ScrollingMovementMethod());

        int noteId = -1;
        String noteName = "";
        String noteContent = "";
        try {
            noteId = getIntent().getIntExtra("noteId", -1);
            noteName = getIntent().getStringExtra("noteName");
            noteContent = getIntent().getStringExtra("noteContent");
            navigationText = getIntent().getStringExtra("navigation");
        } catch (Exception e){
            e.printStackTrace();
        }

        navigationTextView.setText(navigationText);
        noteNameTextView.setText(noteName);
        noteContentTextView.setText(noteContent);

        item = new Note(noteId, noteName, noteContent);
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
                //addFolder(folderId);
                return true;
            case R.id.renameNote:
                //addNote(folderId);
                return true;
            case R.id.deleteNote:
                deleteItem();
                return true;
        }
        return false;
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
                NotesPersistance db = new NotesPersistance(ReadNoteActivity.this, null);
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
