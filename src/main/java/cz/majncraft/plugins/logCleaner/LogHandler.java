package cz.majncraft.plugins.logCleaner;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class LogHandler extends Handler {

	private Handler[] handlers;
	public LogHandler(Handler[] handlers)
	{
		this.handlers=handlers;
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
