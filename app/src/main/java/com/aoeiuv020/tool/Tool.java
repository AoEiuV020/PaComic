package com.aoeiuv020.tool;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by AoEiuV020 on 2016/04/07 - 21:50:51
 */
public class Tool {
    private Tool() {
    }

    public static boolean isEmpty(Object object) {
        try {
            if (object == null)
                return true;
            if (object instanceof CharSequence)
                return "".equals(object);
        } catch (Exception e) {
            Logger.v("%s is empty", object);
            //不抛异常;
            return true;
        }
        return false;
    }

    public static void put(JSONObject json, String key, Object obj) {
        if (isEmpty(key))
            return;
        if (obj == null)
            obj = JSONObject.NULL;
        try {
            json.put(key, obj);
        } catch (JSONException e) {
        }
    }

    public static String getString(JSONObject json, String str) {
        String result = null;
        try {
            result = json.getString(str);
        } catch (JSONException e) {
        }
        return result;
    }

    public static Integer getInt(JSONObject json, String key) {
        Integer result = null;
        try {
            result = json.getInt(key);
        } catch (JSONException e) {
        }
        return result;
    }

    public static boolean isEmpty(JSONObject json, String str) {
        return isEmpty(getString(json, str));
    }
}
