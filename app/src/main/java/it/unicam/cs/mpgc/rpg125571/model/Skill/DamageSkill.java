package it.unicam.cs.mpgc.rpg125571.model.Skill;

import it.unicam.cs.mpgc.rpg125571.model.Character.GameCharacter;
import it.unicam.cs.mpgc.rpg125571.model.enums.Element;

public class DamageSkill extends Skill{
    public DamageSkill(int id, String name, String description, Element element, int baseValue) {
        super(id, name, description, element, baseValue);
    }

    @Override
    public void cast(GameCharacter caster, GameCharacter target, int level, int tier){
        // Calcolo effettivo del danno data la maestria della Skill
        int damage = calculateEffectiveValue(level, tier);

        // Applicazione del danno
        target.getDamage(damage);
    }

}
