package com.xpllyn.utils;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Message工具类
 * created by xiepl1997 at 2019-8-21
 */
@Component
public class MessageInfoUtils {

    /**
     * @param request
     * @return 返回用户的IP地址
     */
    public String getUserIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || "unknow".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknow".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknow".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknow".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknow".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.equals("127.0.0.1")) {
            //根据网卡获取ip
            InetAddress inet = null;
            try {
                inet = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            ip = inet.getHostAddress();
        }

        return ip;
    }

    /**
     * 通过用户ip地址获取地理位置（国家、城市）
     *
     * @param ip
     * @return String[0]国家、String[1]城市
     */
    public String[] getAddressByIp(String ip) {
        String[] result = new String[2];

        //高德地图ip定位服务key
        String GAODE_IP_SERVICE_KEY = "011d114ca63f41e8abbb3de3dddb1dc9";
        //查询链接
        String urlstr = "https://restapi.amap.com/v3/ip?key=" + GAODE_IP_SERVICE_KEY + "&ip=" + ip;

        //获取json
        StringBuffer sb = new StringBuffer();
        try {
            URL url = new URL(urlstr);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF8"));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //解析json，采用正则表达式的方式获取province和city
        String reg = ".*?\"province\":\"(.*?)\",\"city\".*?";
        String reg1 = ".*?\"city\":\"(.*?)\",\"adcode\".*?";

        Matcher matcher = Pattern.compile(reg).matcher(sb.toString());
        Matcher matcher1 = Pattern.compile(reg1).matcher(sb.toString());

        result[0] = result[1] = "";
        if (matcher.find()) {
            result[0] = matcher.group(1);
        }
        if (matcher1.find()) {
            result[1] = matcher1.group(1);
        }

        return result;

    }

}