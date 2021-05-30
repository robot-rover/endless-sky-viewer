package rr.industries;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import rr.industries.structures.Outfit;
import rr.industries.structures.Ship;

import java.util.Map;
import java.util.function.Consumer;

import static java.lang.Math.exp;

public class ShipPreviewPane extends ItemPreviewPane {

    Consumer<ShipListItem> openVariants;
    Consumer<Ship> editShip;

    ShipListItem ship;
    VBox stats;
    Button variantButton;

    public ShipPreviewPane(Consumer<ShipListItem> openVariants, Consumer<Ship> editShip) {
        super();
        this.openVariants = openVariants;
        this.editShip = editShip;

        stats = new VBox();
        stats.setFillWidth(true);
        stats.setPadding(new Insets(10, 20, 10, 20));
        root.getChildren().add(root.getChildren().size() - 2, stats);

        if (openVariants != null || editShip != null) {
            variantButton = new Button("");
            variantButton.setVisible(false);
            root.getChildren().add(root.getChildren().size() - 1, variantButton);
        }
    }

    @Override
    public void setItem(ListItem item) {
        super.setItem(item);

        ship = (ShipListItem) item;
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
        redrawStats(ship.getBaseShip().getAppliedAttributes());
    }

    private HBox createStatEntry(String key, double value1, double value2, boolean shade, boolean warn) {
        return createStatEntry(key, slashFormat(value1, value2), shade, warn);
    }

    private HBox createStatEntry(String key, double value, boolean shade, boolean warn) {
        return createStatEntry(key, attributeFormat.format(value), shade, warn);
    }

    private HBox createStatEntry(String key, String value, boolean shade, boolean warn) {
        HBox item = new HBox();
        item.setPadding(new Insets(2.5, 2.5, 2.5, 2.5));
        Label atName = new Label(key);
        item.getChildren().add(atName);
        Pane spacer = new Pane();
        item.getChildren().add(spacer);
        Label atValue = new Label(value);
        item.getChildren().add(atValue);
        HBox.setHgrow(spacer, Priority.ALWAYS);
        if (shade)
            item.setId("manifest-shade");
        if (warn)
            item.setId("manifest-warn");
        return item;
    }

    public String slashFormat(double val1, double val2) {
        String val1String = attributeFormat.format(val1);
        String val2String = attributeFormat.format(val2);
        return val1String + " / " + val2String;
    }

    public void redrawStats(Map<String, Double> attributes) {
        // Apply Attributes
        ship.getBaseShip().getAppliedAttributes();

        stats.getChildren().clear();
        int shade = 0;
        double shieldGeneration = attributes.getOrDefault("shield generation", 0.0) * (1. + attributes.getOrDefault("shield generation multiplier", 0.0));
        stats.getChildren().add(createStatEntry("Shields Charge / Max", shieldGeneration * 60, attributes.getOrDefault("shields", 0.0), shade++ % 2 == 0, false));
        double hullGeneration = attributes.getOrDefault("hull repair rate", 0.0) * (1. + attributes.getOrDefault("hull repair multiplier", 0.0));
        if (hullGeneration == 0) {
            stats.getChildren().add(createStatEntry("Hull", attributes.getOrDefault("hull", 0.0), shade++ % 2 == 0, false));
        } else {
            stats.getChildren().add(createStatEntry("Hull Repair / Max", hullGeneration * 60, attributes.getOrDefault("hull", 0.0), shade++ % 2 == 0, false));
        }
        double mass = ship.getBaseShip().addAttributes.mass;
        stats.getChildren().add(createStatEntry("Mass w/o Cargo", mass, shade++ % 2 == 0, false));
        stats.getChildren().add(createStatEntry("Cargo Space", attributes.getOrDefault("cargo space", 0.0), shade++ % 2 == 0, false));
        stats.getChildren().add(createStatEntry("Required Crew / Bunks", attributes.getOrDefault("required crew", 0.0), attributes.getOrDefault("bunks", 0.0), shade++ % 2 == 0, false));
        stats.getChildren().add(createStatEntry("Fuel Capacity", attributes.getOrDefault("fuel capacity", 0.0), shade++ % 2 == 0, false));
        double thrust = attributes.getOrDefault("thrust", 0.0);
        double drag = attributes.getOrDefault("drag", 0.0);
        double turn = attributes.getOrDefault("turn", 0.0);
        String speed = drag == 0 ? "<inf>" : attributeFormat.format(thrust / drag * 60.);
        String acceleration = mass == 0 ? "<inf>" : attributeFormat.format(thrust / mass * 3600.);
        String turnRate = mass == 0 ? "<inf>" : attributeFormat.format(turn / mass * 60.);
        stats.getChildren().add(createStatEntry("Max Speed", speed, shade++ % 2 == 0, false));
        stats.getChildren().add(createStatEntry("Acceleration", acceleration, shade++ % 2 == 0, false));
        stats.getChildren().add(createStatEntry("Turning", turnRate, shade++ % 2 == 0, false));
        stats.getChildren().add(createStatEntry("Outfit Space", attributes.getOrDefault("outfit space", 0.0), ship.getBaseShip().getAttributes().getOrDefault("outfit space", 0.0), shade++ % 2 == 0, false));
        stats.getChildren().add(createStatEntry("Weapon Capacity", attributes.getOrDefault("weapon capacity", 0.0), ship.getBaseShip().getAttributes().getOrDefault("weapon capacity", 0.0), shade++ % 2 == 0, false));
        stats.getChildren().add(createStatEntry("Engine Capacity", attributes.getOrDefault("engine capacity", 0.0), ship.getBaseShip().getAttributes().getOrDefault("engine capacity", 0.0), shade++ % 2 == 0, false));
        stats.getChildren().add(createStatEntry("Gun Ports", attributes.getOrDefault("gun ports", 0.0), ship.getBaseShip().getAttributes().getOrDefault("gun ports", 0.0), shade++ % 2 == 0, false));
        stats.getChildren().add(createStatEntry("Turret Mounts", attributes.getOrDefault("turret mounts", 0.0), ship.getBaseShip().getAttributes().getOrDefault("turret mounts", 0.0), shade++ % 2 == 0, false));
        stats.getChildren().add(createStatEntry("", "Energy / Heat", shade++ % 2 == 0, false));
        double idleEnergy = attributes.getOrDefault("energy generation", 0.0)
                + attributes.getOrDefault("solar collection", 0.0)
                + attributes.getOrDefault("fuel energy", 0.0)
                - attributes.getOrDefault("energy consumption", 0.0)
                - attributes.getOrDefault("cooling energy", 0.0);
        double coolingInefficiency = attributes.getOrDefault("cooling inefficiency", 0.0);
        double coolingEfficiency = 2. + 2. / (1. + exp(coolingInefficiency / -2.)) - 4. / (1. + exp(coolingInefficiency / -4.));
        double idleHeat = attributes.getOrDefault("heat generation", 0.0)
                + attributes.getOrDefault("solar heat", 0.0)
                + attributes.getOrDefault("fuel heat", 0.0)
                - coolingEfficiency * (
                attributes.getOrDefault("cooling", 0.0)
                        + attributes.getOrDefault("active cooling", 0.0)
        );
        stats.getChildren().add(createStatEntry("Idle", idleEnergy * 60, idleHeat * 60, shade++ % 2 == 0, false));
        double movingEnergy = attributes.getOrDefault("thrusting energy", 0.0)
                + attributes.getOrDefault("turning energy", 0.0)
                + attributes.getOrDefault("afterburner energy", 0.0);
        double movingHeat = attributes.getOrDefault("thrusting heat", 0.0)
                + attributes.getOrDefault("turning heat", 0.0)
                + attributes.getOrDefault("afterburner heat", 0.0);
        stats.getChildren().add(createStatEntry("Moving", movingEnergy * 60, movingHeat * 60, shade++ % 2 == 0, false));
        double firingEnergy = ship.getBaseShip().getOutfits().stream().map(Outfit::getWeapon).filter(weapon -> weapon != null && weapon.getReload() > 0).mapToDouble(weapon -> weapon.getFiringEnergy() / weapon.getReload()).sum();
        double firingHeat = ship.getBaseShip().getOutfits().stream().map(Outfit::getWeapon).filter(weapon -> weapon != null && weapon.getReload() > 0).mapToDouble(weapon -> weapon.getFiringHeat() / weapon.getReload()).sum();
        stats.getChildren().add(createStatEntry("Firing", firingEnergy * 60, firingHeat * 60, shade++ % 2 == 0, false));
        double shieldEnergy = attributes.getOrDefault("shield energy", 0.0) * (1. + attributes.getOrDefault("shield energy multiplier", 0.0));
        double hullEnergy = attributes.getOrDefault("hull energy", 0.0) * (1. + attributes.getOrDefault("hull energy multiplier", 0.0));
        double combinedEnergy = shieldEnergy + hullEnergy;
        double shieldHeat = attributes.getOrDefault("shield heat", 0.0) * (1. + attributes.getOrDefault("shield heat multiplier", 0.0));
        double hullHeat = attributes.getOrDefault("hull heat", 0.0) * (1. + attributes.getOrDefault("hull heat multiplier", 0.0));
        double combinedHeat = shieldHeat + hullHeat;
        boolean showShields = shieldEnergy != 0 || shieldHeat != 0;
        boolean showHull = hullEnergy != 0 || hullHeat != 0;
        String label;
        if (showShields && showHull) {
            label = "Shields + Heat";
        } else if (!showShields && showHull) {
            label = "Hull Repair";
        } else {
            label = "Shield Generation";
        }
        stats.getChildren().add(createStatEntry(label, combinedEnergy * 60., combinedHeat * 60., shade++ % 2 == 0, false));
        double maxEnergy = attributes.getOrDefault("energy capacity", 0.0);
        double MAXIMUM_TEMPERATURE = 100.;
        double maxHeat = 60 * (mass * MAXIMUM_TEMPERATURE) * (0.001 * attributes.getOrDefault("heat dissipation", 0.0));
        stats.getChildren().add(createStatEntry("Maximum", maxEnergy, maxHeat, shade++ % 2 == 0, false));

    }

    public void redrawAttributes(Map<String, Double> attributes) {
        setAttributes(attributeList, attributes.entrySet().iterator(), true);
        redrawStats(attributes);
    }
}
