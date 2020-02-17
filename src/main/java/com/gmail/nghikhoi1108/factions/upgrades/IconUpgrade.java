package com.gmail.nghikhoi1108.factions.upgrades;

import com.gmail.plasmavn.menubuilder.api.common.Icon;
import com.massivecraft.factions.Faction;

public interface IconUpgrade<V> extends Upgrade<V> {

    Icon getIconExcutor(Faction faction);

    int getSlot();

}
