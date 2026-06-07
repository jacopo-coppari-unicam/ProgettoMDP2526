package it.unicam.cs.mpgc.rpg125571.model.character;

import it.unicam.cs.mpgc.rpg125571.model.modifier.Modifier;
import it.unicam.cs.mpgc.rpg125571.model.item.Equipment;

import java.util.ArrayList;
import java.util.List;

public abstract class GameCharacter implements Damageable {
    private final String name;
    private final int level;
    private final Stats baseStats;
    private int currentHp;
    Equipment equipment;

    public GameCharacter(String name, int level, Stats baseStats, Equipment equipment) {
        this.name = name;
        this.level = level;
        this.baseStats = baseStats;
        this.equipment = equipment;
        this.currentHp = baseStats.getMaxHp();
    }

    public void heal(int value){
        if((currentHp + value) <= baseStats.getMaxHp()) currentHp += value;
        else currentHp = baseStats.getMaxHp();
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

    public String getName() { return name; }
    public int getLevel() { return level; }
    public Stats getBaseStats() { return baseStats; }
    public int getCurrentHp() { return currentHp; }
    public Equipment getEquipment() { return equipment; }

    // Buff temporanei:
    //  - Potion
    //  - (Future)
    private final List<Modifier> temporaryModifiers = new ArrayList<>();

    public void addTemporaryModifier(Modifier modifier) {
        temporaryModifiers.add(modifier);
    }

    public List<Modifier> getTemporaryModifiers() {
        return temporaryModifiers;
    }

    public void clearTemporaryModifiers() {
        temporaryModifiers.clear();
    }
}
