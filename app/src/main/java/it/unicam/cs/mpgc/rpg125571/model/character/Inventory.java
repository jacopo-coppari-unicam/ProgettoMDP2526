package it.unicam.cs.mpgc.rpg125571.model.character;

import it.unicam.cs.mpgc.rpg125571.model.item.Consumable;
import it.unicam.cs.mpgc.rpg125571.model.item.Equipable;
import it.unicam.cs.mpgc.rpg125571.model.item.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Zaino del giocatore: tiene traccia di tutti gli item posseduti
 * ma non ancora usati o equipaggiati.
 *
 * <p>Non impone un limite di capienza per ora. La separazione tra
 * {@code Inventory} e {@link it.unicam.cs.mpgc.rpg125571.model.item.Equipment}
 * riflette la distinzione tra "item che possiedo" e "item che porto addosso":
 * un item equipaggiato non viene rimosso dall'inventario; sta al codice
 * chiamante decidere se tenere o meno questa coerenza.</p>
 *
 * <p>I metodi di filtro per tipo ({@link #getConsumables()},
 * {@link #getEquipables()}) semplificano la vita alla UI senza aggiungere
 * logica di business qui.</p>
 */
public class Inventory {

    private final List<Item> items = new ArrayList<>();

    /**
     * Aggiunge un item all'inventario.
     *
     * @param item l'item da aggiungere; non deve essere {@code null}
     */
    public void addItem(Item item) {
        items.add(item);
    }

    // Rimuove un item dall'inventario.
    // (Item item) è l'item da rimuovere
    // return (true) se l'item era presente ed è stato rimosso
    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    /**
     * Controlla se l'inventario contiene un dato item.
     *
     * @param item l'item da cercare
     * @return {@code true} se presente
     */
    public boolean hasItem(Item item) {
        return items.contains(item);
    }

    /**
     * Vista completa e non modificabile dell'inventario.
     *
     * @return lista immutabile di tutti gli {@link Item} posseduti
     */
    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    /**
     * Filtra e restituisce solo i consumabili presenti nell'inventario.
     * Utile per costruire il menu "usa oggetto" nella UI.
     *
     * @return lista di {@link Consumable} (può essere vuota)
     */
    public List<Consumable> getConsumables() {
        return items.stream()
                .filter(i -> i instanceof Consumable)
                .map(i -> (Consumable) i)
                .toList();
    }

    /**
     * Filtra e restituisce solo gli item equipaggiabili presenti nell'inventario.
     *
     * @return lista di {@link Equipable} (può essere vuota)
     */
    public List<Equipable> getEquipables() {
        return items.stream()
                .filter(i -> i instanceof Equipable)
                .map(i -> (Equipable) i)
                .toList();
    }
}
