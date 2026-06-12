package it.unicam.cs.mpgc.rpg125571.model.skill;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
  Gestisce gli slot delle skill attive del giocatore.

  <p>Un loadout ha un numero massimo di slot ({@value #MAX_SLOTS}). Una skill
  può essere equipaggiata solo se c'è almeno uno slot libero e non è già
  presente nel loadout. Tutta la sincronizzazione del flag {@code isEquipped}
  su {@link SkillEquipable} avviene qui, in modo che nessun altro componente
  debba preoccuparsene.</p>
 */
public class SkillLoadout {

    // Maximum number of skills that can be equipped at one time
    public static final int MAX_SLOTS = 3;

    private final List<SkillEquipable> equippedSkills = new ArrayList<>();


    // Equip a skill if there is an open slot and it is not already in the loadout
    // (true) if the operation was successful
    // (false) if the loadout is full or the skill is already present
    public boolean equip(SkillEquipable skillEquipable) {
        if (equippedSkills.size() >= MAX_SLOTS) return false;
        if (equippedSkills.contains(skillEquipable)) return false;
        equippedSkills.add(skillEquipable);
        return true;
    }

    // Removes a skill from the loadout and updates its equipped state.
    // (true) if the skill was present and deleted
    // (false) if the skill isn't in the loadout
    public boolean unequip(SkillEquipable skillEquipable) {
        return equippedSkills.remove(skillEquipable);
    }

    // Indicates whether the given skill is currently equipped in this loadout.
    public boolean isEquipped(SkillEquipable skillEquipable) {
        return equippedSkills.contains(skillEquipable);
    }

    // Returns a non-editable view of currently equipped skills.
    public List<SkillEquipable> getEquippedSkills() {
        return Collections.unmodifiableList(equippedSkills);
    }

    // Return number of used slot
    public int getUsedSlots() { return equippedSkills.size(); }

    // Indicated whether the loadout is full (true)
    public boolean isFull() { return equippedSkills.size() >= MAX_SLOTS; }
}
