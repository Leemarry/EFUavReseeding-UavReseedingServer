package com.bear.reseeding.utils;

import com.bear.reseeding.MyApplication;
import com.bear.reseeding.entity.EfTaskKmz;
import com.bear.reseeding.entity.EfTaskWps;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * kmz
 *
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
     *  传入数据处理
     * @param coordinateArray
     * @param takeoffAlt
     * @param homeAltAbs
     * @param altType
     * @param uavType
     * @return
     */
    public static  File beforeDataProcessing(List<double[]> coordinateArray,String fileName,double takeoffAlt, double homeAltAbs, int altType, int uavType,String topbasePath){
        boolean flag = false;
        File kmzFile = null; // 返回的文件实例
        try {
            // 距离 点集合
            int numPoints = coordinateArray.size();
            double distaceCount=0.0;
            double distance;
            for (int i = 0; i < numPoints; i++) {
                for (int j = i + 1; j < numPoints; j++) {
                    double[] firstPoint = coordinateArray.get(i);  // 获取第一组坐标点
                    double lng1 = firstPoint[0];  // 获取经度
                    double lat1 = firstPoint[1];  // 获取纬度
                    double[] nextPoint = coordinateArray.get(j);  // 获取第一组坐标点
                    double lng2 = nextPoint[0];  // 获取经度
                    double lat2 = nextPoint[1];  // 获取纬度

                    distance = GisUtil.getDistance(lng1,lat1 , lng2, lat2);
                    distaceCount += distance;
//                    System.out.printf("Distance between P%d and P%d: %.2f m\n", i + 1, j + 1, distance);
                    break;
                }
            }

            // 构建光伏巡检默认参数
            EfTaskWps efTaskWps = new EfTaskWps();
            efTaskWps.setWpsCreateTime(new Date());
            efTaskWps.setWpsUpdateTime(new Date());
            efTaskWps.setWpsGotoWaypointMode(0);
            efTaskWps.setWpsFinishedAction(1);
            efTaskWps.setWpsRcSignalLost(0);
            efTaskWps.setWpsHeadingMode(1);//TODO 机头朝向
            efTaskWps.setWpsGimbalPitchRotationEnabled(true);
            efTaskWps.setWpsAlt(200);   //海拔
            efTaskWps.setWpsSpeed(5f);
            efTaskWps.setWpsDistance(distaceCount);
            Integer WpsUserTime = (int) (distaceCount / 5f);
            efTaskWps.setWpsUserTime(WpsUserTime); //任务预计用时
//            efTaskWps.setWpsUserTime(0);
            //kmlPath

            String wpmlPathString = writewpml( coordinateArray, efTaskWps, fileName, takeoffAlt, homeAltAbs, altType, uavType,topbasePath);
            String kmlPathString = writeKml(coordinateArray, efTaskWps, fileName, takeoffAlt, homeAltAbs, altType, uavType,topbasePath);


            File file1 = null;
            File file2 = null;
            String path = "";  //父目録，wpml
            if (wpmlPathString != null) {
                file1 = new File(wpmlPathString);
                if (file1.exists()) {
                    String p=file1.getParent();
                    path = file1.getParent();
                }
            }
            if (kmlPathString != null) {
                file2 = new File(kmlPathString);
                if (file2.exists()) {
                    String p=file2.getParent();
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

                String kmzPath = getpIngBasePath(topbasePath) + fileName + File.separator + fileName + ".kmz";
                kmzFile = copyKmzStream(kmlPathString,wpmlPathString,kmzPath ,topbasePath);
            } else {
                LogUtil.logWarn("Wpml目録不存在：" + path + fileName);
            }

        }catch (Exception e){
            LogUtil.logError("数据集处理有误：" + e.toString());
        }
        return kmzFile;
    }

    public static String writeKml(List<double[]> coordinateArray, EfTaskWps efTaskWps, String fileName, double takeoffAlt, double homeAltAbs, int altType, int uavType,String topbasePath) {
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
            droneInfo.addElement("wpml:droneEnumValue").addText("67");  // 77
            droneInfo.addElement("wpml:droneSubEnumValue").addText("0");  // 1

            //声明有效载荷模型
            Element payloadInfo = missionConfig.addElement("wpml:payloadInfo");
            payloadInfo.addElement("wpml:payloadEnumValue").addText("67");
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

            folder.addElement("wpml:globalWaypointTurnMode").addText("toPointAndStopWithDiscontinuityCurvature");
            double direction =0;
            for (int i = 0; i < coordinateArray.size(); i++) {
                double[] firstPoint = coordinateArray.get(i);  // 获取第一组坐标点
                double lng = firstPoint[0];  // 获取经度
                double lat = firstPoint[1];  // 获取纬度
//                Map<double[]> entry = coordinateArray.get(i);
//                double lng = entry.get("x");
//                double lat = entry.get("y");
//                double alt = entry.get("z");
                for (int j= i+1;j<coordinateArray.size();j++){

                    double[] nextPoint = coordinateArray.get(j);  // 获取第一组坐标点
                    double longitude1= nextPoint[0];  // 获取经度
                    double latitude2 = nextPoint[1];  // 获取纬度
//                    Map<String,Double>  nextentry = coordinateArray.get(i+1);
//                    double lng1 = nextentry.get("x");
//                    double lat1 = nextentry.get("y");
//                    double alt1 = nextentry.get("z");
//                    direction =getDirection(lat,lng,lat1,lng1);
                    direction =getDirection(lat,lng,latitude2,longitude1);
                    break;
                }

                Element placemark = folder.addElement("Placemark");
                Element point = placemark.addElement("Point");
//                point.addElement("coordinates").addText("\r\n" + lng + "," + lat + "\r\n");
                point.addElement("coordinates").addText( lng + "," + lat );
                placemark.addElement("wpml:index").addText(String.valueOf(i));
                placemark.addElement("wpml:ellipsoidHeight").addText(altType == 0 ? String.valueOf(takeoffAlt) : String.valueOf(homeAltAbs) ); //String.valueOf(alt - homeAltAbs) : String.valueOf(alt)
                placemark.addElement("wpml:height").addText(altType == 0 ? String.valueOf(takeoffAlt) : String.valueOf(homeAltAbs) );
                Element waypointHeadingParam = placemark.addElement("wpml:waypointHeadingParam");
                waypointHeadingParam.addElement("wpml:waypointHeadingMode").addText("smoothTransition");
                waypointHeadingParam.addElement("wpml:waypointHeadingAngle").addText(String.valueOf(0));  //朝向？ direction
                waypointHeadingParam.addElement("wpml:waypointPoiPoint").addText("0.000000,0.000000,0.000000");
                waypointHeadingParam.addElement("wpml:waypointHeadingPathMode").addText("followBadArc");
                waypointHeadingParam.addElement("wpml:waypointHeadingPoiIndex").addText("0");
                placemark.addElement("wpml:useGlobalHeight").addText("0");
                placemark.addElement("wpml:useGlobalSpeed").addText("1");
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

//                Element actionFive = actionGroup.addElement("wpml:action");//动作列表
//                actionFive.addElement("wpml:actionId").addText("4");//动作id
//                actionFive.addElement("wpml:actionActuatorFunc").addText("hover");//动作类型
//                Element actionActuatorFuncParamFive = actionFive.addElement("wpml:actionActuatorFuncParam");//动作参数
//                actionActuatorFuncParamFive.addElement("wpml:hoverTime").addText("2");

                Element actionSix = actionGroup.addElement("wpml:action");//动作列表
                actionSix.addElement("wpml:actionId").addText("5");//动作id
                actionSix.addElement("wpml:actionActuatorFunc").addText("takePhoto");//动作类型
                Element actionActuatorFuncParamSix = actionSix.addElement("wpml:actionActuatorFuncParam");//动作参数
                actionActuatorFuncParamSix.addElement("wpml:fileSuffix").addText("ponit" + String.valueOf(i + 1));
//                actionActuatorFuncParamSix.addElement("wpml:payloadLensIndex").addText("ir,zoom");
                actionActuatorFuncParamSix.addElement("wpml:payloadPositionIndex").addText("0");


//                Element actionSeven = actionGroup.addElement("wpml:action");//动作列表
//                actionSeven.addElement("wpml:actionId").addText("6");//动作id
//                actionSeven.addElement("wpml:actionActuatorFunc").addText("hover");//动作类型
//                Element actionActuatorFuncParamSeven = actionSeven.addElement("wpml:actionActuatorFuncParam");//动作参数
//                actionActuatorFuncParamSeven.addElement("wpml:hoverTime").addText("1");
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

    private static String writewpml(List<double[]> coordinateArray, EfTaskWps efTaskWps, String fileName, double takeoffAlt, double homeAltAbs, int altType, int uavType,String topbasePath) {
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
            droneInfo.addElement("wpml:droneSubEnumValue").addText("1");

            Element payloadInfo = missionConfig.addElement("wpml:payloadInfo");
            //声明有效载荷模型
            payloadInfo.addElement("wpml:payloadEnumValue").addText("67");
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
            double direction =0;
            for (int i = 0; i < coordinateArray.size(); i++) {
                double[] firstPoint = coordinateArray.get(i);  // 获取第一组坐标点
                double lng = firstPoint[0];  // 获取经度
                double lat = firstPoint[1];  // 获取纬度
//                Map<double[]> entry = coordinateArray.get(i);
//                double lng = entry.get("x");
//                double lat = entry.get("y");
//                double alt = entry.get("z");
                for (int j= i+1;j<coordinateArray.size();j++){
                    double[] nextPoint = coordinateArray.get(j);  // 获取第一组坐标点
                    double longitude1= nextPoint[0];  // 获取经度
                    double latitude2 = nextPoint[1];  // 获取纬度
//                    Map<String,Double>  nextentry = coordinateArray.get(i+1);
//                    double lng1 = nextentry.get("x");
//                    double lat1 = nextentry.get("y");
//                    double alt1 = nextentry.get("z");
//                    direction =getDirection(lat,lng,lat1,lng1);
                    direction =getDirection(lat,lng,latitude2,longitude1);
                    break;
                }

//                coordinateArray efPvBoardGroup = boardGroupList.get(i);
                Element placemark = folder.addElement("Placemark");
                Element pointElement = placemark.addElement("Point");
//                pointElement.addElement("coordinates").addText("\r\n" + lng + "," + lat + "\r\n");
                pointElement.addElement("coordinates").addText( lng + "," + lat );
                placemark.addElement("wpml:index").addText(String.valueOf(i));
                placemark.addElement("wpml:executeHeight").addText(altType == 0 ? String.valueOf(takeoffAlt) : String.valueOf(homeAltAbs) );//航点执行高度
                placemark.addElement("wpml:waypointSpeed").addText(String.valueOf(efTaskWps.getWpsSpeed()));//航点飞行速度
                Element waypointHeadingParam = placemark.addElement("wpml:waypointHeadingParam");//偏航角参数模式
                waypointHeadingParam.addElement("wpml:waypointHeadingMode").addText("smoothTransition");
                waypointHeadingParam.addElement("wpml:waypointHeadingAngle").addText(String.valueOf(0)); //朝向？ direction
                waypointHeadingParam.addElement("wpml:waypointPoiPoint").addText("0.000000,0.000000,0.000000");
                waypointHeadingParam.addElement("wpml:waypointHeadingPathMode").addText("followBadArc");
                waypointHeadingParam.addElement("wpml:waypointHeadingAngleEnable").addText("0");

                Element waypointTurnParam = placemark.addElement("wpml:waypointTurnParam");//航点转弯模式
                waypointTurnParam.addElement("wpml:waypointTurnMode").addText("toPointAndStopWithDiscontinuityCurvature");
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
//
//                Element actionFive = actionGroup.addElement("wpml:action");//动作列表
//                actionFive.addElement("wpml:actionId").addText("4");//动作id
//                actionFive.addElement("wpml:actionActuatorFunc").addText("hover");//动作类型
//                Element actionActuatorFuncParamFive = actionFive.addElement("wpml:actionActuatorFuncParam");//动作参数
//                actionActuatorFuncParamFive.addElement("wpml:hoverTime").addText("2");

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
                actionActuatorFuncParamSix.addElement("wpml:fileSuffix").addText("ponit" + String.valueOf(i + 1));
//                actionActuatorFuncParamSix.addElement("wpml:payloadLensIndex").addText("ir,zoom");
                actionActuatorFuncParamSix.addElement("wpml:payloadPositionIndex").addText("0");


//                Element actionSeven = actionGroup.addElement("wpml:action");//动作列表
//                actionSeven.addElement("wpml:actionId").addText("6");//动作id
//                actionSeven.addElement("wpml:actionActuatorFunc").addText("hover");//动作类型
//                Element actionActuatorFuncParamSeven = actionSeven.addElement("wpml:actionActuatorFuncParam");//动作参数
//                actionActuatorFuncParamSeven.addElement("wpml:hoverTime").addText("1");

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
    private static File copyKmzStream(String kmlPathString,String wpmlPathString, String kmzPath,String topbasePath) {
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
                flag =true;
                kmzFile =zipFilePath.toFile();
            } catch (IOException e) {
                LogUtil.logError("写入Kmz航线时IO异常：" + e.toString());
            }
        } catch (Exception e) {
            LogUtil.logError("写入Kmz航线时异常：" + e.toString());
        }
        return  kmzFile;
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
