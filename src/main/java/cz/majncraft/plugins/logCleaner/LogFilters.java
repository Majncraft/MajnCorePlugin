package cz.majncraft.plugins.logCleaner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import cz.majncraft.plugins.LogCleaner;

public class LogFilters {

	private static Set<LogFilter> filters=new HashSet<LogFilter>();
	
	public static LogRecord testLog(LogRecord record)
	{
		for(LogFilter filter:filters)
		{
			record=filter.match(record);
			if(record==null)
				return null;
		}
		return record;
	}
	public static void reload()
	{
		filters.clear();
		File f=new File(LogCleaner.instance.getPluginFolder()+"/filters/");
		if(!f.exists())
		{
			f.mkdirs();
        	try {
        		createExampleFilter();
        	} catch (IOException e) {
				e.printStackTrace();
			}
		}
		File[] fList = f.listFiles();
	    for (File file : fList) {
	        if (file.isFile()) {
	        	try {
					loadFile(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }
	    }
	}
	
	private static void loadFile(File file) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		Logger log=LogCleaner.getLogger();
		int linenumber=-1;
		Map<String,String> data=new HashMap<String,String>();
		while ((line = br.readLine()) != null) {
			linenumber++;
			line=line.trim();
			if(line.indexOf("#")>=0)
				line=line.substring(0,line.indexOf("#")).trim();
			if(line.charAt(0)=='-')
			{
				if(data.size()>0)
				{
					loadFilter(data,file.getName(),log);
					data.clear();
				}
				if(line.indexOf("type")==-1 ||line.indexOf(":")==-1)
				{
					log.severe("Error in parsing file ("+file.getName()+") on line "+linenumber+". Missing 'type:'");
					continue;
				}
				data.put("line",linenumber+"");
				data.put("type",line.substring(line.indexOf(":")+1).trim());
			}
			else if(line.length()==0)
				continue;
			else if(line.indexOf(":")==-1)
			{
				log.severe("Error in parsing file ("+file.getName()+") on line "+linenumber+". Missing ':'");
			}
			else
			{
				data.put(line.substring(0, line.indexOf(":")).trim(),line.substring(line.indexOf(":")+1).trim());
			}
		}
		br.close();
		if(data.size()>0)
		{
			loadFilter(data,file.getName(),log);
			data.clear();
		}
	}
	private static void loadFilter(Map<String,String> data, String fileName,Logger log)
	{
		if(!data.containsKey("expression"))
		{
			log.severe("Error in parsing file ("+fileName+"). In type '"+data.get("type")+"' on line "+data.get("line")+"."
					+ " Missing expression statement");
			return;
		}
		else
		{
			String expr=data.get("expression");
			String logger=(data.containsKey("logger"))?data.get("logger"):"";
			String logtofile=(data.containsKey("log-to-file"))?data.get("log-to-file"):"";
			String replace=(data.containsKey("logger"))?data.get("logger"):"";
			boolean logchanged=(data.containsKey("log-changed"))?Boolean.parseBoolean(data.get("log-changed")):false;
			boolean cs=(data.containsKey("case-sensitive"))?Boolean.parseBoolean(data.get("case-sensitive")):false;
		switch(data.get("type").toLowerCase())
		{
		case "exactfilter":
			filters.add(new LogFilter.ExactFilter(expr, logtofile, logger, cs));
			break;
		case "loggerfilter":
			filters.add(new LogFilter.LoggerFilter(expr, logtofile, logger));
			break;
		case "levelfilter":
			filters.add(new LogFilter.LevelFilter(expr, logtofile, logger, Level.parse(expr)));
			break;
		case "regexfilter":
			filters.add(new LogFilter.RegexFilter(expr, logtofile, logger));
			break;
		case "alternatefilter":
			if(!data.containsKey("replace"))
			{
				log.severe("Error in parsing file ("+fileName+"). In type '"+data.get("type")+"' on line "+data.get("line")+"."
						+ " Missing replace statement");
				return;
			}
			filters.add(new LogFilter.AlternateFilter(expr, logtofile, logger,data.get("replace"),logchanged));
			break;
		default:
			log.severe("Error in parsing file ("+fileName+"). Unknow type '"+data.get(0)+"'");
			return;
		}
		}
	}
	
	private static void createExampleFilter() throws IOException
	{
		File f=new File(LogCleaner.instance.getPluginFolder()+"/filters/examplefilter.filter");
		InputStream stream = LogCleaner.class.getClassLoader().getResourceAsStream("cz/majncraft/plugins/logCleaner/example/ExampleFilter.txt");//note that each / is a directory down in the "jar tree" been the jar the root of the tree"
	    if (stream == null) {
	        //send your exception or warning
	    }
	    OutputStream resStreamOut = null;
	    int readBytes;
	    byte[] buffer = new byte[4096];
	    try {
	        resStreamOut = new FileOutputStream(f);
	        while ((readBytes = stream.read(buffer)) > 0) {
	            resStreamOut.write(buffer, 0, readBytes);
	        }
	    } catch (IOException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	    } finally {
	        stream.close();
	        resStreamOut.close();
	    }

	}
}
