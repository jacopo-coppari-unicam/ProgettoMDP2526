package it.unicam.cs.mpgc.rpg125571.model.modifier;

import it.unicam.cs.mpgc.rpg125571.model.character.Stats;

public class MaxHpModifier implements Modifier {

    private final int value;

    public MaxHpModifier(int value) {
        this.value = value;
    }

    @Override
    public void apply(Stats stats) {
        stats.setMaxHp(stats.getMaxHp() + value);
    }
}
