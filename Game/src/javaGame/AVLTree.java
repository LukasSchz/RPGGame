package javaGame;

import java.io.Serializable;

/**
 * Die generische Klasse AVLBaum.
 *
 * @param <T> Typ welcher Comparable implementiert
 *
 * @author Lukas Schulz
 *
 * @version 1.0
*/
public class AVLTree<T extends Comparable<T>> implements List<T> {

    /**
     * Der Rootknoten
     */
    private AVLNode root;

    /**
     * Ueberprueft ob die Liste leer ist
     *
     * @return true, Liste ist leer
     */
    @Override
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Gibt die Laenge der Liste zurueck
     *
     * @return die Laenge
     */
    @Override
    public int length() {
        if (isEmpty()) {
            return 0;
        }
        return root.size();
    }

    /**
     * Prueft ob ein Item in der Liste ist
     *
     * @param x das Item
     *
     * @return true, x ist in der Liste enthalten
     */
    @Override
    public boolean isInList(T x) {
        if (!isEmpty()) {
            return root.isInList(x);
        }
        return false;
    }

    /**
     * Gibt das erste Item der Liste zurueck
     *
     * @return das erste Item
     *
     * @throws IllegalStateException wenn die Liste leer ist
     */
    @Override
    public T firstItem() throws IllegalStateException {
        if (isEmpty()) {
            throw new IllegalStateException("empty");
        }
        return root.getSmallest().value;
    }

    /**
     * Gibt das i-te Item der Liste zurueck
     *
     * @param i der Index
     *
     * @return das i-te Item
     *
     * @throws IndexOutOfBoundsException wenn i < 0 oder  i >= length()
     */
    @Override
    public T getItem(int i) throws IndexOutOfBoundsException {
        if (i < 0 || i >= length()) {
            throw new IndexOutOfBoundsException();
        }
        return root.getItem(i);
    }

    /**
     * Fuegt ein Element sortiert in die Liste ein
     *
     * @param x das Item
     *
     * @return die geanderte Liste
     */
    @Override
    public List<T> insert(T x) {
        if (isEmpty()) {
            root = new AVLNode(x);
        } else {
            root.insert(x);
        }
        assert root.isBalanced();
        return this;
    }

    /**
     * Fuegt ein Element an das Ende der Liste ein
     *
     * @param x das Item
     *
     * @return die geanderte Liste
     */
    @Override
    public List append(T x) {
        throw new UnsupportedOperationException("append is unsupported");
    }

    /**
     * Loescht das erste vorkommen des Items x
     *
     * @param x das Item
     *
     * @return die geanderte Liste
     */
    @Override
    public List delete(T x) {
        if (!isEmpty()) {
            if (root.delete(x, false)) {
                root = null;
            } else {
                assert root.isBalanced();
            }
        }
        return this;
    }

    /**
     * Loescht das erste Element der Liste
     *
     * @return die geanderte Liste
     */
    @Override
    public List delete() {
        if (!isEmpty()) {
            delete(firstItem());
        }
        return this;
    }

    /**
     * Zaehlt die Vorkommen von x in der Liste
     *
     * @param x das Element
     *
     * @return die Anzahl
     */
    @Override
    public int count(T x) {
        if (!isEmpty()) {
            return root.count(x);
        }
        return 0;
    }

    /**
     * Gibt den ALVBaum in Latex qtree Syntax aus
     */
    private void printAVLTree() {
        System.out.print("\\Tree" + root.printTree());
    }

    /**
     * Koten eines AVL-Baums
     */
    class AVLNode implements Serializable {

        /**
         * linkes Kind
         */
        private AVLNode left;
        /**
         * rechtes Kind
         */
        private AVLNode right;

        /**
         * Hoehe des Knotens im Baum
         */
        private int height;
        /**
         * Wert des Knotens
         */
        private T value;
        /**
         * Anzahl des jeweiligen Items im Baum
         */
        private int count;

        /**
         * Erstellt einen neuen AVLBaum mit einer Leiche als Root Knoten
         */
        public AVLNode() {
        }

        /**
         * Erstellt einen neuen AVLBaum mit dem Element t als Root Knoten
         *
         * @param t Element
         */
        public AVLNode(T t) {
            value = t;
            count = 1;
            height = 1;
        }

        /**
         * Erstellt einen Knoten für den AVLBaum
         *
         * @param old   alter Knoten
         * @param left  Linker Baum
         * @param right Rechter Baum
         */
        private AVLNode(AVLNode old, AVLNode left, AVLNode right) {
            value = old.value;
            count = old.count;
            this.left = left;
            this.right = right;
            height();
        }

        /**
         * Gibt an ob der Teilbaum balanziert ist
         *
         * @return true wenn balanziert
         */
        public boolean isBalanced() {
            if ((left == null) && (right == null)) {
                return Math.abs(balance()) < 2;
            }
            if (left == null) {
                return Math.abs(balance()) < 2 && right.isBalanced();
            }
            if (right == null) {
                return Math.abs(balance()) < 2 && left.isBalanced();
            }
            return Math.abs(balance()) < 2 && right.isBalanced() && left.isBalanced();
        }

        /**
         * berechnet die Höhe des Knotens
         */
        private void height() {
            if (left == null && right == null) {
                height = 1;
                return;
            }
            if (left == null) {
                height = 1 + right.height;
                return;
            }
            if (right == null) {
                height = 1 + left.height;
                return;
            }
            height = 1 + Math.max(left.height, right.height);
        }

        /**
         * Gibt die Balance zurueck
         *
         * @return das Verhaeltnis vom linken zum rechten Unterbaum
         */
        private int balance() {
            if ((left == null) && (right == null)) {
                return 0;
            }
            if (left == null) {
                return 0 - right.height;
            }
            if (right == null) {
                return left.height;
            }
            return left.height - right.height;
        }

        /**
         * Rebalanziert den Baum
         *
         * @param balance die Balance
         */
        private void rebalance(int balance) {
            if (balance == 2) {
                if (left.balance() >= 0) { // right
                    rightRotation();
                } else { //left right
                    left.leftRotation();
                    rightRotation();
                }
                return;
            }
            if (balance == -2) {
                if (right.balance() <= 0) { // left
                    leftRotation();
                } else { // right left
                    right.rightRotation();
                    leftRotation();
                }
                return;
            }
            assert false;
        }

        /**
         * Rotiert den Baum nach Links
         */
        private void leftRotation() {
            AVLNode newLeft = new AVLNode(this, left, right.left);
            value = right.value;
            count = right.count;
            left = newLeft;
            right = right.right;
            height();
        }

        /**
         * Rotiert den Baum nach Rechts
         */
        private void rightRotation() {
            AVLNode newRight = new AVLNode(this, left.right, right);
            value = left.value;
            count = left.count;
            right = newRight;
            left = left.left;
            height();
        }

        /**
         * Prueft ob ein Item in der Liste ist
         *
         * @param elem das Item
         *
         * @return true, x ist in der Liste enthalten
         */
        public boolean isInList(T elem) {
            int comp = value.compareTo(elem);
            if (comp == 0) {
                return value.equals(elem);
            }
            if (right != null && comp < 0) {
                return right.isInList(elem);
            }
            if (left != null && comp > 0) {
                return left.isInList(elem);
            }
            return false;
        }

        /**
         * Fuegt das Element in den Baum ein
         *
         * @param t das Element
         */
        public void insert(T t) {
            int comp = value.compareTo(t);
            if (comp == 0) {
                count++;
                return;
            }
            if (comp > 0) { // ist kleiner also muss es nach links
                if (left == null) {
                    left = new AVLNode(t);
                } else {
                    left.insert(t);
                }
            } else { // und hier rechts
                if (right == null) {
                    right = new AVLNode(t);
                } else {
                    right.insert(t);
                }
            }
            height();
            int balance = balance();
            if (Math.abs(balance) > 1) {
                rebalance(balance);
            }
        }

        /**
         * Loescht das Item T aus dem Baum
         *
         * @param t     das Item
         * @param force gibt an ob der gefundene Knoten ohne testen geloescht werden soll
         *
         * @return bei true soll der Knoten geloescht werden
         */
        private boolean delete(T t, boolean force) {
            if (value.equals(t)) {
                if (force) {
                    return true;
                }
                count--;
                if (count > 0) {
                    return false;
                }

                if ((left == null) && (right == null)) { // er hat keine Kinder
                    return true;
                }
                if (left == null) { // hat nur ein kind rechts
                    value = right.value;
                    count = right.count;
                    right = null;
                    height();
                    return false;
                }
                if (right == null) { // hat nur ein kind links
                    value = left.value;
                    count = left.count;
                    left = null;
                    height();
                    return false;
                }
                // der knoten hat zwei kinder
                AVLNode next = right.getSmallest(); // es muss ein vorgaenger existieren,
                value = next.value;
                count = next.count;
                if (right.delete(next.value, true)) {
                    right = null;
                }
                height();
                int balance = balance();
                if (Math.abs(balance) > 1) {
                    rebalance(balance);
                }
                return false;
            }
            if (value.compareTo(t) > 0) { // ist groesser also muss es nach links
                if (left == null) {
                    return false;
                } else {
                    if (left.delete(t, false)) {
                        left = null;
                    }
                }
            } else {  // und hier rechts
                if (right == null) {
                    return false;
                } else {
                    if (right.delete(t, false)) {
                        right = null;
                    }
                }
            }
            height();
            int balance = balance();
            if (Math.abs(balance) > 1) {
                rebalance(balance);
            }
            return false;
        }

        /**
         * Gibt das linkeste Kind zurueck
         *
         * @return der kleinste Teilbaum
         */
        private AVLNode getSmallest() {
            AVLNode next = this;
            while (next.left != null) {
                next = next.left;
            }
            return next;
        }

        /**
         * Zaehlt Vorkommen von x im Baum
         *
         * @param x das gesuchte Element
         *
         * @return die Anzahl
         */
        private int count(T x) {
            int comp = value.compareTo(x);
            if (value.equals(x)) {
                return count;
            }
            if (left != null && comp > 0) {
                return left.count(x);
            }
            if (right != null && comp < 0) {
                return right.count(x);
            }
            return 0;
        }

        /**
         * Gibt die Anzahl der Items im Baum zurueck
         *
         * @return die Anzahl
         */
        private int size() {
            if (left == null && right == null) {
                return count;
            }
            if (left == null) {
                return count + right.size();
            }
            if (right == null) {
                return count + left.size();
            }
            return count + left.size() + right.size();
        }

        /**
         * Gibt das Item an Position i zurueck
         *
         * @param i die Position
         *
         * @return das Item
         */
        private T getItem(int i) {
            assert i >= 0;
            int sizeLeft = 0;
            if (left != null) {
                sizeLeft = left.size();
            }

            if (i >= sizeLeft && i < sizeLeft + count) {
                return value;
            }

            if (i < sizeLeft) {
                assert left != null;
                return left.getItem(i);
            } else {
                return right.getItem(i - sizeLeft - count);
            }
        }

        /**
         * Hilfsmethode zur qtree Ausgabe
         *
         * @return Ausgabe des Baums als qtree
         */
        private String printTree() {
            if (left == null && right == null) {
                return "{" + value + " " + height + "}";
            }
            if (left == null) {
                return "[.{" + value + " " + height + "} { } " + right.printTree() + " ]";
            }
            if (right == null) {
                return "[.{" + value + " " + height + "} " + left.printTree() + " { } ]";
            }
            return "[.{" + value + " " + height + "} " + left.printTree() + " " + right.printTree() + " ]";
        }
    }

    /**
     * To string.
     *
     * @return the string
     */
    public String toString() {
        int length = length();
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < length; i++) {
            string.append(i);
            string.append(" - ");
            string.append(getItem(i));
            string.append("\n");
        }
        return string.toString();
    }
}
