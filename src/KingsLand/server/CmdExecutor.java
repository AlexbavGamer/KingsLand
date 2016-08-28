package KingsLand.server;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import KingsLand.server.classmanager.classes.Config;
public class CmdExecutor implements CommandExecutor
{
	Config Config = new Config("Config.yml");
    public boolean onCommand(CommandSender sender, Command cmd, String commandName, String[] args)
    {
    	Main.commands.get(commandName.toLowerCase()).sender = sender;
        //Checks if the commandName is one of yours.
        if(Main.commands.containsKey(commandName.toLowerCase()))
        {
            CommandModule mod = Main.commands.get(commandName.toLowerCase());
            
            //If so, it will check if they have the permission.
            if(sender.hasPermission("KingsLand." + (commandName.toLowerCase())))
            {
                //Get the module so you can access its variables.
                //Checks if the amount of arguments are within the minimum amount and maximum amount.
                if(args.length >= mod.minArgs && args.length <= mod.maxArgs)
                {
                	if(mod.enabled() == true)
                	{
                        //If so, it will run the command.
                        mod.run(sender, args);
                	}
                	else
                	{
                		String InvalidCommand = (String) Config.GetValue("DisabledCommand");
                		sender.sendMessage(mod.solver(InvalidCommand, args));
                	}
                }else
                {
                    //If not, it will send the player some sass.
                    mod.errorMsg(sender);
                }
            }
            else
            {
            	String NoPermission = (String) Config.GetValue("NoPermission");
                sender.sendMessage(mod.solver(NoPermission, args));
            }
        }
       
        return false;
    }
}
