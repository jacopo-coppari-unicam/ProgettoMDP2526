package it.unicam.cs.mpgc.rpg125571.model.Collectory;

public class ItemStack {
    private Item item = new Item();
    private int quantity;

    public ItemStack(Item item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    // Add an Item from the Stack
    public void add(int amount){
        quantity += amount;
    }

    // Remove an Item from the Stack
    public void remove(int amount){
        quantity -= amount;
    }

    public Item getItem(){return item;}
    public int getQuantity(){return quantity;}

}
