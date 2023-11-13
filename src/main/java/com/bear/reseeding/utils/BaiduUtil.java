package com.bear.reseeding.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class BaiduUtil {

    private static final  String AK = "XeHpbE6VPXWaazuNmUCTl8qFUwmgzZrC";
    public static final String BAIDU_MAP_REVERSE_GEOCODE="http://api.map.baidu.com/reverse_geocoding/v3/?ak=XeHpbE6VPXWaazuNmUCTl8qFUwmgzZrC&output=json&coordtype=wgs84ll&location=";

    /**
     * 调用百度接口Api转换地址为经纬度
     * @param addr 地址
     * @return 经纬度
     * @throws IOException
     */
    public static Object[] getCoordinate(String addr) throws IOException {
        String lon = null;//经度
        String lat = null;//纬度
        String address = null;
        try {
            address = java.net.URLEncoder.encode(addr, "UTF-8");
        }catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        //String key = "f247cdb592eb43ebac6ccd27f796e2d2";
        String key = "XeHpbE6VPXWaazuNmUCTl8qFUwmgzZrC";
        String url = String .format("http://api.map.baidu.com/geocoder?address=%s&output=json&key=%s", address, key);

        URL myURL = null;
        URLConnection httpsConn = null;
        try {
            myURL = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        InputStreamReader insr = null;
        BufferedReader br = null;
        try {
            httpsConn = (URLConnection) myURL.openConnection();// 不使用代理
            if (httpsConn != null) {
                insr = new InputStreamReader( httpsConn.getInputStream(), "UTF-8");
                br = new BufferedReader(insr);
                String data = null;
                int count = 1;
                while((data= br.readLine())!=null){
                    if(count==5){
                        lon = (String)data.subSequence(data.indexOf(":")+1, data.indexOf(","));//经度
                        count++;
                    }else if(count==6){
                        lat = data.substring(data.indexOf(":")+1);//纬度
                        count++;
                    }else{
                        count++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(insr!=null){
                insr.close();
            }
            if(br!=null){
                br.close();
            }
        }
        return new Object[]{lat,lon};
    }

    /**
     * 百度地图经纬度转换为地址
     * 入参 :longitude,经度
     * 入参：latitude，纬度
     * 返回String类型的地址
     */
    public static String getAddress(String lng, String lat) {
        if(StringUtils.isNotEmpty(lng)&&StringUtils.isNotEmpty(lat)){
            String location=lat+","+lng;
            BufferedReader in = null;
            URL tirc = null;
            try {
                tirc = new URL(BaiduUtil.BAIDU_MAP_REVERSE_GEOCODE+location);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            try {
                URLConnection connection = tirc.openConnection();
                connection.setDoOutput(true);
                in = new BufferedReader(new InputStreamReader(tirc.openStream(), "UTF-8"));
                String res;
                StringBuilder sb = new StringBuilder("");
                while ((res = in.readLine()) != null) {
                    sb.append(res.trim());
                }
                String str = sb.toString();
                ObjectMapper mapper = new ObjectMapper();
                if (StringUtils.isNotEmpty(str)) {
                    JsonNode jsonNode = mapper.readTree(str);
                    jsonNode.findValue("status").toString();
                    JsonNode resultNode = jsonNode.findValue("result");
                    JsonNode locationNode = resultNode.findValue("formatted_address");
                    String address = locationNode.asText();
                    return address;
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }



}
