package javaGame;

import java.io.*;
import java.util.Scanner;

/**
 * Die Klasse Crawler ist die Klasse, in der das gesamte Spiel ausgefuehrt wird.
 *
 * @author Lukas Schulz
 *
 * @version 1.0
 */
public class Crawler {

    /**
     * Dateiname fuer das Savegame
     */
    private static final String SAVE_GAME = "save.ser";

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        try {
            Item.readCSV("C:\\Users\\Lukas\\eclipse-workspaces\\eclipse-workspace-PlayerMonster\\Game\\src\\javaGame\\item.csv");
        } catch (IOException e) {
            System.out.println("Die item.csv kann nicht gelesen werden");
            return;
        }
        try {
            Quest.readCSV("C:\\Users\\Lukas\\eclipse-workspaces\\eclipse-workspace-PlayerMonster\\Game\\src\\javaGamey\\quest.csv");
        } catch (IOException e) {
            System.out.println("Die quest.csv kann nicht gelesen werden");
            return;
        }
        MazeGenerator mg = new RecursiveBacktracker();
        Level m = new Level(mg.generate(11, 31));

        Scanner sc = new Scanner(System.in);
        Player p = new Player();

        System.out.println("Soll das Spiel geladen werden? (j/n)");

        if (sc.nextLine().equals("j")) {
            try {
                FileInputStream fileInputStream = new FileInputStream(SAVE_GAME);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                p = (Player) objectInputStream.readObject();
                fileInputStream.close();
                System.out.println("Das Spiel wurde geladen.");
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Konnte das Spiel nicht laden, es wird ein neues Spiel gestartet");
            }
        }

        while (!p.isDefeated()) {
            System.out.println(m);
            m.showPrompt();
            String input = sc.nextLine();
            if (input.isEmpty()) {
                System.out.println("Leere Eingabe, bitte einen Befehl eingeben");
            } else {
                char direction = input.charAt(0);
                System.out.println();
                if (!(m.canMove(direction) || "ilkq".contains(direction + ""))) {
                    System.out.println("Ungueltiger Befehl");
                } else {
                    switch (direction) {
                        case 'i':
                            System.out.printf("Gold: %.2f%n", p.getGold());
                            System.out.println("Inventar:");
                            System.out.println(p.getInventory());
                            break;
                        case 'l':
                            System.out.println("Questlog:");
                            System.out.println(p.getQuestLog());
                            break;
                        case 'k':
                            System.out.println("Abgegebene Quests:");
                            System.out.println(p.getCompletedQuests());
                            break;
                        case 'q':
                            try {
                                FileOutputStream fileOutputStream = new FileOutputStream(SAVE_GAME);
                                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                                objectOutputStream.writeObject(p);
                                fileOutputStream.flush();
                                fileOutputStream.close();
                                System.out.println("Das Spiel wurde gespeichert");
                            } catch (IOException e) {
                                System.out.println("Konnte das Spiel nicht speichern");
                            }
                            break;
                        default:
                            m.move(direction);
                            m.handleCurrentFieldEvent(p);
                    }
                }
            }
        }
    }
}
