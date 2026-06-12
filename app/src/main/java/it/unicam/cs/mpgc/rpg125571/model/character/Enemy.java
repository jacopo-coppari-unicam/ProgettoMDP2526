package it.unicam.cs.mpgc.rpg125571.model.character;

import it.unicam.cs.mpgc.rpg125571.logic.enemy.LootChance;
import it.unicam.cs.mpgc.rpg125571.model.item.Equipment;

import java.util.ArrayList;
import java.util.List;

public class Enemy extends GameCharacter {
    private final int expReward;
    private final int goldReward;
    private final List<LootChance> lootTable; // Lista caricata direttamente da JSON

    protected Enemy() {
        // Passiamo un oggetto Stats con valori di base (es. 1) così GameCharacter
        // può calcolare gli HP iniziali senza lanciare un NullPointerException.
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