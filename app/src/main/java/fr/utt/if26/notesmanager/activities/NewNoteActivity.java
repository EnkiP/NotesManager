package fr.utt.if26.notesmanager.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import fr.utt.if26.notesmanager.NotesPersistance;
import fr.utt.if26.notesmanager.R;

public class NewNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);



        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        Button okButton = (Button) findViewById(R.id.confirmNoteCreation);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int folderId = getIntent().getIntExtra("folderId", 0);

                EditText noteNameET = (EditText) findViewById(R.id.noteNameEditText);
                EditText noteContentET = (EditText) findViewById(R.id.contentEditText);

                String noteName = noteNameET.getText().toString();
                String noteContent = noteContentET.getText().toString();

                NotesPersistance db = new NotesPersistance(getBaseContext(), null);
                db.addNote(folderId, noteName, noteContent);
                finish();
            }
        });

    }
}
