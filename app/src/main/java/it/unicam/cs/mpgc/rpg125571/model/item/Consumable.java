package it.unicam.cs.mpgc.rpg125571.model.item;

import it.unicam.cs.mpgc.rpg125571.model.character.GameCharacter;

public interface Consumable extends Item {
    void use(GameCharacter target);
}
