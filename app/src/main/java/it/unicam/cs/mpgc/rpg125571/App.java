package it.unicam.cs.mpgc.rpg125571;

import it.unicam.cs.mpgc.rpg125571.model.character.Player;
import it.unicam.cs.mpgc.rpg125571.model.character.Stats;
import it.unicam.cs.mpgc.rpg125571.model.enums.ItemType;
import it.unicam.cs.mpgc.rpg125571.model.item.*;
import it.unicam.cs.mpgc.rpg125571.model.modifier.*;

import java.util.ArrayList;
import java.util.List;

public class App {

    static final String GREEN  = "\u001B[32m";
    static final String RED    = "\u001B[31m";
    static final String YELLOW = "\u001B[33m";
    static final String CYAN   = "\u001B[36m";
    static final String RESET  = "\u001B[0m";

    static int passed = 0;
    static int failed = 0;

    static void assertTrue(String name, boolean cond) {
        if (cond) { System.out.println(GREEN + "[PASS] " + RESET + name); passed++; }
        else      { System.out.println(RED   + "[FAIL] " + RESET + name); failed++; }
    }

    static void assertEqual(String name, Object expected, Object actual) {
        boolean ok = expected == null ? actual == null : expected.equals(actual);
        if (ok) {
            System.out.println(GREEN + "[PASS] " + RESET + name);
            passed++;
        } else {
            System.out.println(RED + "[FAIL] " + RESET + name
                    + "  →  atteso=" + expected + "  ottenuto=" + actual);
            failed++;
        }
    }

    static void section(String title) {
        System.out.println("\n" + YELLOW + "════ " + title + " ════" + RESET);
    }

    public static void main(String[] args) {

        System.out.println(CYAN + "\n╔══════════════════════════════╗");
        System.out.println(      "║         RPG APP TESTER       ║");
        System.out.println(      "╚══════════════════════════════╝" + RESET);

        // ── 1. INIZIALIZZAZIONE APPLICAZIONE / STATO INIZIALE ──────────────────
        section("1. App Setup – Stato Iniziale del Giocatore");

        Player player = new Player("Guerriero", 1, new Stats(12, 6, 120), new Equipment());
        assertEqual("Nome del profilo impostato correttamente", "Guerriero", player.getName());
        assertEqual("Livello di partenza impostato a 1", 1, player.getLevel());
        assertEqual("HP attuali al massimo all'avvio", 120, player.getCurrentHp());

        // ── 2. FLUSSO DI GESTIONE INVENTARIO ─────────────────────────────────────
        section("2. App Flow – Gestione Inventario e Loot");

        // Simuliamo l'ottenimento di oggetti durante il gioco
        Weapon rareSword = new Weapon(10, "Spada di Diamante", ItemType.WEAPON, "Arma leggendaria", 25);
        Potion superPotion = new Potion(11, "Super Pozione", ItemType.POTION, "Cura totale", List.of(new MaxHpModifier(20)));

        // Supponendo che il tuo Player abbia un inventario (List<Item>) gestito da App
        List<Item> inventory = new ArrayList<>();
        inventory.add(rareSword);
        inventory.add(superPotion);

        assertEqual("Looting: 2 oggetti aggiunti all'inventario", 2, inventory.size());
        assertTrue("L'inventario contiene la Spada", inventory.contains(rareSword));

        // ── 3. SIMULAZIONE PRE-COMBATTIMENTO (BUFFING) ───────────────────────────
        section("3. App Flow – Fase di Preparazione (Uso Consumabili)");

        // Il giocatore usa la pozione prima di entrare nel dungeon
        superPotion.use(player);
        assertEqual("La pozione ha iniettato il modificatore temporaneo", 1, player.getTemporaryModifiers().size());

        Stats statsConBuff = ModifierSystem.calculate(player.getBaseStats(), player.getTemporaryModifiers());
        assertEqual("HP massimi incrementati temporaneamente prima del calcolo finale", 140, statsConBuff.getMaxHp());

        // ── 4. SIMULAZIONE CICLO DI COMBATTIMENTO (BATTLE LOOP) ──────────────────
        section("4. App Core – Simulazione Battaglia a Turni (Calcolo Danni)");

        // Creiamo un secondo personaggio (il Nemico)
        Player enemy = new Player("Orco Goblin", 1, new Stats(15, 4, 80), new Equipment());

        // Equipaggiamo il giocatore per la battaglia
        player.getEquipment().equip(rareSword); // +25 ATK

        // Calcolo statistiche finali del Player per il turno attuale
        List<Modifier> currentTurnMods = new ArrayList<>();
        currentTurnMods.addAll(player.getEquipment().getModifiers());
        currentTurnMods.addAll(player.getTemporaryModifiers());
        Stats playerFinalStats = ModifierSystem.calculate(player.getBaseStats(), currentTurnMods);

        // TURNO 1: Il Player attacca il nemico
        // Formula ipotetica di App.java: Danno = (ATK Attaccante) - (DEF Difensore)
        int damageDealt = Math.max(1, playerFinalStats.getAtk() - enemy.getBaseStats().getDef());
        // ATK = 12 (base) + 25 (spada) = 37. DEF Nemico = 4. Danno atteso = 33
        assertEqual("Calcolo danno Turno 1 (37 ATK vs 4 DEF)", 33, damageDealt);

        enemy.takeDamage(damageDealt);
        assertEqual("HP del nemico ridotti dopo il Turno 1 (80 - 33)", 47, enemy.getCurrentHp());
        assertTrue("Il nemico è ancora in piedi", !enemy.isDead());

        // TURNO 2: Il nemico contrattacca
        int damageReceived = Math.max(1, enemy.getBaseStats().getAtk() - playerFinalStats.getDef());
        // ATK Nemico = 15. DEF Player = 6. Danno atteso = 9
        assertEqual("Calcolo danno ricevuto Turno 2 (15 ATK vs 6 DEF)", 9, damageReceived);

        player.takeDamage(damageReceived);
        assertEqual("HP del Player ridotti (120 - 9)", 111, player.getCurrentHp());

        // TURNO 3: Il Player sferra il colpo di grazia (simuliamo un colpo critico o danno massiccio)
        enemy.takeDamage(50);
        assertTrue("Il nemico è stato sconfitto (HP <= 0)", enemy.isDead());

        // ── 5. PULIZIA POST-COMBATTIMENTO ─────────────────────────────────────────
        section("5. App Flow – Fine Battaglia e Reset Modificatori");

        // Al termine della battaglia l'applicazione deve ripulire gli effetti temporanei
        player.clearTemporaryModifiers();
        assertTrue("I buff temporanei della battaglia precedente sono rimossi", player.getTemporaryModifiers().isEmpty());

        Stats postBattleStats = ModifierSystem.calculate(player.getBaseStats(), player.getEquipment().getModifiers());
        assertEqual("Le statistiche tornano normali (solo equipaggiamento attivo)", 37, postBattleStats.getAtk()); // 12 + 25

        // ── Riepilogo ─────────────────────────────────────────
        System.out.println("\n" + CYAN + "╔══════════════════════════════╗");
        System.out.printf(          "║  %s%-4d PASS%s  /  %s%-4d FAIL%s     ║%n",
                GREEN, passed, CYAN, failed > 0 ? RED : GREEN, failed, CYAN);
        System.out.println(         "╚══════════════════════════════╝" + RESET);

        if (failed > 0) System.exit(1);
    }
}