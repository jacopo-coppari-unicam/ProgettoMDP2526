package it.unicam.cs.mpgc.rpg125571.model.item;

import it.unicam.cs.mpgc.rpg125571.model.modifier.Modifier;

import java.util.ArrayList;
import java.util.List;

public class Equipment {
    private final List<Equipable> equippedItems = new ArrayList<>();


    public void equip(Equipable item) {
        equippedItems.add(item);
    }

    public void unequip(Equipable item) {
        equippedItems.remove(item);
    }

    public List<Modifier> getModifiers() {
        List<Modifier> mods = new ArrayList<>();

        for (Equipable item : equippedItems) {
            mods.addAll(item.getModifiers());
        }

        return mods;
    }
}