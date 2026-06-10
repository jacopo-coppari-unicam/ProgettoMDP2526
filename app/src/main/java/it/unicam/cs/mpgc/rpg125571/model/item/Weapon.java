package it.unicam.cs.mpgc.rpg125571.model.item;

import it.unicam.cs.mpgc.rpg125571.model.modifier.AtkModifier;
import it.unicam.cs.mpgc.rpg125571.model.modifier.Modifier;
import it.unicam.cs.mpgc.rpg125571.model.enums.ItemType;

import java.util.List;

public class Weapon extends AbstractItem implements Equipable {
    private final int damage;

    public Weapon(int id, String name, ItemType type, String description, int damage) {
        super(id, name, type, description);
        this.damage = damage;
    }

    @Override
    public List<Modifier> getModifiers() {
        return List.of(new AtkModifier(damage));
    }

}
