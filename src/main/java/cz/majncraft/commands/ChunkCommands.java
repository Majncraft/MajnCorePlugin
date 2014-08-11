package cz.majncraft.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cz.majncraft.MajnCorePlugin;
import cz.majncraft.commands.Cmd.Side;

public class ChunkCommands{
	
	private static Location base(CommandSender sender,String[] args)
	{
		for(String arg:args)
		{
			try
			{
			if(args.length==3 || (args.length==4 && arg!=args[3]))	
			Integer.parseInt(arg);
			}
			catch(NumberFormatException e) {
				sender.sendMessage("Wrong params");
				sender.sendMessage("/chunktest [x] [y] [z] {dim}");
		        return null; 
		    }
		}
		World wrd=null;
		if(args.length==3 && sender instanceof Player)
		{
			wrd=((Player)sender).getWorld();
		}
		if(args.length==4)
		{
			wrd=Bukkit.getWorld(args[3]);
		}
		if(wrd==null || (args.length!=3 &&args.length!=4))
		{
			sender.sendMessage("Wrong params");
			sender.sendMessage("/chunktest [x] [y] [z] {dim}");
	        return null; 
		}
		Location loc=new Location(wrd,Integer.parseInt(args[0]),Integer.parseInt(args[1]),Integer.parseInt(args[2]));
		return loc;
	}
	
	
	@Cmd(commands={"chnkunload"},permission="majncraft.mod.chunkunload")
	public static void chunkunload(CommandEvent event)
	{
		Location loc = base(event.sender, event.args);
		if(loc==null) 
			return;
		if(loc.getChunk().unload(true, true))
			event.sender.sendMessage("Unload of chunk("+loc.getChunk().getX()+";"+loc.getChunk().getZ()+") was successful.");
		else
			event.sender.sendMessage("Unload of chunk("+loc.getChunk().getX()+";"+loc.getChunk().getZ()+") failed.");
	}
	
	@Cmd(commands={"chnkload"},permission="majncraft.mod.chunkload")
	public static void chunkload(CommandEvent event)
	{
		Location loc = base(event.sender, event.args);
		if(loc==null) 
			return;
		if(loc.getChunk().load(false))
			event.sender.sendMessage("Load of chunk("+loc.getChunk().getX()+";"+loc.getChunk().getZ()+") was successful.");
		else
			event.sender.sendMessage("Load of chunk("+loc.getChunk().getX()+";"+loc.getChunk().getZ()+") failed.");
	}
	
	@Cmd(commands={"chnkforceunload"},permission="majncraft.op.chunkforceunload")
	public static void chunkforceunload(CommandEvent event)
	{
		Location loc=base(event.sender, event.args);
		if(loc==null) return;
		if(loc.getChunk().unload(true, true))
			event.sender.sendMessage("Unload of chunk("+loc.getChunk().getX()+";"+loc.getChunk().getZ()+") was successful.");
		else
			event.sender.sendMessage("Unload of chunk("+loc.getChunk().getX()+";"+loc.getChunk().getZ()+") failed.");
	}
	
	@Cmd(commands={"chnktest"},permission="majncraft.mod.chunktest",opHave=true,permissionReverse=false,side=Side.Both)
	public static void chunktest(CommandEvent event)
	{
		Location loc=base(event.sender, event.args);
		if(loc==null) return;
		event.sender.sendMessage("Chunk ("+loc.getChunk().getX()+";"+loc.getChunk().getZ()+")");
		event.sender.sendMessage("Is loaded: "+loc.getWorld().isChunkLoaded(loc.getChunk()));
		event.sender.sendMessage("Number of entities: "+loc.getChunk().getEntities().length);
		event.sender.sendMessage("Number of tileentities: "+loc.getChunk().getTileEntities().length);
	}

}
