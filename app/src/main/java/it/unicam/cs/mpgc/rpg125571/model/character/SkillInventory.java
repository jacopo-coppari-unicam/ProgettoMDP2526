package it.unicam.cs.mpgc.rpg125571.model.character;

import it.unicam.cs.mpgc.rpg125571.model.skill.PlayerSkill;
import it.unicam.cs.mpgc.rpg125571.model.skill.Skill;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SkillInventory {
    private final List<PlayerSkill> ownedSkills = new ArrayList<>();

    public void addSkill(PlayerSkill playerSkill) {
        ownedSkills.add(playerSkill);
    }

    public void removeSkill(PlayerSkill playerSkill) {
        ownedSkills.remove(playerSkill);
    }

    public boolean hasSkill(Skill skill) {
        return ownedSkills.stream()
                .anyMatch(ps -> ps.getSkill().equals(skill));
    }

    public List<PlayerSkill> getOwnedSkills() {
        return Collections.unmodifiableList(ownedSkills);
    }
}
