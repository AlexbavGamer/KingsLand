package KingsLand.server.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import KingsLand.server.CommandModule;
import KingsLand.server.Main;
import KingsLand.server.classmanager.classes.Config;

public class PvpCommand extends CommandModule 
{
	public static Map<Player, Player> InPvp = new HashMap<Player, Player>();
	public static HashMap<Player, Double> Apostas = new HashMap<Player, Double>();
	public Config Config = new Config("Config.yml");
	public PvpCommand(String commandName, int minArgs, int maxArgs) 
	{
		super(commandName, minArgs, maxArgs);
		SetCommandEnabled(true);
	}
	
	public static Player GetTargetBySender(Player p)
	{
		Player result = null;
		{
			result = InPvp.get(p);
		}
		return result;
	}
	


	@Override
	public void run(CommandSender sender, String[] args) 
	{
		if(args.length != 0)
		{
			if(args[0].equalsIgnoreCase(solver("%saveinv%",null)))
			{
				if(args.length < 2)
				{
					getSender().sendMessage(solver(Config.FileConfig.getString("config.messages.saveinv.usage"),null));
					return;
				}
				
				Player p = getPlayer().getServer().getPlayer(args[1].toString());
				if(p == null)
				{
					getSender().sendMessage(solver(Config.FileConfig.getString("config.messages.saveinv.error"),args));
					return;
				}
				Main.instance.SavePlayerInventory(p);
				getSender().sendMessage(solver(Config.FileConfig.getString("config.messages.saveinv.success"),args));
			}
			else if(args[0].equalsIgnoreCase(solver("%loadinv%",null)))
			{
				if(args.length < 2)
				{ 
					getSender().sendMessage(solver(Config.FileConfig.getString("config.messages.loadinv.usage"),null));
					return; 
				}
				
				target = getPlayer().getServer().getPlayer(args[1].toString());
				if(target == null)
				{
					getSender().sendMessage(solver(Config.FileConfig.getString("config.messages.loadinv.error"),args));
					return;
				}
				Main.instance.LoadPlayerInventory(target);
				getSender().sendMessage(solver(Config.FileConfig.getString("config.messages.loadinv.success"),args));
			}
			
			else if(args[0].equalsIgnoreCase(solver("%accept%",null)))
			{
				if(GetTargetBySender(getTarget()) == getPlayer())
				{
					sender.sendMessage(solver("config.messages.accept.error",args));
					return;
				}
				if(InPvp.containsKey(getPlayer()))
				{
					if(GetTargetBySender(getPlayer()) != null)
					{
						Player Sender = GetTargetBySender(getPlayer());
						Player Target = GetTargetBySender(getPlayer());
						sender.sendMessage(solver(Config.FileConfig.getString("config.messages.accept.accepted"), args));
						GetTargetBySender(getPlayer()).sendMessage(solver(Config.FileConfig.getString("config.messages.accept.acceptedtarget"),null));
						GetTargetBySender(getPlayer()).teleport(getPlayer().getLocation());
						if(Sender.getGameMode() != GameMode.SURVIVAL)
						{
							Sender.setGameMode(GameMode.SURVIVAL);
						}

						if(Target.getGameMode() != GameMode.SURVIVAL)
						{
							Target.setGameMode(GameMode.SURVIVAL);
						}
						InPvp.remove(GetTargetBySender(getPlayer()));
						InPvp.remove(getPlayer());
					}
				}
				else
				{
					sender.sendMessage(solver(Config.FileConfig.getString("config.messages.accept.noconvited"),null));
					return;
				}
			}
			else if(args[0].equalsIgnoreCase(solver("%deny%",null)))
			{
				
				if(InPvp.containsKey(getPlayer()))
				{
					if(GetTargetBySender(getPlayer()) != null)
					{
						sender.sendMessage(solver(Config.FileConfig.getString("config.messages.deny.deny"),null));
						GetTargetBySender(getPlayer()).sendMessage(solver(Config.FileConfig.getString("config.messages.deny.denyed"),null));
						InPvp.remove(GetTargetBySender(getPlayer()));
						InPvp.remove(getPlayer());
					}
				}
				else
				{
					sender.sendMessage(solver(Config.FileConfig.getString("config.messages.deny.error"),null));
				}
			}
			else if(args[0].equalsIgnoreCase(solver("%cancel%",null)))
			{
				if(InPvp.containsKey(getPlayer()))
				{
					sender.sendMessage(solver("%prefix% você não tem nenhum convite de duelo para cancelar.",null));
					if(GetTargetBySender(getPlayer()) != null)
					{
						sender.sendMessage(solver("%prefix% você cancelou o pedido de duelo de %player%",null));
						getTarget().sendMessage(solver("%prefix% %player% cancelou o seu pedido de duelo de %target%",null));
						InPvp.remove(GetTargetBySender(getPlayer()));
						InPvp.remove(getPlayer());
					}
					return;
				}


			}
			else if(args[0].equalsIgnoreCase(solver("%invite%", null)))
			{

				if(args.length < 3)
				{
					getPlayer().sendMessage(solver("%prefix% &4Use: &2/%cmd% &c%invite%&2/&c%accept%&2/&c%deny%&2/&c%cancel%",args));
					return;
				}
				else
				{
					if(getPlayer().getServer().getPlayer(args[1].toString()) != null)
					{
						target = getPlayer().getServer().getPlayer(args[1].toString());
						aposta = Double.parseDouble(args[2].toString());
					}
					else
					{
						getPlayer().sendMessage(solver("%prefix% O Jogador %arg-1% não está online",args));
						return;
					}
				}
				
				if(aposta < Config.FileConfig.getInt("config.BetPriceMin") || aposta > Config.FileConfig.getInt("config.BetPriceMax"))
				{
					getSender().sendMessage(solver("%prefix% -> Aposta Minima: %betmin%",args));
					getSender().sendMessage(solver("%prefix% -> Aposta Maxima: %betmax%",args));
					return;
				}
				if(target.isOnline() && target != null)
				{
					if(InPvp.containsValue(getTarget()))
					{
						getPlayer().sendMessage(solver("%prefix% você não pode enviar convite a %target% porque ele já esta convidado por alguem.",args));
						return;
					}
					
					if(getPlayer() == getTarget())
					{
						getPlayer().sendMessage(solver("%prefix% você não pode enviar convite a si mesmo.",args));
						return;
					}
					
					if(Main.econ.has(getPlayer(), aposta))
					{
						Apostas.put(getTarget(), aposta);
						Apostas.put(getPlayer(), aposta);
						InPvp.put(getPlayer(), getTarget());
						InPvp.put(getTarget(), getPlayer());
						Main.instance.SavePlayerInventory(getPlayer());
						Main.instance.SavePlayerInventory(getTarget());
						
						getTarget().sendMessage(solver(Config.FileConfig.getString("config.messages.invite.send.1"), args));
						getTarget().sendMessage(solver(Config.FileConfig.getString("config.messages.invite.send.2"), args));
						getTarget().sendMessage(solver(Config.FileConfig.getString("config.messages.invite.send.3"), args));
						getTarget().sendMessage(solver(Config.FileConfig.getString("config.messages.invite.send.4"), args));
						getTarget().sendMessage(solver(Config.FileConfig.getString("config.messages.invite.send.5"), args));
						getTarget().sendMessage(solver(Config.FileConfig.getString("config.messages.invite.send.6"), args));
						
						getPlayer().sendMessage(solver(Config.FileConfig.getString("config.messages.invite.send.7"), args));
					}
					else
					{
						Double money = Main.econ.getBalance(getPlayer());
						Double result = money + aposta;
						getPlayer().sendMessage(solver("%prefix% -> &cFalha no envio do &6Duelo&c.", args));
						getPlayer().sendMessage(solver("%prefix% -> &cVocê não tem &4" + Main.econ.format(result) + "&c para enviar o pedido de &6Duelo&c.", args));
						getPlayer().sendMessage(solver("%prefix% -> &cFalha no envio do &6Duelo&c.", args));
					}
				}
				else
				{
					getPlayer().sendMessage(solver("%prefix% &4Use: &2/%cmd% &c%invite%&2/&c%accept%&2/&c%deny%&2/&c%cancel%",null));
				}
			}
			else
			{
				errorMsg(sender);
			}
		}
		else
		{
			errorMsg(sender);
		}
	}

	@Override
	public void errorMsg(CommandSender sender) 
	{
		getPlayer().sendMessage(solver("%prefix% &4Help Page", null));
		getPlayer().sendMessage(solver("&4/&2%cmd% &c%invite%&2 [Player] [Bet]",null));
		getPlayer().sendMessage(solver("&4/&2%cmd% &c%accept%&2",null));
		getPlayer().sendMessage(solver("&4/&2%cmd% &c%deny%&2",null));
		getPlayer().sendMessage(solver("&4/&2%cmd% &c%cancel%&2",null));
		if(getPlayer().hasPermission(commandName+".saveinv"))
		{
			getPlayer().sendMessage(solver("&4/&2%cmd% &c%saveinv%&2",null));
		}
		
		if(getPlayer().hasPermission(commandName+".loadinv"))
		{
			getPlayer().sendMessage(solver("&4/&2%cmd% &c%loadinv%&2",null));
		}
		getPlayer().sendMessage(solver("%prefix% &4Help Page",null));
	}
}
