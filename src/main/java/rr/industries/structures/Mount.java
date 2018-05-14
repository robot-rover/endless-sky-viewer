package rr.industries.structures;

import rr.industries.parser.DataNode;

public class Mount extends Structure {
    Type type;
    double x;
    double y;
    String installed;

    public Mount(DataNode node, Type type) {
        if (node.tokens.size() < 3) {
            node.printTrace("Mount needs 3 tokens");
            return;
        }
        this.type = type;
        x = node.value(1);
        y = node.value(2);
        if (node.tokens.size() > 3)
            installed = node.token(3);
    }

    @Override
    public String toString() {
        return type.token + " (" + x + ", " + y + ") | " + installed;
    }

    enum Type {
        DRONE("drone"), FIGHTER("fighter"), ENGINE("engine"), GUN("gun"), TURRET("turret");

        String token;

        Type(String token) {
            this.token = token;
        }
    }
}
