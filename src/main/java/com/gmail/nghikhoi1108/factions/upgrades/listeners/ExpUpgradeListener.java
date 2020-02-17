package com.gmail.nghikhoi1108.factions.upgrades.listeners;

import com.gmail.nghikhoi1108.factions.upgrades.UpgradeType;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class ExpUpgradeListener implements Listener {

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        Entity killer = e.getEntity().getKiller();
        if (!(killer instanceof Player)) {
            return;
        }
        FLocation floc = new FLocation(e.getEntity().getLocation());
        Faction faction = Board.getInstance().getFactionAt(floc);
        if (!faction.isWilderness()) {
            int level = UpgradeType.EXP.getCurrentLevel(faction);
            if (level == 0) return;
            double multiplier = (double) UpgradeType.EXP.getTracker().getValue((byte) level);
            if (multiplier > 0.0) {
                this.spawnMoreExp(e, multiplier);
            }
        }
    }

    private void spawnMoreExp(EntityDeathEvent e, double multiplier) {
        double newExp = e.getDroppedExp() * multiplier;
        e.setDroppedExp((int) newExp);
    }

}
