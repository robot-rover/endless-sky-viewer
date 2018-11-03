package rr.industries;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import rr.industries.structures.Category;
import rr.industries.structures.Outfit;
import rr.industries.structures.Ship;

/**
 * The root of the ship editor
 * This shows a list of outfits, the currently selected outfit,
 * as well as the manifest and stats of the current ship.
 */
public class OutfitEditorRoot extends HBox {

    OutfitListPane outfitListPane;
    ShipManifestPane shipManifestPane;
    ShipPreviewPane shipPane;
    OutfitPreviewPane outfitPreview;
    Ship ship;

    public OutfitEditorRoot(Ship ship) {
        this.ship = ship;

        outfitPreview = new OutfitPreviewPane(this::modifyOutfit);

        outfitListPane = new OutfitListPane(GameData.getOutfits(), this::selectOutfit);

        ListNavBar categoryNavBar = new ListNavBar(outfitListPane::setCategory) {{
            for (int i = 0; i < Category.UNKNOWN.ordinal(); i++) {
                this.createButton(Category.values()[i]);
            }
        }};
        this.getChildren().add(categoryNavBar);

        this.getChildren().add(outfitListPane);

        this.getChildren().add(outfitPreview);

        shipManifestPane = new ShipManifestPane(v -> selectOutfit(outfitListPane.getListItem(v)));
        this.getChildren().add(shipManifestPane);

        shipPane = new ShipPreviewPane(null, null);
        this.getChildren().add(shipPane);

        HBox.setHgrow(outfitListPane, Priority.ALWAYS);
        setShip(new ShipListItem(ship, false, true));
    }

    public void selectOutfit(OutfitListItem outfit) {
        outfitPreview.setItem(outfit);
        shipManifestPane.shadeItem(outfit.getOutfit());
    }

    public void setShip(ShipListItem ship) {
        shipManifestPane.setShip(ship.getBaseShip());
        shipPane.setItem(ship);
    }

    public void modifyOutfit(Outfit outfit, Integer change) {
        if (change == 0)
            return;
        int repititions = Math.abs(change);
        if (change < 0) {
            for (int i = 0; i < repititions; i++) {
                ship.getOutfits().remove(outfit);
            }
        } else {
            for (int i = 0; i < repititions; i++) {
                ship.getOutfits().add(outfit);
            }
        }

        shipPane.redrawAttributes(ship.getAppliedAttributes());
        shipManifestPane.setShip(ship);
    }
}
