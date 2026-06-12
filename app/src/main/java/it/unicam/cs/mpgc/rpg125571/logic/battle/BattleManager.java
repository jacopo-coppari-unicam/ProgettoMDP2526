package it.unicam.cs.mpgc.rpg125571.logic.battle;

import it.unicam.cs.mpgc.rpg125571.logic.LootChance;
import it.unicam.cs.mpgc.rpg125571.model.character.Enemy;
import it.unicam.cs.mpgc.rpg125571.model.character.Player;
import it.unicam.cs.mpgc.rpg125571.model.character.Stats;
import it.unicam.cs.mpgc.rpg125571.model.enums.BattleState;
import it.unicam.cs.mpgc.rpg125571.model.skill.PlayerSkill;

public class BattleManager {
    private final Player player;
    private final Enemy enemy;
    private BattleState state;

    public BattleManager(Player player, Enemy enemy) {
        this.player = player;
        this.enemy = enemy;
        this.state = BattleState.IN_PROGRESS;
    }

    /**
     * Esegue un attacco base del giocatore verso il nemico.
     * Calcola il danno polimorficamente tramite le statistiche correnti (con modificatori).
     * Se il nemico muore, assegna automaticamente i premi al giocatore.
     */
    public void executePlayerBaseAttack() {
        if (state != BattleState.IN_PROGRESS) return;

        Stats playerStats = player.getCurrentStats();
        Stats enemyStats = enemy.getCurrentStats();

        // Formula danno: Attacco - Difesa (Almeno 1 danno garantito)
        int damage = Math.max(1, playerStats.getAtk() - enemyStats.getDef());
        enemy.takeDamage(damage);

        checkBattleStatus();

        // Se il nemico è ancora vivo, risponde immediatamente
        if (state == BattleState.IN_PROGRESS) {
            executeEnemyTurn();
        }
    }

    /**
     * Consuma il turno del giocatore lanciando una skill equipaggiata.
     * * @param skillIndex L'indice della skill all'interno del loadout (0, 1 o 2)
     * @throws IllegalArgumentException se l'indice non corrisponde a una skill attiva
     */
    public void executePlayerSkill(int skillIndex) {
        if (state != BattleState.IN_PROGRESS) return;

        var equippedSkills = player.getSkillLoadout().getEquippedSkills();
        if (skillIndex < 0 || skillIndex >= equippedSkills.size()) {
            throw new IllegalArgumentException("Nessuna skill presente all'indice specificato");
        }

        PlayerSkill selectedSkill = equippedSkills.get(skillIndex);

        // Esegue il cast logico della skill (può fare danno al nemico o curare il player)
        // Usiamo un downcast sicuro per applicare la mastery
        if (selectedSkill instanceof it.unicam.cs.mpgc.rpg125571.model.skill.PlayerSkill ps) {
            ps.getSkill().cast(player, enemy, ps.getCurrentLevel(), 0);
            ps.gainMastery(25); // Avanzamento maestria
        } else {
            selectedSkill.getSkill().cast(player, enemy, 1, 0);
        }

        checkBattleStatus();

        // Se il nemico sopravvive al cast della skill, esegue il suo contrattacco
        if (state == BattleState.IN_PROGRESS) {
            executeEnemyTurn();
        }
    }

    /**
     * Gestisce la logica di attacco del nemico ed esegue i passaggi di fine turno
     * (come il decremento dei buff temporanei).
     */
    private void executeEnemyTurn() {
        Stats enemyStats = enemy.getCurrentStats();
        Stats playerStats = player.getCurrentStats();

        int damage = Math.max(1, enemyStats.getAtk() - playerStats.getDef());
        player.takeDamage(damage);

        // Fase di Fine Turno: i modificatori temporanei perdono 1 turno di durata
        player.tickTemporaryModifiers();
        enemy.tickTemporaryModifiers();

        checkBattleStatus();
    }

    /**
     * Esamina la vitalità dei combattenti per aggiornare lo stato ed erogare ricompense.
     */
    public boolean checkBattleStatus() {
        if (enemy.isDead()) {
            this.state = BattleState.PLAYER_WON;
            finalizeBattle();
            return true;
        } else if (player.isDead()) {
            this.state = BattleState.ENEMY_WON;
            finalizeBattle();
            return true;
        }
        return false;
    }

    /**
     * Operazioni di pulizia e distribuzione dell'esperienza a incontro concluso.
     */
    /**
     * Operazioni di pulizia, distribuzione dell'esperienza e calcolo del Loot
     * a incontro concluso.
     */
    private void finalizeBattle() {
        // 1. Rimuove tutti i buff/debuff temporanei accumulati durante lo scontro
        player.clearTemporaryModifiers();
        enemy.clearTemporaryModifiers();

        // 2. Se ha vinto il giocatore, distribuiamo le ricompense caricate da JSON
        if (state == BattleState.PLAYER_WON) {
            // Assegna l'esperienza
            player.addExperience(enemy.getExpReward());

            // Assegna l'oro (Assicurati di avere una variabile o un metodo per l'oro nel Player, es: player.addGold())
            player.addGold(enemy.getGoldReward());

            // 3. Calcolo del Loot basato sulle percentuali della LootTable
            for (LootChance loot : enemy.getLootTable()) {
                // Math.random() genera un numero tra 0.0 e 1.0.
                // Se è minore o uguale alla chance (es. 0.30 per il 30%), il drop ha successo!
                if (Math.random() <= loot.getChance()) {

                    // Recupera l'oggetto reale tramite l'ID usando il tuo catalogo o ItemPool
                    // e aggiungilo direttamente all'inventario del giocatore

                    // Item itemDroppato = ItemCatalog.getById(loot.getItemId());
                    // player.getInventory().addItem(itemDroppato);
                }
            }
        }
    }

    // GETTER METODI PER CONNETTERE INTERFACCE O TEST
    public BattleState getState() { return state; }
    public Player getPlayer() { return player; }
    public Enemy getEnemy() { return enemy; }
}