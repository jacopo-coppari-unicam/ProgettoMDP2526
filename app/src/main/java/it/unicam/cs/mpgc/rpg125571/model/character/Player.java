package it.unicam.cs.mpgc.rpg125571.model.character;

import it.unicam.cs.mpgc.rpg125571.model.item.Equipment;
import it.unicam.cs.mpgc.rpg125571.model.skill.SkillLoadout;

public class Player extends GameCharacter {
    private final Inventory inventory;
    private final SkillInventory skillInventory;
    private final SkillLoadout skillLoadout;

    /**
     * Costruisce il giocatore con le strutture dati già inizializzate a vuoto.
     *
     * @param name      nome del personaggio
     * @param level     livello iniziale
     * @param baseStats statistiche di base
     * @param equipment slot di equipaggiamento
     */
    public Player(String name, int level, Stats baseStats, Equipment equipment) {
        super(name, level, baseStats, equipment);
        this.inventory = new Inventory();
        this.skillInventory = new SkillInventory();
        this.skillLoadout = new SkillLoadout();
    }

    /**
     * L'inventario del giocatore, contenente item consumabili ed equipaggiabili.
     *
     * @return l'inventario
     */
    public Inventory getInventory() { return inventory; }

    /**
     * L'insieme di tutte le skill sbloccate dal giocatore,
     * con i relativi dati di progressione.
     *
     * @return lo skill inventory
     */
    public SkillInventory getSkillInventory() { return skillInventory; }

    /**
     * Il loadout attivo: le skill equipaggiate per il combattimento.
     *
     * @return il loadout
     */
    public SkillLoadout getSkillLoadout() { return skillLoadout; }
}