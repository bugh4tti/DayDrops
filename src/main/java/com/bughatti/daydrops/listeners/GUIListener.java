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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class GUIListener implements Listener {

    private final DayDrops plugin;

    public GUIListener(DayDrops plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getView().getTopInventory().getHolder() instanceof MenuHolder)) return;
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        Inventory topInventory = event.getView().getTopInventory();
        Inventory clickedInventory = event.getClickedInventory();

        if (clickedInventory == null) return;

        // Click dentro del menú (arriba)
        if (clickedInventory.equals(topInventory)) {
            event.setCancelled(true);
            if (event.getRawSlot() != ConfigMenu.ITEM_SLOT) return;
            if (!player.hasPermission("daydrops.admin")) return;
            handleTopSlotClick(event, player, topInventory);
            return;
        }

        // Click en el inventario del jugador (abajo) con Q o Ctrl+Q
        if (clickedInventory.equals(player.getInventory())) {
            if (!player.hasPermission("daydrops.admin")) return;
            if (event.getClick() != ClickType.DROP && event.getClick() != ClickType.CONTROL_DROP) return;

            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || clicked.getType() == Material.AIR) return;

            event.setCancelled(true);

            boolean takeAll = event.getClick() == ClickType.CONTROL_DROP;
            ItemStack toConfigure = clicked.clone();

            if (takeAll || clicked.getAmount() <= 1) {
                toConfigure.setAmount(clicked.getAmount());
                clickedInventory.setItem(event.getSlot(), null);
            } else {
                toConfigure.setAmount(1);
                clicked.setAmount(clicked.getAmount() - 1);
            }

            plugin.setDropItem(toConfigure);
            topInventory.setItem(ConfigMenu.ITEM_SLOT, ConfigMenu.buildCenterItem(plugin));
            player.sendMessage(plugin.msg("item-set"));
        }
    }

    private void handleTopSlotClick(InventoryClickEvent event, Player player, Inventory topInventory) {
        ItemStack currentSlotItem = event.getCurrentItem();
        boolean isPlaceholder = currentSlotItem != null
                && currentSlotItem.getType() == Material.NETHER_STAR
                && plugin.getDropItem() == null;
        boolean slotHasRealItem = currentSlotItem != null
                && currentSlotItem.getType() != Material.AIR
                && !isPlaceholder;

        // Swap con teclas 1-9 (hotbar) apuntando al slot central
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
                topInventory.setItem(ConfigMenu.ITEM_SLOT, ConfigMenu.buildCenterItem(plugin));
                player.sendMessage(plugin.msg("item-set"));
            }
            return;
        }

        ItemStack cursor = event.getCursor();
        boolean cursorHasItem = cursor != null && cursor.getType() != Material.AIR;

        if (cursorHasItem) {
            ItemStack newItem = cursor.clone();
            ItemStack previous = slotHasRealItem ? plugin.getDropItem().clone() : null;
            plugin.setDropItem(newItem);
            player.setItemOnCursor(previous);
            topInventory.setItem(ConfigMenu.ITEM_SLOT, ConfigMenu.buildCenterItem(plugin));
            player.sendMessage(plugin.msg("item-set"));
            return;
        }

        if (slotHasRealItem) {
            ItemStack toCursor = plugin.getDropItem().clone();
            plugin.setDropItem(null);
            player.setItemOnCursor(toCursor);
            topInventory.setItem(ConfigMenu.ITEM_SLOT, ConfigMenu.buildCenterItem(plugin));
            player.sendMessage(plugin.msg("item-removed"));
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (!(event.getView().getTopInventory().getHolder() instanceof MenuHolder)) return;
        int topSize = event.getView().getTopInventory().getSize();
        for (int slot : event.getRawSlots()) {
            if (slot < topSize) {
                event.setCancelled(true);
                return;
            }
        }
    }
                }
