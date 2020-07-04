package mg.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
	private FileConfiguration lang;
	private FileConfiguration kits;
	
	
	public void onEnable()
	{
		File folder = new File(this.getDataFolder() + "");
		if (!folder.exists()) {
		    folder.mkdir();
		}
		lang = setFileConfiguration("lang.yml");
		kits = setFileConfiguration("kits.yml");
		mafia.Data dataMafia  = new mafia.Data(this, lang);
		Data data = new Data();
		Bukkit.getPluginManager().registerEvents(new EventChecker(), this);
		PluginCommand command = getCommand("play");
		PluginCommand commandMafia = getCommand("mafia");
		PluginCommand commandMafiaChat = getCommand("mafiachat");
		command.setTabCompleter(new TabWork());
		if (command != null) command.setExecutor(new Commands(data, dataMafia));
		commandMafia.setTabCompleter(new mafia.TabWork(dataMafia));
		if (commandMafia != null) commandMafia.setExecutor(new mafia.CommandsMafia(dataMafia, lang, kits));
		if (commandMafiaChat != null) commandMafiaChat.setExecutor(new mafia.CommandsMafiaChat(dataMafia, lang));
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
	
	private FileConfiguration setFileConfiguration(String fileName)
	{
		File file = new File(this.getDataFolder() + File.separator + fileName);
		if (!file.exists())
		{
			InputStream is = null;
			OutputStream os = null;
			try 
			{
				file.createNewFile();
				try
				{
					is = this.getResource(fileName);
					os = new FileOutputStream(file);
					byte[] buffer = new byte[1024];
			        int length;
			        while ((length = is.read(buffer)) > 0) 
			        {
			            os.write(buffer, 0, length);
			        }
				}
				finally
				{
					is.close();
			        os.close();
				}
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			file = new File(this.getDataFolder() + File.separator + fileName);
		}
		return YamlConfiguration.loadConfiguration(file);
	}
	
}
