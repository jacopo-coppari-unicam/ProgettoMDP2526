package it.unicam.cs.mpgc.rpg125571.model.item.itemtype;

import it.unicam.cs.mpgc.rpg125571.model.character.GameCharacter;
import it.unicam.cs.mpgc.rpg125571.model.item.AbstractItem;
import it.unicam.cs.mpgc.rpg125571.model.item.Consumable;
import it.unicam.cs.mpgc.rpg125571.model.modifier.Modifier;
import it.unicam.cs.mpgc.rpg125571.model.modifier.TemporaryModifier;
import it.unicam.cs.mpgc.rpg125571.model.enums.ItemType;

import java.util.List;

public class Potion extends AbstractItem implements Consumable {
    // Immutable list of modifiers (effects) that the potion will apply
    private final List<Modifier> modifiers;
    // Duration in turns or time of the potion's effect
    private final int duration;

    public Potion(int id, String name, ItemType type, String description,
                  List<Modifier> modifiers, int duration) {
        super(id, name, type, description);
        this.modifiers = List.copyOf(modifiers);
        this.duration = duration;
    }

    // Consume the potion, applying its effects to the target character.
    // Implementation of the Consumable interface method.
    @Override
    public void use(GameCharacter target) {
        for (Modifier modifier : modifiers) {
            // Converts each base modifier to a temporary modifier by entering the duration,
            // and adds it directly to the target character.
            target.addTemporaryModifier(new TemporaryModifier(modifier, duration));
        }
    }

    public int getDuration() { return duration; }
}
