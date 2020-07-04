package mafia;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import mafia.Data.Role;
import mafia.Data.allRoles;

public class CommandsMafiaChat implements CommandExecutor
{
	private Data data;
	private FileConfiguration lang;
	
	public CommandsMafiaChat(Data data, FileConfiguration lang)
	{
		this.data = data;
		this.lang = lang;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		Player p = (Player) sender;
		if (!p.hasPermission("mgMinecord.mafia.player")) return true;
		Role tempR = data.takeRole(p);
		if ((tempR.who() == allRoles.noone) || (tempR.who() == allRoles.died)) return true;
		StringBuilder sb = new StringBuilder();
	    for(int i = 0; i < args.length; i++){
	        sb.append(args[i] + " ");
	    }
	    if (!data.isPlay) return true;
	    ArrayList<Role> roles = data.roles;
	    if (tempR.isMute)
	    {
	    	
	    	return true;
	    }
		if (data.isNight)
		{
			if (tempR.who() != data.turns[data.turn])
			{
				p.sendMessage(lang.getString("messages.mafia.notActive").replace("&", "§"));
				return true;
			}
			for (int i = 0; i < data.roles.size(); i++)
			{
				if (roles.get(i).who() == tempR.who())
				{
					roles.get(i).p.sendMessage(p.getDisplayName() + ": " + sb);
				}
			}
		}
		else
		{
			for (int i = 0; i < roles.size(); i++)
			{
				roles.get(i).p.sendMessage(p.getDisplayName() + ": " + sb);
			}
		}
		data.leader.p.sendMessage(tempR.p.getDisplayName() + ": " + sb);
		return true;
	}
}
