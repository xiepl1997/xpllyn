package com.xpllyn.utils.githubpageutil;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * the util to search github page
 *
 * @author xie peiliang
 */
@Component
public class SearchUtil {

    public final String u = "https://api.github.com/search/repositories?q=";

    public JSONObject readJsonFromUrl(String text, int page, String otherConditions) {
        if (!otherConditions.equals("general_match"))
            text = "in:name github.io in:description " + text;
        else {
            otherConditions = "";
        }
        JSONObject jsonObject = null;
        try {
            URL url = new URL(u + URLEncoder.encode(text, "utf-8") + "&per_page=10&page=" + page + otherConditions);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
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

    public class LanguageOrder implements Comparable<LanguageOrder> {
        String language;
        int count;

        public LanguageOrder(String language, int count) {
            this.language = language;
            this.count = count;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        @Override
        public int compareTo(LanguageOrder o) {
            return o.count - this.count;
        }
    }

}
