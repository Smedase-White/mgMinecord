package mafia;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mafia.Data.*;

import java.util.ArrayList;
import java.lang.Math;

import mg.main.mgMain;

public class Commands implements CommandExecutor
{
	private mgMain plugin;
	private Data data;
	
	public Commands(mgMain plugin, Data data)
	{
		this.plugin = plugin;
		this.data = data;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		Player p = (Player) sender;
		switch(cmd.getName())
		{
			case("mafia"):
			{
				if (args.length == 0)
				{
					p.sendMessage("/mafia help (player/lead)" + " - " + plugin.getConfig().getString("messages.mafia.description_help").replace("&", "§"));
					return true;
				}
				
				if (p.hasPermission("mgMinecord.mafia.lead"))
				{
					switch(args[0])
					{
						case("help"):
						{
							if (args.length < 2)
							{
								p.sendMessage(plugin.getConfig().getString("messages.mafia.description_commandPlayer").replace("&", "§"));
								p.sendMessage("/mafia active" + " - " + plugin.getConfig().getString("messages.mafia.description_active").replace("&", "§"));
								p.sendMessage("/mafiachat" + " - " + plugin.getConfig().getString("messages.mafia.description_chat").replace("&", "§"));
								return true;
							}
							if (args[1].equals("player"))
							{
								p.sendMessage(plugin.getConfig().getString("messages.mafia.description_commandPlayer").replace("&", "§"));
								p.sendMessage("/mafia active" + " - " + plugin.getConfig().getString("messages.mafia.description_active").replace("&", "§"));
								p.sendMessage("/mafiachat" + " - " + plugin.getConfig().getString("messages.mafia.description_chat").replace("&", "§"));
								return true;
							}
							if (args[1].equals("lead"))
							{
								StringBuilder sb = new StringBuilder();
								for (int i = 0; i < data.countRoles; i++) sb.append("["+plugin.getConfig().getString("messages.mafia." + data.all[i]).replace("&", "§") + "] ");
								p.sendMessage(plugin.getConfig().getString("messages.mafia.description_commandLead").replace("&", "§"));
								p.sendMessage("/mafia start" + " - " + plugin.getConfig().getString("messages.mafia.description_start").replace("&", "§"));
								p.sendMessage("/mafia end" + " - " + plugin.getConfig().getString("messages.mafia.description_end").replace("&", "§"));
								p.sendMessage("/mafia " +  sb + "- " + plugin.getConfig().getString("messages.mafia.description_roles").replace("&", "§"));
								p.sendMessage("/mafia take [count] [kit]" + " - " + plugin.getConfig().getString("messages.mafia.description_roles").replace("&", "§"));
								p.sendMessage("/mafia add <player>" + " - " + plugin.getConfig().getString("messages.mafia.description_add").replace("&", "§"));
								p.sendMessage("/mafia del <player>" + " - " + plugin.getConfig().getString("messages.mafia.description_del").replace("&", "§"));
								p.sendMessage("/mafia list" + " - " + plugin.getConfig().getString("messages.mafia.description_list").replace("&", "§"));
								p.sendMessage("/mafia night" + " - " + plugin.getConfig().getString("messages.mafia.description_night").replace("&", "§"));
								p.sendMessage("/mafia next" + " - " + plugin.getConfig().getString("messages.mafia.description_next").replace("&", "§"));
								p.sendMessage("/mafia log" + " - " + plugin.getConfig().getString("messages.mafia.description_log").replace("&", "§"));
								p.sendMessage("/mafia kill <Player>" + " - " + plugin.getConfig().getString("messages.mafia.description_kill").replace("&", "§"));
								p.sendMessage("/mafia setDiedPos" + " - " + plugin.getConfig().getString("messages.mafia.description_setDiedPos").replace("&", "§"));
								p.sendMessage("/mafia setStartPos" + " - " + plugin.getConfig().getString("messages.mafia.description_setStartPos").replace("&", "§"));
								return true;
							}
							p.sendMessage(plugin.getConfig().getString("messages.errors.invalidArgument").replace("&", "§"));
							return true;
						}
						case ("start"):
						{
							if (data.isPlay)
							{
								
								return true;
							}
							ArrayList<Player> ps = data.ps;
							int temp = 0;
							for (int add : data.values)
								temp += add;
							if (ps.size() != temp)
							{
								p.sendMessage(plugin.getConfig().getString("messages.mafia.error_players_roles").replace("&", "§"));
								return true;
							}
							while (data.roles.size() > 0)
							{
								data.roles.remove(0);
							}
							for (int i = 0; i < ps.size(); i++)
							{
								int rand = (int)Math.random() * ps.size();
								if (rand != i)
								{
									Player tempP = ps.get(i);
									data.ps.set(i, ps.get(rand));
									data.ps.set(rand, tempP);
								}
							}
							temp = 0;
							data.leader = data.new Lead(p);
							for (int r = 0; r < data.countRoles; r++)
							{
								for (int i = 0; i < data.values[r]; i++)
								{
									Player tempP = ps.get(temp);
									tempP.sendMessage(plugin.getConfig().getString("messages.mafia.text_" + data.all[r]).replace("&", "§"));
									data.leader.p.sendMessage(tempP.getDisplayName() + " - " + plugin.getConfig().getString("messages.mafia." + data.all[r]).replace("&", "§"));
									switch(r)
									{
										case(0): { data.roles.add(data.new Mafia(tempP, temp)); break; }
										case(1): { data.roles.add(data.new Resident(tempP, temp)); break; }
										case(2): { data.roles.add(data.new Doctor(tempP, temp)); break; }
										case(3): { data.roles.add(data.new Commissioner(tempP, temp)); break; }
										case(4): { data.roles.add(data.new Don(tempP, temp)); break; }
										case(5): { data.roles.add(data.new Fallen(tempP, temp)); break; }
										case(6): { data.roles.add(data.new Jealous(tempP, temp)); break; }
										case(7): { data.roles.add(data.new Maniac(tempP, temp)); break; }
										case(8): { data.roles.add(data.new Journalist(tempP, temp)); break; }
										case(9): { data.roles.add(data.new Fatale(tempP, temp)); break; }
										case(10): { data.roles.add(data.new Witness(tempP, temp)); break; }
									}
									tempP.teleport(data.startPos);
									plugin.invisibleNick(tempP);
									temp++;
								}
							}
							p.sendMessage(plugin.getConfig().getString("messages.mafia.success_start").replace("&", "§"));
							p.sendMessage(plugin.getConfig().getString("messages.mafia.success_give").replace("&", "§"));
							data.isPlay = true;
							data.day = 0;
							data.turn = 0;
							return true;
						}
						case ("end"):
						{
							if (!data.isPlay)
							{
								
								return true;
							}
							while (data.roles.size() > 0)
							{
								plugin.visibleNick(data.roles.get(0).p);
								data.awake(data.roles.get(0).p);
								data.roles.remove(0);
							}
							data.day = 0;
							data.turn = 0;
							data.isNight = false;
							data.isPlay = false;
							data.leader.p.sendMessage(plugin.getConfig().getString("messages.mafia.success_end").replace("&", "§"));
							return true;
						}
						case ("roles"):
						{
							if (args.length > data.countRoles + 1) 
							{
								p.sendMessage(plugin.getConfig().getString("messages.errors.invalidArguments").replace("&", "§"));
								return true;
							}
							p.sendMessage(plugin.getConfig().getString("messages.mafia.success_roles").replace("&", "§"));
							for (int i = 1; i < args.length; i++) data.values[i - 1]= Integer.parseInt(args[i]);
							for (int i = args.length - 1; i < data.countRoles; i++) data.values[i] = 0;
							StringBuilder sb = new StringBuilder();
							for (int i = 0; i < data.countRoles; i++) sb.append(plugin.getConfig().getString("messages.mafia." + data.all[i]) + " " + data.values[i] + "; ");
							p.sendMessage("" + sb);
							return true;
						}
						case ("take"):
						{
							if (args.length == 1)
							{
								java.util.List<String> list = plugin.getConfig().getStringList("kits.values");
								for (int i = 0; i < list.size(); i++) p.sendMessage(list.get(i));
								return true;
							}
							if (args.length == 2)
							{
								java.util.List<String> list = plugin.getConfig().getStringList("kits." + args[1]);
								for (int i = 0; i < list.size(); i++) p.sendMessage(list.get(i));
								return true;
							}
							int k = Integer.parseInt(args[2]);
							java.util.List<String> list = plugin.getConfig().getStringList("kits." + args[1]);
							if (k >= list.size())
							{
								
								return true;
							}
							String[] numbers = list.get(k).split("[ ]");
							for (int i = 0; i < data.countRoles; i++)
								data.values[i] = Integer.parseInt(numbers[i]);
							StringBuilder sb = new StringBuilder();
							for (int i = 0; i < data.countRoles; i++) sb.append(plugin.getConfig().getString("messages.mafia." + data.all[i]) + " " + numbers[i] + "; ");
							p.sendMessage("" + sb);
							return true;
						}
						case ("add"):
						{
							if (args.length < 2) 
							{
								p.sendMessage(plugin.getConfig().getString("messages.errors.notEnoughArguments").replace("&", "§"));
								return true;
							}
							Player ap = Bukkit.getPlayer(args[1]);
							if (ap == null)
							{
								p.sendMessage(plugin.getConfig().getString("messages.errors.notFoundPlayer").replace("&", "§"));
								return true;
							}
							for (Player _ap : data.ps) if (_ap == ap)
							{
								
								return true;
							}
							data.ps.add(ap);
							ap.teleport(data.startPos);
							p.sendMessage(plugin.getConfig().getString("messages.mafia.success_add").replace("&", "§"));
							return true;
						}
						case ("del"):
						{
							if (args.length < 2) 
							{
								p.sendMessage(plugin.getConfig().getString("messages.errors.notEnoughArguments").replace("&", "§"));
								return true;
							}
							Player ap = Bukkit.getPlayer(args[1]);
							if (ap == null)
							{
								p.sendMessage(plugin.getConfig().getString("messages.errors.notFoundPlayer").replace("&", "§"));
								return true;
							}
							int temp = data.ps.size();
							data.ps.remove(ap);
							if (temp == data.ps.size())
							{
								p.sendMessage(plugin.getConfig().getString("messages.mafia.error_active").replace("&", "§"));
								return true;
							}
							p.sendMessage(plugin.getConfig().getString("messages.mafia.success_del").replace("&", "§"));
							return true;
						}
						case ("clear"):
						{
							while (data.ps.size() > 0) data.ps.remove(0);
							return true;
						}
						case ("list"):
						{
							int temp = data.ps.size();
							if (temp == 0)
							{
								p.sendMessage(plugin.getConfig().getString("messages.mafia.empty_list").replace("&", "§"));
								return true;
							}
							p.sendMessage(plugin.getConfig().getString("messages.mafia.list").replace("&", "§"));
							for (int i = 0; i < temp - 1; i++) p.sendMessage(data.ps.get(i).getDisplayName() + ", ");
							p.sendMessage(data.ps.get(temp - 1).getDisplayName() + ";");
							return true;
						}
						case ("night"):
						{
							if (!data.isPlay)
							{
								p.sendMessage(plugin.getConfig().getString("messages.errors.notPlayer").replace("&", "§"));
								return true;
							}
							if (data.isNight)
							{
								return true;
							}
							while (data.log.size() > 0) data.log.remove(0);
							for (Role r : data.roles) 
							{
								r.isMute = false;
								r.p.sendMessage(plugin.getConfig().getString("messages.mafia.turn_night").replace("&", "§"));
								
							}
							p.sendMessage(plugin.getConfig().getString("messages.mafia.turn_night").replace("&", "§"));
							data.miss = false;
							data.turn = 0;
							data.isNight = true;
							if (data.day == 0) data.date();
							else data.night();
							return true;
						}
						case ("next"):
						{
							if (!data.isPlay)
							{
								p.sendMessage(plugin.getConfig().getString("messages.errors.notPlayer").replace("&", "§"));
								return true;
							}
							if (!data.isNight)
							{
								p.sendMessage(plugin.getConfig().getString("messages.mafia.day").replace("&", "§"));
								return true;
							}
							data.turn++;
							if (data.turn == data.countTurns)
							{
								data.day();
								return true;
							}
							while (data.day % data.rarity[data.turn] != 0)
							{
								data.turn++;
							}
							if (data.day == 0) data.date();
							else data.night();
							return true;
						}
						case ("log"):
						{
							if (!data.isPlay)
							{
								p.sendMessage(plugin.getConfig().getString("messages.errors.notPlayer").replace("&", "§"));
								return true;
							}
							for (int i = 0; i < data.log.size(); i++) p.sendMessage(data.log.get(i));
						}
						case ("kill"):
						{
							if (!data.isPlay) 
							{
								p.sendMessage(plugin.getConfig().getString("messages.errors.notPlayer").replace("&", "§"));
								return true;
							}
							if (args.length == 1) 
							{
								p.sendMessage(plugin.getConfig().getString("messages.errors.notEnoughArguments").replace("&", "§"));
								return true;
							}
							Player ap = Bukkit.getPlayer(args[1]);
							if (ap == null)
							{
								p.sendMessage(plugin.getConfig().getString("messages.errors.notFoundPlayer").replace("&", "§"));
								return true;
							}
							Role tempR = data.takeRole(ap);
							if (tempR.who() == allRoles.noone)
							{
								p.sendMessage(plugin.getConfig().getString("messages.errors.notFoundPlayer").replace("&", "§"));
								return true;
							}
							data.Kill(tempR.number);
							return true;
						}
						case ("setDiedPos"):
						{
							p.sendMessage(plugin.getConfig().getString("messages.mafia.success_setDiedPos").replace("&", "§"));
							data.diedPos = p.getLocation();
							return true;
						}
						case ("setStartPos"):
						{
							p.sendMessage(plugin.getConfig().getString("messages.mafia.success_setStartPos").replace("&", "§"));
							data.startPos = p.getLocation();
							return true;
						}
						case ("fix"):
						{
							if (args.length == 1) 
							{
								p.sendMessage(plugin.getConfig().getString("messages.errors.notEnoughArguments").replace("&", "§"));
								return true;
							}
							Player ap = Bukkit.getPlayer(args[1]);
							if (ap == null)
							{
								p.sendMessage(plugin.getConfig().getString("messages.errors.notFoundPlayer").replace("&", "§"));
								return true;
							}
							data.awake(ap);
							plugin.visibleNick(ap);
							return true;
						}
						default:
						{
							data.notExistCommand = true;
							break;
						}
					}
				}
				
				if (p.hasPermission("mgMinecord.mafia.player"))
				{
					switch(args[0])
					{
						case("help"):
						{
							if (args.length < 2)
							{
								p.sendMessage(plugin.getConfig().getString("messages.mafia.description_commandPlayer").replace("&", "§"));
								p.sendMessage("/mafia active" + " - " + plugin.getConfig().getString("messages.mafia.description_active").replace("&", "§"));
								p.sendMessage("/mafiachat" + " - " + plugin.getConfig().getString("messages.mafia.description_chat").replace("&", "§"));
								
								return true;
							}
							if (args[1].equals("player"))
							{
								p.sendMessage(plugin.getConfig().getString("messages.mafia.description_commandPlayer").replace("&", "§"));
								p.sendMessage("/mafia active" + " - " + plugin.getConfig().getString("messages.mafia.description_active").replace("&", "§"));
								p.sendMessage("/mafiachat" + " - " + plugin.getConfig().getString("messages.mafia.description_chat").replace("&", "§"));
								
								return true;
							}
							if (args[1].equals("lead"))
							{
								p.sendMessage(plugin.getConfig().getString("messages.errors.notPermission").replace("&", "§"));
								return true;
							}
							p.sendMessage(plugin.getConfig().getString("messages.errors.invalidArgument").replace("&", "§"));
							return true;
						}
						case("active"):
						{
							if (!data.isPlay) 
							{
								p.sendMessage(plugin.getConfig().getString("messages.errors.notPlayer").replace("&", "§"));
								return true;
							}
							if (args.length == 1) 
							{
								p.sendMessage(plugin.getConfig().getString("messages.errors.notEnoughArguments").replace("&", "§"));
								return true;
							}
							Role tempR = data.takeRole(p);
							if (tempR.who() == allRoles.noone)
							{
								p.sendMessage(plugin.getConfig().getString("messages.errors.notPlayer").replace("&", "§"));
								return true;
							}
							int temp = 0;
							boolean tempB = false;
							allRoles[] turns = data.turns;
							for (int l = 0; l < data.countRoles; l++)
							{
								if (turns[l] == tempR.who())
								{
									tempB = true;
									temp = l;
									break;
								}
							}
							if (tempB)
							{
								if (data.canActive[temp])
								{
									if (args.length == 2)
									{
										Player ap = Bukkit.getPlayer(args[1]);
										if (ap == null)
										{
											p.sendMessage(plugin.getConfig().getString("messages.errors.notFoundPlayer").replace("&", "§"));
											return true;
										}
										Role tempR2 = data.takeRole(ap);
										if (tempR2.who() == allRoles.noone)
										{
											p.sendMessage(plugin.getConfig().getString("messages.errors.notFoundPlayer").replace("&", "§"));
											return true;
										}
										tempR.Active(tempR2);
										data.canActive[data.takeActive(tempR.who())] = false;
									}
									if (args.length == 3)
									{
										Player ap1 = Bukkit.getPlayer(args[1]);
										Player ap2 = Bukkit.getPlayer(args[2]);
										if ((ap1 == null) || (ap2 == null))
										{
											p.sendMessage(plugin.getConfig().getString("messages.errors.notFoundPlayer").replace("&", "§"));
											return true;
										}
										Role tempR1 = data.takeRole(ap1);
										Role tempR2 = data.takeRole(ap2);
										if ((tempR1.who() == allRoles.noone) || tempR1.who() == allRoles.noone)
										{
											p.sendMessage(plugin.getConfig().getString("messages.errors.notFoundPlayer").replace("&", "§"));
											return true;
										}
										tempR.Active(tempR1, tempR2);
										data.canActive[data.takeActive(tempR.who())] = false;
									}
								}
							}
							return true;
						}
						default:
						{
							data.notExistCommand = true;
							break;
						}
					}
				}
				if (data.notExistCommand)
				{
					data.notExistCommand = false;
					p.sendMessage(plugin.getConfig().getString("messages.errors.notExistCommand").replace("&", "§"));
					return true;
				}
				p.sendMessage(plugin.getConfig().getString("messages.errors.notPermission").replace("&", "§"));
				return true;
			}
			case("mafiachat"):
			{
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
							p.sendMessage(plugin.getConfig().getString("messages.mafia.notActive").replace("&", "§"));
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
			}
		}
		return true;
	}
}
