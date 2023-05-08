package io.github.vz0n.op.task;

import io.github.vz0n.op.AntiOP;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class OpTask implements Runnable{

    private FileConfiguration config;
    private AntiOP plugin;

    public OpTask(AntiOP plugin, FileConfiguration pluginConfig){
        this.config = pluginConfig;
        this.plugin = plugin;
    }

    @Override
    public void run() {
       Set<OfflinePlayer> ops = plugin.getServer().getOperators();
       String mode = config.getString("mode", "").toUpperCase();

       ops.forEach((player) -> {
           switch (mode){
               case "LIST": {
                   List<String> allowed = config.getStringList("playerList");
                   String name = player.getName();

                   if (player.isOp() && !allowed.contains(name)){
                       player.setOp(false);
                       if(config.getBoolean("noListBan")) plugin.ban(name);
                   }
                   break;
               }
               case "DISABLED": {
                   player.setOp(false);
                   break;
               }
               default: {
                   plugin.getLogger().log(Level.WARNING,
                           "The AntiOP task listener is enabled, but the plugin mode is unknown. Verify your config or contact the dev.");
               }
           }
       });
    }

}
