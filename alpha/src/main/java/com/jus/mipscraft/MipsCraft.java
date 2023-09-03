package com.jus.mipscraft;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import mars.ErrorList;
import mars.MIPSprogram;
import mars.MarsLaunch;
import mars.ProcessingException;
import mars.mips.hardware.RegisterFile;

public class MipsCraft extends JavaPlugin {
    public MarsLaunch mars;

    @Override
    public void onEnable() {
        String[] args = new String[0];
        mars = new MarsLaunch(args);
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
            MIPSprogram prog = new MIPSprogram();
            if(args.length == 0) {
                sender.sendMessage(ChatColor.RED + "Usage: /mips <command>");
                return true;
            }
            if(args[0].equalsIgnoreCase("run")) {
                if(args.length == 1) {
                    sender.sendMessage(ChatColor.RED + "Usage: /mips run <file>");
                    return true;
                }
                ArrayList<String> programs = new ArrayList<String>();
                String filename = args[1];
                File mainFile = new File(filename).getAbsoluteFile();
                if (!mainFile.exists()) {
                    sender.sendMessage("File " + mainFile.getAbsolutePath() + " does not exist.");
                    return true;
                }
                programs.add(filename);
                try {
                    ArrayList<MIPSprogram> toAssemble = prog.prepareFilesForAssembly(programs, mainFile.getAbsolutePath(), null);
                    if (toAssemble == null) {
                        sender.sendMessage("Error preparing file " + mainFile.getAbsolutePath() + " for assembly.");
                        return true;
                    }
                    ErrorList warnings = prog.assemble(toAssemble, true, false);
                    if (warnings != null && warnings.warningsOccurred()) {
                        sender.sendMessage(warnings.generateWarningReport());
                    }
                    RegisterFile.initializeProgramCounter(true);
                    mars.establishObserver();
                    prog.simulate(-1);
                }  catch (ProcessingException e) {
                    sender.sendMessage(e.errors().generateErrorAndWarningReport());
                    sender.sendMessage("Processing terminated due to errors.");
                }
                return true;
            }
        }
        return false;
    }
}