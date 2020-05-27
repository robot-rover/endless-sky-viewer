package rr.industries;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import rr.industries.structures.Category;
import rr.industries.structures.Outfit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Pane for displaying a list of outfits, separated by Category
 */
public class OutfitListPane extends ScrollPane {
    Map<Category, FlowPane> categoryPanes;
    List<OutfitListItem> outfitList;

    OutfitListItem selected;
    Consumer<OutfitListItem> outfitSelect;

    public OutfitListItem getListItem(Outfit outfit) {
        return outfitList.stream().filter(v -> v.getOutfit() == outfit).findAny().orElse(null);
    }

    public OutfitListPane(List<Outfit> outfitListSource, Consumer<OutfitListItem> outfitSelect) {
        this.outfitSelect = outfitSelect;
        this.setFitToWidth(true);
        categoryPanes = new HashMap<>();
        for (int i = 0; i < Category.UNKNOWN.ordinal(); i++) {
            FlowPane flowPane = new FlowPane();
            categoryPanes.put(Category.values()[i], flowPane);
        }

        outfitList = new ArrayList<>();
        for (Outfit outfit : outfitListSource) {
            if(outfit.getCategory() == Category.UNKNOWN) continue;
            FlowPane flowPane = categoryPanes.get(outfit.getCategory());
            OutfitListItem item = new OutfitListItem(outfit);
            outfitList.add(item);
            item.setCallback(this::callback);
            flowPane.getChildren().add(item.getNode());
        }
    }

    void callback(OutfitListItem outfit) {
        if (selected != null)
            selected.setHighlight(false);
        selected = outfit;
        selected.setHighlight(true);
        outfitSelect.accept(outfit);
    }

    public void setCategory(Category category) {
        this.setContent(categoryPanes.get(category));
    }
}
