//package com.efuav.pvinspection.utils;
//
//import com.deepoove.poi.XWPFTemplate;
//import com.deepoove.poi.config.Configure;
//import com.deepoove.poi.data.PictureRenderData;
//import com.efuav.pvinspection.ParkingapronApplication;
//import com.efuav.pvinspection.PvApplication;
//import com.efuav.pvinspection.entity.InsideFirePointModel;
//import com.efuav.pvinspection.entity.InsideFirePositionModel;
//import com.efuav.pvinspection.entity.InsideModel;
//import com.efuav.pvinspection.entity.InsideUnitModel;
//import org.springframework.web.bind.annotation.RequestBody;
//import com.deepoove.poi.policy.HackLoopTableRenderPolicy;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.*;
//
///**
// * @author N.
// * @date 2023-06-06 16:09
// */
//public class PhotovoltaicUtil {
//    public static void insideDaily(HttpServletRequest request, HttpServletResponse response, @RequestBody InsideModel insideModel){
//        try {
//            //list 火点表格
//            List<InsideFirePointModel> insideFirePointModels = insideModel.getInsideFirePointModels();
//            //list 火点位置列表
//            List<InsideFirePositionModel> insideFirePositionModels = insideModel.getInsideFirePositionModels();
//            //list 巡回单元区域
//            List<InsideUnitModel> insideUnitModels = insideModel.getInsideUnitModels();
//            //System.out.println(insideFirePointModels.get(1));
//            //组装表格列表数据
//
//            List<Map<String, Object>> fire = new ArrayList<Map<String, Object>>();
//            for (int i = 0; i < insideFirePointModels.size(); i++) {
//                Map<String, Object> detailMap = new HashMap<String, Object>();
//                detailMap.put("id", insideFirePointModels.get(i).getId());
//                detailMap.put("city", insideFirePointModels.get(i).getCity());
//                detailMap.put("town", insideFirePointModels.get(i).getTown());
//                detailMap.put("township", insideFirePointModels.get(i).getTownship());
//                detailMap.put("longitude", insideFirePointModels.get(i).getLongitude());
//                detailMap.put("latitude", insideFirePointModels.get(i).getLatitude());
//                detailMap.put("num", insideFirePointModels.get(i).getNum());
//                fire.add(detailMap);
//            }
//
//            //列表块  火点位置(图片，详情位置）
//            List<Map<String, Object>> fireposition = new ArrayList<Map<String, Object>>();
//            for (int i = 0; i < insideFirePositionModels.size(); i++) {
//                //new 循环添加map.add
//                Map<String, Object> detailfireposition = new HashMap<String, Object>();
//                //private String firepicture; 火点位置图片
//                detailfireposition.put("picture", new PictureRenderData(600, 300, insideFirePositionModels.get(i).getFirepicture()));
//                //private int firenum; 火点编号：J0331A01编号：J0331A01
//                detailfireposition.put("firenum", insideFirePositionModels.get(i).getFirenum());
//                //private  String firedate; 火点时间:2023年3月31日
//                detailfireposition.put("firedate", insideFirePositionModels.get(i).getFiredate());
//                //火点纬度: 30.455943 经度: 109.729183 Longitude and latitude
//                detailfireposition.put("firelongitude", insideFirePositionModels.get(i).getFirelongitude());
//                detailfireposition.put("firelatitude", insideFirePositionModels.get(i).getFirelatitude());
//                //private String fireposition; 火点位置: 恩施市崔家坝镇南里渡村
//                detailfireposition.put("fireposition", insideFirePositionModels.get(i).getFireposition());
//                fireposition.add(detailfireposition);
//            }
//            //列表块 巡回单元区域<图片，区域>
//            List<Map<String, Object>> fireeare = new ArrayList<Map<String, Object>>();
//            for (int j = 0; j < insideUnitModels.size(); j++) {
//                //new 循环添加map.add
//                Map<String, Object> detailfireeare = new HashMap<String, Object>();
//                //巡回区域图片
//                detailfireeare.put("pictures", new PictureRenderData(600, 300, insideUnitModels.get(j).getFlypicture()));
//                //飞行单元号：{{flynum}}
//                detailfireeare.put("flynum", insideUnitModels.get(j).getFlynum());
//                //飞行区域:{{flyeare}}
//                detailfireeare.put("flyeare", insideUnitModels.get(j).getFlyeare());
//                //中心经纬度: {{flylongitude}} ， {{flylatitude}}
//                detailfireeare.put("flylongitude", insideUnitModels.get(j).getFlylongitude());
//                detailfireeare.put("flylatitude", insideUnitModels.get(j).getFlylatitude());
//                fireeare.add(detailfireeare);
//
//            }
//
//            //封装 传入 模板参数 params
//            //组装散装文本数据
//            Map<String, Object> params = new HashMap<>();
//            //散装文本数据
//            params.put("versionnum", insideModel.getVersionnum());
//            params.put("year", insideModel.getYear());
//            params.put("date", insideModel.getDate());
//            params.put("flyUint", insideModel.getFlyUintCount());
//            params.put("townCount", insideModel.getTownCount());
//            params.put("fireCount", insideModel.getFireCount());
//            params.put("areaCount", insideModel.getAreaCount());
//            params.put("blackpointCount", insideModel.getBlackpointCount());
//            //表单映射ma
//            params.put("fire", fire);
//            //火点列表映射map
//            params.put("post", fireposition);
//            //循回区域单元映射到map
//            params.put("posts", fireeare);
//
//            InputStream bis = PvApplication.class.getClassLoader().getResourceAsStream("certs/PhotovoltaicInspectionTemplate.docx");
//            //渲染表格
//            HackLoopTableRenderPolicy policy = new HackLoopTableRenderPolicy();
//
//            Configure config = Configure.newBuilder().bind("fire", policy).build();
//            XWPFTemplate template = XWPFTemplate.compile(bis, config).render(
//                    params
//            );
//            //=================生成文件保存在本地D盘某目录下=================
//            String temDir = "D:/mimi/" + File.separator + "file/word/";
//            //生成临时文件存放地址
//            //生成文件名
//            Long time = new Date().getTime();
//            // 生成的word格式
//            String formatSuffix = ".docx";
//            // 拼接后的文件名
//            String fileName = time + formatSuffix;//文件名  带后缀
//
//            FileOutputStream fos = new FileOutputStream(temDir + fileName);
//            template.write(fos);
//            //=================生成word到设置浏览默认下载地址=================
//            // 设置强制下载不打开
//            response.setContentType("application/force-download");
//            // 设置文件名
//            response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
//            OutputStream out = response.getOutputStream();
//            template.write(out);
//            out.flush();
//            out.close();
//            template.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
