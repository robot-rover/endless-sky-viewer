package rr.industries;

import rr.industries.parser.Tag;

public class Attributes {
    @Tag("cost")
    public double cost = 0;
    @Tag("mass")
    public double mass = 0;
    @Tag("outfit space")
    public double outfitSpace = 0;
    @Tag("weapon capacity")
    public double weaponCapacity = 0;
    @Tag("gun ports")
    public double gunPorts = 0;
    @Tag("required crew")
    public double requiredCrew = 0;
    @Tag("turret mounts")
    public double turretMounts = 0;
    @Tag("energy capacity")
    public double energyCapacity = 0;
    @Tag("energy consumption")
    public double energyConsumption = 0;
    @Tag("heat generation")
    public double heatGeneration = 0;
    @Tag("energy generation")
    public double energyGeneration = 0;
    @Tag("active cooling")
    public double activeCooling = 0;
    @Tag("cooling energy")
    public double coolingEnergy = 0;
    @Tag("ramscoop")
    public double ramscoop = 0;
    @Tag("quantum keystone")
    public double quantumKeystone = 0;
    @Tag("engine capacity")
    public double engineCapacity = 0;
    @Tag("thrust")
    public double thrust = 0;
    @Tag("thrusting energy")
    public double thrustingEnergy = 0;
    @Tag("thrusting heat")
    public double thrustingHeat = 0;
    @Tag("turn")
    public double turn = 0;
    @Tag("turning energy")
    public double turningEnergy = 0;
    @Tag("turning heat")
    public double turningHeat = 0;
    @Tag("railgun slug capacity")
    public double railgunSlugCapacity = 0;
    @Tag("tracker capacity")
    public double trackerCapacity = 0;
    @Tag("shield generation")
    public double shieldGeneration = 0;
    @Tag("shield energy")
    public double shieldEnergy = 0;
    @Tag("shield heat")
    public double shieldHeat = 0;
    @Tag("cooling")
    public double cooling = 0;
    @Tag("capture attack")
    public double captureAttack = 0;
    @Tag("capture defence")
    public double captureDefence = 0;
    @Tag("unplunderable")
    public double unplunderable = 0;
    @Tag("cargo space")
    public double cargoSpace = 0;
    @Tag("reverse thrust")
    public double reverseThrust = 0;
    @Tag("reverse thrusting energy")
    public double reverseThrustingEnergy = 0;
    @Tag("reverse thrusting heat")
    public double reverseThrustingHeat = 0;
    @Tag("finisher capacity")
    public double finisherCapacity = 0;
    @Tag("inscrutable")
    public double inscrutable = 0;
    @Tag("solar collection")
    public double solarCollection = 0;
    @Tag("hull repair rate")
    public double hullRepairRate = 0;
    @Tag("hull energy")
    public double hullEnergy = 0;
    @Tag("hull heat")
    public double hullHeat = 0;
    @Tag("meteor capacity")
    public double meteorCapacity = 0;
    @Tag("sidewinderCapacity")
    public double sidewinderCapacity = 0;
    @Tag("javelin capacity")
    public double javelinCapacity = 0;
    @Tag("torpedo capacity")
    public double torpedoCapacity = 0;
    @Tag("typhoon capacity")
    public double typhoonCapacity = 0;
    @Tag("rocket capacity")
    public double rocketCapacity = 0;
    @Tag("gatling round capacity")
    public double gatlingRoundCapacity = 0;
    @Tag("radar jamming")
    public double radarJamming = 0;
    @Tag("jump speed")
    public double jumpSpeed = 0;
    @Tag("jump fuel")
    public double jumpFuel = 0;
    @Tag("hyperdrive")
    public double hyperdrive = 0;
    @Tag("scram drive")
    public double scramDrive = 0;
    @Tag("jump drive")
    public double jumpDrive = 0;
    @Tag("cargo scan power")
    public double cargoScanPower = 0;
    @Tag("cargo scan speed")
    public double cargoScanSpeed = 0;
    @Tag("outfit scan power")
    public double outfitScanPower = 0;
    @Tag("outfit scan speed")
    public double outfitScanSpeed = 0;
    @Tag("asteroid scan power")
    public double asteroidScanPower = 0;
    @Tag("tactical scan power")
    public double tacticalScanPower = 0;
    @Tag("atmosphere scan")
    public double atmosphereScan = 0;
    @Tag("cloak")
    public double cloak = 0;
    @Tag("cloaking energy")
    public double cloakingEnergy = 0;
    @Tag("cloak fuel")
    public double cloakFuel = 0;
    @Tag("cooling inefficiency")
    public double coolingInefficiency = 0;
    @Tag("heat dissipation")
    public double heatDissipation = 0;
    @Tag("bunks")
    public double bunks = 0;
    @Tag("fuel capacity")
    public double fuelCapacity = 0;
    @Tag("scan interference")
    public double scanInterference = 0;
    @Tag("piercer capacity")
    public double piercerCapacity = 0;
    @Tag("minelayer capacity")
    public double minelayerCapacity = 0;
    @Tag("thunderhead capacity")
    public double thunderheadCapacity = 0;
    @Tag("automaton")
    public double automaton = 0;
    @Tag("afterburner thrust")
    public double afterburnerThrust = 0;
    @Tag("afterburner fuel")
    public double afterburnerFuel = 0;
    @Tag("afterburner heat")
    public double afterburnerHeat = 0;
    @Tag("afterburner energy")
    public double afterburnerEnergy = 0;

    public Attributes add(Attributes other) {
        Attributes newAttributes = new Attributes();
        newAttributes.cost = this.cost + other.cost;
        newAttributes.mass = this.mass + other.mass;
        newAttributes.outfitSpace = this.outfitSpace + other.outfitSpace;
        newAttributes.weaponCapacity = this.weaponCapacity + other.weaponCapacity;
        newAttributes.gunPorts = this.gunPorts + other.gunPorts;
        newAttributes.requiredCrew = this.requiredCrew + other.requiredCrew;
        newAttributes.turretMounts = this.turretMounts + other.turretMounts;
        newAttributes.energyCapacity = this.energyCapacity + other.energyCapacity;
        newAttributes.energyConsumption = this.energyConsumption + other.energyConsumption;
        newAttributes.heatGeneration = this.heatGeneration + other.heatGeneration;
        newAttributes.energyGeneration = this.energyGeneration + other.energyGeneration;
        newAttributes.activeCooling = this.activeCooling + other.activeCooling;
        newAttributes.coolingEnergy = this.coolingEnergy + other.coolingEnergy;
        newAttributes.ramscoop = this.ramscoop + other.ramscoop;
        newAttributes.quantumKeystone = this.quantumKeystone + other.quantumKeystone;
        newAttributes.engineCapacity = this.engineCapacity + other.engineCapacity;
        newAttributes.thrust = this.thrust + other.thrust;
        newAttributes.thrustingEnergy = this.thrustingEnergy + other.thrustingEnergy;
        newAttributes.thrustingHeat = this.thrustingHeat + other.thrustingHeat;
        newAttributes.turn = this.turn + other.turn;
        newAttributes.turningEnergy = this.turningEnergy + other.turningEnergy;
        newAttributes.turningHeat = this.turningHeat + other.turningHeat;
        newAttributes.railgunSlugCapacity = this.railgunSlugCapacity + other.railgunSlugCapacity;
        newAttributes.trackerCapacity = this.trackerCapacity + other.trackerCapacity;
        newAttributes.shieldGeneration = this.shieldGeneration + other.shieldGeneration;
        newAttributes.shieldEnergy = this.shieldEnergy + other.shieldEnergy;
        newAttributes.shieldHeat = this.shieldHeat + other.shieldHeat;
        newAttributes.cooling = this.cooling + other.cooling;
        newAttributes.captureAttack = this.captureAttack + other.captureAttack;
        newAttributes.captureDefence = this.captureDefence + other.captureDefence;
        newAttributes.unplunderable = this.unplunderable + other.unplunderable;
        newAttributes.cargoSpace = this.cargoSpace + other.cargoSpace;
        newAttributes.reverseThrust = this.reverseThrust + other.reverseThrust;
        newAttributes.reverseThrustingEnergy = this.reverseThrustingEnergy + other.reverseThrustingEnergy;
        newAttributes.reverseThrustingHeat = this.reverseThrustingHeat + other.reverseThrustingHeat;
        newAttributes.finisherCapacity = this.finisherCapacity + other.finisherCapacity;
        newAttributes.inscrutable = this.inscrutable + other.inscrutable;
        newAttributes.solarCollection = this.solarCollection + other.solarCollection;
        newAttributes.hullRepairRate = this.hullRepairRate + other.hullRepairRate;
        newAttributes.hullEnergy = this.hullEnergy + other.hullEnergy;
        newAttributes.hullHeat = this.hullHeat + other.hullHeat;
        newAttributes.meteorCapacity = this.meteorCapacity + other.meteorCapacity;
        newAttributes.sidewinderCapacity = this.sidewinderCapacity + other.sidewinderCapacity;
        newAttributes.javelinCapacity = this.javelinCapacity + other.javelinCapacity;
        newAttributes.torpedoCapacity = this.torpedoCapacity + other.torpedoCapacity;
        newAttributes.typhoonCapacity = this.typhoonCapacity + other.typhoonCapacity;
        newAttributes.rocketCapacity = this.rocketCapacity + other.rocketCapacity;
        newAttributes.gatlingRoundCapacity = this.gatlingRoundCapacity + other.gatlingRoundCapacity;
        newAttributes.radarJamming = this.radarJamming + other.radarJamming;
        newAttributes.jumpSpeed = this.jumpSpeed + other.jumpSpeed;
        newAttributes.jumpFuel = this.jumpFuel + other.jumpFuel;
        newAttributes.hyperdrive = this.hyperdrive + other.hyperdrive;
        newAttributes.scramDrive = this.scramDrive + other.scramDrive;
        newAttributes.jumpDrive = this.jumpDrive + other.jumpDrive;
        newAttributes.cargoScanPower = this.cargoScanPower + other.cargoScanPower;
        newAttributes.cargoScanSpeed = this.cargoScanSpeed + other.cargoScanSpeed;
        newAttributes.outfitScanPower = this.outfitScanPower + other.outfitScanPower;
        newAttributes.outfitScanSpeed = this.outfitScanSpeed + other.outfitScanSpeed;
        newAttributes.asteroidScanPower = this.asteroidScanPower + other.asteroidScanPower;
        newAttributes.tacticalScanPower = this.tacticalScanPower + other.tacticalScanPower;
        newAttributes.atmosphereScan = this.atmosphereScan + other.atmosphereScan;
        newAttributes.cloak = this.cloak + other.cloak;
        newAttributes.cloakingEnergy = this.cloakingEnergy + other.cloakingEnergy;
        newAttributes.cloakFuel = this.cloakFuel + other.cloakFuel;
        newAttributes.coolingInefficiency = this.coolingInefficiency + other.coolingInefficiency;
        newAttributes.heatDissipation = this.heatDissipation + other.heatDissipation;
        newAttributes.bunks = this.bunks + other.bunks;
        newAttributes.fuelCapacity = this.fuelCapacity + other.fuelCapacity;
        newAttributes.scanInterference = this.scanInterference + other.scanInterference;
        newAttributes.piercerCapacity = this.piercerCapacity + other.piercerCapacity;
        newAttributes.minelayerCapacity = this.minelayerCapacity + other.minelayerCapacity;
        newAttributes.thunderheadCapacity = this.thunderheadCapacity + other.thunderheadCapacity;
        newAttributes.automaton = this.automaton + other.automaton;
        newAttributes.afterburnerThrust = this.afterburnerThrust + other.afterburnerThrust;
        newAttributes.afterburnerFuel = this.afterburnerFuel + other.afterburnerFuel;
        newAttributes.afterburnerHeat = this.afterburnerHeat + other.afterburnerHeat;
        newAttributes.afterburnerEnergy = this.afterburnerEnergy + other.afterburnerEnergy;
        return newAttributes;
    }
}
