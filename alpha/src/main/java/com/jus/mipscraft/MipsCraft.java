package com.jus.mipscraft;

import org.bukkit.plugin.java.JavaPlugin;
import mars.MarsLaunch;

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
    public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
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
                String filename = args[1];
                prog.assemble(filename);
                prog.simulate();
                return true;
            }
        }
    }
}