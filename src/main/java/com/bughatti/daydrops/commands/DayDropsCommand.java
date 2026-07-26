package com.bughatti.daydrops.commands;

import com.bughatti.daydrops.DayDrops;
import com.bughatti.daydrops.gui.ConfigMenu;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DayDropsCommand implements CommandExecutor, TabCompleter {

    private final DayDrops plugin;
    private final List<String> subCommands = Arrays.asList("help", "reload", "enable", "disable");

    public DayDropsCommand(DayDrops plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(plugin.msg("only-players"));
                return true;
            }
            Player player = (Player) sender;
            if (!player.hasPermission("daydrops.use")) {
                player.sendMessage(plugin.msg("no-permission"));
                return true;
            }
            ConfigMenu.open(plugin, player);
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "help":
                sendHelp(sender);
                return true;

            case "reload":
                if (!sender.hasPermission("daydrops.admin")) {
                    sender.sendMessage(plugin.msg("no-permission"));
                    return true;
                }
                plugin.reload();
                sender.sendMessage(plugin.msg("reloaded"));
                return true;

            case "enable":
                if (!sender.hasPermission("daydrops.admin")) {
                    sender.sendMessage(plugin.msg("no-permission"));
                    return true;
                }
                if (plugin.isActive()) {
                    sender.sendMessage(plugin.msg("already-enabled"));
                } else {
                    plugin.setActive(true);
                    sender.sendMessage(plugin.msg("enabled"));
                }
                return true;

            case "disable":
                if (!sender.hasPermission("daydrops.admin")) {
                    sender.sendMessage(plugin.msg("no-permission"));
                    return true;
                }
                if (!plugin.isActive()) {
                    sender.sendMessage(plugin.msg("already-disabled"));
                } else {
                    plugin.setActive(false);
                    sender.sendMessage(plugin.msg("disabled"));
                }
                return true;

            default:
                sender.sendMessage(plugin.msg("unknown-command"));
                return true;
        }
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "--- DayDrops Help ---");
        sender.sendMessage(ChatColor.YELLOW + "/daydrops " + ChatColor.GRAY + "- Abre el menú de configuración");
        sender.sendMessage(ChatColor.YELLOW + "/daydrops help " + ChatColor.GRAY + "- Muestra esta ayuda");
        sender.sendMessage(ChatColor.YELLOW + "/daydrops reload " + ChatColor.GRAY + "- Recarga la configuración");
        sender.sendMessage(ChatColor.YELLOW + "/daydrops enable " + ChatColor.GRAY + "- Activa el plugin");
        sender.sendMessage(ChatColor.YELLOW + "/daydrops disable " + ChatColor.GRAY + "- Desactiva el plugin");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            String current = args[0].toLowerCase();
            return subCommands.stream()
                    .filter(s -> s.startsWith(current))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
                  }
