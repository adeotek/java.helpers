package com.hinter.java.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Iterator;

public final class JsonUtils {

    public static JsonElement getJsonElement(JsonObject jsonObject, String memberName) {
        if (jsonObject == null || !jsonObject.has(memberName)) {
            return null;
        }
        return jsonObject.get(memberName);
    }//getJsonElement

    public static int getJsonElementAsInt(JsonObject jsonObject, String memberName, int defaultValue) {
        if (jsonObject == null || !jsonObject.has(memberName)) {
            return defaultValue;
        }
        try {
            return jsonObject.get(memberName).getAsInt();
        } catch (Exception se) {
            // silent error
            return defaultValue;
        }
    }//getJsonElementAsInt

    public static int getJsonElementAsInt(JsonObject jsonObject, String memberName) {
        return getJsonElementAsInt(jsonObject, memberName, 0);
    }

    public static long getJsonElementAsLong(JsonObject jsonObject, String memberName, long defaultValue) {
        if (jsonObject == null || !jsonObject.has(memberName)) {
            return defaultValue;
        }
        try {
            return jsonObject.get(memberName).getAsLong();
        } catch (Exception se) {
            // silent error
            return defaultValue;
        }
    }//getJsonElementAsLong

    public static long getJsonElementAsLong(JsonObject jsonObject, String memberName) {
        return getJsonElementAsLong(jsonObject, memberName, 0);
    }

    public static String getJsonElementAsString(JsonObject jsonObject, String memberName, String defaultValue) {
        if (jsonObject == null || !jsonObject.has(memberName)) {
            return defaultValue;
        }
        try {
            return jsonObject.get(memberName).getAsString();
        } catch (Exception se) {
            // silent error
            return defaultValue;
        }
    }//getJsonElementAsString

    public static String getJsonElementAsString(JsonObject jsonObject, String memberName) {
        return getJsonElementAsString(jsonObject, memberName, null);
    }//getJsonElementAsString


    public static boolean searchInJsonArray(JsonArray json, String key, String value) {
        if(json==null || json.size()==0 || Helpers.isStringEmptyOrNull(key)) { return false; }
        boolean result = false;
        try {
            for (JsonElement aJson : json) {
                JsonObject item = (JsonObject) aJson;
                if (item == null) {
                    continue;
                }
                try {
                    String lname = getJsonElementAsString(item, key);
                    if (lname.equals(value)) {
                        result = true;
                        break;
                    }
                } catch (Exception ee) {
                    result = false;
                }
            }
        } catch (Exception e) {
            result = false;
        }
        return result;
    }//searchInJsonArray
}//JsonUtils
