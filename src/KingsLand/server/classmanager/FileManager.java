package KingsLand.server.classmanager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Color;
import org.bukkit.configuration.file.YamlConfiguration;

public abstract class FileManager
{
	public HashMap<String, java.io.File> Files = new HashMap<String, File>();
	public HashMap<String, Object> Variables = new HashMap<String, Object>();
	public String FileName;
	public File File;
	public YamlConfiguration FileConfig;
	public String Folder = "plugins/KingsLand/";
	private static final Logger log = Logger.getLogger("Minecraft");
	public File getDataFolder()
	{
		return File = new File(Folder);
	}
	/**
    * @param FileName - The name of the file.
    */
	public FileManager(String FileName)
	{
		if(FileName.contains(".yml"))
		{
			this.FileName = FileName;
			this.File = new File(getDataFolder(), FileName);
			this.FileConfig = Utf8YamlConfiguration.loadConfiguration(File);
			Files.put(FileName, File);
			if(!File.exists())
			{
				log.severe("File "+File.getName()+" not exist. creating one");
				try
				{
					File.createNewFile();
					SetupFile();
				} 
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				log.log(Level.INFO,"File "+File.getName()+" has loaded");
			}
		}
		else
		{
			log.severe("Cant be create "+FileName+" reason: need .yml extension");
		}
	}
	
	public abstract void SetupFile();
	
	public boolean AddVariable(String Key, Object Value)
	{
		if(Variables.containsKey(Key) && Variables.containsValue(Value))
		{
			return false;
		}
		else
		{
			log.log(Level.INFO, "Key: "+Key+" => "+Value);
			Variables.put(Key, Value);
			return true;
		}
	}
	
	public void SetVariables()
	{
		for(Entry<String, Object> Vars : Variables.entrySet())
		{
			SetValue(Vars.getKey(), Vars.getValue());
		}
	}
	
	public File getFile()
	{
		return this.File;
	}
	
	public YamlConfiguration getConfig()
	{
		return this.FileConfig;
	}
	
	public void SetValue(String Key, Object Value)
	{

		if(Value instanceof String)
		{
			getConfig().set(Key, (String) Value);
		}
		else if(Value instanceof Integer)
		{
			getConfig().set(Key, (Integer) Value);
		}
		else if(Value instanceof Boolean)
		{
			getConfig().set(Key, (Boolean) Value);
		}
		else if(Value instanceof Float)
		{
			getConfig().set(Key, (Float) Value);
		}
		else if(Value instanceof Color)
		{
			getConfig().set(Key, Value);
		}
		
		try {
			getConfig().save(File);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public YamlConfiguration CopyDefaults(Boolean force)
	{
		getConfig().options().copyDefaults(true);
		return this.FileConfig;
	}
	
	public void save()
	{
		try 
		{
			getConfig().save(File);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public Object GetValue(String Key)
	{
		if(getConfig().get(Key) instanceof String)
		{
			return (String) getConfig().getString(Key);
		}
		else if(getConfig().get(Key) instanceof Integer)
		{
			return (Integer) getConfig().getInt(Key);
		}
		else if(getConfig().get(Key) instanceof Boolean)
		{
			return (Boolean) getConfig().getBoolean(Key);
		}
		else if(getConfig().get(Key) instanceof Float)
		{
			return getConfig().getFloatList(Key);
		}
		else if(getConfig().get(Key) instanceof Color)
		{
			return getConfig().getColor(Key);
		}
		else
		{
			return null;
		}
	}
}
