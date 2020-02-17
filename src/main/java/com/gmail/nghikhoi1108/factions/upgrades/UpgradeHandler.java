package com.gmail.nghikhoi1108.factions.upgrades;

import com.gmail.plasmavn.menubuilder.api.MenuBuilder;
import com.gmail.plasmavn.menubuilder.api.common.Icon;
import com.gmail.plasmavn.menubuilder.api.common.Row;
import com.gmail.plasmavn.menubuilder.api.interfaces.ToggleExcuter;
import com.google.common.collect.Maps;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.util.XMaterial;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class UpgradeHandler {

    private static final Map<String, Inventory> OPENING = Maps.newConcurrentMap();

    public static void init() {
        UpgradeType.init(FactionsPlugin.getInstance().getConfig().getConfigurationSection("fupgrades.MainMenu"));
    }

    public static void open(FPlayer... ps) {
        Faction faction = ps[0].getFaction();
        if (OPENING.containsKey(faction.getTag())) {
            for (FPlayer p : ps)
                p.getPlayer().openInventory(OPENING.get(faction.getTag()));
        }
        MenuBuilder builder = new MenuBuilder(FactionsPlugin.getInstance().getConfig().getString("fupgrades.MainMenu.Title").replace("{faction}", faction.getTag()), Row.SIX);
        for (int i = 0; i < Row.SIX.getSlot(); i++) {
            builder.setIcon(i, getDummyItem(), true);
        }
        for (UpgradeType type : UpgradeType.values()) {
            Icon icon = type.getIconExcutor(faction);
            icon.setCancelled(true);
            builder.setIcon(type.getSlot(), icon);
        }
        builder.setToggleExcuter(new ToggleExcuter() {

            @Override
            public void excute(InventoryCloseEvent e) {
                if (e.getViewers().size() == 1 && e.getPlayer().getName().equals(e.getViewers().get(0).getName())) {
                    OPENING.remove(FPlayers.getInstance().getByPlayer((Player) e.getPlayer()).getFaction().getTag());
                }
            }

            @Override
            public void excute(InventoryOpenEvent e) {
            }

        });
        OPENING.put(faction.getTag(), builder.getInventory());
        for (FPlayer p : ps)
            builder.show(p.getPlayer());
    }

    private static ItemStack getDummyItem() {
        ConfigurationSection config = FactionsPlugin.getInstance().getConfig().getConfigurationSection("fupgrades.MainMenu.DummyItem");
        ItemStack item = XMaterial.matchXMaterial(config.getString("Type")).get().parseItem();
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setLore(FactionsPlugin.getInstance().colorList(config.getStringList("Lore")));
            meta.setDisplayName(FactionsPlugin.getInstance().color(config.getString("Name")));
            item.setItemMeta(meta);
        }
        return item;
    }

}
