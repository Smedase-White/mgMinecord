package mg.main;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventChecker implements Listener
{
	
	@EventHandler
	public void quit(PlayerQuitEvent e)
	{
		Commands.quit(e.getPlayer());
	}
	
}
