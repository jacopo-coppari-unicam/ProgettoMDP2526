package it.unicam.cs.mpgc.rpg125571.model.modifier;

/**
 * Decoratore che aggiunge una durata a qualsiasi {@link Modifier}.
 *
 * <p>Ogni volta che un turno passa, {@link #tick()} va chiamato per
 * decrementare il contatore. Quando {@link #isExpired()} restituisce
 * {@code true} il modifier dovrebbe essere rimosso dal personaggio.</p>
 *
 * <p>Usato principalmente dalle pozioni, ma è generico abbastanza da
 * coprire qualsiasi buff/debuff con durata limitata.</p>
 */
public class TemporaryModifier {

    private final Modifier modifier;
    private int remainingTurns;

    /**
     * Crea un modifier temporaneo.
     *
     * @param modifier      l'effetto da applicare; non deve essere {@code null}
     * @param remainingTurns numero di turni per cui l'effetto rimane attivo (deve essere &gt; 0)
     * @throws IllegalArgumentException se {@code remainingTurns} è minore o uguale a zero
     */
    public TemporaryModifier(Modifier modifier, int remainingTurns) {
        if (remainingTurns <= 0) throw new IllegalArgumentException("remainingTurns must be > 0");
        this.modifier = modifier;
        this.remainingTurns = remainingTurns;
    }

    /**
     * Restituisce l'effetto incapsulato, pronto per essere passato a {@link ModifierSystem}.
     *
     * @return il modifier associato
     */
    public Modifier getModifier() { return modifier; }

    /**
     * Decrementa di uno il contatore di turni rimanenti.
     * Non ha effetto se il modifier è già scaduto.
     */
    public void tick() {
        if (remainingTurns > 0) remainingTurns--;
    }

    /**
     * Indica se il modifier ha esaurito la sua durata e va rimosso.
     *
     * @return {@code true} se i turni rimanenti sono arrivati a zero
     */
    public boolean isExpired() { return remainingTurns <= 0; }

    /**
     * Turni ancora attivi per questo modifier.
     *
     * @return turni rimanenti (0 significa scaduto)
     */
    public int getRemainingTurns() { return remainingTurns; }
}
