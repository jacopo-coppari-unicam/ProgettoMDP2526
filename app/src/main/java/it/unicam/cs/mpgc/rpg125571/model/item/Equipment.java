package it.unicam.cs.mpgc.rpg125571.model.item;

import it.unicam.cs.mpgc.rpg125571.model.enums.EquipmentSlot;
import it.unicam.cs.mpgc.rpg125571.model.modifier.Modifier;

import java.util.*;

/*  A class representing a character's active equipment (Loadout).
    It manages the unique association between a body slot enums EquipmentSlot
    WEAPON, HELMET, CHEST, LEGS, BOOTS
    and the currently equipped itemtype, exposing the logic for adding,
    removing, and aggregating modifiers for all worn items. */
public class Equipment {
    private final Map<EquipmentSlot, Equipable> equippedItems;

    public Equipment() {
        this.equippedItems =
                new EnumMap<>(EquipmentSlot.class);
    }

    // Equip an itemtype, if the Slot if the slot is already occupied
    // the previous itemtype is replaced
    public Equipable equip(Equipable item) {
        if(item == null)
            throw new IllegalArgumentException("Item cannot be null");
        return equippedItems.put(item.getSlot(), item);
    }

    // Removes whatever is equipped in the slot
    public Equipable unequip(EquipmentSlot slot) { return equippedItems.remove(slot); }

    // Returns the equipped itemtype to the slot
    public Equipable getEquippedItem(EquipmentSlot slot) { return equippedItems.get(slot); }

    // Check if the slot is occupied
    public boolean isOccupied(EquipmentSlot slot) { return equippedItems.containsKey(slot); }

    // Returns all equipped items
    public Collection<Equipable> getEquippedItems() {
        return Collections.unmodifiableCollection(
                equippedItems.values()
        );
    }

    /*  Retrieves the complete list of all active modifiers provided by
        currently equipped items.
        A list of Modifier items applied to the character.*/
    public List<Modifier> getModifiers() {
        List<Modifier> mods = new ArrayList<>();
        // Loop through all Equipable items in the (equippedItems) map values.
        for (Equipable item : equippedItems.values()) {
            // Safety check: Skip empty slots or null elements
            // to prevent unexpected NullPointerException throws
            if (item != null) {
                mods.addAll(item.getModifiers());
            }
        }
        return mods;
    }

    // Clear all slots
    public void unequipAll() {
        equippedItems.clear();
    }
}