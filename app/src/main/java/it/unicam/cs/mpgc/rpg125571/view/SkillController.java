package it.unicam.cs.mpgc.rpg125571.view;

import it.unicam.cs.mpgc.rpg125571.model.skill.AttackSkill;
import it.unicam.cs.mpgc.rpg125571.model.skill.HealSkill;
import it.unicam.cs.mpgc.rpg125571.model.skill.PlayerSkill;
import it.unicam.cs.mpgc.rpg125571.model.skill.Skill;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

// Controller for managing the character's skill configuration user interface
public class SkillController {

    @FXML private ListView<PlayerSkill> skillListView;
    @FXML private ListView<PlayerSkill> allSkillsListView;

    private MainDashboardController mainContext;

    public void setMainContext(MainDashboardController mainContext) {
        this.mainContext = mainContext;

        skillListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(PlayerSkill ps, boolean empty) {
                super.updateItem(ps, empty);
                setText(empty || ps == null ? null : buildSkillLabel(ps, false));
            }
        });

        allSkillsListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(PlayerSkill ps, boolean empty) {
                super.updateItem(ps, empty);
                if (empty || ps == null) { setText(null); return; }
                // Indicator tags showing if the cell is bound to an active equipment slot
                boolean equipped = mainContext.getPlayer().getSkillLoadout().isEquipped(ps);
                setText((equipped ? "[O] " : "   ") + buildSkillLabel(ps, true));
            }
        });
    }

    public void refresh() {
        if (mainContext.getPlayer() == null) return;
        skillListView.setItems(FXCollections.observableArrayList(
                mainContext.getPlayer().getSkillLoadout().getEquippedSkills()));
        allSkillsListView.setItems(FXCollections.observableArrayList(
                mainContext.getPlayer().getSkillInventory()));
    }

    // Equip a skill or
    @FXML
    private void handleEquipSkill() {
        PlayerSkill selected = allSkillsListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        if (mainContext.getPlayer().equipSkill(selected)) {
            mainContext.log("[EQUIPED] Skill equipaggiata: " + selected.getSkill().getName());
            refresh();
        } else {
            new Alert(Alert.AlertType.WARNING, "Loadout pieno o skill già presente.", ButtonType.OK).showAndWait();
        }
    }

    // remove an active combat loadout target slot selection
    @FXML
    private void handleUnequipSkill() {
        PlayerSkill selected = skillListView.getSelectionModel().getSelectedItem();
        if (selected != null && mainContext.getPlayer().unequipSkill(selected)) {
            mainContext.log("[REMOVED] Skill rimossa dal loadout: " + selected.getSkill().getName());
            refresh();
        }
    }

    private String buildSkillLabel(PlayerSkill ps, boolean withTypeTag) {
        Skill s     = ps.getSkill();
        int   level = ps.getCurrentLevel();

        String typeTag;
        String valueTag;
        if (s instanceof AttackSkill atk) {
            typeTag  = " - ";
            valueTag = "DMG " + atk.getDamage(level);
        } else if (s instanceof HealSkill heal) {
            typeTag  = " - ";
            valueTag = "HEAL " + heal.getHealAmount(level);
        } else {
            typeTag  = "";
            valueTag = "";
        }

        String prefix = withTypeTag ? typeTag + "  " : "";
        return prefix + s.getName()
                + "  [Lv." + level
                + "  |  " + s.getElement().name()
                + "  |  " + valueTag + "]";
    }
}