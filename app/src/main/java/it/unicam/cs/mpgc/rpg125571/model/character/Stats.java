package it.unicam.cs.mpgc.rpg125571.model.character;

public class Stats {
    private int atk;
    private int def;
    private int maxHp;

    public Stats(int atk, int def, int maxHp) {
        this.atk = atk;
        this.def = def;
        this.maxHp = maxHp;
    }

    public Stats copy(){
        return new Stats(atk, def, maxHp);
    }

    public int getAtk() {return atk;}
    public int getDef() { return def;}
    public int getMaxHp() {return maxHp;}
}