package javaGame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Die Klasse Item.
 *
 * @author Lukas Schulz
 *
 * @version 1.0
 */
public class Item implements Comparable<Item>, Serializable {

    /**
     * The constant names.
     */
    private static List<String> csv = new AVLTree<>();
    /**
     * The Name.
     */
    private String name;
    /**
     * The Value.
     */
    private double value;
    /**
     * The Weight.
     */
    private double weight;

    /**
     * Instantiates a new Item.
     *
     * @param name   the name
     * @param value  the value
     * @param weight the weight
     */
    public Item(String name, double value, double weight) {
        this.name = name;
        this.value = value;
        this.weight = weight;
    }

    /**
     * Instantiates a new Item.
     */
    public Item() {
        int r = (int) (Math.random() * csv.length());
        String[] data = csv.getItem(r).split("\\s*,\\s*");
        this.name = data[0];
        this.value = Double.parseDouble(data[1]);
        this.weight = Double.parseDouble(data[2]);
    }

    /**
     * Erstellt ein "Dummy" Item welches nur einen Namen beseitzt
     *
     * @param name der Name des Items
     */
    public Item(String name) {
        this(name, 0, 0);
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

    /**
     * Gibt die "Itemdatenbank" zurueck
     *
     * @return die Itemdatenbank
     */
    public static List<String> getDatabase() {
        return csv;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public double getValue() {
        return value;
    }

    /**
     * Gets weight.
     *
     * @return the weight
     */
    public double getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Item item = (Item) o;
        return Objects.equals(name, item.name);
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
        return String.format("%s - %.2f Gold - %.2f kg", name, value, weight);
    }

    /**
     * Compare to.
     *
     * @param item the item
     *
     * @return the int
     */
    @Override
    public int compareTo(Item item) {
        int v = name.compareTo(item.name);
        if (v == 0) {
            if (Double.compare(item.value, value) == 0) {
                return Double.compare(item.weight, weight);
            }
            return Double.compare(item.value, value);
        }
        return v;
    }
}
