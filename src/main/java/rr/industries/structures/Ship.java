package rr.industries.structures;

import javafx.scene.image.Image;
import rr.industries.GameData;
import rr.industries.parser.DataNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static rr.industries.EndlessSkyViewer.LOG;

/**
 * Class representing a ship (either a model for sale or an instance of it). A
 * ship's information can be saved to a file, so that it can later be read back
 * in exactly the same state. The same class is used for the player's ship as
 * for all other ships, so their capabilities are exactly the same  within the
 * limits of what the AI knows how to command them to do.
 */
public class Ship {

    private String modelName;
    private String pluralModelName;
    private Image sprite;
    private Image thumbnail;
    private String variantName;
    private Outfit baseAttributes;
    private Outfit addAttributes;
    private List<Outfit> outfits;
    private String description;
    private boolean neverDisabled;
    private boolean uncapturable;
    private String noun;

    private double gunPorts;
    private double turretPorts;
    private boolean redoHardpoints;
    private double droneBays;
    private double fighterBays;
    private boolean redoBays;

    /**
     * Creates a new ship
     *
     * @param node the source DataNode
     */
    public Ship(DataNode node) {
        if (node.tokens.size() >= 2) {
            modelName = node.token(1);
            pluralModelName = modelName + 's';
        }
        if (node.tokens.size() >= 3) {
            variantName = node.token(2);
        }

        //todo: use player files
        // government = GameData::PlayerGovernment();
        redoHardpoints = false;
        gunPorts = 0;
        turretPorts = 0;

        for (DataNode child : node.children) {
            String key = child.token(0);
            int size = child.tokens.size();
            boolean add = (key.equals("add"));
            if (add && (size < 2 || !child.token(1).equals("attributes"))) {
                child.printTrace("Skipping invalid use of 'add' with " + (size < 2
                        ? "no key." : "key: " + child.token(1)));
                continue;
            }
            if (key.equals("sprite")) {
                //todo: sprite loading
                loadSprite(child);
            } else if (child.token(0).equals("thumbnail") && child.tokens.size() >= 2) {
                loadThumbnail(child);
            } else if (key.equals("plural") && size >= 2) {
                pluralModelName = child.token(1);
            } else if (key.equals("noun") && size >= 2) {
                noun = child.token(1);
            } else if (key.equals("swizzle") && size >= 2) {
                //todo: swizzles
            } else if (key.equals("attributes") || add) {
                if (!add)
                    baseAttributes = new Outfit(child);
                else
                    addAttributes = new Outfit(child);
            } else if (key.equals("engine") && size >= 3) {
                //todo: engine hardpoint loading
            } else if (key.equals("gun") || key.equals("turret")) {
                redoHardpoints = true;
                if (key.equals("gun"))
                    gunPorts++;
                else
                    turretPorts++;
            } else if (key.equals("never disabled")) {
                neverDisabled = true;
            } else if (key.equals("uncapturable")) {
                uncapturable = true;
            } else if ((key.equals("fighter") || key.equals("drone")) && size >= 3) {
                redoBays = true;
                if (key.equals("fighter"))
                    fighterBays++;
                else
                    droneBays++;
            } else if (key.equals("explode") && size >= 2) {
                //todo: animations
            } else if (key.equals("final explode") && size >= 2) {
                //todo: animations
            } else if (key.equals("outfits")) {
                outfits = new ArrayList<>();
                for (DataNode grand : child.children) {
                    int count = (grand.tokens.size() >= 2) ? (int) grand.value(1) : 1;
                    Outfit outfit = GameData.getOutfitOrPointer(grand.token(0));
                    for (int i = 0; i < count; i++) {
                        outfits.add(outfit);
                    }
                }
            }
            //todo: save file only (Ship attributes in Attributes node)
            else if (key.equals("cargo")) {
                //todo: cargo
            } else if (key.equals("crew") && size >= 2) {
                //crew = (int) child.value(1);
            } else if (key.equals("fuel") && size >= 2) {
                //fuel = child.value(1);
            } else if (key.equals("shields") && size >= 2) {
                //shields = child.value(1);
            } else if (key.equals("hull") && size >= 2) {
                //hull = child.value(1);
            } else if (key.equals("position") && size >= 3) {
                //todo: save files
            } else if (key.equals("system") && size >= 2) {
                //todo: save files
            } else if (key.equals("planet") && size >= 2) {
                //todo: save files
            } else if (key.equals("destination system") && size >= 2) {
                //todo: save files
            } else if (key.equals("parked")) {
                //todo: save files
            } else if (key.equals("description") && size >= 2) {
                if (description == null)
                    description = child.token(1);
                else
                    description += child.token(1) + "\n";
            } else if (!key.equals("actions"))
                child.printTrace("Skipping unrecognized attribute:");
        }
    }

    /**
     * Gets the Shipyard Icon for this ship
     *
     * @return Image
     */
    public Image getThumbnail() {
        return thumbnail;
    }

    public double getCost() {
        return baseAttributes.getCost();
    }

    public String getName() {
        return variantName == null ? modelName : variantName;
    }

    public String getModelName() {
        return modelName;
    }

    public String getVariantName() {
        return variantName;
    }

    /**
     * Get the base attributes of the ship
     *
     * @return Map of attributes
     */
    public Map<String, Double> getAttributes() {
        return baseAttributes.getAttributes();
    }

    /**
     * Applies attributes of outfits to base ship attributes
     * then returns the total
     *
     * @return Map of attributes
     */
    public Map<String, Double> getAppliedAttributes() {
        applyOutfits();
        return addAttributes.getAttributes();
    }

    public Category getCategory() {
        return baseAttributes.getCategory();
    }

    public List<Outfit> getOutfits() {
        return outfits;
    }

    public String getDescription() {
        return description;
    }

    /**
     * This loads relationships that requires all data to be loaded
     * Resolves outfit placeholders
     * Loads data from base ships to variants
     */
    public void finalLoad() {
        // load data from base ship
        if (variantName != null) {
            Ship base = GameData.getShipBase(modelName);
            if (base != null) {
                if (sprite == null) {
                    sprite = base.sprite;
                }
                if (baseAttributes == null)
                    baseAttributes = new Outfit(base.baseAttributes);
                if (addAttributes != null)
                    baseAttributes.add(addAttributes);
                if (outfits == null) {
                    outfits = new ArrayList<>();
                    outfits.addAll(base.outfits);
                }
                if (description == null) {
                    description = base.description;
                }
                //todo: animations, etc
            } else {
                System.err.println("Unable to load base ship \"" + this.getVariantName() + "\"");
            }
        }
        if (outfits == null)
            outfits = new ArrayList<>();

        if (redoHardpoints) {
            if (turretPorts > 0)
                baseAttributes.getAttributes().put("turret mounts", turretPorts);
            if (gunPorts > 0)
                baseAttributes.getAttributes().put("gun ports", gunPorts);
        }

        if (redoBays) {
            if (droneBays > 0)
                baseAttributes.getAttributes().put("drone bays", droneBays);
            if (fighterBays > 0)
                baseAttributes.getAttributes().put("fighter bays", fighterBays);
        }

        for (int i = 0; i < outfits.size(); i++) {
            Outfit outfit = outfits.get(i);
            if (outfit.getClass().equals(OutfitPlaceholder.class)) {
                OutfitPlaceholder placeholder = (OutfitPlaceholder) outfit;
                Outfit realOutfit = GameData.getOutfit(placeholder.getPointerName());
                if (realOutfit == null) {
                    LOG.warn("Unable to load outfit \"{}\" for ship {}", placeholder.getPointerName(), this.modelName);
                } else {
                    outfits.set(i, realOutfit);
                }
            }
        }
    }

    private void applyOutfits() {
        addAttributes = new Outfit(baseAttributes);
        for (Outfit outfit : outfits) {
            addAttributes.add(outfit);
        }
    }

    private void loadSprite(DataNode node) {
        if (node.tokens.size() < 2)
            return;
        sprite = GameData.getSprite(node.token(1));
    }

    private void loadThumbnail(DataNode node) {
        if (node.tokens.size() < 2) {
            return;
        }
        thumbnail = GameData.getSprite(node.token(1));
    }

    @Override
    public String toString() {
        return variantName == null ? modelName : variantName;
    }

    /**
     * Gets the world view sprite for a ship
     *
     * @return the image
     */
    public Image getSprite() {
        return sprite;
    }
}
