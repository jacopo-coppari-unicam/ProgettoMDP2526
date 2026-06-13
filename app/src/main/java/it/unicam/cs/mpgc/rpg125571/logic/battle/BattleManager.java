package it.unicam.cs.mpgc.rpg125571.logic.battle;

import it.unicam.cs.mpgc.rpg125571.model.character.Enemy;
import it.unicam.cs.mpgc.rpg125571.model.character.Player;
import it.unicam.cs.mpgc.rpg125571.model.item.itemtype.Potion;
import it.unicam.cs.mpgc.rpg125571.model.skill.AttackSkill;
import it.unicam.cs.mpgc.rpg125571.model.skill.HealSkill;
import it.unicam.cs.mpgc.rpg125571.model.skill.PlayerSkill;

// Manages the flow of battle between Player and Enemy.
public class BattleManager {

    private final Player player;
    private final Enemy  enemy;

    // Cooldown pozione: può essere usata ogni 2 attacchi
    private int attacksSinceLastPotion = 2;
    private boolean battleEnded = false;

    public BattleManager(Player player, Enemy enemy) {
        this.player = player;
        this.enemy  = enemy;
    }

    // PLAYER ACTION

    // Basic Attack: Uses ATK from the player's current stats (includes equipment modifiers)
    public int playerBaseAttack() {
        if (battleEnded) return 0;

        // getCurrentStats() include equipment bonus
        int atk    = player.getCurrentStats().getAtk();
        int def    = enemy.getBaseStats().getDef();
        int damage = Math.max(1, atk - def);

        enemy.takeDamage(damage);
        attacksSinceLastPotion++;
        return damage;
    }

    /**
     * Usa una skill dal loadout.
     * Cast delega alla skill concreta tramite Skill.cast(); in più calcola
     * il danno/cura e lo applica al target corretto.
     *
     * @param playerSkill la skill scelta dal loadout
     * @return valore numerico dell'effetto (danno o cura), 0 se tipo sconosciuto
     */
    public int playerSkillAction(PlayerSkill playerSkill) {
        if (battleEnded) return 0;

        int level  = playerSkill.getCurrentLevel();

        if (playerSkill.getSkill() instanceof AttackSkill attackSkill) {
            int def    = enemy.getBaseStats().getDef();
            int raw    = attackSkill.getDamage(level);
            int damage = Math.max(1, raw - def);
            enemy.takeDamage(damage);
            attacksSinceLastPotion++;
            return damage;
        } else if (playerSkill.getSkill() instanceof HealSkill healSkill) {
            int amount = healSkill.getHealAmount(level);
            player.heal(amount);
            attacksSinceLastPotion++;
            return amount;
        }

        return 0;
    }

    // The potion can only be used every 2 attacks.
    public boolean canUsePotion() {
        return attacksSinceLastPotion >= 2;
    }

    // Use a potion on the player, apply TemporaryModifiers to the GameCharacter
    public boolean usePotion(Potion potion) {
        if (battleEnded || !canUsePotion()) return false;
        potion.use(player);          // Consumable.use(GameCharacter)
        attacksSinceLastPotion = 0;
        return true;
    }


    // ENEMY TURN

    // The enemy attacks the player
    // Uses the enemy's base stats and the player's current stats (including temporary buffs)..
    public int enemyTurn() {
        if (battleEnded || enemy.isDead()) return 0;

        int atk    = enemy.getBaseStats().getAtk();
        int def    = player.getCurrentStats().getDef();
        int damage = Math.max(1, atk - def);

        player.takeDamage(damage);

        player.tickTemporaryModifiers();

        return damage;
    }

    // Checks whether the battle is over and handles the consequences.
    public void checkBattleEnd() {
        if (battleEnded) return;

        if (enemy.isDead()) {
            onVictory();
            battleEnded = true;
        } else if (player.isDead()) {
            onDefeat();
            battleEnded = true;
        }
    }

    public boolean isBattleEnded() { return battleEnded; }
    public boolean isPlayerAlive()  { return !player.isDead(); }
    public boolean isEnemyAlive()   { return !enemy.isDead(); }

    public int getPotionCooldown() {
        return Math.max(0, 2 - attacksSinceLastPotion);
    }

    // END OF THE BATTLE

    private void onVictory() {
        player.addExperience(enemy.getExpReward());
        player.addGold(enemy.getGoldReward());
        // Restores HP to the current maximum (includes equipment bonuses)
        player.setCurrentHp(player.getCurrentStats().getMaxHp());
        player.clearTemporaryModifiers();
    }

    private void onDefeat() {
        // Restores HP to allow the player to continue
        player.setCurrentHp(player.getBaseStats().getMaxHp());
        player.clearTemporaryModifiers();
    }

    // UI USEFUL GETTERS

    public Player getPlayer() { return player; }
    public Enemy  getEnemy()  { return enemy;  }
}