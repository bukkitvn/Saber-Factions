package com.gmail.nghikhoi1108.factions;

import com.gmail.nghikhoi1108.factions.pays.PayType;
import com.gmail.nghikhoi1108.factions.upgrades.UpgradeHandler;
import com.massivecraft.factions.FactionsPlugin;

public class FactionAddon {

    private static FactionAddon instance = new FactionAddon();
    private FactionsPlugin plugin;

    public void onEnable(FactionsPlugin plugin) {
        this.plugin = plugin;
        dependCheck("PlayerPoints");

        PayType.init(this.getPlugin());
        UpgradeHandler.init();
    }

    public boolean dependCheck(String plugin) {
        return getPlugin().getServer().getPluginManager().isPluginEnabled(plugin);
    }

    public FactionsPlugin getPlugin() {
        return plugin;
    }

    public static FactionAddon getInstance() {
        return instance;
    }

}
