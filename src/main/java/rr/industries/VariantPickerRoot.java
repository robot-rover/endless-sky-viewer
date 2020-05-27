package rr.industries;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import rr.industries.structures.Ship;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * The root of the Variant Picker
 * allows user to pick a ship out of a list of every variant of a base ship
 * Shows statistics on the side
 * Shows outfits on the side
 */
public class VariantPickerRoot extends HBox {
    ShipListPane variantList;
    ShipPreviewPane variantPane;
    ShipManifestPane outfitListPane;

    public VariantPickerRoot(ShipListItem shipItem, Consumer<Ship> editShip, GameData gameData) {
        List<ShipListItem> variantsListSource = new ArrayList<>();
        variantsListSource.add(new ShipListItem(shipItem.getBaseShip(), gameData));

        for (Ship ship : shipItem.getVariants()) {
            variantsListSource.add(new ShipListItem(ship, gameData, true));
        }

        variantList = new ShipListPane();
        variantList.load(variantsListSource);

        this.getChildren().add(variantList);

        outfitListPane = new ShipManifestPane();
        this.getChildren().add(outfitListPane);

        variantPane = new ShipPreviewPane(null, editShip);
        variantList.setCallback(this::setShip);
        this.getChildren().add(variantPane);

        HBox.setHgrow(variantList, Priority.ALWAYS);
    }

    private void setShip(ShipListItem shipItem) {
        variantPane.setItem(shipItem);
        outfitListPane.setShip(shipItem.getBaseShip());
    }
}
