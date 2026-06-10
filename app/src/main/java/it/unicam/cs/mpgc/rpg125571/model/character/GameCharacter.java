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
    private final int level;
    private final Stats baseStats;
    private int currentHp;
    Equipment equipment;

    // Buff da pozioni, skill, ecc. — hanno una durata in turni
    private final List<TemporaryModifier> temporaryModifiers = new ArrayList<>();



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
     tramite {@link ModifierSystem}. Le stat base non vengono mai mutate.</p>

     @return una nuova istanza di {@link Stats} con tutti i modifier applicati
     */
    public Stats getCurrentStats() {
        List<Modifier> allModifiers = new ArrayList<>(equipment.getModifiers());
        for (TemporaryModifier tm : temporaryModifiers) {
            allModifiers.add(tm.getModifier());
        }
        return ModifierSystem.calculate(baseStats, allModifiers);
    }


    /**
     * Ripristina HP al personaggio senza superare il massimo corrente.
     *
     * <p>Il massimo viene letto da {@link #getCurrentStats()} in modo che
     * eventuali bonus a MaxHp (da equipaggiamento o buff) siano considerati.</p>
     *
     * @param value punti vita da ripristinare; valori negativi vengono ignorati
     */
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

    /**
     * Indica se il personaggio ha esaurito i punti vita.
     *
     * @return {@code true} se gli HP correnti sono zero
     */
    @Override
    public boolean isDead(){
        return currentHp == 0; // =0 morto (true) else vivo (false)
    }

    // BUFF TEMPORANEI:
    //  - Potion
    //  - (Future)
    /**
     * Aggiunge un buff temporaneo al personaggio.
     *
     * @param modifier il buff da aggiungere; non deve essere {@code null}
     */
    public void addTemporaryModifier(TemporaryModifier modifier) {
        temporaryModifiers.add(modifier);
    }

    /**
     * Decrementa di un turno tutti i buff temporanei attivi e
     * rimuove quelli scaduti.
     *
     * <p>Va chiamato una volta alla fine di ogni turno di combattimento.</p>
     */
    public void tickTemporaryModifiers() {
        temporaryModifiers.forEach(TemporaryModifier::tick);
        temporaryModifiers.removeIf(TemporaryModifier::isExpired);
    }

    /**
     * Rimuove immediatamente tutti i buff temporanei, indipendentemente
     * dai turni rimanenti. Utile a fine combattimento.
     */
    public void clearTemporaryModifiers() {
        temporaryModifiers.clear();
    }

    /**
     * Vista non modificabile dei buff temporanei attualmente attivi.
     *
     * @return lista immutabile di {@link TemporaryModifier}
     */
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
