package rr.industries.structures;

import javafx.scene.image.Image;
import rr.industries.GameData;
import rr.industries.parser.DataNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class representing an outfit that can be installed in a ship. A ship's
 * "attributes" are simply stored as a series of key-value pairs, and an outfit
 * can add to or subtract from any of those values. Weapons also have another
 * set of attributes unique to them, and outfits can also specify additional
 * information like the sprite to use in the outfitter panel for selling them,
 * or the sprite or sound to be used for an engine flare.
 */
public class Outfit {
    private String name;
    private double cost;
    private double mass;
    private String ammo;
    private Image thumbnail;
    private String description;
    private String plural;
    private double map;
    private String afterburnerEffect;
    private String flotsamSprite;
    private Weapon weapon;
    private List<String> licenses = new ArrayList<>();
    private Category category;
    private Map<String, Double> attributes = new HashMap<>();

    private GameData gameData;

    /**
     * Creates a new outfit
     *
     * @param node the source DataNode
     */
    public Outfit(DataNode node, GameData gameData) {
        this.gameData = gameData;
        if (node.tokens.size() >= 2) {
            name = node.token(1);
            plural = name + 's';
        }

        for (DataNode child : node.children) {

            String key = child.token(0);
            int size = child.tokens.size();

            if (key.equals("category") && size >= 2) {
                category = Category.parse(child.token(1));
            } else if (key.equals("plural") && size >= 2) {
                plural = child.token(1);
            } else if (key.equals("flare sprite") && size >= 2) {
                //todo: sprites
            } else if (key.equals("flare sound") && size >= 2) {
                //todo: sounds
            } else if (key.equals("afterburner effect") && size >= 2) {
                //todo: effects
            } else if (key.equals("flotsam sprite") && size >= 2) {
                //todo: sprites
            } else if (key.equals("thumbnail") && size >= 2) {
                loadThumbnail(child);
            } else if (key.equals("weapon")) {
                weapon = new Weapon(child);
            } else if (key.equals("ammo") && size >= 2) {
                //todo: ammo
                // ammo = GameData::Outfits ().Get(child.token(1));
            } else if (key.equals("description") && size >= 2) {
                if (description == null)
                    description = child.token(1);
                else
                    description += child.token(1) + "\n";
            } else if (key.equals("cost") && size >= 2) {
                cost = child.value(1);
            } else if (key.equals("mass") && size >= 2) {
                mass = child.value(1);
            } else if (key.equals("licenses")) {
                for (DataNode grand : child.children)
                    licenses.add(grand.token(0));
            } else if (size >= 2) {
                attributes.put(key, child.value(1));
            } else {
                child.printTrace("Skipping unrecognized attribute:");
            }
        }

    }

    /**
     * Copy Constructor
     *
     * @param other Outfit to Copy
     */
    public Outfit(Outfit other) {
        this.name = other.name;
        this.cost = other.cost;
        this.mass = other.mass;
        this.ammo = other.ammo;
        this.thumbnail = other.thumbnail;
        this.description = other.description;
        this.plural = other.plural;
        this.map = other.map;
        this.afterburnerEffect = other.afterburnerEffect;
        this.flotsamSprite = other.flotsamSprite;
        this.weapon = other.weapon;
        this.licenses = other.licenses;
        this.category = other.category;
        //noinspection IncompleteCopyConstructor
        this.attributes = new HashMap<>();
        this.attributes.putAll(other.attributes);
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

    public Image getThumbnail() {
        return thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public Map<String, Double> getAttributes() {
        return attributes;
    }

    private void loadThumbnail(DataNode node) {
        if (node.tokens.size() < 2)
            return;
        thumbnail = gameData.getSprite(node.token(1));
    }

    /**
     * Combines the attributes of 2 outfits
     * Usually used to combine outfits into ships
     *
     * @param other outfit to add into this one
     */
    public void add(Outfit other) {
        add(other, 1);
    }

    /**
     * Combines the attributes of 2 outfits
     * Usually used to combine outfits into ships
     *
     * @param other outfit to add into this one
     * @param count how many to add
     */
    public void add(Outfit other, int count) {
        cost += other.cost * count;
        mass += other.mass * count;
        for (Map.Entry<String, Double> entry : other.attributes.entrySet()) {
            attributes.merge(entry.getKey(), entry.getValue(), (a, b) -> b + a);
        }
        //todo: flare sprites
        //todo: flare sounds
        //todo: afterburner effects
    }

    @Override
    public String toString() {
        return "outfit " + (name == null ? "" : DataNode.escapeWord(name));
    }

}