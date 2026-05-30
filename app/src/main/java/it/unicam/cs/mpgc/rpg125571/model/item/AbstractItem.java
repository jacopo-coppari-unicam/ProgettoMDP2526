package it.unicam.cs.mpgc.rpg125571.model.item;

import it.unicam.cs.mpgc.rpg125571.model.enums.ItemType;

public abstract class AbstractItem implements Item {
    private int id;
    private String name;
    private ItemType type;
    private String description;

    public AbstractItem(int id, String name, ItemType type, String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
    }

    @Override
    public int getId(){ return id; }

    @Override
    public String getName(){ return name; }

    @Override
    public ItemType getType(){ return type; }

    @Override
    public String getDescription(){ return description; }
}
