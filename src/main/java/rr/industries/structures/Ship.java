package rr.industries.structures;

import rr.industries.parser.DataNode;
import rr.industries.parser.Tag;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Ship extends Structure {

    String name;

    List<Mount> mounts = new ArrayList<>();
    Explode explode = new Explode();
    @Tag("sprite")
    String sprite;
    @Tag("attributes")
    Attributes attributes;
    @Tag("outfits")
    Outfits outfits = new Outfits();
    @Tag("final explode")
    String finalExplode;
    @Tag("description")
    String description;
    @Tag("plural")
    String plural;
    @Tag("never disabled")
    boolean neverDisabled;
    @Tag("swizzle")
    double swizzle;
    @Tag("noun")
    String noun;

    public Ship(DataNode node) {
        if (node.tokens.size() >= 2) {
            this.name = node.token(1);
        } else {
            node.printTrace("Invalid Ship Name");
        }

        Field[] fields = this.getClass().getDeclaredFields();
        NODE_LOOP:
        for (DataNode child : node.children) {
            if (child.tokens.size() > 0 && child.token(0).equals("attributes")) {
                attributes = new Attributes();
                Field[] attributeFields = Attributes.class.getDeclaredFields();
                ATTRIBUTES_NODE_LOOP:
                for (DataNode grand : child.children) {
                    for (int i = 0; i < attributeFields.length; i++) {
                        if (deserialize(attributeFields[i], attributes, grand) == 0)
                            continue ATTRIBUTES_NODE_LOOP;
                    }
                }
            } else if (child.tokens.size() >= 3 && child.token(0).equals("engine")) {
                mounts.add(new Mount(child, Mount.Type.ENGINE));
            } else if (child.tokens.size() >= 3 && child.token(0).equals("gun")) {
                mounts.add(new Mount(child, Mount.Type.GUN));
            } else if (child.tokens.size() >= 3 && child.token(0).equals("turret")) {
                mounts.add(new Mount(child, Mount.Type.TURRET));
            } else if (child.tokens.size() >= 3 && child.token(0).equals("drone")) {
                mounts.add(new Mount(child, Mount.Type.DRONE));
            } else if (child.tokens.size() >= 3 && child.token(0).equals("fighter")) {
                mounts.add(new Mount(child, Mount.Type.FIGHTER));
            } else if (child.tokens.size() >= 2 && child.token(0).equals("final explode")) {
                explode.parse(child);
            } else if (child.tokens.size() >= 3 && child.token(0).equals("explode")) {
                explode.parse(child);
            } else if (child.tokens.size() >= 1 && child.token(0).equals("outfits")) {
                outfits.parse(child);
            } else {
                for (int i = 0; i < fields.length; i++) {
                    if (deserialize(fields[i], this, child) == 0) {
                        continue NODE_LOOP;
                    }
                }
                child.printTrace("Unrecognized Node:");
            }
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
