package com.bughatti.daydrops.listeners;

import com.bughatti.daydrops.DayDrops;
import com.bughatti.daydrops.gui.ConfigMenu;
import com.bughatti.daydrops.gui.MenuHolder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

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

        ItemStack currentSlotItem = event.getCurrentItem();
        boolean isPlaceholder = currentSlotItem != null
                && currentSlotItem.getType() == Material.NETHER_STAR
                && plugin.getDropItem() == null;
        boolean slotHasRealItem = currentSlotItem != null
                && currentSlotItem.getType() != Material.AIR
                && !isPlaceholder;

        // Swap usando la tecla de número (1-9) apuntando al hotbar
        if (event.getClick() == ClickType.NUMBER_KEY) {
            PlayerInventory playerInv = player.getInventory();
            int hotbarSlot = event.getHotbarButton();
            ItemStack hotbarItem = playerInv.getItem(hotbarSlot);
            boolean hotbarHasItem = hotbarItem != null && hotbarItem.getType() != Material.AIR;

            if (hotbarHasItem) {
                ItemStack newItem = hotbarItem.clone();
                ItemStack previous = slotHasRealItem ? plugin.getDropItem().clone() : null;
                plugin.setDropItem(newItem);
                playerInv.setItem(hotbarSlot, previous);
                player.sendMessage(plugin.msg("item-set"));
                ConfigMenu.open(plugin, player);
            }
            return;
        }

        ItemStack cursor = event.getCursor();
        boolean cursorHasItem = cursor != null && cursor.getType() != Material.AIR;

        if (cursorHasItem) {
            // Coloca el item del cursor; si ya había uno configurado, se lo devuelve al cursor (swap)
            ItemStack newItem = cursor.clone();
            ItemStack previous = slotHasRealItem ? plugin.getDropItem().clone() : null;
            plugin.setDropItem(newItem);
            player.setItemOnCursor(previous);
            player.sendMessage(plugin.msg("item-set"));
            ConfigMenu.open(plugin, player);
            return;
        }

        if (slotHasRealItem) {
            // Recoge el item configurado y lo deja en el cursor para poder moverlo
            ItemStack toCursor = plugin.getDropItem().clone();
            plugin.setDropItem(null);
            player.setItemOnCursor(toCursor);
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
