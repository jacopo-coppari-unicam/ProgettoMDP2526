package it.unicam.cs.mpgc.rpg125571.model.character;

import it.unicam.cs.mpgc.rpg125571.model.item.Consumable;
import it.unicam.cs.mpgc.rpg125571.model.item.Equipable;
import it.unicam.cs.mpgc.rpg125571.model.item.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Inventory {

    private final List<Item> items = new ArrayList<>();

    public void addItem(Item item) {
        items.add(item);
    }

    // Remove Item from inventory
    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    public boolean hasItem(Item item) {
        return items.contains(item);
    }

    // Complete and non-editable view of the inventory.
    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    // Filters and returns only consumables in your inventory.
    public List<Consumable> getConsumables() {
        return items.stream()
                .filter(i -> i instanceof Consumable)
                .map(i -> (Consumable) i)
                .toList();
    }

    // Filters and returns only Equipables in your inventory.
    public List<Equipable> getEquipables() {
        return items.stream()
                .filter(i -> i instanceof Equipable)
                .map(i -> (Equipable) i)
                .toList();
    }
}
