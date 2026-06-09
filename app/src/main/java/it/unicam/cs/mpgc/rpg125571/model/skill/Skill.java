package it.unicam.cs.mpgc.rpg125571.model.skill;

import it.unicam.cs.mpgc.rpg125571.model.character.GameCharacter;
import it.unicam.cs.mpgc.rpg125571.model.enums.Element;

/**
 * Contratto base per qualsiasi skill del gioco.
 *
 * <p>Ogni skill è identificata da un id univoco, ha un nome, una descrizione
 * e appartiene a un elemento. Il comportamento concreto — danno, cura, buff —
 * è delegato alle sotto-interfacce e alle rispettive implementazioni.</p>
 *
 * <p>Tutto il codice che orchestra le skill (loadout, inventario, UI) dovrebbe
 * dipendere da questo tipo, mai da classi concrete.</p>
 */
public interface Skill {

    /**
     * Identificatore univoco della skill all'interno del gioco.
     *
     * @return l'id della skill
     */
    int getId();

    /**
     * Nome leggibile della skill, usato per la UI e i log di combattimento.
     *
     * @return il nome della skill
     */
    String getName();

    /**
     * Testo che descrive l'effetto della skill al giocatore.
     *
     * @return la descrizione della skill
     */
    String getDescription();

    /**
     * Elemento a cui appartiene la skill (fuoco, acqua, ecc.).
     * Usato per calcolare resistenze ed affinità elementali.
     *
     * @return l'elemento della skill
     */
    Element getElement();

    /**
     * Esegue l'effetto della skill nel contesto di un combattimento.
     *
     * <p>A seconda dell'implementazione, l'effetto può colpire il bersaglio,
     * curare il caster, o entrambe le cose. I parametri {@code level} e
     * {@code tier} scalano il valore finale dell'effetto.</p>
     *
     * @param caster il personaggio che usa la skill
     * @param target il personaggio bersaglio
     * @param level  il livello corrente della skill nel loadout del giocatore
     * @param tier   il tier della skill, che applica un bonus percentuale
     */
    void cast(GameCharacter caster, GameCharacter target, int level, int tier);
}

