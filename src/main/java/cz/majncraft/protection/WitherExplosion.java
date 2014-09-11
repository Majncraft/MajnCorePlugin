package cz.majncraft.protection;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.EventExecutor;

public class WitherExplosion implements Listener {
	  
	private int normalmin=-1,normalmax=256,nethermin=-1,nethermax=256,endmin=-1,endmax=256;
	private boolean acEat=true,acProjectile=true,acExplosion=true,acDenyDrop=true,acDenyDestroy=false,
			acDenyDropW=true,acDenyDestroyW=true;
	private List<String> dropBlocks=new ArrayList<>(),destroyBlocks=new ArrayList<>();
	public void setWitherSpawn(Environment env,int min,int max)
	{
		if(env==Environment.NETHER){
			nethermin=min;	nethermax=max;}
		else if(env==Environment.NORMAL){
			normalmin=min;	normalmax=max;}
		else{
			endmin=min;		endmax=max;}
	}
	public void setActiveEatProtection(boolean status) {acEat=status;}
	public void setActiveProjectileProtection(boolean status) {acProjectile=status;}
	public void setActiveExplosionProtection(boolean status) {acExplosion=status;}
	public void setActiveDenyDrop(boolean status) {acDenyDrop=status;}
	public void setActiveDenyDestroy(boolean status) {acDenyDestroy=status;}
	public void setActiveDenyDropWood(boolean status) {acDenyDropW=status;}
	public void setActiveDenyDestroyWood(boolean status) {acDenyDestroyW=status;}
	public void setDropBlocks(List<String> data){dropBlocks=data;}
	public void setDestroyBlocks(List<String> data){destroyBlocks=data;}
	
		@EventHandler(ignoreCancelled=true)
	    public void WitherProjectile(EntityExplodeEvent event1)
	    {
			 if(!acProjectile || event1==null)return;
			 master(event1.getEntity(),event1.blockList());
			
	    }
		 @EventHandler(ignoreCancelled=true)
		 public void onExplosionWither(EntityExplodeEvent event1) { 
			 if(!acExplosion || event1==null)return;
			 master(event1.getEntity(),event1.blockList());
		 }
		 @EventHandler(ignoreCancelled=true)
		 public void WitherEatBlocks(EntityChangeBlockEvent event1) { 
			 if(!acEat || event1==null)return;
			 List<Block> a=new ArrayList<>();
			 a.add(event1.getBlock());
			 master(event1.getEntity(),a);
		 }
		private void master(Entity ent,List<Block> blocklist)
		{
			if (ent==null || blocklist==null) 
				return;
	        EntityType type = ent.getType();
	        if (type != EntityType.WITHER && type!=EntityType.WITHER_SKULL) 				
	        	return; 
	        if(acDenyDestroy)
	          servant(destroyBlocks, false, blocklist);
		    if(acDenyDrop)
	          servant(dropBlocks, true, blocklist);
		}
		private void servant(List<String> disblocks, boolean type,List<Block> eventblock)
		{
	          List<Block> blocks = new ArrayList<Block>();
	          for(Block a:eventblock)
	          {
	           if(disblocks.contains(a.getType().toString()))

	           for(String b:disblocks)
	           {
	           if(b.equals(a.getType().toString()))
	           blocks.add(a);
	           }
	           if( type?acDenyDropW:acDenyDestroyW &&(a.getType().equals(Material.LOG)||a.getType().equals(Material.LOG_2)))
	        	   blocks.add(a);
	          }
	          eventblock.removeAll(blocks);
	          if(type)
	          for(Block b:blocks)
	          {
	           b.setType(Material.AIR);
	          }
		}
}
