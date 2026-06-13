package it.unicam.cs.mpgc.rpg125571.view;

import it.unicam.cs.mpgc.rpg125571.model.character.Inventory;
import it.unicam.cs.mpgc.rpg125571.model.character.Player;
import it.unicam.cs.mpgc.rpg125571.model.character.Stats;
import it.unicam.cs.mpgc.rpg125571.model.item.Equipment;
import it.unicam.cs.mpgc.rpg125571.model.skill.PlayerSkill;
import it.unicam.cs.mpgc.rpg125571.model.skill.Skill;
import it.unicam.cs.mpgc.rpg125571.model.skill.SkillLoadout;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Controller for managing persistence, saving, loading, and new player setup
// Coordinates interactions between the user interface and file storage management
public class SaveSystemController {

    private MainDashboardController mainContext;

    // Coordinates interactions between the user interface and file storage management
    private static final String SAVE_FILE = "resources/player_save.json";

    private static final List<String> CLASS_OPTIONS = List.of(
            "Guerriero  (ATK 20 / DEF 8 / HP 120)",
            "Arciere    (ATK 18 / DEF 5 / HP 100)",
            "Mago       (ATK 25 / DEF 3 / HP  80)"
    );

    public void setMainContext(MainDashboardController mainContext) {
        this.mainContext = mainContext;
    }

    // Tries to load an existing save; falls back to the new-player dialog.
    public void autoLoad() {
        String path = mainContext.resolveResourcePath(SAVE_FILE);
        try {
            if (path != null && Files.exists(Paths.get(path))) {
                Player loaded = mainContext.getSaveManager().loadGame(path);
                if (loaded != null) {
                    mainContext.updatePlayerContext(loaded);
                    return;
                }
            }
        } catch (Exception ignored) {}
        showNewPlayerDialog();
    }

    // Save game
    @FXML
    private void handleSaveGame() {
        String path = mainContext.resolveWritableSavePath(SAVE_FILE);
        if (mainContext.getSaveManager().saveGame(mainContext.getPlayer(), path))
            mainContext.log("[SAVED] Partita salvata in: " + path);
    }

    // Upload the saved progress
    @FXML
    private void handleLoadGame() {
        String path   = mainContext.resolveResourcePath(SAVE_FILE);
        Player loaded = mainContext.getSaveManager().loadGame(path);
        if (loaded != null) {
            mainContext.updatePlayerContext(loaded);
            mainContext.log("[UPLOADED] Partita caricata! Benvenuto, " + loaded.getName() + "!");
        }
    }

    // Manages application shutdown
    @FXML
    private void handleExit() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Vuoi salvare prima di uscire?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        confirm.showAndWait().ifPresent(result -> {
            if (result == ButtonType.YES)    handleSaveGame();
            if (result != ButtonType.CANCEL) Platform.exit();
        });
    }

    // New player creation
    private void showNewPlayerDialog() {
        Optional<String> nameResult = askHeroName();
        if (nameResult.isEmpty() || nameResult.get().isBlank()) {
            mainContext.updatePlayerContext(buildDefaultPlayer());
            return;
        }

        String heroName = nameResult.get().trim();

        Optional<String> classResult = askHeroClass();
        if (classResult.isEmpty()) {
            mainContext.updatePlayerContext(buildDefaultPlayer());
            return;
        }

        Stats stats = statsForClass(CLASS_OPTIONS.indexOf(classResult.get()));
        Player newPlayer = buildPlayer(heroName, stats);
        mainContext.updatePlayerContext(newPlayer);
        mainContext.log("[WELCOME] Personaggio creato: " + heroName + "!");
    }

    // Prompt display for the user for their custom character identity
    private Optional<String> askHeroName() {
        TextInputDialog dialog = new TextInputDialog("Eroe Unicam");
        dialog.setTitle("Nuovo Personaggio");
        dialog.setHeaderText("Benvenuto in RPG 125571!\nNessun salvataggio trovato.");
        dialog.setContentText("Nome del tuo eroe:");
        return dialog.showAndWait();
    }

    // Displays a choice dialog allowing selection of character class options
    private Optional<String> askHeroClass() {
        ChoiceDialog<String> dialog = new ChoiceDialog<>(CLASS_OPTIONS.get(0), CLASS_OPTIONS);
        dialog.setTitle("Scegli la classe");
        dialog.setHeaderText("Scegli la classe del tuo eroe:");
        return dialog.showAndWait();
    }

    // Displays a choice dialog for class selection
    private Stats statsForClass(int index) {
        return switch (index) {
            case 1  -> new Stats(18,  5, 100);
            case 2  -> new Stats(25,  3,  80);
            default -> new Stats(20,  8, 120);
        };
    }

    // Assembles and instantiates a fully operational Player model
    private Player buildPlayer(String name, Stats stats) {
        List<Skill> allSkills = mainContext.getSkillLoader().getSkills();
        SkillLoadout loadout  = new SkillLoadout();
        List<PlayerSkill> skillList = new ArrayList<>();

        if (!allSkills.isEmpty()) {
            PlayerSkill startSkill = new PlayerSkill(allSkills.get(0), 1, 0);
            skillList.add(startSkill);
            loadout.equip(startSkill);
        }

        return new Player(name, 1, stats, new Equipment(), new Inventory(), skillList, loadout, 0, 0);
    }

    // Builds a standard player profile structure if configuration screens are canceled or skipped
    private Player buildDefaultPlayer() {
        return new Player("UNICAM HERO", 1, new Stats(15, 5, 100),
                new Equipment(), new Inventory(), new ArrayList<>(), new SkillLoadout(), 0, 0);
    }
}