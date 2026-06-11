package it.unicam.cs.mpgc.rpg125571.model.item;

import it.unicam.cs.mpgc.rpg125571.model.enums.EquipmentSlot;
import it.unicam.cs.mpgc.rpg125571.model.modifier.DefModifier;
import it.unicam.cs.mpgc.rpg125571.model.modifier.Modifier;
import it.unicam.cs.mpgc.rpg125571.model.enums.ItemType;

import java.util.List;

public class Armor extends AbstractItem implements Equipable {
    private final int defence;
    private final EquipmentSlot slot;

    public Armor(int id, String name, ItemType type, String description, int defence, EquipmentSlot slot) {
        super(id, name, type, description);
        this.defence = defence;
        this.slot = slot;
    }

    @Override
    public List<Modifier> getModifiers(){
        return List.of(new DefModifier(defence));
    }

    @Override
    public EquipmentSlot getSlot() {
        return slot;
    }
}
