package it.unicam.cs.mpgc.rpg125571.model.item;

import it.unicam.cs.mpgc.rpg125571.model.modifier.Modifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Equipment {
    private final List<Equipable> equippedItems = new ArrayList<>();


    /**
     * Equipaggia un item se non è già presente.
     *
     * @param item l'item da equipaggiare
     * @return {@code true} se l'item è stato aggiunto,
     *         {@code false} se era già equipaggiato
     */
    public boolean equip(Equipable item) {
        if (equippedItems.contains(item)) return false;
        equippedItems.add(item);
        return true;
    }

    /**
     * Rimuove un item dall'equipaggiamento.
     *
     * @param item l'item da rimuovere
     * @return {@code true} se era presente ed è stato rimosso
     */
    public boolean unequip(Equipable item) { return equippedItems.remove(item); }

    /**
     * Aggrega i modifier di tutti gli item attualmente equipaggiati.
     *
     * @return lista di {@link Modifier} pronti per essere passati a {@code ModifierSystem}
     */
    public List<Modifier> getModifiers() {
        List<Modifier> mods = new ArrayList<>();

        for (Equipable item : equippedItems) {
            mods.addAll(item.getModifiers());
        }

        return mods;
    }

    /**
     * Vista non modificabile degli item equipaggiati.
     *
     * @return lista immutabile di {@link Equipable}
     */
    public List<Equipable> getEquippedItems() {
        return Collections.unmodifiableList(equippedItems);
    }
}