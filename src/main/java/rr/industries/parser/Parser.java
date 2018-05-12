package rr.industries.parser;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    List<String> file;
    int nextLine;

    public boolean hasNextLine() {
        return nextLine < file.size();
    }

    public String getNextLine() {
        while (hasNextLine() && (file.get(nextLine).startsWith("#") || file.get(nextLine).replace("\t", "").length() == 0))
            nextLine++;
        if (hasNextLine()) {
            return file.get(nextLine++);
        } else {
            return "";
        }
    }

    public List<String> getNextWords(int[] level) {
        return toWords(getNextLine(), level);
    }

    public void backup() {
        if (hasNextLine())
            nextLine--;
    }

    public void setSource(List<String> newFile) {
        nextLine = 0;
        file = newFile;
    }

    public static List<String> toWords(String line, int[] level) {
        char[] chars = line.toCharArray();
        List<String> words = new ArrayList<>();
        int tabs = 0;
        while (tabs < chars.length && chars[tabs] == '\t')
            tabs++;
        level[0] = tabs;
        for (int i = tabs; i < chars.length; ) {
            StringBuilder word = new StringBuilder();
            boolean quoteMode = false;
            boolean backtickMode = false;
            if (chars[i] == '`') {
                backtickMode = true;
                i++;
            } else if (chars[i] == '\"') {
                quoteMode = true;
                i++;
            }
            for (; i < chars.length; i++) {
                if (backtickMode) {
                    if (chars[i] == '`') {
                        i += 2;
                        break;
                    }
                } else if (quoteMode) {
                    if (chars[i] == '\"') {
                        i += 2;
                        break;
                    }
                } else {
                    if (chars[i] == ' ') {
                        i++;
                        break;
                    }
                }
                word.append(chars[i]);
            }
            words.add(word.toString());
        }
        return words;
    }

    public static String escapeWord(String word) {
        if (word.contains("\""))
            return "`" + word + "`";
        else if (word.contains(" "))
            return "\"" + word + "\"";
        else return word;
    }
}
