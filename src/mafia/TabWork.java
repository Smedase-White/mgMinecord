package mafia;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import mafia.Data.*;

public class TabWork implements TabCompleter
{
	String[] mf_leadCommand = new String[] {"start" , "end", "roles", "take", "add", "del", "clear", "list", "night", "next", "log", "kill", "setStartPos", "setDiedPos", "help", "fix"};
	String[] mf_playerCommand = new String[] {"active"};
	private Data data;
	
	public TabWork(Data data)
	{
		this.data = data;
	}
	
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args)
	{
		List<String> list = new ArrayList<>();
		Player p = (Player) sender;
		switch(cmd.getName())
		{
			case("mafia"):
			{
				if (args.length == 1)
				{
					if (p.hasPermission("mgMinecord.mafia.lead"))
					{
						for (int i = 0; i < mf_leadCommand.length; i++) 
						{
							if (contains(mf_leadCommand[i], args[0])) list.add(mf_leadCommand[i]);
						}
					}
					if (p.hasPermission("mgMinecord.mafia.player"))
					{
						for (int i = 0; i < mf_playerCommand.length; i++)
						{
							if (contains(mf_playerCommand[i], args[0])) list.add(mf_playerCommand[i]);		
						}
					}
				}
				if (args.length > 1)
				{
					if (p.hasPermission("mgMinecord.mafia.lead"))
					{
						switch(args[0])
						{
							case("kill"):
							{
								for (Role r : data.roles)
								{
									if (r.who() != allRoles.died) if (contains(r.p.getDisplayName(), args[1])) list.add(r.p.getDisplayName());
								}
								break;
							}
							case("del"):
							{
								for (Player ap : data.ps)
								{
									if (contains(ap.getDisplayName(), args[1])) list.add(ap.getDisplayName());
								}
								break;
							}
							case("add"):
							{
								boolean tempB;
								for (Player ap : Bukkit.getOnlinePlayers())
								{
									tempB = true;
									for (Player _ap : data.ps) if (_ap == ap) { tempB = false; break;}
									if (tempB) if (contains(ap.getDisplayName(), args[1])) list.add(ap.getDisplayName());
								}
								break;
							}
							case("fix"):
							{
								for (Player ap : Bukkit.getOnlinePlayers())
								{
									if (contains(ap.getDisplayName(), args[1])) list.add(ap.getDisplayName());
								}
								break;
							}
							case("help"):
							{
								if (contains("lead", args[1])) list.add("lead");
								if (contains("player", args[1])) list.add("player");
								break;
							}
						}
					}
					if (p.hasPermission("mgMinecord.mafia.player"))
					{
						switch(args[0])
						{
							case("active"):
							{
								for (Role r : data.roles)
								{
									if (r.who() != allRoles.died) if (contains(r.p.getDisplayName(), args[args.length - 1])) list.add(r.p.getDisplayName());
								}
								break;
							}
							case("help"):
							{
								if (contains("player", args[1])) list.add("player");
								break;
							}
						}
					}
				}
				break;
			}
		}
		return list;
	}
	
	private static boolean contains(String examinee, String pattern) {
	    if (pattern.length() > examinee.length()) return false; 
	    String s1 = " " + examinee + " ";
	    String s2 = "\\Q" + pattern + "\\E";
	    if(s1.split(s2).length > 1) return true;
	    return false;
	  };
}
