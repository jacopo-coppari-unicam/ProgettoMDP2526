package it.unicam.cs.mpgc.rpg125571.model.item.itemtype;

import it.unicam.cs.mpgc.rpg125571.model.enums.ItemType;
import it.unicam.cs.mpgc.rpg125571.model.item.AbstractItem;

public class Material extends AbstractItem {
    public Material(int id, String name, ItemType type, String description) {
        super(id, name, type, description);
    }
}