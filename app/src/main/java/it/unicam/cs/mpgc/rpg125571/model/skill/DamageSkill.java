package it.unicam.cs.mpgc.rpg125571.model.skill;

import it.unicam.cs.mpgc.rpg125571.model.character.GameCharacter;
import it.unicam.cs.mpgc.rpg125571.model.enums.Element;

/**
 * Skill offensiva che infligge danni al bersaglio.
 *
 * <p>Il danno è calcolato tramite la formula di scaling ereditata da
 * {@link AbstractSkill} e applicato direttamente al bersaglio tramite
 * {@link GameCharacter#takeDamage(int)}. Resistenze ed affinità elementali
 * sono responsabilità del sistema di combattimento esterno, non di questa classe.</p>
 */
public class DamageSkill extends AbstractSkill implements AttackSkill {

    /**
     * Crea una nuova skill offensiva.
     *
     * @param id          identificatore univoco
     * @param name        nome della skill
     * @param description descrizione dell'effetto
     * @param element     elemento della skill
     * @param baseValue   danno base prima dello scaling
     */
    public DamageSkill(int id, String name, String description,
                       Element element, int baseValue) {
        super(id, name, description, element, baseValue);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Delega il calcolo a {@link #calculateEffectiveValue(int, int)}.</p>
     */
    @Override
    public int getDamage(int level, int tier) {
        return calculateEffectiveValue(level, tier);
    }

    /**
     * Infligge il danno calcolato al bersaglio.
     * Il caster non viene coinvolto in questa skill.
     */
    @Override
    public void cast(GameCharacter caster, GameCharacter target, int level, int tier) {
        target.takeDamage(getDamage(level, tier));
    }
}