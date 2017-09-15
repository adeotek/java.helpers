package com.hinter.java.rabbitmq.utils;

import com.google.gson.JsonObject;
import com.hinter.java.utils.Helpers;

import java.util.Date;

public abstract class AbstractMessageProcessor {
    protected MessageProcessorStructure _processor;

    public AbstractMessageProcessor() {}

    public void SetMessageProcessorStructure(MessageProcessorStructure processor) throws Exception {
        if (processor==null) {
            throw new Exception("Invalid MessageProcessorStructure instance");
        }
        _processor = processor;
    }//SetMessageProcessorStructure

    public static String GetMessageFile(String path, String subDirectory, String prefix, String extension) throws Exception {
        if (Helpers.isStringEmptyOrNull(path)) {
            throw new Exception("Invalid path");
        }
        if (!Helpers.isStringEmptyOrNull(subDirectory)) {
            if (!Helpers.createSubDirectory(path, subDirectory)) {
                throw new Exception("Unable to create sub-directory [" + path + "/" + subDirectory + "]");
            }
        }
        return path + subDirectory + System.getProperty("file.separator") + (prefix==null ? "" : prefix + "_") + (new Date()).getTime() + (Helpers.isStringEmptyOrNull(extension) ? ".msg" : extension);
    }//getMessageFileName

    protected String getMessageFileName(String subDirectory, String prefix, String extension) throws Exception {
        if (_processor==null || Helpers.isStringEmptyOrNull(_processor.getUnprocessedPath())) {
            throw new Exception("Invalid unprocessed path");
        }
        return GetMessageFile(_processor.getUnprocessedPath(), subDirectory, prefix, extension);
    }//getMessageFileName

    protected boolean logUnprocessedMessage(JsonObject json, String prefix) throws Exception {
        return Helpers.writeToFile(json.toString(), getMessageFileName(null, prefix, ".json"));
    }//logUnprocessedMessage

    public abstract boolean processMessage(JsonObject json);
}//AbstractMessageProcessor