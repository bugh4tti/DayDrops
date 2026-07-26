package com.bughatti.daydrops.listeners;

import com.bughatti.daydrops.DayDrops;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class PlayerDeathListener implements Listener {

    private final DayDrops plugin;

    public PlayerDeathListener(DayDrops plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (!plugin.isActive()) return;

        ItemStack dropItem = plugin.getDropItem();
        if (dropItem == null || dropItem.getType() == Material.AIR) return;

        // Elimina absolutamente todos los drops naturales (incluida la cabeza),
        // sin importar si la víctima tiene keepInventory activado o no.
        event.getDrops().clear();

        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        ItemStack toGive = dropItem.clone();

        if (killer != null) {
            Map<Integer, ItemStack> leftover = killer.getInventory().addItem(toGive);
            if (!leftover.isEmpty()) {
                Location loc = killer.getLocation();
                for (ItemStack extra : leftover.values()) {
                    loc.getWorld().dropItemNaturally(loc, extra);
                }
            }
        } else {
            Location loc = victim.getLocation();
            loc.getWorld().dropItemNaturally(loc, toGive);
        }
    }
    }
