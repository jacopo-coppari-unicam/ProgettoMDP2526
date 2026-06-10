package it.unicam.cs.mpgc.rpg125571;

import it.unicam.cs.mpgc.rpg125571.model.character.Player;
import it.unicam.cs.mpgc.rpg125571.model.character.Stats;
import it.unicam.cs.mpgc.rpg125571.model.enums.Element;
import it.unicam.cs.mpgc.rpg125571.model.enums.ItemType;
import it.unicam.cs.mpgc.rpg125571.model.item.*;
import it.unicam.cs.mpgc.rpg125571.model.modifier.*;
import it.unicam.cs.mpgc.rpg125571.model.skill.*;

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

        // ══════════════════════════════════════════════════════
        // 1. STATO INIZIALE DEL GIOCATORE
        // ══════════════════════════════════════════════════════
        section("1. Stato iniziale del giocatore");

        Player player = new Player("Guerriero", 1, new Stats(12, 6, 120), new Equipment());
        assertEqual("Nome impostato correttamente",       "Guerriero", player.getName());
        assertEqual("Livello di partenza = 1",            1,           player.getLevel());
        assertEqual("HP correnti = maxHp delle baseStats", 120,        player.getCurrentHp());

        // getCurrentStats senza modifier = copia esatta delle baseStats
        Stats statsBase = player.getCurrentStats();
        assertEqual("getCurrentStats() senza modifier: ATK = baseStats.ATK", 12, statsBase.getAtk());
        assertEqual("getCurrentStats() senza modifier: DEF = baseStats.DEF", 6,  statsBase.getDef());
        assertEqual("getCurrentStats() senza modifier: maxHp = baseStats.maxHp", 120, statsBase.getMaxHp());

        // ══════════════════════════════════════════════════════
        // 2. EQUIPAGGIAMENTO — Equipment + Modifier da Weapon/Armor
        // ══════════════════════════════════════════════════════
        section("2. Equipaggiamento (Weapon, Armor, Equipment)");

        Weapon sword   = new Weapon(1, "Spada di Ferro",    ItemType.WEAPON, "+10 ATK",  10);
        Armor  shield  = new Armor (2, "Scudo di Legno",    ItemType.ARMOR,  "+5 DEF",    5);
        Weapon sword2  = new Weapon(3, "Spada di Diamante", ItemType.WEAPON, "+25 ATK",  25);

        assertTrue("equip() prima spada: restituisce true",  player.getEquipment().equip(sword));
        assertTrue("equip() scudo: restituisce true",        player.getEquipment().equip(shield));
        assertTrue("equip() spada duplicata: restituisce false (già equipaggiata)",
                !player.getEquipment().equip(sword));

        Stats statsConEquip = player.getCurrentStats();
        assertEqual("ATK con spada (+10): 12+10 = 22",  22, statsConEquip.getAtk());
        assertEqual("DEF con scudo (+5): 6+5 = 11",     11, statsConEquip.getDef());
        assertEqual("maxHp invariato dall'equipaggiamento", 120, statsConEquip.getMaxHp());

        assertTrue("unequip() scudo: restituisce true",  player.getEquipment().unequip(shield));
        assertEqual("DEF torna a 6 dopo unequip scudo",  6, player.getCurrentStats().getDef());
        assertTrue("unequip() item non equipaggiato: restituisce false",
                !player.getEquipment().unequip(shield));

        // ══════════════════════════════════════════════════════
        // 3. INVENTARIO — Inventory
        // ══════════════════════════════════════════════════════
        section("3. Inventario del giocatore");

        Potion hpPotion  = new Potion(10, "Pozione HP",   ItemType.POTION, "+20 maxHp x3 turni",
                List.of(new MaxHpModifier(20)), 3);
        Potion atkPotion = new Potion(11, "Pozione ATK",  ItemType.POTION, "+8 ATK x2 turni",
                List.of(new AtkModifier(8)),    2);

        player.getInventory().addItem(hpPotion);
        player.getInventory().addItem(atkPotion);
        player.getInventory().addItem(sword2);

        assertEqual("Inventario: 3 item aggiunti",             3, player.getInventory().getItems().size());
        assertEqual("Consumabili filtrati correttamente: 2",   2, player.getInventory().getConsumables().size());
        assertEqual("Equipaggiabili filtrati correttamente: 1",1, player.getInventory().getEquipables().size());
        assertTrue("hasItem(): hpPotion trovata",              player.getInventory().hasItem(hpPotion));

        assertTrue("removeItem(): rimozione di atkPotion",     player.getInventory().removeItem(atkPotion));
        assertEqual("Inventario dopo rimozione: 2 item",       2, player.getInventory().getItems().size());
        assertTrue("removeItem() item assente: false",         !player.getInventory().removeItem(atkPotion));

        // ══════════════════════════════════════════════════════
        // 4. BUFF TEMPORANEI — TemporaryModifier + Potion
        // ══════════════════════════════════════════════════════
        section("4. Buff temporanei (Potion → TemporaryModifier)");

        // Player ha ancora la spada equipaggiata (+10 ATK), nessun buff attivo
        assertEqual("Nessun buff temporaneo prima dell'uso pozione", 0,
                player.getTemporaryModifiers().size());

        hpPotion.use(player);   // +20 maxHp per 3 turni

        assertEqual("1 buff attivo dopo uso pozione",          1, player.getTemporaryModifiers().size());
        assertEqual("Turni rimanenti del buff = 3",            3,
                player.getTemporaryModifiers().get(0).getRemainingTurns());

        Stats statsConBuff = player.getCurrentStats();
        assertEqual("maxHp con buff: 120+20 = 140",            140, statsConBuff.getMaxHp());
        assertEqual("ATK invariato dal buff HP",               22,  statsConBuff.getAtk());

        // Heal rispetta il maxHp corrente (con buff)
        player.takeDamage(50);
        assertEqual("HP dopo 50 danni: 120-50 = 70",           70, player.getCurrentHp());
        player.heal(200);
        assertEqual("heal() cappato al maxHp corrente (140)",  140, player.getCurrentHp());

        // Tick: decrementare i turni e verificare scadenza
        player.tickTemporaryModifiers();
        assertEqual("Buff ancora attivo dopo 1 tick (2 turni rimasti)", 2,
                player.getTemporaryModifiers().get(0).getRemainingTurns());
        player.tickTemporaryModifiers();
        player.tickTemporaryModifiers();
        assertEqual("Buff scaduto dopo 3 tick: lista vuota", 0,
                player.getTemporaryModifiers().size());

        Stats statsDopoScadenza = player.getCurrentStats();
        assertEqual("maxHp torna a 120 dopo scadenza buff",    120, statsDopoScadenza.getMaxHp());

        // clearTemporaryModifiers() — pulizia immediata
        hpPotion.use(player);
        assertEqual("Buff riapplicato: 1 attivo",              1, player.getTemporaryModifiers().size());
        player.clearTemporaryModifiers();
        assertEqual("clearTemporaryModifiers(): lista vuota",  0, player.getTemporaryModifiers().size());

        // ══════════════════════════════════════════════════════
        // 5. SISTEMA SKILL — interfacce, casting, scaling
        // ══════════════════════════════════════════════════════
        section("5. Skill system (DamageSkill, HealingSkill, AbstractSkill scaling)");

        DamageSkill  fireball = new DamageSkill (20, "Fireball",  "Palla di fuoco", Element.FIRE,  30);
        HealingSkill cure     = new HealingSkill(21, "Cure",      "Cura base",      Element.LIGHT, 20);

        // isA checks tramite interfacce
        assertTrue("DamageSkill è un AttackSkill", fireball instanceof AttackSkill);
        assertTrue("DamageSkill è una Skill",      fireball instanceof Skill);
        assertTrue("HealingSkill è una HealSkill", cure     instanceof HealSkill);
        assertTrue("HealingSkill è una Skill",     cure     instanceof Skill);

        // Scaling: effectiveValue = (base + level * 3) * (1 + tier * 0.20)
        // livello 1, tier 0: (30 + 1*3) * 1.0 = 33
        assertEqual("DamageSkill getDamage(1,0) = 33", 33, fireball.getDamage(1, 0));
        // livello 3, tier 1: (30 + 3*3) * 1.20 = 46.8 → troncato a 46 (cast int)
        assertEqual("DamageSkill getDamage(3,1) = 46", 46, fireball.getDamage(3, 1));
        // HealingSkill livello 1, tier 0: (20 + 3) * 1.0 = 23
        assertEqual("HealingSkill getHealAmount(1,0) = 23", 23, cure.getHealAmount(1, 0));

        // cast DamageSkill su nemico
        Player enemy = new Player("Goblin", 1, new Stats(8, 2, 60), new Equipment());
        fireball.cast(player, enemy, 1, 0);
        assertEqual("cast DamageSkill: HP nemico 60-33 = 27", 27, enemy.getCurrentHp());

        // cast HealingSkill: cura il caster (non il target)
        // A questo punto player ha maxHp=120 (buff scaduto), HP correnti variabili.
        // heal() cappato a getCurrentStats().getMaxHp() = 120
        player.takeDamage(40);
        int hpPrimaDiCurare = player.getCurrentHp();
        int maxHpCaster     = player.getCurrentStats().getMaxHp();
        cure.cast(player, enemy, 1, 0);   // heals caster di 23, cappato a maxHp
        int hpAttesoDopoHeal = Math.min(hpPrimaDiCurare + 23, maxHpCaster);
        assertEqual("cast HealingSkill: cura cappata a maxHp corrente",
                hpAttesoDopoHeal, player.getCurrentHp());

        // isDead
        enemy.takeDamage(999);
        assertTrue("isDead() dopo danni fatali", enemy.isDead());

        // ══════════════════════════════════════════════════════
        // 6. PLAYERSKI LL + SKILLINVENTORY + SKILLLOADOUT
        // ══════════════════════════════════════════════════════
        section("6. PlayerSkill, SkillInventory, SkillLoadout");

        PlayerSkill psFireball = new PlayerSkill(fireball);
        PlayerSkill psCure     = new PlayerSkill(cure);
        DamageSkill thunder    = new DamageSkill(22, "Thunder", "Fulmine", Element.WIND, 25);
        PlayerSkill psThunder  = new PlayerSkill(thunder);
        DamageSkill blizzard   = new DamageSkill(23, "Blizzard", "Bufera", Element.ICE,  28);
        PlayerSkill psBlizzard = new PlayerSkill(blizzard);

        // PlayerSkill: stato iniziale
        assertEqual("PlayerSkill: livello iniziale = 1", 1, psFireball.getCurrentLevel());
        assertEqual("PlayerSkill: mastery iniziale = 0", 0, psFireball.getMasteryPoints());
        assertTrue("PlayerSkill: non equipaggiata all'inizio", !psFireball.isEquipped());

        // gainMastery: level up a 100 punti
        psFireball.gainMastery(60);
        assertEqual("Mastery dopo +60 = 60", 60, psFireball.getMasteryPoints());
        psFireball.gainMastery(50);   // 60+50 = 110 → level up, reset a 0
        assertEqual("Level up a 100 punti: livello = 2", 2, psFireball.getCurrentLevel());
        assertEqual("Mastery azzerata dopo level up",    0, psFireball.getMasteryPoints());
        psFireball.gainMastery(-10);  // valori negativi ignorati
        assertEqual("gainMastery valori negativi: mastery invariata", 0, psFireball.getMasteryPoints());

        // SkillInventory
        player.getSkillInventory().addSkill(psFireball);
        player.getSkillInventory().addSkill(psCure);
        player.getSkillInventory().addSkill(psThunder);
        assertEqual("SkillInventory: 3 skill aggiunte", 3,
                player.getSkillInventory().getOwnedSkills().size());
        assertTrue("hasSkill(fireball): trovata", player.getSkillInventory().hasSkill(fireball));
        assertTrue("hasSkill(cure): trovata",     player.getSkillInventory().hasSkill(cure));

        player.getSkillInventory().removeSkill(psThunder);
        assertEqual("SkillInventory dopo rimozione: 2 skill", 2,
                player.getSkillInventory().getOwnedSkills().size());

        // SkillLoadout: max 3 slot
        player.getSkillInventory().addSkill(psThunder);   // re-aggiungo per il loadout
        assertTrue("equip psFireball: OK",    player.getSkillLoadout().equip(psFireball));
        assertTrue("equip psCure: OK",        player.getSkillLoadout().equip(psCure));
        assertTrue("equip psThunder: OK",     player.getSkillLoadout().equip(psThunder));
        assertTrue("psFireball.isEquipped() = true", psFireball.isEquipped());
        assertEqual("Slot usati = 3",         3, player.getSkillLoadout().getUsedSlots());
        assertTrue("Loadout pieno",           player.getSkillLoadout().isFull());
        assertTrue("equip 4a skill: false (loadout pieno)",
                !player.getSkillLoadout().equip(psBlizzard));

        assertTrue("equip duplicato: false",  !player.getSkillLoadout().equip(psFireball));

        assertTrue("unequip psCure: OK",      player.getSkillLoadout().unequip(psCure));
        assertTrue("psCure.isEquipped() = false dopo unequip", !psCure.isEquipped());
        assertEqual("Slot usati dopo unequip = 2", 2, player.getSkillLoadout().getUsedSlots());
        assertTrue("unequip skill non presente: false",
                !player.getSkillLoadout().unequip(psBlizzard));

        // ══════════════════════════════════════════════════════
        // 7. SIMULAZIONE TURNO COMPLETO DI COMBATTIMENTO
        // ══════════════════════════════════════════════════════
        section("7. Simulazione turno completo (stat finali + skill + buff)");

        Player hero    = new Player("Eroe",   1, new Stats(10, 5, 100), new Equipment());
        Player monster = new Player("Mostro", 1, new Stats(12, 3, 80),  new Equipment());

        Weapon heroSword = new Weapon(30, "Lama Eroica", ItemType.WEAPON, "+15 ATK", 15);
        Potion buffAtk   = new Potion(31, "Pozione Forza", ItemType.POTION, "+10 ATK x2 turni",
                List.of(new AtkModifier(10)), 2);
        DamageSkill slash = new DamageSkill(32, "Slash", "Fendente", Element.NEUTRAL, 15);

        hero.getEquipment().equip(heroSword);
        buffAtk.use(hero);

        // Stat finali: base(10) + arma(15) + buff(10) = 35 ATK
        assertEqual("Stat finali turno 1: ATK = 35", 35, hero.getCurrentStats().getAtk());

        // La skill usa takeDamage direttamente, qui simuliamo con stat finali
        int rawDamage = hero.getCurrentStats().getAtk() - monster.getCurrentStats().getDef();
        assertEqual("Danno calcolato: 35 ATK - 3 DEF = 32", 32, rawDamage);
        monster.takeDamage(rawDamage);
        assertEqual("HP mostro dopo turno 1: 80-32 = 48", 48, monster.getCurrentHp());

        // Fine turno: tick buff
        hero.tickTemporaryModifiers();
        assertEqual("Buff forza: 1 turno rimanente", 1,
                hero.getTemporaryModifiers().get(0).getRemainingTurns());
        hero.tickTemporaryModifiers();
        assertEqual("Buff forza scaduto dopo 2 tick", 0, hero.getTemporaryModifiers().size());
        assertEqual("ATK torna a 25 (base+arma) dopo scadenza buff", 25,
                hero.getCurrentStats().getAtk());

        // Skill cast: slash su mostro (livello 1, tier 0)
        slash.cast(hero, monster, 1, 0);
        // effectiveValue = (15 + 1*3) * 1.0 = 18
        assertEqual("HP mostro dopo Slash: 48-18 = 30", 30, monster.getCurrentHp());

        assertTrue("Mostro ancora vivo", !monster.isDead());
        monster.takeDamage(999);
        assertTrue("Mostro sconfitto", monster.isDead());

        // ══════════════════════════════════════════════════════
        // RIEPILOGO
        // ══════════════════════════════════════════════════════
        System.out.println("\n" + CYAN + "╔══════════════════════════════╗");
        System.out.printf(          "║  %s%-4d PASS%s  /  %s%-4d FAIL%s     ║%n",
                GREEN, passed, CYAN, failed > 0 ? RED : GREEN, failed, CYAN);
        System.out.println(         "╚══════════════════════════════╝" + RESET);

        if (failed > 0) System.exit(1);
    }
}