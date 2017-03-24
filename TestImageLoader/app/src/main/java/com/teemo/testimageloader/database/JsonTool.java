package com.teemo.testimageloader.database;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Asus on 2017/3/22.
 */

public class JsonTool {

    public static String createJson(String key, Object object) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(key, object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    public static String createJsonObject(Object obj) {
        Gson gson = new Gson();
        String str = gson.toJson(obj);
        return str;
    }

    public void a() {


        byte b = 1;
        short sh = 0;
        int i = 9;
        char c = 'a';
        String s = "happy";

        switch (b) {}

        switch (sh) {}

        switch (i) {}

        switch (c) {}

        switch (s) {}
    }
}
