package fr.utt.if26.notesmanager;

public class Item{

    private int id;
    private String Name;
    private int parentItemId;
    private ItemType type;


    //constructors
    public Item(int id, String name, ItemType type){
        this.id = id;
        this.Name = name;
        this.type = type;
        this.parentItemId = 0;
    }

    public Item(int id, String name, ItemType type, int parentItemId){
        this.id = id;
        this.Name = name;
        this.type = type;
        this.parentItemId = parentItemId;
    }


    //getters
    public int getId() {
        return id;
    }

    public int getParentItemId() {
        return parentItemId;
    }

    public ItemType getType() {
        return type;
    }

    public String getName() { return Name; }


    //setters
    public void setParentItemId(int parentItemId) {
        this.parentItemId = parentItemId;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public void setName(String name) { Name = name; }
}
