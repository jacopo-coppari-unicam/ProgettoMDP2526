package it.unicam.cs.mpgc.rpg125571.model.skill;

import it.unicam.cs.mpgc.rpg125571.model.character.GameCharacter;
import it.unicam.cs.mpgc.rpg125571.model.enums.Element;


public class HealingSkill extends AbstractSkill implements HealSkill {
    public HealingSkill(int id, String name, String description,
                        Element element, int baseValue) {
        super(id, name, description, element, baseValue);
    }

    @Override
    public int getHealAmount(int level, int tier) {
        return calculateEffectiveValue(level, tier);
    }

    // Apply a heal effect to the caster
    @Override
    public void cast(GameCharacter caster, GameCharacter target, int level, int tier) {
        caster.heal(getHealAmount(level, tier));
    }
}


