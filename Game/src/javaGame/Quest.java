package javaGame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Die Klasse Quest
 *
 * @author Lukas Schulz
 *
 * @version 1.0
 */
public class Quest implements Comparable<Quest>, Serializable {

    /**
     * The constant names.
     */
    private static List<String> csv = new AVLTree<>();
    /**
     * Name der Quest
     */
    private String name;
    /**
     * Name der Vorquest
     */
    private Quest prequest;
    /**
     * Das Questitem
     */
    private Item item;
    /**
     * Die Anzahl vom Questitem
     */
    private int quantity;

    /**
     * Instantiates a new Quest.
     *
     * @param name     the name
     * @param prequest the prequest
     * @param item     the item
     * @param quantity the quantity
     */
    public Quest(String name, String prequest, String item, int quantity) {
        this.name = name;
        this.prequest = prequest.isEmpty() ? null : new Quest(prequest, "", "", 0);
        this.item = new Item(item);
        this.quantity = quantity;
    }

    /**
     * Instantiates a new Quest.
     */
    public Quest() {
        int r = (int) (Math.random() * csv.length());
        String[] data = csv.getItem(r).split("\\s*,\\s*");
        this.name = data[0].trim();
        this.prequest = data[1].isEmpty() ? null : new Quest(data[1], "", "", 0);
        this.item = new Item(data[2]);
        this.quantity = Integer.parseInt(data[3]);
    }

    /**
     * Erstellt eine Platzhalter Quest
     *
     * @param entry Eintrag aus der Questdatenbank
     */
    public Quest(String entry) {
        String[] data = entry.split(",");
        this.name = data[0].trim();
        this.prequest = data[1].trim().isEmpty() ? null : new Quest(data[1].trim(), "", "", 0);
        this.item = new Item(data[2].trim());
        this.quantity = Integer.parseInt(data[3].trim());
    }

    /**
     * Gibt die Quest "Datenbank" zurueck
     *
     * @return die Questdatenbank
     */
    public static List<String> getDatabase() {
        return csv;
    }

    /**
     * Liest die CSV Datei ein
     *
     * @param path der Pfad zur CSV
     *
     * @throws IOException wenn etwas beim einlesen fehlschlaegt
     */
    public static void readCSV(String path) throws IOException {
        BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(path), Charset.forName("UTF-8"));
        String line = bufferedReader.readLine();
        while ((line = bufferedReader.readLine()) != null) {
            csv.insert(line);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Quest quest = (Quest) o;
        return Objects.equals(name, quest.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return String.format("%s [%s Item - %d Stueck]", name, item.getName(), quantity);
    }

    /**
     * Compare to.
     *
     * @param quest the quest
     *
     * @return the int
     */
    @Override
    public int compareTo(Quest quest) {
        return name.compareTo(quest.name);
    }

    /**
     * Prueft ob die Quest abschliessbar ist
     *
     * @param p der Spieler
     *
     * @return true, wenn die Quest abgeschlossen werden kann
     */
    public boolean isCompletable(Player p) {
        if (!p.getQuestLog().isInList(this)) {
            throw new IllegalArgumentException("Der Spieler besitzt diese Quest nicht");
        }
        int c = p.getInventory().count(item);
        return c >= quantity;
    }

    /**
     * Gibt die Quest ab
     *
     * @param p der Spieler
     */
    public void completQuest(Player p) {
        if (!isCompletable(p)) {
            throw new IllegalArgumentException("Der Spieler hat nicht die geforderte Anzahl von Questgegenstaenden");
        }
        for (int i = 0; i < quantity; i++) {
            p.getInventory().delete(item);
        }
        p.getQuestLog().delete(this);
        p.getCompletedQuests().insert(this);
    }

    /**
     * Gibt an, ob eine Quest fuer den jeweiligen Spieler verfuegbar ist
     *
     * @param p der Spieler
     *
     * @return true wenn die Quest annehmbar ist
     */
    public boolean isAvailable(Player p) {
        return !(p.getQuestLog().isInList(this) || p.getCompletedQuests().isInList(this)
            || (prequest != null && !p.getCompletedQuests().isInList(prequest)));
    }
}
