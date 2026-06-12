package it.unicam.cs.mpgc.rpg125571.model.skill;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 Manages the player's active skill slots.

 A loadout has a maximum number of slots. A skill
 can only be equipped if there is at least one free slot and it is not already
 present in the loadout.
 */
public class SkillLoadout {

    // Maximum number of skills that can be equipped at one time
    public static final int MAX_SLOTS = 3;

    private final List<PlayerSkill> equippedSkills = new ArrayList<>();


    // Equip a skill if there is an open slot and it is not already in the loadout
    // (true) if the operation was successful
    // (false) if the loadout is full or the skill is already present
    public boolean equip(PlayerSkill skillEquipable) {
        if (equippedSkills.size() >= MAX_SLOTS) return false;
        if (equippedSkills.contains(skillEquipable)) return false;
        equippedSkills.add(skillEquipable);
        return true;
    }

    // Removes a skill from the loadout and updates its equipped state.
    // (true) if the skill was present and deleted
    // (false) if the skill isn't in the loadout
    public boolean unequip(PlayerSkill skillEquipable) {
        return equippedSkills.remove(skillEquipable);
    }

    // Indicates whether the given skill is currently equipped in this loadout.
    public boolean isEquipped(PlayerSkill skillEquipable) {
        return equippedSkills.contains(skillEquipable);
    }

    // Returns a non-editable view of currently equipped skills.
    public List<PlayerSkill> getEquippedSkills() {
        return Collections.unmodifiableList(equippedSkills);
    }

    // Return number of used slot
    public int getUsedSlots() { return equippedSkills.size(); }

    // Indicated whether the loadout is full (true)
    public boolean isFull() { return equippedSkills.size() >= MAX_SLOTS; }
}
