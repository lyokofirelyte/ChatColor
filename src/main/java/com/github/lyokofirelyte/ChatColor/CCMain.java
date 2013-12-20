package com.github.lyokofirelyte.ChatColor;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

// Plugin created by lyokofirelyte.
// http://dev.bukkit.org/profiles/Lyoko_Firelyte/
// https://github.com/lyokofirelyte?tab=repositories

public class CCMain extends JavaPlugin {
	
	Map <String, ColoredPlayer> players = new HashMap<>();
	YamlConfiguration yaml = new YamlConfiguration();
	YamlConfiguration configYaml = new YamlConfiguration();
	public CVaultHook vaultMgr = new CVaultHook(this);

	public void onEnable(){
		
	    getCommand("chatcolor").setExecutor(new CCListener(this));
	    Bukkit.getServer().getPluginManager().registerEvents(new CCListener(this), this);
		fileInit(new File("plugins/ChatColor/players.yml"));
		vaultMgr.hookSetup();
		getLogger().log(Level.INFO, "Online!");
	}
	
	public void onDisable(){
		clean();
	}

	private void fileInit(File file) {
		
		if (!file.exists()){
			try {
				file.createNewFile();
				if (Bukkit.getOnlinePlayers().length > 0){
					renderPlayers(file);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			tryLoad(file);
		}
		
		File config = new File("plugins/ChatColor/config.yml");
		
		if (!config.exists()){
			try {
				config.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		configYaml = YamlConfiguration.loadConfiguration(config);
	}
	
	public void tryLoad(File file){
		
		yaml = YamlConfiguration.loadConfiguration(file);
		
		for (String user : yaml.getStringList("Users")){
			ColoredPlayer player = new ColoredPlayer(user);
			player.setPlayerChatColor(yaml.getString("User." + user + ".Color"));
			player.setIgnore(yaml.getBoolean("User." + user + ".Ignore"));
			players.put(user, player);
		}
	}
	
	private void renderPlayers(File file){
		
		for (Player p : Bukkit.getOnlinePlayers()){	
			ColoredPlayer player = new ColoredPlayer(p.getName());
			player.setPlayerChatColor("f");
			players.put(p.getName(), player);
			System.out.println("First run completed!");
		}
	}
	
	private void clean(){
		
		List<String> ul = yaml.getStringList("Users");
		
		for (String player : players.keySet()){
			ul.add(player);
			yaml.set("User." + player + ".Color", players.get(player).getPlayerChatColor());
			yaml.set("User." + player + ".Ignore", players.get(player).getIgnore());
		}
		
		yaml.set("Users", ul);
		
		File file = new File("plugins/ChatColor/players.yml");
		
		try {
			yaml.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public ColoredPlayer getColoredPlayer(String player){	
		return players.get(player);
	}
	
	public void updateMap(String player, ColoredPlayer cp){
		players.put(player, cp);
	}
}
