package it.unicam.cs.mpgc.rpg125571.model.character;

import it.unicam.cs.mpgc.rpg125571.model.modifier.Modifier;
import it.unicam.cs.mpgc.rpg125571.model.item.Equipment;
import it.unicam.cs.mpgc.rpg125571.model.modifier.ModifierSystem;
import it.unicam.cs.mpgc.rpg125571.model.modifier.TemporaryModifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class GameCharacter implements Damageable {
    private final String name;
    private int level;
    private final Stats baseStats;
    private int currentHp;
    Equipment equipment;

    // Buff da pozioni, skill, ecc. — hanno una durata in turni
    private final List<TemporaryModifier> temporaryModifiers = new ArrayList<>();

    protected void incrementLevel() {
        this.level++;
    }

    // Costruisce un personaggio inizializzando gli HP al massimo delle stat base.
    public GameCharacter(String name, int level, Stats baseStats, Equipment equipment) {
        this.name = name;
        this.level = level;
        this.baseStats = baseStats;
        this.equipment = equipment;
        this.currentHp = baseStats.getMaxHp();
    }

    /*
     Calcola le statistiche finali del personaggio nel momento corrente.

     Aggrega i modifier provenienti dall'equipaggiamento e dai buff
     temporanei attivi, quindi li applica a una copia delle stat base
     tramite ModifierSystem. Le stat base non vengono mai mutate.

     return una nuova istanza di Stats con tutti i modifier applicati
     */
    public Stats getCurrentStats() {
        List<Modifier> allModifiers = new ArrayList<>(equipment.getModifiers());
        for (TemporaryModifier tm : temporaryModifiers) {
            allModifiers.add(tm.getModifier());
        }
        return ModifierSystem.calculate(baseStats, allModifiers);
    }

    public void heal(int value) {
        if (value <= 0) return;
        int maxHp = getCurrentStats().getMaxHp();
        currentHp = Math.min(currentHp + value, maxHp);
    }

    @Override
    public void takeDamage(int damage) {
        currentHp -= damage;
        if(currentHp < 0) currentHp = 0;
    }

    @Override
    public boolean isDead(){
        return currentHp == 0; // =0 morto (true) else vivo (false)
    }

    // BUFF TEMPORANEI:
    //  - Potion
    //  - (Future)

    // Aggiunge un buff temporaneo al personaggio.
    public void addTemporaryModifier(TemporaryModifier modifier) {
        temporaryModifiers.add(modifier);
    }

    // Decrementa di un turno tutti i buff temporanei attivi e
    // rimuove quelli scaduti.
    public void tickTemporaryModifiers() {
        temporaryModifiers.forEach(TemporaryModifier::tick);
        temporaryModifiers.removeIf(TemporaryModifier::isExpired);
    }

    public void clearTemporaryModifiers() {
        temporaryModifiers.clear();
    }

    public List<TemporaryModifier> getTemporaryModifiers() {
        return Collections.unmodifiableList(temporaryModifiers);
    }

    // GETTER
    public String getName() { return name; }
    public int getLevel() { return level; }
    public Stats getBaseStats() { return baseStats; }
    public int getCurrentHp() { return currentHp; }
    public Equipment getEquipment() { return equipment; }

}
