package com.jus.mipscraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class MipsCraft extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("MARS MIPS Initialized");
    }

    @Override
    public void onDisable() {
        getLogger().info("See you again, SpigotMC!");
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
        if(label.equalsIgnoreCase("mips")) {
            if(args.length == 0) {
                sender.sendMessage(ChatColor.RED + "Usage: /mips <command>");
                return true;
            }
            if(args[0].equalsIgnoreCase("run")) {
                if(args.length == 1) {
                    sender.sendMessage(ChatColor.RED + "Usage: /mips run <file>");
                    return true;
                }
                MIPSRunnable runnable = new MIPSRunnable(this, args, sender);
                runnable.runTaskAsynchronously(this);
                return true;
            }
        }
        return false;
    }
}