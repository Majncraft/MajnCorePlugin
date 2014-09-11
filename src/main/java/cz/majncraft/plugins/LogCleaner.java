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

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

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
		logger.info("Reflection of java.util.logging.Handler");
	    try {
	    ClassPool pool = ClassPool.getDefault();
	    CtClass handler = pool.get("java.util.logging.Handler");
	    CtMethod method = handler.getDeclaredMethod("publish");
	    method.insertBefore("return;");
	    method.insertBefore("if(record==null)");
	    method.insertBefore("record=cz.majncraft.plugins.logCleaner.LogFilters.testLog(record);");
		logger.info("Inserting complate. Saving.");
	    handler.toClass();
		} catch (CannotCompileException e) {
			logger.info(e.getMessage());
		} catch (NotFoundException e) {
			logger.info(e.getMessage());
		}
		Runnable task=new Runnable() {
			
			@Override
			public void run() {
					
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
		yaml.s("Debug", true);
		yaml.s("Start-after",60);
		try {
			yaml.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
