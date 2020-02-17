package com.gmail.nghikhoi1108.factions.pays;

import com.gmail.nghikhoi1108.factions.utils.PlayerUtil;
import com.massivecraft.factions.integration.Econ;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

public enum PayType {
	
	VAULT() {

		public boolean take(Player p, int amount) {
			return vault_take(p, amount);
		}
		
	},
	POINTS() {

		public boolean take(Player p, int amount) {
			return points_take(p, amount);
		}
		
	},
	EXP() {

		public boolean take(Player p, int amount) {
			return xp_take(p, amount);
		}
		
	};
	
	private static PlayerPointsAPI pointsapi;
	
	private static Economy vault;

	public abstract boolean take(Player p, int amount);
	
	public static void init(Plugin plugin) {
		PluginManager plugman = plugin.getServer().getPluginManager();
		try {
			pointsapi = plugman.isPluginEnabled("PlayerPoints")
					? PlayerPoints.class.cast(plugman.getPlugin("PlayerPoints")).getAPI() : null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
		//if (vault == null) throw new NullPointerException("Vault not found.");
	}
	
	public static boolean points_take(Player p, int var) {
		return pointsapi.take(p.getUniqueId(), var);
	}
	
	public static boolean vault_take(Player p, int var) {
		return Econ.getEcon().withdrawPlayer(p, var).transactionSuccess();
	}
	
	public static boolean xp_take(Player p, int var) {
		int exp = PlayerUtil.getTotalExperience(p);
		if (exp < var) return false;
		PlayerUtil.setTotalExperience(p, exp - var);
		return true;
	}
	
}
