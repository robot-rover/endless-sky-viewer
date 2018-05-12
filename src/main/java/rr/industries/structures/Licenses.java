package rr.industries.structures;

import rr.industries.parser.ParseContext;
import rr.industries.parser.Parser;

import java.util.ArrayList;
import java.util.List;

public class Licenses implements ParseContext {
    List<String> requiredLicenses = new ArrayList<>();

    public Licenses(Parser parser) {
        int[] level = new int[1];
        level[0] = 2;
        while (true) {
            List<String> words = parser.getNextWords(level);
            if (level[0] < 2)
                break;
            requiredLicenses.add(words.get(0));
        }
        parser.backup();
    }

    public String toString() {
        StringBuilder string = new StringBuilder();
        requiredLicenses.stream().forEach(s -> string.append("\n\t\t").append(Parser.escapeWord(s)));
        return string.toString();
    }
}
