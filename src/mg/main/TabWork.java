package mg.main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TabWork implements TabCompleter
{
	
	String[] commandsPlay = new String[] {"mafia", "quit"};
	
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args)
	{
		List<String> list = new ArrayList<>();
		for (int i = 0; i < commandsPlay.length; i++)
		{
			if (contains(commandsPlay[i], args[0])) list.add(commandsPlay[i]);		
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
