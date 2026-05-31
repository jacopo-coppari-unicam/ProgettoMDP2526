package it.unicam.cs.mpgc.rpg125571.model.character.modificator;

import it.unicam.cs.mpgc.rpg125571.model.character.Stats;

public class DefModifier implements Modifier {

    private final int value;

    public DefModifier(int value) {
        this.value = value;
    }

    @Override
    public void apply(Stats stats) {
        stats.setDef(stats.getDef() + value);
    }
}
