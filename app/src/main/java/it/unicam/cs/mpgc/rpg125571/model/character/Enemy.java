package it.unicam.cs.mpgc.rpg125571.model.character;

import it.unicam.cs.mpgc.rpg125571.model.enums.EnemyType;
import it.unicam.cs.mpgc.rpg125571.model.item.Equipment;
import it.unicam.cs.mpgc.rpg125571.model.item.Item;

import java.util.ArrayList;
import java.util.List;

public class Enemy extends GameCharacter {

    private final EnemyType enemyType;
    private final int expReward;
    private final int goldReward;

    public Enemy(String name, int level, Stats baseStats, Equipment equipment,
                 EnemyType enemyType, int expReward, int goldReward) {
        super(name, level, baseStats, equipment);
        this.enemyType  = enemyType;
        this.expReward  = expReward;
        this.goldReward = goldReward;
    }



    // ── Getter ──────────────────────────────────────────────
    public EnemyType getEnemyType() { return enemyType; }
    public int       getExpReward() { return expReward; }
    public int       getGoldReward(){ return goldReward; }
}