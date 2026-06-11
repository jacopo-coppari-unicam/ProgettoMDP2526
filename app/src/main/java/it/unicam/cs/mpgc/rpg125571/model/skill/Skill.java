package it.unicam.cs.mpgc.rpg125571.model.skill;

import it.unicam.cs.mpgc.rpg125571.model.character.GameCharacter;
import it.unicam.cs.mpgc.rpg125571.model.enums.Element;

public interface Skill {
    int getId();
    String getName();
    String getDescription();
    Element getElement();
    void cast(GameCharacter caster, GameCharacter target, int level, int tier);
}

