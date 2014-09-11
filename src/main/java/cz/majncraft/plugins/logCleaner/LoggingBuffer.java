package cz.majncraft.plugins.logCleaner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.logging.LogRecord;


public class LoggingBuffer {

	public static void log(String file,LogRecord log)
	{
		File f=new File(LogCleaner.instance.getCustomLogFolder()+"file");
		Writer output;
		try {
			output = new BufferedWriter(new FileWriter(f));
		    Date date = new Date(log.getMillis());
		    String d="["+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds()+"] ";
		    String c="["+log.getThreadID()+"/"+log.getLevel()+"] ["+log.getLoggerName()+"]: ";
			output.append(d+c+log.getMessage());
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
