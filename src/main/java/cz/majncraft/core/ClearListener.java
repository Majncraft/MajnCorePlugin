package cz.majncraft.core;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import cz.majncraft.commands.TextCommands;

public class ClearListener implements Listener {

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		TextCommands.removeUUID(event.getPlayer().getUniqueId());
	}
}
