package com.bear.reseeding.test;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ReadRemoteZipContent {
    public static void main(String[] args) {
        String zipFileURL = "http://8.148.10.90:9000/efuavkmz/reseeding/kmzTasks/1/%E6%9C%AA%E5%91%BD%E5%90%8D.kmz"; // 网络压缩文件URL

        try {
            // 创建URL对象
            URL url = new URL(zipFileURL);

            // 打开URL连接
            InputStream inputStream = url.openStream();
//            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(inputStream));
            ZipInputStream zis = new ZipInputStream(inputStream);
            ZipEntry entry;

            // 遍历压缩文件中的每一个条目
            while ((entry = zis.getNextEntry()) != null) {
                String fileName = entry.getName();
                System.out.println("File: " + entry.getName());
                if(!entry.isDirectory()){

                    if(fileName.endsWith(".kml")){
                        // 读取文件内容
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int count;
                        while ((count = zis.read(buffer)) != -1) {
                            baos.write(buffer, 0, count);
                        }

                        // 将文件内容输出到控制台
                        System.out.println("Content: " + baos.toString("UTF-8")); // 这里假设内容是UTF-8编码的，如果不是，需要根据实际情况修改
                        baos.close();
                        Map docKmlData = readKml(baos);
                        //获取数据流 赋值到本地 、判断是否可以解压 解压 获取里面的数据信息
                        String msg = docKmlData.get("msg").toString();
                    }
                }
                zis.closeEntry();
            }

            zis.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map readKml(ByteArrayOutputStream byteArrayOutputStream){
        Map map =new HashMap();
        map.put("msg","Kml解析有误");
        String heightStr= "";
        try{
            // 转换为ByteArrayInputStream
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(byteArrayInputStream);
            //根节点 即XML文件的最顶层节点 <kml>
            Element rootElement=document.getRootElement();
            Element docElement = rootElement.element("Document");
            // 尝试获取 missionConfig 节点
            Element missionConfigElement = docElement.element("missionConfig");
            if (missionConfigElement != null) {
                // 检查是否成功获取到 missionConfig 节点
                System.out.println("MissionConfig element: " + missionConfigElement.getName());

                // 尝试获取 takeOffSecurityHeight 节点
                Element takeOffSecurityHeightElement = missionConfigElement.element("takeOffSecurityHeight");
                if (takeOffSecurityHeightElement != null) {
                    // 输出 takeOffSecurityHeight 节点的值
                    heightStr=  takeOffSecurityHeightElement.getText();
                    System.out.println("TakeOffSecurityHeight: " + takeOffSecurityHeightElement.getText());
                } else {
                    System.out.println("TakeOffSecurityHeight element not found.");
                }
            } else {
                System.out.println("MissionConfig element not found.");
            }
//            Element takeOffSecurityHeightElement =docElement.element("wpml:missionConfig").element("wpml:takeOffSecurityHeight");
//            String heightStr= takeOffSecurityHeightElement.getStringValue();

            // 创建根字节-->子字节 <Folder></Folder>
            Element folderElement = rootElement.element("Document").element("Folder");
//            Element folderElement = (Element) rootElement.selectSingleNode("Document/Folder");
            if(folderElement == null){
                map.put("msg","解析节点失败");
                return map;
            }
            // 获取<Placemark>节点列表
            List<Element> placemarkList = folderElement.elements("Placemark");
            List<Map> PointList = new ArrayList<>();
            for (Element placemark : placemarkList) {
                Map pointMap = new HashMap();
                // 解析<coordinates>节点
                Element coordinates = placemark.element("Point").element("coordinates");
                String coordinatesValue = coordinates.getTextTrim();
                String[] values = coordinatesValue.split(",");
                String longitude = values[0]; // 经度
                String latitude = values[1]; // 纬度
                pointMap.put("Longitude",longitude);
                pointMap.put("Latitude",latitude);
                // 其他字段类似，根据实际需要解析其他节点
                PointList.add(pointMap); //
            }
            map.put("unifiedHeight",heightStr);
            map.put("msg","解析成功！");
            map.put("PointList",PointList);
            return  map;

        }catch (Exception e){
            System.out.println("写入文件压缩为ZIP格式失败："+e.getMessage());
        }finally {
            return map;
        }
    }
}


