package com.hinter.java.rabbitmq.utils;

import com.google.gson.JsonObject;

public abstract class AbstractMessageProcessor {
    protected MessageProcessorStructure _processor;

    public AbstractMessageProcessor() {}

    public void SetMessageProcessorStructure(MessageProcessorStructure processor) throws Exception {
        if (processor==null) {
            throw new Exception("Invalid MessageProcessorStructure instance");
        }
        _processor = processor;
    }//SetMessageProcessorStructure

    public abstract boolean processMessage(JsonObject json);
}//AbstractMessageProcessor