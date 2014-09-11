package cz.majncraft.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


import cz.majncraft.MajnCorePlugin;
import cz.majncraft.api.MajnPlugin;
import cz.majncraft.plugins.LogCleaner;

public class PluginLoader {

	private static List<Class<? extends MajnPlugin>> classes;
	private static List<MajnPlugin> plugins=new ArrayList<MajnPlugin>();
	public static List<MajnPlugin> getPlugins()
	{
		return plugins;
	}
	public static void loadPlugins()
	{
		plugins.add(new LogCleaner());
	}
	
}
