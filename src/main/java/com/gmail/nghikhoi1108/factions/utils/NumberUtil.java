package com.gmail.nghikhoi1108.factions.utils;

import java.util.Optional;

public class NumberUtil {

    public static Optional<Integer> castToInteger(String source) {
        Integer res = null;
        try {
            res = Integer.parseInt(source);
        } catch (NumberFormatException e) { }
        return Optional.ofNullable(res);
    }

}
