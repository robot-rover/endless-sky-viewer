package rr.industries;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import rr.industries.structures.Category;
import rr.industries.structures.Outfit;
import rr.industries.structures.Ship;

import java.util.*;
import java.util.function.Consumer;

/**
 * Shows a list of outfits installed in a ship
 */
public class ShipManifestPane extends ScrollPane {

    Consumer<Outfit> selectOutfit;

    static final double paneWidth = 300;
    static final double padding = 10;

    private static final Background greyBackground = new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY));
    VBox root;
    ManifestCategory[] categoryList;

    public ShipManifestPane() {
        this(null);
    }

    public ShipManifestPane(Consumer<Outfit> selectOutfit) {
        this.selectOutfit = selectOutfit;

        setMinWidth(paneWidth);
        setFitToWidth(true);
        setVbarPolicy(ScrollBarPolicy.NEVER);

        root = new VBox();
        root.setSpacing(0);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPrefWidth(padding);
        root.setFillWidth(true);
        root.setPadding(new Insets(padding, padding + 20, padding, padding + 20));

        categoryList = new ManifestCategory[Category.UNKNOWN.ordinal()];
        for (int i = 0; i < Category.UNKNOWN.ordinal(); i++) {
            categoryList[i] = new ManifestCategory(Category.values()[i], selectOutfit);
        }
        this.setContent(root);

    }

    protected static final Background highlight = new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY));
    private ManifestItem shaded;

    public void shadeItem(Outfit outfit) {
        if (shaded != null)
            if (shaded.shade) {
                shaded.setBackground(greyBackground);
            } else {
                shaded.setBackground(null);
            }
        shaded = null;
        Category outfitCategory = outfit.getCategory();
        ManifestCategory category = categoryList[outfitCategory.ordinal()];
        Optional<ManifestItem> newShade = category.listItems.stream().filter(v -> v.outfit == outfit).findAny();
        if (newShade.isPresent()) {
            shaded = newShade.get();
            shaded.setBackground(highlight);
        }
    }

    public void setShip(Ship ship) {
        Map<Outfit, Integer> outfitsCollapsed = new HashMap<>();
        for (Outfit outfit : ship.getOutfits()) {
            int present = outfitsCollapsed.getOrDefault(outfit, 0);
            if (present == 0)
                outfitsCollapsed.put(outfit, 1);
            else
                outfitsCollapsed.put(outfit, present + 1);
        }

        for (ManifestCategory category : categoryList) {
            category.clear();
            root.getChildren().remove(category);
            outfitsCollapsed.entrySet().stream().filter(v -> v.getKey().getCategory() == category.category).forEach(v -> category.add(v.getKey(), v.getValue()));
            if (!category.listItems.isEmpty())
                root.getChildren().add(category);
        }
    }

    class ManifestCategory extends VBox {
        HBox categoryHeader;
        List<ManifestItem> listItems;
        final Category category;
        private boolean shade;
        Consumer<Outfit> selectOutfit;

        ManifestCategory(Category category, Consumer<Outfit> selectOutfit) {
            this.selectOutfit = selectOutfit;
            this.category = category;
            categoryHeader = new HBox();
            listItems = new ArrayList<>();
            categoryHeader.setPadding(new Insets(30, 0, 10, -20));
            categoryHeader.setAlignment(Pos.CENTER_LEFT);
            this.getChildren().add(categoryHeader);

            Label categoryHeaderLabel = new Label(category.toString());
            categoryHeaderLabel.setFont(new Font(18));
            categoryHeader.getChildren().add(categoryHeaderLabel);
        }

        private boolean shade() {
            return (shade = !shade);
        }

        void clear() {
            this.getChildren().removeAll(listItems);
            listItems.clear();
            shade = false;
        }

        void add(Outfit outfit, int amount) {
            Optional<ManifestItem> item = listItems.stream().filter(v -> v.outfit == outfit).findAny();
            if (item.isPresent()) {
                item.get().add(amount);
            } else {
                ManifestItem itemNew = new ManifestItem(outfit, amount, shade(), selectOutfit);
                listItems.add(itemNew);
                this.getChildren().add(itemNew);
            }
        }
    }

    class ManifestItem extends HBox {
        int amount;
        Outfit outfit;
        Label atValue;
        boolean shade;

        ManifestItem(Outfit outfit, int amount, boolean shade, Consumer<Outfit> selectOutfit) {
            this.amount = amount;
            this.outfit = outfit;
            this.shade = shade;

            this.setPadding(new Insets(2.5, 2.5, 2.5, 2.5));
            Label atName = new Label(outfit.getName());
            this.getChildren().add(atName);
            Pane spacer = new Pane();
            this.getChildren().add(spacer);
            atValue = new Label(Integer.toString(amount));
            this.getChildren().add(atValue);
            HBox.setHgrow(spacer, Priority.ALWAYS);
            if (shade)
                this.setBackground(greyBackground);
            if (selectOutfit != null) {
                this.setOnMouseClicked(v -> selectOutfit.accept(outfit));
            }
        }

        void add(int amount) {
            this.amount += amount;
            atValue.setText(Integer.toString(amount));
        }
    }
}
