package com.monkeycraftservices.antiop.command;

import com.monkeycraftservices.antiop.AntiOP;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandAntiOp implements CommandExecutor {

    private AntiOP instance;

    public CommandAntiOp(AntiOP plugin){
        this.instance = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){

        String mode = instance.getConfig().getString("mode");
        String subcmd = args.length > 0 ? args[0] : "";

        if(sender instanceof Player && mode.equalsIgnoreCase("CONSOLE")){
            sender.sendMessage(ChatColor.RED + "This command can only be run from the console.");
            return false;
        }

        if(!sender.hasPermission("antiop.main")){
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this!");
            return true;
        }

        switch(subcmd){

            case "reload": {
                instance.reload();
                sender.sendMessage(ChatColor.GREEN + "Config reloaded.");
                break;
            }

            case "list": {
                List<String> players = instance.getConfig().getStringList("playerlist");
                StringBuilder list = new StringBuilder();
                if (!players.isEmpty()){
                    for (String s : players) {
                        list.append(ChatColor.GRAY + "- " + s + "\n");
                    }
                }
                sender.sendMessage(ChatColor.AQUA + "List of players in the OP list:\n" + list);
                break;
            }

            default: {
                sender.sendMessage(ChatColor.GREEN + "AntiOP 1.0",
                        ChatColor.AQUA + "- /antiop reload | Reloads the plugin",
                        ChatColor.AQUA + "- /antiop list | See the players in the OP List.");
            }

        }

        return true;

    }
}
