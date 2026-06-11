package it.unicam.cs.mpgc.rpg125571.logic.enemy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unicam.cs.mpgc.rpg125571.model.character.Enemy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EnemyLoader {

    public static List<Enemy> loadEnemies(String resourcePath) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Legge il file direttamente dal ClassLoader (pesca dentro src/main/resources)
            var inputStream = EnemyLoader.class.getClassLoader().getResourceAsStream(resourcePath);
            if (inputStream == null) {
                throw new IOException("File di risorsa non trovato nel percorso: " + resourcePath);
            }
            return mapper.readValue(inputStream, new TypeReference<List<Enemy>>() {});
        } catch (IOException e) {
            System.err.println("Errore nel caricamento dei nemici dalle risorse. Carico lista vuota.");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}