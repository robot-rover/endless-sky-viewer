package rr.industries;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import rr.industries.structures.Outfit;
import rr.industries.structures.Weapon;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Implementation of ItemPreviewPane for displaying Outfits
 */
public class OutfitPreviewPane extends ItemPreviewPane {

    VBox weaponBox;

    HBox outfitButtons;
    Button addButton;
    Button removeButton;

    BiConsumer<Outfit, Integer> changeOutfit;

    public OutfitPreviewPane(BiConsumer<Outfit, Integer> changeOutfit) {
        super();

        this.changeOutfit = changeOutfit;

        weaponBox = new VBox();
        weaponBox.setFillWidth(true);
        weaponBox.setPadding(new Insets(30, 20, 10, 20));
        root.getChildren().add(weaponBox);

        outfitButtons = new HBox();
        outfitButtons.setPadding(new Insets(10));
        outfitButtons.setSpacing(10);
        outfitButtons.setFillHeight(false);
        outfitButtons.setVisible(false);
        root.getChildren().add(outfitButtons);

        addButton = new Button("+");
        addButton.setPrefWidth(100);
        addButton.setFont(Font.font(22));
        outfitButtons.getChildren().add(addButton);

        Pane spacer = new Pane();

        outfitButtons.getChildren().add(spacer);
        HBox.setHgrow(spacer, Priority.ALWAYS);

        removeButton = new Button("-");
        removeButton.setPrefWidth(100);
        removeButton.setFont(Font.font(22));
        outfitButtons.getChildren().add(removeButton);
    }

    @Override
    public void setItem(ListItem item) {
        super.setItem(item);

        weaponBox.getChildren().clear();
        OutfitListItem outfit = (OutfitListItem) item;
        Weapon weapon = outfit.getOutfit().getWeapon();
        if (weapon != null) {
            List<Map.Entry<String, Double>> weaponAttributes = new ArrayList<>();

            double shotsPerFrame = 1 / weapon.getReload();
            weaponAttributes.add(attribute("Shots per Second", shotsPerFrame * 60));

            double firingHeat = weapon.getFiringHeat();
            if (firingHeat != 0)
                weaponAttributes.add(attribute("Firing Heat", firingHeat * shotsPerFrame));

            double firingEnergy = weapon.getFiringEnergy();
            if (firingEnergy != 0)
                weaponAttributes.add(attribute("Firing Energy", firingEnergy * shotsPerFrame));

            double antiMissle = weapon.getAntiMissile();
            if (antiMissle != 0)
                weaponAttributes.add(attribute("Anti-Missile", antiMissle));

            double[] damage = weapon.getDamage();
            if (damage[Weapon.SHIELD_DAMAGE] != 0)
                weaponAttributes.add(attribute("Shield Damage", damage[Weapon.SHIELD_DAMAGE] * shotsPerFrame));
            if (damage[Weapon.HULL_DAMAGE] != 0)
                weaponAttributes.add(attribute("Hull Damage", damage[Weapon.HULL_DAMAGE] * shotsPerFrame));
            if (damage[Weapon.HEAT_DAMAGE] != 0)
                weaponAttributes.add(attribute("Heat Damage", damage[Weapon.HEAT_DAMAGE] * shotsPerFrame));
            if (damage[Weapon.ION_DAMAGE] != 0)
                weaponAttributes.add(attribute("Ion Damage", damage[Weapon.ION_DAMAGE] * shotsPerFrame));
            if (damage[Weapon.DISRUPTION_DAMAGE] != 0)
                weaponAttributes.add(attribute("Disruption Damage", damage[Weapon.DISRUPTION_DAMAGE] * shotsPerFrame));
            if (damage[Weapon.SLOWING_DAMAGE] != 0)
                weaponAttributes.add(attribute("Slowing Damage", damage[Weapon.SLOWING_DAMAGE] * shotsPerFrame));

            setAttributes(weaponBox, weaponAttributes.iterator());
        }

        addButton.setOnAction(v -> changeOutfit.accept(outfit.getOutfit(), 1));
        removeButton.setOnAction(v -> changeOutfit.accept(outfit.getOutfit(), -1));
        outfitButtons.setVisible(true);
    }

    private Map.Entry<String, Double> attribute(String key, Double val) {
        return new Util.Entry<>(key, val);
    }
}
