package rr.industries;

import javafx.scene.control.Button;
import rr.industries.structures.Ship;

import java.util.Map;
import java.util.function.Consumer;

public class ShipPreviewPane extends ItemPreviewPane {

    Consumer<ShipListItem> openVariants;
    Consumer<Ship> editShip;

    Button variantButton;

    public ShipPreviewPane(Consumer<ShipListItem> openVariants, Consumer<Ship> editShip) {
        super();
        this.openVariants = openVariants;
        this.editShip = editShip;

        if (openVariants != null || editShip != null) {
            variantButton = new Button("");
            variantButton.setVisible(false);
            root.getChildren().add(variantButton);
        }
    }

    @Override
    public void setItem(ListItem item) {
        super.setItem(item);

        ShipListItem ship = (ShipListItem) item;
        if (variantButton != null) {
            if (ship.getVariants().size() > 0 && openVariants != null) {
                variantButton.setOnAction(v -> openVariants.accept(ship));
                variantButton.setText("Open Variants");
            } else {
                variantButton.setOnAction(v -> editShip.accept(ship.getBaseShip()));
                variantButton.setText("Edit Ship");
            }
            variantButton.setVisible(true);
        }
    }

    public void redrawAttributes(Map<String, Double> attributes) {
        setAttributes(attributeList, attributes.entrySet().iterator(), true);
    }
}
