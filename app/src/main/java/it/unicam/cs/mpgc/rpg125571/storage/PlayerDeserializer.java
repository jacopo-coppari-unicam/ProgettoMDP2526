package it.unicam.cs.mpgc.rpg125571.storage;

import com.google.gson.*;
import it.unicam.cs.mpgc.rpg125571.model.character.Inventory;
import it.unicam.cs.mpgc.rpg125571.model.character.Player;
import it.unicam.cs.mpgc.rpg125571.model.character.Stats;
import it.unicam.cs.mpgc.rpg125571.model.enums.EquipmentSlot;
import it.unicam.cs.mpgc.rpg125571.model.item.Equipable;
import it.unicam.cs.mpgc.rpg125571.model.item.Equipment;
import it.unicam.cs.mpgc.rpg125571.model.item.Item;
import it.unicam.cs.mpgc.rpg125571.model.skill.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Deserializza un {@link Player} dal JSON prodotto da {@link PlayerSerializer}.
 * Utilizza ItemLoader e SkillLoader per risolvere i riferimenti agli ID degli oggetti
 * e delle abilità nel mondo di gioco.
 */
public class PlayerDeserializer implements JsonDeserializer<Player> {

    private final ItemLoader itemLoader;
    private final SkillLoader skillLoader;

    /**
     * Costruttore aggiornato che accetta i nuovi componenti di storage.
     * I loader devono aver già effettuato il caricamento dei dati (es. tramite loadItems/loadSkills)
     * prima che venga invocata la deserializzazione del giocatore.
     */
    public PlayerDeserializer(ItemLoader itemLoader, SkillLoader skillLoader) {
        this.itemLoader = itemLoader;
        this.skillLoader = skillLoader;
    }

    @Override
    public Player deserialize(JsonElement json, Type typeOfT,
                              JsonDeserializationContext context) throws JsonParseException {
        JsonObject root = json.getAsJsonObject();

        // ── Dati primitivi ────────────────────────────────────────────────
        String name       = root.get("name").getAsString();
        int    level      = root.get("level").getAsInt();
        int    currentHp  = root.get("currentHp").getAsInt();
        int    experience = root.get("experience").getAsInt();

        // ── Stats ─────────────────────────────────────────────────────────
        JsonObject statsJson = root.getAsJsonObject("stats");
        Stats stats = new Stats(
                statsJson.get("atk").getAsInt(),
                statsJson.get("def").getAsInt(),
                statsJson.get("maxHp").getAsInt()
        );

        // ── Inventario ────────────────────────────────────────────────────
        Inventory inventory = new Inventory();
        if (root.has("inventoryItemIds")) {
            for (JsonElement idElem : root.getAsJsonArray("inventoryItemIds")) {
                int id = idElem.getAsInt();
                Item item = itemLoader.getById(id); // <--- Uso di ItemLoader
                if (item != null) {
                    inventory.addItem(item);
                } else {
                    System.err.println("[WARN] PlayerDeserializer: itemtype id=" + id + " non trovato tramite ItemLoader, saltato.");
                }
            }
        }

        // ── Equipment ─────────────────────────────────────────────────────
        Equipment equipment = new Equipment();
        if (root.has("equipment")) {
            JsonObject equipJson = root.getAsJsonObject("equipment");
            for (Map.Entry<String, JsonElement> entry : equipJson.entrySet()) {
                EquipmentSlot slot;
                try {
                    slot = EquipmentSlot.valueOf(entry.getKey());
                } catch (IllegalArgumentException e) {
                    System.err.println("[WARN] PlayerDeserializer: slot sconosciuto '" + entry.getKey() + "', saltato.");
                    continue;
                }
                int id = entry.getValue().getAsInt();
                Item item = itemLoader.getById(id); // <--- Uso di ItemLoader
                if (item instanceof Equipable equipable) {
                    equipment.equip(equipable);
                } else {
                    System.err.println("[WARN] PlayerDeserializer: itemtype id=" + id + " non trovato o non equipaggiabile, saltato.");
                }
            }
        }

        // ── Skill possedute ───────────────────────────────────────────────
        List<PlayerSkill> skills = new ArrayList<>();
        if (root.has("skills")) {
            for (JsonElement elem : root.getAsJsonArray("skills")) {
                JsonObject skillJson = elem.getAsJsonObject();
                int skillId      = skillJson.get("skillId").getAsInt();
                int skillLevel   = skillJson.get("level").getAsInt();
                int masteryPts   = skillJson.get("masteryPoints").getAsInt();

                Skill skill = skillLoader.getById(skillId); // <--- Uso di SkillLoader
                if (skill == null) {
                    System.err.println("[WARN] PlayerDeserializer: skill id=" + skillId + " non trovata tramite SkillLoader, saltata.");
                    continue;
                }
                PlayerSkill ps = new PlayerSkill(skill, skillLevel, masteryPts);
                skills.add(ps);
            }
        }

        // ── Loadout attivo ────────────────────────────────────────────────
        SkillLoadout loadout = new SkillLoadout();
        if (root.has("equippedSkillIds")) {
            for (JsonElement idElem : root.getAsJsonArray("equippedSkillIds")) {
                int skillId = idElem.getAsInt();
                // Cerca la PlayerSkill corrispondente tra quelle appena ricostruite
                skills.stream()
                        .filter(ps -> ps.getSkill().getId() == skillId)
                        .findFirst()
                        .ifPresentOrElse(
                                loadout::equip,
                                () -> System.err.println("[WARN] PlayerDeserializer: skill id=" + skillId + " non presente tra le skill possedute, saltata dal loadout.")
                        );
            }
        }

        // ── Costruzione Player ────────────────────────────────────────────
        Player player = new Player(name, level, stats, equipment,
                inventory, skills, loadout, experience);

        // ripristino diretto degli HP salvati tramite setCurrentHp().
        player.setCurrentHp(currentHp);

        return player;
    }
}