package rr.industries.parser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Stack;

/**
 * A class which represents a hierarchical data file. Each line of the file that
 * is not empty or a comment is a "node," and the relationship between the nodes
 * is determined by indentation: if a node is more indented than the node before
 * it, it is a "child" of that node. Otherwise, it is a "sibling." Each node is
 * just a collection of one or more tokens that can be interpreted either as
 * strings or as floating point values; see DataNode for more information.
 */
public class DataFile {
    public DataNode root;

    /**
     * Parse a file into a structure of DataNodes
     *
     * @param file the file to parse
     * @throws IOException if an IO error occurs while reading or parsing
     */
    public DataFile(File file) throws IOException {
        root = new DataNode();
        String lines = new String(Files.readAllBytes(file.toPath()), StandardCharsets.US_ASCII);
        if (lines.length() == 0)
            return;

        // Ensure files end in newlines
        if (lines.charAt(lines.length() - 1) != '\n')
            lines = lines + "\n";
        load(lines);

        // Add file to node so it shows up in traces
        root.tokens.add("file");
        root.tokens.add(file.getAbsolutePath());
    }

    private void load(String lines) {
        char[] str = lines.toCharArray();

        /*  Keep track of the current stack of indentation levels and the most recent
            node at each level - that is, the node that will be the "parent" of any
            new node added at the next deeper indentation level. */
        Stack<DataNode> stack = new Stack<>();
        stack.push(root);
        Stack<Integer> whiteStack = new Stack<>();
        whiteStack.push(-1);

        for (int i = 0; i < lines.length(); i++) {

            // Find the first non-white character in this line.
            int white = 0;
            for (; str[i] <= ' ' && str[i] != '\n'; i++)
                white++;

            // If the line is a comment, skip to the end of the line.
            if (str[i] == '#') {
                while (str[i] != '\n')
                    i++;
            }

            // Skip empty lines (including comment lines).
            if (str[i] == '\n')
                continue;

            /*  Determine where in the node tree we are inserting this node, based on
                whether it has more indentation that the previous node, less, or the same. */
            while (whiteStack.peek() >= white) {
                whiteStack.pop();
                stack.pop();
            }

            // Add this node as a child of the proper node.
            List<DataNode> children = stack.peek().children;
            DataNode node = new DataNode(stack.peek());
            children.add(node);

            // Remember where in the tree we are.
            stack.push(node);
            whiteStack.push(white);

            // Tokenize the line. Skip comments and empty lines.
            while (str[i] != '\n') {

                /*  Check if this token begins with a quotation mark. If so, it will
                    include everything up to the next instance of that mark. */
                char endQuote = str[i];
                boolean isQuoted = endQuote == '\"' || endQuote == '`';
                if (isQuoted)
                    i++;

                int start = i;

                // Find the end of this token.
                while (str[i] != '\n' && (isQuoted ? (str[i] != endQuote) : (str[i] != ' ')))
                    i++;

                node.tokens.add(new String(str, start, i - start));
                // This is not a fatal error, but it may indicate a format mistake:
                if (isQuoted && str[i] == '\n') {
                    node.printTrace("Closing quotation mark is missing: ");
                }

                if (str[i] != '\n') {
                    /*  If we've not yet reached the end of the line of text, search
                        forward for the next non-whitespace character. */
                    if (isQuoted)
                        i++;
                    while (str[i] != '\n' && str[i] <= ' ' && str[i] != '#')
                        i++;

                    /*  If a comment is encountered outside of a token, skip the rest
                        of this line of the file. */
                    if (str[i] == '#')
                        while (str[i] != '\n')
                            i++;
                }
            }
        }
    }
}
