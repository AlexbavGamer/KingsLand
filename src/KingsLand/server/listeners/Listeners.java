package KingsLand.server.listeners;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import KingsLand.server.Main;
import KingsLand.server.commands.PvpCommand;
import net.milkbowl.vault.economy.EconomyResponse;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Listeners implements Listener 
{
	public static HashMap<Player, Boolean> RestaurarInventorio = new HashMap<Player, Boolean>();
	public Player GetTargetBySender(Player p)
	{
		return PvpCommand.GetTargetBySender(p);
	}
	
	public Boolean CanInventoryBeRestaured(Player p)
	{
		if(RestaurarInventorio.containsKey(p))
		{
			return RestaurarInventorio.get(p);
		}
		return false;
	}
	
	@EventHandler
	public void OnPlayerJoin(PlayerJoinEvent e)
	{
		if(e != null)
		{
			SetPlayerDisplayName(e.getPlayer());
		}
	}
	
	@EventHandler
	public void OnPlayerDie(PlayerDeathEvent e)
	{
		Player Killed = (Player) e.getEntity();
		Player Killer = (Player) Killed.getKiller();
		
		
		Double Aposta = ObterAposta(Killer);
		if(Aposta == 0) return;
		
		if(Killed != null)
		{
			EconomyResponse r = Main.econ.depositPlayer(Killer, Aposta);
			EconomyResponse r2 = Main.econ.withdrawPlayer(Killed, Aposta);

			if(r.transactionSuccess() && r2.transactionSuccess())
			{
				RestaurarInventorio.put(Killed, true);
				Killer.sendMessage(Solver("&2[&4PvP&2]&r você matou " + Killed.getDisplayName() + " e ganhou " + Main.econ.format(r.amount)));
				Killed.sendMessage(Solver("&2[&4PvP&2]&r você morreu por " + Killer.getDisplayName() + " e perdeu " + Main.econ.format(r.amount)));
				Killed.spigot().respawn();
				PvpCommand.Apostas.remove(Killed, Aposta);
			}
			e.getDrops().clear();
		}
	}
	
	@EventHandler
	public void OnPlayerSpawn(PlayerRespawnEvent e)
	{
		if(CanInventoryBeRestaured(e.getPlayer()))
		{
			PvpCommand.Apostas.remove(e.getPlayer(), ObterAposta(e.getPlayer()));
			Main.instance.LoadPlayerInventory(e.getPlayer());
			e.getPlayer().sendMessage(Main.instance.Solver("&2[&4PvP&2]&r Inventorio restaurado."));
			RestaurarInventorio.remove(e.getPlayer());
			e.getPlayer().spigot().respawn();
		}
	}
	
	
	public void SetPlayerDisplayName(Player p)
	{
		PermissionUser user = PermissionsEx.getUser(p);
		
		if(user.getPrefix() == null) { return; }
		String Result = Solver(user.getPrefix());
		
		p.setCustomName(Result + " - " + p.getName());
		p.setPlayerListName(Result + " - " + p.getName());
	}
	
	public String Solver(String text)
	{
		text = text.replace("&", "§");
		return text;
	}
	
	public Double ObterAposta(Player p)
	{
		Double aposta = 0.0;
		if(PvpCommand.Apostas.containsKey(p))
		{
			aposta = PvpCommand.Apostas.get(p);
		}
		return aposta;
	}
}
