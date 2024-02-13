package com.jsonShift;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.vertx.ext.web.RoutingContext;

/**
 * This class provides methods to convert JSON data to Java objects and perform JSON shifting operations.
 */
public class jsonToJava {
    private InputStream stream;

    public jsonToJava(InputStream stream) {
        this.stream = stream;
    }

    // Read the JSON input stream and convert it to a list of maps
    public List<Map<String, Object>> read() throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        List<Map<String, Object>> response = mapper.readValue(stream, new TypeReference<List<Map<String, Object>>>(){});
        return response;
    }

    // Convert a JSONObject to a Map<String, Object>
    public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<String, Object>();

        if (json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    // Convert a JSONObject to a Map<String, Object>
    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    // Convert a JSONArray to a List<Object>
    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    // Shift the JSON object received in the request body and send the shifted JSON as the response
    public static void shift(RoutingContext routingContext) {
        String body = routingContext.getBodyAsString();
        try {
            JSONObject json = new JSONObject(body);
            Map<String, Object> map = jsonToMap(json);
            routingContext.response().putHeader("content-type", "application/json").end(new JSONObject(map).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
