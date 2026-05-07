package it.unicam.cs.mpgc.rpg125571.model;

import it.unicam.cs.mpgc.rpg125571.model.enums.Element;
import it.unicam.cs.mpgc.rpg125571.model.enums.SkillType;
import it.unicam.cs.mpgc.rpg125571.model.enums.TargetType;

public class Skill {
    private int id;
    private String name;
    private String description;
    private Element element;
    private SkillType type;
    private TargetType target;
    private int value; // valore stat della skill +10 ATK/+50 HEAL
    // REQUISITI DI EVOLUZIONE DELLA SKILL TODO
}
