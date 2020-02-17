package com.massivecraft.factions.util;

import java.util.ArrayList;
import java.util.List;

public class StringUtil {

    public static char COLOR_SYMBOL = '§';

    public static String color(String source) {
        return source.replace('&', COLOR_SYMBOL);
    }

    public static List<String> color(List<String> source) {
        List<String> res = new ArrayList<>(source.size());
        source.forEach((lore) -> res.add(color(lore)));
        return res;
    }

}
