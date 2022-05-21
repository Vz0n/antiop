package com.monkeycraftservices.antiop.listener;

import com.monkeycraftservices.antiop.AntiOP;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;
import java.util.logging.Level;

public class CommandPreProcess implements Listener {

    private AntiOP instance;

    public CommandPreProcess(AntiOP plugin){
        instance = plugin;
    }

    @EventHandler
    public void handle(final PlayerCommandPreprocessEvent e) {
        if (e.isCancelled()) return;

        String[] message = e.getMessage().split(" ");

        if(message[0].equalsIgnoreCase("/op") || message[0].equalsIgnoreCase("/minecraft:op")){

            FileConfiguration config = instance.getConfig();
            String mode = config.isSet("mode") ? config.getString("mode").toUpperCase() : "";
            Player player = e.getPlayer();

            switch(mode){

                case "CONSOLE": {
                    e.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "This can only be run from the console.");
                    break;
                }
                case "LIST": {
                    List<String> players = instance.getConfig().getStringList("playerlist");

                    if(!players.contains(player.getName())){
                           e.setCancelled(true);


                           player.sendMessage(ChatColor.AQUA + "The vanilla OP system is disabled on this server"); //LuckPerms like message.
                    }

                    if(config.getBoolean("nolistban") && player.hasPermission("minecraft.command.op") && message.length > 1){  //Soon i'm going to add handling by UUIDs
                        if(!players.contains(message[1])){
                            String name = message[1];
                            instance.ban(name, player.getName());
                            player.setOp(false);
                        }
                    }

                    break;
                }
                case "DISABLED": {
                    e.setCancelled(true);
                    player.sendMessage(ChatColor.AQUA + "The vanilla OP system is disabled in this server.");
                    break;
                }
                default: {
                    instance.getLogger().log(Level.INFO, "[AntiOP] Seems there's a invalid value in the 'mode' section of the config.");
                    break;
                }
            }
        }
    }

}
