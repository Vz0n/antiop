package io.github.vz0n.op.listener;

import com.google.common.base.Strings;
import io.github.vz0n.op.AntiOP;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class CommandPreProcess implements Listener {

    private AntiOP instance;
    private final Pattern OP_COMMAND_REGEX = Pattern.compile("^/?(\\w+:)?(deop|op)( .*)?$");

    public CommandPreProcess(AntiOP plugin){
        instance = plugin;
    }

    @EventHandler
    public void handle(PlayerCommandPreprocessEvent e) {
        if (e.isCancelled()) return;

        String[] message = e.getMessage().split("/ +/g");

        //Message length will always be n > 0, it's a command.
        if(OP_COMMAND_REGEX.matcher(message[0]).matches()){

            FileConfiguration config = instance.getConfig();
            Player player = e.getPlayer();

            //We will send this message on the 3 modes because it can help to prevent
            //fingerprinting.
            String denyMessage = ChatColor.translateAlternateColorCodes('&',
                    config.getString("denyMessage", ""));
            String mode = config.getString("mode", "").toUpperCase();

            switch(mode){

                case "CONSOLE", "DISABLED": {
                    e.setCancelled(true);
                    player.sendMessage(denyMessage);
                    break;
                }

                case "LIST": {
                    List<String> players = instance.getConfig().getStringList("playerList");
                    String executorName = player.getName();
                    String oppedName = message.length > 1 ? message[1] : "";

                    if(!players.contains(executorName) || !players.contains(oppedName)){
                           e.setCancelled(true);
                           player.sendMessage(denyMessage);

                           //Check for permission because we don't want to ban a normal player that ran
                           //the command with an argument for fun or something else.
                           if(config.getBoolean("noListBan") && !Strings.isNullOrEmpty(oppedName)
                                   && player.hasPermission("minecraft.command.op")){

                               players.remove(executorName);
                               instance.ban(executorName);
                               instance.ban(oppedName);

                               //Maybe there's some crazy guy that has a bunch of people
                               //allowed to be OP, but I need to improve this.
                               instance.getServer().getScheduler().
                                       runTaskAsynchronously(instance, () -> {
                                           try {
                                               config.set("playerList", players);
                                               config.save(
                                                       new File(instance.getDataFolder(), "config.yml")
                                               );
                                           } catch (IOException ex){
                                               instance.getLogger().log(Level.SEVERE, "Error saving the config: %s",
                                                       ex.getCause());
                                           }
                                       });
                           }
                    }
                    break;
                }

                default: {
                    instance.getLogger().log(Level.INFO, "Seems there is a invalid value in the 'mode' section of the config.");
                }

            }
        }
    }

}
