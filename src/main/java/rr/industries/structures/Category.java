package rr.industries.structures;

/**
 * Represents the Categories of Outfits and Ships
 */
public enum Category {
    GUNS("Guns"), TURRETS("Turrets"), SECONDARY_WEAPONS("Secondary Weapons"), AMMUNITION("Ammunition"),
    SYSTEMS("Systems"), POWER("Power"), ENGINES("Engines"), HAND_TO_HAND("Hand to Hand"), SPECIAL("Special"),

    UNKNOWN("Unknown"),

    TRANSPORT("Transport"), LIGHT_FREIGHTER("Light Freighter"), HEAVY_FREIGHTER("Heavy Freighter"),
    INTERCEPTOR("Interceptor"), LIGHT_WARSHIP("Light Warship"), MEDIUM_WARSHIP("Medium Warship"),
    HEAVY_WARSHIP("Heavy Warship"), FIGHTER("Fighter"), DRONE("Drone");


    String token;

    Category(String token) {
        this.token = token;
    }

    /**
     * Parses a category
     *
     * @param token the DataNode token to parse
     * @return the category
     */
    public static Category parse(String token) {
        for (Category category : Category.values()) {
            if (category.token.equals(token))
                return category;
        }
        System.err.println("Could not parse Category \"" + token + "\"");
        return UNKNOWN;
    }

    public String toString() {
        return token;
    }
}
