# 📌 Heros Avventure

Heros Avventure è un RPG single-player in cui il giocatore interpreta un eroe novizio che intraprende un viaggio di crescita attraverso combattimenti a turni contro mostri e boss. Progredendo nel gioco, il personaggio ottiene esperienza, sale di livello e raccoglie equipaggiamenti e oggetti che migliorano le sue statistiche, fino ad affrontare BOSS sempre più potenti.

---

## 🚀 Come eseguire il progetto

### Prerequisiti
- Java 25 (LTS)
- Gradle

### Istruzioni

```bash
git clone <url-del-repository>
cd <nome-cartella>
```

### Build del progetto
```bash
./gradlew build
```

### Esecuzione
```bash
./gradlew run
```

---

## 🤖 Uso di strumenti di AI

Durante la realizzazione del progetto ho utilizzato strumenti di Intelligenza Artificiale (ChatGPT / Google Gemini) come supporto tecnico e didattico.

In particolare, l’AI è stata utilizzata per:

creazione della logica matematica per un corretto avanzamento del giocatore e per la generazione dei nemici (implementata personalmente in seguito sotto forma di codice)
risolvere problemi relativi alla configurazione del progetto Gradle e all’integrazione di JavaFX (build.gradle, librerie, dipendenze)
supporto nella gestione dei file e del caricamento delle risorse JSON, principalmente per quanto riguarda la gestione dei path dei file
chiarimenti sulla sintassi e sull’utilizzo di strutture dati e stream in Java
supporto nella riorganizzazione della struttura del progetto (separazione della view creata con Scene Builder in più file FXML e successiva suddivisione del MainController in sotto-controller indipendenti)

Tutta la logica di gioco, il sistema di combattimento, la gestione dell’inventario, il sistema di progressione del personaggio e la suddivisione delle responsabilità tra le varie componenti sono stati progettati e sviluppati in modo autonomo.

##🧠 Contributo personale

Le principali componenti del progetto sviluppate in autonomia includono:

- progettazione e implementazione della logica di gioco e del sistema di progressione del personaggio (livelli, EXP e scaling delle statistiche)
- sviluppo del sistema di combattimento a turni, con gestione delle azioni del giocatore e dei nemici
- gestione dell’inventario e del sistema di equipaggiamento degli oggetti
- implementazione del sistema di loot e drop dei nemici in base a probabilità
- integrazione tra modello dati e interfaccia grafica JavaFX, mantenendo la separazione tra logica e view
- realizzazione delle interfacce grafiche tramite Scene Builder e successiva riorganizzazione in più file FXML (riorganizzazione AI) con relativi controller dedicati (personale)
