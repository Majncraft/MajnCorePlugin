package cz.majncraft.protection;

import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.util.BlockIterator;

import cz.majncraft.MajnCorePlugin;

public class MinecraftBugAbusing implements Listener {

	@EventHandler
    public void treeBedrockGrowing(StructureGrowEvent event)
    {
		if(!MajnCorePlugin.instance.getCfg().getBoolean("protection.bug.treeGrowingThroughBedrock"))
			return;
		if(event.getSpecies()==TreeType.DARK_OAK)
			for(BlockState a:event.getBlocks())
				if(a.getBlock().getType()==Material.BEDROCK)
					a.setType(Material.BEDROCK);
    }
	
	@EventHandler
    public void tntArrowDuplicator(ProjectileHitEvent event) {
        if(!(event.getEntity() instanceof Arrow)) //Remove to check all projectiles
            return;

        Arrow arrow = (Arrow)event.getEntity() ;
        if((arrow.getShooter() instanceof Player)) //Making sure the shooter is a player
            return;

		if(!MajnCorePlugin.instance.getCfg().getBoolean("protection.bug.arrowTNTDuplicator"))
			return;
        World world = arrow.getWorld();
        BlockIterator iterator = new BlockIterator(world, arrow.getLocation().toVector(), arrow.getVelocity().normalize(), 0, 4);
        Block hitBlock = null;

        while(iterator.hasNext()) {
            hitBlock = iterator.next();
            if(hitBlock.getType()!=Material.AIR) //Check all non-solid blockid's here.
                break;
        }
        if(hitBlock.getType()==Material.TNT && arrow.getFireTicks()<=0)
         event.getEntity().remove();

    }
}
