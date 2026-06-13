package it.unicam.cs.mpgc.rpg125571.logic;

import it.unicam.cs.mpgc.rpg125571.model.character.Player;
import it.unicam.cs.mpgc.rpg125571.model.enums.EquipmentSlot;
import it.unicam.cs.mpgc.rpg125571.model.item.Consumable;
import it.unicam.cs.mpgc.rpg125571.model.item.Equipable;
import it.unicam.cs.mpgc.rpg125571.model.item.Item;

import java.util.List;


public class InventoryManager {

    private static final int MAX_SLOTS = 100;
    private final Player player;

    public InventoryManager(Player player) {
        this.player = player;
    }

    public List<Item> getItems() {
        return player.getInventory().getItems();
    }


    public List<Consumable> getConsumables() {
        return player.getInventory().getConsumables();
    }


    public List<Equipable> getEquipables() {
        return player.getInventory().getEquipables();
    }

    public boolean hasFreeSlot() {
        return player.getInventory().getItems().size() < MAX_SLOTS;
    }


    public boolean addItem(Item item) {
        if (!hasFreeSlot()) {
            System.out.println("[InventoryManager] Inventario pieno, impossibile aggiungere: " + item.getName());
            return false;
        }
        player.getInventory().addItem(item);
        return true;
    }


    public boolean removeItem(Item item) {
        return player.getInventory().removeItem(item);
    }

    // EQUIPMENT

    public boolean equipItem(Item item) {
        if (!(item instanceof Equipable equipable)) return false;

        EquipmentSlot slot = equipable.getSlot();

        Equipable old = player.getEquipment().getEquippedItem(slot);

        if (old != null && old instanceof Item oldItem) {
            player.getInventory().addItem(oldItem);
        }
        player.getInventory().removeItem(item);
        player.getEquipment().equip(equipable);
        return true;
    }


    public boolean unequipItem(EquipmentSlot slot) {
        Equipable removed = player.getEquipment().unequip(slot);
        if (removed == null) return false;
        if (hasFreeSlot()) {
            player.getInventory().addItem((Item) removed);
        } else {
            player.getEquipment().equip(removed);
            System.out.println("[InventoryManager] Impossibile dequipaggiare: inventario pieno.");
            return false;
        }
        return true;
    }


    public Equipable getEquippedItem(EquipmentSlot slot) {
        return player.getEquipment().getEquippedItem(slot);
    }
}