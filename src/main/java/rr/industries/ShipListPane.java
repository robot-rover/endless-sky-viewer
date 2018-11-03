package rr.industries;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.util.Duration;
import rr.industries.structures.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Pane for displaying a list of ships, seperated into sections by Category Headers
 */
public class ShipListPane extends ScrollPane {
    List<ShipListItem> sourceList;
    Map<Category, List<ShipListItem>> sourceTree;
    Map<Category, HBox> headers;
    Consumer<ShipListItem> callbackFunc;
    ShipListItem selected;

    public ShipListPane(List<ShipListItem> sourceList) {
        headers = new HashMap<>();
        this.sourceList = sourceList;
        sourceTree = new HashMap<>();
        for (int i = Category.TRANSPORT.ordinal(); i < Category.values().length; i++) {
            sourceTree.put(Category.values()[i], new ArrayList<>());
        }

        for (ShipListItem shipItem : sourceList) {
            shipItem.setCallback(this::callback);
            Category category = shipItem.getBaseShip().getCategory();
            if (category == null)
                continue;
            sourceTree.get(category).add(shipItem);
        }
        sourceTree.values().removeIf(shipListItems -> shipListItems.size() == 0);

        setMinWidth(280);
        setFitToWidth(true);
        VBox categoryRoot = new VBox();
        categoryRoot.setFillWidth(true);
        setContent(categoryRoot);

        for (int i = Category.TRANSPORT.ordinal(); i < Category.values().length; i++) {
            List<ShipListItem> categorySource = sourceTree.get(Category.values()[i]);

            if (categorySource == null)
                continue;

            HBox categoryHeader = new HBox();
            categoryHeader.setPadding(new Insets(20, 10, 10, 20));
            categoryHeader.setAlignment(Pos.CENTER_LEFT);

            Label categoryHeaderLabel = new Label(Category.values()[i].toString());
            categoryHeaderLabel.setFont(new Font(22));
            categoryHeader.getChildren().add(categoryHeaderLabel);
            headers.put(Category.values()[i], categoryHeader);

            Line categoryHeaderDecoration = new Line(0, 0, 1, 0);
            categoryHeader.getChildren().add(categoryHeaderDecoration);
            HBox.setHgrow(categoryHeaderDecoration, Priority.ALWAYS);

            categoryRoot.getChildren().add(categoryHeader);

            TilePane tile = new TilePane();
            tile.setAlignment(Pos.BASELINE_LEFT);
            tile.setHgap(5);
            tile.setVgap(5);
            tile.setPadding(new Insets(5));
            categoryRoot.getChildren().add(tile);


            if (categorySource != null)
                for (ShipListItem ship : categorySource) {
                    tile.getChildren().add(ship.getNode());
                }
        }
    }

    void callback(ShipListItem ship) {
        if (selected != null)
            selected.setHighlight(false);
        selected = ship;
        selected.setHighlight(true);
        callbackFunc.accept(ship);
    }

    void setCallback(Consumer<ShipListItem> clickedOnCallback) {
        callbackFunc = clickedOnCallback;
    }

    void scrollToCategory(Category category) {
        double height = getContent().getBoundsInLocal().getHeight() - getViewportBounds().getHeight();

        HBox node = headers.get(category);

        if (node == null) {
            System.err.println("Couldn't find header for category" + category.name());
            return;
        }

        double y = node.getBoundsInParent().getMinY();
        ScrollTransition scrollTransition;

        // scrolling values range from 0 to 1
        if (height <= 0)
            scrollTransition = new ScrollTransition(this, 1.);
        else
            scrollTransition = new ScrollTransition(this, y / height);
        scrollTransition.play();
        /*Transition transition = new BackgroundTransition(node, 0.5);
        transition.play();*/


        // just for usability
        node.requestFocus();
    }

    private class BackgroundTransition extends Transition {

        private final Pane node;

        public BackgroundTransition(Pane node, double duration) {
            this.node = node;
            setCycleDuration(Duration.seconds(duration));
        }

        @Override
        protected void interpolate(double frac) {
            node.setBackground(new Background(new BackgroundFill(
                    Color.LIGHTGRAY.deriveColor(1, 1, 1, Math.min(-4 * Math.abs(frac - 0.5) + 2, 1)),
                    new CornerRadii(10),
                    new Insets(10, 10, 0., 10))));
        }
    }

    private class ScrollTransition extends Transition {

        private final ScrollPane node;
        double scrollStart;
        double scrollDelta;

        public ScrollTransition(ScrollPane node, double scrollTo) {
            this.node = node;
            scrollStart = node.getVvalue();
            scrollDelta = scrollTo - scrollStart;
            setCycleDuration(Duration.seconds(0.5 * Math.abs(Math.min(scrollTo, 1) - scrollStart)));
            //setCycleDuration(Duration.seconds(3));
            setInterpolator(new Interpolator() {
                @Override
                protected double curve(double t) {
                    if (t < 0.5)
                        return 2 * t * t;
                    else
                        return -2 * Math.pow((t - 1.), 2.) + 1.;
                }
            });
        }

        @Override
        protected void interpolate(double frac) {
            node.setVvalue(scrollStart + (scrollDelta * frac));
        }
    }
}
