package it.unicam.cs.mpgc.rpg125571.model.modifier;

import java.util.List;

/*  Represents any element in the game that can provide stat modifiers.
    - Equipped Items
    - Pets (Future)
    - Achievements (Future)

    Filters what's active (equipped items, equipped pets, etc.)
    and provides modifiers that are then added to the base stats (baseStats).
*/

public interface ModifierSource {
    List<Modifier> getModifiers();
}
