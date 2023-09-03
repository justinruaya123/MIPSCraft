package com.jus.mipscraft;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import mars.ErrorList;
import mars.MIPSprogram;
import mars.MarsLaunch;
import mars.ProcessingException;
import mars.mips.hardware.RegisterFile;

public class MIPSRunnable extends BukkitRunnable {
    
    private MipsCraft plugin;
    private String[] args;
    private CommandSender sender;
    
    public MIPSRunnable(MipsCraft plugin, String[] args, CommandSender sender) {
        this.plugin = plugin;
        this.args = args;
        this.sender = sender;
    }
    
    @Override
    public void run() {
        MarsLaunch mars = new MarsLaunch(new String[0]);
        MIPSprogram prog = new MIPSprogram();
        ArrayList<String> programs = new ArrayList<String>();
        String filename = "Samples/" + args[1] + ".asm";
        File mainFile = new File(filename).getAbsoluteFile();
        if (!mainFile.exists()) {
            sender.sendMessage("File " + mainFile.getAbsolutePath() + " does not exist.");
            return;
        }
        programs.add(filename);
        try {
            ArrayList<MIPSprogram> toAssemble = prog.prepareFilesForAssembly(programs, mainFile.getAbsolutePath(), null);
            if (toAssemble == null) {
                sender.sendMessage("Error preparing file " + mainFile.getAbsolutePath() + " for assembly.");
                return;
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
    }
}
