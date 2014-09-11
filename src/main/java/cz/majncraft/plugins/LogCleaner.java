package cz.majncraft.plugins;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.swing.text.Position;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import cz.majncraft.MajnCorePlugin;
import cz.majncraft.api.MajnPlugin;
import cz.majncraft.core.YamlConfigurationExtended;
import cz.majncraft.plugins.logCleaner.LogFilters;
import cz.majncraft.plugins.logCleaner.LogHandler;

public class LogCleaner extends MajnPlugin {

	@Override
	public String getName() {
		return "LogCleaner";
	}
	public static LogCleaner instance;
	private static Logger logger;
	public static Logger getLogger()
	{
		return logger;
	}
	public LogCleaner()
	{
		instance=this;
		logger=Logger.getLogger("LogCleaner");
	}
	private static final ScheduledExecutorService worker =Executors.newSingleThreadScheduledExecutor();
	@Override
	public void onEnable() {
		LogFilters.reload();
		Runnable task=new Runnable() {
			
			@Override
			public void run() {
				Set<String> possibleLoggers=new HashSet<>();
				Set<Logger> loggers=new HashSet<>();
				loggers.add(Logger.getGlobal());
				Enumeration<String> s=LogManager.getLogManager().getLoggerNames();
				while(s.hasMoreElements())
				{
					possibleLoggers.add(s.nextElement());
				}
				for(Plugin plugin:Bukkit.getPluginManager().getPlugins())
				{
					possibleLoggers.add(plugin.getName());
					if(plugin.getLogger()!=null)
						loggers.add(plugin.getLogger());
				}
				for(String possibleLogger:possibleLoggers)
				{
					loggers.add(Logger.getLogger(possibleLogger));
				}
				for(Logger log:loggers)
				{
					Handler[] handlers=log.getHandlers();
					for(Handler h:handlers)
						log.removeHandler(h);
					LogHandler handler=new LogHandler(handlers);
					log.addHandler(handler);
				}
				logger.info("LogCleaner now handle controls over "+loggers.size()+" loggers:");
					
			}
		};
		worker.schedule(task, getConfig().getInt("Start-after"), TimeUnit.SECONDS);
	}
	public File getCustomLogFolder()
	{
		return new File(MajnCorePlugin.instance.getDataFolder().getParentFile().getParent()+getConfig().getString("Log-folder"));
	}
	@Override
	protected void setupConfig(YamlConfigurationExtended yaml) {
		yaml.s("Log-folder", "logs/CustomLogging/");
		yaml.s("Log-time", true);
		yaml.s("Archive-after-start", true);
		yaml.s("Start-after",60);
		try {
			yaml.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
