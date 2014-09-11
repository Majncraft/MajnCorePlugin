package cz.majncraft.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;

import cz.majncraft.MajnCorePlugin;
import cz.majncraft.core.PluginLoader;
import cz.majncraft.core.YamlConfigurationExtended;

public abstract class MajnPlugin {

	public MajnPlugin()
	{
	}
	public abstract String getName();
	
	public void onEnable()
	{}
	
	public void onDisable()
	{}
	
	@SuppressWarnings("rawtypes")
	public Class getCommandClass()
	{return null;}
	
	public final File getPluginFolder()
	{
		File folder=new File(MajnCorePlugin.instance.getDataFolder()+"/plugins/"+this.getName()+"/");
		if(!folder.exists());
			folder.mkdirs();
		return folder;
	}
	private YamlConfigurationExtended conf;
	protected final YamlConfigurationExtended getConfig()
	{
		if(conf!=null)
			return conf;
		File file=new File(getPluginFolder()+"/config.yml");
		conf=new YamlConfigurationExtended();
		if(!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		try {
			conf.load(file);
		} catch (IOException| InvalidConfigurationException e) {
			e.printStackTrace();
		}
		setupConfig(conf);
		return conf;
	}
	protected abstract void setupConfig(YamlConfigurationExtended yaml);
	
}
