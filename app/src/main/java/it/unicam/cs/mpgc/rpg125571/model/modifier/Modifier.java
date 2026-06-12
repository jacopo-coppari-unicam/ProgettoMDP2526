package it.unicam.cs.mpgc.rpg125571.model.modifier;

import it.unicam.cs.mpgc.rpg125571.model.character.Stats;

/*  Interface that defines the contract for any game modifier.
    Each class that implements it decides
    autonomously which specific stat to alter and how.
 */
public interface Modifier {
    // Applies the effect of the modifier to the Stats object passed as a parameter.
    void apply(Stats stats);
}
