package it.unicam.cs.mpgc.rpg125571.model.skill;

/**
 * Wrapper che associa una {@link Skill} al progresso del giocatore.
 *
 * <p>Tiene traccia del livello corrente e dei punti mastery accumulati.
 * Quando i mastery points raggiungono la soglia, il livello aumenta
 * automaticamente e il contatore viene azzerato.</p>
 *
 * <p>Implementa {@link SkillEquipable} così da poter essere inserita
 * in un {@link SkillLoadout} senza che quest'ultimo sappia nulla dei
 * dettagli di progressione.</p>
 */
public class PlayerSkill implements SkillEquipable {

    /** Punti mastery necessari per salire di livello. */
    private static final int MASTERY_THRESHOLD = 100;

    private final Skill skill;
    private int currentLevel;
    private int masteryPoints;
    private boolean isEquipped;

    /**
     * Crea un wrapper per la skill data, al livello 1 con zero mastery.
     *
     * @param skill la skill da wrappare; non deve essere {@code null}
     */
    public PlayerSkill(Skill skill) {
        this.skill = skill;
        this.currentLevel = 1;
        this.masteryPoints = 0;
        this.isEquipped = false;
    }

    /**
     * Aggiunge punti mastery e scala il livello se si supera la soglia.
     *
     * <p>Il livello può salire al massimo di uno per chiamata, indipendentemente
     * dai punti aggiunti. Se si vogliono più level-up consecutivi bisogna
     * chiamare il metodo più volte.</p>
     *
     * @param points punti mastery da aggiungere; valori negativi sono ignorati
     */
    public void gainMastery(int points) {
        if (points <= 0) return;
        this.masteryPoints += points;
        if (this.masteryPoints >= MASTERY_THRESHOLD) {
            this.currentLevel++;
            this.masteryPoints = 0;
        }
    }

    @Override
    public Skill getSkill() { return skill; }

    @Override
    public boolean isEquipped() { return isEquipped; }

    @Override
    public void setEquipped(boolean equipped) { this.isEquipped = equipped; }

    /**
     * Livello corrente della skill, usato come parametro in {@link Skill#cast}.
     *
     * @return il livello corrente (minimo 1)
     */
    public int getCurrentLevel() { return currentLevel; }

    /**
     * Punti mastery accumulati nel livello corrente.
     *
     * @return i punti mastery correnti (0 – 99)
     */
    public int getMasteryPoints() { return masteryPoints; }
}