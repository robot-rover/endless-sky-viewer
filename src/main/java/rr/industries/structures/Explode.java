package rr.industries.structures;

import rr.industries.parser.DataNode;

import java.util.ArrayList;

public class Explode extends Structure {
    ArrayList<String> effects = new ArrayList<>();
    ArrayList<Double> numOfExplode = new ArrayList<>();
    String finalExplode;

    public Explode() {

    }

    void parse(DataNode node) {
        if (node.token(0).equals("explode") && node.tokens.size() >= 3) {
            effects.add(node.token(1));
            numOfExplode.add(node.value(2));
        } else if (node.token(0).equals("final explode") && node.tokens.size() >= 2) {
            finalExplode = node.token(1);
        }
    }
}
