package com.bear.reseeding.utils;

import cn.hutool.core.lang.Pair;
import com.bear.reseeding.MyApplication;
import com.bear.reseeding.entity.EfTaskKmz;
import com.bear.reseeding.entity.EfTaskWps;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * kmz
 */
public class KmzUtil {

//  @Value("/wpmz/template.kml")
// public String BucketNameKmz;

    @Value("${BasePath:C://efuav/reseeding/}")
    public String basePath;


    /**
     * 获取Kmz临时文件的缓存根目录
     *
     * @return 父目录
     */
    private static String getpIngBasePath(String basePath) {
        String path = basePath + "temp" + File.separator + "kmz" + File.separator;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }


    //region 写入

    /**
     * 传入数据处理
     *
     * @param coordinateArray
     * @param takeoffAlt
     * @param homeAltAbs
     * @param altType
     * @param uavType
     * @return
     */
    public static File beforeDataProcessing(List<double[]> coordinateArray, String fileName, double takeoffAlt, double homeAltAbs, int altType,
                                            int uavType, String topbasePath ,Float speed ,Integer startTime,Integer endTime) {
        boolean flag = false;
        File kmzFile = null; // 返回的文件实例
        try {
            // 距离 点集合
            int numPoints = coordinateArray.size();
            double distaceCount = 0.0;
            double distance;
            for (int i = 0; i < numPoints; i++) {
                for (int j = i + 1; j < numPoints; j++) {
                    double[] firstPoint = coordinateArray.get(i);  // 获取第一组坐标点
                    double lng1 = firstPoint[0];  // 获取经度
                    double lat1 = firstPoint[1];  // 获取纬度
                    double[] nextPoint = coordinateArray.get(j);  // 获取第一组坐标点
                    double lng2 = nextPoint[0];  // 获取经度
                    double lat2 = nextPoint[1];  // 获取纬度

                    distance = GisUtil.getDistance(lng1, lat1, lng2, lat2);
                    distaceCount += distance;
//                    System.out.printf("Distance between P%d and P%d: %.2f m\n", i + 1, j + 1, distance);
                    break;
                }
            }



            // region 构建光伏巡检默认参数
            EfTaskWps efTaskWps = new EfTaskWps();
            efTaskWps.setWpsCreateTime(new Date());
            efTaskWps.setWpsUpdateTime(new Date());
            efTaskWps.setWpsGotoWaypointMode(0);
            efTaskWps.setWpsFinishedAction(1);
            efTaskWps.setWpsRcSignalLost(0);
            efTaskWps.setWpsHeadingMode(1);//TODO 机头朝向
            efTaskWps.setWpsGimbalPitchRotationEnabled(true);
            efTaskWps.setWpsAlt(200);   //海拔
            efTaskWps.setWpsSpeed(speed); // 速度 5f
            efTaskWps.setWpsDistance(distaceCount);
            Integer WpsUserTime = (int) (distaceCount / speed);
            efTaskWps.setWpsUserTime(WpsUserTime); //任务预计用时
//            efTaskWps.setWpsUserTime(0);
            //kmlPath
            // endregion

            String wpmlPathString = writewpml(coordinateArray, efTaskWps, fileName, takeoffAlt, homeAltAbs, altType, uavType, topbasePath, startTime, endTime);
            String kmlPathString = writeKml(coordinateArray, efTaskWps, fileName, takeoffAlt, homeAltAbs, altType, uavType, topbasePath, startTime, endTime);
            List<String> pathStrList = new ArrayList<>();
            File file1 = null;
            File file2 = null;
            String path = "";  //父目録，wpml
            if (wpmlPathString != null) {
                file1 = new File(wpmlPathString);
                if (file1.exists()) {
                    String p = file1.getParent();
                    path = file1.getParent();
                }
            }
            if (kmlPathString != null) {
                file2 = new File(kmlPathString);
                if (file2.exists()) {
                    String p = file2.getParent();
                    path = file2.getParent();
                }
            }
            if (path != null && FileUtil.isFileExit(path)) {
                File resFile = new File(path, "res");
                if (!resFile.exists()) {
                    boolean res = resFile.mkdirs();
                    if (!res) {
                        LogUtil.logWarn("创建Kmz文件夹中 res 目录失败！");
                    }
                }
//                pathStrList.add(resFile.getPath());
                // kmz 文件路径+文件名
                String kmzPath = getpIngBasePath(topbasePath) + fileName + File.separator + fileName + ".kmz";
//                FileUtil.createOrUpdateFile(kmzPath);
                //

                pathStrList.add(wpmlPathString);
                pathStrList.add(kmlPathString);
                kmzFile = writeZip(pathStrList, kmzPath);
//                kmzFile = copyKmzStream(kmlPathString, wpmlPathString, kmzPath, topbasePath);
            } else {
                LogUtil.logWarn("Wpml目録不存在：" + path + fileName);
            }

        } catch (Exception e) {
            LogUtil.logError("数据集处理有误：" + e.toString());
        }
        return kmzFile;
    }

    // region    Route distance grouping
    public static List<List<double[]>> groupPoints(List<double[]> coordinateArray) {
        List<List<double[]>> groupedPoints = new ArrayList<>(); // 用于存放分组后的点集合

        // 初始化第一个分组
        List<double[]> group = new ArrayList<>();
        group.add(coordinateArray.get(0));
        groupedPoints.add(group);

        double totalDistance = 0.0; // 用于记录当前分组的累计距离

        // 遍历坐标点集合，进行分组
        for (int i = 0; i < coordinateArray.size() - 1; i++) {
            double[] currentPoint = coordinateArray.get(i);
            double[] nextPoint = coordinateArray.get(i + 1);

            // 计算当前点与下一个点之间的距离
            double distance = GisUtil.getDistance(currentPoint[0], currentPoint[1], nextPoint[0], nextPoint[1]);

            // 如果累计距离超过5000m，则创建新的分组
            if (totalDistance + distance > 15000.0) {
                group = new ArrayList<>();
                groupedPoints.add(group);
                totalDistance = 0.0; // 重置累计距离
            }

            // 将点添加到当前分组中
            group.add(nextPoint);
            totalDistance += distance;
        }

        return groupedPoints;
    }


    public static List<Pair<List<double[]>, Double>> groupPointsWithDistance(List<double[]> coordinateArray) {
        List<Pair<List<double[]>, Double>> groupedPointsWithDistance = new ArrayList<>(); // 用于存放分组后的点集合以及每个分组的距离
        List<Map<String,Object>>  mapList = new ArrayList<>();
        // 初始化第一个分组
        List<double[]> group = new ArrayList<>();
        group.add(coordinateArray.get(0));
        double totalDistance = 0.0; // 用于记录当前分组的累计距离
        // 遍历坐标点集合，进行分组
        for (int i = 0; i < coordinateArray.size() - 1; i++) {
            double[] currentPoint = coordinateArray.get(i);
            double[] nextPoint = coordinateArray.get(i + 1);
            // 计算当前点与下一个点之间的距离
            double distance = GisUtil.getDistance(currentPoint[0], currentPoint[1], nextPoint[0], nextPoint[1]);
            // 如果累计距离超过15000m，则创建新的分组
            if (totalDistance + distance > 8000.0) {
                // 将当前分组及其距离添加到结果列表中
                groupedPointsWithDistance.add(new Pair<>(group, totalDistance));
                group = new ArrayList<>();
                totalDistance = 0.0; // 重置累计距离
            }
            // 将点添加到当前分组中
            group.add(nextPoint);
            totalDistance += distance;
        }
        // 添加最后一个分组及其距离
        if (!group.isEmpty()) {
            groupedPointsWithDistance.add(new Pair<>(group, totalDistance));
        }
        return groupedPointsWithDistance;
    }
// endregion

    public static String writeKml(List<double[]> coordinateArray, EfTaskWps efTaskWps, String fileName, double takeoffAlt, double homeAltAbs,
                                  int altType, int uavType, String topbasePath,Integer startTime,Integer endTime) {
        try {
            Element root = DocumentHelper.createElement("kml");
            Namespace namespace = Namespace.get("http://www.opengis.net/kml/2.2");
            root.add(namespace);
            Document document = DocumentHelper.createDocument(root);
            //根节点添加属性
            root.addAttribute("xmlns", "http://www.opengis.net/kml/2.2")
                    .addNamespace("wpml", "http://www.dji.com/wpmz/1.0.0");
            Element documentElement = root.addElement("Document", "http://www.opengis.net/kml/2.2");
            //文件创建信息
            documentElement.addElement("wpml:createTime").addText(String.valueOf(efTaskWps.getWpsCreateTime()));
            documentElement.addElement("wpml:updateTime").addText(String.valueOf(efTaskWps.getWpsUpdateTime()));
            //Mission Configuration任务配置
            Element missionConfig = documentElement.addElement("wpml:missionConfig");
            missionConfig.addElement("wpml:flyToWaylineMode").addText(efTaskWps.getWpsGotoWaypointMode() == 0 ? "safely" : "pointToPoint");
            missionConfig.addElement("wpml:finishAction").addText("goHome");
            missionConfig.addElement("wpml:exitOnRCLost").addText("executeLostAction"); //goContinue
            missionConfig.addElement("wpml:executeRCLostAction").addText("goBack");
            missionConfig.addElement("wpml:takeOffSecurityHeight").addText(String.valueOf(takeoffAlt));
            missionConfig.addElement("wpml:globalTransitionalSpeed").addText("10");

            //声明无人机型号
            Element droneInfo = missionConfig.addElement("wpml:droneInfo");
            droneInfo.addElement("wpml:droneEnumValue").addText("77");  // 77
            droneInfo.addElement("wpml:droneSubEnumValue").addText("2");  // 1

            //声明有效载荷模型
            Element payloadInfo = missionConfig.addElement("wpml:payloadInfo");
            payloadInfo.addElement("wpml:payloadEnumValue").addText("68");
            payloadInfo.addElement("wpml:payloadSubEnumValue").addText("0");
            payloadInfo.addElement("wpml:payloadPositionIndex").addText("0");

            //为航路点模板设置文件夹
            Element folder = documentElement.addElement("Folder");
            folder.addElement("wpml:templateType").addText("waypoint");
            folder.addElement("wpml:useGlobalTransitionalSpeed").addText("0");
            folder.addElement("wpml:templateId").addText("0");

            Element waylineCoordinateSysParam = folder.addElement("wpml:waylineCoordinateSysParam");
            waylineCoordinateSysParam.addElement("wpml:coordinateMode").addText("WGS84");
            waylineCoordinateSysParam.addElement("wpml:heightMode").addText(altType == 0 ? "relativeToStartPoint" : "EGM96");
            waylineCoordinateSysParam.addElement("wpml:globalShootHeight").addText(String.valueOf(efTaskWps.getWpsAlt()));
//            waylineCoordinateSysParam.addElement("wpml:globalHeight").addText(String.valueOf(efTaskWps.getWpsAlt())); //  ??
            waylineCoordinateSysParam.addElement("wpml:positioningType").addText("GPS");
            // surfaceFollowModeEnable
            waylineCoordinateSysParam.addElement("wpml:surfaceFollowModeEnable").addText("1");
            waylineCoordinateSysParam.addElement("wpml:surfaceFollowModeEnable").addText("100");

            folder.addElement("wpml:autoFlightSpeed").addText(String.valueOf(efTaskWps.getWpsSpeed()));
            folder.addElement("wpml:transitionalSpeed").addText("5");
            folder.addElement("wpml:caliFlightEnable").addText("0");
            folder.addElement("wpml:gimbalPitchMode").addText("usePointSetting"); // usePointSetting

            Element globalWaypointHeadingParam = folder.addElement("wpml:globalWaypointHeadingParam");

            globalWaypointHeadingParam.addElement("wpml:waypointHeadingMode").addText("followWayline");
            globalWaypointHeadingParam.addElement("wpml:waypointHeadingAngle").addText("0");
            globalWaypointHeadingParam.addElement("wpml:waypointPoiPoint").addText("0.000000,0.000000,0.000000");
            globalWaypointHeadingParam.addElement("wpml:waypointHeadingAngleEnable").addText("0");

            folder.addElement("wpml:globalWaypointTurnMode").addText("toPointAndStopWithContinuityCurvature"); //
            double direction = 0;
            for (int i = 0; i < coordinateArray.size(); i++) {
                double[] firstPoint = coordinateArray.get(i);  // 获取第一组坐标点
                double lng = firstPoint[0];  // 获取经度
                double lat = firstPoint[1];  // 获取纬度
//                Map<double[]> entry = coordinateArray.get(i);
//                double lng = entry.get("x");
//                double lat = entry.get("y");
//                double alt = entry.get("z");
                for (int j = i + 1; j < coordinateArray.size(); j++) {

                    double[] nextPoint = coordinateArray.get(j);  // 获取第一组坐标点
                    double longitude1 = nextPoint[0];  // 获取经度
                    double latitude2 = nextPoint[1];  // 获取纬度
//                    Map<String,Double>  nextentry = coordinateArray.get(i+1);
//                    double lng1 = nextentry.get("x");
//                    double lat1 = nextentry.get("y");
//                    double alt1 = nextentry.get("z");
//                    direction =getDirection(lat,lng,lat1,lng1);
                    direction = getDirection(lat, lng, latitude2, longitude1);
                    break;
                }

                Element placemark = folder.addElement("Placemark");
                Element point = placemark.addElement("Point");
//                point.addElement("coordinates").addText("\r\n" + lng + "," + lat + "\r\n");
                point.addElement("coordinates").addText(lng + "," + lat);
                placemark.addElement("wpml:index").addText(String.valueOf(i));
                placemark.addElement("wpml:ellipsoidHeight").addText(altType == 0 ? String.valueOf(takeoffAlt) : String.valueOf(homeAltAbs)); //String.valueOf(alt - homeAltAbs) : String.valueOf(alt)
                placemark.addElement("wpml:height").addText(altType == 0 ? String.valueOf(takeoffAlt) : String.valueOf(homeAltAbs));
                Element waypointHeadingParam = placemark.addElement("wpml:waypointHeadingParam");
                waypointHeadingParam.addElement("wpml:waypointHeadingMode").addText("smoothTransition");
                waypointHeadingParam.addElement("wpml:waypointHeadingAngle").addText(String.valueOf(0));  //朝向？ direction
                waypointHeadingParam.addElement("wpml:waypointPoiPoint").addText("0.000000,0.000000,0.000000");
                waypointHeadingParam.addElement("wpml:waypointHeadingPathMode").addText("followBadArc");
                waypointHeadingParam.addElement("wpml:waypointHeadingPoiIndex").addText("0");
                placemark.addElement("wpml:useGlobalHeight").addText("0");
                placemark.addElement("wpml:useGlobalSpeed").addText("1");  // 确认使用全局速度
                placemark.addElement("wpml:useGlobalHeadingParam").addText("1");
                placemark.addElement("wpml:useGlobalTurnParam").addText("1");
                placemark.addElement("wpml:gimbalPitchAngle").addText(String.valueOf(-90));//云台俯仰？
                //动作组
                Element actionGroup = placemark.addElement("wpml:actionGroup");
                actionGroup.addElement("wpml:actionGroupId").addText(String.valueOf(i));
                actionGroup.addElement("wpml:actionGroupStartIndex").addText(String.valueOf(i));//动作组开始生效的航点
                actionGroup.addElement("wpml:actionGroupEndIndex").addText(String.valueOf(i));//动作组结束生效的航点
                actionGroup.addElement("wpml:actionGroupMode").addText("sequence");//动作执行模式
                Element actionTrigger = actionGroup.addElement("wpml:actionTrigger");//动作组触发器
                actionTrigger.addElement("wpml:actionTriggerType").addText("reachPoint");//动作触发器类型
                //动作列表
//                Element actionZero = actionGroup.addElement("wpml:action");
//                actionZero.addElement("wpml:actionId").addText("0");//动作id
//                actionZero.addElement("wpml:actionActuatorFunc").addText("rotateYaw");//动作类型
//                Element actionActuatorFuncParamZero = actionZero.addElement("wpml:actionActuatorFuncParam");
//                actionActuatorFuncParamZero.addElement("wpml:aircraftHeading").addText(String.valueOf(0));  // 相机朝向
//                actionActuatorFuncParamZero.addElement("wpml:gimbalRotateMode").addText("counterClockwise");
//
                Element actionTwo = actionGroup.addElement("wpml:action");//动作列表
                actionTwo.addElement("wpml:actionId").addText("1");//动作id
                actionTwo.addElement("wpml:actionActuatorFunc").addText("gimbalRotate");//动作类型
                Element actionActuatorFuncParamTwo = actionTwo.addElement("wpml:actionActuatorFuncParam");//动作参数
                actionActuatorFuncParamTwo.addElement("wpml:gimbalRotateMode").addText("absoluteAngle");//云台转动模式
                actionActuatorFuncParamTwo.addElement("wpml:gimbalPitchRotateEnable").addText("1");//是否使能云台俯仰转动
                actionActuatorFuncParamTwo.addElement("wpml:gimbalPitchRotateAngle").addText(String.valueOf(-90));//云台俯仰转动角度
//
//                Element actionThree = actionGroup.addElement("wpml:action");//动作列表
//                actionThree.addElement("wpml:actionId").addText("2");//动作id
//                actionThree.addElement("wpml:actionActuatorFunc").addText("zoom");//动作类型
//                Element actionActuatorFuncParamThree = actionThree.addElement("wpml:actionActuatorFuncParam");//动作参数
//                actionActuatorFuncParamThree.addElement("wpml:focalLength").addText(String.valueOf(0));
//                actionActuatorFuncParamThree.addElement("wpml:payloadPositionIndex").addText("0");
//
//                Element actionFour = actionGroup.addElement("wpml:action");//动作列表
//                actionFour.addElement("wpml:actionId").addText("3");//动作id
//                actionFour.addElement("wpml:actionActuatorFunc").addText("focus");//动作类型
//                Element actionActuatorFuncParamFour = actionFour.addElement("wpml:actionActuatorFuncParam");//动作参数
//                actionActuatorFuncParamFour.addElement("wpml:isPointFocus").addText("1");
//                actionActuatorFuncParamFour.addElement("wpml:focusX").addText("0.5");
//                actionActuatorFuncParamFour.addElement("wpml:focusY").addText("0.5");
//                actionActuatorFuncParamFour.addElement("wpml:isInfiniteFocus").addText("0");
//                actionActuatorFuncParamFour.addElement("wpml:payloadPositionIndex").addText("0");

                if (startTime != null && startTime != 0) {
                    Element actionFive = actionGroup.addElement("wpml:action");//动作列表
                    actionFive.addElement("wpml:actionId").addText("4");//动作id
                    actionFive.addElement("wpml:actionActuatorFunc").addText("hover");//动作类型
                    Element actionActuatorFuncParamFive = actionFive.addElement("wpml:actionActuatorFuncParam");//动作参数
                    actionActuatorFuncParamFive.addElement("wpml:hoverTime").addText(startTime.toString());
                }


                Element actionSix = actionGroup.addElement("wpml:action");//动作列表
                actionSix.addElement("wpml:actionId").addText("5");//动作id
                actionSix.addElement("wpml:actionActuatorFunc").addText("takePhoto");//动作类型
                Element actionActuatorFuncParamSix = actionSix.addElement("wpml:actionActuatorFuncParam");//动作参数
//                actionActuatorFuncParamSix.addElement("wpml:fileSuffix").addText("ponit" + String.valueOf(i + 1));
//                actionActuatorFuncParamSix.addElement("wpml:payloadLensIndex").addText("ir,zoom");
                actionActuatorFuncParamSix.addElement("wpml:payloadPositionIndex").addText("0");

                if (endTime != null && endTime != 0) {
                    Element actionSeven = actionGroup.addElement("wpml:action");//动作列表
                    actionSeven.addElement("wpml:actionId").addText("6");//动作id
                    actionSeven.addElement("wpml:actionActuatorFunc").addText("hover");//动作类型
                    Element actionActuatorFuncParamSeven = actionSeven.addElement("wpml:actionActuatorFuncParam");//动作参数
                    actionActuatorFuncParamSeven.addElement("wpml:hoverTime").addText(endTime.toString());
                }

            }

            String basePath = getpIngBasePath(topbasePath);
            //创建kml到本地
            String filePath = basePath + fileName + File.separator + "wpmz" + File.separator + "template.kml";
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setIndent(true);
            format.setEncoding("UTF-8");
            format.setTrimText(false);
            // format.setNewLineAfterDeclaration(false);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            XMLWriter xmlWriter = new XMLWriter(fileOutputStream, format);
            xmlWriter.write(document);
            xmlWriter.close();
            return filePath;
        } catch (Exception e) {
            return null;
        }
    }

    private static String writewpml(List<double[]> coordinateArray, EfTaskWps efTaskWps, String fileName, double takeoffAlt, double homeAltAbs,
                                    int altType, int uavType, String topbasePath,Integer startTime,Integer endTime) {
        try {
            Element root = DocumentHelper.createElement("kml");
            Namespace namespace = Namespace.get("http://www.opengis.net/kml/2.2");
            root.add(namespace);
            Document document = DocumentHelper.createDocument(root);
            //根节点添加属性
            root.addAttribute("xmlns", "http://www.opengis.net/kml/2.2")
                    .addNamespace("wpml", "http://www.dji.com/wpmz/1.0.0");
            Element documentElement = root.addElement("Document", "http://www.opengis.net/kml/2.2");
            //文件创建信息
            Element missionConfig = documentElement.addElement("wpml:missionConfig");
            missionConfig.addElement("wpml:flyToWaylineMode").addText(efTaskWps.getWpsGotoWaypointMode() == 0 ? "safely" : "pointToPoint");
            missionConfig.addElement("wpml:finishAction").addText("goHome");
            missionConfig.addElement("wpml:exitOnRCLost").addText("executeLostAction");
            missionConfig.addElement("wpml:executeRCLostAction").addText("goBack");
            missionConfig.addElement("wpml:takeOffSecurityHeight").addText(String.valueOf(takeoffAlt));
            missionConfig.addElement("wpml:globalTransitionalSpeed").addText("10");

            Element droneInfo = missionConfig.addElement("wpml:droneInfo");
            //声明无人机型号
            droneInfo.addElement("wpml:droneEnumValue").addText("77");
            droneInfo.addElement("wpml:droneSubEnumValue").addText("2");  // 2  -- m3m  68   --- 1   67

            Element payloadInfo = missionConfig.addElement("wpml:payloadInfo");
            //声明有效载荷模型
            payloadInfo.addElement("wpml:payloadEnumValue").addText("68"); // 68
            payloadInfo.addElement("wpml:payloadSubEnumValue").addText("0");
            payloadInfo.addElement("wpml:payloadPositionIndex").addText("0");


            Element folder = documentElement.addElement("Folder");
            folder.addElement("wpml:templateId").addText("0");
            folder.addElement("wpml:executeHeightMode").addText(altType == 0 ? "relativeToStartPoint" : "WGS84");
            folder.addElement("wpml:waylineId").addText("0");
            folder.addElement("wpml:distance").addText(String.valueOf(efTaskWps.getWpsDistance()));
            folder.addElement("wpml:duration").addText(String.valueOf(efTaskWps.getWpsUserTime()));
            folder.addElement("wpml:autoFlightSpeed").addText(String.valueOf(efTaskWps.getWpsSpeed()));

//
            double direction = 0;
            for (int i = 0; i < coordinateArray.size(); i++) {
                double[] firstPoint = coordinateArray.get(i);  // 获取第一组坐标点
                double lng = firstPoint[0];  // 获取经度
                double lat = firstPoint[1];  // 获取纬度
                for (int j = i + 1; j < coordinateArray.size(); j++) {
                    double[] nextPoint = coordinateArray.get(j);  // 获取第一组坐标点
                    double longitude1 = nextPoint[0];  // 获取经度
                    double latitude2 = nextPoint[1];  // 获取纬度
                    direction = getDirection(lat, lng, latitude2, longitude1);
                    break;
                }

//                coordinateArray efPvBoardGroup = boardGroupList.get(i);
                Element placemark = folder.addElement("Placemark");
                Element pointElement = placemark.addElement("Point");
//                pointElement.addElement("coordinates").addText("\r\n" + lng + "," + lat + "\r\n");
                pointElement.addElement("coordinates").addText(lng + "," + lat);
                placemark.addElement("wpml:index").addText(String.valueOf(i));
                placemark.addElement("wpml:executeHeight").addText(altType == 0 ? String.valueOf(takeoffAlt) : String.valueOf(homeAltAbs));//航点执行高度
                placemark.addElement("wpml:waypointSpeed").addText(String.valueOf(efTaskWps.getWpsSpeed()));//航点飞行速度
                Element waypointHeadingParam = placemark.addElement("wpml:waypointHeadingParam");//偏航角参数模式
                waypointHeadingParam.addElement("wpml:waypointHeadingMode").addText("smoothTransition");
                waypointHeadingParam.addElement("wpml:waypointHeadingAngle").addText(String.valueOf(0)); //朝向？ direction
                waypointHeadingParam.addElement("wpml:waypointPoiPoint").addText("0.000000,0.000000,0.000000");
                waypointHeadingParam.addElement("wpml:waypointHeadingPathMode").addText("followBadArc");
                waypointHeadingParam.addElement("wpml:waypointHeadingAngleEnable").addText("0");

                Element waypointTurnParam = placemark.addElement("wpml:waypointTurnParam");//航点转弯模式
                waypointTurnParam.addElement("wpml:waypointTurnMode").addText("toPointAndStopWithContinuityCurvature"); // 点不停 toPointAndPassWithContinuityCurvature
                waypointTurnParam.addElement("wpml:waypointTurnDampingDist").addText("0");
                placemark.addElement("wpml:useStraightLine").addText("1");

                //动作组
                Element actionGroup = placemark.addElement("wpml:actionGroup");
                actionGroup.addElement("wpml:actionGroupId").addText(String.valueOf(i));
                actionGroup.addElement("wpml:actionGroupStartIndex").addText(String.valueOf(i));//动作组开始生效的航点
                actionGroup.addElement("wpml:actionGroupEndIndex").addText(String.valueOf(i));//动作组结束生效的航点
                actionGroup.addElement("wpml:actionGroupMode").addText("sequence");//动作执行模式
                Element actionTrigger = actionGroup.addElement("wpml:actionTrigger");//动作组触发器
                actionTrigger.addElement("wpml:actionTriggerType").addText("reachPoint");//动作触发器类型
                //动作列表
//                Element actionZero = actionGroup.addElement("wpml:action");
//                actionZero.addElement("wpml:actionId").addText("0");//动作id
//                actionZero.addElement("wpml:actionActuatorFunc").addText("rotateYaw");//动作类型
//                Element actionActuatorFuncParamZero = actionZero.addElement("wpml:actionActuatorFuncParam");
//                actionActuatorFuncParamZero.addElement("wpml:aircraftHeading").addText(String.valueOf(0));  //拍摄时的朝向，无人机或相机朝向?
//                actionActuatorFuncParamZero.addElement("wpml:gimbalRotateMode").addText("counterClockwise");
//
                Element actionTwo = actionGroup.addElement("wpml:action");//动作列表 无人机
                actionTwo.addElement("wpml:actionId").addText("1");//动作id
                actionTwo.addElement("wpml:actionActuatorFunc").addText("gimbalRotate");//动作类型
                Element actionActuatorFuncParamTwo = actionTwo.addElement("wpml:actionActuatorFuncParam");//动作参数
                actionActuatorFuncParamTwo.addElement("wpml:gimbalRotateMode").addText("absoluteAngle");//云台转动模式
                actionActuatorFuncParamTwo.addElement("wpml:gimbalPitchRotateEnable").addText("1");//是否使能云台俯仰转动
                actionActuatorFuncParamTwo.addElement("wpml:gimbalPitchRotateAngle").addText(String.valueOf(-90));//云台俯仰转动角度
//
//                Element actionThree = actionGroup.addElement("wpml:action");//动作列表
//                actionThree.addElement("wpml:actionId").addText("2");//动作id
//                actionThree.addElement("wpml:actionActuatorFunc").addText("zoom");//动作类型
//                Element actionActuatorFuncParamThree = actionThree.addElement("wpml:actionActuatorFuncParam");//动作参数
//                actionActuatorFuncParamThree.addElement("wpml:focalLength").addText(String.valueOf(0)); // 自动变焦？
//                actionActuatorFuncParamThree.addElement("wpml:payloadPositionIndex").addText("0");
//
//                Element actionFour = actionGroup.addElement("wpml:action");//动作列表
//                actionFour.addElement("wpml:actionId").addText("3");//动作id
//                actionFour.addElement("wpml:actionActuatorFunc").addText("focus");//动作类型
//                Element actionActuatorFuncParamFour = actionFour.addElement("wpml:actionActuatorFuncParam");//动作参数
//                actionActuatorFuncParamFour.addElement("wpml:isPointFocus").addText("1");
//                actionActuatorFuncParamFour.addElement("wpml:focusX").addText("0.5");
//                actionActuatorFuncParamFour.addElement("wpml:focusY").addText("0.5");
//                actionActuatorFuncParamFour.addElement("wpml:isInfiniteFocus").addText("0");
//                actionActuatorFuncParamFour.addElement("wpml:payloadPositionIndex").addText("0");
              // startTime 不等于o

                if (startTime != null && startTime != 0) {
                    Element actionFive = actionGroup.addElement("wpml:action");//动作列表
                    actionFive.addElement("wpml:actionId").addText("4");//动作id
                    actionFive.addElement("wpml:actionActuatorFunc").addText("hover");//动作类型
                    Element actionActuatorFuncParamFive = actionFive.addElement("wpml:actionActuatorFuncParam");//动作参数
                    actionActuatorFuncParamFive.addElement("wpml:hoverTime").addText(startTime.toString());
                }



//                Element actionSix = actionGroup.addElement("wpml:action");//动作列表
//                actionSix.addElement("wpml:actionId").addText("5");//动作id
//                actionSix.addElement("wpml:actionActuatorFunc").addText("takePhoto");//动作类型
//                Element actionActuatorFuncParamSix = actionSix.addElement("wpml:actionActuatorFuncParam");//动作参数
//
//                actionActuatorFuncParamSix.addElement("wpml:fileSuffix").addText("ponit" + String.valueOf(i + 1));
////                actionActuatorFuncParamSix.addElement("wpml:payloadLensIndex").addText("ir,zoom");
//                actionActuatorFuncParamSix.addElement("wpml:payloadPositionIndex").addText("0");
//                actionActuatorFuncParamSix.addElement("wpml:gimbalRotateMode").addText("absoluteAngle");//云台转动模式
//                actionActuatorFuncParamSix.addElement("wpml:gimbalPitchRotateEnable").addText("1");//是否使能云台俯仰转动
//                actionActuatorFuncParamSix.addElement("wpml:gimbalPitchRotateAngle").addText(String.valueOf(-40));//云台俯仰转动角度

//                actionActuatorFuncParamSix.addElement("wpml:gimbalYawRotateEnable").addText("1");//是否使能云台俯仰转动
//                actionActuatorFuncParamSix.addElement("wpml:gimbalYawRotateAngle").addText(String.valueOf(30));//云台转动角度
//                actionActuatorFuncParamSix.addElement("wpml:gimbalRollRotateEnable").addText("1");//是否使能云台俯仰转动
//                actionActuatorFuncParamSix.addElement("wpml:gimbalRollRotateAngle").addText(String.valueOf(30));//云台转动角度
//                actionActuatorFuncParamSix.addElement("wpml:gimbalRotateTimeEnable").addText("0");//是否使能云台转动时间
//                actionActuatorFuncParamSix.addElement("wpml:gimbalRotateTime").addText("0");//云台转动用时
//                gimbalRollRotateEnable
                Element actionSix = actionGroup.addElement("wpml:action");//动作列表
                actionSix.addElement("wpml:actionId").addText("5");//动作id
                actionSix.addElement("wpml:actionActuatorFunc").addText("takePhoto");//动作类型
                Element actionActuatorFuncParamSix = actionSix.addElement("wpml:actionActuatorFuncParam");//动作参数
//                actionActuatorFuncParamSix.addElement("wpml:fileSuffix").addText("ponit" + String.valueOf(i + 1));
//                actionActuatorFuncParamSix.addElement("wpml:payloadLensIndex").addText("ir,zoom");
                actionActuatorFuncParamSix.addElement("wpml:payloadPositionIndex").addText("0");

                //   endTime 不等于o
                if (endTime != null && endTime != 0) {
                    Element actionSeven = actionGroup.addElement("wpml:action");//动作列表
                    actionSeven.addElement("wpml:actionId").addText("6");//动作id
                    actionSeven.addElement("wpml:actionActuatorFunc").addText("hover");//动作类型
                    Element actionActuatorFuncParamSeven = actionSeven.addElement("wpml:actionActuatorFuncParam");//动作参数
                    actionActuatorFuncParamSeven.addElement("wpml:hoverTime").addText(endTime.toString());
                }



            }
//
            String basePath = getpIngBasePath(topbasePath);
            //创建kml到本地
            String filePath = basePath + fileName + File.separator + "wpmz" + File.separator + "waylines.wpml";
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setIndent(true);
            format.setEncoding("UTF-8");
            format.setTrimText(false);
            // format.setNewLineAfterDeclaration(false);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            XMLWriter xmlWriter = new XMLWriter(fileOutputStream, format);
            xmlWriter.write(document);
            xmlWriter.close();
            return filePath;
        } catch (Exception e) {
            LogUtil.logError("生成waylines.xml異常：" + e.toString());
            return null;
        }
    }

    /**
     * 拷贝
     */
    private static File copyKmzStream(String kmlPathString, String wpmlPathString, String kmzPath, String topbasePath) {
        boolean flag = false;
        File kmzFile = null;  // 返回的文件实例
        try {
            //没有模板
            String templeKmzPath = getpIngBasePath(topbasePath) + "template.kmz";
            File file = new File(templeKmzPath);
            if (!file.exists()) {
                LogUtil.logWarn("Kmz模板文件不存在！");
                return kmzFile;
            }
            Path sourcePath = Paths.get(file.getAbsolutePath());
            Path targetPath = Paths.get(kmzPath);
//            byteCopy(sourcePath.toString(),targetPath.toString());
            Path copiedPath = Files.copy(sourcePath, targetPath, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
            // 如果代码执行到此处，表示复制成功
//            System.out.println("文件复制成功：" + copiedPath.toString());
            // streamCopyFile(file, new File(kmzPath));  // 复制模板文件
            Path kmlPath = Paths.get(kmlPathString);
            Path wpmlPath = Paths.get(wpmlPathString);
            Path zipFilePath = Paths.get(kmzPath);
            try (FileSystem fs = FileSystems.newFileSystem(zipFilePath, ClassLoader.getSystemClassLoader())) {
                Path fileInsideZipPathKml = fs.getPath("/wpmz/template.kml");
                Path fileInsideZipPathWpml = fs.getPath("/wpmz/waylines.wpml");
                Files.copy(kmlPath, fileInsideZipPathKml, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(wpmlPath, fileInsideZipPathWpml, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
                flag = true;
                kmzFile = zipFilePath.toFile();

            } catch (IOException e) {
                LogUtil.logError("写入Kmz航线时IO异常：" + e.toString());
            }
        } catch (Exception e) {
            LogUtil.logError("写入Kmz航线时异常：" + e.toString());
        }

        return kmzFile;
    }


    public static void byteCopy(String sourcePath, String target) throws IOException {
        //1.创建输入流
        InputStream iStream = new FileInputStream(sourcePath);
        //2.创建输出流
        OutputStream oStream = new FileOutputStream(target);
        //3.一部分一部分读出
        byte[] bytes = new byte[10 * 1024];
        int br;//实际的读取长度
        while ((br = iStream.read(bytes)) != -1) {//判断是否读到末尾
            oStream.write(bytes, 0, br);
        }
        //4.清空缓存
        oStream.flush();
        //5.关闭流
        if (iStream != null) {
            iStream.close();
        }
        if (oStream != null) {
            oStream.close();
        }
    }


    /**
     * 写入文件压缩为ZIP格式
     *
     * @param filePaths 被压缩文件路径
     * @param zipPath   生成zip文件路径
     */
    public static File writeZip(List<String> filePaths, String zipPath) {
        File kmzFile = null;  // 返回的文件实例
        try {
            FileUtil.createOrUpdateFile(zipPath);
            File zipFile = new File(zipPath);
//            // 确保路径中的所有目录都存在
//            File parentDir = zipFile.getParentFile();
//            if (!parentDir.exists()) {
//                parentDir.mkdirs();
//            }
//            // 判断文件是否存在，如文件不存在创建一个新文件
//            if (!zipFile.exists()) {
//                zipFile.createNewFile();
//            }
            // 创建一个zip文件输出流
            ZipOutputStream zipOutput = new ZipOutputStream(new FileOutputStream(zipFile));
            for (String filePath : filePaths) {
                File file = new File(filePath);
                // 判断文件是否存在，如不存在直接跳过
                if (!file.exists()) {
                    continue;
                }
                /**
                 * 创建一个缓冲读取流，提高读取效率
                 * 也可以直接创建一个 FileInputStream 对象，BufferedInputStream内部维护了一个8KB的缓冲区，BufferedInputStream本身不具备读取能力
                 * BufferedInputStream 可以手动指定缓冲区大小 单位为字节例如：new BufferedInputStream(new FileInputStream(file), 10240)
                 */
                BufferedInputStream bufferedInput = new BufferedInputStream(new FileInputStream(file));
                // 设置压缩条目名称
                zipOutput.putNextEntry(new ZipEntry(file.getName()));
                byte[] bytes = new byte[1024];
                int len = -1;
                // 读取file内的字节流，写入到zipOutput内
                while ((len = bufferedInput.read(bytes)) != -1) {
                    zipOutput.write(bytes, 0, len);
                }
                // 关闭输入流
                // 无需关闭new FileInputStream(file)的输入流 因为BufferedInputStream.close()方法内部已经调用了FileInputStream.close()方法
                bufferedInput.close();
                // 写入完毕后关闭条目
                zipOutput.closeEntry();
            }
            zipOutput.close();
            kmzFile = new File(zipPath);
        } catch (IOException e) {
            System.out.println("写入文件压缩为ZIP格式失败：" + e.getMessage());
        } finally {
            return kmzFile;
        }

    }

    public static void dozip(List<String> pathList, String zipPath) {
        try {
            File zipfile = new File(zipPath);
            if (!zipfile.exists()) {
                zipfile.createNewFile();
            }
            for (String filePath : pathList) {
                File file = new File(filePath);
                // 判断文件是否存在，如不存在直接跳过
                if (!file.exists()) {
                    continue;
                }
                InputStream inputStream = new FileInputStream(filePath);


            }

        } catch (Exception e) {

        }

    }


    /**
     * 获取kml的经纬度
     *
     * @param byteArrayOutputStream
     * @return
     */
    public static Map readKml(ByteArrayOutputStream byteArrayOutputStream) {
        Map map = new HashMap();
        map.put("msg", false);
        String heightStr = "";
        try {
            // 转换为ByteArrayInputStream
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(byteArrayInputStream);
            //根节点 即XML文件的最顶层节点 <kml>
            Element rootElement = document.getRootElement();
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
                    heightStr = takeOffSecurityHeightElement.getText();
                    System.out.println("TakeOffSecurityHeight: " + takeOffSecurityHeightElement.getText());
                } else {
                    System.out.println("TakeOffSecurityHeight element not found.");
                }
            } else {
                System.out.println("MissionConfig element not found.");
            }
            // 创建根字节-->子字节 <Folder></Folder>
            Element folderElement = rootElement.element("Document").element("Folder");
//            Element folderElement = (Element) rootElement.selectSingleNode("Document/Folder");
            if (folderElement == null) {
                map.put("msg", "解析节点失败");
                return map;
            }
            // 获取<Placemark>节点列表
            List<Element> placemarkList = folderElement.elements("Placemark");
            List<Map> PointList = new ArrayList<>();
            List<String[]> coordinateslist = new ArrayList<>();
            for (Element placemark : placemarkList) {
                Map pointMap = new HashMap();
                // 解析<coordinates>节点
                Element coordinates = placemark.element("Point").element("coordinates");
                String coordinatesValue = coordinates.getTextTrim();
                String[] values = coordinatesValue.split(",");
                String longitude = values[0]; // 经度
                String latitude = values[1]; // 纬度
                // 添加数据
                coordinateslist.add(values);

                pointMap.put("Longitude", longitude);
                pointMap.put("Latitude", latitude);
                // 其他字段类似，根据实际需要解析其他节点
                PointList.add(pointMap); //
            }
            String mid = System.currentTimeMillis() + "-" + new Random().nextInt(9999);
            map.put("msg", true);
            map.put("mid", mid);
            map.put("unifiedHeight", heightStr);
            map.put("PointList", PointList);
            map.put("coordinateslist", coordinateslist);
            return map;

        } catch (Exception e) {

        } finally {
            return map;
        }
    }
    //endregion

    //region 工具

    /**
     * 计算方向角度
     *
     * @param lat1 起点纬度
     * @param lng1 起点经度
     * @param lat2 终点纬度
     * @param lng2 终点经度
     * @return 方向角度，取值范围：[0, 360)
     */
    public static double getDirection(double lat1, double lng1, double lat2, double lng2) {
        double dLng = lng2 - lng1;
        double y = Math.sin(Math.toRadians(dLng)) * Math.cos(Math.toRadians(lat2));
        double x = Math.cos(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2))
                - Math.sin(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(dLng));
        double direction = Math.toDegrees(Math.atan2(y, x));
        if (direction < 0) {
            direction += 360;
        }
        return direction;
    }

    //endregion

    private static String getActionActuatorFunc(String str) {
        switch (str) {
            case "0":
                return "hover";
            case "1":
                return "takePhoto";
            case "2":
                return "startRecord";
            case "3":
                return "stopRecord";
            case "4":
                return "rotateYaw";
            case "5":
                return "gimbalRotate";
            default:
                return "hover";
        }
    }


}
