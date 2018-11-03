package rr.industries.structures;

import rr.industries.parser.DataNode;

/**
 * Class representing all the characteristics of a weapon, including sprites and
 * effects, sounds, icons, ammo, submunitions, and other attributes. Storing
 * these parameters in a separate class keeps each Projectile from needing a
 * copy of them, and storing them as class variables instead of in a map of
 * string to double significantly reduces access time.
 */
public class Weapon {
    public double getBurstCount() {
        return burstCount;
    }

    public double getBurstReload() {
        return burstReload;
    }

    private String sound;
    private String hitEffect;
    private double inaccuracy = 0;
    private double velocity = 0;
    private double lifetime = 0;
    private double reload = 0;
    private double firingEnergy = 0;
    private double firingHeat = 0;
    private String hardpointSprite;
    private double hardpointOffset = 0;
    private double turretTurn = 0;
    private double antiMissile = 0;
    private double firingForce = 0;
    private double hitForce = 0;
    private String submunition;
    private String icon;
    private double firingFuel = 0;
    private String dieEffect;
    private String ammo;
    private String fireEffect;
    private double acceleration = 0;
    private double drag = 0;
    private double turn = 0;
    private double homing = 0;
    private double infraredTracking = 0;
    private double missileStrength = 0;
    private double radarTracking = 0;
    private double opticalTracking = 0;
    private double triggerRadius = 0;
    private double blastRadius = 0;
    private boolean stream = false;
    private boolean noDamageScaling = false;
    private double burstCount = 0;
    private double burstReload = 0;
    private double randomVelocity = 0;
    private double randomLifetime = 0;
    private double splitRange = 0;
    private double piercing = 0;
    private boolean phasing;
    private boolean safe;
    private double tracking;
    private String sprite;
    private String liveEffect;

    /**
     * Creates a new Weapon
     *
     * @param node the source DataNode
     */
    public Weapon(DataNode node) {
        boolean isClustered = false;

        for (DataNode child : node.children) {
            String key = child.token(0);
            if (key.equals("stream"))
                stream = true;
            else if (key.equals("cluster"))
                isClustered = true;
            else if (key.equals("safe"))
                safe = true;
            else if (key.equals("phasing"))
                phasing = true;
            else if (key.equals("no damage scaling"))
                noDamageScaling = true;
            else if (child.tokens.size() < 2)
                child.printTrace("Skipping weapon attribute with no value specified:");
            else if (key.equals("sprite")) {
                //todo: sprite loading
            } else if (key.equals("hardpoint sprite")) {
                //todo: sprite loading
            } else if (key.equals("sound")) {
                //todo: sound loading
            } else if (key.equals("ammo")) {
                //todo: ammo loading
            } else if (key.equals("icon")) {
                //todo: ammo loading
            } else if (key.equals("fire effect")) {
                //todo: loading effects
                /*int count = (child.Size() >= 3) ? child.Value(2) : 1;
                fireEffects[GameData::Effects().Get(child.Token(1))] += count;*/
            } else if (key.equals("live effect")) {
                //todo: loading effects
                /*int count = (child.Size() >= 3) ? child.Value(2) : 1;
                liveEffects[GameData::Effects().Get(child.Token(1))] += count;*/
            } else if (key.equals("hit effect")) {
                //todo: loading effects
                /*int count = (child.Size() >= 3) ? child.Value(2) : 1;
                hitEffects[GameData::Effects().Get(child.Token(1))] += count;*/
            } else if (key.equals("die effect")) {
                //todo: loading effects
                /*int count = (child.Size() >= 3) ? child.Value(2) : 1;
                dieEffects[GameData::Effects().Get(child.Token(1))] += count;*/
            } else if (key.equals("submunition")) {
                //todo: submunition loading
                /*int count = (child.Size() >= 3) ? child.Value(2) : 1;
                submunitions[GameData::Outfits().Get(child.Token(1))] += count;*/
            } else {
                double value = child.value(1);
                if (key.equals("lifetime"))
                    lifetime = Math.max(0., value);
                else if (key.equals("random lifetime"))
                    randomLifetime = Math.max(0., value);
                else if (key.equals("reload"))
                    reload = Math.max(1., value);
                else if (key.equals("burst reload"))
                    burstReload = Math.max(1., value);
                else if (key.equals("burst count"))
                    burstCount = Math.max(1., value);
                else if (key.equals("homing"))
                    homing = value;
                else if (key.equals("missile strength"))
                    missileStrength = Math.max(0., value);
                else if (key.equals("anti-missile"))
                    antiMissile = Math.max(0., value);
                else if (key.equals("velocity"))
                    velocity = value;
                else if (key.equals("random velocity"))
                    randomVelocity = value;
                else if (key.equals("acceleration"))
                    acceleration = value;
                else if (key.equals("drag"))
                    drag = value;
                else if (key.equals("hardpoint offset"))
                    hardpointOffset = value;
                else if (key.equals("turn"))
                    turn = value;
                else if (key.equals("inaccuracy"))
                    inaccuracy = value;
                else if (key.equals("turret turn"))
                    turretTurn = value;
                else if (key.equals("tracking"))
                    tracking = Math.max(0., Math.min(1., value));
                else if (key.equals("optical tracking"))
                    opticalTracking = Math.max(0., Math.min(1., value));
                else if (key.equals("infrared tracking"))
                    infraredTracking = Math.max(0., Math.min(1., value));
                else if (key.equals("radar tracking"))
                    radarTracking = Math.max(0., Math.min(1., value));
                else if (key.equals("firing energy"))
                    firingEnergy = value;
                else if (key.equals("firing force"))
                    firingForce = value;
                else if (key.equals("firing fuel"))
                    firingFuel = value;
                else if (key.equals("firing heat"))
                    firingHeat = value;
                else if (key.equals("split range"))
                    splitRange = value;
                else if (key.equals("trigger radius"))
                    triggerRadius = value;
                else if (key.equals("blast radius"))
                    blastRadius = value;
                else if (key.equals("shield damage"))
                    damage[SHIELD_DAMAGE] = value;
                else if (key.equals("hull damage"))
                    damage[HULL_DAMAGE] = value;
                else if (key.equals("fuel damage"))
                    damage[FUEL_DAMAGE] = value;
                else if (key.equals("heat damage"))
                    damage[HEAT_DAMAGE] = value;
                else if (key.equals("ion damage"))
                    damage[ION_DAMAGE] = value;
                else if (key.equals("disruption damage"))
                    damage[DISRUPTION_DAMAGE] = value;
                else if (key.equals("slowing damage"))
                    damage[SLOWING_DAMAGE] = value;
                else if (key.equals("hit force"))
                    hitForce = value;
                else if (key.equals("piercing"))
                    piercing = Math.max(0., Math.min(1., value));
                else
                    child.printTrace("Unrecognized weapon attribute: \"" + key + "\":");
            }
        }
        // Sanity check:
        if (burstReload > reload)
            burstReload = reload;

        // Weapons of the same type will alternate firing (streaming) rather than
        // firing all at once (clustering) if the weapon is not an anti-missile and
        // is not vulnerable to anti-missile, or has the "stream" attribute.
        stream |= !(missileStrength != 0 || antiMissile != 0);
        stream &= !isClustered;

        // Support legacy missiles with no tracking type defined:
        if (homing == 0 && tracking == 0 && opticalTracking == 0 && infraredTracking == 0 && radarTracking == 0)
            tracking = 1.;

        //todo: effects
        // Convert the "live effect" counts from occurrences per projectile lifetime
        // into chance of occurring per frame.
        /*if(lifetime <= 0)
            liveEffects.clear();
        for(auto it = liveEffects.begin(); it != liveEffects.end(); )
        {
            if(!it->second)
                it = liveEffects.erase(it);
            else
            {
                it->second = Math.max(1, lifetime / it->second);
                ++it;
            }
        }*/
    }

    public static final int DAMAGE_TYPES = 7;
    public static final int SHIELD_DAMAGE = 0;
    public static final int HULL_DAMAGE = 1;
    public static final int FUEL_DAMAGE = 2;
    public static final int HEAT_DAMAGE = 3;
    public static final int ION_DAMAGE = 4;
    public static final int DISRUPTION_DAMAGE = 5;
    public static final int SLOWING_DAMAGE = 6;
    private double[] damage = new double[DAMAGE_TYPES];

    public double getReload() {
        return reload;
    }

    public double getFiringEnergy() {
        return firingEnergy;
    }

    public double getAntiMissile() {
        return antiMissile;
    }

    public double getFiringFuel() {
        return firingFuel;
    }

    public double getPiercing() {
        return piercing;
    }

    public double[] getDamage() {
        return damage;
    }

    public double getFiringHeat() {
        return firingHeat;
    }
}
