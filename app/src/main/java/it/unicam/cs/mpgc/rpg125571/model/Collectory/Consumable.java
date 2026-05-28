package it.unicam.cs.mpgc.rpg125571.model.Collectory;

public class Consumable extends Item{
    private int consumeValue;


    public Consumable(int id, String name, String description, boolean stackable, int consumeValue){
        super(id, name, description, stackable);
        this.consumeValue = consumeValue;
    }

    public int getConsumeValue(){return consumeValue;}

    public void useConsumable(){

    }
}
