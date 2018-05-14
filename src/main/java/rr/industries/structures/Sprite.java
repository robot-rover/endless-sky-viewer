package rr.industries.structures;

import rr.industries.parser.DataNode;
import rr.industries.parser.Tag;

import java.lang.reflect.Field;

public class Sprite extends Structure {

    String spritePath;
    @Tag("frame rate")
    double frameRate = 0;
    @Tag("no repeat")
    boolean noRepeat = false;
    @Tag("random start frame")
    boolean randomStartFrame = false;
    @Tag("delay")
    double delay = 0;

    public Sprite(DataNode node) {
        if (node.tokens.size() >= 2) {
            this.spritePath = node.token(1);
        } else {
            node.printTrace("Invalid Sprite Name");
        }

        Field[] fields = this.getClass().getDeclaredFields();
        NODE_LOOP:
        for (DataNode child : node.children) {
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                if (deserialize(field, this, child) == 0) {
                    continue NODE_LOOP;
                }
            }
            child.printTrace("Unrecognized Node:");
        }
    }

    @Override
    public String toString() {
        return spritePath;
    }
}
