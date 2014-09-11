package cz.majncraft.core;

import java.util.ArrayList;
import java.util.List;

import cz.majncraft.MajnCorePlugin;
import cz.majncraft.api.MajnPlugin;

public class PluginLoader {

	private static List<Class<? extends MajnPlugin>> classes;
	private static List<MajnPlugin> plugins=new ArrayList<MajnPlugin>();
	public static List<MajnPlugin> getPlugins()
	{
		return plugins;
	}
	public static void loadPlugins()
	{
    	MajnCorePlugin.instance.getLogger().info("Starting lookup for majnplugins");
		classes=ClassFinder.find("cz.majncraft.plugins");
		for(Class<? extends MajnPlugin> cl:classes)
			try {
				plugins.add(cl.newInstance());
			} catch (InstantiationException e) {
		    	MajnCorePlugin.instance.getLogger().info(e.getLocalizedMessage());
				e.printStackTrace();
			} catch (IllegalAccessException e) {
		    	MajnCorePlugin.instance.getLogger().info(e.getLocalizedMessage());
				e.printStackTrace();
			}
	}
	
}
