package rr.industries;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;

/**
 * Generic way to display an object that has a:
 * Title, Image, Cost, Description, and a set of attributes
 */
public abstract class ItemPreviewPane extends ScrollPane {

    VBox root;
    Label name;
    Label description;
    Label cost;
    Label image;
    VBox attributeList;

    static final double paneWidth = 400;
    static final double padding = 10;
    static final double imageDim = paneWidth - (padding * 2) - 20;

    public ItemPreviewPane() {
        setMinWidth(paneWidth);
        setFitToWidth(true);
        setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        root = new VBox();
        root.setSpacing(padding);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(padding));
        root.setPrefWidth(padding);
        root.setFillWidth(true);
        this.setContent(root);

        name = new Label();
        name.setFont(Font.font(22));
        StackPane shipNameHolder = new StackPane(name);
        shipNameHolder.setAlignment(Pos.BASELINE_LEFT);
        root.getChildren().add(shipNameHolder);

        image = new Label();
        image.setRotate(0);
        //shipImage.setStyle("-fx-border-color: black");
        root.getChildren().add(image);

        cost = new Label();
        cost.setFont(Font.font(18));
        cost.setAlignment(Pos.BASELINE_RIGHT);
        StackPane shipCostHolder = new StackPane(cost);
        shipCostHolder.setAlignment(Pos.BASELINE_RIGHT);
        root.getChildren().add(shipCostHolder);

        description = new Label();
        description.setWrapText(true);
        root.getChildren().add(description);

        attributeList = new VBox();
        attributeList.setFillWidth(true);
        attributeList.setPadding(new Insets(10, 20, 10, 20));
        root.getChildren().add(attributeList);
    }

    /**
     * Changes the currently displayed object
     *
     * @param item new object to display
     */
    public void setItem(ListItem item) {
        ImageView orig = item.getGraphicView();
        ImageView rotated;
        if (item.isRotate()) {
            orig.setRotate(270);
            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT);
            Image rotatedImage = orig.snapshot(params, null);
            rotated = new ImageView(rotatedImage);
        } else {
            rotated = orig;
        }
        rotated.setPreserveRatio(true);
        if (rotated.getImage() != null) {
            if (rotated.getImage().getHeight() > 200)
                rotated.setFitHeight(200);
            if (rotated.getImage().getWidth() > imageDim)
                rotated.setFitWidth(imageDim);
        }

        StackPane spaceHolder = new StackPane(rotated);
        spaceHolder.setAlignment(Pos.CENTER);
        spaceHolder.setPrefHeight(200);
        spaceHolder.setPrefWidth(imageDim);

        name.setText(item.getName());
        image.setGraphic(spaceHolder);
        double cost = (int) item.getCost();
        this.cost.setText(cost == 0 ? "" : Util.compressCurrency(cost));
        if (item.getDescription() == null)
            description.setText("");
        else
            description.setText(item.getDescription().replace("\t", "\n"));
        setAttributes(attributeList, item.getAttributes().entrySet().iterator());
    }

    protected static final DecimalFormat attributeFormat = new DecimalFormat("#.##");

    protected void setAttributes(VBox list, Iterator<Map.Entry<String, Double>> attributes) {
        setAttributes(list, attributes, false);
    }

    protected void setAttributes(VBox list, Iterator<Map.Entry<String, Double>> attributes, boolean colorNegatives) {
        list.getChildren().clear();
        int shade = 0;
        while (attributes.hasNext()) {
            Map.Entry<String, Double> itemSource = attributes.next();
            boolean negative = itemSource.getValue() < 0;
            HBox item = new HBox();
            item.setPadding(new Insets(2.5, 2.5, 2.5, 2.5));
            Label atName = new Label(Util.capitalize(itemSource.getKey()));
            item.getChildren().add(atName);
            Pane spacer = new Pane();
            item.getChildren().add(spacer);
            Label atValue = new Label(attributeFormat.format(itemSource.getValue()));
            item.getChildren().add(atValue);
            HBox.setHgrow(spacer, Priority.ALWAYS);
            if ((1 & shade++) == 0)
                item.setId("manifest-shade");
            if (negative && colorNegatives)
                item.setId("manifest-warn");
            list.getChildren().add(item);
        }
    }
}
