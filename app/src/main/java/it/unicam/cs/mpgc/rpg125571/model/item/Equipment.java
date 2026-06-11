package it.unicam.cs.mpgc.rpg125571.model.item;

import it.unicam.cs.mpgc.rpg125571.model.enums.EquipmentSlot;
import it.unicam.cs.mpgc.rpg125571.model.modifier.Modifier;
import it.unicam.cs.mpgc.rpg125571.model.modifier.ModifierSystem;

import java.util.*;

public class Equipment {
    private final Map<EquipmentSlot, Equipable> equippedItems;

    public Equipment() {
        this.equippedItems =
                new EnumMap<>(EquipmentSlot.class);
    }

    /**
     * Equipaggia un item.
     * Se lo slot è già occupato,
     * l'item precedente viene sostituito.
     */
    public Equipable equip(Equipable item) {
        if(item == null)
            throw new IllegalArgumentException("Item non può essere null");
        return equippedItems.put(item.getSlot(), item);
    }

    // Rimuove ciò che è equipaggiato nello slot.
    public Equipable unequip(EquipmentSlot slot) { return equippedItems.remove(slot); }

    // Restituisce l'Item equipaggiato nello slot
    public Equipable getEquippedItem(EquipmentSlot slot) { return equippedItems.get(slot); }

    // Verifica se lo slot è occupato
    public boolean isOccupied(EquipmentSlot slot) { return equippedItems.containsKey(slot); }

    // Restituisce tutti gli Item equipaggiati
    public Collection<Equipable> getEquippedItems() {
        return Collections.unmodifiableCollection(
                equippedItems.values()
        );
    }

    public List<Modifier> getModifiers() {
        List<Modifier> mods = new ArrayList<>();
        // Cicliamo su tutti gli oggetti Equipable presenti nella mappa
        for (Equipable item : equippedItems.values()) {
            if (item != null) { // Sicurezza extra per evitare NullPointerException
                mods.addAll(item.getModifiers());
            }
        }
        return mods;
    }

    // Svuota tutti gli slot
    public void unequipAll() {
        equippedItems.clear();
    }
}