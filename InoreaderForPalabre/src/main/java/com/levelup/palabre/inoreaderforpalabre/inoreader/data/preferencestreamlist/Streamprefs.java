
package com.levelup.palabre.inoreaderforpalabre.inoreader.data.preferencestreamlist;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.levelup.palabre.inoreaderforpalabre.BuildConfig;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Streamprefs implements JsonDeserializer<Map<String, Object>> {

    private static final String TAG = Streamprefs.class.getSimpleName();

    @Override
    public Map<String, Object> deserialize(JsonElement json, Type unused, JsonDeserializationContext context)
            throws JsonParseException {
        // if not handling primitives, nulls and arrays, then just
        if (!json.isJsonObject()) throw new JsonParseException("some meaningful message");

        Map<String, Object> result = new HashMap<String, Object>();
        JsonObject jsonObject = json.getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            JsonElement element = entry.getValue();
            if (element.isJsonPrimitive()) {
                result.put(key, element.getAsString());
            } else if (element.isJsonObject()) {
                result.put(key, context.deserialize(element, unused));
            } else if (element.isJsonArray()) {
                Type listType = new TypeToken<ArrayList<Pref>>() {
                }.getType();
                result.put(key, context.deserialize(element, listType));
            } else {
                if (BuildConfig.DEBUG)
                    Log.d(TAG, "element: " + element.getClass() + " - " + element);
                throw new JsonParseException("some meaningful message");
            }
        }
        return result;
    }
}
