package rr.industries.structures;

import rr.industries.parser.DataNode;

import java.util.ArrayList;
import java.util.List;

public class Licenses extends Structure {
    List<String> licenses = new ArrayList<>();

    void parse(DataNode node) {
        for (DataNode child : node.children) {
            if (child.tokens.size() > 0)
                licenses.add(child.tokens.get(0));
        }
    }

    void addAll(Licenses other) {
        licenses.addAll(other.licenses);
    }
}
