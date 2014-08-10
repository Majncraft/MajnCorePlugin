package cz.majncraft.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandEvent {

	public final CommandSender sender;
	public final Command command;
	public final String label;
	public final String[] args;
	public CommandEvent(CommandSender sender, Command command, String label, String[] args)
	{
		this.sender=sender;
		this.command=command;
		this.label=label;
		this.args=args;
	}
}
