package fr.utt.if26.notesmanager;

public class Note extends Item {

    private String content = "";

    //constructors
    public Note(int id, String name, int parentItemId, String content){
        super(id, name, ItemType.Note, parentItemId);
        this.content = content;
    }

    public Note(int id, String name, int parentItemId){
        super(id, name, ItemType.Note, parentItemId);
    }

    public Note(int id, String name){
        super(id, name, ItemType.Note);
    }

    public Note(int id, String name, String content){
        super(id, name, ItemType.Note);
        this.content = content;
    }


    //getters
    public String getContent() {
        return content;
    }


    //setters
    public void setContent(String content) {
        this.content = content;
    }
}
