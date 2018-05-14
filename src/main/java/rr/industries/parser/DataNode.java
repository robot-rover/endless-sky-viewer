package rr.industries.parser;

import java.util.ArrayList;
import java.util.List;

public class DataNode {
    DataNode parent;
    public List<DataNode> children;
    public List<String> tokens;

    public DataNode() {
        children = new ArrayList<>();
        tokens = new ArrayList<>(4);
    }

    public DataNode(DataNode parent) {
        this();
        this.parent = parent;
    }

    public boolean isNumber(int index) {
        // Make sure this token exists and is not empty.
        if (index >= tokens.size() || tokens.get(index) == null)
            return false;

        boolean hasDecimalPoint = false;
        boolean hasExponent = false;
        boolean isLeading = true;
        for (char it : tokens.get(index).toCharArray()) {
            // If this is the start of the number or the exponent, it is allowed to
            // be a '-' or '+' sign.
            if (isLeading) {
                isLeading = false;
                if (it == '-' || it == '+')
                    continue;
            }
            // If this is a decimal, it may or may not be allowed.
            if (it == '.') {
                if (hasDecimalPoint || hasExponent)
                    return false;
                hasDecimalPoint = true;
            } else if (it == 'e' || it == 'E') {
                if (hasExponent)
                    return false;
                hasExponent = true;
                // At the start of an exponent, a '-' or '+' is allowed.
                isLeading = true;
            } else if (it < '0' || it > '9')
                return false;
        }
        return true;
    }

    public void printPreview() {
        printPreview(0);
    }

    void printPreview(int indent) {
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < indent; i++)
            line.append(" ");
        line.append(toString());
        System.err.println(line);
        for (DataNode child : children) {
            child.printPreview(indent + 2);
        }
    }

    int printTrace() {
        int indent = 0;
        if (parent != null)
            indent = parent.printTrace() + 2;
        if (tokens.isEmpty())
            return indent;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < indent; i++)
            builder.append(" ");
        builder.append(toString());
        System.err.println(builder);
        return indent;
    }

    public int printTrace(String message) {
        if (message.length() > 0) {
            System.err.println();
            System.err.println(message);
        }

        return printTrace();
    }

    public double value(int index) throws NumberFormatException {
        // Check for empty strings and out-of-bounds indices.
        int i = 0;
        char[] str = tokens.get(index).toCharArray();
        if (index >= tokens.size() || tokens.get(index) == null) {
            System.err.println("Requested token index (" + index + ") is out of bounds:");
            return 0.;
        }

        // Allowed format: "[+-]?[0-9]*[.]?[0-9]*([eE][+-]?[0-9]*)?".
        if (i < str.length && (str[i] != '-' && str[i] != '.' && str[i] != '+' && !(str[i] >= '0' && str[i] <= '9'))) {
            System.err.println("Cannot convert value \"" + tokens.get(index) + "\" to a number:");
            return 0.;
        }

        // Check for leading sign.
        double sign = (str[i] == '-') ? -1. : 1.;
        if (str[i] == '-' || str[i] == '+')
            i++;

        // Digits before the decimal point.
        long value = 0;
        while (i < str.length && str[i] >= '0' && str[i] <= '9') {
            value = (value * 10) + (str[i] - '0');
            i++;
        }


        // Digits after the decimal point (if any).
        long power = 0;
        if (i < str.length) {
            if (str[i] == '.') {
                i++;
                while (i < str.length && str[i] >= '0' && str[i] <= '9') {
                    value = (value * 10) + (str[i] - '0');
                    i++;
                    --power;
                }
            }
        }

        // Exponent.
        if (i < str.length && (str[i] == 'e' || str[i] == 'E')) {
            i++;
            long exSign = 1;
            if (i < str.length) {
                exSign = (str[i] == '-') ? -1 : 1;
                if (str[i] == '-' || str[i] == '+')
                    i++;
            }

            long exponent = 0;
            while (i < str.length && str[i] >= '0' && str[i] <= '9') {
                exponent = (exponent * 10) + (str[i]++ - '0');
                i++;
            }

            power += sign * exponent;
        }

        // Compose the return value.
        return Math.copySign(value * Math.pow(10., power), sign);
    }

    public String token(int index) {
        return tokens.get(index);
    }

    static DataNode copy(DataNode other) {
        DataNode node = new DataNode(other.parent);
        node.tokens.addAll(other.tokens);
        for (DataNode child : other.children) {
            node.children.add(copy(child));
        }
        return node;
    }

    public static String escapeWord(String word) {
        if (word.contains("\""))
            return "`" + word + "`";
        else if (word.contains(" "))
            return "\"" + word + "\"";
        else return word;
    }

    @Override
    public String toString() {
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < tokens.size(); i++) {
            if (i > 0)
                line.append(" ");
            line.append(escapeWord(tokens.get(i)));
        }
        return line.toString();
    }
}
