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
import javassist.Modifier;
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
		String s=Bukkit.getWorldContainer().getAbsolutePath();
		s=s.substring(0,s.length()-1);
		logger.info(s+"plugins/MajnCorePlugin.jar");
	    try {
	    ClassPool pool = ClassPool.getDefault();
	    pool.appendClassPath(s+"plugins/MajnCorePlugin.jar/*");
	    CtClass log = pool.get("java.util.logging.Logger");
	    CtClass handler = pool.get("cz.majncraft.plugins.logCleaner.LogHandler");
	    CtField f = new CtField(handler, "hiddenValue", log);
	    f.setModifiers(Modifier.PUBLIC);
	    log.addField(f);
	    CtMethod method = log.getDeclaredMethod("addHandler");
	    method.insertBefore("$1=hiddenValue.addHandler($1,getName());");
	    
	    CtMethod method2 = log.getDeclaredMethod("removeHandler");
	    method2.insertBefore("return;");
	    method2.insertBefore("if($1==null)");
	    method2.insertBefore("$1=hiddenValue.removeHandler($1,getName());");
		logger.info("Inserting complate. Saving.");
		log.toClass();
		} catch (CannotCompileException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		} catch (NotFoundException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
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
