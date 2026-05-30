package it.unicam.cs.mpgc.rpg125571.model.item;

import it.unicam.cs.mpgc.rpg125571.model.character.modificator.Modifier;

import java.util.ArrayList;
import java.util.List;

public class Equipment {
    // Lista degli oggetti che possono essere (Equipable) e che sono equipaggiati
    List<Equipable> equippedItems;

    public void equip(Equipable item) { equippedItems.add(item); }

    public void unequip(Equipable item) { equippedItems.remove(item); }

    // prende tutti i modificatori nella lista di items equipaggiati e li restituisce in mods
    public List<Modifier> getModifiers() {
        List<Modifier> mods = new ArrayList<>();

        for (Equipable item : equippedItems) {
            mods.addAll(item.getModifiers());
        }
        return mods;
    }
}