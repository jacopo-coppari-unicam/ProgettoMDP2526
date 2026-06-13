package it.unicam.cs.mpgc.rpg125571.view;

import it.unicam.cs.mpgc.rpg125571.logic.InventoryManager;
import it.unicam.cs.mpgc.rpg125571.logic.LootChance;
import it.unicam.cs.mpgc.rpg125571.logic.SaveManager;
import it.unicam.cs.mpgc.rpg125571.logic.battle.BattleManager;
import it.unicam.cs.mpgc.rpg125571.logic.battle.EnemyManager;
import it.unicam.cs.mpgc.rpg125571.model.character.Enemy;
import it.unicam.cs.mpgc.rpg125571.model.character.Player;
import it.unicam.cs.mpgc.rpg125571.model.character.Stats;
import it.unicam.cs.mpgc.rpg125571.model.character.Inventory;
import it.unicam.cs.mpgc.rpg125571.model.enums.EquipmentSlot;
import it.unicam.cs.mpgc.rpg125571.model.item.Equipment;
import it.unicam.cs.mpgc.rpg125571.model.item.Equipable;
import it.unicam.cs.mpgc.rpg125571.model.item.Item;
import it.unicam.cs.mpgc.rpg125571.model.item.Consumable;
import it.unicam.cs.mpgc.rpg125571.model.item.itemtype.Potion;
import it.unicam.cs.mpgc.rpg125571.model.skill.PlayerSkill;
import it.unicam.cs.mpgc.rpg125571.model.skill.SkillLoadout;
import it.unicam.cs.mpgc.rpg125571.storage.BossLoader;
import it.unicam.cs.mpgc.rpg125571.storage.EnemyLoader;
import it.unicam.cs.mpgc.rpg125571.storage.ItemLoader;
import it.unicam.cs.mpgc.rpg125571.storage.SkillLoader;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Controller principale per main_view.fxml.
 */
public class MainDashboardController {

    // ── TOP BAR ──────────────────────────────────────────────────────────────
    @FXML private Label goldLabel;
    @FXML private Label levelLabel;

    // ── LEFT – STATUS & EQUIPMENT ─────────────────────────────────────────────
    @FXML private Label playerNameLabel;
    @FXML private Label hpLabel;
    @FXML private ProgressBar hpBar;
    @FXML private Label atkLabel;
    @FXML private Label defLabel;

    // Tutti e 5 gli slot equipaggiamento
    @FXML private Label weaponSlotLabel;
    @FXML private Label helmetSlotLabel;
    @FXML private Label chestSlotLabel;
    @FXML private Label legsSlotLabel;
    @FXML private Label bootsSlotLabel;

    // ── CENTER – LOG & COMBAT ─────────────────────────────────────────────────
    @FXML private TextArea eventLogArea;
    @FXML private Button searchEnemyButton;
    @FXML private Button bossButton;

    // ── CENTER – INVENTORY TAB ────────────────────────────────────────────────
    @FXML private ListView<Item> inventoryListView;

    // ── RIGHT – SKILL LOADOUT ─────────────────────────────────────────────────
    @FXML private ListView<PlayerSkill> skillListView;
    @FXML private ListView<PlayerSkill> allSkillsListView;
    @FXML private Button equipSkillButton;
    @FXML private Button unequipSkillButton;

    // ── BOTTOM BAR ────────────────────────────────────────────────────────────
    @FXML private Button saveButton;
    @FXML private Button loadButton;
    @FXML private Button exitButton;

    // ── STATO INTERNO ─────────────────────────────────────────────────────────
    private Player player;
    private InventoryManager inventoryManager;
    private EnemyManager enemyManager;
    private SaveManager saveManager;
    private ItemLoader itemLoader;
    private SkillLoader skillLoader;
    private BattleManager currentBattle;
    private final Random rng = new Random();

    private static final String ITEMS_PATH   = "items.json";
    private static final String SKILLS_PATH  = "skills.json";
    private static final String ENEMIES_PATH = "enemies.json";
    private static final String BOSSES_PATH  = "boss.json";
    private static final String SAVE_PATH    = "player_save.json";

    // ─────────────────────────────────────────────────────────────────────────
    // INITIALIZE
    // ─────────────────────────────────────────────────────────────────────────

    @FXML
    public void initialize() {
        itemLoader  = new ItemLoader();
        skillLoader = new SkillLoader();

        try {
            itemLoader.loadItems(resolveResourcePath(ITEMS_PATH));
            skillLoader.loadSkills(resolveResourcePath(SKILLS_PATH));
        } catch (Exception e) {
            log("[ERRORE] Impossibile caricare il database: " + e.getMessage());
        }

        EnemyLoader enemyLoader = new EnemyLoader();
        BossLoader  bossLoader  = new BossLoader();
        try {
            enemyLoader.loadEnemies(resolveResourcePath(ENEMIES_PATH));
            bossLoader.loadBosses(resolveResourcePath(BOSSES_PATH));
        } catch (Exception e) {
            log("[ERRORE] Impossibile caricare il bestiario: " + e.getMessage());
        }

        enemyManager = new EnemyManager(enemyLoader, bossLoader);
        saveManager  = new SaveManager(itemLoader, skillLoader);

        player = buildDefaultPlayer();
        inventoryManager = new InventoryManager(player);

        autoLoad();
        setupCellFactories();
        refreshAll();

        log("⚔️  Benvenuto in RPG 125571! Buona avventura, " + player.getName() + "!");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PLAYER FACTORY
    // ─────────────────────────────────────────────────────────────────────────

    private Player buildDefaultPlayer() {
        return new Player(
                "Eroe Unicam", 1,
                new Stats(15, 5, 100),
                new Equipment(),
                new Inventory(),
                new ArrayList<>(),
                new SkillLoadout(),
                0
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // CELL FACTORIES
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Costruisce la stringa descrittiva di un item per la ListView inventario.
     * Per Weapon mostra il bonus ATK, per Armor il bonus DEF,
     * per Potion la descrizione, per Material solo il nome.
     */
    private String buildItemLabel(Item item) {
        if (item instanceof it.unicam.cs.mpgc.rpg125571.model.item.itemtype.Weapon w) {
            int atk = w.getModifiers().stream()
                    .filter(m -> m instanceof it.unicam.cs.mpgc.rpg125571.model.modifier.AtkModifier)
                    .mapToInt(m -> {
                        it.unicam.cs.mpgc.rpg125571.model.character.Stats tmp =
                                new it.unicam.cs.mpgc.rpg125571.model.character.Stats(0, 0, 1);
                        m.apply(tmp);
                        return tmp.getAtk();
                    }).sum();
            return w.getName() + "  [⚔️ Weapon  |  ATK +" + atk + "]";
        }
        if (item instanceof it.unicam.cs.mpgc.rpg125571.model.item.itemtype.Armor a) {
            int def = a.getModifiers().stream()
                    .filter(m -> m instanceof it.unicam.cs.mpgc.rpg125571.model.modifier.DefModifier)
                    .mapToInt(m -> {
                        it.unicam.cs.mpgc.rpg125571.model.character.Stats tmp =
                                new it.unicam.cs.mpgc.rpg125571.model.character.Stats(0, 0, 1);
                        m.apply(tmp);
                        return tmp.getDef();
                    }).sum();
            return a.getName() + "  [🛡️ " + a.getSlot().name() + "  |  DEF +" + def + "]";
        }
        if (item instanceof Potion p) {
            return p.getName() + "  [🧪 Pozione  |  " + p.getDescription() + "]";
        }
        return item.getName() + "  [📦 " + item.getItemType().name() + "]";
    }

    /**
     * Costruisce la label descrittiva di una PlayerSkill.
     * Mostra: nome, livello, tipo (danno/cura), elemento, valore al livello corrente.
     * withPrefix=true aggiunge l'icona tipo davanti (per allSkillsListView).
     */
    private String buildSkillLabel(PlayerSkill ps, boolean withPrefix) {
        it.unicam.cs.mpgc.rpg125571.model.skill.Skill s = ps.getSkill();
        int level = ps.getCurrentLevel();

        String typeTag;
        String valueTag;
        if (s instanceof it.unicam.cs.mpgc.rpg125571.model.skill.AttackSkill atk) {
            int dmg = atk.getDamage(level);
            typeTag  = "💥 Danno";
            valueTag = "DMG " + dmg;
        } else if (s instanceof it.unicam.cs.mpgc.rpg125571.model.skill.HealSkill heal) {
            int hp = heal.getHealAmount(level);
            typeTag  = "💚 Cura";
            valueTag = "HEAL " + hp;
        } else {
            typeTag  = "❓";
            valueTag = "";
        }

        String elem = s.getElement().name();
        String prefix = withPrefix ? typeTag + "  " : "";
        return prefix + s.getName()
                + "  [Lv." + level
                + "  |  " + elem
                + "  |  " + valueTag + "]";
    }

    private void setupCellFactories() {
        inventoryListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Item item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); return; }
                setText(buildItemLabel(item));
            }
        });

        skillListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(PlayerSkill ps, boolean empty) {
                super.updateItem(ps, empty);
                setText(empty || ps == null ? null : buildSkillLabel(ps, false));
            }
        });

        allSkillsListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(PlayerSkill ps, boolean empty) {
                super.updateItem(ps, empty);
                if (empty || ps == null) { setText(null); return; }
                boolean equipped = player.getSkillLoadout().isEquipped(ps);
                setText((equipped ? "✅ " : "   ") + buildSkillLabel(ps, true));
            }
        });
    }

    // ─────────────────────────────────────────────────────────────────────────
    // REFRESH
    // ─────────────────────────────────────────────────────────────────────────

    private void refreshAll() {
        refreshStatus();
        refreshInventory();
        refreshSkills();
    }

    private void refreshStatus() {
        Stats cur  = player.getCurrentStats();
        int   hp   = player.getCurrentHp();
        int   maxHp = cur.getMaxHp();

        playerNameLabel.setText(player.getName());
        hpLabel.setText("HP: " + hp + " / " + maxHp);
        hpBar.setProgress(maxHp > 0 ? (double) hp / maxHp : 0.0);
        atkLabel.setText(String.valueOf(cur.getAtk()));
        defLabel.setText(String.valueOf(cur.getDef()));
        levelLabel.setText("Level: " + player.getLevel());
        goldLabel.setText("Gold: " + player.getGold());

        // Tutti gli slot – mostrati sempre, vuoti o pieni
        weaponSlotLabel.setText(slotName(EquipmentSlot.WEAPON));
        helmetSlotLabel.setText(slotName(EquipmentSlot.HELMET));
        chestSlotLabel.setText(slotName(EquipmentSlot.CHEST));
        if (legsSlotLabel  != null) legsSlotLabel.setText(slotName(EquipmentSlot.LEGS));
        if (bootsSlotLabel != null) bootsSlotLabel.setText(slotName(EquipmentSlot.BOOTS));
    }

    private String slotName(EquipmentSlot slot) {
        Equipable e = player.getEquipment().getEquippedItem(slot);
        return (e != null) ? ((Item) e).getName() : "—";
    }

    private void refreshInventory() {
        inventoryListView.setItems(
                FXCollections.observableArrayList(player.getInventory().getItems()));
    }

    private void refreshSkills() {
        skillListView.setItems(
                FXCollections.observableArrayList(player.getSkillLoadout().getEquippedSkills()));
        allSkillsListView.setItems(
                FXCollections.observableArrayList(player.getSkillInventory()));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // HANDLER – INVENTARIO
    // ─────────────────────────────────────────────────────────────────────────

    /** Equipaggia l'oggetto selezionato dall'inventario. */
    @FXML
    private void handleUseItem() {
        Item selected = inventoryListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Nessun oggetto selezionato",
                    "Seleziona un oggetto dall'inventario.");
            return;
        }
        if (selected instanceof Equipable) {
            boolean ok = inventoryManager.equipItem(selected);
            if (ok) log("🛡️ Hai equipaggiato: " + selected.getName());
            else showAlert(Alert.AlertType.WARNING, "Equipaggiamento fallito",
                    "Non è stato possibile equipaggiare " + selected.getName() + ".");
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Oggetto non equipaggiabile",
                    selected.getName() + " non è un equipaggiamento.\n"
                            + "Le pozioni si usano in battaglia.");
        }
        refreshAll();
    }

    /** Disequipaggia lo slot corrispondente all'oggetto selezionato. */
    @FXML
    private void handleUnequipItem() {
        Item selected = inventoryListView.getSelectionModel().getSelectedItem();

        // Se è selezionato qualcosa nell'inventario, cerca di ricavare lo slot
        if (selected instanceof Equipable eq) {
            boolean ok = inventoryManager.unequipItem(eq.getSlot());
            if (ok) log("↩️ Hai rimosso dall'equipaggiamento: " + selected.getName());
            else showAlert(Alert.AlertType.WARNING, "Impossibile rimuovere",
                    "L'oggetto non è attualmente equipaggiato.");
            refreshAll();
            return;
        }

        // Niente selezionato: chiedi quale slot rimuovere
        List<EquipmentSlot> occupati = new ArrayList<>();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (player.getEquipment().isOccupied(slot)) occupati.add(slot);
        }
        if (occupati.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Nessun equipaggiamento",
                    "Non hai nulla equipaggiato al momento.");
            return;
        }
        List<String> scelte = occupati.stream()
                .map(s -> s.name() + " — " + slotName(s))
                .toList();
        ChoiceDialog<String> dialog = new ChoiceDialog<>(scelte.get(0), scelte);
        dialog.setTitle("Rimuovi equipaggiamento");
        dialog.setHeaderText("Quale slot vuoi liberare?");
        dialog.setContentText("Slot:");
        dialog.showAndWait().ifPresent(choice -> {
            int idx = scelte.indexOf(choice);
            EquipmentSlot slot = occupati.get(idx);
            boolean ok = inventoryManager.unequipItem(slot);
            if (ok) log("↩️ Slot " + slot.name() + " liberato.");
            refreshAll();
        });
    }

    // ─────────────────────────────────────────────────────────────────────────
    // HANDLER – SKILL LOADOUT
    // ─────────────────────────────────────────────────────────────────────────

    @FXML
    private void handleEquipSkill() {
        PlayerSkill selected = allSkillsListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Nessuna skill selezionata",
                    "Seleziona una skill da 'Tutte le Skill Sbloccate'.");
            return;
        }
        boolean ok = player.equipSkill(selected);
        if (ok) log("⚡ Skill equipaggiata: " + selected.getSkill().getName());
        else showAlert(Alert.AlertType.WARNING, "Loadout pieno",
                "Il loadout è pieno (max " + SkillLoadout.MAX_SLOTS + ") o la skill è già presente.");
        refreshSkills();
    }

    @FXML
    private void handleUnequipSkill() {
        PlayerSkill selected = skillListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Nessuna skill selezionata",
                    "Seleziona una skill dal loadout attivo.");
            return;
        }
        if (player.unequipSkill(selected))
            log("❌ Skill rimossa dal loadout: " + selected.getSkill().getName());
        refreshSkills();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // HANDLER – ESPLORAZIONE & COMBATTIMENTO
    // ─────────────────────────────────────────────────────────────────────────

    @FXML
    private void handleSearchEnemy() {
        if (currentBattle != null && !currentBattle.isBattleEnded()) {
            log("⚠️ Sei già in combattimento!"); return;
        }
        try {
            Enemy enemy = enemyManager.getRandomEnemy();
            enemy.setCurrentHp(enemy.getBaseStats().getMaxHp());
            startBattle(enemy);
        } catch (IllegalStateException e) {
            log("[ERRORE] " + e.getMessage());
        }
    }

    @FXML
    private void handleChallengeBoss() {
        if (currentBattle != null && !currentBattle.isBattleEnded()) {
            log("⚠️ Sei già in combattimento!"); return;
        }
        List<Enemy> bosses = enemyManager.getBosses();
        if (bosses.isEmpty()) { log("[ERRORE] Nessun boss caricato."); return; }

        List<String> choices = bosses.stream()
                .map(b -> b.getName() + "  (Lv." + b.getLevel() + "  HP:" + b.getBaseStats().getMaxHp() + ")")
                .toList();

        ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.setTitle("Sfida Boss");
        dialog.setHeaderText("Scegli il Boss:");
        dialog.setContentText("Boss:");
        dialog.showAndWait().ifPresent(choice -> {
            int idx = choices.indexOf(choice);
            Enemy boss = enemyManager.getBossByIndex(idx);
            if (boss != null) {
                boss.setCurrentHp(boss.getBaseStats().getMaxHp());
                startBattle(boss);
            }
        });
    }

    // ─────────────────────────────────────────────────────────────────────────
    // COMBAT ENGINE
    // ─────────────────────────────────────────────────────────────────────────

    private void startBattle(Enemy enemy) {
        currentBattle = new BattleManager(player, enemy);
        log("─────────────────────────────────────");
        log("⚔️  INIZIO BATTAGLIA!");
        log("Tu:     " + player.getName() + "  HP: " + player.getCurrentHp() + "/" + player.getCurrentStats().getMaxHp());
        log("Nemico: " + enemy.getName()  + "  HP: " + enemy.getCurrentHp()  + "/" + enemy.getBaseStats().getMaxHp());
        log("─────────────────────────────────────");
        runBattleDialog();
    }

    private void runBattleDialog() {
        if (currentBattle == null || currentBattle.isBattleEnded()) return;

        Enemy  enemy   = currentBattle.getEnemy();
        Player p       = currentBattle.getPlayer();
        List<PlayerSkill> loadout = p.getSkillLoadout().getEquippedSkills();

        List<String> actions = new ArrayList<>();
        actions.add("⚔️ Attacco Base");
        for (PlayerSkill ps : loadout)
            actions.add("🔮 " + ps.getSkill().getName() + "  (Lv." + ps.getCurrentLevel() + ")");

        List<Potion> potions = new ArrayList<>();
        for (Item item : p.getInventory().getItems())
            if (item instanceof Potion pot) potions.add(pot);
        if (!potions.isEmpty() && currentBattle.canUsePotion())
            actions.add("🧪 Usa Pozione (" + potions.get(0).getName() + ")");

        actions.add("🏃 Fuggi");

        ChoiceDialog<String> dialog = new ChoiceDialog<>(actions.get(0), actions);
        dialog.setTitle("Turno Battaglia");
        dialog.setHeaderText(
                "⚔️  " + p.getName() + "  HP: " + p.getCurrentHp() + "/" + p.getCurrentStats().getMaxHp()
                        + "\n👹  " + enemy.getName() + "  HP: " + enemy.getCurrentHp() + "/" + enemy.getBaseStats().getMaxHp());
        dialog.setContentText("Azione:");

        Optional<String> choice = dialog.showAndWait();
        if (choice.isEmpty() || choice.get().startsWith("🏃")) {
            log("🏃 Sei fuggito dalla battaglia!");
            currentBattle = null;
            refreshStatus();
            return;
        }

        String action = choice.get();

        // ── AZIONE GIOCATORE ──
        if (action.startsWith("⚔️ Attacco Base")) {
            int dmg = currentBattle.playerBaseAttack();
            log("⚔️  Attacco base: " + dmg + " danni a " + enemy.getName());
        } else if (action.startsWith("🔮")) {
            int skillIdx = actions.indexOf(action) - 1;
            if (skillIdx >= 0 && skillIdx < loadout.size()) {
                PlayerSkill ps = loadout.get(skillIdx);
                int result = currentBattle.playerSkillAction(ps);
                ps.gainMastery(10);
                log("🔮 " + ps.getSkill().getName() + ": " + result + " effetto su " + enemy.getName());
            }
        } else if (action.startsWith("🧪") && !potions.isEmpty()) {
            Potion pot = potions.get(0);
            boolean used = currentBattle.usePotion(pot);
            if (used) {
                inventoryManager.removeItem(pot);
                log("🧪 Hai usato " + pot.getName() + "!");
            } else {
                log("⚠️ Cooldown pozione attivo.");
            }
        }

        // ── CONTROLLA FINE BATTAGLIA ──
        currentBattle.checkBattleEnd();

        if (currentBattle.isBattleEnded()) {
            if (!currentBattle.isEnemyAlive()) {
                log("🏆 VITTORIA! Hai sconfitto " + enemy.getName() + "!");
                log("✨ +" + enemy.getExpReward() + " EXP  |  💰 +" + enemy.getGoldReward() + " Gold");
                rollLoot(enemy);
            } else {
                log("💀 Sei stato sconfitto da " + enemy.getName() + "...");
                log("   Recuperi le forze e torni a combattere.");
            }
            currentBattle = null;
            refreshAll();
            return;
        }

        // ── TURNO NEMICO ──
        int enemyDmg = currentBattle.enemyTurn();
        log("👹 " + enemy.getName() + " ti attacca: " + enemyDmg + " danni!");

        currentBattle.checkBattleEnd();
        refreshStatus();

        if (currentBattle.isBattleEnded()) {
            if (!currentBattle.isPlayerAlive()) {
                log("💀 Sei stato sconfitto! Il gioco continua...");
            }
            currentBattle = null;
            refreshAll();
            return;
        }

        Platform.runLater(this::runBattleDialog);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // LOOT DROP
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Calcola il loot del nemico sconfitto:
     * per ogni voce nella lootTable tira un numero random e,
     * se inferiore alla chance, aggiunge l'item all'inventario del player.
     */
    private void rollLoot(Enemy enemy) {
        if (enemy.getLootTable() == null || enemy.getLootTable().isEmpty()) return;

        List<String> dropped = new ArrayList<>();
        for (LootChance lc : enemy.getLootTable()) {
            double roll = rng.nextDouble(); // 0.0 – 1.0
            if (roll < lc.getChance()) {
                Item item = itemLoader.getById(lc.getItemId());
                if (item != null) {
                    boolean added = inventoryManager.addItem(item);
                    if (added) dropped.add(item.getName());
                }
            }
        }

        if (dropped.isEmpty()) {
            log("   Nessun loot questa volta...");
        } else {
            log("🎁 Loot ottenuto:");
            for (String name : dropped) log("   + " + name);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // HANDLER – SALVATAGGIO
    // ─────────────────────────────────────────────────────────────────────────

    @FXML
    private void handleSaveGame() {
        // Salva sempre nel file player_save.json dentro src/main/resources
        String path = resolveWritableSavePath();
        boolean ok = saveManager.saveGame(player, path);
        if (ok) {
            log("💾 Partita salvata in: " + path);
            showAlert(Alert.AlertType.INFORMATION, "Salvataggio", "Partita salvata con successo!");
        } else {
            log("[ERRORE] Impossibile salvare la partita.");
            showAlert(Alert.AlertType.ERROR, "Errore", "Non è stato possibile salvare la partita.");
        }
    }

    @FXML
    private void handleLoadGame() {
        String path = resolveResourcePath(SAVE_PATH);
        Player loaded = saveManager.loadGame(path);
        if (loaded != null) {
            this.player           = loaded;
            this.inventoryManager = new InventoryManager(loaded);
            currentBattle         = null;
            refreshAll();
            log("📂 Partita caricata! Benvenuto, " + player.getName() + "!");
            showAlert(Alert.AlertType.INFORMATION, "Caricamento",
                    "Partita di " + player.getName() + " caricata con successo!");
        } else {
            log("[ERRORE] Impossibile caricare la partita.");
            showAlert(Alert.AlertType.ERROR, "Errore",
                    "Non è stato possibile caricare il salvataggio.");
        }
    }

    @FXML
    private void handleExit() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Vuoi salvare prima di uscire?",
                ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        confirm.setTitle("Esci dal gioco");
        confirm.setHeaderText("Sei sicuro di voler uscire?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() == ButtonType.CANCEL) return;
        if (result.get() == ButtonType.YES) handleSaveGame();
        Platform.exit();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // UTILITY – PATH RESOLUTION
    // ─────────────────────────────────────────────────────────────────────────

    private void autoLoad() {
        String path = resolveResourcePath(SAVE_PATH);
        if (path == null) {
            showNewPlayerDialog();
            return;
        }
        try {
            if (Files.exists(Paths.get(path))) {
                Player loaded = saveManager.loadGame(path);
                if (loaded != null) {
                    this.player           = loaded;
                    this.inventoryManager = new InventoryManager(loaded);
                    return;
                }
            }
        } catch (Exception ignored) {}
        // Nessun save valido trovato: chiedi di creare un nuovo personaggio
        showNewPlayerDialog();
    }

    /**
     * Dialog di creazione nuovo personaggio.
     * Chiede nome, classe (che determina le stat base) e una skill iniziale.
     * Se l'utente annulla viene usato il player di default.
     */
    private void showNewPlayerDialog() {
        // ── STEP 1: nome ──
        TextInputDialog nameDialog = new TextInputDialog("Eroe Unicam");
        nameDialog.setTitle("Nuovo Personaggio");
        nameDialog.setHeaderText("Benvenuto in RPG 125571!\nNessun salvataggio trovato.");
        nameDialog.setContentText("Nome del tuo eroe:");
        Optional<String> nameResult = nameDialog.showAndWait();
        if (nameResult.isEmpty() || nameResult.get().isBlank()) return; // usa default

        String heroName = nameResult.get().trim();

        // ── STEP 2: classe ──
        List<String> classi = List.of(
                "⚔️ Guerriero  (ATK 20 / DEF 8 / HP 120)",
                "🏹 Arciere    (ATK 18 / DEF 5 / HP 100)",
                "🧙 Mago       (ATK 25 / DEF 3 / HP  80)",
                "🛡️ Paladino   (ATK 12 / DEF 12 / HP 130)"
        );
        ChoiceDialog<String> classDialog = new ChoiceDialog<>(classi.get(0), classi);
        classDialog.setTitle("Nuovo Personaggio");
        classDialog.setHeaderText("Scegli la classe per " + heroName + ":");
        classDialog.setContentText("Classe:");
        Optional<String> classResult = classDialog.showAndWait();
        if (classResult.isEmpty()) return;

        Stats stats = switch (classi.indexOf(classResult.get())) {
            case 1  -> new Stats(18,  5, 100);
            case 2  -> new Stats(25,  3,  80);
            case 3  -> new Stats(12, 12, 130);
            default -> new Stats(20,  8, 120); // Guerriero
        };

        // ── STEP 3: skill iniziale tra quelle caricate ──
        List<it.unicam.cs.mpgc.rpg125571.model.skill.Skill> allSkills = skillLoader.getSkills();
        it.unicam.cs.mpgc.rpg125571.model.skill.PlayerSkill startSkill = null;

        if (!allSkills.isEmpty()) {
            List<String> skillChoices = allSkills.stream()
                    .map(sk -> {
                        String tipo = (sk instanceof it.unicam.cs.mpgc.rpg125571.model.skill.AttackSkill)
                                ? "💥 Danno" : "💚 Cura";
                        return tipo + "  " + sk.getName()
                                + "  [" + sk.getElement().name() + "]";
                    }).toList();

            ChoiceDialog<String> skillDialog = new ChoiceDialog<>(skillChoices.get(0), skillChoices);
            skillDialog.setTitle("Nuovo Personaggio");
            skillDialog.setHeaderText("Scegli la skill di partenza per " + heroName + ":");
            skillDialog.setContentText("Skill:");
            Optional<String> skillResult = skillDialog.showAndWait();

            if (skillResult.isPresent()) {
                int idx = skillChoices.indexOf(skillResult.get());
                it.unicam.cs.mpgc.rpg125571.model.skill.Skill chosen = allSkills.get(idx);
                startSkill = new it.unicam.cs.mpgc.rpg125571.model.skill.PlayerSkill(chosen, 1, 0);
            }
        }

        // ── COSTRUISCI IL PLAYER ──
        List<it.unicam.cs.mpgc.rpg125571.model.skill.PlayerSkill> skillList = new ArrayList<>();
        if (startSkill != null) skillList.add(startSkill);

        this.player = new Player(
                heroName, 1, stats,
                new Equipment(),
                new Inventory(),
                skillList,
                new SkillLoadout(),
                0
        );

        // Equippa automaticamente la skill nel loadout
        if (startSkill != null) this.player.equipSkill(startSkill);

        this.inventoryManager = new InventoryManager(this.player);
        log("✨ Personaggio creato: " + heroName + "!");
    }

    /**
     * Percorso scrivibile per il salvataggio:
     * usa sempre src/main/resources durante lo sviluppo Gradle,
     * altrimenti la cartella corrente.
     */
    private String resolveWritableSavePath() {
        Path devPath = Paths.get("app/src/main/resources/" + SAVE_PATH);
        if (Files.exists(devPath.getParent())) return devPath.toAbsolutePath().toString();
        return Paths.get(SAVE_PATH).toAbsolutePath().toString();
    }

    /**
     * Risolve il percorso in lettura (cross-platform, Windows safe).
     */
    private String resolveResourcePath(String filename) {
        Path devPath = Paths.get("app/src/main/resources/" + filename);
        if (Files.exists(devPath)) return devPath.toAbsolutePath().toString();

        Path local = Paths.get(filename);
        if (Files.exists(local)) return local.toAbsolutePath().toString();

        var url = getClass().getClassLoader().getResource(filename);
        if (url != null) {
            try { return Paths.get(url.toURI()).toString(); }
            catch (Exception e) { return url.getPath(); }
        }
        return devPath.toAbsolutePath().toString();
    }

    private void log(String msg) {
        eventLogArea.appendText(msg + "\n");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}