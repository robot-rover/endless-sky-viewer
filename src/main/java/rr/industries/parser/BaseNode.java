package rr.industries.parser;

import rr.industries.structures.Outfit;

import java.util.List;
import java.util.Scanner;

public class BaseNode implements ParseContext {
    public BaseNode(Parser parser) {
        Scanner wait = new Scanner(System.in);
        int[] level = new int[1];
        while (parser.hasNextLine()) {
            List<String> words = parser.getNextWords(level);
            //System.out.println(level[0] + " - " + words.toString());
            if (level[0] != 0)
                throw new RuntimeException("Level for base node should always be 0: " + level[0] + " - " + words.toString());

            if (words.get(0).equals("outfit")) {
                Outfit out = new Outfit(words.get(1), parser);
                System.out.println("outfit " + out.toString());
                //wait.nextLine();
            } else {
                while (true) {
                    List<String> innerWords = parser.getNextWords(level);
                    if (level[0] < 1)
                        break;
                    //System.out.println(level[0] + " - " + innerWords.toString());
                }
                parser.backup();
            }
        }
    }
}
