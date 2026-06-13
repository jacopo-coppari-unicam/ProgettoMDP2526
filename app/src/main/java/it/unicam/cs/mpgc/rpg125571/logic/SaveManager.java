package it.unicam.cs.mpgc.rpg125571.logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unicam.cs.mpgc.rpg125571.model.character.Player;
import it.unicam.cs.mpgc.rpg125571.storage.ItemLoader;
import it.unicam.cs.mpgc.rpg125571.storage.PlayerDeserializer;
import it.unicam.cs.mpgc.rpg125571.storage.PlayerSerializer;
import it.unicam.cs.mpgc.rpg125571.storage.SkillLoader;

import java.io.*;

public class SaveManager {

    private final Gson gson;

    public SaveManager(ItemLoader itemLoader, SkillLoader skillLoader) {
        // Initialize Gson using the 'Builder' pattern, to customize
        // the library's behavior before creating the final object.
        this.gson = new GsonBuilder()
                // SERIALIZER REGISTRATION (Writing) use PlayerSerializer custom rules
                .registerTypeAdapter(Player.class, new PlayerSerializer())
                // REGISTRATION OF THE DESERIALIZER (Read)
                // pass 'itemLoader' and 'skillLoader' to reconnect the JSON
                // IDs to the actual objects in the game
                .registerTypeAdapter(Player.class, new PlayerDeserializer(itemLoader, skillLoader))
                // Formats the generated JSON with line breaks and tabs (indents).
                // instead of having all the text compressed into a single line.
                .setPrettyPrinting()
                // Applies all previous configurations and generates the final Gson instance
                .create();
    }

    // Sava Player in file JSON
    public boolean saveGame(Player player, String filePath) {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(player, writer);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Load Player from file JSON
    public Player loadGame(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        try (Reader reader = new FileReader(file)) {
            return gson.fromJson(reader, Player.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}