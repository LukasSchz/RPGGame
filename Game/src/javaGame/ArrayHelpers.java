package javaGame;

/**
 * Die Klasse ArrayHelpers ist eine Hilfsklasse.
 *
 * @author Lukas Schulz
 *
 * @version 1.0
 */
public class ArrayHelpers {
    /**
     * Copy int [ ].
     *
     * @param x      das Array
     * @param length die Laenge des neuen Array
     *
     * @return ein neues Array
     */
    public static int[] copy(int[] x, int length) {
        int[] newCopy = new int[length];
        System.arraycopy(x, 0, newCopy, 0, length);
        return newCopy;
    }

    /**
     * Delete int [ ].
     *
     * @param x     das Array
     * @param index die Position des zu loeschenden Elementes
     *
     * @return ein neues Array ohne das geloeschte Element
     */
    public static int[] delete(int[] x, int index) {
        int[] changed = new int[x.length - 1];
        int next = 0;
        for (int i = 0; i < x.length; ++i) {
            if (!(i == index)) {
                changed[next] = x[i];
                next++;
            }
        }
        return changed;
    }

    /**
     * In int [ ].
     *
     * @param x das Array
     * @param n das zu findene Element
     *
     * @return true wenn das Element n im Array x ist sonst false
     */
    private static boolean in(int[] x, int n) {
        for (int i : x) {
            if (i == n) {
                return true;
            }
        }
        return false;
    }

}
