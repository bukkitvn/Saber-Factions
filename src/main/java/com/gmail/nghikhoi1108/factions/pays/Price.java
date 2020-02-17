package com.gmail.nghikhoi1108.factions.pays;

import com.gmail.nghikhoi1108.factions.utils.NumberUtil;
import org.bukkit.entity.Player;

public class Price {

    private PayType type;
    private int amount;

    public Price(PayType type, int amount) {
        this.type = type;
        this.amount = amount;
    }

    public boolean deal(Player p) {
        return type.take(p, amount);
    }

    public static final Price FREE = new Price(null, 0) {
        public boolean deal(Player p) {
            return true;
        }
    };

    public static Price fromString(String source) {
        if (!source.contains(":")) return new Price(PayType.VAULT, NumberUtil.castToInteger(source).get());
        String[] split = source.split(":");
        return new Price(PayType.valueOf(split[0].toUpperCase()), NumberUtil.castToInteger(split[1]).get());
    }

}
