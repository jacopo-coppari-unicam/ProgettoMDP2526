package it.unicam.cs.mpgc.rpg125571.model;

public class Stats {
    private int atk;
    private int def;
    private int hp;
    private int maxHp;

    public Stats(int atk, int def, int hp) {
        this.atk = atk;
        this.def = def;
        this.hp = hp;
        this.maxHp = hp;
    }

    // Crea una copia delle statistiche che verrà utilizzate in seguito dal
    // ModifierSystem per l'aggiunta o la modifica delle statistiche
    // in base a l'equipaggiamento o le pozioni assunte
    public Stats copy() {
        return new Stats(atk, def, hp);
    }

    // Le statistiche other contengono:
    // Equipaggiamento, Pozioni, Pet (Future), Medagliette (Future)
    public void add(Stats other){
        this.atk += other.atk;
        this.def += other.def;
        this.hp += other.hp;
        this.maxHp += other.maxHp;
    }

    // Getter Stats
    public int getAtk() {return atk;}
    public int getDef() {return def;}
    public int getHp() {return hp;}
    public int getMaxHp() {return maxHp;}

    // Setter Stats
    public int setAtk(int atk) {this.atk = atk;return atk;}
    public int setDef(int def) {this.def = def;return def;}
    public int setHp(int hp) {this.hp = hp;return hp;}
    public int setMaxHp(int maxHp) {this.maxHp = maxHp;return maxHp;}
}
