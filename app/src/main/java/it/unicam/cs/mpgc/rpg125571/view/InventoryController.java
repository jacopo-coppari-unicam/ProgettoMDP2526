package it.unicam.cs.mpgc.rpg125571.view;

import it.unicam.cs.mpgc.rpg125571.model.character.Stats;
import it.unicam.cs.mpgc.rpg125571.model.enums.EquipmentSlot;
import it.unicam.cs.mpgc.rpg125571.model.item.Equipable;
import it.unicam.cs.mpgc.rpg125571.model.item.Item;
import it.unicam.cs.mpgc.rpg125571.model.item.itemtype.Armor;
import it.unicam.cs.mpgc.rpg125571.model.item.itemtype.Potion;
import it.unicam.cs.mpgc.rpg125571.model.item.itemtype.Weapon;
import it.unicam.cs.mpgc.rpg125571.model.modifier.AtkModifier;
import it.unicam.cs.mpgc.rpg125571.model.modifier.DefModifier;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.List;

// Controller for managing the player's inventory user interface
// It handles displaying the owned items, equipping and unequipping items
public class InventoryController {

    @FXML private ListView<Item> inventoryListView;
    private MainDashboardController mainContext;

    public void setMainContext(MainDashboardController mainContext) {
        this.mainContext = mainContext;

        // Cella inline personalizzata (Sostituisce ItemListCell)
        inventoryListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Item item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); }
                else { setText(buildItemLabel(item)); }
            }
        });
    }

    // Refreshes the ListView content by synchronizing it with the items
    // currently stored in the player's inventory data model.
    public void refresh() {
        if (mainContext.getPlayer() != null) {
            inventoryListView.setItems(FXCollections.observableArrayList(
                    mainContext.getPlayer().getInventory().getItems()));
        }
    }

    // Manages the action of using the currently selected item in the list
    @FXML
    private void handleUseItem() {
        Item selected = inventoryListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Nessun oggetto selezionato", "Seleziona un oggetto dall'inventario.");
            return;
        }
        if (selected instanceof Equipable) {
            boolean ok = mainContext.getInventoryManager().equipItem(selected);
            if (ok) mainContext.log("[EQUIPED] Hai equipaggiato: " + selected.getName());
            else showAlert(Alert.AlertType.WARNING, "Equipaggiamento fallito", "Impossibile equipaggiare.");
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Oggetto non equipaggiabile", "Le pozioni si usano in battaglia.");
        }
        mainContext.refreshAllViews();
    }

    // Manages the action of unequipping an item from the character
    // If the item selected in the inventory list is already equipped, it unequips it directly
    @FXML
    private void handleUnequipItem() {
        Item selected = inventoryListView.getSelectionModel().getSelectedItem();

        if (selected instanceof Equipable eq) {
            boolean ok = mainContext.getInventoryManager().unequipItem(eq.getSlot());
            if (ok) mainContext.log("[UNEQUIPED] Hai rimosso dall'equipaggiamento: " + selected.getName());
            mainContext.refreshAllViews();
            return;
        }

        List<EquipmentSlot> occupati = new ArrayList<>();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (mainContext.getPlayer().getEquipment().isOccupied(slot)) occupati.add(slot);
        }
        if (occupati.isEmpty()) return;

        List<String> scelte = occupati.stream()
                .map(s -> s.name() + " — " + getEquippedName(s)).toList();
        ChoiceDialog<String> dialog = new ChoiceDialog<>(scelte.get(0), scelte);
        dialog.showAndWait().ifPresent(choice -> {
            EquipmentSlot slot = occupati.get(scelte.indexOf(choice));
            if (mainContext.getInventoryManager().unequipItem(slot)) {
                mainContext.log("[SLOT] Slot " + slot.name() + " liberato.");
                mainContext.refreshAllViews();
            }
        });
    }

    // Retrieves the name of the item equipped in a specific slot
    // or "—" if the slot is empty
    private String getEquippedName(EquipmentSlot slot) {
        Equipable e = mainContext.getPlayer().getEquipment().getEquippedItem(slot);
        return (e != null) ? ((Item) e).getName() : "—";
    }

    // Constructs a descriptive text label to be displayed inside the list row for a specific item,
    // dynamically calculating its bonus attributes (ATK or DEF)
    private String buildItemLabel(Item item) {
        if (item instanceof Weapon w) {
            int atk = w.getModifiers().stream()
                    .filter(m -> m instanceof AtkModifier)
                    .mapToInt(m -> {
                        Stats tmp = new Stats(0, 0, 1);
                        m.apply(tmp);
                        return tmp.getAtk();
                    }).sum();
            return w.getName() + "  [[W] " + w.getSlot().name() + "  |  ATK +" + atk + "]";
        }
        if (item instanceof Armor a) {
            int def = a.getModifiers().stream()
                    .filter(m -> m instanceof DefModifier)
                    .mapToInt(m -> {
                        Stats tmp = new Stats(0, 0, 1);
                        m.apply(tmp);
                        return tmp.getDef();
                    }).sum();
            return a.getName() + "  [[A] " + a.getSlot().name() + "  |  DEF +" + def + "]";
        }
        if (item instanceof Potion p) {
            return p.getName() + "  [P] Pozione  |  " + p.getDescription() + "]";
        }
        return item.getName() + "  [[M] " + item.getItemType().name() + "]";
    }

    // Utility method to display a blocking information or warning alert dialog box
    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type, msg, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}