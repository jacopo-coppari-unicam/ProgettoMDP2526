package it.unicam.cs.mpgc.rpg125571.view;

import it.unicam.cs.mpgc.rpg125571.logic.InventoryManager;
import it.unicam.cs.mpgc.rpg125571.logic.SaveManager;
import it.unicam.cs.mpgc.rpg125571.logic.battle.EnemyManager;
import it.unicam.cs.mpgc.rpg125571.model.character.Player;
import it.unicam.cs.mpgc.rpg125571.storage.BossLoader;
import it.unicam.cs.mpgc.rpg125571.storage.EnemyLoader;
import it.unicam.cs.mpgc.rpg125571.storage.ItemLoader;
import it.unicam.cs.mpgc.rpg125571.storage.SkillLoader;
import javafx.fxml.FXML;

import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// Main orchestrator controller for the game dashboard view.
// Serves as the central context hub, coordinating communication between sub-views.
public class MainDashboardController {

    @FXML private StatusController    statusViewController;
    @FXML private CharacterController characterViewController;
    @FXML private InventoryController inventoryViewController;
    @FXML private SkillController     skillViewController;
    @FXML private CombatController    combatViewController;
    @FXML private SaveSystemController saveSystemViewController;

    private Player           player;
    private InventoryManager inventoryManager;
    private EnemyManager     enemyManager;
    private SaveManager      saveManager;
    private ItemLoader       itemLoader;
    private SkillLoader      skillLoader;

    // Standard relative paths for project asset files
    private static final String RESOURCES_DIR = "src/main/resources/";
    private static final String ITEMS_PATH    = "items.json";
    private static final String SKILLS_PATH   = "skills.json";
    private static final String ENEMIES_PATH  = "enemies.json";
    private static final String BOSSES_PATH   = "boss.json";

    @FXML
    public void initialize() {
        // Instantiate the primary item and skill data registries
        itemLoader  = new ItemLoader();
        skillLoader = new SkillLoader();

        try {
            itemLoader.loadItems(resolveResourcePath(ITEMS_PATH));
            skillLoader.loadSkills(resolveResourcePath(SKILLS_PATH));
        } catch (Exception e) {
            System.err.println("[ERRORE] Impossibile caricare il database: " + e.getMessage());
        }

        // Initialize local bestiary and boss file parsers
        EnemyLoader enemyLoader = new EnemyLoader();
        BossLoader  bossLoader  = new BossLoader();
        try {
            enemyLoader.loadEnemies(resolveResourcePath(ENEMIES_PATH));
            bossLoader.loadBosses(resolveResourcePath(BOSSES_PATH));
        } catch (Exception e) {
            System.err.println("[ERRORE] Impossibile caricare il bestiario: " + e.getMessage());
        }

        // Bind data loaders directly into operational management facades
        enemyManager = new EnemyManager(enemyLoader, bossLoader);
        saveManager  = new SaveManager(itemLoader, skillLoader);

        statusViewController.setMainContext(this);
        characterViewController.setMainContext(this);
        inventoryViewController.setMainContext(this);
        skillViewController.setMainContext(this);
        combatViewController.setMainContext(this);
        saveSystemViewController.setMainContext(this);

        saveSystemViewController.autoLoad();
    }

    // Updates the shared player context instance and reinitializes inventory logic
    public void updatePlayerContext(Player newPlayer) {
        this.player           = newPlayer;
        this.inventoryManager = new InventoryManager(newPlayer);
        refreshAllViews();
    }

    public void refreshAllViews() {
        statusViewController.refresh();
        characterViewController.refresh();
        inventoryViewController.refresh();
        skillViewController.refresh();
    }

    public void log(String msg) {
        combatViewController.log(msg);
    }

    // --- Path resolution ---

    // Resolves a read-only resource path: checks the dev tree first,
    // then the working directory, then the classpath.
    public String resolveResourcePath(String filename) {
        Path devPath = Paths.get(RESOURCES_DIR + filename);
        if (Files.exists(devPath)) return devPath.toAbsolutePath().toString();

        Path local = Paths.get(filename);
        if (Files.exists(local)) return local.toAbsolutePath().toString();

        var url = getClass().getClassLoader().getResource(filename);
        if (url != null) {
            try { return Paths.get(url.toURI()).toString(); }
            catch (URISyntaxException e) { return url.getPath(); }
        }

        return devPath.toAbsolutePath().toString();
    }

    // Resolves a writable save path: always writes inside the dev resources folder.
    public String resolveWritableSavePath(String filename) {
        return resolveResourcePath(filename);
    }

    // --- Getters ---
    public Player           getPlayer()           { return player; }
    public InventoryManager getInventoryManager() { return inventoryManager; }
    public EnemyManager     getEnemyManager()     { return enemyManager; }
    public SaveManager      getSaveManager()      { return saveManager; }
    public ItemLoader       getItemLoader()       { return itemLoader; }
    public SkillLoader      getSkillLoader()      { return skillLoader; }
}