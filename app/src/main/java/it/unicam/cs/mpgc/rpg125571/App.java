package it.unicam.cs.mpgc.rpg125571;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unicam.cs.mpgc.rpg125571.model.character.Enemy;
import it.unicam.cs.mpgc.rpg125571.model.character.Player;
import it.unicam.cs.mpgc.rpg125571.model.item.Item;
import it.unicam.cs.mpgc.rpg125571.model.skill.PlayerSkill;
import it.unicam.cs.mpgc.rpg125571.storage.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App {
    /* TEST CARICAMENTO ITEM E SKILL
    public static void main(String[] args) {
        System.out.println("====== INIZIO TEST CARICAMENTO DATI (STORAGE) ======");

        // Definiamo i percorsi dei file JSON.
        // NOTA: Usando FileReader, questi percorsi partono dalla root del progetto.
        String itemsPath = "app/src/main/resources/items.json";
        String skillsPath = "app/src/main/resources/skills.json";

        // ==========================================
        // 1. TEST ITEM LOADER
        // ==========================================
        System.out.println("\n--- [ITEM LOADER] Caricamento in corso... ---");
        ItemLoader itemLoader = new ItemLoader();

        try {
            itemLoader.loadItems(itemsPath);
            List<Item> loadedItems = itemLoader.getItems();
            System.out.println("[SUCCESS] Oggetti caricati: " + loadedItems.size());

            for (Item itemtype : loadedItems) {
                System.out.println(String.format(" -> [ID %d] %s (%s) - %s",
                        itemtype.getId(), itemtype.getName(), itemtype.getType(), itemtype.getDescription()));
            }

            // Test recupero per ID singolo
            int targetItemId = 1;
            System.out.println("\n-> Test recupero Item con ID " + targetItemId + ":");
            Item singleItem = itemLoader.getById(targetItemId);
            if (singleItem != null) {
                System.out.println("   Trovato: " + singleItem.getName());
            } else {
                System.out.println("   [WARNING] Nessun itemtype trovato con ID " + targetItemId);
            }

        } catch (Exception e) {
            System.err.println("[ERROR] Errore critico durante il test degli Item: " + e.getMessage());
            e.printStackTrace();
        }

        // ==========================================
        // 2. TEST SKILL LOADER
        // ==========================================
        System.out.println("\n--- [SKILL LOADER] Caricamento in corso... ---");
        SkillLoader skillLoader = new SkillLoader();

        try {
            skillLoader.loadSkills(skillsPath);
            List<Skill> loadedSkills = skillLoader.getSkills();
            System.out.println("[SUCCESS] Abilità caricate: " + loadedSkills.size());

            for (Skill skill : loadedSkills) {
                System.out.println(String.format(" -> [ID %d] %s [%s] - %s",
                        skill.getId(), skill.getName(), skill.getClass().getSimpleName(), skill.getDescription()));
            }

            // Test recupero per ID singolo
            int targetSkillId = 101;
            System.out.println("\n-> Test recupero Skill con ID " + targetSkillId + ":");
            Skill singleSkill = skillLoader.getById(targetSkillId);
            if (singleSkill != null) {
                System.out.println("   Trovato: " + singleSkill.getName());
            } else {
                System.out.println("   [WARNING] Nessuna skill trovata con ID " + targetSkillId);
            }

        } catch (Exception e) {
            System.err.println("[ERROR] Errore critico durante il test delle Skill: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n====== FINE TEST CARICAMENTO DATI ======");
    }
    */

    /*public static void main(String[] args) {
        System.out.println("====== INIZIO TEST COMPLETO STORAGE RPG ======");

        // Configurazione percorsi (Root del progetto con soluzione B)
        String itemsPath = "app/src/main/resources/items.json";
        String skillsPath = "app/src/main/resources/skills.json";
        String playerSavePath = "app/src/main/resources/player_save.json";
        String playerOutputPath = "app/src/main/resources/player_output.json";

        // =================================================================
        // 1. CARICAMENTO DATI GLOBALI (ITEM & SKILL)
        // =================================================================
        ItemLoader itemLoader = new ItemLoader();
        SkillLoader skillLoader = new SkillLoader();

        try {
            System.out.println("\n[1/4] Caricamento configurazioni globali...");
            itemLoader.loadItems(itemsPath);
            skillLoader.loadSkills(skillsPath);
            System.out.println(" -> Item caricati nel database: " + itemLoader.getItems().size());
            System.out.println(" -> Skill caricate nel database: " + skillLoader.getSkills().size());
        } catch (Exception e) {
            System.err.println("[ERRORE CRITICO] Impossibile avviare il database di gioco: " + e.getMessage());
            return;
        }

        // Creiamo l'istanza di Gson configurando sia il Serializer che il Deserializer
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Player.class, new PlayerSerializer())
                .registerTypeAdapter(Player.class, new PlayerDeserializer(itemLoader, skillLoader))
                .setPrettyPrinting() // Rende il JSON di output leggibile ad occhio umano
                .create();

        // =================================================================
        // 2. TEST DESERIALIZZAZIONE (LETTURA SALVATAGGIO PLAYER)
        // =================================================================
        Player player = null;
        System.out.println("\n[2/4] Caricamento del salvataggio del Player...");

        if (!Files.exists(Paths.get(playerSavePath))) {
            System.err.println("[ERRORE] Non trovo il file '" + playerSavePath + "'. Assicurati di averlo creato!");
            return;
        }

        try (FileReader reader = new FileReader(playerSavePath)) {
            player = gson.fromJson(reader, Player.class);

            System.out.println("[SUCCESS] Dati base ripristinati:");
            System.out.println(" -> Nome Eroe: " + player.getName());
            System.out.println(" -> Livello: " + player.getLevel() + " (XP: " + player.getExperience() + ")");
            System.out.println(" -> Vita: " + player.getCurrentHp() + " / " + player.getBaseStats().getMaxHp());

            System.out.println(" -> Inventario idratato con oggetti reali:");
            player.getInventory().getItems().forEach(item ->
                    System.out.println("    * [ID " + item.getId() + "] " + item.getName() + " - Tipo: " + item.getItemType())
            );

            System.out.println(" -> Abilità caricate:");
            for (PlayerSkill ps : player.getSkillInventory()) {
                System.out.println("    * " + ps.getSkill().getName() + " | Livello: " + ps.getCurrentLevel());
            }
        } catch (Exception e) {
            System.err.println("[ERRORE] Fallimento nel caricamento del Player: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // =================================================================
        // 3. SIMULAZIONE MODIFICHE IN-GAME (GAMEPLAY)
        // =================================================================
        System.out.println("\n[3/4] Simulazione modifiche al personaggio in gioco...");

        // Modifichiamo qualche parametro primitivo
        player.setCurrentHp(50);

        // Proviamo a raccogliere un nuovo oggetto dal database globale (es. l'Ascia da Guerra ID 2)
        Item nuovoItem = itemLoader.getById(2);
        if (nuovoItem != null) {
            player.getInventory().addItem(nuovoItem);
            System.out.println(" -> [GAMEPLAY] Il giocatore ha raccolto: " + nuovoItem.getName());
        }

        // =================================================================
        // 4. TEST SERIALIZZAZIONE (SALVATAGGIO SU NUOVO FILE JSON)
        // =================================================================
        System.out.println("\n[4/4] Scrittura del nuovo stato del Player su JSON...");
        try (FileWriter writer = new FileWriter(playerOutputPath)) {
            gson.toJson(player, writer);
            System.out.println("[SUCCESS] Nuovo salvataggio generato in: " + playerOutputPath);
            System.out.println(" -> Verifica il file per controllare che l'ID del nuovo oggetto (2) sia presente nell'array!");
        } catch (Exception e) {
            System.err.println("[ERRORE] Fallimento nel salvataggio del Player: " + e.getMessage());
        }

        // =================================================================
// 5. TEST ENEMY & BOSS LOADER
// =================================================================
        System.out.println("\n--- [MINION & BOSS LOADER] Caricamento del bestiario... ---");
        String enemyPath = "app/src/main/resources/enemies.json";
        String bossPath = "app/src/main/resources/boss.json";

        EnemyLoader enemyLoader = new EnemyLoader();
        BossLoader bossLoader = new BossLoader();

        try {
            enemyLoader.loadEnemies(enemyPath);
            bossLoader.loadBosses(bossPath);

            System.out.println("[SUCCESS] Mostri comuni caricati: " + enemyLoader.getEnemies().size());
            for (Enemy e : enemyLoader.getEnemies()) {
                System.out.println(String.format(" -> Nemico: %s (Liv. %d) | HP: %d | EXP data: %d | Loot slot attivi: %d",
                        e.getName(), e.getLevel(), e.getBaseStats().getMaxHp(), e.getExpReward(), e.getLootTable().size()));
            }

            System.out.println("\n[SUCCESS] Boss di fine zona caricati: " + bossLoader.getBosses().size());
            for (Enemy b : bossLoader.getBosses()) {
                System.out.println(String.format(" -> [BOSS] %s (Liv. %d) | HP: %d | Gold: %d | Drop epici garantiti: %d",
                        b.getName(), b.getLevel(), b.getBaseStats().getMaxHp(), b.getGoldReward(), b.getLootTable().size()));
            }

        } catch (Exception e) {
            System.err.println("[ERRORE] Fallimento nel caricamento del bestiario: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n====== FINE TEST COMPLETO STORAGE RPG ======");
    }*/
    
}
