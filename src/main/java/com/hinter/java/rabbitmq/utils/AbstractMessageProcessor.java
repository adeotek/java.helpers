package com.hinter.java.rabbitmq.utils;

import com.google.gson.JsonObject;

public abstract class AbstractMessageProcessor {
    protected MessageProcessorStructure _processor;

    public AbstractMessageProcessor(MessageProcessorStructure processor) {
        _processor = processor;
    }//AbstractMessageProcessor

    public abstract boolean processMessage(JsonObject json);
}//AbstractMessageProcessor