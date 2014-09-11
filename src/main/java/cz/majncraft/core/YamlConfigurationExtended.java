package cz.majncraft.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class YamlConfigurationExtended extends YamlConfiguration{

	public void s(String key,Object data)
	{
		if(!contains(key))
			set(key, data);
		addDefault(key,data);
	}
	String org;
	@Override
	public void load(File file) throws FileNotFoundException, IOException,
			InvalidConfigurationException {
		org=file+"";
		super.load(file);
	}
	public void save() throws IOException
	{
		if(org!=null)
			this.save(org);
	}
	@Override
	public void load(String file) throws FileNotFoundException, IOException,
			InvalidConfigurationException {
		org=file;
		super.load(file);
	}
}
