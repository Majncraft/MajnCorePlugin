package cz.majncraft.alteration;

import org.bukkit.World.Environment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import cz.majncraft.MajnCorePlugin;

public class Spawn implements Listener {

	@EventHandler(ignoreCancelled=true)
	public void pigmanSpawn(CreatureSpawnEvent event)
	{
		if(event.getSpawnReason()!=SpawnReason.NATURAL 
				||event.getEntityType()!=EntityType.PIG_ZOMBIE 
				||event.getLocation().getWorld().getEnvironment()!=Environment.NETHER
				||event.getLocation().getBlockY()<MajnCorePlugin.instance.getCfg().getInt("alteration.spawn.pigmanUnder", 110))
			return;
		else
			event.setCancelled(true);
	}
}
