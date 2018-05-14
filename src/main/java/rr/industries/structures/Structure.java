package rr.industries.structures;

import rr.industries.parser.DataNode;
import rr.industries.parser.Tag;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Structure {
    public static List<String> tally = new ArrayList<>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (tally.size() != 0)
                System.out.println(tally + " - " + tally.size());
        }));
    }

    public static boolean tallyItem(String item) {
        if (tally.indexOf(item) == -1) {
            tally.add(item);
            return true;
        }
        return false;
    }

    /**
     * Deserializes a node if it matches the field
     *
     * @param field  The field to put data into
     * @param object The object the field belongs to
     * @param child  The DataNode to take data from
     * @return 0 if successful, 1 if Unrecognized Node Type, 2 if Unrecognized Node
     */
    public static int deserialize(Field field, Object object, DataNode child) {
        Tag tag = field.getDeclaredAnnotation(Tag.class);
        if (tag != null && tag.value().equals(child.token(0))) {
            try {
                if (field.getType().equals(double.class) && child.tokens.size() >= 2 && child.isNumber(1)) {
                    field.setDouble(object, child.value(1));

                } else if (field.getType().equals(String.class) && child.tokens.size() > -2) {
                    field.set(object, child.tokens.get(1));
                } else if (field.getType().equals(Weapon.class)) {
                    field.set(object, new Weapon(child));
                } else if (field.getType().equals(boolean.class)) {
                    field.set(object, true);
                } else if (field.getType().equals(Sprite.class)) {
                    field.set(object, new Sprite(child));
                } else if (field.getType().equals(Licenses.class)) {
                    ((Licenses) field.get(object)).parse(child);
                } else {
                    child.printTrace("Unrecognized Node Type:");
                    return 1;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return 0;
        }

        return 2;
    }
}
