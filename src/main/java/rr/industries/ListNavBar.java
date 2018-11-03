package rr.industries;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import rr.industries.structures.Category;

import java.util.function.Consumer;

/**
 * A Navigation Bar that is vertical
 * It allows buttons that scroll to specific categories
 * Implementations call #CreateButton, #CreateSpacer, or #CreateHeader
 * in an initialization block
 */
public abstract class ListNavBar extends ScrollPane {

    VBox root;
    Consumer<Category> scrollTargetCallback;

    /**
     * Creates a new ListNavBar
     *
     * @param scrollTargetCallback callback for the requested category
     */
    public ListNavBar(Consumer<Category> scrollTargetCallback) {
        this.scrollTargetCallback = scrollTargetCallback;
        setVbarPolicy(ScrollBarPolicy.NEVER);
        setFitToWidth(true);

        root = new VBox();
        setContent(root);
        root.setFillWidth(true);

        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(5);
        root.setPadding(new Insets(20, 0, 20, 0));
        root.setPrefWidth(38);
        root.setMinWidth(38);
        setPrefWidth(40);
        setMinWidth(40);
    }

    void createButton(String name, Category category) {
        Button label = new Button(name);
        label.setRotate(-90);
        Group buttonRoot = new Group(label);
        //buttonRoot.setRotate(90);
        root.getChildren().add(buttonRoot);
        final Category temp = category;
        label.setOnAction(event -> scrollTargetCallback.accept(temp));
    }

    void createButton(Category category) {
        createButton(category.toString(), category);
    }

    void createHeader(String name) {
        Label label = new Label(name);
        label.setFont(new Font(15));
        label.setRotate(-90);
        Group buttonRoot = new Group(label);
        root.getChildren().add(buttonRoot);
    }

    void createSpacer() {
        Pane pane = new Pane();
        pane.setPadding(new Insets(20, 0, 0, 0));
        root.getChildren().add(pane);
    }
}
