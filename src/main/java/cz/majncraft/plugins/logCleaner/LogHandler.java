package cz.majncraft.plugins.logCleaner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class LogHandler extends Handler {

	private static Map<String,LogHandler> loghandlers=new HashMap<String,LogHandler>();
	public static LogHandler addHandler(Handler h,String name)
	{
		if(loghandlers.containsKey(name))
		{
			loghandlers.get(name).add(h);
			return loghandlers.get(name);
		}
		LogHandler a=new LogHandler(h);
		loghandlers.put(name, a);
		return a;
		
	}
	public static LogHandler removeHandler(Handler h,String name)
	{
		loghandlers.get(name).remove(h);
		if(loghandlers.get(name).canbeDestroyed())
		{
			LogHandler rh=loghandlers.get(name);
			loghandlers.remove(name);
			return rh;
		}
		return null;
	}
	private static List<Handler> handlers=new ArrayList<Handler>();
	public LogHandler(Handler handler)
	{
		handlers.add(handler);
	}
	public boolean canbeDestroyed()
	{
		return handlers.size()==0;
	}
	public void add(Handler h)
	{
		handlers.add(h);
	}
	public void remove(Handler h)
	{
		handlers.remove(h);
	}

	@Override
	public void close() throws SecurityException {
		for(Handler h:handlers)
			h.close();
	}

	@Override
	public void flush() {
		for(Handler h:handlers)
			h.close();
	}
	private void postLogRecord(LogRecord record)
	{
		for(Handler h:handlers)
			h.publish(record);
	}
	
	@Override
	public void publish(LogRecord record) {
		record=LogFilters.testLog(record);
		if(record!=null)
			postLogRecord(record);
	}
}
