package com.github.lyokofirelyte.ChatColor;

public class ColoredPlayer {

	String name;

	public ColoredPlayer(String name) {
		this.name = name;
	}
	
	String playerChatColor = "f";
	Boolean ignore = false;
	
	public Boolean getIgnore(){
		return ignore;
	}
	
	public String getPlayerChatColor(){
		return playerChatColor;
	}
	
	public void setIgnore(Boolean a){
		ignore = a;
	}
	
	public void setPlayerChatColor(String a){
		playerChatColor = a;
	}
}
