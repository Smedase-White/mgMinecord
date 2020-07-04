package mg.main;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class Data 
{
	
	public enum games {Mafia, Other};
	public static Scoreboard sc = Bukkit.getScoreboardManager().getMainScoreboard();
	public static Team teamMafia;
	
	public static ArrayList<ActivePlayer> allPlayers = new ArrayList<>();
	
	public class ActivePlayer
	{
		Player p;
		games game;
		Location back;
		ActivePlayer(Player p, games game)
		{
			this.p = p;
			this.game = game;
			back = p.getLocation();
		}
	}
	
	public ActivePlayer getActivePlayer(Player p)
	{
		ActivePlayer ap = null;
		for (ActivePlayer a : allPlayers)
		{
			if (a.p == p)
			{
				ap = a;
				break;
			}
		}
		return ap;
	}
}
