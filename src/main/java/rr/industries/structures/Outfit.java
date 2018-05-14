package rr.industries.structures;

import rr.industries.parser.DataNode;
import rr.industries.parser.Tag;

import java.lang.reflect.Field;

//All fields
/*
[category, licenses, cost, thumbnail, mass, outfit space, weapon capacity, gun ports, weapon, description,
required crew, turret mounts, energy capacity, energy consumption, heat generation, energy generation, active cooling,
cooling energy, ramscoop, quantum keystone, engine capacity, thrust, thrusting energy, thrusting heat, flare sprite,
flare sound, turn, turning energy, turning heat, railgun slug capacity, tracker capacity, ammo, shield generation,
shield energy, shield heat, cooling, plural, capture attack, capture defense, unplunderable, cargo space,
reverse thrust, reverse thrusting energy, reverse thrusting heat, finisher capacity, inscrutable, solar collection,
hull repair rate, hull energy, hull heat, meteor capacity, sidewinder capacity, javelin capacity, torpedo capacity,
typhoon capacity, rocket capacity, gatling round capacity, radar jamming, jump speed, jump fuel, hyperdrive,
scram drive, jump drive, cargo scan power, cargo scan speed, outfit scan power, outfit scan speed, asteroid scan power,
tactical scan power, atmosphere scan, cloak, cloaking energy, cloaking fuel, cooling inefficiency, heat dissipation,
bunks, fuel capacity, scan interference, map, piercer capacity, minelayer capacity, thunderhead capacity,
flotsam sprite, installable, automaton, afterburner thrust, afterburner fuel, afterburner heat, afterburner effect,
afterburner energy]
 */

//Decimal Fields
/*
[energyConsumption, heatGeneration, energyGeneration, coolingEnergy, ramscoop, thrust, thrustingEnergy, thrustingHeat,
turn, turningEnergy, turningHeat, mass, shieldGeneration, shieldEnergy, cooling, captureAttack, reverseThrust,
reverseThrustingEnergy, reverseThrustingHeat, solarCollection, shieldHeat, hullRepairRate, hullEnergy, hullHeat,
jumpSpeed, scramDrive, cloak, heatDissipation, scanInterference, afterburnerThrust, afterburnerFuel, afterburnerHeat,
afterburnerEnergy]
 */

public class Outfit extends Structure {
    public String name;

    Attributes attributes;

    @Tag("ammo")
    String ammo;
    @Tag("thumbnail")
    String thumbnail;
    @Tag("description")
    String description;
    @Tag("flare sprite")
    String flareSprite;
    @Tag("flare sound")
    String flareSound;
    @Tag("plural")
    String plural;
    @Tag("installable")
    double installable = 0;
    @Tag("map")
    double map;
    @Tag("afterburner effect")
    String afterburnerEffect;
    @Tag("flotsam sprite")
    String flotsamSprite;
    @Tag("weapon")
    Weapon weapon;

    //String ammo;
    //int map = 0;

    public Outfit(DataNode node) {
        if (node.tokens.size() >= 2) {
            this.name = node.token(1);
        } else {
            node.printTrace("Invalid Outfit Name");
        }
        attributes = new Attributes();
        ;
        Field[] fields1 = this.getClass().getDeclaredFields();
        Field[] fields2 = Attributes.class.getDeclaredFields();
        int length = fields1.length + fields2.length;
        NODE_LOOP:
        for (DataNode child : node.children) {
            for (int i = 0; i < length; i++) {
                Field field;
                Object object;
                if (i < fields1.length) {
                    field = fields1[i];
                    object = this;
                } else {
                    field = fields2[i - fields1.length];
                    object = attributes;
                }
                if (deserialize(field, object, child) == 0)
                    continue NODE_LOOP;
            }
            child.printTrace("Unrecognized Node:");
        }
    }

    @Override
    public String toString() {
        return "outfit " + DataNode.escapeWord(name);
    }
}