package cz.majncraft.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;

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
    	Reflections reflections = new Reflections("cz.majncraft.plugins");    
    	Set<Class<? extends MajnPlugin>> classes = reflections.getSubTypesOf(MajnPlugin.class);
    	MajnCorePlugin.instance.getLogger().info("Founded "+classes.size()+" majnplugins");
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
