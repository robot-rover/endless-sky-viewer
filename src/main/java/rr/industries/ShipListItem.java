package rr.industries;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import rr.industries.structures.Ship;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Implementation of ListItem for representing Ships
 */
class ShipListItem extends ListItem {
    public Ship getBaseShip() {
        return baseShip;
    }

    public List<Ship> getVariants() {
        return variants;
    }

    private Ship baseShip;
    private List<Ship> variants;
    private boolean applyOutfits;
    Consumer<ShipListItem> clickedOnCallback;

    ShipListItem(Ship ship, boolean isVariant, boolean applyOutfits) {
        this.baseShip = ship;
        this.applyOutfits = applyOutfits;
        if (!isVariant)
            this.variants = GameData.getShipVariants(ship.getModelName());
        else
            this.variants = new ArrayList<>(0);
        setup();
    }

    public ShipListItem(Ship ship, boolean isVariant) {
        this(ship, isVariant, false);
    }

    public ShipListItem(Ship ship) {
        this(ship, false);
    }

    VBox getNode() {
        return itemBox;
    }

    public ImageView getGraphicView() {
        Image graphic = baseShip.getThumbnail();
        if (graphic == null)
            return getSpriteView();
        ImageView shipGraphic = new ImageView(graphic);
        shipGraphic.setPreserveRatio(true);
        return shipGraphic;
    }

    public ImageView getSpriteView() {
        ImageView shipGraphic = new ImageView(baseShip.getSprite());
        shipGraphic.setPreserveRatio(true);
        return shipGraphic;
    }

    static final Border border = new Border(new BorderStroke(Color.GREY,
            BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT));
    static final Border noBorder = new Border(new BorderStroke(Color.GREY,
            BorderStrokeStyle.NONE, new CornerRadii(5), BorderWidths.DEFAULT));

    public void setHighlight(boolean isHighlight) {

        if (isHighlight)
            itemBox.setBorder(border);
        else
            itemBox.setBorder(noBorder);
    }

    @Override
    boolean isRotate() {
        return baseShip.getThumbnail() == null;
    }

    @Override
    public Map<String, Double> getAttributes() {
        if (applyOutfits)
            return baseShip.getAppliedAttributes();
        else
            return baseShip.getAttributes();
    }

    @Override
    public String getName() {
        return baseShip.getName();
    }

    @Override
    public double getCost() {
        return baseShip.getCost();
    }

    @Override
    public String getDescription() {
        return baseShip.getDescription();
    }

    public void setCallback(Consumer<ShipListItem> clickedOnCallback) {
        itemBox.setOnMouseClicked(e -> clickedOnCallback.accept(this));
    }

}
