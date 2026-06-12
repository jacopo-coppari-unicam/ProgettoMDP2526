package it.unicam.cs.mpgc.rpg125571.model.skill;

public interface HealSkill extends Skill {
    int getHealAmount(int level, int tier);
}
