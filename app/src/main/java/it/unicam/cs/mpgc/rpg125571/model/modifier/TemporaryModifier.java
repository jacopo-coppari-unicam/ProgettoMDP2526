package it.unicam.cs.mpgc.rpg125571.model.modifier;

/* Adds duration to any [Modifier].
    Each time a turn passes, tick() should be called to
    decrement the counter. When isExpired() returns
    (true), the modifier should be removed from the character. */

/* Used primarily by potions, but is generic enough to
   cover any buff/debuff with a limited duration.*/
public class TemporaryModifier {

    private final Modifier modifier;
    private int remainingTurns;

/*  Creates a temporary modifier.
    modifier - The effect to apply, must not be null
    remainingTurns - The number of turns the effect will remain active (must be > 0)
    IllegalArgumentException if (remainingTurns) is less than or equal to zero */
    public TemporaryModifier(Modifier modifier, int remainingTurns) {
        if (remainingTurns <= 0) throw new IllegalArgumentException("remainingTurns must be > 0");
        this.modifier = modifier;
        this.remainingTurns = remainingTurns;
    }

    // Returns the  effect, ready to be passed to [ModifierSystem]
    public Modifier getModifier() { return modifier; }

    public void tick() {
        if (remainingTurns > 0) remainingTurns--;
    }

    public boolean isExpired() { return remainingTurns <= 0; }

    public int getRemainingTurns() { return remainingTurns; }
}
