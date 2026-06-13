package it.unicam.cs.mpgc.rpg125571.model.character;

import it.unicam.cs.mpgc.rpg125571.model.modifier.Modifier;
import it.unicam.cs.mpgc.rpg125571.model.item.Equipment;
import it.unicam.cs.mpgc.rpg125571.model.modifier.ModifierSystem;
import it.unicam.cs.mpgc.rpg125571.model.modifier.TemporaryModifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class GameCharacter implements Damageable {
    private final String name;
    private int level;
    private final Stats baseStats;
    private int currentHp;
    private Equipment equipment;

    // Buffs from potions, skills, etc. — last for turns
    private final List<TemporaryModifier> temporaryModifiers = new ArrayList<>();

    protected void incrementLevel() {
        this.level++;
    }
    // Builds a character by initializing HP to the maximum base stats.
    public GameCharacter(String name, int level, Stats baseStats, Equipment equipment) {
        this.name = name;
        this.level = level;
        this.baseStats = baseStats;
        this.equipment = equipment;
        this.currentHp = baseStats.getMaxHp();
    }

    /*
        Calculates the character's final stats at the current time.

        Aggregates modifiers from equipment and active temporary buffs
        and applies them to a copy of the base stats
        via ModifierSystem. The base stats are never modified.

        Returns a new Stats instance with all modifiers applied.
     */
    public Stats getCurrentStats() {
        List<Modifier> allModifiers = new ArrayList<>(equipment.getModifiers());
        for (TemporaryModifier tm : temporaryModifiers) {
            allModifiers.add(tm.getModifier());
        }
        return ModifierSystem.calculate(baseStats, allModifiers);
    }

    public void heal(int value) {
        if (value <= 0) return;
        int maxHp = getCurrentStats().getMaxHp();
        currentHp = Math.min(currentHp + value, maxHp);
    }

    @Override
    public void takeDamage(int damage) {
        currentHp -= damage;
        if(currentHp < 0) currentHp = 0;
    }

    @Override
    public boolean isDead(){
        return currentHp == 0; // =0 morto (true) else vivo (false)
    }


    public void setCurrentHp(int hp) {
        int maxHp = baseStats.getMaxHp();
        this.currentHp = Math.max(0, Math.min(hp, maxHp));
    }

    // TEMPORARY BUFF:
    //  - Potion
    //  - (Future)

    // Adds a temporary buff to the character.
    public void addTemporaryModifier(TemporaryModifier modifier) {
        temporaryModifiers.add(modifier);
    }

    // Decrease all active temporary buffs by one turn and
    // remove expired ones.
    public void tickTemporaryModifiers() {
        temporaryModifiers.forEach(TemporaryModifier::tick);
        temporaryModifiers.removeIf(TemporaryModifier::isExpired);
    }

    public void clearTemporaryModifiers() {
        temporaryModifiers.clear();
    }

    public List<TemporaryModifier> getTemporaryModifiers() {
        return Collections.unmodifiableList(temporaryModifiers);
    }

    // GETTER
    public String getName() { return name; }
    public int getLevel() { return level; }
    public Stats getBaseStats() { return baseStats; }
    public int getCurrentHp() { return currentHp; }
    public Equipment getEquipment() { return equipment; }

}
