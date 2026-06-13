package it.unicam.cs.mpgc.rpg125571.model.skill;

import it.unicam.cs.mpgc.rpg125571.model.character.GameCharacter;
import it.unicam.cs.mpgc.rpg125571.model.enums.Element;

public class DamageSkill extends AbstractSkill implements AttackSkill {

    public DamageSkill(int id, String name, String description,
                       Element element, int baseValue) {
        super(id, name, description, element, baseValue);
    }

    @Override
    public int getDamage(int level) {
        return calculateEffectiveValue(level);
    }

    // Deals calculated damage to the target. The caster is not affected by this skill
    @Override
    public void cast(GameCharacter caster, GameCharacter target, int level, int tier) {
        target.takeDamage(getDamage(level));
    }
}