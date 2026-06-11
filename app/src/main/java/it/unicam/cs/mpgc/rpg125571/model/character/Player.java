package it.unicam.cs.mpgc.rpg125571.model.character;

import it.unicam.cs.mpgc.rpg125571.model.item.Equipment;
import it.unicam.cs.mpgc.rpg125571.model.skill.PlayerSkill;
import it.unicam.cs.mpgc.rpg125571.model.skill.SkillLoadout;

import java.util.List;

public class Player extends GameCharacter {
    private final Inventory inventory;
    private List<PlayerSkill> skills;
    private final SkillLoadout skillLoadout;
    private int experience;
    private int experienceToNextLevel;


    public Player(String name, int level, Stats baseStats, Equipment equipment,
                  Inventory inventory, List<PlayerSkill> skills, SkillLoadout skillLoadout,
                  int experience) {
        super(name, level, baseStats, equipment);
        this.inventory = inventory;
        this.skills = skills;
        this.skillLoadout = skillLoadout;
        this.experience = experience;
        this.experienceToNextLevel = level * 100;
    }

    public Inventory getInventory() { return inventory; }

    // L'insieme di tutte le skill sbloccate dal giocatore,
    public  List<PlayerSkill> getSkillInventory() { return skills; }

    // Il loadout attivo: le skill equipaggiate per il combattimento.
    public SkillLoadout getSkillLoadout() { return skillLoadout; }

    public void addExperience(int amount) {
        if (amount <= 0) return;
        this.experience += amount;
        // Il ciclo while gestisce il caso in cui l'exp ricevuta
        // sia talmente alta da fare fare più di un livello in un colpo solo
        while (this.experience >= this.experienceToNextLevel) {
            this.experience -= this.experienceToNextLevel;
            levelUp();
        }
    }


    private void levelUp() {
        // 1. Incrementa il livello memorizzato nella superclasse
        super.incrementLevel();

        // 2. Modifica i valori dentro l'oggetto baseStats esistente (mantenendo il final su baseStats)
        Stats base = getBaseStats();
        base.setMaxHp(base.getMaxHp() + 20);   // +20 HP massimi ad ogni livello
        base.setAtk(base.getAtk() + 5);   // +5 Attacco base
        base.setDef(base.getDef() + 3); // +3 Difesa base

        // 3. Ricalcola la EXP necessaria per il prossimo livello (es: Livello 2 -> 200 EXP)
        this.experienceToNextLevel = getLevel() * 100;

        System.out.println(getName() + " è salito al livello " + getLevel() + "!");
    }

    // GETTER PER LA PROGRESSIONE
    public int getExperience() { return experience; }
    public int getExperienceToNextLevel() { return experienceToNextLevel; }
}