package com.gmail.nghikhoi1108.factions.upgrades;

import com.gmail.nghikhoi1108.factions.upgrades.listeners.*;
import com.gmail.nghikhoi1108.factions.upgrades.upgradetrackers.ImmutableUpgradeTracker;
import com.gmail.nghikhoi1108.factions.upgrades.upgradetrackers.UpgradeTracker;
import com.gmail.plasmavn.menubuilder.api.common.Icon;
import com.gmail.plasmavn.menubuilder.api.interfaces.Excuter;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.util.ItemUtil;
import com.massivecraft.factions.util.Placeholder;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public enum UpgradeType implements IconUpgrade {

    CHEST("Chest", (byte) 3) {

        public void upgrade(Faction faction) {
            super.upgrade(faction);
            String invName = FactionsPlugin.getInstance().color(FactionsPlugin.getInstance().getConfig().getString("fchest.Inventory-Title"));
            for (Player player : faction.getOnlinePlayers()) {
                if (player.getOpenInventory().getTitle().equalsIgnoreCase(invName)) player.closeInventory();
            }
            faction.setChestSize((int) getTracker().getValue((byte) this.getCurrentLevel(faction)) * 9);
        }

        protected void $init(ConfigurationSection section) {
            this.tracker = ImmutableUpgradeTracker.integerBuilderFromConfig(section.getConfigurationSection("level")).build();
        }

    },
    SPAWNER("Spawners", (byte) 3) {

        protected void $init(ConfigurationSection section) {
            this.tracker = ImmutableUpgradeTracker.integerBuilderFromConfig(section.getConfigurationSection("level")).build();
            registerListener(new SpawnerUpgradeListener());
        }

    },
    EXP("EXP", (byte) 3) {

        protected void $init(ConfigurationSection section) {
            this.tracker = ImmutableUpgradeTracker.doubleBuilderFromConfig(section.getConfigurationSection("level")).build();
            registerListener(new ExpUpgradeListener());
        }

    },
    CROP("Crops", (byte) 3) {

        protected void $init(ConfigurationSection section) {
            this.tracker = ImmutableUpgradeTracker.integerBuilderFromConfig(section.getConfigurationSection("level")).build();
            registerListener(new CropUpgradeListener());
        }

    },
    POWER("Power", (byte) 3) {

        public void upgrade(Faction faction) {
            super.upgrade(faction);
            faction.setPowerBoost((double) getTracker().getValue((byte) this.getCurrentLevel(faction)));
        }

        protected void $init(ConfigurationSection section) {
            this.tracker = ImmutableUpgradeTracker.doubleBuilderFromConfig(section.getConfigurationSection("level")).build();
        }

    },
    MEMBERS("Members", (byte) 3) {

        protected void $init(ConfigurationSection section) {
            this.tracker = ImmutableUpgradeTracker.integerBuilderFromConfig(section.getConfigurationSection("level")).build();
        }

    },
    TNT("TNT", (byte) 3) {

        public void upgrade(Faction faction) {
            super.upgrade(faction);
            faction.setTntBankLimit((int) getTracker().getValue((byte) this.getCurrentLevel(faction)));
        }

        protected void $init(ConfigurationSection section) {
            this.tracker = ImmutableUpgradeTracker.integerBuilderFromConfig(section.getConfigurationSection("level")).build();
        }

    },
    WARP("Warps", (byte) 3) {

        public void upgrade(Faction faction) {
            super.upgrade(faction);
            faction.setWarpsLimit((int) getTracker().getValue((byte) this.getCurrentLevel(faction)));
        }

        protected void $init(ConfigurationSection section) {
            this.tracker = ImmutableUpgradeTracker.integerBuilderFromConfig(section.getConfigurationSection("level")).build();
        }

    },
    DAMAGEINCREASE("DamageIncrease", (byte) 3) {

        protected void $init(ConfigurationSection section) {
            this.tracker = ImmutableUpgradeTracker.doubleBuilderFromConfig(section.getConfigurationSection("level")).build();
            registerListener(new DamageInsUpgradeListener());
        }

    },
    DAMAGEDECREASE("DamageReduct", (byte) 3) {

        protected void $init(ConfigurationSection section) {
            this.tracker = ImmutableUpgradeTracker.doubleBuilderFromConfig(section.getConfigurationSection("level")).build();
            registerListener(new DamageDesUpgradeListener());
        }

    },
    REINFORCEDARMOR("Armor", (byte) 3) {

        protected void $init(ConfigurationSection section) {
            this.tracker = ImmutableUpgradeTracker.doubleBuilderFromConfig(section.getConfigurationSection("level")).build();
            registerListener(new ReinforcedAmorUpgradeListener());
        }

    };

    protected void registerListener(Listener listener) {
        FactionsPlugin.getInstance().getServer().getPluginManager().registerEvents(listener, FactionsPlugin.getInstance());
    }

    public static final String VALUE_KEY = "value";
    public static final String PRICE_KEY = "price";
    private String name;
    private boolean enable = false;
    private int slot;
    protected ItemStack icon;
    protected UpgradeTracker<?> tracker;

    private UpgradeType(String name, byte defaultmaxlevel) {
        this.name = name;
    }

    @Override
    public int getSlot() {
        return this.slot;
    }

    @Override
    public int getMaxLevel() {
        return this.tracker.getSize();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getId() {
        return this.name;
    }

    @Override
    public void upgrade(Faction faction) {
        faction.setUpgrade(this.getId(),  this.getCurrentLevel(faction) + 1);
    }

    @Override
    public int getCurrentLevel(Faction faction) {
        return faction.getUpgrade(this.getId());
    }

    @Override
    public UpgradeTracker getTracker() {
        return this.tracker;
    }

    @Override
    public Icon getIconExcutor(Faction faction) {
        ItemStack icon = this.icon.clone();
        if (this.getCurrentLevel(faction) > 1)
            icon.setAmount(this.getCurrentLevel(faction));
        icon = ItemUtil.applyHolder(icon, new Placeholder("{level}", Integer.toString(this.getCurrentLevel(faction)))
                , new Placeholder("%max_level%", Integer.toString(this.getMaxLevel()))
                , new Placeholder("{faction}", faction.getTag()));
        return new Icon(icon, this.getExcuter(faction));
    }

    @Override
    public boolean isEnabled() {
        return this.enable;
    }

    protected Excuter getExcuter(Faction faction) {
        return (event) -> {
            int level = faction.getUpgrade(this.getId());
            Player p = (Player) event.getWhoClicked();
            if (level < this.getMaxLevel()) {
                if (this.getTracker().nextLevelPay((byte) level).deal(p)) {
                    this.upgrade(faction);
                    List<FPlayer> viewers = event.getViewers().stream().filter(bp -> bp instanceof Player)
                            .map(bp -> FPlayers.getInstance().getByPlayer((Player) bp)).collect(Collectors.toList());
                    viewers.forEach(fp -> {
                        fp.getPlayer().closeInventory();
                        fp.getPlayer().playSound(fp.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 0F);
                    });
                    UpgradeHandler.open(viewers.toArray(new FPlayer[]{}));
                } else {
                    p.closeInventory();
                    p.sendMessage(TL.GENERIC_NOTENOUGHMONEY.format());
                }
            }
        };
    }

    protected abstract void $init(ConfigurationSection section);

    private void $preinit(ConfigurationSection section) {
        this.enable = section.getBoolean("enable", false);
        if (!this.enable) return;
        this.slot = section.getInt("displayitem.slot");
        this.icon = ItemUtil.basicDeserialize(section.getConfigurationSection("displayitem"));
        if (this.icon == null) {
            FactionsPlugin.instance.log("Cannot load icon of upgrade " + this.getName());
        }
        this.$init(section);
        FactionsPlugin.instance.log("Tracker loaded " + this.getMaxLevel() + " level.");
    }

    public static void init(ConfigurationSection section) {
        int count = 0;
        for (UpgradeType type : UpgradeType.values()) {
            if (section.isConfigurationSection(type.getId())) {
                type.$preinit(section.getConfigurationSection(type.getId()));
            } else FactionsPlugin.instance.log("Missing config section of " + type.getId() + " upgrade.");
            if (type.isEnabled()) {
                FactionsPlugin.instance.log("Upgrade " + type.getId() + " enabled.");
                count++;
            }
        }
        FactionsPlugin.instance.log("Enabled " + count + " upgrades.");
    }

}
