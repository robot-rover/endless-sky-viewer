import rr.industries.parser.DataFile;
import rr.industries.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataParser {
    static List<String> topLevelLabels = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Please provide the path to the data folder");
            System.exit(1);
        }
        File dataFolder = new File(args[0]);
        if (!dataFolder.exists() || !dataFolder.isDirectory()) {
            System.out.println("Please provide a directory");
            System.exit(1);
        }
        for (File file : dataFolder.listFiles()) {
            DataFile dataStruct = new DataFile(file);
            dataStruct.root.printPreview();
            System.exit(0);
        }
    }

    public static void addLabel(String s) {
        int exists = topLevelLabels.indexOf(s);
        if (exists < 0) {
            topLevelLabels.add(s);
        }

    }

    static void testParser(String line) {
        int[] level = new int[1];
        List<String> words = Parser.toWords(line, level);
        if (level[0] != 0 || words.size() == 2)
            return;
        System.out.println(words.size() + " - " + words.toString());
    }
}
