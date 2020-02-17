package com.gmail.nghikhoi1108.factions.upgrades.upgradetrackers;

import com.gmail.nghikhoi1108.factions.pays.Price;
import com.gmail.nghikhoi1108.factions.upgrades.UpgradeType;
import com.gmail.nghikhoi1108.factions.utils.NumberUtil;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Optional;
import java.util.TreeMap;
import java.util.function.BiFunction;

public class ImmutableUpgradeTracker<V> implements UpgradeTracker<V> {

    private final ImmutableMap<Integer, Pair<V>> tracker;

    private ImmutableUpgradeTracker(ImmutableMap<Integer, Pair<V>> tracker) {
        this.tracker = tracker;
    }

    @Override
    public V nextLevelValue(int level) {
        return getValue((byte) (level + 1));
    }

    @Override
    public Price nextLevelPay(int level) {
        return getPay((byte) (level + 1));
    }

    @Override
    public V getValue(int level) {
        return level > getSize() || level < 1 ? null : tracker.get(level).getValue();
    }

    @Override
    public Price getPay(int level) {
        return level > getSize() || level < 1 ? null : tracker.get(level).getPrice();
    }

    @Override
    public int getSize() {
        return tracker.size();
    }

    public static <V> Builder<V> builder() {
        return new Builder<V>();
    }

    private static <V> Builder<V> builderFromConfig(ConfigurationSection section, BiFunction<ConfigurationSection, String, V> getter) {
        Builder<V> builder = builder();
        for (String key : section.getKeys(false)) {
            Optional<Integer> level = NumberUtil.castToInteger(key);
            if (section.isConfigurationSection(key) && level.isPresent()) {
                ConfigurationSection level_section = section.getConfigurationSection(key);
                //V value = section.get(UpgradeType.VALUE_KEY);
                V value = getter.apply(level_section, UpgradeType.VALUE_KEY);
                if (value != null) {
                    String price_source = level_section.getString(UpgradeType.PRICE_KEY);
                    builder.put(level.get().byteValue()
                            , (V) value
                            , Price.fromString(price_source));
                }
            }
        }
        return builder;
    }

    public static <V> Builder<V> builderFromConfig(ConfigurationSection section) {
        return builderFromConfig(section, (level_section, key) -> (V) level_section.get(key));
    }

    private static final Object DUMP_OBJECT = new Object();

    public static Builder<Object> emptyBuilderFromConfig(ConfigurationSection section) {
        return builderFromConfig(section, (level_section, key) -> DUMP_OBJECT);
    }

    public static Builder<Integer> integerBuilderFromConfig(ConfigurationSection section) {
        return builderFromConfig(section, ConfigurationSection::getInt);
    }

    public static Builder<String> stringBuilderFromConfig(ConfigurationSection section) {
        return builderFromConfig(section, ConfigurationSection::getString);
    }

    public static Builder<Double> doubleBuilderFromConfig(ConfigurationSection section) {
        return builderFromConfig(section, ConfigurationSection::getDouble);
    }

    private static class Pair<T> {

        private final Price price;
        private final T value;

        private Pair(T value, Price price) {
            this.price = price;
            this.value = value;
        }

        public T getValue() {
            return value;
        }

        public Price getPrice() {
            return price;
        }

        public int hashCode() {
            return value.hashCode() * price.hashCode();
        }

        public String toString() {
            return price.toString() + "-" + value.toString();
        }

    }

    public static class Builder<T> {

        private TreeMap<Integer, Pair<T>> resource = Maps.newTreeMap();

        Builder() {}

        public void put(int level, T value, Price price) {
            resource.put(level, new Pair(value, price));
        }

        public void remove(int level) {
            resource.remove(level);
        }

        public ImmutableUpgradeTracker<T> build() {
            return new ImmutableUpgradeTracker<T>(ImmutableMap.copyOf(resource));
        }

    }

}
