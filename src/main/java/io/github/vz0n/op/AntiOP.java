package io.github.vz0n.op;

import io.github.vz0n.op.command.CommandAntiOp;
import io.github.vz0n.op.listener.CommandPreProcess;
import io.github.vz0n.op.task.OpTask;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

public class AntiOP extends JavaPlugin {

    //Why a task? because a malicious plugins can give op to someone
    //silently, and it's needed for the LIST mode.
    private BukkitTask optask;

    @Override
    public void onEnable(){
        getLogger().log(Level.INFO, "Enabling plugin...");

        Path configPath = Paths.get(this.getDataFolder().getPath(), "config.yml");

        //This returns the config embedded on the jar if the file and config directory doesn't
        //exist. So we can continue without worries.
        FileConfiguration config = this.getConfig();

        getCommand("antiop").setExecutor(new CommandAntiOp(this));
        getServer().getPluginManager().registerEvents(new CommandPreProcess(this), this);

        //If the config value is invalid or doesn't exist, just return "CONSOLE".
        //However, is better to use an enum instead of Strings, if you want to modify this.
        if (!config.getString("mode", "CONSOLE").equalsIgnoreCase("CONSOLE"))
            startOpTask();

        if (Files.notExists(configPath))
            this.saveDefaultConfig();

        //Everything is up if the execution got to here.
        getLogger().log(Level.INFO, "Plugin enabled!");
    }

    @Override
    public void onDisable(){
        this.getLogger().log(Level.INFO, "Disabling plugin...");
    }

    public void reload(){
        //We want to load the config changes firstly.
        this.reloadConfig();

        String mode = this.getConfig().getString("mode","CONSOLE").toUpperCase();

        //Only cancel the task if we are on CONSOLE mode and it's active.
        if (mode.equals("CONSOLE")) {
            if (!optask.isCancelled()) {
                optask.cancel();
            }
            return;
        }

        startOpTask();
    }

    private void startOpTask(){
        //TODO: Test this method to check that doesn't happens weird errors.
        //Cancel the previous task if there's one.
        if (optask != null){
            optask.cancel();
        }

        //Create a new task.
        optask = this.getServer().getScheduler().runTaskTimer(this, new OpTask(this, this.getConfig()), 15L, 70L);
    }

    /**
     * A simple method to execute a command that will ban the player.
     *
     * @param player The player name.
     */
    public void ban(String player){
        getServer().dispatchCommand(
                    getServer().getConsoleSender(),
                    getConfig().getString("banCommand", "ban")
                            .replace("%player%", player)
            );
    }

}
