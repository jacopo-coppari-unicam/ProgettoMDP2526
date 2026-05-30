package it.unicam.cs.mpgc.rpg125571.model.character.modificator;

import it.unicam.cs.mpgc.rpg125571.model.character.Stats;

public interface Modifier {
    Stats getBonus(Stats stats);
}
