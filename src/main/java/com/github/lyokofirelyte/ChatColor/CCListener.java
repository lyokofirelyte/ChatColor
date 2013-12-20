package com.github.lyokofirelyte.ChatColor;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;


public class CCListener implements Listener, CommandExecutor {
	
	CCMain pl;
	
	public CCListener(CCMain instance){
		pl = instance;
	}  
	
	String allowed = "0 1 2 3 4 5 6 7 8 9 a b c d e f";

	@EventHandler (priority = EventPriority.LOW)
	public void onChat(AsyncPlayerChatEvent e){
		
		if (pl.getColoredPlayer(e.getPlayer().getName()).getIgnore()){
			return;
		}
		
		e.setCancelled(true);
		
		for (Player p : Bukkit.getOnlinePlayers()){
			String color = pl.getColoredPlayer(p.getName()).getPlayerChatColor();
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c[G] " + CVaultHook.chat.getPlayerPrefix(p) + p.getDisplayName() + "&" + color + ": &" + color + e.getMessage()));
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		if (pl.getColoredPlayer(e.getPlayer().getName()) == null){
			ColoredPlayer player = new ColoredPlayer(e.getPlayer().getName());
			player.setPlayerChatColor("f");
			pl.updateMap(e.getPlayer().getName(), player);
		}
	}
	
	@EventHandler
	public void onCheck(PlayerCommandPreprocessEvent e){
		
		List<String> ignoreList = pl.configYaml.getStringList("Ignore");
		List<String> cleanList = pl.configYaml.getStringList("Allow");
		
		if (ignoreList.contains(e.getMessage())){
			ColoredPlayer p = pl.getColoredPlayer(e.getPlayer().getName());
			p.setIgnore(true);
			pl.updateMap(e.getPlayer().getName(), p);
		} else if (cleanList.contains(e.getMessage())){
			ColoredPlayer p = pl.getColoredPlayer(e.getPlayer().getName());
			p.setIgnore(false);
			pl.updateMap(e.getPlayer().getName(), p);
		}
	}
	
	public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {

		if (label.equalsIgnoreCase("chatcolor")){
			if (args.length == 1 && (allowed.contains(args[0]))){
				Player p = ((Player)sender);
				ColoredPlayer player = pl.getColoredPlayer(p.getName());
				player.setPlayerChatColor(args[0]);
				pl.updateMap(p.getName(), player);
				p.sendMessage("§aSuccess!");
			} else {
				sender.sendMessage("§4Invalid color! Don't use a &.");
			}
		}
		
		return true;
	}
}
