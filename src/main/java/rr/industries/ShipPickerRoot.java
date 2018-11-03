package rr.industries;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import rr.industries.structures.Category;
import rr.industries.structures.Ship;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * The root of the Ship Picker
 * allows user to pick a ship out of a list of every ship
 * Shows statistics on the side
 */
public class ShipPickerRoot extends HBox {
    ShipListPane shipList;
    ShipPreviewPane shipPane;

    public ShipPickerRoot(Consumer<ShipListItem> openVariants, Consumer<Ship> editShip) {
        List<ShipListItem> shipListSource = new ArrayList<>();
        for (Ship ship : GameData.getShips()) {
            if (ship.getVariantName() != null || ship.getSprite() == null)
                continue;
            shipListSource.add(new ShipListItem(ship));
        }

        shipList = new ShipListPane(shipListSource);

        ListNavBar navBar = new ListNavBar(shipList::scrollToCategory) {{
            createButton(Category.TRANSPORT);
            createSpacer();
            createButton("Heavy", Category.HEAVY_FREIGHTER);
            createButton("Light", Category.LIGHT_FREIGHTER);
            createHeader("Freighter");
            createSpacer();
            createButton(Category.INTERCEPTOR);
            createSpacer();
            createButton("Heavy", Category.HEAVY_WARSHIP);
            createButton("Medium", Category.MEDIUM_WARSHIP);
            createButton("Light", Category.LIGHT_WARSHIP);
            createHeader("Warship");
            createSpacer();
            createButton(Category.FIGHTER);
            createButton(Category.DRONE);
        }};

        this.getChildren().add(navBar);

        this.getChildren().add(shipList);

        shipPane = new ShipPreviewPane(openVariants, editShip);
        shipList.setCallback(shipPane::setItem);
        this.getChildren().add(shipPane);

        HBox.setHgrow(shipList, Priority.ALWAYS);
    }
}
