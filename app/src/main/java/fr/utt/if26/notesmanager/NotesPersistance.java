package fr.utt.if26.notesmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class NotesPersistance extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "NotesManager.db"; // nom du fichier pour la base
    private static final String TABLE_ITEMS = "items"; // nom de la table
    private static final String ATTRIBUT_ID = "id"; // liste des attributs
    private static final String ATTRIBUT_NAME = "name";
    private static final String ATTRIBUT_PARENTID = "parentId";
    private static final String ATTRIBUT_TYPE = "type";
    private static final String ATTRIBUT_CONTENT = "content";


    public NotesPersistance(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String table_items_create =
            "CREATE TABLE " + TABLE_ITEMS + "(" +
                    ATTRIBUT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ATTRIBUT_NAME + " TEXT" +
                    ATTRIBUT_PARENTID + " INTEGER, " +
                    ATTRIBUT_TYPE + " TEXT, " +
                    ATTRIBUT_CONTENT + " TEXT" +
                ")";
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }

    /**
     * Ajoute un dossier dans la base de données
     * @param parentId Id du dossier parent (0 si le nouveau dossier est à la racine)
     */
    public void addFolder(int parentId){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues newFolder = new ContentValues();

        newFolder.put(ATTRIBUT_PARENTID, parentId);
        newFolder.put(ATTRIBUT_TYPE, "Folder");

        db.insert(TABLE_ITEMS, null, newFolder);
    }

    /**
     * Ajoute une note dans la base de données
     * @param parentId Id du dossier parent (0 si le nouveau dossier est à la racine)
     * @param content contenu de la note
     */
    public void addNote(int parentId, String content){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues newNote = new ContentValues();

        newNote.put(ATTRIBUT_PARENTID, parentId);
        newNote.put(ATTRIBUT_TYPE, "Note");
        newNote.put(ATTRIBUT_CONTENT, content);

        db.insert(TABLE_ITEMS, null, newNote);
    }

    /**
     * Renvoie tous les items de la base de données (dossiers et notes)
     * @return ArrayList d'objets Item
     */
    public ArrayList<Item> getAllItems(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Item> allItems = new ArrayList<>();
        final String query = "SELECT * FROM " + TABLE_ITEMS;

        Cursor results = db.rawQuery(query, null);

        int itemId;
        String itemName;
        int itemParentId;
        ItemType itemType;
        String itemContent;

        while (! results.isAfterLast()){
            itemId = results.getInt(results.getColumnIndex(ATTRIBUT_ID));
            itemName = results.getString(results.getColumnIndex(ATTRIBUT_NAME));
            itemParentId = results.getInt(results.getColumnIndex(ATTRIBUT_PARENTID));
            itemType = ItemType.valueOf(results.getString(results.getColumnIndex(ATTRIBUT_TYPE)));
            itemContent = results.getString(results.getColumnIndex(ATTRIBUT_CONTENT));

            Item item = new Item(itemId, itemName, itemType, itemParentId);
            if (itemType == ItemType.Note){
                Note note = (Note) item;
                note.setContent(itemContent);
                allItems.add(note);
            } else {
                allItems.add(item);
            }

            results.moveToNext();
        }

        results.close();

        return allItems;
    }

}