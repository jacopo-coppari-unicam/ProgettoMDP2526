package it.unicam.cs.mpgc.rpg125571.model.Collectory;
import it.unicam.cs.mpgc.rpg125571.model.enums.ItemType;

public class Item {
    private int id; // ID univoco dell'item
    private String name; // nome dell'item
    private String description; // descrizione dell'item
    private ItemType type; // tipo di oggeto // materiale, equipaggiamento, consumabile
    // private List<Modifier> modifiers; // MODIFICATORI +ATK, + DEF, +HP da implementare in seguito
    private boolean stackable; // l'oggetto può essere stacckato?
}
