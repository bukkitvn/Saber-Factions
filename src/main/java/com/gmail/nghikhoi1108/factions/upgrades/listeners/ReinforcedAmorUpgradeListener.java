package com.gmail.nghikhoi1108.factions.upgrades.listeners;

import com.gmail.nghikhoi1108.factions.upgrades.UpgradeType;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.FactionsPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class ReinforcedAmorUpgradeListener implements Listener {

    @EventHandler
    public void onArmorDamage(PlayerItemDamageEvent e) {
        FPlayer fplayer = FPlayers.getInstance().getByPlayer(e.getPlayer());
        if (fplayer == null) {
            return;
        }
        if (e.getItem().getType().toString().contains("LEGGINGS")
                || e.getItem().getType().toString().contains("CHESTPLATE")
                || e.getItem().getType().toString().contains("HELMET")
                || e.getItem().getType().toString().contains("BOOTS")) {
            int lvl = UpgradeType.REINFORCEDARMOR.getCurrentLevel(fplayer.getFaction());
            double drop = (double) UpgradeType.REINFORCEDARMOR.getTracker().getValue((byte) lvl);
            int newDamage = (int) Math.round(e.getDamage() - e.getDamage() * drop);
            e.setDamage(newDamage);
        }
    }

}
