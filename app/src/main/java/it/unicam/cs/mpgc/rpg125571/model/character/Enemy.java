package it.unicam.cs.mpgc.rpg125571.model.character;

import it.unicam.cs.mpgc.rpg125571.logic.LootChance;
import it.unicam.cs.mpgc.rpg125571.model.item.Equipment;

import java.util.ArrayList;
import java.util.List;

public class Enemy extends GameCharacter {
    private final int expReward;
    private final int goldReward;
    private final List<LootChance> lootTable; // List loaded directly from JSON

    protected Enemy() {
        // We pass a Stats object with base values so GameCharacter
        // can calculate the initial HP without throwing a NullPointerException.
        super("Unknown Enemy", 1, new Stats(1, 1, 1), new Equipment());
        this.expReward = 0;
        this.goldReward = 0;
        this.lootTable = new ArrayList<>();
    }

    public Enemy(String name, int level, Stats baseStats, Equipment equipment,
                 int expReward, int goldReward, List<LootChance> lootTable) {
        super(name, level, baseStats, equipment);
        this.expReward = expReward;
        this.goldReward = goldReward;
        this.lootTable = lootTable;
    }

    public int getExpReward() { return expReward; }
    public int getGoldReward() { return goldReward; }
    public List<LootChance> getLootTable() { return lootTable; }
}