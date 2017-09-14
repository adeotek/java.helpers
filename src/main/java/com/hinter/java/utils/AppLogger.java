package com.hinter.java.utils;

import java.io.IOException;
import java.util.logging.*;

public final class AppLogger {
    protected Logger _logger;

    public static Logger GetNewLogger(String appName, String logLevel, boolean useConsole) {
        Logger logger = Logger.getLogger(appName);
        logger.setUseParentHandlers(false);
        if(logLevel.equals("off")) {
            logger.setLevel(Level.OFF);
        } else if(logLevel.equals("warning")) {
            logger.setLevel(Level.WARNING);
        } else if(logLevel.equals("info")) {
            logger.setLevel(Level.INFO);
        } else if(logLevel.equals("all")) {
            logger.setLevel(Level.ALL);
        } else {
            logger.setLevel(Level.SEVERE);
        }
        if(useConsole) {
            try {
                ConsoleHandler handler = new ConsoleHandler();
                handler.setFormatter(new MyFormatter());
                logger.addHandler(handler);
            } catch (Exception e) {
                System.out.println("Add console logging handler Exception: " + e.getMessage());
            }
        }
        return logger;
    }//GetInstance

    public static Logger AddLoggerFileHandler(Logger logger, String fileName) throws IOException {
        if (Helpers.isStringEmptyOrNull(fileName)) {
            throw new IOException("Invalid file name");
        }
        FileHandler logFileHandler = new FileHandler(fileName, true);
        logFileHandler.setFormatter(new MyFormatter());
        logger.addHandler(logFileHandler);
        return logger;
    }//AddLoggerFileHandler

    public AppLogger(String appName, String logLevel, boolean useConsole) {
        _logger = GetNewLogger(appName, logLevel, useConsole);
    }//AppLogger

    public AppLogger(String appName, String logLevel) {
        this(appName, logLevel, true);
    }//AppLogger

    public boolean addFileHandler(String fileName) {
        try {
            _logger = AddLoggerFileHandler(_logger, fileName);
            return true;
        } catch (IOException ioe) {
            System.out.println("Add file logging handler IOException: " + ioe.getMessage());
        } catch (Exception e) {
            System.out.println("Add file logging handler Exception: " + e.getMessage());
        }
        return false;
    }//addFileHandler

    public static class MyFormatter extends Formatter {
        public String format(LogRecord record) {
            StringBuilder builder = new StringBuilder(1000);
            builder.append(Helpers.formatDate(record.getMillis(), "yyyy-MM-dd HH:mm:ss.SSS")).append(" ");
            builder.append(record.getSourceClassName()).append("/");
            builder.append(record.getSourceMethodName()).append(" ");
            if (record.getLevel().equals(Level.SEVERE)) {
                builder.append("E/");
            } else if(record.getLevel().equals(Level.WARNING)) {
                builder.append("W/");
            } else if(record.getLevel().equals(Level.INFO)) {
                builder.append("D/");
            } else {
                builder.append("I/");
            }
            builder.append(record.getMessage());
            builder.append("\n");
            return builder.toString();
        }//format

        public String getHead(Handler h) {
            return super.getHead(h);
        }//getHead

        public String getTail(Handler h) {
            return super.getTail(h);
        }//getTail
    }//MyFormatter
}//AppLogger
