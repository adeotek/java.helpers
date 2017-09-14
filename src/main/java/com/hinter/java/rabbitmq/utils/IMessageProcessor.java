package com.hinter.java.rabbitmq.utils;

import com.google.gson.JsonObject;

public interface IMessageProcessor {
    boolean processMessage(JsonObject json);
}//IMessageProcessor
