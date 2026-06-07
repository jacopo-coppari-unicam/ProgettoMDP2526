package it.unicam.cs.mpgc.rpg125571.model.modifier;

import java.util.List;

/*  Rappresenta qualsiasi elemento del gioco che può fornire modificatori alle statistiche
    - Item Eqipaggiati
    - Pet (Future)
    - Achivement (Future)

    si occupa di filtrare ciò che è attivo (es. oggetti equipaggiati, pet equipaggiati..)
    e fornisce i Modifier che vengono in seguito aggiunti alle statistiche di base (baseStats)
*/

public interface ModifierSource {
    List<Modifier> getModifiers();
}
