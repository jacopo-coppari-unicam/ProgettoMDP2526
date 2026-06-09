package it.unicam.cs.mpgc.rpg125571.model.skill;


/**
 * Contratto per le skill offensive.
 *
 * <p>Estende {@link Skill} aggiungendo la capacità di esporre il danno
 * calcolato prima dell'esecuzione. Questo permette, ad esempio, alla UI
 * di mostrare un'anteprima del danno atteso senza dover invocare {@code cast}.</p>
 *
 * <p>Chi ha bisogno di distinguere le skill offensive dalle altre — per
 * filtrare il loadout, per applicare bonus offensivi, ecc. — usa questo
 * tipo come discriminante, senza dover ispezionare l'implementazione concreta.</p>
 */
public interface AttackSkill extends Skill {

    /**
     * Calcola il danno che questa skill infliggerà al dato livello e tier,
     * senza applicare ancora nessun effetto sul campo di battaglia.
     *
     * @param level il livello corrente della skill
     * @param tier  il tier della skill
     * @return il danno calcolato, in punti vita
     */
    int getDamage(int level, int tier);
}
