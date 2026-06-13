package it.unicam.cs.mpgc.rpg125571.view;

import it.unicam.cs.mpgc.rpg125571.model.character.Player;
import it.unicam.cs.mpgc.rpg125571.model.character.Stats;
import it.unicam.cs.mpgc.rpg125571.model.enums.EquipmentSlot;
import it.unicam.cs.mpgc.rpg125571.model.item.Equipable;
import it.unicam.cs.mpgc.rpg125571.model.item.Item;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

// Character management controller (stats and equipment screen).
// Displays and updates the player's vital statistics (HP, attack, defense)
// and the names of items currently equipped in the various slots.
public class CharacterController {

    @FXML private Label playerNameLabel;
    @FXML private Label hpLabel;
    @FXML private ProgressBar hpBar;
    @FXML private Label atkLabel;
    @FXML private Label defLabel;

    @FXML private Label weaponSlotLabel;
    @FXML private Label helmetSlotLabel;
    @FXML private Label chestSlotLabel;
    @FXML private Label legsSlotLabel;
    @FXML private Label bootsSlotLabel;

    // Reference the dashboard's main controller to retrieve model data.
    private MainDashboardController mainContext;

    public void setMainContext(MainDashboardController mainContext) {
        this.mainContext = mainContext;
    }

    // Updates all view graphics by retrieving updated player
    // information from the main context. Synchronizes the player's
    // name, current stats, HP bar, and equipped item names.
    public void refresh() {
        Player player = mainContext.getPlayer();
        if (player == null) return;

        Stats cur = player.getCurrentStats();
        int hp = player.getCurrentHp();
        int maxHp = cur.getMaxHp();

        playerNameLabel.setText(player.getName());
        hpLabel.setText("HP: " + hp + " / " + maxHp);
        hpBar.setProgress(maxHp > 0 ? (double) hp / maxHp : 0.0);
        atkLabel.setText(String.valueOf(cur.getAtk()));
        defLabel.setText(String.valueOf(cur.getDef()));

        weaponSlotLabel.setText(slotName(EquipmentSlot.WEAPON));
        helmetSlotLabel.setText(slotName(EquipmentSlot.HELMET));
        chestSlotLabel.setText(slotName(EquipmentSlot.CHEST));
        if (legsSlotLabel != null) legsSlotLabel.setText(slotName(EquipmentSlot.LEGS));
        if (bootsSlotLabel != null) bootsSlotLabel.setText(slotName(EquipmentSlot.BOOTS));
    }

    // Retrieves the name of the item equipped in
    // the specified slot. If the slot is empty, returns a text placeholder.
    private String slotName(EquipmentSlot slot) {
        Equipable e = mainContext.getPlayer().getEquipment().getEquippedItem(slot);
        return (e != null) ? ((Item) e).getName() : "—";
    }
}