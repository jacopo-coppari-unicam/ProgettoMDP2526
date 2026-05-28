package it.unicam.cs.mpgc.rpg125571.model.Character;

import it.unicam.cs.mpgc.rpg125571.model.Stats;

public abstract class GameCharacter {
    private String name;
    private int level;
    private Stats stats;
    private int currentHP; // vita del giocatore da modificare dopo ATK o HEAL

    // Costruttore
    public GameCharacter(String name, int level, Stats stats) {
        this.name = name;
        this.level = level;
        this.stats = stats;
        this.currentHP = stats.getMaxHp();
    }
    
    // TODO
    public void getDamage(int damage) {

    }

    // TODO
    public void heal(int value){

    }


}
