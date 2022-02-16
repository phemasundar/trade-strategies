package com.strategies.trade.copied;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hemasundarpenugonda
 */
public class Genaral {
    @Test
    public void compareJsonADGroups() throws IOException {
        String harish = Files.lines(Paths.get("harish.json")).collect(Collectors.joining("\n"));
        String hema = Files.lines(Paths.get("hema.json")).collect(Collectors.joining("\n"));
        JSONArray harish_obj = new JSONArray(harish);
        JSONArray hema_obj = new JSONArray(hema);
        List<JSONObject> harish_JsonObjects = extracted(harish_obj);
        List<JSONObject> hema_JsonObjects = extracted(hema_obj);

        List<String> harish_name = harish_JsonObjects.stream()
                .map(item -> item.get("name").toString())
                .collect(Collectors.toList());
        List<String> hema_name = hema_JsonObjects.stream()
                .map(item -> item.get("name").toString())
                .collect(Collectors.toList());
        List<String> strings = new ArrayList<>(harish_name);
        strings.addAll(hema_name);
        List<String> collect = strings.stream()
                .distinct()
                .collect(Collectors.toList());
        System.out.println(collect);
    }

    private List<JSONObject> extracted(JSONArray objects) {
        List<JSONObject> jsonObjects = new ArrayList<>();
        for (Object indObj : objects) {
            JSONObject jsonObj = (JSONObject) indObj;
            jsonObjects.add(jsonObj);
        }
        return jsonObjects;
    }
}
