package it.unicam.cs.mpgc.rpg125571.view;

import it.unicam.cs.mpgc.rpg125571.logic.LootChance;
import it.unicam.cs.mpgc.rpg125571.logic.battle.BattleManager;
import it.unicam.cs.mpgc.rpg125571.model.character.Enemy;
import it.unicam.cs.mpgc.rpg125571.model.character.Player;
import it.unicam.cs.mpgc.rpg125571.model.item.Item;
import it.unicam.cs.mpgc.rpg125571.model.item.itemtype.Potion;
import it.unicam.cs.mpgc.rpg125571.model.skill.PlayerSkill;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

// Controller for the combat UI.
// Manages battle initiation (enemies and bosses), the turn loop via dialogs, and loot distribution.
public class CombatController {

    @FXML private TextArea eventLogArea;

    private MainDashboardController mainContext;
    private BattleManager currentBattle;
    private final Random rng = new Random();

    public void setMainContext(MainDashboardController mainContext) {
        this.mainContext = mainContext;
    }

    public void log(String msg) {
        if (eventLogArea != null)
            eventLogArea.appendText(msg + "\n");
    }

    @FXML
    private void handleSearchEnemy() {
        if (isBattleActive()) { mainContext.log("[WARNING] Sei già in combattimento!"); return; }
        try {
            Enemy enemy = mainContext.getEnemyManager().getRandomEnemy();
            enemy.setCurrentHp(enemy.getBaseStats().getMaxHp());
            startBattle(enemy);
        } catch (IllegalStateException e) {
            mainContext.log("[ERRORE] " + e.getMessage());
        }
    }

    @FXML
    private void handleChallengeBoss() {
        if (isBattleActive()) { mainContext.log("[WARNING] Sei già in combattimento!"); return; }

        List<Enemy> bosses = mainContext.getEnemyManager().getBosses();
        if (bosses.isEmpty()) { mainContext.log("[ERRORE] Nessun boss caricato."); return; }

        List<String> choices = bosses.stream()
                .map(b -> b.getName() + "  (Lv." + b.getLevel() + "  HP:" + b.getBaseStats().getMaxHp() + ")")
                .toList();

        ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.showAndWait().ifPresent(choice -> {
            Enemy boss = mainContext.getEnemyManager().getBossByIndex(choices.indexOf(choice));
            if (boss != null) {
                boss.setCurrentHp(boss.getBaseStats().getMaxHp());
                startBattle(boss);
            }
        });
    }

    private void startBattle(Enemy enemy) {
        currentBattle = new BattleManager(mainContext.getPlayer(), enemy);
        mainContext.log("─────────────────────────────────────");
        mainContext.log("[START]  INIZIO BATTAGLIA!");
        mainContext.log("Tu:     " + mainContext.getPlayer().getName() + "  HP: " + mainContext.getPlayer().getCurrentHp());
        mainContext.log("Nemico: " + enemy.getName() + "  HP: " + enemy.getCurrentHp());
        mainContext.log("─────────────────────────────────────");
        runBattleDialog();
    }

    private void runBattleDialog() {
        if (!isBattleActive()) return;

        Player player    = currentBattle.getPlayer();
        Enemy  enemy     = currentBattle.getEnemy();
        List<PlayerSkill> loadout = player.getSkillLoadout().getEquippedSkills();

        List<String> actions = buildActionList(player, loadout);

        ChoiceDialog<String> dialog = new ChoiceDialog<>(actions.get(0), actions);
        dialog.setTitle("Turno Battaglia");
        dialog.setHeaderText("[PLAYER] " + player.getName() + " HP: " + player.getCurrentHp()
                + "\n[ENEMY] " + enemy.getName() + " HP: " + enemy.getCurrentHp());

        Optional<String> choice = dialog.showAndWait();
        if (choice.isEmpty() || choice.get().startsWith("[RUN]")) {
            mainContext.log("[RUN] Sei fuggito dalla battaglia!");
            currentBattle = null;
            mainContext.refreshAllViews();
            return;
        }

        handlePlayerAction(choice.get(), loadout, actions);
        currentBattle.checkBattleEnd();

        if (currentBattle.isBattleEnded()) {
            handleBattleEnd(enemy);
            return;
        }

        int enemyDmg = currentBattle.enemyTurn();
        mainContext.log("[ENEMY] " + enemy.getName() + " ti attacca: " + enemyDmg + " danni!");
        currentBattle.checkBattleEnd();
        mainContext.refreshAllViews();

        if (currentBattle.isBattleEnded()) {
            currentBattle = null;
            return;
        }
        Platform.runLater(this::runBattleDialog);
    }

    private List<String> buildActionList(Player player, List<PlayerSkill> loadout) {
        List<String> actions = new ArrayList<>();
        actions.add("[N] Attacco Base");
        for (PlayerSkill ps : loadout)
            actions.add("[S] " + ps.getSkill().getName() + "  (Lv." + ps.getCurrentLevel() + ")");

        boolean hasPotion = player.getInventory().getItems().stream().anyMatch(i -> i instanceof Potion);
        if (hasPotion && currentBattle.canUsePotion()) {
            Potion pot = (Potion) player.getInventory().getItems().stream()
                    .filter(i -> i instanceof Potion).findFirst().orElseThrow();
            actions.add("[P] Usa Pozione (" + pot.getName() + ")");
        }
        actions.add("[RUN] Fuggi");
        return actions;
    }

    private void handlePlayerAction(String action, List<PlayerSkill> loadout, List<String> actions) {
        Player player = currentBattle.getPlayer();

        if (action.startsWith("[N]")) {
            int dmg = currentBattle.playerBaseAttack();
            mainContext.log("[N] Attacco base: " + dmg + " danni a " + currentBattle.getEnemy().getName());

        } else if (action.startsWith("[S]")) {
            int skillIdx  = actions.indexOf(action) - 1;
            PlayerSkill ps = loadout.get(skillIdx);
            int result    = currentBattle.playerSkillAction(ps);
            ps.gainMastery(10);
            mainContext.log("[S] " + ps.getSkill().getName() + ": " + result + " effetto.");

        } else if (action.startsWith("[P]")) {
            player.getInventory().getItems().stream()
                    .filter(i -> i instanceof Potion)
                    .findFirst()
                    .map(i -> (Potion) i)
                    .ifPresent(pot -> {
                        if (currentBattle.usePotion(pot)) {
                            mainContext.getInventoryManager().removeItem(pot);
                            mainContext.log("[P] Hai usato " + pot.getName() + "!");
                        }
                    });
        }
    }

    private void handleBattleEnd(Enemy enemy) {
        if (!currentBattle.isEnemyAlive()) {
            mainContext.log("[VICTORY] VITTORIA! Sconfitto " + enemy.getName() + "!");
            mainContext.log("[EXP] +" + enemy.getExpReward() + " EXP  |  [GOLD] +" + enemy.getGoldReward() + " Gold");
            rollLoot(enemy);
        } else {
            mainContext.log("[DEAD] Sei stato sconfitto da " + enemy.getName() + "...");
        }
        currentBattle = null;
        mainContext.refreshAllViews();
    }

    private void rollLoot(Enemy enemy) {
        if (enemy.getLootTable() == null || enemy.getLootTable().isEmpty()) return;
        for (LootChance lc : enemy.getLootTable()) {
            if (rng.nextDouble() < lc.getChance()) {
                Item item = mainContext.getItemLoader().getById(lc.getItemId());
                if (item != null && mainContext.getInventoryManager().addItem(item))
                    mainContext.log("[LOOT] Loot ottenuto: " + item.getName());
            }
        }
    }

    private boolean isBattleActive() {
        return currentBattle != null && !currentBattle.isBattleEnded();
    }
}