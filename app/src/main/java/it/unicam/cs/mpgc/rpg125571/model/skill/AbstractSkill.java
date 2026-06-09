package it.unicam.cs.mpgc.rpg125571.model.skill;

import it.unicam.cs.mpgc.rpg125571.model.enums.Element;

/**
 * Classe base per tutte le skill concrete.
 *
 * <p>Raccoglie lo stato condiviso (id, nome, descrizione, elemento, valore base)
 * e la formula di scaling, evitando duplicazione nelle sottoclassi. Non è pensata
 * per essere esposta come tipo pubblico: il codice esterno dovrebbe sempre
 * riferirsi a {@link Skill}, {@link AttackSkill} o {@link HealSkill}.</p>
 *
 * <p>La formula di scaling è:
 * <pre>
 *   effectiveValue = (baseValue + level * scaleFactor) * (1 + tier * tierBonus)
 * </pre>
 * Il risultato viene troncato a intero.</p>
 */
public abstract class AbstractSkill implements Skill {

    private final int id;
    private final String name;
    private final String description;
    private final Element element;
    private final int baseValue;

    private static final int SCALE_FACTOR = 3;
    private static final double TIER_BONUS = 0.20;

    /**
     * Inizializza i campi comuni a tutte le skill.
     *
     * @param id          identificatore univoco
     * @param name        nome della skill
     * @param description descrizione dell'effetto
     * @param element     elemento della skill
     * @param baseValue   valore base prima dello scaling
     */
    protected AbstractSkill(int id, String name, String description,
                            Element element, int baseValue) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.element = element;
        this.baseValue = baseValue;
    }

    /**
     * Applica la formula di scaling al valore base della skill.
     *
     * <p>Usato internamente dalle sottoclassi per calcolare danno o cura
     * senza riscrivere la logica di progressione.</p>
     *
     * @param level livello corrente della skill (parte da 1)
     * @param tier  tier della skill (0 = nessun bonus tier)
     * @return il valore scalato, troncato a intero
     */
    protected int calculateEffectiveValue(int level, int tier) {
        double value = (this.baseValue + (level * SCALE_FACTOR)) * (1 + (tier * TIER_BONUS));
        return (int) value;
    }

    @Override
    public int getId() { return id; }

    @Override
    public String getName() { return name; }

    @Override
    public String getDescription() { return description; }

    @Override
    public Element getElement() { return element; }

    /**
     * Valore base della skill, prima dell'applicazione di livello e tier.
     *
     * @return il valore base
     */
    public int getBaseValue() { return baseValue; }
}
