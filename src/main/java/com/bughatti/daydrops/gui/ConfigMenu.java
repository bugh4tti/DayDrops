package com.bughatti.daydrops.gui;

import com.bughatti.daydrops.DayDrops;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ConfigMenu {

    public static final int SIZE = 54;
    public static final int ITEM_SLOT = 22;

    public static void open(DayDrops plugin, Player player) {
        String title = ChatColor.translateAlternateColorCodes('&',
                plugin.getConfig().getString("messages.menu-title", "&8DayDrops - Configuración"));

        Inventory inv = Bukkit.createInventory(new MenuHolder(), SIZE, title);

        ItemStack border = createBorderItem();
        for (int slot : getBorderSlots()) {
            inv.setItem(slot, border);
        }

        inv.setItem(ITEM_SLOT, buildCenterItem(plugin));

        player.openInventory(inv);
    }

    private static ItemStack createBorderItem() {
        ItemStack pane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = pane.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(" ");
            pane.setItemMeta(meta);
        }
        return pane;
    }

    public static ItemStack buildCenterItem(DayDrops plugin) {
        ItemStack configured = plugin.getDropItem();

        if (configured != null && configured.getType() != Material.AIR) {
            ItemStack display = configured.clone();
            ItemMeta meta = display.getItemMeta();
            if (meta != null) {
                List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
                lore.add("");
                lore.add(ChatColor.GRAY + "Este es el item que actualmente");
                lore.add(ChatColor.GRAY + "dropean todos los jugadores al morir.");
                lore.add("");
                lore.add(ChatColor.YELLOW + "» Pulsa Q sobre otro item de tu inventario");
                lore.add(ChatColor.YELLOW + "  para reemplazarlo.");
                lore.add(ChatColor.YELLOW + "» Click aquí para quitarlo.");
                meta.setLore(lore);
                display.setItemMeta(meta);
            }
            return display;
        }

        ItemStack star = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = star.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.YELLOW + "Coloca el item que dropearán los jugadores");
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(ChatColor.GRAY + "Pon el cursor sobre un item de tu inventario");
            lore.add(ChatColor.GRAY + "y pulsa Q para configurarlo como el item");
            lore.add(ChatColor.GRAY + "exclusivo que recibirán los jugadores al matar.");
            lore.add("");
            lore.add(ChatColor.GRAY + "Se dropeará sin importar si la víctima");
            lore.add(ChatColor.GRAY + "tiene o no keepInventory activado.");
            meta.setLore(lore);
            star.setItemMeta(meta);
        }
        return star;
    }

    private static int[] getBorderSlots() {
        List<Integer> slots = new ArrayList<>();
        for (int i = 0; i < 9; i++) slots.add(i);          // fila superior
        for (int i = 45; i < 54; i++) slots.add(i);         // fila inferior
        for (int i = 9; i < 45; i += 9) {                   // columnas laterales
            slots.add(i);
            slots.add(i + 8);
        }
        return slots.stream().mapToInt(Integer::intValue).toArray();
    }
                    }
