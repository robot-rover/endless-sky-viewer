package rr.industries;

import javafx.scene.Node;

import java.text.DecimalFormat;
import java.util.Map;

public final class Util {
    private Util() {
    }

    /**
     * Adds a border to a JavaFX Node
     * (Used for Debugging)
     *
     * @param toBorder the node
     */
    public static void addBorder(Node toBorder) {
        toBorder.setStyle("-fx-border-color: black");
    }

    /**
     * Capitalize a string
     * (Used for formatting attributes)
     *
     * @param str phrase to capitalize
     * @return the phrase, capitalized
     */
    public static String capitalize(final String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        final char[] buffer = str.toCharArray();
        boolean capitalizeNext = true;
        for (int i = 0; i < buffer.length; i++) {
            final char ch = buffer[i];
            if (ch == ' ') {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                buffer[i] = Character.toTitleCase(ch);
                capitalizeNext = false;
            }
        }
        return new String(buffer);
    }


    final static DecimalFormat lessThanMil = new DecimalFormat("###,###");
    final static DecimalFormat moreThanMil = new DecimalFormat("##0.000");

    /**
     * Represent currency values like Endless Sky does
     *
     * @param amount amount of currency
     * @return formatted as a String
     */
    public static String compressCurrency(double amount) {
        double placeValue = amount;
        if (placeValue < 1_000_000) {
            return lessThanMil.format(amount);
        }
        String[] postfix = {"M", "B", "T"};
        for (int i = 0; i < 3; i++) {
            placeValue /= 1_000_000;
            if (placeValue < 1_000_000)
                return moreThanMil.format(placeValue) + postfix[i];
        }
        return moreThanMil.format(placeValue) + postfix[2];
    }

    public static class Entry<K, V> implements Map.Entry<K, V> {
        K key;
        V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V temp = this.value;
            this.value = value;
            return temp;
        }
    }
}
