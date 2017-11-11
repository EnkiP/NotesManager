package fr.utt.if26.notesmanager.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import fr.utt.if26.notesmanager.Item;
import fr.utt.if26.notesmanager.ItemsAdapter;
import fr.utt.if26.notesmanager.NotesPersistance;
import fr.utt.if26.notesmanager.R;

public class MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Création des boutons pour ajouter des notes ou des folders.
        Button addNoteButton = (Button) findViewById(R.id.addNoteButton);
        Button addFolderButton = (Button) findViewById(R.id.addFolderButton);

        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNote();
            }
        });

        addFolderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        //------------------------------------------------------------
    }

    @Override
    protected void onResume(){
        super.onResume();

        ListView listeItems = (ListView) findViewById(R.id.itemsListView);

        NotesPersistance db = new NotesPersistance(this, null);
        ArrayList<Item> items = db.getFolderContent(0);

        ItemsAdapter adapteur = new ItemsAdapter(this, R.layout.listview_item, items);
        listeItems.setAdapter(adapteur);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addFolder: // correspond à la définition dans le fichier XML
                addFolder(); // appel à une méthode annexe
                return true;
            case R.id.addNote: // correspond à la définition dans le fichier XML
                addNote(); // appel à une méthode annexe
                return true;
        }
        return false;
    }


    private void addFolder(){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        CharSequence text = "Ajout d'un dossier...";
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


    /**
     * Crée un nouvel intent NewNoteActivity puis ouvre cette activité.
     */
    private void addNote(){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        CharSequence text = "Ajout d'une note...";
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        Intent intent = new Intent(this, NewNoteActivity.class);
        startActivity(intent);
    }
}
