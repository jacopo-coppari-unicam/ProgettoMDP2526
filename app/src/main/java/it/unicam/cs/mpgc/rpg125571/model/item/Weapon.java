package it.unicam.cs.mpgc.rpg125571.model.item;

import it.unicam.cs.mpgc.rpg125571.model.enums.ItemType;

public class Weapon extends AbstractItem implements Equipable {
    private int damage;

    public Weapon(int id, String name, ItemType type, String description, int damage) {
        super(id, name, type, description);
        this.damage = damage;
    }


}
