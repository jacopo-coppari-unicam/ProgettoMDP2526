package it.unicam.cs.mpgc.rpg125571.model.skill;

// Associates a skill with the player's progress.
// It tracks the current level and accumulated mastery points.
/* When mastery points reach the threshold, the level increases
automatically and the counter is reset. */
/* Implements [SkillEquipable] so that it can be inserted
into a [SkillLoadout] without the latter knowing anything about the
progression details. */
public class PlayerSkill{

    // Point mastery needed for level up
    private static final int MASTERY_THRESHOLD = 100;

    private final Skill skill;
    private int currentLevel;
    private int masteryPoints;
    private boolean isEquipped;

    // Create a wrapper for the given skill, at level 1 with zero mastery
    public PlayerSkill(Skill skill, int level, int masteryPoints) {
        if (skill == null) throw new IllegalArgumentException("Skill non può essere null");
        this.skill = skill;
        this.currentLevel = Math.max(1, level);
        this.masteryPoints = Math.max(0, masteryPoints);
    }

    /* Adds mastery points and scales your level each time you exceed
    the threshold, allowing for multiple consecutive level-ups with a single gain. */
    public void gainMastery(int points) {
        if (points <= 0) return;
        this.masteryPoints += points;
        while (this.masteryPoints >= MASTERY_THRESHOLD) {
            this.masteryPoints -= MASTERY_THRESHOLD;
            this.currentLevel++;
        }
    }


    public Skill getSkill() { return skill; }
    // get Skill current level used in Skillcast (min 1)
    public int getCurrentLevel() { return currentLevel; }
    public int getMasteryPoints() { return masteryPoints; }
}