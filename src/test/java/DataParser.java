import rr.industries.parser.DataFile;
import rr.industries.parser.DataNode;
import rr.industries.structures.Outfit;
import rr.industries.structures.Ship;

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
        List<Outfit> outfits = new ArrayList<>();
        List<Ship> ships = new ArrayList<>();
        for (File file : dataFolder.listFiles()) {
            DataFile dataStruct = new DataFile(file);
            //dataStruct.root.printPreview();
            for (DataNode child : dataStruct.root.children) {
                if (child.tokens.size() >= 2 && child.token(0).equals("outfit")) {
                    outfits.add(new Outfit(child));
                } else if (child.tokens.size() == 2 && child.token(0).equals("ship")) { // ignore variants for now
                    ships.add(new Ship(child));
                }
            }
        }
        System.exit(0);
    }
}
