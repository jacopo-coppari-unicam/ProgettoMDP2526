package it.unicam.cs.mpgc.rpg125571.model.modifier;

import it.unicam.cs.mpgc.rpg125571.model.character.Stats;

public class AtkModifier implements Modifier{
    private final int value;

    public AtkModifier(int value) {
        this.value = value;
    }

    // Applies the effect of the modifier to the Stats atk
    @Override
    public void apply(Stats stats){
        stats.setAtk(stats.getAtk() + value);
    }
}
