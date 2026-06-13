package it.unicam.cs.mpgc.rpg125571.logic.battle;

import it.unicam.cs.mpgc.rpg125571.model.character.Enemy;
import it.unicam.cs.mpgc.rpg125571.storage.BossLoader;
import it.unicam.cs.mpgc.rpg125571.storage.EnemyLoader;

import java.util.List;
import java.util.Random;

public class EnemyManager {
    private final List<Enemy> enemies;
    private final List<Enemy> bosses;
    private final Random random = new Random();

    public EnemyManager(EnemyLoader enemyLoader, BossLoader bossLoader) {
        // We extract lists directly from loaders at initialization time
        this.enemies = enemyLoader.getEnemies();
        this.bosses = bossLoader.getBosses();
    }

    // Search Enemy: Extracts a random enemy from the database (Level <= 10)
    public Enemy getRandomEnemy() {
        if (enemies.isEmpty()) {
            throw new IllegalStateException("No common enemies loaded into the database!");
        }
        int index = random.nextInt(enemies.size());
        return enemies.get(index);
    }

    // Challenge Bosses: Returns a specific boss based on the user's selection.
    // The index of the boss selected in the UI (0 to 4).
    public Enemy getBossByIndex(int index) {
        if (index < 0 || index >= bosses.size()) {
            return null;
        }
        return bosses.get(index);
    }

    // getter if JavaFX needs to display the list of available bosses
    public List<Enemy> getBosses() {
        return bosses;
    }
}
