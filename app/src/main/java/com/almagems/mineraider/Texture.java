package com.almagems.mineraider;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Texture {
    public int id;
    public float width;
    public float height;
    public String name;

    private Map<String, Rectangle> frames = new HashMap<String, Rectangle>(20);

    // ctor
    public Texture() {
        System.out.println("Texture ctor...");
    }

    public Rectangle getFrame(String key) {
        Rectangle rect = frames.get(key);

        if (rect != null) {
            Rectangle newRect = new Rectangle(rect);
            newRect.y += newRect.h;
            return newRect;
        }

        System.out.println("Frame with key: [" + key + "]. Not found!" );
        return null;
    }

    public void loadFrames(Context context, int jsonResourceId) {
        String jsonText = TextResourceReader.readTextFileFromResource(context, jsonResourceId);

        try {
            JSONObject jObject = new JSONObject(jsonText);
            JSONArray arr = jObject.getJSONArray("frames");
            JSONObject obj, frame;
            String fileName;
            int x, y, w, h;

            for(int i = 0; i < arr.length(); ++i) {
                obj = arr.getJSONObject(i);

                fileName = obj.getString("filename");
                frame = obj.getJSONObject("frame");
                x = frame.getInt("x");
                y = frame.getInt("y");
                w = frame.getInt("w");
                h = frame.getInt("h");
                //System.out.println("FileName is: " + fileName + ", x: " + x + ", y: " + y + ", w: " + w + ", h: " + h);

                frames.put(fileName, new Rectangle(x, y, w, h));
            }
        } catch (final Exception ex) {
            System.out.println( ex.toString() );
        }
    }

    public String toString() {
        return "Texture name:" + this.name + ", w:" + width + ", h:" + height + ", id:" + id;
    }

}
