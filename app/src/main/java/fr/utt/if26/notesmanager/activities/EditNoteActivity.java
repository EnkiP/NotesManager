package fr.utt.if26.notesmanager.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import fr.utt.if26.notesmanager.Note;
import fr.utt.if26.notesmanager.NotesPersistance;
import fr.utt.if26.notesmanager.R;

public class EditNoteActivity extends AppCompatActivity {

    String navigationText = "";
    int noteId = -1;
    Note item;
    NotesPersistance db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        db = new NotesPersistance(EditNoteActivity.this, null);

        TextView navigationTextView = (TextView) findViewById(R.id.navigation);
        TextView noteContentTextView = (TextView) findViewById(R.id.noteContent);
        TextView noteNameTextView = (TextView) findViewById(R.id.noteName);

        String noteContent = "";
        String name = "";
        try {
            noteId = getIntent().getIntExtra("noteId", -1);
            navigationText = getIntent().getStringExtra("navigation");
            name = getIntent().getStringExtra("noteName");
            noteContent = getIntent().getStringExtra("content");
        } catch (Exception e){
            e.printStackTrace();
        }

        noteContentTextView.setText(noteContent);
        noteNameTextView.setText(name);

        navigationTextView.setText(navigationText);


        //Création des boutons pour confirmer ou annuler la modification.
        Button confirmButton = (Button) findViewById(R.id.confirmButton);
        Button cancelButton = (Button) findViewById(R.id.cancelButton);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //------------------------------------------------------------
    }

    private void saveNote(){
        EditText noteContentEditText = (EditText) findViewById(R.id.noteContent);

        db.editItem(noteId, noteContentEditText.getText().toString());
        finish();
    }

}
