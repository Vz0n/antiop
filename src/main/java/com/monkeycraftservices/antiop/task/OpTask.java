package com.monkeycraftservices.antiop.task;

import com.monkeycraftservices.antiop.AntiOP;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class OpTask implements Runnable{

    private List<String> players;
    private boolean isLBanEnabled;
    private AntiOP instance;

    public OpTask(AntiOP plugin, FileConfiguration pluginconfig){
        players = pluginconfig.getStringList("playerlist");
        isLBanEnabled = pluginconfig.getBoolean("nolistban");
        instance = plugin;
    }

    @Override
    public void run() {

        for(Player p : instance.getServer().getOnlinePlayers()){
            if(p.isOp()){
                if(!players.contains(p.getName())){
                    p.setOp(false);
                    if(isLBanEnabled){
                       instance.ban(p.getName());
                    }
                }
            }
        }
    }

}
