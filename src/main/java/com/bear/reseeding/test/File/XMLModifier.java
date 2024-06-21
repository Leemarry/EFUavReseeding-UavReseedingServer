//package com.bear.reseeding.test.File;
//
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import org.xml.sax.SAXException;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//import java.io.File;
//import java.io.IOException;
//
//public class XMLModifier {
//    public static void main(String[] args) {
//        // XML 文件路径
//        String xmlFilePath = "your_xml_file.xml";
//
//        try {
//            // 创建 DocumentBuilderFactory 对象
//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//
//            // 创建 DocumentBuilder 对象
//            DocumentBuilder builder = factory.newDocumentBuilder();
//
//            // 解析 XML 文件
//            Document document = builder.parse(new File(xmlFilePath));
//
//            // 获取根节点
//            Element root = document.getDocumentElement();
//
//            // 获取所有 Placemark 节点
//            NodeList placemarkNodes = root.getElementsByTagName("Placemark");
//
//            // 用于存储删除后的节点
//            NodeList removedNodes = new NodeList();
//
//            // 遍历 Placemark 节点
//            for (int i = 0; i < placemarkNodes.getLength(); i++) {
//                Node placemarkNode = placemarkNodes.item(i);
//
//                // 获取 wpml:index 属性值
//                String indexValue = ((Element) placemarkNode).getAttribute("wpml:index");
//
//                // 如果 wpml:index 属性值小于等于 1
//                if (Integer.parseInt(indexValue) <= 1) {
//                    // 将节点添加到删除列表中
//                    removedNodes.appendChild(placemarkNode);
//                } else {
//                    // 修改 wpml:index 属性值
//                    ((Element) placemarkNode).setAttribute("wpml:index", String.valueOf(Integer.parseInt(indexValue) - 1));
//                }
//            }
//
//            // 从文档中删除删除列表中的节点
//            for (int i = 0; i < removedNodes.getLength(); i++) {
//                root.removeChild(removedNodes.item(i));
//            }
//
//            // 保存修改后的 XML 文件
//            document.getDocumentElement().normalize();
//            document.getDocumentElement().setAttribute("xmlns:wpml", "http://www.dji.com/wpmz/1.0.0");
//            document.getDocumentElement().setAttribute("xmlns", "http://www.opengis.net/kml/2.2");
//            document.getDocumentElement().setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
//            document.getDocumentElement().setAttribute("xsi:schemaLocation", "http://www.opengis.net/kml/2.2 http://schemas.opengis.net/kml/2.2.0/ogckml22.xsd http://www.dji.com/wpmz/1.0.0 http://www.dji.com/schemas/wpmz.xsd");
//            document.getDocumentElement().setAttribute("createTime", "Tue May 21 10:06:00 GMT+08:00 2024");
//            document.getDocumentElement().setAttribute("updateTime", "Tue May 21 10:06:00 GMT+08:00 2024");
//
//            // 创建一个新的文件对象来保存修改后的 XML 文件
//            File outputFile = new File("output.xml");
//
//            // 创建一个输出流来将修改后的 XML 内容写入文件
//            javax.xml.transform.TransformerFactory transformerFactory = javax.xml.transform.TransformerFactory.newInstance();
//            javax.xml.transform.Transformer transformer = transformerFactory.newTransformer();
//            javax.xml.transform.dom.DOMSource source = new javax.xml.transform.dom.DOMSource(document);
//            javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(outputFile);
//            transformer.transform(source, result);
//
//            System.out.println("XML 文件修改成功并保存到 output.xml 文件中。");
//        } catch (ParserConfigurationException | SAXException | IOException e) {
//            e.printStackTrace();
//        }
//    }
//}