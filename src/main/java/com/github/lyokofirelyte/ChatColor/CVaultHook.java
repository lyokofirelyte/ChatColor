package com.github.lyokofirelyte.ChatColor;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.plugin.RegisteredServiceProvider;

public class CVaultHook {
  public CCMain pl;
  public static Chat chat = null;
  public boolean vaultHooked = false;

  public CVaultHook(CCMain plugin) {
    pl = plugin;
  }

  public boolean hookSetup() {
	  
    if (pl.getServer().getPluginManager().getPlugin("Vault") != null) {
    	vaultHooked = true;
    	setupChat();
    }

    return vaultHooked;
  }

  @SuppressWarnings("rawtypes")
  private boolean setupChat() {
	  RegisteredServiceProvider rsp = pl.getServer().getServicesManager().getRegistration(Chat.class);
	  chat = (Chat)rsp.getProvider();
	  return chat != null;
  }
}