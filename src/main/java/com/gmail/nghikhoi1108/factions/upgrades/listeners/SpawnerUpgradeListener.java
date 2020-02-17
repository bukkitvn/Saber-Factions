package com.gmail.nghikhoi1108.factions.upgrades.listeners;

import com.gmail.nghikhoi1108.factions.upgrades.UpgradeType;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;

public class SpawnerUpgradeListener implements Listener {

    @EventHandler
    public void onSpawn(SpawnerSpawnEvent e) {
        FLocation floc = new FLocation(e.getLocation());
        Faction factionAtLoc = Board.getInstance().getFactionAt(floc);
        if (!factionAtLoc.isWilderness()) {
            int level = UpgradeType.SPAWNER.getCurrentLevel(factionAtLoc);
            if (level == 0) {
                return;
            }
            this.lowerSpawnerDelay(e, (double) UpgradeType.SPAWNER.getTracker().getValue((byte) level));
        }
    }

    private void lowerSpawnerDelay(SpawnerSpawnEvent e, double multiplier) {
        int lowerby = (int) Math.round(e.getSpawner().getDelay() * multiplier);
        e.getSpawner().setDelay(e.getSpawner().getDelay() - lowerby);
    }

}
