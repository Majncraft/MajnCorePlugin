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
		YamlConfigurationExtended fileCfg=new YamlConfigurationExtended();
		fileCfg.load(file);
		fileCfg.set("core.version", 2);
		fileCfg.s("protection.bug.arrowTNTDuplicator", true);
		fileCfg.s("protection.bug.treeGrowingThroughBedrock", true);
		fileCfg.s("core.showmaplink", "http://map.majncraft.cz/");
		fileCfg.s("alteration.spawn.pigmanUnder", 110);
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
		YamlConfigurationExtended fileCfg=new YamlConfigurationExtended();
			fileCfg.load(file);
			fileCfg.s("protection.denyDrop.activated", true);
			fileCfg.s("protection.denyDrop.allWood", true);
			fileCfg.s("protection.denyDrop.others", new ArrayList<String>());
			fileCfg.s("protection.denyDestroy.activated", false);
			fileCfg.s("protection.denyDestroy.allWood", false);
			fileCfg.s("protection.denyDestroy.others", new ArrayList<String>());
			fileCfg.s("core.normal.witherSpawnAboveBlock", -1);
			fileCfg.s("core.normal.witherSpawnBelowBlock", 256);
			fileCfg.s("core.nether.witherSpawnAboveBlock", -1);
			fileCfg.s("core.nether.witherSpawnBelowBlock", 256);
			fileCfg.s("core.end.witherSpawnAboveBlock", -1);
			fileCfg.s("core.end.witherSpawnBelowBlock", 256);
			fileCfg.s("core.protection.witherEatBlock", true);
			fileCfg.s("core.protection.witherProjectile", true);
			fileCfg.s("core.protection.explosionWither", true);
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
