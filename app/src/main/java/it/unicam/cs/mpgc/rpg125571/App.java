package it.unicam.cs.mpgc.rpg125571;

import it.unicam.cs.mpgc.rpg125571.logic.GameController;
import it.unicam.cs.mpgc.rpg125571.logic.battle.BattleManager;
import it.unicam.cs.mpgc.rpg125571.logic.enemy.EnemyLoader;
import it.unicam.cs.mpgc.rpg125571.model.character.Enemy;
import it.unicam.cs.mpgc.rpg125571.model.character.Inventory;
import it.unicam.cs.mpgc.rpg125571.model.character.Player;
import it.unicam.cs.mpgc.rpg125571.model.character.Stats;
import it.unicam.cs.mpgc.rpg125571.model.enums.BattleState;
import it.unicam.cs.mpgc.rpg125571.model.enums.EquipmentSlot;
import it.unicam.cs.mpgc.rpg125571.model.enums.ItemType;
import it.unicam.cs.mpgc.rpg125571.model.item.*;
import it.unicam.cs.mpgc.rpg125571.model.skill.*;

import java.util.List;
import java.util.Scanner;

public class App {
    private static GameController gameController;
    private static List<Enemy> enemyPool;
    private static int currentEnemyIndex = 0;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== INIZIALIZZAZIONE GIOCO ===");

        // 1. Inizializziamo i componenti richiesti dal tuo costruttore a 8 argomenti
        Stats statsIniziali = new Stats(15, 5, 100);
        Equipment equipIniziale = new Equipment();
        Inventory inventarioIniziale = new Inventory();
        java.util.List<PlayerSkill> listaSkillIniziale = new java.util.ArrayList<>();
        SkillLoadout loadoutIniziale = new SkillLoadout();
        int esperienzaIniziale = 0;

// 2. Creiamo l'istanza del Player passando esattamente gli 8 parametri richiesti
        Player player = new Player(
                "Eroe Unicam",
                1,
                statsIniziali,
                equipIniziale,
                inventarioIniziale,
                listaSkillIniziale,
                loadoutIniziale,
                esperienzaIniziale
        );

// 3. Colleghiamo il player appena creato al GameController
        gameController = new GameController(player);

        // Di prova: regaliamo qualche pezzo nell'inventario per i test del menu
        player.getInventory().addItem(new Weapon(1, "Spada di Ferro", ItemType.WEAPON, "+10 ATK", 10));
        player.getInventory().addItem(new Armor(2, "Maglai di ferro", ItemType.ARMOR, "+6 DEF", 6, EquipmentSlot.CHEST));

        // 2. Carichiamo i nemici dal file JSON fisso
        enemyPool = EnemyLoader.loadEnemies("enemies.json");
        if (enemyPool.isEmpty()) {
            System.out.println("Impossibile avviare il gioco senza nemici nel file JSON.");
            return;
        }

        // 3. Loop principale del gioco fuori dal combattimento
        boolean running = true;
        while (running && !player.isDead()) {
            System.out.println("\n==========================================");
            System.out.println(" HUB PRINCIPALE - " + player.getName() + " (Liv. " + player.getLevel() + ")");
            System.out.println(" HP: " + player.getCurrentHp() + "/" + player.getCurrentStats().getMaxHp());
            System.out.println("==========================================");
            System.out.println("1. Gestisci Inventario & Equipaggiamento");
            System.out.println("2. Gestisci Loadout Skill");
            System.out.println("3. Cerca Prossimo Combattimento");
            System.out.println("4. Esci dal gioco");
            System.out.print("Scegli un'opzione: ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1 -> menuInventario();
                case 2 -> menuSkill();
                case 3 -> avviaCombattimento();
                case 4 -> {
                    System.out.println("Grazie per aver giocato!");
                    running = false;
                }
                default -> System.out.println("Scelta non valida.");
            }
        }

        if (player.isDead()) {
            System.out.println("\n[GAME OVER] Il tuo personaggio è caduto in battaglia.");
        }
    }

    private static void menuInventario() {
        System.out.println("\n--- IL TUO INVENTARIO ---");
        List<Item> items = gameController.getInventoryItems();
        if (items.isEmpty()) {
            System.out.println("L'inventario è vuoto.");
            return;
        }

        for (int i = 0; i < items.size(); i++) {
            System.out.println((i + 1) + ". " + items.get(i).getName() + " [" + items.get(i).getType() + "]");
        }
        System.out.println((items.size() + 1) + ". Torna all'hub");
        System.out.print("Seleziona un oggetto da equipaggiare: ");

        int choice = scanner.nextInt();
        int index = choice - 1;

        if (choice > 0 && index < items.size()) {
            Item selezionato = items.get(index);
            boolean successo = gameController.equipItemFromInventory(selezionato);
            if (successo) {
                System.out.println("[OK] Hai equipaggiato: " + selezionato.getName());
            } else {
                System.out.println("[X] Questo oggetto non può essere equipaggiato (es. è una pozione)!");
            }
        }
    }

    private static void menuSkill() {
        System.out.println("\n--- GESTIONE SKILL ---");
        System.out.println("1. Equipaggia una skill nel Loadout");
        System.out.println("2. Rimuovi una skill dal Loadout");
        System.out.println("3. Torna all'hub");
        System.out.print("Scegli: ");
        int scelta = scanner.nextInt();

        if (scelta == 1) {
            List<PlayerSkill> owned = gameController.getOwnedSkills();
            if (owned.isEmpty()) {
                System.out.println("Non conosci ancora nessuna skill.");
                return;
            }
            for (int i = 0; i < owned.size(); i++) {
                PlayerSkill ps = owned.get(i);
                System.out.println((i + 1) + ". " + ps.getSkill().getName() + " (Equipaggiata: " + ps.isEquipped() + ")");
            }
            System.out.print("Scegli quale equipaggiare: ");
            int idx = scanner.nextInt() - 1;
            if (idx >= 0 && idx < owned.size()) {
                boolean ok = gameController.equipSkillToLoadout(owned.get(idx));
                System.out.println(ok ? "[OK] Skill equipaggiata nel loadout!" : "[X] Impossibile equipaggiare (Loadout pieno o già inserita).");
            }
        } else if (scelta == 2) {
            List<SkillEquipable> active = gameController.getActiveLoadout();
            if (active.isEmpty()) {
                System.out.println("Il loadout è vuoto.");
                return;
            }
            for (int i = 0; i < active.size(); i++) {
                System.out.println((i + 1) + ". " + active.get(i).getSkill().getName());
            }
            System.out.print("Scegli quale rimuovere: ");
            int idx = scanner.nextInt() - 1;
            if (idx >= 0 && idx < active.size()) {
                if (active.get(idx) instanceof PlayerSkill ps) {
                    gameController.unequipSkillFromLoadout(ps);
                    System.out.println("[OK] Skill rimossa.");
                }
            }
        }
    }

    private static void avviaCombattimento() {
        // 1. Estraiamo il nemico corrente dalla lista JSON
        Enemy nemicoCorrente = enemyPool.get(currentEnemyIndex);

        // Ripristiniamo gli HP del nemico per lo scontro se era già stato affrontato
        // (Aggiungi un setter o un metodo heal sul nemico se necessario, oppure passa una nuova istanza)
        nemicoCorrente.heal(nemicoCorrente.getCurrentStats().getMaxHp());

        // 2. Creiamo il BattleManager logico
        BattleManager battaglia = new BattleManager(gameController.getPlayer(), nemicoCorrente);

        System.out.println("\n==========================================");
        System.out.println(" INIZIA LO SCONTRO CON: " + nemicoCorrente.getName());
        System.out.println("==========================================");

        // 3. Questo loop gestisce i turni chiedendo l'input dell'utente finché la battaglia è IN_PROGRESS
        while (battaglia.getState() == BattleState.IN_PROGRESS) {
            System.out.println("\n--- " + gameController.getPlayer().getName() + " (" + gameController.getPlayer().getCurrentHp() + " HP) vs "
                    + nemicoCorrente.getName() + " (" + nemicoCorrente.getCurrentHp() + " HP) ---");
            System.out.println("1. Attacco Base");
            System.out.println("2. Usa Skill");
            System.out.print("Scegli l'azione: ");

            int azione = scanner.nextInt();

            if (azione == 1) {
                // Esegue l'attacco del player + la risposta automatica del nemico + i tick dei modificatori
                battaglia.executePlayerBaseAttack();
            } else if (azione == 2) {
                List<SkillEquipable> active = gameController.getActiveLoadout();
                if (active.isEmpty()) {
                    System.out.println("[X] Non hai skill equipaggiate nel loadout! Turno sprecato?");
                    battaglia.executePlayerBaseAttack(); // Di riserva fa un attacco base
                    continue;
                }

                System.out.println("\nSeleziona la skill:");
                for (int i = 0; i < active.size(); i++) {
                    System.out.println((i + 1) + ". " + active.get(i).getSkill().getName());
                }
                System.out.print("Scegli: ");
                int skillIdx = scanner.nextInt() - 1;

                if (skillIdx >= 0 && skillIdx < active.size()) {
                    // Esegue la skill + risposta del nemico + tick modificatori
                    battaglia.executePlayerSkill(skillIdx);
                } else {
                    System.out.println("Scelta non valida, perdi il turno!");
                    battaglia.executePlayerBaseAttack();
                }
            }
        }

        // 4. Risoluzione della battaglia a schermo
        System.out.println("\n==========================================");
        if (battaglia.getState() == BattleState.PLAYER_WON) {
            System.out.println(" VITTORIA! " + nemicoCorrente.getName() + " è stato sconfitto!");
            System.out.println(" Guadagnati " + nemicoCorrente.getExpReward() + " punti EXP.");

            // Passiamo al prossimo nemico del file JSON per il prossimo scontro
            currentEnemyIndex = (currentEnemyIndex + 1) % enemyPool.size();

            // Cura di fine scontro per non far morire subito il player nel prossimo livello
            gameController.getPlayer().heal(30);
        } else {
            System.out.println(" SEI STATO SCONFITTO... GAME OVER.");
        }
        System.out.println("==========================================");
    }
}