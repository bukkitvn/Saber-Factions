package com.massivecraft.factions.util;

/**
 * @author Saser
 */
import com.massivecraft.factions.util.XMaterial;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemUtil {
    private static Map<String, ItemStack> cachedSkulls = new HashMap<>();

    public ItemUtil() {
    }

    public static int getItemCount(Inventory inventory) {
        if (inventory == null) {
            return 0;
        } else {
            int itemsFound = 0;

            for(int i = 0; i < inventory.getSize(); ++i) {
                ItemStack item = inventory.getItem(i);
                if (item != null && item.getType() != Material.AIR) {
                    ++itemsFound;
                }
            }

            return itemsFound;
        }
    }

    public static ItemStack createPlayerHead(String name) {
        ItemStack skull = cachedSkulls.get(name);
        if (skull != null) {
            return skull.clone();
        } else {
            skull = new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial());
            SkullMeta sm = (SkullMeta)skull.getItemMeta();
            sm.setOwner(name);
            skull.setItemMeta(sm);
            cachedSkulls.put(name, skull.clone());
            return skull;
        }
    }

    public static ItemStack basicDeserialize(ConfigurationSection section) {
        if (section == null || !section.isString("type")) return null;
        Material material = Material.matchMaterial(section.getString("type").toUpperCase());
        if (material == null || material == Material.AIR) return null;
        int amount = section.getInt("amount", 1);
        short data = (short) section.getInt("data", 0);
        ItemStack res = new ItemStack(material, amount, data);
        ItemMeta meta = res.getItemMeta();
        if (section.isString("name")) {
            meta.setDisplayName(StringUtil.color(section.getString("name")));
        }
        if (section.isList("lore")) {
            meta.setLore(StringUtil.color(section.getStringList("lore")));
        }
        if (section.getBoolean("glow", false)) {
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        res.setItemMeta(meta);
        return res;
    }

    public static ItemStack applyHolder(ItemStack item, Placeholder... placeholders) {
        if (!item.hasItemMeta()) return item;
        ItemMeta meta = item.getItemMeta();
        if (meta.hasDisplayName()) {
            String name = meta.getDisplayName();
            for (Placeholder holder : placeholders) {
                name.replace(holder.getTag(), holder.getReplace());
            }
            meta.setDisplayName(name);
        }
        if (meta.hasLore()) {
            List<String> lore = meta.getLore();
            for (int i = 0; i < lore.size(); i++) {
                String l = lore.get(i);
                for (Placeholder holder : placeholders) {
                    l.replace(holder.getTag(), holder.getReplace());
                }
                lore.set(i, l);
            }
            meta.setLore(lore);
        }
        item.setItemMeta(meta);
        return item;
    }

}
