package rr.industries;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import rr.industries.structures.Outfit;

import java.util.Map;
import java.util.function.Consumer;

/**
 * Implementation of ListItem for displaying Outfits
 */
public class OutfitListItem extends ListItem {

    public Outfit getOutfit() {
        return outfit;
    }

    public Map<String, Double> getAttributes() {
        return outfit.getAttributes();
    }

    public String getName() {
        return outfit.getName();
    }

    public double getCost() {
        return outfit.getCost();
    }

    public String getDescription() {
        return outfit.getDescription();
    }

    private Outfit outfit;
    private Image outfitThumbnail;
    Consumer<OutfitListItem> clickedOnCallback;

    public OutfitListItem(Outfit outfit) {
        this.outfit = outfit;
        outfitThumbnail = outfit.getThumbnail();
        setup();
    }

    VBox getNode() {
        return itemBox;
    }

    public ImageView getGraphicView() {
        ImageView shipGraphic = new ImageView(outfitThumbnail);
        shipGraphic.setPreserveRatio(true);
        return shipGraphic;
    }

    @Override
    ImageView getSpriteView() {
        return getGraphicView();
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
        return false;
    }

    public void setCallback(Consumer<OutfitListItem> clickedOnCallback) {
        itemBox.setOnMouseClicked(e -> clickedOnCallback.accept(this));
    }
}
