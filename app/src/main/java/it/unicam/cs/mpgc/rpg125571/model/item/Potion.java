package it.unicam.cs.mpgc.rpg125571.model.item;

import it.unicam.cs.mpgc.rpg125571.model.character.GameCharacter;
import it.unicam.cs.mpgc.rpg125571.model.modifier.Modifier;
import it.unicam.cs.mpgc.rpg125571.model.enums.ItemType;

import java.util.List;

public class Potion extends AbstractItem implements Consumable {

    private final List<Modifier> modifiers;

    public Potion(int id, String name, ItemType type, String description, List<Modifier> modifiers) {
        super(id, name, type, description);
        this.modifiers = modifiers;
    }

    @Override
    public void use(GameCharacter target) {

        for (Modifier modifier : modifiers) {
            target.addTemporaryModifier(modifier);
        }
    }
}
