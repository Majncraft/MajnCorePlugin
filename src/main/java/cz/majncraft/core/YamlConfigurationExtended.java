package cz.majncraft.core;

import org.bukkit.configuration.file.YamlConfiguration;

public class YamlConfigurationExtended extends YamlConfiguration{

	public void s(String key,Object data)
	{
		if(!contains(key))
			set(key, data);
		addDefault(key,data);
	}
}
