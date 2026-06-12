package it.unicam.cs.mpgc.rpg125571.model.item;

import it.unicam.cs.mpgc.rpg125571.model.enums.ItemType;

public interface Item{
    int getId();
    String getName();
    ItemType getItemType();
    String getDescription();
}
