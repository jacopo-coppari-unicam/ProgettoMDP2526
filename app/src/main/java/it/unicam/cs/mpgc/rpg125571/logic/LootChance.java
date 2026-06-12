package it.unicam.cs.mpgc.rpg125571.logic;

public class LootChance {
    private int itemId;       // ID dell'itemtype da cercare nel catalogo del gioco
    private double chance;    // Percentuale di drop (es. 0.25 per il 25%, 1.0 per il 100%)

    // Costruttore vuoto per Jackson/JSON
    public LootChance() {}

    public LootChance(int itemId, double chance) {
        this.itemId = itemId;
        this.chance = chance;
    }

    public int getItemId() { return itemId; }
    public double getChance() { return chance; }
}