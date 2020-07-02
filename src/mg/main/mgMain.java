package mg.main;

import org.bukkit.command.PluginCommand;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_15_R1.ChatMessageType;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import net.minecraft.server.v1_15_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_15_R1.PacketPlayOutChat;

public class mgMain extends JavaPlugin
{
	
	public void onEnable()
	{
		PluginCommand command;
		
		mafia.Data data  = new mafia.Data(this);
		command = getCommand("mafia");
		command.setTabCompleter(new mafia.TabWork(data));
		if (command != null) command.setExecutor(new mafia.Commands(this, data));
		command = getCommand("mafiachat");
		if (command != null) command.setExecutor(new mafia.Commands(this, data));
	}
	
	public void setActionBar(Player p, String s)
	{
		EntityPlayer e = ((CraftPlayer) p).getHandle();
		
		IChatBaseComponent ic = ChatSerializer.a("{\"text\":\"" + s + "\"}") ;
		PacketPlayOutChat packet = new PacketPlayOutChat(ic, ChatMessageType.GAME_INFO);
		
		e.playerConnection.sendPacket(packet);;
	}
	
	public void onDisable()
	{
		
	}
	
}
