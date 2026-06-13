package it.unicam.cs.mpgc.rpg125571.model.skill;

import it.unicam.cs.mpgc.rpg125571.model.enums.Element;


public abstract class AbstractSkill implements Skill {

    private final int id;
    private final String name;
    private final String description;
    private final Element element;
    private final int baseValue;

    private static final int SCALE_FACTOR = 3;
    private static final double TIER_BONUS = 0.20;

    protected AbstractSkill(int id, String name, String description,
                            Element element, int baseValue) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.element = element;
        this.baseValue = baseValue;
    }

    /*
    Applies the scaling formula to the skill's base value.

    Used internally by subclasses to calculate damage or healing
    without rewriting the progression logic.

    The scaling formula is:
        effectiveValue = (baseValue + level * scaleFactor) * (1 + tier * tierBonus)

    The result is truncated to an integer.
     */
    public int calculateEffectiveValue(int level) {
        // Level 10 -> Tier 1, Level 20 -> Tier 2
        int tier = level / 10;
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

    // Base value of the skill (damage, healing or defense), before applying level and tier
    public int getBaseValue() { return baseValue; }
}
