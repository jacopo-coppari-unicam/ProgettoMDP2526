//package it.unicam.cs.mpgc.rpg125571.view;
//
//import it.unicam.cs.mpgc.rpg125571.logic.GameManager;
//import it.unicam.cs.mpgc.rpg125571.logic.battle.BattleManager;
//import it.unicam.cs.mpgc.rpg125571.model.character.*;
//import it.unicam.cs.mpgc.rpg125571.model.enums.*;
//import it.unicam.cs.mpgc.rpg125571.model.itemtype.*;
//import it.unicam.cs.mpgc.rpg125571.model.skill.*;
//import javafx.fxml.FXML;
//import javafx.scene.control.*;
//import javafx.collections.FXCollections;
//import java.util.ArrayList;
//import java.util.List;
//
//public class MainGuiController {
//
//    // ── Hub ──────────────────────────────────────────
//    @FXML private Label lblPlayerName;
//    @FXML private Label lblHubHp;
//    @FXML private Label lblHubExp;
//
//    // ── Inventario ───────────────────────────────────
//    @FXML private ListView<Item>     lvInventarioItems;
//    @FXML private ListView<String>   lvEquippedItems;
//
//    // ── Skill / Loadout ───────────────────────────────
//    @FXML private ListView<PlayerSkill>    lvOwnedSkills;
//    @FXML private ListView<PlayerSkill> lvActiveLoadout;
//
//    // ── Battaglia ─────────────────────────────────────
//    @FXML private javafx.scene.layout.VBox paneBattaglia;
//    @FXML private Label   lblBattlePlayerName;
//    @FXML private Label   lblBattlePlayerHp;
//    @FXML private Label   lblEnemyName;
//    @FXML private Label   lblEnemyHp;
//    @FXML private TextArea txtLogBattaglia;
//    @FXML private ListView<PlayerSkill> lvBattleSkills;
//
//    // ── Stato interno ─────────────────────────────────
//    private GameManager inventoryManager;
//    private List<Enemy>    enemyPool;
//    private int            currentEnemyIndex = 0;
//    private BattleManager  currentBattle;
//
//    // ─────────────────────────────────────────────────
//    // INIZIALIZZAZIONE (chiamata da JavaFX dopo il load)
//    // ─────────────────────────────────────────────────
//    @FXML
//    public void initialize() {
//        // Costruisci il player di partenza
//        Player player = new Player(
//                "Eroe Unicam", 1,
//                new Stats(15, 5, 100),
//                new Equipment(),
//                new Inventory(),
//                new ArrayList<>(),
//                new SkillLoadout(),
//                0
//        );
//
//        // Popola inventario di prova
//        player.getInventory().addItem(
//                new Weapon(1, "Spada di Ferro", ItemType.WEAPON, "+10 ATK", 10, EquipmentSlot.WEAPON));
//        player.getInventory().addItem(
//                new Armor(2, "Maglia di Ferro", ItemType.ARMOR, "+6 DEF", 6, EquipmentSlot.CHEST));
//
//        // Skill di prova
//        DamageSkill fireball = new DamageSkill(1, "Fire Ball", "Palla di fuoco", Element.FIRE, 5);
//        PlayerSkill psFireball = new PlayerSkill(fireball,1,0);
//        psFireball.gainMastery(500);
//        player.unlockSkill(psFireball);
//
//        inventoryManager = new GameManager(player);
//        enemyPool = EnemyLoader.loadEnemies("enemies.json");
//
//        // Imposta le ListView con celle personalizzate
//        setupCellFactories();
//
//        // Aggiorna tutta la UI con i dati iniziali
//        refreshAll();
//    }
//
//    // ─────────────────────────────────────────────────
//    // CELL FACTORIES — come ogni riga viene visualizzata
//    // ─────────────────────────────────────────────────
//    private void setupCellFactories() {
//        // Item nell'inventario → mostra nome e tipo
//        lvInventarioItems.setCellFactory(lv -> new ListCell<>() {
//            @Override protected void updateItem(Item itemtype, boolean empty) {
//                super.updateItem(itemtype, empty);
//                setText(empty || itemtype == null ? null
//                        : itemtype.getName() + "  [" + itemtype.getType() + "]");
//            }
//        });
//
//        // PlayerSkill → mostra nome, livello, mastery
//        lvOwnedSkills.setCellFactory(lv -> new ListCell<>() {
//            @Override protected void updateItem(PlayerSkill ps, boolean empty) {
//                super.updateItem(ps, empty);
//                setText(empty || ps == null ? null
//                        : ps.getSkill().getName()
//                        + "  Liv." + ps.getCurrentLevel()
//                        + "  (" + ps.getMasteryPoints() + " MP)");
//            }
//        });
//
//        // Loadout attivo → mostra nome skill
//        lvActiveLoadout.setCellFactory(lv -> new ListCell<>() {
//            @Override protected void updateItem(PlayerSkill se, boolean empty) {
//                super.updateItem(se, empty);
//                setText(empty || se == null ? null : se.getSkill().getName());
//            }
//        });
//
//        // Skill in battaglia
//        lvBattleSkills.setCellFactory(lv -> new ListCell<>() {
//            @Override protected void updateItem(PlayerSkill se, boolean empty) {
//                super.updateItem(se, empty);
//                setText(empty || se == null ? null : se.getSkill().getName());
//            }
//        });
//    }
//
//    // ─────────────────────────────────────────────────
//    // REFRESH — aggiorna tutti i pannelli
//    // ─────────────────────────────────────────────────
//    private void refreshAll() {
//        refreshHub();
//        refreshInventario();
//        refreshSkills();
//    }
//
//    private void refreshHub() {
//        Player p = inventoryManager.getPlayer();
//        lblPlayerName.setText(p.getName() + "  (Liv. " + p.getLevel() + ")");
//        lblHubHp.setText("❤️ HP: " + p.getCurrentHp() + "/" + p.getCurrentStats().getMaxHp());
//        lblHubExp.setText("✨ EXP: " + p.getExperience());
//    }
//
//    private void refreshInventario() {
//        lvInventarioItems.setItems(
//                FXCollections.observableArrayList(inventoryManager.getInventoryItems()));
//
//        // Slot equipaggiati → stringa leggibile
//        List<String> slots = new ArrayList<>();
//        for (var entry : inventoryManager.getPlayer().getEquipment().getEquippedItems()) {
//            slots.add(entry.getSlot() + ": " + entry.getName());
//        }
//        lvEquippedItems.setItems(FXCollections.observableArrayList(slots));
//    }
//
//    private void refreshSkills() {
//        lvOwnedSkills.setItems(
//                FXCollections.observableArrayList(inventoryManager.getOwnedSkills()));
//        lvActiveLoadout.setItems(
//                FXCollections.observableArrayList(inventoryManager.getActiveLoadout()));
//    }
//
//    // ─────────────────────────────────────────────────
//    // HANDLER — Hub
//    // ─────────────────────────────────────────────────
//    @FXML
//    private void handleCercaScontro() {
//        if (enemyPool == null || enemyPool.isEmpty()) {
//            log("Nessun nemico disponibile!");
//            return;
//        }
//
//        Enemy nemico = enemyPool.get(currentEnemyIndex);
//        nemico.heal(nemico.getCurrentStats().getMaxHp()); // reset HP nemico
//        currentBattle = new BattleManager(inventoryManager.getPlayer(), nemico);
//
//        // Mostra pannello battaglia
//        paneBattaglia.setVisible(true);
//        lblBattlePlayerName.setText("🛡️ " + inventoryManager.getPlayer().getName());
//        lblEnemyName.setText("👹 " + nemico.getName());
//        txtLogBattaglia.clear();
//        refreshBattleHp();
//        lvBattleSkills.setItems(
//                FXCollections.observableArrayList(inventoryManager.getActiveLoadout()));
//
//        log("Lo scontro ha inizio! " + nemico.getName() + " ti sfida!");
//    }
//
//    // ─────────────────────────────────────────────────
//    // HANDLER — Inventario
//    // ─────────────────────────────────────────────────
//    @FXML
//    private void handleEquipaggiaItem() {
//        Item selected = lvInventarioItems.getSelectionModel().getSelectedItem();
//        if (selected == null) return;
//
//        boolean ok = inventoryManager.equipItemFromInventory(selected);
//        if (ok) refreshInventario();
//        else showAlert("Questo oggetto non può essere equipaggiato.");
//    }
//
//    // ─────────────────────────────────────────────────
//    // HANDLER — Skill
//    // ─────────────────────────────────────────────────
//    @FXML
//    private void handleEquipaggiaSkill() {
//        PlayerSkill selected = lvOwnedSkills.getSelectionModel().getSelectedItem();
//        if (selected == null) return;
//
//        boolean ok = inventoryManager.equipSkillToLoadout(selected);
//        if (ok) refreshSkills();
//        else showAlert("Loadout pieno (max 3) o skill già equipaggiata.");
//    }
//
//    @FXML
//    private void handleRimuoviSkill() {
//        PlayerSkill selected = lvActiveLoadout.getSelectionModel().getSelectedItem();
//        if (selected == null || !(selected instanceof PlayerSkill ps)) return;
//
//        inventoryManager.unequipSkillFromLoadout(ps);
//        refreshSkills();
//    }
//
//    // ─────────────────────────────────────────────────
//    // HANDLER — Battaglia
//    // ─────────────────────────────────────────────────
//    @FXML
//    private void handleAttaccoBase() {
//        if (currentBattle == null) return;
//        currentBattle.executePlayerBaseAttack();
//        log("⚔️ Attacco base!");
//        afterAction();
//    }
//
//    @FXML
//    private void handleUsaSkill() {
//        if (currentBattle == null) return;
//        int idx = lvBattleSkills.getSelectionModel().getSelectedIndex();
//        if (idx < 0) { showAlert("Seleziona una skill dalla lista."); return; }
//
//        currentBattle.executePlayerSkill(idx);
//        String skillName = inventoryManager.getActiveLoadout().get(idx).getSkill().getName();
//        log("🔮 Skill usata: " + skillName);
//        afterAction();
//    }
//
//    // ─────────────────────────────────────────────────
//    // UTILITY — Battaglia
//    // ─────────────────────────────────────────────────
//    private void afterAction() {
//        refreshBattleHp();
//        refreshHub();
//
//        switch (currentBattle.getState()) {
//            case PLAYER_WON -> {
//                log("🏆 Vittoria! Hai sconfitto " + currentBattle.getEnemy().getName() + "!");
//                log("✨ +" + currentBattle.getEnemy().getExpReward() + " EXP  |  💰 +" + currentBattle.getEnemy().getGoldReward() + " Gold");
//                inventoryManager.getPlayer().heal(30);
//                currentEnemyIndex = (currentEnemyIndex + 1) % enemyPool.size();
//                endBattle();
//            }
//            case ENEMY_WON -> {
//                log("💀 Sei stato sconfitto... GAME OVER.");
//                endBattle();
//            }
//            default -> { /* battaglia ancora in corso */ }
//        }
//    }
//
//    private void refreshBattleHp() {
//        Player p  = inventoryManager.getPlayer();
//        Enemy  e  = currentBattle.getEnemy();
//        lblBattlePlayerHp.setText("HP: " + p.getCurrentHp() + "/" + p.getCurrentStats().getMaxHp());
//        lblEnemyHp.setText("HP: " + e.getCurrentHp() + "/" + e.getCurrentStats().getMaxHp());
//    }
//
//    private void endBattle() {
//        currentBattle = null;
//        paneBattaglia.setVisible(false);
//        refreshAll();
//    }
//
//    private void log(String msg) {
//        txtLogBattaglia.appendText(msg + "\n");
//    }
//
//    private void showAlert(String msg) {
//        Alert alert = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
//        alert.showAndWait();
//    }
//}