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
    ListNavBar navBar;

    public ShipPickerRoot(Consumer<ShipListItem> openVariants, Consumer<Ship> editShip) {
        navBar = new ListNavBar(null) {{
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

        shipList = new ShipListPane();
        this.getChildren().add(shipList);

        shipPane = new ShipPreviewPane(openVariants, editShip);
        shipList.setCallback(shipPane::setItem);
        this.getChildren().add(shipPane);

        HBox.setHgrow(shipList, Priority.ALWAYS);
    }

    public void load(GameData gameData) {
        List<ShipListItem> shipListSource = new ArrayList<>();
        for (Ship ship : gameData.getShips()) {
            if (ship.getVariantName() != null || ship.getSprite() == null)
                continue;
            shipListSource.add(new ShipListItem(ship, gameData));
        }

        shipList.load(shipListSource);

        navBar.setScrollTargetCallback(shipList::scrollToCategory);
    }
}
