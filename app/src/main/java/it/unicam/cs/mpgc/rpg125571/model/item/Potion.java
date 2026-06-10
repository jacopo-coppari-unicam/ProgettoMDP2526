package it.unicam.cs.mpgc.rpg125571.model.item;

import it.unicam.cs.mpgc.rpg125571.model.character.GameCharacter;
import it.unicam.cs.mpgc.rpg125571.model.modifier.Modifier;
import it.unicam.cs.mpgc.rpg125571.model.modifier.TemporaryModifier;
import it.unicam.cs.mpgc.rpg125571.model.enums.ItemType;

import java.util.List;

/**
 * Consumabile che applica uno o più buff temporanei al bersaglio.
 *
 * <p>Ogni modifier passato al costruttore viene incapsulato in un
 * {@link TemporaryModifier} con la durata indicata. I buff scadono
 * automaticamente quando {@link GameCharacter#tickTemporaryModifiers()}
 * viene chiamato a fine turno.</p>
 *
 * <p>Una pozione con {@code duration = 0} non avrebbe senso: il costruttore
 * richiede almeno 1 turno di durata.</p>
 */
public class Potion extends AbstractItem implements Consumable {

    private final List<Modifier> modifiers;
    private final int duration;

    /**
     * Crea una pozione.
     *
     * @param id          identificatore univoco
     * @param name        nome della pozione
     * @param type        tipo dell'item (tipicamente {@code ItemType.POTION})
     * @param description descrizione dell'effetto
     * @param modifiers   lista di modificatori da applicare al consumo
     * @param duration    durata in turni dei buff applicati (deve essere &gt; 0)
     */
    public Potion(int id, String name, ItemType type, String description,
                  List<Modifier> modifiers, int duration) {
        super(id, name, type, description);
        this.modifiers = List.copyOf(modifiers);
        this.duration = duration;
    }

    /**
     * Applica i buff al bersaglio, ciascuno con la durata configurata.
     *
     * @param target il personaggio che usa la pozione
     */
    @Override
    public void use(GameCharacter target) {
        for (Modifier modifier : modifiers) {
            target.addTemporaryModifier(new TemporaryModifier(modifier, duration));
        }
    }

    /**
     * Durata in turni dei buff applicati da questa pozione.
     *
     * @return numero di turni
     */
    public int getDuration() { return duration; }
}
