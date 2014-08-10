package cz.majncraft.api;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.entity.Player;

import cz.majncraft.MajnCorePlugin;
import cz.majncraft.commands.Cmd;
import cz.majncraft.commands.Cmd.Side;
import cz.majncraft.commands.CommandEvent;

public class CommandRegister {

	private static CommandRegister instance;
	public static CommandRegister getInstance()
	{
		return (instance==null)?new CommandRegister():instance;
	}
	private List<Method> commands=new ArrayList<>();
	private CommandRegister()
	{
		instance=this;
	}
	public boolean Execute(CommandEvent event)
	{
		boolean tt=false;
		for(Method mtd:commands)
		{
			Cmd cmd=mtd.getAnnotation(Cmd.class);
			boolean t=false;
			for(String a:cmd.commands())
			{
				if(a.equals(event.label.toLowerCase()))
				{t=true;
				break;
				}
			}
			if(t==false)
				continue;
			else
				tt=true;
			if((cmd.opHave() &&event.sender.isOp())|| (!cmd.permissionReverse() ==event.sender.hasPermission(cmd.permission())))
			{
				
			if(event.sender instanceof Player)
			{
				if(cmd.side()==Side.Server)
				{
					event.sender.sendMessage("Console-only command");
					return true;
				}
			}
			else
			{
				if(cmd.side()==Side.Client)
				{
					event.sender.sendMessage("Client-only command");
					return true;
				}
			}
			try {
				mtd.invoke(null, event);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		}
		return tt;
	}
	public void Register(Class<?> register)
	{
		String met="";
		for(Method mtd: register.getDeclaredMethods())
		{
			if(mtd.isAnnotationPresent(Cmd.class))
			{
				if(mtd.getParameterTypes().length==1 && mtd.getParameterTypes()[0]==CommandEvent.class)
				{
					commands.add(mtd);
					met+=", "+mtd.getName();
				}
				else
					MajnCorePlugin.instance.getLogger().log(Level.WARNING, "Method "+mtd.getName()+" of Class "+register.getName()
							+" has Annotion, but dont have parameter CommandEvent.");
			}
		}
		if(met.length()>2)
			MajnCorePlugin.instance.getLogger().log(Level.FINEST,"Commands ("+met.substring(2)+") from class "+register.getName()+" registred successfully");
	}
}
