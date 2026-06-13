package it.unicam.cs.mpgc.rpg125571.storage;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unicam.cs.mpgc.rpg125571.model.enums.Element;
import it.unicam.cs.mpgc.rpg125571.model.skill.DamageSkill;
import it.unicam.cs.mpgc.rpg125571.model.skill.HealingSkill;
import it.unicam.cs.mpgc.rpg125571.model.skill.Skill;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class SkillLoader {

    private final Gson gson = new Gson();
    private final List<Skill> skills = new ArrayList<>();

    public void loadSkills(String path) {

        skills.clear();

        try (FileReader reader = new FileReader(path)) {

            JsonArray array = gson.fromJson(reader, JsonArray.class);

            for (JsonElement elem : array) {

                JsonObject obj = elem.getAsJsonObject();

                int id = obj.get("id").getAsInt();
                String name = obj.get("name").getAsString();
                String description = obj.get("description").getAsString();
                Element element = Element.valueOf(obj.get("element").getAsString());
                String type = obj.get("type").getAsString();
                int baseValue = obj.get("baseValue").getAsInt();

                Skill skill = switch (type) {
                    case "DAMAGE" -> new DamageSkill(
                            id, name, description, element, baseValue
                    );
                    case "HEALING" -> new HealingSkill(
                            id, name, description, element, baseValue
                    );
                    default -> throw new IllegalArgumentException("Tipo skill non valido: " + type);
                };

                skills.add(skill);
            }

        } catch (Exception e) {
            throw new RuntimeException("Errore caricamento skills", e);
        }
    }

    public List<Skill> getSkills() {
        return List.copyOf(skills);
    }

    public Skill getById(int id) {
        return skills.stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .orElse(null);
    }
}