package rr.industries.parser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Stack;

public class DataFile {
    public DataNode root;

    public DataFile(File file) throws IOException {
        root = new DataNode();
        String lines = new String(Files.readAllBytes(file.toPath()), StandardCharsets.US_ASCII);
        if (lines.length() == 0)
            return;
        load(lines);
        root.tokens.add("file");
        root.tokens.add(file.getAbsolutePath());
    }

    private void load(String lines) {
        char[] str = lines.toCharArray();
        Stack<DataNode> stack = new Stack<>();
        stack.push(root);
        Stack<Integer> whiteStack = new Stack<>();
        whiteStack.push(-1);
        for (int i = 0; i < lines.length(); i++) {
            int white = 0;
            for (; str[i] <= ' ' && str[i] != '\n'; i++)
                white++;
            if (str[i] == '#') {
                while (str[i] != '\n')
                    i++;
            }
            if (str[i] == '\n')
                continue;
            while (whiteStack.peek() >= white) {
                whiteStack.pop();
                stack.pop();
            }
            List<DataNode> children = stack.peek().children;
            DataNode node = new DataNode(stack.peek());
            children.add(node);
            stack.push(node);
            whiteStack.push(white);
            while (str[i] != '\n') {
                char endQuote = str[i];
                boolean isQuoted = endQuote == '\"' || endQuote == '`';
                if (isQuoted)
                    i++;
                int start = i;
                while (str[i] != '\n' && (isQuoted ? (str[i] != endQuote) : (str[i] != ' ')))
                    i++;
                node.tokens.add(new String(str, start, i - start));
                if (isQuoted && str[i] == '\n') {
                    node.printTrace("Closing quotation mark is missing: ");
                }

                if (str[i] != '\n') {
                    if (isQuoted)
                        i++;
                    while (str[i] != '\n' && str[i] <= ' ' && str[i] != '#')
                        i++;

                    if (str[i] == '#')
                        while (str[i] != '\n')
                            i++;
                }
            }
        }
    }
}
