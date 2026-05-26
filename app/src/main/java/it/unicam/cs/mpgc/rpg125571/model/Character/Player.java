package it.unicam.cs.mpgc.rpg125571.model.Character;

import it.unicam.cs.mpgc.rpg125571.model.Collectory.Inventory;
import it.unicam.cs.mpgc.rpg125571.model.PlayerSkill;
import it.unicam.cs.mpgc.rpg125571.model.Stats;

public class Player extends GameCharacter {
    private Inventory inventory = new Inventory();
    private PlayerSkill playerSkill = new PlayerSkill();

    public Player(String name, int level, Stats stats) {
        super(name, level, stats);
    }
}
