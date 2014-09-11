package cz.majncraft.core;

import java.util.List;

import cz.majncraft.api.MajnPlugin;

public class PluginLoader {

	private static List<Class<? extends MajnPlugin>> classes;
	private static List<MajnPlugin> plugins;
	public static List<MajnPlugin> getPlugins()
	{
		return plugins;
	}
	public static void loadPlugins()
	{
		classes=ClassFinder.find("cz.majncraft.plugins");
		for(Class<? extends MajnPlugin> cl:classes)
			try {
				plugins.add(cl.newInstance());
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
	}
	
}
