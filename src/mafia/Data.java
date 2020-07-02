package mafia;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import mg.main.mgMain;

public class Data 
{
	
	private mgMain plugin;
	
	public Data(mgMain plugin)
	{
		this.plugin = plugin;
	}
	

	public enum allRoles {mafia, resident, doctor, commissioner, don, fallen, jealous, maniac, journalist, witness, fatale, noone, lead, died};
	public allRoles[] all = new allRoles[]{allRoles.mafia, allRoles.resident, allRoles.doctor, allRoles.commissioner, allRoles.don, allRoles.fallen, allRoles.jealous, allRoles.maniac, allRoles.journalist, allRoles.witness, allRoles.fatale, allRoles.noone, allRoles.lead, allRoles.died};
	public int countRoles = all.length;
	public int[] lifeCount = new int[countRoles];
	
	public allRoles[] turns = new allRoles[] {allRoles.noone, allRoles.witness, allRoles.journalist, allRoles.fallen, allRoles.jealous, allRoles.mafia, allRoles.maniac, allRoles.doctor, allRoles.commissioner, allRoles.don, allRoles.witness, allRoles.fatale, allRoles.noone};
	public allRoles[][] date = new allRoles[][] {{allRoles.commissioner, allRoles.fallen, allRoles.witness, allRoles.doctor, allRoles.journalist}, {allRoles.mafia, allRoles.don, allRoles.jealous, allRoles.fatale}, {allRoles.maniac}};
	public byte[] rarity = new byte[] {1, 1, 2, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1};
	
	public int countTurns = turns.length;
	public boolean[] canActive = new boolean[countTurns];
	
	public ArrayList<String> log = new ArrayList<>();
	
	public int turn = 0;
	public int day = 0;
	
	public int[] values = new int[countRoles];
	public ArrayList<Player> ps = new ArrayList<>();
	public ArrayList<Role> roles = new ArrayList<>();
	
	public Location diedPos;
	public Location startPos;
	
	public boolean isPlay = false;
	public boolean isNight = false;
	
	public boolean miss = false;
	
	public boolean notExistCommand = false;
	
	Role empty = new Role();
	Lead leader;
	
	Scoreboard sc = Bukkit.getScoreboardManager().getMainScoreboard();
	Team tm;
	
	public class Role
	{
		public Player p;
		public Role killer;
		public boolean isAim;
		public boolean withFallen;
		public boolean isMute;
		public int group;
		public int number;
		public allRoles who() {return allRoles.noone;};
		public void Active(Role _p)
		{
			p.sendMessage(plugin.getConfig().getString("messages.mafia.notActive"));
		}
		public void Active(Role _p1, Role _p2)
		{
			p.sendMessage(plugin.getConfig().getString("messages.mafia.notActive"));
		}
	}
	
	public class Resident extends Role
	{
		Resident(Player _p, int index)
		{
			p = _p;
			standartConstructor(this);
			group = 1;
			number = index;
		}
		public allRoles who() {return allRoles.resident;};
	}
	public class Commissioner extends Role
	{
		Commissioner(Player _p, int index)
		{
			p = _p;
			standartConstructor(this);
			group = 1;
			number = index;
		}
		public allRoles who() {return allRoles.commissioner;};
		public void Active(Role _p) 
		{
			if (this != _p)
			{
				String tempS;
				if ((_p.who() == allRoles.mafia) || (_p.who() == allRoles.don))
				{
					tempS = plugin.getConfig().getString("messages.mafia.mafia").replace("&", "§");
				}
				else
				{
					tempS = plugin.getConfig().getString("messages.mafia.resident").replace("&", "§");
				}
				loging(p, _p, "check");
				p.sendMessage(tempS);
			}
			else
			{
				p.sendMessage(plugin.getConfig().getString("messages.mafia.error_someself").replace("&", "§"));
			}
		}
	}
	public class Fallen extends Role
	{
		Fallen(Player _p, int index)
		{
			p = _p;
			standartConstructor(this);
			group = 1;
			number = index;
		}
		public allRoles who() {return allRoles.fallen;};
		public void Active(Role _p)
		{
			if (this != _p)
			{
				if ((_p.who() == allRoles.mafia) || (_p.who() == allRoles.don))
				{
					miss = true;
				}
				_p.withFallen = true;
				loging(p, _p, "molest");
			}
			else
			{
				p.sendMessage(plugin.getConfig().getString("messages.mafia.error_someself").replace("&", "§"));
			}
		}
	}
	public class Witness extends Role
	{
		private boolean isFollow;
		Role followingP;
		Witness(Player _p, int index)
		{
			p = _p;
			standartConstructor(this);
			group = 1;
			number = index;
			isFollow = false;
		}
		public allRoles who() {return allRoles.witness;};
		public void Active(Role _p)
		{
			if (this != _p)
			{
				if (isFollow)
				{
					if (followingP.isAim)
						p.sendMessage(followingP.killer.p.getDisplayName());
				}
				else
				{
					followingP = _p;
					loging(p, _p, "follow");
					isFollow = true;
				}
			}
			else
			{
				p.sendMessage(plugin.getConfig().getString("messages.mafia.error_someself").replace("&", "§"));
			}
		}
	}
	public class Doctor extends Role
	{
		Doctor(Player _p, int index)
		{
			p = _p;
			standartConstructor(this);
			group = 1;
			number = index;
		}
		public allRoles who() {return allRoles.doctor;};
		public void Active(Role _p) 
		{
			loging(p, _p, "heal");
			_p.isAim = false;
		}	
	}
	public class Journalist extends Role
	{
		Journalist(Player _p, int index)
		{
			p = _p;
			standartConstructor(this);
			group = 1;
			number = index;
		}
		public allRoles who() {return allRoles.journalist;};
		public void Active(Role _p1, Role _p2)
		{
			if ((this != _p1) && (this != _p2))
			{
				if (_p1 != _p2)
				{
					loging(p, _p1, _p2, "check");
					if (_p1.group == _p2.group) p.sendMessage(plugin.getConfig().getString("messages.mafia.one").replace("&", "§"));
					else p.sendMessage(plugin.getConfig().getString("messages.mafia.different").replace("&", "§"));
				}
			}
			else
			{
				p.sendMessage(plugin.getConfig().getString("messages.mafia.error_someself").replace("&", "§"));
			}
		}
	}
	
	public class Mafia extends Role
	{
		Mafia(Player _p, int index)
		{
			p = _p;
			standartConstructor(this);
			group = 2;
			number = index;
		}
		public allRoles who() {return allRoles.mafia;};
		public void Active(Role _p) 
		{
			if (this != _p)
			{
				if (!miss)
				{
					loging(p, _p, "kill");
					_p.isAim = true;
					_p.killer = this;
				}
			}
			else
			{
				p.sendMessage(plugin.getConfig().getString("messages.mafia.error_someself").replace("&", "§"));
			}
		}
	}
	public class Don extends Role
	{
		Don(Player _p, int index)
		{
			p = _p;
			standartConstructor(this);
			group = 2;
			number = index;
		}
		public allRoles who() {return allRoles.don;};
		public void Active(Role _p) 
		{
			if (this != _p)
			{
				if (!miss)
				{
					if (_p.who() == allRoles.commissioner)
					{
						loging(p, _p, "kill");
						_p.isAim = true;
						_p.killer = this;
						roles.set(number, new Mafia(p, number));
					}
				}
			}
			else
			{
				p.sendMessage(plugin.getConfig().getString("messages.mafia.error_someself").replace("&", "§"));
			}
		}
	}
	public class Jealous extends Role
	{
		Jealous(Player _p, int index)
		{
			p = _p;
			standartConstructor(this);
			group = 2;
			number = index;
		}
		public allRoles who() {return allRoles.jealous;};
		public void Active(Role _p1, Role _p2)
		{
			if ((this != _p1) && (this != _p2))
			{
				loging(p, _p1, _p2, "check");
				if (((_p1.withFallen) && (_p2.who() == allRoles.fallen)) || ((_p2.withFallen) && (_p1.who() == allRoles.fallen)))
				{
					loging(p, _p1, _p2, "kill");
					_p1.isAim = true;
					_p2.isAim = true;
				}
			}
			else
			{
				p.sendMessage(plugin.getConfig().getString("messages.mafia.error_someself").replace("&", "§"));
			}
		}
	}
	public class Fatale extends Role
	{
		Fatale(Player _p, int index)
		{
			p = _p;
			standartConstructor(this);
			group = 2;
			number = index;
		}
		public allRoles who() {return allRoles.fatale;};
		public void Active(Role _p)
		{
			if (this != _p)
			{
				_p.isMute = true;
				loging(p, _p, "mute");
			}
			else
			{
				p.sendMessage(plugin.getConfig().getString("messages.mafia.error_someself").replace("&", "§"));
			}
		}
	}
	
	public class Maniac extends Role
	{
		Maniac(Player _p, int index)
		{
			p = _p;
			standartConstructor(this);
			group = 3;
			number = index;
		}
		public allRoles who() {return allRoles.maniac;};
		public void Active(Role _p)
		{
			if (this != _p)
			{
				loging(p, _p, "kill");
				_p.isAim = true;
				_p.killer = this;
			}
			else
			{
				p.sendMessage(plugin.getConfig().getString("messages.mafia.error_someself").replace("&", "§"));
			}
		}
	}
	
	public class Died extends Role
	{
		Died(Player _p)
		{
			p = _p;
			standartConstructor(this);
			group = 0;
		}
		public allRoles who() {return allRoles.died;};
	}
	
	public class Lead extends Role
	{
		Lead(Player _p)
		{
			p = _p;
			standartConstructor(this);
			group = 0;
		}
		public allRoles who() {return allRoles.lead;};
	}
	
	public void sleep(Player p)
	{
		p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60000, 1));
		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60000, 10));	
		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 60000, 10));	
	}
		
	public void awake(Player p)
	{
		p.removePotionEffect(PotionEffectType.BLINDNESS);
		p.removePotionEffect(PotionEffectType.SLOW);
		p.removePotionEffect(PotionEffectType.SLOW_DIGGING);
	}
		
	private void loging(Player p, Role _p, String active)
	{
		p.sendMessage(plugin.getConfig().getString("messages.mafia.success_" + active + "Player").replace("&", "§") + " " + _p.p.getDisplayName());
		leader.p.sendMessage(p.getDisplayName() + plugin.getConfig().getString("messages.mafia.text_" + active).replace("&", "§") + _p.p.getDisplayName());
		log.add(p.getDisplayName() + plugin.getConfig().getString("messages.mafia.text_" + active).replace("&", "§") + _p.p.getDisplayName());
	}
	
	private void loging(Player p, Role _p1, Role _p2, String active)
	{
		p.sendMessage(plugin.getConfig().getString("messages.mafia.success_" + active + "Player").replace("&", "§") + " " + _p1.p.getDisplayName() + ", " + _p2.p.getDisplayName());
		leader.p.sendMessage(p.getDisplayName() + plugin.getConfig().getString("messages.mafia.text_" + active).replace("&", "§") + _p1.p.getDisplayName() + ", " + _p2.p.getDisplayName());
		log.add(p.getDisplayName() + plugin.getConfig().getString("messages.mafia.text_" + active).replace("&", "§") + _p1.p.getDisplayName() + ", " + _p2.p.getDisplayName() );
	}
		
	private void standartConstructor(Role p)
	{
		p.isAim = false;
		p.withFallen = false;
		p.isMute = false;
	}
	
	public int takeActive(allRoles role)
	{
		for (int i = 0; i < countTurns; i++) { if (role == turns[i]) return i; }
		return -1;
	}
	
	public Role takeRole(Player p)
	{
		for (Role r : roles)
		{
			if (p == r.p)
			{
				return r;
			}
		}
		return empty;
	}
	
	public void Kill(int index)
	{
		Role tempR = new Died(roles.get(index).p);
		roles.set(index, tempR);
		tempR.p.teleport(diedPos);
		for(Role r : roles) r.p.sendMessage(plugin.getConfig().getString("messages.mafia.text_died").replace("&", "§") + roles.get(index).p.getDisplayName());
		leader.p.sendMessage(plugin.getConfig().getString("messages.mafia.text_died").replace("&", "§") + roles.get(index).p.getDisplayName());
	}
	
	public void day()
	{
		for (int i = 0; i < countRoles; i++) lifeCount[i] = 0;
		for (Role r : roles) 
		{
			if (r.isAim) Kill(r.number);
			r.p.sendMessage(plugin.getConfig().getString("messages.mafia.turn_day").replace("&", "§"));
			awake(r.p);
			for (int i = 0; i < countRoles; i++) if (r.who() == all[i]) lifeCount[i]++; 
		}
		for (int i = 0; i < countRoles; i++)
		{
			if (lifeCount[i] != 0) leader.p.sendMessage(plugin.getConfig().getString("messages.mafia." + all[i]).replace("&", "§") + ": " + Integer.toString(lifeCount[i]));
		}
		turn = 0;
		day++;
		isNight = false;
		leader.p.sendMessage(plugin.getConfig().getString("messages.mafia.turn_day").replace("&", "§"));
	}
	
	public void date()
	{
		if (turn == 5)
		{
			day();
			return;
		}
		for (Role r : roles) plugin.setActionBar(r.p, plugin.getConfig().getString("messages.mafia.date_group" + Integer.toString(turn)).replace("&", "§"));;
		leader.p.sendMessage(plugin.getConfig().getString("messages.mafia.date_group" + Integer.toString(turn)).replace("&", "§"));
		for (Role r : roles)
		{
			if (r.group == turn)
			{
				awake(r.p);
			}
			else
			{
				sleep(r.p);
			}
		}
	}
	
	public void night()
	{
		boolean tempB = false;
		while (day % rarity[turn] != 0)
		{
			turn++;
		}
		if (turns[turn] == allRoles.noone) tempB = true;
		for (Role r : roles)
			if (r.who() == turns[turn])
			{
				tempB = true;
				break;
			}
		canActive[turn] = true;
		if (tempB)
		{
			for (Role r : roles) plugin.setActionBar(r.p, plugin.getConfig().getString("messages.mafia.turn_" + turns[turn]).replace("&", "§"));;
			leader.p.sendMessage(plugin.getConfig().getString("messages.mafia.turn_" + turns[turn]).replace("&", "§"));
			for (Role r : roles)
			{
				if ((turns[turn] == r.who()) || (r.who() == allRoles.died))
				{
					awake(r.p);
				}
				else
				{
					sleep(r.p);
				}
			}
		}
		else
		{
			turn++;
			if (turn == countTurns)
			{
				day();
				day++;
				return;
			}
			night();
			return;
		}
		
	}
}
