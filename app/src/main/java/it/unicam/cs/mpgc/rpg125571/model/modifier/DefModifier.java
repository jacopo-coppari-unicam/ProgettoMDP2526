package it.unicam.cs.mpgc.rpg125571.model.modifier;

import it.unicam.cs.mpgc.rpg125571.model.character.Stats;

public class DefModifier implements Modifier {

    private final int value;

    public DefModifier(int value) {
        this.value = value;
    }

    // Applies the effect of the modifier to the Stats def
    @Override
    public void apply(Stats stats) {
        stats.setDef(stats.getDef() + value);
    }
}
