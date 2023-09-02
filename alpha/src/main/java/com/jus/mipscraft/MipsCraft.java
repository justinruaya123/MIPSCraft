package com.jus.mipscraft;

import org.bukkit.plugin.java.JavaPlugin;
public class MipsCraft extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("Hello, SpigotMC!");
    }
    @Override
    public void onDisable() {
        getLogger().info("See you again, SpigotMC!");
    }
}