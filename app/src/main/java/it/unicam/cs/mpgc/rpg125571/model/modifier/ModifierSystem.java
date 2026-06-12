package it.unicam.cs.mpgc.rpg125571.model.modifier;

import it.unicam.cs.mpgc.rpg125571.model.character.Stats;

import java.util.List;

/*  Utility system responsible for calculating and dynamically applying
    modifiers to character stats (buffs, debuffs, equipment).*/
public class ModifierSystem {

/*  Calculates a character's final stats by taking their base stats
    and sequentially applying a list of active modifiers.

    baseStats - The character's initial (unchanged) stats.
    modifiers - The list of modifiers (items, potions, penalties) to apply.
    A new Stats object representing the recalculated final values.
     */
    public static Stats calculate(Stats baseStats, List<Modifier> modifiers) {
        Stats finalStats = baseStats.copy();
        // // Iterate through all the modifiers in the list (+10 ATK from a sword, -5 DEF from a debuff)
        for (Modifier modifier : modifiers) {
            modifier.apply(finalStats);
        }
        return finalStats;
    }
}
