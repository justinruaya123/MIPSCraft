package com.jus.mipscraft;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import mars.ErrorList;
import mars.Globals;
import mars.MIPSprogram;
import mars.MarsLaunch;
import mars.ProcessingException;
import mars.mips.hardware.AddressErrorException;
import mars.mips.hardware.Memory;
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

            for(int i = 0; i < 32; i++) {
                // Print in hex 
                
                sender.sendMessage(i + ") Register " + RegisterFile.getRegisters()[i].getName() + ": " + Integer.toHexString(RegisterFile.getValue(i)));
            }
            //mars.displayMemoryPostMortem(sender);
            // Mips memory data segment starts at 0x10010000
            for(int i = 0x10010000; i < 0x10010050; i+=4) {
                try {
                    sender.sendMessage("Memory at " + Integer.toHexString(i) + ": " + Integer.toHexString(Globals.memory.getByte(i)));
                } catch(AddressErrorException e) {
                    sender.sendMessage("Memory at " + Integer.toHexString(i) + ": " + "AddressErrorException");
                }
            }
        }  catch (ProcessingException e) {
            sender.sendMessage(e.errors().generateErrorAndWarningReport());
            sender.sendMessage("Processing terminated due to errors.");
        }
    }
}
