package rr.industries.structures;

import rr.industries.Util;
import rr.industries.parser.ParseContext;
import rr.industries.parser.Parser;

import java.util.List;

public class Weapon implements ParseContext {

    public Weapon(Parser parser) {
        int[] level = new int[1];
        level[0] = 2;
        while (true) {
            List<String> words = parser.getNextWords(level);
            if (level[0] < 2)
                break;
            Util.tallyItem(words.get(0));
        }
        parser.backup();
    }
}
