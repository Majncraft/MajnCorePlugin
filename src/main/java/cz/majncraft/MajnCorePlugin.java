package cz.majncraft;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;

import cz.majncraft.alteration.Spawn;
import cz.majncraft.api.CommandRegister;
import cz.majncraft.commands.ChunkCommands;
import cz.majncraft.commands.CommandEvent;
import cz.majncraft.commands.TextCommands;
import cz.majncraft.commands.WorldCommands;
import cz.majncraft.core.IOUttils;
import cz.majncraft.protection.MinecraftBugAbusing;
import cz.majncraft.protection.WitherExplosion;

public class MajnCorePlugin extends JavaPlugin{
	
	public static MajnCorePlugin instance;
	private YamlConfiguration configMain;
	public YamlConfiguration getCfg()
	{
		return configMain;
	}
	public MajnCorePlugin()
	{
		instance=this;
	}
	@Override
	public void onEnable() {
		witherex=new WitherExplosion();
		IOUttils.loadWitherProtection(witherex);
		mcbugabuse=new MinecraftBugAbusing();
		spawn=new Spawn();
		this.getServer().getPluginManager().registerEvents(witherex, this);
		this.getServer().getPluginManager().registerEvents(mcbugabuse, this);
		this.getServer().getPluginManager().registerEvents(spawn, this);
		super.onEnable();
	}
	@Override
	public void onDisable() {
		super.onDisable();
	}
	@Override
	public void onLoad() {
		configMain=IOUttils.loadCfg();
		CommandRegister.getInstance().Register(ChunkCommands.class);
		CommandRegister.getInstance().Register(TextCommands.class);
		CommandRegister.getInstance().Register(WorldCommands.class);
		
		super.onLoad();
	}
	private WitherExplosion witherex;
	private MinecraftBugAbusing mcbugabuse;
	private Spawn spawn;
	@Override
	public void reloadConfig() {
		IOUttils.loadWitherProtection(witherex);
		configMain=IOUttils.loadCfg();
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		// TODO Auto-generated method stub
		return CommandRegister.getInstance().Execute(new CommandEvent(sender, command, label, args));
	}

}
