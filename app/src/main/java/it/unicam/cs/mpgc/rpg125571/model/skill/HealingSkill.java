package it.unicam.cs.mpgc.rpg125571.model.skill;

import it.unicam.cs.mpgc.rpg125571.model.character.GameCharacter;
import it.unicam.cs.mpgc.rpg125571.model.enums.Element;

/**
 * Skill di cura che ripristina HP al caster.
 *
 * <p>La quantità curata è calcolata tramite la formula di scaling ereditata
 * da {@link AbstractSkill} e applicata al caster tramite
 * {@link GameCharacter#heal(int)}. Il bersaglio viene ignorato: questa skill
 * è sempre self-cast.</p>
 *
 * <p>Il nome della classe è {@code HealingSkill} per non collidere con
 * l'interfaccia {@link HealSkill}.</p>
 */
public class HealingSkill extends AbstractSkill implements HealSkill {

    /**
     * Crea una nuova skill di cura.
     *
     * @param id          identificatore univoco
     * @param name        nome della skill
     * @param description descrizione dell'effetto
     * @param element     elemento della skill
     * @param baseValue   quantità di cura base prima dello scaling
     */
    public HealingSkill(int id, String name, String description,
                        Element element, int baseValue) {
        super(id, name, description, element, baseValue);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Delega il calcolo a {@link #calculateEffectiveValue(int, int)}.</p>
     */
    @Override
    public int getHealAmount(int level, int tier) {
        return calculateEffectiveValue(level, tier);
    }

    /**
     * Applica la cura calcolata al caster. Il parametro {@code target} è
     * ignorato — in futuro potrebbe essere usato per skill di cura su alleato.
     */
    @Override
    public void cast(GameCharacter caster, GameCharacter target, int level, int tier) {
        caster.heal(getHealAmount(level, tier));
    }
}


