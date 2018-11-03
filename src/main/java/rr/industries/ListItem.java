package rr.industries;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.Map;

/**
 * Generic object for use with ItemPreviewPane
 */
public abstract class ListItem {

    protected VBox itemBox;

    protected void setup() {
        itemBox = new VBox();
        itemBox.setPrefSize(120, 120);
        itemBox.setAlignment(Pos.CENTER);
        itemBox.setSpacing(5);
        itemBox.setPadding(new Insets(5));
        ImageView graphic = getSpriteView();
        graphic.setFitWidth(100);
        graphic.setFitHeight(100);
        graphic.setPreserveRatio(true);
        StackPane graphicContainer = new StackPane(graphic);
        graphicContainer.setAlignment(Pos.CENTER);
        itemBox.getChildren().add(graphicContainer);
        Label nameLabel = new Label(getName());
        nameLabel.setAlignment(Pos.BOTTOM_CENTER);
        //shipLabel.setStyle("-fx-border-color: black");
        itemBox.getChildren().add(nameLabel);
        VBox.setVgrow(graphicContainer, Priority.ALWAYS);
        setHighlight(false);
    }

    /**
     * Should be a bigger view
     *
     * @return a bigger view of the object
     */
    abstract ImageView getGraphicView();

    /**
     * Should be a smaller view
     * usually for icons
     *
     * @return a smaller view of the object
     */
    abstract ImageView getSpriteView();

    abstract void setHighlight(boolean isHighlight);

    /**
     * Should the Displaying PreviewPane rotate the result of #getGraphicView()
     *
     * @return true if it should be rotated
     */
    abstract boolean isRotate();

    abstract Map<String, Double> getAttributes();

    abstract String getName();

    abstract double getCost();

    abstract String getDescription();
}
