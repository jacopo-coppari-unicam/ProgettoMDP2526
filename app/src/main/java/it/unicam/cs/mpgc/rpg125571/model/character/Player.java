package it.unicam.cs.mpgc.rpg125571.model.character;

import it.unicam.cs.mpgc.rpg125571.model.item.Equipment;

public class Player extends GameCharacter {
//    private Inventory inventory;
//    private PlayerSkill playerSkill;

    public Player(String name, int level, Stats baseStats, Equipment equipment) {
        super(name, level, baseStats, equipment);
    }
}