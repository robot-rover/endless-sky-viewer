package rr.industries.structures;

import rr.industries.parser.DataNode;

import java.util.ArrayList;
import java.util.List;

public class Outfits extends Structure {
    List<String> outfits = new ArrayList<>();

    void parse(DataNode node) {
        for (DataNode child : node.children) {
            outfits.add(child.token(0));
        }
    }
}
