package cz.majncraft.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cz.majncraft.MajnCorePlugin;
import cz.majncraft.commands.Cmd.Side;

public class TextCommands {

	@Cmd(commands={"showmap"},permission="majncraft.user.showmap",side=Side.Client)
	public static void showmap(CommandEvent event)
	{
		Player pl=(Player)event.sender;
		pl.chat(MajnCorePlugin.instance.getCfg().getString("core.showmaplink")+"?worldname="+pl.getWorld().getName()+"&zoom=8&x="+pl.getLocation().getBlockX()+"&y=64&z="+pl.getLocation().getBlockZ());
	}
	@Cmd(commands={"ruler"},permission="majncraft.user.ruler",side=Side.Client)
	public static void ruler(CommandEvent event)
	{
		Player pl=(Player)event.sender;
		if(rulerData.containsKey(pl.getUniqueId()))
		{
			Location l=rulerData.get(pl.getUniqueId());
			pl.sendMessage("[Ruler]Measurement complete:");
			pl.sendMessage("Total distance: "+pl.getLocation().distance(l));
			pl.sendMessage("Distance X: "+Math.abs(pl.getLocation().getBlockX()-l.getBlockX()));
			pl.sendMessage("Distance Y: "+Math.abs(pl.getLocation().getBlockY()-l.getBlockY()));
			pl.sendMessage("Distance Z: "+Math.abs(pl.getLocation().getBlockZ()-l.getBlockZ()));
			rulerData.remove(pl.getUniqueId());
		}
		else
		{
			rulerData.put(pl.getUniqueId(), pl.getLocation());
			pl.sendMessage("[Ruler]First position set. Set second position");
		}
	}
	private static Map<UUID, Location> rulerData=new HashMap<>();
	public static void removeUUID(UUID uuid)
	{
		rulerData.remove(uuid);
	}
}
