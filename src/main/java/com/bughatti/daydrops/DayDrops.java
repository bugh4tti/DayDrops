package com.bughatti.daydrops;

import com.bughatti.daydrops.commands.DayDropsCommand;
import com.bughatti.daydrops.listeners.GUIListener;
import com.bughatti.daydrops.listeners.PlayerDeathListener;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class DayDrops extends JavaPlugin {

    private static DayDrops instance;
    private boolean active;
    private ItemStack dropItem;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        loadFromConfig();

        DayDropsCommand commandExecutor = new DayDropsCommand(this);
        getCommand("daydrops").setExecutor(commandExecutor);
        getCommand("daydrops").setTabCompleter(commandExecutor);

        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new GUIListener(this), this);

        getLogger().info("DayDrops habilitado correctamente.");
    }

    @Override
    public void onDisable() {
        getLogger().info("DayDrops deshabilitado.");
    }

    public void loadFromConfig() {
        FileConfiguration cfg = getConfig();
        this.active = cfg.getBoolean("enabled", true);
        if (cfg.isItemStack("drop-item")) {
            this.dropItem = cfg.getItemStack("drop-item");
        } else {
            this.dropItem = null;
        }
    }

    public void reload() {
        reloadConfig();
        loadFromConfig();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        getConfig().set("enabled", active);
        saveConfig();
    }

    public ItemStack getDropItem() {
        return dropItem;
    }

    public void setDropItem(ItemStack item) {
        this.dropItem = item;
        getConfig().set("drop-item", item);
        saveConfig();
    }

    public String msg(String path) {
        String raw = getConfig().getString("messages." + path, "");
        return ChatColor.translateAlternateColorCodes('&', raw);
    }

    public static DayDrops getInstance() {
        return instance;
    }
    }
