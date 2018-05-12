package rr.industries.structures;

import rr.industries.Attributes;
import rr.industries.Util;
import rr.industries.parser.ParseContext;
import rr.industries.parser.Parser;
import rr.industries.parser.Tag;

import java.lang.reflect.Field;
import java.util.List;

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

public class Outfit implements ParseContext {
    public String name;

    Attributes attributes;

    @Tag("category")
    String category;
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
    @Tag("afterburner effect")
    String afterburnerEffect;
    @Tag("flotsam sprite")
    String flotsamSprite;
    @Tag("licenses")
    Licenses licenses;
    Weapon weapon;

    //String ammo;
    //int map = 0;

    public Outfit(String name, Parser parser) {
        this.name = name;
        attributes = new Attributes();

        int[] level = new int[1];
        level[0] = 1;
        while (true) {
            List<String> words = parser.getNextWords(level);
            if (words.stream().anyMatch((String s) -> s.contains("`")))
                Util.tallyItem(name);
            if (level[0] < 1) {
                break;
            }
            if (level[0] == 1) {
                boolean found = false;
                Field[] fields1 = this.getClass().getDeclaredFields();
                Field[] fields2 = Attributes.class.getDeclaredFields();
                int length = fields1.length + fields2.length;
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
                    Tag tag = field.getDeclaredAnnotation(Tag.class);
                    if (tag != null && tag.value().equals(words.get(0))) {
                        try {
                            if (field.getType().equals(double.class)) {
                                found = true;
                                try {
                                    field.setDouble(object, Double.parseDouble(words.get(1)));
                                } catch (NumberFormatException e) {
                                    Util.tallyItem(field.getName());
                                }
                            } else if (field.getType().equals(String.class)) {
                                found = true;
                                field.set(object, words.get(1));
                            } else if (field.getType().equals(Licenses.class)) {
                                found = true;
                                field.set(object, new Licenses(parser));
                            } else if (field.getType().equals(Weapon.class)) {
                                found = true;
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    if (found)
                        break;
                }
                if (!found) {
                    System.err.println("Could not find field called " + words.toString());
                }
            }
            //System.out.println(level[0] + " - " + words);
        }
        parser.backup();
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder(Parser.escapeWord(name));
        Field[] fields1 = this.getClass().getDeclaredFields();
        Field[] fields2 = Attributes.class.getDeclaredFields();
        int length = fields1.length + fields2.length;
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
            Tag tag = field.getDeclaredAnnotation(Tag.class);
            if (tag == null)
                continue;
            Object value;
            try {
                value = field.get(object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                value = null;
            }
            if (value == null || value.getClass() == Double.class && ((Double) value) == 0)
                continue;
            String tagName = Parser.escapeWord(tag.value());
            String valueName = Parser.escapeWord(value.toString());
            string.append("\n\t").append(tagName).append(" ").append(valueName);
        }
        return string.toString();
    }
}