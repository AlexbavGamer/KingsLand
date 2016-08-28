package KingsLand.server;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import KingsLand.server.classmanager.classes.Config;
import KingsLand.server.commands.PvpCommand;

public abstract class CommandModule
{
    public String commandName;
    public int minArgs;
    public int maxArgs;
    public boolean enabled;
    public CommandSender sender;
    public Player target;
    public Double aposta;
   
    /**
     * @param label - The label of the command.
     * @param minArgs - The minimum amount of arguments.
     * @param maxArgs - The maximum amount of arguments.
     */
    public CommandModule(String commandName, int minArgs, int maxArgs)
    {
        this.commandName = commandName.toLowerCase();
        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
        Main.instance.getCommand(commandName.toLowerCase()).setExecutor(new CmdExecutor());
        Main.commands.put(commandName.toLowerCase(), this);
    }
   
    //This method will process the command.
    public abstract void run(CommandSender sender, String[] args);
    public abstract void errorMsg(CommandSender sender);
	public boolean enabled()
	{
		return this.enabled;
	}
	public CommandSender getSender()
	{
		return this.sender;
	}
	
	public Player getPlayer()
	{
		return (Player) sender;
	}
	
	public Player getTarget()
	{
		return (Player) target;
	}
	
	public void SetCommandEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}
	
	
	public String solver(String text, String[] args)
	{
		Config Config = new Config("Config.yml");
		
		if(args != null)
		{
			for(Integer i = 0; i < args.length; i++)
			{
				if(text.contains("{"+i+"}") && args[i] != null)
				{
					text = text.replace("{"+i+"}", args[i]);
				}
			}
		}
		
		if(text.contains("&"))
		{
			text = text.replace("&", "§");
		}
		
		if(text.contains("%player%"))
		{
			text = text.replace("%player%", getPlayer().getDisplayName());
		}
		
		if(text.contains("%cmd%"))
		{
			text = text.replace("%cmd%", this.commandName);
		}
		if(text.contains("%accept%"))
		{
			text = text.replace("%accept%", Config.FileConfig.getString("commands.acceptCmd"));
		}
		if(text.contains("%deny%"))
		{
			text = text.replace("%deny%", Config.FileConfig.getString("commands.denyCmd"));
		}
		
		if(text.contains("%invite%"))
		{
			text = text.replace("%invite%", Config.FileConfig.getString("commands.inviteCmd"));
		}
		
		if(text.contains("%cancel%"))
		{
			text = text.replace("%cancel%", Config.FileConfig.getString("commands.cancelCmd"));
		}
		if(text.contains("%saveinv%"))
		{
			text = text.replace("%saveinv%", Config.FileConfig.getString("commands.saveinv"));
		}
		
		if(text.contains("%loadinv%"))
		{
			text = text.replace("%loadinv%", Config.FileConfig.getString("commands.loadinv"));
		}
		
		if(text.contains("%prefix%"))
		{
			text = text.replace("%prefix%", Main.instance.Solver((String) Config.FileConfig.getString("config.prefix")));
		}
		
		if(text.contains("%sender%"))
		{
			Player Target = PvpCommand.GetTargetBySender(getPlayer());
			Player Sender = PvpCommand.GetTargetBySender(Target);
			text = text.replace("%sender%", Sender.getDisplayName());
		}
		
		if(text.contains("%betmax%"))
		{
			text = text.replace("%betmax%", Config.FileConfig.getString("config.BetPriceMax"));
		}
		
		if(text.contains("%betmin%"))
		{
			text = text.replace("%betmin%", Config.FileConfig.getString("config.BetPriceMin"));
		}
		
		if(text.contains("%bet%"))
		{
			text = text.replace("%bet%", aposta.toString());
		}
		
		if(text.contains("%target%"))
		{
			Player Target = PvpCommand.GetTargetBySender(getPlayer());
			text = text.replace("%target%", Target.getDisplayName());
		}
		return text;
	}
	
	
	
	public String GetValue(String Text)
	{
		Config Config = new Config("Config.yml");
		return Config.FileConfig.getString(Text);
	}
}