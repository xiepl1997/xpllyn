package com.xpllyn.utils.githubpageutil;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * the util to search github page
 * @author xie peiliang
 */
@Component
public class SearchUtil {

    public final String u = "https://api.github.com/search/repositories?q=";

    public JSONObject readJsonFromUrl(String text) {
        JSONObject jsonObject = null;
        try {
            URL url = new URL(u + text);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF8"));
            String rowText = readAll(br);
            jsonObject = JSON.parseObject(rowText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public String readAll(BufferedReader br) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line = "";
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

}
