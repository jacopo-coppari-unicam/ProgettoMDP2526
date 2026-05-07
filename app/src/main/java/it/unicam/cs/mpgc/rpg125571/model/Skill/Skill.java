package it.unicam.cs.mpgc.rpg125571.model.Skill;

import it.unicam.cs.mpgc.rpg125571.model.Character.GameCharacter;
import it.unicam.cs.mpgc.rpg125571.model.enums.Element;

public abstract class Skill {
    private final int id;
    private final String name;
    private final String description;
    private final Element element;
    private final int baseValue; // valore stat della skill +10 ATK/+50 HEAL

    public Skill(int id, String name, String description, Element element, int baseValue) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.element = element;
        this.baseValue = baseValue;
    }

    public abstract void cast(GameCharacter caster, GameCharacter target, int level, int tier);

    // Potenziamento delle Skill dato il livello di maestria (Level, Tier)
    private int scaleFactor = 3; // Aumento del value per livello
    private double tierBonus = 0.20; // Ogni Tier aumenta del 20% il valore base della skill, COMULABILE?

    public int calculateEffectiveValue(int level, int tier) {
        // La formula è:
        // Value = (BaseValue + ( Level x ScaleFactor) x (1 + (Tier x TierBonus)))
        double value = (this.baseValue + (level * this.scaleFactor)) * (1 + (tier * this.tierBonus));;

        return (int) value;
    }

    public String getName() { return name; }
    public int getValue() { return baseValue; }
    public Element getElement() { return element; }
}
