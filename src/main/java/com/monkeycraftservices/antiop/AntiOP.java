package com.monkeycraftservices.antiop;

import com.monkeycraftservices.antiop.command.CommandAntiOp;
import com.monkeycraftservices.antiop.listener.CommandPreProcess;
import com.monkeycraftservices.antiop.task.OpTask;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.logging.Level;

public class AntiOP extends JavaPlugin {

    private BukkitTask optask;

    @Override
    public void onEnable(){
        getLogger().log(Level.INFO, "Enabling plugin...");
        saveDefaultConfig();
        FileConfiguration config = this.getConfig();
        getCommand("antiop").setExecutor(new CommandAntiOp(this));
        getServer().getPluginManager().registerEvents(new CommandPreProcess(this), this);

        if(config.getBoolean("nolistban") && config.getString("mode").equalsIgnoreCase("LIST"))
            startOpTask();


        getLogger().log(Level.INFO, "Plugin enabled!°");
    }

    @Override
    public void onDisable(){
        this.getLogger().log(Level.INFO, "Disabling plugin...");
    }

    public void reload(){
        this.reloadConfig();
        boolean nolistmode = this.getConfig().getBoolean("nolistmode");
        String mode = this.getConfig().isSet("mode") ? this.getConfig().getString("mode").toUpperCase() : "";


        if (!mode.equals("LIST") || !nolistmode) {
            if(optask != null) {
                optask.cancel();
            }
            return;
        }

        startOpTask();
    }

    private void startOpTask(){
        if(optask != null) {
            optask.cancel();
            optask = null;
        }
        optask = this.getServer().getScheduler().runTaskTimer(this, new OpTask(this, this.getConfig()), 15L, 70L);
    }

    public void ban(String... players){

        int counter = 0;
        while(counter < players.length) {
            getServer().dispatchCommand(
                    getServer().getConsoleSender(),
                    getConfig().getString("bancommand").replace("%player%", players[counter])
            );
            counter++;
        }
    }

}
