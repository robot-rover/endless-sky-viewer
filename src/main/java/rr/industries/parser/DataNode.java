package rr.industries.parser;

import java.util.ArrayList;
import java.util.List;

import static rr.industries.EndlessSkyViewer.LOG;

/**
 * A DataNode is a single line of a DataFile. It consists of one or more tokens,
 * which can be interpreted either as strings or as floating point values, and
 * it may also have "children," which may each in turn have their own children.
 * The tokens of a node are separated by white space, with quotation marks being
 * used to group multiple words into a single token. If the token text contains
 * quotation marks, it should be enclosed in backticks instead.
 */
public class DataNode {
    private DataNode parent;
    public List<DataNode> children;
    public List<String> tokens;

    /**
     * Constructs a blank DataNode
     */
    public DataNode() {
        children = new ArrayList<>();
        tokens = new ArrayList<>(4);
    }

    /**
     * Constructs a new DataNode with reference to its parent
     *
     * @param parent the parent of the new DataNode
     */
    @SuppressWarnings("IncompleteCopyConstructor")
    DataNode(DataNode parent) {
        this();
        this.parent = parent;
    }

    /**
     * Checks if a the token is a valid number
     *
     * @param index the index of the token to check
     * @return is the token at the index a valid number
     */
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

    private String printPreview(int indent) {
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < indent; i++)
            line.append(" ");
        line.append(toString()).append("\n");
        for (DataNode child : children) {
            line.append(child.printPreview(indent + 2));
        }
        return line.toString();
    }

    /**
     * Prints a preview of this node and all its children
     */
    public void printPrview() {
        LOG.info("\n" + printPreview(0));
    }

    private NodeStackElement printTrace() {
        NodeStackElement previous = null;
        if (parent != null)
            previous = parent.printTrace();
        if (tokens.isEmpty())
            return previous;
        return new NodeStackElement(toString(), previous, previous == null ? 0 : previous.indent + 2);
    }

    /**
     * Prints an error message as well as the stack trace of this node up to a root node
     *
     * @param message The message to log
     */
    public void printTrace(String message) {
        if (message == null) {
            message = "";
        }
        LOG.debug(message + "\n" + printTrace());
    }

    private class NodeStackElement {
        String element;
        NodeStackElement stackRef;
        int indent;

        public NodeStackElement(String element, NodeStackElement stackRef, int indent) {
            this.element = element;
            this.stackRef = stackRef;
            this.indent = indent;
        }

        @Override
        public String toString() {
            StringBuilder space = new StringBuilder();
            for (int i = 0; i < indent; i++) space.append(" ");
            return (stackRef == null ? "" : stackRef + "\n") + space.toString() + element;
        }
    }

    /**
     * Gets the numerical value of a token
     *
     * @param index the index of the token to convert
     * @return the numerical value
     * @throws NumberFormatException if the token isn't a valid number
     */
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

    /**
     * returns a token
     *
     * @param index the index of the token
     * @return the token
     */
    public String token(int index) {
        return tokens.get(index);
    }

    private static DataNode copy(DataNode other) {
        DataNode node = new DataNode(other.parent);
        node.tokens.addAll(other.tokens);
        for (DataNode child : other.children) {
            node.children.add(copy(child));
        }
        return node;
    }

    /**
     * Escapes a phrase so that it is valid to place inside a token
     *
     * @param word the phrase to escape
     * @return the phrase, correctly escaped
     */
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
