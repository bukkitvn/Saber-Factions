package com.gmail.nghikhoi1108.factions.upgrades.upgradetrackers;

import com.gmail.nghikhoi1108.factions.pays.Price;
import com.gmail.nghikhoi1108.factions.upgrades.UpgradeType;
import org.bukkit.configuration.ConfigurationSection;

public class BooleanUpgradeTracker implements UpgradeTracker<Boolean> {

    private Price price;

    private BooleanUpgradeTracker(Price price) {
        this.price = price;
    }

    @Override
    public Boolean nextLevelValue(int level) {
        return getValue((byte) (level + 1));
    }

    @Override
    public Price nextLevelPay(int level) {
        return getPay((byte) (level + 1));
    }

    @Override
    public Boolean getValue(int level) {
        return level == 1;
    }

    @Override
    public Price getPay(int level) {
        return level == 1 ? this.price : Price.FREE;
    }

    @Override
    public int getSize() {
        return 1;
    }

    public static BooleanUpgradeTracker newTracker(Price price) {
        return new BooleanUpgradeTracker(price);
    }

    public static BooleanUpgradeTracker newTrackerFromConfig(ConfigurationSection section) {
        if (section.isConfigurationSection("1")) {
            ConfigurationSection level_section = section.getConfigurationSection("1");
            if (level_section.isString(UpgradeType.PRICE_KEY)) {
                return newTracker(Price.fromString(level_section.getString(UpgradeType.PRICE_KEY)));
            }
        }
        return null;
    }

}
