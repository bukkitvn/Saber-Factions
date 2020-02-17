package com.gmail.nghikhoi1108.factions.upgrades.listeners;

import com.gmail.nghikhoi1108.factions.upgrades.UpgradeType;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.util.XMaterial;
import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.material.Crops;

import java.util.concurrent.ThreadLocalRandom;

public class CropUpgradeListener implements Listener {

    @EventHandler
    public void onCropGrow(BlockGrowEvent e) {
        FLocation floc = new FLocation(e.getBlock().getLocation());
        Faction factionAtLoc = Board.getInstance().getFactionAt(floc);
        if (!factionAtLoc.isWilderness()) {
            int level = UpgradeType.CROP.getCurrentLevel(factionAtLoc);
            int chance = (int) UpgradeType.CROP.getTracker().getValue((byte) level);
            if (level == 0 || chance == 0) {
                return;
            }
            int randomNum = ThreadLocalRandom.current().nextInt(1, 101);
            if (randomNum <= chance) {
                this.growCrop(e);
            }
        }
    }

    private void growCrop(BlockGrowEvent e) {
        if (e.getBlock().getType().equals(XMaterial.WHEAT.parseMaterial())) {
            e.setCancelled(true);
            Crops c = new Crops(CropState.RIPE);
            BlockState bs = e.getBlock().getState();
            bs.setData(c);
            bs.update();
        }
        Block below = e.getBlock().getLocation().subtract(0.0, 1.0, 0.0).getBlock();
        if (below.getType() == XMaterial.SUGAR_CANE.parseMaterial()) {
            Block above = e.getBlock().getLocation().add(0.0, 1.0, 0.0).getBlock();
            if (above.getType() == Material.AIR && above.getLocation().add(0.0, -2.0, 0.0).getBlock().getType() != Material.AIR) {
                above.setType(XMaterial.SUGAR_CANE.parseMaterial());
            }
        }
        else if (below.getType() == Material.CACTUS) {
            Block above = e.getBlock().getLocation().add(0.0, 1.0, 0.0).getBlock();
            if (above.getType() == Material.AIR && above.getLocation().add(0.0, -2.0, 0.0).getBlock().getType() != Material.AIR) {
                above.setType(Material.CACTUS);
            }
        }
    }

}
