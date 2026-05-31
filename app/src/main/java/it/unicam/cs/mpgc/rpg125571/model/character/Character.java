package it.unicam.cs.mpgc.rpg125571.model.character;

import it.unicam.cs.mpgc.rpg125571.model.character.Stats;
import it.unicam.cs.mpgc.rpg125571.model.item.Equipment;

public abstract class Character implements Damageable {
    private String name;
    private int level;
    private Stats baseStats;
    private int currentHp;
    Equipment equipment;

    public Character(String name, int level, Stats baseStats, Equipment equipment) {
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

    public Stats getBaseStats() { return baseStats; }
    public Equipment getEquipment() { return equipment; }
}
