package server.gamemechanics;


import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ParseJsonTest {


    @Test
    public void testingMessageSystem() {
        JSONObject jsonObject = new JSONObject();
        Set<Long> ids = new HashSet<>();
        ids.add((long) 5);
//        ids.add((long) 6);
//        ids.add((long) 7);
//        ids.add((long) 8);
        for (Long id : ids) {
            try {
                jsonObject.append("id", id);
            } catch (JSONException e) {
                System.out.println("error with append to json");
            }
        }
        System.out.println(jsonObject.toString());
        String str = jsonObject.toString();

        Set<Long> IDS = null;
        List<String> strIds = null;
        try {
            JSONObject obj = new JSONObject(str);
            Gson gson = new Gson();
            strIds = gson.fromJson(str, new TypeToken<List<String>>() {
            }.getType());
//            IDS = obj.get("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        if (IDS != null)
        for (String id : strIds) {
            System.out.println("id: " + id);
        }

    }
}
