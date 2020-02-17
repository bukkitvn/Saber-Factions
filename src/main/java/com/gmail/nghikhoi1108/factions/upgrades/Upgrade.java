package com.gmail.nghikhoi1108.factions.upgrades;

import com.gmail.nghikhoi1108.factions.upgrades.upgradetrackers.UpgradeTracker;
import com.massivecraft.factions.Faction;

public interface Upgrade<V> {

    int getMaxLevel();

    String getName();

    String getId();

    void upgrade(Faction faction);

    int getCurrentLevel(Faction faction);

    UpgradeTracker<V> getTracker();

    boolean isEnabled();

}
