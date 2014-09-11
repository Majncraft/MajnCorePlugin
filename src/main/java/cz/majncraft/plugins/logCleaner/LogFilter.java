package cz.majncraft.plugins.logCleaner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class LogFilter {

	protected String expression;
	protected String logfile;
	protected String logger;
	
	protected LogFilter(String expr,String file,String logger)
	{
		this.expression=expr;
		this.logfile=file;
		this.logger=logger;
	}
	public abstract LogRecord match(LogRecord record); 
	
	protected boolean isValidLogger(String loggerName)
	{
		return (logger==null || logger.equals("")||logger.equals(loggerName));
	}
	
	public static class ExactFilter extends LogFilter
	{
		protected ExactFilter(String expr, String file, String logger,boolean casesensitive) {
			super(expr, file, logger);
			this.casesensitive=casesensitive;
		}
		private boolean casesensitive=true;
		@Override
		public LogRecord match(LogRecord record) {
			String d=record.getMessage();
			if(casesensitive?d.equals(expression):d.equalsIgnoreCase(expression) && 
					isValidLogger(record.getLoggerName()))
			{
				LoggingBuffer.log(logfile,record);
				return null;
			}
			return record;
		}
		
	}
	
	public static class LoggerFilter extends LogFilter
	{

		protected LoggerFilter(String expr, String file, String logger) {
			super(expr, file, logger);
		}

		@Override
		public LogRecord match(LogRecord record) {
			if(expression.equals(record.getLoggerName()))
			{
				LoggingBuffer.log(logfile,record);
				return null;
			}
			return record;
		}
		
	}
	
	public static class LevelFilter extends LogFilter
	{
		private Level level;
		protected LevelFilter(String expr, String file, String logger,Level level) {
			super(expr, file, logger);
			this.level=level;
		}

		@Override
		public LogRecord match(LogRecord record) {
			if(isValidLogger(record.getLoggerName()) && level==record.getLevel())
			{
				LoggingBuffer.log(logfile,record);
				return null;
			}
			return record;
		}
	}
	
	public static class RegexFilter extends LogFilter
	{
		private Pattern regex;
		protected RegexFilter(String expr, String file, String logger) {
			super(expr, file, logger);
			regex=Pattern.compile(expression);
		}

		@Override
		public LogRecord match(LogRecord record) {
			Matcher match=regex.matcher(record.getMessage());
			if(isValidLogger(record.getLoggerName()) && match.find())
			{
				LoggingBuffer.log(logfile,record);
				return null;
			}
			return record;
		}
		
	}
	
	public static class AlternateFilter extends LogFilter
	{

		private boolean changed=false;
		private String replace;
		private Pattern regex;
		protected AlternateFilter(String expr, String file, String logger, String replace,boolean changed) {
			super(expr, file, logger);
			regex=Pattern.compile(expression);
			this.replace=replace;
			this.changed=changed;
			prepareStatement();
		}

		@Override
		public LogRecord match(LogRecord record) {
			List<String> matches=new ArrayList<>();
			Matcher match=regex.matcher(record.getMessage());
			while(match.find())
			{
				for(int i=0; i<match.groupCount();i++)
					matches.add(match.group(i));
			}
			if(isValidLogger(record.getLoggerName()) && matches.size()>0)
			{
				if(!changed)
					LoggingBuffer.log(logfile,record);
				record.setMessage(getChangedMsg(matches));
				if(changed)
					LoggingBuffer.log(logfile,record);
			}
			return record;
		}
		private Set<Integer> stateMap=new HashSet<>();
		private void prepareStatement()
		{
			int pos=-1;
			String num="";
			while(pos<replace.length())
			{
				if(num.equals(""))
				{
					pos=replace.indexOf("$",pos);
					if(pos==-1)
						break;
					pos++;
					if(Character.isDigit(replace.charAt(pos)))
						num=""+replace.charAt(pos+1);
				}
				else
				{
					pos++;
					if(Character.isDigit(replace.charAt(pos)))
						num+=replace.charAt(pos+1);
					else
					{
						stateMap.add(Integer.parseInt(num));
						num="";
					}
				}
			}
		}
		private String getChangedMsg(List<String> matches)
		{
			String msg=replace;
			for(Integer i:stateMap)
			{
				if(matches.size()>i)
				{
					msg=msg.replace("$"+i, matches.get(i));
				}
			}
			return msg;
		}
		
	}
}
