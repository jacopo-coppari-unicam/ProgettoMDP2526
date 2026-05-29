package it.unicam.cs.mpgc.rpg125571.model;

public class Stats {
    private int atk;
    private int def;
    // private int hp; Rimosso per Single Source of Truth
    private int maxHp;

    public Stats(int atk, int def, int maxHp) {
        this.atk = atk;
        this.def = def;
        this.maxHp = maxHp;
    }

    // Crea una copia delle statistiche che verrà utilizzate in seguito dal
    // ModifierSystem per l'aggiunta o la modifica delle statistiche
    // in base a l'equipaggiamento o le pozioni assunte
    public Stats copy() {
        return new Stats(atk, def, maxHp);
    }

    // Le statistiche other contengono:
    // Equipaggiamento, Pozioni, Pet (Future), Medagliette (Future)
    public void add(Stats other){
        this.atk += other.atk;
        this.def += other.def;
        this.maxHp += other.maxHp;
    }

    // Getter Stats
    public int getAtk() {return atk;}
    public int getDef() {return def;}
    public int getMaxHp() {return maxHp;}

    // Setter Stats
    public void setAtk(int atk) {this.atk = atk;}
    public void setDef(int def) {this.def = def;}
    public void setMaxHp(int maxHp) {this.maxHp = maxHp;}
}
