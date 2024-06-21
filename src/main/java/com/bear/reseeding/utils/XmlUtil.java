package com.bear.reseeding.utils;

import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.io.OutputFormat;
import org.dom4j.Element;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
public class XmlUtil {

    public static String  copyKml(String xmlFilePathStr, String copyPathStr ,Integer index){
        String  filePath = null;
        try{
            // 创建 SAXReader 对象
            SAXReader reader = new SAXReader();
            // 读取 XML 文件
            Document document = reader.read(new File(xmlFilePathStr));
            //#region 获取旧文件所需dom
            // 获取根节点
            Element oldRootElement = document.getRootElement();
            // 创建根字节-->子字节 <Folder></Folder>
            Element folderElement = oldRootElement.element("Document").element("Folder");
            // 获取<Document>节点
            Element documentEle = oldRootElement.element("Document");
            // 获取<Placemark>节点列表
            List<Element> placemarkList = folderElement.elements("Placemark");

            //#endregion

            //#region 新文件dom
            Element newRoot = DocumentHelper.createElement("kml");
            Namespace namespace = Namespace.get("http://www.opengis.net/kml/2.2");
            // 创建新的Document
            Document newDocument = DocumentHelper.createDocument(newRoot);
             // Element newRoot = newDocument.addElement("kml");
            newRoot.add(namespace);
            //根节点添加属性
            newRoot.addAttribute("xmlns", "http://www.opengis.net/kml/2.2")
                    .addNamespace("wpml", "http://www.dji.com/wpmz/1.0.0");
            // 复制Document元素及其所有子元素到newRoot
            newRoot.add(documentEle.createCopy());
            // 移除Document元素中的所有Placemark元素
            Element newfolderElement = newRoot.element("Document").element("Folder");
            List<Element> placemarks = newfolderElement.elements("Placemark");
            int i =0;
            for (Element placemark : placemarks) {
                Element indexElement = placemark.element("index");
                String indexStr =  indexElement.getText();
                if (indexElement != null &&  Integer.parseInt(indexElement.getText()) <= index  ) {
                    placemark.detach();
                }else {
//                   Element indexEle=  placemark.element("index").addText(String.valueOf(i));
                    // 获取 wpml:index 元素
                    Element wpmlIndexElement = placemark.element("index");
                    // 修改 wpml:index 的值
                    wpmlIndexElement.setText(String.valueOf(i));
                    i++;
                }
            }
            //#endregion

            //#region 生成新的文件 、、
            // 生成新的XML文件
            OutputFormat format = OutputFormat.createPrettyPrint();
//            format.setIndent(true);
//            format.setEncoding("UTF-8");
//            format.setTrimText(false);
            // format.setNewLineAfterDeclaration(false);
            FileWriter fileWriter = new FileWriter(copyPathStr);
             // FileOutputStream fileOutputStream = new FileOutputStream(file);
            XMLWriter xmlWriter = new XMLWriter(fileWriter, format);
            xmlWriter.write(newDocument);
            xmlWriter.close();
            System.out.println("新的XML文件已生成成功。");
            //#endregion

            filePath = copyPathStr;
        }catch (Exception e){
            LogUtil.logError("e:"+e.getMessage());


        }finally {
            return filePath;
        }

    }


    public static File copywpml(String xmlFilePathStr, String copyPathStr ,Integer index){
        File file = null;
        try{
            // 创建 SAXReader 对象
            SAXReader reader = new SAXReader();
            // 读取 XML 文件
            Document document = reader.read(new File(xmlFilePathStr));
            //#region 获取旧文件所需dom
            // 获取根节点
            Element oldRootElement = document.getRootElement();
            // 创建根字节-->子字节 <Folder></Folder>
            Element folderElement = oldRootElement.element("Document").element("Folder");
            // 获取<Document>节点
            Element documentEle = oldRootElement.element("Document");
            // 获取<Placemark>节点列表
            List<Element> placemarkList = folderElement.elements("Placemark");

            //#endregion

            //#region 新文件dom
            Element newRoot = DocumentHelper.createElement("kml");
            Namespace namespace = Namespace.get("http://www.opengis.net/kml/2.2");
            // 创建新的Document
            Document newDocument = DocumentHelper.createDocument(newRoot);
            // Element newRoot = newDocument.addElement("kml");
            newRoot.add(namespace);
            //根节点添加属性
            newRoot.addAttribute("xmlns", "http://www.opengis.net/kml/2.2")
                    .addNamespace("wpml", "http://www.dji.com/wpmz/1.0.0");
            // 复制Document元素及其所有子元素到newRoot
            newRoot.add(documentEle.createCopy());
            // 移除Document元素中的所有Placemark元素
            Element newfolderElement = newRoot.element("Document").element("Folder");
            List<Element> placemarks = newfolderElement.elements("Placemark");
            int i =0;
            for (Element placemark : placemarks) {
                Element indexElement = placemark.element("index");
                String indexStr =  indexElement.getText();
                if (indexElement != null &&  Integer.parseInt(indexElement.getText()) <= index  ) {
                    placemark.detach();
                }else {
//                   Element indexEle=  placemark.element("index").addText(String.valueOf(i));
                    // 获取 wpml:index 元素
                    Element wpmlIndexElement = placemark.element("index");
                    // 修改 wpml:index 的值
                    wpmlIndexElement.setText(String.valueOf(i));
                    i++;
                }
            }
            //#endregion

            //#region 生成新的文件 、、
            // 生成新的XML文件
            OutputFormat format = OutputFormat.createPrettyPrint();
//            format.setIndent(true);
//            format.setEncoding("UTF-8");
//            format.setTrimText(false);
            // format.setNewLineAfterDeclaration(false);
            FileWriter fileWriter = new FileWriter(copyPathStr);
            // FileOutputStream fileOutputStream = new FileOutputStream(file);
            XMLWriter xmlWriter = new XMLWriter(fileWriter, format);
            xmlWriter.write(newDocument);
            xmlWriter.close();
            System.out.println("新的XML文件已生成成功。");
            //#endregion
            file = new File(copyPathStr);

        }catch (Exception e){
            LogUtil.logError("e:"+e.getMessage());


        }finally {
            return file;
        }

    }



}
