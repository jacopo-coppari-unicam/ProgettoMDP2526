package it.unicam.cs.mpgc.rpg125571.model.skill;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Gestisce gli slot delle skill attive del giocatore.
 *
 * <p>Un loadout ha un numero massimo di slot ({@value #MAX_SLOTS}). Una skill
 * può essere equipaggiata solo se c'è almeno uno slot libero e non è già
 * presente nel loadout. Tutta la sincronizzazione del flag {@code isEquipped}
 * su {@link SkillEquipable} avviene qui, in modo che nessun altro componente
 * debba preoccuparsene.</p>
 */
public class SkillLoadout {

    /** Numero massimo di skill equipaggiabili contemporaneamente. */
    public static final int MAX_SLOTS = 3;

    private final List<SkillEquipable> equippedSkills = new ArrayList<>();

    /**
     * Equipaggia una skill se c'è uno slot libero e non è già nel loadout.
     *
     * @param skillEquipable la skill da equipaggiare
     * @return {@code true} se l'operazione è andata a buon fine,
     *         {@code false} se il loadout è pieno o la skill era già presente
     */
    public boolean equip(SkillEquipable skillEquipable) {
        if (equippedSkills.size() >= MAX_SLOTS) return false;
        if (equippedSkills.contains(skillEquipable)) return false;
        equippedSkills.add(skillEquipable);
        skillEquipable.setEquipped(true);
        return true;
    }

    /**
     * Rimuove una skill dal loadout e aggiorna il suo stato di equipaggiamento.
     *
     * @param skillEquipable la skill da rimuovere
     * @return {@code true} se era presente ed è stata rimossa,
     *         {@code false} se non era nel loadout
     */
    public boolean unequip(SkillEquipable skillEquipable) {
        boolean removed = equippedSkills.remove(skillEquipable);
        if (removed) skillEquipable.setEquipped(false);
        return removed;
    }

    /**
     * Restituisce una vista non modificabile delle skill attualmente equipaggiate.
     *
     * @return lista immutabile di {@link SkillEquipable}
     */
    public List<SkillEquipable> getEquippedSkills() {
        return Collections.unmodifiableList(equippedSkills);
    }

    /**
     * Numero di slot attualmente occupati.
     *
     * @return slot usati (0 – {@value #MAX_SLOTS})
     */
    public int getUsedSlots() { return equippedSkills.size(); }

    /**
     * Indica se tutti gli slot sono occupati.
     *
     * @return {@code true} se il loadout è al completo
     */
    public boolean isFull() { return equippedSkills.size() >= MAX_SLOTS; }
}
