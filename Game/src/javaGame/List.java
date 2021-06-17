package javaGame;

import java.io.Serializable;

/**
 * Das Interface List.
 *
 * @param <T> Typ welcher Comparable implementiert
 *
 * @author Lukas Schulz
 *
 * @version 1.0
 */
public interface List<T extends Comparable<T>> extends Serializable {

    /**
     * Ueberprueft ob die Liste leer ist
     *
     * @return true, Liste ist leer
     */
    boolean isEmpty();

    /**
     * Gibt die Laenge der Liste zurueck
     *
     * @return die Laenge
     */
    int length();

    /**
     * Prueft ob ein Item in der Liste ist
     *
     * @param x das Item
     *
     * @return true, x ist in der Liste enthalten
     */
    boolean isInList(T x);

    /**
     * Gibt das erste Item der Liste zurueck
     *
     * @return das erste Item
     *
     * @throws IllegalStateException wenn die Liste leer ist
     */
    T firstItem() throws IllegalStateException;

    /**
     * Gibt das i-te Item der Liste zurueck
     *
     * @param i der Index
     *
     * @return das i-te Item
     *
     * @throws IndexOutOfBoundsException wenn i < 0 oder  i >= length()
     */
    T getItem(int i) throws IndexOutOfBoundsException;

    /**
     * Fuegt ein Element sortiert in die Liste ein
     *
     * @param x das Item
     *
     * @return die geanderte Liste
     */
    List insert(T x);

    /**
     * Fuegt ein Element an das Ende der Liste ein
     *
     * @param x das Item
     *
     * @return die geanderte Liste
     */
    List append(T x);

    /**
     * Loescht das erste vorkommen des Items x
     *
     * @param x das Item
     *
     * @return die geanderte Liste
     */
    List delete(T x);

    /**
     * Loescht das erste Element der Liste
     *
     * @return die geanderte Liste
     */
    List delete();

    /**
     * Zaehlt die Vorkommen von x in der Liste
     *
     * @param x das Element
     *
     * @return die Anzahl
     */
    int count(T x);
}
