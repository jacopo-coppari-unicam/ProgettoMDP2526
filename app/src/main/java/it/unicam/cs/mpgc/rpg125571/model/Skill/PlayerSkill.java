package it.unicam.cs.mpgc.rpg125571.model.Skill;

public class PlayerSkill {
    private Skill skill;
    private int currentLevel;
    private int masteryPoints;
    private boolean isEquipped;

    public PlayerSkill(Skill skill){
        this.skill = skill;
        this.currentLevel = 1;
        this.masteryPoints = 0;
        this.isEquipped = false; // Rendo la skill non equipaggiata di base
    }

    public void gainMastery(int points){
        this.masteryPoints += points;
        if(this.masteryPoints > 100){
            this.currentLevel++; // la skill sale di livello
            this.masteryPoints = 0; // I punti maestria si azzerano
        }
    }
}
