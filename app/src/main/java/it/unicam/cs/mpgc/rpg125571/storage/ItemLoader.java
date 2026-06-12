package it.unicam.cs.mpgc.rpg125571.storage;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unicam.cs.mpgc.rpg125571.model.enums.EquipmentSlot;
import it.unicam.cs.mpgc.rpg125571.model.enums.ItemType;
import it.unicam.cs.mpgc.rpg125571.model.item.Item;
import it.unicam.cs.mpgc.rpg125571.model.item.itemtype.*;
import it.unicam.cs.mpgc.rpg125571.model.modifier.AtkModifier;
import it.unicam.cs.mpgc.rpg125571.model.modifier.DefModifier;
import it.unicam.cs.mpgc.rpg125571.model.modifier.Modifier;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ItemLoader {

    private final Gson gson = new Gson();
    private final List<Item> items = new ArrayList<>();

    public void loadItems(String path) {

        items.clear();

        try (FileReader reader = new FileReader(path)) {

            JsonArray array = gson.fromJson(reader, JsonArray.class);

            for (JsonElement elem : array) {
                JsonObject obj = elem.getAsJsonObject();

                int id = obj.get("id").getAsInt();
                String name = obj.get("name").getAsString();
                ItemType type = ItemType.valueOf(obj.get("type").getAsString());
                String description = obj.get("description").getAsString();

                Item item = switch (type) {
                    case WEAPON -> new Weapon(
                            id,
                            name,
                            type,
                            description,
                            obj.get("damage").getAsInt(),
                            EquipmentSlot.valueOf(obj.get("slot").getAsString())
                    );
                    case ARMOR -> new Armor(
                            id,
                            name,
                            type,
                            description,
                            obj.get("defence").getAsInt(),
                            EquipmentSlot.valueOf(obj.get("slot").getAsString())
                    );
                    case POTION -> {
                        int duration = obj.get("duration").getAsInt();
                        JsonArray modifiersArray = obj.getAsJsonArray("modifiers");
                        List<Modifier> modifiersList = new ArrayList<>();

                        if (modifiersArray != null) {
                            for (JsonElement modElem : modifiersArray) {
                                JsonObject modObj = modElem.getAsJsonObject();
                                String modifier = modObj.get("stat").getAsString();
                                int val = modObj.get("value").getAsInt();

                                if ("ATK".equalsIgnoreCase(modifier)) {
                                    modifiersList.add(new AtkModifier(val));
                                } else if ("DEF".equalsIgnoreCase(modifier)) {
                                    modifiersList.add(new DefModifier(val));
                                }
                            }
                        }
                        yield new Potion(id, name, type, description, modifiersList, duration);
                    }
                    case MATERIAL -> new Material(id, name, type, description);
                };

                items.add(item);
            }

        } catch (Exception e) {
            throw new RuntimeException("Errore caricamento items", e);
        }
    }

    public List<Item> getItems() {
        return List.copyOf(items);
    }

    public Item getById(int id) {
        return items.stream()
                .filter(i -> i.getId() == id)
                .findFirst()
                .orElse(null);
    }
}