package it.unicam.cs.mpgc.rpg125571.model.item;

import it.unicam.cs.mpgc.rpg125571.model.enums.EquipmentSlot;
import it.unicam.cs.mpgc.rpg125571.model.modifier.ModifierSource;

public interface Equipable extends Item, ModifierSource {
    EquipmentSlot getSlot();
}
