package rr.industries.structures;

import rr.industries.GameData;
import rr.industries.parser.DataNode;

/**
 * When Loading ships, some outfits aren't loaded yet.
 * If that's the case, use one of these. They are replaced
 * with actual outfits during Ship#finalLoad()
 */
public class OutfitPlaceholder extends Outfit {
    private final String pointerName;

    public OutfitPlaceholder(String pointerName, GameData gameData) {
        super(new DataNode(), gameData);
        this.pointerName = pointerName;
    }

    public String getPointerName() {
        return pointerName;
    }
}
