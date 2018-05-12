package rr.industries;

import java.util.ArrayList;
import java.util.List;

public class Util {
    static List<String> tally = new ArrayList<>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (tally.size() != 0)
                System.out.println(tally);
        }));
    }

    public static boolean tallyItem(String item) {
        if (tally.indexOf(item) == -1) {
            tally.add(item);
            return true;
        }
        return false;
    }
}
