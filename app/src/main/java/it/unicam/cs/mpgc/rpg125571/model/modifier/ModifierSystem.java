package it.unicam.cs.mpgc.rpg125571.model.modifier;

import it.unicam.cs.mpgc.rpg125571.model.character.Stats;

import java.util.List;

public class ModifierSystem {

    public static Stats calculate(Stats baseStats, List<Modifier> modifiers) {

        Stats finalStats = baseStats.copy();

        for (Modifier modifier : modifiers) {
            modifier.apply(finalStats);
        }

        return finalStats;
    }
}
