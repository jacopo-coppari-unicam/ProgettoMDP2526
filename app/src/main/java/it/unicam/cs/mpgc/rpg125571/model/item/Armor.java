package it.unicam.cs.mpgc.rpg125571.model.item;

import it.unicam.cs.mpgc.rpg125571.model.character.modificator.Modifier;
import it.unicam.cs.mpgc.rpg125571.model.enums.ItemType;

import java.util.List;

public class Armor extends AbstractItem implements Equipable {
    private int defence;

    public Armor(int id, String name, ItemType type, String description, int defence) {
        super(id, name, type, description);
        this.defence = defence;
    }

    @Override
    public List<Modifier> getModifiers(){
        return List.of(
                stats -> stats.addDef(defence)
        );
    }
}
