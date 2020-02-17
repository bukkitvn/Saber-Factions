package com.gmail.nghikhoi1108.factions.upgrades.listeners;

import com.gmail.nghikhoi1108.factions.upgrades.UpgradeType;
import com.massivecraft.factions.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageDesUpgradeListener implements Listener {

    @EventHandler
    public static void onDamageReduction(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (!(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player)) {
            return;
        }
        FPlayer fme = FPlayers.getInstance().getByPlayer((Player) e.getEntity());
        FPlayer dame = FPlayers.getInstance().getByPlayer((Player) e.getDamager());
        if (fme == null || dame == null) {
            return;
        }
        FLocation floc = new FLocation(fme.getPlayer().getLocation());
        if (Board.getInstance().getFactionAt(floc) == fme.getFaction()) {
            if (dame.getFaction() == fme.getFaction()) {
                return;
            }
            double damage = e.getDamage();
            int level = UpgradeType.DAMAGEDECREASE.getCurrentLevel(fme.getFaction());
            double increase = (double) UpgradeType.DAMAGEDECREASE.getTracker().getValue((byte) level);
            e.setDamage(damage - damage / 100.0 * increase);
        }
    }

}
