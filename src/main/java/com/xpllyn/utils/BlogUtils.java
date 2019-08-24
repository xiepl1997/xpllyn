package com.xpllyn.utils;

import com.xpllyn.pojo.Blog;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * bolg工具类
 * created by xiepl1997 at 2019-8-18
 */
@Component
public class BlogUtils {

    private String path = "https://xiepl1997.github.io";
    private String source = null;

    /**
     * 获取xiepl1997.github.io文章的标题，日期，url
     */
    public List getBlogInfo(){

        source = getPageSource();

        List<Blog> BlogList = new ArrayList<>();

        Matcher matcher = null;
        Matcher matcher1 = null;
        Matcher matcher2 = null;
        String reg = "<h2 class=\"post-title\">(.*?)</h2>"; //title
        String reg1 = "<time class=\"post-date\" datetime=.*?>(.*?)</time>"; //time
        String reg2 = "(?s)"+"<section class=\"post-preview\">.*?"+"<a class=\"post-link\" href=\"(.*?)\" title=\""; //url

        matcher = Pattern.compile(reg).matcher(source);
        matcher1 = Pattern.compile(reg1).matcher(source);
        matcher2 = Pattern.compile(reg2).matcher(source);

        while(matcher.find() && matcher1.find() && matcher2.find()){
            String title = matcher.group(1);
            String time = matcher1.group(1);
            String url = path + matcher2.group(1);
            Blog blog = new Blog();
            blog.setTitle(title);
            blog.setTime(time);
            blog.setUrl(url);
            BlogList.add(blog);
        }

        return BlogList;
    }

    /**
     * 获取html
     */
    public String getPageSource(){

        StringBuffer sb = new StringBuffer();

        try {
            URL url = new URL(path);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF8"));
            String line;
            while((line = in.readLine()) != null){
                sb.append(line+"\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();

    }
}
