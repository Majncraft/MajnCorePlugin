package cz.majncraft.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.World.Environment;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import cz.majncraft.MajnCorePlugin;
import cz.majncraft.protection.WitherExplosion;

public class IOUttils {

	public static YamlConfiguration loadCfg()
	{
		File file=new File(MajnCorePlugin.instance.getDataFolder()+"/config.yml");
		YamlConfiguration cfg=new YamlConfiguration();
			try
			{	
				firstrunMain();
			cfg.load(file);
			}
			catch(Exception ex){ ex.printStackTrace();}
		return cfg;
	}
	private static void firstrunMain() throws IOException, InvalidConfigurationException
	{
		File file=new File(MajnCorePlugin.instance.getDataFolder()+"/config.yml");
		if(!file.exists())
		{
			MajnCorePlugin.instance.getDataFolder().mkdirs();
			file.createNewFile();
		}
		YamlConfiguration fileCfg=new YamlConfiguration();
		fileCfg.load(file);
		fileCfg.addDefault("core.version", 1);
		fileCfg.addDefault("protection.bug.arrowTNTDuplicator", true);
		fileCfg.addDefault("protection.bug.treeGrowingThroughBedrock", true);
		fileCfg.addDefault("core.showmaplink", "http://map.majncraft.cz/");
		fileCfg.save(file);
		
	}
	private static void firstrunWitherProtection() throws IOException, InvalidConfigurationException
	{
		File file=new File(MajnCorePlugin.instance.getDataFolder()+"/witherProtection.yml");
		if(!file.exists())
		{
			MajnCorePlugin.instance.getDataFolder().mkdirs();
			file.createNewFile();
		}
		YamlConfiguration fileCfg=new YamlConfiguration();
		fileCfg.load(file);
		fileCfg.createSection("core");
		fileCfg.addDefault("core.normal.witherSpawnAboveBlock", -1);
		fileCfg.addDefault("core.normal.witherSpawnBelowBlock", 256);
		fileCfg.addDefault("core.nether.witherSpawnAboveBlock", -1);
		fileCfg.addDefault("core.nether.witherSpawnBelowBlock", 256);
		fileCfg.addDefault("core.end.witherSpawnAboveBlock", -1);
		fileCfg.addDefault("core.end.witherSpawnBelowBlock", 256);
		fileCfg.addDefault("core.protection.witherEatBlock", true);
		fileCfg.addDefault("core.protection.witherProjectile", true);
		fileCfg.addDefault("core.protection.explosionWither", true);
		fileCfg.createSection("protection");
		fileCfg.addDefault("protection.denyDrop.activated", true);
		fileCfg.addDefault("protection.denyDrop.allWood", true);
		fileCfg.addDefault("protection.denyDrop.others", new ArrayList<String>());
		fileCfg.addDefault("protection.denyDestroy.activated", false);
		fileCfg.addDefault("protection.denyDestroy.allWood", false);
		fileCfg.addDefault("protection.denyDestroy.others", new ArrayList<String>());
		fileCfg.save(file);
	}
	@SuppressWarnings("unchecked")
	public static WitherExplosion loadWitherProtection(WitherExplosion data)
	{
		File file=new File(MajnCorePlugin.instance.getDataFolder()+"/witherProtection.yml");
		YamlConfiguration cfg=new YamlConfiguration();
			try
			{	
			firstrunWitherProtection();
			cfg.load(file);
			}
			catch(Exception ex){ ex.printStackTrace();}
		data.setWitherSpawn(Environment.NORMAL, cfg.getInt("core.normal.witherSpawnAboveBlock"), cfg.getInt("core.normal.witherSpawnBelowBlock"));
		data.setWitherSpawn(Environment.NETHER, cfg.getInt("core.nether.witherSpawnAboveBlock"), cfg.getInt("core.nether.witherSpawnBelowBlock"));
		data.setWitherSpawn(Environment.THE_END, cfg.getInt("core.end.witherSpawnAboveBlock"), cfg.getInt("core.end.witherSpawnBelowBlock"));
		data.setActiveEatProtection(cfg.getBoolean("core.protection.witherEatBlock"));
		data.setActiveProjectileProtection(cfg.getBoolean("core.protection.witherProjectile"));
		data.setActiveExplosionProtection(cfg.getBoolean("core.protection.explosionWither"));
		data.setActiveDenyDrop(cfg.getBoolean("protection.denyDrop.activated"));
		data.setActiveDenyDropWood(cfg.getBoolean("protection.denyDrop.allWood"));
		data.setDropBlocks((List<String>)cfg.getList("protection.denyDrop.others"));
		data.setActiveDenyDestroy(cfg.getBoolean("protection.denyDestroy.activated"));
		data.setActiveDenyDropWood(cfg.getBoolean("protection.denyDestroy.allWood"));
		data.setDestroyBlocks((List<String>)cfg.getList("protection.denyDrop.others"));
		return data;
		
	}
	
}
