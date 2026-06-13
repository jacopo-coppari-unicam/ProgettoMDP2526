package it.unicam.cs.mpgc.rpg125571.storage;

import com.google.gson.*;
import it.unicam.cs.mpgc.rpg125571.model.character.Player;
import it.unicam.cs.mpgc.rpg125571.model.enums.EquipmentSlot;
import it.unicam.cs.mpgc.rpg125571.model.item.Equipable;
import it.unicam.cs.mpgc.rpg125571.model.item.Item;
import it.unicam.cs.mpgc.rpg125571.model.skill.PlayerSkill;

import java.lang.reflect.Type;


public class PlayerSerializer implements JsonSerializer<Player> {

    @Override
    public JsonElement serialize(Player player, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("name",       player.getName());
        jsonObject.addProperty("level",      player.getLevel());
        jsonObject.addProperty("currentHp",  player.getCurrentHp());
        jsonObject.addProperty("experience", player.getExperience());
        jsonObject.addProperty("gold",       player.getGold());

        JsonObject stats = new JsonObject();
        stats.addProperty("atk",   player.getBaseStats().getAtk());
        stats.addProperty("def",   player.getBaseStats().getDef());
        stats.addProperty("maxHp", player.getBaseStats().getMaxHp());
        jsonObject.add("stats", stats);

        JsonArray inventoryIds = new JsonArray();
        for (Item item : player.getInventory().getItems()) {
            inventoryIds.add(item.getId());
        }
        jsonObject.add("inventoryItemIds", inventoryIds);

        JsonObject equipment = new JsonObject();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            Equipable equipped = player.getEquipment().getEquippedItem(slot);
            if (equipped != null) {
                equipment.addProperty(slot.name(), equipped.getId());
            }
        }
        jsonObject.add("equipment", equipment);

        JsonArray skillsArray = new JsonArray();
        for (PlayerSkill ps : player.getSkillInventory()) {
            JsonObject skillEntry = new JsonObject();
            skillEntry.addProperty("skillId",       ps.getSkill().getId());
            skillEntry.addProperty("level",         ps.getCurrentLevel());
            skillEntry.addProperty("masteryPoints", ps.getMasteryPoints());
            skillsArray.add(skillEntry);
        }
        jsonObject.add("skills", skillsArray);

        JsonArray loadoutIds = new JsonArray();
        for (PlayerSkill se : player.getSkillLoadout().getEquippedSkills()) {
            loadoutIds.add(se.getSkill().getId());
        }
        jsonObject.add("equippedSkillIds", loadoutIds);

        return jsonObject;
    }
}