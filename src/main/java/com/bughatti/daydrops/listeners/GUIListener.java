package com.bughatti.daydrops.listeners;

import com.bughatti.daydrops.DayDrops;
import com.bughatti.daydrops.gui.ConfigMenu;
import com.bughatti.daydrops.gui.MenuHolder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

public class GUIListener implements Listener {

    private final DayDrops plugin;

    public GUIListener(DayDrops plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof MenuHolder)) return;
        event.setCancelled(true);

        if (event.getRawSlot() != ConfigMenu.ITEM_SLOT) return;
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        if (!player.hasPermission("daydrops.admin")) return;

        ItemStack cursor = event.getCursor();
        ItemStack clicked = event.getCurrentItem();

        if (cursor != null && cursor.getType() != Material.AIR) {
            plugin.setDropItem(cursor.clone());
            player.setItemOnCursor(null);
            player.sendMessage(plugin.msg("item-set"));
            ConfigMenu.open(plugin, player);
            return;
        }

        boolean isPlaceholder = clicked != null
                && clicked.getType() == Material.NETHER_STAR
                && plugin.getDropItem() == null;

        if (clicked != null && clicked.getType() != Material.AIR && !isPlaceholder) {
            plugin.setDropItem(null);
            player.sendMessage(plugin.msg("item-removed"));
            ConfigMenu.open(plugin, player);
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof MenuHolder) {
            event.setCancelled(true);
        }
    }
}
