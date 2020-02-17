package com.gmail.nghikhoi1108.factions.upgrades.upgradetrackers;

import com.gmail.nghikhoi1108.factions.pays.Price;

public interface UpgradeTracker<V> {

    V nextLevelValue(int level);

    Price nextLevelPay(int level);

    V getValue(int level);

    Price getPay(int level);

    int getSize();

}
