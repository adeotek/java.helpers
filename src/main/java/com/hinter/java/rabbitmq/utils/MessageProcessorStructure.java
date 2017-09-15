package com.hinter.java.rabbitmq.utils;

import com.hinter.java.utils.Helpers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public final class MessageProcessorStructure {
    private String _tag;
    private String _type;
    private String _className;
    private String _unprocessedPath;
    private String _repositoryPath;
    private HashMap<String, String> _dbConfig;

    public String getTag() { return _tag; }
    public String getType() { return _type; }
    public String getClassName() { return _className; }
    public String getUnprocessedPath() { return _unprocessedPath; }
    public String getRepositoryPath() { return _repositoryPath; }
    public HashMap<String, String> getDbConfig() { return _dbConfig; }

    public MessageProcessorStructure(String tag, String type, String className, String unprocessedPath, String repositoryPath, HashMap<String, String> dbConfig) {
        _tag = tag;
        _type = type;
        _className = className;
        _unprocessedPath = unprocessedPath;
        _repositoryPath = repositoryPath;
        _dbConfig = dbConfig;
    }//MessageProcessorStructure

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("-Global").append("\n");
        sb.append("Tag").append(": ").append((_tag==null ? "[NULL]" : _tag)).append("\n");
        sb.append("Type").append(": ").append((_type==null ? "[NULL]" : _type)).append("\n");
        sb.append("Class").append(": ").append((_className==null ? "[NULL]" : _className)).append("\n");
        sb.append("UnprocessedPath").append(": ").append((_unprocessedPath==null ? "[NULL]" : _unprocessedPath)).append("\n");
        sb.append("RepositoryPath").append(": ").append((_repositoryPath==null ? "[NULL]" : _repositoryPath)).append("\n");
        if (_dbConfig==null) {
            sb.append("-DBConfig").append(": ").append("[NULL]").append("\n");
        } else {
            sb.append("-DBConfig").append("\n");
            for (Map.Entry<String, String> kv : _dbConfig.entrySet()) {
                sb.append("DB-").append(kv.getKey()).append(": ").append((kv.getValue()==null ? "[NULL]" : kv.getValue())).append("\n");
            }
        }
        return sb.toString();
    }//toString

    public AbstractMessageProcessor getProcessorInstance() throws Exception {
        if (Helpers.isStringEmptyOrNull(this.getClassName())) {
            throw new Exception("Invalid class name");
        }
        try {
            AbstractMessageProcessor processorInstance = (AbstractMessageProcessor) Class.forName(this.getClassName()).newInstance();
            processorInstance.SetMessageProcessorStructure(this);
            return processorInstance;
        } catch (ClassNotFoundException cnfe) {
            throw new Exception("ClassNotFoundException: " + cnfe.getMessage(), cnfe);
        } catch (NoSuchMethodException nsme) {
            throw new Exception("NoSuchMethodException: " + nsme.getMessage(), nsme);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException inse) {
            throw new Exception(inse.getMessage(), inse);
        }
    }//getProcessorInstance
}//MessageProcessorStructure
