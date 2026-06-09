package it.unicam.cs.mpgc.rpg125571.model.skill;

/**
 * Rappresenta una skill che può essere equipaggiata in un loadout.
 *
 * <p>Separa il concetto di "skill posseduta dal giocatore" dalla skill stessa:
 * {@link PlayerSkill} è il wrapper che porta stato di gioco (livello, mastery,
 * slot), mentre {@link Skill} è il dato statico di design.</p>
 *
 * <p>Il metodo {@link #getSkill()} restituisce l'interfaccia {@link Skill},
 * non una classe concreta, in modo che {@link SkillLoadout} e la UI non
 * debbano conoscere nulla dell'implementazione sottostante.</p>
 */
public interface SkillEquipable {

    /**
     * Indica se questa skill occupa attualmente uno slot nel loadout.
     *
     * @return {@code true} se equipaggiata, {@code false} altrimenti
     */
    boolean isEquipped();

    /**
     * Imposta lo stato di equipaggiamento. Chiamato da {@link SkillLoadout}
     * durante le operazioni di equip/unequip.
     *
     * @param equipped {@code true} per marcare come equipaggiata
     */
    void setEquipped(boolean equipped);

    /**
     * Restituisce la skill associata a questo wrapper.
     *
     * @return la skill, espressa come interfaccia {@link Skill}
     */
    Skill getSkill();
}
