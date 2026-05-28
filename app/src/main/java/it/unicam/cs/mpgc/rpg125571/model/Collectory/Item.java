package it.unicam.cs.mpgc.rpg125571.model.Collectory;
import it.unicam.cs.mpgc.rpg125571.model.enums.ItemType;

public class Item {
    private int id; // ID univoco dell'item
    private String name; // nome dell'item
    private String description; // descrizione dell'item
    private boolean stackable; // l'oggetto può essere stacckato?
    // private ItemType type; // tipo di oggeto // materiale, equipaggiamento, consumabile
    // private List<Modifier> modifiers; // MODIFICATORI +ATK, + DEF, +HP da implementare in seguito

    public Item(int id, String name, String description, boolean stackable) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.stackable = stackable;
    }

    public int getId() {return id;}
    public String getName() {return name;}
    public String getDescription() {return description;}
    public boolean isStackable() {return stackable;}
}
