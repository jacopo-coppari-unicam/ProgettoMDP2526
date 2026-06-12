package it.unicam.cs.mpgc.rpg125571.logic;

import it.unicam.cs.mpgc.rpg125571.model.character.Player;
import it.unicam.cs.mpgc.rpg125571.model.item.Equipable;
import it.unicam.cs.mpgc.rpg125571.model.item.Item;
import it.unicam.cs.mpgc.rpg125571.model.skill.PlayerSkill;

import java.util.List;

public class InventoryManager {
    private final Player player;

    public InventoryManager(Player player) {
        this.player = player;
    }

    // ==========================================
    // SEZIONE INVENTARIO & EQUIPAGGIAMENTO
    // ==========================================

    /**
     * Ottiene tutti gli oggetti presenti nell'inventario del giocatore.
     */
    public List<Item> getInventoryItems() {
        return player.getInventory().getItems();
    }

    /**
     * Equipaggia un oggetto specifico preso dall'inventario.
     * Se l'oggetto è equipaggiabile, viene rimosso dall'inventario e messo nello slot corretto.
     * L'eventuale oggetto precedentemente indossato torna nell'inventario.
     *
     * @param item L'oggetto da equipaggiare
     * @return true se l'operazione ha successo, false se l'oggetto non è equipaggiabile
     */
    public boolean equipItemFromInventory(Item item) {
        if (!(item instanceof Equipable equipableItem)) {
            return false; // L'oggetto non è un'arma o armatura
        }

        // 1. Rimuovi l'oggetto dall'inventario
        player.getInventory().removeItem(item);

        // 2. Mettilo nell'equipment e recupera l'eventuale rimpiazzo
        Equipable vecchioItem = player.getEquipment().equip(equipableItem);

        // 3. Se c'era già qualcosa equipaggiato in quel dello slot, rimettilo nell'inventario
        if (vecchioItem instanceof Item vecchioAsItem) {
            player.getInventory().addItem(vecchioAsItem);
        }

        return true;
    }

    // ==========================================
    // SEZIONE SKILL SYSTEM & LOADOUT
    // ==========================================

    // Get all the unlocked skill of the player
    public List<PlayerSkill> getOwnedSkills() {
        return player.getSkillInventory();
    }

    // Add a Skill of the player (PlayerSkill) in the loadout
    public boolean equipSkillToLoadout(PlayerSkill playerSkill) {
        return player.equipSkill(playerSkill);
    }

    /**
     * Rimuove una skill dal loadout attivo per liberare spazio.
     */
    //
    public boolean unequipSkillFromLoadout(PlayerSkill playerSkill) {
        return player.getSkillLoadout().unequip(playerSkill);
    }

    /**
     * Ottiene le skill attualmente pronte per il combattimento.
     */
    public List<it.unicam.cs.mpgc.rpg125571.model.skill.SkillEquipable> getActiveLoadout() {
        return player.getSkillLoadout().getEquippedSkills();
    }

    public Player getPlayer() { return player; }
}
