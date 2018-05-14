package rr.industries.structures;

import rr.industries.parser.DataNode;
import rr.industries.parser.Tag;

import java.lang.reflect.Field;

//[sprite, sound, hit effect, inaccuracy, velocity, lifetime, reload, firing energy, firing heat, shield damage,
// hull damage, hardpoint sprite, hardpoint offset, turret turn, anti-missile, firing force, hit force, submunition,
// heat damage, icon, firing fuel, die effect, ammo, fire effect, acceleration, drag, turn, homing, infrared tracking,
// missile strength, radar tracking, optical tracking, trigger radius, blast radius, stream, burst count, burst reload,
// cluster, random velocity, random lifetime]

//sprite needs second layer

public class Weapon extends Structure {
    //@Tag("sprite")
    //double sprite = 0;
    @Tag("sound")
    String sound;
    @Tag("hit effect")
    String hitEffect;
    @Tag("inaccuracy")
    double inaccuracy = 0;
    @Tag("velocity")
    double velocity = 0;
    @Tag("lifetime")
    double lifetime = 0;
    @Tag("reload")
    double reload = 0;
    @Tag("firing energy")
    double firingEnergy = 0;
    @Tag("firing heat")
    double firingHeat = 0;
    @Tag("shield damage")
    double shieldDamage = 0;
    @Tag("hull damage")
    double hullDamage = 0;
    @Tag("slowing damage")
    double slowingDamage = 0;
    @Tag("ion damage")
    double ionDamage = 0;
    @Tag("hardpoint sprite")
    String hardpointSprite;
    @Tag("hardpoint offset")
    double hardpointOffset = 0;
    @Tag("turret turn")
    double turretTurn = 0;
    @Tag("anti-missile")
    double antiMissile = 0;
    @Tag("firing force")
    double firingForce = 0;
    @Tag("hit force")
    double hitForce = 0;
    @Tag("submunition")
    String submunition;
    @Tag("heat damage")
    double heatDamage = 0;
    @Tag("icon")
    String icon;
    @Tag("firing fuel")
    double firingFuel = 0;
    @Tag("die effect")
    String dieEffect;
    @Tag("ammo")
    String ammo;
    @Tag("fire effect")
    String fireEffect;
    @Tag("acceleration")
    double acceleration = 0;
    @Tag("drag")
    double drag = 0;
    @Tag("turn")
    double turn = 0;
    @Tag("homing")
    double homing = 0;
    @Tag("infrared tracking")
    double infraredTracking = 0;
    @Tag("missile strength")
    double missileStrength = 0;
    @Tag("radar tracking")
    double radarTracking = 0;
    @Tag("optical tracking")
    double opticalTracking = 0;
    @Tag("trigger radius")
    double triggerRadius = 0;
    @Tag("blast radius")
    double blastRadius = 0;
    @Tag("stream")
    boolean stream = false;
    @Tag("burst count")
    double burstCount = 0;
    @Tag("burst reload")
    double burstReload = 0;
    @Tag("disruption damage")
    double disruptionDamage;
    @Tag("cluster")
    boolean cluster = false;
    @Tag("random velocity")
    double randomVelocity = 0;
    @Tag("random lifetime")
    double randomLifetime = 0;
    @Tag("split range")
    double splitRange = 0;
    @Tag("piercing")
    double piercing = 0;
    @Tag("phasing")
    boolean phasing;
    @Tag("safe")
    boolean safe;
    @Tag("tracking")
    double tracking;
    @Tag("sprite")
    Sprite sprite;
    //todo: this should have a 3rd token w/ a double
    @Tag("live effect")
    String liveEffect;

    public Weapon(DataNode node) {
        Field[] fields = this.getClass().getDeclaredFields();
        NODE_LOOP:
        for (DataNode child : node.children) {
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                if (Structure.deserialize(field, this, child) == 0) {
                    continue NODE_LOOP;
                }
            }
            child.printTrace("Unrecognized Node:");
        }
    }
}
