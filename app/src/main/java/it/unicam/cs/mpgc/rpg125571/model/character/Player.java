package it.unicam.cs.mpgc.rpg125571.model.character;

import it.unicam.cs.mpgc.rpg125571.model.item.Equipment;
import it.unicam.cs.mpgc.rpg125571.model.skill.PlayerSkill;
import it.unicam.cs.mpgc.rpg125571.model.skill.SkillLoadout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player extends GameCharacter {
    private final Inventory inventory;
    private final List<PlayerSkill> skills;
    private final SkillLoadout skillLoadout;
    private int experience;
    private int experienceToNextLevel;
    private int gold;

    public Player(String name, int level, Stats baseStats, Equipment equipment,
                  Inventory inventory, List<PlayerSkill> skills, SkillLoadout skillLoadout,
                  int experience, int gold) {
        super(name, level, baseStats, equipment);
        this.inventory = inventory;
        this.skills = new ArrayList<>(skills);
        this.skillLoadout = skillLoadout;
        this.experience = experience;
        this.experienceToNextLevel = level * 100;
        this.gold = gold;
    }

    public Inventory getInventory() { return inventory; }


    // --------
    // | GOLD |
    // --------
    public void addGold(int gold) { this.gold += gold; }
    public void removeGold(int gold) { this.gold -= gold; }
    public int getGold() { return gold; }


    // -------------------
    // | SKILL E LOADOUT |
    // -------------------
    // All skills unlocked by the player (unmodifiable list)
    public List<PlayerSkill> getSkillInventory() { return Collections.unmodifiableList(skills); }

    // Unlock a new usable Skill
    public void unlockSkill(PlayerSkill skill) {
        // If skill is null or not exist STOP (return)
        if (skill == null || skills.contains(skill)) return;
        skills.add(skill);
    }

    // Remove a Skill from the usable Skill
    // If the skill is equipped in the loadout, remove
    public void removeSkill(PlayerSkill skill) {
        if (skill == null) return;
        skillLoadout.unequip(skill);
        skills.remove(skill);
    }

    // Equip a skill in the loadout.
    // It ensures that a skill the player doesn't possess cannot be equipped,
    // which SkillLoadout alone doesn't verify.
    public boolean equipSkill(PlayerSkill skill) {
        if (!skills.contains(skill)) return false;
        return skillLoadout.equip(skill);
    }
    // Unequip a skill from the loadout.
    public boolean unequipSkill(PlayerSkill skill) {
        return skillLoadout.unequip(skill);
    }

    // The active loadout: the skills equipped for combat
    public SkillLoadout getSkillLoadout() { return skillLoadout; }


    // EXPERIENCE AND LEVEL UP
    public void addExperience(int amount) {
        if (amount <= 0) return;
        this.experience += amount;
        // The while loop handles the case where the received experience
        // is so high that it allows you to progress through more than one level at a time.
        while (this.experience >= this.experienceToNextLevel) {
            this.experience -= this.experienceToNextLevel;
            levelUp();
        }
    }

    private void levelUp() {
        // 1. Increases the level stored in the superclass (int level)
        super.incrementLevel();

        // 2. Modifies the values within the existing baseStats
        Stats base = getBaseStats();
        base.setMaxHp(base.getMaxHp() + 20);   // +20 HP
        // heal(20); {HP set to maximum value after each fight}
        base.setAtk(base.getAtk() + 5);   // +5 ATK
        base.setDef(base.getDef() + 3); // +3 DEF

        // 3. Recalculates the EXP needed for the next level (e.g., Level 2 -> 200 EXP)
        this.experienceToNextLevel = getLevel() * 100;
        System.out.println(getName() + " leveled up " + getLevel() + "!");
    }

    // GETTER
    public int getExperience() { return experience; }
    public int getExperienceToNextLevel() { return experienceToNextLevel; }
}