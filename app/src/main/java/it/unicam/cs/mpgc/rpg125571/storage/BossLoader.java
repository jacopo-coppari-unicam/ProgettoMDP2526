package it.unicam.cs.mpgc.rpg125571.storage;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unicam.cs.mpgc.rpg125571.model.character.Enemy;
import it.unicam.cs.mpgc.rpg125571.model.character.Stats;
import it.unicam.cs.mpgc.rpg125571.model.item.Equipment;
import it.unicam.cs.mpgc.rpg125571.logic.LootChance;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class BossLoader {

    private final Gson gson = new Gson();
    private final List<Enemy> bosses = new ArrayList<>();

    public void loadBosses(String path) {
        bosses.clear();

        try (FileReader reader = new FileReader(path)) {
            JsonArray array = gson.fromJson(reader, JsonArray.class);

            for (JsonElement elem : array) {
                JsonObject obj = elem.getAsJsonObject();

                String name = obj.get("name").getAsString();
                int level = obj.get("level").getAsInt();
                int expReward = obj.get("expReward").getAsInt();
                int goldReward = obj.get("goldReward").getAsInt();

                JsonObject statsJson = obj.getAsJsonObject("stats");
                Stats stats = new Stats(
                        statsJson.get("atk").getAsInt(),
                        statsJson.get("def").getAsInt(),
                        statsJson.get("maxHp").getAsInt()
                );

                // BOSS TABLE
                List<LootChance> lootTable = new ArrayList<>();
                JsonArray lootArray = obj.getAsJsonArray("lootTable");
                if (lootArray != null) {
                    for (JsonElement lootElem : lootArray) {
                        JsonObject lootObj = lootElem.getAsJsonObject();
                        int itemId = lootObj.get("itemId").getAsInt();
                        double chance = lootObj.get("chance").getAsDouble();

                        lootTable.add(new LootChance(itemId, chance));
                    }
                }

                Enemy boss = new Enemy(name, level, stats, new Equipment(), expReward, goldReward, lootTable);

                bosses.add(boss);
            }

        } catch (Exception e) {
            throw new RuntimeException("Errore nel caricamento dei Boss di gioco", e);
        }
    }

    public List<Enemy> getBosses() {
        return List.copyOf(bosses);
    }
}