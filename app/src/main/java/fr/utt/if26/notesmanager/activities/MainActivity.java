package fr.utt.if26.notesmanager.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import fr.utt.if26.notesmanager.Item;
import fr.utt.if26.notesmanager.ItemsAdapter;
import fr.utt.if26.notesmanager.NotesPersistance;
import fr.utt.if26.notesmanager.R;

import static fr.utt.if26.notesmanager.ItemType.*;

public class MainActivity extends AppCompatActivity  {

    private int folderId = 0;
    private String[] navigationFolders = {"root"};
    private ListView listeItems = null;
    NotesPersistance db = null;
    String navigationText = "";


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new NotesPersistance(getBaseContext(), null);

        //récupération de l'id du dossier courant et les noms de chaque dossier de la navigation
        Intent intent = getIntent();
        if (intent.hasExtra("folderId") && intent.hasExtra("folderName") && intent.hasExtra("navigation")){
            try {
                folderId = getIntent().getIntExtra("folderId", 0);
                String folderName = getIntent().getStringExtra("folderName");
                ArrayList<String> oldNavigationFolders = new ArrayList<String>(Arrays.asList(getIntent().getStringArrayExtra("navigation")));
                oldNavigationFolders.add(folderName);
                navigationFolders = oldNavigationFolders.toArray(new String[oldNavigationFolders.size()]);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        //------------------------------------------------------------


        //Mise à jour du textview de navigation
        TextView navigation = (TextView) findViewById(R.id.navigation);
        if (navigationFolders != null) {
            for (String folder : navigationFolders) {
                navigationText = navigationText + " > " + folder;
            }
        }
        navigation.setText(navigationText);
        //------------------------------------------------------------

        //liste des items (dossiers et notes)
        listeItems = (ListView) findViewById(R.id.itemsListView);
        //------------------------------------------------------------


        /*menu "click droit" quand on appuie longuement sur un item*/
//            //Marche mais le popup apparait en X=0 sous l'item
//            listeItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
//                public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
//
//                    PopupMenu popup = new PopupMenu(MainActivity.this, arg1);
//                    //Inflating the Popup using xml file
//                    popup.getMenuInflater().inflate(R.menu.menu_item, popup.getMenu());
//
//                    //registering popup with OnMenuItemClickListener
//                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                        public boolean onMenuItemClick(MenuItem item) {
//                            Toast.makeText(MainActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
//                            return true;
//                        }
//                    });
//
//                    popup.show();//showing popup menu
//
//                    return true;
//                }
//            });

//            //Marche mais ne récupère pas l'item cliqué
//            //Le GestureDetector sert à pouvoir associer un événement onLongPress à listeItems pour pouvoir récupérer les coordonnées du click
//            final GestureDetector gestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener()
//            {
//                @Override
//                public boolean onSingleTapUp(MotionEvent e)
//                {
//                    return false;
//                }
//
//                @Override
//                public void onLongPress(MotionEvent e){
//                    //affichage du menu de popup aux coordonnées du click
//                    displayPopupMenu(e.getX(), e.getY());
//                }
//            });
//
//            //le gestureDetector est associé à un OnTouchListener pour pouvoir être associé à listeItems en tant que listener d'événements de clicks générique
//            View.OnTouchListener gestureListener = new View.OnTouchListener() {
//                public boolean onTouch(View v, MotionEvent event) {
//                    return gestureDetector.onTouchEvent(event);
//                }};
//            listeItems.setOnTouchListener(gestureListener);
//            */

            //Nickel !
        final int[] touchPosition = new int[2];
        //récupération de la position du click avec un onTouchListener
        //cette méthode sera exécutée avant OnItemLongClick et ne consumera pas le click car elle retourne false
        listeItems.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                touchPosition[0] = (int) event.getX();
                touchPosition[1] = (int) event.getY();
                return false;
            }
        });

        listeItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            //ouverture du menu "click droit" quand on reste appuyé sur un item
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = (Item) listeItems.getAdapter().getItem(position);
                displayPopupMenu(item, touchPosition[0], touchPosition[1]);
                return true;
            }
        });

        listeItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //ouverture de l'item
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = (Item) listeItems.getAdapter().getItem(position);
                displayItem(item);
            }
        });
        //------------------------------------------------------------


        //Création des boutons pour ajouter des notes ou des dossiers.
        Button addNoteButton = (Button) findViewById(R.id.addNoteButton);
        Button addFolderButton = (Button) findViewById(R.id.addFolderButton);

        //ajouter une note
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNote(folderId);
            }
        });

        //ajouter un dossier
        addFolderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFolder(folderId);
            }
        });
        //------------------------------------------------------------

    }


    @Override
    protected void onResume(){
        super.onResume();

        //récupération des items dans la BDD
        Item[] items = db.getFolderContent(folderId);

        //affichage des items dans la liste
        ItemsAdapter adapteur = new ItemsAdapter(this, R.layout.listview_item, items);
        listeItems.setAdapter(adapteur);
    }


    /**
     * Affichage d'un menu de popup aux coordonnées spécifiées en paramètre
     * @param itemInList item cliqué
     * @param fingerX coordonnée en abscisse du click
     * @param fingerY coordonnée en ordonnée du click
     */
    public void displayPopupMenu(final Item itemInList, float fingerX, float fingerY){
        //liste des items (dossiers et notes)
        ListView listeItems = (ListView) findViewById(R.id.itemsListView);

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.relativeLayout);

        //Objet view utilisé pour faire apparaitre le popup menu à l'endroit du click
        View emptyView = new View(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(1, 1);
        params.leftMargin = (int) fingerX;
        params.topMargin = (int) fingerY;
        rl.addView(emptyView, params);

        //apparition du popup menu
        PopupMenu popup = new PopupMenu(MainActivity.this, emptyView);
        popup.getMenuInflater().inflate(R.menu.menu_item, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_open:
                        displayItem(itemInList);
                        break;
                    case R.id.action_rename:
                        Toast.makeText(MainActivity.this,"renaming " + itemInList.getName(),Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_delete:
                        deleteItem(itemInList);
                        break;
                    default:

                }
                return true;
            }
        });

        popup.show();//showing popup menu
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }


    /**
     * Gère les options du menu (ajouter une note ou un dossier)
     * @param item bouton du menu cliqué
     * @return true si le bouton est implémenté, false sinon
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addFolder: // correspond à la définition dans le fichier XML
                addFolder(folderId); // appel à une méthode annexe
                return true;
            case R.id.addNote: // correspond à la définition dans le fichier XML
                addNote(folderId); // appel à une méthode annexe
                return true;
        }
        return false;
    }


    /**
     * Ajoute un dossier à l'emplacement actuel, le nom du dossier est demandé à l'utilisateur via une AlertDialog
     * @param currentFolderId id du dossier courant
     */
    private void addFolder(final int currentFolderId){
        //création de la popup qui demandera à l'utilisateur le nom du nouveau dossier
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("InflateParams")
        final View view = inflater.inflate(R.layout.popup_new_folder,null);
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("New folder");
        alertDialog.setCancelable(true);

        final EditText folderName = (EditText) view.findViewById(R.id.newFolderName);

        //création du dossier
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String folderNameString = folderName.getText().toString();
                db.addFolder(currentFolderId, folderNameString);
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


    /**
     * Crée un nouvel intent NewNoteActivity puis ouvre cette activité.
     */
    private void addNote(int currentFolderId){
        Context context = getApplicationContext();
        Intent intent = new Intent(this, NewNoteActivity.class);
        intent.putExtra("folderId", currentFolderId);
        startActivity(intent);
    }


    private void displayItem(Item item){
        Intent intent;
        switch (item.getType()) {
            case Folder:
                intent = new Intent(this, MainActivity.class);
                intent.putExtra("folderId", item.getId());
                intent.putExtra("navigation", navigationFolders);
                intent.putExtra("folderName", item.getName());
                startActivity(intent);
                break;
            case Note:
                intent = new Intent(this, ReadNoteActivity.class);
                intent.putExtra("noteId", item.getId());
                intent.putExtra("noteName", item.getName());
                intent.putExtra("noteContent", ((fr.utt.if26.notesmanager.Note) item).getContent());
                intent.putExtra("navigation", navigationText);
                startActivity(intent);
                break;
        }
    }


    private void renameItem() {

    }


    private void deleteItem(final Item item){
        //création d'une popup de confirmation
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Deleting " + item.getName() + " (" + item.getType().toString() + ")");
        alertDialog.setMessage("Are you sure ?");
        alertDialog.setCancelable(true);

        //suppression de l'item
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.deleteItem(item.getId(), item.getType() == Folder);
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
        alertDialog.show();
    }
}
