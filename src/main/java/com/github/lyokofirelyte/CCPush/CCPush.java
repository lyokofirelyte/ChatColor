package com.github.lyokofirelyte.CCPush;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;

public class CCPush extends JavaPlugin {

	public void onEnable(){
		
		File config = new File("plugins/CCPush/config.yml");
		
		if (!config.exists()){
			lg("No config found - which means no repos were defined! Add your repos as names.");
			writeDefault(config);
			return;
		}
		
		YamlConfiguration configYaml = YamlConfiguration.loadConfiguration(config);
		
		if (configYaml.getBoolean("git.enable")){
			tryInit(configYaml.getStringList("git.repos"));
		}
	}
	
	public void tryInit(List<String> repoList){
		
		if (repoList.size() <= 0){
			lg("No repos found!");
			return;
		}
		
		for (String repo : repoList){
			
	        Git git;  
	        File project = new File("plugins/CCPush/" + repo);
	        
	        try {
	         	git = Git.open(project);
	         	commit(git);
	        } catch (Exception e) {
	        	lg("No git files in " + repo + " found! Did you init this and give it a remote yet?");
	        }
		}
	}
	
	public void commit(Git git){
		
        try {
        	git.add().addFilepattern(".").call();
            Status status = git.status().call();
            
            if (status.getAdded().size() > 0 || status.getChanged().size() > 0){
            	git.commit().setMessage("onLoad commit").call();
            	tryPush(git);
            } else {
            	lg("Nothing to commit!");
            }
        } catch (Exception e) {
        	lg("Error commiting files!");
        }
	}
	
	public void tryPush(Git git){
		
		try {
			git.push().call();
			System.out.println("SUCCESS! Pushed new changes.");
		} catch (InvalidRemoteException e) {
			e.printStackTrace();
		} catch (TransportException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
	}
	
	private void lg(String log){
		getLogger().log(Level.SEVERE, log);
	}
	
	private void writeDefault(File f){
		
		try { 
			f.createNewFile();
		} catch (IOException e) {}

		YamlConfiguration y = YamlConfiguration.loadConfiguration(f);
		y.set("git.enable", true);
		y.set("git.repos", new ArrayList<String>(Arrays.asList("TestProjectName")));
		
		try {
			y.save(f);
		} catch (IOException e) {} 

	}
	
	public void onDisable(){ 
		lg("TwatMuffins."); 
	}
}
