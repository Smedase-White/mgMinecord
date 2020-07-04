package mg.main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import mg.main.Data.ActivePlayer;
import mg.main.Data.games;

public class Commands implements CommandExecutor
{
	private static Data data;
	private static mafia.Data dataM;
	public Commands(Data data, mafia.Data dataM)
	{
		Commands.data = data;
		Commands.dataM = dataM;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		Player p = (Player) sender;
		if (!p.hasPermission("mgMinecord.player"))
		{
			
			return true;
		}
		switch(args[0])
		{
			case("mafia"):
			{
				ActivePlayer ap = data.getActivePlayer(p);
				if (ap != null)
				{
					
					return true;
				}
				Data.allPlayers.add(data.new ActivePlayer(p, games.Mafia));
				dataM.ps.add(p);
				if (dataM.startPos != null) p.teleport(dataM.startPos);
				break;
			}
			case("quit"):
			{
				quit(p);
			}
		}
		return true;
	}
	
	public static void quit(Player p)
	{
		ActivePlayer ap = data.getActivePlayer(p);
		switch (ap.game)
		{
			case Mafia:
			{
				p.removePotionEffect(PotionEffectType.BLINDNESS);
				p.removePotionEffect(PotionEffectType.SLOW);
				p.removePotionEffect(PotionEffectType.SLOW_DIGGING);
				if (dataM.isPlay)
				{
					Data.teamMafia.removeEntry(p.getName());
					dataM.roles.remove(dataM.takeRole(p));
				}
				p.teleport(ap.back);
				Data.allPlayers.remove(ap);
				dataM.ps.remove(p);
			}
			case Other:
			{
				
			}
		}
	}
}
