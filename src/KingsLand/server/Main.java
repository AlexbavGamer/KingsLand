package KingsLand.server;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


//Vault Suport


import net.milkbowl.vault.economy.Economy;
import KingsLand.server.classmanager.classes.Config;
import KingsLand.server.classmanager.classes.Reward;
import KingsLand.server.commands.PvpCommand;
import KingsLand.server.listeners.Listeners;
import org.bukkit.plugin.RegisteredServiceProvider;
public class Main extends JavaPlugin
{
	public static Main instance;
	private static final Logger log = Logger.getLogger("Minecraft");
	public static Economy econ = null;
	public static HashMap<String, CommandModule> commands;
    
	public void SavePlayerInventory(Player p)
	{
		File file = new File(getDataFolder(), "invSaves/"+p.getName()+".yml");
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		
		if(file.exists())
		{
			file.delete();
		}

		if(!file.exists())
		{
			c.set("inventory.armor", p.getInventory().getArmorContents());
			c.set("inventory.content", p.getInventory().getContents());
			try {
				c.save(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void LoadPlayerInventory(Player p)
	{
		File file = new File(getDataFolder(), "invSaves/"+p.getName()+".yml");
		if(!file.exists()) return;
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
        ItemStack[] content = ((List<ItemStack>) c.get("inventory.armor")).toArray(new ItemStack[0]);
        p.getInventory().setArmorContents(content);
        content = ((List<ItemStack>) c.get("inventory.content")).toArray(new ItemStack[0]);
        p.getInventory().setContents(content);
        file.delete();
	}
	
	public void createFile()
	{
		registerFiles();
	}
	
	
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
	
	@Override
	public void onEnable() 
	{
        instance = this;
        commands = new HashMap<String, CommandModule>();
        
        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        registerFiles();
        
        Bukkit.getPluginManager().registerEvents(new Listeners(),(Plugin)this);
        //A method that calls all the constructors.
        registerCommands();
	}
	
	 public void onDisable()
	 {
		 instance = null;
	     commands.clear();
	 }
	 
	 public String Solver(String text)
	 {
		 text = text.replace("&", "§");
		 return text;
	 }
	 
	 public void registerCommands()
	 {
		 new PvpCommand("pvp",0,5);
	 }
	 
	 public void registerFiles()
	 {
		new Config("Config.yml");
		new Reward("Rewards.yml");
	 }
}
