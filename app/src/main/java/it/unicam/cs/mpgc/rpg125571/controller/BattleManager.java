package it.unicam.cs.mpgc.rpg125571.controller;

import it.unicam.cs.mpgc.rpg125571.model.Character.Enemy;
import it.unicam.cs.mpgc.rpg125571.model.Character.Player;

import java.util.Scanner;

public class BattleManager {
    private final Player player;
    private final Enemy enemy;
    private final Scanner scanner; // Per leggere gli input dell'utente da console

    public BattleManager(Player player, Enemy enemy) {
        this.player = player;
        this.enemy = enemy;
        this.scanner = new Scanner(System.in);
    }

    public void startBattle(){
        System.out.println("INIZIA LA BATTAGLIA CONTRO " + enemy.getName() + "!\n");

        // Il loop continua finché entrambi sono ancora vivi
        while(player.isAlive() && enemy.isAlive()){
            // Il giocatore attacca
            playerTurn();

            if(enemy.isDead()) break;

            // Il nemico attacca
            enemyTurn();

            System.out.println("------------------------------------------");
        }

        resolveBattle();
    }

    private void playerTurn(){
        System.out.println("\n--- TURNO DI " + player.getName() + " (" + player.getCurrentHP() + " HP) ---");
        System.out.println("Cosa vuoi fare?");
        System.out.println("1. Attacco Base");
        System.out.println("2. Usa Skill (Non ancora implementato)");
        System.out.print("Scegli un'opzione: ");

        int choice = scanner.nextInt();

        //
        if (choice == 1) {
            //
            int damage = player.getStats().getAtk();
            enemy.getDamage(damage);
            System.out.println("(+) " + player.getName() + " attacca e infligge " + damage + " danni a " + enemy.getName() + "!");
        }
    }

    private void enemyTurn(){
        System.out.println("\n--- TURNO DI " + enemy.getName() + " (" + enemy.getCurrentHP() + " HP) ---");

        // per ora implemento solo l'attacco base per i mostri, in seguito potrei impolementare
        int damage = enemy.getStats().getAtk();
        player.getDamage(damage);
        System.out.println("(+) " + enemy.getName() + " attacca e infligge " + damage + " danni a " + player.getName() + "!");
    }

    private void resolveBattle(){
        System.out.println("\n==========================================");
        if (player.isAlive()) {
            System.out.println("VITTORIA! " + enemy.getName() + " è stato sconfitto!");
            // player.gainExp(enemy.getExpReward());
            // player.getInventory().addItem(enemy.getLoot());
        } else {
            System.out.println("SEI STATO SCONFITTO... GAME OVER.");
        }
        System.out.println("==========================================");
    }
}
