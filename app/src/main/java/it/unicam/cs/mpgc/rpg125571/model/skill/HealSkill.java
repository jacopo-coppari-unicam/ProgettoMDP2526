package it.unicam.cs.mpgc.rpg125571.model.skill;

/**
 * Contratto per le skill di cura.
 *
 * <p>Estende {@link Skill} esponendo la quantità di HP recuperati prima
 * dell'esecuzione effettiva. Utile per mostrare preview di cura nella UI
 * o per logiche che dipendono dal valore di healing (es. over-heal check).</p>
 *
 * <p>Separare questa interfaccia da {@link AttackSkill} rispetta l'ISP:
 * i sistemi che gestiscono solo cure non devono conoscere nulla del danno,
 * e viceversa.</p>
 */
public interface HealSkill extends Skill {

    /**
     * Calcola la quantità di HP che questa skill ripristinerà al dato livello
     * e tier, senza applicare ancora alcun effetto.
     *
     * @param level il livello corrente della skill
     * @param tier  il tier della skill
     * @return la quantità di cura calcolata, in punti vita
     */
    int getHealAmount(int level, int tier);
}
